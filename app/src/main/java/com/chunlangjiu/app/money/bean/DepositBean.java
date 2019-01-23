package com.chunlangjiu.app.money.bean;

public class DepositBean {

    /**
     * deposit_status : 1
     * deposit : 0.01
     */

    private String deposit_status;
    private String deposit;

    public String getDeposit_status() {
        return deposit_status;
    }

    public void setDeposit_status(String deposit_status) {
        this.deposit_status = deposit_status;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }
}
