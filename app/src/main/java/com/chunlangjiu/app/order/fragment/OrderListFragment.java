package com.chunlangjiu.app.order.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseFragment;
import com.chunlangjiu.app.goods.activity.ShopMainActivity;
import com.chunlangjiu.app.goods.bean.CreateOrderBean;
import com.chunlangjiu.app.goods.bean.PaymentBean;
import com.chunlangjiu.app.goods.dialog.BalancePayDialog;
import com.chunlangjiu.app.goods.dialog.InputPriceDialog;
import com.chunlangjiu.app.goods.dialog.PayDialog;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.order.activity.OrderDetailActivity;
import com.chunlangjiu.app.order.activity.OrderEvaluationDetailActivity;
import com.chunlangjiu.app.order.activity.OrderMainNewActivity;
import com.chunlangjiu.app.order.adapter.OrderListAdapter;
import com.chunlangjiu.app.order.bean.AuctionOrderListBean;
import com.chunlangjiu.app.order.bean.CancelReasonBean;
import com.chunlangjiu.app.order.bean.LogisticsBean;
import com.chunlangjiu.app.order.bean.OrderListBean;
import com.chunlangjiu.app.order.bean.PayResultBean;
import com.chunlangjiu.app.order.dialog.CancelOrderDialog;
import com.chunlangjiu.app.order.dialog.ChooseExpressDialog;
import com.chunlangjiu.app.order.dialog.ChooseExpressSellerDialog;
import com.chunlangjiu.app.order.dialog.RefundAfterSaleOrderDialog;
import com.chunlangjiu.app.order.dialog.RefundAmountDialog;
import com.chunlangjiu.app.order.dialog.SellerCancelOrderDialog;
import com.chunlangjiu.app.order.params.OrderParams;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.PayResult;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.pkqup.commonlibrary.dialog.CommonConfirmDialog;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.HttpUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OrderListFragment extends BaseFragment {
    private OrderMainNewActivity activity;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView listView;
    private View rlLoading;
    private View rlEmptyView;

    private boolean isVisible;
    private boolean isLoaded;
    private boolean initViewFinished;
    private String status; // 0 待处理 1 处理中 2 已处理 3 已驳回
    private String progress;// 0 等待商家处理 1 商家接受申请，等待消费者回寄 2 消费者回寄，等待商家收货确认 3 商家已驳回 4 商家已处理 5 商家确认收货 6 平台驳回退款申请 7 平台已处理退款申请 8 同意退款,提交到平台,等待平台处理
    private CompositeDisposable disposable;

    private ArrayList<OrderListBean.ListBean> listBeans;
    private OrderListAdapter orderListAdapter;

    private int type = 0;
    private int target = 0;

    private int pageNo = 1;

    private CancelOrderDialog cancelOrderDialog;
    private String tid;
    private ChooseExpressDialog chooseExpressDialog;
    private ChooseExpressSellerDialog chooseExpressSellerDialog;
    private String aftersales_bn;
    private String cancel_id;
    private SellerCancelOrderDialog sellerCancelOrderDialog;
    private RefundAfterSaleOrderDialog refundAfterSaleOrderDialog;
    private RefundAfterSaleOrderDialog refundCancelOrderDialog;

    private List<PaymentBean.PaymentInfo> payList;
    private String payMehtodId;//支付方式类型
    private PayDialog payDialog;
    private IWXAPI wxapi;
    private static final int SDK_PAY_FLAG = 1;
    private ResultBean<CreateOrderBean> createOrderBeanResultBean;
    private OrderListBean.ListBean payBean ;
    private String paymentId;

    private InputPriceDialog inputPriceDialog;

    CommonConfirmDialog confirmDialog ;
    RefundAmountDialog refundAmountDialog ;
    /**
     *  静态工厂方法需要一个int型的值来初始化fragment的参数，
     *  然后返回新的fragment到调用者
     */
    public static OrderListFragment newInstance(int type,int position) {
        OrderListFragment f = new OrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(OrderParams.TYPE, type);
        bundle.putInt(OrderParams.TARGET, position);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (OrderMainNewActivity) context;
        wxapi = WXAPIFactory.createWXAPI(activity, null);
        wxapi.registerApp("wx0e1869b241d7234f");
    }

    @Override
    public void getContentView(LayoutInflater inflater, ViewGroup container) {
        inflater.inflate(R.layout.order_fragment_orderlist, container, true);
    }

    @Override
    public void initView() {
        disposable = new CompositeDisposable();

        confirmDialog = new CommonConfirmDialog(activity,"确认删除订单吗？");
        confirmDialog.setDialogStr("取消","删除");
        refundAmountDialog = new RefundAmountDialog(activity);

        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo++;
                loadData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                loadData();
            }
        });
        listView = rootView.findViewById(R.id.listView);
        listView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        listView.addItemDecoration(new GridSpacingItemDecoration(1, Utils.dp2px(activity, 5), false));
        rlLoading = rootView.findViewById(R.id.rlLoading);
        rlEmptyView = rootView.findViewById(R.id.rlEmptyView);

        initViewFinished = true;
        EventManager.getInstance().registerListener(onNotifyListener);
    }

    @Override
    public void initData() {
        listBeans = new ArrayList<>();
        orderListAdapter = new OrderListAdapter(activity, R.layout.order_adapter_list_item, listBeans);

        GridSpacingItemDecoration decoration2 = new GridSpacingItemDecoration(1, Utils.dp2px(activity, 5), false);
        listView.addItemDecoration(decoration2);
        orderListAdapter.setOnItemChildClickListener(onItemChildClickListener);
        listView.setAdapter(orderListAdapter);

        delayLoad();
    }

    //判断是否展示—与ViewPager连用，进行左右切换
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        delayLoad();
    }

    private void delayLoad() {
        if (isVisible && initViewFinished && !isLoaded) {
            isLoaded = true;
            Bundle arguments = getArguments();
            if (null != arguments) {
                type = arguments.getInt(OrderParams.TYPE, 0);
                target = arguments.getInt(OrderParams.TARGET, 0);
                orderListAdapter.setType(type);
                loadData();
            }
        }
    }

    private void loadData() {
        switch (type) {
            case 0:
                switch (target) {
                    case 0:
                        status = "";
                        getNormalOrderList();
                        break;
                    case 1:
                        status = OrderParams.WAIT_BUYER_PAY;
                        getNormalOrderList();
                        break;
                    case 2:
                        status = OrderParams.WAIT_SELLER_SEND_GOODS;
                        getNormalOrderList();
                        break;
                    case 3:
                        status = OrderParams.WAIT_BUYER_CONFIRM_GOODS;
                        getNormalOrderList();
                        break;
                    case 4:
                        status = OrderParams.TRADE_FINISHED;
                        getNormalOrderList();
                        break;

                }

                break;
            case 1:
                switch (target) {
                    case 0:
                        status = OrderParams.AUCTION_WAIT_PAY;
                        getAuctionOrderLists();
                        break;
                    case 1:
                        status = OrderParams.AUCTION_BIDDING;
                        getAuctionOrderLists();
                        break;
                    case 2:
                        status = OrderParams.AUCTION_WON_BID;
                        getAuctionOrderLists();
                        break;
                    case 3:
                        status = OrderParams.AUCTION_OUTBID;
                        getAuctionOrderLists();
                        break;
                    case 4:
                        status = OrderParams.AUCTION_DELIVERY;
                        getAuctionOrderLists();
                        break;
                }
                getAuctionOrderLists();
                break;
            case 2:
                switch (target) {
                    case 0:
                        status = "";
                        progress = "";
                        break;
                    case 1:
                        status = "0";
                        progress = "0";
                        break;
                    case 2:
                        status = "1";
                        progress = "1";
                        break;
                    case 3:
                        status = "1";
                        progress = "2";
                        break;
                    case 4:
                        status = "2";
                        progress = "";
                        break;
                }
                getAfterSaleOrderList();
                break;
            case 3:
                switch (target) {
                    case 0:
                        status = "";
                        break;
                    case 1:
                        status = OrderParams.WAIT_BUYER_PAY;
                        break;
                    case 2:
                        status = OrderParams.WAIT_SELLER_SEND_GOODS;
                        break;
                    case 3:
                        status = OrderParams.WAIT_BUYER_CONFIRM_GOODS;
                        break;
                    case 4:
                        status = OrderParams.TRADE_FINISHED;
                        break;
                }
                getSellerNormalOrderList();
                break;
            case 4:
                switch (target) {
                    case 0:
                        status = "";
                        progress = "";
                        break;
                    case 1:
                        status = "0";
                        progress = "0";
                        break;
                    case 2:
                        status = "1";
                        progress = "1";
                        break;
                    case 3:
                        status = "1";
                        progress = "2";
                        break;
                    case 4:
                        status = "2";
                        progress = "";
                        break;
                }
                getSellerAfterSaleOrderList();
                break;
            case 5:
                getSellerAfterSaleCencelOrderList();
                break;
        }

    }

    private void getNormalOrderList() {
        disposable.add(ApiUtils.getInstance().getOrderLists(status, pageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<OrderListBean>>() {
                    @Override
                    public void accept(ResultBean<OrderListBean> orderListBeanResultBean) throws Exception {
                        if (null == orderListBeanResultBean.getData().getPagers() || pageNo == orderListBeanResultBean.getData().getPagers().getTotal() / 10 + 1) {
                            refreshLayout.setEnableLoadMore(false);
                        } else {
                            refreshLayout.setEnableLoadMore(true);
                        }
                        if (0 == orderListBeanResultBean.getErrorcode()) {
                            if (1 == pageNo) {
                                listBeans.clear();
                            }
                            if (null != orderListBeanResultBean.getData() && null != orderListBeanResultBean.getData().getList()) {
                                listBeans.addAll(orderListBeanResultBean.getData().getList());
                            }
                            orderListAdapter.notifyDataSetChanged();
                        } else if (0 != orderListBeanResultBean.getErrorcode()) {
                            if (1 < pageNo) {
                                pageNo--;
                            }
                        }
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                        rlLoading.setVisibility(View.GONE);
                        if (listBeans.isEmpty()) {
                            rlEmptyView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else {
                            listView.setVisibility(View.VISIBLE);
                            rlEmptyView.setVisibility(View.GONE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        rlLoading.setVisibility(View.GONE);
                        if (listBeans.isEmpty()) {
                            rlEmptyView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else {
                            listView.setVisibility(View.VISIBLE);
                            rlEmptyView.setVisibility(View.GONE);
                        }
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                        if (1 < pageNo) {
                            pageNo--;
                        }
                    }
                }));
    }

    private void getAuctionOrderLists() {
        disposable.add(ApiUtils.getInstance().getAuctionOrderLists(status, pageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<List<AuctionOrderListBean>>>() {
                    @Override
                    public void accept(ResultBean<List<AuctionOrderListBean>> orderListBeanResultBean) throws Exception {
//                        if (null == orderListBeanResultBean.getData().getPagers() || pageNo == orderListBeanResultBean.getData().getPagers().getTotal() / 10 + 1) {
//                            refreshLayout.setEnableLoadMore(false);
//                        } else {
//                            refreshLayout.setEnableLoadMore(true);
//                        }
                        if (0 == orderListBeanResultBean.getErrorcode()) {
                            if (1 == pageNo) {
                                listBeans.clear();
                            }
                            List<AuctionOrderListBean> data = orderListBeanResultBean.getData();
                            if (null != data && !data.isEmpty()) {
                                for (AuctionOrderListBean bean : data) {
                                    OrderListBean.ListBean listBean = new OrderListBean.ListBean();
                                    listBean.setTid(bean.getTid());
                                    listBean.setShopname(bean.getItem().getShopname());
                                    listBean.setShop_logo(bean.getItem().getShoplogo());
                                    listBean.setShop_id(bean.getItem().getShop_id());
                                    listBean.setTotalItem(1);
                                    listBean.setPayment(bean.getCur_money());
                                    listBean.setStatus(bean.getStatus());
                                    listBean.setPaymentId(bean.getPayment_id());
                                    listBean.setAuctionitem_id(bean.getAuctionitem_id());
                                    listBean.setAuction(bean.getAuction());
                                    listBean.setStatus_desc(bean.getStatus_desc());
                                    listBean.setTrade_ststus(bean.getTrade_ststus());
                                    List<OrderListBean.ListBean.OrderBean> order = new ArrayList<>();
                                    OrderListBean.ListBean.OrderBean orderBean = new OrderListBean.ListBean.OrderBean();
                                    orderBean.setNum(1);
                                    orderBean.setTitle(bean.getItem().getTitle());
                                    orderBean.setPrice(bean.getAuction().getStarting_price());
                                    orderBean.setPic_path(bean.getItem().getImage_default_id());
                                    if ("false".equalsIgnoreCase(bean.getAuction().getAuction_status())) {
                                        orderBean.setSpec_nature_info("保密出价");
                                    } else {
                                        orderBean.setSpec_nature_info(bean.getAuction().getMax_price());
                                    }
                                    order.add(orderBean);
                                    listBean.setOrder(order);

                                    listBeans.add(listBean);
                                }
                            }
                            orderListAdapter.notifyDataSetChanged();
                        } else if (0 != orderListBeanResultBean.getErrorcode()) {
                            if (1 < pageNo) {
                                pageNo--;
                            }
                        }
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                        rlLoading.setVisibility(View.GONE);
                        if (listBeans.isEmpty()) {
                            rlEmptyView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else {
                            listView.setVisibility(View.VISIBLE);
                            rlEmptyView.setVisibility(View.GONE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        rlLoading.setVisibility(View.GONE);
                        if (listBeans.isEmpty()) {
                            rlEmptyView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else {
                            listView.setVisibility(View.VISIBLE);
                            rlEmptyView.setVisibility(View.GONE);
                        }
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                        if (1 < pageNo) {
                            pageNo--;
                        }
                    }
                }));
    }

    private void getAfterSaleOrderList() {
        disposable.add(ApiUtils.getInstance().getAfterSaleOrderList(status, progress, pageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<OrderListBean>>() {
                    @Override
                    public void accept(ResultBean<OrderListBean> orderListBeanResultBean) throws Exception {
                        if (null == orderListBeanResultBean.getData().getPagers() || pageNo == orderListBeanResultBean.getData().getPagers().getTotal() / 10 + 1) {
                            refreshLayout.setEnableLoadMore(false);
                        } else {
                            refreshLayout.setEnableLoadMore(true);
                        }
                        if (0 == orderListBeanResultBean.getErrorcode()) {
                            if (1 == pageNo) {
                                listBeans.clear();
                            }
                            if (null != orderListBeanResultBean.getData() && null != orderListBeanResultBean.getData().getList()) {
                                listBeans.addAll(orderListBeanResultBean.getData().getList());
                            }
                            orderListAdapter.notifyDataSetChanged();
                        } else if (0 != orderListBeanResultBean.getErrorcode()) {
                            if (1 < pageNo) {
                                pageNo--;
                            }
                        }
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                        rlLoading.setVisibility(View.GONE);
                        if (listBeans.isEmpty()) {
                            rlEmptyView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else {
                            listView.setVisibility(View.VISIBLE);
                            rlEmptyView.setVisibility(View.GONE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        rlLoading.setVisibility(View.GONE);
                        if (listBeans.isEmpty()) {
                            rlEmptyView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else {
                            listView.setVisibility(View.VISIBLE);
                            rlEmptyView.setVisibility(View.GONE);
                        }
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                        if (1 < pageNo) {
                            pageNo--;
                        }
                    }
                }));
    }

    private void getSellerNormalOrderList() {
        disposable.add(ApiUtils.getInstance().getSellerOrderLists(status, pageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<OrderListBean>>() {
                    @Override
                    public void accept(ResultBean<OrderListBean> orderListBeanResultBean) throws Exception {
                        switch (type) {
                            case 3:
                                if (pageNo == orderListBeanResultBean.getData().getCount() / 10 + 1) {
                                    refreshLayout.setEnableLoadMore(false);
                                } else {
                                    refreshLayout.setEnableLoadMore(true);
                                }
                                break;
                        }
                        if (0 == orderListBeanResultBean.getErrorcode()) {
                            if (1 == pageNo) {
                                listBeans.clear();
                            }
                            if (null != orderListBeanResultBean.getData() && null != orderListBeanResultBean.getData().getList()) {
                                listBeans.addAll(orderListBeanResultBean.getData().getList());
                            }
                            orderListAdapter.notifyDataSetChanged();
                        } else if (0 != orderListBeanResultBean.getErrorcode()) {
                            if (1 < pageNo) {
                                pageNo--;
                            }
                        }
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                        rlLoading.setVisibility(View.GONE);
                        if (listBeans.isEmpty()) {
                            rlEmptyView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else {
                            listView.setVisibility(View.VISIBLE);
                            rlEmptyView.setVisibility(View.GONE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        rlLoading.setVisibility(View.GONE);
                        if (listBeans.isEmpty()) {
                            rlEmptyView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else {
                            listView.setVisibility(View.VISIBLE);
                            rlEmptyView.setVisibility(View.GONE);
                        }
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                        if (1 < pageNo) {
                            pageNo--;
                        }
                    }
                }));
    }

    private void getSellerAfterSaleOrderList() {
        disposable.add(ApiUtils.getInstance().getSellerAfterSaleOrderList(status, progress, pageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<OrderListBean>>() {
                    @Override
                    public void accept(ResultBean<OrderListBean> orderListBeanResultBean) throws Exception {
                        if (null == orderListBeanResultBean.getData().getPagers() || pageNo == orderListBeanResultBean.getData().getPagers().getTotal() / 10 + 1) {
                            refreshLayout.setEnableLoadMore(false);
                        } else {
                            refreshLayout.setEnableLoadMore(true);
                        }
                        if (0 == orderListBeanResultBean.getErrorcode()) {
                            if (1 == pageNo) {
                                listBeans.clear();
                            }
                            if (null != orderListBeanResultBean.getData() && null != orderListBeanResultBean.getData().getList()) {
                                listBeans.addAll(orderListBeanResultBean.getData().getList());
                            }
                            orderListAdapter.notifyDataSetChanged();
                        } else if (0 != orderListBeanResultBean.getErrorcode()) {
                            if (1 < pageNo) {
                                pageNo--;
                            }
                        }
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                        rlLoading.setVisibility(View.GONE);
                        if (listBeans.isEmpty()) {
                            rlEmptyView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else {
                            listView.setVisibility(View.VISIBLE);
                            rlEmptyView.setVisibility(View.GONE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        rlLoading.setVisibility(View.GONE);
                        if (listBeans.isEmpty()) {
                            rlEmptyView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else {
                            listView.setVisibility(View.VISIBLE);
                            rlEmptyView.setVisibility(View.GONE);
                        }
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                        if (1 < pageNo) {
                            pageNo--;
                        }
                    }
                }));
    }

    private void getSellerAfterSaleCencelOrderList() {
        disposable.add(ApiUtils.getInstance().getSellerAfterSaleCencelOrderList(pageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<OrderListBean>>() {
                    @Override
                    public void accept(ResultBean<OrderListBean> orderListBeanResultBean) throws Exception {
                        if (null == orderListBeanResultBean.getData().getPagers() || pageNo == orderListBeanResultBean.getData().getPagers().getTotal() / 10 + 1) {
                            refreshLayout.setEnableLoadMore(false);
                        } else {
                            refreshLayout.setEnableLoadMore(true);
                        }
                        if (0 == orderListBeanResultBean.getErrorcode()) {
                            if (1 == pageNo) {
                                listBeans.clear();
                            }
                            if (null != orderListBeanResultBean.getData() && null != orderListBeanResultBean.getData().getList()) {
                                listBeans.addAll(orderListBeanResultBean.getData().getList());
                            }
                            orderListAdapter.notifyDataSetChanged();
                        } else if (0 != orderListBeanResultBean.getErrorcode()) {
                            if (1 < pageNo) {
                                pageNo--;
                            }
                        }
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                        rlLoading.setVisibility(View.GONE);
                        if (listBeans.isEmpty()) {
                            rlEmptyView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else {
                            listView.setVisibility(View.VISIBLE);
                            rlEmptyView.setVisibility(View.GONE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        rlLoading.setVisibility(View.GONE);
                        if (listBeans.isEmpty()) {
                            rlEmptyView.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        } else {
                            listView.setVisibility(View.VISIBLE);
                            rlEmptyView.setVisibility(View.GONE);
                        }
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                        if (1 < pageNo) {
                            pageNo--;
                        }
                    }
                }));
    }


    private BaseQuickAdapter.OnItemChildClickListener onItemChildClickListener = new BaseQuickAdapter.OnItemChildClickListener() {
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view,final int position) {
            OrderListBean.ListBean orderDetailBean = listBeans.get(position);
            switch (view.getId()) {
                case R.id.llStore:
                    ShopMainActivity.startShopMainActivity(activity, String.valueOf(listBeans.get(position).getShop_id()));
                    break;
                case R.id.llProducts:
                case R.id.llBottom:
                    toOrderDetailActivity(position);
                    break;
                case R.id.tvCancel://取消订单
                    if (type == 3) {
                        tid = String.valueOf(orderDetailBean.getTid());
                        getSellerCancelReason();
                    } else {
                        tid = String.valueOf(orderDetailBean.getTid());
                        getCancelReason();
                    }
                    break;
                case R.id.tvRefund://申请退款
                    break;
                case R.id.tvNotGoods://无货
                    tid = String.valueOf(orderDetailBean.getTid());
                    getSellerCancelReason();
                    break;
                case R.id.tvConsentRefund://同意退款
                    if (type == 5) {
                        cancel_id = String.valueOf(orderDetailBean.getCancel_id());
                        showLoadingDialog();
                        disposable.add(ApiUtils.getInstance().applySellerCancelOrder(cancel_id, "agree", "")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<ResultBean>() {
                                    @Override
                                    public void accept(ResultBean resultBean) throws Exception {
                                        hideLoadingDialog();
                                        if (0 == resultBean.getErrorcode()) {
                                            ToastUtils.showShort("同意退款成功");
//                                            EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
                                            refreshLayout.autoRefresh();
                                        } else {
                                            if (TextUtils.isEmpty(resultBean.getMsg())) {
                                                ToastUtils.showShort("同意退款失败");
                                            } else {
                                                ToastUtils.showShort(resultBean.getMsg());
                                            }
                                        }
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        hideLoadingDialog();
                                        if (TextUtils.isEmpty(throwable.getMessage())) {
                                            ToastUtils.showShort("同意退款失败");
                                        } else {
                                            ToastUtils.showShort(throwable.getMessage());
                                        }
                                    }
                                }));
                        break;
                    } else {
                        refundAmountDialog.show();
                        refundAmountDialog.setCallBack(new RefundAmountDialog.CallBack() {
                            @Override
                            public void confirm(String money) {
                                showLoadingDialog();
                                OrderListBean.ListBean orderDetailBean = listBeans.get(position);
                                aftersales_bn = String.valueOf(orderDetailBean.getAftersales_bn());
                                disposable.add(ApiUtils.getInstance().applySellerAfterSale(aftersales_bn, "true",money, "")
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<ResultBean>() {
                                            @Override
                                            public void accept(ResultBean resultBean) throws Exception {
                                                hideLoadingDialog();
                                                if (0 == resultBean.getErrorcode()) {
                                                    ToastUtils.showShort("商品签单并同意退款成功");
//                                                    EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
                                                    refreshLayout.autoRefresh();
                                                } else {
                                                    if (TextUtils.isEmpty(resultBean.getMsg())) {
                                                        ToastUtils.showShort("商品签单并同意退款失败");
                                                    } else {
                                                        ToastUtils.showShort(resultBean.getMsg());
                                                    }
                                                }
                                            }
                                        }, new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable throwable) throws Exception {
                                                hideLoadingDialog();
                                                if (TextUtils.isEmpty(throwable.getMessage())) {
                                                    ToastUtils.showShort("商品签单并同意退款失败");
                                                } else {
                                                    ToastUtils.showShort(throwable.getMessage());
                                                }
                                            }
                                        }));
                            }
                        });

                    }
                    break;
                case R.id.tvRefusedRefund://拒绝退款
                    break;
                case R.id.tvReturnSend://退货发货
                    aftersales_bn = String.valueOf(listBeans.get(position).getAftersales_bn());
                    getLogisticsList();
                    break;
                case R.id.tvBackOutApply ://撤销申请
                    break;
                case R.id.tvRefusedApply ://拒绝申请
                    if (type == 4) {
                        aftersales_bn = String.valueOf(orderDetailBean.getAftersales_bn());
                        if (null == refundAfterSaleOrderDialog) {
                            refundAfterSaleOrderDialog = new RefundAfterSaleOrderDialog(activity);
                            refundAfterSaleOrderDialog.setCallBack(new RefundAfterSaleOrderDialog.CallBack() {
                                @Override
                                public void confirm(String reason) {
                                    showLoadingDialog();
                                    disposable.add(ApiUtils.getInstance().applySellerAfterSale(aftersales_bn, "false", "", reason)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<ResultBean>() {
                                                @Override
                                                public void accept(ResultBean resultBean) throws Exception {
                                                    hideLoadingDialog();
                                                    if (0 == resultBean.getErrorcode()) {
                                                        ToastUtils.showShort("拒绝申请成功");
//                                                        EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
                                                        refreshLayout.autoRefresh();
                                                    } else {
                                                        if (TextUtils.isEmpty(resultBean.getMsg())) {
                                                            ToastUtils.showShort("拒绝申请失败");
                                                        } else {
                                                            ToastUtils.showShort(resultBean.getMsg());
                                                        }
                                                    }
                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Exception {
                                                    hideLoadingDialog();
                                                    if (TextUtils.isEmpty(throwable.getMessage())) {
                                                        ToastUtils.showShort("拒绝申请失败");
                                                    } else {
                                                        ToastUtils.showShort(throwable.getMessage());
                                                    }
                                                }
                                            }));
                                }
                            });
                        }
                        refundAfterSaleOrderDialog.show();
                    } else if (type == 5) {
                        cancel_id = String.valueOf(orderDetailBean.getCancel_id());
                        if (null == refundCancelOrderDialog) {
                            refundCancelOrderDialog = new RefundAfterSaleOrderDialog(activity);
                            refundCancelOrderDialog.setCallBack(new RefundAfterSaleOrderDialog.CallBack() {
                                @Override
                                public void confirm(String reason) {
                                    showLoadingDialog();
                                    disposable.add(ApiUtils.getInstance().applySellerCancelOrder(cancel_id, "reject", reason)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new Consumer<ResultBean>() {
                                                @Override
                                                public void accept(ResultBean resultBean) throws Exception {
                                                    hideLoadingDialog();
                                                    if (0 == resultBean.getErrorcode()) {
                                                        ToastUtils.showShort("拒绝申请成功");
                                                        refreshLayout.autoRefresh();
                                                    } else {
                                                        if (TextUtils.isEmpty(resultBean.getMsg())) {
                                                            ToastUtils.showShort("拒绝申请失败");
                                                        } else {
                                                            ToastUtils.showShort(resultBean.getMsg());
                                                        }
                                                    }
                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Exception {
                                                    hideLoadingDialog();
                                                    if (TextUtils.isEmpty(throwable.getMessage())) {
                                                        ToastUtils.showShort("拒绝申请失败");
                                                    } else {
                                                        ToastUtils.showShort(throwable.getMessage());
                                                    }
                                                }
                                            }));
                                }
                            });
                        }
                        refundCancelOrderDialog.show();
                    }
                    break;
                case R.id.tvConsentApply://同意申请
                    showLoadingDialog();
                    aftersales_bn = String.valueOf(orderDetailBean.getAftersales_bn());
                    disposable.add(ApiUtils.getInstance().applySellerAfterSale(aftersales_bn, "true", "", "")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<ResultBean>() {
                                @Override
                                public void accept(ResultBean resultBean) throws Exception {
                                    hideLoadingDialog();
                                    if (0 == resultBean.getErrorcode()) {
                                        ToastUtils.showShort("同意申请成功");
                                        refreshLayout.autoRefresh();
                                    } else {
                                        if (TextUtils.isEmpty(resultBean.getMsg())) {
                                            ToastUtils.showShort("同意申请失败");
                                        } else {
                                            ToastUtils.showShort(resultBean.getMsg());
                                        }
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    hideLoadingDialog();
                                    if (TextUtils.isEmpty(throwable.getMessage())) {
                                        ToastUtils.showShort("同意申请失败");
                                    } else {
                                        ToastUtils.showShort(throwable.getMessage());
                                    }
                                }
                            }));
                    break;
                case R.id.tvSendGoods://发货
                    tid = String.valueOf(orderDetailBean.getTid());
                    getSellerLogisticsList();
                    break;
                case R.id.tvGoodsSignBill://商品签单
                case R.id.tvConfirmReceipt://确认收货
                    tid = String.valueOf(listBeans.get(position).getTid());
                    confirmReceipt();
                    break;
                case R.id.tvEvaluate://去评价
                    OrderListBean.ListBean listBean = listBeans.get(position);
                    Intent intent = new Intent(activity, OrderEvaluationDetailActivity.class);
                    intent.putExtra(OrderParams.PRODUCTS, listBean);
                    startActivity(intent);
                    break;
                case R.id.tvPay://去支付
                    payBean = listBeans.get(position);
                    tid = payBean.getTid()+"";
                    repay("true");
                    break;
                case R.id.tvPaymentBtn://去支付
                    payBean = listBeans.get(position);
                    tid = payBean.getTid()+"";
                    repay("");
                break;
                case R.id.tvEditPrice://修改出价
                    changeMyPrice(position);
                    break;
                case R.id.tvPayDeposit://去付定金
                    payBean = listBeans.get(position);
                    paymentId = payBean.getPaymentId();
                    getPayment();
                    break;
                case R.id.tvDelete://删除订单
                case R.id.ivDel:
                    //删除订单
                    tid = String.valueOf(listBeans.get(position).getTid());
                    delete();
                    break;
                case R.id.tvServerInto:
                    CommonUtils.callPhone("4007889550");
                    break;
            }
        }
    };

    private void changeMyPrice(int position) {
        String max_price;

        AuctionOrderListBean.AuctionBean auctionBean = listBeans.get(position).getAuction();
        if ("false".equalsIgnoreCase(auctionBean.getAuction_status())) {
            max_price = "保密出价";
        } else {
            max_price = auctionBean.getMax_price();
        }
        String original_bid = auctionBean.getOriginal_bid();
        if (inputPriceDialog == null) {
            inputPriceDialog = new InputPriceDialog(activity, max_price, original_bid);
        } else {
            inputPriceDialog.updatePrice(max_price, original_bid);
        }
        final String auctionItemId = String.valueOf(auctionBean.getAuctionitem_id());
        inputPriceDialog.setCallBack(new InputPriceDialog.CallBack() {
            @Override
            public void editPrice(String price) {
                editGivePrice(auctionItemId,price);
            }
        });
        inputPriceDialog.show();
    }

    private void editGivePrice(String auctionItemId,String price) {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().auctionAddPrice(auctionItemId, price)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        if (0 == resultBean.getErrorcode()) {
                            ToastUtils.showShort("修改出价成功");
                            refreshLayout.autoRefresh();
                        } else {
                            if (TextUtils.isEmpty(resultBean.getMsg())) {
                                ToastUtils.showShort("修改出价失败");
                            } else {
                                ToastUtils.showShort(resultBean.getMsg());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        if (TextUtils.isEmpty(throwable.getMessage())) {
                            ToastUtils.showShort("修改出价失败");
                        } else {
                            ToastUtils.showShort(throwable.getMessage());
                        }
                    }
                }));
    }

    private void repay(String merge) {
        activity.showLoadingDialog();
        disposable.add(ApiUtils.getInstance().repay(tid, merge)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<CreateOrderBean>>() {
                    @Override
                    public void accept(final ResultBean<CreateOrderBean> resultBean) throws Exception {
                        activity.hideLoadingDialog();
                        if (0 == resultBean.getErrorcode()) {
                            createOrderBeanResultBean = resultBean;
                            paymentId = createOrderBeanResultBean.getData().getPayment_id();
                            getPayment();
                        } else {
                            if (TextUtils.isEmpty(resultBean.getMsg())) {
                                ToastUtils.showShort("支付失败");
                            } else {
                                ToastUtils.showShort(resultBean.getMsg());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        activity.hideLoadingDialog();
                        if (TextUtils.isEmpty(throwable.getMessage())) {
                            ToastUtils.showShort("支付失败");
                        } else {
                            ToastUtils.showShort(throwable.getMessage());
                        }
                    }
                }));
    }

    private void getPayment() {
        activity.showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getPayment()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<PaymentBean>>() {
                    @Override
                    public void accept(ResultBean<PaymentBean> paymentBeanResultBean) throws Exception {
                        activity.hideLoadingDialog();
                        if (0 == paymentBeanResultBean.getErrorcode()) {
                            payList = paymentBeanResultBean.getData().getList();
                            if (payList == null || payList.size() == 0) {
                                ToastUtils.showShort("获取支付方式失败");
                            } else {
                                String payMoney = payBean!=null ?payBean.getPayment():"";
                                payDialog = new PayDialog(activity, payList,payMoney);
                                payDialog.setCallBack(new PayDialog.CallBack() {
                                    @Override
                                    public void choicePayMethod( String payMethodId) {
//                                        payDo(payMethod, payMethodId);
                                        confirmPayMode(payMethodId);
                                    }
                                });
                                payDialog.show();
                            }
                        } else {
                            if (TextUtils.isEmpty(paymentBeanResultBean.getMsg())) {
                                ToastUtils.showShort("获取支付方式失败");
                            } else {
                                ToastUtils.showShort(paymentBeanResultBean.getMsg());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        activity.hideLoadingDialog();
                        if (TextUtils.isEmpty(throwable.getMessage())) {
                            ToastUtils.showShort("获取支付方式失败");
                        } else {
                            ToastUtils.showShort(throwable.getMessage());
                        }
                    }
                }));
    }

    /**
     * 确认支付方式
     */
    private void confirmPayMode(final String payMethodId){
        if(OrderParams.PAY_APP_DEPOSIT.equals(payMethodId)){
            String payMoney = payBean!=null ?payBean.getPayment():"";
            BalancePayDialog balancePayDialog = new BalancePayDialog(activity,payMoney);
            balancePayDialog.setCallBack(new BalancePayDialog.CallBack() {
                @Override
                public void cancelPay() {
                }
                @Override
                public void confirmPay(String pwd) {
                    payDo(payMethodId,pwd);
                }
            });
            balancePayDialog.show();
        }else{
            payDo(payMethodId,"");
        }
    }

    private void payDo( String payMethodId,String payPwd) {
        this.payMehtodId = payMethodId;
        disposable.add(ApiUtils.getInstance().payDo(paymentId, payMehtodId,payPwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        if (0 == resultBean.getErrorcode()) {
                            invokePay(resultBean);
                        } else {
                            if (TextUtils.isEmpty(resultBean.getMsg())) {
                                ToastUtils.showShort("支付失败");
                            } else {
                                ToastUtils.showShort(resultBean.getMsg());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        if (TextUtils.isEmpty(throwable.getMessage())) {
                            ToastUtils.showShort("支付失败");
                        } else {
                            ToastUtils.showShort(throwable.getMessage());
                        }
                    }
                }));
    }

    private void invokePay(ResultBean data) {
        switch (payMehtodId) {
            case OrderParams.PAY_APP_WXPAY:
                invokeWeixinPay(data);
                break;
            case OrderParams.PAY_APP_ALIPAY:
                invokeZhifubaoPay(data);
                break;
            case OrderParams.PAY_APP_DEPOSIT:
                invokeYuePay(data);
                break;
        }
    }

    private void invokeWeixinPay(ResultBean data) {
        PayReq request = new PayReq();
        request.appId = "wx0e1869b241d7234f";
        request.partnerId = data.getPartnerid();
        request.prepayId = data.getPrepayid();
        request.packageValue = data.getPackageName();
        request.nonceStr = data.getNoncestr();
        request.timeStamp = data.getTimestamp();
        request.sign = data.getSign();
        wxapi.sendReq(request);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == SDK_PAY_FLAG) {
                @SuppressWarnings("unchecked")
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();
                    refreshLayout.autoRefresh();
                    EventManager.getInstance().notify(null, ConstantMsg.UPDATE_CART_LIST);
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private void invokeZhifubaoPay(ResultBean data) {
        final String url = data.getUrl();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                Map<String, String> stringStringMap = alipay.payV2(url, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = stringStringMap;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void invokeYuePay(ResultBean data) {
        PayResultBean payResultBean = (PayResultBean)data.getData();
        if(payResultBean!=null && "success".equals(payResultBean.getStatus())){
            refreshLayout.autoRefresh();
        }
    }

    private void invokeDaePay(ResultBean data) {

    }

    private void getCancelReason() {
        activity.showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getCancelReason()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<CancelReasonBean>>() {
                    @Override
                    public void accept(ResultBean<CancelReasonBean> cancelReasonBeanResultBean) throws Exception {
                        activity.hideLoadingDialog();
                        if (0 == cancelReasonBeanResultBean.getErrorcode()) {
                            List<String> list = cancelReasonBeanResultBean.getData().getList();
                            if (null == cancelOrderDialog) {
                                cancelOrderDialog = new CancelOrderDialog(activity, list, tid);
                                cancelOrderDialog.setCancelCallBack(new CancelOrderDialog.CancelCallBack() {
                                    @Override
                                    public void cancelSuccess() {
                                        ToastUtils.showShort("取消订单成功");
                                        refreshLayout.autoRefresh();
                                    }

                                    @Override
                                    public void cancelFail(String msg) {
                                        if (TextUtils.isEmpty(msg)) {
                                            ToastUtils.showShort("取消订单失败");
                                        } else {
                                            ToastUtils.showShort(msg);
                                        }
                                    }
                                });
                            } else {
                                cancelOrderDialog.setData(list, tid);
                            }
                            cancelOrderDialog.show();
                        } else {
                            if (TextUtils.isEmpty(cancelReasonBeanResultBean.getMsg())) {
                                ToastUtils.showShort("获取取消原因列表失败");
                            } else {
                                ToastUtils.showShort(cancelReasonBeanResultBean.getMsg());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        activity.hideLoadingDialog();
                        if (TextUtils.isEmpty(throwable.getMessage())) {
                            ToastUtils.showShort("获取取消原因列表失败");
                        } else {
                            ToastUtils.showShort(throwable.getMessage());
                        }
                    }
                }));
    }

    private void confirmReceipt() {
        activity.showLoadingDialog();
        disposable.add(ApiUtils.getInstance().confirmReceipt(tid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        activity.hideLoadingDialog();
                        if (0 == resultBean.getErrorcode()) {
                            ToastUtils.showShort("商品签单成功");
                            refreshLayout.autoRefresh();
                        } else {
                            if (TextUtils.isEmpty(resultBean.getMsg())) {
                                ToastUtils.showShort("商品签单失败");
                            } else {
                                ToastUtils.showShort(resultBean.getMsg());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        activity.hideLoadingDialog();
                        if (TextUtils.isEmpty(throwable.getMessage())) {
                            ToastUtils.showShort("商品签单失败");
                        } else {
                            ToastUtils.showShort(throwable.getMessage());
                        }
                    }
                }));
    }

    private void delete() {
        confirmDialog.show();
        confirmDialog.setCallBack(new CommonConfirmDialog.CallBack() {
            @Override
            public void onConfirm() {
                activity.showLoadingDialog();
                disposable.add(ApiUtils.getInstance().delete(tid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResultBean>() {
                            @Override
                            public void accept(ResultBean resultBean) throws Exception {
                                activity.hideLoadingDialog();
                                if (0 == resultBean.getErrorcode()) {
                                    ToastUtils.showShort("删除订单成功");
                                    refreshLayout.autoRefresh();
                                } else {
                                    if (TextUtils.isEmpty(resultBean.getMsg())) {
                                        ToastUtils.showShort("删除订单失败");
                                    } else {
                                        ToastUtils.showShort(resultBean.getMsg());
                                    }
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                activity.hideLoadingDialog();
                                if (TextUtils.isEmpty(throwable.getMessage())) {
                                    ToastUtils.showShort("删除订单失败");
                                } else {
                                    ToastUtils.showShort(throwable.getMessage());
                                }
                            }
                        }));
            }

            @Override
            public void onCancel() {

            }
        });

    }

    private void toOrderDetailActivity(int position) {
        Intent intent = new Intent(activity, OrderDetailActivity.class);
        intent.putExtra(OrderParams.ORDERID, listBeans.get(position).getTid());
        if (null != listBeans.get(position).getSku()) {
            intent.putExtra(OrderParams.OID, listBeans.get(position).getSku().getOid());
            intent.putExtra(OrderParams.AFTERSALESBN, listBeans.get(position).getAftersales_bn());
        }
        intent.putExtra(OrderParams.CANCELID, listBeans.get(position).getCancel_id());
        intent.putExtra(OrderParams.AUCTIONITEMID, listBeans.get(position).getAuctionitem_id());
        OrderListBean.ListBean listBean = listBeans.get(position);
        intent.putExtra(OrderParams.PRODUCTS, listBean);
        intent.putExtra(OrderParams.TYPE, type);
        startActivity(intent);
    }

    private void getLogisticsList() {
        activity.showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getLogisticsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<LogisticsBean>>() {
                    @Override
                    public void accept(ResultBean<LogisticsBean> logisticsBeanResultBean) throws Exception {
                        activity.hideLoadingDialog();
                        if (0 == logisticsBeanResultBean.getErrorcode()) {
                            LogisticsBean data = logisticsBeanResultBean.getData();
                            if (null == chooseExpressDialog) {
                                chooseExpressDialog = new ChooseExpressDialog(activity, data.getList(), aftersales_bn);
                                chooseExpressDialog.setCallBack(new ChooseExpressDialog.CallBack() {
                                    @Override
                                    public void sendExpressSuccess() {
                                        refreshLayout.autoRefresh();
                                    }
                                });
                            } else {
                                chooseExpressDialog.setData(data.getList(), aftersales_bn);
                            }
                            chooseExpressDialog.show();
                        } else {
                            if (TextUtils.isEmpty(logisticsBeanResultBean.getMsg())) {
                                ToastUtils.showShort("获取快递公司列表失败");
                            } else {
                                ToastUtils.showShort(logisticsBeanResultBean.getMsg());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        activity.hideLoadingDialog();
                        if (TextUtils.isEmpty(throwable.getMessage())) {
                            ToastUtils.showShort("获取快递公司列表失败");
                        } else {
                            ToastUtils.showShort(throwable.getMessage());
                        }
                    }
                }));
    }

    private void getSellerLogisticsList() {
        activity.showLoadingDialog();
        HttpUtils.USER_TOKEN = true;
        disposable.add(ApiUtils.getInstance().getLogisticsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<LogisticsBean>>() {
                    @Override
                    public void accept(ResultBean<LogisticsBean> logisticsBeanResultBean) throws Exception {
                        activity.hideLoadingDialog();
                        HttpUtils.USER_TOKEN = false;
                        if (0 == logisticsBeanResultBean.getErrorcode()) {
                            LogisticsBean data = logisticsBeanResultBean.getData();
                            if (null == chooseExpressSellerDialog) {
                                chooseExpressSellerDialog = new ChooseExpressSellerDialog(activity, data.getList(), tid);
                                chooseExpressSellerDialog.setCallBack(new ChooseExpressSellerDialog.CallBack() {
                                    @Override
                                    public void sendExpressSuccess() {
                                        refreshLayout.autoRefresh();
                                    }
                                });
                            } else {
                                chooseExpressSellerDialog.setData(data.getList(), tid);
                            }
                            chooseExpressSellerDialog.show();
                        } else {
                            if (TextUtils.isEmpty(logisticsBeanResultBean.getMsg())) {
                                ToastUtils.showShort("获取快递公司列表失败");
                            } else {
                                ToastUtils.showShort(logisticsBeanResultBean.getMsg());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        activity.hideLoadingDialog();
                        if (TextUtils.isEmpty(throwable.getMessage())) {
                            ToastUtils.showShort("获取快递公司列表失败");
                        } else {
                            ToastUtils.showShort(throwable.getMessage());
                        }
                    }
                }));
    }

    private void getSellerCancelReason() {
        activity.showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getSellerCancelReason()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<CancelReasonBean>>() {
                    @Override
                    public void accept(ResultBean<CancelReasonBean> cancelReasonBeanResultBean) throws Exception {
                        activity.hideLoadingDialog();
                        if (0 == cancelReasonBeanResultBean.getErrorcode()) {
                            List<String> list = cancelReasonBeanResultBean.getData().getList();
                            if (null == sellerCancelOrderDialog) {
                                sellerCancelOrderDialog = new SellerCancelOrderDialog(activity, list, tid);
                                sellerCancelOrderDialog.setCancelCallBack(new SellerCancelOrderDialog.CancelCallBack() {
                                    @Override
                                    public void cancelSuccess() {
                                        refreshLayout.autoRefresh();
                                    }
                                });
                            } else {
                                sellerCancelOrderDialog.setData(list, tid);
                            }
                            sellerCancelOrderDialog.show();
                        } else {
                            if (TextUtils.isEmpty(cancelReasonBeanResultBean.getMsg())) {
                                ToastUtils.showShort("获取取消原因列表失败");
                            } else {
                                ToastUtils.showShort(cancelReasonBeanResultBean.getMsg());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        activity.hideLoadingDialog();
                        if (TextUtils.isEmpty(throwable.getMessage())) {
                            ToastUtils.showShort("获取取消原因列表失败");
                        } else {
                            ToastUtils.showShort(throwable.getMessage());
                        }
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        EventManager.getInstance().unRegisterListener(onNotifyListener);
    }

    private void weixinPaySuccess(Object object, String eventTag) {
        if (eventTag.equals(ConstantMsg.WEIXIN_PAY_CALLBACK)) {
            int code = (int) object;
            if (code == 0) {
                //支付成功
                ToastUtils.showShort("支付成功");
                refreshLayout.autoRefresh();
                EventManager.getInstance().notify(null, ConstantMsg.UPDATE_CART_LIST);
            } else if (code == -1) {
                //支付错误
                ToastUtils.showShort("支付失败");
            } else if (code == -2) {
                //支付取消
                ToastUtils.showShort("支付失败");
            }
        }
    }

    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            if (OrderParams.REFRESH_ORDER_LIST.equals(eventTag)) {
                refreshLayout.autoRefresh();
            } else {
                weixinPaySuccess(object, eventTag);
            }
        }
    };

}
