package com.biz.service.api;

import com.biz.model.Pmodel.PorderMainUnion;
import com.biz.model.Pmodel.basic.Brand;
import com.biz.model.Pmodel.weixin.AccessToken;
import com.biz.model.Pmodel.weixin.JsApiTicket;
import com.biz.model.Singleton.ZkNode;
import com.biz.service.weixin.WeiXinServiceI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.ConfigUtil;
import com.framework.utils.Tools;
import com.framework.utils.UuidUtil;
import com.framework.utils.weixin.EnumMethod;
import com.framework.utils.weixin.HttpRequestUtil;
import com.framework.utils.weixin.WechatAccessToken;
import com.framework.utils.weixin.WechatJsApiTicket;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lzq on 2017/2/9.
 */
@Service("wxUtilService")
public class WxUtilServiceImpl  implements WxUtilServiceI{

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Autowired
    private PhoneUserServiceI phoneUserServiceI;

    @Resource(name = "weiXinService")
    private WeiXinServiceI weiXinService;

    /**
     * 获取AccessToken(授权)
     *
     * @return
     */
    @Override
    public String getAccessToken() throws Exception {
        String tolen="";
        //本地token
        tolen=get_Ben_Di_token();

        //授权token
        //String appid=ConfigUtil.get("aolong_appid");
        //tolen=weiXinService.get_authorizer_access_token(appid);
        return tolen;
    }

    /**
     * 获取AccessToken(本地)
     *
     * @return
     */
    @Override
    public String getAccessToken_bd() throws Exception {
        String tolen="";
        //本地token
        tolen=get_Ben_Di_token();
        return tolen;
    }


    private String get_Ben_Di_token(){
        String newAccessToken = "";
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = (Map<String, Object>)dao.findForObject("WxUtilDao.getWxTicketById", null);
        } catch (Exception e) {

        }
        if(map == null )
        {
            newAccessToken = reloadAccessToken("", "1").getToken();
        }else if(map !=null && map.get("ACCESS_TOKEN") == null){
            newAccessToken = reloadAccessToken((String) map.get("id"), "0").getToken();
        }else if(map !=null && map.get("ACCESS_TOKEN") != null){
            String accessTokenStr = (String) map.get("ACCESS_TOKEN");
            if (Tools.isEmpty(accessTokenStr))
            {
                System.out
                        .println("################################################");
                System.out.println("载入JsApiTicket已超时，重新载入");
                System.out
                        .println("################################################");
                newAccessToken = reloadAccessToken((String) map.get("id"), "0").getToken();
            }
            else
            {
                String[] accessTokenArg = accessTokenStr.split(",");
                long nowTime = new Date().getTime();
                long putTime=0,diffTime=0;
                if(accessTokenArg[1]!=null){
                    putTime = Long.parseLong(accessTokenArg[1]);
                }
                if(accessTokenArg[2]!=null){
                    diffTime = Long.parseLong(accessTokenArg[2]);
                }
                if (Math.abs(nowTime - putTime) >= diffTime)
                {
                    System.out
                            .println("################################################");
                    System.out.println("载入token已超时，重新载入");
                    System.out
                            .println("################################################");
                    newAccessToken = reloadAccessToken((String) map.get("id"),"0").getToken();
                }
                if(!select_accessToken(accessTokenArg[0]))//是否有效
                {
                    System.out.println("载入JsApiTicket已超时，重新载入");
                    newAccessToken = reloadAccessToken((String) map.get("id"),"0").getToken();
                }
                else
                {
                    newAccessToken = accessTokenArg[0];
                }
            }
        }

