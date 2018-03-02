package com.biz.model.Pmodel;

/**
 * 手机端用户积分
 */
public class PhoneUserIntegral {
    /**
     * 来源说明
     */
    private String source;
    /**
     * 涉及的积分
     */
    private int integral;
    /**
     * 积分获取时间
     */
    private String create_time;

    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }
    public int getIntegral() {
        return integral;
    }
    public void setIntegral(int integral) {
        this.integral = integral;
    }
    public String getCreate_time() {
        return create_time;
    }
    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }


}
