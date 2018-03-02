package com.biz.model.Pmodel.basic;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Created by ldd_person on 2017/2/9.
 */
@Entity
public class Recharge90 implements Serializable {
    private String code;
    private String brand_code;
    private String brand_name;
    private int total_90;
    private int total_rmb;
    private String total90;
    private String totalrmb;
    private int balance_90;
    private String biz_code;
    private String notes;
    private String user_code;
    private String isdel;
    private String create_time;
    private String agent_code;
    private String agent_name;
    private String person_name;
    private String ticketType;
    private int startRecord;
    private int pageSize;
    private int totalNow_90;
    private String  brand_balance;


    public Recharge90() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBrand_code() {
        return brand_code;
    }

    public void setBrand_code(String brand_code) {
        this.brand_code = brand_code;
    }

    public int getTotal_90() {
        return total_90;
    }

    public void setTotal_90(int total_90) {
        this.total_90 = total_90;
    }

    public int getTotal_rmb() {
        return total_rmb;
    }

    public void setTotal_rmb(int total_rmb) {
        this.total_rmb = total_rmb;
    }

    public String getBiz_code() {
        return biz_code;
    }

    public void setBiz_code(String biz_code) {
        this.biz_code = biz_code;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getAgent_code() {
        return agent_code;
    }

    public void setAgent_code(String agent_code) {
        this.agent_code = agent_code;
    }

    public int getStartRecord() {
        return startRecord;
    }

    public void setStartRecord(int startRecord) {
        this.startRecord = startRecord;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public int getBalance_90() {
        return balance_90;
    }

    public void setBalance_90(int balance_90) {
        this.balance_90 = balance_90;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getTotal90() {
        return total90;
    }

    public void setTotal90(String total90) {
        this.total90 = total90;
    }

    public String getTotalrmb() {
        return totalrmb;
    }

    public void setTotalrmb(String totalrmb) {
        this.totalrmb = totalrmb;
    }

    public int getTotalNow_90() {
        return totalNow_90;
    }

    public void setTotalNow_90(int totalNow_90) {
        this.totalNow_90 = totalNow_90;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getBrand_balance() {
        return brand_balance;
    }

    public void setBrand_balance(String brand_balance) {
        this.brand_balance = brand_balance;
    }
}
