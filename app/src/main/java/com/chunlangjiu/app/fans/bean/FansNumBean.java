package com.chunlangjiu.app.fans.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/12/5.
 */

public class FansNumBean {
    private String fans_sum;//我的粉丝数量
    private String commission_sum ;//总佣金

    public String getFans_sum() {
        return fans_sum;
    }

    public void setFans_sum(String fans_sum) {
        this.fans_sum = fans_sum;
    }

    public String getCommission_sum() {
        return commission_sum;
    }

    public void setCommission_sum(String commission_sum) {
        this.commission_sum = commission_sum;
    }
}
