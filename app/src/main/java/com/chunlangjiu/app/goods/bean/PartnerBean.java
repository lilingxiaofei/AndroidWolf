package com.chunlangjiu.app.goods.bean;

import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/9/27
 * @Describe:
 */
public class PartnerBean {

    private List<AlcBean> list;

    public List<AlcBean> getList() {
        return list;
    }

    public void setList(List<AlcBean> list) {
        this.list = list;
    }

    public class AlcBean{

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
