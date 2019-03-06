package com.chunlangjiu.app.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/7/15.
 * @Describe:
 */
public class SetPayPasswordActivity extends BaseActivity {

    private TextView tvPayPhone ;
    private EditText etVerifyCode ;
    private EditText etPwd;
    private EditText etConfirmPwd ;
    private TextView  tvSendCode;
    private TextView tvCommit ;

    private CountDownTimer countDownTimer;
    private CompositeDisposable disposable;
    @Override
    public void setTitleView() {
        titleName.setText(R.string.edit_pay_pwd);
        titleImgLeft.setOnClickListener(onClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_set_pay_password);
        disposable = new CompositeDisposable();
        initView();
    }

    public static void startActivity(Activity activity){
        if(activity!=null){
            Intent intent = new Intent(activity,SetPayPasswordActivity.class);
            activity.startActivity(intent);
        }
    }

    private void initView(){
        tvPayPhone = findViewById(R.id.tvPayPhone);
        etVerifyCode = findViewById(R.id.etVerifyCode);
        etPwd = findViewById(R.id.etPwd);
        etConfirmPwd = findViewById(R.id.etConfirmPwd);
        tvSendCode = findViewById(R.id.tvSendCode);
        tvCommit = findViewById(R.id.tvCommit);
        tvSendCode.setOnClickListener(onClickListener);
        tvCommit.setOnClickListener(onClickListener);

        String phoneStr = SPUtils.get("account", "").toString();
        if(phoneStr.length()>7){
            phoneStr = phoneStr.substring(0,3)+"****"+phoneStr.substring(7);
        }
        tvPayPhone.setText(getString(R.string.pay_phone,phoneStr));
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int resId = view.getId();
            if(resId == R.id.tvSendCode){
                sendVerifyCode();
            }else if(resId == R.id.tvCommit){
                setPayPwd();
            }else if(resId == R.id.img_title_left){
                finish();
            }
        }
    };

    /**
     * 修改支付密码
     */
    private void setPayPwd() {
        tvCommit.setEnabled(false);
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().updatePayPassword(etPwd.getText().toString(), etConfirmPwd.getText().toString(),etVerifyCode.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        tvCommit.setEnabled(true);
                        ToastUtils.showShort("修改成功");
                        finish();
                        hideLoadingDialog();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        tvCommit.setEnabled(true);
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }

    /**
     * 发送验证码
     */
    private void sendVerifyCode() {
        countDownTime();
        tvSendCode.setEnabled(false);
        disposable.add(ApiUtils.getInstance().sendSmsCode(etPwd.getText().toString(), etConfirmPwd.getText().toString(),etVerifyCode.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        countDownTimer.cancel();
                        countDownTimer.onFinish();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }

    private void countDownTime() {
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millsTime) {
                if (millsTime / 1000 == 60) {
                    tvSendCode.setText("59s");
                } else {
                    tvSendCode.setText(millsTime / 1000 + "s");
                }
            }

            @Override
            public void onFinish() {
                tvSendCode.setText(R.string.reset_send);
                tvSendCode.setEnabled(true);
            }
        };
        countDownTimer.start();
        tvSendCode.setEnabled(false);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
