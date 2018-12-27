package com.chunlangjiu.app.fans.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.fans.bean.FansBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.web.WebViewActivity;
import com.pkqup.commonlibrary.net.bean.ResultBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FansHomeNewActivity extends BaseActivity {
    RelativeLayout rl_fans_list;
    TextView tv_fans_num;
    RelativeLayout rl_invite_code;
    TextView tv_invite_code;
    RelativeLayout rl_invite_fans;


    private ImageView ivQrCode ;
    private TextView  tvMyCode;
    private TextView tvShare ;
    private FansBean fansBean ;
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

        rl_fans_list = findViewById(R.id.rl_fans_list);
        tv_fans_num = findViewById(R.id.tv_fans_num);
        rl_invite_code = findViewById(R.id.rl_invite_code);
        tv_invite_code = findViewById(R.id.tv_invite_code);
        rl_invite_fans = findViewById(R.id.rl_invite_fans);

        rl_fans_list.setOnClickListener(onClickListener);
        rl_invite_code.setOnClickListener(onClickListener);
        rl_invite_fans.setOnClickListener(onClickListener);
    }

    private void initData() {
        getFansInfo();
    }


    private void getFansInfo() {
        //获取粉丝信息
        disposable.add(ApiUtils.getInstance().getFansInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<FansBean>>() {
                    @Override
                    public void accept(ResultBean<FansBean> mainClassBeanResultBean) throws Exception {
                        fansBean = mainClassBeanResultBean.getData();
                        setFansCode();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
        testData();
    }

    private void testData(){
        fansBean= new FansBean();
        fansBean.setFansNum("23");
        fansBean.setInviteCode("34837487384");
        fansBean.setInvitePerson("恁大哥");
        setFansCode();
    };

    private void setFansCode(){
        if(fansBean!=null){
            tv_fans_num.setText(getString(R.string.fans_num,fansBean.getFansNum()));
            tv_invite_code.setText(fansBean.getInviteCode());
        }
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int resId = v.getId();
            if(resId == R.id.rl_fans_list){
                startFansListActivity();
            }else if(resId == R.id.rl_invite_code){
                startInviteCodeActivity();
            }else if(resId == R.id.rl_invite_fans){
                startFansInviteActivity();
            }
        }
    };


    private void startFansListActivity(){
        Intent intent = new Intent();
        intent.setClass(this,FansListActivity.class);
        startActivity(intent);
    }
    private void startInviteCodeActivity(){
        Intent intent = new Intent();
        intent.setClass(this,FansInfoActivity.class);
        intent.putExtra("fansBean",fansBean);
        startActivity(intent);
    }
    private void startFansInviteActivity(){
        String url = "http://www.baidu.com";
        String title = getString(R.string.fans_register_app);
        WebViewActivity.startWebViewActivity(this,url,title);
    }
    @Override
    public void setTitleView() {
        hideTitleView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
