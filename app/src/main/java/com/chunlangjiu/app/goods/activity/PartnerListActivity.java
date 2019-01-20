package com.chunlangjiu.app.goods.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.abase.BaseActivity;
import com.chunlangjiu.app.goods.bean.PartnerBean;
import com.chunlangjiu.app.net.ApiUtils;
import com.lzy.imagepicker.util.Utils;
import com.lzy.imagepicker.view.GridSpacingItemDecoration;
import com.pkqup.commonlibrary.net.bean.ResultBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class PartnerListActivity extends BaseActivity {

    private RecyclerView rvOartnerList;

    private List<PartnerBean> list;
    private PartnerListAdapter fansAdapter;

    private CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partner_activity_list);
        initView();
        initData();
    }

    private void initView() {
        disposable = new CompositeDisposable();
        rvOartnerList = findViewById(R.id.rv_partner_list);
        rvOartnerList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvOartnerList.addItemDecoration(new GridSpacingItemDecoration(1, Utils.dp2px(this, 5), false));
        list = new ArrayList<>();
        fansAdapter = new PartnerListAdapter(R.layout.partner_list_item, list);
        fansAdapter.setOnItemClickListener(onItemChildClickListener);
        rvOartnerList.setAdapter(fansAdapter);
    }

    private void initData() {
        loadPartnerList();
    }


    BaseQuickAdapter.OnItemClickListener onItemChildClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            String shopId = list.get(position).getShop_id();
            ShopMainActivity.startShopMainActivity(PartnerListActivity.this, shopId);
        }
    };

    private void loadPartnerList() {
        disposable.add(ApiUtils.getInstance().getPartnerList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResultBean<List<PartnerBean>>>() {
                    @Override
                    public void accept(ResultBean<List<PartnerBean>> result) throws Exception {
                        if (result != null && result.getData() != null) {
                            list.clear();
                            list.addAll(result.getData());
                            updateListView();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));
    }

    private void updateListView() {
        if (list == null || list.size() == 0) {
            showEmptyView();
        } else {
            showContentView();
            fansAdapter.setNewData(list);
        }
    }


    class PartnerListAdapter extends BaseQuickAdapter<PartnerBean, BaseViewHolder> {
        public PartnerListAdapter(int layoutResId, List<PartnerBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, PartnerBean item) {
            ImageView iv = helper.getView(R.id.ivStoreLogo);
            Glide.with(PartnerListActivity.this).load(item.getShop_logo()).into(iv);

            helper.setText(R.id.tv_store_name, item.getShopname());
            helper.setText(R.id.tvAddress, item.getShop_addr());

            String num = item.getGrade();
            if (TextUtils.isEmpty(num)) {
                helper.setText(R.id.tvSellGoods, "暂无可售商品");
            } else {
                String sellGoods = getString(R.string.sell_goods, num);
                helper.setText(R.id.tvSellGoods, sellGoods);
            }
            LinearLayout ll_store_label = helper.getView(R.id.ll_store_label);
            for (int i = 0; i < 3; i++) {
                View view = LayoutInflater.from(PartnerListActivity.this).inflate(R.layout.partner_list_label_item, null);
                TextView storeLabel = view.findViewById(R.id.tvLabel1);
                storeLabel.setText("标签" + i);
                ll_store_label.addView(view);
            }
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int resId = view.getId();
            if (resId == R.id.img_title_left) {
                finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    @Override
    public void setTitleView() {
        titleImgLeft.setOnClickListener(onClickListener);
        titleName.setText(R.string.fans_list);
    }
}
