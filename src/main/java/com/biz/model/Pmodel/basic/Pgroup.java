package com.biz.model.Pmodel.basic;


import java.util.Date;

/*************************************************************************
* 版本：         V1.0
*
* 文件名称 ：Tgroup.java 描述说明 ：
*
* 创建信息 : create by 曹凯 on 2016-8-24 上午8:50:48  修订信息 : modify by ( ) on (date) for ( )
*
* 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
**************************************************************************/

public class Pgroup {
  private String id;
  private String name;
  private Integer state=0;
  private Date createTime=new Date();
  private Integer isdel=0;
  private String shopid;
  private String htmlTag;
  private String note;
  private String goodsCount;
  private String icon;
  private String path;
  private Integer isTicket=0;
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
  public Integer getState() {
      return state;
  }
  public void setState(Integer state) {
      this.state = state;
  }
  public Date getCreateTime() {
      return createTime;
  }
  public void setCreateTime(Date createTime) {
      this.createTime = createTime;
  }
  public Integer getIsdel() {
      return isdel;
  }
  public void setIsdel(Integer isdel) {
      this.isdel = isdel;
  }
  public String getShopid() {
      return shopid;
  }
  public void setShopid(String shopid) {
      this.shopid = shopid;
  }
  public String getHtmlTag() {
      return htmlTag;
  }
  public void setHtmlTag(String htmlTag) {
      this.htmlTag = htmlTag;
  }
  public String getNote() {
      return note;
  }
  public void setNote(String note) {
      this.note = note;
  }
  public String getGoodsCount() {
      return goodsCount;
  }
  public void setGoodsCount(String goodsCount) {
      this.goodsCount = goodsCount;
  }
  public String getIcon() {
      return icon;
  }
  public void setIcon(String icon) {
      this.icon = icon;
  }
  public String getPath() {
      return path;
  }
  public void setPath(String path) {
      this.path = path;
  }
  public Integer getIsTicket() {
      return isTicket;
  }
  public void setIsTicket(Integer isTicket) {
      this.isTicket = isTicket;
  }


}
