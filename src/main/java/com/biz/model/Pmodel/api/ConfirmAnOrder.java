package com.biz.model.Pmodel.api;

import java.util.List;

/**
 * Created by ldd_person on 2017/2/7.
 */
public class ConfirmAnOrder {
    private String userId;//购买人
    private String openid;//购买人openid

    private String addressId;//配送地址
    private String giveType;//0送自己，1送他人
    private String giveMsg;//特殊要求
    private String givePhone;//送他人留下自己的联系方式
    private String message;//贺卡留言，不启用则空
    private String DiscountState;//0不启用积分抵用，1启用
    private String shopId;//店铺id
    private double payTotal=0.0;//支付总额(元)
    private double couponsPayTotal=0.0;//支付总额(点券)
    private List<ConfirmAnOrderCard> CardList;//卡券列表
    private List<ConfirmAnOrderGood> GoodsList;//商品列表

    private String usemodel;//smallint  1:统一运费,2模板运费
    private String freightstate;//smallint  运费是否叠加(0:不叠加1:叠加)
    private double freight=0.0;//运费
    private String purchasingId;//导购员id

    public ConfirmAnOrder() {
        super();
    }

    public ConfirmAnOrder(String userId, String openid, String addressId, String giveType, String giveMsg, String givePhone, String message, String discountState, String shopId, double payTotal, double couponsPayTotal, List<ConfirmAnOrderCard> cardList, List<ConfirmAnOrderGood> goodsList, String usemodel, String freightstate, double freight, String purchasingId) {
        this.userId = userId;
        this.openid = openid;
        this.addressId = addressId;
        this.giveType = giveType;
        this.giveMsg = giveMsg;
        this.givePhone = givePhone;
        this.message = message;
        DiscountState = discountState;
        this.shopId = shopId;
        this.payTotal = payTotal;
        this.couponsPayTotal = couponsPayTotal;
        CardList = cardList;
        GoodsList = goodsList;
        this.usemodel = usemodel;
        this.freightstate = freightstate;
        this.freight = freight;
        this.purchasingId = purchasingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getGiveType() {
        return giveType;
    }

    public void setGiveType(String giveType) {
        this.giveType = giveType;
    }

    public String getGiveMsg() {
        return giveMsg;
    }

    public void setGiveMsg(String giveMsg) {
        this.giveMsg = giveMsg;
    }

    public String getGivePhone() {
        return givePhone;
    }

    public void setGivePhone(String givePhone) {
        this.givePhone = givePhone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDiscountState() {
        return DiscountState;
    }

    public void setDiscountState(String discountState) {
        DiscountState = discountState;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public double getPayTotal() {
        return payTotal;
    }

    public void setPayTotal(double payTotal) {
        this.payTotal = payTotal;
    }

    public double getCouponsPayTotal() {
        return couponsPayTotal;
    }

    public void setCouponsPayTotal(double couponsPayTotal) {
        this.couponsPayTotal = couponsPayTotal;
    }

    public List<ConfirmAnOrderCard> getCardList() {
        return CardList;
    }

    public void setCardList(List<ConfirmAnOrderCard> cardList) {
        CardList = cardList;
    }

    public List<ConfirmAnOrderGood> getGoodsList() {
        return GoodsList;
    }

    public void setGoodsList(List<ConfirmAnOrderGood> goodsList) {
        GoodsList = goodsList;
    }

    public String getUsemodel() {
        return usemodel;
    }

    public void setUsemodel(String usemodel) {
        this.usemodel = usemodel;
    }

    public String getFreightstate() {
        return freightstate;
    }

    public void setFreightstate(String freightstate) {
        this.freightstate = freightstate;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public String getPurchasingId() {
        return purchasingId;
    }

    public void setPurchasingId(String purchasingId) {
        this.purchasingId = purchasingId;
    }
}
