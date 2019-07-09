package com.chunlangjiu.app.amain.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.abase.BaseApplication;
import com.chunlangjiu.app.abase.BaseFragmentAdapter;
import com.chunlangjiu.app.amain.bean.CheckUpdateBean;
import com.chunlangjiu.app.amain.bean.HomeModulesBean;
import com.chunlangjiu.app.amain.fragment.AuctionFragment;
import com.chunlangjiu.app.amain.fragment.CartFragment;
import com.chunlangjiu.app.amain.fragment.ClassifyFragment;
import com.chunlangjiu.app.amain.fragment.HomeFragment;
import com.chunlangjiu.app.amain.fragment.UserFragment;
import com.chunlangjiu.app.dialog.AppUpdateDialog;
import com.chunlangjiu.app.dialog.OpenDialog;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.activity.AddGoodsActivity;
import com.chunlangjiu.app.user.activity.VerifiedActivity;
import com.chunlangjiu.app.user.bean.AuthStatusBean;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.GeTuiIntentService;
import com.chunlangjiu.app.util.GeTuiPushService;
import com.chunlangjiu.app.util.MyStatusBarUtils;
import com.chunlangjiu.app.util.UmengEventUtil;
import com.igexin.sdk.PushManager;
import com.pkqup.commonlibrary.dialog.CommonConfirmDialog;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.AppUtils;
import com.pkqup.commonlibrary.util.PermissionUtils;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.pkqup.commonlibrary.view.MyViewPager;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends BaseActivity {



    @BindView(R.id.view_pager)
    MyViewPager viewPager;

    @BindView(R.id.tab_one)
    LinearLayout tabOne;
    @BindView(R.id.tab_one_image)
    ImageView tabOneImage;
    @BindView(R.id.tab_one_text)
    TextView tabOneText;

    @BindView(R.id.tab_two)
    LinearLayout tabTwo;
    @BindView(R.id.tab_two_image)
    ImageView tabTwoImage;
    @BindView(R.id.tab_two_text)
    TextView tabTwoText;

//    @BindView(R.id.rlTabThreeBg)
//    RelativeLayout rlTabThreeBg;
//    @BindView(R.id.rlTabThreeContent)
//    RelativeLayout rlTabThreeContent;
    @BindView(R.id.tab_three)
    LinearLayout tabThree;
    @BindView(R.id.tab_three_image)
    ImageView tabThreeImage;
    @BindView(R.id.tab_three_text)
    TextView tabThreeText;

    @BindView(R.id.tab_four)
    LinearLayout tabFour;
    @BindView(R.id.tab_four_image)
    ImageView tabFourImage;
    @BindView(R.id.tab_four_text)
    TextView tabFourText;
    @BindView(R.id.tv_cart_num)
    TextView tvCartNum;

    @BindView(R.id.tab_five)
    LinearLayout tabFive;
    @BindView(R.id.tab_five_image)
    ImageView tabFiveImage;
    @BindView(R.id.tab_five_text)
    TextView tabFiveText;

    private CompositeDisposable disposable;

    private BaseFragmentAdapter myFragmentAdapter;
    private List<Fragment> fragments;
    private List<ImageView> imageViews;
    private List<TextView> textViews;

    private long exitTime;

    HomeModulesBean.Params params ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amain_activity_main);
        EventManager.getInstance().registerListener(onNotifyListener);
        requestPermission();
        initGeTuiPush();
        initView();
        initData();
        checkUpdate();
