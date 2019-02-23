package com.chunlangjiu.app.goods.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.awen.photo.photopick.controller.PhotoPagerConfig;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseFragment;
import com.chunlangjiu.app.goods.activity.GoodsDetailsActivity;
import com.chunlangjiu.app.goods.activity.GoodsListNewActivity;
import com.chunlangjiu.app.goods.activity.ShopMainActivity;
import com.chunlangjiu.app.goods.bean.EvaluateListBean;
import com.chunlangjiu.app.goods.bean.GivePriceBean;
import com.chunlangjiu.app.goods.bean.GoodsDetailBean;
import com.chunlangjiu.app.goods.bean.RecommendGoodsBean;
import com.chunlangjiu.app.goods.dialog.PriceListDialog;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.glide.BannerGlideLoader;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.SizeUtils;
import com.pkqup.commonlibrary.util.TimeUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.pkqup.commonlibrary.view.countdownview.CountdownView;
import com.pkqup.commonlibrary.view.verticalview.VerticalScrollView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.transformer.AccordionTransformer;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/9/12
 * @Describe:
 */
public class AuctionScrollViewFragment extends BaseFragment {

    private VerticalScrollView scrollView;
    private RelativeLayout rlBanner;
    private Banner banner;
    private LinearLayout indicator;
    private TextView tvPrice;
    private TextView tvGoodsName;
    private TextView tvGoodsNameSecond;
    private CountdownView countdownView;
    private TextView tvType;
    private TextView tvPriceList;
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

    private LinearLayout llSeeMore;
    private RecyclerView recyclerView;//推荐商品列表


    private List<ImageView> imageViews;
    private List<String> bannerUrls;

    private List<RecommendGoodsBean> recommendLists;
    private RecommendAdapter recommendAdapter;

    private CompositeDisposable disposable;
    private GoodsDetailBean goodsDetailBean;
    private List<GivePriceBean> priceList;//出价列表

