package com.chunlangjiu.app.amain.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.abase.BaseApplication;
import com.chunlangjiu.app.amain.bean.LoginBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.web.WebViewActivity;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/7/6
 * @Describe: 登录页面
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etAuthCode)
    EditText etAuthCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvLicence)
    TextView tvLicence;

    @BindView(R.id.tvPsdLogin)
    TextView tvPsdLogin;

    private CompositeDisposable disposable;

    private CountDownTimer countDownTimer;


    public static void startLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_title_left:
                    finish();
                    break;
                case R.id.tvGetCode:
                    checkPhone();
                    break;
                case R.id.tvLogin:
                    checkSmsCode();
                    break;
                case R.id.tvLicence:
                    toLicence();
                    break;
                case R.id.tvPsdLogin:
                    startActivity(new Intent(LoginActivity.this, PasswordLoginActivity.class));
                    break;
            }
        }

    };


    @Override
    public void setTitleView() {
        titleName.setText("登录注册");
        titleImgLeft.setOnClickListener(onClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amain_activity_login);
        EventManager.getInstance().registerListener(onNotifyListener);
        initView();
    }

    private void initView() {
        disposable = new CompositeDisposable();
        tvGetCode.setOnClickListener(onClickListener);
        tvLogin.setOnClickListener(onClickListener);
        tvLicence.setOnClickListener(onClickListener);
        tvPsdLogin.setOnClickListener(onClickListener);
    }


    private void checkPhone() {
        if (etPhone.getText().toString().length() == 11) {
            getSmsCode();
            countDownTime();
        } else {
            ToastUtils.showShort("请输入正确的手机号码");
        }
    }

    //获取短信验证码
    private void getSmsCode() {
        disposable.add(ApiUtils.getInstance().getAuthSms(etPhone.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void countDownTime() {
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millsTime) {
                if (millsTime / 1000 == 60) {
                    tvGetCode.setText("59s");
                } else {
                    tvGetCode.setText(millsTime / 1000 + "s");
                }
            }

            @Override
            public void onFinish() {
                tvGetCode.setText("获取验证码");
                tvGetCode.setClickable(true);
            }
        };
        countDownTimer.start();
        tvGetCode.setClickable(false);
    }


    private void checkSmsCode() {
        if (!TextUtils.isEmpty(etAuthCode.getText().toString())) {
            login();
        } else {
            ToastUtils.showShort("请输入正确的验证码");
        }
    }

    private void login() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().login(etPhone.getText().toString(), etAuthCode.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<LoginBean>>() {
                    @Override
                    public void accept(ResultBean<LoginBean> loginBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showShort("登录成功");
                        SPUtils.put("token", loginBeanResultBean.getData().getAccessToken());
                        SPUtils.put("account", etPhone.getText().toString());
                        BaseApplication.setToken(loginBeanResultBean.getData().getAccessToken());
                        BaseApplication.initToken();
                        EventManager.getInstance().notify(null, ConstantMsg.LOGIN_SUCCESS);
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }


    private void toLicence() {
        WebViewActivity.startWebViewActivity(this, ConstantMsg.WEB_URL_LICENSE, "醇狼APP隐私政策");
    }

    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            loginSuccess(eventTag);
        }
    };

    //登录成功
    private void loginSuccess(String eventTag) {
        if (eventTag.equals(ConstantMsg.LOGIN_SUCCESS)) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        EventManager.getInstance().unRegisterListener(onNotifyListener);
    }
}
