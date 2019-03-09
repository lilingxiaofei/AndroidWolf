package com.chunlangjiu.app.money.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.money.bean.FundDetailListBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.annotations.Nullable;

public class FundDetailAdapter extends BaseQuickAdapter<FundDetailListBean.FundDetailBean, BaseViewHolder> {

    public FundDetailAdapter(int layoutResId, @Nullable List<FundDetailListBean.FundDetailBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FundDetailListBean.FundDetailBean item) {
        helper.setText(R.id.tvFundType, item.getMessage());
        helper.setText(R.id.tvCount, String.format("¥ %s", item.getFee()));
        helper.setText(R.id.tvTime, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(item.getLogtime() * 1000)));
        helper.setText(R.id.tvStatus, "" + item.getLogtime());

    }
}