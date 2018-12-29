package com.chunlangjiu.app.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;

/**
 * @CreatedbBy: liucun on 2018/7/15.
 * @Describe: 设置账号密码
 */
public class SetAccountPasswordActivity extends BaseActivity {


    @Override
    public void setTitleView() {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_set_account_password);
    }

    public static void startActivity(Activity activity){
        if(activity!=null){
            Intent intent = new Intent(activity,SetAccountPasswordActivity.class);
            activity.startActivity(intent);
        }
    }
}
