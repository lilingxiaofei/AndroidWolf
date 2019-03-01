package com.chunlangjiu.app.order.bean;

import java.io.Serializable;
import java.util.List;

public class OrderDetailBean {
    /**
     * tid : 2439616000040086
     * shipping_type : express
     * status : TRADE_FINISHED
     * payment : 0.100
     * points_fee : 0.000
     * cancel_status : NO_APPLY_CANCEL
     * hongbao_fee : 0.000
     * post_fee : 0.000
     * pay_type : online
     * payed_fee : 0.100
     * pay_time : 1536132318
     * receiver_state : 广东省
     * receiver_city : 深圳市
     * receiver_district : 南山区
     * receiver_address : 1227474
     * trade_memo :
     * receiver_name : 邓浩然
     * receiver_mobile : 18682330720
     * ziti_addr : null
     * ziti_memo : null
     * total_fee : 0.100
     * discount_fee : 0.000
     * buyer_rate : 1
     * adjust_fee : 0.000
     * created_time : 1536132310
     * shop_id : 1
     * need_invoice : 0
     * invoice_name :
     * invoice_type : null
     * invoice_main :
     * invoice_vat_main :
     * cancel_reason : null
     * cancelInfo : []
     * refund_fee : 0
     * orders : [{"price":"0.100","aftersales_status":null,"num":1,"title":"皇家礼炮","item_id":175,"cat_id":33,"end_time":1536133308,"status":"TRADE_FINISHED","pic_path":"http://mall.chunlangjiu.com/images/1e/fc/29/56e3751ced12cf94f44968c1af3f34accb5e7405.JPG?x-oss-process=style/t","total_fee":"0.100","adjust_fee":"0.000","spec_nature_info":"颜色：紫色、尺码：m","gift_data":null,"complaints_status":"NOT_COMPLAINTS","buyer_rate":1,"oid":2439616000080086,"refund_enabled":true,"changing_enabled":true}]
     * shipping_type_name : 快递
     * status_desc : 已完成
     * is_buyer_rate : false
     * shopname : GAP官方旗舰店
     * logi : {"logi_name":"申通快递","logi_no":"123456","corp_code":"STO","delivery_id":1201809052106023165,"receiver_name":"邓浩然","t_begin":1536132349}
     */

    private long tid;
    private long cancel_id;
    private long aftersales_bn;
    private String shipping_type;
    private String status;
    private String progress;
    private String payment;
    private String points_fee;
    private String cancel_status;
    private String hongbao_fee;
    private String post_fee;
    private String pay_type;
    private String payed_fee;
    private int pay_time;
    private String receiver_state;
    private String receiver_city;
    private String receiver_district;
    private String receiver_address;
    private String trade_memo;
    private String receiver_name;
    private String receiver_mobile;
    private Object ziti_addr;
    private Object ziti_memo;
    private String total_fee;
    private String discount_fee;
    private int buyer_rate;
    private boolean is_buyer_rate;
    private String adjust_fee;
    private int created_time;
    private int modified_time;
    private int shop_id;
    private int need_invoice;
    private String invoice_name;
    private Object invoice_type;
    private String invoice_main;
    private String invoice_vat_main;
    private String cancel_reason;
    private String refund_fee;
    private String shipping_type_name;
    private String status_desc;
    private String pay_name;
    private int close_time;// 剩余的支付时间。
    private int end_time;// 订单完成时间。
    private int consign_time;//发货时间
    private String shopname;
    private String shoplogo;
    private LogiBean logi;
    //    private List<?> cancelInfo;
    private List<OrdersBean> orders;
    private OrdersBean order;
    private int item_id;
    private int cat_id;
    private int brand_id;
    private String shop_cat_id;
    private String title;
    private String sub_title;
    private String bn;
    private String price;
    private String cost_price;
    private String mkt_price;
    private int show_mkt_price;
    private String weight;
    private String unit;
    private String image_default_id;
    private String list_image;
    private int order_sort;
    private int has_discount;
    private int is_virtual;
    private int is_timing;
    private int violation;
    private int is_selfshop;
    private int nospec;
    private Object props_name;
    private Object params;
    private String sub_stock;
    private Object outer_id;
    private int is_offline;
    private Object barcode;
    private int disabled;
    private String use_platform;
    private int dlytmpl_id;
    private String label;
    private Object explain;
    private String parameter;
    private String approve_status;
    private Object reason;
    private String shop_explanation;
    private Object list_time;
    private Object delist_time;
    private int sold_quantity;
    private int rate_count;
    private int rate_good_count;
    private int rate_neutral_count;
    private int rate_bad_count;
    private int view_count;
    private int buy_count;
    private String original_bid;
    private int aftersales_month_count;
    private Object default_weight;
    private DefaultAddressBean default_address;
    private List<?> spec_desc;
    private AuctionBean auction;
    private PaymentsBean payments;
    private String paymentId;
    private String info;
    private String trade_ststus ;

