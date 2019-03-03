package com.chunlangjiu.app.order.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.abase.BaseFragmentAdapter;
import com.chunlangjiu.app.order.fragment.OrderListFragment;
import com.chunlangjiu.app.order.params.OrderParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OrderMainActivity extends BaseActivity {
    @BindView(R.id.tabTitle)
    TabLayout tabLayout;
    @BindView(R.id.vpContent)
    ViewPager vpContent;

    private List<Fragment> fragments;
    private BaseFragmentAdapter myFragmentAdapter;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_main);

        initView();
        initData();
    }

    @Override
    public void setTitleView() {
        titleImgLeft.setOnClickListener(onClickListener);
    }

    private void initView() {
        type = getIntent().getIntExtra(OrderParams.TYPE, 0);
        int target = getIntent().getIntExtra(OrderParams.TARGET, 0);

        fragments = new ArrayList<>();
        fragments.add(new OrderListFragment());
        fragments.add(new OrderListFragment());
        fragments.add(new OrderListFragment());
        fragments.add(new OrderListFragment());
        fragments.add(new OrderListFragment());
        myFragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager());
        myFragmentAdapter.setLists(fragments);
        vpContent.setAdapter(myFragmentAdapter);
        vpContent.addOnPageChangeListener(onPageChangeListener);
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
        fillTab(type, target);

    }

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            TabLayout.Tab tabAt = tabLayout.getTabAt(position);
            if (null != tabAt) {
                tabAt.select();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int position = tab.getPosition();
            Bundle bundle = new Bundle();
            //由于竞拍订单在现在排在了6到10 ；了所以修改逻辑
            int tempType = 0 ;
            int temPosition = 0 ;
            if(type == 0 && position>4){
                tempType = 1 ;
                temPosition = position -5 ;
            }else{
                tempType = type ;
                temPosition = position ;
            }
            bundle.putInt(OrderParams.TYPE, tempType);
            bundle.putInt(OrderParams.TARGET, temPosition);
            fragments.get(position).setArguments(bundle);
            vpContent.setCurrentItem(position);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    private void fillTab(int type, int target) {
        switch (type) {
            case 0:
                titleName.setText("我的订单");
                tabLayout.addTab(tabLayout.newTab().setText("全部"));
                tabLayout.addTab(tabLayout.newTab().setText("待付款"));
                tabLayout.addTab(tabLayout.newTab().setText("待发货"));
                tabLayout.addTab(tabLayout.newTab().setText("待收货"));
                tabLayout.addTab(tabLayout.newTab().setText("已完成"));
                break;
            case 1:
                titleName.setText("竞拍订单管理");
                tabLayout.addTab(tabLayout.newTab().setText("全部"));
                tabLayout.addTab(tabLayout.newTab().setText("待付定金"));
                tabLayout.addTab(tabLayout.newTab().setText("竞拍中"));
                tabLayout.addTab(tabLayout.newTab().setText("已中标"));
                tabLayout.addTab(tabLayout.newTab().setText("待收货"));
                break;
            case 2:
                titleName.setText("售后订单");
                tabLayout.addTab(tabLayout.newTab().setText("全部"));
                tabLayout.addTab(tabLayout.newTab().setText("待处理"));
                tabLayout.addTab(tabLayout.newTab().setText("待退货"));
                tabLayout.addTab(tabLayout.newTab().setText("待退款"));
                tabLayout.addTab(tabLayout.newTab().setText("退款完成"));
                break;
            case 3:
                titleName.setText("订单管理");
                tabLayout.addTab(tabLayout.newTab().setText("全部"));
                tabLayout.addTab(tabLayout.newTab().setText("待付款"));
                tabLayout.addTab(tabLayout.newTab().setText("待发货"));
                tabLayout.addTab(tabLayout.newTab().setText("待收货"));
                tabLayout.addTab(tabLayout.newTab().setText("已完成"));
                break;
            case 4:
                titleName.setText("售后订单");
                tabLayout.addTab(tabLayout.newTab().setText("全部"));
                tabLayout.addTab(tabLayout.newTab().setText("待处理"));
                tabLayout.addTab(tabLayout.newTab().setText("待退货"));
                tabLayout.addTab(tabLayout.newTab().setText("待退款"));
                tabLayout.addTab(tabLayout.newTab().setText("退款完成"));
                break;
            case 5:
                titleName.setText("取消订单");
                tabLayout.addTab(tabLayout.newTab().setText("全部"));
                tabLayout.setVisibility(View.GONE);
                break;
        }
        TabLayout.Tab tabAt = tabLayout.getTabAt(target);
        if (null != tabAt) {
            tabAt.select();
        }
    }

    private void initData() {

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

}
