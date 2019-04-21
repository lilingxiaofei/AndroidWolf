package com.chunlangjiu.app.appraise.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.chunlangjiu.app.appraise.adapter.AppraiseGoodsAdapter;
import com.chunlangjiu.app.appraise.bean.AppraiseBean;
import com.chunlangjiu.app.appraise.bean.AppraiseGoodsBean;
import com.chunlangjiu.app.util.CommonUtils;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.pkqup.commonlibrary.glide.GlideUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


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

    @BindView(R.id.rvAppraiserList)
    RecyclerView rvAppraiserList;
    List<AppraiseGoodsBean> appraiseGoodsList;
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

    }

    private void initData(){
        appraiserId = getIntent().getStringExtra("appraiserId");
    }


    private void updateView(){
        if(appraiseBean!=null){

        }
        GlideUtils.loadImageHead(this, "", imgHead);
        tvName.setText("姓名");
        tvRequestAppraise.setOnClickListener(onClickListener);
        tvName.setText("鉴定师名称");
        tvAppraiseScope.setText(getString(R.string.appraise_scope,"茅台，五粮液，各种皮牌老酒"));
        tvAppraiseRequire.setText(getString(R.string.appraise_require,"拍摄物品图清晰，有主题"));
        tvTipsOne.setText("日均"+12);
        tvTipsTwo.setText("完成率"+"%80");
        String queueUp = "5";
        tvTipsThree.setText(CommonUtils.setSpecifiedTextsColor("排队"+queueUp,queueUp, ContextCompat.getColor(this,R.color.t_red)));


        appraiseGoodsList = new ArrayList<>();
        appraiseGoodsList.add(new AppraiseGoodsBean());
        appraiseGoodsList.add(new AppraiseGoodsBean());
        appraiseGoodsList.add(new AppraiseGoodsBean());
        appraiseGoodsList.add(new AppraiseGoodsBean());
        appraiseGoodsAdapter = new AppraiseGoodsAdapter(this,appraiseGoodsList);
        appraiseGoodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AppraiseGoodsBean appraiseBean= appraiseGoodsAdapter.getItem(position);
                if(appraiseBean.isAdd()){

                }else{
//                    AppraiserInfoActivity.startAppraiserInfoActivity(AppraiserMainActivity.this,"");
                }
            }
        });
        appraiseGoodsAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.common_empty_view, (ViewGroup) rvAppraiserList.getParent(), false));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvAppraiserList.setLayoutManager(gridLayoutManager);
        rvAppraiserList.addItemDecoration(new GridSpacingItemDecoration(2, Utils.dp2px(this, 5), false));
        rvAppraiserList.setAdapter(appraiseGoodsAdapter);
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

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.img_title_left){
                finish();
            }else if(view.getId() == R.id.tvRequestAppraise){

            }
        }
    };

}
