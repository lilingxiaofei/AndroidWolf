package com.chunlangjiu.app.goods.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/1/29.
 */

public class StoreActivityBean {
    private String top_img;
    private String bottom_img;
    private String img ;
    private String pattern;//1单图，2多图
    private String color;
    private String open;
    private List<GoodsListDetailBean> item;
    private List<GoodsListDetailBean> auction;

    public String getTop_img() {
        return top_img;
    }

    public void setTop_img(String top_img) {
        this.top_img = top_img;
    }

    public String getBottom_img() {
        return bottom_img;
    }

    public void setBottom_img(String bottom_img) {
        this.bottom_img = bottom_img;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public List<GoodsListDetailBean> getItem() {
        return item;
    }

    public void setItem(List<GoodsListDetailBean> item) {
        this.item = item;
    }

    public List<GoodsListDetailBean> getAuction() {
        return auction;
    }

    public void setAuction(List<GoodsListDetailBean> auction) {
        this.auction = auction;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
