package com.chunlangjiu.app.goodsmanage.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseFragment;
import com.chunlangjiu.app.amain.bean.ItemListBean;
import com.chunlangjiu.app.goodsmanage.activity.GoodsManageDetailsActivity;
import com.chunlangjiu.app.goodsmanage.activity.GoodsManageSetAuctionActivity;
import com.chunlangjiu.app.goodsmanage.adapter.GoodsManageAdapter;
import com.chunlangjiu.app.goodsmanage.bean.GoodsBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.chunlangjiu.app.user.activity.EditGoodsActivity;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.chunlangjiu.app.util.PageUtils;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.pkqup.commonlibrary.dialog.CommonConfirmDialog;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.net.bean.ResultBean;
import com.pkqup.commonlibrary.util.BigDecimalUtils;
import com.pkqup.commonlibrary.util.TimeUtils;
import com.pkqup.commonlibrary.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @CreatedbBy: liucun on 2018/9/11
 * @Describe:
 */
public class GoodsManageFragment extends BaseFragment {
    TextView tvTime;
    SmartRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    GoodsManageAdapter goodsManageAdapter;

    private CompositeDisposable disposable;
    private PageUtils<GoodsBean> pageUtils;
    private String status = "";
    private TimePickerDialog startTimeDialog;
    private long startTime;

    public static GoodsManageFragment newInstance(String status) {
        GoodsManageFragment auctionDetailFragment = new GoodsManageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("status", status);
        auctionDetailFragment.setArguments(bundle);
        return auctionDetailFragment;
    }


    @Override
    public void getContentView(LayoutInflater inflater, ViewGroup container) {
        inflater.inflate(R.layout.user_goodsmanage_fragment, container, true);
    }


    private EventManager.OnNotifyListener onNotifyListener = new EventManager.OnNotifyListener() {
        @Override
        public void onNotify(Object object, String eventTag) {
            eventTag = eventTag == null ? "" : eventTag;
            switch (eventTag) {
                case ConstantMsg.SHOP_DATA_CHANGE:
                    refreshLayout.autoRefresh();
                    break;
            }
        }
    };

