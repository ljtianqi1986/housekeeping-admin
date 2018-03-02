package com.biz.model.Pmodel.basic;


public class PwxGoodsStockbak {

    private String lmStockId;//联盟商品
    private String orderId;
    private String stockId;//90商品
    private Integer changeNumber;
    private String whareHouseId;
    private Integer state;
    private Integer orderState;  //退货时订单状态
    private Integer isTicket;
    private String city;
    private String detailId;
    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getChangeNumber() {
        return changeNumber;
    }

    public void setChangeNumber(Integer changeNumber) {
        this.changeNumber = changeNumber;
    }

    public String getWhareHouseId() {
        return whareHouseId;
    }

    public void setWhareHouseId(String whareHouseId) {
        this.whareHouseId = whareHouseId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Integer getIsTicket() {
        return isTicket;
    }

    public void setIsTicket(Integer isTicket) {
        this.isTicket = isTicket;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLmStockId() {
        return lmStockId;
    }

    public void setLmStockId(String lmStockId) {
        this.lmStockId = lmStockId;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
}
