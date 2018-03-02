package com.biz.controller.api;


import com.alibaba.fastjson.JSON;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.*;
import com.biz.model.Pmodel.basic.Base90Detail;
import com.biz.model.Pmodel.offlineCard.OfflineCardDetail;
import com.biz.model.Pmodel.offlineCard.PofflineCard;
import com.biz.service.api.ApiInterfaceServiceI;
import com.biz.service.api.QueryClerkServiceI;
import com.biz.service.api.WxUtilServiceI;
import com.biz.service.basic.ShopServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.ConfigUtil;
import com.framework.utils.DateUtil;
import com.framework.utils.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.framework.utils.UuidUtil.get32UUID;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * qt接口
 *
 * @author  lzq
 *
 */
@Controller
@RequestMapping("/api/QTclerk")
public class QueryClerkController extends BaseController {
    @Resource(name = "queryClerkService")
    private QueryClerkServiceI queryClerkService;

    @Autowired
    private ShopServiceI shopService;

    @Resource(name = "wxUtilService")
    private WxUtilServiceI wxUtilService;

    @Resource(name = "apiInterfaceService")
    private ApiInterfaceServiceI apiInterfaceService;

    /**
     * 查询收银员-返回phoneNumber
     *
     * @param shop_id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/phone_QueryClerk")
    @ResponseBody
    public void phoneQueryClerk(String shop_id, HttpServletResponse response) throws Exception {
        JSONObject jSONObject = new JSONObject();
        try {
//            shop_id="536f154159f94eafad334e1fb68cca7d";
            List<HashMap<String, Object>> user_list = queryClerkService.queryClerkByShopWithPhone(shop_id);
            JSONArray subMsgs = JSONArray.fromObject(user_list);
            jSONObject.put("return_code", 1);
            jSONObject.put("return_info", "");
            jSONObject.put("return_data", subMsgs);
            logger.error("查询收银员=返回数据jSONObject=" + jSONObject);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg.length() > 30) {
                msg = msg.substring(0, 29);
            }
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", msg);
            jSONObject.put("return_data", null);
            logger.error("查询收银员=报错=", e.fillInStackTrace());
            e.printStackTrace();
        }
       writeJson(jSONObject,response);
    }

    /**
     * 手机端新增店员+PHONENUMBER
     *
     * @param shop_id
     * @param person_name
     * @param login_name
     * @param pwd
     * @return
     */
    @RequestMapping(value = "/phone_AddClerk")
    public
    @ResponseBody
    void phoneAddClerk(String shop_id,
                         String person_name, String login_name, String pwd, String phone,HttpServletResponse response) {
        JSONObject jSONObject = new JSONObject();
//        Result result = new Result();
        try {
            int login_num = queryClerkService.findLoginName(login_name);
            if (login_num > 0) {
                jSONObject.put("return_code","0");
                jSONObject.put("return_info","用户名已存在！");
            } else if (queryClerkService.getUserCountByPhone(phone) > 0) {
                jSONObject.put("return_code","0");
                jSONObject.put("return_info","手机号已被其他用户绑定，请填写新的手机号！");
            } else {
                User clerk_user = new User();
                clerk_user.setUser_code(getRndCode());
                clerk_user.setLogin_name(login_name);
                clerk_user.setPerson_name(person_name);
                clerk_user.setPwd(pwd);
                clerk_user.setPhone(phone);
                clerk_user.setIdentity(4);
                clerk_user.setIdentity_code(shop_id);
                clerk_user.setType(2);
                clerk_user.setIslock(0);
                clerk_user.setRole_code("b94e46fac1fe4160a499456f2b18ed13");
                queryClerkService.insertShopUser(clerk_user);
                jSONObject.put("return_code","1");
                jSONObject.put("return_info","新增店员成功");
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg.length() > 30) {
                msg = msg.substring(0, 29);
            }
            jSONObject.put("return_code","0");
            jSONObject.put("return_info",msg);
            logger.error("新增店员接口=报错=" + e.fillInStackTrace());
            e.printStackTrace();
        }
       writeJson(jSONObject,response);
    }

    /**
     * 锁定店员帐号并删除企业通讯录
     *
     * @param clerk_user_code
     * @param islock
     * @return
     */
    @RequestMapping(value = "/phone_LockClerk")
    public
    @ResponseBody
    void phone_LockClerk(String clerk_user_code,
                           int islock,HttpServletResponse response) {
        JSONObject jSONObject = new JSONObject();
//        clerk_user_code="fbee025995c74f14b42657b9fdba7910";
        try {
            User clerk_user=new User();
            clerk_user.setIslock(islock);
            clerk_user.setUser_code(clerk_user_code);
            queryClerkService.updateClerkUserLock(clerk_user);
            jSONObject.put("return_code","1");
            jSONObject.put("return_info","更改锁定状态成功");
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg.length() > 30) {
                msg = msg.substring(0, 29);
            }
            jSONObject.put("return_code","0");
            jSONObject.put("return_info",msg);
            logger.error("锁定店员接口=报错=" + e.fillInStackTrace());
            e.printStackTrace();
        }
        writeJson(jSONObject,response);
    }

    /**
     * 删除店员
     *
     * @param user_code
     * @return
     */
    @RequestMapping(value = "/phone_DelClerk")
    public
    @ResponseBody
    void phoneDelClerk(String user_code,HttpServletResponse response) {
        JSONObject jSONObject = new JSONObject();
        try {
            String res = queryClerkService.delClerkByCode(user_code);
            if ("success".equalsIgnoreCase(res)) {
                jSONObject.put("return_code","1");
                jSONObject.put("return_info","删除店员成功");
            } else {
                jSONObject.put("return_code","0");
                jSONObject.put("return_info","删除店员失败");
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg.length() > 30) {
                msg = msg.substring(0, 29);
            }
            jSONObject.put("return_code","0");
            jSONObject.put("return_info",msg);
            logger.error("删除店员接口=报错=" + e.fillInStackTrace());
            e.printStackTrace();
        }
writeJson(jSONObject,response);
    }

    /**
     * 更新店员手机号
     *
     * @param user_code
     * @param phone
     * @return
     */
    @RequestMapping(value = "phone_updatePhone")
    public
    @ResponseBody
    void phone_updatePhone(String user_code, String phone,HttpServletResponse response) {
        JSONObject jSONObject = new JSONObject();
        try {
            User user = queryClerkService.getUserByCode(user_code);
            if(user!=null){
                if (phone.equals(user.getPhone())) {
                    jSONObject.put("return_code","0");
                    jSONObject.put("return_info","店员手机号与之前相同，无需变更");
                } else if (queryClerkService.getUserCountByPhone(phone) > 0) {
                    jSONObject.put("return_code","0");
                    jSONObject.put("return_info","手机号已被其他用户绑定，请填写新的手机号！");
                } else {
                    user.setPhone(phone);
                    queryClerkService.updateUserPhone(user);
                    jSONObject.put("return_code","1");
                    jSONObject.put("return_info","更新店员手机号成功！");
                }
            }else{
                jSONObject.put("return_code","0");
                jSONObject.put("return_info","查无此用户！");
            }

        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg.length() > 30) {
                msg = msg.substring(0, 29);
            }
            jSONObject.put("return_code","0");
            jSONObject.put("return_info",msg);
            logger.error("更新店员手机号接口=报错=" + e.fillInStackTrace());
            e.printStackTrace();
        }
        writeJson(jSONObject,response);
    }

    /**
     * 修改店员密码
     *
     * @param user_code
     * @param pwd
     * @param clerk_user_code
     * @param clerk_pwd
     * @return
     */
    @RequestMapping(value = "/phone_resetClerkPwd")
    public
    @ResponseBody
    void phone_resetClerkPwd(String user_code,
                               String pwd, String clerk_user_code, String clerk_pwd,HttpServletResponse response) {
        JSONObject jSONObject = new JSONObject();
        try {
            User user = queryClerkService.getUserByCode(user_code);
            if(user!=null){
                if (user.getPwd().equalsIgnoreCase(pwd)) {
                    User clerk_user = new User();
                    clerk_user.setUser_code(clerk_user_code);
                    clerk_user.setPwd(clerk_pwd);
                    queryClerkService.updateUserPwd(clerk_user);
                    jSONObject.put("return_code","1");
                    jSONObject.put("return_info","修改店员密码成功");
                } else {
                    jSONObject.put("return_code","0");
                    jSONObject.put("return_info","管理员密码不正确，修改失败");
                }
            }else{
                jSONObject.put("return_code","0");
                jSONObject.put("return_info","查不到管理员");
            }

        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg.length() > 30) {
                msg = msg.substring(0, 29);
            }
            jSONObject.put("return_code","0");
            jSONObject.put("return_info",msg);
            logger.error("修改店员密码接口=报错=" + e.fillInStackTrace());
            e.printStackTrace();
        }
        writeJson(jSONObject,response);
    }


    /**
     * 移动端用户修改密码
     *
     * @param user_code
     * @param pwd
     * @param
     * @param new_pwd
     * @return
     */
    @RequestMapping(value = "/phone_reset_pwd")
    public
    @ResponseBody
    void phone_reset_pwd(String user_code,
                             String pwd, String new_pwd,HttpServletResponse response) {
        JSONObject jSONObject = new JSONObject();
        try {
            User user = queryClerkService.getUserByCode(user_code);
            if(user!=null){
                if (user.getPwd().equalsIgnoreCase(pwd)) {
                    User clerk_user = new User();
                    clerk_user.setUser_code(user_code);
                    clerk_user.setPwd(new_pwd);
                    queryClerkService.updateUserPwd(clerk_user);
                    jSONObject.put("return_code","1");
                    jSONObject.put("return_info","移动端用户修改密码密码成功");
                } else {
                    jSONObject.put("return_code","0");
                    jSONObject.put("return_info","原密码不正确，修改失败");
                }
            }else{
                jSONObject.put("return_code","0");
                jSONObject.put("return_info","查不到该用户");
            }

        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg.length() > 30) {
                msg = msg.substring(0, 29);
            }
            jSONObject.put("return_code","0");
            jSONObject.put("return_info",msg);
            logger.error("修改店员密码接口=报错=" + e.fillInStackTrace());
            e.printStackTrace();
        }
        writeJson(jSONObject,response);
    }


