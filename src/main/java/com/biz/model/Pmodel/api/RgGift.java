package com.biz.model.Pmodel.api;

/**
*人工发券
*/
public class RgGift {
	/**
	 * 主键
	 */
	private String code;
	/**
	 * 品牌主键
	 */
	private String brand_code;
	/**
	 * 门店主键
	 */
	private String shop_code;
	/**
	 * 营业员
	 */
	private String user_code = "";
	/**
	 * 领取人
	 */
	private String open_id;
	/**
	 * 久零券发放额
	 */
	private double point_90 = 0;
	/**
	 * 0:未领取 1:已领取
	 */
	private int state = 0;
    /**
     * 发劵类型0:默认，1:2%，2:5%
     */
    private int gift_type=0;
	/**
	 * 删除标志0：未删除，1：删除
	 */
	private int isdel = 0;
	/**
	 * 领取时间
	 */
	private String get_time;
	/**
	 * 获取时间
	 */
	private String create_time;

    private String type = "0";

    private int  typeInt = 0;
	
	/***********************扩展字段开始***************************/
	
	/**
	 * 卡号
	 */
	private String card_code;
	/**
	 * 说明
	 */
	private String memo;

    /**
     * 赠送的90贝数
     */
    private double coin_90=0;

    /**
     * 商户名称
     */
    private String brand_name;
	
	/***********************扩展字段结束***************************/
    public void setCode(String code){
       this.code = code;
    }
    
    public String getCode(){
       return this.code;
    }
    
    public void setBrand_code(String brand_code){
       this.brand_code = brand_code;
    }
    
    public String getBrand_code(){
       return this.brand_code;
    }
    
    public void setShop_code(String shop_code){
       this.shop_code = shop_code;
    }
    
    public String getShop_code(){
       return this.shop_code;
    }
    
    public void setUser_code(String user_code){
       this.user_code = user_code;
    }
    
    public String getUser_code(){
       return this.user_code;
    }
    
    public void setOpen_id(String open_id){
       this.open_id = open_id;
    }
    
    public String getOpen_id(){
       return this.open_id;
    }
    
    public double getPoint_90() {
		return point_90;
	}

	public void setPoint_90(double point_90) {
		this.point_90 = point_90;
	}

	public void setState(int state){
       this.state = state;
    }
    
    public int getState(){
       return this.state;
    }

    public int getGift_type() {
        return gift_type;
    }

    public void setGift_type(int gift_type) {
        this.gift_type = gift_type;
    }

    public void setIsdel(int isdel){
       this.isdel = isdel;
    }
    
    public int getIsdel(){
       return this.isdel;
    }
    
    public void setGet_time(String get_time){
       this.get_time = get_time;
    }
    
    public String getGet_time(){
       return this.get_time;
    }
    
    public void setCreate_time(String create_time){
       this.create_time = create_time;
    }
    
    public String getCreate_time(){
       return this.create_time;
    }

	public String getCard_code() {
		return card_code;
	}

	public void setCard_code(String card_code) {
		this.card_code = card_code;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

    public double getCoin_90() {
        return coin_90;
    }

    public void setCoin_90(double coin_90) {
        this.coin_90 = coin_90;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTypeInt() {
        return typeInt;
    }

    public void setTypeInt(int typeInt) {
        this.typeInt = typeInt;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }
}
