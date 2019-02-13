package com.chunlangjiu.app.money.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.goods.bean.PaymentBean;
import com.chunlangjiu.app.goods.dialog.BalancePayDialog;
import com.chunlangjiu.app.money.bean.CreateRechargeOrderBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.order.params.OrderParams;
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

    @BindView(R.id.rbBalance)
    RadioButton rbBalance;
    @BindView(R.id.rbWx)
    RadioButton rbWx;
    @BindView(R.id.rbZfb)
    RadioButton rbZfb;
    @BindView(R.id.edtMoney)
    EditText edtMoney;

//    private PayType payType = PayType.Wx;
    private RechargeType rechargeType = RechargeType.Balance;
    private List<PaymentBean.PaymentInfo> payList;
    private String paymentId ;
    private String payMehtodId ;//支付方式类型

    private CompositeDisposable disposable;

    private BalancePayDialog balancePayDialog ;
    private IWXAPI wxapi;
//
//    enum PayType {
//        Wx, Alipay
//    }

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
//        togglePayType(PayType.Wx);
        initPay();
        initView();
        initData();
    }

    private void initView() {

        if (rechargeType==RechargeType.SecurityDeposit){
            String money = getIntent().getStringExtra(DepositMoney);
            edtMoney.setEnabled(false);
            edtMoney.setText(money);
            rbBalance.setVisibility(View.VISIBLE);
            balancePayDialog = new BalancePayDialog(this,money);
            balancePayDialog.setCallBack(new BalancePayDialog.CallBack() {
                @Override
                public void cancelPay() {

                }

                @Override
                public void confirmPay(String payPwd) {
                    createSuccess(paymentId,payPwd);
                }
            });
            togglePayType(OrderParams.PAY_APP_DEPOSIT);
        }else{
            togglePayType(OrderParams.PAY_APP_WXPAY);
            rbBalance.setVisibility(View.GONE);
        }
    }

    private void initPay() {
        wxapi = WXAPIFactory.createWXAPI(this, null);
        wxapi.registerApp("wx0e1869b241d7234f");
    }

    private void initData() {
//        getPaymentList();
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
                        paymentId = resultBean.getData().getPayment_id() ;
                        if(OrderParams.PAY_APP_DEPOSIT.equals(payMehtodId)){
                            if(balancePayDialog == null){
                                String money = getIntent().getStringExtra(DepositMoney);
                                balancePayDialog = new BalancePayDialog(ReChargeActivity.this,money);
                            }
                            balancePayDialog.show();
                        }else{
                            createSuccess(paymentId,"");
                        }
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
                        paymentId = resultBean.getData().getPayment_id() ;
                        createSuccess(paymentId,"");
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
                            payMehtodId = OrderParams.PAY_APP_WXPAY;
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                    }
                }));
    }

    private void createSuccess(String paymentId,String pwd) {
        if (!TextUtils.isEmpty(paymentId)) {
            disposable.add(ApiUtils.getInstance().payDo(paymentId, payMehtodId,pwd)
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
        switch (payMehtodId) {
            case OrderParams.PAY_APP_DEPOSIT:
                invokeBalancePay();
                break;
            case OrderParams.PAY_APP_WXPAY:
                invokeWeixinPay(resultBean);
                break;
            case OrderParams.PAY_APP_ALIPAY:
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

    private void invokeBalancePay() {
        Toast.makeText(ReChargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
        EventManager.getInstance().notify(null, ConstantMsg.RECHARGE);
        finish();
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
            titleName.setText(R.string.payment_deposit);
        }else {
            titleName.setText("充值");
        }
    }

    @OnClick({R.id.btnOk, R.id.rbBalance,R.id.rbWx, R.id.rbZfb, R.id.img_title_left})
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
            case R.id.rbBalance:
                payMehtodId = OrderParams.PAY_APP_DEPOSIT;
//                toggleBalanceSecurityDeposit(RechargeType.Balance);
                break;
//            case R.id.relSecurityDeposit:
//                toggleBalanceSecurityDeposit(RechargeType.SecurityDeposit);
//                break;
            case R.id.rbWx:
//                togglePayType(PayType.Wx);
                payMehtodId = OrderParams.PAY_APP_WXPAY;
                break;
            case R.id.rbZfb:
                payMehtodId = OrderParams.PAY_APP_ALIPAY;
//                togglePayType(PayType.Alipay);
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

//    public void togglePayType(PayType payType) {
//        this.payType = payType;
//        switch (payType) {
//            case Alipay:
//                payMehtodId = OrderParams.PAY_APP_ALIPAY;
//                rbZfb.setChecked(true);
//                break;
//            case Wx:
//                rbWx.setChecked(true);
//                payMehtodId = OrderParams.PAY_APP_WXPAY;
//                break;
//        }
//
//    }

    public void togglePayType(String payMethodId) {
        payMehtodId = OrderParams.PAY_APP_DEPOSIT;
        switch (payMethodId) {
            case OrderParams.PAY_APP_DEPOSIT:
                rbBalance.setChecked(true);
                break;
            case  OrderParams.PAY_APP_ALIPAY:
                rbZfb.setChecked(true);
                break;
            case  OrderParams.PAY_APP_WXPAY:
                rbWx.setChecked(true);
                break;
        }

    }

}
