package com.biz.controller.QT;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.biz.model.Pmodel.QT.PqtUser;
import com.biz.service.basic.UserServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.ConfigUtil;
import com.framework.utils.MD5;
import com.framework.utils.StringUtil;
import com.framework.utils.URLConectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
//import java.util.StringJoiner;


/**
 * Created by tomchen on 17/2/4.
 */
@Controller
@RequestMapping("/QTMain")
public class QTMainController extends BaseController {

    private String phone_validate_user_rul="/api/QToayWx/VerificationTicket.ac";//QT首页 支付付款码验证(查询所需支付服务费)
    private String Generate_Order_rul="/api/QToayWx/Generate_Order.ac";//直接付款生产订单
    private String phone_wx_scan_qt_rul="/api/QToayWx/payOrderMain.ac";//QT首页 微信 支付宝 90币 支付
    private String editPwd_rul="/api/QTUser/phoneResetPwd.ac";//QT修改密码接口
    final static String QUERY_QUAN_URL = "/api/QTrecord/phone_90Record.ac";
//    final static String QUERY_MONEY_URL = "/api/QTUser/phone_queryRecordsByParam.ac";
    final static String QUERY_TICKET_URL = "/api/QTTicket/queryTicketInfoByOrderId.ac";
    //final static String QUERY_MONEY_URL = "/api/QTUser/phone_queryRecordsByParam.ac";
    final static String QUERY_MONEY_URL = "/api/QTUser/phone_queryRecordsByParam.ac";

    @Autowired
    private UserServiceI userService;

    /**
    *登出
     */
    @RequestMapping("toLogOut")
    public ModelAndView toLogOut(ModelAndView mv){
        mv.clear();
        setShiroAttribute("version", null);
        setShiroAttribute("QTUser", null);
        setShiroAttribute("Type", "QT");
        mv.setViewName("redirect:/QT/loginQT/toLoginQT.ac");
        return mv;
    }


    @RequestMapping("toMain")
    public ModelAndView toCardType(ModelAndView mv){
        mv.setViewName("QT/main/main");
        return mv;
    }


    @RequestMapping("toQuan")
    public ModelAndView to90Juan(ModelAndView mv){
        //URLConectionUtil.httpURLConectionGET();
        mv.setViewName("QT/main/90quan");
        return mv;
    }

    @RequestMapping("queryQuan")
    public void queryQuan(HttpServletResponse response, int page) {

        Map<String, Object> reslutMap = new HashMap<>();
         page = page/10+1;
        try{
            if (page <= 0) {
                page = 1;
            }
            PqtUser pqtUser = (PqtUser) getShiroAttribute("QTUser");
            Map<String, String> params = new HashMap<>();
            params.put("code", pqtUser.getIdentity_code());
            params.put("page", page + "");

            Map<String, Object> map = callQTInterface(params, QUERY_QUAN_URL);

            reslutMap.put("total",map.get("total"));
            reslutMap.put("rows",map.get("return_data"));
        }catch (Exception e){
            reslutMap.put("rows", null);
            reslutMap.put("total", 0);
        }


        writeJson(reslutMap, response);
    }

    @RequestMapping("toReceipt")
    public ModelAndView toReceipt(ModelAndView mv){
        //URLConectionUtil.httpURLConectionGET();
        mv.setViewName("QT/main/receipt");
        return mv;
    }

    @RequestMapping("toMoney")
    public ModelAndView toMoney(ModelAndView mv){
        mv.setViewName("QT/main/money");
        return mv;
    }

    @RequestMapping("queryMoney")
    public void toMoney(HttpServletResponse response, HttpServletRequest request){
        Map<String, Object> reslutMap = new HashMap<>();
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("state", request.getParameter("state"));
            params.put("dateStart", StringUtil.convertNullToEmpty(request.getParameter("dateStart")));
            params.put("dateEnd", StringUtil.convertNullToEmpty(request.getParameter("dateEnd")));
            params.put("telephone", StringUtil.convertNullToEmpty(request.getParameter("telephone")));
            params.put("orderCode", StringUtil.convertNullToEmpty(request.getParameter("orderCode")));

            int limit = Integer.parseInt(request.getParameter("limit"));
            int page = Integer.parseInt(request.getParameter("page"))/limit+1;
            if (page <= 0) {
                page = 1;
            }
            params.put("page", page + "");
            //params.put("trade_type", "0");

            PqtUser pqtUser = (PqtUser) getShiroAttribute("QTUser");
            if (1 == pqtUser.getType()) {//0：其他 1：店长2:店员
                params.put("code", pqtUser.getIdentity_code());
                params.put("type", "1");
            } else{
                params.put("code", pqtUser.getUser_code());
                params.put("type", "0");
            }

            Map<String, Object> map = callQTInterface(params, QUERY_MONEY_URL);

            reslutMap.put("total",map.get("total"));
            reslutMap.put("rows",map.get("return_data"));

        }catch (Exception e){
            reslutMap.put("rows", null);
            reslutMap.put("total", 0);
        }

