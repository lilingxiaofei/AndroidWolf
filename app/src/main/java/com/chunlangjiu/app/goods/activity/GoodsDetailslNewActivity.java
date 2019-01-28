package com.chunlangjiu.app.goods.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.abase.BaseApplication;
import com.chunlangjiu.app.amain.activity.LoginActivity;
import com.chunlangjiu.app.amain.bean.CartCountBean;
import com.chunlangjiu.app.cart.CartActivity;
import com.chunlangjiu.app.cart.ChoiceNumDialog;
import com.chunlangjiu.app.goods.bean.ConfirmOrderBean;
import com.chunlangjiu.app.goods.bean.EvaluateListBean;
import com.chunlangjiu.app.goods.bean.GivePriceBean;
import com.chunlangjiu.app.goods.bean.GoodsDetailBean;
import com.chunlangjiu.app.goods.bean.PaymentBean;
import com.chunlangjiu.app.goods.bean.RecommendGoodsBean;
import com.chunlangjiu.app.goods.dialog.CallDialog;
import com.chunlangjiu.app.goods.dialog.InputPriceDialog;
import com.chunlangjiu.app.goods.dialog.PayDialog;
import com.chunlangjiu.app.goods.dialog.PriceListDialog;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.order.activity.OrderMainActivity;
import com.chunlangjiu.app.order.params.OrderParams;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.PayResult;
import com.chunlangjiu.app.util.ShareUtils;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.glide.BannerGlideLoader;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SPUtils;
import com.pkqup.commonlibrary.util.SizeUtils;
import com.pkqup.commonlibrary.util.TimeUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.pkqup.commonlibrary.view.countdownview.CountdownView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.transformer.AccordionTransformer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class GoodsDetailslNewActivity extends BaseActivity {
    private static final int SDK_PAY_FLAG = 1;
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
    private ImageView ivStoreLevel ;
    private TextView tvStoreDesc;
    private TextView tvLookAll;

    private RelativeLayout rlEvaluate;
    private TextView tvEvaluate;
    private LinearLayout llEvaluate;

    private LinearLayout llAuctionInfo;//拍品详情
    private LinearLayout llInfoList;

    private ImageView ivSafeguard;

    private LinearLayout llSeeMore;
    private RecyclerView recyclerView;//推荐商品列表

    private RelativeLayout rlBottom;
    private ImageView ivService;
    private ImageView ivCollect;

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


    private CompositeDisposable disposable;
    private GoodsDetailBean goodsDetailBean;//商品详情数据
    private GoodsDetailBean.Auction auction;//竞拍信息
    private String itemId;
    private String skuId;
    private int cartCount;//购物车数量
    private int realStock = 1;//库存

    private boolean isFavorite = false;//是否收藏

    private List<ImageView> imageViews;
    private List<String> bannerUrls;

    private List<RecommendGoodsBean> recommendLists;
    private RecommendAdapter recommendAdapter;

    //竞拍所需数据
    private List<GivePriceBean> priceList;//出价列表
    private IWXAPI wxapi;
    private String payment_id;
    private List<PaymentBean.PaymentInfo> payList;
    private InputPriceDialog inputPriceDialog;
    private PayDialog payDialog;


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
                case R.id.tvAuctionBuy:
                    toConfirmOrder();
                    break;
                case R.id.tvPriceList://查看出价
//                    showPriceListDialog();
                    GoodsPriceListActivity.startActivity(GoodsDetailslNewActivity.this,goodsDetailBean.getItem().getAuction().getAuctionitem_id());
                    break;
                case R.id.tvLookAll://查看店铺
                    ShopMainActivity.startShopMainActivity(GoodsDetailslNewActivity.this, goodsDetailBean.getShop().getShop_id());
                    break;
                case R.id.rlEvaluate://评价
//                    EventManager.getInstance().notify(null, ConstantMsg.CHANGE_TO_EVALUATE);
                    GoodsEvaluateActivity.startActivity(GoodsDetailslNewActivity.this,itemId);
                    break;
                case R.id.llSeeMore://查看更多
                    GoodsListNewActivity.startGoodsListNewActivity(GoodsDetailslNewActivity.this, "", "", "");
                    break;
                case R.id.tvMoreExplain:
                    AuctionExplainActivity.startActivity(GoodsDetailslNewActivity.this);
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
                    case R.id.ivService://聊天
                        showCallDialog();
                        break;
                    case R.id.tvAuctionBuy:
                        toConfirmOrder();
                        break;
                    case R.id.ivCollect://收藏
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
        initPay();
    }

    private void initPay() {
        wxapi = WXAPIFactory.createWXAPI(this, null);
        wxapi.registerApp("wx0e1869b241d7234f");
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
        ivStoreLevel = findViewById(R.id.ivStoreLevel);
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
        llInfoList = findViewById(R.id.llInfoList);

        ivSafeguard = findViewById(R.id.ivSafeguard);

        //底部栏代码
        rlBottom = findViewById(R.id.rlBottom);
        ivService = findViewById(R.id.ivService);
        ivCollect = findViewById(R.id.ivCollect);
        rlBottom.setVisibility(View.GONE);

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


        tvMoreExplain.setOnClickListener(onClickListener);
        tvBuy.setOnClickListener(onClickListenerLogin);
        tvPriceList.setOnClickListener(onClickListener);
        tvAddCart.setOnClickListener(onClickListenerLogin);
        ivService.setOnClickListener(onClickListenerLogin);
        ivCollect.setOnClickListener(onClickListenerLogin);
        rlCart.setOnClickListener(onClickListenerLogin);
        tvAuctionBuy.setOnClickListener(onClickListenerLogin);
        itemId = getIntent().getStringExtra("goodsId");
        disposable = new CompositeDisposable();
    }

    private void initData() {
        getCartNum();
        getGoodsDetail();
        getEvaluateData();
        getRecommendGoods();
    }


    private void initBannerData() {
        try {
            imageViews = new ArrayList<>();
            bannerUrls = goodsDetailBean.getItem().getImages();

            banner.setImages(bannerUrls)
                    .setImageLoader(new BannerGlideLoader())
                    .setBannerStyle(BannerConfig.NOT_INDICATOR)//去掉自带的indicator
                    .setBannerAnimation(AccordionTransformer.class)
                    .isAutoPlay(true)
                    .setDelayTime(4000)
                    .start();

            imageViews.clear();
            indicator.removeAllViews();
            for (int i = 0; i < bannerUrls.size(); i++) {
                ImageView imageView = new ImageView(GoodsDetailslNewActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 8;
                params.rightMargin = 8;
                if (i == 0) {
                    imageView.setImageResource(R.drawable.banner_select);
                } else {
                    imageView.setImageResource(R.drawable.banner_unselect);
                }
                imageViews.add(imageView);
                indicator.addView(imageView, params);
            }
            banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < imageViews.size(); i++) {
                        if (position == i) {
                            imageViews.get(i).setImageResource(R.drawable.banner_select);
                        } else {
                            imageViews.get(i).setImageResource(R.drawable.banner_unselect);
                        }
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

            //设置banner点击事件
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    toLargeImage(position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toLargeImage(int position) {
        new PhotoPagerConfig.Builder(GoodsDetailslNewActivity.this)
                .setBigImageUrls((ArrayList<String>) bannerUrls)
                .setSavaImage(false)
                .setPosition(position)
//                        .setSaveImageLocalPath("这里是你想保存的图片地址")
                .build();
    }

    private void initCommonView() {
        try {
            tvPrice.setText("¥" + goodsDetailBean.getItem().getPrice());
            tvGoodsName.setText(goodsDetailBean.getItem().getTitle());
            tvGoodsNameSecond.setText(goodsDetailBean.getItem().getSub_title());
            if (TextUtils.isEmpty(goodsDetailBean.getItem().getSub_title())) {
                tvGoodsNameSecond.setVisibility(View.GONE);
            } else {
                tvGoodsNameSecond.setVisibility(View.VISIBLE);
            }
            Glide.with(this).load(goodsDetailBean.getItem().getService_url()).into(ivSafeguard);
            tvCountry.setText(goodsDetailBean.getItem().getLabel());
            tvDesc.setText(goodsDetailBean.getItem().getExplain());
            GlideUtils.loadImageShop(GoodsDetailslNewActivity.this, goodsDetailBean.getShop().getShop_logo(), imgStore);
            tvStoreName.setText(goodsDetailBean.getShop().getShop_name());
            tvStoreDesc.setText(goodsDetailBean.getShop().getShop_descript());

            String level = goodsDetailBean.getShop().getGrade();
            if("2".equals(level)){
                ivStoreLevel.setImageResource(R.mipmap.store_partner);
            }else if("1".equals(level)){
                ivStoreLevel.setImageResource(R.mipmap.store_star);
            }else{
                ivStoreLevel.setImageResource(R.mipmap.store_common);
            }

            Object parameter = goodsDetailBean.getItem().getParameter();
            String arrayStr = parameter == null?"":parameter.toString();
            JSONArray jsonArray = new JSONArray(arrayStr);
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = -1; i < jsonArray.length(); i++) {
                    View infoView = getLayoutInflater().inflate(R.layout.goods_activity_details_new_table_item, null);
                    TextView tvLabel = infoView.findViewById(R.id.tvLabel);
                    TextView tvValue = infoView.findViewById(R.id.tvValue);

                    if(i==-1){
                        tvLabel.setText("商品名称：");
                        tvValue.setText(goodsDetailBean.getItem().getTitle());
                        llInfoList.addView(infoView);
                    }else{
                        JSONObject objItem = jsonArray.getJSONObject(i);
                        if(!TextUtils.isEmpty(objItem.optString("value"))&& !TextUtils.isEmpty(objItem.optString("value"))){
                            tvLabel.setText(objItem.optString("title")+"：");
                            tvValue.setText(objItem.optString("value"));
                            llInfoList.addView(infoView);
                        }
                    }

                }
                llAuctionInfo.setVisibility(View.VISIBLE);
            } else {
                llAuctionInfo.setVisibility(View.GONE);
            }

            if (TextUtils.isEmpty(goodsDetailBean.getItem().getLabel())) {
                tvCountry.setVisibility(View.GONE);
            } else {
                tvCountry.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getEvaluateData() {
        disposable.add(ApiUtils.getInstance().getEvaluateList(itemId, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<EvaluateListBean>>() {
                    @Override
                    public void accept(ResultBean<EvaluateListBean> evaluateListBeanResultBean) throws Exception {
                        List<EvaluateListBean.EvaluateDetailBean> list = evaluateListBeanResultBean.getData().getList();
                        initEvaluateView(list);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void initEvaluateView(List<EvaluateListBean.EvaluateDetailBean> list) {
        llEvaluate.removeAllViews();
        for (int i = 0; i < list.size(); i++) {
            if (i < 2) {
                View evaluateView = View.inflate(GoodsDetailslNewActivity.this, R.layout.goods_item_details_evaluate, null);
                TextView tvName = evaluateView.findViewById(R.id.tvName);
                RatingBar ratingBar = evaluateView.findViewById(R.id.ratingBar);
                TextView tvContent = evaluateView.findViewById(R.id.tvContent);
                TextView tvTime = evaluateView.findViewById(R.id.tvTime);
                tvName.setText(list.get(i).getUser_name());
                ratingBar.setRating(3);
                tvContent.setText(list.get(i).getContent());
                tvTime.setText(TimeUtils.millisToYearMD(list.get(i).getCreated_time() + "000"));
                llEvaluate.addView(evaluateView);
            }
        }
    }


    private void getRecommendGoods() {
        disposable.add(ApiUtils.getInstance().getRecommendGoods(itemId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<List<RecommendGoodsBean>>>() {
                    @Override
                    public void accept(ResultBean<List<RecommendGoodsBean>> listResultBean) throws Exception {
                        List<RecommendGoodsBean> list = listResultBean.getData();
                        if (list != null && list.size() > 0) {
                            initRecommendView(list);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }


    private void initRecommendView(List<RecommendGoodsBean> list) {
        recommendLists = list;
        recommendAdapter = new RecommendAdapter(R.layout.goods_item_recommend, recommendLists);
        ;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        GridSpacingItemDecoration decoration2 = new GridSpacingItemDecoration(2, Utils.dp2px(this, 5), false);
        recyclerView.addItemDecoration(decoration2);
        recyclerView.setAdapter(recommendAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recommendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                /*if ("true".equals(recommendLists.get(position).getAuction_status())) {
                    AuctionDetailActivity.startAuctionDetailsActivity(GoodsDetailsNewActivity.this, recommendLists.get(position).getGoodsDetailslNewActivity     } else {
                    GoodsDetailsActivity.startGoodsDetailsActivity(GoodsDetailslNewActivity.this, recommendLists.get(position).getItem_id());
                }*/
                GoodsDetailslNewActivity.startActivity(GoodsDetailslNewActivity.this, recommendLists.get(position).getItem_id());
            }
        });
    }


    public class RecommendAdapter extends BaseQuickAdapter<RecommendGoodsBean, BaseViewHolder> {
        public RecommendAdapter(int layoutResId, List<RecommendGoodsBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, RecommendGoodsBean item) {
            ImageView imgPic = helper.getView(R.id.img_pic);
            ViewGroup.LayoutParams layoutParams = imgPic.getLayoutParams();
            int screenWidth = SizeUtils.getScreenWidth();
            int picWidth = (screenWidth - SizeUtils.dp2px(25)) / 2;
            layoutParams.height = picWidth;
            layoutParams.width = picWidth;
            imgPic.setLayoutParams(layoutParams);

            TextView tv_name = helper.getView(R.id.tv_name);
            ViewGroup.LayoutParams nameLayoutParams = tv_name.getLayoutParams();
            nameLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            nameLayoutParams.width = picWidth;
            tv_name.setLayoutParams(nameLayoutParams);
            helper.setText(R.id.tv_name, item.getTitle());
            helper.setText(R.id.tv_price, "¥" + item.getPrice());
            GlideUtils.loadImage(GoodsDetailslNewActivity.this, item.getImage_default_id(), imgPic);
        }
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
                        auction = goodsDetailBean.getItem().getAuction();
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
        initBannerData();
        initCommonView();
        rlBottom.setVisibility(View.VISIBLE);


        if ("true".equals(goodsDetailBean.getItem().getIs_collect())) {
            isFavorite = true;
            ivCollect.setImageResource(R.mipmap.collect_true);
        } else {
            isFavorite = false;
            ivCollect.setImageResource(R.mipmap.collect_false);
        }
        setAuctionView();
    }

    private void setAuctionView() {
        if (auction != null && !TextUtils.isEmpty(auction.getAuctionitem_id())) {
            getPaymentList();
            getPriceList();
            String check = goodsDetailBean.getItem().getAuction().getCheck();
            String isPay = goodsDetailBean.getItem().getAuction().getIs_pay();
            if ("true".equals(check)) {
                if ("true".equals(isPay)) {
                    //已经出过价并且已支付
                    tvAuctionBuy.setText("修改出价");
                    tvPayMoney.setText("已付定金:¥" + goodsDetailBean.getItem().getAuction().getPledge());
                } else {
                    //已经出过价但是未支付
                    tvAuctionBuy.setText("去支付");
                    tvPayMoney.setText("应付定金:¥" + goodsDetailBean.getItem().getAuction().getPledge());
                    payment_id = auction.getPayment_id();
                }
            } else {
                tvAuctionBuy.setText("立即出价");
                tvPayMoney.setText("应付定金:¥" + goodsDetailBean.getItem().getAuction().getPledge());
            }


            tvPrice.setText("¥" + auction.getStarting_price());
            try {
                String end_time = auction.getEnd_time();
                long endTime = 0;
                if (!TextUtils.isEmpty(end_time)) {
                    endTime = Long.parseLong(end_time);
                }
                if ((endTime * 1000 - System.currentTimeMillis()) > 0) {
                    countdownView.start(endTime * 1000 - System.currentTimeMillis());
                    countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                        @Override
                        public void onEnd(CountdownView cv) {
                            EventManager.getInstance().notify(null, ConstantMsg.DETAIL_COUNT_END);
                        }
                    });
                    dealWithLifeCycle();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String status = goodsDetailBean.getItem().getAuction().getAuction_status();
            if ("true".equals(status)) {
                //明拍
                tvType.setText("明拍");
            } else {
                tvType.setText("暗拍");
                tvPriceList.setVisibility(View.GONE);
            }

            rlCart.setVisibility(View.GONE);
            tvBuy.setVisibility(View.GONE);
            tvAddCart.setVisibility(View.GONE);

            rlAuctionTime.setVisibility(View.VISIBLE);
            rlAuctionExplain.setVisibility(View.VISIBLE);
            llAuctionMode.setVisibility(View.VISIBLE);
            tvAuctionBuy.setVisibility(View.VISIBLE);
            tvPayMoney.setVisibility(View.VISIBLE);
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
                            ivCollect.setImageResource(R.mipmap.collect_false);
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
                            ivCollect.setImageResource(R.mipmap.collect_true);
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
        EventManager.getInstance().unRegisterListener(onNotifyListener);
    }


    private void toConfirmOrder() {
        String check = goodsDetailBean.getItem().getAuction().getCheck();
        String isPay = goodsDetailBean.getItem().getAuction().getIs_pay();
        if ("true".equals(check)) {
            if ("true".equals(isPay)) {
                if (inputPriceDialog == null) {
                    inputPriceDialog = new InputPriceDialog(this, goodsDetailBean.getItem().getAuction().getMax_price(),
                            goodsDetailBean.getItem().getAuction().getOriginal_bid());
                    inputPriceDialog.setCallBack(new InputPriceDialog.CallBack() {
                        @Override
                        public void editPrice(String price) {
                            editGivePrice(price);
                        }
                    });
                }
                inputPriceDialog.show();
            } else {
                showPayMethodDialog();
            }
        } else {
            AuctionConfirmOrderActivity.startConfirmOrderActivity(this, goodsDetailBean);
        }
    }


    private void editGivePrice(String price) {
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().auctionAddPrice(goodsDetailBean.getItem().getAuction().getAuctionitem_id(), price)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showShort("修改出价成功");
                        finish();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }


    private void getPaymentList() {
        disposable.add(ApiUtils.getInstance().getPayment()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<PaymentBean>>() {
                    @Override
                    public void accept(ResultBean<PaymentBean> paymentBeanResultBean) throws Exception {
                        payList = paymentBeanResultBean.getData().getList();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                    }
                }));
    }


    private void showPayMethodDialog() {
        if (payList == null & payList.size() == 0) {
            ToastUtils.showShort("获取支付方式失败");
        } else {
            if (payDialog == null) {
                payDialog = new PayDialog(this, payList);
                payDialog.setCallBack(new PayDialog.CallBack() {
                    @Override
                    public void choicePayMethod(int payMethod, String payMethodId) {
                        payMoney(payMethod, payMethodId);
                    }
                });
            }
            payDialog.show();
        }
    }

    private void payMoney(final int payMethod, String payMethodId) {
        disposable.add(ApiUtils.getInstance().payDo(payment_id, payMethodId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        invokePay(payMethod, resultBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }


    private void invokePay(int payMethod, ResultBean data) {
        switch (payMethod) {
            case 0:
                invokeWeixinPay(data);
                break;
            case 1:
                invokeZhifubaoPay(data);
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    private void invokeWeixinPay(ResultBean data) {
        PayReq request = new PayReq();
        request.appId = "wx0e1869b241d7234f";
        request.partnerId = data.getPartnerid();
        request.prepayId = data.getPrepayid();
        request.packageValue = data.getPackageName();
        request.nonceStr = data.getNoncestr();
        request.timeStamp = data.getTimestamp();
        request.sign = data.getSign();
        wxapi.sendReq(request);
    }

    /**
     * 以下两个接口代替 activity.onStart() 和 activity.onStop(), 控制 timer 的开关
     */
    private void dealWithLifeCycle() {
        countdownView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                String end_time = goodsDetailBean.getItem().getAuction().getEnd_time();
                try {
                    long endTime = 0;
                    if (!TextUtils.isEmpty(end_time)) {
                        endTime = Long.parseLong(end_time);
                    }
                    if ((endTime * 1000 - System.currentTimeMillis()) > 0) {
                        countdownView.start(endTime * 1000 - System.currentTimeMillis());
                        countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                            @Override
                            public void onEnd(CountdownView cv) {
                                EventManager.getInstance().notify(null, ConstantMsg.DETAIL_COUNT_END);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                countdownView.stop();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == SDK_PAY_FLAG) {
                @SuppressWarnings("unchecked")
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    Toast.makeText(GoodsDetailslNewActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Toast.makeText(GoodsDetailslNewActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    finish();
                }
                toOrderMainActivity(1, 0);
            }
        }
    };

    private void invokeZhifubaoPay(ResultBean data) {
        final String url = data.getUrl();
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(GoodsDetailslNewActivity.this);
                Map<String, String> stringStringMap = alipay.payV2(url, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = stringStringMap;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            detailCountEnd(eventTag);
            weixinPaySuccess(object, eventTag);
        }
    };

    private void detailCountEnd(String eventTag) {
        if (eventTag.equals(ConstantMsg.DETAIL_COUNT_END)) {
            finish();
        }
    }

    private void weixinPaySuccess(Object object, String eventTag) {
        if (eventTag.equals(ConstantMsg.WEIXIN_PAY_CALLBACK)) {
            int code = (int) object;
            if (code == 0) {
                //支付成功
                ToastUtils.showShort("支付成功");
                EventManager.getInstance().notify(null, ConstantMsg.UPDATE_CART_LIST);
            } else if (code == -1) {
                //支付错误
                ToastUtils.showShort("支付失败");
            } else if (code == -2) {
                //支付取消
                ToastUtils.showShort("支付取消");
            }
            finish();
            toOrderMainActivity(1, 0);
        }
    }

    private void getPriceList() {
        disposable.add(ApiUtils.getInstance().getAuctionPriceList(goodsDetailBean.getItem().getAuction().getAuctionitem_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<List<GivePriceBean>>>() {
                    @Override
                    public void accept(ResultBean<List<GivePriceBean>> listResultBean) throws Exception {
                        priceList = listResultBean.getData();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private PriceListDialog priceListDialog;

    private void showPriceListDialog() {
        if (priceList == null || priceList.size() == 0) {
            ToastUtils.showShort("暂无出价");
        } else {
            if (priceListDialog == null) {
                priceListDialog = new PriceListDialog(this, priceList);
            }
            priceListDialog.show();
        }
    }

    private void toOrderMainActivity(int type, int target) {
        Intent intent = new Intent(this, OrderMainActivity.class);
        intent.putExtra(OrderParams.TYPE, type);
        intent.putExtra(OrderParams.TARGET, target);
        startActivity(intent);
    }
}
