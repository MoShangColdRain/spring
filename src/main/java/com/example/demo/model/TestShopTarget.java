package com.example.demo.model;

public class TestShopTarget {
	
    private Integer id;

    private String date;

    private String shopCode;

    private Integer validNewCustomerTargetNum;

    private Integer intoStoreTargetNum;

    private Integer orderTargetNum;

    private Integer deliveryTargetNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date == null ? null : date.trim();
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode == null ? null : shopCode.trim();
    }

    public Integer getValidNewCustomerTargetNum() {
        return validNewCustomerTargetNum;
    }

    public void setValidNewCustomerTargetNum(Integer validNewCustomerTargetNum) {
        this.validNewCustomerTargetNum = validNewCustomerTargetNum;
    }

    public Integer getIntoStoreTargetNum() {
        return intoStoreTargetNum;
    }

    public void setIntoStoreTargetNum(Integer intoStoreTargetNum) {
        this.intoStoreTargetNum = intoStoreTargetNum;
    }

    public Integer getOrderTargetNum() {
        return orderTargetNum;
    }

    public void setOrderTargetNum(Integer orderTargetNum) {
        this.orderTargetNum = orderTargetNum;
    }

    public Integer getDeliveryTargetNum() {
        return deliveryTargetNum;
    }

    public void setDeliveryTargetNum(Integer deliveryTargetNum) {
        this.deliveryTargetNum = deliveryTargetNum;
    }
}