package com.chunlangjiu.app.amain.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseApplication;
import com.chunlangjiu.app.abase.BaseFragment;
import com.chunlangjiu.app.amain.activity.LoginMainActivity;
import com.chunlangjiu.app.amain.adapter.CartGoodsListAdapter;
import com.chunlangjiu.app.amain.bean.CartGoodsBean;
import com.chunlangjiu.app.amain.bean.CartListBean;
import com.chunlangjiu.app.cart.UpdateCartGoodsBean;
import com.chunlangjiu.app.goods.activity.ConfirmOrderActivity;
import com.chunlangjiu.app.goods.bean.ConfirmOrderBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.MyStatusBarUtils;
import com.google.gson.Gson;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.BigDecimalUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.socks.library.KLog;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/6/24.
 * @Describe:
 */
public class CartFragment extends BaseFragment {


    private RelativeLayout rlContent;
    private RelativeLayout rlLogin;
    private TextView tvLogin;

    private RefreshLayout refreshLayout;
    private SwipeMenuRecyclerView recycleView;
    private RelativeLayout rlEmptyView;

    private ImageView img_back;
    private ImageView imgEdit;
    private TextView tvEditFinish;
    private RelativeLayout rl_bottom;
    private LinearLayout ll_check_all;
    private ImageView imgCheckAll;
    private LinearLayout llTotalPrice;
    private TextView tvTotalPrice;
    private TextView tv_commit;
    private TextView tv_delete;

