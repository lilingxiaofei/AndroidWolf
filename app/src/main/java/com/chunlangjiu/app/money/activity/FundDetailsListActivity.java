package com.chunlangjiu.app.money.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.money.fragment.FundDetailListFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FundDetailsListActivity extends BaseActivity {

    @BindView(R.id.vpFundDetail)
    ViewPager vpFundDetail;
    @BindView(R.id.titleTabLayout)
    SlidingTabLayout titleTabLayout;

    private FundDetailViewPageAdapter fundDetailViewPageAdapter;
    private ArrayList<Fragment> fragmentList =new ArrayList<>();

    private final String[] mTitles = {"销售记录", "充值", "提现"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_details);
        initView();
    }

    @Override
    public void setTitleView() {
        titleName.setText("资金明细");
    }


    private void initView(){
        fundDetailViewPageAdapter = new FundDetailViewPageAdapter(getSupportFragmentManager());
        vpFundDetail.setAdapter(fundDetailViewPageAdapter);
        for (int i = 0; i < mTitles.length; i++) {
            fragmentList.add(new FundDetailListFragment());
        }
        fundDetailViewPageAdapter.notifyDataSetChanged();
        titleTabLayout.setViewPager(vpFundDetail,mTitles);
    };

    public class FundDetailViewPageAdapter extends FragmentStatePagerAdapter {

        public FundDetailViewPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }

}