        writeJson(reslutMap, response);

    }

    /**
     * QT首页 支付付款码验证(查询所需支付服务费),并生订单
     * @param response
     * @param only_code 付款码值
     * @param total 付款金额
     * @param iscoin 是否使用9贝抵扣(0,true：使用 1:false：不使用)
     */
    @RequestMapping(value = "/toValidateUser")
    public void toValidateUser(HttpServletResponse response, HttpServletRequest request,String only_code,String total,String iscoin) {
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
        String openid="";
        double ninecoinMoney=0.0;//90贝应该支付多少金额
        String msg="";
        String orderCode="";//订单号
        try{
                //参数验证
        if(StringUtil.isNullOrEmpty(only_code) || StringUtil.isNullOrEmpty(total) ){
            msg="金额或付款码不能为空";
        }else if(!isnumber(total)){
            msg="金额只能为数字";
        }else if(!("0".equals(iscoin) || "1".equals(iscoin))){
            msg="是否使用90贝参数错误";
        }
        else {
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
               needMoney=Double.parseDouble(r_map.get("needMoney").toString());//还需支付多少钱
               iscoinbool= Boolean.parseBoolean(r_map.get("iscoinbool").toString());//true: 抵用， false：不抵用
               ninecoinMoney=Double.parseDouble(r_map.get("ninecoinMoney").toString());
               openid=r_map.get("openid").toString();
               msg=r_map.get("msg").toString();

            }

            //生成订单接口参数
            hashMap=new HashMap<>();
            hashMap.put("jsonSting", OrderMain90json(total,qtUser.getIdentity_code(),only_code,iscoinbool,qtUser.getUser_code()));
            hashMap.put("type","1");//1:立即支付订单

            //生成订单接口
            if("1".equals(return_code)){
                Map<String, Object> order_map= this.QTInterface(hashMap, Generate_Order_rul);
                if(!(order_map==null || (order_map.size()==0))){
                    //成功
                    String result=order_map.get("result").toString();//fail:生成失败,success :成功
                    if(result.equals("success")){
                        //成功
                        orderCode=order_map.get("orderCode").toString();
                        if(!StringUtil.isNullOrEmpty(orderCode)){
                            return_code="0"; //订单生产失败，验证改为失败
                        }
                    }else{
                        //失败
                        return_code="0";
                        msg=order_map.get("msg").toString();
                    }
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
            r_json.put("return_info", return_info);//服务费金额
            r_json.put("ninezero", ninezero);//90贝余额是否足够支付服务费 :true 足够,false :不够
            r_json.put("needMoney", needMoney);//还需支付多少钱
            r_json.put("iscoinbool", iscoinbool);//true: 抵用， false：不抵用
            r_json.put("orderCode", orderCode);//订单号
            r_json.put("openid", openid);
            r_json.put("ninecoinMoney", ninecoinMoney);
            r_json.put("msg", msg);
            writeJson(r_json, response);
        }
    }


    /**
     * QT首页 微信 支付宝 90币 支付
     * @param response
     */
    @RequestMapping(value = "/toHoneWxScanQT")
    public void toHoneWxScanQT(HttpServletResponse response, HttpServletRequest request,String orderCode,String openid,double paycoin,double needMoney,String someinputPay) {
        double r_useriscoin=0.0;//当前用户剩余90贝
        double r_ewpaycoin=0.0; //需要额外支付金额
        String return_code="fail";//fail:生成失败,success :成功
        String r_msg="";

        Map<String, Object> r_json = new HashMap<String, Object>();
        boolean pd=false;//true 成功，false 失败
        String author_code="";
        if("9".equals(someinputPay)){
            author_code="3";
        }else if("8".equals(someinputPay)){
            author_code="4";
        }else{
            author_code=someinputPay.trim();
        }

        JSONObject s=new JSONObject();
        s.put("orderCode",orderCode);
        s.put("openid",openid);
        s.put("order_total",paycoin);
        s.put("iscoin_total", needMoney);
        s.put("author_code",author_code);

        Map<String, String> hash1=new HashMap<String, String>();
        hash1.put("jsonSting", s.toString());
        hash1.put("type","1");//1:立即支付订单

        Map<String, Object> r_map= this.QTInterface(hash1, phone_wx_scan_qt_rul);
        if(r_map==null || (r_map.size()==0)){
            //失败
        }else if(r_map.get("return_code").toString().equals("success")){
           //成功
            pd=true;
            return_code= r_map.get("return_code").toString();
        }else if(r_map.get("return_code").toString().equals("fail")){
            return_code=r_map.get("return_code").toString();
            r_useriscoin=Double.parseDouble(r_map.get("useriscoin").toString());//当前用户剩余90贝
            r_ewpaycoin=Double.parseDouble(r_map.get("paycoin").toString());//需要额外支付金额
            r_msg= r_map.get("msg").toString();
        }
        r_json.put("return_code",return_code);
        r_json.put("useriscoin",r_useriscoin);
        r_json.put("ewpaycoin",r_ewpaycoin);
        r_json.put("msg",r_msg);
        r_json.put("error_code", pd);
        writeJson(r_json, response);
    }


    //接口
    public Map<String, Object> QTInterface(Map<String,String> jSONObject,String url){
        Map<String, Object> map2=new HashMap<String, Object>();
        try {
            String requestUrl = ConfigUtil.get("QT_Url")+url;
            String x= URLConectionUtil.httpURLConnectionPostDiy(requestUrl,jSONObject);
            x = URLDecoder.decode(x, "utf-8");
            if(x==null||x.trim().equals("") || x.trim().equals("失败")){
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

    private String OrderMain90json(String total,String shop_code,String only_code,boolean iscoin,String user_code){
        JSONObject s=new JSONObject();
        s.put("total",total);//90券金额
        s.put("shop_code",shop_code);//店铺id，门店code
        s.put("only_code",only_code);//付款码
        s.put("user_code", user_code);//营业员,登录者code
        s.put("is90coin",iscoin+"");
        return s.toString();
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

    /**
     *跳转修改密码
     * @param mv
     * @return
     */
    @RequestMapping("toQTEditPwd")
    public ModelAndView toQTEditPwd(ModelAndView mv)throws Exception{
        mv.clear();
        mv.setViewName("QT/login/editLogin");
        return mv;
    }
    /**
     *修改密码
     * @return
     */
    @RequestMapping("doEditPwd")
    public void toQTEditPwd(HttpServletResponse response,String pwdOld,String pwdNew)throws Exception{
          PqtUser user= (PqtUser) getShiroAttribute("QTUser");
        Map<String, String> map=new HashMap<String, String>();
        Map<String, Object> map2=new HashMap<String, Object>();
        try {
            String requestUrl = ConfigUtil.get("QT_Url")+editPwd_rul;
            map.put("user_code",user.getUser_code());
            map.put("pwd", MD5.md5(pwdOld));
            map.put("new_pwd",MD5.md5(pwdNew));
            String x= URLConectionUtil.httpURLConnectionPostDiy(requestUrl,map);
            x = URLDecoder.decode(x, "utf-8");
            map.clear();
            if(x==null||x.trim().equals("")){
               map.put("flag","0");//调用接口失败
                map.put("msg","连接接口错误");
            }else {
                map2 = JSON.parseObject(x, Map.class);
                map.put("flag",map2.get("return_code")+"");
                map.put("msg",map2.get("return_info")+"");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            map.put("flag","0");//调用接口失败
            map.put("msg","异常出错");
        }
writeJson(map,response);
    }
    /**
     *获取版本号
     * @return
     */
    @RequestMapping("loadVersion")
    public void loadVersion(HttpServletResponse response)throws Exception{
       String version= userService.getCurrentVersion();
        writeJson(version,response);
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


    /**
     * 根据订单ID查询订单信息
     * @param id
     * @param response
     */
    @RequestMapping("/findOrderInfoById")
    public void findOrderInfoByCode(String id,HttpServletResponse response){
        Map<String, String> param = new HashMap<>();
        param.put("id",id);
        Map<String,Object> result =callQTInterface(param,"");
        writeJson(result,response);
    }

}
