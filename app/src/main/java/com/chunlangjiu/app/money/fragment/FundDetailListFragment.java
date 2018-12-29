package com.chunlangjiu.app.money.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseFragment;
import com.chunlangjiu.app.money.activity.BillDetailActivity;
import com.chunlangjiu.app.money.bean.FundDetailBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FundDetailListFragment extends BaseFragment {

    @BindView(R.id.fundDetail)
    RecyclerView fundDetail;
    private List<FundDetailBean> fundDetailBeans = new ArrayList<>();
    private FundDetailAdapter fundDetailAdapter;

    public FundDetailListFragment(){}

    @Override
    public void getContentView(LayoutInflater inflater, ViewGroup container) {
        inflater.inflate(R.layout.fund_detail_list_fragment, container, true);
        ButterKnife.bind(this, contentView);
    }

    @Override
    public void initView() {
        fundDetailAdapter = new FundDetailAdapter(R.layout.fund_details_item,fundDetailBeans);
        fundDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        fundDetail.setAdapter(fundDetailAdapter);
        fundDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                 startActivity(new Intent(getActivity(),BillDetailActivity.class));
            }
        });
    }

    @Override
    public void initData() {
        for (int i=0;i<10;i++){
            fundDetailBeans.add(new FundDetailBean());
        }

    }

    public class FundDetailAdapter extends BaseQuickAdapter<FundDetailBean,BaseViewHolder> {

        public FundDetailAdapter(int layoutResId, @Nullable List<FundDetailBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FundDetailBean item) {

        }
    }
}
