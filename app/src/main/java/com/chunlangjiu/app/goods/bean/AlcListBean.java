package com.chunlangjiu.app.goods.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/9/27
 * @Describe:
 */
public class AlcListBean implements Serializable {

    private ArrayList<AlcBean> list;

    public ArrayList<AlcBean> getList() {
        return list;
    }

    public void setList(ArrayList<AlcBean> list) {
        this.list = list;
    }

    public class AlcBean implements Serializable{

        private String alcohol_id;
        private String alcohol_name;

        public String getAlcohol_id() {
            return alcohol_id;
        }

        public void setAlcohol_id(String alcohol_id) {
            this.alcohol_id = alcohol_id;
        }

        public String getAlcohol_name() {
            return alcohol_name;
        }

        public void setAlcohol_name(String alcohol_name) {
            this.alcohol_name = alcohol_name;
        }
    }
}
