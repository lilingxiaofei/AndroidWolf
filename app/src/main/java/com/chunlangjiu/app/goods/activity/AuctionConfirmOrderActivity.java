package com.chunlangjiu.app.goods.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.goods.bean.CreateAuctionBean;
import com.chunlangjiu.app.goods.bean.GoodsDetailBean;
import com.chunlangjiu.app.goods.bean.PaymentBean;
import com.chunlangjiu.app.goods.dialog.BalancePayDialog;
import com.chunlangjiu.app.goods.dialog.PayDialog;
import com.chunlangjiu.app.goods.dialog.PayNewActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.order.activity.OrderMainNewActivity;
import com.chunlangjiu.app.order.bean.PayResultBean;
import com.chunlangjiu.app.order.params.OrderParams;
import com.chunlangjiu.app.user.activity.AddressListActivity;
import com.chunlangjiu.app.user.bean.AddressListBean;
import com.chunlangjiu.app.user.bean.AddressListDetailBean;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.PayResult;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/9/11
 * @Describe:
 */
public class AuctionConfirmOrderActivity extends BaseActivity {

    private static final int SDK_PAY_FLAG = 1;
    private static final int CHOICE_ADDRESS = 1000;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.rlNoAddress)
    RelativeLayout rlNoAddress;

    @BindView(R.id.rlHasAddress)
    RelativeLayout rlHasAddress;
    @BindView(R.id.tvConsignee)
    TextView tvConsignee;
    @BindView(R.id.tvAddressLabel)
    TextView tvAddressLabel;
    @BindView(R.id.tvAddressName)
    TextView tvAddressName;
    @BindView(R.id.tvAddressPhone)
    TextView tvAddressPhone;
    @BindView(R.id.tvAddressDetails)
    TextView tvAddressDetails;

    @BindView(R.id.tvGivePrice)
    TextView tvGivePrice;
    @BindView(R.id.tvPayPrice)
    TextView tvPayPrice;
    @BindView(R.id.tvPayPriceTips)
    TextView tvPayPriceTips;
    @BindView(R.id.llCommit)
    LinearLayout llCommit;

    private IWXAPI wxapi;

    private CompositeDisposable disposable;
    private PayDialog payDialog;
    private String payMehtodId;//支付方式类型
    private String payPwd;


    private GoodsDetailBean goodsDetailBean;
    private String addressId = "";
    private List<PaymentBean.PaymentInfo> payList;

    public static void startConfirmOrderActivity(Activity activity, GoodsDetailBean goodsDetailBean) {
        Intent intent = new Intent(activity, AuctionConfirmOrderActivity.class);
        intent.putExtra("goodsDetailBean", goodsDetailBean);
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
//                    showPayMethodDialog();
                    break;
                case R.id.llCommit:
                    checkData();
                    break;
            }
        }
    };


    @Override
    public void setTitleView() {
        titleName.setText("参拍交定金");
        titleImgLeft.setOnClickListener(onClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_activity_auction_confirm_order);
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
        llCommit.setOnClickListener(onClickListener);
        tvConsignee.setText("收货人");
        tvAddressLabel.setText("收货地址");
    }

    private void initData() {
        goodsDetailBean = (GoodsDetailBean) getIntent().getSerializableExtra("goodsDetailBean");


        tvGivePrice.setText(goodsDetailBean.getItem().getAuction().getPledge());
        tvPayPrice.setText(CommonUtils.getString(R.string.auction_deposit, goodsDetailBean.getItem().getAuction().getPledge()));
        String auction_deposit_tipsValue = getString(R.string.auction_deposit_tips_value);
        String auction_deposit_tips = CommonUtils.getString(R.string.auction_deposit_tips, auction_deposit_tipsValue);
        tvPayPriceTips.setText(CommonUtils.setSpecifiedTextsColor(auction_deposit_tips, auction_deposit_tipsValue, ContextCompat.getColor(this, R.color.t_red)));
//        if ("true".equals(goodsDetailBean.getItem().getAuction().getAuction_status())) {
//            //明拍
//            tvPriceHint.setHint(CommonUtils.joinStr("(目前最高出价为¥",goodsDetailBean.getItem().getAuction().getMax_price(),")"));
//        } else {
//            //暗拍
//            tvPriceHint.setHint("(暗拍商品，其他出价保密)");
//        }
        getDefaultAddress();
    }

    private void getDefaultAddress() {
        disposable.add(ApiUtils.getInstance().getAddressList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<AddressListBean>>() {
                    @Override
                    public void accept(ResultBean<AddressListBean> addressListBeanResultBean) throws Exception {
                        List<AddressListDetailBean> list = addressListBeanResultBean.getData().getList();
                        if (list != null) {
                            for (AddressListDetailBean item : list) {
                                if ("1".equals(item.getDef_addr())) {
                                    setAddressView(item);
                                }
                            }
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
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
//                            tvPayMethod.setText(payList.get(0).getApp_display_name());
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
                String payMoney = goodsDetailBean.getItem().getAuction().getPledge();
                payDialog = new PayDialog(this, payList, payMoney);
                payDialog.setCallBack(new PayDialog.CallBack() {
                    @Override
                    public void choicePayMethod(String payMethodId) {
                        AuctionConfirmOrderActivity.this.payMehtodId = payMethodId;
                        confirmPayMode();
                    }
                });
            }
            payDialog.show();
        }
    }

//    private void updatePayMethod(String payMethodId) {
//        this.payMehtodId = payMethodId;
//        switch (payMethodId) {
//            case OrderParams.PAY_APP_WXPAY:
//                tvPayMethod.setText("微信支付");
//                break;
//            case OrderParams.PAY_APP_ALIPAY:
//                tvPayMethod.setText("支付宝支付");
//                break;
//            case OrderParams.PAY_APP_DEPOSIT:
//                tvPayMethod.setText("余额支付");
//                break;
//        }
//    }


    private void startAddressListActivity() {
        Intent intent = new Intent(this, AddressListActivity.class);
        intent.putExtra("selectAddressId", addressId);
        intent.putExtra("isSelect", true);
        startActivityForResult(intent, CHOICE_ADDRESS);
    }

    private void checkData() {
        if (TextUtils.isEmpty(addressId)) {
            ToastUtils.showShort("请选择地址");
        }
        else {
            commitOrder();
//            showPayMethodDialog();
        }
    }

    private void confirmPayMode() {
        if (OrderParams.PAY_APP_DEPOSIT.equals(payMehtodId)) {
            String payMoney = goodsDetailBean.getItem().getAuction().getPledge();
            BalancePayDialog balancePayDialog = new BalancePayDialog(this, payMoney);
            balancePayDialog.setCallBack(new BalancePayDialog.CallBack() {
                @Override
                public void cancelPay() {
                }

                @Override
                public void confirmPay(String pwd) {
                    payPwd = pwd;
                    commitOrder();
                }
            });
            balancePayDialog.show();
        } else {
            this.payPwd = "";
            commitOrder();
        }
    }

    private void commitOrder() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().createAuctionOrder(goodsDetailBean.getItem().getAuction().getAuctionitem_id(),
                addressId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<CreateAuctionBean>>() {
                    @Override
                    public void accept(ResultBean<CreateAuctionBean> createAuctionBeanResultBean) throws Exception {
                        payMoney(createAuctionBeanResultBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }

    private void payMoney(CreateAuctionBean data) {
        PayNewActivity.startPayActivity(this,data.getPayment_id(),payList);
//        disposable.add(ApiUtils.getInstance().payDo(data.getPayment_id(), payMehtodId, payPwd)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<ResultBean>() {
//                    @Override
//                    public void accept(ResultBean resultBean) throws Exception {
//                        hideLoadingDialog();
//                        invokePay(resultBean);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        hideLoadingDialog();
//                        ToastUtils.showErrorMsg(throwable);
//                        toOrderMainActivity(false);
//                    }
//                }));
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
                    Toast.makeText(AuctionConfirmOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    toOrderMainActivity(true);
                    finish();
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Toast.makeText(AuctionConfirmOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    toOrderMainActivity(false);
                    finish();
                }

            }
        }
    };

    private void invokeZhifubaoPay(ResultBean data) {
        final String url = data.getUrl();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(AuctionConfirmOrderActivity.this);
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
            toOrderMainActivity(true);
        } else {
            toOrderMainActivity(false);
        }
    }

    private void invokeDaePay(ResultBean data) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == CHOICE_ADDRESS) {
                AddressListDetailBean addressListDetailBean =
                        (AddressListDetailBean) data.getSerializableExtra("addressListDetailBean");
                setAddressView(addressListDetailBean);
            }
        }
    }

    private void setAddressView(AddressListDetailBean addressListDetailBean) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        EventManager.getInstance().unRegisterListener(onNotifyListener);
    }

    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            if(ConstantMsg.PAY_SUCCESS.equals(eventTag)){
                toOrderMainActivity(true);
            }else if(ConstantMsg.PAY_FAIL.equals(eventTag)){
                toOrderMainActivity(false);
            }
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
            toOrderMainActivity(code == 0 ? true : false);
            finish();
        }
    }

    private void toOrderMainActivity(boolean isPaySuccess) {
        EventManager.getInstance().notify(null, ConstantMsg.UPDATE_CART_LIST);
        EventManager.getInstance().notify(null, ConstantMsg.AUCTION_ORDER_SUCCESS);
        Intent intent = new Intent(this, OrderMainNewActivity.class);
        intent.putExtra(OrderParams.TYPE, 1);
        intent.putExtra(OrderParams.TARGET, isPaySuccess ? 1 : 0);
        startActivity(intent);
        finish();
    }
}
