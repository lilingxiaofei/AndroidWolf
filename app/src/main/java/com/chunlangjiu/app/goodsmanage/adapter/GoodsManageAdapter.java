package com.chunlangjiu.app.goodsmanage.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.amain.bean.HomeBean;
import com.chunlangjiu.app.goodsmanage.bean.GoodsBean;
import com.chunlangjiu.app.util.CommonUtils;
import com.chunlangjiu.app.util.ConstantMsg;
import com.daimajia.swipe.SwipeLayout;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.util.BigDecimalUtils;
import com.pkqup.commonlibrary.view.countdownview.CountdownView;

import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/7/30
 * @Describe:
 */
public class GoodsManageAdapter extends BaseQuickAdapter<GoodsBean, BaseViewHolder> {

    private Context context;
    private String goodsStatus;

    public GoodsManageAdapter(Context context, String goodsStatus, List<GoodsBean> data) {
        super(R.layout.user_item_goods_manage, data);
        this.goodsStatus = goodsStatus;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsBean item) {
        setItemContent(helper, item);
    }

    private void setItemContent(BaseViewHolder helper, GoodsBean item) {
        try {
            ImageView imgPic = helper.getView(R.id.imgPic);
            ImageView imgAuction = helper.getView(R.id.imgAuction);
            LinearLayout llStartPrice = helper.getView(R.id.llStartPrice);
            LinearLayout llHighPrice = helper.getView(R.id.llHighPrice);
            TextView tvStartPrice = helper.getView(R.id.tvStartPrice);
            TextView tvAnPaiStr = helper.getView(R.id.tvAnPaiStr);
            helper.addOnClickListener(R.id.tvFindCause);
            helper.addOnClickListener(R.id.tvEdit);
            helper.addOnClickListener(R.id.tvPutaway);
            helper.addOnClickListener(R.id.tvSetAuction);
            helper.addOnClickListener(R.id.tvUnShelve);
            helper.addOnClickListener(R.id.tvEditTwo);
            helper.addOnClickListener(R.id.tvDelete);
            helper.addOnClickListener(R.id.rl_item_layout);

            GlideUtils.loadImage(context, item.getImage_default_id(), imgPic);
            helper.setText(R.id.tv_name, item.getTitle());


            helper.setGone(R.id.llAuditStatus, false);
            helper.setGone(R.id.tvFindCause, false);
            helper.setGone(R.id.llDepot, false);
            helper.setGone(R.id.llSell, false);
            helper.setGone(R.id.llSellPrice,false);

            String status = item.getApprove_status();
            status = status == null ? "" : status;
            SwipeLayout swipeLayout = helper.getView(R.id.swipeLayout);
            if (CommonUtils.GOODS_STATUS_AUCTION_NOT_START.equals(goodsStatus) || CommonUtils.GOODS_STATUS_AUCTION_STOP.equals(goodsStatus) || CommonUtils.GOODS_STATUS_INSTOCK.equals(goodsStatus)) {
                swipeLayout.setSwipeEnabled(true);
            } else {
                swipeLayout.setSwipeEnabled(false);
            }

            if (CommonUtils.GOODS_STATUS_AUCTION_NOT_START.equals(goodsStatus) || CommonUtils.GOODS_STATUS_AUCTION_STOP.equals(goodsStatus) || CommonUtils.GOODS_STATUS_AUCTION_ACTIVE.equals(goodsStatus)) {
                //竞拍
                llHighPrice.setVisibility(View.VISIBLE);
                imgAuction.setVisibility(View.VISIBLE);
                llStartPrice.setVisibility(View.VISIBLE);
                helper.setText(R.id.tvStartPriceStr, "起拍价：");
                tvStartPrice.setText("¥" + item.getAuction_starting_price());
                //明拍
                llHighPrice.setVisibility(View.VISIBLE);
                tvAnPaiStr.setVisibility(View.GONE);
                helper.setText(R.id.tvSellPriceStr, "最高出价：");
                if (BigDecimalUtils.objToBigDecimal(item.getMax_price()).doubleValue() > 0) {
                    helper.setText(R.id.tvSellPrice, "¥" + item.getMax_price());
                } else {
                    helper.setText(R.id.tvSellPrice, "暂无出价");
                }
            } else {
                //普通商品
                imgAuction.setVisibility(View.GONE);
                llStartPrice.setVisibility(View.GONE);
                llHighPrice.setVisibility(View.GONE);
                tvAnPaiStr.setVisibility(View.GONE);
                helper.setGone(R.id.llSellPrice,true);
                helper.setText(R.id.tvSellPriceStr, "");
                helper.setText(R.id.tvSellPrice, "");
                helper.setText(R.id.tvGoodsPrice, "¥" + item.getPrice());
                helper.setVisible(R.id.tvGoodsPrice, true);
                helper.setText(R.id.tvStartPriceStr, "原价：");
                helper.setText(R.id.tvStock, item.getStore());
                tvStartPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
                switch (status) {
                    case CommonUtils.GOODS_STATUS_AUDIT_PENDING:
                        helper.setGone(R.id.llAuditStatus, true);
                        helper.setText(R.id.tvAuditStatus, "审核中");
                        break;
                    case CommonUtils.GOODS_STATUS_AUDIT_REFUSE:
                        helper.setGone(R.id.llAuditStatus, true);
                        helper.setGone(R.id.tvFindCause, true);
                        helper.setText(R.id.tvAuditStatus, "审核驳回");
                        break;
                    case CommonUtils.GOODS_STATUS_INSTOCK:
                        helper.setGone(R.id.llSell, true);
                        helper.setGone(R.id.tvUnShelve, false);
                        helper.setGone(R.id.llDepot, true);
                        break;
                    case CommonUtils.GOODS_STATUS_SELL:
                        helper.setGone(R.id.tvUnShelve, true);
                        helper.setGone(R.id.llSell, true);
                        break;

                }
            }

            RelativeLayout llTime = helper.getView(R.id.llTime);
            CountdownView countdownView = helper.getView(R.id.countdownView);
            if (CommonUtils.GOODS_STATUS_AUCTION_ACTIVE.equals(goodsStatus)) {
                llTime.setVisibility(View.VISIBLE);
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
            }else{
                llTime.setVisibility(View.GONE);
            }

            LinearLayout labelLayout = helper.getView(R.id.llLabel);
            setLabelList(labelLayout, item.getLabel());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 以下两个接口代替 activity.onStart() 和 activity.onStop(), 控制 timer 的开关
     */
    private void dealWithLifeCycle(final BaseViewHolder viewHolder, final int position, final GoodsBean item) {
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
                                EventManager.getInstance().notify(null, ConstantMsg.SHOP_DATA_CHANGE);
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

    private void setLabelList(LinearLayout layout, String label) {
        //设置标签显示
        try {
            layout.removeAllViews();
            String[] labelList = label.split(",");
            if (labelList.length == 1) {
                labelList = label.split(" ");
            }
            if (labelList.length == 1) {
                labelList = label.split("，");
            }
            for (int i = 0; i < labelList.length; i++) {
                TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.amain_item_goods_list_label, null);
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