package com.biz.service.base;

import com.alibaba.fastjson.JSON;
import com.biz.conf.Global;
import com.biz.model.Hmodel.TRefundLog;
import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Pmodel.PorderMain;
import com.biz.model.Pmodel.basic.PorderSend;
import com.biz.model.Pmodel.basic.Ppics;
import com.biz.model.Pmodel.basic.PwxGoodsStockHistory;
import com.biz.model.Pmodel.pay17.Pay17BackPay;
import com.biz.model.Pmodel.pay17.Pay17SelectPay;
import com.biz.model.Singleton.ZkNode;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.utils.*;
import com.framework.utils.pay17.*;
import com.framework.utils.weixin.WXRefund;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;

/*************************************************************************
 * create by lzq
 *
 * 文件名称 ：OrderServiceI.java 描述说明 ：
 *
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/

@Service("pay17Service")
public class Pay17ServiceImpl extends BaseServiceImpl<Object> implements Pay17ServiceI {


    /**
     * 被扫支付(微信——支付宝被扫)
     * @param order_code 订单号
     * @param total
     * @param author_code 扫码的数字
     * @param device_ip ip
     * @param pay17_shop_code 门店code
     * @param pay17_user_code 收银员code
     * @return
     * @throws Exception
     */
    @Override
    public Pay17SelectPay getPay17_bs(String order_code, int total, String author_code, String device_ip, String pay17_shop_code, String pay17_user_code) throws Exception {
        String pay17key= ZkNode.getIstance().getJsonConfig().get("pay17key").toString();
        //String pay17_shop_code=ZkNode.getIstance().getJsonConfig().get("pay17_shop_code").toString();//门店code
        //String pay17_user_code=ZkNode.getIstance().getJsonConfig().get("pay17_user_code").toString();//收银员code

        String url = "http://open.pay17.cn/phone_scan_pay.do";
        Map<String, String> map = new HashMap<String, String>();
        map.put("order_code", order_code);//商户订单号
        map.put("shop_code", pay17_shop_code);//商户门店标识
        map.put("user_code", pay17_user_code);//收银员标识
        map.put("total", String.valueOf(total));//单位：分
        map.put("author_code", author_code);//扫码支付授权码，设备读取用户的条码或者二维码信息
        map.put("device_ip", device_ip);

        Map<String, String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        String sign = com.framework.utils.pay17.MD5.sign(preStr, "&key=" + pay17key, "utf-8");

        map.put("sign", sign);
        map.put("version", "1.0");

        //调用返回参数
        Map<String,Object> r_map= post(url, map, "UTF-8", "UTF-8");
        //参数赋值对象

        return Select_MapToObject(r_map);
    }


    /**
     * 防钓鱼验证,查询接口
     * @param shop_code 门店标识
     * @param order_code 订单号 (order_code，out_trade_no，任填一个)
     * @param out_trade_no 第三方支付编号
     * @return
     * @throws Exception
     */
    @Override
    public Pay17SelectPay getPay17_select(String shop_code,String order_code,String out_trade_no) throws Exception {
        String pay17key= ZkNode.getIstance().getJsonConfig().get("pay17key").toString();

        String url = "http://open.pay17.cn/phone_orderQuery.do";
        Map<String, String> map = new HashMap<String, String>();
        map.put("shop_code", shop_code);//商户门店标识
        if(!Tools.isEmpty(order_code)){
            map.put("order_code", order_code);//商户订单号
        }
        if(!Tools.isEmpty(out_trade_no)){
            map.put("out_trade_no", out_trade_no);
        }
        map.put("nonce_str", String.valueOf(new Date().getTime()));//返回的随机字符串

        Map<String, String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        String sign = com.framework.utils.pay17.MD5.sign(preStr, "&key=" + pay17key, "utf-8");

        map.put("sign", sign);
        map.put("version", "1.0");

        //调用返回参数
        Map<String,Object> r_map= post(url, map, "UTF-8", "UTF-8");
        //参数赋值对象

        return Select_MapToObject(r_map);
    }

    /**
     * 退款操作
     * @param order_code 商户订单号
     * @param out_refund_no 退款订单号
     * @param shop_code 商户门店标识
     * @param user_code 收银员标识
     * @param refund_fee 退款金额
     * @return
     * @throws Exception
     */
    @Override
    public Pay17BackPay getPay17_back(String order_code,String out_refund_no,String shop_code,String user_code,int refund_fee) throws Exception {
        String pay17key= Global.getPay17key();
        String CERT_URL=Global.getConfig("PAY_BACK_URL");//"D:\\cert\\pay17\\taizhou\\public_key";
        String url = "http://open.pay17.cn/phone_refund.do";
        Map<String, String> map = new HashMap<String, String>();
        map.put("order_code", order_code);//商户订单号
        map.put("out_refund_no", out_refund_no);//商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
        map.put("shop_code", shop_code);//商户门店标识
        map.put("user_code", user_code); //收银员标识
        map.put("refund_fee", String.valueOf(refund_fee) );//退款金额，单位为分，只能为整数，可部分退款
        map.put("nonce_str", String.valueOf(new Date().getTime()));//返回的随机字符串


        Map<String, String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        String sign = com.framework.utils.pay17.MD5.sign(preStr, "&key=" + pay17key, "utf-8");

        String cert_sign = RSAUtil.encrypttoStr(CERT_URL, out_refund_no);
        map.put("sign", sign);
        map.put("cert_sign", cert_sign);//证书加密
        map.put("version", "1.0");

        //调用返回参数
        Map<String,Object> r_map= post(url, map, "UTF-8", "UTF-8");
        //参数赋值对象

        return back_MapToObject(r_map);
    }

    /**
     * 退款查询
     * @param shop_code 商户门店标识
     * @param back_code 退款订单订单号
     * @return
     * @throws Exception
     */
    @Override
    public Pay17BackPay getPay17_backSelect(String shop_code,String back_code) throws Exception {
        String pay17key= ZkNode.getIstance().getJsonConfig().get("pay17key").toString();
        String url = "http://open.pay17.cn/phone_backOrderQuery.do";
        Map<String, String> map = new HashMap<String, String>();
        map.put("shop_code", shop_code);//商户门店标识
        map.put("back_code", back_code);//退款订单订单号
        map.put("nonce_str", String.valueOf(new Date().getTime()));//返回的随机字符串

        Map<String, String> params = SignUtils.paraFilter(map);
        StringBuilder buf = new StringBuilder((params.size() + 1) * 10);
        SignUtils.buildPayParams(buf, params, false);
        String preStr = buf.toString();
        String sign = com.framework.utils.pay17.MD5.sign(preStr, "&key=" + pay17key, "utf-8");

        map.put("sign", sign);
        map.put("version", "1.0");

        //调用返回参数
        Map<String,Object> r_map= post(url, map, "UTF-8", "UTF-8");
        //参数赋值对象

        return back_MapToObject(r_map);
    }

    /**
     * pay17接口
     * @param url
     * @param para
     * @param inEncode
     * @param outEncode
     * @return
     */
    public Map<String,Object> post(String url, Map<String, String> para, String inEncode,
                                          String outEncode) {
        Map<String,Object> returnParam = new HashMap<String,Object>();
        // 创建默认的httpclient实例
        CloseableHttpClient httpclient = null;
        HttpEntity httpEntity = null;
        String returnValue = null;
        try {
            httpclient = HttpClients.createDefault();
            // 创建参数队列
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            if (para != null && !para.isEmpty()) {
                for (String key : para.keySet()) {
                    params.add(new BasicNameValuePair(key, para.get(key)));
                }
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,
                    inEncode);
            org.apache.http.client.methods.HttpPost httppost = new org.apache.http.client.methods.HttpPost(url);
            httppost.setEntity(entity);
            // 设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(30000).setConnectTimeout(30000).build();
            httppost.setConfig(requestConfig);
            CloseableHttpResponse response = httpclient.execute(httppost);
            System.out.println("返回code=="
                    + response.getStatusLine().getStatusCode());
            httpEntity = response.getEntity();
            if (httpEntity != null) {
                returnValue = EntityUtils.toString(httpEntity, outEncode);
                //处理返回参数
                Map<String,Object> param = JSONObject.fromObject(returnValue);
                returnParam=param;

            }else{
                //logger.error("===调用pay17网页付接口出现异常了===httpEntity==null");
                returnParam.put("return_code", "0");
                returnParam.put("return_info", "获取参数失败，请刷新后重试！");
                returnParam.put("err_code", "");
            }
            EntityUtils.consume(httpEntity);
            response.close();
        } catch (Exception e) {
            returnParam.put("return_code", "0");
            returnParam.put("return_info", "获取参数失败，请刷新后重试！");
            returnParam.put("err_code", "");
        } finally {
            try
            {
                httpclient.close();
            } catch (Exception e)
            {
                returnParam.put("return_code", "0");
                returnParam.put("return_info", "获取参数失败，请刷新后重试！");
                returnParam.put("err_code", "");
            }
            return returnParam;
        }
    }


    private static Pay17SelectPay Select_MapToObject(Map<String, Object> map) throws Exception {
        Pay17SelectPay pay=new Pay17SelectPay();

        try {
            if(map!=null){
                int ReturnCode=Integer.parseInt(map.get("return_code").toString());
                pay.setReturn_code(ReturnCode);
                pay.setReturn_info(map.get("return_info").toString());

                if(map.containsKey("trade_state")) {
                    pay.setTrade_state(Integer.parseInt(map.get("trade_state").toString()));//int 0：未生效 1：交易成功 2：错误 3：已退款 4：部分退款 5：用户支付中
                }
                pay.setShop_code(map.get("shop_code").toString());//商户门店标识
                pay.setUser_code(map.get("user_code").toString());//收银员标识
                pay.setTotal((int)Double.parseDouble((map.get("total").toString())));//int(9) 订单金额 单位：分
                pay.setOrder_code(map.get("order_code").toString());//商户订单号
                if(map.containsKey("customer_id")) {
                    pay.setCustomer_id(map.get("customer_id").toString());//支付宝或微信的用户ID
                }
                pay.setOut_trade_no(map.get("out_trade_no").toString());//第三方交易单号 out_trade_no
                pay.setIs_account(Integer.parseInt(map.get("is_account").toString()));//0非二清，1二清
                pay.setAccount_id(map.get("account_id").toString());//收款账号
                if(map.containsKey("trade_type")) {
                    pay.setTrade_type(map.get("trade_type").toString());//支付类型
                }

            }
        } catch (Exception e) {
        }
        return pay;
    }

    private static Pay17BackPay back_MapToObject(Map<String, Object> map) throws Exception {
        Pay17BackPay pay=new Pay17BackPay();

        try {
            if(map!=null){
                int ReturnCode=Integer.parseInt(map.get("return_code").toString());
                pay.setReturn_code(ReturnCode);
                pay.setReturn_info(map.get("return_info").toString());

                /*if(ReturnCode==1){
                    pay.setShop_code(map.get("shop_code").toString());
                    pay.setBack_total((int)Double.parseDouble((map.get("back_total").toString())));
                    pay.setOrder_code(map.get("order_code").toString());
                    pay.setTrade_state(Integer.parseInt(map.get("trade_state").toString()));
                    if(map.containsKey("error_msg")) {
                        pay.setError_msg(map.get("error_msg").toString());
                    }
                    pay.setBack_code(map.get("back_code").toString());
                    pay.setCreate_time(map.get("create_time").toString());
                }*/



            }
        } catch (Exception e) {
        }
        return pay;
    }


}
