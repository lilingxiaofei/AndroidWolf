package com.chunlangjiu.app.amain.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseApplication;
import com.chunlangjiu.app.abase.BaseFragment;
import com.chunlangjiu.app.amain.activity.LoginActivity;
import com.chunlangjiu.app.amain.adapter.AuctionListAdapter;
import com.chunlangjiu.app.amain.bean.AuctionListBean;
import com.chunlangjiu.app.amain.bean.FirstClassBean;
import com.chunlangjiu.app.amain.bean.MainClassBean;
import com.chunlangjiu.app.amain.bean.SecondClassBean;
import com.chunlangjiu.app.amain.bean.ThirdClassBean;
import com.chunlangjiu.app.goods.activity.GoodsDetailslNewActivity;
import com.chunlangjiu.app.goods.activity.SearchActivity;
import com.chunlangjiu.app.goods.activity.ShopMainActivity;
import com.chunlangjiu.app.goods.bean.AlcListBean;
import com.chunlangjiu.app.goods.bean.AreaListBean;
import com.chunlangjiu.app.goods.bean.BrandsListBean;
import com.chunlangjiu.app.goods.bean.OrdoListBean;
import com.chunlangjiu.app.goods.bean.PriceBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.dialog.ChoiceAlcPopWindow;
import com.chunlangjiu.app.user.dialog.ChoiceAreaPopWindow;
import com.chunlangjiu.app.user.dialog.ChoiceBrandPopWindow;
import com.chunlangjiu.app.user.dialog.ChoiceOrdoPopWindow;
import com.chunlangjiu.app.user.dialog.ChoicePricePopWindow;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.MyStatusBarUtils;
import com.chunlangjiu.app.util.PageUtils;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/6/16.
 * @Describe: 竞拍
 */
public class AuctionFragment extends BaseFragment {

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
    private PageUtils<AuctionListBean.AuctionBean> pageUtils;
    private AuctionListAdapter linearAdapter;

    private CompositeDisposable disposable;

    private String brandId = "";
    private String areaId = "";
    private String ordoId = "";
    private String alcoholId = "";
    private String minPrice = "";
    private String maxPrice = "";

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


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_title_right_one_f:
                    startActivity(new Intent(getActivity(), SearchActivity.class));
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

    @Override
    public void getContentView(LayoutInflater inflater, ViewGroup container) {
        inflater.inflate(R.layout.amain_fragment_auction, container, true);
    }


    @Override
    public void initView() {
        MyStatusBarUtils.setTitleBarPadding(getActivity(), rootView.findViewById(R.id.title_view));
        disposable = new CompositeDisposable();
        titleView.setVisibility(View.VISIBLE);
        tvTitleF.setText("竞拍专区");
        imgTitleRightOneF.setVisibility(View.VISIBLE);
        imgTitleRightOneF.setOnClickListener(onClickListener);

        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(true);//设置可以下拉刷新
//        refreshLayout.setEnableLoadMore(false);//设置不能加载更多
        recyclerView = rootView.findViewById(R.id.recycle_view);
        pageUtils = new PageUtils<>();
        linearAdapter = new AuctionListAdapter(getActivity(), R.layout.amain_item_goods_list_linear, pageUtils.getList());
        linearAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (BaseApplication.isLogin()) {
//                    AuctionDetailActivity.startAuctionDetailsActivity(getActivity(), lists.get(position).getItem_id());
                    GoodsDetailslNewActivity.startActivity(getActivity(), pageUtils.get(position).getItem_id());
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });
        linearAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.common_empty_view, (ViewGroup) recyclerView.getParent(), false));
        linearAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_store_into) {
                    ShopMainActivity.startShopMainActivity(getActivity(), pageUtils.get(position).getShop_id());
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, Utils.dp2px(getActivity(), 5), false));
        recyclerView.setAdapter(linearAdapter);
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getListData(pageUtils.nextPage());
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getListData(pageUtils.firstPage());
            }
        });

        rlBrand = rootView.findViewById(R.id.rlBrand);
        tvBrand = rootView.findViewById(R.id.tvBrand);
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
                selectClassId = classLists.get(position).getCat_id();
                classAdapter.notifyDataSetChanged();
                clearSelectFilterData();
                getListData(pageUtils.firstPage());
            }
        });
    }


    @Override
    public void initData() {
        EventManager.getInstance().registerListener(onNotifyListener);
        initPriceLists();
        getClassData();
        getBrandLists();
        getAreaLists();
        getInsenceLists();
        getAlcLists();
        getListData(pageUtils.firstPage());
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
                        if(null != alcListBeanResultBean.getData()){
                            alcLists = alcListBeanResultBean.getData().getList();
                            AlcListBean.AlcBean alcBean = new AlcListBean().new AlcBean();
                            alcBean.setAlcohol_id("");
                            alcBean.setAlcohol_name("全部");
                            alcLists.add(0, alcBean);
                        }
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

    private void getListData(int page) {
        disposable.add(ApiUtils.getInstance().getAuctionList(brandId, areaId, ordoId, minPrice, maxPrice, page, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<AuctionListBean>>() {
                    @Override
                    public void accept(ResultBean<AuctionListBean> listResultBean) throws Exception {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                        List<AuctionListBean.AuctionBean> list = listResultBean.getData().getList();
                        getListSuccess(list);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        refreshLayout.finishRefresh();
                        refreshLayout.finishLoadMore();
                    }
                }));
    }

    private void getListSuccess(List<AuctionListBean.AuctionBean> list) {
        if (list != null) {
            Iterator< AuctionListBean.AuctionBean> it = list.iterator();
            while(it.hasNext()){
                AuctionListBean.AuctionBean bean = it.next();
                String end_time = bean.getAuction_end_time();
                long endTime = 0;
                if (!TextUtils.isEmpty(end_time)) {
                    endTime = Long.parseLong(end_time);
                }
                if ((endTime * 1000 - System.currentTimeMillis()) <= 0) {
                    it.remove();
                }
            }
            pageUtils.loadListSuccess(list);
            linearAdapter.setNewData(pageUtils.getList());
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
                        brandId = brandIdC;
                        tvBrand.setText(TextUtils.isEmpty(brandId) ? "品牌" : brandName);
                        getListData(pageUtils.firstPage());
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
                        areaId = brandId;
                        tvArea.setText(TextUtils.isEmpty(areaId) ? "产地" : brandName);
                        getListData(pageUtils.firstPage());
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
                        ordoId = brandId;
                        tvIncense.setText(TextUtils.isEmpty(ordoId) ? "类型" : brandName);
                        getListData(pageUtils.firstPage());
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
                        alcoholId = brandId;
                        tvAlc.setText(TextUtils.isEmpty(alcoholId) ? "酒精度" : brandName);
                        getListData(pageUtils.firstPage());
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
                        minPrice = minPriceC;
                        maxPrice = maxPriceC;
                        priceId = id;
                        tvPrice.setText(content);
                        tvPrice.setText(TextUtils.isEmpty(priceId) ? "价格区间" : content);
                        getListData(pageUtils.firstPage());
                    }
                });
            }
            choicePricePopWindow.showAsDropDown(rlBrand, 0, 1);
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

    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            auctionCountEnd(eventTag);
        }
    };

    private void auctionCountEnd(String eventTag) {
        if (eventTag.equals(ConstantMsg.AUCTION_COUNT_END)) {
            getListData(pageUtils.firstPage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventManager.getInstance().unRegisterListener(onNotifyListener);
        disposable.dispose();
    }
}
