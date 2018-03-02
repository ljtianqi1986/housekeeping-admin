package com.biz.model.Pmodel.api;


import java.util.Date;

public class OrderMain {
     private String id	;//varchar
	 private String code	;//varchar	订单编号
	 private String payCode;//	varchar	微信支付单号
	 private String buyUserId;//	varchar	购买人
	 private String buyAddr;//	varchar	详细地址
	 private String pro;//省
	 private String city;//市
	 private String area;//区
	 private double goodsTotal=0;//	decimal	商品总金额  单价*数量
	 private String couponId;//	varchar	所使用的优惠券的ID
	 private double couponTotal=0;//	decimal	优惠券费用(只抵商品物流除外)
     private String discountState;//(0不启用积分抵用，1启用)
     private double integralTotal=0;//积分抵用金额
     private double freightTotal=0;//	decimal	物流金额
     private double allTotal=0;//	decimal	合计总金额 (goodsTotal+运费)
	 private double payTotal=0;//	decimal	需要额外支付的费用(抵去抵用券) allTotal-优惠金额合计-抵用金额合计
	 private Date payTime;//	datetime	付款时间
	 private Date confirmTime;//	datetime	签收时间
	 private String state;//	smallint	0:未付款1:已付款
	 private String isGroup;//	smallint	0:正常订单1:团购订单
	 private String isPrint;//	smallint	是否打印
	 private String groupId;//	varchar	团购编号
	 private String paymentMethod;//	smallint	付款方式：0.在线支付 1.货到付款
	 private String paymentRoute;//	smallint	付款途径：0.余额支付 1.银联支付 2.支付宝支付 3.微信支付 4.其他支付
	 private String test;//	varchar
	 private String shopId;//varchar	商店id
	 private String groupCode;//	varchar	支付订单编号，因为出现合并付款情况，使用传微信订单号用此字段
	 private String giveType;//smallint (0送自己，1送他人)
	 private String giveMsg;//varchar	 特殊要求
	 private String givePhone;//varchar	电话
	 private String message;//varchar	贺卡留言，不启用则空
	 private String isMessage;//smallint 0:无贺卡留言,1:有留言
	 private String usemodel;//smallint  1:统一运费,2模板运费
	 private String freightstate;//smallint  运费是否叠加(0:不叠加1:叠加)
	 private double couponsPayTotal=0;//decimal  按比例需要额外支付的费用(抵去抵用券)
    private String purchasingId;//导购员id
	private int  cashPayType = 0;
	private int cardCount = 0;
	private String openId = "";
	private String userCode = "";
	private String shopCode = "";
	private String brandCode = "";
	private int payType = 0;
	private double giftCoupon = 0.00;
	private double payCoupon = 0.00;
	private String otherOrderCode = "";
	private String errorPayMsg =  "";
	private String backCode = "";
	private Date backTime = new Date();
	private String backUserCode="";
	 
