package com.biz.model.Hmodel;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * TSysLog entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "t_refund_log")
public class TRefundLog implements java.io.Serializable {

	// Fields

	/**
	 *
	 */
	private static final long serialVersionUID = -3519488187397926909L;
	private String id;
	private Date modifyTime=new Date();
	private String doUserId;
	private String state;
	private String goodsId;
	private String modifyInfo;
	private String mapId;
	private Date publishTime;

	// Constructors

	/** default constructor */
	public TRefundLog() {
	}


	public TRefundLog(String id, Date modifyTime, String doUserId, String state,
                      String goodsId, String modifyInfo, String mapId, Date publishTime) {
		super();
		this.id = id;
		this.modifyTime = modifyTime;
		this.doUserId = doUserId;
		this.state = state;
		this.goodsId = goodsId;
		this.modifyInfo = modifyInfo;
		this.mapId = mapId;
		this.publishTime = publishTime;
	}



	// Property accessors
	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false, length = 50)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "modifyTime", length = 19)
	public Date getModifyTime() {
		return modifyTime;
	}


	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "doUserId", length = 50)
	public String getDoUserId() {
		return doUserId;
	}


	public void setDoUserId(String doUserId) {
		this.doUserId = doUserId;
	}

	
	

	@Column(name = "goodsId", length = 50)
	public String getGoodsId() {
		return goodsId;
	}


	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	@Column(name = "modifyInfo", length = 500)
	public String getModifyInfo() {
		return modifyInfo;
	}


	public void setModifyInfo(String modifyInfo) {
		this.modifyInfo = modifyInfo;
	}

	
	@Column(name = "mapId", length = 50)
	public String getMapId() {
		return mapId;
	}


	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
	
	
	@Column(name = "publishTime", length = 19)
	public Date getPublishTime() {
		return publishTime;
	}


	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}


	
	@Column(name = "state", length = 50)
	public String getState() {
		return state;
	}


	public void setState(String state) {
		this.state = state;
	}


}