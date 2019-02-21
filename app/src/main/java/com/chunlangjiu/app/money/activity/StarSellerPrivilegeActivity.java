package com.chunlangjiu.app.money.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;

public class StarSellerPrivilegeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_seller_privilege);
    }

    @Override
    public void setTitleView() {
        titleName.setText("星级卖家特权");
    }
}
