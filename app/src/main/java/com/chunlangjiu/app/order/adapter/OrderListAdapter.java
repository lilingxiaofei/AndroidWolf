package com.chunlangjiu.app.order.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chunlangjiu.app.R;
import com.chunlangjiu.app.order.bean.OrderListBean;
import com.chunlangjiu.app.order.params.OrderParams;
import com.pkqup.commonlibrary.glide.GlideUtils;

import java.math.BigDecimal;
import java.util.List;

public class OrderListAdapter extends BaseQuickAdapter<OrderListBean.ListBean, BaseViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private View.OnClickListener onClickListener;
    private int type = 0;

    public OrderListAdapter(Context context, int layoutResId, List<OrderListBean.ListBean> data) {
        super(layoutResId, data);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderListBean.ListBean item) {
        LinearLayout llStore = helper.getView(R.id.llStore);
        ImageView imgStore = helper.getView(R.id.imgStore);
        TextView tvStore = helper.getView(R.id.tvStore);
        TextView tvStatus = helper.getView(R.id.tvStatus);
        LinearLayout llProducts = helper.getView(R.id.llProducts);
        TextView tvTotalNum = helper.getView(R.id.tvTotalNum);
        LinearLayout llBottom = helper.getView(R.id.llBottom);

        helper.setGone(R.id.ivDel,false);
        TextView tv1 = helper.getView(R.id.tv1);
        TextView tv2 = helper.getView(R.id.tv2);

        switch (type) {
            case 0:
                switch (item.getStatus()) {
                    case OrderParams.WAIT_BUYER_PAY:
                        tv1.setText("取消订单");
                        tv1.setVisibility(View.VISIBLE);
                        tv2.setText("去支付");
                        tv2.setVisibility(View.VISIBLE);
                        tvStatus.setTextColor(ContextCompat.getColor(context,R.color.t_red));
                        break;
                    case OrderParams.TRADE_CLOSED_BY_SYSTEM:
                        tv1.setVisibility(View.GONE);
                        tv2.setText("删除订单");
                        tv2.setVisibility(View.VISIBLE);
                        tvStatus.setTextColor(ContextCompat.getColor(context,R.color.t_black));
                        break;
                    case OrderParams.WAIT_SELLER_SEND_GOODS:
                        tv1.setVisibility(View.GONE);
                        if ("NO_APPLY_CANCEL".equals(item.getCancel_status())) {
                            tv2.setText("取消订单");
                            tv2.setVisibility(View.VISIBLE);
                        } else {
                            tv2.setVisibility(View.GONE);
                        }
                        tvStatus.setTextColor(ContextCompat.getColor(context,R.color.t_red));
                        break;
                    case OrderParams.WAIT_BUYER_CONFIRM_GOODS:
                        tv1.setVisibility(View.GONE);
                        tv2.setText("商品签单");
                        tv2.setVisibility(View.VISIBLE);
                        tvStatus.setTextColor(ContextCompat.getColor(context,R.color.t_red));
                        break;
                    case OrderParams.TRADE_FINISHED:
                        tv1.setVisibility(View.GONE);
                        if (1 == item.getBuyer_rate()) {
                            tv2.setText("评价");
                            tv2.setVisibility(View.VISIBLE);
                        } else {
                            tv2.setVisibility(View.GONE);
                        }
                        helper.setGone(R.id.ivDel,true);
                        tvStatus.setTextColor(ContextCompat.getColor(context,R.color.t_black));
                        break;
                }
                break;
            case 1://竞拍订单
                tv1.setVisibility(View.GONE);
                helper.setVisible(R.id.ivAuctionBid,false );
                switch (item.getStatus()) {
                    case "0":
                        tv2.setText("去付定金");
                        tv2.setVisibility(View.VISIBLE);
                        tvStatus.setTextColor(ContextCompat.getColor(context,R.color.t_red));
                        break;
                    case "1":
                        tv2.setText("修改出价");
                        tv2.setVisibility(View.VISIBLE);
                        tvStatus.setTextColor(ContextCompat.getColor(context,R.color.t_red));
                        break;
                    case "2":
                        helper.setVisible(R.id.ivAuctionBid,true );
                        helper.setImageResource(R.id.ivAuctionBid,R.mipmap.bid_already );
                        tvStatus.setText("");
//                        tv2.setText("去支付");
//                        tv2.setVisibility(View.VISIBLE);
                        break;
                    case "3":
//                        tv2.setText("删除订单");
//                        tv2.setVisibility(View.VISIBLE);
                        break;
                    default:
                        helper.setVisible(R.id.ivAuctionBid,true );
                        helper.setImageResource(R.id.ivAuctionBid,R.mipmap.bid_not);
                        tv2.setVisibility(View.GONE);
                        break;
                }
                break;
            case 2:
                switch (item.getStatus()) {
                    case "0":
                        tv1.setVisibility(View.GONE);
                        tv2.setText("撤销申请");
                        tv2.setVisibility(View.GONE);
                        break;
                    case "1":
                        if ("1".equals(item.getProgress())) {
                            tv1.setText("撤销申请");
                            tv1.setVisibility(View.GONE);
                            tv2.setText("退货发货");
                            tv2.setVisibility(View.VISIBLE);
                        } else {
                            tv1.setVisibility(View.GONE);
                            tv2.setText("撤销申请");
                            tv2.setVisibility(View.GONE);
                        }
                        break;
                    case "2":
                        tv1.setVisibility(View.GONE);
                        tv2.setVisibility(View.GONE);
                        break;
                    case "3":
                        tv1.setVisibility(View.GONE);
                        tv2.setText("删除");
                        tv2.setVisibility(View.GONE);
                        break;
                }
                break;
            case 3:
                switch (item.getStatus()) {
                    case OrderParams.WAIT_SELLER_SEND_GOODS:
                        if ("NO_APPLY_CANCEL".equals(item.getCancel_status()) || "FAILS".equals(item.getCancel_status())) {
                            tv1.setText("无货");
                            tv1.setVisibility(View.VISIBLE);
                            tv2.setText("发货");
                            tv2.setVisibility(View.VISIBLE);
                        } else {
                            tv1.setVisibility(View.GONE);
                            tv2.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        tv1.setVisibility(View.GONE);
                        tv2.setVisibility(View.GONE);
                        break;
                }
                break;
            case 4:
                switch (item.getStatus()) {
                    case "0":
                        tv1.setText("拒绝申请");
                        tv1.setVisibility(View.VISIBLE);
                        tv2.setText("同意申请");
                        tv2.setVisibility(View.VISIBLE);
                        break;
                    case "1":
                        if ("2".equals(item.getProgress())) {
                            tv1.setVisibility(View.GONE);
                            tv2.setText("同意退款");
                            tv2.setVisibility(View.VISIBLE);
                        } else {
                            tv1.setVisibility(View.GONE);
                            tv2.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        tv1.setVisibility(View.GONE);
                        tv2.setVisibility(View.GONE);
                        break;
                }
                break;
            case 5:
                switch (item.getStatus()) {
                    case "WAIT_CHECK":
                        tv1.setText("拒绝");
                        tv1.setVisibility(View.VISIBLE);
                        tv2.setText("同意退款");
                        tv2.setVisibility(View.VISIBLE);
                        break;
                    default:
                        tv1.setVisibility(View.GONE);
                        tv2.setVisibility(View.GONE);
                        break;
                }
                break;
        }

        llStore.setTag(helper.getAdapterPosition());
        llStore.setOnClickListener(onClickListener);
        llProducts.setTag(helper.getAdapterPosition());
        llProducts.setOnClickListener(onClickListener);
        llBottom.setTag(helper.getAdapterPosition());
        llBottom.setOnClickListener(onClickListener);
        tv1.setTag(helper.getAdapterPosition());
        tv1.setOnClickListener(onClickListener);
        tv2.setTag(helper.getAdapterPosition());
        tv2.setOnClickListener(onClickListener);
        tvStore.setText(item.getShopname());
        tvStatus.setText(item.getStatus_desc());
        GlideUtils.loadImageShop(context, item.getShop_logo(), imgStore);
        llProducts.removeAllViews();
        switch (type) {
            case 0:
            case 3:
            case 5:
                for (OrderListBean.ListBean.OrderBean orderBean : item.getOrder()) {
                    View inflate = inflater.inflate(R.layout.order_adapter_list_product_item, null);
                    ImageView imgProduct = inflate.findViewById(R.id.imgProduct);
                    GlideUtils.loadImage(context, orderBean.getPic_path(), imgProduct);
                    TextView tvProductName = inflate.findViewById(R.id.tvProductName);
                    tvProductName.setText(orderBean.getTitle());
                    TextView tvProductPrice = inflate.findViewById(R.id.tvProductPrice);
                    if (!TextUtils.isEmpty(orderBean.getPrice())) {
                        tvProductPrice.setText(String.format("¥%s", new BigDecimal(orderBean.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                    }
                    TextView tvProductDesc = inflate.findViewById(R.id.tvProductDesc);
                    tvProductDesc.setText(orderBean.getSpec_nature_info());

                    TextView tvProductNum = inflate.findViewById(R.id.tvProductNum);
                    tvProductNum.setText(String.format("x%d", orderBean.getNum()));
                    llProducts.addView(inflate);
//                    if (llProducts.getChildCount() == item.getOrder().size()) {
//                        View view_line = inflate.findViewById(R.id.view_line);
//                        view_line.setVisibility(View.GONE);
//                    }
                }
                if (3 == type) {
                    tvTotalNum.setText(String.format("共%s件商品\u3000合计：¥%s", new BigDecimal(item.getItemnum()).setScale(0, BigDecimal.ROUND_HALF_UP).toString(), new BigDecimal(item.getPayment()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                } else {
                    tvTotalNum.setText(String.format("共%s件商品\u3000合计：¥%s", new BigDecimal(item.getTotalItem()).setScale(0, BigDecimal.ROUND_HALF_UP).toString(), new BigDecimal(item.getPayment()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                }
                break;
            case 1:
                for (OrderListBean.ListBean.OrderBean orderBean : item.getOrder()) {
                    View inflate = inflater.inflate(R.layout.order_adapter_list_product_item, null);
                    ImageView imgProduct = inflate.findViewById(R.id.imgProduct);
                    GlideUtils.loadImage(context, orderBean.getPic_path(), imgProduct);
                    TextView tvProductName = inflate.findViewById(R.id.tvProductName);
                    tvProductName.setText(orderBean.getTitle());
                    TextView tvProductPrice = inflate.findViewById(R.id.tvProductPrice);
                    TextView tvProductNum = inflate.findViewById(R.id.tvProductNum);
                    tvProductPrice.setVisibility(View.GONE);
                    tvProductNum.setVisibility(View.GONE);
                    TextView tvAuctionPrice = inflate.findViewById(R.id.tvAuctionPrice);
                    String startPrice = "" ;
                    if (!TextUtils.isEmpty(orderBean.getPrice())) {
                        startPrice = String.format("起拍价：%s", String.format("¥%s", new BigDecimal(orderBean.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                    }

                    tvProductNum.setGravity(Gravity.CENTER_VERTICAL);
                    String maxPrice = "";
                    try {
                        BigDecimal bigDecimal = new BigDecimal(orderBean.getSpec_nature_info());
                        maxPrice =String.format("最高出价：%s", String.format("¥%s", bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                    } catch (Exception ex) {
                        maxPrice =String.format("最高出价：%s", orderBean.getSpec_nature_info());
                    }
                    tvAuctionPrice.setText(startPrice+"\n"+maxPrice);
                    tvAuctionPrice.setVisibility(View.VISIBLE);
                    llProducts.addView(inflate);
//                    if (llProducts.getChildCount() == item.getOrder().size()) {
//                        View view_line = inflate.findViewById(R.id.view_line);
//                        view_line.setVisibility(View.GONE);
//                    }
                }
                if (3 == type) {
                    tvTotalNum.setText(String.format("共%s件商品\u3000合计：¥%s", new BigDecimal(item.getItemnum()).setScale(0, BigDecimal.ROUND_HALF_UP).toString(), new BigDecimal(item.getPayment()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                } else {
                    tvTotalNum.setText(String.format("共%s件商品\u3000合计：¥%s", new BigDecimal(item.getTotalItem()).setScale(0, BigDecimal.ROUND_HALF_UP).toString(), new BigDecimal(item.getPayment()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                }
                break;
            case 2:
            case 4:
                View inflate = inflater.inflate(R.layout.order_adapter_list_product_item, null);
                ImageView imgProduct = inflate.findViewById(R.id.imgProduct);
                GlideUtils.loadImage(context, item.getSku().getPic_path(), imgProduct);
                TextView tvProductName = inflate.findViewById(R.id.tvProductName);
                tvProductName.setText(item.getSku().getTitle());
                TextView tvProductPrice = inflate.findViewById(R.id.tvProductPrice);
                tvProductPrice.setText(String.format("¥%s", new BigDecimal(item.getSku().getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));

                TextView tvProductDesc = inflate.findViewById(R.id.tvProductDesc);
                tvProductDesc.setText(item.getSku().getSpec_nature_info());

                TextView tvProductNum = inflate.findViewById(R.id.tvProductNum);
                tvProductNum.setText(String.format("x%d", item.getNum()));
                llProducts.addView(inflate);
//                View view_line = inflate.findViewById(R.id.view_line);
//                view_line.setVisibility(View.GONE);
                tvTotalNum.setText(String.format("共%s件商品\u3000合计：¥%s", new BigDecimal(item.getTotalItem()).setScale(0, BigDecimal.ROUND_HALF_UP).toString(), new BigDecimal(item.getSku().getPayment()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                break;
        }
    }
}
