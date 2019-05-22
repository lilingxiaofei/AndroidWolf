package com.chunlangjiu.app.amain.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/8/27.
 * @Describe:
 */
public class HomeModulesBean implements Serializable{

    public static final String ITEM_GOODS = "item";//商品详情
    public static final String ITEM_SHOP = "shop";//店铺详情
    public static final String ITEM_CLASS = "category";//我要买酒-分类
    public static final String ITEM_BRAND = "SPbrand";//品牌
    public static final String ITEM_ACTIVITY = "activity";//活动列表
    public static final String ITEM_WINERY = "winery";//酒庄
    public static final String ITEM_EVALUATION = "evaluation";//名酒估价
    public static final String ITEM_MEMBER = "member";//我的
    public static final String ITEM_CART = "cart";//购物车
    public static final String ITEM_H5 = "h5";//网页
    public static final String ITEM_SELLWINE = "sellwine";//我要卖酒
    public static final String ITEM_AUCTION = "auction";//竞拍列表
    public static final String ITEM_PARTNER = "partner";//竞拍列表




    private List<Modules> modules;

    public List<Modules> getModules() {
        return modules;
    }

    public void setModules(List<Modules> modules) {
        this.modules = modules;
    }

    public class Modules implements Serializable{
        private String tmpl;
        private String widget;
        private String order_sort;
        private Params params;

        public String getTmpl() {
            return tmpl;
        }

        public void setTmpl(String tmpl) {
            this.tmpl = tmpl;
        }

        public String getWidget() {
            return widget;
        }

        public void setWidget(String widget) {
            this.widget = widget;
        }

        public String getOrder_sort() {
            return order_sort;
        }

        public void setOrder_sort(String order_sort) {
            this.order_sort = order_sort;
        }

        public Params getParams() {
            return params;
        }

        public void setParams(Params params) {
            this.params = params;
        }
    }

    public class Params implements Serializable{
        private String id ;
        private String image;
        private String linktype;
        private String link;
        private String imagesrc;
        private String webview;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private List<Pic> pic;

        public List<Pic> getPic() {
            return pic;
        }

        public void setPic(List<Pic> pic) {
            this.pic = pic;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLinktype() {
            return linktype;
        }

        public void setLinktype(String linktype) {
            this.linktype = linktype;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getImagesrc() {
            return imagesrc;
        }

        public void setImagesrc(String imagesrc) {
            this.imagesrc = imagesrc;
        }

        public String getWebview() {
            return webview;
        }

        public void setWebview(String webview) {
            this.webview = webview;
        }

    }

    public class Pic implements Serializable{
        private String tag;
        private String link;
        private String linktarget;
        private String linktype;
        private String imagesrc;
        private String webview;
        //        private Webparam webparam;
        private String itemid;
        private String categoryname;
        private String linkinfo;
        private String cat_id;

        private String brandname;
        private String image;
        private String brand_id;


        public String getBrandname() {
            return brandname;
        }

        public void setBrandname(String brandname) {
            this.brandname = brandname;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(String brand_id) {
            this.brand_id = brand_id;
        }

        public String getCategoryname() {
            return categoryname;
        }

        public void setCategoryname(String categoryname) {
            this.categoryname = categoryname;
        }

        public String getCat_id() {
            return cat_id;
        }

        public void setCat_id(String cat_id) {
            this.cat_id = cat_id;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getLinktarget() {
            return linktarget;
        }

        public void setLinktarget(String linktarget) {
            this.linktarget = linktarget;
        }

        public String getLinkinfo() {
            return linkinfo;
        }

        public void setLinkinfo(String linkinfo) {
            this.linkinfo = linkinfo;
        }

        public String getLinktype() {
            return linktype;
        }

        public void setLinktype(String linktype) {
            this.linktype = linktype;
        }

        public String getImagesrc() {
            return imagesrc;
        }

        public void setImagesrc(String imagesrc) {
            this.imagesrc = imagesrc;
        }

        public String getWebview() {
            return webview;
        }

        public void setWebview(String webview) {
            this.webview = webview;
        }

    }

    public class Webparam {
        private String itemid;
        private String shopid;
        private String activity_id;
        private String promotion_id;
        private String catid;

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getShopid() {
            return shopid;
        }

        public void setShopid(String shopid) {
            this.shopid = shopid;
        }

        public String getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(String activity_id) {
            this.activity_id = activity_id;
        }

        public String getPromotion_id() {
            return promotion_id;
        }

        public void setPromotion_id(String promotion_id) {
            this.promotion_id = promotion_id;
        }

        public String getCatid() {
            return catid;
        }

        public void setCatid(String catid) {
            this.catid = catid;
        }
    }
}
