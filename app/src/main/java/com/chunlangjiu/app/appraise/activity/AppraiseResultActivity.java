package com.chunlangjiu.app.appraise.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.appraise.bean.AppraiseGoodsBean;


/**
 * @CreatedbBy: 吴申飞 on 2018/6/16.
 * @Describe: 鉴定结果
 */
public class AppraiseResultActivity extends BaseActivity {

    private String appraiseGoodsId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity_main);
        initData();
    }

    private void initData(){
        appraiseGoodsId = getIntent().getStringExtra("appraiseGoodsId");
    }

    @Override
    public void setTitleView() {
        titleName.setText("鉴别结果");
        titleImgLeft.setOnClickListener(onClickListener);
    }

    public static void startAppraiserResultActivity(Activity activity, AppraiseGoodsBean appraiseGoods){
        if(activity != null){
            Intent intent = new Intent(activity,AppraiseResultActivity.class);
            intent.putexs("appraiseGoods",appraiseGoods);
            activity.startActivity(intent);
        }
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };
}
