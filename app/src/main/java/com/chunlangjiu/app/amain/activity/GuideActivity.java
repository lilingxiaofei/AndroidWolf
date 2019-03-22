package com.chunlangjiu.app.amain.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.util.MyStatusBarUtils;

import java.util.ArrayList;


public class GuideActivity extends AppCompatActivity {
    TextView tvSkip;
    ViewPager vpGuide;


    private static int[] imgs = {R.mipmap.welcomepage1, R.mipmap.welcomepage2, R.mipmap.welcomepage3};
    private ArrayList<ImageView> imageViews;
    private VpAdapter vpAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        MyStatusBarUtils.setStatusBar(this,R.color.bg_red);
        setContentView(R.layout.gride_activity);
        tvSkip = findViewById(R.id.tvSkip);
        vpGuide = findViewById(R.id.vpGuide);
        vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(imgs.length-1 == position){
                    tvSkip.setText(R.string.into_str);
                }else{
                    tvSkip.setText(R.string.skip_str);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();
            }
        });
        initImages();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtras(getIntent());
        startActivity(intent);
        finish();
    }

    private void initImages() {
        //设置每一张图片都填充窗口
        ViewPager.LayoutParams mParams = new ViewPager.LayoutParams();
        imageViews = new ArrayList<ImageView>();

        for (int i = 0; i < imgs.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);//设置布局
            iv.setImageResource(imgs[i]);//为ImageView添加图片资源
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);//这里也是一个图片的适配
            imageViews.add(iv);
//            if (i == imgs.length - 1) {
                //为最后一张图片添加点击事件
//                iv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
//            }
        }
        vpAdapter = new VpAdapter(imageViews);
        vpGuide.setAdapter(vpAdapter);
    }


    class VpAdapter extends PagerAdapter {

        private ArrayList<ImageView> imageViews;

        public VpAdapter(ArrayList<ImageView> imageViews) {
            this.imageViews = imageViews;
        }

        /**
         * 获取当前要显示对象的数量
         */
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imageViews.size();
        }

        /**
         * 判断是否用对象生成界面
         */
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        /**
         * 从ViewGroup中移除当前对象（图片）
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));
        }

        /**
         * 当前要显示的对象（图片）
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position));
            return imageViews.get(position);
        }

    }
}