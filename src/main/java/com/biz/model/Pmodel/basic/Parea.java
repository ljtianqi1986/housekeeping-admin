package com.biz.model.Pmodel.basic;


import java.sql.Timestamp;

/**
 * 地区管理
 * @author GengLong
 * 创建时间：2015-05-18
 */
public class Parea
{
	private String id;
    private String pid;
    private String text;
    private String iconCls;
    private Integer sorts;
    private Short deep;
    private String state="closed";
//    private Short isdel=Global.OPEN;
    private Timestamp createTime;
    private String cityId;
    
	public Parea() {
		super();

	}
	
	public Parea(String id, String pid, String text, String iconCls,
                 Integer sorts, Short deep,  Timestamp createTime, String cityId) {
		super();
		this.id = id;
		this.pid = pid;
		this.text = text;
		this.iconCls = iconCls;
		this.sorts = sorts;
		this.deep = deep;
//		this.isdel = isdel;
		this.createTime = createTime;
		this.cityId = cityId;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getPid() {
		return pid;
	}


	public void setPid(String pid) {
		this.pid = pid;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public String getIconCls() {
		return iconCls;
	}


	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}


	public Integer getSorts() {
		return sorts;
	}


	public void setSorts(Integer sorts) {
		this.sorts = sorts;
	}


	public Short getDeep() {
		return deep;
	}


	public void setDeep(Short deep) {
		this.deep = deep;
	}


	public Timestamp getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	
}
