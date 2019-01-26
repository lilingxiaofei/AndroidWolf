package com.chunlangjiu.app.goods.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.goods.fragment.GoodsCommentFragment;

/**
 * @CreatedbBy: liucun on 2018/7/15.
 * @Describe:
 */
public class GoodsEvaluateActivity extends BaseActivity implements View.OnClickListener {
    GoodsCommentFragment goodsCommentFragment;

    @Override
    public void setTitleView() {
        titleName.setText("评价列表");
        titleImgLeft.setOnClickListener(this);
    }

    public static void startActivity(Activity activity,String goodsId) {
        if (activity != null) {
            Intent intent = new Intent(activity, GoodsEvaluateActivity.class);
            intent.putExtra("goodsId",goodsId);
            activity.startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        String goodsId = getIntent().getStringExtra("goodsId");
        goodsCommentFragment = GoodsCommentFragment.newInstance(goodsId);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_view, goodsCommentFragment)
                .commit();
    }

    @Override
    public void onClick(View view) {
        int resId = view.getId();
        if (resId == R.id.img_title_left) {
            finish();
        }
    }
}
