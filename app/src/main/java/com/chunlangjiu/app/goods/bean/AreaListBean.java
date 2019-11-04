package com.chunlangjiu.app.goods.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/9/27
 * @Describe:
 */
public class AreaListBean implements Serializable {

    private ArrayList<AreaBean> list;

    public ArrayList<AreaBean> getList() {
        return list;
    }

    public void setList(ArrayList<AreaBean> list) {
        this.list = list;
    }

    public class AreaBean implements Serializable{

        private String area_id;
        private String area_name;

        public String getArea_id() {
            return area_id;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }
    }
}
