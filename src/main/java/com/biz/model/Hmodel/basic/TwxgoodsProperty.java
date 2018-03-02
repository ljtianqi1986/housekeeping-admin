package com.biz.model.Hmodel.basic;
// default package

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
 * TwxgoodsProperty entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="base_wxgoods_property"
)

public class TwxgoodsProperty implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
     private String goodsId;
     private String property;
     private String picId;
     private Short type;
     private Short isdel;
     private Date createTime;


    // Constructors

    /** default constructor */
    public TwxgoodsProperty() {
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
    
    @Column(name="goodsId", length=50)

    public String getGoodsId() {
        return this.goodsId;
    }
    
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
    
    @Column(name="property")

    public String getProperty() {
        return this.property;
    }
    
    public void setProperty(String property) {
        this.property = property;
    }
    
    @Column(name="picId", length=50)

    public String getPicId() {
        return this.picId;
    }
    
    public void setPicId(String picId) {
        this.picId = picId;
    }
    
    @Column(name="type")

    public Short getType() {
        return this.type;
    }

    public void setType(Short type) {
        this.type = type;
    }
    
    @Column(name="isdel")

    public Short getIsdel() {
        return this.isdel;
    }
    
    public void setIsdel(Short isdel) {
        this.isdel = isdel;
    }
    
    @Column(name="createTime", length=19)

    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
   








}