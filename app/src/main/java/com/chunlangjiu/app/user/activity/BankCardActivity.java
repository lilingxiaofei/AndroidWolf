package com.chunlangjiu.app.user.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;

import butterknife.OnClick;

public class BankCardActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card);
    }

    @Override
    public void setTitleView() {
        titleName.setText("银行卡");
    }
    @OnClick({R.id.btnAddBankCard})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnAddBankCard:
                startActivity(new Intent(this,AddBankCardActivity.class));
                break;

        }

    }
}
