package com.chunlangjiu.app.amain.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.MyStatusBarUtils;
import com.chunlangjiu.app.web.WebViewActivity;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @CreatedbBy: liucun on 2018/7/6
 * @Describe: 登录页面
 */
public class LoginMainActivity extends BaseActivity {

    @BindView(R.id.tvLogin)
    TextView tvLogin;

    @BindView(R.id.ivQQLogin)
    ImageView ivQQLogin;
    @BindView(R.id.ivWeChatLogin)
    ImageView ivWeChatLogin;
    @BindView(R.id.ivSinaLogin)
    ImageView ivSinaLogin;
    @BindView(R.id.tvRegister)
    TextView tvRegister;


    private CompositeDisposable disposable;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amain_activity_login_main);
        EventManager.getInstance().registerListener(onNotifyListener);
        initView();
    }

    public static void startLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginMainActivity.class);
        activity.startActivity(intent);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_title_left:
                    finish();
                    break;
                case R.id.tvLogin:
                    LoginActivity.startLoginActivity(LoginMainActivity.this);
                    break;
                case R.id.tvRegister:
                    startActivity(new Intent(LoginMainActivity.this,RegisterActivity.class));
                    break;
                case R.id.ivQQLogin:
                    UMShareAPI.get(LoginMainActivity.this).getPlatformInfo(LoginMainActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                    break;
                case R.id.ivWeChatLogin:
                    UMShareAPI.get(LoginMainActivity.this).getPlatformInfo(LoginMainActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                    break;
                case R.id.ivSinaLogin:
                    UMShareAPI.get(LoginMainActivity.this).getPlatformInfo(LoginMainActivity.this, SHARE_MEDIA.SINA, umAuthListener);
                    break;
            }
        }

    };

    UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            if (!map.containsKey("iconurl")) { //判断是授权还是获取用户信息
                UMShareAPI.get(LoginMainActivity.this).getPlatformInfo(LoginMainActivity.this, share_media, umAuthListener);
            } else {
                System.out.println("uid========" + map.get("uid"));
                System.out.println("name========" + map.get("name"));
                System.out.println("iconurl========" + map.get("iconurl"));
                ToastUtils.showShort("社会唐哥" + map.get("name"));
                thirdPartyLogin(map.get("uid"));
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            UMShareAPI.get(LoginMainActivity.this).deleteOauth(LoginMainActivity.this,share_media,umAuthListener);
            ToastUtils.showShort("已经删除错误授权，请重新登录");
//            UMShareAPI.get(LoginActivity.this).getPlatformInfo(LoginActivity.this, share_media, umAuthListener);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
        }
    };

    @Override
    public void setTitleView() {
        hideTitleView();
        titleName.setText("登录注册");
        titleImgLeft.setOnClickListener(onClickListener);
    }



    private void initView() {
        MyStatusBarUtils.setStatusBar(this, ContextCompat.getColor(this, R.color.bg_red));
        MyStatusBarUtils.setFitsSystemWindows(findViewById(R.id.rlTitle), true);
        disposable = new CompositeDisposable();
        tvLogin.setOnClickListener(onClickListener);
        tvRegister.setOnClickListener(onClickListener);

        ivQQLogin.setOnClickListener(onClickListener);
        ivWeChatLogin.setOnClickListener(onClickListener);
        ivSinaLogin.setOnClickListener(onClickListener);
    }




    private void thirdPartyLogin (String loginId) {
        if(TextUtils.isEmpty(loginId)){
            ToastUtils.showShort("第三方登录失败，请稍后重试!");
        }else{
//            showLoadingDialog();
//            disposable.add(ApiUtils.getInstance().login(etPhone.getText().toString(), etAuthCode.getText().toString())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<ResultBean<LoginBean>>() {
//                        @Override
//                        public void accept(ResultBean<LoginBean> loginBeanResultBean) throws Exception {
//                            hideLoadingDialog();
//                            ToastUtils.showShort("登录成功");
//                            SPUtils.put("token", loginBeanResultBean.getData().getAccessToken());
//                            SPUtils.put("account", etPhone.getText().toString());
//                            BaseApplication.setToken(loginBeanResultBean.getData().getAccessToken());
//                            BaseApplication.initToken();
//                            if ("false".equals(loginBeanResultBean.getData().getReferrer())) {
//                                EventManager.getInstance().notify(null, ConstantMsg.SET_INVITATION_CODE);
//                            }
//                            EventManager.getInstance().notify(null, ConstantMsg.LOGIN_SUCCESS);
//                            finish();
//
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                            hideLoadingDialog();
//                            ToastUtils.showErrorMsg(throwable);
//                        }
//                    }));
        }


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        EventManager.getInstance().unRegisterListener(onNotifyListener);
    }
}
