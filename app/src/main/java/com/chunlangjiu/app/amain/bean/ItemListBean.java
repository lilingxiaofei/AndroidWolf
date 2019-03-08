package com.chunlangjiu.app.amain.bean;


import java.util.List;

public class ItemListBean<T> {
    private List<T> list;//竞拍列表
    private List<T> item_list;//普通商品 列表
    private int total;
    private int totalPage;

    public List<T> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<T> item_list) {
        this.item_list = item_list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
