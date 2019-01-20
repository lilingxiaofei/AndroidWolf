package com.chunlangjiu.app.amain.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/9/9.
 * @Describe:
 */
public class AuctionListBean implements Serializable {

    private List<AuctionBean> list;

    public List<AuctionBean> getList() {
        return list;
    }

    public void setList(List<AuctionBean> list) {
        this.list = list;
    }

    public class AuctionBean implements Serializable{

        private String item_id;
        private String cat_id;
        private String brand_id;
        private String shop_cat_id;
        private String title;
        private String sub_title;
        private String price;
        private String cost_price;
        private String mkt_price;
        private String weight;
        private String unit;
        private String image_default_id;
        private String list_image;
        private String label;
        private String explain;

        private String desc;
        private String auction_starting_price;
        private String auction_status;
        private String auction_store;
        private String auction_begin_time;
        private String auction_end_time;
        private String auction_number;
        private String max_price;


        private String view_count;//关注人数
        private String rate_count;//评价条数
        private String rate ;//好评率

        private String shop_id ;//店铺id
        private String shop_name ;//店铺id
        private String grade ;//店铺等级 0普通店铺，1,星级卖家，2城市合伙人

        public String getMax_price() {
            return max_price;
        }

        public void setMax_price(String max_price) {
            this.max_price = max_price;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getAuction_starting_price() {
            return auction_starting_price;
        }

        public void setAuction_starting_price(String auction_starting_price) {
            this.auction_starting_price = auction_starting_price;
        }

        public String getAuction_status() {
            return auction_status;
        }

        public void setAuction_status(String auction_status) {
            this.auction_status = auction_status;
        }

        public String getAuction_store() {
            return auction_store;
        }

        public void setAuction_store(String auction_store) {
            this.auction_store = auction_store;
        }

        public String getAuction_begin_time() {
            return auction_begin_time;
        }

        public void setAuction_begin_time(String auction_begin_time) {
            this.auction_begin_time = auction_begin_time;
        }

        public String getAuction_end_time() {
            return auction_end_time;
        }

        public void setAuction_end_time(String auction_end_time) {
            this.auction_end_time = auction_end_time;
        }

        public String getAuction_number() {
            return auction_number;
        }

        public void setAuction_number(String auction_number) {
            this.auction_number = auction_number;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
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

        public String getView_count() {
            return view_count;
        }

        public void setView_count(String view_count) {
            this.view_count = view_count;
        }

        public String getRate_count() {
            return rate_count;
        }

        public void setRate_count(String rate_count) {
            this.rate_count = rate_count;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }
    }



}
