package com.biz.controller.POS;

import com.alibaba.fastjson.JSON;
import com.biz.model.Pmodel.PhoneUserIntegral;
import com.biz.model.Pmodel.PorderMainUnion;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.BaseUser;
import com.biz.model.Pmodel.api.PayScene;
import com.biz.model.Pmodel.api.Result;
import com.biz.model.Pmodel.api.Shop;
import com.biz.model.Pmodel.basic.Base90Detail;
import com.biz.model.Pmodel.basic.Pbrand;
import com.biz.model.Pmodel.pay17.Pay17SelectPay;
import com.biz.model.Singleton.ZkNode;
import com.biz.service.api.OrderMainUnionServiceI;
import com.biz.service.api.PhoneUserServiceI;
import com.biz.service.api.WxTemplateServiceI;
import com.biz.service.api.WxUtilServiceI;
import com.biz.service.base.Pay17ServiceI;
import com.biz.service.basic.MerchantServiceI;
import com.biz.service.basic.ShopServiceI;
import com.biz.service.basic.UserServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.*;
import com.framework.utils.weixin.HttpRequestUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ldd_person on 2017/3/16.
 */
@Controller
@RequestMapping("/api/pos")
public class PosController extends BaseController {

    @Autowired
    private UserServiceI userServiceI;
    @Autowired
    private ShopServiceI shopServiceI;
    @Autowired
    private MerchantServiceI merchantService;
    @Autowired
    private OrderMainUnionServiceI orderMainUnionServiceI;
    @Autowired
    private WxUtilServiceI wxUtilServiceI;
    @Autowired
    private PhoneUserServiceI phoneUserServiceI;

    @Resource(name = "wxTemplateService")
    private WxTemplateServiceI wxTemplateService;

    @Resource(name = "pay17Service")
    private Pay17ServiceI pay17Service;

    @Resource(name = "orderMainUnionService")
    private OrderMainUnionServiceI orderMainUnionService;

    /**
     * 联盟商户 APP端登录
     */
    @RequestMapping(value = "/phoneu_login")
    public void pos_login(String login_name,String pwd,
                         int device_ver,String device_info,String device_ip,HttpServletResponse response) {
        HashMap<String, Object>  jSONObject = new HashMap<String, Object>();
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("login_name", login_name);
        hashMap.put("pwd_md5", pwd);
        try {
            User user = userServiceI.getUserForLoginhashMap(hashMap);

            if (user == null) { // 不存在或非门店
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", "用户名或密码有误！");
                jSONObject.put("return_data", null);
            } else {
                // 门店
                Shop shop = shopServiceI.getShopByCode(user.getIdentity_code());
                if (shop == null || shop.getIs_90() == 1) {
                    jSONObject.put("return_code", 0);
                    jSONObject.put("return_info", "门店不存在或非门店用户！");
                    jSONObject.put("return_data", null);
                }
                // 返回正确
                else {
                    //获取是否有分期发券权限
                    int isPeriodization= userServiceI.findBaseUserisPeriodization(user.getIdentity(),user.getIdentity_code().trim());

                    HashMap<String, Object> hm = new HashMap<String, Object>();
                    hm.put("user_code", user.getId());
                    hm.put("login_name", user.getLoginName());
                    hm.put("pwd", user.getPwd());
                    hm.put("brand_code", shop.getBrand_code());
                    hm.put("phone", user.getPhone());
                    hm.put("person_name", user.getPersonName());
                    hm.put("identity_code", user.getIdentity_code());
                    hm.put("dxy_code", notNull(shop.getDxy_code()));//店小翼门店code
                    hm.put("type", user.getType());
                    hm.put("shop_name", shop.getBusiness_name());
                    hm.put("initial_money", shop.getInitial_money());//初始收银金额，默认：0 单位：分
                    hm.put("money_fixed", shop.getMoney_fixed());//是否固定收银金额 0：不固定 ;1：固定
                    hm.put("isPeriodization",isPeriodization);
                    if (StringUtils.isNotBlank(shop.getLogo_url())) {
                        hm.put("shop_logo", ConfigUtil.get("OSSURL") +"ninezero/img_small/"
                                + shop.getLogo_url());
                    } else {
                        hm.put("shop_logo",ConfigUtil.get("OSSURL") +"ninezero/img_small/"
                                + "/2015-12-04/63b60c399ca14e2693b70df99c6d8379.jpg");
                    }
                    JSONObject subMsgs = JSONObject.fromObject(hm);
                    jSONObject.put("return_code", 1);
                    jSONObject.put("return_info", "");
                    jSONObject.put("return_data", hm);
//					userService.updateUserDeviceVer(user,device_ver,device_ip,device_info);//更新用户手机客户端版本号及登陆历史
                }
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg.length() > 30) {
                msg = msg.substring(0, 29);
            }
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", msg);
            jSONObject.put("return_data", null);
            logger.error("联盟商户登录接口=报错=", e.fillInStackTrace());
            e.printStackTrace();
        }
       writeJson(jSONObject,response);
    }


