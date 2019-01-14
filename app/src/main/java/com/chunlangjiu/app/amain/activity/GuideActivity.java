package com.chunlangjiu.app.amain.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.chunlangjiu.app.R;
import com.pkqup.commonlibrary.util.SPUtils;

import java.util.ArrayList;

import butterknife.BindView;


public class GuideActivity extends AppCompatActivity {
    @BindView(R.id.tvSkip)
    EditText tvSkip;
    @BindView(R.id.vpGuide)
    ViewPager vpGuide;


    private static int[] imgs = {R.mipmap.welcomepage1, R.mipmap.welcomepage2, R.mipmap.welcomepage3};
    private ArrayList<ImageView> imageViews;
    private VpAdapter vpAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SPUtils.put("firstStart", true);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.gride_activity);
        initImages();
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMainActivity();
            }
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
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
            iv.setScaleType(ImageView.ScaleType.FIT_XY);//这里也是一个图片的适配
            imageViews.add(iv);
            if (i == imgs.length - 1) {
                //为最后一张图片添加点击事件
                iv.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Intent toMainActivity = new Intent(GuideActivity.this, MainActivity.class);//跳转到主界面
                        startActivity(toMainActivity);
                        return true;

                    }
                });
            }
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