package com.chunlangjiu.app.appraise.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * @CreatedbBy: 吴申飞 on 2018/6/16.
 * @Describe: 鉴定师主页
 */
public class AppraiserSellerHomeActivity extends BaseActivity {

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

    @BindView(R.id.rvAppraiserList)
    RecyclerView rvAppraiserList;
    AppraiserAdapter appraiserAdapter ;
    List<AppraiseBean> appraiseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appraise_activity_main);
        initView();
    }

    private void initView(){
        rlBeginner.setOnClickListener(onClickListener);

        appraiseList = new ArrayList<>();
        appraiseList.add(new AppraiseBean());
        appraiseList.add(new AppraiseBean());
        appraiseList.add(new AppraiseBean());
        appraiseList.add(new AppraiseBean());
        appraiserAdapter = new AppraiserAdapter(this,appraiseList);
        appraiserAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AppraiseBean appraiseBean= appraiserAdapter.getItem(position);
                if(appraiseBean.isAdd()){

                }else{
                    AppraiserInfoActivity.startAppraiserInfoActivity(AppraiserSellerHomeActivity.this,"");
                }
            }
        });
        appraiserAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.common_empty_view, (ViewGroup) rvAppraiserList.getParent(), false));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvAppraiserList.setLayoutManager(gridLayoutManager);
        rvAppraiserList.addItemDecoration(new GridSpacingItemDecoration(2, Utils.dp2px(this, 5), false));
        rvAppraiserList.setAdapter(appraiserAdapter);
    }

    @Override
    public void setTitleView() {
        titleName.setText("选择鉴定师");
        titleImgLeft.setOnClickListener(onClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(R.id.img_title_left == view.getId()){
                finish();
            }else if(R.id.rlBeginner == view.getId()){
                startActivity(new Intent(AppraiserSellerHomeActivity.this,NoviceMustSeeActivity.class));
            }
        }
    };
}
