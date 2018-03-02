package com.biz.controller.api;

import com.alibaba.fastjson.JSON;
import com.biz.conf.Global;
import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Pmodel.api.OrderMain90;
import com.biz.model.Pmodel.api.Result;
import com.biz.model.Pmodel.api.Shop;
import com.biz.model.Pmodel.pay17.Pay17BackPay;
import com.biz.service.QT.QtUserServiceI;
import com.biz.service.api.*;
import com.biz.service.base.Pay17ServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.CryptTool;
import com.framework.utils.DateUtil;
import com.framework.utils.StringUtil;
import com.framework.utils.URLConectionUtil;
import com.framework.utils.weixin.HttpRequestUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付相关接口
 *
 * Created by lzq on 2017/2/5.
 */
@Controller
@RequestMapping("/api/QToayWx")
public class PayWxController extends BaseController{

    @Resource(name = "phoneUserService")
    private PhoneUserServiceI phoneUserService;

    @Resource(name = "orderMainService")
    private OrderMainServiceI orderMainService;

    @Resource(name = "wxUtilService")
    private WxUtilServiceI wxUtilService;

    @Resource(name = "myOrderService")
    private MyOrderServiceI myOrderService;

    @Resource(name = "qtUserService")
    private QtUserServiceI qtUserService ;

    @Resource(name = "pay17Service")
    private Pay17ServiceI pay17Service;

