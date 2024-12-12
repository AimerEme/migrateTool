package com.example.migrate.sync.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 临时订单项目
 *
 * @author lihaiqiang
 * @date 2019/4/5
 */
public class OrderTempItem implements Serializable {
    private static final long serialVersionUID = -1L;
    /**
     * 项目ID
     */
    private String itemId;
    /**
     * 所属临时订单ID
     */
    private String orderId;
    /**
     * 产品ID
     */
    private String productId;
    /**
     * 产品编码
     */
    private String productCode;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 产品数量
     */
    private Integer count;
    /**
     * 产品价格，单位：元
     */
    private Double price;
    /**
     * 计费代码ID
     */
    private String chargeId;
    /**
     * 计费代码价格，单位：元
     */
    private Double fees;
    /**
     * 计费类型 1点播 2包月
     */
    private String feeType;
    /**
     * 所属合作伙伴ID
     */
    private String partnerId;
    /**
     * 提交时间
     */
    private Date submitTime;

    private String diversionCode;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public Double getFees() {
        return fees;
    }

    public void setFees(Double fees) {
        this.fees = fees;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public String getDiversionCode() {
        return diversionCode;
    }

    public void setDiversionCode(String diversionCode) {
        this.diversionCode = diversionCode;
    }
}
