package com.biz.model.Hmodel;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by tomchen on 17/1/6.
 */

@Entity
@Table(name="t_sys_log")
public class TSysLog implements java.io.Serializable{

    @GenericGenerator(name="generator", strategy="uuid.hex")@Id
    @GeneratedValue(generator="generator")
    @Column(name="id", unique=true, nullable=false)
    private String id;

    @Column(name="type")
    private String type;

    @Column(name="userName")
    private String userName;

    @Column(name="ip")
    private String ip;

    @Column(name="detail")
    private String detail;

    @Column(name="createTime")
    private Date createTime;

    public TSysLog() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
