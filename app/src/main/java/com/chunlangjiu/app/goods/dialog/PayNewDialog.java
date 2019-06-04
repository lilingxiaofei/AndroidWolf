package com.chunlangjiu.app.goods.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
public class PayNewDialog extends Dialog {
    private static final int SDK_PAY_FLAG = 1;
    private static final int PAY_WEIXIN = 0;
    private static final int PAY_ZHIFUBAO = 1;
    private static final int PAY_WALLET = 2;
    private static final int PAY_LARGE = 3;

    View rootView;

    RelativeLayout rlWeiXin;
    RelativeLayout rlZhiFuBao;
    RelativeLayout rlWallet;
    TextView tvWalletTips;
    RelativeLayout rlLarge;


    private IWXAPI wxapi;

    private Activity context;
    private int payMethod = PAY_WEIXIN;
    private List<PaymentBean.PaymentInfo> payList;
    //    private List<ImageView> imageViewLists;
    private String weixinPayId;
    private String zhifubaoPayId;
    private String yuePayId;
    private String daePayId;
    private String payMethodId ;
    private String payMoney;


    private String paymentId ;//需要支付的交易号Id


    public PayNewDialog(Activity context,String paymentId, List<PaymentBean.PaymentInfo> payList) {
        super(context, R.style.dialog_transparent);
        this.context = context;
        this.payList = payList;
        this.payMoney = payMoney;
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        initView();
        initData();
        initPay();
    }

    private void initPay() {
        wxapi = WXAPIFactory.createWXAPI(context, null);
        wxapi.registerApp("wx0e1869b241d7234f");
    }


    private void initView() {
        rootView = LayoutInflater.from(context).inflate(R.layout.goods_dialog_pay_method, null);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        window.setAttributes(params);
        window.setGravity(Gravity.CENTER);
        setContentView(rootView);// 设置布局

        findViewById(R.id.imgClose).setOnClickListener(onClickListener);
        rlWeiXin = findViewById(R.id.rlWeiXin);
        rlZhiFuBao = findViewById(R.id.rlZhiFuBao);
        rlWallet = findViewById(R.id.rlWallet);
        tvWalletTips = findViewById(R.id.tvWalletTips);
        rlLarge = findViewById(R.id.rlLarge);
        rlWeiXin.setOnClickListener(onClickListener);
        rlZhiFuBao.setOnClickListener(onClickListener);
        rlWallet.setOnClickListener(onClickListener);
        rlLarge.setOnClickListener(onClickListener);
        rlWeiXin.setVisibility(View.GONE);
        rlZhiFuBao.setVisibility(View.GONE);
        rlWallet.setVisibility(View.GONE);
        rlLarge.setVisibility(View.GONE);
        uipdatePayList();
    }


    private void initData(){
        if(payList == null || payList.size()==0){
            getPaymentList();
        }
    }

    private void getPaymentList() {
        ApiUtils.getInstance().getPayment()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<PaymentBean>>() {
                    @Override
                    public void accept(ResultBean<PaymentBean> paymentBeanResultBean) throws Exception {
                        payList = paymentBeanResultBean.getData().getList();
                        uipdatePayList();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    private void uipdatePayList(){
        for (int i = 0; i < payList.size(); i++) {
            PaymentBean.PaymentInfo payInfo = payList.get(i);
            if (payInfo != null) {
                String displayName = payInfo.getApp_display_name();
                displayName = null != displayName ? displayName : "";
                if (displayName.contains("微信")) {
                    weixinPayId = payList.get(i).getApp_id();
                    rlWeiXin.setVisibility(View.VISIBLE);
                }
                if (displayName.contains("支付宝")) {
                    zhifubaoPayId = payList.get(i).getApp_id();
                    rlZhiFuBao.setVisibility(View.VISIBLE);
                }
                if (displayName.contains("余额")) {
                    yuePayId = payList.get(i).getApp_id();
                    rlWallet.setVisibility(View.VISIBLE);
                }
                if (displayName.contains("大额")) {
                    daePayId = payList.get(i).getApp_id();
                    rlLarge.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgClose:
                    dismiss();
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
            }
        }
    };


    private void choicePay(String payMethodId) {
        this.payMethodId = payMethodId ;
        confirmPayMode();
    }


    /**
     * 确认支付方式
     */
    private void confirmPayMode() {
        if (OrderParams.PAY_APP_DEPOSIT.equals(payMethodId)) {
            rootView.setVisibility(View.GONE);
            BalancePayDialog balancePayDialog = new BalancePayDialog(context, "");
            balancePayDialog.setCallBack(new BalancePayDialog.CallBack() {
                @Override
                public void cancelPay() {
                    rootView.setVisibility(View.VISIBLE);
                }

                @Override
                public void confirmPay(String pwd) {
                    rootView.setVisibility(View.VISIBLE);
                    payMoney(payMethodId, pwd);
                }
            });
            balancePayDialog.show();
        } else {
            payMoney(payMethodId, "");
        }
    }


    private void payMoney(final String payMethodId, String payPwd) {
//        ApiUtils.getInstance().payDo(paymentId, payMethodId, payPwd)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<ResultBean>() {
//                    @Override
//                    public void accept(ResultBean resultBean) throws Exception {
////                        hideLoadingDialog();
//                        invokePay(payMethodId, resultBean);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
////                        hideLoadingDialog();
//                        ToastUtils.showErrorMsg(throwable);
//                    }
//                });


                    ApiUtils.getInstance().payPing(paymentId, payMethodId, payPwd)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean<Map>>() {
                        @Override
                        public void accept(ResultBean<Map> resultBean) throws Exception {
//                            hideLoadingDialog();
//                            invokePay(resultBean);
                            invokePayPing(resultBean);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
//                            toOrderMainActivity();
//                            hideLoadingDialog();
                            ToastUtils.showErrorMsg(throwable);
                            payFail();
                        }
                    });
    }

    private void invokePayPing(ResultBean<Map> data) {
        try {
            JSONObject jsonObject = new JSONObject(data.getData());
            Pingpp.createPayment(context,jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void invokePay(String payMethodId, ResultBean data) {
        switch (payMethodId) {
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
                PayTask alipay = new PayTask(context);
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
                    Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                    paySuccess();
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
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
    public void show() {
        super.show();
        EventManager.getInstance().registerListener(onNotifyListener);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        EventManager.getInstance().unRegisterListener(onNotifyListener);
    }

    private void paySuccess(){
        if(callBack!=null){
            callBack.paySuccess();
        }
    }

    private void payFail(){
        if(callBack!=null){
            callBack.payFail();
        }
    }

    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
    }

    private PayCallBack callBack;

    public void setCallBack(PayCallBack callBack) {
        this.callBack = callBack;
    }

    public interface PayCallBack {
        void paySuccess();
        void payFail();
    }
}
