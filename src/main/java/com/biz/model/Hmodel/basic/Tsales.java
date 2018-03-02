package com.biz.model.Hmodel.basic;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by tomchen on 17/2/6.
 */
@Entity
@Table(name="base_sales")
public class Tsales {

    @GenericGenerator(name="generator", strategy="uuid.hex")@Id
    @GeneratedValue(generator="generator")
    @Column(name="id", unique=true, nullable=false)
    private String id;

    @Column(name="name")
    private String name;

    @Column(name="phone")
    private String phone;

    @Column(name="email")
    private String email;

    @Column(name="shopId")
    private String shopId;

    @Column(name="createTime")
    private Date createTime;

    @Column(name="isdel")
    private int isdel;

    public Tsales() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getIsdel() {
        return isdel;
    }

    public void setIsdel(int isdel) {
        this.isdel = isdel;
    }

}
