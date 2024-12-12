package com.example.migrate.sync.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 临时订单
 *
 * @author lihaiqiang
 * @date 2019/4/1
 */
public class OrderTemp implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 交易流水
     */
    private String transId;
    /**
     * 业务平台侧订单编号
     */
    private String orderNo;
    /**
     * 支付令牌
     */
    private String sessionId;
    /**
     * 用户登录token
     */
    private String userToken;
    /**
     * 手机号码（账号）
     */
    private String phoneNum;
    /**
     * 用来支付的手机号
     */
    private String payPhoneNum;
    /**
     * 牌照方ID
     */
    private String partnerId;
    /**
     * 渠道合作伙伴ID
     */
    private String spPartnerId;
    /**
     * 支付结果通知地址
     */
    private String notifyUrl;
    /**
     * 终端设备SN号
     */
    private String snNo;
    /**
     * 数据来源
     */
    private String dataSrc;
    /**
     * 支付二维码
     */
    private String qrCode;
    /**
     * 票券ID
     */
    private Long couponId;
    /**
     * 生成订单时是否需要二次确认 1不需要 2需要
     */
    private String secondConfirm;
    /**
     * 生成订单时用户是否在黑名单中 1是 2不是
     */
    private String inBlackList;
    /**
     * 是否CRM故障
     */
    private String isCrmError;
    /**
     * 支付渠道
     */
    private String payChannel;
    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 是否已生成话单（话费支付的时候设置该值）
     */
    private Integer isGenerated;
    /**
     * 订单提交时间
     */
    private Date submitTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    private String diversionCode;

    private List<OrderTempItem> orderItems;

    /**
     * 1：从搜索结果访问
     * 2：从用户收藏/书签访问
     * 3：从EPG访问
     * 4：从推荐页面访问
     * 5：从猜你喜欢个性化推荐页面访问(带上推荐接口的trace_id)
     * 99：其他
     * 退订时为NULL
     */
    private String cSource;

    /**
     * 推荐服务接口返回的推荐结果标记，可以跟踪是哪次推荐产生的订购行为
     * 当内容访问来源字段取值为5时填写，否则为NULL
     */
    private String traceId;

    /**
     * CP标识
     */
    private String cpId;

    /**
     * 内容标识
     */
    private String contentId;

    /**
     * 位置等导流标识，格式：
     * F:S:T
     */
    private String diversionFST;

    /**
     * 内容等导流标识，格式：
     * C:S:K
     */
    private String diversionCSK;

    /**
     * 前N月特价活动ID
     */
    private String activityId;
    /**
     * 超前点播 活动编码
     */
    private String actionId;
    /**
     * 业务参数，用于自订购
     */
    private String actionCode;

    /**
     * 扫码前N月特价活动ID
     */
    private String scanActivityId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPayPhoneNum() {
        return payPhoneNum;
    }

    public void setPayPhoneNum(String payPhoneNum) {
        this.payPhoneNum = payPhoneNum;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getSpPartnerId() {
        return spPartnerId;
    }

    public void setSpPartnerId(String spPartnerId) {
        this.spPartnerId = spPartnerId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getSnNo() {
        return snNo;
    }

    public void setSnNo(String snNo) {
        this.snNo = snNo;
    }

    public String getDataSrc() {
        return dataSrc;
    }

    public void setDataSrc(String dataSrc) {
        this.dataSrc = dataSrc;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public String getSecondConfirm() {
        return secondConfirm;
    }

    public void setSecondConfirm(String secondConfirm) {
        this.secondConfirm = secondConfirm;
    }

    public String getInBlackList() {
        return inBlackList;
    }

    public void setInBlackList(String inBlackList) {
        this.inBlackList = inBlackList;
    }

    public String getIsCrmError() {
        return isCrmError;
    }

    public void setIsCrmError(String isCrmError) {
        this.isCrmError = isCrmError;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsGenerated() {
        return isGenerated;
    }

    public void setIsGenerated(Integer isGenerated) {
        this.isGenerated = isGenerated;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<OrderTempItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderTempItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getDiversionCode() {
        return diversionCode;
    }

    public void setDiversionCode(String diversionCode) {
        this.diversionCode = diversionCode;
    }

    public String getcSource() {
        return cSource;
    }

    public void setcSource(String cSource) {
        this.cSource = cSource;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getCpId() {
        return cpId;
    }

    public void setCpId(String cpId) {
        this.cpId = cpId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getDiversionFST() {
        return diversionFST;
    }

    public void setDiversionFST(String diversionFST) {
        this.diversionFST = diversionFST;
    }

    public String getDiversionCSK() {
        return diversionCSK;
    }

    public void setDiversionCSK(String diversionCSK) {
        this.diversionCSK = diversionCSK;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getScanActivityId() {
        return scanActivityId;
    }

    public void setScanActivityId(String scanActivityId) {
        this.scanActivityId = scanActivityId;
    }

    @Override
    public String toString() {
        return "OrderTemp [orderId=" + orderId +
                ", transId=" + transId +
                ", orderNo=" + orderNo +
                ", sessionId=" + sessionId +
                ", phoneNum=" + phoneNum +
                ", payPhoneNum=" + payPhoneNum +
                ", partnerId=" + partnerId +
                ", spPartnerId=" + spPartnerId +
                ", notifyUrl=" + notifyUrl +
                ", snNo=" + snNo +
                ", dataSrc=" + dataSrc +
                ", qrCode=" + qrCode +
                ", couponId=" + couponId +
                ", secondConfirm=" + secondConfirm +
                ", payChannel=" + payChannel +
                ", status=" + status +
                ", submitTime=" + submitTime +
                ", diversionCode=" + diversionCode +
                ", cSource=" + cSource +
                ", traceId=" + traceId +
                ", cpId=" + cpId +
                ", contentId=" + contentId +
                ", diversionFST='" + diversionFST + '\'' +
                ", diversionCSK='" + diversionCSK + '\'' +
                ", activityId='" + activityId + '\'' +
                ", scanActivityId='" + scanActivityId + '\'' +
                "]";
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
}
