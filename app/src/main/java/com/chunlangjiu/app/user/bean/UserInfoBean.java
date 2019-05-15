package com.chunlangjiu.app.user.bean;

/**
 * @CreatedbBy: liucun on 2018/9/17
 * @Describe:
 */
public class UserInfoBean {


    private String login_account;
    private String username;
    private String name;
    private String sex;
    private String birthday;
    private String shop_name;
    private String company_name;
    private String head_portrait;
    private String shop_id;

    private String bulletin;   //店铺简介
    private String company_area;// 经营地址
    private String representative;// 法人名称
    private String establish_date;//成立时间
    private String idcard;//身份证号码
    private String phone; //联系方式
    private String area; //店铺地址
    private String company_phone;

    private String authenticate;//是否是鉴定师
    private String authenticate_id;//鉴定师Id

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }

    public String getLogin_account() {
        return login_account;
    }

    public String getBulletin() {
        return bulletin;
    }

    public void setBulletin(String bulletin) {
        this.bulletin = bulletin;
    }

    public String getCompany_area() {
        return company_area;
    }

    public void setCompany_area(String company_area) {
        this.company_area = company_area;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public String getEstablish_date() {
        return establish_date;
    }

    public void setEstablish_date(String establish_date) {
        this.establish_date = establish_date;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setLogin_account(String login_account) {
        this.login_account = login_account;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthenticate() {
        return authenticate;
    }

    public void setAuthenticate(String authenticate) {
        this.authenticate = authenticate;
    }

    public String getAuthenticate_id() {
        return authenticate_id;
    }

    public void setAuthenticate_id(String authenticate_id) {
        this.authenticate_id = authenticate_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCompany_phone() {
        return company_phone;
    }

    public void setCompany_phone(String company_phone) {
        this.company_phone = company_phone;
    }
}
