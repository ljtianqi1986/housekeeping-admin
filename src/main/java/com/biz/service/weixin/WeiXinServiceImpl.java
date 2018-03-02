package com.biz.service.weixin;


import com.biz.model.Hmodel.basic.*;
import com.biz.model.Pmodel.weixin.Pauthorization;
import com.biz.model.Pmodel.weixin.WeChatInfo;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.Tools;
import com.framework.utils.weixin.HttpRequestUtil;
import com.framework.weixn.weixConst;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

//import com.biz.model.Pmodel.basic.PwxgoodsStock;

/**
 * Created by liujiajia on 2017/1/6.
 */
@Service("weiXinService")
public class WeiXinServiceImpl extends BaseServiceImpl<Object> implements WeiXinServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    private static String ComponentVerifyTicket="ComponentVerifyTicket";
    private static String Component_access_token="component_access_token";
    private static String preAuthCode= "pre_auth_code";

    //private static String appid_value="wx6f49c666fab387e6";
   // private static String appsecret_value="b739b9b6a27cb8e877af9cde1c8ae67d";


    /**
     * 保存 component_verify_ticket
     * @param mapData
     * @return
     * @throws Exception
     */
    @Override
    public boolean save_component_verify_ticket(Map<String, String> mapData) throws Exception {
        boolean pd=false;
        if(mapData!=null) {
            Pauthorization save_auth=new Pauthorization();
            save_auth.setId(ComponentVerifyTicket);
            save_auth.setCreateTime(mapData.get("CreateTime").toString());
            save_auth.setText(mapData.get("ComponentVerifyTicket").toString());
            save_auth.setEnd_time("0");
            pd=save_authorization(save_auth);
        }
        return pd;
    }


    /**
     * 获取 component_verify_ticket
     * @return
     * @throws Exception
     */
    @Override
    public String get_component_verify_ticket() throws Exception {
        Pauthorization  authorization= get_authorization(ComponentVerifyTicket);
        if(authorization==null){
            return "";
        }else{
            return authorization.getText();
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //获取第三方平台component_access_token
    @Override
    public String get_component_access_token() throws Exception {
        String AccessToken="";
        Pauthorization  authorization= get_authorization(Component_access_token);
        long new_time = System.currentTimeMillis();//当前时间戳
        boolean pf = true;//判断是否需要重新获取
        if (Long.parseLong(authorization.getEnd_time()) < new_time) {//过期
            pf = false;
        } else {
            AccessToken = authorization.getText();
        }
        if (!pf) {
            //重新获取
            String ticket_value= get_component_verify_ticket();
            Map<String,Object> accessTokenMap= getAccessToken(ticket_value);
            String component_access_token=accessTokenMap.get("component_access_token").toString();
            int expires_in=Integer.parseInt(accessTokenMap.get("expires_in").toString());
            if (!Tools.isEmpty(component_access_token)) {
                Pauthorization save_auth=new Pauthorization();
                save_auth.setId(Component_access_token);
                save_auth.setText(component_access_token);
                save_auth.setEnd_time(String.valueOf(new_time + (expires_in * 1000)));
                if(save_authorization(save_auth)){
                    AccessToken=component_access_token;
                }
            }
        }
        return AccessToken;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//获取预授权码 pre_auth_code
    @Override
    public String get_pre_auth_code() throws Exception {
        String preAuthCodeString="";
        Pauthorization  authorization= get_authorization(preAuthCode);
        long new_time = System.currentTimeMillis();//当前时间戳
        boolean pf = true;//判断是否需要重新获取
        if (Long.parseLong(authorization.getEnd_time())< new_time) {//过期
            pf = false;
        } else {
            preAuthCodeString = authorization.getText();
        }
        if (!pf) {
            //重新获取
            Map<String,Object> accessTokenMap= getPreAuthCode();
            String pre_auth_code=accessTokenMap.get("pre_auth_code").toString();
            int expires_in=Integer.parseInt(accessTokenMap.get("expires_in").toString());
            if (!Tools.isEmpty(pre_auth_code)) {
                Pauthorization save_auth=new Pauthorization();
                save_auth.setId(preAuthCode);
                save_auth.setText(pre_auth_code);
                save_auth.setEnd_time(String.valueOf(new_time + (expires_in*1000)));
                if(save_authorization(save_auth)){
                    preAuthCodeString=pre_auth_code;
                }
            }
        }
        return preAuthCodeString;
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 使用授权码换取公众号的授权信息
     * 请求参数说明
     参数	说明
     component_appid	第三方平台appid
     authorization_code	授权code,会在授权成功时返回给第三方平台，详见第三方平台授权流程说明
     结果参数说明
     authorization_info	授权信息
     authorizer_appid	授权方appid
     authorizer_access_token	授权方令牌（在授权的公众号具备API权限时，才有此返回值）
     expires_in	有效期（在授权的公众号具备API权限时，才有此返回值）
     authorizer_refresh_token	刷新令牌（在授权的公众号具备API权限时，才有此返回值），刷新令牌主要用于公众号第三方平台获取和刷新已授权用户的access_token，只会在授权时刻提供，请妥善保存。 一旦丢失，只能让用户重新授权，才能再次拿到新的刷新令牌
     func_info	公众号授权给开发者的权限集列表（请注意，当出现用户已经将消息与菜单权限集授权给了某个第三方，再授权给另一个第三方时，由于该权限集是互斥的，后一个第三方的授权将去除此权限集，开发者可以在返回的func_info信息中验证这一点，避免信息遗漏），1到13分别代表：

     请注意：
     1）该字段的返回不会考虑公众号是否具备该权限集的权限（因为可能部分具备），请根据公众号的帐号类型和认证情况，来判断公众号的接口权限。

     */
    @Override
    public JSONObject api_query_auth(String authorization_code)throws Exception
    {
        try{
            String token = get_component_access_token();

            String jsonString=	"{"+
                    "\"component_appid\": \""+weixConst.APPID+"\","+
                    "\"authorization_code\": \""+authorization_code+"\""+
                    "}";

            String requestUrl = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=" + token;
            JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl,"POST", jsonString);
            System.out.println("公众号的授权信息=返回值jsonObject=" + jsonObject);
            return jsonObject;
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("公众号的授权信息报错===="+e.fillInStackTrace());
            return null;
        }
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 获取授权微信信息
     */
    @Override
    public WeChatInfo getWeChatInfoByAppid(String appid) throws Exception
    {
        return (WeChatInfo) dao.findForObject("WeiXinDao.getWeChatInfoByAppid", appid);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 保存，修改微信授权信息
     * @param authorizer_appid 当前授权的微信appid
     * @param auth_code
     * @param authorizer_refresh_token
     * @param authorizer_access_token
     * @return
     * @throws Exception
     */
    @Override
    public boolean save_WeChatInfoByAppid(String authorizer_appid,String auth_code, String authorizer_refresh_token,String authorizer_access_token,int expires_in) throws Exception
    {
        long new_time = System.currentTimeMillis();//当前时间戳
        new_time=new_time + (expires_in*1000);//计算失效时间

        boolean pd=false;

        WeChatInfo weChatInfo = getWeChatInfoByAppid(authorizer_appid);
        if(weChatInfo==null){
            weChatInfo = new WeChatInfo();
            weChatInfo.setAppid(authorizer_appid);
            weChatInfo.setRefresh_token(authorizer_refresh_token);
            weChatInfo.setEnd_time(String.valueOf(new_time));
            weChatInfo.setAccess_token(authorizer_access_token);
            weChatInfo.setAuthorization_code(auth_code);
            pd=(Integer)dao.save("WeiXinDao.saveWeChat",weChatInfo)>0;
        }else{
            weChatInfo.setAppid(authorizer_appid);
            weChatInfo.setRefresh_token(authorizer_refresh_token);
            weChatInfo.setEnd_time(String.valueOf(new_time));
            weChatInfo.setAccess_token(authorizer_access_token);
            weChatInfo.setAuthorization_code(auth_code);
            pd=(Integer)dao.update("WeiXinDao.updateWeChat", weChatInfo)>0;
        }

        return pd;
    }

    //获取微信授权公众号或小程序的 authorizer_access_token
    @Override
    public String get_authorizer_access_token(String appid) throws Exception {
        appid=appid.trim();
        String r_authorizer_access_token="";
        WeChatInfo weChatInf= getWeChatInfoByAppid(appid);
        long new_time = System.currentTimeMillis();//当前时间戳
        boolean pf = true;//判断是否需要重新获取
        if (Long.parseLong(weChatInf.getEnd_time())< new_time) {//过期
            pf = false;
        } else {
            r_authorizer_access_token = weChatInf.getAccess_token();
        }
        if (!pf) {
            //重新获取
            String component_access_token=get_component_access_token();
            Map<String,Object> accessTokenMap= getAuthorizerAccessToken(component_access_token,appid,weChatInf.getRefresh_token());
            String M_authorizer_access_token=accessTokenMap.get("authorizer_access_token").toString();
            int M_expires_in=Integer.parseInt(accessTokenMap.get("expires_in").toString());
            String M_authorizer_refresh_token=accessTokenMap.get("authorizer_refresh_token").toString();
            if (!Tools.isEmpty(M_authorizer_access_token)) {
                WeChatInfo save_chat=new WeChatInfo();
                save_chat.setAppid(appid);
                save_chat.setAccess_token(M_authorizer_access_token);
                save_chat.setRefresh_token(M_authorizer_refresh_token);
                save_chat.setEnd_time(String.valueOf(new_time + (M_expires_in*1000)));

                boolean pd=(Integer)dao.update("WeiXinDao.updateWeChat2", save_chat)>0;
                if(pd){
                    r_authorizer_access_token=M_authorizer_access_token;
                }
            }
        }
        return r_authorizer_access_token;

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////



    /**
     * 获取 Pauthorization 通用方法
     * @return
     * @throws Exception
     */
    public Pauthorization get_authorization(String id) throws Exception {
        Pauthorization select_auth=new Pauthorization();
        select_auth.setId(id);
        Pauthorization  authorization= (Pauthorization)dao.findForObject("WeiXinDao.getAuthorization",select_auth);

            return authorization;

    }

    /**
     * 保存 Pauthorization 通用方法
     * @return
     * @throws Exception
     */
    private boolean save_authorization(Pauthorization save_auth) throws Exception {
        boolean pd=false;
        pd=(Integer)dao.update("WeiXinDao.updateAuthorization",save_auth)==1;
        return pd;
    }


    /**
     * 获取access_token
     * @param ticket_value
     * @return
     */
    public static Map<String,Object> getAccessToken(String ticket_value)
    {
        //返回参数
        String component_access_token="";
        String expires_in="0";
        if(!Tools.isEmpty(ticket_value)) {
            String requestUrl = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
            String result = "{\"component_appid\":\"" + weixConst.APPID + "\",\"component_appsecret\": \"" + weixConst.APPSECRET + "\",\"component_verify_ticket\": \"" + ticket_value + "\" }";
            JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl, "POST", result);
            if (jsonObject == null) {
                //第一次不通再调一次
                jsonObject = HttpRequestUtil.httpRequest(requestUrl, "POST", result);
            }
            // 如果请求成功
            if (null != jsonObject) {
                try {
                    component_access_token = jsonObject.getString("component_access_token");
                    expires_in = jsonObject.getString("expires_in");
                } catch (Exception e) {
                    // 获取token失败
                }
            }
        }

        Map<String,Object> r_map=new HashMap<>();
        r_map.put("component_access_token",component_access_token);
        r_map.put("expires_in",expires_in);
        return r_map;
    }


    /**
     * 获取pre_auth_code
     * @return
     */
    private Map<String,Object> getPreAuthCode() throws Exception {
        //返回参数
        String component_access_token=get_component_access_token();
        String pre_auth_code="";
        String expires_in="0";
        if(!Tools.isEmpty(component_access_token)) {
            String requestUrl = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=" + component_access_token;
            String result = "{\"component_appid\":\"" + weixConst.APPID + "\" }";
            JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl, "POST", result);
            if (jsonObject == null) {
                //第一次不通再调一次
                jsonObject = HttpRequestUtil.httpRequest(requestUrl, "POST", result);
            }
            // 如果请求成功
            if (null != jsonObject) {
                try {
                    pre_auth_code = jsonObject.getString("pre_auth_code");
                    expires_in = jsonObject.getString("expires_in");
                } catch (Exception e) {
                    // 获取token失败
                }
            }
        }

        Map<String,Object> r_map=new HashMap<>();
        r_map.put("pre_auth_code",pre_auth_code);
        r_map.put("expires_in",expires_in);
        return r_map;
    }


    /**
     * 获取 authorizer_access_token
     * @param component_access_token
     * @param authorizer_appid 授权公众号或小程序的appid
     * @param authorizer_refresh_token 授权方的刷新令牌
     * @return
     */
    public static Map<String,Object> getAuthorizerAccessToken(String component_access_token,String authorizer_appid,String authorizer_refresh_token)
    {
        //返回参数
        String z_authorizer_access_token="";
        String z_authorizer_refresh_token="";
        String z_expires_in="0";
        if(!(Tools.isEmpty(authorizer_appid) || Tools.isEmpty(authorizer_refresh_token) || Tools.isEmpty(component_access_token))) {
            String requestUrl = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token="+component_access_token;
            String result = "{\"component_appid\":\"" + weixConst.APPID + "\",\"authorizer_appid\": \"" + authorizer_appid + "\",\"authorizer_refresh_token\": \"" + authorizer_refresh_token + "\" }";
            JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl, "POST", result);
            if (jsonObject == null) {
                //第一次不通再调一次
                jsonObject = HttpRequestUtil.httpRequest(requestUrl, "POST", result);
            }
            // 如果请求成功
            if (null != jsonObject) {
                try {
                    z_authorizer_access_token = jsonObject.getString("authorizer_access_token");
                    z_authorizer_refresh_token = jsonObject.getString("authorizer_refresh_token");
                    z_expires_in = jsonObject.getString("expires_in");
                } catch (Exception e) {
                    // 获取token失败
                }
            }
        }

        Map<String,Object> r_map=new HashMap<>();
        r_map.put("authorizer_access_token",z_authorizer_access_token);
        r_map.put("authorizer_refresh_token",z_authorizer_refresh_token);
        r_map.put("expires_in",z_expires_in);
        return r_map;
    }



    @Override
    public void updateUserState(Map<String, Object> user) throws Exception {
        dao.save("userDao.updateCliUserState", user);
    }

    //用微信账户 换取 获取appid
    @Override
    public String toUserNameToAppid(String toUserName) throws Exception {
        String pd="";
        if(!Tools.isEmpty(toUserName)){
            Map<String,Object> select_map=new HashMap<>();
            select_map.put("origin_id",toUserName);
            WeChatInfo weChatInfo=(WeChatInfo)dao.findForObject("WeiXinDao.getWeChatInfoByToUserName", select_map);
            if(weChatInfo!=null){
                pd=weChatInfo.getAppid();
            }
        }
        return pd;
    }
}
