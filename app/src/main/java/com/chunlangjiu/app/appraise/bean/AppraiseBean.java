package com.chunlangjiu.app.appraise.bean;

/**
 * Created by Administrator on 2019/4/11.
 */

public class AppraiseBean {


    /**
     * "shop_id": 2,
     "authenticate_name": "版本更新",
     "authenticate_scope": "刚好哈哈哈哈哈哈个好伙伴vvbbbb",
     "authenticate_require": "刚好好吧宝贝ｖｖｖｖｖｖ个很好",
     "authenticate_content": "刚好宝贝发广告粉 v 个",
     "authenticate_img": "http://chunlang.oss-cn-shenzhen.aliyuncs.com/687e4f23177c90154154b3a8122bf9d9.jpg",
     "authenticate_id": 2,
     "line": "0",
     "rate": "0%",
     "day": "0"
     */

    private String shop_id;
    private String authenticate_name;
    private String authenticate_scope;
    private String authenticate_require;
    private String authenticate_content;
    private String authenticate_img;
    private String authenticate_id;
    private String line;
    private String rate;
    private String day;

    private boolean isAdd;

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getAuthenticate_name() {
        return authenticate_name;
    }

    public void setAuthenticate_name(String authenticate_name) {
        this.authenticate_name = authenticate_name;
    }

    public String getAuthenticate_scope() {
        return authenticate_scope;
    }

    public void setAuthenticate_scope(String authenticate_scope) {
        this.authenticate_scope = authenticate_scope;
    }

    public String getAuthenticate_require() {
        return authenticate_require;
    }

    public void setAuthenticate_require(String authenticate_require) {
        this.authenticate_require = authenticate_require;
    }

    public String getAuthenticate_content() {
        return authenticate_content;
    }

    public void setAuthenticate_content(String authenticate_content) {
        this.authenticate_content = authenticate_content;
    }

    public String getAuthenticate_img() {
        return authenticate_img;
    }

    public void setAuthenticate_img(String authenticate_img) {
        this.authenticate_img = authenticate_img;
    }

    public String getAuthenticate_id() {
        return authenticate_id;
    }

    public void setAuthenticate_id(String authenticate_id) {
        this.authenticate_id = authenticate_id;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
