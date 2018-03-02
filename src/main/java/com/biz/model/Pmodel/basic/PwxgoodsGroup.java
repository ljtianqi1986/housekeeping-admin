package com.biz.model.Pmodel.basic;

import java.util.Date;

/**
 * Created by tomchen on 17/3/17.
 */
public class PwxgoodsGroup {

    private String id;
    private String name;
    private String state;
    private Date createTime;
    private short isdel;
    private String shopId;
    private String note;
    private String icon;
    private short isTicket;

    private String tags;
    private String path;
    private String defImgUrl;
    private String customImgUrl;
    private String isCustom;
    private int sort;
    private String customImg;

    public PwxgoodsGroup() {
    }

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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public short getIsdel() {
        return isdel;
    }

    public void setIsdel(short isdel) {
        this.isdel = isdel;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public short getIsTicket() {
        return isTicket;
    }

    public void setIsTicket(short isTicket) {
        this.isTicket = isTicket;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDefImgUrl() {
        return defImgUrl;
    }

    public void setDefImgUrl(String defImgUrl) {
        this.defImgUrl = defImgUrl;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getCustomImgUrl() {
        return customImgUrl;
    }

    public void setCustomImgUrl(String customImgUrl) {
        this.customImgUrl = customImgUrl;
    }

    public String getIsCustom() {
        return isCustom;
    }

    public void setIsCustom(String isCustom) {
        this.isCustom = isCustom;
    }

    public String getCustomImg() {
        return customImg;
    }

    public void setCustomImg(String customImg) {
        this.customImg = customImg;
    }
}