    private CompositeDisposable disposable;
    private boolean isActivity = false;//是否是Activity
    private boolean isEdit = false;//是否是编辑状态，默认为false
    private boolean isCheckAll = false;//是否全选，默认为false
    private String totalPrice = "0.00";//选中商品的价格
    private LinkedHashMap<CartGoodsBean, List<CartGoodsBean>> goodsMaps;
    private List<CartGoodsBean> lists;
    private CartGoodsListAdapter cartAdapter;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_back:
                    checkBack();
                    break;
                case R.id.imgEdit:
                    changeEditStatus();
                    break;
                case R.id.tvEditFinish:
                    changeEditStatus();
                    break;
                case R.id.ll_check_all:
                    changeCheckAllStatus();
                    break;
                case R.id.tv_commit:
                    confirmOrder();
                    break;
                case R.id.tv_delete:
                    deleteMulGoods();
                    break;
                case R.id.tvLogin:
                    LoginMainActivity.startLoginActivity(activity);
                    break;
            }
        }
    };


    private void checkBack() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    public void setIsActivity(boolean isActivity) {
        this.isActivity = isActivity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        EventManager.getInstance().unRegisterListener(onNotifyListener);
    }

    @Override
    public void getContentView(LayoutInflater inflater, ViewGroup container) {
        inflater.inflate(R.layout.amain_fragment_cart, container, true);
    }

    @Override
    public void initView() {
        MyStatusBarUtils.setTitleBarPadding(getActivity(),rootView.findViewById(R.id.rlCartTitle));
        EventManager.getInstance().registerListener(onNotifyListener);
        disposable = new CompositeDisposable();

        rlContent = rootView.findViewById(R.id.rlContent);
        rlLogin = rootView.findViewById(R.id.rlLogin);
        tvLogin = rootView.findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(onClickListener);

        img_back = rootView.findViewById(R.id.img_back);
        tvEditFinish = rootView.findViewById(R.id.tvEditFinish);
        imgEdit = rootView.findViewById(R.id.imgEdit);
        rl_bottom = rootView.findViewById(R.id.rl_bottom);
        rl_bottom.setVisibility(View.GONE);
        ll_check_all = rootView.findViewById(R.id.ll_check_all);
        llTotalPrice = rootView.findViewById(R.id.llTotalPrice);
        imgCheckAll = rootView.findViewById(R.id.img_check_all);
        tvTotalPrice = rootView.findViewById(R.id.tvTotalPrice);
        tv_commit = rootView.findViewById(R.id.tv_commit);
        tv_delete = rootView.findViewById(R.id.tv_delete);

        img_back.setOnClickListener(onClickListener);
        imgEdit.setOnClickListener(onClickListener);
        tvEditFinish.setOnClickListener(onClickListener);
        ll_check_all.setOnClickListener(onClickListener);
        tv_commit.setOnClickListener(onClickListener);
        tv_delete.setOnClickListener(onClickListener);
        img_back.setVisibility(isActivity ? View.VISIBLE : View.GONE);

        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        recycleView = rootView.findViewById(R.id.recycle_view);
        rlEmptyView = rootView.findViewById(R.id.rlEmptyView);
        refreshLayout.setEnableRefresh(true);//设置可以下拉刷新
        refreshLayout.setEnableLoadMore(false);//设置不能加载更多
        goodsMaps = new LinkedHashMap<>();
        lists = new ArrayList<>();
        cartAdapter = new CartGoodsListAdapter(getActivity(), lists);
        cartAdapter.setIsEdit(isEdit);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleView.setSwipeMenuCreator(swipeMenuCreator);//设置侧滑删除菜单
        recycleView.setSwipeMenuItemClickListener(mMenuItemClickListener);
        recycleView.setAdapter(cartAdapter);
        cartAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.check_store://选中店铺
                        choiceStore(position);
                        break;
                    case R.id.check_goods://选中商品
                        choiceGoods(position);
                        break;
                    case R.id.imgSub://数量减
                        subGoodsNum(position);
                        break;
                    case R.id.imgAdd://数量加
                        addGoodsNum(position);
                        break;
                    case R.id.deleteOne://删除单个商品
                        deleteOneGoods(position);
                }
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshLayout) {
                getCartList();
            }
        });
    }


    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_70);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            if (viewType == CartGoodsBean.ITEM_GOODS) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity())
                        .setBackground(R.color.bg_red)
                        .setText("删除")
                        .setTextColorResource(R.color.t_white)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。
            }
        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();
            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                deleteOneGoods(adapterPosition);
            }
        }
    };

    @Override
    public void initData() {
        if (BaseApplication.isLogin()) {
            getCartList();
            rlContent.setVisibility(View.VISIBLE);
            rl_bottom.setVisibility(View.VISIBLE);
            rlLogin.setVisibility(View.GONE);
        } else {
            imgEdit.setVisibility(View.GONE);
            rlContent.setVisibility(View.GONE);
            rl_bottom.setVisibility(View.GONE);
            rlLogin.setVisibility(View.VISIBLE);
        }
    }

    private void getCartList() {
        disposable.add(ApiUtils.getInstance().getCartList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<CartListBean>>() {
                    @Override
                    public void accept(ResultBean<CartListBean> cartListBeanResultBean) throws Exception {
                        refreshLayout.finishRefresh();
                        getListSuccess(cartListBeanResultBean.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        refreshLayout.finishRefresh();
                    }
                }));
    }

    private void getListSuccess(CartListBean data) {
        if (data != null && data.getCartlist() != null && data.getCartlist().size() > 0) {
            //有数据
            List<CartListBean.ShopInfo> cartlist = data.getCartlist();
            goodsMaps.clear();
            for (int i = 0; i < cartlist.size(); i++) {
                CartListBean.ShopInfo shopInfo = cartlist.get(i);
                CartGoodsBean shopBean = new CartGoodsBean();
                shopBean.setItemType(CartGoodsBean.ITEM_STORE);
                shopBean.setStoreId(shopInfo.getShop_id());
                shopBean.setStoreLogo(shopInfo.getShop_logo());
                shopBean.setStoreName(shopInfo.getShop_name());
                shopBean.setStoreCheck(shopInfo.getNocheckedall().equals("0"));

                List<CartListBean.PromotionList> promotion_cartitems = shopInfo.getPromotion_cartitems();
                List<CartGoodsBean> goodsLists = new ArrayList<>();
                for (int x = 0; x < promotion_cartitems.size(); x++) {
                    List<CartListBean.GoodsItem> cartitemlist = shopInfo.getPromotion_cartitems().get(x).getCartitemlist();
                    for (int j = 0; j < cartitemlist.size(); j++) {
                        CartListBean.GoodsItem goodsItem = cartitemlist.get(j);
                        CartGoodsBean goodsBean = new CartGoodsBean();
                        goodsBean.setItemType(CartGoodsBean.ITEM_GOODS);
                        goodsBean.setCart_id(goodsItem.getCart_id());
                        goodsBean.setSku_id(goodsItem.getSku_id());
                        goodsBean.setItem_id(goodsItem.getItem_id());
                        goodsBean.setGoodsName(goodsItem.getTitle());
                        goodsBean.setGoodsPic(goodsItem.getImage_default_id());
                        goodsBean.setGoodsPrice(goodsItem.getPrice().getPrice());
                        goodsBean.setGoodsNum(goodsItem.getQuantity());
                        goodsBean.setStore(goodsItem.getStore());
                        goodsBean.setGoodsCheck(!goodsItem.getIs_checked().equals("0"));
                        goodsLists.add(goodsBean);
                    }
                }
                goodsMaps.put(shopBean, goodsLists);
            }
            if (isEdit) {
                imgEdit.setVisibility(View.GONE);
                tvEditFinish.setVisibility(View.VISIBLE);
            } else {
                imgEdit.setVisibility(View.VISIBLE);
                tvEditFinish.setVisibility(View.GONE);
            }
            updateRecycleViewList();
            updateCheckAll();
            updateTotalPrice();
        } else {
            //无数据
            imgEdit.setVisibility(View.GONE);
            tvEditFinish.setVisibility(View.GONE);
            rl_bottom.setVisibility(View.GONE);
            recycleView.setVisibility(View.GONE);
            rlEmptyView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 更新购物车列表
     */
    private void updateRecycleViewList() {
        lists.clear();
        Iterator<Map.Entry<CartGoodsBean, List<CartGoodsBean>>> iterator = goodsMaps.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<CartGoodsBean, List<CartGoodsBean>> next = iterator.next();
            if (next.getValue() != null && next.getValue().size() > 0) {
                List<CartGoodsBean> value = next.getValue();
                boolean isCheckStore = true;
                for (CartGoodsBean cartGoodsBean : value) {
                    if (!cartGoodsBean.isGoodsCheck()) {
                        isCheckStore = false;
                    }
                }
                next.getKey().setStoreCheck(isCheckStore);
                lists.add(next.getKey());
                lists.addAll(next.getValue());
            }
        }
        if (lists.size() > 0) {
            rl_bottom.setVisibility(View.VISIBLE);
            recycleView.setVisibility(View.VISIBLE);
            rlEmptyView.setVisibility(View.GONE);
            cartAdapter.setNewData(lists);
        } else {
            rl_bottom.setVisibility(View.GONE);
            recycleView.setVisibility(View.GONE);
            rlEmptyView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 更新判断是否全选
     */
    private void updateCheckAll() {
        boolean checkAll = true;
        for (CartGoodsBean cartGoodsBean : lists) {
            if (cartGoodsBean.getItemType() == CartGoodsBean.ITEM_GOODS) {
                if (!cartGoodsBean.isGoodsCheck()) {
                    checkAll = false;
                }
            }
        }
        imgCheckAll.setSelected(checkAll);
        isCheckAll = checkAll;
    }

    /**
     * 更新选中的总价格
     */
    private void updateTotalPrice() {
        totalPrice = "0.00";
        for (CartGoodsBean cartGoodsBean : lists) {
            if (cartGoodsBean.isGoodsCheck()) {
                String goodsNum = cartGoodsBean.getGoodsNum();
                String goodsPrice = cartGoodsBean.getGoodsPrice();
                totalPrice = BigDecimalUtils.add(totalPrice, BigDecimalUtils.multiply(goodsNum, goodsPrice, 2), 2);

            }
        }
        tvTotalPrice.setText(totalPrice);
    }

    /**
     * 切换全选状态
     */
    private void changeCheckAllStatus() {
        Iterator<Map.Entry<CartGoodsBean, List<CartGoodsBean>>> iterator = goodsMaps.entrySet().iterator();
        List<CartGoodsBean> changeLists = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<CartGoodsBean, List<CartGoodsBean>> next = iterator.next();
            next.getKey().setStoreCheck(!isCheckAll);
            for (CartGoodsBean cartGoodsBean : next.getValue()) {
                cartGoodsBean.setGoodsCheck(!isCheckAll);
                changeLists.add(cartGoodsBean);
            }
        }
        isCheckAll = !isCheckAll;
        imgCheckAll.setSelected(isCheckAll);
        updateRecycleViewList();
        updateTotalPrice();
        updateNetData(changeLists);
    }


    private void changeEditStatus() {
        if (isEdit) {
            //编辑状态切换到正常状态
            isEdit = false;
            imgEdit.setVisibility(View.VISIBLE);
            tvEditFinish.setVisibility(View.GONE);
            llTotalPrice.setVisibility(View.VISIBLE);
            tv_commit.setVisibility(View.VISIBLE);
            tv_delete.setVisibility(View.GONE);
        } else {
            //正常状态切换到编辑状态
            isEdit = true;
            imgEdit.setVisibility(View.GONE);
            tvEditFinish.setVisibility(View.VISIBLE);
            llTotalPrice.setVisibility(View.GONE);
            tv_commit.setVisibility(View.GONE);
            tv_delete.setVisibility(View.VISIBLE);
        }
        cartAdapter.setIsEdit(isEdit);
    }

    private void choiceStore(int position) {
        CartGoodsBean cartGoodsBean = lists.get(position);
        boolean storeCheck = cartGoodsBean.isStoreCheck();
        Iterator<Map.Entry<CartGoodsBean, List<CartGoodsBean>>> iterator = goodsMaps.entrySet().iterator();
        List<CartGoodsBean> changeLists = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<CartGoodsBean, List<CartGoodsBean>> next = iterator.next();
            if (next.getKey().equals(cartGoodsBean)) {
                CartGoodsBean key = next.getKey();
                key.setStoreCheck(!storeCheck);
                List<CartGoodsBean> value = next.getValue();
                for (CartGoodsBean goodsBean : value) {
                    goodsBean.setGoodsCheck(!storeCheck);
                    changeLists.add(goodsBean);
                }
            }
        }
        updateRecycleViewList();
        updateCheckAll();
        updateTotalPrice();
        updateNetData(changeLists);
    }

    private void choiceGoods(int position) {
        CartGoodsBean cartGoodsBean = lists.get(position);
        List<CartGoodsBean> changeLists = new ArrayList<>();
        Iterator<Map.Entry<CartGoodsBean, List<CartGoodsBean>>> iterator = goodsMaps.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<CartGoodsBean, List<CartGoodsBean>> next = iterator.next();
            List<CartGoodsBean> value = next.getValue();
            for (CartGoodsBean goodsBean : value) {
                if (cartGoodsBean.equals(goodsBean)) {
                    goodsBean.setGoodsCheck(!cartGoodsBean.isGoodsCheck());
                    changeLists.add(goodsBean);
                }
            }
        }
        updateRecycleViewList();
        updateCheckAll();
        updateTotalPrice();
        updateNetData(changeLists);
    }

    private void subGoodsNum(int position) {
        final CartGoodsBean cartGoodsBean = lists.get(position);
        if (Integer.parseInt(cartGoodsBean.getGoodsNum()) > 1) {
            showLoadingDialog();
            UpdateCartGoodsBean updateCartGoodsBean = new UpdateCartGoodsBean();
            updateCartGoodsBean.setCart_id(cartGoodsBean.getCart_id());
            updateCartGoodsBean.setIs_checked(cartGoodsBean.isStoreCheck() ? "1" : "0");
            updateCartGoodsBean.setSelected_promotion("0");
            updateCartGoodsBean.setTotalQuantity(Integer.parseInt(cartGoodsBean.getGoodsNum()) - 1 + "");
            List<UpdateCartGoodsBean> list = new ArrayList<>();
            list.add(updateCartGoodsBean);
            String jsonStr = new Gson().toJson(list);
            disposable.add(ApiUtils.getInstance().updateCartData(jsonStr)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean>() {
                        @Override
                        public void accept(ResultBean resultBean) throws Exception {
                            hideLoadingDialog();
                            cartGoodsBean.setGoodsNum(Integer.parseInt(cartGoodsBean.getGoodsNum()) - 1 + "");
                            updateRecycleViewList();
                            updateCheckAll();
                            updateTotalPrice();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            hideLoadingDialog();
                        }
                    }));
        }
    }

    private void addGoodsNum(int position) {
        try {
            final CartGoodsBean cartGoodsBean = lists.get(position);
            if (Integer.parseInt(cartGoodsBean.getGoodsNum()) >= Integer.parseInt(cartGoodsBean.getStore())) {
                ToastUtils.showShort("库存不足");
            } else {
                showLoadingDialog();
                UpdateCartGoodsBean updateCartGoodsBean = new UpdateCartGoodsBean();
                updateCartGoodsBean.setCart_id(cartGoodsBean.getCart_id());
                updateCartGoodsBean.setIs_checked(cartGoodsBean.isStoreCheck() ? "1" : "0");
                updateCartGoodsBean.setSelected_promotion("0");
                updateCartGoodsBean.setTotalQuantity(Integer.parseInt(cartGoodsBean.getGoodsNum()) + 1 + "");
                List<UpdateCartGoodsBean> list = new ArrayList<>();
                list.add(updateCartGoodsBean);
                String jsonStr = new Gson().toJson(list);
                disposable.add(ApiUtils.getInstance().updateCartData(jsonStr)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<ResultBean>() {
                            @Override
                            public void accept(ResultBean resultBean) throws Exception {
                                hideLoadingDialog();
                                cartGoodsBean.setGoodsNum(Integer.parseInt(cartGoodsBean.getGoodsNum()) + 1 + "");
                                updateRecycleViewList();
                                updateCheckAll();
                                updateTotalPrice();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                hideLoadingDialog();
                            }
                        }));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateNetData(List<CartGoodsBean> changeLists) {
        List<UpdateCartGoodsBean> updateLists = new ArrayList<>();
        for (int i = 0; i < changeLists.size(); i++) {
            UpdateCartGoodsBean updateCartGoodsBean = new UpdateCartGoodsBean();
            updateCartGoodsBean.setCart_id(changeLists.get(i).getCart_id());
            updateCartGoodsBean.setIs_checked(changeLists.get(i).isGoodsCheck() ? "1" : "0");
            updateCartGoodsBean.setSelected_promotion("0");
            updateCartGoodsBean.setTotalQuantity(changeLists.get(i).getGoodsNum());
            updateLists.add(updateCartGoodsBean);
        }
        String jsonStr = new Gson().toJson(updateLists);
        disposable.add(ApiUtils.getInstance().updateCartData(jsonStr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        KLog.e("updateSuccess");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }


    private void deleteOneGoods(final int position) {
        CartGoodsBean cartGoodsBean = lists.get(position);
        showLoadingDialog();
        disposable.add(ApiUtils.getInstance().deleteCartItem(cartGoodsBean.getCart_id())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean resultBean) throws Exception {
                        hideLoadingDialog();
                        deleteOneSuccess(position);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        hideLoadingDialog();
                    }
                }));
    }

    private void deleteOneSuccess(int position) {
        CartGoodsBean cartGoodsBean = lists.get(position);
        Iterator<Map.Entry<CartGoodsBean, List<CartGoodsBean>>> iterator = goodsMaps.entrySet().iterator();
        CartGoodsBean deleteBean = null;
        while (iterator.hasNext()) {
            Map.Entry<CartGoodsBean, List<CartGoodsBean>> next = iterator.next();
            List<CartGoodsBean> value = next.getValue();
            for (CartGoodsBean goodsBean : value) {
                if (cartGoodsBean.equals(goodsBean)) {
                    deleteBean = next.getKey();
                }
            }
        }
        List<CartGoodsBean> deleteLists = goodsMaps.get(deleteBean);
        deleteLists.remove(cartGoodsBean);
        updateRecycleViewList();
        updateCheckAll();
        updateTotalPrice();
    }

    private void deleteMulGoods() {
        List<String> deleteIds = new ArrayList<>();
        final List<CartGoodsBean> deleteGoods = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).getItemType() == CartGoodsBean.ITEM_GOODS) {
                if (lists.get(i).isGoodsCheck()) {
                    deleteIds.add(lists.get(i).getCart_id());
                    deleteGoods.add(lists.get(i));
                }
            }
        }
        if (deleteIds.size() == 0) {
            ToastUtils.showShort("请选择商品");
        } else {
            showLoadingDialog();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < deleteIds.size(); i++) {
                if (i != deleteIds.size() - 1) {
                    stringBuffer.append(deleteIds.get(i)).append(",");
                } else {
                    stringBuffer.append(deleteIds.get(i));
                }
            }
            disposable.add(ApiUtils.getInstance().deleteCartItem(stringBuffer.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean>() {
                        @Override
                        public void accept(ResultBean resultBean) throws Exception {
                            hideLoadingDialog();
                            deleteMulSuccess(deleteGoods);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            hideLoadingDialog();
                        }
                    }));
        }
    }

    private void deleteMulSuccess(List<CartGoodsBean> deleteGoods) {
        LinkedHashMap<CartGoodsBean, List<CartGoodsBean>> deleteMaps = new LinkedHashMap<>();
        Iterator<Map.Entry<CartGoodsBean, List<CartGoodsBean>>> iterator = goodsMaps.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<CartGoodsBean, List<CartGoodsBean>> next = iterator.next();
            List<CartGoodsBean> value = next.getValue();
            List<CartGoodsBean> deleteLists = new ArrayList<>();
            for (int i = 0; i < deleteGoods.size(); i++) {
                for (int j = 0; j < value.size(); j++) {
                    if (deleteGoods.get(i).equals(value.get(j))) {
                        deleteLists.add(value.get(j));
                    }
                }
            }
            if (deleteLists.size() > 0) {
                deleteMaps.put(next.getKey(), deleteLists);
            }
        }

        Iterator<Map.Entry<CartGoodsBean, List<CartGoodsBean>>> iteratorDelete = deleteMaps.entrySet().iterator();
        while (iteratorDelete.hasNext()) {
            Map.Entry<CartGoodsBean, List<CartGoodsBean>> next = iteratorDelete.next();
            List<CartGoodsBean> cartGoodsBeans = goodsMaps.get(next.getKey());
            List<CartGoodsBean> value = next.getValue();
            for (int i = 0; i < value.size(); i++) {
                cartGoodsBeans.remove(value.get(i));
            }
        }
        updateRecycleViewList();
        updateCheckAll();
        updateTotalPrice();
    }

    private void confirmOrder() {
        boolean hasCheckGoods = false;
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).getItemType() == CartGoodsBean.ITEM_GOODS) {
                if (lists.get(i).isGoodsCheck()) {
                    hasCheckGoods = true;
                }
            }
        }
        if (hasCheckGoods) {
            showLoadingDialog();
            disposable.add(ApiUtils.getInstance().cartConfirmOrder()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean<ConfirmOrderBean>>() {
                        @Override
                        public void accept(ResultBean<ConfirmOrderBean> resultBean) throws Exception {
                            hideLoadingDialog();
                            if (resultBean.getData() != null && !TextUtils.isEmpty(resultBean.getData().getMd5_cart_info())) {
                                ConfirmOrderActivity.startConfirmOrderActivity(getActivity(), resultBean.getData(), "cart");
                            } else {
                                ToastUtils.showShort("请选择商品");
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            hideLoadingDialog();
                            ToastUtils.showErrorMsg(throwable);
                        }
                    }));
        } else {
            ToastUtils.showShort("请选择商品");
        }
    }

    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            updateCartList(eventTag);
            loginSuccess(eventTag);
            logoutSuccess(eventTag);
        }
    };


    private void updateCartList(String eventTag) {
        if (eventTag.equals(ConstantMsg.UPDATE_CART_LIST)) {
            getCartList();
        }
    }

    //登录成功
    private void loginSuccess(String eventTag) {
        if (eventTag.equals(ConstantMsg.LOGIN_SUCCESS)) {
            requestData();
        }
    }

    private void requestData() {
        rlContent.setVisibility(View.VISIBLE);
        rl_bottom.setVisibility(View.VISIBLE);
        rlLogin.setVisibility(View.GONE);
        getCartList();
    }


    //退出登录
    private void logoutSuccess(String eventTag) {
        if (eventTag.equals(ConstantMsg.LOGOUT_SUCCESS)) {
            imgEdit.setVisibility(View.GONE);
            rlContent.setVisibility(View.GONE);
            rl_bottom.setVisibility(View.GONE);
            rlLogin.setVisibility(View.VISIBLE);
        }
    }

}
