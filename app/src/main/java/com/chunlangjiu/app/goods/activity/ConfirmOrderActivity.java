package com.chunlangjiu.app.goods.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.goods.adapter.ConfirmOrderGoodsAdapter;
import com.chunlangjiu.app.goods.bean.ConfirmOrderBean;
import com.chunlangjiu.app.goods.bean.CreateOrderBean;
import com.chunlangjiu.app.goods.bean.MarkBean;
import com.chunlangjiu.app.goods.bean.OrderGoodsBean;
import com.chunlangjiu.app.goods.bean.PaymentBean;
import com.chunlangjiu.app.goods.bean.ShippingTypeBean;
import com.chunlangjiu.app.goods.dialog.BalancePayDialog;
import com.chunlangjiu.app.goods.dialog.PayDialog;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.order.activity.OrderMainNewActivity;
import com.chunlangjiu.app.order.bean.PayPingBean;
import com.chunlangjiu.app.order.params.OrderParams;
import com.chunlangjiu.app.user.activity.AddressListActivity;
import com.chunlangjiu.app.user.bean.AddressListDetailBean;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.PayResult;
import com.google.gson.Gson;
import com.pingplusplus.android.Pingpp;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.BigDecimalUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/7/6
 * @Describe:
 */
public class ConfirmOrderActivity extends BaseActivity {

