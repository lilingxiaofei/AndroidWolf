package com.chunlangjiu.app.amain.bean;


import java.util.List;

public class ListBean<T>{

    private List<T> list;

    private int false_count;
    private int true_count;
    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getFalse_count() {
        return false_count;
    }

    public void setFalse_count(int false_count) {
        this.false_count = false_count;
    }

    public int getTrue_count() {
        return true_count;
    }

    public void setTrue_count(int true_count) {
        this.true_count = true_count;
    }
}
