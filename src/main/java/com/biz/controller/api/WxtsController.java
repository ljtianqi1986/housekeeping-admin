package com.biz.controller.api;

import com.alibaba.fastjson.JSON;
import com.biz.model.Hmodel.TBizPerson;
import com.biz.model.Hmodel.api.TpaySceneDetail;
import com.biz.model.Pmodel.api.*;
import com.biz.service.api.*;
import com.biz.service.basic.BizpersonServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * qt接口
 *
 * @author lzq
 */
@Controller
@RequestMapping("/api/wxts")
public class WxtsController extends BaseController {
    @Resource(name = "wxtsService")
    private WxtsServiceI wxtsService;

    @Resource(name = "wxActivityService")
    private WxActivityServiceI wxActivityService;

    @Autowired
    private BizpersonServiceI bizpersonService;
    @Resource(name = "apiInterfaceService")
    private ApiInterfaceServiceI apiInterfaceService;
    @Resource(name = "wxTemplateService")
    private WxTemplateServiceI wxTemplateService;

    @Autowired
    private PhoneUserServiceI phoneUserServiceI;

    /**
     * 扫描带参数二维码事件,用户已关注时的事件推送
     */
    @RequestMapping(value = "/wxscan")
    private synchronized void  wxscan(HttpServletResponse response,
                        @RequestParam String open_id,
                        @RequestParam String eventKey,
                        @RequestParam String appid) {
        try {
            String msg="";
            msg=wxActivityService.doWxscan(open_id,eventKey,appid);
        } catch (Exception e) {
            logger.error("=扫描带参数二维码=报错=", e.fillInStackTrace());
            writeJson(false, response);
        }
    }

    private void doSendForJlBank(PayScene payScene, String open_id) {
        TpaySceneDetail detail=wxtsService.getDetail(payScene.getId());//获取发券的参数
        Map<String,Object> map = new HashMap<>();
        //发券
        JSONObject jSONObject_q = new JSONObject();
        jSONObject_q.put("brand_code",detail.getBrandId());
        jSONObject_q.put("shop_code", detail.getShopId());
        jSONObject_q.put("user_code", detail.getUserId());
        jSONObject_q.put("order_code", payScene.getMain_code());
        jSONObject_q.put("open_id", open_id);
        jSONObject_q.put("type", "0");
        jSONObject_q.put("source", "1");
        jSONObject_q.put("source_msg", "pos机自动发券");
        jSONObject_q.put("balance_90", detail.getPoint90());
        jSONObject_q.put("state", 2);
        jSONObject_q.put("commission", detail.getCommission());
        jSONObject_q.put("tradeType", "JLBANK");
        jSONObject_q.put("orderState", "1");
        jSONObject_q.put("orderTotal", detail.getOrderTotal());
        int f_q = user_Balance90(jSONObject_q);
        wxTemplateService.send_kf_template(open_id, "成功领取"+MathUtil.mul(new Double(detail.getPoint90()),0.01)+"久零券");

    }


