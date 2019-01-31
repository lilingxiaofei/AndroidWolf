package com.chunlangjiu.app.user.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
    @BindView(R.id.btnCompany)
    Button btnCompany;

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

    @OnClick({R.id.btnPerson, R.id.btnCompany, R.id.img_title_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPerson:
                checkPersonStatus();
//                startActivity(new Intent(this,PersonAuthActivity.class));
                break;
            case R.id.btnCompany:
                checkCompanyStatus();
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


    private void checkCompanyStatus() {
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
                        } else if (AuthStatusBean.AUTH_SUCCESS.equals(authStatusBeanResultBean.getData().getStatus())) {
                            ToastUtils.showShort("您的认证已成功");
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
        if (AuthStatusBean.AUTH_SUCCESS.equals(personStatus)) {
            tvDesc.setText("已认证");
            btnPerson.setText("已认证");
            btnCompany.setSelected(false);
            btnPerson.setEnabled(false);
            authed = true;
        }else {
            btnPerson.setSelected(true);
            btnPerson.setEnabled(true);
        }
        if (AuthStatusBean.AUTH_SUCCESS.equals(companyStatus)) {
            tvDesc.setText("已认证");
            btnCompany.setText("已认证");
            btnCompany.setSelected(false);
            btnCompany.setEnabled(false);
            authed = true;
        }else {
            btnCompany.setSelected(true);
            btnCompany.setEnabled(true);
        }
        if (!authed) {
            tvDesc.setText("立即实名认证享受更多特权服务");
        }
    }

    private void toAuthActivity() {
        startActivity(new Intent(this, PersonAuthActivity.class));
    }

    private void toAuthCompanyActivity() {
        startActivity(new Intent(this, CompanyAuthActivity.class));
    }

}
