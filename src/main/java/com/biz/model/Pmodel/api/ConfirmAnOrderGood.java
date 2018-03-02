package com.biz.model.Pmodel.api;
/**
 * 商品信息实体类
 * @author 孙韩越
 *
 */
public class ConfirmAnOrderGood {
	
	private String goodsId;
	private String shopId;
	private String stockId;//sku id 库存表id base_wxgoods_stock id
	private String typesId1;
	private String typesId2;
	private String typesId3;
	private String isSale;//0:下架1:上架
	private int count=0;//购买数量
	private double price=0.0;//单价
	private int stockNow=0;//当前库存
	private String isOpen;//0:未营业1:营业
	private int isTicket=0;//0 普通商品，1卡券商品
	
	private String usemodel;//1:统一运费,2模板运费
	private double freightmoney=0.0;//统一运费
	private String freightstate;//运费是否叠加(0:不叠加1:叠加)
	
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public String getIsSale() {
		return isSale;
	}
	public void setIsSale(String isSale) {
		this.isSale = isSale;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getStockNow() {
		return stockNow;
	}
	public void setStockNow(int stockNow) {
		this.stockNow = stockNow;
	}
	public String getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}
	public int getIsTicket() {
		return isTicket;
	}
	public void setIsTicket(int isTicket) {
		this.isTicket = isTicket;
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
	public String getUsemodel() {
		return usemodel;
	}
	public void setUsemodel(String usemodel) {
		this.usemodel = usemodel;
	}
	public double getFreightmoney() {
		return freightmoney;
	}
	public void setFreightmoney(double freightmoney) {
		this.freightmoney = freightmoney;
	}
	public String getFreightstate() {
		return freightstate;
	}
	public void setFreightstate(String freightstate) {
		this.freightstate = freightstate;
	}
	
	
	

}
