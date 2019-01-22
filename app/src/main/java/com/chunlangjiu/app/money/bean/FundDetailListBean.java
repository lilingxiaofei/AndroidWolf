package com.chunlangjiu.app.money.bean;

import java.util.List;

public class FundDetailListBean {
    private List<FundDetailBean> list;

    public List<FundDetailBean> getList() {
        return list;
    }

    public void setList(List<FundDetailBean> list) {
        this.list = list;
    }

    public static class FundDetailBean{

        /**
         * log_id : 44
         * type : add
         * user_id : 179
         * operator : 系统
         * fee : 0.01
         * message : 充值
         * logtime : 1548062853
         */

        private int log_id;
        private String type;
        private int user_id;
        private String operator;
        private String fee;
        private String message;
        private int logtime;

        public int getLog_id() {
            return log_id;
        }

        public void setLog_id(int log_id) {
            this.log_id = log_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getLogtime() {
            return logtime;
        }

        public void setLogtime(int logtime) {
            this.logtime = logtime;
        }
    }


}
