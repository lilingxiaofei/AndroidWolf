package com.chunlangjiu.app.goods.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.abase.BaseApplication;
import com.chunlangjiu.app.abase.BaseFragmentAdapter;
import com.chunlangjiu.app.amain.activity.LoginActivity;
import com.chunlangjiu.app.amain.bean.CartCountBean;
import com.chunlangjiu.app.cart.CartActivity;
import com.chunlangjiu.app.cart.ChoiceNumDialog;
import com.chunlangjiu.app.goods.bean.ConfirmOrderBean;
import com.chunlangjiu.app.goods.bean.GoodsDetailBean;
import com.chunlangjiu.app.goods.bean.RecommendGoodsBean;
import com.chunlangjiu.app.goods.dialog.CallDialog;
import com.chunlangjiu.app.goods.fragment.GoodsCommentFragment;
import com.chunlangjiu.app.goods.fragment.GoodsDetailsFragment;
import com.chunlangjiu.app.goods.fragment.GoodsWebFragment;
import com.chunlangjiu.app.goods.fragment.ScrollViewFragment;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.ShareUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.SizeUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.pkqup.commonlibrary.view.countdownview.CountdownView;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class GoodsDetailslNewActivity extends BaseActivity {

    //竞拍和普通商品公用的UI
    private RelativeLayout rlBanner;
    private Banner banner;
    private LinearLayout indicator;
    private TextView tvPrice;
    private TextView tvGoodsName;
    private TextView tvGoodsNameSecond;
    private TextView tvCountry;
    private TextView tvYear;
    private TextView tvDesc;

    private CircleImageView imgStore;
    private TextView tvStoreName;
    private TextView tvStoreDesc;
    private TextView tvLookAll;

    private RelativeLayout rlEvaluate;
    private TextView tvEvaluate;
    private LinearLayout llEvaluate;

    private LinearLayout llAuctionInfo;//拍品详情
    private TextView tvAuName;//名称
    private TextView tvAuYear;//年份
    private TextView tvAuChateau;//酒庄
    private TextView tvAuVolume;//容量
    private TextView tvAuMaterial;//原料
    private TextView tvAuStorage;//储存条件
    private TextView tvAuResources;//来源

    private LinearLayout llSeeMore;
    private RecyclerView recyclerView;//推荐商品列表

    private RelativeLayout rlBottom;
    private ImageView ivService;
    private ImageView ivCollect;


    private List<ImageView> imageViews;
    private List<String> bannerUrls;

    private List<RecommendGoodsBean> recommendLists;

    private CompositeDisposable disposable;
    private GoodsDetailBean goodsDetailBean;
    private String itemId;
    private String skuId;
    private int cartCount;//购物车数量
    private int realStock = 1;//库存

    private boolean isFavorite = false;//是否收藏

    private ChoiceNumDialog buyNowDialog;
    private ChoiceNumDialog addCartDialog;
    private CallDialog callDialog;


    //普通商品需要和竞拍区分的UI
    private TextView tvBuy;
    private TextView tvAddCart;
    private RelativeLayout rlCart;
    private TextView tvCartNum;

    //竞拍需要和普通商品有差别的UI
    private RelativeLayout rlAuctionTime;
    private CountdownView countdownView;
    private RelativeLayout rlAuctionExplain;
    private TextView tvMoreExplain;
    private LinearLayout llAuctionMode;
    private TextView tvType;
    private TextView tvPriceList;
    private TextView tvAuctionBuy;
    private TextView tvPayMoney;


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_title_left:
                    finish();
                    break;
                case R.id.img_title_right_one://分享
                    showShare();
                    break;
            }
        }
    };

    private View.OnClickListener onClickListenerLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (BaseApplication.isLogin()) {
                switch (view.getId()) {
                    case R.id.tvAddCart://加入购物车
                        showAddCartDialog();
                        break;
                    case R.id.tvBuy://立即购买
                        showBuyNowDialog();
                        break;
                    case R.id.rlChat://聊天
                        showCallDialog();
                        break;
                    case R.id.rlCollect://收藏
                        changeCollectStatus();
                        break;
                    case R.id.rlCart://跳转到购物车
                        startActivity(new Intent(GoodsDetailslNewActivity.this, CartActivity.class));
                        break;
                }
            } else {
                startActivity(new Intent(GoodsDetailslNewActivity.this, LoginActivity.class));
            }
        }
    };


    public static void startActivity(Activity activity, String goodsId) {
        Intent intent = new Intent(activity, GoodsDetailslNewActivity.class);
        intent.putExtra("goodsId", goodsId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_activity_details_new);
        initView();
        initData();
    }

    @Override
    public void setTitleView() {
        titleName.setText("商品详情");
        titleImgRightOne.setVisibility(View.VISIBLE);
        titleImgRightOne.setImageResource(R.mipmap.white_share);
        titleImgRightOne.setOnClickListener(onClickListener);
        titleImgLeft.setOnClickListener(onClickListener);
    }


    private void initView() {
        rlBanner = findViewById(R.id.rlBanner);
        banner = findViewById(R.id.banner);
        indicator = findViewById(R.id.indicator);

        int screenWidth = SizeUtils.getScreenWidth();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlBanner.getLayoutParams();
        layoutParams.height = screenWidth;
        rlBanner.setLayoutParams(layoutParams);

        tvPrice = findViewById(R.id.tvPrice);
        tvGoodsName = findViewById(R.id.tvGoodsName);
        tvGoodsNameSecond = findViewById(R.id.tvGoodsNameSecond);
        tvCountry = findViewById(R.id.tvCountry);
        tvYear = findViewById(R.id.tvYear);
        tvDesc = findViewById(R.id.tvDesc);

        imgStore = findViewById(R.id.imgStore);
        tvStoreName = findViewById(R.id.tvStoreName);
        tvStoreDesc = findViewById(R.id.tvStoreDesc);
        tvLookAll = findViewById(R.id.tvLookAll);
        tvLookAll.setOnClickListener(onClickListener);

        rlEvaluate = findViewById(R.id.rlEvaluate);
        rlEvaluate.setOnClickListener(onClickListener);
        tvEvaluate = findViewById(R.id.tvEvaluate);
        llEvaluate = findViewById(R.id.llEvaluate);
        llSeeMore = findViewById(R.id.llSeeMore);
        llSeeMore.setOnClickListener(onClickListener);
        recyclerView = findViewById(R.id.recyclerView);


        llAuctionInfo = findViewById(R.id.llAuctionInfo);//拍品详情
        tvAuName = findViewById(R.id.tvAuName);//名称
        tvAuYear = findViewById(R.id.tvAuYear);//年份
        tvAuChateau = findViewById(R.id.tvAuChateau);//酒庄
        tvAuVolume = findViewById(R.id.tvAuVolume);//容量
        tvAuMaterial = findViewById(R.id.tvAuMaterial);//原料
        tvAuStorage = findViewById(R.id.tvAuStorage);//储存条件
        tvAuResources = findViewById(R.id.tvAuResources);//来源

        //普通商品需要和竞拍区分的UI
        tvBuy = findViewById(R.id.tvBuy);
        tvAddCart = findViewById(R.id.tvAddCart);
        rlCart = findViewById(R.id.rlCart);
        tvCartNum = findViewById(R.id.tvCartNum);


        //竞拍需要和普通商品有差别的UI
        rlAuctionTime = findViewById(R.id.rlAuctionTime);
        countdownView = findViewById(R.id.countdownView);
        rlAuctionExplain = findViewById(R.id.rlAuctionExplain);
        tvMoreExplain = findViewById(R.id.tvMoreExplain);
        llAuctionMode = findViewById(R.id.llAuctionMode);
        tvType = findViewById(R.id.tvType);
        tvPriceList = findViewById(R.id.tvPriceList);
        tvAuctionBuy = findViewById(R.id.tvAuctionBuy);
        tvPayMoney = findViewById(R.id.tvPayMoney);

        rlBottom.setVisibility(View.GONE);
        tvBuy.setOnClickListener(onClickListenerLogin);
        tvAddCart.setOnClickListener(onClickListenerLogin);
        ivService.setOnClickListener(onClickListenerLogin);
        ivCollect.setOnClickListener(onClickListenerLogin);
        rlCart.setOnClickListener(onClickListenerLogin);

        itemId = getIntent().getStringExtra("goodsId");
        disposable = new CompositeDisposable();
    }

    private void initData() {
        getCartNum();
        getGoodsDetail();
    }


    private void getCartNum() {
        disposable.add(ApiUtils.getInstance().getCartCount()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<CartCountBean>>() {
                    @Override
                    public void accept(ResultBean<CartCountBean> cartCountBeanResultBean) throws Exception {
                        String number = cartCountBeanResultBean.getData().getNumber();
                        if (!TextUtils.isEmpty(number) && Integer.parseInt(number) > 0) {
                            cartCount = Integer.parseInt(number);
                            tvCartNum.setVisibility(View.VISIBLE);
                            tvCartNum.setText(number);
                        } else {
                            tvCartNum.setVisibility(View.GONE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void getGoodsDetail() {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().getGoodsDetailWithToken(itemId, (String) SPUtils.get("token", ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<GoodsDetailBean>>() {
                    @Override
                    public void accept(ResultBean<GoodsDetailBean> goodsDetailBeanResultBean) throws Exception {
                        hideLoadingDialog();
                        goodsDetailBean = goodsDetailBeanResultBean.getData();
                        skuId = goodsDetailBean.getItem().getDefault_sku_id();
                        String realStore = goodsDetailBean.getItem().getRealStore();
                        if (!TextUtils.isEmpty(realStore)) {
                            realStock = Integer.parseInt(realStore);
                        }
                        updateView();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                    }
                }));
    }

    private void updateView() {
        rlBottom.setVisibility(View.VISIBLE);
        if ("true".equals(goodsDetailBean.getItem().getIs_collect())) {
            isFavorite = true;
            ivCollect.setBackgroundResource(R.mipmap.collect_true);
        } else {
            isFavorite = false;
            ivCollect.setBackgroundResource(R.mipmap.collect_false);
        }
    }


    private void showShare() {
        UMImage thumb = new UMImage(this, goodsDetailBean.getShare().getImage());
        UMWeb web = new UMWeb(goodsDetailBean.getShare().getH5href());
        web.setTitle(goodsDetailBean.getItem().getTitle());//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription(goodsDetailBean.getItem().getSub_title());//描述

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


    private void changeCollectStatus() {
        if (isFavorite) {
            showLoadingDialog();
            disposable.add(ApiUtils.getInstance().favoriteCancelGoods(itemId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean>() {
                        @Override
                        public void accept(ResultBean resultBean) throws Exception {
                            hideLoadingDialog();
                            isFavorite = false;
                            ivCollect.setBackgroundResource(R.mipmap.collect_false);
                            ToastUtils.showShort("取消收藏成功");
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            hideLoadingDialog();
                            ToastUtils.showShort("取消收藏失败");
                        }
                    }));
        } else {
            showLoadingDialog();
            disposable.add(ApiUtils.getInstance().favoriteAddGoods(itemId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean>() {
                        @Override
                        public void accept(ResultBean resultBean) throws Exception {
                            hideLoadingDialog();
                            isFavorite = true;
                            ivCollect.setBackgroundResource(R.mipmap.collect_true);
                            ToastUtils.showShort("收藏成功");
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            hideLoadingDialog();
                            ToastUtils.showShort("收藏失败");
                        }
                    }));
        }
    }


    private void showAddCartDialog() {
        if (addCartDialog == null) {
            addCartDialog = new ChoiceNumDialog(this, realStock);
            addCartDialog.setCallBackListener(new ChoiceNumDialog.OnCallBackListener() {
                @Override
                public void choiceNum(int num) {
                    addGoodsToCart(num);
                }
            });
        }
        addCartDialog.show();
    }

    private void addGoodsToCart(int num) {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().addGoodsToCart(num, skuId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        getCartNum();
                        EventManager.getInstance().notify(null, ConstantMsg.UPDATE_CART_LIST);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }


    private void showCallDialog() {
        if (callDialog == null) {
            callDialog = new CallDialog(this, goodsDetailBean.getShop().getMobile());
            callDialog.setCallBack(new CallDialog.CallBack() {
                @Override
                public void onConfirm() {
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + goodsDetailBean.getShop().getMobile()));//跳转到拨号界面，同时传递电话号码
                    startActivity(dialIntent);
                }

                @Override
                public void onCancel() {
                }
            });
        }
        callDialog.show();
    }

    private void showBuyNowDialog() {
        if (buyNowDialog == null) {
            buyNowDialog = new ChoiceNumDialog(this, realStock);
            buyNowDialog.setCallBackListener(new ChoiceNumDialog.OnCallBackListener() {
                @Override
                public void choiceNum(int num) {
                    buyNow(num);
                }
            });
        }
        buyNowDialog.show();
    }

    private void buyNow(int num) {
        showLoadingDialog();
        //立即购买流程为先调用添加到购物车接口（模式为fastbuy），然后调用结算接口（模式为fastbuy）
        disposable.add(ApiUtils.getInstance().addGoodsToCartBuyNow(num, skuId)
                .concatMap(new Function<ResultBean, Flowable<ResultBean<ConfirmOrderBean>>>() {
                    @Override
                    public Flowable<ResultBean<ConfirmOrderBean>> apply(ResultBean resultBean) throws Exception {
                        return ApiUtils.getInstance().buyNowConfirmOrder();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<ConfirmOrderBean>>() {
                    @Override
                    public void accept(ResultBean<ConfirmOrderBean> resultBean) throws Exception {
                        hideLoadingDialog();
                        ConfirmOrderActivity.startConfirmOrderActivity(GoodsDetailslNewActivity.this, resultBean.getData(), "fastbuy");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
