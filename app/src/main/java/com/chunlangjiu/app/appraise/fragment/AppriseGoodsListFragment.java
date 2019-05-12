package com.chunlangjiu.app.appraise.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseFragment;
import com.chunlangjiu.app.amain.bean.ListBean;
import com.chunlangjiu.app.appraise.activity.AppraiseAssessActivity;
import com.chunlangjiu.app.appraise.activity.AppraiseResultActivity;
import com.chunlangjiu.app.appraise.adapter.AppraiseGoodsAdapter;
import com.chunlangjiu.app.appraise.bean.AppraiseGoodsBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.PageUtils;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/9/27
 * @Describe:
 */
public class AppriseGoodsListFragment extends BaseFragment {

    private Activity activity;
    private SmartRefreshLayout refreshLayout;
    //分类列表
    private RecyclerView rvAppraiserList;
    private boolean isSeller = false;//是否卖家
    private String status = "";

    private CompositeDisposable disposable;
    PageUtils<AppraiseGoodsBean> pageUtils = new PageUtils<>();
    AppraiseGoodsAdapter appraiseGoodsAdapter;


    public static AppriseGoodsListFragment newInstance(boolean isSeller, String status) {
        Bundle bundle = new Bundle();
        AppriseGoodsListFragment goodsFragment = new AppriseGoodsListFragment();
        bundle.putString("status", status);
        bundle.putBoolean("isSeller", isSeller);
        goodsFragment.setArguments(bundle);
        return goodsFragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void getContentView(LayoutInflater inflater, ViewGroup container) {
        inflater.inflate(R.layout.appraiser_goods_list_fragment, container, true);
    }


    @Override
    public void initView() {
        EventManager.getInstance().registerListener(onNotifyListener);
        status = getArguments().getString("status");
        isSeller = getArguments().getBoolean("isSeller");


        disposable = new CompositeDisposable();
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getAppraiseGoodsList(pageUtils.nextPage());
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getAppraiseGoodsList(pageUtils.firstPage());

            }
        });
        rvAppraiserList = rootView.findViewById(R.id.rvAppraiserGoodsList);
        appraiseGoodsAdapter = new AppraiseGoodsAdapter(activity, isSeller, pageUtils.getList());
        appraiseGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AppraiseGoodsBean item = appraiseGoodsAdapter.getItem(position);
                if ("true".equals(item.getStatus())) {
                    AppraiseResultActivity.startAppraiserResultActivity(activity, item.getChateau_id());
                } else {
                    if (isSeller) {
                        AppraiseAssessActivity.startAppraiserAssessActivity(activity, item.getChateau_id());
                    } else {
                        AppraiseResultActivity.startAppraiserResultActivity(activity, item.getChateau_id());
                    }
                }

            }
        });
        appraiseGoodsAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.common_empty_view, (ViewGroup) rvAppraiserList.getParent(), false));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
        rvAppraiserList.setLayoutManager(gridLayoutManager);
        rvAppraiserList.addItemDecoration(new GridSpacingItemDecoration(2, Utils.dp2px(activity, 5), false));
        rvAppraiserList.setAdapter(appraiseGoodsAdapter);

    }


    @Override
    public void initData() {
        getAppraiseGoodsList(pageUtils.firstPage());
    }


    private void getAppraiseGoodsList(int page) {
        boolean tempStatus = "1".equals(status);
        Flowable<ResultBean<ListBean<AppraiseGoodsBean>>> flowable;
        if (isSeller) {
            flowable = ApiUtils.getInstance().getAppraiseShopGoodsList(tempStatus, page, 10);
        } else {
            flowable = ApiUtils.getInstance().getAppraiseGoodsList(tempStatus, page, 10);
        }

        disposable.add(flowable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<ListBean<AppraiseGoodsBean>>>() {
                    @Override
                    public void accept(ResultBean<ListBean<AppraiseGoodsBean>> result) throws Exception {
                        resetRefreshLayout();
                        pageUtils.loadListSuccess(result.getData().getList());
                        appraiseGoodsAdapter.setNewData(pageUtils.getList());
                        if (goodsNum != null) {
                            goodsNum.getGoodsNum(result.getData().getFalse_count(), result.getData().getTrue_count());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        resetRefreshLayout();
                    }
                }));
    }


    private void resetRefreshLayout() {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }


    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            switch (eventTag) {
                case ConstantMsg.APPRAISE_GOODS_SUCCESS:
                    initData();
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        EventManager.getInstance().unRegisterListener(onNotifyListener);
    }

    public GoodsNum goodsNum;

    public GoodsNum getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(GoodsNum goodsNum) {
        this.goodsNum = goodsNum;
    }

    public interface GoodsNum {
        public void getGoodsNum(int falseNum, int trueNum);
    }
}