    /**
     * 绑定会员，订单处理
     */
    private boolean to_be_vip(PayScene payScene, String open_id,BaseUser user) {
        boolean pd_bool=false;
        try {
            int type = payScene.getType(); //0:未知 1：微信 2：支付宝 3：易支付 4:银联刷卡
            String pay_user_id = payScene.getPay_user_id();
            /*if (StringUtils.isBlank(pay_user_id)) {
                return;
            }*/
            if (type == 1) { //微信
                //已绑定
                if(!StringUtils.isBlank(pay_user_id)) {
                    if (StringUtils.isNotBlank(user.getXy_openid())) {
                        return false;
                    }
                }
                //查询该xy_openid是否绑定
                if(!StringUtils.isBlank(pay_user_id)) {
                    Map<String, Object> pd = new HashMap<>();
                    pd.put("xy_openid", pay_user_id);
                    List<BaseUser> list = wxtsService.getBaseUserByxy_openid(pd);
                    if (list != null && list.size() > 0) {
                        System.out.println("兴业openid没有");
                        return false;
                    }
                }
                //绑定xy_openid
                if(!StringUtils.isBlank(pay_user_id)) {
                    Map<String, Object> pds = new HashMap<String, Object>();
                    pds.put("open_id", open_id);
                    pds.put("xy_openid", pay_user_id);
                    if (wxtsService.updateWhereBaseUser(pds) == 0) {
                        wxTemplateService.send_kf_template(open_id, "xy_openid绑定失败！");
                        return false;
                    }
                }
                //处理订单
                pd_bool=handle_order(payScene, open_id,user.getId());
            }else if (type == 2) { //支付宝
                //已绑定
                if(!StringUtils.isBlank(pay_user_id)) {
                    if (StringUtils.isNotBlank(user.getScan_ali_id())) {
                        return false;
                    }
                }
                //查询该支付宝是否绑定
                if(!StringUtils.isBlank(pay_user_id)) {
                    Map<String, Object> pd = new HashMap();
                    pd.put("scan_ali_id", pay_user_id);
                    List<BaseUser> list = wxtsService.selectWhereBaseUser(pd);
                    if (list != null && list.size() > 0) {
                        return false;
                    }
                }
                //绑定支付宝
                if(!StringUtils.isBlank(pay_user_id)) {
                    Map<String, Object> pds = new HashMap<String, Object>();
                    pds.put("open_id", open_id);
                    pds.put("scan_ali_id", pay_user_id);
                    if (wxtsService.updateWhereBaseUser(pds) == 0) {
                        wxTemplateService.send_kf_template(open_id, "xy_openid绑定失败！");
                        return false;
                    }
                }
                //处理订单
                pd_bool=handle_order(payScene, open_id,user.getId());
            }else if (type == 3) { //易支付
                //已绑定
                if (StringUtils.isNotBlank(user.getScan_yi_id())) {
                    return false;
                }
                //查询该易支付是否绑定
                Map<String, Object> pd = new HashMap<>();
                pd.put("scan_yi_id", pay_user_id);
                List<BaseUser> list = wxtsService.selectWhereBaseUser(pd);
                if (list != null && list.size() > 0) {
                    return false;
                }
                //绑定易支付
                Map<String, Object> pds = new HashMap<String, Object>();
                pds.put("open_id", open_id);
                pds.put("scan_yi_id", pay_user_id);
                if (wxtsService.updateWhereBaseUser(pds) == 0) {
                    wxTemplateService.send_kf_template(open_id, "xy_openid绑定失败！");
                    return false;
                }
                //处理订单
                pd_bool=handle_order(payScene, open_id,user.getId());
            }else if (type == 4) { //银联刷卡

                //查询银联刷卡是否绑定
                BaseUser  user_objcet=phoneUserServiceI.getOneBaseUserByUnionpay(pay_user_id);
                if(user_objcet!=null){
                    return false;
                }
                //绑定银联刷卡
                Map<String, Object> pds = new HashMap<String, Object>();
                pds.put("id", UuidUtil.get32UUID());
                pds.put("userid", user.getId());
                pds.put("unionpayid", pay_user_id);
                if (!wxtsService.addWhereBaseUserByunionpay(pds)) {
                    wxTemplateService.send_kf_template(open_id, "xy_openid绑定失败！");
                    return false;
                }

                //处理订单
                pd_bool=handle_order(payScene, open_id,user.getId());
            }else if(type==8 ||  type==9 ||  type==6||  type==7){
                //处理订单
                pd_bool=handle_order(payScene, open_id,user.getId());
            }

            return pd_bool;
        } catch (Exception e) {
            logger.error("=绑定会员=报错=", e.fillInStackTrace());
            e.printStackTrace();
            return false;

        }
    }


    /**
     * 发券
     *
     * @return 0成功 1失败 3重
     */
    private int user_Balance90(JSONObject jSONObject1) {
        int zt=1;
        Map<String, String> hash1 = new HashMap<String, String>();
        hash1.put("json", jSONObject1.toString());
        Map<String, Object> r_Interface = APIInterface(hash1, "api/balance/operUserBalance90.ac");
        if (r_Interface != null) {
            return zt=Integer.parseInt(r_Interface.get("flag").toString());
        } else {
            return zt;
        }
    }

    //发久零币 0成功 1失败 3重
    private int user_coin(BaseUser y_user, RgGift rgGift, PayScene payScene) {
        int zt=1;
        int gift_type = rgGift.getGift_type();
        //if (gift_type != 0 && rgGift.getPoint_90() > 0) {
        if (rgGift.getPoint_90() > 0 || rgGift.getCoin_90()>0) {
            double balance_90 = rgGift.getPoint_90();
            double bei_90 = 0;
            if (gift_type == 1)  //2% (qt)
            {
                bei_90 = balance_90 * 0.02;
            } else if (gift_type == 2)  //5% (qt)
            {
                bei_90 = balance_90 * 0.05;
            } else if(gift_type==0){//(Pos机)
                bei_90=MathUtil.mul(rgGift.getCoin_90(),100.0);//元转换分
            }

            if(bei_90==0){
                return zt;
            }
            bei_90 = MathUtil.mul(bei_90, 0.01);  //四舍五入，保留两位小数
            JSONObject jSONObject90 = new JSONObject();
            jSONObject90.put("userId", y_user.getId());
            jSONObject90.put("state", "1");
            jSONObject90.put("orderNum", payScene.getMain_code());
            jSONObject90.put("source", "3");
            jSONObject90.put("amount", bei_90);

            Map<String, String> map90 = new HashMap<>();
            map90.put("json", jSONObject90.toString());
            Map<String, Object> r_Interface = APIInterface(map90, "api/balance/operUserCoin90.ac");
            if (r_Interface != null) {
                return zt=Integer.parseInt(r_Interface.get("flag").toString());
            } else {
                return zt;
            }
        } else {
            return zt;
        }

    }


