package com.chunlangjiu.app.fans.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.fans.bean.FansCodeBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.ShareUtils;
import com.chunlangjiu.app.web.WebViewActivity;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SystemUtils;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FansHomeActivity extends BaseActivity {


    private ImageView ivQrCode ;
    private TextView  tvMyCode;
    private TextView tvShare ;
    private TextView tvCopy ;
    private FansCodeBean fansBean ;
    private CompositeDisposable disposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fans_activity_home_new);
        initView();
        initData();
    }

    private void initView() {
        disposable = new CompositeDisposable();

        ivQrCode = findViewById(R.id.ivQrCode) ;
        tvMyCode = findViewById(R.id.tvMyCode) ;
        tvShare  = findViewById(R.id.tvShare) ;
        tvCopy = findViewById(R.id.tvCopy);
        tvShare.setOnClickListener(onClickListener);
        tvCopy.setOnClickListener(onClickListener);
    }

    private void createEnglishQRCodeWithLogo(final String qrCode) {
        tvMyCode.setText(qrCode);
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                Bitmap logoBitmap = BitmapFactory.decodeResource(FansHomeActivity.this.getResources(), R.mipmap.launcher);
                return QRCodeEncoder.syncEncodeQRCode(qrCode, BGAQRCodeUtil.dp2px(FansHomeActivity.this, 150), Color.BLACK, Color.WHITE,
                        logoBitmap);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    ivQrCode.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(FansHomeActivity.this, "生成带logo的英文二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void initData() {
        getFansInfo();
    }


    private void getFansInfo() {
        //获取粉丝信息
        disposable.add(ApiUtils.getInstance().getMyInvitationCode()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<FansCodeBean>>() {
                    @Override
                    public void accept(ResultBean<FansCodeBean> mainClassBeanResultBean) throws Exception {
                        fansBean = mainClassBeanResultBean.getData();
                        createEnglishQRCodeWithLogo(fansBean.getCode());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int resId = v.getId();
            if(resId == R.id.img_title_left){
                finish();
            }else if(resId == R.id.tv_right){
                startFansListActivity();
            }else if(resId == R.id.tvShare){
                showShare();
            }else if(resId ==R.id.tvCopy){
                setCopyContent();
            }
        }
    };

    private void setCopyContent(){
        if(fansBean!=null){
            SystemUtils.copyContent(this,fansBean.getCode());
        }
    }

    private void startFansListActivity(){
        Intent intent = new Intent();
        intent.setClass(this,FansListActivity.class);
        String url = fansBean!=null?fansBean.getUrl():"";
        intent.putExtra("shareUrl",url);
        startActivity(intent);
    }
    private void startFansInviteActivity(){
        if(null != fansBean){
            String url = fansBean.getUrl();
            String title = getString(R.string.fans_register_app);
            WebViewActivity.startWebViewActivity(this,url,title);
        }
    }


    private void showShare() {
        UMImage thumb = new UMImage(this, R.mipmap.launcher);
        UMWeb web = new UMWeb(fansBean.getUrl());
        web.setTitle("邀请码分享");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription("邀请码分享");//描述

        ShareUtils.shareLink(this, web, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
            }
        });
    }

    @Override
    public void setTitleView() {
        titleName.setText(R.string.my_recommend);
        tvRight.setText(R.string.fans_list);
        tvRight.setVisibility(View.VISIBLE);
        titleImgLeft.setOnClickListener(onClickListener);
        tvRight.setOnClickListener(onClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
