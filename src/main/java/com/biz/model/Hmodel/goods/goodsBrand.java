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
@Table(name = "t_goods_brand")
public class goodsBrand implements java.io.Serializable {

	// Fields
	private String id;
	private String name;
	private Integer type=0;
    private Integer isdel=0;
	private Date createTime=new Date();


    // Constructors

	/** default constructor */
	public goodsBrand() {
	}

	/** full constructor */
	public goodsBrand(String name, Integer type, Integer isdel, Date createTime) {
		this.type = type;
		this.name = name;
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

    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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
}