    //发久零币 0成功 1失败 3重
    private int user_coin2(String userId,String orderNum,Double bei_90) {
        int zt = 1;
        try {
            if (bei_90 > 0) {
                JSONObject jSONObject90 = new JSONObject();
                jSONObject90.put("userId", userId);
                jSONObject90.put("state", "1");
                jSONObject90.put("orderNum", orderNum);
                jSONObject90.put("source", "3");
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


    /**
     * 订单处理
     */
    private boolean handle_order(PayScene payScene, String open_id,String Userid) {
        try {
            //获取交易成功的订单
            Map<String, Object> pd = new HashMap<String, Object>();
            String ticketType=payScene.getTicketType();
            if(StringUtil.isNullOrEmpty(ticketType)||!StringUtil.isNumeric(ticketType))
            {ticketType="0";}
            OrderMainUnion orderMain = wxtsService.getOrderMainUnionByCode(payScene.getMain_code());
            if (orderMain == null) {
                return false;
            }
            if (orderMain.getState() != 1) {
                return false;
            }
            //更新订单表，完善open_id和90券
            //Brand brand = wxtsService.getBrandOnlyByCode(orderMain.getBrand_code());
            //double jl_total = 0;
            //jl_total = (brand.getProportion() == 0 ? 1 : brand.getProportion()) * orderMain.getCash_total();
            pd.clear();
            pd.put("code", payScene.getMain_code());
            pd.put("open_id", open_id);
            //pd.put("gift_90", jl_total);
            wxtsService.updateWhereOrderMainUnion(pd);

            JSONObject jSONObject = new JSONObject();
            //发券
            JSONObject jSONObject_q = new JSONObject();
            jSONObject_q.put("brand_code", orderMain.getBrand_code());
            jSONObject_q.put("shop_code", orderMain.getShop_code());
            jSONObject_q.put("user_code", orderMain.getUser_code());
            jSONObject_q.put("order_code", payScene.getMain_code());
            jSONObject_q.put("open_id", open_id);
            jSONObject_q.put("type", "0");
            jSONObject_q.put("source", "1");
            jSONObject_q.put("source_msg", "pos机自动发券");
            jSONObject_q.put("ticketType", ticketType);
            jSONObject_q.put("balance_90", orderMain.getGift_90());
            jSONObject_q.put("state", 2);
            jSONObject_q.put("commission", orderMain.getCommission());
            jSONObject_q.put("tradeType", orderMain.getTrade_type());
            jSONObject_q.put("orderState", "1");
            jSONObject_q.put("orderTotal", orderMain.getOrder_total());
            jSONObject_q.put("ticketType", orderMain.getBalance_type());

            int f_q = user_Balance90(jSONObject_q);
            //发贝
            int f_b=user_coin2(Userid,payScene.getMain_code(),orderMain.getCoin_90());

            if((f_q==0 || f_q==3)&&(f_b==0 || f_b==3))
            {
                return true;
            }else{
                return false;
            }

        } catch (Exception e) {
            //logger.error("=订单处理=报错=", e.fillInStackTrace());
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 推送模板信息
     */
    private void send_point_template(PayScene payScene, String open_id) throws Exception {
        try {
            OrderMainUnion orderMain = wxtsService.getOrderMainUnionByCode(payScene.getMain_code());
            double jl_money= MathUtil.mul(orderMain.getGift_90(),0.01);
            double money=MathUtil.mul(orderMain.getCash_total(), 0.01);
//            wxTemplateService.send_pay_template(payScene.getId(), open_id,jl_money,money,orderMain.getPay_time());

            wxTemplateService.send_kf_template(open_id, "支付成功，支付金额："+money+"元，赠送久零券："+jl_money);
        } catch (Exception e) {
            e.printStackTrace();
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
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            return map2;
        }
    }


}