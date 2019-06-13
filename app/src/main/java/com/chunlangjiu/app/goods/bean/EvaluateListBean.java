package com.chunlangjiu.app.goods.bean;

import java.util.List;

/**
 * @CreatedbBy: liucun on 2018/8/15
 * @Describe:
 */
public class EvaluateListBean {

    private List<EvaluateDetailBean> list;

    public List<EvaluateDetailBean> getList() {
        return list;
    }

    public void setList(List<EvaluateDetailBean> list) {
        this.list = list;
    }

    public class EvaluateDetailBean{
        private String rate_id;
        private String result;
        private String content;
        private String[] rate_pic;
        private String is_reply;
        private String reply_content;
        private String reply_time;
        private String anony;
        private String is_append;
        private String created_time;
        private String append;
        private String user_name;

        public String getRate_id() {
            return rate_id;
        }

        public void setRate_id(String rate_id) {
            this.rate_id = rate_id;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String[] getRate_pic() {
            return rate_pic;
        }

        public void setRate_pic(String[] rate_pic) {
            this.rate_pic = rate_pic;
        }

        public String getIs_reply() {
            return is_reply;
        }

        public void setIs_reply(String is_reply) {
            this.is_reply = is_reply;
        }

        public String getReply_content() {
            return reply_content;
        }

        public void setReply_content(String reply_content) {
            this.reply_content = reply_content;
        }

        public String getReply_time() {
            return reply_time;
        }

        public void setReply_time(String reply_time) {
            this.reply_time = reply_time;
        }

        public String getAnony() {
            return anony;
        }

        public void setAnony(String anony) {
            this.anony = anony;
        }

        public String getIs_append() {
            return is_append;
        }

        public void setIs_append(String is_append) {
            this.is_append = is_append;
        }

        public String getCreated_time() {
            return created_time;
        }

        public void setCreated_time(String created_time) {
            this.created_time = created_time;
        }

        public String getAppend() {
            return append;
        }

        public void setAppend(String append) {
            this.append = append;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }
    }
}
