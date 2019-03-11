package com.chunlangjiu.app.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.bean.AuthStatusBean;
import com.chunlangjiu.app.util.ConstantMsg;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class VerifiedActivity extends BaseActivity {
    @BindView(R.id.imgHead)
    ImageView imgHead;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.btnPerson)
    Button btnPerson;
    @BindView(R.id.tvUpdatePerson)
    TextView tvUpdatePerson;
    @BindView(R.id.btnCompany)
    Button btnCompany;
    @BindView(R.id.tvUpdateCompany)
    TextView tvUpdateCompany;

    private CompositeDisposable disposable;
    private String personStatus;
    private String companyStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified);
        disposable = new CompositeDisposable();
        initView();
        getPersonAndCompanyAuthStatus();
    }

    private void initView() {
        EventManager.getInstance().registerListener(onNotifyListener);
        String headUrl = (String) SPUtils.get("avator", "");
        if (!TextUtils.isEmpty(headUrl)) {
            GlideUtils.loadImageHead(this, headUrl, imgHead);
        }
        tvAccount.setText((String) SPUtils.get("account", ""));
    }

    @Override
    public void setTitleView() {
        titleName.setText("实名认证");
    }

    @OnClick({R.id.btnPerson, R.id.btnCompany, R.id.img_title_left,R.id.tvUpdatePerson,R.id.tvUpdateCompany})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvUpdatePerson:
                checkPersonStatus();
                break;
            case R.id.btnPerson:
                checkPersonStatus();
//                startActivity(new Intent(this,PersonAuthActivity.class));
                break;
            case R.id.tvUpdateCompany:
                checkCompanyStatus("edit");
                break;
            case R.id.btnCompany:
                checkCompanyStatus("add");