    /**
     * 银联退款接口
     * @return
     *
     * @author zhengXin 2016-11-01 Update
     * 修改用户退款判断，增加提示信息
     * 在退款方法中增加判断，需要回收用户消费获得的90券，
     * 当退款用户当前剩余90券余额不足以支持退款订单赠送的90券时，不允许退款
     *
     * 通过订单信息order_code获得用户open_id查询到用户的剩余90券余额
     * 将订单的赠送90券Gift_90与用户当前剩余的90券余额Balance_90进行对比，
     * 当用户剩余90券余额Balance_90少于订单赠送90券Gift_90，返回信息用户90券余额不足，不允许退款，退款失败
     * 退款成功时，用户90券余额需要扣去退款订单赠送90券数额，同时，联盟商户90券余额需要增加订单赠送90券数额
     */
    @RequestMapping(value="/phone_unionPayRefund")
    public void phone_unionPayRefund(String shop_id, String user_code, String order_code,
                                                     int total,String pwd, String refund_terminalId,HttpServletResponse response)
    {
        logger.error("银联退款接口=传入参数=" + "=shop_id="
                + shop_id + "=user_code=" + user_code + "=total=" + total
                + "=order_code=" + order_code + "=pwd=" +pwd + "=refund_terminalId=" +refund_terminalId);

        Result result = new Result();
        try
        {
            Shop shop = shopServiceI.getShopBySid(shop_id);
            PorderMainUnion orderMain = orderMainUnionServiceI.getOrderMainUnionByCode(order_code);
            if(orderMain ==null){
                result.setReturn_code(0);
                result.setReturn_info("订单不存在");
                logger.error("银联退款,订单不存在=order_code="+order_code);
                writeJson(result,response);
            }
            String main_user_id="";

			/*Update zhengXin 2016-11-01 start*/
            BaseUser user = userServiceI.getBaseUserByOpen_id(orderMain.getOpen_id());
            if (user != null) {
                double user_card_total = user.getBalance_90();
                double user_shoping_card_total = user.getBalance_shopping_90();
                double card_total = orderMain.getGift_90();
                main_user_id=user.getId();
                if(orderMain.getBalance_type()==0){
                    if (user_card_total < card_total) {
                        result.setReturn_code(0);
                        result.setReturn_info("退款用户久零券余额不足");
                        logger.error("银联退款,用户久零券余额不足=order_code=" + order_code);
                        writeJson(result,response);
                    }
                }else{
                    if (user_shoping_card_total < card_total) {
                        result.setReturn_code(0);
                        result.setReturn_info("退款用户购物券余额不足");
                        logger.error("银联退款,用户购物券余额不足=order_code=" + order_code);
                        writeJson(result,response);
                    }
                }

                //判断贝是否足够
                if(orderMain.getCoin_90()>user.getGiveAmount()){
                    result.setReturn_code(0);
                    result.setReturn_info("退款用户久零贝余额不足");
                    writeJson(result,response);
                }
            }
			/*Update zhengXin 2016-11-01 end*/

            /****************调用店小翼退款接口**********************/
            /*String url = ConfigUtil.get("KQ_URL")+"phone_unionPayRefund.do?shop_id="+shop.getDxy_code()+"&user_code="+shop.getDxy_person_code()
                    +"&order_code="+order_code+"&total="+total+"&pwd="+pwd+"&refund_terminalId="+refund_terminalId;
            String back_result = HttpURL(url);
            JSONObject json = JSONObject.fromObject(back_result);
            logger.error("=银联退款=json="+json);
            int return_code = json.getInt("return_code");*/
            int return_code=1;//默认成功


            if(return_code == 0){ //错误
                result.setReturn_code(0);
                //result.setReturn_info(json.getString("return_info"));(店小翼)
                result.setReturn_info("");
            }else if(return_code == 1){ //正确
                //订单赠送90券数额
                double double_total = orderMain.getGift_90();
                //订单原始金额与退款金额数值差
                double back_order_total = orderMain.getOrder_total()-orderMain.getOrder_total();
                //订单原始赠送90券与退款返还90券数额数值差
                double back_gift_90 = orderMain.getGift_90()-orderMain.getGift_90();
                //订单原始实际支付金额与退款数额差
                double back_cash_total = orderMain.getCash_total()-orderMain.getCash_total();

                result.setReturn_code(1);
                //result.setReturn_info(json.getString("return_info"));(店小翼)
                result.setReturn_info("");
                Map<String,Object> param = new HashMap<String,Object>();
                param.put("code", order_code);
                param.put("back_user_code", user_code);
                param.put("state", 3);
                param.put("back_code", String.valueOf(Tools.getRandomNum()));
                param.put("back_time", DateUtil.getTime());
                param.put("old_order_total", orderMain.getOrder_total());	//保存退款前订单原始金额
                param.put("old_gift_90", orderMain.getGift_90());			//保存退款前订单原始赠送90券
                param.put("order_total", back_order_total);		//保存退款后订单原始金额与退款金额的数值差
                param.put("gift_90", back_gift_90);				//保存退款后订单原始赠送90券与退款返还90券的数值差
                param.put("cash_total", back_cash_total);			//保存退款前订单实际付款金额与实际金额差
                orderMainUnionServiceI.updateWhereOrderMainUnion(param);

                String base_90_dtail_id = orderMainUnionService.getOrderGetBalance90(order_code);

                Map<String,String> map1=new HashMap<>();
                JSONObject jSONObject1 = new JSONObject();
                jSONObject1.put("brand_code",shop.getBrand_code());
                jSONObject1.put("shop_code",orderMain.getShop_code());
                jSONObject1.put("user_code",user_code);
                jSONObject1.put("order_code",base_90_dtail_id);
                jSONObject1.put("open_id",orderMain.getOpen_id());
                jSONObject1.put("type","0");
                jSONObject1.put("source","10");
                jSONObject1.put("source_msg","用户退款");
                jSONObject1.put("balance_90", double_total);
                jSONObject1.put("state", "1");
                jSONObject1.put("commission", orderMain.getCommission());
                jSONObject1.put("tradeType", orderMain.getTrade_type());
                jSONObject1.put("orderState","3");
                jSONObject1.put("orderTotal",orderMain.getOrder_total());
                jSONObject1.put("ticketType",orderMain.getBalance_type());
                //map1.put("json", jSONObject1.toString());
                int f_q=1;//发券是否成功， 0成功 1失败 3重
                int f_b=1;//发贝是否成功， 0成功 1失败 3重
                String r_msg="";
                //不是会员不扣券
                if(double_total!=0){
                    f_q=user_quan(jSONObject1);
                }
                //退贝
                f_b = user_coin(main_user_id,order_code,orderMain.getCoin_90(),2,7) ;

                if((f_q==0|| f_q==3) && (f_b==0|| f_b==3)){
                    result.setReturn_code(1);//0错误 1成功
                }else{
                    result.setReturn_code(0);//0错误 1成功
                }
                result.setReturn_info("****************************"+r_msg);;

                //send_refund_template(json.getString("return_info"),orderMain,total);(店小翼)
                //send_refund_template("",orderMain,total);
            }
        } catch (Exception e)
        {
            String msg = e.getMessage();
            if(StringUtils.isNotBlank(msg) && msg.length()>30){
                msg = msg.substring(0, 29);
            }
            result.setReturn_code(0);
            result.setReturn_info(msg);
            logger.error("银联支付退款接口=报错=" +e,e.fillInStackTrace());
        }finally {
            // 发送 模板消息
            wxTemplateService.sendOrderRefundDelayInfo(order_code,"",result.getReturn_code()==1?"2":"3");
        }
        writeJson(result,response);
    }


