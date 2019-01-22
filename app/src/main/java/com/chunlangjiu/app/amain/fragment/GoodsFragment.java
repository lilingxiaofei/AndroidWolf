package com.chunlangjiu.app.amain.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseApplication;
import com.chunlangjiu.app.abase.BaseFragment;
import com.chunlangjiu.app.amain.bean.FirstClassBean;
import com.chunlangjiu.app.amain.bean.MainClassBean;
import com.chunlangjiu.app.amain.bean.SecondClassBean;
import com.chunlangjiu.app.amain.bean.ThirdClassBean;
import com.chunlangjiu.app.goods.activity.AuctionDetailActivity;
import com.chunlangjiu.app.goods.activity.GoodsDetailsActivity;
import com.chunlangjiu.app.goods.activity.SearchActivity;
import com.chunlangjiu.app.goods.activity.ShopMainActivity;
import com.chunlangjiu.app.goods.adapter.GoodsAdapter;
import com.chunlangjiu.app.goods.bean.AlcListBean;
import com.chunlangjiu.app.goods.bean.AreaListBean;
import com.chunlangjiu.app.goods.bean.BrandsListBean;
import com.chunlangjiu.app.goods.bean.GoodsListBean;
import com.chunlangjiu.app.goods.bean.GoodsListDetailBean;
import com.chunlangjiu.app.goods.bean.OrdoListBean;
import com.chunlangjiu.app.goods.bean.PriceBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.dialog.ChoiceAlcPopWindow;
import com.chunlangjiu.app.user.dialog.ChoiceAreaPopWindow;
import com.chunlangjiu.app.user.dialog.ChoiceBrandPopWindow;
import com.chunlangjiu.app.user.dialog.ChoiceOrdoPopWindow;
import com.chunlangjiu.app.user.dialog.ChoicePricePopWindow;
import com.chunlangjiu.app.util.UmengEventUtil;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.KeyBoardUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/9/27
 * @Describe:
 */
public class GoodsFragment extends BaseFragment {
    private Activity activity ;
    private RelativeLayout rlBrand;
    private TextView tvBrand;
    private RelativeLayout rlArea;
    private TextView tvArea;
    private RelativeLayout rlIncense;
    private TextView tvIncense;
    private RelativeLayout rlAlc;
    private TextView tvAlc;
    private RelativeLayout rlPrice;
    private TextView tvPrice;

    //分类列表
    private RecyclerView recyclerViewClass;
    private ClassAdapter classAdapter;
    private List<ThirdClassBean> classLists;
    private String selectClassId = "";

    //商品列表
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private GridSpacingItemDecoration decoration1;
    private GridSpacingItemDecoration decoration2;
    private List<GoodsListDetailBean> lists;
//    private LinearAdapter linearAdapter;
//    private GridAdapter gridAdapter;

    private GoodsAdapter goodsAdapter ;
    private boolean listType = false;//是否是列表形式
    private int pageNum = 1;

    private String brandId = "";
    private String areaId = "";
    private String ordoId = "";
    private String alcoholId = "";
    private String minPrice = "";
    private String maxPrice = "";

    private String intentBrandId;
    private String intentBrandName;
    private boolean isActivity = false;
    private String searchKey = "";

    private CompositeDisposable disposable;

    //品牌列表
    private ChoiceBrandPopWindow choiceBrandPopWindow;
    private List<BrandsListBean.BrandBean> brandLists = new ArrayList<>();

    //产地列表
    private ChoiceAreaPopWindow choiceAreaPopWindow;
    private List<AreaListBean.AreaBean> areaLists = new ArrayList<>();

    //香型列表
    private ChoiceOrdoPopWindow choiceOrdoPopWindow;
    private List<OrdoListBean.OrdoBean> ordoLists = new ArrayList<>();

    //酒精度列表
    private ChoiceAlcPopWindow choiceAlcPopWindow;
    private List<AlcListBean.AlcBean> alcLists = new ArrayList<>();

