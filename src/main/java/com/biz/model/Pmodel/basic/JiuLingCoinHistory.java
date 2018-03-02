package com.biz.model.Pmodel.basic;

/**
 * Created by ldd_person on 2017/3/7.
 */
public class JiuLingCoinHistory {
    private String id = "";
    private String targetUser = "";
    private String opUser = "";
    private int amount=0;
    private double amount1=0.00;
    private String type = "";
    private String reason = "";
    private String createTime = "";
    private String phone = "";
    private int state=0;
    private String serialNum="";
    private int source=0;


    public JiuLingCoinHistory() {
        super();
    }

    public JiuLingCoinHistory(String id, String targetUser, String opUser, int amount, double amount1,
                              String type, String reason, String createTime,String phone,int state,String serialNum,int source) {
        this.id = id;
        this.targetUser = targetUser;
        this.opUser = opUser;
        this.amount = amount;
        this.amount1 = amount1;
        this.type = type;
        this.reason = reason;
        this.createTime = createTime;
        this.phone = phone;
        this.state = state;
        this.serialNum = serialNum;
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOpUser() {
        return opUser;
    }

    public void setOpUser(String opUser) {
        this.opUser = opUser;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public double getAmount1() {
        return amount1;
    }

    public void setAmount1(double amount1) {
        this.amount1 = amount1;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }
}