    /**
     * 微信刷卡付=调用店小翼支付接口 total为字符的元
     *
     * @author zhengXin Update 2016-10-31
     * 更新代码，添加判断当当前付款人已存在，且关联付款方式时返回的将不再是二维码而是提示信息
     * 操作：判断 user 在 base_user 中存在，且 scan_ali_id,scan_yi_id,scan_yhk_id,xy_openid
     * 	四个字段任意一个不为空判断成立，输出提示信息而不是二维码
     */
    @RequestMapping(value = "/phone_wx_scan")
    public void phone_wx_scan(String shop_id,
                                              String user_code, String author_code, String total, String order_code,
                                              String device_info, String device_ip,HttpServletResponse response){
        logger.error("####weixin=dianxiaoyi=" + "=shop_id="
                + shop_id + "=user_code=" + user_code + "=author_code="
                + author_code + "=total=" + total + "=order_code=" + order_code
                + "=device_info=" + device_info + "=device_ip=" + device_ip);

        Map<String,Object> map=new HashMap<>();
        map.put("shop_id",shop_id);
        map.put("user_code",user_code);
        map.put("author_code",author_code);
        map.put("total",total);
        map.put("order_code",order_code);
        map.put("device_info",device_info);
        map.put("device_ip",device_ip);
        Result result=new Result();
        try {
             result =orderMainUnionServiceI.phone_wx_scan(map);
        }catch (Exception e){
            e.getMessage();
            result.setReturn_code(0);
            result.setReturn_info(e.getMessage());
        }
        writeJson(result,response);
    }

    /**
     * 微信刷卡付=新版，pos机支付成功后，调用此接口 total为字符的元
     * @param shop_id 门店 base_shop 的id (原参数)
     * @param user_code 操作人t_sys_user 的id (原参数)
     * @param pay_type 第三方类型：1银联，2：拉卡拉
     * @param author_code 1：微信(MICROPAY) 2:支付宝(ZFB-MICROPAY)(原参数) ，3.银联卡(UNIONPAY)，4:银联钱包(UNIONPAY-WALLET)，5:百度钱包(BAIDU-WALLET),6:京东钱包(JD-WALLET),7:拉卡拉钱包 (lkl-WALLET)
     * @param total total为字符的元(原参数)
     * @param order_code 支付单号(原参数)
     * @param card_no 卡号,银联卡支付时必传(author_code=3)
     * @param userNo 用户号(暂时无用，预留用来绑定系统用户和pos用户，实现第一次需要扫码，以后不需要)
     * @param response
     */
    @RequestMapping(value = "/phone_yl_lkl_scan")
    public void phone_yl_lkl_scan(String shop_id,
                              String user_code, String author_code, String total, String order_code,int pay_type,String card_no,String userNo
            ,HttpServletResponse response){
        logger.error("####weixin=dianxiaoyi=" + "=shop_id="
                + shop_id + "=user_code=" + user_code + "=author_code="
                + author_code + "=total=" + total + "=order_code=" + order_code+"=pay_type="+pay_type+"=card_no="+card_no);

        Result result=new Result();
        try {
            result =orderMainUnionServiceI.phone_yl_lkl_scan(shop_id, user_code, author_code, total, order_code, pay_type, card_no);
        }catch (Exception e){
            e.getMessage();
            result.setReturn_code(0);
            result.setReturn_info(e.getMessage());
        }
        writeJson(result,response);
    }


