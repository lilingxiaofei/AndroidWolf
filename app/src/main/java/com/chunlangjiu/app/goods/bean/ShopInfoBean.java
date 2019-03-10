package com.chunlangjiu.app.goods.bean;

/**
 * @CreatedbBy: liucun on 2018/8/18.
 * @Describe:
 */
public class ShopInfoBean {

    private ShopInfo shopInfo;

    public ShopInfo getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(ShopInfo shopInfo) {
        this.shopInfo = shopInfo;
    }

    public class ShopInfo{
        private String open_time;
        private String qq;
        private String wangwang;
        private String shop_area;
        private String shop_addr;

        private String shop_id;
        private String shop_name;
        private String shop_descript;
        private String shop_type;
        private String status;
        private String shop_logo;
        private String mobile;
        private String shopname;
        private String authentication ;
        private String grade ;//店铺等级 0普通店铺，1,星级卖家，2城市合伙人

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
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

        public String getAuthentication() {
            return authentication;
        }

        public void setAuthentication(String authentication) {
            this.authentication = authentication;
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

        public String getShop_type() {
            return shop_type;
        }

        public void setShop_type(String shop_type) {
            this.shop_type = shop_type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getShop_logo() {
            return shop_logo;
        }

        public void setShop_logo(String shop_logo) {
            this.shop_logo = shop_logo;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getOpen_time() {
            return open_time;
        }

        public void setOpen_time(String open_time) {
            this.open_time = open_time;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getWangwang() {
            return wangwang;
        }

        public void setWangwang(String wangwang) {
            this.wangwang = wangwang;
        }

        public String getShop_area() {
            return shop_area;
        }

        public void setShop_area(String shop_area) {
            this.shop_area = shop_area;
        }

        public String getShop_addr() {
            return shop_addr;
        }

        public void setShop_addr(String shop_addr) {
            this.shop_addr = shop_addr;
        }
    }

}
