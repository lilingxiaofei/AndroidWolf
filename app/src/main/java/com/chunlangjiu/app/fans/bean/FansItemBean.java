package com.chunlangjiu.app.fans.bean;

/**
 * Created by Administrator on 2018/12/5.
 */

public class FansItemBean {
    private String id ;
    private String name;//粉丝名称
    private String mobile ;//粉丝电话
    private String createtime ;//注册时间
    private String commission;//累计佣金

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }
}