        return newAccessToken;
    }

    /**
     * 重新装载AccessToken
     *
     * @return
     */
    public AccessToken reloadAccessToken(String id, String type)
    {
        AccessToken accessToken = WechatAccessToken.getAccessToken(ZkNode.getIstance().getJsonConfig().get("appid")+"",
                ZkNode.getIstance().getJsonConfig().get("appsecret")+"");
        Map<String, String> map = new HashMap<String, String>();
        if("1".equals(type)){
            map.put("id", UuidUtil.get32UUID());
        }else{
            map.put("id", id);
        }
        map.put("ACCESS_TOKEN", accessToken.getToken() + ","
                + new Date().getTime() + "," + (accessToken.getExpiresIn() * 1000));
        try {
            if("1".equals(type))//添加
            {
                dao.save("WxUtilDao.saveAccessToken",map);
            }else{
                dao.update("WxUtilDao.updateAccessToken",map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    /**
     * 获取JsApiTicket
     *
     * @return
     */
    @Override
    public  String getJsApiTicket() throws Exception {
        String newJsApiTicket = "";
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            map = (Map<String, Object>)dao.findForObject("WxUtilDao.getWxTicketById", null);
        } catch (Exception e) {

        }

        if(map == null )
        {
            newJsApiTicket = reloadJsApiTicket("", "1").getTicket();
        }else if(map !=null && map.get("JSAPI_TICKET") == null){
            newJsApiTicket = reloadJsApiTicket((String) map.get("id"), "0").getTicket();
        }else if(map !=null && map.get("JSAPI_TICKET") != null) {
            String jsApiTicketStr = (String) map.get("JSAPI_TICKET");
            if (Tools.isEmpty(jsApiTicketStr)) {
                System.out
                        .println("################################################");
                System.out.println("载入JsApiTicket已超时，重新载入");
                System.out
                        .println("################################################");
                newJsApiTicket = reloadJsApiTicket((String) map.get("id"), "0").getTicket();
            } else {
                String[] jsApiTicketArg = jsApiTicketStr.split(",");
                long nowTime = new Date().getTime();
                long putTime = Long.parseLong(jsApiTicketArg[1]);
                long diffTime = Long.parseLong(jsApiTicketArg[2]);
                if (Math.abs(nowTime - putTime) >= diffTime) {
                    System.out
                            .println("################################################");
                    System.out.println("载入JsApiTicket已超时，重新载入");
                    System.out
                            .println("################################################");
                    newJsApiTicket = reloadJsApiTicket((String) map.get("id"), "0").getTicket();
                } else {
                    newJsApiTicket = jsApiTicketArg[0];
                }
            }
        }
        return newJsApiTicket;
    }

    /**
     * 重新装载JsApiTicket
     *
     * @return
     */
    public JsApiTicket reloadJsApiTicket(String id, String type) throws Exception {
        String accessToken = getAccessToken();
        JsApiTicket jsApiTicket = WechatJsApiTicket.getJsApiTicket(accessToken);
        Map<String, String> map = new HashMap<String, String>();
        map.put("JSAPI_TICKET", jsApiTicket.getTicket() + ","
                + new Date().getTime() + ","
                + (jsApiTicket.getExpiresIn() * 1000));
        if("1".equals(type)){
            map.put("id", UuidUtil.get32UUID());
        }else{
            map.put("id", id);
        }
        try {
            if("1".equals(type))//添加
            {
                dao.save("WxUtilDao.saveAccessToken",map);
            }else{
                dao.update("WxUtilDao.updateJsApiToken",map);
            }
        } catch (Exception e) {
        }
        return jsApiTicket;
    }


    /**
     * 验证accessToken是否有效
     * @param accessToken true:有效 false：无效
     * @return
     */
    public boolean select_accessToken(String accessToken)
    {
        try
        {
            String ip_list="";
            //空
            if(Tools.isEmpty(accessToken))
            {
                return false;
            }
            //获取微信服务器IP地址，目前用于验证access_token是否有效
            String select_access_token_url="https://api.weixin.qq.com/cgi-bin/getcallbackip?access_token=ACCESS_TOKEN";

            String requestUrl = select_access_token_url.replace("ACCESS_TOKEN", accessToken);

            JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl,
                    EnumMethod.GET.name(), null);
            if (jsonObject == null)
            {
                jsonObject = HttpRequestUtil.httpRequest(requestUrl,
                        EnumMethod.GET.name(), null);
            }
            // 如果请求成功
            if (null != jsonObject)
            {
                try
                {
                    ip_list=jsonObject.getString("ip_list").toString();

                } catch (JSONException e)
                {
                    ip_list = null;
                }
            }

            if(Tools.isEmpty(ip_list))
            {
                return false;
            }
            else
            {
                return true;
            }

        } catch (Exception e)
        {
            return false;
        }

    }

    /**
     * 推送退款信息（90体验店）
     */
    public void send_refund_template(String back_code,String code,String openId, Integer int_total) throws Exception {
        String pay_template = ConfigUtil.get("REFUND_TEMPLATE");
        String url =ConfigUtil.get("weixinShop_URL") + "client_userPoint_refund90.do?ordercode="+code;
        JSONObject jsonObject = new JSONObject();
        String token = this.getAccessToken();
        String remark = "更多详情请点击！";
        try {
            //发送模板消息
            //		您好，您已成功消费。
            //		{{productType.DATA}}：{{name.DATA}}
            //		消费{{accountType.DATA}}：{{account.DATA}}
            //		消费时间：{{time.DATA}}
            //		{{remark.DATA}}
            String sendUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
                    + token;
            String sendString = "{"+
                    "\"touser\":\""+openId+"\","+
                    "\"template_id\":\""+pay_template+"\","+
                    "\"url\":\""+url+"\","+
                    "\"data\":{"+
                    "\"first\": {"+
                    "\"value\":\"您好，您已成功退款\","+
                    "\"color\":\"#000000\""+
                    "},"+
                    "\"keyword1\": {"+
                    "\"value\":\""+back_code+"\","+
                    "\"color\":\"#000000\""+
                    "},"+
                    "\"keyword2\": {"+
                    "\"value\":\""+Double.valueOf(int_total)/100+"元\","+
                    "\"color\":\"#000000\""+
                    "},"+
                    "\"remark\": {"+
                    "\"value\":\""+remark+"\","+
                    "\"color\":\"#EA15CE\""+
                    "}"+
                    "}"+
                    "}";
            //logger.error("=推送模板消息=sendString="+sendString);
            JSONObject sendObject = HttpRequestUtil.httpRequest(sendUrl,"POST",sendString);
            //logger.error("=推送模板消息=sendObject="+sendObject);
        } catch (Exception e) {
            String msg = e.getMessage();
            if(msg.length()>100){
                msg = msg.substring(0, 99);
            }
            jsonObject.put("errmsg", msg);
            //logger.error("推送模板消息=报错=" ,e.fillInStackTrace());
            e.printStackTrace();
        }
    }

    /**
     * 推送支付信息
     */
    public void send_pay_template(int scene_id,String open_id,PorderMainUnion orderMainUnion) throws Exception {
        String pay_template = ConfigUtil.get("PAY_TEMPLATE");
        String url = ConfigUtil.get("weixinShop_URL") + "client_userPoint.do?scene_id="+scene_id;
        JSONObject jsonObject = new JSONObject();
        String token = this.getAccessToken();
        String remark = "更多详情请点击！";
        Brand brand = phoneUserServiceI.getBrandOnlyByCode(orderMainUnion.getBrand_code());
        double jl_total=0;
        jl_total = (brand.getProportion()==0?1:brand.getProportion())*orderMainUnion.getCash_total();
        try {
            //发送模板消息
            //		您好，您已成功消费。
            //		{{productType.DATA}}：{{name.DATA}}
            //		消费{{accountType.DATA}}：{{account.DATA}}
            //		消费时间：{{time.DATA}}
            //		{{remark.DATA}}
            String sendUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
                    + token;
            String sendString = "{"+
                    "\"touser\":\""+open_id+"\","+
                    "\"template_id\":\""+pay_template+"\","+
                    "\"url\":\""+url+"\","+
                    "\"data\":{"+
                    "\"productType\": {"+
                    "\"value\":\"获得久零券\","+
                    "\"color\":\"#000000\""+
                    "},"+
                    "\"name\": {"+
                    "\"value\":\""+jl_total/100+"元\","+
                    "\"color\":\"#2C12EA\""+
                    "},"+
                    "\"accountType\": {"+
                    "\"value\":\"金额\","+
                    "\"color\":\"#000000\""+
                    "},"+
                    "\"account\": {"+
                    "\"value\":\""+orderMainUnion.getCash_total()/100+"元\","+
                    "\"color\":\"#2C12EA\""+
                    "},"+
                    "\"time\": {"+
                    "\"value\":\""+orderMainUnion.getPay_time()+"\","+
                    "\"color\":\"#2C12EA\""+
                    "},"+
                    "\"remark\": {"+
                    "\"value\":\""+remark+"\","+
                    "\"color\":\"#EA15CE\""+
                    "}"+
                    "}"+
                    "}";
            JSONObject sendObject = HttpRequestUtil.httpRequest(sendUrl,"POST",sendString);
        } catch (Exception e) {
            String msg = e.getMessage();
            if(msg.length()>100){
                msg = msg.substring(0, 99);
            }
            jsonObject.put("errmsg", msg);
            e.printStackTrace();
        }
    }

    @Override
    public String getQrcodeTicket(String  code,int couponsMoney) throws Exception {
        String ticket="";

        //获取accesstoken
        String accessToken=getAccessToken();
        String requestUrl="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+accessToken;
        String paramStr = "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"bizperson_"+code+"_"+couponsMoney+"\"}}}";
       System.out.print("微信二维码参数："+paramStr);
        JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl,EnumMethod.POST.name(), paramStr);
        if (jsonObject == null)
        {
            jsonObject = HttpRequestUtil.httpRequest(requestUrl,EnumMethod.GET.name(), null);
        }
        // 如果请求成功
        if (null != jsonObject)
        {
            try
            {
                System.out.println("==========>"+jsonObject.toString());
                ticket =  jsonObject.get("ticket").toString();
            }catch (JSONException e)
            {
                System.out.println(jsonObject.get("errmsg").toString());
                ticket=null;
            }
        }
        return ticket;
    }

}
