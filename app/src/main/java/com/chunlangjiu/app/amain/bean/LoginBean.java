package com.chunlangjiu.app.amain.bean;

/**
 * @CreatedbBy: liucun on 2018/8/8
 * @Describe:
 */
public class LoginBean {

    private String accessToken;
    private String referrer ;//是否设置邀请码
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }
}
