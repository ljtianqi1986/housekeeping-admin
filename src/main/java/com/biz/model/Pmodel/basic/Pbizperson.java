package com.biz.model.Pmodel.basic;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tomchen on 17/1/11.
 */
public class Pbizperson implements Serializable {

    private String code = "";
    private String person_name = "";
    private String phone = "";
    private String number = "";
    private int brand_count = 0;
    private String notes = "";
    private double couponsMoney =0;
    private String ticket="";
    private int isdel;
    private Date create_time;

    public String getCode()
    {
        return code;
    }
    public void setCode(String code)
    {
        this.code = code;
    }
    public String getPerson_name()
    {
        return person_name;
    }
    public void setPerson_name(String person_name)
    {
        this.person_name = person_name;
    }
    public String getPhone()
    {
        return phone;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    public String getNumber()
    {
        return number;
    }
    public void setNumber(String number)
    {
        this.number = number;
    }
    public int getBrand_count()
    {
        return brand_count;
    }
    public void setBrand_count(int brand_count)
    {
        this.brand_count = brand_count;
    }
    public String getNotes()
    {
        return notes;
    }
    public void setNotes(String notes)
    {
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

    public double getCouponsMoney() {
        return couponsMoney;
    }

    public void setCouponsMoney(double couponsMoney) {
        this.couponsMoney = couponsMoney;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }
}
