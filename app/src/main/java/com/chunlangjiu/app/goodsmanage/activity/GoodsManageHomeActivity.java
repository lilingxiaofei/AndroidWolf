package com.chunlangjiu.app.goodsmanage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.goods.bean.ShopInfoBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.activity.AddGoodsActivity;
import com.chunlangjiu.app.user.bean.MyNumBean;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.MyStatusBarUtils;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;

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
public class GoodsManageHomeActivity extends BaseActivity {


    @BindView(R.id.imgHead)
    CircleImageView imgHead;
    @BindView(R.id.tvShopName)
    TextView tvShopName;

    @BindView(R.id.llSaleLayout)
    LinearLayout llSaleLayout;
    @BindView(R.id.tvSaleNum)
    TextView tvSaleNum;


    @BindView(R.id.llWareHouseLayout)
    LinearLayout llWareHouseLayout;
    @BindView(R.id.tvWareHouseNum)
    TextView tvWareHouseNum;


    @BindView(R.id.llAuditLayout)
    LinearLayout llAuditLayout;
    @BindView(R.id.tvAuditNum)
    TextView tvAuditNum;


    @BindView(R.id.llAuctionLayout)
    LinearLayout llAuctionLayout;
    @BindView(R.id.tvAuctionNum)
    TextView tvAuctionNum;

    @BindView(R.id.ivAddGoods)
    ImageView ivAddGoods;


    private CompositeDisposable disposable;


    private String shopId;


    public static void startShopMainActivity(Activity activity, String shopId) {
        Intent intent = new Intent(activity, GoodsManageHomeActivity.class);
        intent.putExtra("shopId", shopId);
        activity.startActivity(intent);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivBack:
                case R.id.img_title_left:
                    finish();
                    break;
                case R.id.ivAddGoods:
                    startActivity(new Intent(GoodsManageHomeActivity.this,AddGoodsActivity.class));
                    break;
                case R.id.llAuctionLayout:
                    GoodsManageListActivity.startGoodsManageActivity(GoodsManageHomeActivity.this, CommonUtils.GOODS_STATUS_AUCTION_ACTIVE);
                    break;
                case R.id.llSaleLayout:
                    GoodsManageListActivity.startGoodsManageActivity(GoodsManageHomeActivity.this, CommonUtils.GOODS_STATUS_SELL);
                    break;
                case R.id.llWareHouseLayout:
                    GoodsManageListActivity.startGoodsManageActivity(GoodsManageHomeActivity.this, CommonUtils.GOODS_STATUS_INSTOCK);
                    break;
                case R.id.llAuditLayout:
                    GoodsManageListActivity.startGoodsManageActivity(GoodsManageHomeActivity.this, CommonUtils.GOODS_STATUS_AUCTION_ACTIVE);
                    break;

            }
        }
    };


    @Override
    public void setTitleView() {
        hideTitleView();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_goods_manage_home);
        initView();
        initData();
    }



    private void initView() {
        MyStatusBarUtils.setStatusBar(this,ContextCompat.getColor(this, R.color.bg_red));
        MyStatusBarUtils.setFitsSystemWindows(findViewById(R.id.rlShopTitle),true);

         llSaleLayout.setOnClickListener(onClickListener);
        llWareHouseLayout.setOnClickListener(onClickListener);
        llAuditLayout.setOnClickListener(onClickListener);
        llAuctionLayout.setOnClickListener(onClickListener);
        ivAddGoods.setOnClickListener(onClickListener);
    }

    private void initData() {
        disposable = new CompositeDisposable();
        shopId = getIntent().getStringExtra("shopId");
        getShopInfo();
        getSellerOrderNumIndex();
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
    }

    private void getSellerOrderNumIndex() {
        disposable.add(ApiUtils.getInstance().getMyNumFlag("shop")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<MyNumBean>>() {
                    @Override
                    public void accept(ResultBean<MyNumBean> myNumBeanResultBean) throws Exception {
                        MyNumBean data = myNumBeanResultBean.getData();
                        if (data != null) {
                            tvAuditNum.setText(data.getPending_num());
                            tvWareHouseNum.setText(data.getInstock_num());
//                            tvSaleNum.setText("0");
//                            tvAuctionNum.setText("0");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));


    }

}
