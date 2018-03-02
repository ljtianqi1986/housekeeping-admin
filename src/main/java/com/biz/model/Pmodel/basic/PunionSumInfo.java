package com.biz.model.Pmodel.basic;

/**
 * Created by lzq.
 */

public class PunionSumInfo {
    private String id = "";
    private double cashTotal = 0.00;
    private double coinTotal = 0.00;
    private double cashBack = 0.00;
    private double coinBack = 0.00;
    private int orderCount = 0;
    private int backCount = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getCashTotal() {
        return cashTotal;
    }

    public void setCashTotal(double cashTotal) {
        this.cashTotal = cashTotal;
    }

    public double getCoinTotal() {
        return coinTotal;
    }

    public void setCoinTotal(double coinTotal) {
        this.coinTotal = coinTotal;
    }

    public double getCashBack() {
        return cashBack;
    }

    public void setCashBack(double cashBack) {
        this.cashBack = cashBack;
    }

    public double getCoinBack() {
        return coinBack;
    }

    public void setCoinBack(double coinBack) {
        this.coinBack = coinBack;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getBackCount() {
        return backCount;
    }

    public void setBackCount(int backCount) {
        this.backCount = backCount;
    }
}
