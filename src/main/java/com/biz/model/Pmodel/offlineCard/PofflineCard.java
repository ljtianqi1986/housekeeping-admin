package com.biz.model.Pmodel.offlineCard;
// default package


/**
 * TUser entity. @author MyEclipse Persistence Tools
 */
//offline_card_90 主表
public class PofflineCard implements java.io.Serializable {

    private String id;
    private String name;//标题
    private String agentId;//代理商编号
    private String userId; //userId
    private Integer cardTotal = 0;//90券额（分）
    private Integer cardCount = 0; //生成的90券的数量
    private Integer cardUseCount = 0; //已使用数量
    private Integer isdel = 0;//删除标志0：未删除，1：删除
    private Integer creatState = 0;//0.生成中1.已生成2.生成失败
    private Integer batch = 0;//实体卡序列号(防止卡密匙重复使用)
    private String createTime;//创建时间

    //拓展
    private String brandId;//商户id
    private String typeId;//类型id

    //发放备注
    private String card_notes = "";
    private String cardCodeType="";
    private String cardCode="";
    private String type="";
    private String bizPersonId="";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getCardTotal() {
        return cardTotal;
    }

    public void setCardTotal(Integer cardTotal) {
        this.cardTotal = cardTotal;
    }

    public Integer getCardCount() {
        return cardCount;
    }

    public void setCardCount(Integer cardCount) {
        this.cardCount = cardCount;
    }

    public Integer getCardUseCount() {
        return cardUseCount;
    }

    public void setCardUseCount(Integer cardUseCount) {
        this.cardUseCount = cardUseCount;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public Integer getCreatState() {
        return creatState;
    }

    public void setCreatState(Integer creatState) {
        this.creatState = creatState;
    }

    public Integer getBatch() {
        return batch;
    }

    public void setBatch(Integer batch) {
        this.batch = batch;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getCard_notes() {
        return card_notes;
    }

    public void setCard_notes(String card_notes) {
        this.card_notes = card_notes;
    }

    public String getCardCodeType() {
        return cardCodeType;
    }

    public void setCardCodeType(String cardCodeType) {
        this.cardCodeType = cardCodeType;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBizPersonId() {
        return bizPersonId;
    }

    public void setBizPersonId(String bizPersonId) {
        this.bizPersonId = bizPersonId;
    }
}