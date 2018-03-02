package com.biz.controller.pay17;

import com.alibaba.fastjson.JSON;
import com.framework.controller.BaseController;
import com.framework.utils.pay17.*;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

/**
 * desc:Pay17支付订单相关操作接口调用示例
 * Created by ldd_person on 2017/2/28.
 */
@Controller
@RequestMapping("/pay17/order")
public class Pay17OrderController extends BaseController {

    /**
     * pay17支付查询
     * @param shop_code 商户门店标识
     * @param order_code 商户订单号
     * @param out_trade_no  第三方交易订单号
     */
    @RequestMapping("/order/{shop_code}/query_{order_code}")
    public void doQuery(HttpServletResponse response,@PathVariable String shop_code,@PathVariable String order_code,@RequestParam(value="out_trade_no",required = false)String out_trade_no){
        Pay17OrderQuery param = new Pay17OrderQuery();
        param.setDxy_key("");
        param.setOrder_code(order_code);
        param.setOut_trade_no(out_trade_no);
        param.setShop_code(shop_code);
        JSONObject jsonObject =HttpRequestUtil.httpRequest("http://open.pay17.cn/phone_orderQuery.do", "POST", JSON.toJSONString(param.StitchingParam()));
        if(Integer.parseInt(jsonObject.get("return_code").toString())==1){

        }else{
            jsonObject.put("success",false);
            jsonObject.put("msg",jsonObject.get("err_code").toString()+":"+jsonObject.get("return_info").toString());
        }
        writeJson(jsonObject,response);
    }


    /**
     * pay17退款查询
     * @param shop_code 商户门店标识
     * @param back_code 退款订单号
     */
    @RequestMapping("/order/{shop_code}/refundquery_{back_code}")
    public void doRefundQuery(HttpServletResponse response,@PathVariable String shop_code,@PathVariable String back_code){
        Pay17OrderRefundQuery param = new Pay17OrderRefundQuery();
        param.setDxy_key("");
        param.setBack_code(back_code);
        param.setShop_code(shop_code);
        JSONObject jsonObject =HttpRequestUtil.httpRequest("http://open.pay17.cn/phone_backOrderQuery.do", "POST", JSON.toJSONString(param.StitchingParam()));
        if(Integer.parseInt(jsonObject.get("return_code").toString())==1){
            //退款成功
        }else{
            jsonObject.put("success",false);
            jsonObject.put("msg",jsonObject.get("err_code").toString()+":"+jsonObject.get("return_info").toString());
        }
        writeJson(jsonObject,response);
    }


    /**
     * pay17刷卡付
     * @param shop_code 商户门店标识
     * @param  order_code 商户订单号
     */
    @RequestMapping("/order/{shop_code}/scanPay_{order_code}")
    public void doScanPay(HttpServletResponse response,@PathVariable String shop_code,@PathVariable String order_code,
                          @RequestParam("user_code")String user_code,@RequestParam("total")int total,
                          @RequestParam("author_code")String author_code,@RequestParam("device_ip")String device_ip){
        Pay17OrderScanPay param = new Pay17OrderScanPay();
        param.setDxy_key("");
        param.setShop_code(shop_code);
        param.setOrder_code(order_code);
        param.setUser_code(user_code);
        param.setTotal(total);
        param.setAuthor_code(author_code);
        param.setDevice_ip(device_ip);
        JSONObject jsonObject =HttpRequestUtil.httpRequest("http://open.pay17.cn/phone_scan_pay.do", "POST", JSON.toJSONString(param.StitchingParam()));
        if(Integer.parseInt(jsonObject.get("return_code").toString())==1&&Integer.parseInt(jsonObject.get("trade_state").toString())==1){
            //交易成功
        }else{
            jsonObject.put("success",false);
            jsonObject.put("msg",jsonObject.get("err_code").toString()+":"+jsonObject.get("return_info").toString());
        }
        writeJson(jsonObject,response);
    }


