package com.biz.model.Pmodel;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by tomchen on 17/1/7.
 */
public class PSysLog implements Serializable {

    private String id;
    private String type;
    private String userName;
    private String ip;
    private String detail;
    private Date createTime;

    public PSysLog() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
