package com.biz.model.Pmodel;

import java.io.Serializable;

/**
 * 关注增加点券数
 */
public class PfirstBalance implements Serializable{
	/**
	 * id
	 */
	private String id;
	/**
	 * 90点券数
	 */
	private int balance_90;
	/**
	 * 删除标志0：未删除，1：删除
	 */
	private int isdel = 0;
	/**
	 * 创建时间
	 */
	private String create_time;

    /**
     * 活动设置id
     */
    private String  activityId;

    private String Userid;
    private int ticketType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBalance_90() {
        return balance_90;
    }

    public void setBalance_90(int balance_90) {
        this.balance_90 = balance_90;
    }

    public int getIsdel() {
        return isdel;
    }

    public void setIsdel(int isdel) {
        this.isdel = isdel;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public int getTicketType() {
        return ticketType;
    }

    public void setTicketType(int ticketType) {
        this.ticketType = ticketType;
    }
}
