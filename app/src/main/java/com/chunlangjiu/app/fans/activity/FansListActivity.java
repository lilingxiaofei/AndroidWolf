package com.chunlangjiu.app.fans.activity;

import android.os.Bundle;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;

public class FansListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fans_activity_home);
    }

    @Override
    public void setTitleView() {
        hideTitleView();
    }
}
