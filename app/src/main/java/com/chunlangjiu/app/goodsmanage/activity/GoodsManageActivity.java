package com.chunlangjiu.app.goodsmanage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.abase.BaseFragmentAdapter;
import com.chunlangjiu.app.goods.activity.ShopMainActivity;
import com.chunlangjiu.app.goodsmanage.fragment.GoodsManageFragment;
import com.chunlangjiu.app.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GoodsManageActivity extends BaseActivity {

    @BindView(R.id.tabTitle)
    TabLayout tabLayout;

    @BindView(R.id.tvTime)
    ViewPager tvTime;
    @BindView(R.id.vpContent)
    ViewPager vpContent;

    private String goodsStatus;


    private List<Fragment> fragments;
    private BaseFragmentAdapter myFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_goods_audit);
        initView();
        initData();
    }

    @Override
    public void setTitleView() {
        titleImgLeft.setOnClickListener(onClickListener);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    public static void startGoodsManageActivity(Activity activity, String goodsStatus) {
        Intent intent = new Intent(activity, GoodsManageActivity.class);
        intent.putExtra("goodsStatus", goodsStatus);
        activity.startActivity(intent);
    }


    private void initView() {
        tvTime.setOnClickListener(onClickListener);
        goodsStatus = getIntent().getStringExtra("goodsStatus");
        if (TextUtils.isEmpty(goodsStatus)) {
            finish();
        }
        fragments = new ArrayList<>();
        switch (goodsStatus) {
            case CommonUtils.GOODS_STATUS_AUDIT_PENDING:
                titleName.setText("审核商品");
                tabLayout.addTab(tabLayout.newTab().setText("审核中"));
                tabLayout.addTab(tabLayout.newTab().setText("审核驳回"));
                tabLayout.setVisibility(View.VISIBLE);
                fragments.add(GoodsManageFragment.newInstance( CommonUtils.GOODS_STATUS_AUDIT_PENDING));
                fragments.add(GoodsManageFragment.newInstance( CommonUtils.GOODS_STATUS_AUDIT_REFUSE));
                break;
            case  CommonUtils.GOODS_STATUS_AUDIT_REFUSE:
                titleName.setText("审核商品");
                tabLayout.addTab(tabLayout.newTab().setText("审核中"));
                tabLayout.addTab(tabLayout.newTab().setText("审核驳回"));
                tabLayout.setVisibility(View.VISIBLE);
                fragments.add(GoodsManageFragment.newInstance( CommonUtils.GOODS_STATUS_AUDIT_PENDING));
                fragments.add(GoodsManageFragment.newInstance( CommonUtils.GOODS_STATUS_AUDIT_REFUSE));
                break;
            case  CommonUtils.GOODS_STATUS_SELL:
                titleName.setText("在售商品");
                tabLayout.setVisibility(View.GONE);
                fragments.add(GoodsManageFragment.newInstance( CommonUtils.GOODS_STATUS_SELL));
                break;
            case  CommonUtils.GOODS_STATUS_INSTOCK:
                titleName.setText("仓库商品");
                tabLayout.setVisibility(View.GONE);
                fragments.add(GoodsManageFragment.newInstance( CommonUtils.GOODS_STATUS_INSTOCK));
                break;
        }
        myFragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager());
        myFragmentAdapter.setLists(fragments);
        vpContent.setAdapter(myFragmentAdapter);
        tabLayout.setupWithViewPager(vpContent);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.img_title_left:
                    finish();
                    break;
                case R.id.tvTime:

                    break;
            }
        }
    };

    private void initData() {

    }
}