    /**
     * 微信刷卡付,用户输入密码,再次验证
     */
    @RequestMapping(value = "/phone_wx_scan_again")
    public void phone_wx_scan_again(String order_code,String user_code,HttpServletResponse response){
        logger.error("微信刷卡付,用户输入密码,再次验证=order_code="
                + order_code + "=user_code=" + user_code);
        Result result = new Result();
        Map<String, String> mapQrparam = new HashMap<>();
        try {
            BaseUser user = null;
            PorderMainUnion orderMain = orderMainUnionServiceI.getOrderMainUnionByCode(order_code);
            if(orderMain ==null){
                result.setReturn_code(0);
                result.setReturn_info("订单不存在");
                logger.error("订单不存在=order_code="+order_code);
                writeJson(result,response);
            }
            String shop_id = orderMain.getShop_code();
            Shop shop = shopServiceI.getShopBySid(shop_id);

            //此处添加是否分期
            boolean isPeriodization = false;
            try{
                isPeriodization =  "1".equals(shop.getIsPeriodization())? true : false;
            }catch(Exception e)
            {
                e.printStackTrace();
            }

            //pay17支付再次验证
            Pay17SelectPay bs_r= pay17Service.getPay17_select(shop.getDxy_code(), order_code,null);

            if(bs_r.getReturn_code()==1 && bs_r.getTrade_state()==1)
            { //正确
                result.setReturn_code(1);

                //用户通知
                String xy_openid = bs_r.getCustomer_id();
                String payTime =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String trade_type = orderMain.getTrade_type();
                Map<String,Object> pd = new HashMap<>();
                if(trade_type.equals("ZFB-MICROPAY")){
                    pd.put("scan_ali_id", xy_openid);
                }else if(trade_type.equals("MICROPAY")){
                    pd.put("xy_openid", xy_openid);
                }
                user = phoneUserServiceI.getOneBaseUser(pd);
                //场景值  带参数关注二维码
                int scene_id = 0;

                if(trade_type.equals("ZFB-MICROPAY")){
                    scene_id = save_pay_scence(2,xy_openid,order_code);
                }else if(trade_type.equals("MICROPAY")){
                    scene_id = save_pay_scence(1,xy_openid,order_code);
                }


				/*@Update zhengXin start 2016-10-31  String link = temporaryCode(scene_id);*/
                String link = "";
                double totals = Double.valueOf(orderMain.getCash_total())/100;
                if(!isPeriodization) //不进行分期
                {
                    if (user != null && (
                            StringUtils.isNotEmpty(user.getScan_ali_id()) ||
                                    StringUtils.isNotEmpty(user.getScan_yhk_id()) ||
                                    StringUtils.isNotEmpty(user.getScan_yi_id()) ||
                                    StringUtils.isNotEmpty(user.getXy_openid()))
                            ) {
                        link = "亲爱的久零会员-你的"+ totals +"久零券已经到账！-可进入“辽宁久零”公众号内-查看和使用。";
                        wxTemplateService.send_kf_template(user.getOpen_id(), "支付成功，支付金额："+totals+"元，赠送久零券："+totals);

                    }
                    else
                    {
                        link = temporaryCode(scene_id);
                    }
                }else{//分期处理，生成二维码

                    mapQrparam = orderMainUnionServiceI.getPeriodizationLinkUrl(totals,shop_id,order_code,shop.getBrand_code(),xy_openid);
                    link = mapQrparam.get("link");
                    result.setMainId(mapQrparam.get("mainId"));
                }
				/*@Update zhengXin end 2016-10-31 */

                result.setReturn_info(link+","+orderMain.getTrade_type());
                orderMain.setPay_time(payTime);
                double double_total = orderMain.getCash_total();
                pd.clear();
                pd.put("code", order_code);
                pd.put("pay_user_id", xy_openid);
                pd.put("pay_time", payTime);
                if(user!=null){
                    pd.put("open_id", user.getOpen_id());
                    //pd.put("gift_90", double_total);
                }
                pd.put("state", 1);
                pd.put("error_pay_msg", "");
                orderMainUnionServiceI.updateWhereOrderMainUnion(pd);
                /****************会员自动发模板消息，增加券，记录**********************/
                if(user!=null && !isPeriodization){
                    //发送模板消息
                    send_pay_template(scene_id,user.getOpen_id(),orderMain);
                    //增加用户90券
                    //add_balance_90(user.getOpen_id(),double_total);
                    //增加发券记录
                    //add_auto_balance(shop.getBrand_code(),shop_id,order_code,user.getOpen_id(),double_total,user_code,1,"微信刷卡付自动发券");

                    int f_q=1;//发券是否成功， 0成功 1失败 3重
                    int f_b=1;//发贝是否成功， 0成功 1失败 3重
                    String r_msg="";
                    JSONObject jSONObject1 = new JSONObject();
                    jSONObject1.put("brand_code",shop.getBrand_code());
                    jSONObject1.put("shop_code",shop_id);
                    jSONObject1.put("user_code",user_code);
                    jSONObject1.put("order_code",order_code);
                    jSONObject1.put("open_id",user.getOpen_id());
                    jSONObject1.put("type","0");
                    jSONObject1.put("source","1");
                    jSONObject1.put("source_msg","会员购买商品自动发券");
                    jSONObject1.put("balance_90", orderMain.getGift_90());
                    jSONObject1.put("state", "2");
                    jSONObject1.put("commission", orderMain.getCommission());
                    jSONObject1.put("tradeType",trade_type);
                    jSONObject1.put("tradeType",trade_type);
                    jSONObject1.put("orderState","1");
                    jSONObject1.put("orderTotal",double_total);
                    jSONObject1.put("ticketType",orderMain.getBalance_type());
                    f_q = user_quan(jSONObject1);
                    f_b = user_coin(user.getId(),order_code,orderMain.getCoin_90(),1,3) ;
                    if((f_q==0|| f_q==3)){
                        r_msg+="-发券成功;";
                    }else{
                        r_msg+="-发券失败;";
                    }

                    if((f_b==0|| f_b==3)){
                        r_msg+="-发贝成功;";
                    }else{
                        r_msg+="-发贝失败;";
                    }

                    if((f_q==0|| f_q==3) && (f_b==0|| f_b==3)){
                        result.setReturn_code(1);//0错误 1成功
                    }else{
                        result.setReturn_code(0);//0错误 1成功
                    }

                    result.setReturn_info("************-***************"+r_msg+","+trade_type);
                }
            }else
            {
                result.setReturn_code(0);
                //201-11-03 zhengXin 修改返回信息,判断当返回信息是NOPAY时,将数据修改为用户没有付款
                double tradeState = bs_r.getTrade_state();
                String msg = "";
                if ((int)tradeState == 5) {
                    msg = "用户没有付款";
                }
                result.setReturn_info(msg);
            }
        }catch (Exception e){
            String msg = e.getMessage();
            if(StringUtils.isNotBlank(msg) && msg.length()>30){
                msg = msg.substring(0, 29);
            }
            result.setReturn_code(0);
            result.setReturn_info("错误-" + msg);
            logger.error("=微信刷卡付,用户输入密码,再次验证=报错=" + e, e.fillInStackTrace());
        }
        writeJson(result,response);
    }
    /**
     * 银联支付接口
     *
     * @author zhengXin Update 2016-10-31
     * 更新代码，添加判断当当前付款人已存在，且关联付款方式时返回的将不再是二维码而是提示信息
     * 操作：判断 user 在 base_user 中存在，且 scan_ali_id,scan_yi_id,scan_yhk_id,xy_openid
     * 	四个字段任意一个不为空判断成立，输出提示信息而不是二维码
     */
    @RequestMapping(value="/phone_unionPayScan")
    public void phone_unionPayScan(String shop_id, String user_code, int cash_total , int card_total, String order_code,
                         String device_info, String device_ip, String cardNo, String traceNo, String referenceNo, String type,
                         String issue, String batchNo, String terminalId, String merchantId, String merchantName,HttpServletResponse response)
    {
        logger.error("******银联支付，刷客户的银行卡接口=phone_unionPayScan传入参数=" + "=shop_id="
                + shop_id + "=user_code=" + user_code + "=card_total=" + card_total + "=cash_total=" + cash_total + "=order_code=" + order_code
                + "=device_info=" + device_info + "=device_ip=" + device_ip + "=cardNo=" + cardNo);
        Result result = new Result();

        Map<String, String> mapQrparam = new HashMap<>();
        logger.error("***********"+"银联支付 回调成功！");

   try{
       BaseUser user = null;
       PorderMainUnion orderMainUnion = orderMainUnionServiceI.getOrderMainUnionByCode(order_code);
       if(orderMainUnion!=null){
           result.setReturn_code(0);
           result.setReturn_info("订单已存在");
           logger.error("订单已存在=order_code="+order_code);
       }else
       {
           Shop shop = shopServiceI.getShopBySid(shop_id);
           Pbrand brand=merchantService.findById(shop.getBrand_code());

           int scene_id = 0;
           /*String url = ConfigUtil.get("KQ_URL")+"phone_unionPayScan.do";
           Map<String,String> map=new HashMap<>();
           map.put("shop_id",shop.getDxy_code());
           map.put("user_code",shop.getDxy_person_code());
           map.put("cash_total",cash_total+"");
           map.put("card_total",card_total+"");
           map.put("order_code",order_code);
           map.put("device_info",device_info);
           map.put("device_ip",device_ip);
           map.put("cardNo",cardNo);
           map.put("traceNo",traceNo);
           map.put("referenceNo",referenceNo);
           map.put("type",type);
           map.put("issue",issue);
           map.put("batchNo",batchNo);
           map.put("terminalId",terminalId);
           map.put("merchantId",merchantId);
           map.put("merchantName",merchantName);
           String back_result = URLConectionUtil.httpURLConnectionPostDiy(url,map);
           Map<String,Object> res=JSON.parseObject(back_result,Map.class);
           logger.error("=银联支付接口=调用店小翼支付接口=json="+res);
           int return_code =Integer.valueOf(res.get("return_code")+"") ;*/
           int return_code=1;//默认成功
           double jl_total=0;
           String proportion="";

           try{proportion=brand.getProportion();}catch (Exception e){}
           Double pro=0.0;
           if(!StringUtil.isNullOrEmpty(proportion)&&StringUtil.isNumeric(proportion))
           {pro=Double.valueOf(proportion);}
           jl_total = (pro<=0?1:pro)*cash_total;

           //***********计算送90贝数*************//
           double coin_90_total=0.0;
           if(shop.getIscoin()==1){
               try{
                   double bl = Double.parseDouble(ZkNode.getIstance().getJsonConfig().get("service_charge").toString());
                   System.out.println("service_charge:::"+bl);
                   coin_90_total= MathUtil.mul(MathUtil.mul(jl_total,0.01),bl);
                   System.out.println("coin_90_total:::"+coin_90_total);
               }catch(Exception e)
               {
                   System.out.println("zk_service_charge:::"+e.toString());
                   e.printStackTrace();
               }
           }

           //此处添加是否分期
           boolean isPeriodization = false;
           try{
               isPeriodization =  "1".equals(brand.getIsPeriodization())? true : false;
           }catch(Exception e)
           {
               e.printStackTrace();
           }
           if(return_code == 0){ //错误
               result.setReturn_code(0);
               //result.setReturn_info(res.get("return_info")+"");(店小翼)
               result.setReturn_info("");
           }else if(return_code == 1) { //正确

               orderMainUnion = new PorderMainUnion();
               orderMainUnion.setCode(order_code);
               int total = cash_total + card_total;
               orderMainUnion.setOrder_total(total);

               orderMainUnion.setCash_total(cash_total);
               orderMainUnion.setCard_total(card_total);
               orderMainUnion.setCard_count(0);
               orderMainUnion.setGift_90(0);
               orderMainUnion.setState(1);
               orderMainUnion.setPay_time(DateUtil.getTime());
               orderMainUnion.setPay_user_id(cardNo);
               /*Map<String,Object> pd=new HashMap<>();
               pd.put("scan_yhk_id", cardNo);
               user = phoneUserServiceI.getOneBaseUser(pd);*/
               user=phoneUserServiceI.getOneBaseUserByUnionpay(cardNo);
               if(user!=null){
                   orderMainUnion.setOpen_id(user.getOpen_id());
               }
               orderMainUnion.setGift_90(jl_total);
               orderMainUnion.setUser_code(user_code);
               orderMainUnion.setShop_code(shop_id);
               orderMainUnion.setCommission(Double.valueOf(brand.getCommission()));
               orderMainUnion.setProcedures(Double.valueOf(brand.getProcedures()));
               orderMainUnion.setBrand_code(shop.getBrand_code());
               orderMainUnion.setTrade_type("UNIONPAY");
               orderMainUnion.setIs_clean(0);
               orderMainUnion.setPay_type(4);
               orderMainUnion.setCoin_90(coin_90_total);
               orderMainUnion.setBalance_type(shop.getTicketType());
               orderMainUnionServiceI.insertOrderMainUnion(orderMainUnion);
               //场景值  带参数关注二维码
               logger.error("***********"+"发券调用开始");
               scene_id = save_pay_scence(4,cardNo,order_code);
               String link = "";
               double totals = jl_total /100;
               if(!isPeriodization) //不进行分期
               {
                   if (user != null && (
                           StringUtils.isNotEmpty(user.getScan_ali_id()) ||
                                   StringUtils.isNotEmpty(user.getScan_yhk_id()) ||
                                   StringUtils.isNotEmpty(user.getScan_yi_id()) ||
                                   StringUtils.isNotEmpty(user.getXy_openid()))
                           ) {
                       link = "亲爱的久零会员-你的"+ totals +"久零券已经到账！-可进入“辽宁久零”公众号内-查看和使用。";
                       wxTemplateService.send_kf_template(user.getOpen_id(), "支付成功，支付金额："+totals+"元，赠送久零券："+totals);
                   }
                   else
                   {
                       link = temporaryCode(scene_id);
                   }
               }else{//分期处理，生成二维码
                   mapQrparam = orderMainUnionServiceI.getPeriodizationLinkUrl(totals,shop_id,order_code,shop.getBrand_code(),cardNo);
                   link = mapQrparam.get("link");
                   result.setMainId(mapQrparam.get("mainId"));
               }
               logger.error("***********二维码"+link);
               result.setReturn_code(1);
               result.setReturn_info(link+",UNIONPAY");
           }		/****************会员自动发模板消息，增加券，记录**********************/
           if(user!=null && !isPeriodization){
               int f_q=1;//发券是否成功， 0成功 1失败 3重
               int f_b=1;//发贝是否成功， 0成功 1失败 3重
               String r_msg="";
               //发送模板消息
               send_pay_template(scene_id,user.getOpen_id(),orderMainUnion);

               JSONObject jSONObject1 = new JSONObject();
               jSONObject1.put("brand_code",shop.getBrand_code());
               jSONObject1.put("shop_code",shop_id);
               jSONObject1.put("user_code",user_code);
               jSONObject1.put("order_code",order_code);
               jSONObject1.put("open_id",user.getOpen_id());
               jSONObject1.put("type","0");
               jSONObject1.put("source","1");
               jSONObject1.put("source_msg","联盟商家pos机银联刷卡付自动发券");
               jSONObject1.put("balance_90", jl_total);
               jSONObject1.put("state", "2");
               jSONObject1.put("commission", brand.getCommission());
               jSONObject1.put("tradeType","UNIONPAY");
               jSONObject1.put("orderState","1");
               jSONObject1.put("orderTotal",orderMainUnion.getOrder_total());
               jSONObject1.put("ticketType",shop.getTicketType());
               f_q = user_quan(jSONObject1);
               f_b = user_coin(user.getId(),order_code,coin_90_total,1,3) ;
               if((f_q==0|| f_q==3)){
                   r_msg+="-发券成功;";
               }else{
                   r_msg+="-发券失败;";
               }

               if((f_b==0|| f_b==3)){
                   r_msg+="-发贝成功;";
               }else{
                   r_msg+="-发贝失败;";
               }

               if((f_q==0|| f_q==3) && (f_b==0|| f_b==3)){
                   result.setReturn_code(1);//0错误 1成功
               }else{
                   result.setReturn_code(0);//0错误 1成功
               }
               result.setReturn_info("************-***************"+r_msg+","+"UNIONPAY");


           }

       }
   }catch (Exception e)
    {
        String msg = e.getMessage();
        if(msg.length()>30){
            msg = msg.substring(0, 29);
        }
        result.setReturn_code(0);
        result.setReturn_info("错误-" + msg);
        logger.error("银联支付接口=报错=" + e, e.fillInStackTrace());
    }
        System.out.println("###jieguo:::" + result);
        writeJson(result, response);
    }
    /**
     * 微信刷卡付,退款
     *
     *
     * @author zhengXin 2016-11-01 Update
     * 修改用户退款判断，增加提示信息
     * 在退款方法中增加判断，需要回收用户消费获得的90券，
     * 当退款用户当前剩余90券余额不足以支持退款订单赠送的90券时，不允许退款
     *
     * 通过订单信息order_code获得用户open_id查询到用户的剩余90券余额
     * 将订单的赠送90券Gift_90与用户当前剩余的90券余额Balance_90进行对比，
     * 当用户剩余90券余额Balance_90少于订单赠送90券Gift_90，返回信息用户久零券余额不足，不允许退款，退款失败
     * 退款成功时，用户90券余额需要扣去退款订单赠送90券数额，同时，联盟商户90券余额需要增加订单赠送90券数额
     * 同时在流水记录中增加退款数据信息
     */
    @RequestMapping(value = "/phone_wx_back_money")
    public void phone_wx_back_money(String order_code,String total,String user_code,HttpServletResponse response){
        logger.error("微信刷卡付,退款=order_code="+order_code+"=total="+total+"=user_code="+user_code);
        Result result = new Result();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("order_code",order_code);
        map.put("total",total);
        map.put("user_code",user_code);
        try {
            result=orderMainUnionServiceI.phone_wx_back_money(map);

        }catch (Exception e){

        }finally {
            // 发送 模板消息
            wxTemplateService.sendOrderRefundDelayInfo(order_code,"",result.getReturn_code()==1?"2":"3");
        }
        writeJson(result,response);
    }


