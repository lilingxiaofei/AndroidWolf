package com.chunlangjiu.app.fans.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/12/5.
 */

public class FansCodeBean implements Serializable{
    private String code;//邀请码
    private String url;
    private String title ;
    private String sub_title;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }
}
