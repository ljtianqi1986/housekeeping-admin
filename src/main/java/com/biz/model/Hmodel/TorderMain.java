package com.biz.model.Hmodel;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/*************************************************************************
* 版本：         V1.0
*
* 文件名称 ：TorderMain.java 描述说明 ：
*
* 创建信息 : create by 曹凯 on 2016-9-1 上午10:05:52  修订信息 : modify by ( ) on (date) for ( )
*
* 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
**************************************************************************/
@Entity
@Table(name="order_main"
)
public class TorderMain implements java.io.Serializable {
  private String id;
  private String code;
  private String payCode;
    private String openId;
  private String buyUserId;
  private String buyAddr;
  private String pro;
  private String city;
  private String area;
  private Double goodsTotal=0.0;
  private Double freightTotal=0.0;
  private Double allTotal=0.0;
  private Double payTotal=0.0;
  private Date payTime;
  private Integer state=0;
  private Integer paymentRoute=0;
  private Integer isdel=0;
    private Integer source = 0;
  private Date createTime=new Date();
  private Date confirmTime;
  private String shopId;
  private String message;
  private Integer usemodel=0;
  private Integer freightstate=0;
  private Integer isClosed=0;
    private Double servicePayTotal=	0.0;
    private Double coinPayTotal = 0.0;
    private Double giftCoupon=	0.0;
    private Double payCoupon=	0.0;
    private Integer payType=0;

    private String userCode = "";
    private Date backTime;


    private String backUserCode;

    private String salesUserCode="";

    private String brandCode = "";

    private int balance_type;

    private Double balance_90=0.0;

    private Double balance_shopping_90=0.0;

    private Double balance_experience_90=0.0;


  public TorderMain() {
      super();
  }
     @GenericGenerator(name="generator", strategy="uuid.hex")@Id @GeneratedValue(generator="generator")

  @Column(name="id", unique=true, nullable=false)
  public String getId() {
      return id;
  }
  public void setId(String id) {
      this.id = id;
  }
  @Column(name="code")
  public String getCode() {
      return code;
  }
  public void setCode(String code) {
      this.code = code;
  }
  @Column(name="payCode")
  public String getPayCode() {
      return payCode;
  }
  public void setPayCode(String payCode) {
      this.payCode = payCode;
  }
  @Column(name="buyUserId")
  public String getBuyUserId() {
      return buyUserId;
  }
  public void setBuyUserId(String buyUserId) {
      this.buyUserId = buyUserId;
  }
  @Column(name="buyAddr")
  public String getBuyAddr() {
      return buyAddr;
  }
  public void setBuyAddr(String buyAddr) {
      this.buyAddr = buyAddr;
  }
  @Column(name="pro")
  public String getPro() {
      return pro;
  }
  public void setPro(String pro) {
      this.pro = pro;
  }
  @Column(name="city")
  public String getCity() {
      return city;
  }
  public void setCity(String city) {
      this.city = city;
  }
  @Column(name="area")
  public String getArea() {
      return area;
  }
  public void setArea(String area) {
      this.area = area;
  }
  @Column(name="goodsTotal")
  public Double getGoodsTotal() {
      return goodsTotal;
  }
  public void setGoodsTotal(Double goodsTotal) {
      this.goodsTotal = goodsTotal;
  }

  @Column(name="freightTotal")
  public Double getFreightTotal() {
      return freightTotal;
  }
  public void setFreightTotal(Double freightTotal) {
      this.freightTotal = freightTotal;
  }
  @Column(name="allTotal")
  public Double getAllTotal() {
      return allTotal;
  }
  public void setAllTotal(Double allTotal) {
      this.allTotal = allTotal;
  }
  @Column(name="payTotal")
  public Double getPayTotal() {
      return payTotal;
  }
  public void setPayTotal(Double payTotal) {
      this.payTotal = payTotal;
  }
  @Column(name="payTime")
  public Date getPayTime() {
      return payTime;
  }
  public void setPayTime(Date payTime) {
      this.payTime = payTime;
  }
  @Column(name="state")
  public Integer getState() {
      return state;
  }
  public void setState(Integer state) {
      this.state = state;
  }

