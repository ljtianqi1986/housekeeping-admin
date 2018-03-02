package com.biz.model.Pmodel.offlineCard;
// default package


/**
 * TUser entity. @author MyEclipse Persistence Tools
 */
//offline_card_90 主表
public class PofflineCardDetail implements java.io.Serializable {

    private String id;
    private String mainId;//主表id
    private String cardCode; //实体卡密钥
    private String userId; //使用人Id
    private String cardNumber; //实体卡卡号
    private String openId;//使用人openId
    private Integer state = 0;//0:待充值1:已充值
    private String useTime; //充值时间
    private String createTime;//创建时间
    private String isGrand;//是否发放 0:已发放1:未发放
    private String brandName; //发放商户
    private String person_name; //领取人

    //拓展
    private String typeName;//类型

    private String cardNotes;
    private String cardTotal;

    private String bizPersonName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIsGrand() {
        return isGrand;
    }

    public void setIsGrand(String isGrand) {
        this.isGrand = isGrand;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCardNotes() {
        return cardNotes;
    }

    public void setCardNotes(String cardNotes) {
        this.cardNotes = cardNotes;
    }

    public String getCardTotal() {
        return cardTotal;
    }

    public void setCardTotal(String cardTotal) {
        this.cardTotal = cardTotal;
    }

    public String getBizPersonName() {
        return bizPersonName;
    }

    public void setBizPersonName(String bizPersonName) {
        this.bizPersonName = bizPersonName;
    }
}