package com.chunlangjiu.app.money.activity;

import android.os.Bundle;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;

public class WithDrawActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_draw);
    }

    @Override
    public void setTitleView() {
        titleName.setText("提现");
    }
}
