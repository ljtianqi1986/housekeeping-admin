package com.framework.utils.pay17;

import com.alibaba.fastjson.JSONObject;
import com.biz.model.Singleton.ZkNode;
import com.framework.utils.ConfigUtil;
import com.framework.utils.weixin.pay.MD5;

import java.util.Random;

/**
 * Created by ldd_person on 2017/2/28.
 */
public class Pay17OrderRefundQuery {

    private String shop_code;//商户门店标识
    private String back_code;//退款订单号
    private String none_str=CreateNoncestr();//随机字符串
    private String version= ZkNode.getIstance().getJsonConfig().get("version")+""; //接口版本号
    private String dxy_key;
    private String sign;

    public Pay17OrderRefundQuery() {
        super();
    }

    public String StitchingParam(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shop_code", this.shop_code);
        jsonObject.put("back_code", this.back_code);
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

    public String getBack_code() {
        return back_code;
    }

    public void setBack_code(String back_code) {
        this.back_code = back_code;
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