//        showErrorLog();
    }

    private void showErrorLog(){
        String errorLog = SPUtils.get("errorLog","").toString();
        if(!TextUtils.isEmpty(errorLog)){
            CommonConfirmDialog commonConfirmDialog = new CommonConfirmDialog(this,errorLog);
            commonConfirmDialog.show();
        }
        String errorLogTwo = SPUtils.get("errorLogTwo","").toString();
        if(!TextUtils.isEmpty(errorLogTwo)){
            CommonConfirmDialog commonConfirmDialog = new CommonConfirmDialog(this,errorLogTwo);
            commonConfirmDialog.show();
        }
    }

    private void initGeTuiPush() {
        PushManager.getInstance().initialize(getApplicationContext(), GeTuiPushService.class);
        PushManager.getInstance().registerPushIntentService(getApplicationContext(), GeTuiIntentService.class);
    }

    private void requestPermission() {
        //请求必要的权限
        PermissionUtils.PermissionForStart(this, new PermissionListener() {
            @Override
            public void onSucceed(int requestCode, List<String> grantPermissions) {
                if(grantPermissions!=null){
                    for (String perm:grantPermissions) {
                        if(Manifest.permission.ACCESS_FINE_LOCATION.equals(perm) || Manifest.permission.ACCESS_COARSE_LOCATION.equals(perm)){
                            EventManager.getInstance().notify(null, ConstantMsg.APPLY_LOCATION_SUCCESS);
                        }
                    }
                }

            }

            @Override
            public void onFailed(int requestCode, List<String> deniedPermissions) {
                showPermissionDialog();
            }
        });
    }

    private void showPermissionDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final CommonConfirmDialog commonConfirmDialog = new CommonConfirmDialog(MainActivity.this, "信任是美好的开始，请授权相关权限，让我们更好的为你服务");
                commonConfirmDialog.setDialogStr("取消", "去授权");
                commonConfirmDialog.setCallBack(new CommonConfirmDialog.CallBack() {
                    @Override
                    public void onConfirm() {
                        commonConfirmDialog.dismiss();
                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + MainActivity.this.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MainActivity.this.startActivity(intent);
                    }

                    @Override
                    public void onCancel() {
                        commonConfirmDialog.dismiss();
                    }
                });
                commonConfirmDialog.show();
            }
        });
    }

    @Override
    public void setTitleView() {
        titleView.setVisibility(View.GONE);
    }

    private void initView() {
        MyStatusBarUtils.setStatusBar(this,R.color.bg_red);
        disposable = new CompositeDisposable();
        tabOne.setOnClickListener(onClickListener);
        tabTwo.setOnClickListener(onClickListener);
        tabThree.setOnClickListener(onClickListener);
        tabFour.setOnClickListener(onClickListener);
        tabFive.setOnClickListener(onClickListener);

        imageViews = new ArrayList<>();
        imageViews.add(tabOneImage);
        imageViews.add(tabTwoImage);
        imageViews.add(tabThreeImage);
        imageViews.add(tabFourImage);
        imageViews.add(tabFiveImage);

        textViews = new ArrayList<>();
        textViews.add(tabOneText);
        textViews.add(tabTwoText);
        textViews.add(tabThreeText);
        textViews.add(tabFourText);
        textViews.add(tabFiveText);

        if (BaseApplication.HIDE_AUCTION) {
            tabThree.setVisibility(View.GONE);
//            rlTabThreeBg.setVisibility(View.GONE);
//            rlTabThreeContent.setVisibility(View.GONE);
        } else {
            tabThree.setVisibility(View.VISIBLE);
//            rlTabThreeBg.setVisibility(View.VISIBLE);
//            rlTabThreeContent.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
//        fragments.add(GoodsFragment.newInstance("", false, "", ""));
        fragments.add(new ClassifyFragment());
        fragments.add(new AuctionFragment());
        fragments.add(new CartFragment());
        fragments.add(new UserFragment());
        myFragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager());
        myFragmentAdapter.setLists(fragments);
        viewPager.setAdapter(myFragmentAdapter);
        setPageFragment(0);
    }


    private void loadOpen() {
        params = (HomeModulesBean.Params)getIntent().getSerializableExtra("openParams");
        if(params!= null){
            OpenDialog dialog = new OpenDialog(MainActivity.this,params);
            dialog.show();
        }else{
            disposable.add(ApiUtils.getInstance().getOpenAd()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean<HomeModulesBean>>() {
                        @Override
                        public void accept(ResultBean<HomeModulesBean> brandsListBeanResultBean) throws Exception {
                            if(null != brandsListBeanResultBean.getData()){
                                params = brandsListBeanResultBean.getData().getModules().get(0).getParams();
                                OpenDialog dialog = new OpenDialog(MainActivity.this,params);
                                dialog.show();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                        }
                    }));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(viewPager.getCurrentItem() == ){
//
//        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tab_one:
                    setPageFragment(0);
                    break;
                case R.id.tab_two:
                    setPageFragment(1);
                    break;
                case R.id.tab_three:
                    setPageFragment(2);
                    break;
                case R.id.tab_four:
                    setPageFragment(3);
                    break;
                case R.id.tab_five:
                    setPageFragment(4);
//                    loadAuthStatus();
                    break;
            }
        }
    };


    private void loadAuthStatus(){
        if (BaseApplication.isLogin()) {
            Observable<ResultBean<AuthStatusBean>> personAuthStatus = ApiUtils.getInstance().getPersonAuthStatus();
            Observable<ResultBean<AuthStatusBean>> companyAuthStatus = ApiUtils.getInstance().getCompanyAuthStatus();
            disposable.add(Observable.zip(personAuthStatus, companyAuthStatus, new BiFunction<ResultBean<AuthStatusBean>, ResultBean<AuthStatusBean>, List<AuthStatusBean>>() {
                @Override
                public List<AuthStatusBean> apply(ResultBean<AuthStatusBean> uploadImageBeanResultBean, ResultBean<AuthStatusBean> uploadImageBeanResultBean2) throws Exception {
                    List<AuthStatusBean> imageLists = new ArrayList<>();
                    imageLists.add(uploadImageBeanResultBean.getData());
                    imageLists.add(uploadImageBeanResultBean2.getData());
                    return imageLists;
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<AuthStatusBean>>() {
                        @Override
                        public void accept(List<AuthStatusBean> authStatusBeans) throws Exception {
                            if (AuthStatusBean.AUTH_SUCCESS.equals(authStatusBeans.get(0).getStatus()) || AuthStatusBean.AUTH_SUCCESS.equals(authStatusBeans.get(1).getStatus())) {
//                                checkUploadGoods();
                                showContentView();
                            } else if (AuthStatusBean.AUTH_LOCKED.equals(authStatusBeans.get(0).getStatus()) || AuthStatusBean.AUTH_LOCKED.equals(authStatusBeans.get(1).getStatus())) {
                                ToastUtils.showShort("您的认证正在审核中，我们会尽快处理");
                                finish();
                            } else if (AuthStatusBean.AUTH_FAILING.equals(authStatusBeans.get(0).getStatus()) || AuthStatusBean.AUTH_FAILING.equals(authStatusBeans.get(1).getStatus())) {
                                ToastUtils.showShort("您的认证被驳回，请重新提交资料审核");
                                startActivity(new Intent(MainActivity.this,VerifiedActivity.class));
                                finish();
                            } else {
                                ToastUtils.showShort("您还没有进行实名认证，请先认证");
                                startActivity(new Intent(MainActivity.this,VerifiedActivity.class));
                                finish();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            finish();
                        }
                    }));
        }
    }

    private void setPageFragment(int position) {

        UMENGIndexEvent(position);

        viewPager.setCurrentItem(position, false);
        for (int i = 0; i < imageViews.size(); i++) {
            if (position == i) {
                imageViews.get(i).setSelected(true);
            } else {
                imageViews.get(i).setSelected(false);
            }
        }
        for (int i = 0; i < textViews.size(); i++) {
            if (position == i) {
                textViews.get(i).setSelected(true);
            } else {
                textViews.get(i).setSelected(false);
            }
        }
    }

    private void UMENGIndexEvent(int position) {
        switch (position) {
            case 0:
                UmengEventUtil.homeEvent(this);
                break;
            case 1:
                UmengEventUtil.allEvent(this);
                break;
            case 2:
                UmengEventUtil.auctionEvent(this);
                break;
            case 3:
                UmengEventUtil.shopCartEvent(this);
                break;
            case 4:
                UmengEventUtil.mimeEvent(this);
                break;
        }
    }

    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            if (eventTag.equals(ConstantMsg.LOGIN_SUCCESS)) {
                Intent intent = new Intent(MainActivity.this, GeTuiIntentService.class);
                intent.putExtra("action", true);
                startService(intent);
            }
            msgToPageClass(eventTag);//我要买酒
            msgToPageAuction(eventTag);//竞拍专区
            msgToPageCart(eventTag);//购物车
            msgToPageMy(eventTag);//我的
        }
    };

    private void msgToPageClass(String eventTag) {
        if (eventTag.equals(ConstantMsg.MSG_PAGE_CLASS)) {
            setPageFragment(1);
        }
    }

    private void msgToPageAuction(String eventTag) {
        if (eventTag.equals(ConstantMsg.MSG_PAGE_AUCTION)) {
            setPageFragment(2);
        }
    }

    private void msgToPageCart(String eventTag) {
        if (eventTag.equals(ConstantMsg.MSG_PAGE_CART)) {
            setPageFragment(3);
        }
    }

    private void msgToPageMy(String eventTag) {
        if (eventTag.equals(ConstantMsg.MSG_PAGE_MY)) {
            setPageFragment(4);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventManager.getInstance().unRegisterListener(onNotifyListener);
        disposable.dispose();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
        return true;
    }


    private void checkUpdate() {
        disposable.add(ApiUtils.getInstance().checkUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<CheckUpdateBean>>() {
                    @Override
                    public void accept(ResultBean<CheckUpdateBean> checkUpdateBeanResultBean) throws Exception {
                        CheckUpdateBean checkUpdateBean = checkUpdateBeanResultBean.getData();
                        if (checkUpdateBean != null) {
                            String versions = checkUpdateBean.getVersions();
                            int newVersionCode = 0;
                            try {
                                if (!TextUtils.isEmpty(versions)) {
                                    newVersionCode = Integer.parseInt(versions);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            int versionCode = AppUtils.getVersionCode();
                            if (versionCode < newVersionCode) {
                                showUpdateDialog(checkUpdateBean);
                            }else{
                                loadOpen();
                            }
                        }else{
                            loadOpen();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        loadOpen();
                    }
                }));
    }

    /**
     * 强制升级对话框
     */
    private void showUpdateDialog(final CheckUpdateBean checkUpdateBean) {
        AppUpdateDialog appUpdateDialog = new AppUpdateDialog(this, checkUpdateBean.getMessage());
        appUpdateDialog.setCallBack(new AppUpdateDialog.CallBack() {
            @Override
            public void onConfirm() {
                Intent intent = new Intent();
                intent.setData(Uri.parse(checkUpdateBean.getUrl()));//Url 就是你要打开的网址
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent); //启动浏览器
            }
        });
        if (!"true".equals(checkUpdateBean.getCoerciveness())) {
            appUpdateDialog.notForceUpdate();
        }
        appUpdateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                loadOpen();
            }
        });
        appUpdateDialog.show();
    }


}