//                startActivity(new Intent(this,CompanyAuthActivity.class));
                break;
            case R.id.img_title_left:
                finish();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        EventManager.getInstance().unRegisterListener(onNotifyListener);
    }

    private void checkPersonStatus() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getPersonAuthStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<AuthStatusBean>>() {
                    @Override
                    public void accept(ResultBean<AuthStatusBean> authStatusBeanResultBean) throws Exception {
                        personStatus = authStatusBeanResultBean.getData().getStatus();
                        hideLoadingDialog();
                        if ("active".equals(authStatusBeanResultBean.getData().getStatus())) {
                            //未认证
                            toAuthActivity();
                        } else if ("locked".equals(authStatusBeanResultBean.getData().getStatus())) {
                            ToastUtils.showShort("您的认证正在审核中，我们会尽快处理");
                        } else if ("failing".equals(authStatusBeanResultBean.getData().getStatus())) {
                            ToastUtils.showShort("您的认证被驳回，请重新提交资料审核");
                            toAuthActivity();
                        } else if (AuthStatusBean.AUTH_SUCCESS.equals(authStatusBeanResultBean.getData().getStatus())) {
                            ToastUtils.showShort("您的认证已成功");
                            toAuthActivity();
//                            tvAuthPerson.setVisibility(View.GONE);
//                            imgAuthStatus.setImageResource(R.mipmap.my_auth);
//                            tvAuthStatus.setText("已认证");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                    }
                }));
    }


    private void checkCompanyStatus(String type) {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getCompanyAuthStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<AuthStatusBean>>() {
                    @Override
                    public void accept(ResultBean<AuthStatusBean> authStatusBeanResultBean) throws Exception {
                        companyStatus = authStatusBeanResultBean.getData().getStatus();
                        hideLoadingDialog();
                        if ("active".equals(authStatusBeanResultBean.getData().getStatus())) {
                            //未认证
                            toAuthCompanyActivity();
                        } else if ("locked".equals(authStatusBeanResultBean.getData().getStatus())) {
                            ToastUtils.showShort("您的认证正在审核中，我们会尽快处理");
                        } else if ("failing".equals(authStatusBeanResultBean.getData().getStatus())) {
                            ToastUtils.showShort("您的认证被驳回，请重新提交资料审核");
                            toAuthCompanyActivity();
                        } else if (AuthStatusBean.AUTH_MODIFIER.equals(authStatusBeanResultBean.getData().getStatus())) {
                            toAuthCompanyActivity();
//                            tvAuthPerson.setVisibility(View.GONE);
//                            tvAuthCompany.setVisibility(View.GONE);
//                            imgAuthStatus.setImageResource(R.mipmap.my_auth);
//                            tvAuthStatus.setText("已认证");
//                            if (userType == TYPE_BUYER) {
//                                if (AuthStatusBean.AUTH_SUCCESS.equals(companyStatus)) {
//                                    tvMyTitle.setText("企业买家");
//                                    imgMyTitleType.setImageResource(R.mipmap.my_company);
//                                } else {
//                                    tvMyTitle.setText("个人买家");
//                                    imgMyTitleType.setImageResource(R.mipmap.my_person);
//                                }
//                            } else {
//                                if (AuthStatusBean.AUTH_SUCCESS.equals(companyStatus)) {
//                                    tvMyTitle.setText("企业卖家");
//                                    imgMyTitleType.setImageResource(R.mipmap.my_company);
//                                } else {
//                                    tvMyTitle.setText("个人卖家");
//                                    imgMyTitleType.setImageResource(R.mipmap.my_person);
//                                }
//                            }
                        }else if (AuthStatusBean.AUTH_SUCCESS.equals(authStatusBeanResultBean.getData().getStatus())) {
                            ToastUtils.showShort("您的认证已成功");
                            toAuthCompanyActivity();
//                            tvAuthPerson.setVisibility(View.GONE);
//                            tvAuthCompany.setVisibility(View.GONE);
//                            imgAuthStatus.setImageResource(R.mipmap.my_auth);
//                            tvAuthStatus.setText("已认证");
//                            if (userType == TYPE_BUYER) {
//                                if (AuthStatusBean.AUTH_SUCCESS.equals(companyStatus)) {
//                                    tvMyTitle.setText("企业买家");
//                                    imgMyTitleType.setImageResource(R.mipmap.my_company);
//                                } else {
//                                    tvMyTitle.setText("个人买家");
//                                    imgMyTitleType.setImageResource(R.mipmap.my_person);
//                                }
//                            } else {
//                                if (AuthStatusBean.AUTH_SUCCESS.equals(companyStatus)) {
//                                    tvMyTitle.setText("企业卖家");
//                                    imgMyTitleType.setImageResource(R.mipmap.my_company);
//                                } else {
//                                    tvMyTitle.setText("个人卖家");
//                                    imgMyTitleType.setImageResource(R.mipmap.my_person);
//                                }
//                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                    }
                }));
    }

    private void getPersonAndCompanyAuthStatus() {
        showLoadingDialog();
        Observable<ResultBean<AuthStatusBean>> personAuthStatus = ApiUtils.getInstance().getPersonAuthStatus();
        Observable<ResultBean<AuthStatusBean>> companyAuthStatus = ApiUtils.getInstance().getCompanyAuthStatus();
        disposable.add(Observable.zip(personAuthStatus, companyAuthStatus, new BiFunction<ResultBean<AuthStatusBean>, ResultBean<AuthStatusBean>, List<AuthStatusBean>>() {
            @Override
            public List<AuthStatusBean> apply(ResultBean<AuthStatusBean> uploadImageBeanResultBean, ResultBean<AuthStatusBean> uploadImageBeanResultBean2) throws Exception {
                List<AuthStatusBean> imageLists = new ArrayList<>();
                imageLists.add(uploadImageBeanResultBean.getData());
                imageLists.add(uploadImageBeanResultBean2.getData());
                return imageLists;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AuthStatusBean>>() {
                    @Override
                    public void accept(List<AuthStatusBean> authStatusBeans) throws Exception {
                        hideLoadingDialog();
                        personStatus = authStatusBeans.get(0).getStatus();
                        companyStatus = authStatusBeans.get(1).getStatus();
                        setAuthView();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showNetErrorMsg(throwable);
                    }
                }));
    }

    private void setAuthView() {
        boolean authed = false;
        if (AuthStatusBean.AUTH_SUCCESS.equals(companyStatus)) {
            tvDesc.setText("企业实名已认证");
//            btnCompany.setText("已认证");
            btnCompany.setClickable(false);
            btnPerson.setEnabled(false);
            tvUpdatePerson.setVisibility(View.INVISIBLE);
            authed = true;
        }else if (AuthStatusBean.AUTH_MODIFIER.equals(companyStatus)) {
            tvDesc.setText("企业实名认证审核中");
            btnCompany.setClickable(false);
            btnPerson.setEnabled(false);
            tvUpdatePerson.setVisibility(View.INVISIBLE);
        }else {
            btnCompany.setClickable(true);
            btnCompany.setEnabled(true);
            tvUpdatePerson.setVisibility(View.VISIBLE);
            if (AuthStatusBean.AUTH_SUCCESS.equals(personStatus)) {
                tvDesc.setText("个人实名已认证");
//                btnPerson.setText("已认证");
                btnPerson.setClickable(false);
                authed = true;
            }else {
                btnPerson.setClickable(true);
            }
        }
        if (!authed) {
            tvDesc.setText("立即实名认证享受更多特权服务");
        }
    }

    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            eventTag = eventTag == null ? "" : eventTag;
            switch (eventTag) {
                case ConstantMsg.PERSON_COMPANY_AUTH_SUCCESS:
                    getPersonAndCompanyAuthStatus();
                    break;
            }


//            authSuccess(eventTag);
        }
    };



    private void toAuthActivity() {
        startActivity(new Intent(this, PersonAuthActivity.class));
    }

    private void toAuthCompanyActivity() {
        startActivity(new Intent(this, CompanyAuthActivity.class));
    }

}