    private static final int CHOICE_ADDRESS = 1000;
    private static final int SDK_PAY_FLAG = 1;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.rlNoAddress)
    RelativeLayout rlNoAddress;

    @BindView(R.id.rlHasAddress)
    RelativeLayout rlHasAddress;
    @BindView(R.id.tvAddressName)
    TextView tvAddressName;
    @BindView(R.id.tvAddressPhone)
    TextView tvAddressPhone;
    @BindView(R.id.tvAddressDetails)
    TextView tvAddressDetails;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.rlChoicePay)
    RelativeLayout rlChoicePay;
    @BindView(R.id.tvPayMethod)
    TextView tvPayMethod;
    @BindView(R.id.etRemark)
    EditText etRemark;

    @BindView(R.id.tvGoodsPrice)
    TextView tvGoodsPrice;
    @BindView(R.id.tvSendPrice)
    TextView tvSendPrice;
    @BindView(R.id.tvPayPrice)
    TextView tvPayPrice;
    @BindView(R.id.tvCommit)
    TextView tvCommit;

    private IWXAPI wxapi;

    private CompositeDisposable disposable;
    private List<OrderGoodsBean> lists;
    private ConfirmOrderGoodsAdapter orderGoodsAdapter;
    private PayDialog payDialog;
    private String payMehtodId;//支付方式类型

    private String mode;//是立即购买还是购物车购买
    private ConfirmOrderBean confirmOrderBean;
    private CreateOrderBean createOrderBean;
    private String addressId = "";
    private String sendPrice = "0.00";
    private String goodsPrice = "0.00";
    private String payPrice = "0.00";
    private List<PaymentBean.PaymentInfo> payList;

    public static void startConfirmOrderActivity(Activity activity, ConfirmOrderBean confirmOrderBean, String mode) {
        Intent intent = new Intent(activity, ConfirmOrderActivity.class);
        intent.putExtra("confirmOrderBean", confirmOrderBean);
        intent.putExtra("mode", mode);
        activity.startActivity(intent);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_title_left:
                    finish();
                    break;
                case R.id.rlNoAddress:
                    startAddressListActivity();
                    break;
                case R.id.rlHasAddress:
                    startAddressListActivity();
                    break;
                case R.id.rlChoicePay:
                    showPayMethodDialog();
                    break;
                case R.id.tvCommit:
                    confirmPayMode();
                    break;
            }
        }
    };


    @Override
    public void setTitleView() {
        titleName.setText("确认订单");
        titleImgLeft.setOnClickListener(onClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_activity_confirm_order);
        EventManager.getInstance().registerListener(onNotifyListener);
        initPay();
        initView();
        initData();
        getPaymentList();
    }

    private void initPay() {
        wxapi = WXAPIFactory.createWXAPI(this, null);
        wxapi.registerApp("wx0e1869b241d7234f");
    }

    private void initView() {
        disposable = new CompositeDisposable();
        rlNoAddress.setOnClickListener(onClickListener);
        rlHasAddress.setOnClickListener(onClickListener);
        rlChoicePay.setOnClickListener(onClickListener);
        tvCommit.setOnClickListener(onClickListener);


        lists = new ArrayList<>();
        orderGoodsAdapter = new ConfirmOrderGoodsAdapter(this, lists);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(orderGoodsAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void initData() {
        mode = getIntent().getStringExtra("mode");
        confirmOrderBean = (ConfirmOrderBean) getIntent().getSerializableExtra("confirmOrderBean");
        if (confirmOrderBean != null) {
            ConfirmOrderBean.Address default_address = confirmOrderBean.getDefault_address();
            if (default_address != null && !default_address.getAddr_id().isEmpty()) {
                addressId = default_address.getAddr_id();
                rlNoAddress.setVisibility(View.GONE);
                rlHasAddress.setVisibility(View.VISIBLE);
                tvAddressName.setText(default_address.getName());
                tvAddressPhone.setText(default_address.getMobile());
                tvAddressDetails.setText(default_address.getArea() + default_address.getAddr());
            } else {
                rlNoAddress.setVisibility(View.VISIBLE);
                rlHasAddress.setVisibility(View.GONE);
            }

            List<ConfirmOrderBean.CartData> resultCartData = confirmOrderBean.getCartInfo().getResultCartData();

            lists.clear();
            for (int i = 0; i < resultCartData.size(); i++) {
                OrderGoodsBean storeBean = new OrderGoodsBean();
                storeBean.setItemType(OrderGoodsBean.ITEM_STORE);
                storeBean.setStoreName(resultCartData.get(i).getShop_name());
                storeBean.setStoreLogo(resultCartData.get(i).getShop_logo());
                storeBean.setStoreId(resultCartData.get(i).getShop_id());
                lists.add(storeBean);

                List<ConfirmOrderBean.GoodsItem> items = resultCartData.get(i).getItems();
                for (int j = 0; j < items.size(); j++) {
                    OrderGoodsBean goodsBean = new OrderGoodsBean();
                    goodsBean.setItemType(OrderGoodsBean.ITEM_GOODS);
                    goodsBean.setGoodsId(items.get(j).getSku_id());
                    goodsBean.setGoodsPic(items.get(j).getImage_default_id());
                    goodsBean.setGoodsName(items.get(j).getTitle());
                    goodsBean.setGoodsPrice(BigDecimalUtils.add(items.get(j).getPrice().getPrice(), "0.00", 2));
                    goodsBean.setGoodsNum(items.get(j).getQuantity());
                    lists.add(goodsBean);
                }
            }
            orderGoodsAdapter.setNewData(lists);

            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(0, 0);
                }
            });

            goodsPrice = BigDecimalUtils.add(confirmOrderBean.getTotal().getAllCostFee(), "0.00", 2);
            tvGoodsPrice.setText("¥" + goodsPrice);

            String allPostfee = confirmOrderBean.getTotal().getAllPostfee();
            if (TextUtils.isEmpty(allPostfee)) {
                tvSendPrice.setText("¥0.00");
            } else {
                sendPrice = BigDecimalUtils.add(confirmOrderBean.getTotal().getAllPostfee(), "0.00", 2);
                tvSendPrice.setText("¥" + sendPrice);
            }

            payPrice = BigDecimalUtils.add(goodsPrice, sendPrice, 2);
            tvPayPrice.setText(payPrice);
        }
    }


    private void getPaymentList() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getPayment()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<PaymentBean>>() {
                    @Override
                    public void accept(ResultBean<PaymentBean> paymentBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        payList = paymentBeanResultBean.getData().getList();
                        if (payList != null & payList.size() > 0) {
                            tvPayMethod.setText(payList.get(0).getApp_display_name());
                            payMehtodId = payList.get(0).getApp_id();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                    }
                }));
    }


    private void showPayMethodDialog() {
        if (payList == null & payList.size() == 0) {
            ToastUtils.showShort("获取支付方式失败");
        } else {
            if (payDialog == null) {
                payDialog = new PayDialog(this, payList, payPrice);
                payDialog.setCallBack(new PayDialog.CallBack() {
                    @Override
                    public void choicePayMethod(String payMethodId) {
                        updatePayMethod(payMethodId);
                    }
                });
            }
            payDialog.show();
        }
    }

    private void updatePayMethod(String payMethodId) {
        this.payMehtodId = payMethodId;
        switch (payMehtodId) {
            case OrderParams.PAY_APP_WXPAY:
                tvPayMethod.setText("微信支付");
                break;
            case OrderParams.PAY_APP_ALIPAY:
                tvPayMethod.setText("支付宝支付");
                break;
            case OrderParams.PAY_APP_DEPOSIT:
                tvPayMethod.setText("余额支付");
                break;
        }
    }


    private void startAddressListActivity() {
        Intent intent = new Intent(this, AddressListActivity.class);
        intent.putExtra("selectAddressId", addressId);
        intent.putExtra("isSelect", true);
        startActivityForResult(intent, CHOICE_ADDRESS);
    }

    /**
     * 确认支付方式
     */
    private void confirmPayMode() {
        if (!TextUtils.isEmpty(addressId)) {
            String payMoned = tvPayMethod.getText().toString();
            if (payMoned.contains("余额")) {
                hideLoadingDialog();
                BalancePayDialog balancePayDialog = new BalancePayDialog(this, payPrice);
                balancePayDialog.setCallBack(new BalancePayDialog.CallBack() {
                    @Override
                    public void cancelPay() {

                    }

                    @Override
                    public void confirmPay(String pwd) {
                        showLoadingDialog();
//                        createSuccess();
                        createOrder(pwd);
                    }
                });
                balancePayDialog.show();
            } else {
                createOrder("");
//                createSuccess("");
            }
        } else {
            ToastUtils.showShort("请选择地址");
        }
    }

    private void createOrder(final String pwd) {
        if (!TextUtils.isEmpty(addressId)) {
            showLoadingDialog();
            List<ConfirmOrderBean.CartData> resultCartData = confirmOrderBean.getCartInfo().getResultCartData();
            List<ShippingTypeBean> shipLists = new ArrayList<>();
            List<MarkBean> markLists = new ArrayList<>();
            for (ConfirmOrderBean.CartData resultCartDatum : resultCartData) {
                shipLists.add(new ShippingTypeBean(resultCartDatum.getShop_id(), "express"));
                markLists.add(new MarkBean(resultCartDatum.getShop_id(), etRemark.getText().toString().trim()));
            }
            String shippingStr = new Gson().toJson(shipLists);
            String markStr;
            if (TextUtils.isEmpty(etRemark.getText().toString().trim())) {
                markStr = "";
            } else {
                markStr = new Gson().toJson(markLists);
            }
            disposable.add(ApiUtils.getInstance().createOrder(mode, confirmOrderBean.getMd5_cart_info(), addressId,
                    "online", shippingStr, markStr)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean<CreateOrderBean>>() {
                        @Override
                        public void accept(ResultBean<CreateOrderBean> resultBean) throws Exception {
                            createOrderBean = resultBean.getData();
                            createSuccess(pwd);
//                            confirmPayMode();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            hideLoadingDialog();
                            ToastUtils.showErrorMsg(throwable);
                        }
                    }));
        } else {
            ToastUtils.showShort("请选择地址");
        }
    }


    private void createSuccess(String payPwd) {
        if (null != createOrderBean) {
            disposable.add(ApiUtils.getInstance().payDo(createOrderBean.getPayment_id(), payMehtodId, payPwd)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean>() {
                        @Override
                        public void accept(ResultBean resultBean) throws Exception {
                            hideLoadingDialog();
                            invokePay(resultBean);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            toOrderMainActivity();
                            hideLoadingDialog();
                            ToastUtils.showErrorMsg(throwable);
                        }
                    }));

//            disposable.add(ApiUtils.getInstance().payPing(createOrderBean.getPayment_id(), payMehtodId, payPwd)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<ResultBean<PayPingBean>>() {
//                        @Override
//                        public void accept(ResultBean<PayPingBean> resultBean) throws Exception {
//                            hideLoadingDialog();
////                            invokePay(resultBean);
//                            invokePayPing(resultBean);
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                            toOrderMainActivity();
//                            hideLoadingDialog();
//                            ToastUtils.showErrorMsg(throwable);
//                        }
//                    }));
        }
    }


    private void invokePayPing(ResultBean<PayPingBean> data) {
        try {
            JSONObject jsonObject = new JSONObject(data.getData().getData());
            Pingpp.createPayment(ConfirmOrderActivity.this,jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    Toast.makeText(ConfirmOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Toast.makeText(ConfirmOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
                toOrderMainActivity();
            }
        }
    };

    private void invokeZhifubaoPay(ResultBean data) {
        final String url = data.getUrl();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(ConfirmOrderActivity.this);
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
        toOrderMainActivity();
    }

    private void invokeDaePay(ResultBean data) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == CHOICE_ADDRESS) {
                AddressListDetailBean addressListDetailBean =
                        (AddressListDetailBean) data.getSerializableExtra("addressListDetailBean");
                if (addressListDetailBean == null || TextUtils.isEmpty(addressListDetailBean.getAddr_id())) {
                    addressId = "";
                    rlNoAddress.setVisibility(View.VISIBLE);
                    rlHasAddress.setVisibility(View.GONE);
                } else {
                    addressId = addressListDetailBean.getAddr_id();
                    rlNoAddress.setVisibility(View.GONE);
                    rlHasAddress.setVisibility(View.VISIBLE);
                    tvAddressName.setText(addressListDetailBean.getName());
                    tvAddressPhone.setText(addressListDetailBean.getMobile());
                    String address = addressListDetailBean.getArea() + addressListDetailBean.getAddr();
                    tvAddressDetails.setText(address.replace("/", ""));
                }
            }
        }
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - 支付
                 * 成功
                 * "fail"    - 支付失败
                 * "cancel"  - 取消支付
                 * "invalid" - 支付插件未安装（一般是微信客户端未安装的情况）
                 * "unknown" - app进程异常被杀死(一般是低内存状态下,app进程被杀死)
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                ToastUtils.showShort(result+"=========="+ errorMsg+"=========="+ extraMsg);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        EventManager.getInstance().unRegisterListener(onNotifyListener);
    }

    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            weixinPaySuccess(object, eventTag);
        }
    };

    private void weixinPaySuccess(Object object, String eventTag) {
        if (eventTag.equals(ConstantMsg.WEIXIN_PAY_CALLBACK)) {
            int code = (int) object;
            if (code == 0) {
                //支付成功
                ToastUtils.showShort("支付成功");
            } else if (code == -1) {
                //支付错误
                ToastUtils.showShort("支付失败");
            } else if (code == -2) {
                //支付取消
                ToastUtils.showShort("支付取消");
            }
            toOrderMainActivity();
        }
    }

    private void toOrderMainActivity() {
        EventManager.getInstance().notify(null, ConstantMsg.UPDATE_CART_LIST);
        Intent intent = new Intent(this, OrderMainNewActivity.class);
        intent.putExtra(OrderParams.TYPE, 0);
        intent.putExtra(OrderParams.TARGET, 0);
        startActivity(intent);
        finish();
    }
}
