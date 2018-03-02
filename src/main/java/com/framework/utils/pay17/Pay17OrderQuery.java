package com.framework.utils.pay17;

import com.alibaba.fastjson.JSONObject;
import com.biz.model.Singleton.ZkNode;
import com.framework.utils.weixin.pay.MD5;

import java.util.Random;

/**
 * Created by ldd_person on 2017/2/28.
 */
public class Pay17OrderQuery {

    private String shop_code;//商户门店标识
    private String order_code;//商户订单号
    private String out_trade_no;//第三方交易订单号 非必须
    private String none_str=CreateNoncestr();//随机字符串
    private String version= ZkNode.getIstance().getJsonConfig().get("version")+""; //接口版本号
    private String dxy_key;
    private String sign;

    public Pay17OrderQuery() {
        super();
    }

    public String StitchingParam(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shop_code", this.shop_code);
        jsonObject.put("order_code", this.order_code);
        jsonObject.put("out_trade_no", this.out_trade_no);
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

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
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