    private String commission ;//平台佣金
    private String shop_payment ;//店铺实收

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public long getCancel_id() {
        return cancel_id;
    }

    public void setCancel_id(long cancel_id) {
        this.cancel_id = cancel_id;
    }

    public long getAftersales_bn() {
        return aftersales_bn;
    }

    public void setAftersales_bn(long aftersales_bn) {
        this.aftersales_bn = aftersales_bn;
    }

    public String getShipping_type() {
        return shipping_type;
    }

    public void setShipping_type(String shipping_type) {
        this.shipping_type = shipping_type;
    }

    public String getStatus() {
        return status;
    }

    public String getTrade_ststus() {
        return trade_ststus;
    }

    public void setTrade_ststus(String trade_ststus) {
        this.trade_ststus = trade_ststus;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPoints_fee() {
        return points_fee;
    }

    public void setPoints_fee(String points_fee) {
        this.points_fee = points_fee;
    }

    public String getCancel_status() {
        return cancel_status;
    }

    public void setCancel_status(String cancel_status) {
        this.cancel_status = cancel_status;
    }

    public String getHongbao_fee() {
        return hongbao_fee;
    }

    public void setHongbao_fee(String hongbao_fee) {
        this.hongbao_fee = hongbao_fee;
    }

    public String getPost_fee() {
        return post_fee;
    }

    public void setPost_fee(String post_fee) {
        this.post_fee = post_fee;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getPayed_fee() {
        return payed_fee;
    }

    public void setPayed_fee(String payed_fee) {
        this.payed_fee = payed_fee;
    }

    public int getPay_time() {
        return pay_time;
    }

    public void setPay_time(int pay_time) {
        this.pay_time = pay_time;
    }

    public String getReceiver_state() {
        return receiver_state;
    }

    public void setReceiver_state(String receiver_state) {
        this.receiver_state = receiver_state;
    }

    public String getReceiver_city() {
        return receiver_city;
    }

    public void setReceiver_city(String receiver_city) {
        this.receiver_city = receiver_city;
    }

    public String getReceiver_district() {
        return receiver_district;
    }

    public void setReceiver_district(String receiver_district) {
        this.receiver_district = receiver_district;
    }

    public String getReceiver_address() {
        return receiver_address;
    }

    public void setReceiver_address(String receiver_address) {
        this.receiver_address = receiver_address;
    }

    public String getTrade_memo() {
        return trade_memo;
    }

    public void setTrade_memo(String trade_memo) {
        this.trade_memo = trade_memo;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getReceiver_mobile() {
        return receiver_mobile;
    }

    public void setReceiver_mobile(String receiver_mobile) {
        this.receiver_mobile = receiver_mobile;
    }

    public Object getZiti_addr() {
        return ziti_addr;
    }

    public void setZiti_addr(Object ziti_addr) {
        this.ziti_addr = ziti_addr;
    }

    public Object getZiti_memo() {
        return ziti_memo;
    }

    public void setZiti_memo(Object ziti_memo) {
        this.ziti_memo = ziti_memo;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getDiscount_fee() {
        return discount_fee;
    }

    public void setDiscount_fee(String discount_fee) {
        this.discount_fee = discount_fee;
    }

    public int getBuyer_rate() {
        return buyer_rate;
    }

    public void setBuyer_rate(int buyer_rate) {
        this.buyer_rate = buyer_rate;
    }

    public String getAdjust_fee() {
        return adjust_fee;
    }

    public void setAdjust_fee(String adjust_fee) {
        this.adjust_fee = adjust_fee;
    }

    public int getCreated_time() {
        return created_time;
    }

    public void setCreated_time(int created_time) {
        this.created_time = created_time;
    }

    public int getModified_time() {
        return modified_time;
    }

    public void setModified_time(int modified_time) {
        this.modified_time = modified_time;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getNeed_invoice() {
        return need_invoice;
    }

    public void setNeed_invoice(int need_invoice) {
        this.need_invoice = need_invoice;
    }

    public String getInvoice_name() {
        return invoice_name;
    }

    public void setInvoice_name(String invoice_name) {
        this.invoice_name = invoice_name;
    }

    public Object getInvoice_type() {
        return invoice_type;
    }

    public void setInvoice_type(Object invoice_type) {
        this.invoice_type = invoice_type;
    }

    public String getInvoice_main() {
        return invoice_main;
    }

    public void setInvoice_main(String invoice_main) {
        this.invoice_main = invoice_main;
    }

    public String getInvoice_vat_main() {
        return invoice_vat_main;
    }

    public void setInvoice_vat_main(String invoice_vat_main) {
        this.invoice_vat_main = invoice_vat_main;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    public String getShop_explanation() {
        return shop_explanation;
    }

    public void setShop_explanation(String shop_explanation) {
        this.shop_explanation = shop_explanation;
    }

    public String getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(String refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getShipping_type_name() {
        return shipping_type_name;
    }

    public void setShipping_type_name(String shipping_type_name) {
        this.shipping_type_name = shipping_type_name;
    }

    public String getStatus_desc() {
        return status_desc;
    }

    public void setStatus_desc(String status_desc) {
        this.status_desc = status_desc;
    }

    public boolean isIs_buyer_rate() {
        return is_buyer_rate;
    }

    public void setIs_buyer_rate(boolean is_buyer_rate) {
        this.is_buyer_rate = is_buyer_rate;
    }

    public String getPay_name() {
        return pay_name;
    }

    public void setPay_name(String pay_name) {
        this.pay_name = pay_name;
    }

    public int getClose_time() {
        return close_time;
    }

    public void setClose_time(int close_time) {
        this.close_time = close_time;
    }

    public int getEnd_time() {
        return end_time;
    }

    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }

    public int getConsign_time() {
        return consign_time;
    }

    public void setConsign_time(int consign_time) {
        this.consign_time = consign_time;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShoplogo() {
        return shoplogo;
    }

    public void setShoplogo(String shoplogo) {
        this.shoplogo = shoplogo;
    }

    public LogiBean getLogi() {
        return logi;
    }

    public void setLogi(LogiBean logi) {
        this.logi = logi;
    }

//    public List<?> getCancelInfo() {
//        return cancelInfo;
//    }
//
//    public void setCancelInfo(List<?> cancelInfo) {
//        this.cancelInfo = cancelInfo;
//    }

    public List<OrdersBean> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersBean> orders) {
        this.orders = orders;
    }

    public OrdersBean getOrder() {
        return order;
    }

    public void setOrder(OrdersBean order) {
        this.order = order;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public String getShop_cat_id() {
        return shop_cat_id;
    }

    public void setShop_cat_id(String shop_cat_id) {
        this.shop_cat_id = shop_cat_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getBn() {
        return bn;
    }

    public void setBn(String bn) {
        this.bn = bn;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCost_price() {
        return cost_price;
    }

    public void setCost_price(String cost_price) {
        this.cost_price = cost_price;
    }

    public String getMkt_price() {
        return mkt_price;
    }

    public void setMkt_price(String mkt_price) {
        this.mkt_price = mkt_price;
    }

    public int getShow_mkt_price() {
        return show_mkt_price;
    }

    public void setShow_mkt_price(int show_mkt_price) {
        this.show_mkt_price = show_mkt_price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getImage_default_id() {
        return image_default_id;
    }

    public void setImage_default_id(String image_default_id) {
        this.image_default_id = image_default_id;
    }

    public String getList_image() {
        return list_image;
    }

    public void setList_image(String list_image) {
        this.list_image = list_image;
    }

    public int getOrder_sort() {
        return order_sort;
    }

    public void setOrder_sort(int order_sort) {
        this.order_sort = order_sort;
    }

    public int getHas_discount() {
        return has_discount;
    }

    public void setHas_discount(int has_discount) {
        this.has_discount = has_discount;
    }

    public int getIs_virtual() {
        return is_virtual;
    }

    public void setIs_virtual(int is_virtual) {
        this.is_virtual = is_virtual;
    }

    public int getIs_timing() {
        return is_timing;
    }

    public void setIs_timing(int is_timing) {
        this.is_timing = is_timing;
    }

    public int getViolation() {
        return violation;
    }

    public void setViolation(int violation) {
        this.violation = violation;
    }

    public int getIs_selfshop() {
        return is_selfshop;
    }

    public void setIs_selfshop(int is_selfshop) {
        this.is_selfshop = is_selfshop;
    }

    public int getNospec() {
        return nospec;
    }

    public void setNospec(int nospec) {
        this.nospec = nospec;
    }

    public Object getProps_name() {
        return props_name;
    }

    public void setProps_name(Object props_name) {
        this.props_name = props_name;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public String getSub_stock() {
        return sub_stock;
    }

    public void setSub_stock(String sub_stock) {
        this.sub_stock = sub_stock;
    }

    public Object getOuter_id() {
        return outer_id;
    }

    public void setOuter_id(Object outer_id) {
        this.outer_id = outer_id;
    }

    public int getIs_offline() {
        return is_offline;
    }

    public void setIs_offline(int is_offline) {
        this.is_offline = is_offline;
    }

    public Object getBarcode() {
        return barcode;
    }

    public void setBarcode(Object barcode) {
        this.barcode = barcode;
    }

    public int getDisabled() {
        return disabled;
    }

    public void setDisabled(int disabled) {
        this.disabled = disabled;
    }

    public String getUse_platform() {
        return use_platform;
    }

    public void setUse_platform(String use_platform) {
        this.use_platform = use_platform;
    }

    public int getDlytmpl_id() {
        return dlytmpl_id;
    }

    public void setDlytmpl_id(int dlytmpl_id) {
        this.dlytmpl_id = dlytmpl_id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getExplain() {
        return explain;
    }

    public void setExplain(Object explain) {
        this.explain = explain;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getApprove_status() {
        return approve_status;
    }

    public void setApprove_status(String approve_status) {
        this.approve_status = approve_status;
    }

    public Object getReason() {
        return reason;
    }

    public void setReason(Object reason) {
        this.reason = reason;
    }

    public Object getList_time() {
        return list_time;
    }

    public void setList_time(Object list_time) {
        this.list_time = list_time;
    }

    public Object getDelist_time() {
        return delist_time;
    }

    public void setDelist_time(Object delist_time) {
        this.delist_time = delist_time;
    }

    public int getSold_quantity() {
        return sold_quantity;
    }

    public void setSold_quantity(int sold_quantity) {
        this.sold_quantity = sold_quantity;
    }

    public int getRate_count() {
        return rate_count;
    }

    public void setRate_count(int rate_count) {
        this.rate_count = rate_count;
    }

    public int getRate_good_count() {
        return rate_good_count;
    }

    public void setRate_good_count(int rate_good_count) {
        this.rate_good_count = rate_good_count;
    }

    public int getRate_neutral_count() {
        return rate_neutral_count;
    }

    public void setRate_neutral_count(int rate_neutral_count) {
        this.rate_neutral_count = rate_neutral_count;
    }

    public int getRate_bad_count() {
        return rate_bad_count;
    }

    public void setRate_bad_count(int rate_bad_count) {
        this.rate_bad_count = rate_bad_count;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public int getBuy_count() {
        return buy_count;
    }

    public void setBuy_count(int buy_count) {
        this.buy_count = buy_count;
    }

    public String getOriginal_bid() {
        return original_bid;
    }

    public void setOriginal_bid(String original_bid) {
        this.original_bid = original_bid;
    }

    public int getAftersales_month_count() {
        return aftersales_month_count;
    }

    public void setAftersales_month_count(int aftersales_month_count) {
        this.aftersales_month_count = aftersales_month_count;
    }

    public Object getDefault_weight() {
        return default_weight;
    }

    public void setDefault_weight(Object default_weight) {
        this.default_weight = default_weight;
    }

    public DefaultAddressBean getDefault_address() {
        return default_address;
    }

    public void setDefault_address(DefaultAddressBean default_address) {
        this.default_address = default_address;
    }

    public List<?> getSpec_desc() {
        return spec_desc;
    }

    public void setSpec_desc(List<?> spec_desc) {
        this.spec_desc = spec_desc;
    }

    public AuctionBean getAuction() {
        return auction;
    }

    public void setAuction(AuctionBean auction) {
        this.auction = auction;
    }

    public PaymentsBean getPayments() {
        return payments;
    }

    public void setPayments(PaymentsBean payments) {
        this.payments = payments;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getShop_payment() {
        return shop_payment;
    }

    public void setShop_payment(String shop_payment) {
        this.shop_payment = shop_payment;
    }

    public static class LogiBean {
        /**
         * logi_name : 申通快递
         * logi_no : 123456
         * corp_code : STO
         * delivery_id : 1201809052106023165
         * receiver_name : 邓浩然
         * t_begin : 1536132349
         */

        private String logi_name;
        private String logi_no;
        private String corp_code;
        private long delivery_id;
        private String receiver_name;
        private long t_begin;

        public String getLogi_name() {
            return logi_name;
        }

        public void setLogi_name(String logi_name) {
            this.logi_name = logi_name;
        }

        public String getLogi_no() {
            return logi_no;
        }

        public void setLogi_no(String logi_no) {
            this.logi_no = logi_no;
        }

        public String getCorp_code() {
            return corp_code;
        }

        public void setCorp_code(String corp_code) {
            this.corp_code = corp_code;
        }

        public long getDelivery_id() {
            return delivery_id;
        }

        public void setDelivery_id(long delivery_id) {
            this.delivery_id = delivery_id;
        }

        public String getReceiver_name() {
            return receiver_name;
        }

        public void setReceiver_name(String receiver_name) {
            this.receiver_name = receiver_name;
        }

        public long getT_begin() {
            return t_begin;
        }

        public void setT_begin(long t_begin) {
            this.t_begin = t_begin;
        }
    }

    public static class OrdersBean implements Serializable {
        /**
         * price : 0.100
         * aftersales_status : null
         * num : 1
         * title : 皇家礼炮
         * item_id : 175
         * cat_id : 33
         * end_time : 1536133308
         * status : TRADE_FINISHED
         * pic_path : http://mall.chunlangjiu.com/images/1e/fc/29/56e3751ced12cf94f44968c1af3f34accb5e7405.JPG?x-oss-process=style/t
         * total_fee : 0.100
         * adjust_fee : 0.000
         * spec_nature_info : 颜色：紫色、尺码：m
         * gift_data : null
         * complaints_status : NOT_COMPLAINTS
         * buyer_rate : 1
         * oid : 2439616000080086
         * refund_enabled : true
         * changing_enabled : true
         */

        private String price;
        private String aftersales_status;
        private int num;
        private String title;
        private int item_id;
        private int cat_id;
        private String end_time;
        private String status;
        private String pic_path;
        private String total_fee;
        private String adjust_fee;
        private String spec_nature_info;
        private Object gift_data;
        private String complaints_status;
        private int buyer_rate;
        private long oid;
        private boolean refund_enabled;
        private boolean changing_enabled;
        private String payment;
        private String pay_time;
        private String consign_time;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAftersales_status() {
            return aftersales_status;
        }

        public void setAftersales_status(String aftersales_status) {
            this.aftersales_status = aftersales_status;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getItem_id() {
            return item_id;
        }

        public void setItem_id(int item_id) {
            this.item_id = item_id;
        }

        public int getCat_id() {
            return cat_id;
        }

        public void setCat_id(int cat_id) {
            this.cat_id = cat_id;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPic_path() {
            return pic_path;
        }

        public void setPic_path(String pic_path) {
            this.pic_path = pic_path;
        }

        public String getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
        }

        public String getAdjust_fee() {
            return adjust_fee;
        }

        public void setAdjust_fee(String adjust_fee) {
            this.adjust_fee = adjust_fee;
        }

        public String getSpec_nature_info() {
            return spec_nature_info;
        }

        public void setSpec_nature_info(String spec_nature_info) {
            this.spec_nature_info = spec_nature_info;
        }

        public Object getGift_data() {
            return gift_data;
        }

        public void setGift_data(Object gift_data) {
            this.gift_data = gift_data;
        }

        public String getComplaints_status() {
            return complaints_status;
        }

        public void setComplaints_status(String complaints_status) {
            this.complaints_status = complaints_status;
        }

        public int getBuyer_rate() {
            return buyer_rate;
        }

        public void setBuyer_rate(int buyer_rate) {
            this.buyer_rate = buyer_rate;
        }

        public long getOid() {
            return oid;
        }

        public void setOid(long oid) {
            this.oid = oid;
        }

        public boolean isRefund_enabled() {
            return refund_enabled;
        }

        public void setRefund_enabled(boolean refund_enabled) {
            this.refund_enabled = refund_enabled;
        }

        public boolean isChanging_enabled() {
            return changing_enabled;
        }

        public void setChanging_enabled(boolean changing_enabled) {
            this.changing_enabled = changing_enabled;
        }

        public String getPayment() {
            return payment;
        }

        public void setPayment(String payment) {
            this.payment = payment;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public String getConsign_time() {
            return consign_time;
        }

        public void setConsign_time(String consign_time) {
            this.consign_time = consign_time;
        }
    }

    public static class DefaultAddressBean {
        /**
         * addr_id : 229
         * user_id : 81
         * name : Add
         * area : 上海市/徐汇区:310100/310104
         * addr : Sadsadsadsadasdsadsadsadkjiodsnadksandsandknsaidnsaindsad
         * zip : null
         * tel : null
         * mobile : 13824404512
         * def_addr : 1
         */

        private int addr_id;
        private int user_id;
        private String name;
        private String area;
        private String addr;
        private Object zip;
        private Object tel;
        private String mobile;
        private int def_addr;

        public int getAddr_id() {
            return addr_id;
        }

        public void setAddr_id(int addr_id) {
            this.addr_id = addr_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public Object getZip() {
            return zip;
        }

        public void setZip(Object zip) {
            this.zip = zip;
        }

        public Object getTel() {
            return tel;
        }

        public void setTel(Object tel) {
            this.tel = tel;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getDef_addr() {
            return def_addr;
        }

        public void setDef_addr(int def_addr) {
            this.def_addr = def_addr;
        }
    }

    public static class AuctionBean {
        /**
         * auctionitem_id : 1
         * item_id : 178
         * starting_price : 1.000
         * auction_status : true
         * store : 100
         * number : 0
         * begin_time : 1537229439
         * end_time : 1538093439
         * status : f
         * pledge : 0
         * is_pay : true
         * original_bid : 2588888.000
         */

        private int auctionitem_id;
        private int item_id;
        private String starting_price;
        private String max_price;
        private String auction_status;
        private int store;
        private int number;
        private int begin_time;
        private int end_time;
        private String status;
        private String pledge;
        private String is_pay;
        private String original_bid;
        private String status_desc;

        public int getAuctionitem_id() {
            return auctionitem_id;
        }

        public void setAuctionitem_id(int auctionitem_id) {
            this.auctionitem_id = auctionitem_id;
        }

        public int getItem_id() {
            return item_id;
        }

        public void setItem_id(int item_id) {
            this.item_id = item_id;
        }

        public String getStarting_price() {
            return starting_price;
        }

        public void setStarting_price(String starting_price) {
            this.starting_price = starting_price;
        }

        public String getMax_price() {
            return max_price;
        }

        public void setMax_price(String max_price) {
            this.max_price = max_price;
        }

        public String getAuction_status() {
            return auction_status;
        }

        public void setAuction_status(String auction_status) {
            this.auction_status = auction_status;
        }

        public int getStore() {
            return store;
        }

        public void setStore(int store) {
            this.store = store;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getBegin_time() {
            return begin_time;
        }

        public void setBegin_time(int begin_time) {
            this.begin_time = begin_time;
        }

        public int getEnd_time() {
            return end_time;
        }

        public void setEnd_time(int end_time) {
            this.end_time = end_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPledge() {
            return pledge;
        }

        public void setPledge(String pledge) {
            this.pledge = pledge;
        }

        public String getIs_pay() {
            return is_pay;
        }

        public void setIs_pay(String is_pay) {
            this.is_pay = is_pay;
        }

        public String getOriginal_bid() {
            return original_bid;
        }

        public void setOriginal_bid(String original_bid) {
            this.original_bid = original_bid;
        }

        public String getStatus_desc() {
            return status_desc;
        }

        public void setStatus_desc(String status_desc) {
            this.status_desc = status_desc;
        }
    }

    public static class PaymentsBean {
        /**
         * payment_id : 2018091851985110
         * money : 0.100
         * cur_money : 0.100
         * status : succ
         * user_id : 81
         * auctionitem_id : 1
         * user_name : 81
         * pay_app_id : null
         * pay_name : null
         * payed_time : null
         * account : null
         * bank : null
         * pay_account : null
         * currency : null
         * paycost : null
         * pay_ver : null
         * ip : null
         * created_time : 1537285907
         * modified_time : 1537285907
         * memo : 押金支付
         * return_url : null
         * next_page : null
         * disabled : 0
         * trade_no : null
         * thirdparty_account : null
         * type : auction
         * addr_id : 226
         * next_url : null
         * groomcode : null
         * price : 10.000
         */

        private String payment_id;
        private String money;
        private String cur_money;
        private String status;
        private int user_id;
        private int auctionitem_id;
        private String user_name;
        private Object pay_app_id;
        private Object pay_name;
        private Object payed_time;
        private Object account;
        private Object bank;
        private Object pay_account;
        private Object currency;
        private Object paycost;
        private Object pay_ver;
        private Object ip;
        private int created_time;
        private int modified_time;
        private String memo;
        private Object return_url;
        private Object next_page;
        private int disabled;
        private Object trade_no;
        private Object thirdparty_account;
        private String type;
        private String addr_id;
        private Object next_url;
        private Object groomcode;
        private String price;

        public String getPayment_id() {
            return payment_id;
        }

        public void setPayment_id(String payment_id) {
            this.payment_id = payment_id;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getCur_money() {
            return cur_money;
        }

        public void setCur_money(String cur_money) {
            this.cur_money = cur_money;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getAuctionitem_id() {
            return auctionitem_id;
        }

        public void setAuctionitem_id(int auctionitem_id) {
            this.auctionitem_id = auctionitem_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public Object getPay_app_id() {
            return pay_app_id;
        }

        public void setPay_app_id(Object pay_app_id) {
            this.pay_app_id = pay_app_id;
        }

        public Object getPay_name() {
            return pay_name;
        }

        public void setPay_name(Object pay_name) {
            this.pay_name = pay_name;
        }

        public Object getPayed_time() {
            return payed_time;
        }

        public void setPayed_time(Object payed_time) {
            this.payed_time = payed_time;
        }

        public Object getAccount() {
            return account;
        }

        public void setAccount(Object account) {
            this.account = account;
        }

        public Object getBank() {
            return bank;
        }

        public void setBank(Object bank) {
            this.bank = bank;
        }

        public Object getPay_account() {
            return pay_account;
        }

        public void setPay_account(Object pay_account) {
            this.pay_account = pay_account;
        }

        public Object getCurrency() {
            return currency;
        }

        public void setCurrency(Object currency) {
            this.currency = currency;
        }

        public Object getPaycost() {
            return paycost;
        }

        public void setPaycost(Object paycost) {
            this.paycost = paycost;
        }

        public Object getPay_ver() {
            return pay_ver;
        }

        public void setPay_ver(Object pay_ver) {
            this.pay_ver = pay_ver;
        }

        public Object getIp() {
            return ip;
        }

        public void setIp(Object ip) {
            this.ip = ip;
        }

        public int getCreated_time() {
            return created_time;
        }

        public void setCreated_time(int created_time) {
            this.created_time = created_time;
        }

        public int getModified_time() {
            return modified_time;
        }

        public void setModified_time(int modified_time) {
            this.modified_time = modified_time;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public Object getReturn_url() {
            return return_url;
        }

        public void setReturn_url(Object return_url) {
            this.return_url = return_url;
        }

        public Object getNext_page() {
            return next_page;
        }

        public void setNext_page(Object next_page) {
            this.next_page = next_page;
        }

        public int getDisabled() {
            return disabled;
        }

        public void setDisabled(int disabled) {
            this.disabled = disabled;
        }

        public Object getTrade_no() {
            return trade_no;
        }

        public void setTrade_no(Object trade_no) {
            this.trade_no = trade_no;
        }

        public Object getThirdparty_account() {
            return thirdparty_account;
        }

        public void setThirdparty_account(Object thirdparty_account) {
            this.thirdparty_account = thirdparty_account;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAddr_id() {
            return addr_id;
        }

        public void setAddr_id(String addr_id) {
            this.addr_id = addr_id;
        }

        public Object getNext_url() {
            return next_url;
        }

        public void setNext_url(Object next_url) {
            this.next_url = next_url;
        }

        public Object getGroomcode() {
            return groomcode;
        }

        public void setGroomcode(Object groomcode) {
            this.groomcode = groomcode;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}
