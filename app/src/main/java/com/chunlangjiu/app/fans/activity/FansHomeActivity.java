package com.chunlangjiu.app.fans.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

public class FansHomeActivity extends BaseActivity {
    RelativeLayout rl_fans_list;
    TextView tv_fans_num;
    RelativeLayout rl_invite_code;
    TextView tv_invite_code;
    RelativeLayout rl_invite_fans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fans_activity_home);
        initView();
        initData();
    }

    private void initView() {
        rl_fans_list = findViewById(R.id.rl_fans_list);
        tv_fans_num = findViewById(R.id.tv_fans_num);
        rl_invite_code = findViewById(R.id.rl_invite_code);
        tv_invite_code = findViewById(R.id.tv_invite_code);
        rl_invite_fans = findViewById(R.id.rl_invite_fans);
    }

    private void initData() {

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int resId = v.getId();
            if(resId == R.id.rl_fans_list){
                startFansListActivity();
            }else if(resId == R.id.rl_invite_code){

            }else if(resId == R.id.rl_invite_fans){

            }
        }
    };


    private void startFansListActivity(){
        Intent intent = new Intent();
        intent.setClass(this,FansListActivity.class);
        startActivity(intent);
    }
    @Override
    public void setTitleView() {
        hideTitleView();
    }
}
