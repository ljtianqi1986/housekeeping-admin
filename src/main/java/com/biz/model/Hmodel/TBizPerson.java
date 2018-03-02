package com.biz.model.Hmodel;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by tomchen on 17/1/11.
 */
@Entity
@Table(name="base_biz_person")
public class TBizPerson {

    @GenericGenerator(name="generator", strategy="uuid.hex")@Id
    @GeneratedValue(generator="generator")
    @Column(name="code", unique=true, nullable=false)
    private String code = "";

    @Column(name="person_name")
    private String person_name = "";

    @Column(name="phone")
    private String phone = "";

    @Column(name="number")
    private String number = "";

    @Column(name="brand_count")
    private int brand_count = 0;

    @Column(name="notes")
    private String notes = "";

    @Column(name="couponsMoney")
    private int couponsMoney = 0;

    @Column(name="ticket")
    private String ticket = "";

    @Column(name="isdel")
    private int isdel;

    @Column(name="create_time")
    private Date create_time;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getBrand_count() {
        return brand_count;
    }

    public void setBrand_count(int brand_count) {
        this.brand_count = brand_count;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getIsdel() {
        return isdel;
    }

    public void setIsdel(int isdel) {
        this.isdel = isdel;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public int getCouponsMoney() {
        return couponsMoney;
    }

    public void setCouponsMoney(int couponsMoney) {
        this.couponsMoney = couponsMoney;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
