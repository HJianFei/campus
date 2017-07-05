package com.loosoo100.campus100.wxapi;

/**
 * Created by Administrator on 2016/10/27.
 */

public class WXBean {


    /**
     * appid : wxd2e31944db9cf56f
     * prepayid : wx2016102716281483eb0b783f0100878704
     * partnerid : 1304633401
     * timestamp : 1477556894
     * noncestr : w417zhhsik4d5xepk45nokdn3y6nov8m
     * packageX : Sign=WXPay
     * sign : BC49888E9E760202361681B259A48694
     */

    private String appid;
    private String prepayid;
    private String partnerid;
    private String timestamp;
    private String noncestr;
    private String packageX;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