    //价格列表
    private ChoicePricePopWindow choicePricePopWindow;
    private List<PriceBean> priceLists;
    private String priceId = "";


    public static GoodsFragment newInstance(String searchKey, boolean isActivity, String brandId, String brandName) {
        Bundle bundle = new Bundle();
        bundle.putString("searchKey", searchKey);
        bundle.putBoolean("isActivity", isActivity);
        bundle.putString("brandId", brandId);
        bundle.putString("brandName", brandName);
        GoodsFragment goodsFragment = new GoodsFragment();
        goodsFragment.setArguments(bundle);
        return goodsFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity)context ;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_title_left_f:
                    getActivity().finish();
                    break;
                case R.id.img_title_right_one_f:
                    startActivity(new Intent(getActivity(), SearchActivity.class));
                    break;
                case R.id.img_title_right_two_f:
                    changeListType();
                    break;
                case R.id.rlBrand:
                    showBrandPopWindow();
                    break;
                case R.id.rlArea:
                    showAreaPopWindow();
                    break;
                case R.id.rlIncense:
                    showIncensePopWindow();
                    break;
                case R.id.rlAlc:
                    showDegreePopWindow();
                    break;
                case R.id.rlPrice:
                    showPricePopWindow();
                    break;
            }
        }
    };

    private TextView.OnEditorActionListener onEditorActionListener = new
            TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        if (!TextUtils.isEmpty(titleSearchEdit.getText().toString().trim())) {
                            KeyBoardUtils.hideSoftInput(getActivity());
                            refreshLayout.autoRefresh();
                            searchKey = titleSearchEdit.getText().toString().trim();
                            getGoodsList(pageNum, true);
                        }
                    }
                    return true;
                }
            };


    @Override
    public void getContentView(LayoutInflater inflater, ViewGroup container) {
        inflater.inflate(R.layout.amain_fragment_goods, container, true);
    }

    private void initTitleView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            intentBrandId = bundle.getString("brandId");
            intentBrandName = bundle.getString("brandName");
            brandId = intentBrandId;
            isActivity = bundle.getBoolean("isActivity");
            searchKey = bundle.getString("searchKey");
        }
        titleView.setVisibility(View.VISIBLE);
        tvTitleF.setText("全部商品");
        imgTitleRightTwoF.setImageResource(R.mipmap.icon_list);
        imgTitleLeftF.setOnClickListener(onClickListener);
        imgTitleRightOneF.setOnClickListener(onClickListener);
        imgTitleRightTwoF.setOnClickListener(onClickListener);
        titleSearchEdit.setOnEditorActionListener(onEditorActionListener);
        if (isActivity) {
            if (!TextUtils.isEmpty(searchKey)) {
                //是搜索
                tvTitleF.setVisibility(View.GONE);
                titleSearchView.setVisibility(View.VISIBLE);
                imgTitleRightOneF.setVisibility(View.GONE);
            } else {
                tvTitleF.setVisibility(View.VISIBLE);
                titleSearchView.setVisibility(View.GONE);
                imgTitleRightOneF.setVisibility(View.VISIBLE);
            }
            imgTitleLeftF.setVisibility(View.VISIBLE);
            imgTitleRightTwoF.setVisibility(View.VISIBLE);
        } else {
            tvTitleF.setVisibility(View.VISIBLE);
            titleSearchView.setVisibility(View.GONE);
            imgTitleLeftF.setVisibility(View.GONE);
            imgTitleRightOneF.setVisibility(View.VISIBLE);
            imgTitleRightTwoF.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initView() {
        initTitleView();

        disposable = new CompositeDisposable();
        rlBrand = rootView.findViewById(R.id.rlBrand);
        tvBrand = rootView.findViewById(R.id.tvBrand);
        if (!TextUtils.isEmpty(intentBrandName)) {
            tvBrand.setText(TextUtils.isEmpty(brandId) ? "品牌" : intentBrandName);
        }
        rlArea = rootView.findViewById(R.id.rlArea);
        tvArea = rootView.findViewById(R.id.tvArea);
        rlIncense = rootView.findViewById(R.id.rlIncense);
        tvIncense = rootView.findViewById(R.id.tvIncense);
        rlAlc = rootView.findViewById(R.id.rlAlc);
        tvAlc = rootView.findViewById(R.id.tvAlc);
        rlPrice = rootView.findViewById(R.id.rlPrice);
        tvPrice = rootView.findViewById(R.id.tvPrice);
        rlBrand.setOnClickListener(onClickListener);
        rlArea.setOnClickListener(onClickListener);
        rlIncense.setOnClickListener(onClickListener);
        rlAlc.setOnClickListener(onClickListener);
        rlPrice.setOnClickListener(onClickListener);

        recyclerViewClass = rootView.findViewById(R.id.recyclerViewClass);
        classLists = new ArrayList<>();
        ThirdClassBean thirdClassBean = new ThirdClassBean();
        thirdClassBean.setCat_id("");
        thirdClassBean.setCat_name("全部");
        classLists.add(thirdClassBean);
        classAdapter = new ClassAdapter(R.layout.amain_item_class, classLists);
        recyclerViewClass.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewClass.setAdapter(classAdapter);
        classAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UmengEventUtil.search_category(getActivity(), classLists.get(position).getCat_name());
                selectClassId = classLists.get(position).getCat_id();
                classAdapter.notifyDataSetChanged();
                clearSelectFilterData();
                getGoodsList(1, true);
            }
        });

        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        goodsAdapter = new GoodsAdapter(activity,lists);
        lists = new ArrayList<>();
        goodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsListDetailBean details= lists.get(position);
                if(view.getId() == R.id.rl_store_layout){
                    ShopMainActivity.startShopMainActivity(activity, details.getShop_id());
                }else{
                    if (TextUtils.isEmpty(lists.get(position).getAuction().getAuctionitem_id())) {
                        GoodsDetailsActivity.startGoodsDetailsActivity(getActivity(), details.getItem_id());
                    } else {
                        AuctionDetailActivity.startAuctionDetailsActivity(getActivity(), details.getItem_id());
                    }
                }
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        decoration1 = new GridSpacingItemDecoration(1, Utils.dp2px(activity, 5), false);
        decoration2 = new GridSpacingItemDecoration(2, Utils.dp2px(activity, 5), false);
        recyclerView.addItemDecoration(decoration2);
