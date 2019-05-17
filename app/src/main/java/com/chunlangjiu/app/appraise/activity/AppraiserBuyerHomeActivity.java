package com.chunlangjiu.app.appraise.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.abase.BaseFragmentAdapter;
import com.chunlangjiu.app.appraise.fragment.AppriseGoodsListFragment;
import com.chunlangjiu.app.util.CommonUtils;
import com.pkqup.commonlibrary.util.SizeUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * @CreatedbBy: 吴申飞 on 2018/6/16.
 * @Describe: 鉴定师主页
 */
public class AppraiserBuyerHomeActivity extends BaseActivity {

    @BindView(R.id.llToAppraise)
    LinearLayout llToAppraise;

    @BindView(R.id.tabTitle)
    TabLayout tabLayout;
    @BindView(R.id.vpContent)
    ViewPager vpContent;

    private List<Fragment> fragments;
    private BaseFragmentAdapter myFragmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appraise_activity_report);
        initView();
    }

    private void initView(){
        llToAppraise.setOnClickListener(onClickListener);
        fragments = new ArrayList<>();
        AppriseGoodsListFragment fragmentOne = AppriseGoodsListFragment.newInstance(CommonUtils.APPRAISE_ROLE_APPLY,"0") ;
        AppriseGoodsListFragment fragmentTwo = AppriseGoodsListFragment.newInstance(CommonUtils.APPRAISE_ROLE_APPLY,"1") ;
        fragments.add(fragmentOne);
        fragments.add(fragmentTwo);
        initFragmentAdapter("鉴定中\n(0)","已鉴定\n(0)");
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

    private void setFragmentTitle(String... title){
        //手动添加标题 ,必须在setupwidthViewPager之后否则不行
        if(null != title && title.length>0){
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                if(i<title.length){
                    tabLayout.getTabAt(i).setText(title[i]);
                }
            }
        }
    }

    private void initFragmentAdapter(String... title){
        myFragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager());
        myFragmentAdapter.setLists(fragments);
        vpContent.setAdapter(myFragmentAdapter);
        tabLayout.setupWithViewPager(vpContent);
        //手动添加标题 ,必须在setupwidthViewPager之后否则不行
        setFragmentTitle(title);
    }

    public void reflex(final TabLayout tabLayout){
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp10 = SizeUtils.dp2px( 10);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width ;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void setTitleView() {
        titleName.setText("鉴定报告");
        titleImgLeft.setOnClickListener(onClickListener);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(R.id.img_title_left == view.getId()){
                finish();
            }else if(R.id.llToAppraise == view.getId()){
                startActivity(new Intent(AppraiserBuyerHomeActivity.this,AppraiserMainActivity.class));
            }
        }
    };
}
