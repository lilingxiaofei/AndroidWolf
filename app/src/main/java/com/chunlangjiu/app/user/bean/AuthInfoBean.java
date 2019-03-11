package com.chunlangjiu.app.user.bean;

/**
 * @CreatedbBy: liucun on 2018/9/21
 * @Describe:
 */
public class AuthInfoBean {

    /**
     *
     "company_name": "我",
     "representative": "李致远",
     "idcard": "400000000000000000",
     "license_img": "http://chunlang.oss-cn-shenzhen.aliyuncs.com/94ccf4674b0d3b605fb0458f755037b6.jpg",
     "shopuser_identity_img_z": "null",
     "shopuser_identity_img_f": "null",
     "food_or_wine_img": null
     */

    private String company_name ;
    private String representative ;
    private String  idcard;
    private String  license_img;
    private String  shopuser_identity_img_z;
    private String  shopuser_identity_img_f;
    private String  food_or_wine_img;


    /**
     * 个人认证信息
     "name": "吴申飞",
     "mobile": null,
     "idcard": "430281199101039116",
     "dentity": "http:\/\/chunlang.oss-cn-shenzhen.aliyuncs.com\/ea643e894eaf891afc511339f247cf0f.jpg",
     "dentity_front": "http:\/\/chunlang.oss-cn-shenzhen.aliyuncs.com\/c5fa528b0f3fdb5995ca20fa80214f5f.jpg",
     "dentity_reverse": "http:\/\/chunlang.oss-cn-shenzhen.aliyuncs.com\/5bf94a9a0055d7c3e58eb1ce935dff45.jpg"
     */

    private String name;
    private String  mobile;
    private String dentity;
    private String dentity_front;
    private String dentity_reverse;

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getLicense_img() {
        return license_img;
    }

    public void setLicense_img(String license_img) {
        this.license_img = license_img;
    }

    public String getShopuser_identity_img_z() {
        return shopuser_identity_img_z;
    }

    public void setShopuser_identity_img_z(String shopuser_identity_img_z) {
        this.shopuser_identity_img_z = shopuser_identity_img_z;
    }

    public String getShopuser_identity_img_f() {
        return shopuser_identity_img_f;
    }

    public void setShopuser_identity_img_f(String shopuser_identity_img_f) {
        this.shopuser_identity_img_f = shopuser_identity_img_f;
    }

    public String getFood_or_wine_img() {
        return food_or_wine_img;
    }

    public void setFood_or_wine_img(String food_or_wine_img) {
        this.food_or_wine_img = food_or_wine_img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDentity() {
        return dentity;
    }

    public void setDentity(String dentity) {
        this.dentity = dentity;
    }

    public String getDentity_front() {
        return dentity_front;
    }

    public void setDentity_front(String dentity_front) {
        this.dentity_front = dentity_front;
    }

    public String getDentity_reverse() {
        return dentity_reverse;
    }

    public void setDentity_reverse(String dentity_reverse) {
        this.dentity_reverse = dentity_reverse;
    }
}