    /**
     * pay17用户扫码付生成二维码
     * @param shop_code 商户门店标识
     * @param  order_code 商户订单号
     */
    @RequestMapping("/order/{shop_code}/preCreatePay_{order_code}")
    public void doPreCreatePay(HttpServletResponse response,@PathVariable String shop_code,@PathVariable String order_code,
                          @RequestParam("user_code")String user_code,@RequestParam("total")int total,@RequestParam("device_info")String device_info,
                          @RequestParam("device_ip")String device_ip,@RequestParam("trade_type")String trade_type){
        Pay17OrderPreCreatePay param = new Pay17OrderPreCreatePay();
        param.setDxy_key("");
        param.setShop_code(shop_code);
        param.setOrder_code(order_code);
        param.setUser_code(user_code);
        param.setTotal(total);
        param.setDevice_info(device_info);
        param.setDevice_ip(device_ip);
        param.setTrade_type(trade_type);
        JSONObject jsonObject =HttpRequestUtil.httpRequest("http://open.pay17.cn/phone_pre_create_pay.do", "POST", JSON.toJSONString(param.StitchingParam()));
        if(Integer.parseInt(jsonObject.get("return_code").toString())==1){
            //生成成功
        }else{
            jsonObject.put("success",false);
            jsonObject.put("msg",jsonObject.get("err_code").toString()+":"+jsonObject.get("return_info").toString());
        }
        writeJson(jsonObject,response);
    }


    /**
     * pay17微信支付
     * @param shop_code 商户门店标识
     * @param  order_code 商户订单号
     */
    @RequestMapping("/order/{shop_code}/wechatPay_{order_code}")
    public void doWechatPay(HttpServletResponse response,@PathVariable String shop_code,@PathVariable String order_code,
                               @RequestParam("user_code")String user_code,@RequestParam(value="open_id",required = true)String open_id,
                               @RequestParam(value="total",required = true)int total,@RequestParam("is_raw")String is_raw){
        Pay17OrderWechatPay param = new Pay17OrderWechatPay();
        param.setDxy_key("");
        param.setShop_code(shop_code);
        param.setOrder_code(order_code);
        param.setUser_code(user_code);
        param.setTotal(total);
        param.setOpen_id(open_id);
        param.setTotal(total);
        param.setIs_raw(is_raw);
        JSONObject jsonObject =HttpRequestUtil.httpRequest("http://open.pay17.cn/phone_wechat_pay.do", "POST", JSON.toJSONString(param.StitchingParam()));
        if(Integer.parseInt(jsonObject.get("return_code").toString())==1){
            //生成成功
        }else{
            jsonObject.put("success",false);
            jsonObject.put("msg",jsonObject.get("err_code").toString()+":"+jsonObject.get("return_info").toString());
        }
        writeJson(jsonObject,response);
    }


    /**
     * pay17微信支付
     * @param shop_code 商户门店标识
     * @param  order_code 商户订单号
     */
    @RequestMapping("/order/{shop_code}/refund_{order_code}")
    public void doRefund(HttpServletResponse response,@PathVariable String shop_code,@PathVariable String order_code,
                            @RequestParam("user_code")String user_code,@RequestParam(value="out_refund_no")String out_refund_no,
                            @RequestParam(value="refund_fee",required = true)int refund_fee) throws Exception {
        Pay17OrderRefund param = new Pay17OrderRefund();
        param.setDxy_key("");
        param.setShop_code(shop_code);
        param.setOrder_code(order_code);
        param.setUser_code(user_code);
        param.setOut_refund_no(out_refund_no);
        param.setRefund_fee(refund_fee);
        JSONObject jsonObject =HttpRequestUtil.httpRequest("http://open.pay17.cn/phone_refund.do", "POST", JSON.toJSONString(param.StitchingParam()));
        if(Integer.parseInt(jsonObject.get("return_code").toString())==1){
            //生成成功
        }else{
            jsonObject.put("success",false);
            jsonObject.put("msg",jsonObject.get("err_code").toString()+":"+jsonObject.get("return_info").toString());
        }
        writeJson(jsonObject,response);
    }
}
