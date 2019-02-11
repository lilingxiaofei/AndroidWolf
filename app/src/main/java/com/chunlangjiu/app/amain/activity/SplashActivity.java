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
import com.chunlangjiu.app.abase.BaseApplication;
import com.chunlangjiu.app.amain.bean.HomeModulesBean;
import com.chunlangjiu.app.goods.activity.FestivalActivity;
import com.chunlangjiu.app.goods.activity.GoodsDetailslNewActivity;
import com.chunlangjiu.app.goods.activity.GoodsListNewActivity;
import com.chunlangjiu.app.goods.activity.ShopMainActivity;
import com.chunlangjiu.app.goods.activity.ValuationActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.store.activity.StoreListActivity;
import com.chunlangjiu.app.user.activity.AddGoodsActivity;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.UmengEventUtil;
import com.chunlangjiu.app.web.WebViewActivity;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SPUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class SplashActivity extends AppCompatActivity {


    private CompositeDisposable disposable;
    private TextView tvOpenPic;
    private ImageView ivOpenPic;
    private String openPic;
    private int loadTime = 2000;
    private final int WHAT_COUNT_DOWN = 88;

    HomeModulesBean.Params params ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_activity);

        initView();
        initData();
    }

    private void initView() {
        tvOpenPic = findViewById(R.id.tvOpenPic);
        ivOpenPic = findViewById(R.id.ivOpenPic);
    }

    private void initData() {
        disposable = new CompositeDisposable();
        startCountDown();
        loadOpen();
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
                    SplashActivity.this.finish();
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
        boolean isFirstStart = (boolean) SPUtils.get("firstStart", true);
        if (isFirstStart) {
            Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        }
        SPUtils.put("firstStart", false);
        SplashActivity.this.finish();
    }

    private void loadOpen() {
        disposable.add(ApiUtils.getInstance().getOpenAd()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<HomeModulesBean>>() {
                    @Override
                    public void accept(ResultBean<HomeModulesBean> brandsListBeanResultBean) throws Exception {
                        params = brandsListBeanResultBean.getData().getModules().get(0).getParams();
                        if (null != params) {
                            loadTime = 5000;
                            startCountDown();
                            openPic = params.getImagesrc();
                            tvOpenPic.setVisibility(View.VISIBLE);
                            tvOpenPic.setOnClickListener(onClickListener);
                            ivOpenPic.setOnClickListener(onClickListener);
                            setOpenPic();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tvOpenPic) {
                startNextActivity();
                SplashActivity.this.finish();
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
                    GoodsDetailslNewActivity.startActivity(SplashActivity.this, value);
                    UmengEventUtil.bannerEvent(SplashActivity.this, "商品");
                    break;
                case HomeModulesBean.ITEM_SHOP:
                    ShopMainActivity.startShopMainActivity(SplashActivity.this, value);
                    UmengEventUtil.bannerEvent(SplashActivity.this, "店铺");
                    break;
                case HomeModulesBean.ITEM_CLASS:
                    EventManager.getInstance().notify(null, ConstantMsg.MSG_PAGE_CLASS);
                    UmengEventUtil.bannerEvent(SplashActivity.this, "全部");
                    break;
                case HomeModulesBean.ITEM_BRAND:
                    GoodsListNewActivity.startGoodsListNewActivity(SplashActivity.this, value, "", "");
                    UmengEventUtil.bannerEvent(SplashActivity.this, "品牌");
                    break;
                case HomeModulesBean.ITEM_AUCTION:
                    EventManager.getInstance().notify(null, BaseApplication.HIDE_AUCTION ? ConstantMsg.MSG_PAGE_CLASS : ConstantMsg.MSG_PAGE_AUCTION);
                    UmengEventUtil.bannerEvent(SplashActivity.this, "竞拍专区");
                    break;
                case HomeModulesBean.ITEM_ACTIVITY:
                    FestivalActivity.startActivity(SplashActivity.this);
                    UmengEventUtil.bannerEvent(SplashActivity.this, "店铺活动");
                    break;
                case HomeModulesBean.ITEM_WINERY:
                    startActivity(new Intent(SplashActivity.this, StoreListActivity.class));
                    UmengEventUtil.bannerEvent(SplashActivity.this, "名庄查询");
                    break;
                case HomeModulesBean.ITEM_EVALUATION:
                    if (BaseApplication.isLogin()) {
                        startActivity(new Intent(SplashActivity.this, ValuationActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    UmengEventUtil.bannerEvent(SplashActivity.this, "名酒估价");
                    break;
                case HomeModulesBean.ITEM_MEMBER:
                    EventManager.getInstance().notify(null, ConstantMsg.MSG_PAGE_MY);
                    UmengEventUtil.bannerEvent(SplashActivity.this, "我的");
                    break;
                case HomeModulesBean.ITEM_CART:
                    EventManager.getInstance().notify(null, ConstantMsg.MSG_PAGE_CART);
                    UmengEventUtil.bannerEvent(SplashActivity.this, "我要买酒");
                    break;
                case HomeModulesBean.ITEM_H5:
                    WebViewActivity.startWebViewActivity(SplashActivity.this, value, "");
                    UmengEventUtil.bannerEvent(SplashActivity.this, "H5");
                    break;
                case HomeModulesBean.ITEM_SELLWINE:
                    startActivity(new Intent(SplashActivity.this, AddGoodsActivity.class));
                    UmengEventUtil.bannerEvent(SplashActivity.this, "我要卖酒");
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
        disposable.dispose();
    }
}
