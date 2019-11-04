package com.chunlangjiu.app.goods.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/9/27
 * @Describe:
 */
public class BrandsListBean implements Serializable{

    private ArrayList<BrandBean> brands;
    private ArrayList<BrandBean> list;

    public ArrayList<BrandBean> getList() {
        return list;
    }

    public void setList(ArrayList<BrandBean> list) {
        this.list = list;
    }

    public ArrayList<BrandBean> getBrands() {
        return brands;
    }

    public void setBrands(ArrayList<BrandBean> brands) {
        this.brands = brands;
    }

    public class BrandBean implements Serializable {

        private String brand_id;
        private String brand_name;

        public String getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(String brand_id) {
            this.brand_id = brand_id;
        }

        public String getBrand_name() {
            return brand_name;
        }

        public void setBrand_name(String brand_name) {
            this.brand_name = brand_name;
        }
    }
}
