package com.chunlangjiu.app.appraise.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.abase.BaseFragmentAdapter;
import com.chunlangjiu.app.appraise.bean.AppraiseBean;
import com.chunlangjiu.app.appraise.fragment.AppriseGoodsListFragment;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.CommonUtils;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @CreatedbBy: 吴申飞 on 2018/6/16.
 * @Describe: 鉴定师主页
 */
public class AppraiserSellerHomeActivity extends BaseActivity {

    @BindView(R.id.imgHead)
    ImageView imgHead;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.rbEvaluation)
    RatingBar rbEvaluation;
    @BindView(R.id.tvEdit)
    TextView tvEdit;
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


    @BindView(R.id.tabTitle)
    TabLayout tabTitle ;
    @BindView(R.id.vpContent)
    ViewPager vpContent ;

    private String appraiserId ;
    private AppraiseBean appraiseBean;

    private List<Fragment> fragments;
    private BaseFragmentAdapter myFragmentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appraise_activity_my_home);
        initView();
        initData();
    }

    private void initData(){
        appraiserId = getIntent().getStringExtra("appraiserId");
        loadAppraiserInfo();
    }

    private void initView() {
        tvEdit.setOnClickListener(onClickListener);
        fragments = new ArrayList<>();
        AppriseGoodsListFragment fragmentOne = AppriseGoodsListFragment.newInstance(true,"0") ;
        AppriseGoodsListFragment fragmentTwo = AppriseGoodsListFragment.newInstance(true,"1") ;
        fragments.add(fragmentOne);
        fragments.add(fragmentTwo);
        initFragmentAdapter("待鉴定\n(0)","已鉴定\n(0)");
        fragmentOne.setGoodsNum(new AppriseGoodsListFragment.GoodsNum() {
            @Override
            public void getGoodsNum(int falseNum, int trueNum) {
                setFragmentTitle("待鉴定\n("+falseNum+")","已鉴定\n("+trueNum+")");
            }
        });
        fragmentOne.setGoodsNum(new AppriseGoodsListFragment.GoodsNum() {
            @Override
            public void getGoodsNum(int falseNum, int trueNum) {
                setFragmentTitle("待鉴定\n("+falseNum+")","已鉴定\n("+trueNum+")");
            }
        });
    }

    public static void startAppraiserInfoActivity(Activity activity,String id){
        if(activity != null){
            Intent intent = new Intent(activity,AppraiserSellerHomeActivity.class);
            intent.putExtra("appraiserId",id);
            activity.startActivity(intent);
        }
    }

    private void initFragmentAdapter(String... title){
        myFragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager());
        myFragmentAdapter.setLists(fragments);
        vpContent.setAdapter(myFragmentAdapter);
        tabTitle.setupWithViewPager(vpContent);
        //手动添加标题 ,必须在setupwidthViewPager之后否则不行
        setFragmentTitle(title);
    }
    private void setFragmentTitle(String... title){
        //手动添加标题 ,必须在setupwidthViewPager之后否则不行
        if(null != title && title.length>0){
            for (int i = 0; i < tabTitle.getTabCount(); i++) {
                if(i<title.length){
                    tabTitle.getTabAt(i).setText(title[i]);
                }
            }
        }
    }

    @Override
    public void setTitleView() {
        titleName.setText("鉴定师");
        titleImgLeft.setOnClickListener(onClickListener);
    }


    private void loadAppraiserInfo(){
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

    private void updateView(){
        if(appraiseBean!=null){
            GlideUtils.loadImageHead(this, appraiseBean.getAuthenticate_img(), imgHead);
            tvName.setText(appraiseBean.getAuthenticate_name());
            tvAppraiseScope.setText(getString(R.string.appraise_scope,appraiseBean.getAuthenticate_scope()));
            tvAppraiseRequire.setText(getString(R.string.appraise_require,appraiseBean.getAuthenticate_require()));
            tvAppraiseAttention.setText(getString(R.string.appraise_attention,appraiseBean.getAuthenticate_content()));
            tvTipsOne.setText("日均"+appraiseBean.getDay());
            tvTipsTwo.setText("完成率"+appraiseBean.getRate());
            String queueUp = appraiseBean.getLine();
            tvTipsThree.setText(CommonUtils.setSpecifiedTextsColor("排队"+queueUp,queueUp, ContextCompat.getColor(this,R.color.t_red)));
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (R.id.img_title_left == view.getId()) {
                finish();
            } else if (R.id.tvEdit == view.getId()) {
                AppraiserInfoEditActivity.startAppraiserInfoEditActivity(AppraiserSellerHomeActivity.this,appraiseBean,1001);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1001 && resultCode == Activity.RESULT_OK){
            loadAppraiserInfo();
        }
    }
}
