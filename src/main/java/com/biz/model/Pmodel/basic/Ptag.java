package com.biz.model.Pmodel.basic;


import java.util.Date;

/*************************************************************************
* 版本：         V1.0
*
* 文件名称 ：Ttag.java 描述说明 ：
*
* 创建信息 : create by 曹凯 on 2016-8-24 上午8:53:58  修订信息 : modify by ( ) on (date) for ( )
*
* 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
**************************************************************************/

public class Ptag {
  private String id;
  private String id2;
  private String name;
  private Integer state=0;
  private Date createTime=new Date();
  private Integer isdel=0;
  private String shopid;
  private String note;
  private String goodsCount;
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
  public String getId2() {
      return id2;
  }
  public void setId2(String id2) {
      this.id2 = id2;
  }


}
