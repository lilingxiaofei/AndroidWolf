package com.chunlangjiu.app.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.ToastUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/7/15.
 * @Describe: 设置账号密码
 */
public class SetAccountPasswordActivity extends BaseActivity {

    private EditText etPwd;
    private EditText etNewPwd ;
    private EditText etConfirmPwd ;
    private TextView tvCommit ;
    private CompositeDisposable disposable;
    @Override
    public void setTitleView() {
        titleName.setText(R.string.edit_pwd);
        titleImgLeft.setOnClickListener(onClickListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_set_account_password);
        disposable = new CompositeDisposable();
        initView();
    }

    private void initView(){
        etPwd = findViewById(R.id.etPwd);
        etNewPwd = findViewById(R.id.etNewPwd);
        etConfirmPwd = findViewById(R.id.etConfirmPwd);
        tvCommit = findViewById(R.id.tvCommit);
        tvCommit.setOnClickListener(onClickListener);
    }

    /**
     * 修改支付密码
     */
    private void setPayPwd() {
        tvCommit.setEnabled(false);
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().updateLoginPassword(etNewPwd.getText().toString(),etConfirmPwd.getText().toString(),etPwd.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        tvCommit.setEnabled(true);
                        hideLoadingDialog();
                        if(resultBean.getErrorcode() == 0){
                            ToastUtils.showShort("修改成功");
                            finish();
                        }
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

    public static void startActivity(Activity activity){
        if(activity!=null){
            Intent intent = new Intent(activity,SetAccountPasswordActivity.class);
            activity.startActivity(intent);
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int resId = view.getId();
            if(resId == R.id.img_title_left){
                finish();
            }else if(resId == R.id.tvCommit){
                setPayPwd();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
