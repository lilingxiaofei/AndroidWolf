package com.chunlangjiu.app.goods.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.goods.activity.GoodsListActivity;
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

    private Context context ;
    private boolean isShowStoreView = true;
    public GoodsAdapter(Context context, List<GoodsListDetailBean> data) {
        super(data);
        this.context = context ;
        setMultiTypeDelegate(new MultiTypeDelegate<GoodsListDetailBean>() {
            @Override
            protected int getItemType(GoodsListDetailBean cartGoodsBean) {
                return itemType;
            }
        });
        getMultiTypeDelegate()
                .registerItemType(LIST_LINEAR, R.layout.amain_item_goods_list_linear)
                .registerItemType(LIST_GRID, R.layout.amain_item_goods_list_grid);
    }
    //切换布局显示样式
    public void switchListType(){
        itemType = itemType == LIST_LINEAR?LIST_GRID:LIST_LINEAR;
    }
    //设置布局显示样式
    public void setListType(int type){
        itemType = type == LIST_LINEAR?LIST_LINEAR:LIST_GRID;
    }

    public void setShowStoreView(boolean isShow){
        isShowStoreView = isShow;
    }

    public boolean isGridLayout(){
        return itemType == LIST_GRID ;
    }
    @Override
    protected void convert(BaseViewHolder helper, GoodsListDetailBean item) {
        try {
            ImageView imgPic = helper.getView(R.id.img_pic);
            ImageView imgAuction = helper.getView(R.id.imgAuction);
            LinearLayout llStartPrice = helper.getView(R.id.llStartPrice);
            LinearLayout llHighPrice = helper.getView(R.id.llHighPrice);
            TextView tvStartPrice = helper.getView(R.id.tvStartPrice);
            TextView tvAnPaiStr = helper.getView(R.id.tvAnPaiStr);


            //网格布局的时候设置图片大小
            ViewGroup.LayoutParams layoutParams = imgPic.getLayoutParams();
            if(itemType == LIST_GRID && layoutParams.width == layoutParams.height && layoutParams.width>0){
                int picWidth = (SizeUtils.getScreenWidth() - SizeUtils.dp2px(40)) / 2;
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
                helper.setText(R.id.tvSellPrice, item.getPrice());

                helper.setText(R.id.tvStartPriceStr, "原价：");
                tvStartPrice.setText(item.getMkt_price());
                tvStartPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
            } else {
                //竞拍
                imgAuction.setVisibility(View.VISIBLE);
                llStartPrice.setVisibility(View.VISIBLE);
                helper.setText(R.id.tvStartPriceStr, "起拍价：");
                tvStartPrice.setText("¥" + item.getAuction().getStarting_price());
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

            LinearLayout llTime = helper.getView(R.id.llTime);
            llTime.setVisibility(View.GONE);
            helper.setText(R.id.tvLabel, item.getLabel());
            TextView tvLabel = helper.getView(R.id.tvLabel);
            if (TextUtils.isEmpty(item.getLabel())) {
                tvLabel.setVisibility(View.GONE);
            } else {
                tvLabel.setVisibility(View.VISIBLE);
            }
            helper.setText(R.id.tv_attention, item.getView_count() + "人关注");
            helper.setText(R.id.tv_evaluate, item.getRate_count() + "条评价");


            helper.setGone(R.id.ll_store_name,isShowStoreView);
            helper.setGone(R.id.ll_store_grade,isShowStoreView);
            if(isShowStoreView){
                helper.addOnClickListener(R.id.ll_store_name);
                helper.setText(R.id.tv_store_name,item.getStoreName());
                String level = item.getStoreLevel();
                if("1".equals(level)){
                    helper.setGone(R.id.tv_star_level,true);
                    helper.setGone(R.id.tv_partner,false);
                }else if("2".equals(level)){
                    helper.setGone(R.id.tv_star_level,false);
                    helper.setGone(R.id.tv_partner,true);
                }else{
                    helper.setGone(R.id.tv_star_level,false);
                    helper.setGone(R.id.tv_partner,false);
                }
            }






        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}