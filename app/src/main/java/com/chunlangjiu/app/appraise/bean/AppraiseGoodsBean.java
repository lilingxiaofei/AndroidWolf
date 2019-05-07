package com.chunlangjiu.app.appraise.bean;

/**
 * Created by Administrator on 2019/4/11.
 */

public class AppraiseGoodsBean {

    /**
     * "chateau_id": 16,
     "title": "刚好",
     "name": "",
     "img": "http:\/\/chunlang.oss-cn-shenzhen.aliyuncs.com\/07a553344e4aa6107e5733e601f886cd.jpg,http:\/\/chunlang.oss-cn-shenzhen.aliyuncs.com\/7649b7383ed0477b3756b54a99d2bcd8.jpg,http:\/\/chunlang.oss-cn-shenzhen.aliyuncs.com\/bdb0d73363c8ab942e1196c0bc293afe.jpg,http:\/\/chunlang.oss-cn-shenzhen.aliyuncs.com\/f99694304a5a33f653863a3c065f9a52.jpg,http:\/\/chunlang.oss-cn-shenzhen.aliyuncs.com\/311d940f45d05778c9a4c1edca067ff1.jpg,http:\/\/chunlang.oss-cn-shenzhen.aliyuncs.com\/29fabdc66962f896435d84246581be5e.jpg",
     "series": "哈哈",
     "user_id": 2,
     "price": "100.00",
     "status": "true",
     "year": "2078",
     "authenticate_id": 2,
     "colour": "0.00",
     "flaw": "0.00",
     "accessory": "0.00",
     "content": "姐姐斤斤计较"
     */

    private String chateau_id;
    private String title;
    private String name;
    private String img;
    private String series;
    private String user_id;
    private String price;
    private String status;
    private String year;
    private String authenticate_id;
    private String colour;
    private String flaw;
    private String accessory;
    private String content;



    private boolean isAdd;

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    public String getChateau_id() {
        return chateau_id;
    }

    public void setChateau_id(String chateau_id) {
        this.chateau_id = chateau_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAuthenticate_id() {
        return authenticate_id;
    }

    public void setAuthenticate_id(String authenticate_id) {
        this.authenticate_id = authenticate_id;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getFlaw() {
        return flaw;
    }

    public void setFlaw(String flaw) {
        this.flaw = flaw;
    }

    public String getAccessory() {
        return accessory;
    }

    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
