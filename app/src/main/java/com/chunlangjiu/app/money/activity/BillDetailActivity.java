package com.chunlangjiu.app.money.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;

public class BillDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
    }

    @Override
    public void setTitleView() {
        titleName.setText("账单详情");
    }
}
