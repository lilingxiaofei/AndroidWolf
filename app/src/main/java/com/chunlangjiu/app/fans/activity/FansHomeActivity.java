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
import com.chunlangjiu.app.web.WebViewActivity;
import com.pkqup.commonlibrary.net.bean.ResultBean;

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
        tvShare.setOnClickListener(onClickListener);
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
            if(resId == R.id.tv_right){
                startFansListActivity();
            }else if(resId == R.id.tvShare){
                startFansInviteActivity();
            }
        }
    };


    private void startFansListActivity(){
        Intent intent = new Intent();
        intent.setClass(this,FansListActivity.class);
        startActivity(intent);
    }
    private void startFansInviteActivity(){
        String url = "http://www.baidu.com";
        String title = getString(R.string.fans_register_app);
        WebViewActivity.startWebViewActivity(this,url,title);
    }
    @Override
    public void setTitleView() {
        titleName.setText(R.string.my_recommend);
        tvRight.setText(R.string.fans_list);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setOnClickListener(onClickListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
