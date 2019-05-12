package com.chunlangjiu.app.amain.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseApplication;
import com.chunlangjiu.app.amain.bean.HomeModulesBean;
import com.chunlangjiu.app.appraise.activity.AppraiserMainActivity;
import com.chunlangjiu.app.goods.activity.FestivalActivity;
import com.chunlangjiu.app.goods.activity.GoodsDetailslNewActivity;
import com.chunlangjiu.app.goods.activity.GoodsListNewActivity;
import com.chunlangjiu.app.goods.activity.ShopMainActivity;
import com.chunlangjiu.app.store.activity.StoreListActivity;
import com.chunlangjiu.app.user.activity.AddGoodsActivity;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.MyStatusBarUtils;
import com.chunlangjiu.app.util.UmengEventUtil;
import com.chunlangjiu.app.web.WebViewActivity;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.util.SPUtils;

import io.reactivex.disposables.CompositeDisposable;


public class OpenActivity extends Activity {


    private CompositeDisposable disposable;
    private TextView tvOpenPic;
    private ImageView ivOpenPic;
    private String openPic;
    private int loadTime = 4000;
    private final int WHAT_COUNT_DOWN = 88;

    HomeModulesBean.Params params ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /**
         * View.SYSTEM_UI_FLAG_VISIBLE：显示状态栏，Activity不全屏显示(恢复到有状态的正常情况)。
         View.INVISIBLE：隐藏状态栏，同时Activity会伸展全屏显示。
         View.SYSTEM_UI_FLAG_FULLSCREEN：Activity全屏显示，且状态栏被隐藏覆盖掉。
         View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住。
         View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
         View.SYSTEM_UI_LAYOUT_FLAGS：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
         View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键。
         View.SYSTEM_UI_FLAG_LOW_PROFILE：状态栏显示处于低能显示状态(low profile模式)，状态栏上一些图标显示会被隐藏。
         */

//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        MyStatusBarUtils.setNavigationBarStatusBarTranslucent(this);
        setContentView(R.layout.splash_activity);
        params = (HomeModulesBean.Params)getIntent().getSerializableExtra("openParams");
        initView();
        initData();
    }

    private void initView() {
        tvOpenPic = findViewById(R.id.tvOpenPic);
        ivOpenPic = findViewById(R.id.ivOpenPic);
    }

    private void initData() {
        disposable = new CompositeDisposable();
        if (null != params) {
            loadTime = 5000;
            openPic = params.getImagesrc();
            tvOpenPic.setVisibility(View.VISIBLE);
            tvOpenPic.setOnClickListener(onClickListener);
            ivOpenPic.setOnClickListener(onClickListener);
            setOpenPic();
        }
        startCountDown();
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_COUNT_DOWN:
                    loadTime = loadTime - 1000;
                    if (loadTime <= 0) {
                        startNextActivity();
                    } else {
                        startCountDown();
                    }
                    break;
                default:
                    functionJump();
                    OpenActivity.this.finish();
                    break;
            }
        }
    };

    private void startCountDown() {
        tvOpenPic.setText(getString(R.string.openPicStr, loadTime / 1000 + ""));
        handler.removeMessages(WHAT_COUNT_DOWN);
        handler.sendEmptyMessageDelayed(WHAT_COUNT_DOWN, 1000);
    }

    private void startNextActivity() {
        if(params == null){
            boolean isFirstStart = (boolean) SPUtils.get("firstStart", true);
            if (isFirstStart) {
                Intent intent = new Intent(OpenActivity.this, GuideActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(OpenActivity.this, MainActivity.class);
                startActivity(intent);
            }
            SPUtils.put("firstStart", false);
        }
        OpenActivity.this.finish();
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tvOpenPic) {
                startNextActivity();
                OpenActivity.this.finish();
            } else if (v.getId() == R.id.ivOpenPic) {
                handler.removeMessages(WHAT_COUNT_DOWN);
                startNextActivity();
                handler.sendEmptyMessageDelayed(11, 200);
            }

        }
    };


    public void functionJump() {
        String type = params .getLinktype() ;
        String value = params.getLink();
        if(type != null) {
            switch (type) {
                case HomeModulesBean.ITEM_GOODS:
                    GoodsDetailslNewActivity.startActivity(OpenActivity.this, value);
                    UmengEventUtil.bannerEvent(OpenActivity.this, "商品");
                    break;
                case HomeModulesBean.ITEM_SHOP:
                    ShopMainActivity.startShopMainActivity(OpenActivity.this, value);
                    UmengEventUtil.bannerEvent(OpenActivity.this, "店铺");
                    break;
                case HomeModulesBean.ITEM_CLASS:
                    EventManager.getInstance().notify(null, ConstantMsg.MSG_PAGE_CLASS);
                    UmengEventUtil.bannerEvent(OpenActivity.this, "全部");
                    break;
                case HomeModulesBean.ITEM_BRAND:
                    GoodsListNewActivity.startGoodsListNewActivity(OpenActivity.this,"","", value, "", "");
                    UmengEventUtil.bannerEvent(OpenActivity.this, "品牌");
                    break;
                case HomeModulesBean.ITEM_AUCTION:
                    EventManager.getInstance().notify(null, BaseApplication.HIDE_AUCTION ? ConstantMsg.MSG_PAGE_CLASS : ConstantMsg.MSG_PAGE_AUCTION);
                    UmengEventUtil.bannerEvent(OpenActivity.this, "竞拍专区");
                    break;
                case HomeModulesBean.ITEM_ACTIVITY:
                    FestivalActivity.startActivity(OpenActivity.this);
                    UmengEventUtil.bannerEvent(OpenActivity.this, "店铺活动");
                    break;
                case HomeModulesBean.ITEM_WINERY:
                    startActivity(new Intent(OpenActivity.this, StoreListActivity.class));
                    UmengEventUtil.bannerEvent(OpenActivity.this, "名庄查询");
                    break;
                case HomeModulesBean.ITEM_EVALUATION:
                    if (BaseApplication.isLogin()) {
                        startActivity(new Intent(OpenActivity.this, AppraiserMainActivity.class));
                    } else {
                        startActivity(new Intent(OpenActivity.this, LoginMainActivity.class));
                    }
                    UmengEventUtil.bannerEvent(OpenActivity.this, "名酒估价");
                    break;
                case HomeModulesBean.ITEM_MEMBER:
                    EventManager.getInstance().notify(null, ConstantMsg.MSG_PAGE_MY);
                    UmengEventUtil.bannerEvent(OpenActivity.this, "我的");
                    break;
                case HomeModulesBean.ITEM_CART:
                    EventManager.getInstance().notify(null, ConstantMsg.MSG_PAGE_CART);
                    UmengEventUtil.bannerEvent(OpenActivity.this, "我要买酒");
                    break;
                case HomeModulesBean.ITEM_H5:
                    WebViewActivity.startWebViewActivity(OpenActivity.this, value, "");
                    UmengEventUtil.bannerEvent(OpenActivity.this, "H5");
                    break;
                case HomeModulesBean.ITEM_SELLWINE:
                    startActivity(new Intent(OpenActivity.this, AddGoodsActivity.class));
                    UmengEventUtil.bannerEvent(OpenActivity.this, "我要卖酒");
                    break;
            }
        }
    }


    private void setOpenPic() {
        Glide.with(this).load(openPic).into(ivOpenPic);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != disposable){
            disposable.dispose();
        }
    }
}
