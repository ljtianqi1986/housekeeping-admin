package com.biz.model.Pmodel.api;

/**
*体验店订单
*/
public class OrderMain90 {

    private String id ;
    private String code;//对应店小翼的order_code,90开头
    private double cash_total = 0.00;//应该付服务费金额
    private double tickets_total=0.0;//订单应付90券---------（新增）

    private double pay_90 = 0.00;//消费的久零券额(已经付款的90券点)
	private double order_total = 0.00;//订单应支付额 //90币抵押后，还欠多少费用(已经付款的额外金额)
    private double pay_coin = 0.00;//消费的90币(已经付款的90券点)---------（新增）


    private int cash_payType=0;  //服务费支付方式 0店小翼支付接口，1久零币支付接口
	private double card_total = 0.00;//优惠券产生的优惠金额
	private int card_count = 0;//所用优惠券张数
	private String open_id;//个人open_id
	private String user_code;//营业员
	private String shop_code;//门店主键
	private String brand_code;//品牌主键
	private int state = 0;// 0:未生效1:交易成功2:错误3:已退款
	private String pay_time;//支付时间
	private String back_code;//退款单号
	private String back_time;//退款时间
	private String back_user_code;//退款操作人
	private String error_pay_msg;//错误信息
	private String trade_type;//支付类型 MICROPAY:刷卡支付 NATIVE:扫码支付 JSAPI:公众号支付 offline:线下支付
	private double gift_90=0.0;//赠送的90券
	private int pay_type = 0;//0:未知 1：微信 2：支付宝 3：易支付 4:银联卡 5:线下
	private String pay_user_id;//微信的open_id/支付宝的id/翼支付的id
	private int isdel = 0;//删除标志0：未删除，1：删除
	private String create_time;//创建时间
	private String other_order_code;//对方原久零平台订单唯一标识
	/***********************扩展字段开始***************************/
	private String person_name;//营业员名称
	private String business_name;//门店名称
	private String custom_name;//消费者昵称
    private String dxy_code;//店小翼code
    private String dxy_person_code;
    private int balance_type;
    private double balance_90=0;
    private double balance_shopping_90=0;
    private double balance_experience_90=0;
    private int isPay90;
    private int isPayShopping90;
    private int isPayExperience90;



	/***********************扩展字段结束***************************/
    public void setCode(String code){
       this.code = code;
    }
    
    public String getCode(){
       return this.code;
    }

    public void setOrder_total(double order_total){
       this.order_total = order_total;
    }
    
    public double getOrder_total(){
       return this.order_total;
    }
    
    public void setCash_total(double cash_total){
       this.cash_total = cash_total;
    }
    
    public double getCash_total(){
       return this.cash_total;
    }
    
    public void setCard_total(double card_total){
       this.card_total = card_total;
    }
    
    public double getCard_total(){
       return this.card_total;
    }
    
    public void setCard_count(int card_count){
       this.card_count = card_count;
    }

    public int getCard_count(){
       return this.card_count;
    }

    public void setOpen_id(String open_id){
       this.open_id = open_id;
    }
    
    public String getOpen_id(){
       return this.open_id;
    }
    
    public void setUser_code(String user_code){
       this.user_code = user_code;
    }
    
    public String getUser_code(){
       return this.user_code;
    }
    
    public void setShop_code(String shop_code){
       this.shop_code = shop_code;
    }
    
    public String getShop_code(){
       return this.shop_code;
    }
    
    public void setBrand_code(String brand_code){
       this.brand_code = brand_code;
    }
    
    public String getBrand_code(){
       return this.brand_code;
    }
    
    public void setState(int state){
       this.state = state;
    }
    
    public int getState(){
       return this.state;
    }
    
    public void setPay_time(String pay_time){
       this.pay_time = pay_time;
    }
    
    public String getPay_time(){
       return this.pay_time;
    }
    
    public void setBack_code(String back_code){
       this.back_code = back_code;
    }
    
    public String getBack_code(){
       return this.back_code;
    }
    
    public void setBack_time(String back_time){
       this.back_time = back_time;
    }
    
    public String getBack_time(){
       return this.back_time;
    }
    
    public void setBack_user_code(String back_user_code){
       this.back_user_code = back_user_code;
    }
    
    public String getBack_user_code(){
       return this.back_user_code;
    }
    
    public void setError_pay_msg(String error_pay_msg){
       this.error_pay_msg = error_pay_msg;
    }
    
    public String getError_pay_msg(){
       return this.error_pay_msg;
    }
    
    public void setTrade_type(String trade_type){
       this.trade_type = trade_type;
    }
    
    public String getTrade_type(){
       return this.trade_type;
    }
    
    public void setGift_90(double gift_90){
       this.gift_90 = gift_90;
    }
    
    public double getGift_90(){
       return this.gift_90;
    }
    
    public void setPay_90(double pay_90){
       this.pay_90 = pay_90;
    }
    
    public double getPay_90(){
       return this.pay_90;
    }
    
    public void setIsdel(int isdel){
       this.isdel = isdel;
    }
    
    public int getIsdel(){
       return this.isdel;
    }
    
    public void setCreate_time(String create_time){
       this.create_time = create_time;
    }
    
    public String getCreate_time(){
       return this.create_time;
    }

	public String getPay_user_id() {
		return pay_user_id;
	}

	public void setPay_user_id(String pay_user_id) {
		this.pay_user_id = pay_user_id;
	}

	public int getPay_type() {
		return pay_type;
	}

	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}

	public String getPerson_name() {
		return person_name;
	}

	public void setPerson_name(String person_name) {
		this.person_name = person_name;
	}

	public String getCustom_name() {
		return custom_name;
	}

	public void setCustom_name(String custom_name) {
		this.custom_name = custom_name;
	}

	public String getBusiness_name() {
		return business_name;
	}

	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}

	public String getOther_order_code() {
		return other_order_code;
	}

	public void setOther_order_code(String other_order_code) {
		this.other_order_code = other_order_code;
	}

    public int getCash_payType() {
        return cash_payType;
    }

    public void setCash_payType(int cash_payType) {
        this.cash_payType = cash_payType;
    }

    public double getTickets_total() {
        return tickets_total;
    }

    public void setTickets_total(double tickets_total) {
        this.tickets_total = tickets_total;
    }

    public double getPay_coin() {
        return pay_coin;
    }

    public void setPay_coin(double pay_coin) {
        this.pay_coin = pay_coin;
    }

    public String getDxy_code() {
        return dxy_code;
    }

    public void setDxy_code(String dxy_code) {
        this.dxy_code = dxy_code;
    }

    public String getDxy_person_code() {
        return dxy_person_code;
    }

    public void setDxy_person_code(String dxy_person_code) {
        this.dxy_person_code = dxy_person_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBalance_type() {
        return balance_type;
    }

    public void setBalance_type(int balance_type) {
        this.balance_type = balance_type;
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

    public int getIsPay90() {
        return isPay90;
    }

    public void setIsPay90(int isPay90) {
        this.isPay90 = isPay90;
    }

    public int getIsPayShopping90() {
        return isPayShopping90;
    }

    public void setIsPayShopping90(int isPayShopping90) {
        this.isPayShopping90 = isPayShopping90;
    }

    public int getIsPayExperience90() {
        return isPayExperience90;
    }

    public void setIsPayExperience90(int isPayExperience90) {
        this.isPayExperience90 = isPayExperience90;
    }
}