  @Column(name="paymentRoute")
  public Integer getPaymentRoute() {
      return paymentRoute;
  }
  public void setPaymentRoute(Integer paymentRoute) {
      this.paymentRoute = paymentRoute;
  }
  @Column(name="isdel")
  public Integer getIsdel() {
      return isdel;
  }
  public void setIsdel(Integer isdel) {
      this.isdel = isdel;
  }
  @Column(name="createTime")
  public Date getCreateTime() {
      return createTime;
  }
  public void setCreateTime(Date createTime) {
      this.createTime = createTime;
  }

  @Column(name="confirmTime")
  public Date getConfirmTime() {
      return confirmTime;
  }
  public void setConfirmTime(Date confirmTime) {
      this.confirmTime = confirmTime;
  }
  @Column(name="shopId")
  public String getShopId() {
      return shopId;
  }
  public void setShopId(String shopId) {
      this.shopId = shopId;
  }

  @Column(name="message")
  public String getMessage() {
      return message;
  }
  public void setMessage(String message) {
      this.message = message;
  }

  @Column(name="usemodel")
  public Integer getUsemodel() {
      return usemodel;
  }
  public void setUsemodel(Integer usemodel) {
      this.usemodel = usemodel;
  }
  @Column(name="freightstate")
  public Integer getFreightstate() {
      return freightstate;
  }
  public void setFreightstate(Integer freightstate) {
      this.freightstate = freightstate;
  }
  @Column(name="isClosed")
  public Integer getIsClosed() {
      return isClosed;
  }
  public void setIsClosed(Integer isClosed) {
      this.isClosed = isClosed;
  }

    @Column(name="source")
    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }


    @Column(name="servicePayTotal")
    public Double getServicePayTotal() {
        return servicePayTotal;
    }

    public void setServicePayTotal(Double servicePayTotal) {
        this.servicePayTotal = servicePayTotal;
    }



    @Column(name="coinPayTotal")
    public Double getCoinPayTotal() {
        return coinPayTotal;
    }

    public void setCoinPayTotal(Double coinPayTotal) {
        this.coinPayTotal = coinPayTotal;
    }





    @Column(name="userCode")
    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }


    @Column(name="payType")
    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }



    @Column(name="backTime")
    public Date getBackTime() {
        return backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }



    @Column(name="backUserCode")
    public String getBackUserCode() {
        return backUserCode;
    }

    public void setBackUserCode(String backUserCode) {
        this.backUserCode = backUserCode;
    }


    @Column(name="salesUserCode")
    public String getSalesUserCode() {
        return salesUserCode;
    }

    public void setSalesUserCode(String salesUserCode) {
        this.salesUserCode = salesUserCode;
    }
    @Column(name="giftCoupon")
    public Double getGiftCoupon() {
        return giftCoupon;
    }

    public void setGiftCoupon(Double giftCoupon) {
        this.giftCoupon = giftCoupon;
    }
    @Column(name="payCoupon")
    public Double getPayCoupon() {
        return payCoupon;
    }

    public void setPayCoupon(Double payCoupon) {
        this.payCoupon = payCoupon;
    }


    @Column(name="brandCode")
    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }
    @Column(name="balance_type")
    public int getBalance_type() {
        return balance_type;
    }

    public void setBalance_type(int balance_type) {
        this.balance_type = balance_type;
    }
    @Column(name="balance_90")
    public Double getBalance_90() {
        return balance_90;
    }

    public void setBalance_90(Double balance_90) {
        this.balance_90 = balance_90;
    }
    @Column(name="balance_shopping_90")
    public Double getBalance_shopping_90() {
        return balance_shopping_90;
    }

    public void setBalance_shopping_90(Double balance_shopping_90) {
        this.balance_shopping_90 = balance_shopping_90;
    }
    @Column(name="balance_experience_90")
    public Double getBalance_experience_90() {
        return balance_experience_90;
    }

    public void setBalance_experience_90(Double balance_experience_90) {
        this.balance_experience_90 = balance_experience_90;
    }
    @Column(name="openId")
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
