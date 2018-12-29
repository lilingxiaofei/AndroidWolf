package com.chunlangjiu.app.fans.bean;

/**
 * Created by Administrator on 2018/12/5.
 */

public class FansItemBean {
    private String id ;
    private String fansName;//粉丝名称
    private String phone ;//粉丝电话
    private String registerTime ;//注册时间
    private String totalMoney;//累计佣金

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFansName() {
        return fansName;
    }

    public void setFansName(String fansName) {
        this.fansName = fansName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }
}
