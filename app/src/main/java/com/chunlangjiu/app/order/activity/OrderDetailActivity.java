package com.chunlangjiu.app.order.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.goods.bean.CreateOrderBean;
import com.chunlangjiu.app.goods.bean.PaymentBean;
import com.chunlangjiu.app.goods.dialog.BalancePayDialog;
import com.chunlangjiu.app.goods.dialog.InputPriceDialog;
import com.chunlangjiu.app.goods.dialog.PayDialog;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.order.bean.CancelReasonBean;
import com.chunlangjiu.app.order.bean.LogisticsBean;
import com.chunlangjiu.app.order.bean.OrderDetailBean;
import com.chunlangjiu.app.order.bean.OrderListBean;
import com.chunlangjiu.app.order.bean.SellerOrderDetailBean;
import com.chunlangjiu.app.order.dialog.CancelOrderDialog;
import com.chunlangjiu.app.order.dialog.ChooseExpressDialog;
import com.chunlangjiu.app.order.dialog.ChooseExpressSellerDialog;
import com.chunlangjiu.app.order.dialog.RefundAfterSaleOrderDialog;
import com.chunlangjiu.app.order.dialog.SellerCancelOrderDialog;
import com.chunlangjiu.app.order.params.OrderParams;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.PayResult;
import com.pkqup.commonlibrary.dialog.CommonConfirmDialog;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.HttpUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.BeanCopyUitl;
import com.pkqup.commonlibrary.util.TimeUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.pkqup.commonlibrary.view.countdownview.CountdownView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.math.BigDecimal;
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
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
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
    @BindView(R.id.tvChangePrice)
    TextView tvChangePrice;

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

    private RefundAfterSaleOrderDialog refundAfterSaleOrderDialog;
    private RefundAfterSaleOrderDialog refundCancelOrderDialog;
    private CommonConfirmDialog confirmDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_detail);
        confirmDialog = new CommonConfirmDialog(this, "确认删除订单吗？");
        confirmDialog.setDialogStr("取消", "删除");
        initData();
    }


    @Override
    public void setTitleView() {
        titleImgLeft.setOnClickListener(onClickListener);
        titleName.setText("订单详情");

        EventManager.getInstance().registerListener(onNotifyListener);
    }

    private void initData() {
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
                disposable.add(ApiUtils.getInstance().getOrderDetail(String.valueOf(getIntent().getLongExtra(OrderParams.ORDERID, 0)))
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
        tvCreateTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getCreated_time() + "000")));
        if (TextUtils.isEmpty(orderDetailBean.getPay_name())) {
            llPayType.setVisibility(View.GONE);
        } else {
            tvPayType.setText(orderDetailBean.getPay_name());
        }
        tv1.setOnClickListener(onClickListener);
        tv2.setOnClickListener(onClickListener);

        switch (type) {
            case 0:
            case 3:
            case 5:
                if (5 == type) {
                    switch (orderDetailBean.getStatus()) {
                        case "WAIT_CHECK":
                            tv1.setText("拒绝");
                            tv1.setVisibility(View.VISIBLE);
                            tv2.setText("同意退款");
                            tv2.setVisibility(View.VISIBLE);
                            break;
                        default:
                            tv1.setVisibility(View.GONE);
                            tv2.setVisibility(View.GONE);
                            break;
                    }
                    tvRightContentDesc.setVisibility(View.GONE);
                    tvRightContent.setVisibility(View.GONE);
                    llPayTime.setVisibility(View.GONE);
                    llSendTime.setVisibility(View.GONE);
                    llFinishTime.setVisibility(View.GONE);
                    countdownView.setVisibility(View.GONE);
                } else {
                    switch (orderDetailBean.getStatus()) {
                        case OrderParams.WAIT_BUYER_PAY:
                            if (0 == type) {
                                tvRightContentDesc.setText("剩余支付时间：");
                                int close_time = orderDetailBean.getClose_time();
                                try {
                                    int i = close_time * 1000;
                                    countdownView.start(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                tvRightContent.setVisibility(View.GONE);
                                tv1.setText("取消订单");
                                tv1.setVisibility(View.VISIBLE);
                                tv2.setText("去支付");
                                tv2.setVisibility(View.VISIBLE);
                            } else {
                                tvRightContentDesc.setVisibility(View.GONE);
                                tvRightContent.setVisibility(View.GONE);
                                tv1.setVisibility(View.GONE);
                                tv2.setVisibility(View.GONE);
                            }
                            llPayTime.setVisibility(View.GONE);
                            llSendTime.setVisibility(View.GONE);
                            llFinishTime.setVisibility(View.GONE);
                            break;
                        case OrderParams.TRADE_CLOSED_BY_SYSTEM:
                            if (0 == type) {
                                tv1.setText("");
                                tv2.setText("删除订单");
                                tv1.setVisibility(View.GONE);
                                tv2.setVisibility(View.VISIBLE);
                            } else {
                                tv1.setVisibility(View.GONE);
                                tv2.setVisibility(View.GONE);
                            }
                            tvRightContentDesc.setText("取消原因：");
                            tvRightContent.setText(orderDetailBean.getCancel_reason());
                            llPayTime.setVisibility(View.GONE);
                            llSendTime.setVisibility(View.GONE);
                            llFinishTime.setVisibility(View.GONE);
                            countdownView.setVisibility(View.GONE);
                            break;
                        case OrderParams.WAIT_SELLER_SEND_GOODS:
                            if (0 == type) {
                                tv1.setVisibility(View.GONE);
                                if ("NO_APPLY_CANCEL".equals(orderDetailBean.getCancel_status())) {
                                    tv2.setText("取消订单");
                                    tv2.setVisibility(View.VISIBLE);
                                } else {
                                    tv2.setVisibility(View.GONE);
                                }
                            } else {
                                if ("NO_APPLY_CANCEL".equals(orderDetailBean.getCancel_status()) || "FAILS".equals(orderDetailBean.getCancel_status())) {
                                    tv1.setText("无货");
                                    tv1.setVisibility(View.VISIBLE);
                                    tv2.setText("发货");
                                    tv2.setVisibility(View.VISIBLE);
                                } else {
                                    tv1.setVisibility(View.GONE);
                                    tv2.setVisibility(View.GONE);
                                }
                            }
                            tvRightContentDesc.setVisibility(View.GONE);
                            tvRightContent.setVisibility(View.GONE);
                            tvPayTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getPay_time() + "000")));
                            llSendTime.setVisibility(View.GONE);
                            llFinishTime.setVisibility(View.GONE);
                            countdownView.setVisibility(View.GONE);
                            break;
                        case OrderParams.WAIT_BUYER_CONFIRM_GOODS:
                            tv1.setVisibility(View.GONE);
                            if (0 == type) {
                                tv2.setText("商品签单");
                                tv2.setVisibility(View.VISIBLE);
                            } else {
                                tv2.setVisibility(View.GONE);
                            }
                            tvRightContentDesc.setVisibility(View.GONE);
                            tvRightContent.setVisibility(View.GONE);
                            tvPayTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getPay_time() + "000")));
                            tvSendTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getConsign_time() + "000")));
                            llFinishTime.setVisibility(View.GONE);
                            countdownView.setVisibility(View.GONE);
                            break;
                        case OrderParams.TRADE_FINISHED:
                            tv1.setVisibility(View.GONE);
                            if (0 == type) {
                                if (0 == orderDetailBean.getBuyer_rate()) {
                                    tv2.setText("评价");
                                    tv2.setVisibility(View.VISIBLE);
                                } else {
                                    tv2.setVisibility(View.GONE);
                                }
                            } else {
                                tv2.setVisibility(View.GONE);
                            }
                            tvRightContentDesc.setVisibility(View.GONE);
                            tvRightContent.setVisibility(View.GONE);
                            countdownView.setVisibility(View.GONE);
                            tvPayTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getPay_time() + "000")));
                            tvSendTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getConsign_time() + "000")));
                            tvFinishTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getEnd_time() + "000")));
                            break;
                    }
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

                String commission = orderDetailBean.getCommission();
                if (!TextUtils.isEmpty(commission) && new BigDecimal(commission).doubleValue() > 0) {
                    llCommission.setVisibility(View.VISIBLE);
                    tvCommission.setText(new BigDecimal(orderDetailBean.getTotal_fee()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                } else {
                    llCommission.setVisibility(View.GONE);
                }

                String shopPayment = orderDetailBean.getShop_payment();
                if (!TextUtils.isEmpty(shopPayment) && new BigDecimal(shopPayment).doubleValue() > 0) {
                    llShopPayment.setVisibility(View.VISIBLE);
                    tvShopPayment.setText(new BigDecimal(orderDetailBean.getShop_payment()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                } else {
                    llShopPayment.setVisibility(View.GONE);
                }


                tvOrderStatus.setText(orderDetailBean.getStatus_desc());
                tvOrderId.setText(String.valueOf(orderDetailBean.getTid()));

                if (!TextUtils.isEmpty(orderDetailBean.getInfo())) {
                    llInfo.setVisibility(View.VISIBLE);
                    tvInfo.setText(orderDetailBean.getInfo());
                }
                break;
            case 2:
            case 4:
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
                            tv1.setVisibility(View.GONE);
                            tv2.setText("撤销申请");
                            tv2.setVisibility(View.GONE);
                        } else {
                            tv1.setText("拒绝申请");
                            tv1.setVisibility(View.GONE);
                            tv2.setText("同意申请");
                            tv2.setVisibility(View.VISIBLE);
                        }
                        break;
                    case "1":
                        llAfterSaleSendTime.setVisibility(View.GONE);
                        llAfterSalePayTime.setVisibility(View.GONE);
                        if (2 == type) {
                            if ("1".equals(orderDetailBean.getProgress())) {
                                tv1.setText("撤销申请");
                                tv1.setVisibility(View.GONE);
                                tv2.setText("退货发货");
                                tv2.setVisibility(View.VISIBLE);
                            } else {
                                tv1.setVisibility(View.GONE);
                                tv2.setText("撤销申请");
                                tv2.setVisibility(View.GONE);
                            }
                        } else {
                            if ("2".equals(orderDetailBean.getProgress())) {
                                tv1.setVisibility(View.GONE);
                                tv2.setText("同意退款");
                                tv2.setVisibility(View.VISIBLE);
                            } else {
                                tv1.setVisibility(View.GONE);
                                tv2.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case "2":
                        llAfterSalePayTime.setVisibility(View.GONE);
                        tv1.setVisibility(View.GONE);
                        tv2.setVisibility(View.GONE);
                        break;
                    case "3":
                        if (2 == type) {
                            tv1.setVisibility(View.GONE);
                            tv2.setText("删除");
                            tv2.setVisibility(View.GONE);
                        } else {
                            tv1.setVisibility(View.GONE);
                            tv2.setVisibility(View.GONE);
                        }
                        break;
                }
                llPayType.setVisibility(View.GONE);
                tvRightContentDesc.setVisibility(View.GONE);
                tvAfterSaleCreateTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getModified_time() + "000")));

                llProducts.removeAllViews();
                inflater = LayoutInflater.from(this);
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
                tvPayment.setText(String.format("¥%s", new BigDecimal(order.getPayment()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                tvPaymentTips.setText("退款金额：");

                tvUserInfo.setText(String.format("%s\u3000%s", orderDetailBean.getReceiver_name(), orderDetailBean.getReceiver_mobile()));
                tvAddress.setText(String.format("%s%s%s%s", orderDetailBean.getReceiver_state(), orderDetailBean.getReceiver_city(), orderDetailBean.getReceiver_district(), orderDetailBean.getReceiver_address()));
                if (!TextUtils.isEmpty(orderDetailBean.getTotal_fee())) {
                    tvTotalPrice.setText(String.format("¥%s", new BigDecimal(orderDetailBean.getTotal_fee()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                    llTotalPrice.setVisibility(View.VISIBLE);
                } else {
                    llTotalPrice.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(orderDetailBean.getCommission())) {
                    llCommission.setVisibility(View.VISIBLE);
                    tvCommission.setText(new BigDecimal(orderDetailBean.getTotal_fee()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                } else {
                    llCommission.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(orderDetailBean.getShop_payment())) {
                    llShopPayment.setVisibility(View.VISIBLE);
                    tvShopPayment.setText(new BigDecimal(orderDetailBean.getShop_payment()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                } else {
                    llShopPayment.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(orderDetailBean.getPost_fee())) {
                    tvSendPrice.setText(String.format("¥%s", new BigDecimal(orderDetailBean.getPost_fee()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                    llSendPrice.setVisibility(View.VISIBLE);
                } else {
                    llSendPrice.setVisibility(View.GONE);
                }
                tvOrderStatus.setText(orderDetailBean.getStatus_desc());
                tvOrderId.setText(String.valueOf(orderDetailBean.getTid()));
                if (!TextUtils.isEmpty(orderDetailBean.getOrder().getPay_time())) {
                    tvAfterSaleCreateTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getOrder().getPay_time() + "000")));
                    llAfterSaleTme.setVisibility(View.VISIBLE);
                } else {
                    llAfterSaleTme.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(orderDetailBean.getOrder().getConsign_time())) {
                    tvAfterSaleSendTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getOrder().getConsign_time() + "000")));
                    llAfterSaleSendTime.setVisibility(View.VISIBLE);
                } else {
                    llAfterSaleSendTime.setVisibility(View.GONE);
                }
                if (!TextUtils.isEmpty(orderDetailBean.getOrder().getEnd_time())) {
                    tvAfterSalePayTime.setText(TimeUtils.millisToDate(String.valueOf(orderDetailBean.getOrder().getEnd_time() + "000")));
                    llAfterSalePayTime.setVisibility(View.VISIBLE);
                } else {
                    llAfterSalePayTime.setVisibility(View.GONE);
                }
                break;
            case 1:
                tv1.setVisibility(View.GONE);
                tvChangePrice.setOnClickListener(onClickListener);
                switch (orderDetailBean.getAuction().getStatus()) {
                    case "0":
                        tvRightContentDesc.setText("剩余支付时间：");
                        int close_time = orderDetailBean.getClose_time();
                        try {
                            int i = close_time * 1000;
                            countdownView.start(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tvRightContent.setVisibility(View.GONE);
                        tv2.setText("去付定金");
                        tv2.setVisibility(View.VISIBLE);
                        llPayTime.setVisibility(View.GONE);
                        llSendTime.setVisibility(View.GONE);
                        llFinishTime.setVisibility(View.GONE);
                        TextView tvPaymentTips = findViewById(R.id.tvPaymentTips);
                        tvPaymentTips.setText("应付定金：");
                        break;
                    case "1":
                        tvRightContentDesc.setVisibility(View.GONE);
                        tvRightContent.setVisibility(View.GONE);
                        tv2.setText("修改出价");
                        tv2.setVisibility(View.VISIBLE);
                        llOrderId.setVisibility(View.GONE);
                        countdownView.setVisibility(View.GONE);

                        llPayTime.setVisibility(View.GONE);
                        llSendTime.setVisibility(View.GONE);
                        llFinishTime.setVisibility(View.GONE);
                        tvPaymentTips = findViewById(R.id.tvPaymentTips);
                        tvPaymentTips.setText("已付定金：");
                        break;
//                    case "2":
//                        tvRightContentDesc.setText("剩余支付时间：");
//                        close_time = orderDetailBean.getClose_time();
//                        try {
//                            int i = close_time * 1000;
//                            countdownView.start(i);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        tvRightContent.setVisibility(View.GONE);
//                        tv2.setText("去支付");
//                        tv2.setVisibility(View.VISIBLE);
//                        llPayTime.setVisibility(View.GONE);
//                        llSendTime.setVisibility(View.GONE);
//                        llFinishTime.setVisibility(View.GONE);
//                        tvPaymentTips = findViewById(R.id.tvPaymentTips);
//                        tvPaymentTips.setText("应付定金：");
//                        tvChangePrice.setVisibility(View.GONE);
//                        break;
//                    case "3":
//                        break;
                    default:
                        tvRightContentDesc.setVisibility(View.GONE);
                        tvRightContent.setVisibility(View.GONE);
                        llPayTime.setVisibility(View.GONE);
                        llSendTime.setVisibility(View.GONE);
                        llFinishTime.setVisibility(View.GONE);
                        countdownView.setVisibility(View.GONE);
                        tvOrderId.setText(orderDetailBean.getPayments().getPayment_id());
                        tv2.setVisibility(View.GONE);
                        tvPaymentTips = findViewById(R.id.tvPaymentTips);
                        tvPaymentTips.setText("已付定金：");
                        tvChangePrice.setVisibility(View.GONE);
                        break;
                }
                llAfterSaleTme.setVisibility(View.GONE);

                llProducts.removeAllViews();
                inflater = LayoutInflater.from(this);
                inflate = inflater.inflate(R.layout.order_adapter_list_product_item, null);
                imgProduct = inflate.findViewById(R.id.imgProduct);
                GlideUtils.loadImage(getApplicationContext(), orderDetailBean.getImage_default_id(), imgProduct);
                tvProductName = inflate.findViewById(R.id.tvProductName);
                tvProductName.setText(orderDetailBean.getTitle());
                tvProductPrice = inflate.findViewById(R.id.tvProductPrice);
                if (!TextUtils.isEmpty(orderDetailBean.getCost_price())) {
                    tvProductPrice.setText(String.format("¥%s", new BigDecimal(orderDetailBean.getCost_price()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                }
                tvProductDesc = inflate.findViewById(R.id.tvProductDesc);
//                tvProductDesc.setText(orderDetailBean.getSpec_desc());
                tvProductNum = inflate.findViewById(R.id.tvProductNum);
                tvProductNum.setText("x1");
                llProducts.addView(inflate);

                if (!TextUtils.isEmpty(orderDetailBean.getAuction().getStarting_price())) {
                    tvTotalPrice.setText(String.format("¥%s", new BigDecimal(orderDetailBean.getAuction().getStarting_price()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                }

                if (!TextUtils.isEmpty(orderDetailBean.getCommission())) {
                    llCommission.setVisibility(View.VISIBLE);
                    tvCommission.setText(new BigDecimal(orderDetailBean.getTotal_fee()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                } else {
                    llCommission.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(orderDetailBean.getShop_payment())) {
                    llShopPayment.setVisibility(View.VISIBLE);
                    tvShopPayment.setText(new BigDecimal(orderDetailBean.getShop_payment()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                } else {
                    llShopPayment.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(orderDetailBean.getAuction().getMax_price())) {
                    tvSendPrice.setText(String.format("¥%s", new BigDecimal(orderDetailBean.getAuction().getMax_price()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
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
                tvPayment.setText(String.format("¥%s", new BigDecimal(orderDetailBean.getAuction().getPledge()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));

                tvOrderStatus.setText(orderDetailBean.getAuction().getStatus_desc());

                if (!TextUtils.isEmpty(orderDetailBean.getInfo())) {
                    llInfo.setVisibility(View.VISIBLE);
                    tvInfo.setText(orderDetailBean.getInfo());
                }
                break;
        }
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
                    intent.putExtra(OrderParams.ORDERID, String.valueOf(orderDetailBean.getTid()));
                    startActivity(intent);
                    break;
                case R.id.tv1:
                    switch (type) {
                        case 0:
                            switch (orderDetailBean.getStatus()) {
                                case OrderParams.WAIT_BUYER_PAY:
                                    tid = String.valueOf(orderDetailBean.getTid());
                                    getCancelReason();
                                    break;
                            }
                            break;
                        case 3:
                            tid = String.valueOf(orderDetailBean.getTid());
                            getSellerCancelReason();
                            break;
                        case 4:
                            switch (orderDetailBean.getStatus()) {
                                case "0":
                                    aftersales_bn = String.valueOf(orderDetailBean.getAftersales_bn());
                                    if (null == refundAfterSaleOrderDialog) {
                                        refundAfterSaleOrderDialog = new RefundAfterSaleOrderDialog(OrderDetailActivity.this);
                                        refundAfterSaleOrderDialog.setCallBack(new RefundAfterSaleOrderDialog.CallBack() {
                                            @Override
                                            public void confirm(String reason) {
                                                showLoadingDialog();
                                                disposable.add(ApiUtils.getInstance().applySellerAfterSale(aftersales_bn, "true", "", reason)
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
                                    break;
                            }
                            break;
                        case 5:
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
                            break;
                    }
                    break;
                case R.id.tv2:
                    switch (type) {
                        case 0:
                            switch (orderDetailBean.getStatus()) {
                                case OrderParams.WAIT_BUYER_PAY:
                                    tid = String.valueOf(orderDetailBean.getTid());
                                    repay();
                                    break;
                                case OrderParams.WAIT_BUYER_CONFIRM_GOODS:
                                    tid = String.valueOf(orderDetailBean.getTid());
                                    confirmReceipt();
                                    break;
                                case OrderParams.TRADE_FINISHED:
                                    intent = new Intent(OrderDetailActivity.this, OrderEvaluationDetailActivity.class);
                                    OrderListBean.ListBean listBean = (OrderListBean.ListBean) getIntent().getSerializableExtra(OrderParams.PRODUCTS);
                                    intent.putExtra(OrderParams.PRODUCTS, listBean);
                                    startActivity(intent);
                                    break;
                                case OrderParams.WAIT_SELLER_SEND_GOODS:
                                    tid = String.valueOf(orderDetailBean.getTid());
                                    getCancelReason();
                                    break;
                                case OrderParams.TRADE_CLOSED_BY_SYSTEM:
                                    //删除订单
                                    tid = String.valueOf(orderDetailBean.getTid());
                                    delete();
                                    break;
                            }
                            break;
                        case 1:
                            String status = orderDetailBean.getAuction().getStatus();
                            switch (status) {
                                case "0":
                                    paymentId = orderDetailBean.getPaymentId();
                                    getPayment();
                                    break;
                                case "1":
                                    changeMyPrice();
                                    break;
                                case "2":
                                    paymentId = orderDetailBean.getPaymentId();
                                    getPayment();
                                    break;
                            }
                            break;
                        case 2:
                            switch (orderDetailBean.getStatus()) {
                                case "1":
                                    aftersales_bn = String.valueOf(orderDetailBean.getAftersales_bn());
                                    getLogisticsList();
                                    break;
                            }
                            break;
                        case 3:
                            tid = String.valueOf(orderDetailBean.getTid());
                            getSellerLogisticsList();
                            break;
                        case 4:
                            switch (orderDetailBean.getStatus()) {
                                case "0":
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
                                case "1":
                                    if ("2".equals(orderDetailBean.getProgress())) {
                                        showLoadingDialog();
                                        aftersales_bn = String.valueOf(orderDetailBean.getAftersales_bn());
                                        disposable.add(ApiUtils.getInstance().applySellerAfterSale(aftersales_bn, "true",
                                                new BigDecimal(orderDetailBean.getPayment()).setScale(2, BigDecimal.ROUND_HALF_UP).toString(), "")
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
                                    break;
                            }
                            break;
                        case 5:
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
                    }
                    break;
                case R.id.tvChangePrice:
                    switch (type) {
                        case 1:
                            switch (orderDetailBean.getAuction().getStatus()) {
                                case "1":
                                    changeMyPrice();
                                    break;
                            }
                            break;
                    }
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

    private void repay() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().repay(tid, "true")
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
        if (data.getResult()) {
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
