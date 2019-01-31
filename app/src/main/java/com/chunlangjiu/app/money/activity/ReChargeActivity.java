package com.chunlangjiu.app.money.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.goods.bean.PaymentBean;
import com.chunlangjiu.app.money.bean.CreateRechargeOrderBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.PayResult;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ReChargeActivity extends BaseActivity {
    private static final int SDK_PAY_FLAG = 1;
    public static final String ReChargeType = "ReChargeType";
    public static final String DepositMoney="DepositMoney";

//    @BindView(R.id.relBalance)
//    RelativeLayout relBalance;
//    @BindView(R.id.imgBalance)
//    ImageView imgBalance;
//    @BindView(R.id.relSecurityDeposit)
//    RelativeLayout relSecurityDeposit;
//    @BindView(R.id.imgSecurityDeposit)
//    ImageView imgSecurityDeposit;
    @BindView(R.id.relWx)
    RelativeLayout relWx;
    @BindView(R.id.imgWxCheck)
    ImageView imgWxCheck;
    @BindView(R.id.relZfb)
    RelativeLayout relZfb;
    @BindView(R.id.imgZfbCheck)
    ImageView imgZfbCheck;
    @BindView(R.id.edtMoney)
    EditText edtMoney;

    private PayType payType = PayType.Wx;
    private RechargeType rechargeType = RechargeType.Balance;
    private List<PaymentBean.PaymentInfo> payList;
    private String payMehtodId;//支付方式类型
    private int payMehtod;//默认微信支付

    private CompositeDisposable disposable;
    private IWXAPI wxapi;

    enum PayType {
        Wx, Alipay
    }

    public enum RechargeType {
        Balance, SecurityDeposit
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disposable = new CompositeDisposable();
        setContentView(R.layout.activity_re_charge);
        rechargeType = (RechargeType) getIntent().getSerializableExtra(ReChargeType);
        if (rechargeType==null){
            rechargeType=RechargeType.Balance;
        }
        togglePayType(PayType.Wx);
        initPay();
        initView();
        initData();
    }

    private void initView() {
        if (rechargeType==RechargeType.SecurityDeposit){
            edtMoney.setEnabled(false);
            edtMoney.setText(getIntent().getStringExtra(DepositMoney));
        }
    }

    private void initPay() {
        wxapi = WXAPIFactory.createWXAPI(this, null);
        wxapi.registerApp("wx0e1869b241d7234f");
    }

    private void initData() {
        getPaymentList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != disposable) {
            disposable.dispose();
        }
    }

    private void createOrder(String count) {
        disposable.add(ApiUtils.getInstance().reCharge((String) SPUtils.get("token", ""), count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<CreateRechargeOrderBean>>() {
                    @Override
                    public void accept(ResultBean<CreateRechargeOrderBean> resultBean) throws Exception {
                        createSuccess(resultBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));

    }

    private void createDespositOrder() {
        disposable.add(ApiUtils.getInstance().depositCreate((String) SPUtils.get("token", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<CreateRechargeOrderBean>>() {
                    @Override
                    public void accept(ResultBean<CreateRechargeOrderBean> resultBean) throws Exception {
                        createSuccess(resultBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showErrorMsg(throwable);
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

    private void createSuccess(CreateRechargeOrderBean data) {
        if (null != data) {
            disposable.add(ApiUtils.getInstance().payDo(data.getPayment_id(), payMehtodId,"")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean>() {
                        @Override
                        public void accept(ResultBean resultBean) throws Exception {
//                            hideLoadingDialog();
                            invokePay(resultBean);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            hideLoadingDialog();
                            ToastUtils.showErrorMsg(throwable);
                        }
                    }));

        }
    }

//    private void createSuccess2(CreateRechargeOrderBean data){
//        if (null!=data){
//            disposable.add(ApiUtils.getInstance().storedPay(data.getPayment_id(), payMehtodId,"wap","0.01",String.valueOf(payType.ordinal()))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<ResultBean>() {
//                        @Override
//                        public void accept(ResultBean resultBean) throws Exception {
////                            hideLoadingDialog();
//                            invokePay(resultBean);
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                            hideLoadingDialog();
//                            ToastUtils.showErrorMsg(throwable);
//                        }
//                    }));
//
//        }
//
//    }

    private void invokePay(ResultBean resultBean) {
        switch (payType) {
            case Wx:
                invokeWeixinPay(resultBean);
                break;
            case Alipay:
                invokeZhifubaoPay(resultBean);
                break;
        }

    }

    private void invokeZhifubaoPay(ResultBean data) {
        final String url = data.getUrl();
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(ReChargeActivity.this);
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
                    Toast.makeText(ReChargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    EventManager.getInstance().notify(null, ConstantMsg.RECHARGE);
                    finish();
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Toast.makeText(ReChargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
//                toOrderMainActivity(0,0);
            }
        }
    };

    @Override
    public void setTitleView() {
        rechargeType = (RechargeType) getIntent().getSerializableExtra(ReChargeType);
        if (rechargeType==RechargeType.SecurityDeposit){
            titleName.setText("交纳保证金");
        }else {
            titleName.setText("充值");
        }
    }

    @OnClick({R.id.btnOk, R.id.relWx, R.id.relZfb, R.id.img_title_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOk:
                String money = edtMoney.getText().toString().trim();
                if (rechargeType == RechargeType.Balance) {
                    if (TextUtils.isEmpty(money)){
                        ToastUtils.showShort("请输入充值金额");
                        return;
                    }
                    createOrder(money);
                } else {
                    //depositCreate
                    createDespositOrder();
                }
                break;
//            case R.id.relBalance:
//                toggleBalanceSecurityDeposit(RechargeType.Balance);
//                break;
//            case R.id.relSecurityDeposit:
//                toggleBalanceSecurityDeposit(RechargeType.SecurityDeposit);
//                break;
            case R.id.relWx:
                togglePayType(PayType.Wx);
                break;
            case R.id.relZfb:
                togglePayType(PayType.Alipay);
                break;
            case R.id.img_title_left:
                finish();
                break;
        }

    }
//
//    public void toggleBalanceSecurityDeposit(RechargeType rechargeType) {
//        this.rechargeType = rechargeType;
//        switch (rechargeType) {
//            case Balance:
//                imgBalance.setSelected(true);
//                imgSecurityDeposit.setSelected(false);
//                break;
//            case SecurityDeposit:
//                imgBalance.setSelected(false);
//                imgSecurityDeposit.setSelected(true);
//                break;
//        }
//
//    }

    public void togglePayType(PayType payType) {
        this.payType = payType;
        switch (payType) {
            case Alipay:
                imgZfbCheck.setSelected(true);
                imgWxCheck.setSelected(false);
                if (null != payList && payList.size() > 0) {
                    payMehtodId = payList.get(1).getApp_id();
                }
                break;
            case Wx:
                imgZfbCheck.setSelected(false);
                imgWxCheck.setSelected(true);
                if (null != payList && payList.size() > 0) {
                    payMehtodId = payList.get(0).getApp_id();
                }
                break;
        }

    }

}
