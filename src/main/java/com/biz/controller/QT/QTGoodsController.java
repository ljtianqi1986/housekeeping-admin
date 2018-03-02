package com.biz.controller.QT;

import com.alibaba.fastjson.JSON;
import com.biz.model.Pmodel.QT.PqtUser;
import com.biz.service.QT.QtUserServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.ConfigUtil;
import com.framework.utils.IpUtil;
import com.framework.utils.StringUtil;
import com.framework.utils.URLConectionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lzq on 2017/2/17.
 */
@Controller
@RequestMapping("/qtGoods")
public class QTGoodsController extends BaseController {

    private String phone_validate_user_rul="/api/QToayWx/VerificationTicket.ac";//QT首页 支付付款码验证(查询所需支付服务费)
    private String Generate_Order_rul="/api/QToayWx/Generate_Order.ac";//直接付款生产订单
    private String getPhone_validate_user="/api/QTUser/phoneDoSendSMS.ac";//手机号作为支付码
    private String proGift_rul="/api/QTTicket/phone_gift_rg_tyd.ac";//发券生成二维码
    private String saleSum_rul="/api/QTUser/qtGetSumInfoByUserCode.ac";//销售统计


    @Resource(name = "qtUserService")
    private QtUserServiceI qtUserService ;

    @RequestMapping("toGoodsList")
    public ModelAndView toGoodsList(ModelAndView mv){
        PqtUser userMap=(PqtUser) getShiroAttribute("QTUser");
        String sid =userMap.getIdentity_code();
        //获取导购员列表
        Map<String,Object> map1 = qtUserService.getQtDaoGouYuanByShopCode(sid);

        mv.addObject("mapUser", map1);
        mv.setViewName("QT/goods/goods_detail");
        return mv;
    }


    /***
     * 根据商品code 获取商品信息
     * @param code
     * @param response
     */
    @RequestMapping("getGoodsDetailByCode")
    public void getGoodsDetailByCode(String code , HttpServletResponse response)throws Exception{
        PqtUser pqtUser=(PqtUser) getShiroAttribute("QTUser");
        String shopId=pqtUser.getIdentity_code();
        String whareid =qtUserService.getWhareid(shopId);
        String type = "2";//查询商品信息
        String count="1";
        String x = qtUserService.getGoodsDetailByCodes(whareid,code,type,count);
        writeJson(x,response);
    }


    /***
     * 生成订单
     * @param
     * @param response
     */
    @RequestMapping("toProductOrder")
    public void toProductOrder(String json, HttpServletResponse response)throws Exception{
        Map<String,Object> map1 = new HashMap<>();
        json = URLDecoder.decode(json, "utf-8");
        String only_code = "90100814826455209510";

        map1 = qtUserService.doProductOrder(json,only_code);


        writeJson(map1,response);
    }



