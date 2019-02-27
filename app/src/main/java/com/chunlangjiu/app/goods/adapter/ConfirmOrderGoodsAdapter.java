package com.chunlangjiu.app.goods.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.goods.bean.OrderGoodsBean;
import com.pkqup.commonlibrary.glide.GlideUtils;

import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/7/10
 * @Describe:
 */
public class ConfirmOrderGoodsAdapter extends BaseQuickAdapter<OrderGoodsBean, BaseViewHolder> {


    private Context context;
    private List<OrderGoodsBean> list;

    public ConfirmOrderGoodsAdapter(Context context, List<OrderGoodsBean> list) {
        super(list);
        this.context = context;
        setMultiTypeDelegate(new MultiTypeDelegate<OrderGoodsBean>() {
            @Override
            protected int getItemType(OrderGoodsBean cartGoodsBean) {
                //根据你的实体类来判断布局类型
                return cartGoodsBean.getItemType();
            }
        });
        getMultiTypeDelegate()
                .registerItemType(OrderGoodsBean.ITEM_STORE, R.layout.goods_item_confirm_order_store)
                .registerItemType(OrderGoodsBean.ITEM_GOODS, R.layout.goods_item_confirm_order_goods);
    }


    @Override
    protected void convert(BaseViewHolder helper, OrderGoodsBean item) {
        switch (helper.getItemViewType()) {
            case OrderGoodsBean.ITEM_STORE:
                if (helper.getAdapterPosition() == 0) {
                    helper.setVisible(R.id.view_line, false);
                } else {
                    helper.setVisible(R.id.view_line, true);
                }

                ImageView img_store = helper.getView(R.id.img_store);
                GlideUtils.loadImageShop(context, item.getStoreLogo(), img_store);
                helper.setText(R.id.tv_store_name, item.getStoreName());
                break;
            case OrderGoodsBean.ITEM_GOODS:
                ImageView img_pic = helper.getView(R.id.img_pic);
                GlideUtils.loadImageShop(context, item.getGoodsPic(), img_pic);
                helper.setText(R.id.tv_name, item.getGoodsName());
                helper.setText(R.id.tv_price, "¥" + item.getGoodsPrice());
                helper.setText(R.id.tv_num, "x" + item.getGoodsNum());
                break;
        }
    }
}
