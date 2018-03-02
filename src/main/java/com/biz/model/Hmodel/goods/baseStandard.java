package com.biz.model.Hmodel.goods;
// default package


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Tpics entity. @author MyEclipse Persistence Tools
 * id 赋值，供应链接口用
 */
@Entity
@Table(name = "base_standard")
public class baseStandard implements java.io.Serializable {

	// Fields
	private String id;
	private String name;
	private String pid;
    private String shopId;
    private Integer isdel=0;
	private Date createTime=new Date();
    private Integer type=0;

    // Constructors

	/** default constructor */
	public baseStandard() {
	}

	/** full constructor */
	public baseStandard(String name,String pid,String shopId, Integer isdel, Date createTime, Integer type) {
		this.pid = pid;
		this.name = name;
        this.shopId=shopId;
		this.isdel = isdel;
		this.createTime = createTime;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false, length = 50)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

    @Column(name = "name", length = 50)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "pid", length = 50)
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Column(name = "shopId", length = 50)
    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    @Column(name = "isdel")
	public Integer getIsdel() {
		return this.isdel;
	}

	public void setIsdel(Integer isdel) {
		this.isdel = isdel;
	}

	@Column(name = "createTime", length = 19)

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}