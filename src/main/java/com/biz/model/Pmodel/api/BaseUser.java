package com.biz.model.Pmodel.api;

/**
*用户
*/
public class BaseUser {
    private String id;
	private String open_id;//用户的open_id
	private String person_name;//用户昵称
	private int sex = 0;//值为1时是男性，值为2时是女性，值为0时是未知
	private String phone;//用户手机号
	private String cover;//用户头像
	private double balance_cash;//代金券余额
	private double balance_90;//90券余额
	private int state = 1;//0:取消关注1:关注
	private String country;//国家
	private String province;//省份
	private String city;//城市
	private String birthday;//生日
	private String scan_ali_id;//第一次扫码后绑定的支付宝id
	private String scan_yi_id;//第一次扫码后绑定的易支付的id
	private String scan_yhk_id;//第一次扫码后绑定的银行卡的id
	private String xy_openid;// 兴业的open_id
	private int isdel = 0;// 删除标志0：未删除，1：删除
	private String create_time;//创建时间
	private String only_code;//付款码 每分钟更新  唯一标识
	private double balance_90_total;//90券总额
	private String user_latitude;//用户地理位置纬度
	private String user_longitude;//用户地理位置经度

    private double chargeAmount=0.0;//充值金额
    private double giveAmount=0.0;//赠送金额
    private double extraAmount=0.0;//临时久零贝余额
    private  String unionId;
    private double balance_shopping_90=0.0;//购物券余额
    private String shop_id="";
    private String appid="";
    private double balance_experience_90=0.0;//体验券


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOpen_id(String open_id){
       this.open_id = open_id;
    }
    
    public String getOnly_code()
	{
		return only_code;
	}

	public void setOnly_code(String only_code)
	{
		this.only_code = only_code;
	}

	public String getOpen_id(){
       return this.open_id;
    }
    
    public void setPerson_name(String person_name){
       this.person_name = person_name;
    }
    
    public String getPerson_name(){
       return this.person_name;
    }
    
    public void setSex(int sex){
       this.sex = sex;
    }
    
    public int getSex(){
       return this.sex;
    }
    
    public void setPhone(String phone){
       this.phone = phone;
    }
    
    public String getPhone(){
       return this.phone;
    }
    
    public void setCover(String cover){
       this.cover = cover;
    }
    
    public String getCover(){
       return this.cover;
    }
    
    public double getBalance_cash() {
		return balance_cash;
	}

	public void setBalance_cash(double balance_cash) {
		this.balance_cash = balance_cash;
	}

	public double getBalance_90() {
		return balance_90;
	}

	public void setBalance_90(double balance_90) {
		this.balance_90 = balance_90;
	}

	public void setState(int state){
       this.state = state;
    }
    
    public int getState(){
       return this.state;
    }
    
    public void setCountry(String country){
       this.country = country;
    }
    
    public String getCountry(){
       return this.country;
    }
    
    public void setProvince(String province){
       this.province = province;
    }
    
    public String getProvince(){
       return this.province;
    }
    
    public void setCity(String city){
       this.city = city;
    }
    
    public String getCity(){
       return this.city;
    }
    
    public void setBirthday(String birthday){
       this.birthday = birthday;
    }
    
    public String getBirthday(){
       return this.birthday;
    }
    
    public void setScan_ali_id(String scan_ali_id){
       this.scan_ali_id = scan_ali_id;
    }
    
    public String getScan_ali_id(){
       return this.scan_ali_id;
    }
    
    public void setScan_yi_id(String scan_yi_id){
       this.scan_yi_id = scan_yi_id;
    }
    
    public String getScan_yi_id(){
       return this.scan_yi_id;
    }
    
    public void setScan_yhk_id(String scan_yhk_id){
       this.scan_yhk_id = scan_yhk_id;
    }
    
    public String getScan_yhk_id(){
       return this.scan_yhk_id;
    }
    
    public String getXy_openid() {
		return xy_openid;
	}

	public void setXy_openid(String xy_openid) {
		this.xy_openid = xy_openid;
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

	public double getBalance_90_total() {
		return balance_90_total;
	}

	public void setBalance_90_total(double balance_90_total) {
		this.balance_90_total = balance_90_total;
	}

	public String getUser_latitude() {
		return user_latitude;
	}

	public void setUser_latitude(String user_latitude) {
		this.user_latitude = user_latitude;
	}

	public String getUser_longitude() {
		return user_longitude;
	}

	public void setUser_longitude(String user_longitude) {
		this.user_longitude = user_longitude;
	}

    public double getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(double chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public double getGiveAmount() {
        return giveAmount;
    }

    public void setGiveAmount(double giveAmount) {
        this.giveAmount = giveAmount;
    }

    public double getExtraAmount() {
        return extraAmount;
    }

    public void setExtraAmount(double extraAmount) {
        this.extraAmount = extraAmount;
    }

    public double getBalance_shopping_90() {
        return balance_shopping_90;
    }

    public void setBalance_shopping_90(double balance_shopping_90) {
        this.balance_shopping_90 = balance_shopping_90;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public double getBalance_experience_90() {
        return balance_experience_90;
    }

    public void setBalance_experience_90(double balance_experience_90) {
        this.balance_experience_90 = balance_experience_90;
    }
}