    /**
     * 拉卡拉 微信支付宝刷卡付,退款
     *
     *
     * @author zhengXin 2016-11-01 Update
     * 修改用户退款判断，增加提示信息
     * 在退款方法中增加判断，需要回收用户消费获得的90券，
     * 当退款用户当前剩余90券余额不足以支持退款订单赠送的90券时，不允许退款
     *
     * 通过订单信息order_code获得用户open_id查询到用户的剩余90券余额
     * 将订单的赠送90券Gift_90与用户当前剩余的90券余额Balance_90进行对比，
     * 当用户剩余90券余额Balance_90少于订单赠送90券Gift_90，返回信息用户久零券余额不足，不允许退款，退款失败
     * 退款成功时，用户90券余额需要扣去退款订单赠送90券数额，同时，联盟商户90券余额需要增加订单赠送90券数额
     * 同时在流水记录中增加退款数据信息
     */
    @RequestMapping(value = "/phone_lkl_back_money")
    public void phone_lkl_back_money(String order_code,String total,String user_code,HttpServletResponse response){
        logger.error("微信刷卡付,退款=order_code="+order_code+"=total="+total+"=user_code="+user_code);
        Result result = new Result();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("order_code",order_code);
        map.put("total",total);
        map.put("user_code",user_code);
        try {
            result=orderMainUnionServiceI.updatePhone_lkl_back_money(map);

        }catch (Exception e){

        }finally {
            // 发送 模板消息
            wxTemplateService.sendOrderRefundDelayInfo(order_code,"",result.getReturn_code()==1?"2":"3");
        }
        writeJson(result,response);
    }



