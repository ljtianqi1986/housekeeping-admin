package com.biz.model.Hmodel;
// default package

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
 * TRole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "base_90_detail")

public class TbaseDetail implements java.io.Serializable {


    // Fields
    private String id;

    private String brandId;
    private String shopId;
    private String sourceId;
    private String userId;
    private String userCode;
    private String openId;
    private Integer source=0;
    private String sourceMsg;
    private Integer type=0;
    private Integer inOuts=0;
    private Integer point90=0;
    private Integer point90Now=0;
    private Integer sorts=99;
    private Integer state=0;
    private Date cancelTime;

    private Integer isdel = 0;
    private Date createTime = new Date();
    private Date  expireTime;
    private Integer ticketType=0;
    // Constructors

    /**
     * default constructor
     */
    public TbaseDetail() {
    }



    // Property accessors
    @GenericGenerator(name = "generator", strategy = "uuid.hex")
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "id", unique = true, nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "isdel")
    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    @Column(name = "createTime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "brandId")
    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
    @Column(name = "shopId")
    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
    @Column(name = "sourceId")
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
    @Column(name = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Column(name = "userCode")
    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
    @Column(name = "openId")
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
    @Column(name = "source")
    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }
    @Column(name = "sourceMsg")
    public String getSourceMsg() {
        return sourceMsg;
    }

    public void setSourceMsg(String sourceMsg) {
        this.sourceMsg = sourceMsg;
    }
    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    @Column(name = "point90")
    public Integer getPoint90() {
        return point90;
    }

    public void setPoint90(Integer point90) {
        this.point90 = point90;
    }
    @Column(name = "cancelTime")
    public Date getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }
    @Column(name = "point90Now")
    public Integer getPoint90Now() {
        return point90Now;
    }

    public void setPoint90Now(Integer point90Now) {
        this.point90Now = point90Now;
    }
    @Column(name = "state")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
    @Column(name = "`inOut`")
    public Integer getInOuts() {
        return inOuts;
    }

    public void setInOuts(Integer inOuts) {
        this.inOuts = inOuts;
    }
    @Column(name = "expireTime")
    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
    @Column(name = "sorts")
    public Integer getSorts() {
        return sorts;
    }

    public void setSorts(Integer sorts) {
        this.sorts = sorts;
    }
    @Column(name = "ticketType")
    public Integer getTicketType() {
        return ticketType;
    }

    public void setTicketType(Integer ticketType) {
        this.ticketType = ticketType;
    }
}