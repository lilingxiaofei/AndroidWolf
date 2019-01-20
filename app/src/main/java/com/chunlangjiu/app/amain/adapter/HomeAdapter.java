package com.chunlangjiu.app.amain.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import com.chunlangjiu.app.util.ConstantMsg;
import com.pkqup.commonlibrary.eventmsg.EventManager;
import com.pkqup.commonlibrary.glide.GlideUtils;
import com.pkqup.commonlibrary.util.SizeUtils;
import com.pkqup.commonlibrary.view.countdownview.CountdownView;

import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/7/21.
 * @Describe:
 */
public class HomeAdapter extends BaseQuickAdapter<HomeBean, BaseViewHolder> {

    private Context context;
    private int gridHead = -100;

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
                .registerItemType(HomeBean.ITEM_GOODS, R.layout.amain_item_goods_list_linear)
                .registerItemType(HomeBean.ITEM_GRID_GOODS, R.layout.amain_item_goods_list_grid)
                .registerItemType(HomeBean.ITEM_JINGPAI, R.layout.amain_item_home_list_auction)
                .registerItemType(HomeBean.ITEM_TUIJIAN, R.layout.amain_item_home_list_pic);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, HomeBean item) {
        int itemType = viewHolder.getItemViewType();
        RelativeLayout rlItemLayout = viewHolder.getView(R.id.rl_item_layout);
        int size =  SizeUtils.dp2px(10);
        switch (itemType) {
            case HomeBean.ITEM_GRID_GOODS:
                int left = 0;
                int right = 0;
                int top = 0;
                int bottom = 0;
                int index = viewHolder.getLayoutPosition()-gridHead;
                if (index % 2 == 0) {
                    right = size;
                }else {
                    left = size;
                }
                RecyclerView.LayoutParams rlItemParams = (RecyclerView.LayoutParams) rlItemLayout.getLayoutParams();
                rlItemParams.setMargins(left,top,right,bottom);
                rlItemLayout.setLayoutParams(rlItemParams);
                //网格布局的时候设置图片大小
                ImageView imgPic = viewHolder.getView(R.id.imgPic);
                ViewGroup.LayoutParams layoutParams = imgPic.getLayoutParams();
                if (itemType == HomeBean.ITEM_GRID_GOODS && layoutParams.width == layoutParams.height && layoutParams.width > 0) {
                    int picWidth = (SizeUtils.getScreenWidth() - SizeUtils.dp2px(25)) / 2;
                    layoutParams.width = picWidth;
                    layoutParams.height = picWidth;
                    imgPic.setLayoutParams(layoutParams);
                }
                setItemContent(viewHolder, item);
                break;
            case HomeBean.ITEM_GOODS:
                RecyclerView.LayoutParams rlItemParam = (RecyclerView.LayoutParams) rlItemLayout.getLayoutParams();
                rlItemParam.setMargins(size,0,size,0);
                rlItemLayout.setLayoutParams(rlItemParam);
                setItemContent(viewHolder, item);
                break;
            case HomeBean.ITEM_TUIJIAN:
                gridHead = viewHolder.getLayoutPosition();
                break;
        }
    }

    private void setItemContent(BaseViewHolder viewHolder, HomeBean item) {
        CountdownView countdownView = viewHolder.getView(R.id.countdownView);
        ImageView imgPic = viewHolder.getView(R.id.imgPic);
        TextView tvStartPrice = viewHolder.getView(R.id.tvStartPrice);
        LinearLayout llStartPrice = viewHolder.getView(R.id.llStartPrice);
        RelativeLayout llTime = viewHolder.getView(R.id.llTime);
        TextView tvStartPriceStr = viewHolder.getView(R.id.tvStartPriceStr);
        LinearLayout llHighPrice = viewHolder.getView(R.id.llHighPrice);
        TextView tvAnPaiStr = viewHolder.getView(R.id.tvAnPaiStr);
        TextView tvSellPriceStr = viewHolder.getView(R.id.tvSellPriceStr);
        TextView tvSellPrice = viewHolder.getView(R.id.tvSellPrice);


        viewHolder.addOnClickListener(R.id.rl_store_layout);
        viewHolder.setText(R.id.tv_store_name, item.getShop_name());
        viewHolder.setText(R.id.tv_store_into,"进店");
        viewHolder.addOnClickListener(R.id.tv_store_into);
        String level = item.getGrade();
        if ("1".equals(level)) {
            viewHolder.setBackgroundRes(R.id.tv_store_level, R.mipmap.store_partner);
        } else if ("2".equals(level)) {
            viewHolder.setBackgroundRes(R.id.tv_store_level, R.mipmap.store_star);
        } else {
            viewHolder.setBackgroundRes(R.id.tv_store_level, R.mipmap.store_common);
        }

        GlideUtils.loadImage(context, item.getImage_default_id(), imgPic);
        viewHolder.setText(R.id.tv_name, item.getTitle());
        viewHolder.setText(R.id.tv_store_name,item.getShop_name());
//                tvStartPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        //设置标签显示
        String label = item.getLabel();
        Log.d(this.getClass().getName(),"label="+label);
        viewHolder.setGone(R.id.tvLabel,!TextUtils.isEmpty(label));
        TextView tvLabel = viewHolder.getView(R.id.tvLabel);
        tvLabel.setText(label);
//        viewHolder.setText(R.id.tvLabel,);

        if (item.isAuction()) {
            llStartPrice.setVisibility(View.VISIBLE);
            tvStartPriceStr.setText("起拍价：");
            tvStartPrice.setText("¥" + item.getAuction_starting_price());
            viewHolder.setText(R.id.tvGoodsPrice,"");
            llTime.setVisibility(View.VISIBLE);
            String actionNum = item.getAuction_number();
            String actionNumStr = context.getString(R.string.action_number,actionNum);
            viewHolder.setText(R.id.tvActionNum,actionNumStr);
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
        } else {
            llStartPrice.setVisibility(View.GONE);
            tvSellPriceStr.setVisibility(View.GONE);
            llHighPrice.setVisibility(View.VISIBLE);
            tvAnPaiStr.setVisibility(View.GONE);
            viewHolder.setText(R.id.tvStartPriceStr, "原价：");
            tvStartPrice.setText("¥" + item.getMkt_price());
            viewHolder.setText(R.id.tvSellPriceStr, "");
            viewHolder.setText(R.id.tvSellPrice, "");
            viewHolder.setText(R.id.tvGoodsPrice,"¥" +item.getPrice());
        }

        TextView tv_attention = viewHolder.getView(R.id.tvAttention);
        TextView tv_evaluate = viewHolder.getView(R.id.tvEvaluate);
        TextView tv_good_evaluate  = viewHolder.getView(R.id.tv_good_evaluate);
        String viewCount = item.getView_count() ;
        String  rateCount =  item.getRate_count() ;
        String  rate =  item.getRate() ;
        tv_attention.setText((TextUtils.isEmpty(viewCount) ? "0" :viewCount ) + "人关注");
        tv_evaluate.setText((TextUtils.isEmpty(rateCount) ? "0" :rateCount) + "条评价");
        tv_good_evaluate.setText((TextUtils.isEmpty(rate) ? "0%" :rate+"%") + "好评");
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
