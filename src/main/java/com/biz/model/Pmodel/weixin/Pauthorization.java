package com.biz.model.Pmodel.weixin;



/**
 * TUser entity. @author MyEclipse Persistence Tools
 */
//offline_card_90 主表
public class Pauthorization {

    private String id;
    private String text;
    private String createTime;//获取时间时间戳
    private String end_time="0";//有效时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}