//    /**
//     * 线下支付接口
//     *
//     * @author zhengXin Update 添加参数type
//     * 添加参数type,用于保存数据类型,type = xj 为现金支付，type=union 为银联
//     * 通过type值判断，修改订单的支付类型Trade_type 现金支付=offline 银联=UNIONPAY
//     */
//    @RequestMapping(value = "/phone_offLinePay")
//    public @ResponseBody Result phone_offLinePay(String shop_id,String user_code, String total, String order_code,
//                                                 String device_info, String device_ip,String cash_total, String open_id, String type){
//        logger.error("线下支付接口=" + "=shop_id="
//                + shop_id + "=user_code=" + user_code + "=total=" + total + "=order_code=" + order_code
//                + "=device_info=" + device_info + "=device_ip=" + device_ip+ "=cash_total=" + cash_total+ "=open_id=" + open_id);
//        Result result = new Result();
//        /***********对传入金额处理*************/
//        double double_total = 0d;//分
//        int int_total = 0; //分
//        double double_cash_total = 0d;//分
//        int int_cash_total = 0; //分
//        double needMoney=0;
//        try {
//            double_total = money_convert(total);
//            int_total = money_convert_Int(total);
//            double_cash_total = money_convert(cash_total);
//            int_cash_total = money_convert_Int(cash_total);
//        } catch (Exception e) {
//            result.setReturn_code(0);
//            result.setReturn_info("金额错误，金额为"+total);
//            logger.error("线下支付接口金额错误，金额为"+total);
//            return result;
//        }
//        /***********对传入金额处理*************/
//        try {
//            Shop shop = shopService.getShopBySid(shop_id);
//            if(shop == null ){
//                result.setReturn_code(0);
//                result.setReturn_info("门店不存在");
//                logger.error("门店不存在=shop_id="+shop_id);
//                return result;
//            }
//            OrderMain90 orderMain90 = queryClerkService.getOrderMain90ByCode(order_code);
//            if(orderMain90 != null ){
//                result.setReturn_code(0);
//                result.setReturn_info("订单已存在");
//                logger.error("订单已存在=order_code="+order_code);
//                return result;
//            }
//            String tSysUserId = queryClerkService.getSysUserIdByOpenId(open_id);
//            /****************调用付90币接口*************************/
//            if(tSysUserId!=null && !tSysUserId.trim().equals(""))
//            {
//                Map<String,String> map90=new HashMap<>();
//                JSONObject jSONObject90 = new JSONObject();
//                jSONObject90.put("userId",tSysUserId);
//                jSONObject90.put("state","2");
//                jSONObject90.put("orderNum",order_code);
//                jSONObject90.put("source","2");
//                jSONObject90.put("amount",cash_total);
//                map90.put("json", jSONObject90.toString());
//                System.out.print(jSONObject90.toString());
//                String requestUrl = ConfigUtil.get("BALANCE_90")+"orther_operUserCoin90.do";
//                String x =URLConectionUtil.httpURLConnectionPostDiy(requestUrl, map90);
//                x = URLDecoder.decode(x, "utf-8");
//                if(x==null||x.trim().equals(""))
//                {
//
//                    System.out.print("调90贝付款接口调用失败");
//                }else
//                {
//                    Map<String, Object> map2 = JSON.parseObject(x, Map.class);
//                    if(map2!=null && map2.get("flag").equals("0"))
//                    {
//                        needMoney = Double.valueOf(map2.get("needMoney").toString());
//                        System.out.print("调90贝付款接口付款成功");
//                    }else
//                    {
//                        System.out.print("调90贝付款接口付款失败");
//                    }
//                }
//            }
//
//            /****************调用店小翼支付接口**********************/
//            /****************支付成功，判断用户是否会员**********************/
//            String trade_type = "offline";
//            if (StringUtils.isNotBlank(type) && type.equals("xj"))
//            {
//                trade_type = "offline";
//            } else if (StringUtils.isNotBlank(type) && type.equals("union"))
//            {
//                trade_type = "UNIONPAY";
//            }
//            //1.是会员，直接推送模板消息，订单-兴业手续费，久零券
//            orderMain90 = new OrderMain90();
//            orderMain90.setCode(order_code);
//            orderMain90.setOrder_total(needMoney*100);
//            orderMain90.setCash_total(double_cash_total);
//            orderMain90.setCard_total(0);
//            orderMain90.setCard_count(0);
//            orderMain90.setOpen_id(open_id);
//            orderMain90.setUser_code(user_code);
//            orderMain90.setShop_code(shop_id);
//            orderMain90.setBrand_code(shop.getBrand_code());
//            orderMain90.setTrade_type(trade_type);
//            orderMain90.setPay_type(5);
//            orderMain90.setGift_90(0);
//            orderMain90.setPay_90(double_total);
//            orderMain90.setState(1);
//            orderMain90.setPay_time(DateUtil.getTime());
//            orderMain90.setPay_user_id("");
//            orderMain90.setGift_90(double_cash_total);
//
//            Map<String,String> map1=new HashMap<>();
//            JSONObject jSONObject1 = new JSONObject();
//            jSONObject1.put("sceneType","1");
//            jSONObject1.put("open_id",open_id);
//            jSONObject1.put("total_90",double_total);
//            jSONObject1.put("shop_code",shop_id);
//            jSONObject1.put("source_code",order_code);
//            jSONObject1.put("brand_code",shop.getBrand_code());
//            jSONObject1.put("user_code",user_code);
//            map1.put("json", jSONObject1.toString());
//            System.out.print(jSONObject1.toString());
//            String requestUrl = ConfigUtil.get("BALANCE_90")+"orther_minus_balance_90.do";
//            String x = URLConectionUtil.httpURLConnectionPostDiy(requestUrl, map1);
//            x = URLDecoder.decode(x, "utf-8");
//            if(x==null||x.trim().equals("")){
//                result.setReturn_code(0);
//                result.setReturn_info("减劵失败");
//            }else {
//                System.out.println(x.toString());
//                @SuppressWarnings({"unused", "unchecked"})
//                Map<String, Object> map2 = JSON.parseObject(x, Map.class);
//                if(map2!=null && map2.get("return_code").equals("1"))  //发劵成功
//                {
//                    result.setReturn_code(1);
//                    result.setReturn_info("减劵成功");
//                }else
//                {
//                    result.setReturn_code(0);
//                    result.setReturn_info("减劵失败");
//                }
//            }
//            /******************减去用户90券**************************//*
//			minus_balance_90(open_id,double_total);
//			*//******************增加用户90券**************************//*
//			//add_balance_90(open_id,double_cash_total);
//			*//******************消费发券记录**************************//*
//			logger.error("=线下支付消费发券记录=open_id=" + open_id);
//			Base90Detail base90Detail = new Base90Detail();
//			base90Detail.setBrand_code(shop.getBrand_code());
//			base90Detail.setShop_code(shop_id);
//			base90Detail.setSource_code(order_code);
//			base90Detail.setOpen_id(open_id);
//			base90Detail.setSource(3);
//			base90Detail.setSource_msg("体验店消费");
//			base90Detail.setPoint_90(int_total);
//			base90Detail.setUser_code(user_code);
//			base90DetailService.insertBase90Detail(base90Detail);*/
//
//
//
//
////			/******************赠送发券记录**************************/
////			logger.error("=线下支付赠送发券记录=open_id=" + open_id);
////			Base90Detail gbase90Detail = new Base90Detail();
////			gbase90Detail.setBrand_code(shop.getBrand_code());
////			gbase90Detail.setShop_code(shop_id);
////			gbase90Detail.setSource_code(order_code);
////			gbase90Detail.setOpen_id(open_id);
////			gbase90Detail.setSource(1);
////			gbase90Detail.setSource_msg("服务费发放券");
////			gbase90Detail.setPoint_90(int_cash_total*-1);
////			base90Detail.setUser_code(user_code);
////			base90DetailService.insertBase90Detail(gbase90Detail);
//            queryClerkService.insertOrderMain90(orderMain90);
//            BaseUser baseUser = queryClerkService.getBaseUserByOpen_id(open_id);
//            result.setReturn_code(1);
//            result.setReturn_info("offline"+","+baseUser.getPhone()+","+(double_cash_total/100));
//            send_pay_template(open_id,orderMain90,orderMain90.getPay_time());
//        } catch (Exception e){
//            String msg = e.getMessage();
//            if(StringUtils.isNotBlank(msg) && msg.length()>30){
//                msg = msg.substring(0, 29);
//            }
//            result.setReturn_code(0);
//            result.setReturn_info("错误-" + msg);
//            logger.error("=线下支付=报错=" + e, e.fillInStackTrace());
//        }
//        return result;
//    }

    /**
     * 推送支付模板信息（90体验店）
     */
    protected void send_pay_template(String open_id,OrderMain90 orderMain90,String pay_time)throws Exception{
        logger.error("=推送支付模板信息=open_id=" + open_id);
        queryClerkService.send_pay_template(open_id, orderMain90.getCode(),orderMain90.getPay_90(),pay_time);
    }

    /**
     * 增加90劵接口
     * @throws Exception
     */
    @RequestMapping(value = "/orther_add_balance_90")
    public synchronized @ResponseBody void other_add_balance_90(String json,HttpServletResponse response) throws Exception
    {
        Map<String,Object> map=new HashMap<String, Object>();
        JSONObject jSONObject = new JSONObject();
        System.out.print("进入发劵接口");
        try {
            map=JSON.parseObject(json,Map.class);
            String id=get32UUID();
            if(map.get("sceneType")!=null){
                String sceneType=map.get("sceneType").toString().trim();
                if(sceneType.equals("1"))  //联盟商户后台手工发券
                {
                    String phone=map.get("phone").toString();
                    String brand_code=map.get("brand_code").toString();
                    String total_90 = map.get("total_90").toString();  //元
                    String user_code = map.get("user_code").toString();
                    jSONObject=doIssuedGift(phone,brand_code,total_90,user_code);

                }else if(sceneType.equals("2"))  //实体卡  提取卡密 发券
                {
                    String open_id=map.get("open_id").toString();
                    double card_total= Double.parseDouble(map.get("total_90").toString());  //分
                    String shop_code = map.get("shop_code").toString();
                    String source_code = map.get("source_code").toString();
                    jSONObject=extract_card(open_id,card_total,shop_code,source_code);
                }else if(sceneType.equals("3"))  //QT、pos机\刷银联卡  支付完成自动发券
                {
                    String open_id=map.get("open_id").toString();
                    double total_90= Double.parseDouble(map.get("total_90").toString());  //分
                    String shop_code = map.get("shop_code").toString();
                    String source_code = map.get("source_code").toString();
                    String brand_code=map.get("brand_code").toString();
                    String user_code = map.get("user_code").toString();
                    String pay_type = map.get("pay_type").toString();//1:QT、pos机，2:刷银联卡
                    jSONObject=auto_balance(open_id,total_90,shop_code,source_code,brand_code,user_code,pay_type);
                }else if(sceneType.equals("4")) //联盟商户、90体验店线下手工发券（扫带参二维码发劵）
                {
                    String rg_code = map.get("rg_code").toString();
                    String open_id = map.get("open_id").toString();
                    jSONObject=doIssuedSceneGift(rg_code, open_id);
                }else if(sceneType.equals("5")) //线上购买联盟商户的商品自动发券
                {
                    jSONObject=online_addBalance(map);
                }else if(sceneType.equals("6"))//支付完成未能自动发券转（扫带参二维码发劵）
                {
                    String orderMainId = map.get("orderMainId").toString();
                    String open_id = map.get("open_id").toString();
                    int scene_id = Integer.valueOf(map.get("scene_id").toString());
                    jSONObject=autoSceneBalance(orderMainId,open_id,scene_id);
                }
                else if(sceneType.equals("7")){//首次关注加积分
                    String open_id = map.get("open_id").toString();
                    //getFirstConcern(open_id);//老的舍弃
                    boolean issave= queryClerkService.addActivityListDate(open_id);
                }  else if(sceneType.equals("8")){//每日签到增加90券
                    jSONObject=sign_addBalance(map);
                }
            }else
            {
                jSONObject.put("return_code", "0");
                jSONObject.put("return_info","无效的调用场景");
            }

        } catch (Exception e) {
            jSONObject.put("return_code", "0");
            jSONObject.put("return_info","操作失败");
            writeJson(jSONObject,response);
        }
        writeJson(jSONObject,response);
    }

    //联盟商户后台手工发券
    private JSONObject doIssuedGift(String phone,String brand_code,String total_90,String user_code)
    {
        JSONObject jSONObject = new JSONObject();

        try {
            BaseUser baseuser = queryClerkService.getBaseUserByPhone(phone);
            Brand brand = queryClerkService.getBrandOnlyByCode(brand_code);
            if(baseuser == null){
                jSONObject.put("return_code", "0");
                jSONObject.put("return_info","该手机号的用户不存在");
                return jSONObject;
            }else if(brand == null ){
                jSONObject.put("return_code", "0");
                jSONObject.put("return_info","商户不存在");
                return jSONObject;
            }else{
                double balance_90 = 0d;//分
                try {
                    balance_90 = money_convert(total_90);
                } catch (Exception e) {
                    jSONObject.put("return_code", "0");
                    jSONObject.put("return_info","券额错误");
                    return jSONObject;
                }
                long brand_balance_90 = brand.getBalance_90();//九零券余额（分）
                long credit_total_90 = brand.getCredit_total_90();//久零券透支额度（分）
                long credit_now_90 = brand.getCredit_now_90();//久零券当前透支额度（分）
                if((brand_balance_90 + credit_total_90 - credit_now_90) <  balance_90){
                    int edu = (int) (credit_now_90/100);
                    jSONObject.put("return_code", "0");
                    jSONObject.put("return_info","商户额度不够，当前可用透支额度:"+edu);
                    return jSONObject;
                }else {
                    //人工发券记录
                    RgGift rgGift = new RgGift();
                    String code = getRndCode();
                    rgGift.setCode(code);
                    rgGift.setBrand_code(brand_code);
                    rgGift.setShop_code("");
                    rgGift.setUser_code(user_code);
                    rgGift.setPoint_90(balance_90);
                    rgGift.setState(0);
                    queryClerkService.insertRgGift(rgGift);
                    /******************增加用户90券**************************/
                    add_balance_90(baseuser.getOpen_id(),balance_90);
                    /******************商户扣除90券**************************/
                    if(brand_balance_90>=balance_90){
                        Map<String,Object> pd = new HashMap<>();
                        pd.put("brand_code", brand_code);
                        pd.put("balance_90", balance_90);
                        queryClerkService.minus_balance_90(pd);
                    }else{
                        long minus = (long) (balance_90 - brand_balance_90);
                        Map<String,Object> pd = new HashMap<>();
                        pd.put("brand_code", brand_code);
                        pd.put("minus", minus);
                        queryClerkService.add_credit_now_90(pd);
                    }
                    /******************发券记录**************************/
                    Base90Detail base90Detail = new Base90Detail();
                    base90Detail.setBrand_code(brand_code);
                    base90Detail.setShop_code(rgGift.getShop_code());
                    base90Detail.setSource_code(rgGift.getUser_code());
                    base90Detail.setOpen_id(baseuser.getOpen_id());
                    base90Detail.setSource(2);
                    base90Detail.setSource_msg("远程人工发券");
                    int point_90 = (int) (balance_90*-1);
                    base90Detail.setPoint_90(point_90);
                    base90Detail.setUser_code(rgGift.getUser_code());
                    queryClerkService.insertBase90Detail(base90Detail);
                    /******************更新人工发券**************************/
                   Map<String,Object> map=new HashMap<>();
                    map.put("code", code);
                    map.put("open_id", baseuser.getOpen_id());
                    map.put("state", 1);
                    map.put("get_time", DateUtil.getTime());
                    queryClerkService.updateWhereRgGift(map);
                    /******************向用户发送模板消息************************/
                    String url =ConfigUtil.get("weixinShop_URL")+"client_toClientCenter.do";
                    String title= "亲，恭喜你 "+brand.getName()+" 向你发放了"+balance_90/100+"元久零券";
                    String content = "点此进入个人中心查看！";
                    queryClerkService.kf_reply_news(baseuser.getOpen_id(),title,content,"",url);
                    jSONObject.put("return_code", "1");
                    jSONObject.put("return_info","发劵成功!");
                    return jSONObject;
                }
            }
        } catch (Exception e) {
            jSONObject.put("return_code", "0");
            jSONObject.put("return_info","调用出错");
            return jSONObject;
        }
    }

    /**
     * 增加用户90券
     */
    protected void add_balance_90(String open_id, double balance_90){
        logger.error("=增加用户90券=open_id=" + open_id + "=balance_90="
                + balance_90);
        try {
           Map<String,Object> map=new HashMap<>();
            map.put("open_id", open_id);
            map.put("balance_90", balance_90);
            queryClerkService.add_balance_90(map);
        } catch (Exception e) {
            logger.error("=增加用户90券=报错=" + e, e.fillInStackTrace());
        }
    }

    //实体卡  提取卡密 发券
    private JSONObject extract_card(String open_id,double card_total,String shop_code,String source_code)
    {
        JSONObject jSONObject = new JSONObject();
        try {
            /******************增加用户90券**************************/
            add_balance_90(open_id,card_total);
            //插入base_90_detail
            /******************发券记录**************************/
            Base90Detail base90Detail = new Base90Detail();
            base90Detail.setBrand_code("");
            base90Detail.setShop_code(shop_code);
            base90Detail.setSource_code(source_code);
            base90Detail.setOpen_id(open_id);
            base90Detail.setSource(4);
            base90Detail.setSource_msg("久零券充值卡");
            int point_90 = (int) (card_total*-1);
            base90Detail.setPoint_90(point_90);
            base90Detail.setUser_code("");
            queryClerkService.insertBase90Detail(base90Detail);
            /******************向用户发送模板消息************************/
            String url = ConfigUtil.get("weixinShop_URL")+"client_toClientCenter.do";
            String title= "亲，恭喜你 "+" 成功充值"+card_total/100+"元久零券";
            String content = "点此进入个人中心查看！";
            queryClerkService.kf_reply_news(open_id,title,content,"",url);
            jSONObject.put("return_code", "1");
            jSONObject.put("return_info","发劵成功!");
            return jSONObject;
        } catch (Exception e) {
            jSONObject.put("return_code", "0");
            jSONObject.put("return_info","调用出错");
            return jSONObject;
        }

    }


    //QT、pos机、刷银联卡支付完成自动发券
    private JSONObject auto_balance(String open_id,double total_90,String shop_code,String source_code,String brand_code,String user_code,String pay_type)
    {
        JSONObject jSONObject = new JSONObject();

        try {
            //增加用户90券

            add_balance_90(open_id,total_90);
            String msg="";
            if(pay_type.trim().equals("1"))
            {
                msg="微信刷卡付自动发券";
            }else if(pay_type.trim().equals("2"))
            {
                msg="银联支付自动发券";
            }
            //增加发券记录
            add_auto_balance(brand_code,shop_code,source_code,open_id,total_90,user_code,1,msg);
            jSONObject.put("return_code", "1");
            jSONObject.put("return_info", "发劵成功!");
            return jSONObject;
        } catch (Exception e) {
            jSONObject.put("return_code", "0");
            jSONObject.put("return_info","调用出错");
            return jSONObject;
        }
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
        queryClerkService.insertBase90Detail(base90Detail);
    }

    //联盟商户、90体验店线下手工发券（扫带参二维码发劵）
    private JSONObject doIssuedSceneGift(String rg_code,String open_id)
    {
        JSONObject jSONObject = new JSONObject();
        System.out.print("进入扫带参二维码发劵方法");
        try {
            RgGift rgGift = queryClerkService.getRgGiftByCode(rg_code);
            double balance_90 = rgGift.getPoint_90();
            String brand_code = rgGift.getBrand_code();
            Brand brand = queryClerkService.getBrandOnlyByCode(brand_code);
            long brand_balance_90 = brand.getBalance_90();//九零券余额（分）
            long credit_total_90 = brand.getCredit_total_90();//久零券透支额度（分）
            long credit_now_90 = brand.getCredit_now_90();//久零券当前透支额度（分）
            if((brand_balance_90 + credit_total_90 - credit_now_90) <  balance_90){
                queryClerkService.send_kf_template(open_id,"商户额度不够！");
                jSONObject.put("return_code", "0");
                jSONObject.put("return_info","商户额度不够");
                return jSONObject;
            }
            /******************增加用户90券**************************/
            add_balance_90(open_id,balance_90);
            /******************商户扣除90券**************************/
            if(brand_balance_90>=balance_90){
                Map<String,Object> pd = new HashMap<>();
                pd.put("brand_code", brand_code);
                pd.put("balance_90", balance_90);
                queryClerkService.minus_balance_90(pd);
            }else{
                long minus = (long) (balance_90 - brand_balance_90);
                Map<String,Object> pd = new HashMap<>();
                pd.put("brand_code", brand_code);
                pd.put("minus", minus);
                queryClerkService.add_credit_now_90(pd);
            }
            /******************发券记录**************************/
            Base90Detail base90Detail = new Base90Detail();
            base90Detail.setBrand_code(brand_code);
            base90Detail.setShop_code(rgGift.getShop_code());
            base90Detail.setSource_code(rgGift.getUser_code());
            base90Detail.setOpen_id(open_id);
            base90Detail.setSource(2);
            base90Detail.setSource_msg("店员人工发券");
            int point_90 = (int) (balance_90*-1);
            base90Detail.setPoint_90(point_90);
            base90Detail.setUser_code(rgGift.getUser_code());
            base90Detail.setRg_code(rgGift.getCode());
            queryClerkService.insertBase90Detail(base90Detail);
            /******************更新人工发券**************************/
            Map<String,Object> pd = new HashMap<>();
            pd.put("code", rg_code);
            pd.put("open_id", open_id);
            pd.put("state", 1);
            pd.put("get_time", DateUtil.getTime());
            queryClerkService.updateWhereRgGift(pd);

            String url =ConfigUtil.get("weixinShop_URL")+"client_toClientCenter.do";
            String title= "亲，恭喜你成功领取了"+balance_90/100+"元久零券";
            String content = "点此进入个人中心查看！";
            queryClerkService.kf_reply_news(open_id,title,content,"",url);
            jSONObject.put("return_code", "1");
            jSONObject.put("return_info","发劵成功!");
            return jSONObject;
        } catch (Exception e) {
            jSONObject.put("return_code", "0");
            jSONObject.put("return_info","调用出错");
            return jSONObject;
        }
    }

    //线上购买的商品自动发券
    private JSONObject online_addBalance(Map<String,Object> map)
    {
        JSONObject jSONObject = new JSONObject();
        try {
            Brand brand = queryClerkService.getBrandOnlyByCode(map.get("brand_code").toString());
            double jl_total=0;
            if("1".equals(map.get("type").toString().trim())) {
                jl_total = (brand.getProportion() == 0 ? 1 : brand.getProportion()) * Double.valueOf(map.get("balance_90").toString());
            }
            else
            {
                jl_total=Double.valueOf(map.get("balance_90").toString());
            }
            map.put("balance_90",jl_total);

            jSONObject=queryClerkService.oper_balance_90ByWX(map);
            try {
                if(jSONObject.get("flag").toString().trim().equals("0")) //执行成功
                {
                    if(map.get("type").toString().trim().equals("1")||map.get("type").toString().trim().equals("5"))//发送加券消息
                    {

                        String url = ConfigUtil.get("weixinShop_URL")+"client_toClientCenter.do";
                        String title= "亲，恭喜你"+" 增加"+Double.valueOf(map.get("balance_90").toString())/100+"元久零券";
                        String content = "点此进入个人中心查看！";
                        queryClerkService.kf_reply_news(map.get("open_id").toString(),title,content,"",url);
                    }else if(map.get("type").toString().trim().equals("3"))//发送减券消息
                    {

                        String url =ConfigUtil.get("weixinShop_URL")+"client_toClientCenter.do";
                        String title= "亲，"+"扣除"+Double.valueOf(map.get("balance_90").toString())/100+"元久零券";
                        String content = "点此进入个人中心查看！";
                        queryClerkService.kf_reply_news(map.get("open_id").toString(),title,content,"",url);
                    }
                }
            } catch (Exception e) {
                System.out.printf(e.getMessage());
                jSONObject.put("flag", "1");
                jSONObject.put("msg","操作失败，计算错误");
                return jSONObject;
            }
            return jSONObject;
        } catch (Exception e) {
            jSONObject.put("flag", "1");
            jSONObject.put("msg","操作失败,异常错误");
            return jSONObject;
        }
    }

    //支付完成未能自动发券转（扫带参二维码发劵）
    private JSONObject autoSceneBalance(String orderMainId,String open_id,int scene_id)
    {
        JSONObject jSONObject = new JSONObject();
        try
        {
            OrderMainUnion orderMain = queryClerkService.getOrderMainUnionByCode(orderMainId);
            if(orderMain == null){
                jSONObject.put("return_code", "0");
                jSONObject.put("return_info","发劵失败");
                return jSONObject;
            }
            if(orderMain.getState() != 1){
                jSONObject.put("return_code", "0");
                jSONObject.put("return_info","发劵失败");
                return jSONObject;
            }

           /* Brand brand = brandService.getBrandOnlyByCode(orderMain.getBrand_code());
            double jl_total=0;
            jl_total = (brand.getProportion()==0?1:brand.getProportion())*orderMain.getCash_total();*/
            //处理90券	is_clean 0:未结账1已结账
            if(orderMain.getIs_clean() == 0){
                //增加发券记录
                queryClerkService.add_auto_balance(orderMain.getBrand_code(), orderMain.getShop_code(), orderMainId, open_id, orderMain.getGift_90(), orderMain.getUser_code());
            }else if(orderMain.getIs_clean() == 1){
                //人工发券
                queryClerkService.add_rg_balance(orderMain.getBrand_code(), orderMain.getShop_code(), orderMainId, open_id, orderMain.getGift_90(), orderMain.getUser_code());
            }
            //增加用户90券
            queryClerkService.add_balance_90(open_id, orderMain.getGift_90());
            //发送模板消息
            send_pay_template(scene_id,open_id,orderMain);
            jSONObject.put("return_code", "1");
            jSONObject.put("return_info","发劵成功!");
            return jSONObject;
        }
        catch(Exception e){
            jSONObject.put("return_code", "0");
            jSONObject.put("return_info","调用出错");
            return jSONObject;
        }
    }

    /**
     * 推送支付模板信息
     */
    protected void send_pay_template(int scene_id, String open_id,
                                     OrderMainUnion orderMainUnion) throws Exception {
        queryClerkService.send_pay_template(scene_id, open_id, orderMainUnion);
    }

    private JSONObject sign_addBalance(Map<String, Object> map) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject=queryClerkService.sign_addBalance(map);
            try {
                if(jSONObject.get("flag").toString().trim().equals("0")) //执行成功
                {
                    String url = ConfigUtil.get("weixinShop_URL")+"client_toClientCenter.do";
                    String title= "亲，恭喜你"+" 增加"+Double.valueOf(map.get("balance_90").toString())/100+"元久零券";
                    String content = "点此进入个人中心查看！";
                    queryClerkService.kf_reply_news(map.get("open_id").toString(),title,content,"",url);
                }
            } catch (Exception e) {
                System.out.printf(e.getMessage());
                jSONObject.put("flag", "1");
                jSONObject.put("msg","操作失败，发送模板消息错误");
                return jSONObject;
            }
            return jSONObject;
        } catch (Exception e) {
            jSONObject.put("flag", "1");
            jSONObject.put("msg","操作失败,异常错误");
            return jSONObject;
        }
    }


    /**
     * 提取卡密
     */
    @RequestMapping(value = "/orther_extract_card", method = RequestMethod.POST)
    public void client_extract_card(String card_code,String open_id,HttpServletResponse response) throws Exception {
        //充值记录
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("errmsg", "ok");
        List<String> userBalanceList=queryClerkService.getUserBalanceDetailListByOpenId(open_id);
        if(StringUtils.isBlank(card_code)){
            jSONObject.put("errmsg", "卡券id为空");
        }else
        {//验证是否正确
            OfflineCardDetail offlineCardDetail = queryClerkService.getOfflineCardDetailByCard_code(card_code);
           //Map<String,String> userInfo=apiInterfaceService.getUserInfoByOpenId(open_id);
            if(offlineCardDetail == null){
                jSONObject.put("errmsg", "卡密不正确");
            }else if(StringUtil.isNullOrEmpty(offlineCardDetail.getBrandCode())){
                jSONObject.put("errmsg", "该充值卡尚未激活，请至泰州久零微信公众号回复联系我们");
            }
            /*else if(!offlineCardDetail.getBrandCode().equals(userInfo.get("brand_code"))){
                jSONObject.put("errmsg", "该充值卡不属于您所在的代理商！");
            }*/
            else if(offlineCardDetail.getState() == 1){
                jSONObject.put("errmsg", "卡密已使用！");
            }else if(userBalanceList.size()>0&&"1".equals(offlineCardDetail.getIsFirst())){
                jSONObject.put("errmsg", "本充值卡仅限新用户充值！");
            }else
            {
                String main_code = offlineCardDetail.getMainId();
                PofflineCard offlineCard =  queryClerkService.getOfflineCardByCode(main_code);
                if(offlineCard == null){
                    jSONObject.put("errmsg", "此批次充值卡已经失效！");
                }
                else
                {  //修改offline_card_90，offline_card_90_detail
                    Map<String,Object> map=new HashMap<>();
                    map.put("id", offlineCardDetail.getId());
                    map.put("open_id", open_id);
                    map.put("state", 1);
                    map.put("user_time", DateUtil.getTime());

                    Map<String,Object> map1=new HashMap<>();
                    JSONObject jSONObject1 = new JSONObject();
                    map1.put("brand_code",ConfigUtil.get("sign_brand"));
                    map1.put("order_code",offlineCardDetail.getCardCode());
                    map1.put("open_id",open_id);
                    map1.put("type","4");
                    map1.put("source","4");
                    map1.put("balance_90",offlineCard.getCardTotal());
                    map1.put("state","2");
                    map1.put("orderState","1");
                    Map<String,Object> res = new HashMap<String, Object>();
                    res=apiInterfaceService.updateUserBalance_90(map1);
                    if(res==null||res.equals("")){
                        jSONObject.put("errmsg", "发劵失败");
                    }else {
                        if(res!=null && res.get("flag").equals("0"))  //发劵成功
                        {
                            queryClerkService.updateWhereOfflineCardDetail(map);
                            queryClerkService.add_card_use_count(main_code);
                            jSONObject.put("errmsg", "ok");
                        }else
                        {
                            jSONObject.put("errmsg", "发劵失败");
                        }
                    }
                }

            }

        }

       writeJson(jSONObject,response);
    }

    /**
     * 久零劵明细--领取
     * @throws Exception
     */
    @RequestMapping(value = "/orther_balance_in")
    public void orther_balance_in(String open_id,String page,HttpServletResponse response)
    {
        JSONObject jSONObject = new JSONObject();
        Map<String,Object> pd = new HashMap<>();
        logger.error("久零劵明细--领取接口=open_id=" + open_id);
        if(StringUtils.isBlank(open_id)){
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", "接口参数错误：open_id为空");
            jSONObject.put("return_data", null);

        }
        else
        { pd.put("open_id", open_id);
            pd.put("state", "in");
            int page_int = Integer.valueOf(page);
            int pageSize = 10;
            pd.put("pageSize", pageSize);
            pd.put("pageNum", pageSize * (page_int - 1));

            try {

                //领取金额
                List<Base90Detail> intList = queryClerkService.selectOrtherBalanceIn(pd);
                Collections.sort(intList,new ComparatorBase90Deatil());
                for (Base90Detail item : intList) {
                    String create_time = item.getCreate_time();
                    String c_time = create_time.substring(0, create_time.lastIndexOf(" "));
                    item.setCreate_time(c_time);
                }
                //领取总额
                int int_sum = queryClerkService.int_sum(pd);
                JSONObject jsonObject_sum = new JSONObject();
                jsonObject_sum.put("int_sum", int_sum);
                JSONArray jsonStrs = new JSONArray();
                for (Base90Detail Detail : intList) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("create_time", notNull(Detail.getCreate_time()));
                    jsonObject.put("source", Detail.getSource());
                    jsonObject.put("business_name", Detail.getBusiness_name());
                    jsonObject.put("source_msg", notNull(Detail.getSource_msg()));
                    jsonObject.put("point_90", Detail.getPoint_90());
                    jsonStrs.add(jsonObject);
                }
                jsonStrs.add(jsonObject_sum);
                jSONObject.put("return_code", 1);
                jSONObject.put("return_info", "");
                jSONObject.put("return_data", jsonStrs);
            } catch (Exception e) {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", e.getMessage());
                jSONObject.put("return_data", null);
                logger.error("久零劵明细--领取=报错=" ,e.fillInStackTrace());
                e.printStackTrace();
            }
        }
       writeJson(jSONObject,response);
    }

    class ComparatorBase90Deatil implements Comparator<Base90Detail> {

        @Override
        public int compare(Base90Detail o1, Base90Detail o2) {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date dateO1= null;
            Date dateO2=null;
            try {
                dateO1 = sdf.parse(o1.getCreate_time());
                dateO2 = sdf.parse(o2.getCreate_time());

                if(dateO1.after(dateO2)){
                    return -1;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                logger.error(e.fillInStackTrace());
                return 0;
            }
            return 1;
        }
    }

    /**
     * 久零劵明细--消费
     */
    @RequestMapping(value = "/orther_balance_out")
    public void orther_balance_out(String open_id,String page,HttpServletResponse response)
    {
        JSONObject jSONObject = new JSONObject();
        Map<String,Object> pd = new HashMap<>();
        logger.error("久零劵明细--消费接口=open_id=" + open_id);
        if(StringUtils.isBlank(open_id)){
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", "接口参数错误：open_id为空");
            jSONObject.put("return_data", null);
        }
        else {
            pd.put("open_id", open_id);
            int page_int = Integer.valueOf(page);
            int pageSize = 10;
            pd.put("pageSize", pageSize);
            pd.put("pageNum", pageSize * (page_int - 1));

            try {
                //消费金额
                //联盟商户退款金额
                pd.put("state","RefLM");
                pd.put("source", 3);
                List<Base90Detail> outList = queryClerkService.selectOrtherBalanceOut(pd);

                Collections.sort(outList,new ComparatorBase90Deatil());
                for (Base90Detail item : outList) {
                    String create_time = item.getCreate_time();
                    String c_time = create_time.substring(0, create_time.lastIndexOf(" "));
                    item.setCreate_time(c_time);
                }

                //消费总额
                int out_sum = queryClerkService.out_sum(pd);
                JSONObject jsonObject_sum = new JSONObject();
                jsonObject_sum.put("out_sum", out_sum);
                JSONArray jsonStrs = new JSONArray();
                for (Base90Detail Detail : outList) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("create_time", notNull(Detail.getCreate_time()));
                    jsonObject.put("source", Detail.getSource());
                    jsonObject.put("business_name", Detail.getBusiness_name());
                    jsonObject.put("source_msg", notNull(Detail.getSource_msg()));
                    jsonObject.put("point_90", Detail.getPoint_90());
                    jsonStrs.add(jsonObject);
                }
                jsonStrs.add(jsonObject_sum);
                jSONObject.put("return_code", 1);
                jSONObject.put("return_info", "");
                jSONObject.put("return_data", jsonStrs);
            } catch (Exception e) {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", e.getMessage());
                jSONObject.put("return_data", null);
                logger.error("久零劵明细--消费=报错=" ,e.fillInStackTrace());
                e.printStackTrace();
            }
        }

       writeJson(jSONObject,response);
    }

    /**
     * 获取付款码 90券
     */
    @RequestMapping(value = "/orther_getUserOnlyCode")
    public void orther_getUserOnlyCode(String open_id,HttpServletResponse response,String type)
    {

        JSONObject jSONObject = new JSONObject();
        if(StringUtils.isBlank(open_id)){
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", "接口参数错误：open_id为空");
            jSONObject.put("return_data", null);
        }
        else
        {try {
            BaseUser user = queryClerkService.getBaseUserByOpen_id(open_id);
            if(user==null){
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", "接口参数错误：无效的open_id");
                jSONObject.put("return_data", null);

            }
            else{

                String only_code = this.createPayCode(type,"100800");
                if (only_code.length()!=20)
                {
                    jSONObject.put("return_code", 0);
                    jSONObject.put("return_info", "接口参数错误：付款码生成失败，请重新获取");
                    jSONObject.put("return_data", null);
                }else{
                    Map<String,Object> pd1 = new HashMap<>();
                    pd1.put("only_code", only_code);
                    pd1.put("open_id", open_id);
                    //更新 baseUser 付款码
                    queryClerkService.updateBaseUserOnlyCode(pd1);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("only_code", only_code);  //付款码
                    jsonObject.put("balance_90", user.getBalance_90() / 100);
                    jsonObject.put("balance_shopping_90", user.getBalance_shopping_90() / 100);

                    jSONObject.put("return_code", 1);
                    jSONObject.put("return_info", "");
                    jSONObject.put("return_data", jsonObject);
                }
            }

            logger.error("获取付款吗接口返回jSONObject=" + jSONObject);

        } catch (Exception e) {
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", e.getMessage());
            jSONObject.put("return_data", null);
            logger.error("获取付款吗接口=报错=" ,e.fillInStackTrace());
            e.printStackTrace();
        }}

        writeJson(jSONObject,response);
    }

    /**
     * 获取付款码 购物券
     */
    @RequestMapping(value = "/orther_getUserShoppingOnlyCode")
    public void orther_getUserShoppingOnlyCode(String open_id,HttpServletResponse response)
    {

        JSONObject jSONObject = new JSONObject();
        if(StringUtils.isBlank(open_id)){
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", "接口参数错误：open_id为空");
            jSONObject.put("return_data", null);
        }
        else
        {try {
            BaseUser user = queryClerkService.getBaseUserByOpen_id(open_id);
            if(user==null){
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", "接口参数错误：无效的open_id");
                jSONObject.put("return_data", null);

            }
            else{

                String only_code = this.createPayCode("80","100800");
                if (only_code.length()!=20)
                {
                    jSONObject.put("return_code", 0);
                    jSONObject.put("return_info", "接口参数错误：付款码生成失败，请重新获取");
                    jSONObject.put("return_data", null);
                }else{
                    Map<String,Object> pd1 = new HashMap<>();
                    pd1.put("only_code", only_code);
                    pd1.put("open_id", open_id);
                    //更新 baseUser 付款码
                    queryClerkService.updateBaseUserOnlyCode(pd1);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("only_code", only_code);  //付款码
                    jsonObject.put("balance_90", user.getBalance_90() / 100);
                    jsonObject.put("balance_shopping_90", user.getBalance_shopping_90() / 100);
                    jSONObject.put("return_code", 1);
                    jSONObject.put("return_info", "");
                    jSONObject.put("return_data", jsonObject);
                }
            }

            logger.error("获取付款吗接口返回jSONObject=" + jSONObject);

        } catch (Exception e) {
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", e.getMessage());
            jSONObject.put("return_data", null);
            logger.error("获取付款吗接口=报错=" ,e.fillInStackTrace());
            e.printStackTrace();
        }}

        writeJson(jSONObject,response);
    }

    /**
     * 生成付款码
     * agent_code
     * @return
     */
    private String createPayCode(String codePrefix,String agent_code)
    {
        StringBuilder sb = new StringBuilder(codePrefix);
        sb.append(agent_code.substring(0,4));
        sb.append(this.getRandomCode(1));
        return sb.toString();
    }

    /**
     * 生成 随机数
     * @param RandomMax
     * @return
     */
    private String getRandomCode(int RandomMax)
    {
        Random rand = new Random();
        Date now = new Date();
        return (new StringBuilder(String.valueOf(Long.toString(now.getTime()))))
                .append(Integer.toString(rand.nextInt(RandomMax))).toString();
    }

    int pageSize=10;//翻页10行
    /**
     * 充值记录
     * @param open_id
     * @param page 1开始计数翻页
     * @return
     */
    @RequestMapping(value = "/orther_offline_card", method = RequestMethod.POST)
    public void orther_offline_card(String open_id,int page,HttpServletResponse response) {
        JSONObject jSONObject = new JSONObject();
        try {
            Map<String,Object> pd = new HashMap<>();
            pd.put("open_id", open_id);
            pd.put("source", "4");
            /***开始：翻页***/
            int pageNum=(page-1)*pageSize;
            pd.put("pageSize", pageSize);
            pd.put("pageNum", pageNum);
            /***结束：翻页***/
            //充值记录
            List<Base90Detail> list = queryClerkService.selectWhereBase90Detail(pd);
          /*  for (Base90Detail item : list) {
                String create_time = item.getCreate_time();
                String c_time = create_time.substring(0, create_time.lastIndexOf(" "));
                item.setCreate_time(c_time);
            }*/
            jSONObject.put("list", list);
        } catch (Exception e) {
            jSONObject.put("list", new ArrayList<>());
            e.printStackTrace();
        }
       writeJson(jSONObject,response);

    }

    /**
     *
     * @param open_id
     * @param page 1开始计数翻页
     * @param state 1:已消费订单 3 退款订单
     * @return
     */
    @RequestMapping(value = "/orther_orderPoint90", method = RequestMethod.POST)
    public void orther_orderPoint90(String open_id,int page,int state,HttpServletResponse response)
    {
        JSONObject jSONObject = new JSONObject();
        try {
            Map<String,Object> pd = new HashMap<>();
            pd.put("open_id", open_id);
            pd.put("state", state);
            /***开始：翻页***/
            int pageNum=(page-1)*pageSize;
            pd.put("pageSize", pageSize);
            pd.put("pageNum", pageNum);
            /***结束：翻页***/

            List<OrderMain90> orderMainUnionList;
            orderMainUnionList = queryClerkService.selectWhereOrderMain90(pd);
            for (OrderMain90 userPointRecord : orderMainUnionList) {
                String create_time=userPointRecord.getCreate_time();
                String c_time = create_time.substring(0, create_time.lastIndexOf(" "));
                userPointRecord.setCreate_time(c_time);
            }

            Map<String,Object> tmpmap= queryClerkService.selectWhereOrderMain90Count(pd);

            jSONObject.put("orderList", orderMainUnionList);
            jSONObject.put("size", tmpmap.get("listcount"));

        } catch (Exception e) {
            jSONObject.put("orderList", null);
            jSONObject.put("size", 0);
            e.printStackTrace();
        }
      writeJson(jSONObject,response);
    }

    /**
     * 个人中心数据
     */
    @RequestMapping(value = "/orther_getUserCenter")
    public void orther_getUserCenter(String open_id,HttpServletResponse response)
    {
        logger.error("获取个人中心接口=open_id=" + open_id);
        JSONObject jSONObject = new JSONObject();
        if(StringUtils.isBlank(open_id)){
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", "接口参数错误：open_id为空");
            jSONObject.put("return_data", null);

        }else
        {
            try {
                BaseUser user = queryClerkService.getBaseUserByOpen_id(open_id);
                if(user==null){
                    jSONObject.put("return_code", 0);
                    jSONObject.put("return_info", "接口参数错误：无效的open_id");
                    jSONObject.put("return_data", null);
                }else{
                    //查询消费次数
                    Map<String,Object> map=new HashMap<>();
                    map.put("open_id", open_id);
                    map.put("state", 1);
                    //体验店的消费次数
                    int pay_cnt_90 = queryClerkService.getCountOrderMain90(map);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("balance_90_total", user.getBalance_90_total());  //累计券额(元)
                    jsonObject.put("balance_90", user.getBalance_90());              //久零券余额(元)
                    jsonObject.put("pay_cnt_90", pay_cnt_90);                        //体验店服务费记录次数

                    jSONObject.put("return_code", 1);
                    jSONObject.put("return_info", "");
                    jSONObject.put("return_data", jsonObject);
                }

                logger.error("统计首页接口返回jSONObject=" + jSONObject);

            } catch (Exception e) {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", e.getMessage());
                jSONObject.put("return_data", null);
                logger.error("获取个人中心接口=报错=" ,e.fillInStackTrace());
                e.printStackTrace();
            }
        }

        writeJson(jSONObject,response);
    }

    /**
     * 增加90劵接口
     * @throws Exception
     */
    @RequestMapping(value = "/orther_operUserCoin90")
    public synchronized @ResponseBody JSONObject orther_operUserCoin90(String json) throws Exception
    {

        Map<String,Object> map=new HashMap<String, Object>();
        JSONObject jSONObject = new JSONObject();
        try {
            map= JSON.parseObject(json,Map.class);
            queryClerkService.checkUserCoin(map);
            jSONObject=queryClerkService.oper_coin_90(map);
            return jSONObject;

        } catch (Exception e) {
            jSONObject.put("flag", "1");
            jSONObject.put("msg","操作失败,参数错误");
            return jSONObject;
        }
    }

    ////////////////////////////////消劵/////////////////////////////////////////
    /**
     * 消90券接口
     * @throws Exception
     */
    @RequestMapping(value = "/orther_minus_balance_90")
    public @ResponseBody synchronized JSONObject other_minus_balance_90(String json) throws Exception
    {
        Map<String,Object> map=new HashMap<String, Object>();
        JSONObject jSONObject = new JSONObject();
        try {
            map=JSON.parseObject(json,Map.class);
            String id=get32UUID();
            if(map.get("sceneType")!=null){
                String sceneType=map.get("sceneType").toString().trim();
                if(sceneType.equals("1")) //体验店消费 减去90券
                {
                    String open_id=map.get("open_id").toString();
                    double total_90= Double.parseDouble(map.get("total_90").toString());  //分
                    String shop_code = map.get("shop_code").toString();
                    String source_code = map.get("source_code").toString();
                    String brand_code=map.get("brand_code").toString();
                    String user_code = map.get("user_code").toString();
                    jSONObject=offLinePayMinus90(open_id, total_90,shop_code,source_code,brand_code, user_code);

                }else if(sceneType.equals("2")) //体验店退款 加90券
                {
                    String open_id=map.get("open_id").toString();
                    double total_90= Double.parseDouble(map.get("total_90").toString());  //分
                    String shop_code = map.get("shop_code").toString();
                    String source_code = map.get("source_code").toString();
                    String brand_code=map.get("brand_code").toString();
                    String user_code = map.get("user_code").toString();
                    jSONObject=offLinebackAdd90(open_id, total_90,shop_code,source_code,brand_code, user_code);
                }else if(sceneType.equals("3")) //联盟商户 线下 退券
                {
                    String open_id=map.get("open_id").toString();
                    double total_90= Double.parseDouble(map.get("total_90").toString());  //分
                    String shop_code = map.get("shop_code").toString();
                    String source_code = map.get("source_code").toString();
                    String brand_code=map.get("brand_code").toString();
                    String user_code = map.get("user_code").toString();
                    String pay_type = map.get("pay_type").toString();//1:支付宝+微信，2:银联
                    jSONObject=backAdd90(open_id, total_90,shop_code,source_code,brand_code, user_code,pay_type);
                }else if(sceneType.equals("4")) //线上购买 的商品 退款退券
                {
                    jSONObject=online_minusBalance(map);
                }
            }else
            {
                jSONObject.put("return_code", "0");
                jSONObject.put("return_info","无效的调用场景");
            }

        } catch (Exception e) {
            jSONObject.put("return_code", "0");
            jSONObject.put("return_info","操作失败,参数错误");
            return jSONObject;
        }
        return jSONObject;
    }

    //体验店消费 减去90券
    private JSONObject offLinePayMinus90(String open_id,double total_90,String shop_code,String source_code,String brand_code,String user_code)
    {
        JSONObject jSONObject = new JSONObject();

        try {
            /******************减去用户90券**************************/
            minus_balance_90(open_id,total_90);
            /******************消费发券记录**************************/
            logger.error("=线下支付消费发券记录=open_id=" + open_id);
            Base90Detail base90Detail = new Base90Detail();
            base90Detail.setBrand_code(brand_code);
            base90Detail.setShop_code(shop_code);
            base90Detail.setSource_code(source_code);
            base90Detail.setOpen_id(open_id);
            base90Detail.setSource(3);
            base90Detail.setSource_msg("体验店消费");
            base90Detail.setPoint_90((int)total_90);
            base90Detail.setUser_code(user_code);
            queryClerkService.insertBase90Detail(base90Detail);
            jSONObject.put("return_code", "1");
            jSONObject.put("return_info", "扣劵成功!");
            return jSONObject;
        } catch (Exception e) {
            jSONObject.put("return_code", "0");
            jSONObject.put("return_info","调用出错");
            return jSONObject;
        }
    }

    /**
     * 减去用户90券
     */
    protected void minus_balance_90(String open_id, double balance_90){
        logger.error("=减去用户90券=open_id=" + open_id + "=balance_90="
                + balance_90);
        try {
            Map<String,Object> map=new HashMap<>();
            map.put("open_id", open_id);
            map.put("balance_90", balance_90);
            queryClerkService.minus_balance_90(map);
        } catch (Exception e) {
            logger.error("=增加用户90券=报错=" + e, e.fillInStackTrace());
        }
    }


    //体验店退款 加90券
    private JSONObject offLinebackAdd90(String open_id,double total_90,String shop_code,String source_code,String brand_code,String user_code)
    {
        JSONObject jSONObject = new JSONObject();

        try {
            /******************增加用户90券**************************/
            add_balance_90(open_id, total_90);
            /******************退款发券记录**************************/
            logger.error("体验店退款--"+total_90*-1);
            Base90Detail base90Detail = new Base90Detail();
            base90Detail.setBrand_code(brand_code);
            base90Detail.setShop_code(shop_code);
            base90Detail.setSource_code(source_code);
            base90Detail.setOpen_id(open_id);
            base90Detail.setSource(5);
            base90Detail.setSource_msg("用户退款-体验店退款");
            int point_90 = (int) (total_90*-1);
            base90Detail.setPoint_90(point_90);
            base90Detail.setUser_code(user_code);
            queryClerkService.insertBase90Detail(base90Detail);
            jSONObject.put("return_code", "1");
            jSONObject.put("return_info", "退劵成功!");
            return jSONObject;
        } catch (Exception e) {
            jSONObject.put("return_code", "0");
            jSONObject.put("return_info","调用出错");
            return jSONObject;
        }
    }

    //联盟商户 线下 退券
    private JSONObject backAdd90(String open_id,double total_90,String shop_code,String source_code,String brand_code,String user_code,String pay_type)
    {
        JSONObject jSONObject = new JSONObject();

        try {
            //退款同时，用户90券余额减去订单赠送90券数额
            minus_balance_90(open_id, total_90);
            //退款同时，联盟商户90券余额增加订单赠送90券余额
           /* pd.put("balance_90", total_90);
            pd.put("brand_code", brand_code);
            brandService.add_balance_90(pd);*/

            /******************发券记录**************************/
            String msg="";
            if(pay_type.trim().equals("1"))
            {
                msg="用户退款-微信刷卡付";
            }else if(pay_type.trim().equals("2"))
            {
                msg="用户退款-银联";
            }

            Base90Detail base90Detail = new Base90Detail();
            base90Detail.setBrand_code(brand_code);
            base90Detail.setShop_code(shop_code);
            base90Detail.setSource_code(source_code);
            base90Detail.setOpen_id(open_id);
            base90Detail.setSource(5);
            base90Detail.setSource_msg(msg);
            int point_90 = (int) (total_90);
            base90Detail.setPoint_90(point_90);
            base90Detail.setUser_code(user_code);
            queryClerkService.insertBase90Detail(base90Detail);

            jSONObject.put("return_code", "1");
            jSONObject.put("return_info", "退劵成功!");
            return jSONObject;
        } catch (Exception e) {
            jSONObject.put("return_code", "0");
            jSONObject.put("return_info","调用出错");
            return jSONObject;
        }
    }

    //线上购买 的商品 退款退券
    private JSONObject online_minusBalance(Map<String,Object> map)
    {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject=queryClerkService.oper_balance_90ByWX(map);
            return jSONObject;
        } catch (Exception e) {
            jSONObject.put("flag", "1");
            jSONObject.put("msg","操作失败,参数错误");
            return jSONObject;
        }
    }

    /**
     * h获取token(授权)
     *
     * @return
     */
    @RequestMapping(value = "/wx_getToken")
    public
    @ResponseBody
    void wx_getToken(HttpServletResponse response) throws Exception {
        String token=wxUtilService.getAccessToken();
        writeJson(token,response);
    }

    /**
     * h获取token(本地)
     *
     * @return
     */
    @RequestMapping(value = "/wx_getToken_bd")
    public
    @ResponseBody
    void wx_getToken_bd(HttpServletResponse response) throws Exception {
        String token=wxUtilService.getAccessToken_bd();
        writeJson(token,response);
    }

    /**
     * 获取获取JsApiTicket
     *
     * @return
     */
    @RequestMapping(value = "/wx_getJsApiTicket")
    public
    @ResponseBody
    void wx_getJsApiTicket(HttpServletResponse response) throws Exception {
        String jsApiTicket=wxUtilService.getJsApiTicket();
        writeJson(jsApiTicket,response);
    }
}