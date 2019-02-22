package com.chunlangjiu.app.goods.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.goods.bean.PaymentBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/7/30
 * @Describe:
 */
public class PayDialog extends Dialog {

    private static final int PAY_WEIXIN = 0;
    private static final int PAY_ZHIFUBAO = 1;
    private static final int PAY_WALLET = 2;
    private static final int PAY_LARGE = 3;

    RelativeLayout rlWeiXin;
    RelativeLayout rlZhiFuBao;
    RelativeLayout rlWallet;
    TextView tvWalletTips;
    RelativeLayout rlLarge;


    private Activity context;
    private int payMethod = PAY_WEIXIN;
    private List<PaymentBean.PaymentInfo> payList;
//    private List<ImageView> imageViewLists;
    private String weixinPayId;
    private String zhifubaoPayId;
    private String yuePayId;
    private String daePayId;

    private String payMoney;

    public PayDialog(Activity context, List<PaymentBean.PaymentInfo> payList, String payMoney) {
        super(context, R.style.dialog_transparent);
        this.context = context;
        this.payList = payList;
        this.payMoney = payMoney;
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.goods_dialog_pay_method, null);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        window.setAttributes(params);
        window.setGravity(Gravity.CENTER);
        setContentView(view);// 设置布局

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
//        ImageView imgChoiceWeixin = findViewById(R.id.imgChoiceWeixin);
//        ImageView imgChoiceZhifubao = findViewById(R.id.imgChoiceZhifubao);
//        ImageView imgChoiceWallet = findViewById(R.id.imgChoiceWallet);
//        ImageView imgChoiceLarge = findViewById(R.id.imgChoiceLarge);
//        imgChoiceWeixin.setVisibility(View.VISIBLE);
//        imageViewLists = new ArrayList<>();
//        imageViewLists.add(imgChoiceWeixin);
//        imageViewLists.add(imgChoiceZhifubao);
//        imageViewLists.add(imgChoiceWallet);
//        imageViewLists.add(imgChoiceLarge);

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
                    choicePay( yuePayId);
                    break;
                case R.id.rlLarge:
                    choicePay(daePayId);
                    break;
            }
        }
    };


    private void choicePay( String payMethodId) {
//        for (int i = 0; i < imageViewLists.size(); i++) {
//            if (payMethod == i) {
//                imageViewLists.get(i).setVisibility(View.VISIBLE);
//            } else {
//                imageViewLists.get(i).setVisibility(View.GONE);
//            }
//        }
        if (callBack != null) {
            callBack.choicePayMethod(payMethodId);
        }
        dismiss();
    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void choicePayMethod(String payMethodId);
    }
}
