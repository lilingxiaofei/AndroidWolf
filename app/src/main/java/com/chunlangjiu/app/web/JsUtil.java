package com.chunlangjiu.app.web;

import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.chunlangjiu.app.amain.activity.LoginMainActivity;
import com.chunlangjiu.app.goods.activity.GoodsDetailslNewActivity;
import com.chunlangjiu.app.user.activity.AddGoodsActivity;
import com.chunlangjiu.app.user.activity.EditGoodsActivity;
import com.chunlangjiu.app.util.ConstantMsg;
import com.pkqup.commonlibrary.eventmsg.EventManager;

/**
 * @CreatedbBy: liucun on 2018/9/23.
 * @Describe:
 */
public class JsUtil {

    private WebViewActivity activity;

    public JsUtil(WebViewActivity activity) {
        this.activity = activity;
    }


    //退出登录
    @JavascriptInterface
    public void logout() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EventManager.getInstance().notify(null, ConstantMsg.LOGOUT_SUCCESS);
                activity.finish();
            }
        });
    }

    //跳转新页面
    @JavascriptInterface
    public void pushWeb(final String url, final String title) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WebViewActivity.startWebViewActivity(activity, url, title);
            }
        });
    }

    //关闭当前页
    @JavascriptInterface
    public void closePage() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.finish();
                EventManager.getInstance().notify(null, ConstantMsg.UPDATE_WEBVIEW);
            }
        });
    }

    //跳转到商品详情
    @JavascriptInterface
    public void goodsDetail(final String item_id) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GoodsDetailslNewActivity.startActivity(activity, item_id);
            }
        });
    }

    //登录
    @JavascriptInterface
    public void login() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoginMainActivity.startLoginActivity(activity);
            }
        });
    }

    //编辑商品
    @JavascriptInterface
    public void editGoodsDetail(final String item_id) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditGoodsActivity.startEditGoodsDetailsActivity(activity, item_id);
            }
        });
    }

    //添加商品
    @JavascriptInterface
    public void addGoods() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              activity.startActivity(new Intent(activity, AddGoodsActivity.class));
            }
        });
    }

    //设置竞拍起始时间
    @JavascriptInterface
    public void showStartTimeDialog() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.showStartTime();
            }
        });
    }

    //设置竞拍结束时间
    @JavascriptInterface
    public void showEndTimeDialog() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.showEndTime();
            }
        });
    }

}