    /**
     * 查询积分明细
     *
     * @param user_code
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/phone_query_integral")
    public void phone_query_integral(String user_code, int page,HttpServletResponse response) throws Exception {
        Map<String,Object> jSONObject = new HashMap<>();
        try {
            int pageSize = 10;
            PageData pd = new PageData();
            pd.put("pageSize", pageSize);
            pd.put("pageNum", pageSize * (page - 1));
            pd.put("user_code", user_code);
            List<PhoneUserIntegral> integral_list = phoneUserServiceI.queryIntegralListByUser(pd);
            jSONObject.put("return_code", 1);
            jSONObject.put("return_info", "");
            jSONObject.put("return_data", integral_list);
            logger.error("查询收银员积分明细=返回数据jSONObject=" + jSONObject);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg.length() > 30) {
                msg = msg.substring(0, 29);
            }
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", msg);
            jSONObject.put("return_data", null);
            logger.error("查询收银员积分明细=报错=", e.fillInStackTrace());
            e.printStackTrace();
        }
        writeJson(jSONObject,response);
    }


    /**
     * 增加发券记录
     */
    protected void add_auto_balance(String brand_code, String shop_code,
                                    String source_code, String open_id, double balance_90,String user_code,
                                    int source,String source_msg)
            throws Exception {
        logger.error("=增加发券记录=open_id=" + open_id);
        Base90Detail base90Detail = new Base90Detail();
        base90Detail.setBrand_code(brand_code);
        base90Detail.setShop_code(shop_code);
        base90Detail.setSource_code(source_code);
        base90Detail.setOpen_id(open_id);
        base90Detail.setSource(source);
        base90Detail.setSource_msg(source_msg);
        base90Detail.setUser_code(user_code);
        int point_90 = (int) (balance_90 * -1);
        base90Detail.setPoint_90(point_90);
        orderMainUnionServiceI.insertBase90Detail(base90Detail);
    }