    @Resource(name = "wxTemplateService")
    private WxTemplateServiceI wxTemplateService;

//    /**
//     * QT端服务费-微信刷卡付
//     *
//     * @author zhengXin 2016-11-03 修改接口方法
//     * 增加接口参数other_order_code 用于接收对方原90平台订单唯一标识并保存进入数据库
//     * 在return_info返回信息中，新增返回用户手机号和服务费数据，用逗号隔开，第一个是支付方式，第二个是手机号，第三个是服务费
//     */
//    @RequestMapping(value = "/phone_wx_scan_qt")
//    public @ResponseBody
//    Result phone_wx_scan_qt(String shop_id, String user_code, String author_code, String total, String order_code,
//                            String device_info, String device_ip, String cash_total, String open_id, String other_order_code){
//        Result result = new Result();
//        result.setReturn_code(0);
//        /***********对传入金额处理*************/
//        //此处将类型转换方法统一到baseController
//        double double_total = 0.00, double_cash_total = 0.00;//分
//        int int_cash_total=0; //分
//        try {
//            double_total = money_convert(total);
//            double_cash_total = money_convert(cash_total);
//            int_cash_total = money_convert_Int(cash_total);
//        } catch (Exception e) {
//            result.setReturn_info("金额错误，金额为" + total);
//            logger.error("QT端服务费-微信刷卡付金额错误，金额为" + total);
//            return result;
//        }
//        try {
//            Shop shop = phoneUserService.getShopBySid(shop_id);
//            if(shop == null ){
//                result.setReturn_info("门店不存在");
//                logger.error("门店不存在=shop_id="+shop_id);
//                return result;
//            }
//            ////此处修改为查询数量
//            Integer orderMain90Count = phoneUserService.getOrderMainCount(order_code);
//            if(orderMain90Count.intValue() >0){
//                result.setReturn_info("订单已存在");
//                logger.error("订单已存在=order_code="+order_code);
//                return result;
//            }
//            ////统一为久零币支付方法
//            OrderMain orderMain90 = new OrderMain();
//            int return_code =phoneUserService.doNineZeroPay(open_id, order_code, cash_total);
//            //跟新支付方式
//            orderMain90.setCashPayType(1);
//            //调用店小翼支付接口
//            String return_info="",error_pay_msg="",pay_user_id="", pay_time= DateUtil.getTime();
//            //90币付款失败，调用店小翼支付接口
//            if(return_code==0)
//            {
//                String url = Global.getConfig("KQ_URL")+"phone_swiftPassScanPay.do?shop_id="+shop.getDxy_code().trim()+"&user_code="+shop.getDxy_person_code().trim()+"&author_code="+author_code
//                        +"&total="+int_cash_total+"&order_code="+order_code+"&device_info="+device_info+"&device_ip="+device_ip;
//                String back_result = HttpURL(url);
//
//                JSONObject json = JSONObject.fromObject(back_result);
//                return_info=json.getString("return_info");
//                error_pay_msg=json.getString("error_pay_msg");
//                pay_user_id=json.getString("pay_user_id");
//                return_code = json.getInt("return_code");
////                orderMain90.setCash_payType(0);
//                orderMain90.setCashPayType(0);
//            }
//            //判断1为微信，2为支付宝
//            String trade_type = "";
//            if(author_code.startsWith("1")){ //微信
//                trade_type = "MICROPAY";
//            }else if(author_code.startsWith("2")){ //支付宝
//                trade_type = "ZFB-MICROPAY";
//            }else if(author_code.startsWith(""))//久零币
//            {
//                trade_type = "JLB";
//            }
//            //1.是会员，直接推送模板消息，订单-兴业手续费，久零券
//
//            orderMain90.setId(order_code);
//            orderMain90.setAllTotal(double_cash_total);
//            orderMain90.setPayTotal(double_cash_total);
//            orderMain90.setCouponTotal(0);
//            orderMain90.setCardCount(0);
//            orderMain90.setOpenId(open_id);
//            orderMain90.setUserCode(user_code);
//            orderMain90.setShopCode(shop_id);
//            orderMain90.setBrandCode(shop.getBrand_code());
//            orderMain90.setPaymentRoute(trade_type);
//            orderMain90.setPayType(1);
//            orderMain90.setGiftCoupon(0);
//            orderMain90.setPayCoupon(double_total);
//            orderMain90.setOtherOrderCode(other_order_code);
//            if(return_code == 0){ //错误
//                result.setReturn_info(return_info);
//                orderMain90.setState("2");
//                orderMain90.setErrorPayMsg(error_pay_msg);
//            }else if(return_code == 1){ //正确
//                BaseUser baseUser = phoneUserService.getBaseUserByOpen_id(open_id);
//                result.setReturn_code(1);
//                result.setReturn_info(trade_type+","+baseUser.getPhone()+","+(double_cash_total /100));
//                orderMain90.setState("1");
//                orderMain90.setPayTime(new Date());
//                //用户通知
//                orderMain90.setBuyUserId(pay_user_id);
//                orderMain90.setGiftCoupon(double_cash_total);
//                Map<String,String> map1=new HashMap<>();
//                JSONObject jSONObject1 = new JSONObject();
//                jSONObject1.put("sceneType","1");
//                jSONObject1.put("open_id",open_id);
//                jSONObject1.put("total_90",double_total);
//                jSONObject1.put("shop_code",shop_id);
//                jSONObject1.put("source_code",order_code);
//                jSONObject1.put("brand_code",shop.getBrand_code());
//                jSONObject1.put("user_code",user_code);
//                map1.put("json", jSONObject1.toString());
//                System.out.print(jSONObject1.toString());
//                String requestUrl = ConfigUtil.get("BALANCE_90")+"orther_minus_balance_90.do";
//                String x = URLConectionUtil.httpURLConnectionPostDiy(requestUrl, map1);
//                x = URLDecoder.decode(x, "utf-8");
//                if(x==null||x.trim().equals("")){
//                    result.setReturn_info("减劵失败");
//                }else {
//                    Map<String, Object> map2 = JSON.parseObject(x, Map.class);
//                    if(map2!=null && map2.get("return_code").equals("1"))  //发劵成功
//                    {}else
//                    {
//                        result.setReturn_info("减劵失败");
//                    }
//                }
//            }
//            phoneUserService.insertOrderMain90(orderMain90);
//            //90体验店收单发送模板消息
////            phoneUserService.send_pay_template(open_id, orderMain90,pay_time);
//        }catch (Exception e){
//            String msg = e.getMessage(), async = "";
//            if(StringUtils.isNotBlank(msg) && msg.length()>30){
//                msg = msg.substring(0, 29);
//            }
//            result.setReturn_code(0);
//            result.setReturn_info("错误-" + msg);
//            logger.error("=QT端服务费-微信刷卡付=报错=" + e, e.fillInStackTrace());
//        }
//        return result;
//    }


