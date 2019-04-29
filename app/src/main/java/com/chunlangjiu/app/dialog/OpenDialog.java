package com.chunlangjiu.app.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseApplication;
import com.chunlangjiu.app.amain.activity.LoginMainActivity;
import com.chunlangjiu.app.amain.bean.HomeModulesBean;
import com.chunlangjiu.app.goods.activity.FestivalActivity;
import com.chunlangjiu.app.goods.activity.GoodsDetailslNewActivity;
import com.chunlangjiu.app.goods.activity.GoodsListNewActivity;
import com.chunlangjiu.app.goods.activity.ShopMainActivity;
import com.chunlangjiu.app.goods.activity.ValuationActivity;
import com.chunlangjiu.app.store.activity.StoreListActivity;
import com.chunlangjiu.app.user.activity.AddGoodsActivity;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.UmengEventUtil;
import com.chunlangjiu.app.web.WebViewActivity;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.util.SizeUtils;

/**
 * @CreatedbBy: liucun on 2018/10/31
 * @Describe:
 */
public class OpenDialog extends Dialog {

    private Activity context;
    private CallBack callBack;

    private ImageView ivOpenPic;
    private TextView tvOpenPic;

    int loadTime = 5000;


    HomeModulesBean.Params picParams;

    public OpenDialog(Activity context, HomeModulesBean.Params params) {
        super(context, R.style.dialog_fullscreen);
        this.context = context;
        this.picParams = params;
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.splash_activity, null);
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = SizeUtils.getScreenWidth();
        params.height = SizeUtils.getScreenHeight();
        window.setAttributes(params);
        window.setGravity(Gravity.CENTER);

        if (Build.VERSION.SDK_INT >= 21) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(view);// 设置布局

        tvOpenPic = findViewById(R.id.tvOpenPic);
        ivOpenPic = findViewById(R.id.ivOpenPic);


        tvOpenPic.setOnClickListener(onClickListener);
        ivOpenPic.setOnClickListener(onClickListener);

        if (null != picParams) {
            loadTime = 5000;
            tvOpenPic.setVisibility(View.VISIBLE);
            tvOpenPic.setOnClickListener(onClickListener);
            ivOpenPic.setOnClickListener(onClickListener);
            Glide.with(context).load(picParams.getImagesrc()).into(ivOpenPic);
            startCountDown();
        }
    }

    private void startCountDown() {
        tvOpenPic.setText(context.getString(R.string.openPicStr, loadTime / 1000 + ""));
        handler.removeMessages(1);
        handler.sendEmptyMessageDelayed(1, 1000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loadTime = loadTime - 1000;
            if (loadTime <= 0) {
                dismiss();
            } else {
                startCountDown();
            }
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ivOpenPic) {
                functionJump();
            }
            dismiss();
        }
    };

    public void functionJump() {
        if (null == picParams)
            return;
        String type = picParams.getLinktype();
        String value = picParams.getLink();
        if (type != null) {
            switch (type) {
                case HomeModulesBean.ITEM_GOODS:
                    GoodsDetailslNewActivity.startActivity(context, value);
                    UmengEventUtil.bannerEvent(context, "商品");
                    break;
                case HomeModulesBean.ITEM_SHOP:
                    ShopMainActivity.startShopMainActivity(context, value);
                    UmengEventUtil.bannerEvent(context, "店铺");
                    break;
                case HomeModulesBean.ITEM_CLASS:
                    EventManager.getInstance().notify(null, ConstantMsg.MSG_PAGE_CLASS);
                    UmengEventUtil.bannerEvent(context, "全部");
                    break;
                case HomeModulesBean.ITEM_BRAND:
                    GoodsListNewActivity.startGoodsListNewActivity(context, "", "", value, "", "");
                    UmengEventUtil.bannerEvent(context, "品牌");
                    break;
                case HomeModulesBean.ITEM_AUCTION:
                    EventManager.getInstance().notify(null, BaseApplication.HIDE_AUCTION ? ConstantMsg.MSG_PAGE_CLASS : ConstantMsg.MSG_PAGE_AUCTION);
                    UmengEventUtil.bannerEvent(context, "竞拍专区");
                    break;
                case HomeModulesBean.ITEM_ACTIVITY:
                    FestivalActivity.startActivity(context);
                    UmengEventUtil.bannerEvent(context, "店铺活动");
                    break;
                case HomeModulesBean.ITEM_WINERY:
                    context.startActivity(new Intent(context, StoreListActivity.class));
                    UmengEventUtil.bannerEvent(context, "名庄查询");
                    break;
                case HomeModulesBean.ITEM_EVALUATION:
                    if (BaseApplication.isLogin()) {
                        context.startActivity(new Intent(context, ValuationActivity.class));
                    } else {
                        LoginMainActivity.startLoginActivity(context);
                    }
                    UmengEventUtil.bannerEvent(context, "名酒估价");
                    break;
                case HomeModulesBean.ITEM_MEMBER:
                    EventManager.getInstance().notify(null, ConstantMsg.MSG_PAGE_MY);
                    UmengEventUtil.bannerEvent(context, "我的");
                    break;
                case HomeModulesBean.ITEM_CART:
                    EventManager.getInstance().notify(null, ConstantMsg.MSG_PAGE_CART);
                    UmengEventUtil.bannerEvent(context, "我要买酒");
                    break;
                case HomeModulesBean.ITEM_H5:
                    WebViewActivity.startWebViewActivity(context, value, "");
                    UmengEventUtil.bannerEvent(context, "H5");
                    break;
                case HomeModulesBean.ITEM_SELLWINE:
                    context.startActivity(new Intent(context, AddGoodsActivity.class));
                    UmengEventUtil.bannerEvent(context, "我要卖酒");
                    break;
            }
        }
    }

    public interface CallBack {
        void onConfirm();
    }
}
