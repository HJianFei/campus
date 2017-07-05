package com.loosoo100.campus100.beans;

/**
 * Created by yang on 2016/10/31.
 * 礼物盒子领取页面信息
 */

public class GiftGetInfo {

    /**
     * sendUid : 496,619,
     * logisticsComs : zhongtong
     * logisticsNum : 412726261110
     * logisticsName : 中通
     * orderStatus : 1
     * receiveUid : 495
     * userAddress : 幸福街
     * userName : 刘刘刘大哥
     * userPhoto : http://www.campus100.cn/App/Upload/touxiang/2016-10-14/5800b258e36fa.png
     * goodsThums :   http://ocawozwsv.bkt.clouddn.com/attr_2016-09-28_57eb6adfc49da.jpg_thumb
     * status : 1
     */

    private ListBean list;

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    public static class ListBean {
        private String sendUid;
        private String logisticsComs;
        private String logisticsNum;
        private String logisticsName;
        private String orderStatus;
        private String receiveUid;
        private String userAddress;
        private String userName;
        private String userPhoto;
        private String goodsThums;
        private int status;

        public String getSendUid() {
            return sendUid;
        }

        public void setSendUid(String sendUid) {
            this.sendUid = sendUid;
        }

        public String getLogisticsComs() {
            return logisticsComs;
        }

        public void setLogisticsComs(String logisticsComs) {
            this.logisticsComs = logisticsComs;
        }

        public String getLogisticsNum() {
            return logisticsNum;
        }

        public void setLogisticsNum(String logisticsNum) {
            this.logisticsNum = logisticsNum;
        }

        public String getLogisticsName() {
            return logisticsName;
        }

        public void setLogisticsName(String logisticsName) {
            this.logisticsName = logisticsName;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getReceiveUid() {
            return receiveUid;
        }

        public void setReceiveUid(String receiveUid) {
            this.receiveUid = receiveUid;
        }

        public String getUserAddress() {
            return userAddress;
        }

        public void setUserAddress(String userAddress) {
            this.userAddress = userAddress;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPhoto() {
            return userPhoto;
        }

        public void setUserPhoto(String userPhoto) {
            this.userPhoto = userPhoto;
        }

        public String getGoodsThums() {
            return goodsThums;
        }

        public void setGoodsThums(String goodsThums) {
            this.goodsThums = goodsThums;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
