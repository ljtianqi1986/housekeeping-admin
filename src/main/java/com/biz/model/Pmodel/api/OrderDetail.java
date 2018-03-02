package com.biz.model.Pmodel.api;

public class OrderDetail {

	private String id;//	varchar
	private String orderId;//	varchar	订单总表
	private String goodsId;//	varchar	商品编码
	private double price=0;//	decimal	价格
	private int count=0;//	int	数量
	private double goodsTotal=0;//	decimal	商品总金额
	private double integralTotal=0;//	decimal	积分抵扣合计
	private double couponTotal=0;//	decimal	优惠券对应比例费用(只抵商品物流除外)
	private double payTotal=0;//	decimal	按比例需要额外支付的费用(抵去抵用券)
	private String sendId;//	varchar	物流对应id
	private String shopId;//varchar	仓库ID
	private String state;//	smallint	0:待付款1:待发货2:待收货3:待评价4:完成5:退款中6:已退款7:待备货8:未备货取消
	private String buyNotes;//	varchar	购买备注
	private String incom;//	int	来源（0：普通店家 1:闪电发货）
	private String isdel;
	private String arrivalTime;//	datetime
	private String confirmTimes;//	datetime	该笔商品确认收货时间
	private String confirmIncom;//	int	0:微信 1：定时器自动 2：售后收货3：android 4：ios
	private String freightid;//  varchar  模版id
	private String typesId1;//   规格
	private String typesId2;//   规格
	private String typesId3;//   规格
	private String stockId;//   规格
	private int goodsType=0;
	private double couponsPayTotal=0;//decimal  按比例需要额外支付的费用(抵去抵用券)
	private double balance_90 = 0.0;
	private double balance_shopping_90 = 0.0;
	private double balance_experience_90 = 0.0;
	private String isPay90="";
	private String isPayShopping = "";
	private String isPayExperience = "";
	private String ticketType="";
	private int isPayCoupons=0;
	private String startTime = "";
	private String endTime = "";

	private String coinPayTotal;
	private String servicePayTotal;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getGoodsTotal() {
		return goodsTotal;
	}
	public void setGoodsTotal(double goodsTotal) {
		this.goodsTotal = goodsTotal;
	}
	public double getIntegralTotal() {
		return integralTotal;
	}
	public void setIntegralTotal(double integralTotal) {
		this.integralTotal = integralTotal;
	}
	public double getCouponTotal() {
		return couponTotal;
	}
	public void setCouponTotal(double couponTotal) {
		this.couponTotal = couponTotal;
	}
	public double getPayTotal() {
		return payTotal;
	}
	public void setPayTotal(double payTotal) {
		this.payTotal = payTotal;
	}
	public String getSendId() {
		return sendId;
	}
	public void setSendId(String sendId) {
		this.sendId = sendId;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getBuyNotes() {
		return buyNotes;
	}
	public void setBuyNotes(String buyNotes) {
		this.buyNotes = buyNotes;
	}
	public String getIncom() {
		return incom;
	}
	public void setIncom(String incom) {
		this.incom = incom;
	}
	public String getIsdel() {
		return isdel;
	}
	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public String getConfirmTimes() {
		return confirmTimes;
	}
	public void setConfirmTimes(String confirmTimes) {
		this.confirmTimes = confirmTimes;
	}
	public String getConfirmIncom() {
		return confirmIncom;
	}
	public void setConfirmIncom(String confirmIncom) {
		this.confirmIncom = confirmIncom;
	}
	public String getFreightid() {
		return freightid;
	}
	public void setFreightid(String freightid) {
		this.freightid = freightid;
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
	public int getGoodsType() {
		return goodsType;
	}
	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}
	public double getCouponsPayTotal() {
		return couponsPayTotal;
	}
	public void setCouponsPayTotal(double couponsPayTotal) {
		this.couponsPayTotal = couponsPayTotal;
	}

	public String getStockId() {
		return stockId;
	}

	public void setStockId(String stockId) {
		this.stockId = stockId;
	}

	public double getBalance_90() {
		return balance_90;
	}

	public void setBalance_90(double balance_90) {
		this.balance_90 = balance_90;
	}

	public double getBalance_shopping_90() {
		return balance_shopping_90;
	}

	public void setBalance_shopping_90(double balance_shopping_90) {
		this.balance_shopping_90 = balance_shopping_90;
	}

	public double getBalance_experience_90() {
		return balance_experience_90;
	}

	public void setBalance_experience_90(double balance_experience_90) {
		this.balance_experience_90 = balance_experience_90;
	}

	public String getIsPay90() {
		return isPay90;
	}

	public void setIsPay90(String isPay90) {
		this.isPay90 = isPay90;
	}

	public String getIsPayShopping() {
		return isPayShopping;
	}

	public void setIsPayShopping(String isPayShopping) {
		this.isPayShopping = isPayShopping;
	}

	public String getIsPayExperience() {
		return isPayExperience;
	}

	public void setIsPayExperience(String isPayExperience) {
		this.isPayExperience = isPayExperience;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}

	public int getIsPayCoupons() {
		return isPayCoupons;
	}

	public void setIsPayCoupons(int isPayCoupons) {
		this.isPayCoupons = isPayCoupons;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCoinPayTotal() {
		return coinPayTotal;
	}

	public void setCoinPayTotal(String coinPayTotal) {
		this.coinPayTotal = coinPayTotal;
	}

	public String getServicePayTotal() {
		return servicePayTotal;
	}

	public void setServicePayTotal(String servicePayTotal) {
		this.servicePayTotal = servicePayTotal;
	}
}
