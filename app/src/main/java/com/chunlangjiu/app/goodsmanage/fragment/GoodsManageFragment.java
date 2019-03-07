package com.chunlangjiu.app.goodsmanage.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseFragment;
import com.chunlangjiu.app.amain.bean.ItemListBean;
import com.chunlangjiu.app.amain.bean.ListBean;
import com.chunlangjiu.app.goodsmanage.adapter.GoodsManageAdapter;
import com.chunlangjiu.app.goodsmanage.bean.GoodsBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.PageUtils;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/9/11
 * @Describe:
 */
public class GoodsManageFragment extends BaseFragment {

    SmartRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    GoodsManageAdapter goodsManageAdapter ;

    private CompositeDisposable disposable;
    private PageUtils<GoodsBean> pageUtils ;
    private String status = "";
    private String time ;


    public static GoodsManageFragment newInstance(String status) {
        GoodsManageFragment auctionDetailFragment = new GoodsManageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("status", status);
        auctionDetailFragment.setArguments(bundle);
        return auctionDetailFragment;
    }


    @Override
    public void getContentView(LayoutInflater inflater, ViewGroup container) {
        inflater.inflate(R.layout.user_goodsmanage_fragment, container, true);
    }

    @Override
    public void initView() {
        pageUtils = new PageUtils<>();
        disposable = new CompositeDisposable();
        status = getArguments().getString("status");

        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        goodsManageAdapter = new GoodsManageAdapter(getActivity(),pageUtils.getList());
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, Utils.dp2px(activity, 5), false));
        recyclerView.setAdapter(goodsManageAdapter);
        goodsManageAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.common_empty_view, (ViewGroup) recyclerView.getParent(), false));
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getGoodsList(pageUtils.nextPage());
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getGoodsList(pageUtils.firstPage());
            }
        });
    }

    @Override
    public void initData() {
        getGoodsList(1);
    }

    public void getGoodsList(int page) {
        disposable.add(ApiUtils.getInstance().getManageGoodsList(status,time,page,pageUtils.getPageSize())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<ItemListBean<GoodsBean>>>() {
                    @Override
                    public void accept(ResultBean<ItemListBean<GoodsBean>> result) throws Exception {
                        resetGoodsList();
                        if(result.getErrorcode() == 0){
                            pageUtils.loadListSuccess(result.getData().getItem_list());
                            pageUtils.setTotalPage(result.getData().getTotalPage());
                            updateLListUi();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        resetGoodsList();
                    }
                }));
    }

    private void resetGoodsList(){
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }

    private void updateLListUi(){
        goodsManageAdapter.setNewData(pageUtils.getList());
        refreshLayout.setEnableLoadMore(pageUtils.isNextPage());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }


}
