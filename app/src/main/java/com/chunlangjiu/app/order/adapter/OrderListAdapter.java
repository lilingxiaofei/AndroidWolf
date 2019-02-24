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

        tvStore.setText(item.getShopname());
        tvStatus.setText(item.getStatus_desc());
        GlideUtils.loadImageShop(context, item.getShop_logo(), imgStore);

        ImageView ivDel = helper.getView(R.id.ivDel);
//        ivDel.setVisibility(View.GONE);
//        TextView tv1 = helper.getView(R.id.tv1);
//        TextView tv2 = helper.getView(R.id.tv2);
//        tv1.setVisibility(View.GONE);
//        tv2.setVisibility(View.GONE);
        TextView tvDelete = helper.getView(R.id.tvDelete);//删除订单
        TextView tvCancel = helper.getView(R.id.tvCancel);//取消订单
        TextView tvRefund = helper.getView(R.id.tvRefund);//申请退款
        TextView tvNotGoods = helper.getView(R.id.tvNotGoods);//无货
        TextView tvConsentRefund = helper.getView(R.id.tvConsentRefund);//同意退款
        TextView tvRefusedRefund = helper.getView(R.id.tvRefusedRefund);//拒绝退款
        TextView tvReturnSend = helper.getView(R.id.tvReturnSend);//退货发货
        TextView tvBackOutApply = helper.getView(R.id.tvBackOutApply);//撤销申请
        TextView tvRefusedApply = helper.getView(R.id.tvRefusedApply);//拒绝申请
        TextView tvConsentApply = helper.getView(R.id.tvConsentApply);//同意申请
        TextView tvSendGoods = helper.getView(R.id.tvSendGoods);//发货
        TextView tvGoodsSignBill = helper.getView(R.id.tvGoodsSignBill);//商品签单
        TextView tvConfirmReceipt = helper.getView(R.id.tvConfirmReceipt);//确认收货
        TextView tvEvaluate = helper.getView(R.id.tvEvaluate);//去评价
        TextView tvPay = helper.getView(R.id.tvPay);//去支付
        TextView tvEditPrice = helper.getView(R.id.tvEditPrice);//修改出价
        TextView tvPayDeposit = helper.getView(R.id.tvPayDeposit);//去付定金


        tvDelete.setVisibility(View.GONE);//删除订单
        tvCancel.setVisibility(View.GONE);//取消订单
        tvRefund.setVisibility(View.GONE);//申请退款
        tvNotGoods.setVisibility(View.GONE);//无货
        tvConsentRefund.setVisibility(View.GONE);//同意退款
        tvRefusedRefund.setVisibility(View.GONE);//拒绝退款
        tvReturnSend.setVisibility(View.GONE);//退货发货
        tvBackOutApply .setVisibility(View.GONE);//撤销申请
        tvRefusedApply .setVisibility(View.GONE);//拒绝申请
        tvConsentApply.setVisibility(View.GONE);//同意申请
        tvSendGoods.setVisibility(View.GONE);//发货
        tvGoodsSignBill.setVisibility(View.GONE);//商品签单
        tvConfirmReceipt.setVisibility(View.GONE);//确认收货
        tvEvaluate.setVisibility(View.GONE);//去评价
        tvPay.setVisibility(View.GONE);//去支付
        tvEditPrice.setVisibility(View.GONE);//修改出价
        tvPayDeposit.setVisibility(View.GONE);//去付定金

        switch (type) {
            case 0:
                switch (item.getStatus()) {
                    case OrderParams.WAIT_BUYER_PAY:
                        tvCancel.setVisibility(View.VISIBLE);
                        tvPay.setVisibility(View.VISIBLE);
                        tvStatus.setTextColor(ContextCompat.getColor(context,R.color.t_red));
                        break;
                    case OrderParams.TRADE_CLOSED_BY_SYSTEM:
                        tvDelete.setVisibility(View.VISIBLE);
                        tvStatus.setTextColor(ContextCompat.getColor(context,R.color.t_black));
                        break;
                    case OrderParams.WAIT_SELLER_SEND_GOODS:
                        if ("NO_APPLY_CANCEL".equals(item.getCancel_status())) {
                            tvCancel.setVisibility(View.VISIBLE);
                        }
                        tvStatus.setTextColor(ContextCompat.getColor(context,R.color.t_red));
                        break;
                    case OrderParams.WAIT_BUYER_CONFIRM_GOODS:
                        tvGoodsSignBill.setVisibility(View.VISIBLE);
                        tvStatus.setTextColor(ContextCompat.getColor(context,R.color.t_red));
                        break;
                    case OrderParams.TRADE_FINISHED:
                        if (!item.isIs_buyer_rate()) {
                            tvEvaluate.setVisibility(View.VISIBLE);
                        }
                        tvDelete.setVisibility(View.VISIBLE);
                        tvStatus.setTextColor(ContextCompat.getColor(context,R.color.t_black));
                        break;
                }
                break;
            case 1://竞拍订单
                helper.setVisible(R.id.ivAuctionBid,false );
                switch (item.getStatus()) {
                    case OrderParams.AUCTION_WAIT_PAY:
                        tvPayDeposit.setVisibility(View.VISIBLE);
                        tvStatus.setTextColor(ContextCompat.getColor(context,R.color.t_red));
                        break;
                    case OrderParams.AUCTION_BIDDING:
                        tvEditPrice.setVisibility(View.VISIBLE);
                        tvStatus.setTextColor(ContextCompat.getColor(context,R.color.t_red));
                        break;
                    case OrderParams.AUCTION_WON_BID:
                        helper.setVisible(R.id.ivAuctionBid,true );
                        helper.setImageResource(R.id.ivAuctionBid,R.mipmap.bid_already );
                        tvStatus.setText("");
                        break;
                    case OrderParams.AUCTION_OUTBID:
                        helper.setVisible(R.id.ivAuctionBid,true );
                        helper.setImageResource(R.id.ivAuctionBid,R.mipmap.bid_not);
                        break;
                    case OrderParams.AUCTION_DELIVERY:
//                        tv2.setText("删除订单");
//                        tv2.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                switch (item.getStatus()) {
                    case "0":
                        tvBackOutApply.setVisibility(View.VISIBLE);
                        break;
                    case "1":
                        if ("1".equals(item.getProgress())) {
                            tvBackOutApply.setVisibility(View.GONE);
                            tvReturnSend.setVisibility(View.VISIBLE);
                        } else {
                            tvBackOutApply.setVisibility(View.GONE);
                        }
                        break;
                    case "2":
                        break;
                    case "3":
                        tvDelete.setVisibility(View.GONE);
                        break;
                }
                break;
            case 3:
                switch (item.getStatus()) {
                    case OrderParams.WAIT_SELLER_SEND_GOODS:
                        if ("NO_APPLY_CANCEL".equals(item.getCancel_status()) || "FAILS".equals(item.getCancel_status())) {
                            tvNotGoods.setVisibility(View.VISIBLE);
                            tvSendGoods.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case 4:
                switch (item.getStatus()) {
                    case "0":
                        tvRefusedApply.setVisibility(View.VISIBLE);
                        tvConsentApply.setVisibility(View.VISIBLE);
                        break;
                    case "1":
                        if ("2".equals(item.getProgress())) {
                            tvConsentRefund.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case 5:
                switch (item.getStatus()) {
                    case "WAIT_CHECK":
                        tvRefusedRefund.setVisibility(View.VISIBLE);
                        tvConsentRefund.setVisibility(View.VISIBLE);
                        break;
                    default:
                }
                break;
        }

        helper.addOnClickListener(R.id.tvDelete);//删除订单
        helper.addOnClickListener(R.id. tvCancel);//取消订单
        helper.addOnClickListener(R.id.tvRefund);//申请退款
        helper.addOnClickListener(R.id.tvNotGoods);//无货
        helper.addOnClickListener(R.id.tvConsentRefund);//同意退款
        helper.addOnClickListener(R.id.tvRefusedRefund);//拒绝退款
        helper.addOnClickListener(R.id.tvReturnSend);//退货发货
        helper.addOnClickListener(R.id.tvBackOutApply );//撤销申请
        helper.addOnClickListener(R.id.tvRefusedApply );//拒绝申请
        helper.addOnClickListener(R.id.tvConsentApply);//同意申请
        helper.addOnClickListener(R.id.tvSendGoods);//发货
        helper.addOnClickListener(R.id.tvGoodsSignBill);//商品签单
        helper.addOnClickListener(R.id.tvConfirmReceipt);//确认收货
        helper.addOnClickListener(R.id.tvEvaluate);//去评价
        helper.addOnClickListener(R.id.tvPay);//去支付
        helper.addOnClickListener(R.id.tvEditPrice);//修改出价
        helper.addOnClickListener(R.id.tvPayDeposit);//去付定金


        helper.addOnClickListener(R.id.llStore);//点击店铺信息
        helper.addOnClickListener(R.id.llProducts);//
        helper.addOnClickListener(R.id.llBottom);//


        llProducts.removeAllViews();
        switch (type) {
            case 0:
            case 3:
            case 5:
                if(item.getOrder()!=null){
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
                    }
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
                tvTotalNum.setText(String.format("共%s件商品\u3000合计：¥%s", new BigDecimal(item.getTotalItem()).setScale(0, BigDecimal.ROUND_HALF_UP).toString(), new BigDecimal(item.getSku().getPayment()).setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
                break;
        }
    }
}
