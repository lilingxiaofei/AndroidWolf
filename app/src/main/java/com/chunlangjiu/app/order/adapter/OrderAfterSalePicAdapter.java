package com.chunlangjiu.app.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.order.bean.OrderListBean;
import com.pkqup.commonlibrary.glide.GlideUtils;

import java.util.List;

public class OrderAfterSalePicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private View.OnClickListener onClickListener;
    private List<String> data;

    public OrderAfterSalePicAdapter(Context context, List<String> data) {
        super(R.layout.order_after_sale_pic, data);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        ImageView imgProduct = helper.getView(R.id.ivAfterSale);
        GlideUtils.loadImage(context, item, imgProduct);
    }
}
