package com.biz.model.Pmodel.task;


/**
 * 活动设置
 * @author Administrator
 *
 */
public class Pbase90Detail
{
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 品牌主键
	 */
	private String brand_code;
	/**
	 * 门店主键
	 */
	private String shop_code;
	/**
	 * 0：充值code；1：订单code;2；收银员code；3订单code 4.卡密
	 */
	private String source_code;
	/**
	 * 领取人
	 */
	private String open_id;
	/**
	 * 0、充值；1:自动；2人工；3消券  4.久零券充值卡
	 */
	private int source = 0;
	/**
	 * 说明,当source=5时,用户退款-微信刷卡付,用户退款-银联,用户退款-体验店退款
	 */
	private String source_msg="";
	/**
	 * 久零券发放额
	 */
	private int point_90 = 0;
	/**
	 * 删除标志0：未删除，1：删除
	 */
	private int isdel = 0;
	/**
	 * 获取时间
	 */
	private String create_time;
	private String cancel_time;
	private String user_code;
	private String rg_code;
	private String business_name;
	private String card_number;
	private int point90Now;
	private int state;
	private String sourceId;
	private String sourceMsg;
	private int inOut;
	private int point90;
	private String openId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBrand_code() {
		return brand_code;
	}

	public void setBrand_code(String brand_code) {
		this.brand_code = brand_code;
	}

	public String getShop_code() {
		return shop_code;
	}

	public void setShop_code(String shop_code) {
		this.shop_code = shop_code;
	}

	public String getSource_code() {
		return source_code;
	}

	public void setSource_code(String source_code) {
		this.source_code = source_code;
	}

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public String getSource_msg() {
		return source_msg;
	}

	public void setSource_msg(String source_msg) {
		this.source_msg = source_msg;
	}

	public int getPoint_90() {
		return point_90;
	}

	public void setPoint_90(int point_90) {
		this.point_90 = point_90;
	}

	public int getIsdel() {
		return isdel;
	}

	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getCancel_time() {
		return cancel_time;
	}

	public void setCancel_time(String cancel_time) {
		this.cancel_time = cancel_time;
	}

	public String getUser_code() {
		return user_code;
	}

	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}

	public String getRg_code() {
		return rg_code;
	}

	public void setRg_code(String rg_code) {
		this.rg_code = rg_code;
	}

	public String getBusiness_name() {
		return business_name;
	}

	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}

	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	public int getPoint90Now() {
		return point90Now;
	}

	public void setPoint90Now(int point90Now) {
		this.point90Now = point90Now;
	}


	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceMsg() {
		return sourceMsg;
	}

	public void setSourceMsg(String sourceMsg) {
		this.sourceMsg = sourceMsg;
	}

	public int getInOut() {
		return inOut;
	}

	public void setInOut(int inOut) {
		this.inOut = inOut;
	}

	public int getPoint90() {
		return point90;
	}

	public void setPoint90(int point90) {
		this.point90 = point90;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
     
	