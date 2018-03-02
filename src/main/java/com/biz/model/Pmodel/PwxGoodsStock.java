package com.biz.model.Pmodel;


import java.util.Date;


public class PwxGoodsStock {

	
	private String id;
	private String name;
	private String goodsId;							//商品ID
	private String typesId1;							//规格1ID
	private String typesId2;							//规格2ID
	private String typesId3;							//规格3ID
	private Double price;								//供货价（即采购价，入库价）
	private Double priceMoney;								//现金价
	private Double priceJiuling;						//销售给久零的价格（出库价）
	private Double priceDiaopai;					//吊牌价（建议零售价）
	private Integer stock;								//库存
	private String venderId;							//商家编码
	private Integer isdel;
	private Date createTime=new Date();
	private Integer saleCount;						//销量
	private String skuCode;							//sku扫码编码，看数据库与商家编码字段一致
	
	private String standard1Name;				//规格1名称
	private String standard1Pname;				//规格1的父级规格名称
	private String standard1Pid;					//规格1的父级规格ID
	private String standard2Name;				//规格2名称
	private String standard2Pname;				//规格2的父级规格名称
	private String standard2Pid;					//规格2的父级规格ID
	private String standard3Name;				//规格3名称
	private String standard3Pname;				//规格3的父级规格名称
	private String standard3Pid;					//规格3的父级规格ID
	private String coverId;								//图片Id
	private String coverPath;							//图片Path
	
	
	
	public String getStandard1Name() {
		return standard1Name;
	}
	public void setStandard1Name(String standard1Name) {
		this.standard1Name = standard1Name;
	}
	public String getStandard1Pname() {
		return standard1Pname;
	}
	public void setStandard1Pname(String standard1Pname) {
		this.standard1Pname = standard1Pname;
	}
	public String getStandard1Pid() {
		return standard1Pid;
	}
	public void setStandard1Pid(String standard1Pid) {
		this.standard1Pid = standard1Pid;
	}
	public String getStandard2Name() {
		return standard2Name;
	}
	public void setStandard2Name(String standard2Name) {
		this.standard2Name = standard2Name;
	}
	public String getStandard2Pname() {
		return standard2Pname;
	}
	public void setStandard2Pname(String standard2Pname) {
		this.standard2Pname = standard2Pname;
	}
	public String getStandard2Pid() {
		return standard2Pid;
	}
	public void setStandard2Pid(String standard2Pid) {
		this.standard2Pid = standard2Pid;
	}
	public String getStandard3Name() {
		return standard3Name;
	}
	public void setStandard3Name(String standard3Name) {
		this.standard3Name = standard3Name;
	}
	public String getStandard3Pname() {
		return standard3Pname;
	}
	public void setStandard3Pname(String standard3Pname) {
		this.standard3Pname = standard3Pname;
	}
	public String getStandard3Pid() {
		return standard3Pid;
	}
	public void setStandard3Pid(String standard3Pid) {
		this.standard3Pid = standard3Pid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getTypesId1() {
		return typesId1;
	}
	public void setTypesId1(String typesId1) {
		this.typesId1 = typesId1;
	}
	public String getTypesId2() {
		return typesId2;
	}
	public void setTypesId2(String typesId2) {
		this.typesId2 = typesId2;
	}
	public String getTypesId3() {
		return typesId3;
	}
	public void setTypesId3(String typesId3) {
		this.typesId3 = typesId3;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getPriceJiuling() {
		return priceJiuling;
	}
	public void setPriceJiuling(Double priceJiuling) {
		this.priceJiuling = priceJiuling;
	}
	public Double getPriceDiaopai() {
		return priceDiaopai;
	}
	public void setPriceDiaopai(Double priceDiaopai) {
		this.priceDiaopai = priceDiaopai;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public String getVenderId() {
		return venderId;
	}
	public void setVenderId(String venderId) {
		this.venderId = venderId;
	}
	public Integer getIsdel() {
		return isdel;
	}
	public void setIsdel(Integer isdel) {
		this.isdel = isdel;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getSaleCount() {
		return saleCount;
	}
	public void setSaleCount(Integer saleCount) {
		this.saleCount = saleCount;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Double getPriceMoney() {
		return priceMoney;
	}

	public void setPriceMoney(Double priceMoney) {
		this.priceMoney = priceMoney;
	}
}
