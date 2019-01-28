package com.chunlangjiu.app.amain.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.net.ApiUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SPUtils;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class SplashActivity extends AppCompatActivity {


    private CompositeDisposable disposable;
    private TextView tvOpenPic;
    private ImageView ivOpenPic ;
    private String openPic ;
    private int loadTime = 2000;
    private int what = 88 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_activity);

        initView();
        initData();
    }

    private void initView(){
        tvOpenPic = findViewById(R.id.tvOpenPic);
        ivOpenPic = findViewById(R.id.ivOpenPic);
    }

    private void initData(){
        disposable = new CompositeDisposable();
        startCountDown();
        loadOpen();
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loadTime = loadTime -1000 ;
            if(loadTime<=0){
                startNextActivity();
            }else{
                startCountDown();
            }
        }
    };

    private void startCountDown(){
        tvOpenPic.setText(getString(R.string.openPicStr,loadTime/1000+""));
        handler.removeMessages(what);
        handler.sendEmptyMessageDelayed(what,1000);
    }

    private void startNextActivity(){
        boolean isFirstStart = (boolean) SPUtils.get("firstStart",true);
        if(isFirstStart){
            Intent intent = new Intent(SplashActivity.this,GuideActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        SPUtils.put("firstStart",false);
    }

    private void loadOpen(){
        disposable.add(ApiUtils.getInstance().appOpenAd()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<Map>>() {
                    @Override
                    public void accept(ResultBean<Map> brandsListBeanResultBean) throws Exception {
                        Map resultMap =  brandsListBeanResultBean.getData();
                        if(resultMap != null && resultMap.containsKey("url")){
                            loadTime = 5000 ;
                            startCountDown();
                            openPic = (String)resultMap.get("url");
                            tvOpenPic.setVisibility(View.VISIBLE);
                            setOpenPic();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void setOpenPic(){
        Glide.with(this).load(openPic).into(ivOpenPic);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
