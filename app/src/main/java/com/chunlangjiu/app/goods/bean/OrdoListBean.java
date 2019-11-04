package com.chunlangjiu.app.goods.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/9/27
 * @Describe:
 */
public class OrdoListBean implements Serializable {

    private ArrayList<OrdoBean> list;

    public ArrayList<OrdoBean> getList() {
        return list;
    }

    public void setList(ArrayList<OrdoBean> list) {
        this.list = list;
    }

    public class OrdoBean implements Serializable{

        private String odor_id;
        private String odor_name;

        public String getOdor_id() {
            return odor_id;
        }

        public void setOdor_id(String odor_id) {
            this.odor_id = odor_id;
        }

        public String getOdor_name() {
            return odor_name;
        }

        public void setOdor_name(String odor_name) {
            this.odor_name = odor_name;
        }
    }
}
