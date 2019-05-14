package com.chunlangjiu.app.amain.bean;

/**
 * @CreatedbBy: liucun on 2018/8/8
 * @Describe:
 */
public class ThirdpartyLoginBean {

    private String binded;
    private String user_info ;//是否设置邀请码

    public String getBinded() {
        return binded;
    }

    public void setBinded(String binded) {
        this.binded = binded;
    }

    public String getUser_info() {
        return user_info;
    }

    public void setUser_info(String user_info) {
        this.user_info = user_info;
    }
}
