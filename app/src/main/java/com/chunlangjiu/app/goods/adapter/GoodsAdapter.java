package com.chunlangjiu.app.goods.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import com.chunlangjiu.app.goodsmanage.bean.GoodsBean;
import com.chunlangjiu.app.util.ConstantMsg;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.util.SizeUtils;
import com.pkqup.commonlibrary.view.countdownview.CountdownView;

import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/7/30
 * @Describe:
 */
public class GoodsAdapter extends BaseQuickAdapter<GoodsListDetailBean, BaseViewHolder> {
    public static final int LIST_LINEAR = 0;
    public static final int LIST_GRID = 1;
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
                .registerItemType(GoodsListDetailBean.ITEM_JINGPAI, R.layout.goods_item_list_auction)
                .registerItemType(GoodsListDetailBean.ITEM_TUIJIAN, R.layout.goods_item_list_tuijian);

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


            RelativeLayout llTime = helper.getView(R.id.llTime);
            CountdownView countdownView = helper.getView(R.id.countdownView);
            if (llTime != null) {
                llTime.setVisibility(View.GONE);
            }
            if (item.getAuction()!=null && !TextUtils.isEmpty(item.getAuction().getAuctionitem_id())) {
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
            } else if (item.isAuction()) {
                llTime.setVisibility(View.VISIBLE);
                //竞拍
                imgAuction.setVisibility(View.VISIBLE);
                llStartPrice.setVisibility(View.VISIBLE);
                helper.setText(R.id.tvStartPriceStr, "起拍价：");

                String actionNumStr = context.getString(R.string.action_number,item.getAuction_number());
                helper.setText(R.id.tvActionNum,actionNumStr);
                tvStartPrice.setText("¥" + item.getAuction_starting_price());
                helper.setText(R.id.tvGoodsPrice, "");
                if ("true".equals(item.getAuction_status())) {
                    //明拍
                    llHighPrice.setVisibility(View.VISIBLE);
                    tvAnPaiStr.setVisibility(View.GONE);
                    helper.setText(R.id.tvSellPriceStr, "最高出价：");
                    if (TextUtils.isEmpty(item.getMax_price())) {
                        helper.setText(R.id.tvSellPrice, "暂无出价");
                    } else {
                        helper.setText(R.id.tvSellPrice, "¥" + item.getMax_price());
                    }
                } else {
                    llHighPrice.setVisibility(View.GONE);
                    tvAnPaiStr.setVisibility(View.VISIBLE);
                }

                String end_time = item.getAuction_end_time();
                long endTime = 0;
                if (!TextUtils.isEmpty(end_time)) {
                    endTime = Long.parseLong(end_time);
                }
                if ((endTime * 1000 - System.currentTimeMillis()) > 0) {
                    countdownView.start(endTime * 1000 - System.currentTimeMillis());
                    countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                        @Override
                        public void onEnd(CountdownView cv) {
                            EventManager.getInstance().notify(null, ConstantMsg.HOME_COUNT_END);
                        }
                    });
                    dealWithLifeCycle(helper, helper.getAdapterPosition(), item);
                }
            }else {
                //普通商品

                imgAuction.setVisibility(View.GONE);
                llHighPrice.setVisibility(View.VISIBLE);
                tvAnPaiStr.setVisibility(View.GONE);
                helper.setText(R.id.tvSellPriceStr, "");
                helper.setText(R.id.tvSellPrice, "");
                helper.setText(R.id.tvGoodsPrice, "¥" + item.getPrice());
                helper.setVisible(R.id.tvGoodsPrice, true);
                helper.setText(R.id.tvStartPriceStr, "原价：");
                tvStartPrice.setText(item.getMkt_price());
                tvStartPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
                int position =helper.getLayoutPosition();
                //判断列表是两列的情况下，旁边的数据如果是竞拍 就设置竞拍启动价隐藏并占位置 以保证布局排版对不齐问题
                if (itemType == LIST_GRID ) {
                    int index = position%2==0?position+1:position-1;
                    GoodsListDetailBean tempItem = getItem(index);

                    if(tempItem ==null ){
                        llStartPrice.setVisibility(View.GONE);
                    }else if( tempItem.isAuction() || (tempItem.getAuction()!=null && !TextUtils.isEmpty(tempItem.getAuction().getAuctionitem_id()))){
                        llStartPrice.setVisibility(View.INVISIBLE);
                    }else{
                        llStartPrice.setVisibility(View.GONE);
                    }
                }else{
                    llStartPrice.setVisibility(View.GONE);
                }
            }



            LinearLayout labelLayout = helper.getView(R.id.llLabel);
            setLabelList(labelLayout,item.getLabel());



            String viewCount = item.getView_count() ;
            String  rateCount =  item.getRate_count() ;
            String  rate =  item.getRate() ;
            helper.setText(R.id.tvAttention, (TextUtils.isEmpty(viewCount) ? "0" :viewCount ) + "人关注");
            helper.setText(R.id.tvEvaluate, (TextUtils.isEmpty(rateCount) ? "0" :rateCount) + "条评价");
            helper.setText(R.id.tv_good_evaluate, TextUtils.isEmpty(rate) ? "0%" :rate+"%"+ "好评");


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


    /**
     * 以下两个接口代替 activity.onStart() 和 activity.onStop(), 控制 timer 的开关
     */
    private void dealWithLifeCycle(final BaseViewHolder viewHolder, final int position, final GoodsListDetailBean item) {
        final CountdownView countdownView = viewHolder.getView(R.id.countdownView);
        countdownView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                String end_time = item.getAuction_end_time();
                try {
                    long endTime = 0;
                    if (!TextUtils.isEmpty(end_time)) {
                        endTime = Long.parseLong(end_time);
                    }
                    if ((endTime * 1000 - System.currentTimeMillis()) > 0) {
                        countdownView.start(endTime * 1000 - System.currentTimeMillis());
                        countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                            @Override
                            public void onEnd(CountdownView cv) {
                                EventManager.getInstance().notify(null, ConstantMsg.HOME_COUNT_END);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                countdownView.stop();
            }
        });
    }


    private void setLabelList(LinearLayout layout,String label){
        //设置标签显示
        try {
            layout.removeAllViews();
            String[] labelList = label.split(",");
            if(labelList.length== 1){
                labelList = label.split(" ");
            }
            if(labelList.length== 1){
                labelList = label.split("，");
            }
            for (int i = 0; i < labelList.length; i++) {
                TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.amain_item_goods_list_label,null);
                if(labelList[i].trim().length()>0){
                    textView.setText(labelList[i]);
                    layout.addView(textView);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}