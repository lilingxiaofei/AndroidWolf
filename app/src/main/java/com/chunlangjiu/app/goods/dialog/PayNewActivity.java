package com.chunlangjiu.app.goods.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.goods.bean.PaymentBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.order.bean.PayResultBean;
import com.chunlangjiu.app.order.params.OrderParams;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.PayResult;
import com.pingplusplus.android.Pingpp;
import com.pkqup.commonlibrary.dialog.CommonLoadingDialog;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/7/30
 * @Describe:
 */
public class PayNewActivity extends Activity {
    private static final int SDK_PAY_FLAG = 1;

    RelativeLayout rlWeiXin;
    RelativeLayout rlZhiFuBao;
    RelativeLayout rlWallet;
    RelativeLayout rlUnionPay ;
    TextView tvWalletTips;
    RelativeLayout rlLarge;
    ImageView imgClose;
    private View rootView;

    private IWXAPI wxapi;

    private static String paymentId;//需要支付的交易号Id
    private static List<PaymentBean.PaymentInfo> payList;
    //    private List<ImageView> imageViewLists;
    private String weixinPayId;
    private String zhifubaoPayId;
    private String yuePayId;
    private String daePayId;
    private String upPayId;

    private String payMoney;
    private String payMethodId;

    public CommonLoadingDialog loadingDialog;


    public static void startPayActivity(Activity activity, String paymentIds, List<PaymentBean.PaymentInfo> payLists) {
        payList = payLists;
        paymentId = paymentIds;
        if (activity != null) {
            Intent intent = new Intent(activity, PayNewActivity.class);
            activity.startActivityForResult(intent,1010);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventManager.getInstance().registerListener(onNotifyListener);
        initView();
        initData();
        initPay();
        setFinishOnTouchOutside(false);
    }

//    public PayNewActivity(Activity context, String paymentId, List<PaymentBean.PaymentInfo> payList) {
//        super(context, R.style.dialog_transparent);
//        this.context = context;
//        this.payList = payList;
//        this.payMoney = payMoney;
//        setCancelable(true);
//        setCanceledOnTouchOutside(true);
//    }

    private void initPay() {
        wxapi = WXAPIFactory.createWXAPI(this, null);
        wxapi.registerApp("wx0e1869b241d7234f");
    }


    private void initView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.goods_dialog_pay_method, null);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        window.setAttributes(params);
        window.setGravity(Gravity.CENTER);
        setContentView(rootView);// 设置布局

