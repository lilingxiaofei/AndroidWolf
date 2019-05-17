package com.chunlangjiu.app.appraise.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.amain.bean.ListBean;
import com.chunlangjiu.app.appraise.adapter.AppraiseGoodsAdapter;
import com.chunlangjiu.app.appraise.bean.AppraiseBean;
import com.chunlangjiu.app.appraise.bean.AppraiseGoodsBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.PageUtils;
import com.chunlangjiu.app.util.RatingBarUtils;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SizeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @CreatedbBy: wushenfei on 2018/6/16.
 * @Describe: 鉴定师信息
 */
public class AppraiserInfoActivity extends BaseActivity {

    @BindView(R.id.imgHead)
    ImageView imgHead;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.rbEvaluation)
    RatingBar rbEvaluation;
    @BindView(R.id.tvRequestAppraise)
    TextView tvRequestAppraise;
    @BindView(R.id.tvAppraiseScope)
    TextView tvAppraiseScope;
    @BindView(R.id.tvAppraiseRequire)
    TextView tvAppraiseRequire;
    @BindView(R.id.tvAppraiseAttention)
    TextView tvAppraiseAttention;

    @BindView(R.id.tvTipsOne)
    TextView tvTipsOne;
    @BindView(R.id.tvTipsTwo)
    TextView tvTipsTwo;
    @BindView(R.id.tvTipsThree)
    TextView tvTipsThree;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rvAppraiserList)
    RecyclerView rvAppraiserList;
    private PageUtils<AppraiseGoodsBean> pageUtils = new PageUtils<>();
    AppraiseGoodsAdapter appraiseGoodsAdapter;

    private AppraiseBean appraiseBean;

    private String appraiserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appraiser_activity_info);
        initView();
        initData();
    }

    private void initView(){
        RatingBarUtils.setRatingBarStyle(this,rbEvaluation,1,5,5, SizeUtils.dp2px(10));
        appraiseGoodsAdapter = new AppraiseGoodsAdapter(this,CommonUtils.APPRAISE_ROLE_VISITOR ,pageUtils.getList());
        appraiseGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AppraiseGoodsBean appraiseBean= appraiseGoodsAdapter.getItem(position);
                AppraiseResultActivity.startAppraiserResultActivity(AppraiserInfoActivity.this,appraiseBean.getChateau_id(),CommonUtils.APPRAISE_ROLE_VISITOR);
            }
        });
        appraiseGoodsAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.common_empty_view, (ViewGroup) rvAppraiserList.getParent(), false));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvAppraiserList.setLayoutManager(gridLayoutManager);
        rvAppraiserList.addItemDecoration(new GridSpacingItemDecoration(2, Utils.dp2px(this, 5), false));
        rvAppraiserList.setAdapter(appraiseGoodsAdapter);

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadAppraiserGoodsList(pageUtils.nextPage());
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadAppraiserGoodsList(pageUtils.firstPage());
            }
        });
    }

    private void initData(){
        appraiserId = getIntent().getStringExtra("appraiserId");
        loadAppraiserDetails();
        loadAppraiserGoodsList(pageUtils.firstPage());
    }


    private void loadAppraiserDetails(){
        disposable.add(ApiUtils.getInstance().getAppraiserDetails(appraiserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<AppraiseBean>>() {
                    @Override
                    public void accept(ResultBean<AppraiseBean> result) throws Exception {
                        appraiseBean = result.getData();
                        updateView();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void loadAppraiserGoodsList(final int page){
        disposable.add(ApiUtils.getInstance().getAppraiseGoodsListById(appraiserId,page,10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<ListBean<AppraiseGoodsBean>>>() {
                    @Override
                    public void accept(ResultBean<ListBean<AppraiseGoodsBean>> result) throws Exception {
                        resetRefreshLayout();
                        List<AppraiseGoodsBean> list = result.getData().getList();
                        pageUtils.loadListSuccess(list);
                        appraiseGoodsAdapter.setNewData(pageUtils.getList());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        resetRefreshLayout();
                    }
                }));
    }

    private void resetRefreshLayout(){
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }


    private void updateView(){
        if(appraiseBean!=null){
            GlideUtils.loadImageHead(this, appraiseBean.getAuthenticate_img(), imgHead);
            tvName.setText(appraiseBean.getAuthenticate_name());
            tvRequestAppraise.setOnClickListener(onClickListener);
            tvAppraiseScope.setText(getString(R.string.appraise_scope,appraiseBean.getAuthenticate_scope()));
            tvAppraiseRequire.setText(getString(R.string.appraise_require,appraiseBean.getAuthenticate_require()));
            tvAppraiseAttention.setText(getString(R.string.appraise_attention,appraiseBean.getAuthenticate_content()));
            tvTipsOne.setText("日均"+appraiseBean.getDay());
            tvTipsTwo.setText("完成率"+appraiseBean.getRate());
            String queueUp = appraiseBean.getLine();
            tvTipsThree.setText(CommonUtils.setSpecifiedTextsColor("排队"+queueUp,queueUp, ContextCompat.getColor(this,R.color.t_red)));
        }
    }

    public static void startAppraiserInfoActivity(Activity activity,String id){
        if(activity != null){
            Intent intent = new Intent(activity,AppraiserInfoActivity.class);
            intent.putExtra("appraiserId",id);
            activity.startActivity(intent);
        }
    }

    @Override
    public void setTitleView() {
        titleName.setText(R.string.appraiser);
        titleImgLeft.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.img_title_left){
                finish();
            }else if(view.getId() == R.id.tvRequestAppraise){
                AppraiseApplyAssessActivity.startApplyAssessActivity(AppraiserInfoActivity.this,appraiserId);
            }
        }
    };

}
