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
import com.chunlangjiu.app.user.bean.UserInfoBean;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.MyStatusBarUtils;
import com.pkqup.commonlibrary.eventmsg.EventManager;
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


    @BindView(R.id.ivBack)
    ImageView ivBack;
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




    public static void startShopMainActivity(Activity activity) {
        Intent intent = new Intent(activity, GoodsManageHomeActivity.class);
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
                    GoodsManageListActivity.startGoodsManageActivity(GoodsManageHomeActivity.this, CommonUtils.GOODS_STATUS_AUDIT_PENDING);
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

    @Override
    public void onResume() {
        super.onResume();
        getSellerOrderNumIndex();
    }

    private void initView() {
        MyStatusBarUtils.setStatusBar(this,ContextCompat.getColor(this, R.color.bg_white));
        MyStatusBarUtils.setFitsSystemWindows(findViewById(R.id.rlShopTitle),true);
        ivBack.setOnClickListener(onClickListener);
         llSaleLayout.setOnClickListener(onClickListener);
        llWareHouseLayout.setOnClickListener(onClickListener);
        llAuditLayout.setOnClickListener(onClickListener);
        llAuctionLayout.setOnClickListener(onClickListener);
        ivAddGoods.setOnClickListener(onClickListener);
    }

    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            eventTag = eventTag == null ? "" : eventTag;
            switch (eventTag) {
                case ConstantMsg.SHOP_DATA_CHANGE:
                    getSellerOrderNumIndex();
                    break;
            }
        }
    };

    private void initData() {
        EventManager.getInstance().registerListener(onNotifyListener);
        disposable = new CompositeDisposable();
        getUserInfo();
    }

    private void getUserInfo() {
        disposable.add(ApiUtils.getInstance().getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<UserInfoBean>>() {
                    @Override
                    public void accept(ResultBean<UserInfoBean> userInfoBeanResultBean) throws Exception {
                        UserInfoBean userInfoBean = userInfoBeanResultBean.getData();
                        if(userInfoBean!=null){
                            GlideUtils.loadImageShop(GoodsManageHomeActivity.this,userInfoBean.getHead_portrait(), imgHead);
                            tvShopName.setText(userInfoBean.getShop_name());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }


//    private void getShopInfo() {
//        disposable.add(ApiUtils.getInstance().getShopInfo(shopId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<ResultBean<ShopInfoBean>>() {
//                    @Override
//                    public void accept(ResultBean<ShopInfoBean> shopInfoBeanResultBean) throws Exception {
//                        getShopInfoSuccess(shopInfoBeanResultBean.getData());
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                    }
//                }));
//    }

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
                            tvSaleNum.setText(data.getOnsale_num());
                            tvAuctionNum.setText(data.getAuction_num());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        EventManager.getInstance().unRegisterListener(onNotifyListener);
    }
}
