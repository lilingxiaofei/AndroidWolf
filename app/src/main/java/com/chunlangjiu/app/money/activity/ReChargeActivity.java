package com.chunlangjiu.app.money.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
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
import com.chunlangjiu.app.money.bean.DepositBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.order.bean.PayResultBean;
import com.chunlangjiu.app.order.params.OrderParams;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.PayResult;
import com.pingplusplus.android.Pingpp;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

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
    public static final String DepositMoney = "DepositMoney";

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
    @BindView(R.id.rbYl)
    RadioButton rbYl;

    @BindView(R.id.edtMoney)
    EditText edtMoney;

    //    private PayType payType = PayType.Wx;
    private static RechargeType rechargeType = RechargeType.Balance;
    private List<PaymentBean.PaymentInfo> payList;
    private String paymentId;
    private String payMehtodId = OrderParams.PAY_PING_WXPAY;//支付方式类型

    private CompositeDisposable disposable;

    private BalancePayDialog balancePayDialog;
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
        if (rechargeType == null) {
            rechargeType = RechargeType.Balance;
        }
//        togglePayType(PayType.Wx);
        initPay();
        initView();
        initData();
        initEvent();
    }

    private void initEvent() {
        EventManager.getInstance().registerListener(onNotifyListener);
    }

    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            if (eventTag.equals(String.valueOf(RechargeType.Balance.ordinal()))) {
                EventManager.getInstance().notify(null, ConstantMsg.RECHARGE);
                finish();
            } else if (eventTag.equals(String.valueOf(RechargeType.SecurityDeposit.ordinal()))) {
                EventManager.getInstance().notify(null, ConstantMsg.DEPOSIT_CREATE);
                Intent intent = new Intent(ReChargeActivity.this, SecurityDepositManagerActivity.class);
                intent.putExtra(SecurityDepositManagerActivity.SECURITY_DEPOSIT_TYPE, SecurityDepositManagerActivity.PAYING_DEPOSIT);
                intent.putExtra(SecurityDepositManagerActivity.STATUS, SecurityDepositManagerActivity.PAY_SUCCESS);
                intent.putExtra(SecurityDepositManagerActivity.PAYMENT_MONEY, edtMoney.getText().toString());
                startActivity(intent);
                finish();

            }

        }

    };

    public static RechargeType getPayType() {
        return rechargeType;

    }

    private void initView() {

        if (rechargeType == RechargeType.SecurityDeposit) {
            String money = getIntent().getStringExtra(DepositMoney);
            setDepositMoney(money);
            edtMoney.setEnabled(false);
            rbBalance.setVisibility(View.VISIBLE);
            togglePayType(OrderParams.PAY_APP_DEPOSIT);
        } else {
            togglePayType(OrderParams.PAY_APP_WXPAY);
            rbBalance.setVisibility(View.GONE);
        }
    }

    private void setDepositMoney(String money) {
        if (!TextUtils.isEmpty(money)) {
            edtMoney.setText(money);
            balancePayDialog = new BalancePayDialog(this, money);
            balancePayDialog.setCallBack(new BalancePayDialog.CallBack() {
                @Override
                public void cancelPay() {

                }

                @Override
                public void confirmPay(String payPwd) {
                    createSuccess(paymentId, payPwd);
                }
            });
        } else {
            getDepositMoney();
        }
    }

    private void initPay() {
        wxapi = WXAPIFactory.createWXAPI(this, null);
        wxapi.registerApp("wx0e1869b241d7234f");
    }

    private void initData() {
//        getPaymentList();
    }

    private void getDepositMoney() {
        disposable.add(ApiUtils.getInstance().getDeposit((String) SPUtils.get("token", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<DepositBean>>() {
                    @Override
                    public void accept(ResultBean<DepositBean> resultBean) throws Exception {
                        DepositBean depositBean = resultBean.getData();
                        if (null != depositBean) {
                            setDepositMoney(depositBean.getDeposit());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != disposable) {
            disposable.dispose();
        }
        EventManager.getInstance().unRegisterListener(onNotifyListener);
    }

    private void createOrder(String count) {
        disposable.add(ApiUtils.getInstance().reCharge((String) SPUtils.get("token", ""), count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<CreateRechargeOrderBean>>() {
                    @Override
                    public void accept(ResultBean<CreateRechargeOrderBean> resultBean) throws Exception {
                        paymentId = resultBean.getData().getPayment_id();
                        createSuccess(paymentId, "");
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
                        paymentId = resultBean.getData().getPayment_id();
                        if (OrderParams.PAY_APP_DEPOSIT.equals(payMehtodId)) {
                            if (balancePayDialog == null) {
                                String money = getIntent().getStringExtra(DepositMoney);
                                balancePayDialog = new BalancePayDialog(ReChargeActivity.this, money);
                            }
                            balancePayDialog.show();
                        } else {
                            createSuccess(paymentId, "");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }

    private void createSuccess(String paymentId, String pwd) {
        if (!TextUtils.isEmpty(paymentId)) {
            if(OrderParams.PAY_PING_ALIPAY.equals(payMehtodId) || OrderParams.PAY_PING_WXPAY.equals(payMehtodId) || OrderParams.PAY_PING_UNIONPAY.equals(payMehtodId)){
                disposable.add(ApiUtils.getInstance().payDoPing(paymentId, payMehtodId, pwd)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Map>() {
                            @Override
                            public void accept(Map resultBean) throws Exception {
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
            }else{
                disposable.add(ApiUtils.getInstance().payDo(paymentId, payMehtodId, pwd)
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
    }

    private void invokePayPing(Map data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            Pingpp.createPayment(this, jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void invokePay(Object resultBean) {
        switch (payMehtodId) {
            case OrderParams.PAY_APP_DEPOSIT:
                invokeBalancePay((ResultBean)resultBean);
                break;
            case OrderParams.PAY_APP_WXPAY:
                invokeWeixinPay((ResultBean)resultBean);
                break;
            case OrderParams.PAY_APP_ALIPAY:
                invokeZhifubaoPay((ResultBean)resultBean);
                break;
            case OrderParams.PAY_PING_ALIPAY:
            case OrderParams.PAY_PING_WXPAY:
            case OrderParams.PAY_PING_UNIONPAY:
                invokePayPing((Map)resultBean);
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

    private void invokeBalancePay(ResultBean data) {
        PayResultBean payResultBean = (PayResultBean) data.getData();
        if (payResultBean != null && "success".equals(payResultBean.getStatus())) {
            paySuccess();
        } else {
            payFail();
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
                    Toast.makeText(ReChargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    paySuccess();

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
        if (rechargeType == RechargeType.SecurityDeposit) {
            titleName.setText(R.string.payment_deposit);
        } else {
            titleName.setText("充值");
        }
    }

    @OnClick({R.id.btnOk, R.id.rbBalance, R.id.rbWx, R.id.rbZfb,R.id.rbYl, R.id.img_title_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOk:
                String money = edtMoney.getText().toString().trim();
                if (rechargeType == RechargeType.Balance) {
                    if (TextUtils.isEmpty(money)) {
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
                payMehtodId = OrderParams.PAY_PING_WXPAY;
                break;
            case R.id.rbZfb:
                payMehtodId = OrderParams.PAY_PING_ALIPAY;
//                togglePayType(PayType.Alipay);
                break;
            case R.id.rbYl:
                payMehtodId = OrderParams.PAY_PING_UNIONPAY;
//                togglePayType(PayType.Alipay);
                break;

            case R.id.img_title_left:
                finish();
                break;
        }

    }

    private void paySuccess() {
        if (rechargeType == RechargeType.Balance) {
            EventManager.getInstance().notify(null, ConstantMsg.RECHARGE);
            finish();
        } else if (rechargeType == RechargeType.SecurityDeposit) {
            EventManager.getInstance().notify(null, ConstantMsg.DEPOSIT_CREATE);
            Intent intent = new Intent(ReChargeActivity.this, SecurityDepositManagerActivity.class);
            intent.putExtra(SecurityDepositManagerActivity.SECURITY_DEPOSIT_TYPE, SecurityDepositManagerActivity.PAYING_DEPOSIT);
            intent.putExtra(SecurityDepositManagerActivity.STATUS, SecurityDepositManagerActivity.PAY_SUCCESS);
            intent.putExtra(SecurityDepositManagerActivity.PAYMENT_MONEY, edtMoney.getText().toString());
            startActivity(intent);
            finish();
        }
    }


    private void payFail() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                if ("success".equals(result)) {
                    paySuccess();
                } else {
                    payFail();
                }
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
                Log.e("Ping++支付结果：", result + "==========" + errorMsg + "==========" + extraMsg);
            } else {
                payFail();
            }
        }
    }

    public void togglePayType(String payMethodId) {
        payMehtodId = payMethodId;
        switch (payMethodId) {
            case OrderParams.PAY_APP_DEPOSIT:
                rbBalance.setChecked(true);
                break;
            case OrderParams.PAY_APP_ALIPAY:
            case OrderParams.PAY_PING_ALIPAY:
                rbZfb.setChecked(true);
                break;
            case OrderParams.PAY_APP_WXPAY:
            case OrderParams.PAY_PING_WXPAY:
                rbWx.setChecked(true);
                break;
            case OrderParams.PAY_PING_UNIONPAY:
                rbYl.setChecked(true);
                break;
        }

    }

}