    public static AuctionScrollViewFragment newInstance(GoodsDetailBean goodsDetailBean) {
        AuctionScrollViewFragment auctionScrollViewFragment = new AuctionScrollViewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("goodsDetailBean", goodsDetailBean);
        auctionScrollViewFragment.setArguments(bundle);
        return auctionScrollViewFragment;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvPriceList://查看出价
                    showPriceListDialog();
                    break;
                case R.id.tvLookAll://查看店铺
                    ShopMainActivity.startShopMainActivity(getActivity(), goodsDetailBean.getShop().getShop_id());
                    break;
                case R.id.rlEvaluate://查看评价
                    EventManager.getInstance().notify(null, ConstantMsg.CHANGE_TO_EVALUATE);
                    break;
                case R.id.llSeeMore://查看更多
                    GoodsListNewActivity.startGoodsListNewActivity(getActivity(), "","","", "", "");
                    break;
            }
        }
    };


    @Override
    public void getContentView(LayoutInflater inflater, ViewGroup container) {
        inflater.inflate(R.layout.goods_fragment_auction_scrollview, container, true);
    }

    @Override
    public void initView() {
        scrollView = rootView.findViewById(R.id.scrollView);
        rlBanner = rootView.findViewById(R.id.rlBanner);
        banner = rootView.findViewById(R.id.banner);
        indicator = rootView.findViewById(R.id.indicator);

        int screenWidth = SizeUtils.getScreenWidth();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlBanner.getLayoutParams();
        layoutParams.height = screenWidth;
        rlBanner.setLayoutParams(layoutParams);

        tvPrice = rootView.findViewById(R.id.tvPrice);
        tvGoodsName = rootView.findViewById(R.id.tvGoodsName);
        tvGoodsNameSecond = rootView.findViewById(R.id.tvGoodsNameSecond);
        countdownView = rootView.findViewById(R.id.countdownView);
        tvType = rootView.findViewById(R.id.tvType);
        tvPriceList = rootView.findViewById(R.id.tvPriceList);
        tvPriceList.setOnClickListener(onClickListener);
        tvCountry = rootView.findViewById(R.id.tvCountry);
        tvYear = rootView.findViewById(R.id.tvYear);
        tvDesc = rootView.findViewById(R.id.tvDesc);

        imgStore = rootView.findViewById(R.id.imgStore);
        tvStoreName = rootView.findViewById(R.id.tvStoreName);
        tvStoreDesc = rootView.findViewById(R.id.tvStoreDesc);
        tvLookAll = rootView.findViewById(R.id.tvLookAll);
        tvLookAll.setOnClickListener(onClickListener);

        rlEvaluate = rootView.findViewById(R.id.rlEvaluate);
        rlEvaluate.setOnClickListener(onClickListener);
        tvEvaluate = rootView.findViewById(R.id.tvEvaluate);
        llEvaluate = rootView.findViewById(R.id.llEvaluate);
        llSeeMore = rootView.findViewById(R.id.llSeeMore);
        llSeeMore.setOnClickListener(onClickListener);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        disposable = new CompositeDisposable();
        goodsDetailBean = (GoodsDetailBean) getArguments().getSerializable("goodsDetailBean");
    }

    @Override
    public void initData() {
        initBannerData();
        initCommonView();
        getEvaluateData();
        getPriceList();
        getRecommendGoods();
    }


    private void initBannerData() {
        try {
            imageViews = new ArrayList<>();
            bannerUrls = new ArrayList<>();
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
                ImageView imageView = new ImageView(getActivity());
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
        new PhotoPagerConfig.Builder(getActivity())
                .setBigImageUrls((ArrayList<String>) bannerUrls)
                .setSavaImage(false)
                .setPosition(position)
//                        .setSaveImageLocalPath("这里是你想保存的图片地址")
                .build();
    }


    private void initCommonView() {
        tvPrice.setText("¥" + goodsDetailBean.getItem().getAuction().getStarting_price());
        tvGoodsName.setText(goodsDetailBean.getItem().getTitle());
        tvGoodsNameSecond.setText(goodsDetailBean.getItem().getSub_title());
        if (TextUtils.isEmpty(goodsDetailBean.getItem().getSub_title())) {
            tvGoodsNameSecond.setVisibility(View.GONE);
        } else {
            tvGoodsNameSecond.setVisibility(View.VISIBLE);
        }
        try {
            GoodsDetailBean.Auction auction = goodsDetailBean.getItem().getAuction();
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

        GlideUtils.loadImageShop(getActivity(), goodsDetailBean.getShop().getShop_logo(), imgStore);
        tvStoreName.setText(goodsDetailBean.getShop().getShop_name());
        tvStoreDesc.setText(goodsDetailBean.getShop().getShop_descript());
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

    private void getEvaluateData() {
        disposable.add(ApiUtils.getInstance().getEvaluateList(goodsDetailBean.getItem().getItem_id(), 1)
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
                View evaluateView = View.inflate(getActivity(), R.layout.goods_item_details_evaluate, null);
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
        disposable.add(ApiUtils.getInstance().getRecommendGoods(goodsDetailBean.getItem().getItem_id())
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(recommendAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recommendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                 /*if ("true".equals(recommendLists.get(position).getAuction_status())) {
                    AuctionDetailActivity.startAuctionDetailsActivity(getActivity(), recommendLists.get(position).getItem_id());
                } else {
                    GoodsDetailsActivity.startGoodsDetailsActivity(getActivity(), recommendLists.get(position).getItem_id());
                }*/
                GoodsDetailsActivity.startGoodsDetailsActivity(getActivity(), recommendLists.get(position).getItem_id());
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
            int picWidth = (screenWidth - SizeUtils.dp2px(45)) / 3;
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
            GlideUtils.loadImage(getActivity(), item.getImage_default_id(), imgPic);
        }
    }

    public void goTop() {
        scrollView.goTop();
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
                priceListDialog = new PriceListDialog(getActivity(), priceList);
            }
            priceListDialog.show();
        }
    }

}