    @Override
    public void initView() {
        pageUtils = new PageUtils<>();
        disposable = new CompositeDisposable();
        status = getArguments().getString("status");
        tvTime = rootView.findViewById(R.id.tvTime);
        refreshLayout = rootView.findViewById(R.id.refreshLayout);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        goodsManageAdapter = new GoodsManageAdapter(getActivity(), status, pageUtils.getList());
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, Utils.dp2px(activity, 5), false));
        recyclerView.setAdapter(goodsManageAdapter);
        goodsManageAdapter.setEmptyView(getLayoutInflater().inflate(R.layout.common_empty_view, (ViewGroup) recyclerView.getParent(), false));
        goodsManageAdapter.setOnItemChildClickListener(onItemChildClickListener);
        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartTime();
            }
        });
        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getGoodsList(pageUtils.nextPage());
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getGoodsList(pageUtils.firstPage());
            }
        });
    }




    BaseQuickAdapter.OnItemChildClickListener onItemChildClickListener = new BaseQuickAdapter.OnItemChildClickListener() {
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            final GoodsBean goods = pageUtils.get(position);
            switch (view.getId()) {
                case R.id.rl_item_layout:
                    if(CommonUtils.GOODS_STATUS_SELL.equals(status) || CommonUtils.GOODS_STATUS_AUCTION_ACTIVE.equals(status)){
                        GoodsManageDetailsActivity.startActivity(activity,goods.getItem_id(),"");
                    }else if(CommonUtils.GOODS_STATUS_AUCTION_STOP.equals(status)){
                        GoodsManageDetailsActivity.startActivity(activity,goods.getItem_id(),goods.getAuctionitem_id());
                    }
                    break;
                case R.id.tvFindCause:
                    CommonConfirmDialog commonConfirmDialog = new CommonConfirmDialog(activity, goods.getReason());
                    commonConfirmDialog.show();
                    break;
                case R.id.tvEditTwo:
                case R.id.tvEdit:
                    EditGoodsActivity.startEditGoodsDetailsActivity(activity, goods.getItem_id());
                    break;
                case R.id.tvPutaway:
                    if (BigDecimalUtils.objToBigDecimal(goods.getStore()).intValue() > 0) {
                        editGoodsShelves(goods.getItem_id(), "tosale");
                    } else {
                        ToastUtils.showShort("库存为0，请先设置库存再上架此商品");
                    }

                    break;
                case R.id.tvSetAuction:
                    if (BigDecimalUtils.objToBigDecimal(goods.getStore()).intValue() > 0) {
                        GoodsManageSetAuctionActivity.startSetAuctionActivity(activity, goods);
                    } else {
                        ToastUtils.showShort("库存为0，请先设置库存再设置为竞拍商品");
                    }
                    break;
                case R.id.tvUnShelve:
                    CommonConfirmDialog confirmDialog = new CommonConfirmDialog(activity, "您确定要下架此商品吗？");
                    confirmDialog.setCallBack(new CommonConfirmDialog.CallBack() {
                        @Override
                        public void onConfirm() {
                            editGoodsShelves(goods.getItem_id(), "tostock");
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    confirmDialog.show();
                    break;
                case R.id.tvDelete:
                    if (CommonUtils.GOODS_STATUS_AUCTION_NOT_START.equals(status) || CommonUtils.GOODS_STATUS_AUCTION_STOP.equals(status) || CommonUtils.GOODS_STATUS_INSTOCK.equals(status)) {
                        CommonConfirmDialog deleteDialog = new CommonConfirmDialog(activity, "您确定要下架此商品吗？");
                        deleteDialog.setCallBack(new CommonConfirmDialog.CallBack() {
                            @Override
                            public void onConfirm() {
                                deleteGoodsShelves(goods);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                        deleteDialog.show();
                    } else {
                        ToastUtils.showShort("目前只能删除竞拍未开始，竞拍结束，和仓库中的商品");
                    }
                    break;
            }
        }
    };


    @Override
    public void initData() {
        EventManager.getInstance().registerListener(onNotifyListener);
        getGoodsList(1);
    }

    public void editGoodsShelves(String itemId, String type) {
        disposable.add(ApiUtils.getInstance().editGoodsShelves(itemId, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean>() {
                    @Override
                    public void accept(ResultBean result) throws Exception {
                        resetGoodsList();
                        if (result.getErrorcode() == 0) {
                            refreshLayout.autoRefresh();
                            EventManager.getInstance().notify(null, ConstantMsg.SHOP_DATA_CHANGE);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showErrorMsg(throwable);
                    }
                }));
    }

    public void deleteGoodsShelves(GoodsBean goodsBean) {
        showLoadingDialog();
        boolean isAuction = CommonUtils.GOODS_STATUS_AUCTION_NOT_START.equals(status) || CommonUtils.GOODS_STATUS_AUCTION_ACTIVE.equals(status) || CommonUtils.GOODS_STATUS_AUCTION_STOP.equals(status) ? true : false;
        if (isAuction) {
            disposable.add(ApiUtils.getInstance().deleteAuctionGoods(goodsBean.getAuctionitem_id())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean>() {
                        @Override
                        public void accept(ResultBean result) throws Exception {
                            hideLoadingDialog();
                            if (result.getErrorcode() == 0) {
                                refreshLayout.autoRefresh();
                                EventManager.getInstance().notify(null, ConstantMsg.SHOP_DATA_CHANGE);
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
            disposable.add(ApiUtils.getInstance().deleteCommonGoods(goodsBean.getItem_id())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ResultBean>() {
                        @Override
                        public void accept(ResultBean result) throws Exception {
                            hideLoadingDialog();
                            if (result.getErrorcode() == 0) {
                                refreshLayout.autoRefresh();
                                EventManager.getInstance().notify(null, ConstantMsg.SHOP_DATA_CHANGE);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            hideLoadingDialog();
                            ToastUtils.showErrorMsg(throwable);
                        }
                    }));
        }


    }

    public void getGoodsList(int page) {
        disposable.add(ApiUtils.getInstance().getManageGoodsList(status, startTime + "", page, pageUtils.getPageSize())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<ItemListBean<GoodsBean>>>() {
                    @Override
                    public void accept(ResultBean<ItemListBean<GoodsBean>> result) throws Exception {
                        resetGoodsList();
                        if (result.getErrorcode() == 0) {
                            boolean isAuction = CommonUtils.isAuctionGoods(status);
                            if (isAuction) {
                                pageUtils.loadListSuccess(result.getData().getList());
                                pageUtils.setTotalPage(result.getData().getTotalPage());
                            } else {
                                pageUtils.loadListSuccess(result.getData().getItem_list());
                                pageUtils.setTotalPage(result.getData().getTotalPage());
                            }

                            updateLListUi();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        resetGoodsList();
                    }
                }));
    }

    private void resetGoodsList() {
        refreshLayout.finishLoadMore();
        refreshLayout.finishRefresh();
    }

    private void updateLListUi() {
        goodsManageAdapter.setNewData(pageUtils.getList());
        boolean isAuction = CommonUtils.isAuctionGoods(status);
        if (!isAuction) {
            refreshLayout.setEnableLoadMore(pageUtils.isNextPage());
        }
    }


    public void showStartTime() {
        if (startTimeDialog == null) {
            startTimeDialog = new TimePickerDialog.Builder().setCallBack(startDataSetListener)
                    .setCancelStringId("取消")
                    .setSureStringId("确定")
                    .setTitleStringId("")
                    .setYearText("年")
                    .setMonthText("月")
                    .setDayText("日")
                    .setHourText("时")
                    .setMinuteText("分")
                    .setCyclic(false)
                    .setCurrentMillseconds(System.currentTimeMillis())
                    .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                    .setType(Type.YEAR_MONTH_DAY)
                    .setWheelItemTextNormalColor(
                            getResources().getColor(R.color.timetimepicker_default_text_color))
                    .setWheelItemTextSelectorColor(
                            getResources().getColor(R.color.bg_white))
                    .setWheelItemTextSize(12).build();
        }
        startTimeDialog.show(getFragmentManager(), "all");
    }

    private OnDateSetListener startDataSetListener = new OnDateSetListener() {
        @Override
        public void onDateSet(TimePickerDialog timePickerView, long millSeconds) {
            startTime = millSeconds / 1000;
            tvTime.setText(TimeUtils.millisToDate(startTime + ""));
            getGoodsList(pageUtils.firstPage());
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
        EventManager.getInstance().unRegisterListener(onNotifyListener);
    }


}
