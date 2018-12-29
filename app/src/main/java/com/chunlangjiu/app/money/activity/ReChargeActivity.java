package com.chunlangjiu.app.money.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ReChargeActivity extends BaseActivity {

    @BindView(R.id.relBalance)
    RelativeLayout relBalance;
    @BindView(R.id.imgBalance)
    ImageView imgBalance;
    @BindView(R.id.relSecurityDeposit)
    RelativeLayout relSecurityDeposit;
    @BindView(R.id.imgSecurityDeposit)
    ImageView imgSecurityDeposit;
    @BindView(R.id.relWx)
    RelativeLayout relWx;
    @BindView(R.id.imgWxCheck)
    ImageView imgWxCheck;
    @BindView(R.id.relZfb)
    RelativeLayout relZfb;
    @BindView(R.id.imgZfbCheck)
    ImageView imgZfbCheck;

    private PayType payType = null;
    private RechargeType rechargeType = null;

    enum PayType {
        Wx, Alipay
    }

    enum RechargeType {
        Balance, SecurityDeposit
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_charge);
    }

    @Override
    public void setTitleView() {
        titleName.setText("充值");
    }

    @OnClick({R.id.btnOk, R.id.relBalance, R.id.relSecurityDeposit, R.id.relWx, R.id.relZfb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOk:
                break;
            case R.id.relBalance:
                toggleBalanceSecurityDeposit(RechargeType.Balance);
                break;
            case R.id.relSecurityDeposit:
                toggleBalanceSecurityDeposit(RechargeType.SecurityDeposit);
                break;
            case R.id.relWx:
                togglePayType(PayType.Wx);
                break;
            case R.id.relZfb:
                togglePayType(PayType.Alipay);
                break;
        }

    }

    public void toggleBalanceSecurityDeposit(RechargeType rechargeType) {
        this.rechargeType = rechargeType;
        switch (rechargeType) {
            case Balance:
                imgBalance.setSelected(true);
                imgSecurityDeposit.setSelected(false);
                break;
            case SecurityDeposit:
                imgBalance.setSelected(false);
                imgSecurityDeposit.setSelected(true);
                break;
        }

    }

    public void togglePayType(PayType payType) {
        this.payType = payType;
        switch (payType) {
            case Alipay:
                imgZfbCheck.setSelected(true);
                imgWxCheck.setSelected(false);
                break;
            case Wx:
                imgZfbCheck.setSelected(false);
                imgWxCheck.setSelected(true);
                break;
        }

    }

}
