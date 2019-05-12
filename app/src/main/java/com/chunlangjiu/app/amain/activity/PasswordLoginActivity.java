package com.chunlangjiu.app.amain.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.abase.BaseApplication;
import com.chunlangjiu.app.amain.bean.LoginBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.jaeger.library.StatusBarUtil;
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
 * @CreatedbBy: liucun on 2018/10/10
 * @Describe:
 */
public class PasswordLoginActivity extends BaseActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etPsd)
    EditText etPsd;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvForgetPsd)
    TextView tvForgetPsd;

    private CompositeDisposable disposable;


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_title_left:
                case R.id.ivBack:
                    finish();
                    break;
                case R.id.tvLogin:
                    checkData();
                    break;
                case R.id.tvForgetPsd:
                    startActivity(new Intent(PasswordLoginActivity.this, ResetPsdActivity.class));
                    break;
            }
        }
    };


    @Override
    public void setTitleView() {
        hideTitleView();
        titleName.setText("密码登录");
        titleImgLeft.setOnClickListener(onClickListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amain_activity_password_login);
        initView();
    }

    private void initView() {
        StatusBarUtil.setTranslucentForImageView(this,0,findViewById(R.id.rlTitle));
        StatusBarUtil.setLightMode(this);
        disposable = new CompositeDisposable();
        ivBack.setOnClickListener(onClickListener);
        tvLogin.setOnClickListener(onClickListener);
        tvForgetPsd.setOnClickListener(onClickListener);
    }


    private void checkData() {
        if (etPhone.getText().toString().length() < 11) {
            ToastUtils.showShort("请输入正确的手机号码");
        } else if (etPsd.getText().toString().length() < 6) {
            ToastUtils.showShort("密码长度最低为6位");
        } else {
            login();
        }
    }

    private void login() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().psdLogin(etPhone.getText().toString(), etPsd.getText().toString(), CommonUtils.getUniquePsuedoID())
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
                        if("false".equals(loginBeanResultBean.getData().getReferrer())){
                            EventManager.getInstance().notify(null, ConstantMsg.SET_INVITATION_CODE);
                        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
