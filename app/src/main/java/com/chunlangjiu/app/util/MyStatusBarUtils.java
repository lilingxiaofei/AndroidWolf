package com.chunlangjiu.app.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.chunlangjiu.app.R;

/**
 * Created by Administrator on 2019/1/22.
 */

public class MyStatusBarUtils {


    /**
     *
     View.SYSTEM_UI_FLAG_VISIBLE：显示状态栏，Activity不全屏显示(恢复到有状态的正常情况)。
     View.INVISIBLE：隐藏状态栏，同时Activity会伸展全屏显示。
     View.SYSTEM_UI_FLAG_FULLSCREEN：Activity全屏显示，且状态栏被隐藏覆盖掉。
     View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住。
     View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
     View.SYSTEM_UI_LAYOUT_FLAGS：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
     View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键。
     View.SYSTEM_UI_FLAG_LOW_PROFILE：状态栏显示处于低能显示状态(low profile模式)，状态栏上一些图标显示会被隐藏。
     ---------------------
     * @param context
     * @param colorResId
     */
    public static void setStatusBar(Activity context,int colorResId){
        if(Build.VERSION.SDK_INT >= 21) {
            Window window = context.getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }else{
            ViewGroup decorView = (ViewGroup) context.getWindow().getDecorView();
            View statusBarView = new View(context);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(context));
            statusBarView.setBackgroundColor(colorResId);
            decorView.addView(statusBarView, lp);
        }

    }

    public static void setTitleBarPadding(Activity context, View view){
        if(Build.VERSION.SDK_INT >= 21 && null != view) {
        // 获得状态栏高度
        int statusBarHeight = getStatusBarHeight(context);
        view.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    /**
     * 设置页面最外层布局 FitsSystemWindows 属性
     * @param value
     */
    public static void setFitsSystemWindows(View view, boolean value) {
        if (view != null && Build.VERSION.SDK_INT >= 14) {
            view.setFitsSystemWindows(value);
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}
