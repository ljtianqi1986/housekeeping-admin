package com.biz.model.Pmodel.QT;

/**
 * Created by lzq on 2017/2/22.
 */
public class PgoodsDetail {
    private String goodsId = "";
    private String goodsStockId = "";
    private String goodsName = "";
    private String goodsStockName = "";
    private String goodsUnit = "";
    private String goodsPrice = "";
    private String goodsCount = "";
    private String goodsPriceThis = "";
    private String onlyCode="";
    private String whareHouseId;


    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsStockId() {
        return goodsStockId;
    }

    public void setGoodsStockId(String goodsStockId) {
        this.goodsStockId = goodsStockId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsStockName() {
        return goodsStockName;
    }

    public void setGoodsStockName(String goodsStockName) {
        this.goodsStockName = goodsStockName;
    }

    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(String goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getGoodsPriceThis() {
        return goodsPriceThis;
    }

    public void setGoodsPriceThis(String goodsPriceThis) {
        this.goodsPriceThis = goodsPriceThis;
    }

    public String getWhareHouseId() {
        return whareHouseId;
    }

    public void setWhareHouseId(String whareHouseId) {
        this.whareHouseId = whareHouseId;
    }

    @Override
    public String toString() {
        return "PgoodsDetail{" +
                "goodsId='" + goodsId + '\'' +
                ", goodsStockId='" + goodsStockId + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsStockName='" + goodsStockName + '\'' +
                ", goodsUnit='" + goodsUnit + '\'' +
                ", goodsPrice='" + goodsPrice + '\'' +
                ", goodsCount='" + goodsCount + '\'' +
                ", goodsPriceThis='" + goodsPriceThis + '\'' +
                '}';
    }

    public String getOnlyCode() {
        return onlyCode;
    }

    public void setOnlyCode(String onlyCode) {
        this.onlyCode = onlyCode;
    }
}
