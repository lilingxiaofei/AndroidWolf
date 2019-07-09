package com.chunlangjiu.app.cart;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.amain.fragment.CartFragment;
import com.chunlangjiu.app.util.MyStatusBarUtils;


/**
 * @CreatedbBy: liucun on 2018/6/16.
 * @Describe: 购物车
 */
public class CartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity_main);
        MyStatusBarUtils.setStatusBar(this, ContextCompat.getColor(this, R.color.bg_white));
        CartFragment cartFragment = new CartFragment();
        cartFragment.setIsActivity(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.llContent, cartFragment)
                .commit();
    }

    @Override
    public void setTitleView() {
        hideTitleView();
    }

}