    /**
     * 增加用户90券
     */
    protected void add_balance_90(String open_id, double balance_90){
        logger.error("=增加用户90券=open_id=" + open_id + "=balance_90="
                + balance_90);
        try {
            Map<String,Object> pd = new HashMap<>();
            pd.put("open_id", open_id);
            pd.put("balance_90", balance_90);
            orderMainUnionServiceI.add_balance_90(pd);
        } catch (Exception e) {
            logger.error("=增加用户90券=报错=" + e, e.fillInStackTrace());
        }
    }


    /**
     * 非空转换
     */
    private String notNull(String in)
    {
        if(StringUtils.isBlank(in)){
            return "";
        }
        return in;
    }

    /**
     * 推送退款模板信息（联盟商户）
     */
    protected void send_refund_template(String back_code,PorderMainUnion orderMainUnion,Integer int_total) throws Exception {

        logger.error("=推送支付模板信息=open_id=" + orderMainUnion.getOpen_id() + "=int_total=" + int_total);
        wxUtilServiceI.send_refund_template(back_code,orderMainUnion.getCode(),orderMainUnion.getOpen_id(), int_total);
    }

    /**
     * 推送支付模板信息
     */
    protected void send_pay_template(int scene_id, String open_id,
                                     PorderMainUnion orderMainUnion) throws Exception {
        logger.error("=推送支付模板信息=open_id=" + open_id + "=scene_id=" + scene_id);

        try{
            wxUtilServiceI.send_pay_template(scene_id, open_id, orderMainUnion);
        }catch (Exception e){

        }

    }

