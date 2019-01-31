package com.chunlangjiu.app.amain.bean;

import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/8/27.
 * @Describe:
 */
public class HomeListBean {

    private List<HomeBean> list;

    private List<HomeBean> auction_list;

    public List<HomeBean> getAuction_list() {
        return auction_list;
    }

    public void setAuction_list(List<HomeBean> auction_list) {
        this.auction_list = auction_list;
    }

    public List<HomeBean> getList() {
        return list;
    }

    public void setList(List<HomeBean> list) {
        this.list = list;
    }

}
