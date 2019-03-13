package com.chunlangjiu.app.user.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.goodsmanage.activity.GoodsManageHomeActivity;
import com.chunlangjiu.app.user.activity.VerifiedActivity;
import com.chunlangjiu.app.user.bean.AuthStatusBean;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.SizeUtils;

/**
 * @CreatedbBy: liucun on 2018/7/30
 * @Describe:
 */
public class UserCheckAuthDialog extends Dialog {

    private Activity context;
    private ImageView ivCheckPic;
    private TextView tvTipsOne;
    private TextView tvTipsTwo;
    private TextView tvCancel;
    private TextView tvConfirm;
    private TextView tvTips;



    private String personStatus;
    private String companyStatus ;
    private String loginAccount ;



    public UserCheckAuthDialog(Activity context) {
        super(context, R.style.dialog_transparent);
        this.context = context;
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        initView();
    }


    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.user_check_auth, null);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width  = SizeUtils.getScreenWidth()-SizeUtils.dp2px(50);
        window.setAttributes(params);
        window.setGravity(Gravity.CENTER);
        setContentView(view);// 设置布局

        ivCheckPic = findViewById(R.id.ivCheckPic);
        tvTipsOne = findViewById(R.id.tvTipsOne);
        tvTipsTwo = findViewById(R.id.tvTipsTwo);
        tvCancel = findViewById(R.id.tvCancel);
        tvConfirm = findViewById(R.id.tvConfirm);
        tvTips = findViewById(R.id.tvTips);
        tvCancel.setOnClickListener(onClickListener);
        tvConfirm.setOnClickListener(onClickListener);
    }

    public void updateTips(String tips) {
        if (null != tvTips) {
            tvTips.setText(tips);
        }
    }


    public void updateAuthStatus(String personStatus, String companyStatus, String loginAccount) {
        this.personStatus = personStatus;
        this.companyStatus = companyStatus;
        this.loginAccount = loginAccount;

        if (!TextUtils.isEmpty(loginAccount)) {
            if ((AuthStatusBean.AUTH_SUCCESS.equals(personStatus) || AuthStatusBean.AUTH_SUCCESS.equals(companyStatus))) {
                String key = loginAccount + AuthStatusBean.AUTH_SUCCESS;
                boolean flag = (boolean) SPUtils.get(key, false);
                if (!flag) {
                    ivCheckPic.setImageResource(R.mipmap.pass);
                    tvTipsOne.setText(R.string.check_auth_pass_one);
                    tvTipsTwo.setText(R.string.check_auth_pass_two);
                    tvConfirm.setText("去发布商品");
                    show();
                }
                SPUtils.put(key, true);
            } else if ((AuthStatusBean.AUTH_FAILING.equals(personStatus) || AuthStatusBean.AUTH_FAILING.equals(companyStatus))) {
                String key = loginAccount + AuthStatusBean.AUTH_FAILING;
                boolean flag = (boolean) SPUtils.get(key, false);
                if (!flag) {
                    ivCheckPic.setImageResource(R.mipmap.warn);
                    tvTipsOne.setText(R.string.check_auth_fail_one);
                    tvTipsTwo.setText(R.string.check_auth_fail_two);
                    tvConfirm.setText("重新提交认证");
                    show();
                }
                SPUtils.put(key, true);
            }

        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.tvConfirm) {
                confirm();
            } else {
                cancelPay();
            }
        }
    };

    private void cancelPay() {
        if (callBack != null) {
            callBack.cancel();
        }
    }

    private void confirm() {
        if ((AuthStatusBean.AUTH_SUCCESS.equals(personStatus) || AuthStatusBean.AUTH_SUCCESS.equals(companyStatus))) {
            GoodsManageHomeActivity.startShopMainActivity(context);
        } else if ((AuthStatusBean.AUTH_FAILING.equals(personStatus) || AuthStatusBean.AUTH_FAILING.equals(companyStatus))) {
            context.startActivity(new Intent(context,VerifiedActivity.class));
        }
        if (callBack != null) {
            callBack.confirm();
        }
    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void cancel();

        void confirm();
    }
}
