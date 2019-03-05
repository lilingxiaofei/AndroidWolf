package com.chunlangjiu.app.order.bean;

import java.io.Serializable;

public class AuctionOrderListBean {
    /**
     * payment_id : 2018091854541015
     * cur_money : 0.100
     * auctionitem_id : 1
     * price : 10.000
     * created_time : 1537284774
     * auction : {"auctionitem_id":1,"item_id":178,"starting_price":"1.000","auction_status":"true","store":100,"number":0,"begin_time":1537229439,"end_time":1538093439,"status":"true"}
     * item : {"item_id":178,"shop_id":1,"cat_id":399,"brand_id":59,"shop_cat_id":",63,","title":"Zzz","sub_title":"Xzccxz","bn":"G5B8EB977082D5","price":"0.010","cost_price":"0.000","mkt_price":"0.000","show_mkt_price":0,"weight":"750.000","unit":"ML","image_default_id":"http://chunlang.oss-cn-shenzhen.aliyuncs.com/52ad7b1072a4b818b75e8e088fc56085.jpg","list_image":"http://chunlang.oss-cn-shenzhen.aliyuncs.com/52ad7b1072a4b818b75e8e088fc56085.jpg,http://chunlang.oss-cn-shenzhen.aliyuncs.com/6fbf35f13fbc8c11c026e0bf132ee8b8.jpg","order_sort":0,"created_time":1536080247,"modified_time":1536080247,"has_discount":0,"is_virtual":0,"is_timing":0,"violation":0,"is_selfshop":0,"nospec":1,"spec_desc":[],"props_name":null,"params":null,"sub_stock":"1","outer_id":null,"is_offline":0,"barcode":null,"disabled":0,"use_platform":"0","dlytmpl_id":15,"label":null,"explain":null,"parameter":null,"approve_status":"onsale","reason":null,"list_time":1536080274,"delist_time":null,"sold_quantity":5,"rate_count":2,"rate_good_count":2,"rate_neutral_count":0,"rate_bad_count":0,"view_count":0,"buy_count":0,"aftersales_month_count":0,"default_weight":null,"shopname":"GAP官方","shoplogo":"http://images.bbc.shopex123.com/images/0f/58/13/820703fb57f96fbaa8bafdc156db83fea50b1ba6.gif"}
     * status : 0
     * type : auction
     */
    private long tid;
    private String payment_id;
    private String cur_money;
    private int auctionitem_id;
    private String price;
    private int created_time;
    private AuctionBean auction;
    private ItemBean item;
    private String status;
    private String type;
    private String status_desc;
    private String trade_ststus;

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getCur_money() {
        return cur_money;
    }

    public void setCur_money(String cur_money) {
        this.cur_money = cur_money;
    }

    public int getAuctionitem_id() {
        return auctionitem_id;
    }

