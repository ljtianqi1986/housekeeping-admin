package com.biz.model.Hmodel.basic;
// default package

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
 * TwxGoodsGroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="wx_goods_group")

public class TwxGoodsGroup implements java.io.Serializable {


    // Fields    

     private String id;
     private String name;
     private Short state;
     private Date createTime;
     private Short isdel;
    private String shopid;
    private String icon;
    private String note;
    private Short isTicket;
    private String customImg;
    private int isCustom;
    private int sort;

    // Constructors

    /** default constructor */
    public TwxGoodsGroup() {
    }

	/** minimal constructor */
    public TwxGoodsGroup(String name) {
        this.name = name;
    }
    
    /** full constructor */
    public TwxGoodsGroup(String name, Short state, Date createTime, Short isdel) {
        this.name = name;
        this.state = state;
        this.createTime = createTime;
        this.isdel = isdel;
    }

   
    // Property accessors
    @GenericGenerator(name="generator", strategy="uuid.hex")@Id @GeneratedValue(generator="generator")
    
    @Column(name="id", unique=true, nullable=false, length=50)

    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name="name", nullable=false, length=50)

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name="state")

    public Short getState() {
        return this.state;
    }
    
    public void setState(Short state) {
        this.state = state;
    }
    
    @Column(name="createTime", length=19)

    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    @Column(name="isdel")

    public Short getIsdel() {
        return this.isdel;
    }
    
    public void setIsdel(Short isdel) {
        this.isdel = isdel;
    }

    @Column(name="shopid")
    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }
    @Column(name="icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Column(name="isTicket")
    public Short getIsTicket() {
        return isTicket;
    }

    public void setIsTicket(Short isTicket) {
        this.isTicket = isTicket;
    }

    @Column(name="note")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Column(name="customImg")
    public String getCustomImg() {
        return customImg;
    }

    public void setCustomImg(String customImg) {
        this.customImg = customImg;
    }

    @Column(name="isCustom")
    public int getIsCustom() {
        return isCustom;
    }

    public void setIsCustom(int isCustom) {
        this.isCustom = isCustom;
    }

    @Column(name="sort")
    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}