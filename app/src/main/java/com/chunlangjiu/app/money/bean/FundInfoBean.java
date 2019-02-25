package com.chunlangjiu.app.money.bean;

public class FundInfoBean {

    /**
     * status : 成功
     * type : add
     * pay_name : 支付宝支付
     * id : 20190225515610110
     * time : 1551070811
     * memo : 充值
     */

    private String status;
    private String type;
    private String pay_name;
    private String id;
    private long time;
    private String memo;
    private String bank_name;

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPay_name() {
        return pay_name;
    }

    public void setPay_name(String pay_name) {
        this.pay_name = pay_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
