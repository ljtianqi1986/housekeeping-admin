package com.biz.model.Pmodel;

import java.util.Date;
import java.util.List;

public class PwxGood {

	private String id;						
	private String name;				//商品名称
	private Integer isSale;				//0:下架1:上架
	private Integer sales;				//总销量
	private Integer isdel;
	private Date createTime = new Date();
	private String shopid;				//供应商ID
	private String coverId;				//封面图Id,base_pics表id
	private String unitId;				//单位表主键
	private String brand;				//品牌
	private String group;				//分组id
	private String tag;					//标签
	private String supplierName;	//供应商名称
	private String unitName;			//单位名称
	private String allianceName;	//品牌名称
	private String groupName;		//分组名称
	private String tagName;			//标签名称
	private String path;					
	private String coverPath;			//封面图片的路径
	private String brandName;		//品牌名称
	private String brandPath;		//品牌图片路径
	private String groupPath;		//分组图片路径
	
	private String state;
	private String note;
	private String icon;
	private String num;
	private String type;
	private String pid;
private String goodsIdSup;
	private Integer nowStock;		//当前总库存，包含了该商品下所有规格的库存总和

	private List<PwxGoodsStock> stocklist;		//库存集合
	private List<PwxGoodsStock> stockParList;		//父级规格列表
	private List<PwxGoodsStock> coverList;

	public String getCoverPath() {
		return coverPath;
	}

	public void setCoverPath(String coverPath) {
		this.coverPath = coverPath;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBrandPath() {
		return brandPath;
	}

	public void setBrandPath(String brandPath) {
		this.brandPath = brandPath;
	}

	public String getGroupPath() {
		return groupPath;
	}

	public void setGroupPath(String groupPath) {
		this.groupPath = groupPath;
	}

	public List<PwxGoodsStock> getStocklist() {
		return stocklist;
	}

	public void setStocklist(List<PwxGoodsStock> stocklist) {
		this.stocklist = stocklist;
	}

	public Integer getNowStock() {
		return nowStock;
	}

	public void setNowStock(Integer nowStock) {
		this.nowStock = nowStock;
	}

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

	public Integer getIsSale() {
		return isSale;
	}

	public void setIsSale(Integer isSale) {
		this.isSale = isSale;
	}

	public Integer getSales() {
		return sales;
	}

	public void setSales(Integer sales) {
		this.sales = sales;
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

	public String getShopid() {
		return shopid;
	}

	public void setShopid(String shopid) {
		this.shopid = shopid;
	}

	public String getCoverId() {
		return coverId;
	}

	public void setCoverId(String coverId) {
		this.coverId = coverId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getAllianceName() {
		return allianceName;
	}

	public void setAllianceName(String allianceName) {
		this.allianceName = allianceName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public List<PwxGoodsStock> getStockParList() {
		return stockParList;
	}

	public void setStockParList(List<PwxGoodsStock> stockParList) {
		this.stockParList = stockParList;
	}

	public List<PwxGoodsStock> getCoverList() {
		return coverList;
	}

	public void setCoverList(List<PwxGoodsStock> coverList) {
		this.coverList = coverList;
	}

	public String getGoodsIdSup() {
		return goodsIdSup;
	}

	public void setGoodsIdSup(String goodsIdSup) {
		this.goodsIdSup = goodsIdSup;
	}
}
