package com.chunlangjiu.app.goods.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.amain.fragment.GoodsFragment;

/**
 * @CreatedbBy: liucun on 2018/9/27
 * @Describe:
 */
public class GoodsListNewActivity extends BaseActivity {

    public static void startGoodsListNewActivity(Activity activity, String classId, String className, String brandId, String brandName, String searchKey) {
        Intent intent = new Intent(activity, GoodsListNewActivity.class);
        intent.putExtra("classId", classId);
        intent.putExtra("className", className);
        intent.putExtra("brandId", brandId);
        intent.putExtra("brandName", brandName);
        intent.putExtra("searchKey", searchKey);
        activity.startActivity(intent);
    }

    @Override
    public void setTitleView() {
        hideTitleView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_activity_goods_list_new);
        String classId = getIntent().getStringExtra("classId");
        String className = getIntent().getStringExtra("className");
        String brandId = getIntent().getStringExtra("brandId");
        String brandName = getIntent().getStringExtra("brandName");
        String searchKey = getIntent().getStringExtra("searchKey");
        GoodsFragment goodsFragment = GoodsFragment.newInstance(searchKey, true,classId,className, brandId, brandName);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, goodsFragment)
                .commit();
    }
}
