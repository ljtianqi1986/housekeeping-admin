package com.biz.model.Hmodel.basic;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
@Entity
@Table(name="wx_goods_tag"
)
public class Ttag implements java.io.Serializable{
  private String id;
  private String name;
  private Integer state=0;
  private Date createTime=new Date();
  private Integer isdel=0;
  private String shopid;
  private String note;
    private  Integer num=0;
    private String icon;
     @GenericGenerator(name="generator", strategy="uuid.hex")@Id @GeneratedValue(generator="generator")

      @Column(name="id", unique=true, nullable=false, length=40)

  public String getId() {
      return id;
  }
  public void setId(String id) {
      this.id = id;
  }
  @Column(name="name")
  public String getName() {
      return name;
  }
  public void setName(String name) {
      this.name = name;
  }
  @Column(name="state")
  public Integer getState() {
      return state;
  }
  public void setState(Integer state) {
      this.state = state;
  }
  @Column(name="createTime")
  public Date getCreateTime() {
      return createTime;
  }
  public void setCreateTime(Date createTime) {
      this.createTime = createTime;
  }
  @Column(name="isdel")
  public Integer getIsdel() {
      return isdel;
  }
  public void setIsdel(Integer isdel) {
      this.isdel = isdel;
  }
  @Column(name="shopid")
  public String getShopid() {
      return shopid;
  }
  public void setShopid(String shopid) {
      this.shopid = shopid;
  }
  @Column(name="note")
  public String getNote() {
      return note;
  }
  public void setNote(String note) {
      this.note = note;
  }
    @Column(name="num")
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Column(name="icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Ttag(String id, String name, Integer state, Date createTime,
                Integer isdel, String shopid, String note) {
      super();
      this.id = id;
      this.name = name;
      this.state = state;
      this.createTime = createTime;
      this.isdel = isdel;
      this.shopid = shopid;
      this.note = note;
  }
  public Ttag() {
      super();
  }


}
