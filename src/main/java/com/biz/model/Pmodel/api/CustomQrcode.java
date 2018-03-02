package com.biz.model.Pmodel.api;

/**
*用户
*/
public class CustomQrcode {

    /**
     * 二维码自定义主键
     */
    private String code;
    /**
     * 门店主键
     */
    private String shop_code;
    /**
     * 用户主键
     */
    private String user_code;
    /**
     * 金额
     */
    private String money;
    /**
     * 备注
     */
    private String remark;
    /**
     * 二维码地址
     */
    private String img_url;
    /**
     * 删除标志0：未删除，1：删除
     */
    private int isdel=0;
    /**
     * 创建时间
     */
    private String create_time;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShop_code() {
        return shop_code;
    }

    public void setShop_code(String shop_code) {
        this.shop_code = shop_code;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
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
}
