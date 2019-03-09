package com.chunlangjiu.app.money.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.money.adapter.FundDetailAdapter;
import com.chunlangjiu.app.money.bean.FundDetailListBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.PageUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FreezeListActivity extends BaseActivity {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private CompositeDisposable disposable;

    private PageUtils<FundDetailListBean.FundDetailBean> pageUtils ;
    private FundDetailAdapter fundDetailAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_recycler_view);
        disposable = new CompositeDisposable();
        pageUtils = new PageUtils<>();
        initView();
        initData();
    }

    public static void startFundListActivity(Activity activity) {
        Intent intent = new Intent(activity, FreezeListActivity.class);
        activity.startActivity(intent);
    }

    private void initData() {
        getFundDetails(pageUtils.firstPage());
    }

    private void getFundDetails(int page) {
        disposable.add(ApiUtils.getInstance().getFreezeList(page,10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<FundDetailListBean>>() {
                    @Override
                    public void accept(ResultBean<FundDetailListBean> resultBean) throws Exception {
                        resetRequest();
                        List<FundDetailListBean.FundDetailBean> fundDetailBeanList = resultBean.getData().getList();
                        if (null != fundDetailBeanList) {
                            pageUtils.loadListSuccess(fundDetailBeanList);
                            fundDetailAdapter.setNewData(pageUtils.getList());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        resetRequest();
                    }
                }));

    }

    private void resetRequest(){
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }

    @Override
    public void setTitleView() {
        titleName.setText("冻结资金");
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
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getFundDetails(pageUtils.nextPage());
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getFundDetails(pageUtils.firstPage());
            }
        });
        fundDetailAdapter = new FundDetailAdapter(R.layout.fund_details_item, pageUtils.getList());
        fundDetailAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.common_empty_view, (ViewGroup) recyclerView.getParent(), false));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(fundDetailAdapter);
        fundDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(FreezeListActivity.this, BillDetailActivity.class);
                intent.putExtra("log_id", String.valueOf(pageUtils.get(position).getLog_id()));
                intent.putExtra("amount",pageUtils.get(position).getFee());
                intent.putExtra("type","freeze");
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != disposable)
            disposable.dispose();
    }
}
