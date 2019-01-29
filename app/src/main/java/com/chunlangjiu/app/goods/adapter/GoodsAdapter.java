package com.chunlangjiu.app.goods.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.amain.bean.HomeBean;
import com.chunlangjiu.app.goods.bean.GoodsListDetailBean;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.util.SizeUtils;

import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/7/30
 * @Describe:
 */
public class GoodsAdapter extends BaseQuickAdapter<GoodsListDetailBean, BaseViewHolder> {
    public static final int LIST_LINEAR = 1;
    public static final int LIST_GRID = 2;
    private int itemType = LIST_GRID;

    private Context context;
    private boolean isShowStoreView = true;

    public GoodsAdapter(Context context, List<GoodsListDetailBean> data) {
        super(data);
        this.context = context;
        setMultiTypeDelegate(new MultiTypeDelegate<GoodsListDetailBean>() {
            @Override
            protected int getItemType(GoodsListDetailBean cartGoodsBean) {
                switch (cartGoodsBean.getItemType()){
                    case GoodsListDetailBean.ITEM_TUIJIAN:
                        return GoodsListDetailBean.ITEM_TUIJIAN;
                    case GoodsListDetailBean.ITEM_JINGPAI:
                        return GoodsListDetailBean.ITEM_JINGPAI;
                    default:
                        return itemType;
                }
            }
        });
        getMultiTypeDelegate()
                .registerItemType(LIST_LINEAR, R.layout.amain_item_goods_list_linear)
                .registerItemType(LIST_GRID, R.layout.amain_item_goods_list_grid)
                .registerItemType(GoodsListDetailBean.ITEM_JINGPAI, R.layout.amain_item_home_list_auction)
                .registerItemType(GoodsListDetailBean.ITEM_TUIJIAN, R.layout.amain_item_home_list_pic);

    }

    //切换布局显示样式
    public void switchListType() {
        itemType = itemType == LIST_LINEAR ? LIST_GRID : LIST_LINEAR;
    }

    public int getItemType() {
        return itemType;
    }

    //设置布局显示样式
    public void setListType(int type) {
        itemType = type == LIST_LINEAR ? LIST_LINEAR : LIST_GRID;
    }

    public void setShowStoreView(boolean isShow) {
        isShowStoreView = isShow;
    }

    public boolean isGridLayout() {
        return itemType == LIST_GRID;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsListDetailBean item) {

        int itemType = helper.getItemViewType();
//        RelativeLayout rlItemLayout = viewHolder.getView(R.id.rl_item_layout);
//        int size =  SizeUtils.dp2px(10);
        switch (itemType) {
            case HomeBean.ITEM_GRID_GOODS:
                setItemContent(helper, item);
                break;
            case HomeBean.ITEM_GOODS:
                setItemContent(helper, item);
                break;
            case GoodsListDetailBean.ITEM_TUIJIAN:
                break;
            case GoodsListDetailBean.ITEM_JINGPAI:
                helper.addOnClickListener(R.id.tvMoreAuction);
                break;

        }

    }

    private void setItemContent(BaseViewHolder helper, GoodsListDetailBean item) {
        try {
            ImageView imgPic = helper.getView(R.id.imgPic);
            ImageView imgAuction = helper.getView(R.id.imgAuction);
            LinearLayout llStartPrice = helper.getView(R.id.llStartPrice);
            LinearLayout llHighPrice = helper.getView(R.id.llHighPrice);
            TextView tvStartPrice = helper.getView(R.id.tvStartPrice);
            TextView tvAnPaiStr = helper.getView(R.id.tvAnPaiStr);

            //网格布局的时候设置图片大小
            ViewGroup.LayoutParams layoutParams = imgPic.getLayoutParams();
            if (itemType == LIST_GRID && layoutParams.width == layoutParams.height && layoutParams.width > 0) {
                int picWidth = (SizeUtils.getScreenWidth() - SizeUtils.dp2px(25)) / 2;
                layoutParams.width = picWidth;
                layoutParams.height = picWidth;
                imgPic.setLayoutParams(layoutParams);
            }


            GlideUtils.loadImage(context, item.getImage_default_id(), imgPic);
            helper.setText(R.id.tv_name, item.getTitle());

            if (TextUtils.isEmpty(item.getAuction().getAuctionitem_id())) {
                //普通商品
                imgAuction.setVisibility(View.GONE);
                llStartPrice.setVisibility(View.GONE);
                llHighPrice.setVisibility(View.VISIBLE);
                tvAnPaiStr.setVisibility(View.GONE);
                helper.setText(R.id.tvSellPriceStr, "");
                helper.setText(R.id.tvSellPrice, "");
                helper.setText(R.id.tvGoodsPrice, "¥" + item.getPrice());
                helper.setVisible(R.id.tvGoodsPrice, true);
                helper.setText(R.id.tvStartPriceStr, "原价：");
                tvStartPrice.setText(item.getMkt_price());
                tvStartPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
            } else {
                //竞拍
                imgAuction.setVisibility(View.VISIBLE);
                llStartPrice.setVisibility(View.VISIBLE);
                helper.setText(R.id.tvStartPriceStr, "起拍价：");
                tvStartPrice.setText("¥" + item.getAuction().getStarting_price());
                helper.setText(R.id.tvGoodsPrice, "");
                if ("true".equals(item.getAuction().getAuction_status())) {
                    //明拍
                    llHighPrice.setVisibility(View.VISIBLE);
                    tvAnPaiStr.setVisibility(View.GONE);
                    helper.setText(R.id.tvSellPriceStr, "最高出价：");
                    if (TextUtils.isEmpty(item.getAuction().getMax_price())) {
                        helper.setText(R.id.tvSellPrice, "暂无出价");
                    } else {
                        helper.setText(R.id.tvSellPrice, "¥" + item.getAuction().getMax_price());
                    }
                } else {
                    llHighPrice.setVisibility(View.GONE);
                    tvAnPaiStr.setVisibility(View.VISIBLE);
                }
            }

            RelativeLayout llTime = helper.getView(R.id.llTime);
            if (llTime != null) {
                llTime.setVisibility(View.GONE);
            }

            String label = item.getLabel();
            helper.setText(R.id.tvLabel, label);
            helper.setGone(R.id.tvLabel, !TextUtils.isEmpty(label));
            helper.setText(R.id.tvAttention, item.getView_count() + "人关注");
            helper.setText(R.id.tvEvaluate, item.getRate_count() + "条评价");
            helper.setText(R.id.tv_good_evaluate, item.getRate_count() + "好评");

            helper.setText(R.id.tv_store_into, R.string.into_store);

            helper.setGone(R.id.rl_store_layout, isShowStoreView);
            if (isShowStoreView) {
                helper.addOnClickListener(R.id.rl_store_layout);
                helper.setText(R.id.tv_store_name, item.getShop_name());
                String level = item.getGrade();
                if ("2".equals(level)) {
                    helper.setBackgroundRes(R.id.tv_store_level, R.mipmap.store_partner);
                } else if ("1".equals(level)) {
                    helper.setBackgroundRes(R.id.tv_store_level, R.mipmap.store_star);
                } else {
                    helper.setBackgroundRes(R.id.tv_store_level, R.mipmap.store_common);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}