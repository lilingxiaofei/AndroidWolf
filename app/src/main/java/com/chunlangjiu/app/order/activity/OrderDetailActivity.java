package com.chunlangjiu.app.order.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.goods.bean.CreateOrderBean;
import com.chunlangjiu.app.goods.bean.PaymentBean;
import com.chunlangjiu.app.goods.dialog.BalancePayDialog;
import com.chunlangjiu.app.goods.dialog.InputPriceDialog;
import com.chunlangjiu.app.goods.dialog.PayDialog;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.order.adapter.OrderAfterSalePicAdapter;
import com.chunlangjiu.app.order.bean.CancelReasonBean;
import com.chunlangjiu.app.order.bean.LogisticsBean;
import com.chunlangjiu.app.order.bean.OrderDetailBean;
import com.chunlangjiu.app.order.bean.OrderListBean;
import com.chunlangjiu.app.order.bean.PayResultBean;
import com.chunlangjiu.app.order.bean.SellerOrderDetailBean;
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
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.HttpUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.BeanCopyUitl;
import com.pkqup.commonlibrary.util.BigDecimalUtils;
import com.pkqup.commonlibrary.util.TimeUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.pkqup.commonlibrary.view.countdownview.CountdownView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OrderDetailActivity extends BaseActivity {
    private CompositeDisposable disposable;

    private OrderDetailBean orderDetailBean;
    private ClipboardManager myClipboard;

    @BindView(R.id.rlLoading)
    View rlLoading;
    @BindView(R.id.tvOrderStatus)
    TextView tvOrderStatus;
    @BindView(R.id.imgStore)
    ImageView imgStore;
    @BindView(R.id.tvStore)
    TextView tvStore;
    @BindView(R.id.tvOrderId)
    TextView tvOrderId;
    @BindView(R.id.tvCopy)
    TextView tvCopy;
    @BindView(R.id.tvCreateTime)
    TextView tvCreateTime;
    @BindView(R.id.tvPayType)
    TextView tvPayType;
    @BindView(R.id.tvPayTime)
    TextView tvPayTime;
    @BindView(R.id.llSendTime)
    LinearLayout llSendTime;
    @BindView(R.id.tvSendTime)
    TextView tvSendTime;
    @BindView(R.id.llFinishTime)
    LinearLayout llFinishTime;
    @BindView(R.id.tvFinishTime)
    TextView tvFinishTime;
    @BindView(R.id.tvUserInfo)
    TextView tvUserInfo;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvTotalPrice)
    TextView tvTotalPrice;

    @BindView(R.id.llCommission)
    LinearLayout llCommission;
    @BindView(R.id.tvCommission)
    TextView tvCommission;

    @BindView(R.id.llShopPayment)
    LinearLayout llShopPayment;
    @BindView(R.id.tvShopPayment)
    TextView tvShopPayment;

    @BindView(R.id.tvSendPrice)
    TextView tvSendPrice;
    @BindView(R.id.tvPayment)
    TextView tvPayment;
    @BindView(R.id.llProducts)
    LinearLayout llProducts;
    @BindView(R.id.llPayTime)
    LinearLayout llPayTime;
    @BindView(R.id.llOrderTitleRightContent)
    LinearLayout llOrderTitleRightContent;
    @BindView(R.id.tvRightContentDesc)
    TextView tvRightContentDesc;
    @BindView(R.id.tvRightContent)
    TextView tvRightContent;

    @BindView(R.id.llApplyReason)
    LinearLayout llApplyReason;
    @BindView(R.id.tvApplyReason)
    TextView tvApplyReason;
    @BindView(R.id.llRefusalCause)
    LinearLayout llRefusalCause;
    @BindView(R.id.tvRefusalCause)
    TextView tvRefusalCause;

    //售后备注
    @BindView(R.id.llAfterSaleRemark)
    LinearLayout llAfterSaleRemark;
    @BindView(R.id.tvAfterSaleRemark)
    TextView tvAfterSaleRemark;
    //售后图片
    @BindView(R.id.rlAfterSalePic)
    RelativeLayout rlAfterSalePic;
    @BindView(R.id.tvAfterSalePic)
    TextView tvAfterSalePic;
    @BindView(R.id.rvAfterSalePic)
    RecyclerView rvAfterSalePic;

    @BindView(R.id.llPayType)
    LinearLayout llPayType;
    @BindView(R.id.llAfterSaleTme)
    LinearLayout llAfterSaleTme;
    @BindView(R.id.tvAfterSaleCreateTime)
    TextView tvAfterSaleCreateTime;
    @BindView(R.id.llAfterSaleSendTime)
    LinearLayout llAfterSaleSendTime;
    @BindView(R.id.tvAfterSaleSendTime)
    TextView tvAfterSaleSendTime;
    @BindView(R.id.llAfterSalePayTime)
    LinearLayout llAfterSalePayTime;
    @BindView(R.id.tvAfterSalePayTime)
    TextView tvAfterSalePayTime;
    @BindView(R.id.countdownView)
    CountdownView countdownView;
    @BindView(R.id.llUserInfo)
    LinearLayout llUserInfo;
    @BindView(R.id.llSendPrice)
    LinearLayout llSendPrice;
    @BindView(R.id.llTotalPrice)
    LinearLayout llTotalPrice;
    @BindView(R.id.tvPaymentTips)
    TextView tvPaymentTips;
    @BindView(R.id.llOrderId)
    LinearLayout llOrderId;
    @BindView(R.id.ivAuctionBid)
    ImageView ivAuctionBid;


    @BindView(R.id.llLogi)
    LinearLayout llLogi;
    @BindView(R.id.tvLogiName)
    TextView tvLogiName;
    @BindView(R.id.tvLogiNo)
    TextView tvLogiNo;
    @BindView(R.id.tvLogiNoCopy)
    TextView tvLogiNoCopy;


    @BindView(R.id.llInfo)
    LinearLayout llInfo;
    @BindView(R.id.tvInfo)
    TextView tvInfo;

    @BindView(R.id.tvComplain)
    TextView tvComplain;
    @BindView(R.id.tvDelete)
    TextView tvDelete;//删除订单
    @BindView(R.id.tvServerInto)
    TextView tvServerInto;//服务介入
    @BindView(R.id.tvCancel)
    TextView tvCancel;//取消订单
    @BindView(R.id.tvRefund)
    TextView tvRefund;//申请退款
    @BindView(R.id.tvNotGoods)
    TextView tvNotGoods;//无货
    @BindView(R.id.tvConsentRefund)
    TextView tvConsentRefund;//同意退款
    @BindView(R.id.tvRefusedRefund)
    TextView tvRefusedRefund;//拒绝退款
    @BindView(R.id.tvReturnSend)
    TextView tvReturnSend;//退货发货
    @BindView(R.id.tvBackOutApply)
    TextView tvBackOutApply;//撤销申请
    @BindView(R.id.tvRefusedApply)
    TextView tvRefusedApply;//拒绝申请
    @BindView(R.id.tvConsentApply)
    TextView tvConsentApply;//同意申请
    @BindView(R.id.tvSendGoods)
    TextView tvSendGoods;//发货
    @BindView(R.id.tvGoodsSignBill)
    TextView tvGoodsSignBill;//商品签单
    @BindView(R.id.tvConfirmReceipt)
    TextView tvConfirmReceipt;//确认收货
    @BindView(R.id.tvEvaluate)
    TextView tvEvaluate;//去评价
    @BindView(R.id.tvPay)
    TextView tvPay;//去支付
    @BindView(R.id.tvPaymentBtn)
    TextView tvPaymentBtn;//去支付

    @BindView(R.id.tvEditPrice)
    TextView tvEditPrice;//修改出价
    @BindView(R.id.tvPayDeposit)
    TextView tvPayDeposit;//去付定金

    private int type = 0;
    private String oid;
    private String aftersalesBn;

    private CancelOrderDialog cancelOrderDialog;
    private String tid;
    private ChooseExpressDialog chooseExpressDialog;
    private String aftersales_bn;
    private String cancel_id;
    private SellerCancelOrderDialog sellerCancelOrderDialog;
    private ChooseExpressSellerDialog chooseExpressSellerDialog;

    private List<PaymentBean.PaymentInfo> payList;
    private String payMehtodId;//支付方式类型
    private PayDialog payDialog;
    private IWXAPI wxapi;
    private static final int SDK_PAY_FLAG = 1;
    private ResultBean<CreateOrderBean> createOrderBeanResultBean;
    private String paymentId;
    private InputPriceDialog inputPriceDialog;
    private ArrayList<String> afterSalePicList;

    private RefundAfterSaleOrderDialog refundAfterSaleOrderDialog;
    private RefundAfterSaleOrderDialog refundCancelOrderDialog;
    private RefundAmountDialog refundAmountDialog;
    private CommonConfirmDialog confirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_detail);
        confirmDialog = new CommonConfirmDialog(this, "确认删除订单吗？");
        confirmDialog.setDialogStr("取消", "删除");
        initView();
        initData();
    }


    @Override
    public void setTitleView() {
        titleImgLeft.setOnClickListener(onClickListener);
        titleName.setText("订单详情");

        EventManager.getInstance().registerListener(onNotifyListener);
    }

    private void initView() {
        refundAmountDialog = new RefundAmountDialog(this);
        tvDelete.setOnClickListener(onClickListener);//删除订单
        tvServerInto.setOnClickListener(onClickListener);
        tvComplain.setOnClickListener(onClickListener);
        tvCancel.setOnClickListener(onClickListener);//取消订单
        tvRefund.setOnClickListener(onClickListener);//申请退款
        tvNotGoods.setOnClickListener(onClickListener);//无货
        tvConsentRefund.setOnClickListener(onClickListener);//同意退款
        tvRefusedRefund.setOnClickListener(onClickListener);//拒绝退款
        tvReturnSend.setOnClickListener(onClickListener);//退货发货
        tvBackOutApply.setOnClickListener(onClickListener);//撤销申请
        tvRefusedApply.setOnClickListener(onClickListener);//拒绝申请
        tvConsentApply.setOnClickListener(onClickListener);//同意申请
        tvSendGoods.setOnClickListener(onClickListener);//发货
        tvGoodsSignBill.setOnClickListener(onClickListener);//商品签单
        tvConfirmReceipt.setOnClickListener(onClickListener);//确认收货
        tvEvaluate.setOnClickListener(onClickListener);//去评价
        tvPay.setOnClickListener(onClickListener);//去支付
        tvPaymentBtn.setOnClickListener(onClickListener);
        tvEditPrice.setOnClickListener(onClickListener);//修改出价
        tvPayDeposit.setOnClickListener(onClickListener);//去付定金

        countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                initData();
            }
        });
    }

    private void hideOrderBtn() {
        tvComplain.setVisibility(View.GONE);
        tvDelete.setVisibility(View.GONE);//删除订单
        tvCancel.setVisibility(View.GONE);//取消订单
        tvRefund.setVisibility(View.GONE);//申请退款
        tvNotGoods.setVisibility(View.GONE);//无货
        tvConsentRefund.setVisibility(View.GONE);//同意退款
        tvRefusedRefund.setVisibility(View.GONE);//拒绝退款
        tvReturnSend.setVisibility(View.GONE);//退货发货
        tvBackOutApply.setVisibility(View.GONE);//撤销申请
        tvRefusedApply.setVisibility(View.GONE);//拒绝申请
        tvConsentApply.setVisibility(View.GONE);//同意申请
        tvSendGoods.setVisibility(View.GONE);//发货
        tvGoodsSignBill.setVisibility(View.GONE);//商品签单
        tvConfirmReceipt.setVisibility(View.GONE);//确认收货
        tvEvaluate.setVisibility(View.GONE);//去评价
        tvPay.setVisibility(View.GONE);//去支付
        tvPaymentBtn.setVisibility(View.GONE);
        tvEditPrice.setVisibility(View.GONE);//修改出价
        tvPayDeposit.setVisibility(View.GONE);//去付定金
    }

    private void initData() {
        tid = getIntent().getLongExtra(OrderParams.ORDERID, 0) + "";
        if (null == wxapi) {
            wxapi = WXAPIFactory.createWXAPI(this, null);
            wxapi.registerApp("wx0e1869b241d7234f");
            type = getIntent().getIntExtra(OrderParams.TYPE, 0);
            oid = String.valueOf(getIntent().getLongExtra(OrderParams.OID, 0));
            aftersalesBn = String.valueOf(getIntent().getLongExtra(OrderParams.AFTERSALESBN, 0));

            myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            disposable = new CompositeDisposable();
        }
        switch (type) {
            case 0:
                disposable.add(ApiUtils.getInstance().getOrderDetail(tid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResultBean<OrderDetailBean>>() {
                            @Override
                            public void accept(ResultBean<OrderDetailBean> orderDetailBeanResultBean) throws Exception {
                                if (0 == orderDetailBeanResultBean.getErrorcode()) {
                                    orderDetailBean = orderDetailBeanResultBean.getData();
                                    processData();
                                } else {
                                    if (TextUtils.isEmpty(orderDetailBeanResultBean.getMsg())) {
                                        ToastUtils.showShort("获取订单详情失败");
                                    } else {
                                        ToastUtils.showShort(orderDetailBeanResultBean.getMsg());
                                    }
                                }
                                rlLoading.setVisibility(View.GONE);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                if (TextUtils.isEmpty(throwable.getMessage())) {
                                    ToastUtils.showShort("获取订单详情失败");
                                } else {
                                    ToastUtils.showShort(throwable.getMessage());
                                }
                                rlLoading.setVisibility(View.GONE);
                                Log.e(OrderDetailActivity.class.getSimpleName(), throwable.toString());
                            }
                        }));
                break;
            case 1:
                disposable.add(ApiUtils.getInstance().getAuctionOrderDetail(String.valueOf(getIntent().getIntExtra(OrderParams.AUCTIONITEMID, 0)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResultBean<OrderDetailBean>>() {
                            @Override
                            public void accept(ResultBean<OrderDetailBean> orderDetailBeanResultBean) throws Exception {
                                if (0 == orderDetailBeanResultBean.getErrorcode()) {
                                    orderDetailBean = orderDetailBeanResultBean.getData();
                                    processData();
                                } else {
                                    if (TextUtils.isEmpty(orderDetailBeanResultBean.getMsg())) {
                                        ToastUtils.showShort("获取订单详情失败");
                                    } else {
                                        ToastUtils.showShort(orderDetailBeanResultBean.getMsg());
                                    }
                                }
                                rlLoading.setVisibility(View.GONE);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                if (TextUtils.isEmpty(throwable.getMessage())) {
                                    ToastUtils.showShort("获取订单详情失败");
                                } else {
                                    ToastUtils.showShort(throwable.getMessage());
                                }
                                rlLoading.setVisibility(View.GONE);

                                Log.e(OrderDetailActivity.class.getSimpleName(), throwable.toString());
                            }
                        }));
                break;
            case 2:
                disposable.add(ApiUtils.getInstance().getAfterSaleOrderDetail(aftersalesBn, oid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResultBean<OrderDetailBean>>() {
                            @Override
                            public void accept(ResultBean<OrderDetailBean> orderDetailBeanResultBean) throws Exception {
                                if (0 == orderDetailBeanResultBean.getErrorcode()) {
                                    orderDetailBean = orderDetailBeanResultBean.getData();
                                    processData();
                                } else {
                                    if (TextUtils.isEmpty(orderDetailBeanResultBean.getMsg())) {
                                        ToastUtils.showShort("获取订单详情失败");
                                    } else {
                                        ToastUtils.showShort(orderDetailBeanResultBean.getMsg());
                                    }
                                }
                                rlLoading.setVisibility(View.GONE);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                if (TextUtils.isEmpty(throwable.getMessage())) {
                                    ToastUtils.showShort("获取订单详情失败");
                                } else {
                                    ToastUtils.showShort(throwable.getMessage());
                                }
                                rlLoading.setVisibility(View.GONE);

                                Log.e(OrderDetailActivity.class.getSimpleName(), throwable.toString());
                            }
                        }));
                break;
            case 3:
                disposable.add(ApiUtils.getInstance().getSellerOrderDetail(String.valueOf(getIntent().getLongExtra(OrderParams.ORDERID, 0)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResultBean<SellerOrderDetailBean>>() {
                            @Override
                            public void accept(ResultBean<SellerOrderDetailBean> orderDetailBeanResultBean) throws Exception {
                                if (0 == orderDetailBeanResultBean.getErrorcode()) {
                                    BeanCopyUitl beanCopyUitl = new BeanCopyUitl();
                                    orderDetailBean = new OrderDetailBean();
                                    beanCopyUitl.copyPropertiesExclude(orderDetailBeanResultBean.getData(), orderDetailBean, new String[]{"order", "orders"});
                                    orderDetailBean.setOrders(orderDetailBeanResultBean.getData().getOrder());
                                    orderDetailBean.setLogi(orderDetailBeanResultBean.getData().getLogi());
                                    orderDetailBean.setInfo(orderDetailBeanResultBean.getData().getInfo());
                                    processData();
                                } else {
                                    if (TextUtils.isEmpty(orderDetailBeanResultBean.getMsg())) {
                                        ToastUtils.showShort("获取订单详情失败");
                                    } else {
                                        ToastUtils.showShort(orderDetailBeanResultBean.getMsg());
                                    }
                                }
                                rlLoading.setVisibility(View.GONE);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                if (TextUtils.isEmpty(throwable.getMessage())) {
                                    ToastUtils.showShort("获取订单详情失败");
                                } else {
                                    ToastUtils.showShort(throwable.getMessage());
                                }
                                rlLoading.setVisibility(View.GONE);

                                Log.e(OrderDetailActivity.class.getSimpleName(), throwable.toString());
                            }
                        }));
                break;
            case 4:
                disposable.add(ApiUtils.getInstance().getSellerAfterSaleOrderDetail(aftersalesBn, oid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResultBean<OrderDetailBean>>() {
                            @Override
                            public void accept(ResultBean<OrderDetailBean> orderDetailBeanResultBean) throws Exception {
                                if (0 == orderDetailBeanResultBean.getErrorcode()) {
                                    orderDetailBean = orderDetailBeanResultBean.getData();
                                    processData();
                                } else {
                                    if (TextUtils.isEmpty(orderDetailBeanResultBean.getMsg())) {
                                        ToastUtils.showShort("获取订单详情失败");
                                    } else {
                                        ToastUtils.showShort(orderDetailBeanResultBean.getMsg());
                                    }
                                }
                                rlLoading.setVisibility(View.GONE);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                if (TextUtils.isEmpty(throwable.getMessage())) {
                                    ToastUtils.showShort("获取订单详情失败");
                                } else {
                                    ToastUtils.showShort(throwable.getMessage());
                                }
                                rlLoading.setVisibility(View.GONE);

                                Log.e(OrderDetailActivity.class.getSimpleName(), throwable.toString());
                            }
                        }));
                break;
            case 5:
                disposable.add(ApiUtils.getInstance().getSellerAfterSaleCencelOrderDetail(String.valueOf(getIntent().getLongExtra(OrderParams.CANCELID, 0)))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResultBean<OrderDetailBean>>() {
                            @Override
                            public void accept(ResultBean<OrderDetailBean> orderDetailBeanResultBean) throws Exception {
                                if (0 == orderDetailBeanResultBean.getErrorcode()) {
                                    orderDetailBean = orderDetailBeanResultBean.getData();
                                    processData();
                                } else {
                                    if (TextUtils.isEmpty(orderDetailBeanResultBean.getMsg())) {
                                        ToastUtils.showShort("获取订单详情失败");
                                    } else {
                                        ToastUtils.showShort(orderDetailBeanResultBean.getMsg());
                                    }
                                }
                                rlLoading.setVisibility(View.GONE);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                if (TextUtils.isEmpty(throwable.getMessage())) {
                                    ToastUtils.showShort("获取订单详情失败");
                                } else {
                                    ToastUtils.showShort(throwable.getMessage());
                                }
                                rlLoading.setVisibility(View.GONE);
                                Log.e(OrderDetailActivity.class.getSimpleName(), throwable.toString());
                            }
                        }));
                break;
        }
    }

    private void processData() {
        GlideUtils.loadImageShop(getApplicationContext(), orderDetailBean.getShoplogo(), imgStore);
        tvStore.setText(orderDetailBean.getShopname());

        tvCopy.setOnClickListener(onClickListener);
        tvCreateTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getCreated_time())));

        if (TextUtils.isEmpty(orderDetailBean.getPay_name())) {
            llPayType.setVisibility(View.GONE);
        } else {
            llPayType.setVisibility(View.VISIBLE);
            tvPayType.setText(orderDetailBean.getPay_name());
        }


        hideOrderBtn();
        setNormalOrderUi();
        setAuctionOrder();
        setAfterSaleUi();

        OrderDetailBean.LogiBean logi = orderDetailBean.getLogi();
        if (null != logi) {
            tvLogiName.setText(logi.getLogi_name());
            tvLogiNo.setText(logi.getLogi_no());
            tvLogiNoCopy.setOnClickListener(onClickListener);
            llLogi.setVisibility(View.VISIBLE);
        }

        if (orderDetailBean.getReason() != null && !TextUtils.isEmpty(orderDetailBean.getReason().toString())) {
            tvApplyReason.setText(orderDetailBean.getReason().toString());
            llApplyReason.setVisibility(View.VISIBLE);
        } else {
            llApplyReason.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(orderDetailBean.getShop_explanation())) {
            tvRefusalCause.setText(orderDetailBean.getShop_explanation());
            llRefusalCause.setVisibility(View.VISIBLE);
        } else {
            llRefusalCause.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(orderDetailBean.getInfo())) {
            llInfo.setVisibility(View.VISIBLE);
            tvInfo.setText(orderDetailBean.getInfo());
        } else {
            llInfo.setVisibility(View.GONE);
        }


    }


    private void setAuctionOrder() {
        if (type == 1) {
            int close_time = orderDetailBean.getClose_time();
            switch (orderDetailBean.getAuction().getStatus()) {
                case OrderParams.AUCTION_WAIT_PAY:
                    llOrderTitleRightContent.setVisibility(View.VISIBLE);
                    tvRightContentDesc.setText("剩余支付时间：");
                    try {
                        int i = close_time * 1000;
                        countdownView.start(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    tvRightContent.setVisibility(View.GONE);
                    tvPayDeposit.setVisibility(View.VISIBLE);
                    llPayTime.setVisibility(View.GONE);
                    llSendTime.setVisibility(View.GONE);
                    llFinishTime.setVisibility(View.GONE);

                    break;
                case OrderParams.AUCTION_BIDDING:
                    tvRightContentDesc.setVisibility(View.GONE);
                    tvRightContent.setVisibility(View.GONE);
                    tvEditPrice.setVisibility(View.VISIBLE);
                    llOrderId.setVisibility(View.GONE);
                    countdownView.setVisibility(View.GONE);

                    llPayTime.setVisibility(View.GONE);
                    llSendTime.setVisibility(View.GONE);
                    llFinishTime.setVisibility(View.GONE);
                    break;
                case OrderParams.AUCTION_WON_BID:
                    if (OrderParams.WAIT_BUYER_PAY.equals(orderDetailBean.getTrade_ststus())) {
                        tvPaymentBtn.setVisibility(View.VISIBLE);
                        llOrderTitleRightContent.setVisibility(View.VISIBLE);
                        tvRightContentDesc.setText("剩余支付时间：");
                        try {
                            int i = close_time * 1000;
                            countdownView.start(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (OrderParams.WAIT_BUYER_CONFIRM_GOODS.equals(orderDetailBean.getTrade_ststus())) {
                        tvGoodsSignBill.setVisibility(View.VISIBLE);
                    } else if (OrderParams.WAIT_SELLER_SEND_GOODS.equals(orderDetailBean.getTrade_ststus())) {
                    } else if (OrderParams.TRADE_FINISHED.equals(orderDetailBean.getTrade_ststus())) {
                    }
                    ivAuctionBid.setImageResource(R.mipmap.bid_already);
                    ivAuctionBid.setVisibility(View.VISIBLE);
                    break;
                case OrderParams.AUCTION_OUTBID:
                    ivAuctionBid.setImageResource(R.mipmap.bid_not);
                    ivAuctionBid.setVisibility(View.VISIBLE);
                    break;
                default:
                    tvRightContentDesc.setVisibility(View.GONE);
                    tvRightContent.setVisibility(View.GONE);
                    llPayTime.setVisibility(View.GONE);
                    llSendTime.setVisibility(View.GONE);
                    llFinishTime.setVisibility(View.GONE);
                    countdownView.setVisibility(View.GONE);

                    break;
            }

            TextView tvPaymentTips = findViewById(R.id.tvPaymentTips);
            if (OrderParams.AUCTION_WAIT_PAY.equals(orderDetailBean.getAuction().getStatus())) {
                tvPaymentTips.setText("应付定金：");
            } else {
                tvPaymentTips.setText("已付定金：");
            }


            if (!TextUtils.isEmpty(orderDetailBean.getPayments().getPayment_id())) {
                tvOrderId.setText(orderDetailBean.getPayments().getPayment_id());
            } else {
                tvOrderId.setText(tid);
            }

            llAfterSaleTme.setVisibility(View.GONE);

            llProducts.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);
            View inflate = inflater.inflate(R.layout.order_adapter_list_product_item, null);
            ImageView imgProduct = inflate.findViewById(R.id.imgProduct);
            GlideUtils.loadImage(getApplicationContext(), orderDetailBean.getImage_default_id(), imgProduct);
            TextView tvProductName = inflate.findViewById(R.id.tvProductName);
            tvProductName.setText(orderDetailBean.getTitle());
            TextView tvProductPrice = inflate.findViewById(R.id.tvProductPrice);
            TextView tvProductNum = inflate.findViewById(R.id.tvProductNum);
            tvProductNum.setText("x1");

            if ("false".equalsIgnoreCase(orderDetailBean.getAuction().getAuction_status())) {
                tvProductPrice.setText("保密出价");
            } else if (!TextUtils.isEmpty(orderDetailBean.getCost_price())) {
                tvProductPrice.setText(String.format("¥%s", new BigDecimal(orderDetailBean.getCost_price()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
            }

            tvProductPrice.setVisibility(View.GONE);
            tvProductNum.setVisibility(View.GONE);
            TextView tvAuctionPrice = inflate.findViewById(R.id.tvAuctionPrice);
            String startPrice = "";
            if (!TextUtils.isEmpty(orderDetailBean.getAuction().getStarting_price())) {
                startPrice = String.format("起拍价：%s", String.format("¥%s", BigDecimalUtils.objToStr(orderDetailBean.getAuction().getStarting_price(), 2)));
            }

            tvProductNum.setGravity(Gravity.CENTER_VERTICAL);
            String maxPrice = "";
            if ("false".equalsIgnoreCase(orderDetailBean.getAuction().getAuction_status())) {
                maxPrice = "最高出价：保密出价";
            } else {
                maxPrice = CommonUtils.joinStr("最高出价：¥", BigDecimalUtils.objToStr(orderDetailBean.getAuction().getMax_price(), 2));
            }
            tvAuctionPrice.setText(startPrice + "\n" + maxPrice);
            tvAuctionPrice.setVisibility(View.VISIBLE);

            llProducts.addView(inflate);
            if (!TextUtils.isEmpty(orderDetailBean.getAuction().getStarting_price())) {
                tvTotalPrice.setText(String.format("¥%s", new BigDecimal(orderDetailBean.getAuction().getStarting_price()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
            }


            if ("false".equalsIgnoreCase(orderDetailBean.getAuction().getAuction_status())) {
                tvSendPrice.setText("保密出价");
            } else if (!TextUtils.isEmpty(orderDetailBean.getCost_price())) {
                tvSendPrice.setText(CommonUtils.joinStr("¥", orderDetailBean.getAuction().getMax_price()));
            }


            LinearLayout llTips3 = findViewById(R.id.llTips3);
            llTips3.setVisibility(View.VISIBLE);
            TextView tvContent3 = findViewById(R.id.tvContent3);
            tvContent3.setText(String.format("¥%s", new BigDecimal(orderDetailBean.getOriginal_bid()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
            OrderDetailBean.DefaultAddressBean default_address = orderDetailBean.getDefault_address();
            tvUserInfo.setText(String.format("%s\u3000%s", default_address.getName(), default_address.getMobile()));
            tvAddress.setText(String.format("%s%s", default_address.getArea(), default_address.getAddr()));
            TextView tvTips1 = findViewById(R.id.tvTips1);
            tvTips1.setText("商品起拍价：");
            TextView tvTips2 = findViewById(R.id.tvTips2);
            tvTips2.setText("当前最高出价：");
            tvPayment.setText(CommonUtils.joinStr("¥", BigDecimalUtils.objToStr(orderDetailBean.getAuction().getPledge(), 2)));

            tvOrderStatus.setText(orderDetailBean.getAuction().getStatus_desc());

            if (!TextUtils.isEmpty(orderDetailBean.getInfo())) {
                llInfo.setVisibility(View.VISIBLE);
                tvInfo.setText(orderDetailBean.getInfo());
            }

            if (null != orderDetailBean.getPayments()) {
                String payName = orderDetailBean.getPayments().getPay_name();
                String payed_time = orderDetailBean.getPayments().getPayed_time();

                if (payName == null || TextUtils.isEmpty(payName)) {
                    llPayType.setVisibility(View.GONE);
                } else {
                    llPayType.setVisibility(View.VISIBLE);
                    tvPayType.setText(payName.toString());
                }

                if (payed_time == null || TextUtils.isEmpty(payed_time.toString())) {
                    llPayTime.setVisibility(View.GONE);
                } else {
                    llPayTime.setVisibility(View.VISIBLE);
                    tvPayTime.setText(TimeUtils.millisToDate(String.valueOf(payed_time)));
                }

                tvCreateTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getPayments().getCreated_time())));

                if (orderDetailBean.getConsign_time() > 0) {
                    llSendTime.setVisibility(View.VISIBLE);
                    tvSendTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getConsign_time())));
                } else {
                    llSendTime.setVisibility(View.GONE);
                }

                if (orderDetailBean.getEnd_time() > 0) {
                    llFinishTime.setVisibility(View.VISIBLE);
                    tvFinishTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getEnd_time())));
                } else {
                    llFinishTime.setVisibility(View.GONE);
                }

            }
        }
    }

    /**
     * 正常订单UI
     */
    private void setNormalOrderUi() {
        if (type == 0 || type == 3 || type == 5) {
            String status = orderDetailBean.getStatus();
            switch (status) {
                case OrderParams.WAIT_BUYER_PAY://等待付款
                    if (0 == type) {
                        llOrderTitleRightContent.setVisibility(View.VISIBLE);
                        tvRightContentDesc.setText("剩余支付时间：");
                        int close_time = orderDetailBean.getClose_time();
                        try {
                            int i = close_time * 1000;
                            countdownView.start(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tvRightContent.setVisibility(View.GONE);
                        tvCancel.setVisibility(View.VISIBLE);
                        tvPay.setVisibility(View.VISIBLE);
                    } else {
                        tvRightContentDesc.setVisibility(View.GONE);
                        tvRightContent.setVisibility(View.GONE);
                    }
                    llPayTime.setVisibility(View.GONE);
                    llSendTime.setVisibility(View.GONE);
                    llFinishTime.setVisibility(View.GONE);
                    break;
                case OrderParams.WAIT_SELLER_SEND_GOODS://待发货
                    if (0 == type) {
                        if ("NO_APPLY_CANCEL".equals(orderDetailBean.getCancel_status())) {
                            tvCancel.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if ("NO_APPLY_CANCEL".equals(orderDetailBean.getCancel_status()) || "FAILS".equals(orderDetailBean.getCancel_status())) {
                            tvNotGoods.setVisibility(View.VISIBLE);
                            tvSendGoods.setVisibility(View.VISIBLE);
                        }
                    }
                    tvRightContentDesc.setVisibility(View.GONE);
                    tvRightContent.setVisibility(View.GONE);
                    tvPayTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getPay_time())));
                    llSendTime.setVisibility(View.GONE);
                    llFinishTime.setVisibility(View.GONE);
                    countdownView.setVisibility(View.GONE);
                    break;
                case OrderParams.WAIT_BUYER_CONFIRM_GOODS:
                    ;//等待确认收货
                    if (0 == type) {
                        tvGoodsSignBill.setVisibility(View.VISIBLE);
                    }
                    tvRightContentDesc.setVisibility(View.GONE);
                    tvRightContent.setVisibility(View.GONE);
                    tvPayTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getPay_time())));
                    tvSendTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getConsign_time())));
                    llFinishTime.setVisibility(View.GONE);
                    countdownView.setVisibility(View.GONE);
                    break;
                case OrderParams.TRADE_FINISHED://已完成
                    if (0 == type) {
                        if (!orderDetailBean.isIs_buyer_rate()) {
                            tvEvaluate.setVisibility(View.VISIBLE);
                        }
                        tvDelete.setVisibility(View.VISIBLE);
                    }
                    tvRightContentDesc.setVisibility(View.GONE);
                    tvRightContent.setVisibility(View.GONE);
                    countdownView.setVisibility(View.GONE);
                    tvPayTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getPay_time())));
                    tvSendTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getConsign_time())));
                    tvFinishTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getEnd_time())));
                    break;
                case OrderParams.TRADE_CLOSED_BY_SYSTEM://已关闭
                    llOrderTitleRightContent.setVisibility(View.VISIBLE);
                    if (0 == type) {
                        tvDelete.setVisibility(View.VISIBLE);
                    }
                    tvRightContentDesc.setText("取消原因：");
                    tvRightContent.setText(orderDetailBean.getCancel_reason());
                    llPayTime.setVisibility(View.GONE);
                    llSendTime.setVisibility(View.GONE);
                    llFinishTime.setVisibility(View.GONE);
                    countdownView.setVisibility(View.GONE);
                    break;
                case "WAIT_CHECK":
                    if ("WAIT_CHECK".equals(orderDetailBean.getStatus())) {
                        tvRefusedRefund.setVisibility(View.VISIBLE);
                        tvConsentRefund.setVisibility(View.VISIBLE);
                    }
                    tvRightContentDesc.setVisibility(View.GONE);
                    tvRightContent.setVisibility(View.GONE);
                    llPayTime.setVisibility(View.GONE);
                    llSendTime.setVisibility(View.GONE);
                    llFinishTime.setVisibility(View.GONE);
                    countdownView.setVisibility(View.GONE);
                    break;
            }


            llAfterSaleTme.setVisibility(View.GONE);
            LayoutInflater inflater = LayoutInflater.from(this);
            List<OrderDetailBean.OrdersBean> orders = orderDetailBean.getOrders();
            llProducts.removeAllViews();
            for (int i = 0; i <= orders.size() - 1; i++) {
                OrderDetailBean.OrdersBean orderBean = orders.get(i);
                View inflate = inflater.inflate(R.layout.order_adapter_list_product_item, null);
                ImageView imgProduct = inflate.findViewById(R.id.imgProduct);
                GlideUtils.loadImage(getApplicationContext(), orderBean.getPic_path(), imgProduct);
                TextView tvProductName = inflate.findViewById(R.id.tvProductName);
                tvProductName.setText(orderBean.getTitle());
                TextView tvProductPrice = inflate.findViewById(R.id.tvProductPrice);
                tvProductPrice.setText(String.format("¥%s", new BigDecimal(orderBean.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                TextView tvProductDesc = inflate.findViewById(R.id.tvProductDesc);
                tvProductDesc.setText(orderBean.getSpec_nature_info());
                switch (type) {
                    case 0:
                        switch (orderDetailBean.getStatus()) {
                            case OrderParams.TRADE_FINISHED:
                                if (TextUtils.isEmpty(orderBean.getAftersales_status()) && orderBean.isRefund_enabled()) {
                                    TextView tvAfterSale = inflate.findViewById(R.id.tvAfterSale);
                                    tvAfterSale.setTag(i);
                                    tvAfterSale.setOnClickListener(onClickListener);
                                    tvAfterSale.setVisibility(View.VISIBLE);
                                } else if ("SELLER_REFUSE_BUYER".equals(orderBean.getAftersales_status()) && "FINISHED".equals(orderBean.getComplaints_status())) {
                                    TextView tvAfterSale = inflate.findViewById(R.id.tvAfterSale);
                                    tvAfterSale.setTag(i);
                                    tvAfterSale.setOnClickListener(onClickListener);
                                    tvAfterSale.setVisibility(View.VISIBLE);
                                }
                                break;
                        }
                        break;
                }
                TextView tvProductNum = inflate.findViewById(R.id.tvProductNum);
                tvProductNum.setText(String.format("x%d", orderBean.getNum()));
                llProducts.addView(inflate);
            }
            tvPayment.setText(String.format("¥%s", new BigDecimal(orderDetailBean.getPayment()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
            tvPaymentTips.setText("实付金额：");


            OrderDetailBean.LogiBean logi = orderDetailBean.getLogi();
            if (null != logi) {
                tvLogiName.setText(logi.getLogi_name());
                tvLogiNo.setText(logi.getLogi_no());
                tvLogiNoCopy.setOnClickListener(onClickListener);
                llLogi.setVisibility(View.VISIBLE);
            }

            tvUserInfo.setText(String.format("%s\u3000%s", orderDetailBean.getReceiver_name(), orderDetailBean.getReceiver_mobile()));
            tvAddress.setText(String.format("%s%s%s%s", orderDetailBean.getReceiver_state(), orderDetailBean.getReceiver_city(), orderDetailBean.getReceiver_district(), orderDetailBean.getReceiver_address()));
            tvTotalPrice.setText(String.format("¥%s", new BigDecimal(orderDetailBean.getTotal_fee()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
            tvSendPrice.setText(String.format("¥%s", new BigDecimal(orderDetailBean.getPost_fee()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));

            if (type == 3) {
                llCommission.setVisibility(View.VISIBLE);
//                tvCommission.setText("¥" + BigDecimalUtils.objToStr(orderDetailBean.getDistribution_admin()));
                tvCommission.setText("¥" + BigDecimalUtils.objToStr(orderDetailBean.getCommission()));
            } else {
                llCommission.setVisibility(View.GONE);
            }

            if (type == 3) {
                llShopPayment.setVisibility(View.VISIBLE);
                tvShopPayment.setText("¥" + BigDecimalUtils.objToStr(orderDetailBean.getShop_payment()));
            } else {
                llShopPayment.setVisibility(View.GONE);
            }


            tvOrderStatus.setText(orderDetailBean.getStatus_desc());
            tvOrderId.setText(tid);

        }
    }

    /**
     * 设置售后订单UI
     */
    private void setAfterSaleUi() {
        if (type == 2 || type == 4) {
            tvRightContentDesc.setVisibility(View.GONE);
            tvRightContent.setVisibility(View.GONE);
            countdownView.setVisibility(View.GONE);
            llPayTime.setVisibility(View.GONE);
            llSendTime.setVisibility(View.GONE);
            llFinishTime.setVisibility(View.GONE);
            llUserInfo.setVisibility(View.GONE);
            llSendPrice.setVisibility(View.GONE);
            llTotalPrice.setVisibility(View.GONE);
            switch (orderDetailBean.getStatus()) {
                case "0":
                    llAfterSaleSendTime.setVisibility(View.GONE);
                    llAfterSalePayTime.setVisibility(View.GONE);
                    if (2 == type) {
                        tvBackOutApply.setVisibility(View.GONE);
                    } else {
                        tvRefusedApply.setVisibility(View.VISIBLE);
                        tvConsentApply.setVisibility(View.VISIBLE);
                    }
                    break;
                case "1":
                    llAfterSaleSendTime.setVisibility(View.GONE);
                    llAfterSalePayTime.setVisibility(View.GONE);
                    if (2 == type) {
                        if ("1".equals(orderDetailBean.getProgress())) {
                            tvBackOutApply.setVisibility(View.GONE);
                            tvReturnSend.setVisibility(View.VISIBLE);
                        } else {
                            tvBackOutApply.setVisibility(View.GONE);
                        }
                    } else {
                        if ("2".equals(orderDetailBean.getProgress())) {
                            tvConsentRefund.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                case "2":
                    llAfterSaleSendTime.setVisibility(View.VISIBLE);
                    llAfterSalePayTime.setVisibility(View.GONE);
                    break;
                case "3":
                    llAfterSaleSendTime.setVisibility(View.VISIBLE);
                    tvAfterSalePayTime.setVisibility(View.VISIBLE);
                    if (2 == type) {
                        tvDelete.setVisibility(View.GONE);
                        OrderDetailBean.OrdersBean orderBean = orderDetailBean.getOrder();
                        if ("SELLER_REFUSE_BUYER".equals(orderBean.getAftersales_status())) {
                            if ("NOT_COMPLAINTS".equals(orderBean.getComplaints_status())) {
                                tvComplain.setVisibility(View.VISIBLE);
                            }
                        }
//                        tvServerInto.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            llPayType.setVisibility(View.GONE);
            tvRightContentDesc.setVisibility(View.GONE);
            tvAfterSaleCreateTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getModified_time())));


            llAfterSaleRemark.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(orderDetailBean.getDescription())) {
                tvAfterSaleRemark.setText(orderDetailBean.getDescription());
            } else {
                tvAfterSaleRemark.setText("无");
            }
            //预防后台数据格式错误问题
            Object afterSalePicObj = orderDetailBean.getEvidence_pic();

            if(afterSalePicObj!=null){
                if(afterSalePicObj instanceof ArrayList){
                    afterSalePicList = (ArrayList)afterSalePicObj;
                }else if(afterSalePicObj.toString().length()>0){
                    String afterSalePic = afterSalePicObj.toString() ;
                    String[] afterSalePics = afterSalePic.split(",");
                    afterSalePicList = new ArrayList<>(Arrays.asList(afterSalePics));
                }
            }

            rlAfterSalePic.setVisibility(View.VISIBLE);
            if (afterSalePicList!=null && afterSalePicList.size()>0) {
                OrderAfterSalePicAdapter goodsAdapter = new OrderAfterSalePicAdapter(this, afterSalePicList);
                goodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        toLargeImage(position);
                    }
                });
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
                rvAfterSalePic.setLayoutManager(new LinearLayoutManager(this) {
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                });
                rvAfterSalePic.setNestedScrollingEnabled(false);
                rvAfterSalePic.setLayoutManager(gridLayoutManager);

                GridSpacingItemDecoration decoration2 = new GridSpacingItemDecoration(3, Utils.dp2px(this, 5), false);
                rvAfterSalePic.addItemDecoration(decoration2);
                rvAfterSalePic.setAdapter(goodsAdapter);
                rvAfterSalePic.setVisibility(View.VISIBLE);
                tvAfterSalePic.setText("");
            } else {
                tvAfterSalePic.setText("无");
                rvAfterSalePic.setVisibility(View.GONE);
            }


            llProducts.removeAllViews();
            LayoutInflater inflater = LayoutInflater.from(this);
            OrderDetailBean.OrdersBean order = orderDetailBean.getOrder();
            View inflate = inflater.inflate(R.layout.order_adapter_list_product_item, null);
            ImageView imgProduct = inflate.findViewById(R.id.imgProduct);
            GlideUtils.loadImage(getApplicationContext(), order.getPic_path(), imgProduct);
            TextView tvProductName = inflate.findViewById(R.id.tvProductName);
            tvProductName.setText(order.getTitle());
            TextView tvProductPrice = inflate.findViewById(R.id.tvProductPrice);
            tvProductPrice.setText(String.format("¥%s", new BigDecimal(order.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
            TextView tvProductDesc = inflate.findViewById(R.id.tvProductDesc);
            tvProductDesc.setText(order.getSpec_nature_info());
            TextView tvProductNum = inflate.findViewById(R.id.tvProductNum);
            tvProductNum.setText(String.format("x%d", order.getNum()));
            llProducts.addView(inflate);

            if (orderDetailBean.getRefunds() != null && BigDecimalUtils.objToBigDecimal(orderDetailBean.getRefunds().getRefund_money()).doubleValue() > 0) {
                tvPayment.setText(String.format("¥%s", BigDecimalUtils.objToStr(orderDetailBean.getRefunds().getRefund_money())));
            } else {
                tvPayment.setText(String.format("¥%s", BigDecimalUtils.objToStr(order.getPayment())));
            }

            tvPaymentTips.setText("退款金额：");

            tvUserInfo.setText(String.format("%s\u3000%s", orderDetailBean.getReceiver_name(), orderDetailBean.getReceiver_mobile()));
            tvAddress.setText(String.format("%s%s%s%s", orderDetailBean.getReceiver_state(), orderDetailBean.getReceiver_city(), orderDetailBean.getReceiver_district(), orderDetailBean.getReceiver_address()));
            if (!TextUtils.isEmpty(orderDetailBean.getTotal_fee())) {
                tvTotalPrice.setText(String.format("¥%s", new BigDecimal(orderDetailBean.getTotal_fee()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                llTotalPrice.setVisibility(View.VISIBLE);
            } else {
                llTotalPrice.setVisibility(View.GONE);
            }


            if (!TextUtils.isEmpty(orderDetailBean.getPost_fee())) {
                tvSendPrice.setText(String.format("¥%s", new BigDecimal(orderDetailBean.getPost_fee()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                llSendPrice.setVisibility(View.VISIBLE);
            } else {
                llSendPrice.setVisibility(View.GONE);
            }
            tvOrderStatus.setText(orderDetailBean.getStatus_desc());
            tvOrderId.setText(String.valueOf(tid));
            if (!TextUtils.isEmpty(orderDetailBean.getOrder().getPay_time())) {
                tvAfterSaleCreateTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getOrder().getPay_time())));
            }
            if (!TextUtils.isEmpty(orderDetailBean.getOrder().getConsign_time())) {
                tvAfterSaleSendTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getOrder().getConsign_time())));
            }
            if (!TextUtils.isEmpty(orderDetailBean.getOrder().getEnd_time())) {
                tvAfterSalePayTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getOrder().getEnd_time())));
            }
        }
    }


    private void toLargeImage(int position) {
        new PhotoPagerConfig.Builder(this)
                .setBigImageUrls(afterSalePicList)
                .setSavaImage(false)
                .setPosition(position)
//                        .setSaveImageLocalPath("这里是你想保存的图片地址")
                .build();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_title_left:
                    finish();
                    break;
                case R.id.tvLogiNoCopy:
                    copyLogiNo();
                    break;
                case R.id.tvCopy:
                    copy();
                    break;
                case R.id.tvAfterSale:
                    int position = Integer.parseInt(view.getTag().toString());
                    Intent intent = new Intent(OrderDetailActivity.this, OrderApplyForAfterSaleActivity.class);
                    intent.putExtra(OrderParams.PRODUCTS, orderDetailBean.getOrders().get(position));
                    intent.putExtra(OrderParams.ORDERID, String.valueOf(tid));
                    startActivity(intent);
                    break;
                case R.id.tvDelete://删除订单
                    //删除订单
//                    tid = String.valueOf(orderDetailBean.getTid());
                    delete();
                    break;
                case R.id.tvCancel://取消订单
                    if (type == 3) {
//                        tid = String.valueOf(orderDetailBean.getTid());
                        getSellerCancelReason();
                    } else {
//                        tid = String.valueOf(orderDetailBean.getTid());
                        getCancelReason();
                    }
                    break;
                case R.id.tvRefund://申请退款
                    break;
                case R.id.tvNotGoods://无货
//                    tid = String.valueOf(orderDetailBean.getTid());
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
                                            initData();
                                            EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
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
                                aftersales_bn = String.valueOf(orderDetailBean.getAftersales_bn());
                                disposable.add(ApiUtils.getInstance().applySellerAfterSale(aftersales_bn, "true", money, "")
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<ResultBean>() {
                                            @Override
                                            public void accept(ResultBean resultBean) throws Exception {
                                                hideLoadingDialog();
                                                if (0 == resultBean.getErrorcode()) {
                                                    ToastUtils.showShort("商品签单并同意退款成功");
                                                    initData();
                                                    EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
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
                    aftersales_bn = String.valueOf(orderDetailBean.getAftersales_bn());
                    getLogisticsList();
                    break;
                case R.id.tvBackOutApply://撤销申请
                    break;
                case R.id.tvRefusedApply://拒绝申请
                    if (type == 4) {
                        aftersales_bn = String.valueOf(orderDetailBean.getAftersales_bn());
                        if (null == refundAfterSaleOrderDialog) {
                            refundAfterSaleOrderDialog = new RefundAfterSaleOrderDialog(OrderDetailActivity.this);
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
                                                        initData();
                                                        EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
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
                            refundCancelOrderDialog = new RefundAfterSaleOrderDialog(OrderDetailActivity.this);
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
                                                        initData();
                                                        EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
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
                                        initData();
                                        EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
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
//                    tid = String.valueOf(orderDetailBean.getTid());
                    getSellerLogisticsList();
                    break;
                case R.id.tvGoodsSignBill://商品签单
//                    tid = String.valueOf(orderDetailBean.getTid());
                    confirmReceipt();
                    break;
                case R.id.tvConfirmReceipt://确认收货
//                    tid = String.valueOf(orderDetailBean.getTid());
                    confirmReceipt();
                    break;
                case R.id.tvEvaluate://去评价
                    intent = new Intent(OrderDetailActivity.this, OrderEvaluationDetailActivity.class);
                    OrderListBean.ListBean listBean = (OrderListBean.ListBean) getIntent().getSerializableExtra(OrderParams.PRODUCTS);
                    intent.putExtra(OrderParams.PRODUCTS, listBean);
                    startActivity(intent);
                    break;
                case R.id.tvPay://去支付
//                    tid = String.valueOf(orderDetailBean.getTid());
                    repay("true");
                    break;
                case R.id.tvPaymentBtn://去支付
//                    tid = String.valueOf(orderDetailBean.getTid());
                    repay("");
                    break;
                case R.id.tvEditPrice://修改出价
                    changeMyPrice();
                    break;
                case R.id.tvPayDeposit://去付定金
                    paymentId = orderDetailBean.getPaymentId();
                    getPayment();
                    break;
                case R.id.tvServerInto:
                    CommonUtils.callPhone("4007889550");
                    break;
                case R.id.tvComplain:
                    toOrderComplainActivity();
                    break;
            }
        }
    };

    private void changeMyPrice() {
        String max_price;
        if (!TextUtils.isEmpty(orderDetailBean.getAuction().getAuction_status())
                && "false".equalsIgnoreCase(orderDetailBean.getAuction().getAuction_status())) {
            max_price = "保密出价";
        } else {
            max_price = orderDetailBean.getAuction().getMax_price();
        }
        String original_bid = orderDetailBean.getAuction().getOriginal_bid();
        String moneyPrice = orderDetailBean.getPayments().getPrice();
        original_bid = BigDecimalUtils.objToBigDecimal(original_bid).doubleValue() > 0 ? original_bid : moneyPrice;
        if (inputPriceDialog == null) {
            inputPriceDialog = new InputPriceDialog(this, max_price, original_bid);
            inputPriceDialog.setCallBack(new InputPriceDialog.CallBack() {
                @Override
                public void editPrice(String price) {
                    editGivePrice(price);
                }
            });
        } else {
            inputPriceDialog.updatePrice(max_price, original_bid);
        }
        inputPriceDialog.show();
    }

    private void editGivePrice(String price) {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().auctionAddPrice(String.valueOf(orderDetailBean.getAuction().getAuctionitem_id()), price)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        if (0 == resultBean.getErrorcode()) {
                            ToastUtils.showShort("修改出价成功");
                            initData();
                            EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
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
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().repay(tid, merge)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<CreateOrderBean>>() {
                    @Override
                    public void accept(final ResultBean<CreateOrderBean> resultBean) throws Exception {
                        hideLoadingDialog();
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
                        hideLoadingDialog();
                        if (TextUtils.isEmpty(throwable.getMessage())) {
                            ToastUtils.showShort("支付失败");
                        } else {
                            ToastUtils.showShort(throwable.getMessage());
                        }
                    }
                }));
    }

    private void getPayment() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getPayment()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<PaymentBean>>() {
                    @Override
                    public void accept(ResultBean<PaymentBean> paymentBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        if (0 == paymentBeanResultBean.getErrorcode()) {
                            payList = paymentBeanResultBean.getData().getList();
                            if (payList == null || payList.size() == 0) {
                                ToastUtils.showShort("获取支付方式失败");
                            } else {
                                payDialog = new PayDialog(OrderDetailActivity.this, payList, orderDetailBean.getPayment());
                                payDialog.setCallBack(new PayDialog.CallBack() {
                                    @Override
                                    public void choicePayMethod(String payMethodId) {
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
                        hideLoadingDialog();
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
    private void confirmPayMode(final String payMethodId) {
        if (OrderParams.PAY_APP_DEPOSIT.equals(payMethodId)) {
            String payMoney = orderDetailBean.getPayment();
            if (TextUtils.isEmpty(payMoney)) {
                payMoney = orderDetailBean.getCost_price();
            }
            BalancePayDialog balancePayDialog = new BalancePayDialog(this, payMoney);
            balancePayDialog.setCallBack(new BalancePayDialog.CallBack() {
                @Override
                public void cancelPay() {
                }

                @Override
                public void confirmPay(String pwd) {
                    payDo(payMethodId, pwd);
                }
            });
            balancePayDialog.show();
        } else {
            payDo(payMethodId, "");
        }
    }

    private void payDo(String payMethodId, String payPwd) {
        showLoadingDialog();
        this.payMehtodId = payMethodId;
        disposable.add(ApiUtils.getInstance().payDo(paymentId, payMehtodId, payPwd)
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
                    Toast.makeText(OrderDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
                    EventManager.getInstance().notify(null, ConstantMsg.UPDATE_CART_LIST);
                    finish();
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Toast.makeText(OrderDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private void invokeZhifubaoPay(ResultBean data) {
        final String url = data.getUrl();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(OrderDetailActivity.this);
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
        PayResultBean payResultBean = (PayResultBean) data.getData();
        if (payResultBean != null && "success".equals(payResultBean.getStatus())) {
            Toast.makeText(OrderDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
            EventManager.getInstance().notify(null, ConstantMsg.UPDATE_CART_LIST);
            finish();
        }
    }

    private void invokeDaePay(ResultBean data) {

    }

    private void weixinPaySuccess(Object object, String eventTag) {
        if (eventTag.equals(ConstantMsg.WEIXIN_PAY_CALLBACK)) {
            int code = (int) object;
            if (code == 0) {
                //支付成功
                ToastUtils.showShort("支付成功");
                finish();
                EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
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

    private void getCancelReason() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getCancelReason()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<CancelReasonBean>>() {
                    @Override
                    public void accept(ResultBean<CancelReasonBean> cancelReasonBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        if (0 == cancelReasonBeanResultBean.getErrorcode()) {
                            List<String> list = cancelReasonBeanResultBean.getData().getList();
                            String s = String.valueOf(orderDetailBean.getTid());
                            if (null == cancelOrderDialog) {
                                cancelOrderDialog = new CancelOrderDialog(OrderDetailActivity.this, list, s);
                                cancelOrderDialog.setCancelCallBack(new CancelOrderDialog.CancelCallBack() {
                                    @Override
                                    public void cancelSuccess() {
                                        ToastUtils.showShort("取消订单成功");
                                        initData();
                                        EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
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
                                cancelOrderDialog.setData(list, s);
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
                        hideLoadingDialog();
                        if (TextUtils.isEmpty(throwable.getMessage())) {
                            ToastUtils.showShort("获取取消原因列表失败");
                        } else {
                            ToastUtils.showShort(throwable.getMessage());
                        }
                    }
                }));
    }

    private void confirmReceipt() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().confirmReceipt(tid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        if (0 == resultBean.getErrorcode()) {
                            ToastUtils.showShort("商品签单成功");
                            EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
                            finish();
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
                        hideLoadingDialog();
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
                showLoadingDialog();
                disposable.add(ApiUtils.getInstance().delete(tid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResultBean>() {
                            @Override
                            public void accept(ResultBean resultBean) throws Exception {
                                hideLoadingDialog();
                                if (0 == resultBean.getErrorcode()) {
                                    ToastUtils.showShort("删除订单成功");
                                    EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
                                    finish();
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
                                hideLoadingDialog();
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

    private void getLogisticsList() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getLogisticsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<LogisticsBean>>() {
                    @Override
                    public void accept(ResultBean<LogisticsBean> logisticsBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        if (0 == logisticsBeanResultBean.getErrorcode()) {
                            LogisticsBean data = logisticsBeanResultBean.getData();
                            if (null == chooseExpressDialog) {
                                chooseExpressDialog = new ChooseExpressDialog(OrderDetailActivity.this, data.getList(), aftersales_bn);
                                chooseExpressDialog.setCallBack(new ChooseExpressDialog.CallBack() {
                                    @Override
                                    public void sendExpressSuccess() {
                                        EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
                                        finish();
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
                        hideLoadingDialog();
                        if (TextUtils.isEmpty(throwable.getMessage())) {
                            ToastUtils.showShort("获取快递公司列表失败");
                        } else {
                            ToastUtils.showShort(throwable.getMessage());
                        }
                    }
                }));
    }

    private void getSellerCancelReason() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getSellerCancelReason()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<CancelReasonBean>>() {
                    @Override
                    public void accept(ResultBean<CancelReasonBean> cancelReasonBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        if (0 == cancelReasonBeanResultBean.getErrorcode()) {
                            List<String> list = cancelReasonBeanResultBean.getData().getList();
                            if (null == sellerCancelOrderDialog) {
                                sellerCancelOrderDialog = new SellerCancelOrderDialog(OrderDetailActivity.this, list, tid);
                                sellerCancelOrderDialog.setCancelCallBack(new SellerCancelOrderDialog.CancelCallBack() {
                                    @Override
                                    public void cancelSuccess() {
                                        finish();
                                        EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
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
                        hideLoadingDialog();
                        if (TextUtils.isEmpty(throwable.getMessage())) {
                            ToastUtils.showShort("获取取消原因列表失败");
                        } else {
                            ToastUtils.showShort(throwable.getMessage());
                        }
                    }
                }));
    }

    private void getSellerLogisticsList() {
        showLoadingDialog();
        HttpUtils.USER_TOKEN = true;
        disposable.add(ApiUtils.getInstance().getLogisticsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<LogisticsBean>>() {
                    @Override
                    public void accept(ResultBean<LogisticsBean> logisticsBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        HttpUtils.USER_TOKEN = false;
                        if (0 == logisticsBeanResultBean.getErrorcode()) {
                            LogisticsBean data = logisticsBeanResultBean.getData();
                            if (null == chooseExpressSellerDialog) {
                                chooseExpressSellerDialog = new ChooseExpressSellerDialog(OrderDetailActivity.this, data.getList(), tid);
                                chooseExpressSellerDialog.setCallBack(new ChooseExpressSellerDialog.CallBack() {
                                    @Override
                                    public void sendExpressSuccess() {
                                        finish();
                                        EventManager.getInstance().notify(null, OrderParams.REFRESH_ORDER_LIST);
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
                        if (TextUtils.isEmpty(throwable.getMessage())) {
                            ToastUtils.showShort("获取快递公司列表失败");
                        } else {
                            ToastUtils.showShort(throwable.getMessage());
                        }
                    }
                }));
    }


    private void toOrderComplainActivity() {
        Intent intent = new Intent(this, OrderComplainActivity.class);
        intent.putExtra(OrderParams.ORDERID, orderDetailBean.getTid());
        if (null != orderDetailBean.getOrder()) {
            intent.putExtra(OrderParams.OID, orderDetailBean.getOrder().getOid());
            intent.putExtra(OrderParams.AFTERSALESBN, orderDetailBean.getAftersales_bn());
        }
        startActivity(intent);
    }

    public void copy() {
        ClipData text = ClipData.newPlainText("chunLangOrderId", String.valueOf(orderDetailBean.getTid()));
        myClipboard.setPrimaryClip(text);
        ToastUtils.showShort("订单号已复制");
    }

    public void copyLogiNo() {
        ClipData text = ClipData.newPlainText("chunLangLogiNo", String.valueOf(orderDetailBean.getLogi().getLogi_no()));
        myClipboard.setPrimaryClip(text);
        ToastUtils.showShort("物流单号已复制");
    }
    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            switch (eventTag) {
                case OrderParams.REFRESH_ORDER_DETAIL:
                    initData();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        EventManager.getInstance().unRegisterListener(onNotifyListener);
    }

}
