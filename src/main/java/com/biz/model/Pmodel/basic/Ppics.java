package com.biz.model.Pmodel.basic;


import java.util.Date;

/**
 * 平台附件管理
 * @author GengLong
 * 创建时间：2015-05-18
 */
public class Ppics
{
	private String id;
	private String mainId;
	private Integer mainType;
	private String name;
	private String path;
	private Double size=0.0;
	private Integer isdel=0;
	private Date createTtime=new Date();
	public Ppics() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMainId() {
		return mainId;
	}
	public void setMainId(String mainId) {
		this.mainId = mainId;
	}
	public Integer getMainType() {
		return mainType;
	}
	public void setMainType(Integer mainType) {
		this.mainType = mainType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Double getSize() {
		return size;
	}
	public void setSize(Double size) {
		this.size = size;
	}
	public Integer getIsdel() {
		return isdel;
	}
	public void setIsdel(Integer isdel) {
		this.isdel = isdel;
	}
	public Date getCreateTtime() {
		return createTtime;
	}
	public void setCreateTtime(Date createTtime) {
		this.createTtime = createTtime;
	}
	
	
}
