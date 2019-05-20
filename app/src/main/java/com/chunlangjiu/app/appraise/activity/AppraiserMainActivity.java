package com.chunlangjiu.app.appraise.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.appraise.adapter.AppraiserAdapter;
import com.chunlangjiu.app.appraise.bean.AppraiseBean;
import com.chunlangjiu.app.appraise.bean.AppraiseListBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.PageUtils;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.pkqup.commonlibrary.dialog.CommonConfirmDialog;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @CreatedbBy: 吴申飞 on 2018/6/16.
 * @Describe: 鉴定师主页
 */
public class AppraiserMainActivity extends BaseActivity {

    @BindView(R.id.tvAppraiseNum)
    TextView tvAppraiseNum;
    @BindView(R.id.tvTipsOne)
    TextView tvTipsOne;
    @BindView(R.id.tvTipsTwo)
    TextView tvTipsTwo;
    @BindView(R.id.tvTipsThree)
    TextView tvTipsThree;
    @BindView(R.id.rlBeginner)
    RelativeLayout rlBeginner;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rvAppraiserList)
    RecyclerView rvAppraiserList;
    AppraiserAdapter appraiserAdapter ;
    private PageUtils<AppraiseBean> pageUtils = new PageUtils<>();


    CommonConfirmDialog commonConfirmDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appraise_activity_main);
        initView();
        loadAppraiserList(pageUtils.firstPage());
    }

    private void initView(){
        commonConfirmDialog = new CommonConfirmDialog(this,"“若想成为鉴定师，可拨打400-189-0095”联系平台申请！");
        commonConfirmDialog.setDialogStr("取消","联系");
        commonConfirmDialog.setCallBack(new CommonConfirmDialog.CallBack() {
            @Override
            public void onConfirm() {
                CommonUtils.callPhone("400-189-0095");
            }

            @Override
            public void onCancel() {

            }
        });
        rlBeginner.setOnClickListener(onClickListener);

        appraiserAdapter = new AppraiserAdapter(this,pageUtils.getList());
        appraiserAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AppraiseBean appraiseBean= appraiserAdapter.getItem(position);
                if(appraiseBean.isAdd()){
                    commonConfirmDialog.show();
                }else{
                    AppraiserInfoActivity.startAppraiserInfoActivity(AppraiserMainActivity.this,appraiseBean.getAuthenticate_id());
                }
            }
        });
        appraiserAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.common_empty_view, (ViewGroup) rvAppraiserList.getParent(), false));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvAppraiserList.setLayoutManager(gridLayoutManager);
        rvAppraiserList.addItemDecoration(new GridSpacingItemDecoration(2, Utils.dp2px(this, 5), false));
        rvAppraiserList.setAdapter(appraiserAdapter);

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadAppraiserList(pageUtils.nextPage());
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadAppraiserList(pageUtils.firstPage());
            }
        });
    }

    private void loadAppraiserList(int page){
        disposable.add(ApiUtils.getInstance().getAppraiserList(page,10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<AppraiseListBean<AppraiseBean>>>() {
                    @Override
                    public void accept(ResultBean<AppraiseListBean<AppraiseBean>> result) throws Exception {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        String count = result.getData().getCount();
                        String countStr = CommonUtils.getString(R.string.appraise_num,count);
                        tvAppraiseNum.setText(CommonUtils.setSpecifiedTextsColor(countStr,count, ContextCompat.getColor(AppraiserMainActivity.this,R.color.t_red)));
                        tvTipsOne.setText(result.getData().getContent());
                        pageUtils.loadListSuccess(result.getData().getList());
                        appraiserAdapter.setAddNewData(pageUtils.getList());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                }));
    }


    @Override
    public void setTitleView() {
        titleName.setText("选择鉴定师");
        titleImgLeft.setOnClickListener(onClickListener);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(R.id.img_title_left == view.getId()){
                finish();
            }else if(R.id.rlBeginner == view.getId()){
                startActivity(new Intent(AppraiserMainActivity.this,NoviceMustSeeActivity.class));
            }
        }
    };
}
