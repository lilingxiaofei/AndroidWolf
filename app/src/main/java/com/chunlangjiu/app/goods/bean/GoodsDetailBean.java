package com.chunlangjiu.app.goods.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/8/15
 * @Describe:
 */
public class GoodsDetailBean implements Serializable{

    private ShareBean share;
    private ShopBean shop;
    private ItemBean item;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ShareBean getShare() {
        return share;
    }

    public void setShare(ShareBean share) {
        this.share = share;
    }

    public ShopBean getShop() {
        return shop;
    }

    public void setShop(ShopBean shop) {
        this.shop = shop;
    }

    public ItemBean getItem() {
        return item;
    }

    public void setItem(ItemBean item) {
        this.item = item;
    }

    public class ShareBean implements Serializable{
        private String h5href;
        private String image;

        public String getH5href() {
            return h5href;
        }

        public void setH5href(String h5href) {
            this.h5href = h5href;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public class ParameterBean implements Serializable{
        private String title;
        private String value;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


    public class ShopBean implements Serializable{
        private String shop_id;
        private String shop_name;
        private String shop_descript;
        private String shop_logo;
        private String shop_type;
        private String shopname;
        private String mobile;

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getShop_descript() {
            return shop_descript;
        }

        public void setShop_descript(String shop_descript) {
            this.shop_descript = shop_descript;
        }

        public String getShop_logo() {
            return shop_logo;
        }

        public void setShop_logo(String shop_logo) {
            this.shop_logo = shop_logo;
        }

        public String getShop_type() {
            return shop_type;
        }

        public void setShop_type(String shop_type) {
            this.shop_type = shop_type;
        }
    }

    public class ItemBean implements Serializable{
        private String item_id;
        private String shop_id;
        private String cat_id;
        private String brand_id;
        private String shop_cat_id;
        private String is_collect;

        private List<String> images;
        private String title;
        private String sub_title;
        private String bn;
        private String price;
        private String cost_price;
        private String mkt_price;
        private String show_mkt_price;
        private String weight;
        private String unit;
        private String image_default_id;
        private String order_sort;
        private String is_selfshop;
        private String sub_stock;
        private String outer_id;
        private String is_offline;
        private String sold_quantity;//销量

        private String rate_count;
        private String rate_good_count;
        private String rate_neutral_count;
        private String rate_bad_count;
        private String view_count;
        private String buy_count;
        private String aftersales_month_count;


        private String brand_name;
        private String brand_alias;
        private String brand_logo;
        private String realStore;
        private String freez;
        private String store;
        private String default_sku_id;

        private String label;//标签
        private String explain;//商品说明
        private Auction auction;
        private String service_url ;
        private List<ParameterBean> parameter ;

        public String getIs_collect() {
            return is_collect;
        }

        public void setIs_collect(String is_collect) {
            this.is_collect = is_collect;
        }

        public Auction getAuction() {
            return auction;
        }

        public List<ParameterBean> getParameter() {
            return parameter;
        }

        public void setParameter(List<ParameterBean> parameter) {
            this.parameter = parameter;
        }

        public void setAuction(Auction auction) {
            this.auction = auction;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getDefault_sku_id() {
            return default_sku_id;
        }

        public void setDefault_sku_id(String default_sku_id) {
            this.default_sku_id = default_sku_id;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getCat_id() {
            return cat_id;
        }

        public void setCat_id(String cat_id) {
            this.cat_id = cat_id;
        }

        public String getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(String brand_id) {
            this.brand_id = brand_id;
        }

        public String getShop_cat_id() {
            return shop_cat_id;
        }

        public void setShop_cat_id(String shop_cat_id) {
            this.shop_cat_id = shop_cat_id;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
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

        public String getShow_mkt_price() {
            return show_mkt_price;
        }

        public void setShow_mkt_price(String show_mkt_price) {
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

        public String getOrder_sort() {
            return order_sort;
        }

        public void setOrder_sort(String order_sort) {
            this.order_sort = order_sort;
        }

        public String getIs_selfshop() {
            return is_selfshop;
        }

        public void setIs_selfshop(String is_selfshop) {
            this.is_selfshop = is_selfshop;
        }

        public String getSub_stock() {
            return sub_stock;
        }

        public void setSub_stock(String sub_stock) {
            this.sub_stock = sub_stock;
        }

        public String getOuter_id() {
            return outer_id;
        }

        public void setOuter_id(String outer_id) {
            this.outer_id = outer_id;
        }

        public String getIs_offline() {
            return is_offline;
        }

        public void setIs_offline(String is_offline) {
            this.is_offline = is_offline;
        }

        public String getSold_quantity() {
            return sold_quantity;
        }

        public void setSold_quantity(String sold_quantity) {
            this.sold_quantity = sold_quantity;
        }

        public String getRate_count() {
            return rate_count;
        }

        public void setRate_count(String rate_count) {
            this.rate_count = rate_count;
        }

        public String getRate_good_count() {
            return rate_good_count;
        }

        public void setRate_good_count(String rate_good_count) {
            this.rate_good_count = rate_good_count;
        }

        public String getRate_neutral_count() {
            return rate_neutral_count;
        }

        public void setRate_neutral_count(String rate_neutral_count) {
            this.rate_neutral_count = rate_neutral_count;
        }

        public String getRate_bad_count() {
            return rate_bad_count;
        }

        public void setRate_bad_count(String rate_bad_count) {
            this.rate_bad_count = rate_bad_count;
        }

        public String getView_count() {
            return view_count;
        }

        public void setView_count(String view_count) {
            this.view_count = view_count;
        }

        public String getBuy_count() {
            return buy_count;
        }

        public void setBuy_count(String buy_count) {
            this.buy_count = buy_count;
        }

        public String getAftersales_month_count() {
            return aftersales_month_count;
        }

        public void setAftersales_month_count(String aftersales_month_count) {
            this.aftersales_month_count = aftersales_month_count;
        }

        public String getBrand_name() {
            return brand_name;
        }

        public void setBrand_name(String brand_name) {
            this.brand_name = brand_name;
        }

        public String getBrand_alias() {
            return brand_alias;
        }

        public void setBrand_alias(String brand_alias) {
            this.brand_alias = brand_alias;
        }

        public String getBrand_logo() {
            return brand_logo;
        }

        public void setBrand_logo(String brand_logo) {
            this.brand_logo = brand_logo;
        }

        public String getRealStore() {
            return realStore;
        }

        public void setRealStore(String realStore) {
            this.realStore = realStore;
        }

        public String getFreez() {
            return freez;
        }

        public void setFreez(String freez) {
            this.freez = freez;
        }

        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
        }

        public String getService_url() {
            return service_url;
        }

        public void setService_url(String service_url) {
            this.service_url = service_url;
        }
    }

    public class Auction implements Serializable{
        private String data;
        private String auctionitem_id;
        private String item_id;
        private String starting_price;
        private String auction_status;
        private String store;
        private String number;
        private String begin_time;
        private String end_time;
        private String status;//true-明拍   false-暗拍
        private String max_price;//最高出价
        private String check;//是否已经出过价
        private String pledge;//定金
        private String original_bid;//自己当前的出价
        private String is_pay;//是否支付过定金
        private String payment_id;//订单id

        public String getPayment_id() {
            return payment_id;
        }

        public void setPayment_id(String payment_id) {
            this.payment_id = payment_id;
        }

        public String getOriginal_bid() {
            return original_bid;
        }

        public void setOriginal_bid(String original_bid) {
            this.original_bid = original_bid;
        }

        public String getIs_pay() {
            return is_pay;
        }

        public void setIs_pay(String is_pay) {
            this.is_pay = is_pay;
        }

        public String getPledge() {
            return pledge;
        }

        public void setPledge(String pledge) {
            this.pledge = pledge;
        }

        public String getMax_price() {
            return max_price;
        }

        public void setMax_price(String max_price) {
            this.max_price = max_price;
        }

        public String getCheck() {
            return check;
        }

        public void setCheck(String check) {
            this.check = check;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getAuctionitem_id() {
            return auctionitem_id;
        }

        public void setAuctionitem_id(String auctionitem_id) {
            this.auctionitem_id = auctionitem_id;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
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

        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public void setBegin_time(String begin_time) {
            this.begin_time = begin_time;
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
    }

}
