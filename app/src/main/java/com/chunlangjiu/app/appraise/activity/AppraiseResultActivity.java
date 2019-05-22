package com.chunlangjiu.app.appraise.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.appraise.adapter.AppraiseGoodsPicAdapter;
import com.chunlangjiu.app.appraise.bean.AppraiseGoodsBean;
import com.chunlangjiu.app.goods.activity.GoodsDetailslNewActivity;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.CommonUtils;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @CreatedbBy: 吴申飞 on 2018/6/16.
 * @Describe: 鉴定结果
 */
public class AppraiseResultActivity extends BaseActivity {
    //鉴定师信息
    @BindView(R.id.llAppraiseSource)
    LinearLayout llAppraiseSource;
    @BindView(R.id.imgHead)
    ImageView imgHead;
    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.llAppraisePrice)
    LinearLayout llAppraisePrice;
    @BindView(R.id.tvPrice)
    TextView tvPrice;

    @BindView(R.id.llAppraiseDetails)
    LinearLayout llAppraiseDetails;
    @BindView(R.id.tvColour)
    TextView tvColour;
    @BindView(R.id.tvFlaw)
    TextView tvFlaw;
    @BindView(R.id.tvAccessory)
    TextView tvAccessory;
    @BindView(R.id.tvOtherHelp)
    TextView tvOtherHelp;

    //商品UI

    @BindView(R.id.tvGoodsTitle)
    TextView tvGoodsTitle;
    @BindView(R.id.tvGoodsSeries)
    TextView tvGoodsSeries;
    @BindView(R.id.tvGoodsYear)
    TextView tvGoodsYear;
    @BindView(R.id.tvGoodsExplain)
    TextView tvGoodsExplain;
    @BindView(R.id.rvPicList)
    RecyclerView rvPicList;

    //快速提现UI
    @BindView(R.id.llCommit)
    LinearLayout llCommit;
    @BindView(R.id.tvCommitPrice)
    TextView tvCommitPrice;

    @BindView(R.id.tvAppraiseTips)
    TextView tvAppraiseTips ;

    private List<String> picList = new ArrayList<>();
    AppraiseGoodsPicAdapter picAdapter;

    private String appraiseGoodsId;
    private String appraiseRole ;
    private AppraiseGoodsBean appraiseGoodsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appraiser_activity_result);
        initData();
    }

    private void initData() {
        appraiseGoodsId = getIntent().getStringExtra("appraiseGoodsId");
        appraiseRole = getIntent().getStringExtra("appraiseRole");
        loadAppraiserGoodsDetails();
    }


    private void loadAppraiserGoodsDetails() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getAppraiseGoodsDetails(appraiseGoodsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<AppraiseGoodsBean>>() {
                    @Override
                    public void accept(ResultBean<AppraiseGoodsBean> result) throws Exception {
                        hideLoadingDialog();
                        appraiseGoodsBean = result.getData();
                        updateView();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                    }
                }));
    }

    @Override
    public void setTitleView() {
        titleName.setText("鉴别结果");
        titleImgLeft.setOnClickListener(onClickListener);
    }


    private void updateView() {
        if (appraiseGoodsBean != null) {
            //鉴定师信息
            GlideUtils.loadImageHead(this, appraiseGoodsBean.getAuthenticate_img(), imgHead);
            tvName.setText(appraiseGoodsBean.getAuthenticate_name());

            tvPrice.setText(CommonUtils.getString(R.string.rmb_one,appraiseGoodsBean.getPrice()));

            tvColour.setText(appraiseGoodsBean.getColour());
            tvFlaw.setText(appraiseGoodsBean.getFlaw());
            tvAccessory.setText(appraiseGoodsBean.getAccessory());
            tvOtherHelp.setText(appraiseGoodsBean.getDetails());



            //商品UI
            tvGoodsTitle.setText(CommonUtils.getString(R.string.appraise_goods_title,appraiseGoodsBean.getTitle()));
            tvGoodsSeries.setText(CommonUtils.getString(R.string.appraise_goods_series,appraiseGoodsBean.getSeries()));
            tvGoodsYear.setText(CommonUtils.getString(R.string.appraise_goods_year,appraiseGoodsBean.getYear()));
            tvGoodsExplain.setText(CommonUtils.getString(R.string.appraise_goods_other,appraiseGoodsBean.getContent()));
            if(!TextUtils.isEmpty(appraiseGoodsBean.getImg())){
                picList = Arrays.asList(appraiseGoodsBean.getImg().split(","));
                picAdapter = new AppraiseGoodsPicAdapter(this,picList);
                rvPicList.setLayoutManager(new GridLayoutManager(this, 3));
                rvPicList.addItemDecoration(new GridSpacingItemDecoration(3, Utils.dp2px(this, 3), false));
                rvPicList.setAdapter(picAdapter);
                picAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        toLargeImage(position);
                    }
                });
            }

            //快速提现UI
            if("true".equals(appraiseGoodsBean.getStatus())){
//                llAppraiseSource.setVisibility(View.VISIBLE);
                titleName.setText("鉴别结果");
                llAppraisePrice.setVisibility(View.VISIBLE);
                llAppraiseDetails.setVisibility(View.VISIBLE);
                tvAppraiseTips.setVisibility(View.VISIBLE);
                if(CommonUtils.APPRAISE_ROLE_APPLY.equals(appraiseRole)){
                    llCommit.setVisibility(View.VISIBLE);
                    llCommit.setOnClickListener(onClickListener);
                    tvCommitPrice.setText(CommonUtils.getString(R.string.rmb_two,appraiseGoodsBean.getPrice()));
                    if(!"true".equals(appraiseGoodsBean.getSell())){
                        llCommit.setEnabled(true);
                    }else{
                        llCommit.setEnabled(false);
                    }
                }else{
                    llCommit.setVisibility(View.GONE);
                }
            }else{
                titleName.setText("待鉴定");
                llCommit.setVisibility(View.GONE);
//                llAppraiseSource.setVisibility(View.GONE);
                llAppraisePrice.setVisibility(View.GONE);
                llAppraiseDetails.setVisibility(View.GONE);
                tvAppraiseTips.setVisibility(View.GONE);
            }

        }
    }

    private void toLargeImage(int position) {
        new PhotoPagerConfig.Builder(this)
                .setBigImageUrls((ArrayList<String>) picList)
                .setSavaImage(false)
                .setPosition(position)
//                        .setSaveImageLocalPath("这里是你想保存的图片地址")
                .build();
    }

    public static void startAppraiserResultActivity(Activity activity, String appraiseGoodsId,String appraiseRole) {
        if (activity != null) {
            Intent intent = new Intent(activity, AppraiseResultActivity.class);
            intent.putExtra("appraiseGoodsId", appraiseGoodsId);
            intent.putExtra("appraiseRole",appraiseRole);
            activity.startActivity(intent);
        }
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.img_title_left){
                finish();
            }else if(view.getId() == R.id.llCommit){
                quickCash();
            }
        }
    };

    private void quickCash(){
        disposable.add(ApiUtils.getInstance().quickCash(appraiseGoodsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean result) throws Exception {
                        if(result.getErrorcode() == 0){
                            startActivity(new Intent(AppraiseResultActivity.this,QuickCashActivity.class));
                            initData();
                        }else{
                            ToastUtils.showShort("快速提现失败，请稍后重试");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }
}