	 public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPayCode() {
		return payCode;
	}
	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public String getBuyUserId() {
		return buyUserId;
	}
	public void setBuyUserId(String buyUserId) {
		this.buyUserId = buyUserId;
	}
	public String getBuyAddr() {
		return buyAddr;
	}
	public void setBuyAddr(String buyAddr) {
		this.buyAddr = buyAddr;
	}
	public String getPro() {
		return pro;
	}
	public void setPro(String pro) {
		this.pro = pro;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public double getGoodsTotal() {
		return goodsTotal;
	}
	public void setGoodsTotal(double goodsTotal) {
		this.goodsTotal = goodsTotal;
	}
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	public double getCouponTotal() {
		return couponTotal;
	}
	public void setCouponTotal(double couponTotal) {
		this.couponTotal = couponTotal;
	}
	public String getDiscountState() {
		return discountState;
	}
	public void setDiscountState(String discountState) {
		this.discountState = discountState;
	}
	public double getIntegralTotal() {
		return integralTotal;
	}
	public void setIntegralTotal(double integralTotal) {
		this.integralTotal = integralTotal;
	}
	public double getFreightTotal() {
		return freightTotal;
	}
	public void setFreightTotal(double freightTotal) {
		this.freightTotal = freightTotal;
	}
	public double getAllTotal() {
		return allTotal;
	}
	public void setAllTotal(double allTotal) {
		this.allTotal = allTotal;
	}
	public double getPayTotal() {
		return payTotal;
	}
	public void setPayTotal(double payTotal) {
		this.payTotal = payTotal;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public Date getConfirmTime() {
		return confirmTime;
	}
	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getIsGroup() {
		return isGroup;
	}
	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
	}
	public String getIsPrint() {
		return isPrint;
	}
	public void setIsPrint(String isPrint) {
		this.isPrint = isPrint;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getPaymentRoute() {
		return paymentRoute;
	}
	public void setPaymentRoute(String paymentRoute) {
		this.paymentRoute = paymentRoute;
	}
	public String getTest() {
		return test;
	}
	public void setTest(String test) {
		this.test = test;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getGiveType() {
		return giveType;
	}
	public void setGiveType(String giveType) {
		this.giveType = giveType;
	}
	public String getGiveMsg() {
		return giveMsg;
	}
	public void setGiveMsg(String giveMsg) {
		this.giveMsg = giveMsg;
	}
	public String getGivePhone() {
		return givePhone;
	}
	public void setGivePhone(String givePhone) {
		this.givePhone = givePhone;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getIsMessage() {
		return isMessage;
	}
	public void setIsMessage(String isMessage) {
		this.isMessage = isMessage;
	}
	public String getUsemodel() {
		return usemodel;
	}
	public void setUsemodel(String usemodel) {
		this.usemodel = usemodel;
	}
	public String getFreightstate() {
		return freightstate;
	}
	public void setFreightstate(String freightstate) {
		this.freightstate = freightstate;
	}
	public double getCouponsPayTotal() {
		return couponsPayTotal;
	}
	public void setCouponsPayTotal(double couponsPayTotal) {
		this.couponsPayTotal = couponsPayTotal;
	}

    public String getPurchasingId() {
        return purchasingId;
    }

    public void setPurchasingId(String purchasingId) {
        this.purchasingId = purchasingId;
    }

    public void setCashPayType(int cashPayType){
		this.cashPayType = cashPayType;
	}
	public int getCashPayType()
	{
		return cashPayType;
	}

	public void setCardCount(int cardCount){
		this.cardCount = cardCount;
	}

	public int getCardCount(){
		return cardCount;
	}

	public String getOpenId()
	{
		return openId;
	}

	public void setOpenId(String openId)
	{
		this.openId = openId;
	}

	public void setUserCode(String userCode)
	{
		this.userCode = userCode;
	}

	public String getUserCode()
	{
		return userCode;
	}

	public String getShopCode()
	{
		return shopCode;
	}

	public void setShopCode(String shopCode){

		this.shopCode = shopCode;
	}

	public String getBrandCode()
	{
		return brandCode;
	}

	public void setBrandCode(String brandCode){
		this.brandCode = brandCode;
	}

	public void setPayType(int payType){
		this.payType = payType;
	}

	public int getPayType()
	{
		return payType;
	}

	public double getGiftCoupon()
	{
		return giftCoupon;
	}


	public void setGiftCoupon(double giftCoupon){
		this.giftCoupon = giftCoupon;
	}

	public double getPayCoupon()
	{
		return payCoupon;
	}

	public void setPayCoupon(double payCoupon){

		this.payCoupon =payCoupon;
	}

	public void setOtherOrderCode(String otherOrderCode){
		this.otherOrderCode = otherOrderCode;
	}

	public String getOtherOrderCode()
	{
		return otherOrderCode;
	}

	public String getErrorPayMsg()
	{
		return errorPayMsg;
	}

	public void setErrorPayMsg(String errorPayMsg){

		this.errorPayMsg = errorPayMsg;
	}

	public void setBackCode(String backCode)
	{
		this.backCode = backCode;
	}

	public String getBackCode(){
		return backCode;
	}

	public Date getBackTime()
	{
		return backTime;

	}

	public void setBackTime(Date backTime){
		this.backTime = backTime;
	}
	public String getBackUserCode(){
		return backUserCode;

	}

	public void setBackUserCode(String backUserCode){
		this.backUserCode = backUserCode;
	}
}
