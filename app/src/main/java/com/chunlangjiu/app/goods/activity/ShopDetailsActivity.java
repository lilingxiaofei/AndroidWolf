package com.chunlangjiu.app.goods.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.goods.bean.ShopInfoBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.TimeUtils;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/7/5
 * @Describe: 卖家主页
 */
public class ShopDetailsActivity extends BaseActivity {


    @BindView(R.id.imgHead)
    CircleImageView imgHead;
    @BindView(R.id.tvShopName)
    TextView tvShopName;
    @BindView(R.id.ivShopLevel)
    ImageView ivShopLevel;
    @BindView(R.id.tvShopTips)
    TextView tvShopTips;
    @BindView(R.id.rlShopDetail)
    RelativeLayout rlShopDetail;
    @BindView(R.id.tvShopPhone)
    TextView tvShopPhone;
    @BindView(R.id.tvDesc)
    TextView tvDesc;


    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvOpenShopTime)
    TextView tvOpenShopTime;
    @BindView(R.id.tvToShopMain)
    TextView tvToShopMain;

    @BindView(R.id.tvCompanyStatus)
    TextView tvCompanyStatus;

    private CompositeDisposable disposable;
    private String shopId;


    public static void startShopMainActivity(Activity activity, String shopId) {
        Intent intent = new Intent(activity, ShopDetailsActivity.class);
        intent.putExtra("shopId", shopId);
        activity.startActivity(intent);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvToShopMain:
                    finish();
                    break;
                case R.id.img_title_left:
                    finish();
                    break;

            }
        }
    };


    @Override
    public void setTitleView() {
        titleImgLeft.setOnClickListener(onClickListener);
//        titleImgRightOne.setVisibility(View.VISIBLE);
//        titleImgRightOne.setImageResource(R.mipmap.icon_list);
//        titleImgRightOne.setOnClickListener(onClickListener);
        titleName.setText(R.string.store_details);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_activity_shop_details);
        initFilterView();
        initView();
        initData();
    }

    private void initFilterView() {
    }


    private void initView() {
//        MyStatusBarUtils.setStatusBar(this,ContextCompat.getColor(this, R.color.bg_red));
//        MyStatusBarUtils.setFitsSystemWindows(findViewById(R.id.rlShopTitle),true);
        tvToShopMain.setOnClickListener(onClickListener);
    }

    private void initData() {
        disposable = new CompositeDisposable();
        shopId = getIntent().getStringExtra("shopId");
        getShopInfo();
    }





    private void getShopInfo() {
        disposable.add(ApiUtils.getInstance().getShopInfo(shopId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<ShopInfoBean>>() {
                    @Override
                    public void accept(ResultBean<ShopInfoBean> shopInfoBeanResultBean) throws Exception {
                        getShopInfoSuccess(shopInfoBeanResultBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void getShopInfoSuccess(ShopInfoBean data) {
        GlideUtils.loadImageShop(this, data.getShopInfo().getShop_logo(), imgHead);
        tvShopName.setText(data.getShopInfo().getShop_name());
        tvShopTips.setText(data.getShopInfo().getShop_descript());
        tvShopPhone.setText(data.getShopInfo().getMobile());
        tvDesc.setText(data.getShopInfo().getShop_descript());
        String shopType = data.getShopInfo().getGrade();

        if(!TextUtils.isEmpty(data.getShopInfo().getAuthentication())){
            tvCompanyStatus.setText(data.getShopInfo().getAuthentication());
        }else{
            tvCompanyStatus.setText("个人认证");
        }

//        String companyStatus = data.getShopInfo().getStatus();
//        if (AuthStatusBean.AUTH_SUCCESS.equals(companyStatus)) {
//            tvCompanyStatus.setText("企业认证");
//        } else {
//            tvCompanyStatus.setText("个人认证");
//        }

        tvOpenShopTime.setText(TimeUtils.millisToDate(data.getShopInfo().getOpen_time()));
        tvAddress.setText(data.getShopInfo().getShop_addr());

        if ("2".equals(shopType)) {
            ivShopLevel.setImageResource(R.mipmap.store_partner);
        } else if ("1".equals(shopType)) {
            ivShopLevel.setImageResource(R.mipmap.store_star);
        } else {
            ivShopLevel.setImageResource(R.mipmap.store_common);
        }
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
