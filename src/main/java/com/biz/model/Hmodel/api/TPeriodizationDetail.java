package com.biz.model.Hmodel.api;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lzq on 2017/4/25.
 */
@Entity
@Table(name="periodization_detail")
public class TPeriodizationDetail implements java.io.Serializable {


    private String id;
    private Integer thisTerm ;
    private String mainId;
    private Double thisTotal;
    private Date thisStartTime;
    private Date thisEndTime;
    private Date getTime;
    private Integer state;
    private Date createTime = new Date();
    private Integer isdel = 0;
    private Integer getTerm = 0;


    public TPeriodizationDetail(){}
    //full
    public TPeriodizationDetail(String id, Integer thisTerm, String mainId, Double thisTotal, Date thisStartTime, Date thisEndTime, Date getTime, Integer state, Date createTime, Integer isdel, Integer getTerm) {
        this.id = id;
        this.thisTerm = thisTerm;
        this.mainId = mainId;
        this.thisTotal = thisTotal;
        this.thisStartTime = thisStartTime;
        this.thisEndTime = thisEndTime;
        this.getTime = getTime;
        this.state = state;
        this.createTime = createTime;
        this.isdel = isdel;
        this.getTerm = getTerm;
    }

    @Id
//    @GeneratedValue(generator = "generator")

    @Column(name = "id", length = 50)

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Column(name="thisTerm", length=10)

    public Integer getThisTerm() {
        return thisTerm;
    }

    public void setThisTerm(Integer thisTerm) {
        this.thisTerm = thisTerm;
    }

    @Column(name="mainId", length=50)

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    @Column(name="thisTotal", length=10)
    public Double getThisTotal() {
        return thisTotal;
    }

    public void setThisTotal(Double thisTotal) {
        this.thisTotal = thisTotal;
    }

    @Column(name="thisStartTime")
    public Date getThisStartTime() {
        return thisStartTime;
    }

    public void setThisStartTime(Date thisStartTime) {
        this.thisStartTime = thisStartTime;
    }

    @Column(name="thisEndTime")
    public Date getThisEndTime() {
        return thisEndTime;
    }

    public void setThisEndTime(Date thisEndTime) {
        this.thisEndTime = thisEndTime;
    }

    @Column(name="getTime")
    public Date getGetTime() {
        return getTime;
    }

    public void setGetTime(Date getTime) {
        this.getTime = getTime;
    }

    @Column(name="state")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Column(name="createTime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name="isdel")
    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    @Column(name="getTerm")
    public Integer getGetTerm() {
        return getTerm;
    }

    public void setGetTerm(Integer getTerm) {
        this.getTerm = getTerm;
    }
}