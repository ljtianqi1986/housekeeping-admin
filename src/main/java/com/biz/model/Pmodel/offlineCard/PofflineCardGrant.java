package com.biz.model.Pmodel.offlineCard;
// default package


/**
 * TUser entity. @author MyEclipse Persistence Tools
 */
//offline_card_90 主表
public class PofflineCardGrant implements java.io.Serializable {

    private String id;
    private String brandId; //商户ID
    private String detailId;//实体卡ID
    private String typeId;//卡券类型ID
    private Integer isdel = 0;
    private String createTime;
    private String userId;
    private String cardNotes="";
    private String bizPersonId="";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCardNotes() {
        return cardNotes;
    }

    public void setCardNotes(String cardNotes) {
        this.cardNotes = cardNotes;
    }

    public String getBizPersonId() {
        return bizPersonId;
    }

    public void setBizPersonId(String bizPersonId) {
        this.bizPersonId = bizPersonId;
    }
}