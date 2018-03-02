package com.biz.model.Hmodel;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by qziwm on 2017/5/15.
 */
@Entity
@Table(name = "order_server_statistics"
)
public class TorderServerStatistics implements java.io.Serializable {
    private String id;
    private String getDate;
    private String serverId;
    private String json;
    private Date createTime=new Date();
    @GenericGenerator(name="generator", strategy="uuid.hex")@Id
    @GeneratedValue(generator="generator")

    @Column(name="id", unique=true, nullable=false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Column(name="getDate")
    public String getGetDate() {
        return getDate;
    }

    public void setGetDate(String getDate) {
        this.getDate = getDate;
    }
    @Column(name="serverId")
    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }
    @Column(name="json")
    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
    @Column(name="createTime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
