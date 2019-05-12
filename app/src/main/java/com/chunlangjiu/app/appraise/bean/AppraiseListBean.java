package com.chunlangjiu.app.appraise.bean;


import java.util.List;

public class AppraiseListBean<T>{

    private List<T> list;
    private String content;
    private String count;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
