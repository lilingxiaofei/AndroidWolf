package com.chunlangjiu.app.goods.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;

import butterknife.OnClick;

/**
 * @CreatedbBy: liucun on 2018/7/15.
 * @Describe:
 */
public class AuctionExplainActivity extends BaseActivity implements View.OnClickListener{

    @Override
    public void setTitleView() {
        titleName.setText("竞拍说明");
    }

    public static void startActivity(Activity activity){
        if(activity!=null){
            Intent intent = new Intent(activity, AuctionExplainActivity.class);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auction_explain_activity);
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
