package com.chunlangjiu.app.goods.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.abase.BaseApplication;
import com.chunlangjiu.app.amain.bean.HomeBean;
import com.chunlangjiu.app.goods.adapter.GoodsAdapter;
import com.chunlangjiu.app.goods.bean.GoodsListDetailBean;
import com.chunlangjiu.app.goods.bean.StoreActivityBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.MyStatusBarUtils;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.net.bean.ResultBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/7/5
 * @Describe: 活动页面
 */
public class FestivalActivity extends BaseActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.ivBgHead)
    ImageView ivBgHead ;
    @BindView(R.id.ivBgBottom)
    ImageView ivBgBottom;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.rlRootLayout)
    RelativeLayout rlRootLayout;


    private CompositeDisposable disposable;
    private StoreActivityBean activityBean;
    private List<GoodsListDetailBean> productList = new ArrayList<>();
    private GoodsAdapter goodsAdapter;

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, FestivalActivity.class);
        activity.startActivity(intent);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ivBack:
                    finish();
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
        setContentView(R.layout.goods_activity_festival);
        initFilterView();
        initView();
        initData();
    }

    private void initFilterView() {
        ivBack.setOnClickListener(onClickListener);
        tv_title.setText("店铺活动");
    }


    private void initView() {
        MyStatusBarUtils.setStatusBar(this, ContextCompat.getColor(this, R.color.bg_red));
        MyStatusBarUtils.setFitsSystemWindows(findViewById(R.id.rlShopTitle), true);

        productList = new ArrayList<>();
        goodsAdapter = new GoodsAdapter(this, productList);
        goodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsListDetailBean details = productList.get(position);
                GoodsDetailslNewActivity.startActivity(FestivalActivity.this, details.getItem_id());
            }
        });

        recycleView.setLayoutManager(new LinearLayoutManager(this));
        GridSpacingItemDecoration decoration2 = new GridSpacingItemDecoration(1, Utils.dp2px(this, 5), false);
        recycleView.addItemDecoration(decoration2);
        recycleView.setAdapter(goodsAdapter);

        goodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsListDetailBean homeBean = productList.get(position);
                if(view.getId() == R.id.rl_store_layout){
                    ShopMainActivity.startShopMainActivity(FestivalActivity.this, homeBean.getShop_id());
                }else if(view.getId() == R.id.tvMoreAuction){
                    EventManager.getInstance().notify(null, BaseApplication.HIDE_AUCTION ? ConstantMsg.MSG_PAGE_CLASS : ConstantMsg.MSG_PAGE_AUCTION);
                }
            }
        });

        goodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsListDetailBean homeBean = productList.get(position);
                if (homeBean.getItemType() == HomeBean.ITEM_GOODS || homeBean.getItemType() == HomeBean.ITEM_GRID_GOODS) {
                    GoodsDetailslNewActivity.startActivity(FestivalActivity.this, homeBean.getItem_id());
                }
            }
        });
    }

    private void initData() {
        disposable = new CompositeDisposable();
        getHomeList();
    }

    private void getHomeList() {
        disposable.add(ApiUtils.getInstance().getStoreActivityLists()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<StoreActivityBean>>() {
                    @Override
                    public void accept(ResultBean<StoreActivityBean> homeListBeanResultBean) throws Exception {
                        activityBean = homeListBeanResultBean.getData();
                        getListSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void getListSuccess() {

        if (activityBean != null) {
            productList.clear();
            GlideUtils.loadImage(this, activityBean.getTop_img(),ivBgHead);
            GlideUtils.loadImage(this, activityBean.getBottom_img(), ivBgBottom);
            try {
                rlRootLayout.setBackgroundColor(Color.parseColor(activityBean.getColor()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<GoodsListDetailBean> dataLists = activityBean.getItem();
            List<GoodsListDetailBean> auctionItems = activityBean.getItem();

            if (!BaseApplication.HIDE_AUCTION && auctionItems != null) {
                //过滤竞拍商品
                GoodsListDetailBean detailBean = new GoodsListDetailBean();
                detailBean.setItemType(GoodsListDetailBean.ITEM_JINGPAI);
                productList.add(detailBean);
                productList.addAll(auctionItems);
            }

            if (dataLists != null) {
                GoodsListDetailBean detailBean = new GoodsListDetailBean();
                detailBean.setItemType(GoodsListDetailBean.ITEM_TUIJIAN);
                productList.add(detailBean);
                productList.addAll(dataLists);
            }
        }
        goodsAdapter.setListType(GoodsAdapter.LIST_LINEAR);
        goodsAdapter.setNewData(productList);
        goodsAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.common_empty_view, (ViewGroup) recycleView.getParent(), false));
    }

    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
