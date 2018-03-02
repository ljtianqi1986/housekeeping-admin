package com.biz.model.Hmodel.basic;
// default package


import com.biz.conf.Global;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Tpics entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "base_pics")
public class Tpics implements java.io.Serializable {

	// Fields

	private String id;
	private String mainId;
	private Integer mainType;
	private String name;
	private String path;
	private Long size=0l;
	private Integer isdel=0;
	private Date createTtime=new Date();

	// Constructors

	/** default constructor */
	public Tpics() {
	}

	/** full constructor */
	public Tpics(String mainId, Integer mainType, String name, String path,
                 long size, Integer isdel, Date createTtime) {
		this.mainId = mainId;
		this.mainType = mainType;
		this.name = name;
		this.path = path;
		this.size = size;
		this.isdel = isdel;
		this.createTtime = createTtime;
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

	@Column(name = "mainId", length = 50)
	public String getMainId() {
		return this.mainId;
	}

	public void setMainId(String mainId) {
		this.mainId = mainId;
	}

	@Column(name = "mainType")
	public Integer getMainType() {
		return this.mainType;
	}

	public void setMainType(Integer mainType) {
		this.mainType = mainType;
	}

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "path", length = 50)
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(name = "size", precision = 10)
	public Long getSize() {
		return this.size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	@Column(name = "isdel")
	public Integer getIsdel() {
		return this.isdel;
	}

	public void setIsdel(Integer isdel) {
		this.isdel = isdel;
	}

	@Column(name = "createTtime", length = 19)
	public Date getCreateTtime() {
		return this.createTtime;
	}

	public void setCreateTtime(Date createTtime) {
		this.createTtime = createTtime;
	}

}