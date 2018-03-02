package com.biz.model.Pmodel.basic;
// default package


import java.util.Date;

/**
 * 轮辐管理
 * @author YangFan
 * 创建时间：2015-06-11
 */
public class Pslide  {

     // Fields    
	private String id;
    private Integer type;
    private String types;
    private String title;
    private String notes;
    private String goodsId;
    private String goodsName;
    private String goodsPic;
    private String picId;
    private String picUrl;
    private Integer isdel;
    private String path;
    private String url;
    private String price;
    private String name;
    private String isOpen;
    private String createTime;
    private String page_type;
	private String sort;
	private String position;



    public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	private Date create_time;


    // Constructors
    public Pslide() {
    }
    
	public Pslide(String id, Integer type, String types, String title,
                  String notes, String goodsId, String goodsName, String goodsPic,
                  String picId, String picUrl, Integer isdel, String path, Date create_time) {
		super();
		this.id = id;
		this.type = type;
		this.types = types;
		this.title = title;
		this.notes = notes;
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.goodsPic = goodsPic;
		this.picId = picId;
		this.picUrl = picUrl;
		this.isdel = isdel;
		this.path = path;
		this.create_time = create_time;
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}


	public String getTypes() {
		return types;
	}


	public void setTypes(String types) {
		this.types = types;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getNotes() {
		return notes;
	}


	public void setNotes(String notes) {
		this.notes = notes;
	}


	public String getGoodsId() {
		return goodsId;
	}


	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}


	public String getGoodsName() {
		return goodsName;
	}


	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}


	public String getGoodsPic() {
		return goodsPic;
	}


	public void setGoodsPic(String goodsPic) {
		this.goodsPic = goodsPic;
	}


	public String getPicId() {
		return picId;
	}


	public void setPicId(String picId) {
		this.picId = picId;
	}


	public String getPicUrl() {
		return picUrl;
	}


	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}


	public Integer getIsdel() {
		return isdel;
	}


	public void setIsdel(Integer isdel) {
		this.isdel = isdel;
	}


	public Date getCreate_time() {
		return create_time;
	}


	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(String isOpen) {
		this.isOpen = isOpen;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getPage_type() {
		return page_type;
	}

	public void setPage_type(String page_type) {
		this.page_type = page_type;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
}