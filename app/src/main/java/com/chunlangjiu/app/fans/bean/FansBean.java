package com.chunlangjiu.app.fans.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2018/12/5.
 */

public class FansBean implements Parcelable {
    private String fansNum;//我的粉丝数量
    private String inviteCode ;//我的邀请码
    private String invitePerson ;//我的邀请人

    public String getFansNum() {
        return fansNum;
    }

    public void setFansNum(String fansNum) {
        this.fansNum = fansNum;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getInvitePerson() {
        return invitePerson;
    }

    public void setInvitePerson(String invitePerson) {
        this.invitePerson = invitePerson;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fansNum);
        dest.writeString(this.inviteCode);
        dest.writeString(this.invitePerson);
    }

    public FansBean() {
    }

    protected FansBean(Parcel in) {
        this.fansNum = in.readString();
        this.inviteCode = in.readString();
        this.invitePerson = in.readString();
    }

    public static final Parcelable.Creator<FansBean> CREATOR = new Parcelable.Creator<FansBean>() {
        @Override
        public FansBean createFromParcel(Parcel source) {
            return new FansBean(source);
        }

        @Override
        public FansBean[] newArray(int size) {
            return new FansBean[size];
        }
    };
}
