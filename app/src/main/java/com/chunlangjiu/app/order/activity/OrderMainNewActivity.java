package com.chunlangjiu.app.order.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.abase.BaseFragmentAdapter;
import com.chunlangjiu.app.order.fragment.OrderListFragment;
import com.chunlangjiu.app.order.params.OrderParams;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OrderMainNewActivity extends BaseActivity {
    @BindView(R.id.rgOrderTab)
    RadioGroup rgOrderTab;
    @BindView(R.id.rbOrderGoods)
    RadioButton rbOrderGoods;
    @BindView(R.id.rbOrderAction)
    RadioButton rbOrderAction;
    @BindView(R.id.tabTitle)
    TabLayout tabLayout;
    @BindView(R.id.vpContent)
    ViewPager vpContent;

    private List<Fragment> fragments;
    private BaseFragmentAdapter myFragmentAdapter;
    private int selectedPosition ;

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


        rgOrderTab.setVisibility(View.GONE);
        fragments = new ArrayList<>();
        fragments.add(OrderListFragment.newInstance(type,0));
        fragments.add(OrderListFragment.newInstance(type,1));
        fragments.add(OrderListFragment.newInstance(type,2));
        fragments.add(OrderListFragment.newInstance(type,3));
        fragments.add(OrderListFragment.newInstance(type,4));
        if(type ==0){
            rgOrderTab.setVisibility(View.VISIBLE);
            fragments.add(OrderListFragment.newInstance(1,0));
            fragments.add(OrderListFragment.newInstance(1,1));
            fragments.add(OrderListFragment.newInstance(1,2));
            fragments.add(OrderListFragment.newInstance(1,3));
            fragments.add(OrderListFragment.newInstance(1,4));
        }

        myFragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager());
        myFragmentAdapter.setLists(fragments);
        vpContent.setAdapter(myFragmentAdapter);
        vpContent.addOnPageChangeListener(onPageChangeListener);
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
        rbOrderGoods.setOnClickListener(onClickListener);
        rbOrderAction.setOnClickListener(onClickListener);
        fillTab(type, target);

    }



    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            setSelectItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int position = tab.getPosition();
            if(position<5){
                rbOrderGoods.setChecked(true);
                rbOrderGoods.setTag(R.id.position,position);
            }else{
                rbOrderAction.setChecked(true);
                rbOrderAction.setTag(R.id.position,position);
            }
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
                tabLayout.addTab(tabLayout.newTab().setText("待付定金"));
                tabLayout.addTab(tabLayout.newTab().setText("竞拍中"));
                tabLayout.addTab(tabLayout.newTab().setText("已中标"));
                tabLayout.addTab(tabLayout.newTab().setText("落标"));
                tabLayout.addTab(tabLayout.newTab().setText("待收货"));
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
        setSelectItem(target);
    }

    private void initData() {

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int resId = view.getId();
            if(resId == R.id.img_title_left){
                finish();
            }else if(resId == R.id.rbOrderGoods){
                Object posTag = rbOrderGoods.getTag(R.id.position);
                int position = posTag == null ?0:(int)posTag;
                setSelectItem(position);
            }else if(resId == R.id.rbOrderAction){
                Object posTag = rbOrderAction.getTag(R.id.position);
                int position = posTag == null ?5:(int)posTag;
                setSelectItem(position);
            }
        }
    };

    private void setSelectItem(int position){
        int size = tabLayout.getTabCount();
        for (int i=0;i<size;i++){
            View view = getTabView(i);
            if(position<5){
                if(i<5){
                    view.setVisibility(View.VISIBLE);
                }else{
                    view.setVisibility(View.GONE);
                }
            }else{
                if(i<5){
                    view.setVisibility(View.GONE);
                }else{
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
        TabLayout.Tab tabAt = tabLayout.getTabAt(position);
        if (null != tabAt) {
            tabAt.select();
        }
    }

    public View getTabView(int index){
        View tabView = null;
        TabLayout.Tab tab = tabLayout.getTabAt(index);
        Field view = null;
        try {
            view = TabLayout.Tab.class.getDeclaredField("mView");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        view.setAccessible(true);
        try {
            tabView = (View) view.get(tab);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return tabView;
    }

}