        imgClose = findViewById(R.id.imgClose);
        rlWeiXin = findViewById(R.id.rlWeiXin);
        rlZhiFuBao = findViewById(R.id.rlZhiFuBao);
        rlWallet = findViewById(R.id.rlWallet);
        rlUnionPay = findViewById(R.id.rlUnionPay);
        tvWalletTips = findViewById(R.id.tvWalletTips);
        rlLarge = findViewById(R.id.rlLarge);
        rlWeiXin.setOnClickListener(onClickListener);
        rlZhiFuBao.setOnClickListener(onClickListener);
        rlWallet.setOnClickListener(onClickListener);
        rlUnionPay.setOnClickListener(onClickListener);
        rlLarge.setOnClickListener(onClickListener);
        imgClose.setOnClickListener(onClickListener);
        rlWeiXin.setVisibility(View.GONE);
        rlZhiFuBao.setVisibility(View.GONE);
        rlWallet.setVisibility(View.GONE);
        rlLarge.setVisibility(View.GONE);
        uipdatePayList();
    }


    private void initData() {
        if (payList == null || payList.size() == 0) {
            getPaymentList();
        }
    }

    private void getPaymentList() {
        showLoadingDialog();
        ApiUtils.getInstance().getPayment()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<PaymentBean>>() {
                    @Override
                    public void accept(ResultBean<PaymentBean> paymentBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        payList = paymentBeanResultBean.getData().getList();
                        initView();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        initView();
                    }
                });
    }

    private void uipdatePayList() {
        if (payList != null) {
            for (int i = 0; i < payList.size(); i++) {
                PaymentBean.PaymentInfo payInfo = payList.get(i);
                if (payInfo != null) {
                    String displayName = payInfo.getApp_display_name();
                    displayName = null != displayName ? displayName : "";
                    if (displayName.contains("微信")) {
                        weixinPayId = payInfo.getApp_id();
                        rlWeiXin.setVisibility(View.VISIBLE);
                    }
                    if (displayName.contains("支付宝")) {
                        zhifubaoPayId = payInfo.getApp_id();
                        rlZhiFuBao.setVisibility(View.VISIBLE);
                    }
                    if (displayName.contains("余额")) {
                        yuePayId = payInfo.getApp_id();
                        rlWallet.setVisibility(View.VISIBLE);
                    }
                    if (displayName.contains("银联")) {
                        upPayId = payInfo.getApp_id();
                        rlUnionPay.setVisibility(View.VISIBLE);
                    }
                    if (displayName.contains("大额")) {
                        daePayId = payInfo.getApp_id();
                        rlLarge.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgClose:
                    payFail();
                    finish();
                    break;
                case R.id.rlWeiXin:
                    choicePay(weixinPayId);
                    break;
                case R.id.rlZhiFuBao:
                    choicePay(zhifubaoPayId);
                    break;
                case R.id.rlWallet:
                    choicePay(yuePayId);
                    break;
                case R.id.rlLarge:
                    choicePay(daePayId);
                    break;
                case R.id.rlUnionPay:
                    choicePay(upPayId);
                    break;
            }
        }
    };


    private void choicePay(String payMethodId) {
        this.payMethodId = payMethodId;
        confirmPayMode();
    }


    /**
     * 确认支付方式
     */
    private void confirmPayMode() {
        rootView.setVisibility(View.GONE);
        if (OrderParams.PAY_APP_DEPOSIT.equals(payMethodId)) {
            BalancePayDialog balancePayDialog = new BalancePayDialog(this, "");
            balancePayDialog.setCallBack(new BalancePayDialog.CallBack() {
                @Override
                public void cancelPay() {
                    rootView.setVisibility(View.VISIBLE);
                }

                @Override
                public void confirmPay(String pwd) {
                    payMoney(payMethodId, pwd);
                }
            });
            balancePayDialog.show();
        } else {
            payMoney(payMethodId, "");
        }
    }


    private void payMoney(final String payMethodId, String payPwd) {
//        if(OrderParams.PAY_PING_ALIPAY.equals(payMethodId) || OrderParams.PAY_PING_WXPAY.equals(payMethodId) || OrderParams.PAY_PING_UNIONPAY.equals(payMethodId)) {
//            ApiUtils.getInstance().payDoPing(paymentId, payMethodId, payPwd)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<Map>() {
//                        @Override
//                        public void accept(Map resultBean) throws Exception {
//                            hideLoadingDialog();
//                            invokePay(payMethodId, resultBean);
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                            payFail();
//                            hideLoadingDialog();
//                            ToastUtils.showErrorMsg(throwable);
//                        }
//                    });
//        }else{
            ApiUtils.getInstance().payDo(paymentId, payMethodId, payPwd)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean>() {
                        @Override
                        public void accept(ResultBean resultBean) throws Exception {
                            hideLoadingDialog();
                            invokePay(payMethodId, resultBean);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            payFail();
                            hideLoadingDialog();
                            ToastUtils.showErrorMsg(throwable);
                        }
                    });
//        }



    }

    private void invokePayPing(Map data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            Pingpp.createPayment(this, jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void invokePay(String payMethodId, Object data) {
        try {
            switch (payMethodId) {
                case OrderParams.PAY_APP_WXPAY:
                    invokeWeixinPay((ResultBean)data);
                    break;
                case OrderParams.PAY_APP_ALIPAY:
                    invokeZhifubaoPay((ResultBean)data);
                    break;
                case OrderParams.PAY_APP_DEPOSIT:
                    invokeYuePay((ResultBean)data);
                    break;
                case OrderParams.PAY_PING_ALIPAY:
                case OrderParams.PAY_PING_WXPAY:
                case OrderParams.PAY_PING_UNIONPAY:
                    invokePayPing((Map) data);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void invokeYuePay(ResultBean data) {

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

    private void invokeZhifubaoPay(ResultBean data) {
        final String url = data.getUrl();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(PayNewActivity.this);
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
                    Toast.makeText(PayNewActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    paySuccess();
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Toast.makeText(PayNewActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    payFail();
                }
            }
        }
    };


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
                EventManager.getInstance().notify(null, ConstantMsg.UPDATE_CART_LIST);
                paySuccess();
            } else if (code == -1) {
                //支付错误
                ToastUtils.showShort("支付失败");
                payFail();
            } else if (code == -2) {
                //支付取消
                ToastUtils.showShort("支付取消");
                payFail();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventManager.getInstance().unRegisterListener(onNotifyListener);
    }


    private void paySuccess() {
        EventManager.getInstance().notify(paymentId, ConstantMsg.PAY_SUCCESS);
        setResult(ConstantMsg.PAY_SUCCESS_INT);
        finish();
    }


    private void payFail() {
        EventManager.getInstance().notify(paymentId, ConstantMsg.PAY_FAIL);
        setResult(ConstantMsg.PAY_FAIL_INT);
        finish();
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
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                Log.e("Ping++支付结果：", result + "==========" + errorMsg + "==========" + extraMsg);
            } else {
                payFail();
            }
        }
    }


    public void showLoadingDialog() {
        if (null == loadingDialog) {
            loadingDialog = new CommonLoadingDialog(this);
        }
        loadingDialog.show();
    }

    public void showLoadingDialog(String loadContent) {
        if (null == loadingDialog) {
            loadingDialog = new CommonLoadingDialog(this);
        }
        loadingDialog.setLoadingText(loadContent);
        loadingDialog.show();
    }

    public void hideLoadingDialog() {
        if (null != loadingDialog) {
            loadingDialog.dismiss();
        }
    }

//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
