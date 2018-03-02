package com.biz.model.Pmodel.api;

/**
 * 支付场景
 */
public class PayScene {
	/**
	 * 支付场景id
	 */
	private int id;
	/**
	 * order_main编码
	 */
	private String main_code;
	/**
	 * 0:未知 1：微信 2：支付宝 3：易支付 4银行卡,5吉林银行,8:银联钱包，9:百度钱包,6:京东钱包,7:拉卡拉钱包
	 */
	private int type;
	/**
	 * 微信的open_id/支付宝的id/翼支付的id
	 */
	private String pay_user_id;
	/**
	 * 0:未扫码 1:已扫码
	 */
	private int state = 0;
	/**
	 * 删除标志0：未删除，1：删除
	 */
	private int isdel = 0;
	/**
	 * 创建时间
	 */
	private String create_time;
	/**
	 * 0:订单 1：人工发券
	 */
	private int scene_type = 0;
	private String  ticketType = "0";
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMain_code() {
		return main_code;
	}

	public void setMain_code(String main_code) {
		this.main_code = main_code;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPay_user_id() {
		return pay_user_id;
	}

	public void setPay_user_id(String pay_user_id) {
		this.pay_user_id = pay_user_id;
	}

	public int getScene_type() {
		return scene_type;
	}

	public void setScene_type(int scene_type) {
		this.scene_type = scene_type;
	}

	public String getTicketType() {
		return ticketType;
	}

	public void setTicketType(String ticketType) {
		this.ticketType = ticketType;
	}
}
