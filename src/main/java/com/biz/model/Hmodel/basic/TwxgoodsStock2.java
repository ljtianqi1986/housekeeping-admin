package com.biz.model.Hmodel.basic;
// default package

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
 * TwxgoodsStock entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="base_wxgoods_stock"
)

public class TwxgoodsStock2 implements java.io.Serializable {


    // Fields

     /**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String id;
     private String goodsId;
     private String typesId1;
     private String typesId2;
     private String typesId3;
     private Double price;
     private Integer stock;
     private String venderId;
     private Short isdel;
     private Date createTime;
     private Integer stockOccupy;
     private Integer stockNow;
     private Integer saleCount;
    private String skuCode;
    private String skuId;
    // Constructors

    /** default constructor */
    public TwxgoodsStock2() {
    }


    // Property accessors
    @Id
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

    @Column(name="typesId1")
    public String getTypesId1() {
        return typesId1;
    }

    public void setTypesId1(String typesId1) {
        this.typesId1 = typesId1;
    }

    @Column(name="typesId2")
    public String getTypesId2() {
        return typesId2;
    }

    public void setTypesId2(String typesId2) {
        this.typesId2 = typesId2;
    }

    @Column(name="typesId3")
    public String getTypesId3() {
        return typesId3;
    }

    public void setTypesId3(String typesId3) {
        this.typesId3 = typesId3;
    }

    @Column(name="price", precision=10)

    public Double getPrice() {
        return this.price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    @Column(name="stock")

    public Integer getStock() {
        return this.stock;
    }
    
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    
    @Column(name="venderId")

    public String getVenderId() {
        return this.venderId;
    }
    
    public void setVenderId(String venderId) {
        this.venderId = venderId;
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

    @Column(name="stockOccupy")
	public Integer getStockOccupy() {
		return stockOccupy;
	}


	public void setStockOccupy(Integer stockOccupy) {
		this.stockOccupy = stockOccupy;
	}

	@Column(name="stockNow")
	public Integer getStockNow() {
		return stockNow;
	}


	public void setStockNow(Integer stockNow) {
		this.stockNow = stockNow;
	}

	@Column(name="saleCount")
	public Integer getSaleCount() {
		return saleCount;
	}


	public void setSaleCount(Integer saleCount) {
		this.saleCount = saleCount;
	}
    @Column(name="skuCode")
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }
    @Column(name="skuId")
    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }
}