    /**
     * QT首页 支付付款码验证(查询所需支付服务费),并生订单
     * @param response
     * @param only_code 付款码值
     * @param total 付款金额
     * @param iscoin 是否使用9贝抵扣(0,true：使用 1:false：不使用)
     */
    @RequestMapping(value = "/toValidateUser")
    public void toValidateUser(HttpServletResponse response, HttpServletRequest request, String only_code, String total, String iscoin ,String json) {
        Map<String, Object> r_json = new HashMap<String, Object>();

        boolean pd=false;//接口是否调用成功 true 成功，false 失败
        only_code=only_code.trim();
        total=total.trim();
        PqtUser qtUser = (PqtUser) getShiroAttribute("QTUser");

        //返回参数
        String return_code="0";//返回码1 ：验证成功 0：验证失败
        double return_info=0.0;//服务费金额
        boolean ninezero=false;//90贝余额是否足够支付服务费 :true 足够,false :不够
        double needMoney=0.0;//还需支付多少钱
        boolean iscoinbool=StringtoBoolean(iscoin);//true: 抵用， false：不抵用
        String msg="";
        String orderCode="";//订单号
        //返回类型 0：手机号支付 1:付款码支付
        String return_type="0";
        String openid="";
        String phone_code="";
        try{
            json = URLDecoder.decode(json, "utf-8");
            //参数验证
            if(StringUtil.isNullOrEmpty(only_code) || StringUtil.isNullOrEmpty(total) ){
                msg="金额或付款码不能为空";
            }else if(!isnumber(total)){
                msg="金额只能为数字";
            }else if(!("0".equals(iscoin) || "1".equals(iscoin))){
                msg="是否使用90贝参数错误";
            }else {
                if(only_code.length()==11){
                    return_type="0";
                    Map<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("shop_code", qtUser.getIdentity_code());//门店code
                    hashMap.put("phone", only_code);//手机号
                    hashMap.put("total", total);//付款金额
                    hashMap.put("iscoin",iscoinbool+"");//是否使用9贝抵扣(0,true：使用 1:false：不使用)
                    //验证接口
                    Map<String, Object> r_map= this.QTInterface(hashMap, getPhone_validate_user);
                    if(!(r_map==null || (r_map.size()==0))){
                        //成功
                        return_code=r_map.get("return_code").toString();//返回码1 ：成功 0：失败
                        return_info=Double.parseDouble(r_map.get("return_info").toString()) ;//服务费金额
                        ninezero=Boolean.parseBoolean(r_map.get("ninezero").toString());//90贝余额是否足够支付服务费 :true 足够,false :不够
                        needMoney=Double.parseDouble(r_map.get("needMoney").toString());//还需支付多少钱
                        iscoinbool= Boolean.parseBoolean(r_map.get("iscoinbool").toString());//true: 抵用， false：不抵用
                        msg=r_map.get("msg").toString();
                        openid=r_map.get("openid").toString();
                        phone_code=r_map.get("phone_code").toString();
                        pd=true;
                    }
                }else{
                    return_type="1";

                    //验证接口参数
                    Map<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("shop_code", qtUser.getIdentity_code());//门店code
                    hashMap.put("only_code", only_code);//付款码值
                    hashMap.put("total", total);//付款金额
                    hashMap.put("iscoin",iscoinbool+"");//是否使用9贝抵扣(0,true：使用 1:false：不使用)
                    //验证接口
                    Map<String, Object> r_map= this.QTInterface(hashMap, phone_validate_user_rul);
                    if(!(r_map==null || (r_map.size()==0))){
                        //成功
                        return_code=r_map.get("return_code").toString();//返回码1 ：成功 0：失败
                        return_info=Double.parseDouble(r_map.get("return_info").toString()) ;//服务费金额
                        ninezero=Boolean.parseBoolean(r_map.get("ninezero").toString());//90贝余额是否足够支付服务费 :true 足够,false :不够
                        needMoney=Double.parseDouble(r_map.get("ninecoinMoney").toString());//还需支付多少钱
                        iscoinbool= Boolean.parseBoolean(r_map.get("iscoinbool").toString());//true: 抵用， false：不抵用
                        msg=r_map.get("msg").toString();
                        pd=true;
                    }
                }
            }
        }catch (Exception e){
            msg=e.toString();
        }
        finally {
            r_json.put("error_code", pd);
            r_json.put("return_code",return_code);//返回码1 ：验证成功 0：验证失败
            r_json.put("return_type",return_type);//返回类型 1：付款码支付 0：手机号支付
            r_json.put("return_info", return_info);//服务费金额
            r_json.put("ninezero", ninezero);//90贝余额是否足够支付服务费 :true 足够,false :不够
            r_json.put("needMoney", needMoney);//还需支付多少钱
            r_json.put("iscoinbool", iscoinbool);//true: 抵用， false：不抵用
            r_json.put("orderCode", orderCode);//订单号
            r_json.put("msg", msg);
            r_json.put("phone_code",phone_code);
            r_json.put("openid",openid);
            writeJson(r_json, response);
        }
    }



