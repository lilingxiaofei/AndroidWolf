package com.chunlangjiu.app.user.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.pkqup.commonlibrary.util.AppUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @CreatedbBy: liucun on 2018/7/15.
 * @Describe:
 */
public class AboutActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.tvVersionName)
    private TextView tvVersionName;
    @Override
    public void setTitleView() {
        titleName.setText("关于");
        titleImgLeft.setOnClickListener(this);
    }


    public static void startActivity(Activity activity){
        if(activity!=null){
            Intent intent = new Intent(activity, AboutActivity.class);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        tvVersionName.setText("醇狼"+AppUtils.getAppName());
    }
    @OnClick({R.id.img_title_left})
    @Override
    public void onClick(View view) {
        int resId = view.getId();
        if(resId == R.id.img_title_left){
            finish();
        }
    }
}
