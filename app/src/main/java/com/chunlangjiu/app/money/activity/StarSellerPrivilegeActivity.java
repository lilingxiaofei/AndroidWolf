package com.chunlangjiu.app.money.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;

public class StarSellerPrivilegeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_seller_privilege);
    }

    @Override
    public void setTitleView() {
        titleName.setText("星级卖家特权");
        titleImgLeft.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.img_title_left){
                finish();
            }
        }
    };
}