    /**
     * QT端体验店退款
     */
    @RequestMapping(value = "/phone_wx_qt_back_money")
    public void phone_wx_qt_back_money(String order_code,String total,String user_code, HttpServletResponse response){
        Result result = new Result();
        OrderMain90 orderMain90=null;
        int return_code =1;
        String return_info="";
        //判断是商品收银还是直接收银
        try {

            TorderMain torderMain = qtUserService.getOrderMainById(order_code);
            orderMain90 = orderMainService.getOrderMain90ByCode(order_code);
            if(orderMain90 != null)
            {
                if(orderMain90.getBalance_type()!=3)    //混合支付不可退款
                {
                    String tSysUserId = orderMainService.getSysUserIdByOpenId(orderMain90.getOpen_id());
                    Double amount = orderMainService.getBalanceByOrderNum(order_code.trim());
                    Shop shop = orderMainService.getShopBySid(orderMain90.getShop_code());

                    /***********对传入金额处理*************/
                    if(return_code == 1)
                    {
                        //                int int_total = 0; //分
                        //                try {
                        //                    int_total = Integer.valueOf(total*100);
                        //                } catch (Exception e) {
                        //                    result.setReturn_code(0);
                        //                    result.setReturn_info("金额错误，金额为"+total);
                        //                    return_code = 0;
                        //                    return_info="金额错误，金额为"+total;
                        //                }

                        if(return_code == 1)
                        {
                            //店小翼key
                        /*String key = getKeyByParam(shop.getDxy_code(),shop.getDxy_person_code(),order_code);
                        //判断线上线下支付
                        if("offline".equals(orderMain90.getTrade_type()) || "UNIONPAY".equals(orderMain90.getTrade_type()))//线下支付
                        {
                            return_code = 1;
                        }else{
                            *//****************调用店小翼退款接口**********************//*
                            String url = Global.getConfig("KQ_URL")+"phone_swiftPassBack.do?order_code="+order_code+"&total="+(int)orderMain90.getOrder_total()+"&user_code="+shop.getDxy_person_code()+"&backcode="+key;
                            String back_result = HttpURL(url);
                            JSONObject json = JSONObject.fromObject(back_result);
                            logger.error("=QT端体验店退款,退款=json="+json);
                            return_info=json.getString("return_info");
                            return_code = json.getInt("return_code");
                        }*/

                            if("offline".equals(orderMain90.getTrade_type()) || "UNIONPAY".equals(orderMain90.getTrade_type()))//线下支付
                            {
                                return_code = 1;
                            }else{
                                Pay17BackPay backPay=pay17Service.getPay17_back(order_code,order_code,shop.getDxy_code(),shop.getDxy_person_code(),(int)orderMain90.getOrder_total());
                                return_info=backPay.getReturn_info();
                                return_code =backPay.getReturn_code();
                            }

                        }
                    }
                    if(return_code == 0){ //错误
                        result.setReturn_code(0);
                        result.setReturn_info(return_info);
                    }else if(return_code == 1){ //正确
                        return_code = 0 ; /// 重新定位
                        /****************判断是否需要退久零贝*************************/
                        if(amount!=null && amount.doubleValue()>0 && !StringUtil.isNullOrEmpty(tSysUserId))
                        {
                            Map<String,String> map90=new HashMap<>();
                            JSONObject jSONObject90 = new JSONObject();
                            jSONObject90.put("userId",tSysUserId);
                            jSONObject90.put("state","1");
                            jSONObject90.put("orderNum",order_code);
                            jSONObject90.put("source","4");
                            jSONObject90.put("amount",amount);
                            map90.put("json", jSONObject90.toString());
                            System.out.print(jSONObject90.toString());
                            String requestUrl = Global.getConfig("QT_Url")+"api/balance/operUserCoin90.ac";
                            String x = URLConectionUtil.httpURLConnectionPostDiy(requestUrl, map90);
                            x = URLDecoder.decode(x, "utf-8");
                            if(!StringUtil.isNullOrEmpty(x) )
                            {
                                Map<String, Object> map2 = JSON.parseObject(x, Map.class);
                                if(map2!=null && map2.get("flag").equals("0"))
                                {
                                    return_code=1;
                                }
                            }
                        }else{
                            return_code = 1;
                        }
                        if(return_code == 0) { //错误
                            result.setReturn_code(0);
                            result.setReturn_info(return_info);
                        }else {
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                            result.setReturn_code(1);
                            result.setReturn_info(return_info);
                            Map<String, Object> params = new HashMap<>();
                            params.put("code", order_code);
                            params.put("back_user_code", user_code);
                            params.put("state", 3);
                            params.put("back_code", return_info);
                            params.put("back_time", DateUtil.getTime());
                            params.put("create_time", df.format(new Date()));//修改创建时间为修改时间
                            orderMainService.updateWhereOrderMain90(params);
                            String open_id = orderMain90.getOpen_id();
                            double double_total = 0;
                            if(orderMain90.getBalance_90()>0)
                            {
                                double_total=orderMain90.getBalance_90();
                            }else if(orderMain90.getBalance_experience_90()>0)
                            {
                                double_total=orderMain90.getBalance_experience_90();
                            }else if(orderMain90.getBalance_shopping_90()>0)
                            {
                                double_total=orderMain90.getBalance_shopping_90();
                            }
                            //orderMain90.getPay_90();
                            /******************加久零券**************************/
                            Map<String,String> map1=new HashMap<>();

                            JSONObject jSONObject1 = new JSONObject();
                            jSONObject1.put("open_id",open_id);
                            jSONObject1.put("balance_90",double_total);
                            jSONObject1.put("shop_code",orderMain90.getShop_code());
                            jSONObject1.put("order_code",orderMain90.getCode());
                            jSONObject1.put("brand_code",orderMain90.getBrand_code());
                            jSONObject1.put("user_code",user_code);
                            jSONObject1.put("type", "1");
                            jSONObject1.put("source", "5");//退款
                            jSONObject1.put("source_msg", "用户退款");
                            jSONObject1.put("state",2);
                            jSONObject1.put("commission","0");
                            jSONObject1.put("orderState","1");
                            jSONObject1.put("orderTotal","0");
                            jSONObject1.put("tradeType",orderMain90.getTrade_type());
                            jSONObject1.put("ticketType",orderMain90.getBalance_type());
                            map1.put("json", jSONObject1.toString());
                            System.out.print(jSONObject1.toString());
                            String requestUrl = Global.getConfig("QT_Url")+"api/balance/operUserBalance90.ac";
                            String x = URLConectionUtil.httpURLConnectionPostDiy(requestUrl, map1);
                            x = URLDecoder.decode(x, "utf-8");
                            if(x==null||x.trim().equals("")){
                                result.setReturn_code(0);
                                result.setReturn_info("加劵失败");
                            }else {
                                Map<String, Object> map2 = JSON.parseObject(x, Map.class);
                                if(map2!=null && map2.get("flag").equals("0"))  //发劵成功
                                {
                                    result.setReturn_code(1);
                                    result.setReturn_info("退款成功");
                                }else
                                {
                                    result.setReturn_code(0);
                                    result.setReturn_info("加劵失败");
                                }
                            }

                            //发送模板消息
                            try{
                                //wxUtilService.send_refund_template(return_info,orderMain90,int_total);
                                wxTemplateService.send_kf_template(open_id,"订单"+orderMain90.getCode()+result.getReturn_info());
                            }catch (Exception e){

                            }


                        }
                    }
                }else{
                    result.setReturn_code(0);
                    result.setReturn_info("混合支付不可退款");
                }

            }else if(torderMain != null)
            {
                result = qtUserService.goodsCashBackMoney(order_code, user_code);
            }else{
                result.setReturn_code(0);
                result.setReturn_info("订单不存在");
                return_info="订单不存在";
                return_code = 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            String msg = e.getMessage();
            if(StringUtils.isNotBlank(msg) && msg.length()>30){
                msg = msg.substring(0, 29);
            }
            result.setReturn_code(0);
            result.setReturn_info("错误-" + msg);
        }
        Map<String ,Object> mapReturn = new HashMap<>();

        mapReturn.put("return_code",result.getReturn_code()+"");
        mapReturn.put("return_info",result.getReturn_info());
        mapReturn.put("return_data",result.getReturn_data());

        writeJson(mapReturn,response);
    }


    /**
     * 订单生成
     */
    @RequestMapping(value = "/Generate_Order")
    public void Generate_Order(HttpServletResponse response,String jsonSting,
                               String type) throws Exception {
        Map<String, Object> hashMap = new HashMap<String, Object>();
        if("1".equals(type)){
            hashMap=myOrderService.addGenerateOrderMain90(jsonSting);
        }else if("2".equals(type))
        {
            hashMap=myOrderService.doProductOrder(jsonSting);
        }else {
            hashMap.put("result","fail");
            hashMap.put("orderCode","");
            hashMap.put("msg","订单类型不匹配");
        }
        writeJson(hashMap, response);
    }


    /**
     * 付款码验证
     * @param response
     * @param shop_code
     * @param only_code 付款码
     * @param total
     * @param iscoin 是否使用9贝抵扣(0：使用 1：不使用)
     * @throws Exception
     */
    @RequestMapping(value = "/VerificationTicket")
    public void VerificationTicket(HttpServletResponse response,String shop_code,String only_code,String total,String iscoin) throws Exception {

        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap=myOrderService.Verification_Payment_Code(shop_code.trim(),only_code.trim(),total.trim(),iscoin.trim());
        writeJson(hashMap, response);
    }


    /**
     * 订单付款
     * @param response
     * @param jsonSting

     * @throws Exception
     */
    @RequestMapping(value = "/payOrderMain")
    public void payOrderMain(HttpServletResponse response,String jsonSting) throws Exception {

        Map<String, Object> hashMap = myOrderService.PayOrderMain90(jsonSting.trim());

        writeJson(hashMap, response);
    }


    /***
     * 支付
     * @param
     * @param response
     */
    @RequestMapping(value="/toDoCashPay")
    public void toDoCashPay(String isCoinPay, String orderCode,String userPayCode, String shopId, String device_info, String device_ip, HttpServletResponse response)throws Exception{
        System.out.println("isCoinPay="+isCoinPay+"=orderCode="+orderCode+"=userPayCode="+userPayCode+"=shopId="+shopId+"=device_info="+device_info+"=device_ip="+device_ip);
        Map<String,Object> map1 = qtUserService.toDoCashPay(isCoinPay,orderCode, userPayCode, shopId, device_info, device_ip);

        map1.put("msg", map1.get("return_msg").toString());

        writeJson(map1,response);
    }




    /**
     * QT端服直接付款验证
     *
     * @author lzq
     *
     */
    @RequestMapping(value = "/payOrderMainAgain")
    public void payOrderMainAgain(HttpServletResponse response,String jsonSting)throws Exception{


        Map<String, Object> hashMap = myOrderService.payOrderMainAgain(jsonSting.trim());

        writeJson(hashMap, response);

    }



    /**
     * QT端服商品收银付款验证
     *
     * @author lzq
     *
     */
    @RequestMapping(value = "/payGoodsOrderAgain")
    public void payGoodsOrderAgain(HttpServletResponse response,String jsonSting)throws Exception{


        Map<String, Object> hashMap = myOrderService.payGoodsOrderAgain(jsonSting.trim());

        writeJson(hashMap, response);

    }


    private String getKeyByParam(String dxyCode, String dxyPersonCode, String orderCode)
    {
        String key = "";
        try {
            StringBuffer key_sb = new StringBuffer();
            key_sb.append("SHOPCODE=");
            key_sb.append(dxyCode);
            key_sb.append("&USERCODE=");
            key_sb.append(dxyPersonCode);
            key_sb.append("&ORDERCODE=");
            key_sb.append(orderCode);
            System.err.println(key_sb);
            key = CryptTool.md5Digest(key_sb.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return key;
    }

    /**
     * 取消QT订单
     * @param type 订单类型(1:直接付款，2:商品收银)
     * @param jsonString
     * @return
     */
    @RequestMapping(value = "/cancleQTOrder", method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> cancleQTOrder(String jsonString){
        Map<String,Object> result ;
//            String requestUrl=Global.get("jnUrl2")+"/QToayWx/";
//            if(type.equals("1")){
//                requestUrl+="payOrderMainAgain.ac";
//            }else if(type.equals("2")){
//                requestUrl+="payGoodsOrderAgain.ac";
//            }

        System.out.println("###################qu_xiao_din_dan:"+jsonString);
                //用户支付失败，执行取消订单操作
        result =myOrderService.cancleQTOrder(jsonString);
                //result.put("return_code",0);
                //result.put("msg","订单取消成功！");
                return result;

        //JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl,"POST",jsonString);
//            if(jsonObject.get("return_code").equals("1")){
//                //验证成功说明已经付款，无法取消
//                result.put("return_code",0);
//                result.put("msg","用户支付成功，无法取消，如需退款，请走退款流程！");
//                return result;
//            }else if(jsonObject.get("return_code").equals("1")){
//                //用户支付失败，执行取消订单操作
//                myOrderService.cancleQTOrder(jsonString);
//
//                return result;
//            }else{
//                result.put("return_code",0);
//                result.put("msg","未知错误！");
//                return result;
//            }
    }

}
