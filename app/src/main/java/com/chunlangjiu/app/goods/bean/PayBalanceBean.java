package com.chunlangjiu.app.goods.bean;

public class PayBalanceBean {
        private boolean password;//是否设置了支付密码
        private String deposit;//账户余额

    public boolean isPassword() {
        return password;
    }

    public void setPassword(boolean password) {
        this.password = password;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }
}
