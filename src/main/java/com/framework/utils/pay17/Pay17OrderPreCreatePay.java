package com.framework.utils.pay17;

import com.alibaba.fastjson.JSONObject;
import com.biz.model.Singleton.ZkNode;
import com.framework.utils.ConfigUtil;
import com.framework.utils.weixin.pay.MD5;

import java.util.Random;

/**
 * Created by ldd_person on 2017/2/28.
 */
public class Pay17OrderPreCreatePay {

    private String shop_code;//商户门店标识
    private String order_code;//商户订单号
    private String user_code;//操作员
    private int total;//订单金额：分
    private String device_info;//设备号
    private String device_ip;//终端设备IP
    private String trade_type;//支付方式
    private String notify_url=ConfigUtil.get("notify_url");//回调通知地址

    private String none_str=CreateNoncestr();//随机字符串
    private String version= ZkNode.getIstance().getJsonConfig().get("version")+""; //接口版本号
    private String dxy_key;
    private String sign;

    public Pay17OrderPreCreatePay() {
        super();
    }

    public String StitchingParam(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shop_code", this.shop_code);
        jsonObject.put("order_code", this.order_code);
        jsonObject.put("user_code", this.user_code);
        jsonObject.put("total", this.total);
        jsonObject.put("device_info", this.device_info);
        jsonObject.put("device_ip", this.device_ip);
        jsonObject.put("trade_type", this.trade_type);
        jsonObject.put("notify_url", this.notify_url);
        jsonObject.put("none_str", this.none_str);
        jsonObject.put("version", this.version);
        SignUtil.getSign(jsonObject,dxy_key);
        jsonObject.put("sign", sign);
        return jsonObject.toString();
    }



    /**
     * 生成随机数
     *
     * @return
     */
    private static String CreateNoncestr()
    {
        Random random = new Random();
        return MD5.GetMD5String(String.valueOf(random.nextInt(10000)));
    }

    public String getShop_code() {
        return shop_code;
    }

    public void setShop_code(String shop_code) {
        this.shop_code = shop_code;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getDevice_ip() {
        return device_ip;
    }

    public void setDevice_ip(String device_ip) {
        this.device_ip = device_ip;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getNone_str() {
        return none_str;
    }

    public void setNone_str(String none_str) {
        this.none_str = none_str;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDxy_key() {
        return dxy_key;
    }

    public void setDxy_key(String dxy_key) {
        this.dxy_key = dxy_key;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
