package com.chunlangjiu.app.amain.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.amain.bean.AuctionListBean;
import com.chunlangjiu.app.amain.bean.HomeBean;
import com.chunlangjiu.app.util.ConstantMsg;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.view.countdownview.CountdownView;

import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/7/21.
 * @Describe:
 */
public class HomeAdapter extends BaseQuickAdapter<HomeBean, BaseViewHolder> {

    private Context context;

    public HomeAdapter(Context context, List<HomeBean> list) {
        super(list);
        this.context = context;
        setMultiTypeDelegate(new MultiTypeDelegate<HomeBean>() {
            @Override
            protected int getItemType(HomeBean homeBean) {
                //根据你的实体类来判断布局类型
                return homeBean.getItemType();
            }
        });
        getMultiTypeDelegate()
                .registerItemType(HomeBean.ITEM_GOODS, R.layout.amain_item_home_list_goods)
                .registerItemType(HomeBean.ITEM_JINGPAI, R.layout.amain_item_home_list_auction)
                .registerItemType(HomeBean.ITEM_TUIJIAN, R.layout.amain_item_home_list_pic);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, HomeBean item) {
        switch (viewHolder.getItemViewType()) {
            case HomeBean.ITEM_GOODS:
                CountdownView countdownView = viewHolder.getView(R.id.countdownView);
                ImageView imgPic = viewHolder.getView(R.id.imgPic);
                TextView tvStartPrice = viewHolder.getView(R.id.tvStartPrice);
                TextView tv_give_price_num = viewHolder.getView(R.id.tv_give_price_num);
                LinearLayout llStartPrice = viewHolder.getView(R.id.llStartPrice);
                LinearLayout llTime = viewHolder.getView(R.id.llTime);
                TextView tvStartPriceStr = viewHolder.getView(R.id.tvStartPriceStr);
                LinearLayout llHighPrice = viewHolder.getView(R.id.llHighPrice);
                TextView tvAnPaiStr = viewHolder.getView(R.id.tvAnPaiStr);
                TextView tvSellPriceStr = viewHolder.getView(R.id.tvSellPriceStr);
                TextView tvSellPrice = viewHolder.getView(R.id.tvSellPrice);

                TextView tv_attention = viewHolder.getView(R.id.tv_attention);
                TextView tv_evaluate = viewHolder.getView(R.id.tv_evaluate);

                viewHolder.addOnClickListener(R.id.ll_store_name);
                viewHolder.setText(R.id.tv_store_name,item.getStoreName());
                String level = item.getStoreLevel();
                if("1".equals(level)){
                    viewHolder.setGone(R.id.tv_star_level,true);
                    viewHolder.setGone(R.id.tv_partner,false);
                }else if("2".equals(level)){
                    viewHolder.setGone(R.id.tv_star_level,false);
                    viewHolder.setGone(R.id.tv_partner,true);
                }else{
                    viewHolder.setGone(R.id.tv_star_level,false);
                    viewHolder.setGone(R.id.tv_partner,false);
                }

                GlideUtils.loadImage(context, item.getImage_default_id(), imgPic);
                viewHolder.setText(R.id.tv_name, item.getTitle());
//                tvStartPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
                TextView tvLabel = viewHolder.getView(R.id.tvLabel);
                if (TextUtils.isEmpty(item.getLabel())) {
                    tvLabel.setVisibility(View.GONE);
                } else {
                    tvLabel.setVisibility(View.VISIBLE);
                    tvLabel.setText(item.getLabel());
                }
                if (item.isAuction()) {
                    llStartPrice.setVisibility(View.VISIBLE);
                    tv_give_price_num.setVisibility(View.VISIBLE);
                    tv_give_price_num.setText("人数：" + item.getAuction_number());
                    tvStartPriceStr.setText("起拍价：");
                    tvStartPrice.setText("¥" + item.getAuction_starting_price());
                    llTime.setVisibility(View.VISIBLE);
                    if ("true".equals(item.getAuction_status())) {
                        //明拍
                        llHighPrice.setVisibility(View.VISIBLE);
                        tvAnPaiStr.setVisibility(View.GONE);
                        tvSellPriceStr.setVisibility(View.VISIBLE);
                        tvSellPriceStr.setText("最高出价：");
                        if (TextUtils.isEmpty(item.getMax_price())) {
                            viewHolder.setText(R.id.tvSellPrice, "暂无出价");
                        } else {
                            viewHolder.setText(R.id.tvSellPrice, "¥" + item.getMax_price());
                        }
                    } else {
                        //暗拍
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
                        dealWithLifeCycle(viewHolder, viewHolder.getAdapterPosition(), item);
                    }
                    tv_attention.setVisibility(View.GONE);
                    tv_evaluate.setVisibility(View.GONE);
                } else {
                    llStartPrice.setVisibility(View.GONE);
                    tv_give_price_num.setVisibility(View.GONE);
                    llTime.setVisibility(View.GONE);
                    tvSellPriceStr.setVisibility(View.GONE);
                    llHighPrice.setVisibility(View.VISIBLE);
                    tvAnPaiStr.setVisibility(View.GONE);
                    viewHolder.setText(R.id.tvStartPriceStr, "原价：");
                    tvStartPrice.setText("¥" + item.getMkt_price());
                    viewHolder.setText(R.id.tvSellPriceStr, "");
                    viewHolder.setText(R.id.tvSellPrice, "¥" + item.getPrice());

                    tv_attention.setVisibility(View.VISIBLE);
                    tv_evaluate.setVisibility(View.VISIBLE);
                    tv_attention.setText((TextUtils.isEmpty(item.getView_count()) ? "0" : item.getView_count()) + "人关注");
                    tv_evaluate.setText((TextUtils.isEmpty(item.getRate_count()) ? "0" : item.getRate_count()) + "条评价");
                }
                break;
            case HomeBean.ITEM_TUIJIAN:

                break;
        }
    }


    /**
     * 以下两个接口代替 activity.onStart() 和 activity.onStop(), 控制 timer 的开关
     */
    private void dealWithLifeCycle(final BaseViewHolder viewHolder, final int position, final HomeBean item) {
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

    public static class MyViewHolder extends BaseViewHolder {
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

}
