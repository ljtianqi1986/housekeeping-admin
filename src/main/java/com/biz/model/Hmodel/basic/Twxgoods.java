package com.biz.model.Hmodel.basic;
// default package

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
 * Twxgoods entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="base_wxgoods"
)

public class Twxgoods implements java.io.Serializable {


    // Fields    

     private String id;
    private String code;
     private String name;
     //private String category;
     //private String groupId;
     private String info;
     private Short discount=0;
     private Short isSale=0;
     //private Integer stockTotal;
     private Integer sales=0;
     private Short isdel=0;
     private Date createTime=new Date();
     private String freight;
     private String subtitle;
     private Date sendTime;
     private Integer countLimit=0;
     private String memberLimit;
     private Integer isPromptly=0;
     private Date saleTime;

    private String venderIdMain;
    private String infoLite;
    private String buyerId;
    private String unitId;
    private String brand;
    private String venderIdName;
    private String venderAddr;
    private String goodsIdSup;
    // Constructors
    private Integer isTicket=0;
    private String categoryFirst;
    private String categorySecond;
    private String coverId;
    private Integer ticketType;
    private Date startTime;
    private Date endTime;
    /** default constructor */
    public Twxgoods() {
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
    
    @Column(name="name", length=50)

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    

    @Column(name="info", length=65535)

    public String getInfo() {
        return this.info;
    }
    
    public void setInfo(String info) {
        this.info = info;
    }
    
    @Column(name="discount")

    public Short getDiscount() {
        return this.discount;
    }
    
    public void setDiscount(Short discount) {
        this.discount = discount;
    }
    
    @Column(name="isSale")
    public Short getIsSale() {
		return isSale;
	}


	public void setIsSale(Short isSale) {
		this.isSale = isSale;
	}


	@Column(name="sales")
	public Integer getSales() {
		return sales;
	}


	public void setSales(Integer sales) {
		this.sales = sales;
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
	public String getFreight() {
		return freight;
	}


	public void setFreight(String freight) {
		this.freight = freight;
	}

    @Column(name="subtitle", length=255)
    
	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	@Column(name="sendTime", length=19)
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	@Column(name="countLimit", length=19)
	public Integer getCountLimit() {
		return countLimit;
	}


	public void setCountLimit(Integer countLimit) {
		this.countLimit = countLimit;
	}

	@Column(name="memberLimit", length=19)
	public String getMemberLimit() {
		return memberLimit;
	}


	public void setMemberLimit(String memberLimit) {
		this.memberLimit = memberLimit;
	}

	@Column(name="isPromptly", length=19)
	public Integer getIsPromptly() {
		return isPromptly;
	}


	public void setIsPromptly(Integer isPromptly) {
		this.isPromptly = isPromptly;
	}

	@Column(name="saleTime", length=19)
	public Date getSaleTime() {
		return saleTime;
	}


	public void setSaleTime(Date saleTime) {
		this.saleTime = saleTime;
	}

    @Column(name = "infolite", length = 50)
    public String getInfoLite() {
        return infoLite;
    }

    public void setInfoLite(String infoLite) {
        this.infoLite = infoLite;
    }

    @Column(name = "venderId", length = 50)
    public String getVenderIdMain() {
        return venderIdMain;
    }

    public void setVenderIdMain(String venderIdMain) {
        this.venderIdMain = venderIdMain;
    }

    @Column(name = "shopid", length = 50)
    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }
    
    @Column(name = "unitId", length = 50)
    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
    @Column(name = "brand", length = 50)
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Column(name = "venderIdName", length = 50)
    public String getVenderIdName() {
        return venderIdName;
    }

    public void setVenderIdName(String venderIdName) {
        this.venderIdName = venderIdName;
    }

    @Column(name = "venderAddr", length = 50)
    public String getVenderAddr() {
        return venderAddr;
    }

    public void setVenderAddr(String venderAddr) {
        this.venderAddr = venderAddr;
    }

    @Column(name = "isTicket", length = 50)
    public Integer getIsTicket() {
        return isTicket;
    }

    public void setIsTicket(Integer isTicket) {
        this.isTicket = isTicket;
    }

    @Column(name = "goodsIdSup", length = 50)
    public String getGoodsIdSup() {
        return goodsIdSup;
    }

    public void setGoodsIdSup(String goodsIdSup) {
        this.goodsIdSup = goodsIdSup;
    }

    public String getCategoryFirst() {
        return categoryFirst;
    }

    public void setCategoryFirst(String categoryFirst) {
        this.categoryFirst = categoryFirst;
    }

    public String getCategorySecond() {
        return categorySecond;
    }

    public void setCategorySecond(String categorySecond) {
        this.categorySecond = categorySecond;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "coverId", length = 50)
    public String getCoverId() {
        return coverId;
    }

    public void setCoverId(String coverId) {
        this.coverId = coverId;
    }

    @Column(name = "ticketType")
    public Integer getTicketType() {
        return ticketType;
    }

    public void setTicketType(Integer ticketType) {
        this.ticketType = ticketType;
    }
    @Column(name = "startTime")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    @Column(name = "endTime")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}