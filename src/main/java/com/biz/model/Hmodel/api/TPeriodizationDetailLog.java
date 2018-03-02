package com.biz.model.Hmodel.api;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lzq on 2017/4/27.
 */
@Entity
@Table(name = "periodization_detail_log")
public class TPeriodizationDetailLog {
    private String id;
    private Integer state;
    private String userId;
    private String userName;
    private String detailId;
    private String msg;
    private String mainId;
    private Integer type;
    private Integer lastTerm;
    private Double thisCoupon;
    private Integer thisTerm;
    private Double lastCoupon;
    private Date getTime;
    private Date nextStartTime;
    private Date nextEndTime;
    private String brandCode;
    private Date createTime;
    private String thisGetTerms;
    private Double nextCoupon;
    private Integer totalTerm;
    private Double totalCoupon;

    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "state")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Basic
    @Column(name = "userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "detailId")
    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    @Basic
    @Column(name = "msg")
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Basic
    @Column(name = "mainId")
    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    @Basic
    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Basic
    @Column(name = "lastTerm")
    public Integer getLastTerm() {
        return lastTerm;
    }

    public void setLastTerm(Integer lastTerm) {
        this.lastTerm = lastTerm;
    }

    @Basic
    @Column(name = "thisCoupon")
    public Double getThisCoupon() {
        return thisCoupon;
    }

    public void setThisCoupon(Double thisCoupon) {
        this.thisCoupon = thisCoupon;
    }

    @Basic
    @Column(name = "thisTerm")
    public Integer getThisTerm() {
        return thisTerm;
    }

    public void setThisTerm(Integer thisTerm) {
        this.thisTerm = thisTerm;
    }

    @Basic
    @Column(name = "lastCoupon")
    public Double getLastCoupon() {
        return lastCoupon;
    }

    public void setLastCoupon(Double lastCoupon) {
        this.lastCoupon = lastCoupon;
    }

    @Basic
    @Column(name = "getTime")
    public Date getGetTime() {
        return getTime;
    }

    public void setGetTime(Date getTime) {
        this.getTime = getTime;
    }

    @Basic
    @Column(name = "nextStartTime")
    public Date getNextStartTime() {
        return nextStartTime;
    }

    public void setNextStartTime(Date nextStartTime) {
        this.nextStartTime = nextStartTime;
    }

    @Basic
    @Column(name = "nextEndTime")
    public Date getNextEndTime() {
        return nextEndTime;
    }

    public void setNextEndTime(Date nextEndTime) {
        this.nextEndTime = nextEndTime;
    }

    @Basic
    @Column(name = "brandCode")
    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    @Basic
    @Column(name = "createTime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "thisGetTerms")
    public String getThisGetTerms() {
        return thisGetTerms;
    }

    public void setThisGetTerms(String thisGetTerms) {
        this.thisGetTerms = thisGetTerms;
    }


    @Basic
    @Column(name = "userName")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "nextCoupon")
    public Double getNextCoupon() {
        return nextCoupon;
    }

    public void setNextCoupon(Double nextCoupon) {
        this.nextCoupon = nextCoupon;
    }

    @Basic
    @Column(name = "totalTerm")
    public Integer getTotalTerm() {
        return totalTerm;
    }

    public void setTotalTerm(Integer totalTerm) {
        this.totalTerm = totalTerm;
    }

    @Basic
    @Column(name = "totalCoupon")
    public Double getTotalCoupon() {
        return totalCoupon;
    }

    public void setTotalCoupon(Double totalCoupon) {
        this.totalCoupon = totalCoupon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TPeriodizationDetailLog that = (TPeriodizationDetailLog) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (detailId != null ? !detailId.equals(that.detailId) : that.detailId != null) return false;
        if (msg != null ? !msg.equals(that.msg) : that.msg != null) return false;
        if (mainId != null ? !mainId.equals(that.mainId) : that.mainId != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (lastTerm != null ? !lastTerm.equals(that.lastTerm) : that.lastTerm != null) return false;
        if (thisCoupon != null ? !thisCoupon.equals(that.thisCoupon) : that.thisCoupon != null) return false;
        if (thisTerm != null ? !thisTerm.equals(that.thisTerm) : that.thisTerm != null) return false;
        if (lastCoupon != null ? !lastCoupon.equals(that.lastCoupon) : that.lastCoupon != null) return false;
        if (getTime != null ? !getTime.equals(that.getTime) : that.getTime != null) return false;
        if (nextStartTime != null ? !nextStartTime.equals(that.nextStartTime) : that.nextStartTime != null)
            return false;
        if (nextEndTime != null ? !nextEndTime.equals(that.nextEndTime) : that.nextEndTime != null) return false;
        if (brandCode != null ? !brandCode.equals(that.brandCode) : that.brandCode != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (thisGetTerms != null ? !thisGetTerms.equals(that.thisGetTerms) : that.thisGetTerms != null) return false;
        if (nextCoupon != null ? !nextCoupon.equals(that.nextCoupon) : that.nextCoupon != null) return false;
        if (totalTerm != null ? !totalTerm.equals(that.totalTerm) : that.totalTerm != null) return false;
        if (totalCoupon != null ? !totalCoupon.equals(that.totalCoupon) : that.totalCoupon != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (detailId != null ? detailId.hashCode() : 0);
        result = 31 * result + (msg != null ? msg.hashCode() : 0);
        result = 31 * result + (mainId != null ? mainId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (lastTerm != null ? lastTerm.hashCode() : 0);
        result = 31 * result + (thisCoupon != null ? thisCoupon.hashCode() : 0);
        result = 31 * result + (thisTerm != null ? thisTerm.hashCode() : 0);
        result = 31 * result + (lastCoupon != null ? lastCoupon.hashCode() : 0);
        result = 31 * result + (getTime != null ? getTime.hashCode() : 0);
        result = 31 * result + (nextStartTime != null ? nextStartTime.hashCode() : 0);
        result = 31 * result + (nextEndTime != null ? nextEndTime.hashCode() : 0);
        result = 31 * result + (brandCode != null ? brandCode.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (thisGetTerms != null ? thisGetTerms.hashCode() : 0);
        result = 31 * result + (nextCoupon != null ? nextCoupon.hashCode() : 0);
        result = 31 * result + (totalTerm != null ? totalTerm.hashCode() : 0);
        result = 31 * result + (totalCoupon != null ? totalCoupon.hashCode() : 0);
        return result;
    }
}
