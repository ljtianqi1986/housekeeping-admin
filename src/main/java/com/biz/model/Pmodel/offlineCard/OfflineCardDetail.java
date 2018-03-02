package com.biz.model.Pmodel.offlineCard;

import java.io.Serializable;

/**
 * 
 * 类名称：User.java 类描述：
 * 
 * @author GengLong
 */
public class OfflineCardDetail implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	private int id ;
	/**
	 * 实体卡主表code
	 */
	private String main_code = "";
	/**
	 * 实体卡密钥
	 */
	private String card_code;
	/**
	 * 实体卡卡号
	 */
	private String card_number;
	/**
	 * 充值人即使用人
	 */
	private String open_id;
	/**
	 * 0:待充值1:已充值
	 */
	private int state;
	/**
	 * 充值时间
	 */
	private String user_time;
	/**
	 * 创建时间
	 */
	private int create_time;
	/**
	 * 领券人姓名
	 */
	private String person_name;
	private String brandCode;

	private String isGrand;

	private String brandName;
	private String typeName;
	private  String typeId;
	private String mainId;
	private String cardCode;
	private String isFirst;
	private String giveTime = "";
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
	public String getCard_code() {
		return card_code;
	}
	public void setCard_code(String card_code) {
		this.card_code = card_code;
	}
	public String getCard_number() {
		return card_number;
	}
	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getUser_time() {
		return user_time;
	}
	public void setUser_time(String user_time) {
		this.user_time = user_time;
	}
	public int getCreate_time() {
		return create_time;
	}
	public void setCreate_time(int create_time) {
		this.create_time = create_time;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getPerson_name() {
		return person_name;
	}
	public void setPerson_name(String person_name) {
		this.person_name = person_name;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getIsGrand() {
		return isGrand;
	}

	public void setIsGrand(String isGrand){
		this.isGrand=isGrand;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getGiveTime() {
		return giveTime;
	}

	public void setGiveTime(String giveTime) {
		this.giveTime = giveTime;
	}

	public String getMainId() {
		return mainId;
	}

	public void setMainId(String mainId) {
		this.mainId = mainId;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(String isFirst) {
		this.isFirst = isFirst;
	}
}
