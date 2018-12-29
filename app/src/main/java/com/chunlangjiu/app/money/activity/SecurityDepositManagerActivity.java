package com.chunlangjiu.app.money.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class SecurityDepositManagerActivity extends BaseActivity {

    @BindView(R.id.bankCardList)
    RecyclerView bankCardList;


    private final String SECURITY_DEPOSIT_TYPE = "SECURITY_DEPOSIT_TYPE";
    private final String PAYING_DEPOSIT = "PAYING_DEPOSIT";
    private final String REFUND_DEPOSIT = "REFUND_DEPOSIT";
    private String securityDepositType = PAYING_DEPOSIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_deposit_manager);
    }
    @OnClick({R.id.img_title_left})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_title_left:
                finish();
                break;
        }
    }

    @Override
    public void setTitleView() {
        switch (securityDepositType) {
            case PAYING_DEPOSIT:
                titleName.setText("交纳保证金");
                break;
            case REFUND_DEPOSIT:
                titleName.setText("撤销保证金");
                break;
        }

    }

}
