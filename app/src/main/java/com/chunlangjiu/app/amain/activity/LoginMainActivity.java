package com.chunlangjiu.app.amain.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.abase.BaseApplication;
import com.chunlangjiu.app.amain.bean.LoginBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.web.WebViewActivity;
import com.jaeger.library.StatusBarUtil;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/7/6
 * @Describe: 登录页面
 */
public class LoginMainActivity extends BaseActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;
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
                case R.id.ivBack:
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
            if(map!=null){
                if (!map.containsKey("iconurl")) { //判断是授权还是获取用户信息
                    UMShareAPI.get(LoginMainActivity.this).getPlatformInfo(LoginMainActivity.this, share_media, umAuthListener);
                } else {
                    JSONObject json = new JSONObject(map);
                    String loginInfo = json.toString().replaceAll("\\\\","/");
                    thirdPartyLogin(loginInfo);
                }
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
//        MyStatusBarUtils.setStatusBar(this, ContextCompat.getColor(this, R.color.bg_red));
//        MyStatusBarUtils.setFitsSystemWindows(findViewById(R.id.rlTitle), true);
        StatusBarUtil.setTranslucentForImageView(this,0,findViewById(R.id.rlTitle));
        StatusBarUtil.setLightMode(this);
        ivBack.setOnClickListener(onClickListener);
        disposable = new CompositeDisposable();
        tvLogin.setOnClickListener(onClickListener);
        tvRegister.setOnClickListener(onClickListener);
        tvRegister.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        tvRegister.getPaint().setAntiAlias(true);//抗锯齿

        ivQQLogin.setOnClickListener(onClickListener);
        ivWeChatLogin.setOnClickListener(onClickListener);
        ivSinaLogin.setOnClickListener(onClickListener);
    }




    private void thirdPartyLogin (final String loginInfo) {
        if(loginInfo == null || TextUtils.isEmpty(loginInfo.toString())){
            ToastUtils.showShort("第三方登录失败，请稍后重试!");
        }else{
            showLoadingDialog();
            disposable.add(ApiUtils.getInstance().thirdPartyLogin(loginInfo,  CommonUtils.getUniquePsuedoID())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean<LoginBean>>() {
                        @Override
                        public void accept(ResultBean<LoginBean> loginBeanResultBean) throws Exception {
                            hideLoadingDialog();
                            if(loginBeanResultBean.getErrorcode() == 0){
                                if("1".equals(loginBeanResultBean.getData().getBinded())){
                                    ToastUtils.showShort("登录成功");
                                    SPUtils.put("token", loginBeanResultBean.getData().getAccessToken());
                                    BaseApplication.setToken(loginBeanResultBean.getData().getAccessToken());
                                    BaseApplication.initToken();
                                    if ("false".equals(loginBeanResultBean.getData().getReferrer())) {
                                        EventManager.getInstance().notify(null, ConstantMsg.SET_INVITATION_CODE);
                                    }
                                    EventManager.getInstance().notify(null, ConstantMsg.LOGIN_SUCCESS);
                                    finish();
                                }else{
                                    BindingActivity.startLoginActivity(LoginMainActivity.this,loginInfo);
                                }
                            }else{
                                ToastUtils.showShort(loginBeanResultBean.getMsg());
                            }
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
