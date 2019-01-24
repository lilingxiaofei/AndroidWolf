package com.chunlangjiu.app.amain.bean;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListBean<T>{

    private List<T> list;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
