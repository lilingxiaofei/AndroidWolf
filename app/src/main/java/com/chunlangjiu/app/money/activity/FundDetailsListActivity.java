package com.chunlangjiu.app.money.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.money.bean.FundDetailListBean;
import com.chunlangjiu.app.money.fragment.FundDetailListFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

public class FundDetailsListActivity extends BaseActivity {

    @BindView(R.id.vpFundDetail)
    ViewPager vpFundDetail;
    @BindView(R.id.titleTabLayout)
    SlidingTabLayout titleTabLayout;

    private CompositeDisposable disposable;
    private FundDetailViewPageAdapter fundDetailViewPageAdapter;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private List<FundDetailListBean.FundDetailBean> fundDetailBeans = new ArrayList<>();

    private final String[] mTitles = {"销售记录", "充值", "提现", "退款"};
    private final String[] types = {"sell", "add", "expense", "refund"};
    public static final String TYPE_FREEZE = "freeze";
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_details);
        disposable = new CompositeDisposable();
        initView();
        initData();
    }

    public static void startFundListActivity(Activity activity, String type) {
        Intent intent = new Intent(activity, FundDetailsListActivity.class);
        intent.putExtra("type", type);
        activity.startActivity(intent);
    }

    private void initData() {
//        getFundDetails();
    }

    @Override
    public void setTitleView() {
        titleName.setText("资金明细");
    }

    @OnClick({R.id.img_title_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_left:
                finish();
                break;
        }
    }

    private void initView() {
        type = getIntent().getStringExtra("type");
        fundDetailViewPageAdapter = new FundDetailViewPageAdapter(getSupportFragmentManager());
        vpFundDetail.setAdapter(fundDetailViewPageAdapter);
        if (!TYPE_FREEZE.equals(type)) {
            for (int i = 0; i < mTitles.length; i++) {
                FundDetailListFragment fundDetailListFragment = new FundDetailListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", types[i]);
                fundDetailListFragment.setArguments(bundle);
                fragmentList.add(fundDetailListFragment);
            }
            fundDetailViewPageAdapter.notifyDataSetChanged();
            titleTabLayout.setViewPager(vpFundDetail, mTitles);
        } else {
            titleTabLayout.setVisibility(View.GONE);
            FundDetailListFragment fundDetailListFragment = new FundDetailListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", type);
            fundDetailListFragment.setArguments(bundle);
            fragmentList.add(fundDetailListFragment);
            fundDetailViewPageAdapter.notifyDataSetChanged();
        }

    }

    ;

    private void getFundDetails() {
//        disposable.add(ApiUtils.getInstance().getFundDetails((String) SPUtils.get("token", ""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<ResultBean<FundDetailListBean>>() {
//                    @Override
//                    public void accept(ResultBean<FundDetailListBean> resultBean) throws Exception {
//                        fundDetailBeans.clear();
//                        List<FundDetailListBean.FundDetailBean> fundDetailBeanList = resultBean.getData().getList();
//                        if (null != fundDetailBeanList) {
//                            fundDetailBeans.addAll(fundDetailBeanList);
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                    }
//                }));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != disposable)
            disposable.dispose();
    }

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