//        // 设置运营位置一行有多少个Item
//        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                int spanCount = goodsAdapter.isGridLayout()?1:2;
//                return spanCount;
//            }
//        });
        recyclerView.setAdapter(goodsAdapter);

        refreshLayout.setEnableAutoLoadMore(false);//关闭自动加载更多
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getGoodsList(1, true);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                getGoodsList(pageNum + 1, false);
            }
        });
    }


    @Override
    public void initData() {
        initPriceLists();
        getClassData();
        getBrandLists();
        getAreaLists();
        getInsenceLists();
        getAlcLists();
        getGoodsList(pageNum, true);
    }

    private void initPriceLists() {
        priceLists = new ArrayList<>();
        priceLists.add(new PriceBean("", "", ""));
        priceLists.add(new PriceBean("1", "", "999"));
        priceLists.add(new PriceBean("2", "1000", "2999"));
        priceLists.add(new PriceBean("3", "3000", "4999"));
        priceLists.add(new PriceBean("4", "5000", "99999"));
        priceLists.add(new PriceBean("5", "10000", ""));
    }

    private void getBrandLists() {
        disposable.add(ApiUtils.getInstance().getUserBrandList(selectClassId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<BrandsListBean>>() {
                    @Override
                    public void accept(ResultBean<BrandsListBean> brandsListBeanResultBean) throws Exception {
                        brandLists = brandsListBeanResultBean.getData().getList();
                        BrandsListBean.BrandBean brandBean = new BrandsListBean().new BrandBean();
                        brandBean.setBrand_id("");
                        brandBean.setBrand_name("全部");
                        brandLists.add(0, brandBean);

//                        checkIntentBrandId();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }


    private void getAreaLists() {
        disposable.add(ApiUtils.getInstance().getUserAreaList(selectClassId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<AreaListBean>>() {
                    @Override
                    public void accept(ResultBean<AreaListBean> areaListBeanResultBean) throws Exception {
                        areaLists = areaListBeanResultBean.getData().getList();
                        AreaListBean.AreaBean areaBean = new AreaListBean().new AreaBean();
                        areaBean.setArea_id("");
                        areaBean.setArea_name("全部");
                        areaLists.add(0, areaBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void getInsenceLists() {
        disposable.add(ApiUtils.getInstance().getUserOrdoList(selectClassId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<OrdoListBean>>() {
                    @Override
                    public void accept(ResultBean<OrdoListBean> ordoListBeanResultBean) throws Exception {
                        ordoLists = ordoListBeanResultBean.getData().getList();
                        OrdoListBean.OrdoBean ordoBean = new OrdoListBean().new OrdoBean();
                        ordoBean.setOdor_id("");
                        ordoBean.setOdor_name("全部");
                        ordoLists.add(0, ordoBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void getAlcLists() {
        disposable.add(ApiUtils.getInstance().getUserAlcList(selectClassId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<AlcListBean>>() {
                    @Override
                    public void accept(ResultBean<AlcListBean> alcListBeanResultBean) throws Exception {
                        alcLists = alcListBeanResultBean.getData().getList();
                        AlcListBean.AlcBean alcBean = new AlcListBean().new AlcBean();
                        alcBean.setAlcohol_id("");
                        alcBean.setAlcohol_name("全部");
                        alcLists.add(0, alcBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                }));
    }

    private void getClassData() {
        disposable.add(ApiUtils.getInstance().getMainClass()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<MainClassBean>>() {
                    @Override
                    public void accept(ResultBean<MainClassBean> mainClassBean) throws Exception {
                        classLists.clear();
                        ThirdClassBean thirdClassBean = new ThirdClassBean();
                        thirdClassBean.setCat_id("");
                        thirdClassBean.setCat_name("全部");
                        classLists.add(thirdClassBean);
                        List<FirstClassBean> categorys = mainClassBean.getData().getCategorys();
                        for (int i = 0; i < categorys.size(); i++) {
                            List<SecondClassBean> lv2 = categorys.get(i).getLv2();
                            for (int j = 0; j < lv2.size(); j++) {
                                List<ThirdClassBean> lv3 = lv2.get(j).getLv3();
                                classLists.addAll(lv3);
                            }
                        }
                        classAdapter.setNewData(classLists);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void getGoodsList(int pageNum, final boolean isRefresh) {
        if (isRefresh) {
            refreshLayout.autoRefresh();
        }
        disposable.add(ApiUtils.getInstance().getGoodsList(selectClassId, pageNum, searchKey, "", brandId, areaId, ordoId, alcoholId, minPrice, maxPrice)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<GoodsListBean>>() {
                    @Override
                    public void accept(ResultBean<GoodsListBean> goodsListBeanResultBean) throws Exception {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        getListSuccess(goodsListBeanResultBean.getData(), isRefresh);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                }));
    }

    private void getListSuccess(GoodsListBean goodsListBean, boolean isRefresh) {
        if (goodsListBean != null && goodsListBean.getList() != null && goodsListBean.getList().size() > 0) {
            List<GoodsListDetailBean> dataLists = goodsListBean.getList();
            List<GoodsListDetailBean> newLists = new ArrayList<>();

            if (BaseApplication.HIDE_AUCTION) {
                //过滤竞拍商品
                try {
                    for (int i = 0; i < dataLists.size(); i++) {
                        if (TextUtils.isEmpty(dataLists.get(i).getAuction().getAuctionitem_id())) {
                            newLists.add(dataLists.get(i));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                newLists = dataLists;
            }

            if (newLists == null) newLists = new ArrayList<>();

            if (isRefresh) {
                pageNum = 1;
                lists = newLists;
            } else {
                pageNum++;
                lists.addAll(newLists);
            }
            if (goodsListBean.getPagers().getTotal() <= lists.size()) {
                refreshLayout.setFooterHeight(30);
                refreshLayout.finishLoadMoreWithNoMoreData();//显示没有更多数据
            } else {
                refreshLayout.setNoMoreData(false);
            }


            if (lists.size() == 0) {
                goodsAdapter.setNewData(lists);
                goodsAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.common_empty_view, (ViewGroup) recyclerView.getParent(), false));
            } else {
                goodsAdapter.setNewData(lists);
            }
        } else {
            lists.clear();
            goodsAdapter.setNewData(lists);
            goodsAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.common_empty_view, (ViewGroup) recyclerView.getParent(), false));
        }
    }

    private void changeListType() {
        goodsAdapter.switchListType();
        if(goodsAdapter.isGridLayout()){
            imgTitleRightTwoF.setImageResource(R.mipmap.icon_list);
            recyclerView.removeItemDecoration(decoration1);
            recyclerView.addItemDecoration(decoration2);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }else{
            imgTitleRightTwoF.setImageResource(R.mipmap.icon_grid);
            recyclerView.removeItemDecoration(decoration2);
            recyclerView.addItemDecoration(decoration1);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    private void showBrandPopWindow() {
        if (brandLists == null || brandLists.size() == 0) {
            ToastUtils.showShort("暂无品牌");
        } else {
            if (choiceBrandPopWindow == null) {
                choiceBrandPopWindow = new ChoiceBrandPopWindow(getActivity(), brandLists, brandId);
                choiceBrandPopWindow.setCallBack(new ChoiceBrandPopWindow.CallBack() {
                    @Override
                    public void choiceBrand(String brandName, String brandIdC) {
                        UmengEventUtil.search_brand(getActivity(), brandName);
                        brandId = brandIdC;
                        tvBrand.setText(TextUtils.isEmpty(brandId) ? "品牌" : brandName);
                        getGoodsList(1, true);
                    }
                });
            }
            choiceBrandPopWindow.setBrandList(brandLists, brandId);
            choiceBrandPopWindow.showAsDropDown(rlBrand, 0, 1);
        }
    }


    private void showAreaPopWindow() {
        if (areaLists == null || areaLists.size() == 0) {
            ToastUtils.showShort("暂无产地");
        } else {
            if (choiceAreaPopWindow == null) {
                choiceAreaPopWindow = new ChoiceAreaPopWindow(getActivity(), areaLists, areaId);
                choiceAreaPopWindow.setCallBack(new ChoiceAreaPopWindow.CallBack() {
                    @Override
                    public void choiceBrand(String brandName, String brandId) {
                        UmengEventUtil.search_place(getActivity(), brandName);
                        areaId = brandId;
                        tvArea.setText(TextUtils.isEmpty(areaId) ? "产地" : brandName);
                        getGoodsList(1, true);
                    }
                });
            }
            choiceAreaPopWindow.setBrandList(areaLists, areaId);
            choiceAreaPopWindow.showAsDropDown(rlBrand, 0, 1);
        }
    }

    private void showIncensePopWindow() {
        if (ordoLists == null || ordoLists.size() == 0) {
            ToastUtils.showShort("暂无类型");
        } else {
            if (choiceOrdoPopWindow == null) {
                choiceOrdoPopWindow = new ChoiceOrdoPopWindow(getActivity(), ordoLists, ordoId);
                choiceOrdoPopWindow.setCallBack(new ChoiceOrdoPopWindow.CallBack() {
                    @Override
                    public void choiceBrand(String brandName, String brandId) {
                        UmengEventUtil.search_type(getActivity(), brandName);
                        ordoId = brandId;
                        tvIncense.setText(TextUtils.isEmpty(ordoId) ? "类型" : brandName);
                        getGoodsList(1, true);
                    }
                });
            }
            choiceOrdoPopWindow.setBrandList(ordoLists, ordoId);
            choiceOrdoPopWindow.showAsDropDown(rlBrand, 0, 1);
        }
    }

    private void showDegreePopWindow() {
        if (alcLists == null || alcLists.size() == 0) {
            ToastUtils.showShort("暂无酒精度");
        } else {
            if (choiceAlcPopWindow == null) {
                choiceAlcPopWindow = new ChoiceAlcPopWindow(getActivity(), alcLists, alcoholId);
                choiceAlcPopWindow.setCallBack(new ChoiceAlcPopWindow.CallBack() {
                    @Override
                    public void choiceBrand(String brandName, String brandId) {
                        UmengEventUtil.search_alc(getActivity(), brandName);
                        alcoholId = brandId;
                        tvAlc.setText(TextUtils.isEmpty(alcoholId) ? "酒精度" : brandName);
                        getGoodsList(1, true);
                    }
                });
            }
            choiceAlcPopWindow.setBrandList(alcLists, alcoholId);
            choiceAlcPopWindow.showAsDropDown(rlBrand, 0, 1);
        }
    }

    private void showPricePopWindow() {
        if (priceLists == null || priceLists.size() == 0) {
            ToastUtils.showShort("暂无价格区间");
        } else {
            if (choicePricePopWindow == null) {
                choicePricePopWindow = new ChoicePricePopWindow(getActivity(), priceLists, priceId);
                choicePricePopWindow.setCallBack(new ChoicePricePopWindow.CallBack() {
                    @Override
                    public void choicePrice(String minPriceC, String maxPriceC, String id, String content) {
                        UmengEventUtil.search_price(getActivity(), content);
                        minPrice = minPriceC;
                        maxPrice = maxPriceC;
                        priceId = id;
                        tvPrice.setText(content);
                        tvPrice.setText(TextUtils.isEmpty(priceId) ? "价格区间" : content);
                        getGoodsList(1, true);
                    }
                });
            }
            choicePricePopWindow.showAsDropDown(rlBrand, 0, 1);
        }
    }

    private void checkIntentBrandId() {
        if (!TextUtils.isEmpty(intentBrandId) && brandLists != null && brandLists.size() > 0) {
            for (int i = 0; i < brandLists.size(); i++) {
                if (brandLists.get(i).getBrand_id().equals(intentBrandId)) {
                    brandId = brandLists.get(i).getBrand_id();
                    tvBrand.setText(TextUtils.isEmpty(brandId) ? "品牌" : brandLists.get(i).getBrand_name());
                    getGoodsList(1, true);
                }
            }
        }
    }

    //重置品牌、产地、香型、酒精度
    private void clearSelectFilterData() {
        brandId = "";
        tvBrand.setText("品牌");

        areaId = "";
        tvArea.setText("产地");

        ordoId = "";
        tvIncense.setText("香型");

        alcoholId = "";
        tvAlc.setText("酒精度");

        //重新请求相关数据
        getBrandLists();
        getAreaLists();
        getInsenceLists();
        getAlcLists();
    }

    public class ClassAdapter extends BaseQuickAdapter<ThirdClassBean, BaseViewHolder> {
        public ClassAdapter(int layoutResId, List<ThirdClassBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, ThirdClassBean item) {
            TextView tvClass = helper.getView(R.id.tvClass);
            tvClass.setText(item.getCat_name());
            tvClass.setSelected(item.getCat_id().equals(selectClassId));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
