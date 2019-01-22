package com.chunlangjiu.app.money.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.activity.BankCardActivity;
import com.chunlangjiu.app.user.bean.BankCardListBean;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WithDrawActivity extends BaseActivity {
    public static final int BankCardRequestCode = 188;
    public static final int BankCardResultCode = 200;
    public static final String BankCardData = "BankCardData";
    @BindView(R.id.edtAmount)
    EditText edtAmount;
    private CompositeDisposable disposable;
    private String bankCardId = "";
    private BankCardListBean.BankCardDetailBean bankCardDetailBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_draw);
        disposable = new CompositeDisposable();
    }

    @Override
    public void setTitleView() {
        titleName.setText("提现");
    }

    @OnClick({R.id.img_title_left, R.id.reBankCard, R.id.btnOk})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
                finish();
                break;
            case R.id.reBankCard:
                startActivityForResult(new Intent(this, BankCardActivity.class), BankCardRequestCode);
                break;
            case R.id.btnOk:
                String amount = edtAmount.getText().toString().trim();
                if (TextUtils.isEmpty(amount)) {
                    ToastUtils.showShort("请输入提现金额");
                    return;
                }
                if (TextUtils.isEmpty(bankCardId)){
                    ToastUtils.showShort("请选择银行卡");
                    return;
                }
                depositCash(bankCardId, amount);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();

    }

    private void depositCash(String bankCardId, String amount) {
        disposable.add(ApiUtils.getInstance().depositCash((String) SPUtils.get("token", ""), bankCardId, amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean loginBeanResultBean) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BankCardRequestCode && resultCode == BankCardResultCode) {
            bankCardDetailBean = (BankCardListBean.BankCardDetailBean) data.getSerializableExtra(BankCardData);
            if (null!=bankCardDetailBean){
                bankCardId = String.valueOf(bankCardDetailBean.getBank_id());
            }

        }
    }
}
