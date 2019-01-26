package com.chunlangjiu.app.user.bean;

public class BankCardInfoBean {

    /**
     * bankname : 招商银行
     * cardprefixnum : 621483
     * cardname : 银联IC普卡
     * cardtype : 银联借记卡
     * cardprefixlength : 6
     * banknum : 3080000
     * isLuhn : false
     * iscreditcard : 1
     * cardlength : 16
     * bankurl : http://www.cmbchina.com/
     * enbankname : China Merchants Bank
     * abbreviation : CMB
     * bankimage : http://auth.apis.la/bank/4_CMB.png
     * servicephone : 95555
     */

    private String bankname;
    private String cardprefixnum;
    private String cardname;
    private String cardtype;
    private String cardprefixlength;
    private String banknum;
    private boolean isLuhn;
    private int iscreditcard;
    private String cardlength;
    private String bankurl;
    private String enbankname;
    private String abbreviation;
    private String bankimage;
    private String servicephone;

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getCardprefixnum() {
        return cardprefixnum;
    }

    public void setCardprefixnum(String cardprefixnum) {
        this.cardprefixnum = cardprefixnum;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getCardprefixlength() {
        return cardprefixlength;
    }

    public void setCardprefixlength(String cardprefixlength) {
        this.cardprefixlength = cardprefixlength;
    }

    public String getBanknum() {
        return banknum;
    }

    public void setBanknum(String banknum) {
        this.banknum = banknum;
    }

    public boolean isIsLuhn() {
        return isLuhn;
    }

    public void setIsLuhn(boolean isLuhn) {
        this.isLuhn = isLuhn;
    }

    public int getIscreditcard() {
        return iscreditcard;
    }

    public void setIscreditcard(int iscreditcard) {
        this.iscreditcard = iscreditcard;
    }

    public String getCardlength() {
        return cardlength;
    }

    public void setCardlength(String cardlength) {
        this.cardlength = cardlength;
    }

    public String getBankurl() {
        return bankurl;
    }

    public void setBankurl(String bankurl) {
        this.bankurl = bankurl;
    }

    public String getEnbankname() {
        return enbankname;
    }

    public void setEnbankname(String enbankname) {
        this.enbankname = enbankname;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getBankimage() {
        return bankimage;
    }

    public void setBankimage(String bankimage) {
        this.bankimage = bankimage;
    }

    public String getServicephone() {
        return servicephone;
    }

    public void setServicephone(String servicephone) {
        this.servicephone = servicephone;
    }
}
