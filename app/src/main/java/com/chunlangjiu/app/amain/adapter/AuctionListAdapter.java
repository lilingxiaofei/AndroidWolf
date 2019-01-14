package com.chunlangjiu.app.amain.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.amain.bean.AuctionListBean;
import com.chunlangjiu.app.util.ConstantMsg;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.view.countdownview.CountdownView;

import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/6/26
 * @Describe:
 */
public class AuctionListAdapter extends BaseQuickAdapter<AuctionListBean.AuctionBean, BaseViewHolder> {

    private Context context;

    public AuctionListAdapter(Context context, int layoutResId, List<AuctionListBean.AuctionBean> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, AuctionListBean.AuctionBean item) {
        ImageView imageView = helper.getView(R.id.imgPic);
        LinearLayout llHighPrice = helper.getView(R.id.llHighPrice);
        TextView tvAnPaiStr = helper.getView(R.id.tvAnPaiStr);
        TextView tvStartPrice = helper.getView(R.id.tvStartPrice);
        TextView tv_give_price_num = helper.getView(R.id.tv_give_price_num);
        tv_give_price_num.setVisibility(View.VISIBLE);
        tv_give_price_num.setText("人数："+ item.getAuction_number());
        GlideUtils.loadImage(context, item.getImage_default_id(), imageView);
        helper.setText(R.id.tv_name, item.getTitle());
        helper.setText(R.id.tvStartPrice, "¥" + item.getAuction_starting_price());
//        tvStartPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰

        if ("true".equals(item.getAuction_status())) {
            //明拍
            llHighPrice.setVisibility(View.VISIBLE);
            tvAnPaiStr.setVisibility(View.GONE);
        }else{
            llHighPrice.setVisibility(View.GONE);
            tvAnPaiStr.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(item.getMax_price())) {
            helper.setText(R.id.tvSellPrice, "暂无出价");
        } else {
            helper.setText(R.id.tvSellPrice, "¥" + item.getAuction_starting_price());
        }

        TextView tvLabel = helper.getView(R.id.tvLabel);
        if (TextUtils.isEmpty(item.getLabel())) {
            tvLabel.setVisibility(View.GONE);
        } else {
            tvLabel.setVisibility(View.VISIBLE);
            tvLabel.setText(item.getLabel());
        }

        CountdownView countdownView = helper.getView(R.id.countdownView);
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
                        EventManager.getInstance().notify(null, ConstantMsg.AUCTION_COUNT_END);
                    }
                });
                dealWithLifeCycle(helper, helper.getAdapterPosition(), item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 以下两个接口代替 activity.onStart() 和 activity.onStop(), 控制 timer 的开关
     */
    private void dealWithLifeCycle(final BaseViewHolder viewHolder, final int position, final AuctionListBean.AuctionBean item) {
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
                                EventManager.getInstance().notify(null, ConstantMsg.AUCTION_COUNT_END);
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

