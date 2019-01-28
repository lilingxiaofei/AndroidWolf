package com.chunlangjiu.app.user.bean;

import java.io.Serializable;
import java.util.List;

public class BankCardListBean {
    public List<BankCardDetailBean> getList() {
        return list;
    }

    public void setList(List<BankCardDetailBean> list) {
        this.list = list;
    }

    private List<BankCardDetailBean> list;


    public static class BankCardDetailBean implements Serializable {


        /**
         * bank_id : 24
         * user_id : 179
         * name : 陈斌
         * bank : 建设银行
         * card : 6214837708967932
         * mobile : 15919823747
         * bank_branch : 龙华
         */

        private int bank_id;
        private int user_id;
        private String name;
        private String bank;
        private String card;
        private String mobile;
        private String bank_branch;
        private BankCardInfoBean bankCardInfoBean;

        public BankCardInfoBean getBankCardInfoBean() {
            return bankCardInfoBean;
        }

        public void setBankCardInfoBean(BankCardInfoBean bankCardInfoBean) {
            this.bankCardInfoBean = bankCardInfoBean;
        }

        public int getBank_id() {
            return bank_id;
        }

        public void setBank_id(int bank_id) {
            this.bank_id = bank_id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getBank_branch() {
            return bank_branch;
        }

        public void setBank_branch(String bank_branch) {
            this.bank_branch = bank_branch;
        }
    }


}