    public void setAuctionitem_id(int auctionitem_id) {
        this.auctionitem_id = auctionitem_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCreated_time() {
        return created_time;
    }

    public void setCreated_time(int created_time) {
        this.created_time = created_time;
    }

    public AuctionBean getAuction() {
        return auction;
    }

    public void setAuction(AuctionBean auction) {
        this.auction = auction;
    }

    public ItemBean getItem() {
        return item;
    }

    public void setItem(ItemBean item) {
        this.item = item;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus_desc() {
        return status_desc;
    }

    public String getTrade_ststus() {
        return trade_ststus;
    }

    public void setTrade_ststus(String trade_ststus) {
        this.trade_ststus = trade_ststus;
    }

    public void setStatus_desc(String status_desc) {
        this.status_desc = status_desc;
    }

    public static class AuctionBean implements Serializable{
        /**
         * auctionitem_id : 1
         * item_id : 178
         * starting_price : 1.000
         * auction_status : true
         * store : 100
         * number : 0
         * begin_time : 1537229439
         * end_time : 1538093439
         * status : true
         */

        private int auctionitem_id;
        private int item_id;
        private String starting_price;
        private String auction_status;
        private int store;
        private int number;
        private int begin_time;
        private int end_time;
        private String status;
        private String max_price;
        private String original_bid;

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

        public String getMax_price() {
            return max_price;
        }

        public void setMax_price(String max_price) {
            this.max_price = max_price;
        }

        public String getOriginal_bid() {
            return original_bid;
        }

        public void setOriginal_bid(String original_bid) {
            this.original_bid = original_bid;
        }
    }

    public static class ItemBean {
        /**
         * item_id : 178
         * shop_id : 1
         * cat_id : 399
         * brand_id : 59
         * shop_cat_id : ,63,
         * title : Zzz
         * sub_title : Xzccxz
         * bn : G5B8EB977082D5
         * price : 0.010
         * cost_price : 0.000
         * mkt_price : 0.000
         * show_mkt_price : 0
         * weight : 750.000
         * unit : ML
         * image_default_id : http://chunlang.oss-cn-shenzhen.aliyuncs.com/52ad7b1072a4b818b75e8e088fc56085.jpg
         * list_image : http://chunlang.oss-cn-shenzhen.aliyuncs.com/52ad7b1072a4b818b75e8e088fc56085.jpg,http://chunlang.oss-cn-shenzhen.aliyuncs.com/6fbf35f13fbc8c11c026e0bf132ee8b8.jpg
         * order_sort : 0
         * created_time : 1536080247
         * modified_time : 1536080247
         * has_discount : 0
         * is_virtual : 0
         * is_timing : 0
         * violation : 0
         * is_selfshop : 0
         * nospec : 1
         * spec_desc : []
         * props_name : null
         * params : null
         * sub_stock : 1
         * outer_id : null
         * is_offline : 0
         * barcode : null
         * disabled : 0
         * use_platform : 0
         * dlytmpl_id : 15
         * label : null
         * explain : null
         * parameter : null
         * approve_status : onsale
         * reason : null
         * list_time : 1536080274
         * delist_time : null
         * sold_quantity : 5
         * rate_count : 2
         * rate_good_count : 2
         * rate_neutral_count : 0
         * rate_bad_count : 0
         * view_count : 0
         * buy_count : 0
         * aftersales_month_count : 0
         * default_weight : null
         * shopname : GAP官方
         * shoplogo : http://images.bbc.shopex123.com/images/0f/58/13/820703fb57f96fbaa8bafdc156db83fea50b1ba6.gif
         */

        private int item_id;
        private int shop_id;
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
        private int created_time;
        private int modified_time;
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
        private Object label;
        private Object explain;
        private Object parameter;
        private String approve_status;
        private Object reason;
        private int list_time;
        private Object delist_time;
        private int sold_quantity;
        private int rate_count;
        private int rate_good_count;
        private int rate_neutral_count;
        private int rate_bad_count;
        private int view_count;
        private int buy_count;
        private int aftersales_month_count;
        private Object default_weight;
        private String shopname;
        private String shoplogo;
//        private List<?> spec_desc;

        public int getItem_id() {
            return item_id;
        }

        public void setItem_id(int item_id) {
            this.item_id = item_id;
        }

        public int getShop_id() {
            return shop_id;
        }

        public void setShop_id(int shop_id) {
            this.shop_id = shop_id;
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

        public Object getLabel() {
            return label;
        }

        public void setLabel(Object label) {
            this.label = label;
        }

        public Object getExplain() {
            return explain;
        }

        public void setExplain(Object explain) {
            this.explain = explain;
        }

        public Object getParameter() {
            return parameter;
        }

        public void setParameter(Object parameter) {
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

        public int getList_time() {
            return list_time;
        }

        public void setList_time(int list_time) {
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

//        public List<?> getSpec_desc() {
//            return spec_desc;
//        }
//
//        public void setSpec_desc(List<?> spec_desc) {
//            this.spec_desc = spec_desc;
//        }
    }
}
