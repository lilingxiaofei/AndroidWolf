package com.chunlangjiu.app.amain.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.pkqup.commonlibrary.util.SPUtils;


public class SplashActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SPUtils.put("firstStart",true);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.loading);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Boolean isFirstStart = (Boolean) SPUtils.get("firstStart",false);
                if(isFirstStart!=null && isFirstStart.booleanValue()){
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(SplashActivity.this,GuideActivity.class);
                    startActivity(intent);
                    finish();
                }
                SPUtils.put("firstStart",true);

            }
        }, 1500);
    }
}