    //接口
    public Map<String, Object> QTInterface(Map<String,String> jSONObject,String url){
        Map<String, Object> map2=new HashMap<String, Object>();
        try {
            String requestUrl = ConfigUtil.get("QT_Url")+url;
            String x= URLConectionUtil.httpURLConnectionPostDiy(requestUrl,jSONObject);
            x = URLDecoder.decode(x, "utf-8");
            if(x==null||x.trim().equals("")){
                // 失败
            }else {
                map2 = JSON.parseObject(x, Map.class);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        finally {
            return map2;
        }
    }



    /**
     * string 转换 boolen(0:true，1：false)
     * @param isboolen
     * @return
     */
    private boolean StringtoBoolean(String isboolen){
        boolean pd=false;
        if("0".equals(isboolen)){
            pd=true;
        }
        return pd;
    }


    //java判断数字类型（小数和整数）
    private boolean isnumber(String total){
        boolean pd=false;
        try {
            pd= total.matches("\\d+(\\.\\d+)?");
        }   catch (Exception e){

        }
        return pd;
    }

    //java判断数字类型（整数）
    private boolean isinteger(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }



    /***
     * 生成订单
     * @param
     * @param response
     */
    @RequestMapping("toDoCashPay")
    public void toDoCashPay(String isCoinPay, String orderCode,String userPayCode, HttpServletResponse response)throws Exception{

        PqtUser pqtUser=(PqtUser) getShiroAttribute("QTUser");
        String shopId=pqtUser.getIdentity_code();

        Map<String,Object> map1 = qtUserService.toDoCashPay(isCoinPay,orderCode, userPayCode, shopId,"", "");

        writeJson(map1,response);
    }


    /***
     * 商品收银生成订单
     * @param
     * @param response
     */
    @RequestMapping("toDoGoodPay")
    public void toDoGoodPay(String isCoinPay, String str,String userPayCode,String type,String onlyCode, HttpServletResponse response)throws Exception{

        PqtUser pqtUser=(PqtUser) getShiroAttribute("QTUser");
        String shopId=pqtUser.getIdentity_code();
        String cashUserCode=pqtUser.getUser_code();
        HttpServletRequest hp=this.getRequest();
        String ip= IpUtil.getIpAddr(hp);
        str = URLDecoder.decode(str, "utf-8");
        Map<String,Object> mapJson = JSON.parseObject(str,Map.class);
        mapJson.put("cashUserCode",cashUserCode);
        mapJson.put("shopId",shopId);
        str=JSON.toJSONString(mapJson);
        Map<String,Object> map1 = qtUserService.toDoGoodPay(isCoinPay,str, userPayCode, shopId,type,onlyCode,ip);

        writeJson(map1,response);
    }


    /***
     * 商品收银再次验证
     * @param
     * @param response
     */
    @RequestMapping("checkPay")
    public void checkPay(String orderCode,String onlyCode, HttpServletResponse response)throws Exception{

        Map<String,Object> map1 = qtUserService.checkPhoneCode(orderCode,onlyCode);

        writeJson(map1,response);
    }


    /***
     * 直接收银生成订单
     * @param
     * @param response
     */
    @RequestMapping("toDoNewCashPay")
    public void toDoNewCashPay(String isCoinPay, String str,String userPayCode,String type,String onlyCode, HttpServletResponse response)throws Exception{

        PqtUser pqtUser=(PqtUser) getShiroAttribute("QTUser");
        String shopId=pqtUser.getIdentity_code();
        HttpServletRequest hp=this.getRequest();
        String cashUserCode=pqtUser.getUser_code();
        String ip= IpUtil.getIpAddr(hp);
        str = URLDecoder.decode(str, "utf-8");
        Map<String,Object> mapJson = JSON.parseObject(str,Map.class);
        mapJson.put("user_code",cashUserCode);
        mapJson.put("shop_code",shopId);
        str=JSON.toJSONString(mapJson);
        Map<String,Object> map1 = qtUserService.toDoGoodPay(isCoinPay,str, userPayCode, shopId,type,onlyCode,ip);

        writeJson(map1,response);
    }

    /***
     * 直接付款收银再次验证
     * @param
     * @param response
     */
    @RequestMapping("checkPayAgain")
    public void checkPayAgain(String orderCode,String onlyCode,String needMoney, HttpServletResponse response)throws Exception{
        HttpServletRequest hp=this.getRequest();
        String ip= IpUtil.getIpAddr(hp);
        Map<String,Object> map1 = qtUserService.checkPhoneCodeAgain(orderCode,onlyCode,ip,needMoney);

        writeJson(map1,response);
    }

    /***
     * 发券生成二维码
     * @param
     * @param response
     */
    @RequestMapping("proGift")
    public void proGift( HttpServletResponse response,String gift_total,String gift_type,String gift_card,String gift_memo)throws Exception{
        PqtUser pqtUser=(PqtUser) getShiroAttribute("QTUser");
        String shop_code=pqtUser.getIdentity_code();
        String user_code=pqtUser.getUser_code();
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("shop_code", shop_code);//门店code
        hashMap.put("user_code", user_code);//
        hashMap.put("total", gift_total);//付款金额
        hashMap.put("gift_type",gift_type);//
        hashMap.put("card_code",gift_card);//
        hashMap.put("memo",gift_memo);//
        //验证接口
        Map<String, Object> r_map= this.QTInterface(hashMap, proGift_rul);
        writeJson(r_map,response);
    }

    /***
     * 销售统计
     * @param
     * @param response
     */
    @RequestMapping("saleSum")
    public void saleSum(String id, HttpServletResponse response)throws Exception{
        PqtUser pqtUser=(PqtUser) getShiroAttribute("QTUser");
        String shop_code=pqtUser.getIdentity_code();
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("shopId", shop_code);//门店code
        hashMap.put("userCode", id);//
        //验证接口
        Map<String, Object> r_map= this.QTInterface(hashMap, saleSum_rul);
        writeJson(r_map,response);
    }
}
