package com.biz.model.Pmodel.basic;


import java.util.List;

import java.util.Date;

public class Pwxgoods {
    private String id;
    private String name;
    private String brandName2;
    private String category;
    private String groupId;
    private String groupName;
    private String info;
    private Short isSale;
    private Integer stockTotal;
    private Integer sales;
    private String discount;
    private String isdel;
    private Date createTime;
    private String types;
    private String typesPic;
    private String buyTime;
    private String isVase;
    private String price;
    private String stock;
    private String venderId;
    private String goodsPicId;
    private String path;
	private String mainType;
    private String stockId;
	private String stockIds;
    private String stockNow;
    private String freight;
    private String subtitle;
    private String categoryName;
    private String sendTime;
    private Integer countLimit;
    private String memberLimit;
    private Integer isPromptly;
    private String saleTime;
private String goodsIdSup;
	private String venderIdMain;
	private String infoLite;
	private String buyerId;
	private String tags;
	private String tagName;
	private String unitId;
	private String unitName;
	private String brand;
    private String venderIdName;
private String brandName;
    private String venderAddr;
    private List<PwxgoodsStock> stocklist;
    private String categoryFirst;
    private String categorySecond;
    private String page_type;
	private String code;
	private String venderIdSup="";
	private String coverId;
	private String coverPath;
    private int ticketType;

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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getIsdel() {
		return isdel;
	}
	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	public String getTypesPic() {
		return typesPic;
	}
	public void setTypesPic(String typesPic) {
		this.typesPic = typesPic;
	}
	public String getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}
	public String getIsVase() {
		return isVase;
	}
	public void setIsVase(String isVase) {
		this.isVase = isVase;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getVenderId() {
		return venderId;
	}
	public void setVenderId(String venderId) {
		this.venderId = venderId;
	}
	public String getGoodsPicId() {
		return goodsPicId;
	}
	public void setGoodsPicId(String goodsPicId) {
		this.goodsPicId = goodsPicId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public Short getIsSale() {
		return isSale;
	}
	public void setIsSale(Short isSale) {
		this.isSale = isSale;
	}
	public Integer getStockTotal() {
		return stockTotal;
	}
	public void setStockTotal(Integer stockTotal) {
		this.stockTotal = stockTotal;
	}
	public Integer getSales() {
		return sales;
	}
	public void setSales(Integer sales) {
		this.sales = sales;
	}
	public String getStockNow() {
		return stockNow;
	}
	public void setStockNow(String stockNow) {
		this.stockNow = stockNow;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getFreight() {
		return freight;
	}
	public void setFreight(String freight) {
		this.freight = freight;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public Integer getCountLimit() {
		return countLimit;
	}
	public void setCountLimit(Integer countLimit) {
		this.countLimit = countLimit;
	}
	public String getMemberLimit() {
		return memberLimit;
	}
	public void setMemberLimit(String memberLimit) {
		this.memberLimit = memberLimit;
	}
	public Integer getIsPromptly() {
		return isPromptly;
	}
	public void setIsPromptly(Integer isPromptly) {
		this.isPromptly = isPromptly;
	}
	public String getSaleTime() {
		return saleTime;
	}
	public void setSaleTime(String saleTime) {
		this.saleTime = saleTime;
	}

	public String getVenderIdMain() {
		return venderIdMain;
	}

	public void setVenderIdMain(String venderIdMain) {
		this.venderIdMain = venderIdMain;
	}

	public String getInfoLite() {
		return infoLite;
	}

	public void setInfoLite(String infoLite) {
		this.infoLite = infoLite;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

    public String getVenderIdName() {
        return venderIdName;
    }

    public void setVenderIdName(String venderIdName) {
        this.venderIdName = venderIdName;
    }
	public String getMainType() {
		return mainType;
	}

	public void setMainType(String mainType) {
		this.mainType = mainType;
	}

    public String getVenderAddr() {
        return venderAddr;
    }

    public void setVenderAddr(String venderAddr) {
        this.venderAddr = venderAddr;
    }

    public String getBrandName2() {
        return brandName2;
    }

    public void setBrandName2(String brandName2) {
        this.brandName2 = brandName2;
    }

	public String getStockIds() {
		return stockIds;
	}

	public void setStockIds(String stockIds) {
		this.stockIds = stockIds;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getGoodsIdSup() {
		return goodsIdSup;
	}

	public void setGoodsIdSup(String goodsIdSup) {
		this.goodsIdSup = goodsIdSup;
	}

	public List<PwxgoodsStock> getStocklist() {
		return stocklist;
	}

	public void setStocklist(List<PwxgoodsStock> stocklist) {
		this.stocklist = stocklist;
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

	public String getPage_type() {
		return page_type;
	}

	public void setPage_type(String page_type) {
		this.page_type = page_type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getVenderIdSup() {
		return venderIdSup;
	}

	public void setVenderIdSup(String venderIdSup) {
		this.venderIdSup = venderIdSup;
	}

	public String getCoverId() {
		return coverId;
	}

	public void setCoverId(String coverId) {
		this.coverId = coverId;
	}

	public String getCoverPath() {
		return coverPath;
	}

	public void setCoverPath(String coverPath) {
		this.coverPath = coverPath;
	}

    public int getTicketType() {
        return ticketType;
    }

    public void setTicketType(int ticketType) {
        this.ticketType = ticketType;
    }
}
