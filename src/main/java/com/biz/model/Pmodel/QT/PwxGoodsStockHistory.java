package com.biz.model.Pmodel.QT;

import java.util.Date;
import java.util.List;

public class PwxGoodsStockHistory  {

	private String id;
	private String stockId;
	private Integer type;
	private Integer changeNumber;
	private String note;
	private String changeUser;
	private String province;
	private String country;
	private String city;
	private Integer isdel=0;
	private Date createTime=new Date();

	private String provinceName;
	private String countryName;
	private String cityName;

	private String relateCode;
	private Integer lostNumber;
	private String privateUser;
	private Integer state;
	private String whareHouseId;
	private String wharePositionId;
	private String wharePositionName;
	private String oriWhareHouseId;
	private Integer stockNum;
	private Integer inUserNum;
	private Integer oriNumber;
	private Integer nowNumber;


    public Integer getOriNumber() {
        return oriNumber;
    }

    public void setOriNumber(Integer oriNumber) {
        this.oriNumber = oriNumber;
    }

    public Integer getNowNumber() {
        return nowNumber;
    }

    public void setNowNumber(Integer nowNumber) {
        this.nowNumber = nowNumber;
    }

    private List<PwxGoodsStockHistory> parmlist;

	public List<PwxGoodsStockHistory> getParmlist() {
		return parmlist;
	}

	public void setParmlist(List<PwxGoodsStockHistory> parmlist) {
		this.parmlist = parmlist;
	}

	public String getOriWhareHouseId() {
        return oriWhareHouseId;
    }

    public void setOriWhareHouseId(String oriWhareHouseId) {
        this.oriWhareHouseId = oriWhareHouseId;
    }

    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getChangeNumber() {
		return changeNumber;
	}
	public void setChangeNumber(Integer changeNumber) {
		this.changeNumber = changeNumber;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getChangeUser() {
		return changeUser;
	}
	public void setChangeUser(String changeUser) {
		this.changeUser = changeUser;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getIsdel() {
		return isdel;
	}
	public void setIsdel(Integer isdel) {
		this.isdel = isdel;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

    public String getRelateCode() {
        return relateCode;
    }

    public void setRelateCode(String relateCode) {
        this.relateCode = relateCode;
    }

    public Integer getLostNumber() {
        return lostNumber;
    }

    public void setLostNumber(Integer lostNumber) {
        this.lostNumber = lostNumber;
    }

    public String getPrivateUser() {
        return privateUser;
    }

    public void setPrivateUser(String privateUser) {
        this.privateUser = privateUser;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getWhareHouseId() {
        return whareHouseId;
    }

    public void setWhareHouseId(String whareHouseId) {
        this.whareHouseId = whareHouseId;
    }

    public String getWharePositionId() {
        return wharePositionId;
    }

    public void setWharePositionId(String wharePositionId) {
        this.wharePositionId = wharePositionId;
    }

    public String getWharePositionName() {
        return wharePositionName;
    }

    public void setWharePositionName(String wharePositionName) {
        this.wharePositionName = wharePositionName;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }

    public Integer getInUserNum() {
        return inUserNum;
    }

    public void setInUserNum(Integer inUserNum) {
        this.inUserNum = inUserNum;
    }
}
