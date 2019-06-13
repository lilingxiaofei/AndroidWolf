package com.chunlangjiu.app.goods.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseFragment;
import com.chunlangjiu.app.appraise.adapter.AppraiseGoodsPicAdapter;
import com.chunlangjiu.app.goods.bean.EvaluateListBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.TimeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class GoodsCommentFragment extends BaseFragment {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<EvaluateListBean.EvaluateDetailBean> lists;
    private View notDataView;

    private CompositeDisposable disposable;
    private String itemId;
    private int pageNo = 1;

    public static GoodsCommentFragment newInstance(String itemId) {
        GoodsCommentFragment goodsCommentFragment = new GoodsCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("itemId", itemId);
        goodsCommentFragment.setArguments(bundle);
        return goodsCommentFragment;
    }

    @Override
    public void getContentView(LayoutInflater inflater, ViewGroup container) {
        inflater.inflate(R.layout.goods_fragment_comment, container, true);
    }

    @Override
    public void initView() {
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        lists = new ArrayList<>();
        commentAdapter = new CommentAdapter(R.layout.goods_item_details_evaluate, lists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(commentAdapter);

        refreshLayout.setEnableAutoLoadMore(false);//关闭自动加载更多
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getEvaluateList(1, true);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                getEvaluateList(pageNo + 1, false);
            }
        });

        itemId = getArguments().getString("itemId");
        disposable = new CompositeDisposable();
        notDataView = getLayoutInflater().inflate(R.layout.common_empty_view, (ViewGroup) recyclerView.getParent(), false);

    }

    @Override
    public void initData() {
        getEvaluateList(pageNo, true);
    }

    private void getEvaluateList(int pageNo, final boolean isRefresh) {
        disposable.add(ApiUtils.getInstance().getEvaluateList(itemId, pageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<EvaluateListBean>>() {
                    @Override
                    public void accept(ResultBean<EvaluateListBean> evaluateListBeanResultBean) throws Exception {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        getListSuccess(evaluateListBeanResultBean, isRefresh);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                }));
    }

    private void getListSuccess(ResultBean<EvaluateListBean> evaluateListBeanResultBean, boolean isRefresh) {
        List<EvaluateListBean.EvaluateDetailBean> newLists = evaluateListBeanResultBean.getData().getList();
        if (newLists == null) {
            newLists = new ArrayList<>();
        }
        if (isRefresh) {
            pageNo = 1;
            lists = newLists;
        } else {
            pageNo++;
            lists.addAll(newLists);
        }
        if (lists.size() < 10) {
            refreshLayout.setFooterHeight(30);
            refreshLayout.finishLoadMoreWithNoMoreData();//显示没有更多数据
        } else {
            refreshLayout.setNoMoreData(false);
        }
        if (lists.size() == 0) {
            commentAdapter.setEmptyView(notDataView);
        } else {
            commentAdapter.setNewData(lists);
        }
    }

    public class CommentAdapter extends BaseQuickAdapter<EvaluateListBean.EvaluateDetailBean, BaseViewHolder> {
        public CommentAdapter(int layoutResId, List<EvaluateListBean.EvaluateDetailBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, EvaluateListBean.EvaluateDetailBean item) {
            helper.setText(R.id.tvName, item.getUser_name());
            helper.setText(R.id.tvContent, item.getContent());
            helper.setText(R.id.tvTime, TimeUtils.millisToYearMD(item.getCreated_time() + "000"));
            RatingBar ratingBar = helper.getView(R.id.ratingBar);

            RecyclerView rvPicList = helper.getView(R.id.rvPicList);
            String pics[] = item.getRate_pic();
            if(pics != null && pics.length>0){
                rvPicList.setVisibility(View.VISIBLE);
                List<String> picList = Arrays.asList(item.getRate_pic());
                AppraiseGoodsPicAdapter picAdapter = new AppraiseGoodsPicAdapter(activity,picList);
                rvPicList.setLayoutManager(new GridLayoutManager(activity, 3));
                rvPicList.addItemDecoration(new GridSpacingItemDecoration(3, Utils.dp2px(activity, 3), false));
                rvPicList.setAdapter(picAdapter);
            }else{
                rvPicList.setVisibility(View.GONE);
            }

//            picAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                    toLargeImage(position);
//                }
//            });
            String result = item.getResult();
            if ("good".equals(result)) {
                ratingBar.setRating(5);
            } else if ("neutral".equals(result)) {
                ratingBar.setRating(3);
            } else {
                ratingBar.setRating(2);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
