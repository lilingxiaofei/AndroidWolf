package com.chunlangjiu.app.web;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.util.ConstantMsg;
import com.jaeger.library.StatusBarUtil;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.pkqup.commonlibrary.eventmsg.EventManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @CreatedbBy: liucun on 2018/8/27
 * @Describe:
 */
public class WebViewActivity extends FragmentActivity {

    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.img_close)
    ImageView img_close;
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.webView)
    WebView webView;

    private TimePickerDialog startTimeDialog;
    private TimePickerDialog endTimeDialog;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_back:
                    webGoBack();
                    break;
                case R.id.img_close:
                    finish();
                    break;

            }
        }

    };
    private String url;
    private String title;

    public static void startWebViewActivity(Activity activity, String url, String title) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity_webview);
        EventManager.getInstance().registerListener(onNotifyListener);
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.bg_red),0);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        img_back.setOnClickListener(onClickListener);
        img_close.setOnClickListener(onClickListener);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置同时支持http和https加载
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);// 设置默认缓存模式
        webView.getSettings().setBlockNetworkImage(false);// 解除网络图片数据阻止
        webView.getSettings().setLoadsImagesAutomatically(true);// 支持自动加载图片
        webView.getSettings().setDomStorageEnabled(true);// 设置是否开启DOM存储API权限
        webView.getSettings().setAppCacheEnabled(true);// 设置Application缓存API是否开启
        webView.getSettings().setAppCachePath(getCacheDir().getAbsolutePath());// 设置缓存路径
        webView.getSettings().setAllowFileAccess(true);// 设置在WebView内部是否允许访问文件
        webView.getSettings().setAllowContentAccess(true);// 设置WebView是否使用其内置的变焦机制
        webView.getSettings().setDisplayZoomControls(true);// 设置WebView使用内置缩放机制时，是否展现在屏幕缩放控件上
        webView.getSettings().setUseWideViewPort(true);// 将图片调整到适合WebView的大小
        webView.setHorizontalScrollBarEnabled(false);//水平不显示滚动条
        webView.setVerticalScrollBarEnabled(false); //垂直不显示滚动条
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//设置允许js弹框
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);// 设置js可用
        webView.addJavascriptInterface(new JsUtil(this), "chunlang");
    }

    private void initData() {
        title = getIntent().getStringExtra("title");
        tv_title.setText(title);
        url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
    }

    public void showStartTime() {
        if (startTimeDialog == null) {
            startTimeDialog = new TimePickerDialog.Builder().setCallBack(startDataSetListener)
                    .setCancelStringId("取消")
                    .setSureStringId("确定")
                    .setTitleStringId("")
                    .setYearText("年")
                    .setMonthText("月")
                    .setDayText("日")
                    .setHourText("时")
                    .setMinuteText("分")
                    .setCyclic(false)
                    .setMinMillseconds(System.currentTimeMillis())
                    .setCurrentMillseconds(System.currentTimeMillis())
                    .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                    .setType(Type.ALL)
                    .setWheelItemTextNormalColor(
                            getResources().getColor(R.color.timetimepicker_default_text_color))
                    .setWheelItemTextSelectorColor(
                            getResources().getColor(R.color.timepicker_toolbar_bg))
                    .setWheelItemTextSize(12).build();
        }
        startTimeDialog.show(getSupportFragmentManager(), "all");
    }

    private OnDateSetListener startDataSetListener = new OnDateSetListener() {
        @Override
        public void onDateSet(TimePickerDialog timePickerView, long millSeconds) {
            long startTime = millSeconds / 1000;
            webView.loadUrl("javascript:setStartTime(" + startTime + ")");
        }
    };

    public void showEndTime() {
        if (endTimeDialog == null) {
            endTimeDialog = new TimePickerDialog.Builder().setCallBack(endDataSetListener)
                    .setCancelStringId("取消")
                    .setSureStringId("确定")
                    .setTitleStringId("")
                    .setYearText("年")
                    .setMonthText("月")
                    .setDayText("日")
                    .setHourText("时")
                    .setMinuteText("分")
                    .setCyclic(false)
                    .setMinMillseconds(System.currentTimeMillis())
                    .setCurrentMillseconds(System.currentTimeMillis())
                    .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                    .setType(Type.ALL)
                    .setWheelItemTextNormalColor(
                            getResources().getColor(R.color.timetimepicker_default_text_color))
                    .setWheelItemTextSelectorColor(
                            getResources().getColor(R.color.timepicker_toolbar_bg))
                    .setWheelItemTextSize(12).build();
        }
        endTimeDialog.show(getSupportFragmentManager(), "all");
    }

    private OnDateSetListener endDataSetListener = new OnDateSetListener() {
        @Override
        public void onDateSet(TimePickerDialog timePickerView, long millSeconds) {
            long endTime = millSeconds / 1000;
            webView.loadUrl("javascript:setEndTime(" + endTime + ")");
        }
    };

    private void webGoBack() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            finish();
        }
        return true;
    }

    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            updateWebView(eventTag);
        }
    };

    private void updateWebView(String eventTag) {
        try {
            if (eventTag.equals(ConstantMsg.UPDATE_WEBVIEW)) {
                webView.loadUrl(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventManager.getInstance().registerListener(onNotifyListener);
    }
}
