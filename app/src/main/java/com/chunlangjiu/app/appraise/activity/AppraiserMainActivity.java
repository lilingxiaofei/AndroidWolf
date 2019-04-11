package com.chunlangjiu.app.appraise.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.amain.fragment.CartFragment;
import com.chunlangjiu.app.util.MyStatusBarUtils;


/**
 * @CreatedbBy: 吴申飞 on 2018/6/16.
 * @Describe: 鉴定师主页
 */
public class AppraiserMainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity_main);
    }

    @Override
    public void setTitleView() {

    }

}
