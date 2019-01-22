package com.chunlangjiu.app.money.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseFragment;
import com.chunlangjiu.app.money.activity.BillDetailActivity;
import com.chunlangjiu.app.money.bean.FundDetailListBean;
import com.chunlangjiu.app.money.bean.UserMoneyBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FundDetailListFragment extends BaseFragment {

    @BindView(R.id.fundDetail)
    RecyclerView fundDetail;
    private List<FundDetailListBean> fundDetailBeans = new ArrayList<>();
    private FundDetailAdapter fundDetailAdapter;
    private CompositeDisposable disposable;

    public FundDetailListFragment() {
    }

    @Override
    public void getContentView(LayoutInflater inflater, ViewGroup container) {
        inflater.inflate(R.layout.fund_detail_list_fragment, container, true);
        ButterKnife.bind(this, contentView);

    }

    @Override
    public void initView() {
        fundDetailAdapter = new FundDetailAdapter(R.layout.fund_details_item, fundDetailBeans);
        fundDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        fundDetail.setAdapter(fundDetailAdapter);
        fundDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getActivity(), BillDetailActivity.class));
            }
        });
    }

    @Override
    public void initData() {
        disposable = new CompositeDisposable();
        for (int i = 0; i < 10; i++) {
            fundDetailBeans.add(new FundDetailListBean());
        }
        getFundDetails();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != disposable) {
            disposable.dispose();
        }
    }

    private void getFundDetails() {
        disposable.add(ApiUtils.getInstance().getFundDetails((String) SPUtils.get("token",""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<FundDetailListBean>>() {
                    @Override
                    public void accept(ResultBean<FundDetailListBean> resultBean) throws Exception {
                        String message = resultBean.getData().getList().get(0).getMessage();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));

    }

    public class FundDetailAdapter extends BaseQuickAdapter<FundDetailListBean, BaseViewHolder> {

        public FundDetailAdapter(int layoutResId, @Nullable List<FundDetailListBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FundDetailListBean item) {

        }
    }
}