    /**
     * 保存支付场景
     * type: 0:未知 1：微信 2：支付宝 3：易支付 4银行卡,8:银联钱包，5:百度钱包,6:京东钱包,7:拉卡拉钱包
     */
    protected int save_pay_scence(int type,String pay_user_id,String order_code) throws Exception{
        PayScene payScene = new PayScene();
        payScene.setMain_code(order_code);
        payScene.setType(type);
        payScene.setPay_user_id(pay_user_id);
        payScene.setScene_type(0);
        phoneUserServiceI.insertPayScene(payScene);
        return payScene.getId();
    }

    /**
     * 生成关注带参二维码
     */
    protected String temporaryCode(int scene_id) throws Exception {
        String access_token = wxUtilServiceI.getAccessToken();
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + access_token;
        JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl,
                "POST",
                "{\"expire_seconds\": 2592000,\"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": \"" + scene_id + "\"}}}");
        String back = "";
        if(jsonObject!=null && jsonObject.size()>0){
            String ticket = jsonObject.getString("ticket");
            if(StringUtils.isNotBlank(ticket)){
                back = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
            }
        }
        return back;
    }
    /**
     *吉林银行支付直接调用自动发券接口
     * 传递参数 openId 用户的openid，total 支付金额 userCode 营业员code
     */
    @RequestMapping(value = "/jlBank_payBackForBalance")
    public void jlBank_payBackForBalance(String total,String userCode,String order_code,HttpServletResponse response){
        Result result = new Result();
        try
        {
            int isChecked=orderMainUnionServiceI.checkPayScene(order_code);
            if(isChecked<=0)
            {result=orderMainUnionServiceI.updateBalanceForJLBankBack(total,userCode,order_code);}
            else
            {   result.setReturn_code(0);
                result.setReturn_info("操作失败，该订单已存在!");
            }

        }
        catch (Exception e)
        {
            result.setReturn_code(0);
            result.setReturn_info("操作失败，异常错误!");
        }
        writeJson(result,response);
    }


    /**
     * 门店获取分期付款二维码
     */
    @RequestMapping(value = "/shopGetQrcodeByShopId")
    public void shopGetQrcodeByShopId(String shopId, HttpServletResponse response){
        Result result = new Result();
        try
        {
            result = orderMainUnionServiceI.shopGetQrcodeByShopId(shopId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result.setReturn_code(0);
            result.setReturn_info("操作失败，异常错误!");
        }
        writeJson(result,response);
    }


    /**
     * 通过mainId 获取扫码状态和发券信息
     */
    @RequestMapping(value = "/getQrcodeStateAndInfo")
    public void getQrcodeStateAndInfo(String mainId, HttpServletResponse response){
        Result result = new Result();
        try
        {
            result = orderMainUnionServiceI.getQrcodeStateAndInfo(mainId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result.setReturn_code(0);
            result.setReturn_info("操作失败，异常错误!");
        }
        writeJson(result,response);
    }


    //发久零券 0成功 1失败 3重
    private int user_quan(JSONObject jSONObject1) {
        int zt = 1;
        try {
            Map<String, String> map1 = new HashMap<>();
            map1.put("json", jSONObject1.toString());
            Map<String, Object> r_Interface = APIInterface(map1, "api/balance/operUserBalance90.ac");
            if (r_Interface != null) {
                return zt = Integer.parseInt(r_Interface.get("flag").toString());
            } else {
                return zt;
            }
        } catch (Exception e) {
            return 1;
        }
    }

    //发久零币 0成功 1失败 3重
    private int user_coin(String userId,String orderNum,Double bei_90,int state,int source) {
        int zt = 1;
        try {
            if (bei_90 > 0) {
                JSONObject jSONObject90 = new JSONObject();
                jSONObject90.put("userId", userId);
                jSONObject90.put("state", state);
                jSONObject90.put("orderNum", orderNum);
                jSONObject90.put("source", source);
                jSONObject90.put("amount", bei_90);

                Map<String, String> map90 = new HashMap<>();
                map90.put("json", jSONObject90.toString());
                Map<String, Object> r_Interface = APIInterface(map90, "api/balance/operUserCoin90.ac");
                if (r_Interface != null) {
                    return zt = Integer.parseInt(r_Interface.get("flag").toString());
                } else {
                    return zt;
                }
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 1;
        }

    }

    //接口
    public Map<String, Object> APIInterface(Map<String, String> jSONObject, String url) {
        Map<String, Object> map2 = new HashMap<String, Object>();
        try {
            String requestUrl = ConfigUtil.get("QT_Url") + url;
            String x = URLConectionUtil.httpURLConnectionPostDiy(requestUrl, jSONObject);
            x = URLDecoder.decode(x, "utf-8");
            if (x == null || x.trim().equals("") || x.trim().equals("失败")) {
                // 失败
            } else {
                map2 = JSON.parseObject(x, Map.class);
                System.out.println("diao_yong_90_bei::"+map2);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            return map2;
        }
    }


}
