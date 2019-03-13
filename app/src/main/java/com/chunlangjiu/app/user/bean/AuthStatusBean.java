package com.chunlangjiu.app.user.bean;

/**
 * @CreatedbBy: liucun on 2018/9/21
 * @Describe:
 */
public class AuthStatusBean {

    public static final String AUTH_SUCCESS = "finish";
    public static final String AUTH_LOCKED = "locked";
    public static final String AUTH_FAILING = "failing";
    public static final String AUTH_MODIFIER= "modifier";
    public static final String AUTH_MODEFIER_FAIL= "modefierfail";


    //'active'=>'未审核',
    //'locked'=>'审核中',
    //'failing'=>'审核驳回',
    //'finish'=>'审核通过',
    private String status;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
