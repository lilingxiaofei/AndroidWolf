package com.chunlangjiu.app.appraise.activity;

import android.os.Bundle;
import android.view.View;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;


/**
 * @CreatedbBy: 吴申飞 on 2018/6/16.
 * @Describe: 新手必看
 */
public class NoviceMustSeeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appraise_activity_beginner_must_see);
    }

    @Override
    public void setTitleView() {
        titleName.setText("新手必看");
        titleImgLeft.setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.img_title_left){
                finish();
            }
        }
    };
}
