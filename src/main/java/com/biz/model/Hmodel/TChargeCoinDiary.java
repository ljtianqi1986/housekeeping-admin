package com.biz.model.Hmodel;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by tomchen on 17/1/6.
 */

@Entity
@Table(name="charge_coin_diary")
public class TChargeCoinDiary implements java.io.Serializable{

    @GenericGenerator(name="generator", strategy="uuid.hex")@Id
    @GeneratedValue(generator="generator")
    @Column(name="id", unique=true, nullable=false)
    private String id;

    @Column(name="userId")
    private String userId;

    @Column(name="amount")
    private double amount;

    @Column(name="createTime")
    private Date createTime;

    @Column(name="payment_method")
    private int payment_method;

    @Column(name="serialNum")
    private String serialNum;

    @Column(name="note")
    private String note;

    public TChargeCoinDiary() {
    }

    public TChargeCoinDiary(String userId, double amount, Date createTime, int payment_method, String serialNum, String note) {
        this.userId = userId;
        this.amount = amount;
        this.createTime = createTime;
        this.payment_method = payment_method;
        this.serialNum = serialNum;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(int payment_method) {
        this.payment_method = payment_method;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
