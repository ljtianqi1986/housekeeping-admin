package com.framework.utils.pay17;

import com.alibaba.fastjson.JSONObject;
import com.biz.model.Singleton.ZkNode;
import com.framework.utils.ConfigUtil;
import com.framework.utils.weixin.pay.MD5;

import java.util.Random;

/**
 * Created by ldd_person on 2017/2/28.
 */
public class Pay17OrderWechatPay {

    private String shop_code;//商户门店标识
    private String order_code;//商户订单号
    private String user_code;//操作员
    private String open_id;//配置公众号的 open_id
    private int total;//订单金额：分
    private String is_raw;//是否原生态
    private String notify_url=ConfigUtil.get("notify_url");//回调通知地址

    private String none_str=CreateNoncestr();//随机字符串
    private String version= ZkNode.getIstance().getJsonConfig().get("version")+""; //接口版本号
    private String dxy_key;
    private String sign;

    public Pay17OrderWechatPay() {
        super();
    }

    public String StitchingParam(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shop_code", this.shop_code);
        jsonObject.put("order_code", this.order_code);
        jsonObject.put("user_code", this.user_code);
        jsonObject.put("total", this.total);
        jsonObject.put("open_id", this.open_id);
        jsonObject.put("is_raw", this.is_raw);
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

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public String getIs_raw() {
        return is_raw;
    }

    public void setIs_raw(String is_raw) {
        this.is_raw = is_raw;
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
