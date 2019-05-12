package com.chunlangjiu.app.amain.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.jaeger.library.StatusBarUtil;
import com.pkqup.commonlibrary.net.bean.ResultBean;
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
public class ResetPsdActivity extends BaseActivity {
    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etAuthCode)
    EditText etAuthCode;
    @BindView(R.id.tvGetCode)
    TextView tvGetCode;
    @BindView(R.id.etPsd)
    EditText etPsd;

    @BindView(R.id.tvSave)
    TextView tvSave;

    private CompositeDisposable disposable;

    private CountDownTimer countDownTimer;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_title_left:
                case R.id.ivBack:
                    finish();
                    break;
                case R.id.tvGetCode:
                    checkPhone();
                    break;
                case R.id.tvSave:
                    checkData();
                    break;
            }
        }
    };

    @Override
    public void setTitleView() {
        hideTitleView();
        titleName.setText("忘记密码");
        titleImgLeft.setOnClickListener(onClickListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amain_activity_set_psd);
        initView();
    }

    private void initView() {
        StatusBarUtil.setTranslucentForImageView(this,0,findViewById(R.id.rlTitle));
        StatusBarUtil.setLightMode(this);
        disposable = new CompositeDisposable();
        ivBack.setOnClickListener(onClickListener);
        tvGetCode.setOnClickListener(onClickListener);
        tvSave.setOnClickListener(onClickListener);
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


    private void checkData() {
        if (etPhone.getText().toString().length() < 11) {
            ToastUtils.showShort("请输入正确的手机号码");
        } else if (TextUtils.isEmpty(etAuthCode.getText().toString())) {
            ToastUtils.showShort("请输入正确的验证码");
        } else if (etPsd.getText().toString().length() < 6) {
            ToastUtils.showShort("密码长度最低为6位");
        } else {
            setPsd();
        }
    }

    private void setPsd() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().setPsd(etPhone.getText().toString(), etAuthCode.getText().toString(), etPsd.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showShort("密码设置成功");
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
