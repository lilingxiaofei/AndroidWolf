package com.chunlangjiu.app.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.util.ConstantMsg;
import com.pkqup.commonlibrary.eventmsg.EventManager;

import butterknife.OnClick;

/**
 * @CreatedbBy: liucun on 2018/7/15.
 * @Describe:
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener{

    @Override
    public void setTitleView() {

    }

    public static void startActivity(Activity activity){
        if(activity!=null){
            Intent intent = new Intent(activity, SettingActivity.class);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_setting);
    }
    @OnClick({R.id.img_title_left,R.id.tvLoginPwd,R.id.tvMyData,R.id.tvMyAttestation,R.id.tvAddressManager,R.id.tvBankManager,R.id.tvAbout,R.id.tvUseAgreement})
    @Override
    public void onClick(View view) {
        int resId = view.getId();
        if(resId == R.id.img_title_left){
            finish();
        }else if(resId == R.id.tvLoginPwd){
            SetAccountPasswordActivity.startActivity(SettingActivity.this);
        }else if(resId == R.id.tvPayPwd){
            SetPayPasswordActivity.startActivity(SettingActivity.this);
        }else if(resId == R.id.tvMyData){

        }else if(resId == R.id.tvMyAttestation){
            startActivity(new Intent(this,VerifiedActivity.class));

        }else if(resId == R.id.tvAddressManager){
            startActivity(new Intent(SettingActivity.this, AddressListActivity.class));
        }else if(resId == R.id.tvBankManager){
            startActivity(new Intent(this,BankCardActivity.class));
        }else if(resId == R.id.tvAbout){

        }else if(resId == R.id.tvUseAgreement){

        }else if(resId == R.id.tvLoginOut){
            EventManager.getInstance().notify(null, ConstantMsg.LOGOUT_SUCCESS);
            finish();
        }
    }
}
