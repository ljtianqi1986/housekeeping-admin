package com.biz.service.weixin;


import com.biz.model.Pmodel.weixin.Pauthorization;
import com.biz.model.Pmodel.weixin.UserInfo;
import com.biz.model.Pmodel.weixin.WeChatInfo;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.Tools;
import com.framework.utils.weixin.HttpRequestUtil;
import com.framework.weixn.weixConst;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

//import com.biz.model.Pmodel.basic.PwxgoodsStock;

/**
 * Created by liujiajia on 2017/1/6.
 */
@Service("apiService")
public class ApiServiceImpl extends BaseServiceImpl<Object> implements ApiServiceI {

    @Resource(name = "weiXinService")
    private WeiXinServiceI weiXinService;


    /**
     * 获取菜单
     * @param access_token
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject get_Menu(String access_token)throws Exception
    {
        try{

            /*String jsonString=	"{"+
                    "\"component_appid\": \""+weixConst.APPID+"\","+
                    "\"authorization_code\": \""+authorization_code+"\""+
                    "}";*/

            String requestUrl = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=" + access_token;
            JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl,"GET", null);
            return jsonObject;
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("公众号的授权信息报错===="+e.fillInStackTrace());
            return null;
        }
    }


    /**
     * 创建菜单
     * @param access_token
     * @return
     * @throws Exception
     */
    @Override
    public JSONObject set_Menu(String access_token,String jsonString)throws Exception
    {
        try{
            String requestUrl = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + access_token;
            JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl,"POST", jsonString);
            return jsonObject;
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("公众号的授权信息报错===="+e.fillInStackTrace());
            return null;
        }
    }


    /**
     * 获取用户基本信息
     * @throws Exception
     */
    @Override
    public UserInfo getUserInfo(String authorizer_appid,String agent_openid) throws Exception
    {
        UserInfo userInfo = null;
        String get_user_is_join="https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        String agent_access_token = weiXinService.get_authorizer_access_token(authorizer_appid);
        String requestUrl = get_user_is_join.replace("ACCESS_TOKEN", agent_access_token).replace("OPENID", agent_openid);
        JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl,"GET", null);
        System.out.println("=获取用户基本信息返回=" + jsonObject);
        if (jsonObject == null){
            return null;
        }
        Object errmsg= jsonObject.get("errmsg");
        if(errmsg !=null ){ //异常
            return null;
        }
        userInfo = new UserInfo();
        try{
            userInfo.setHeadimgurl(jsonObject.getString("headimgurl"));
        }
        catch (Exception e){
            userInfo.setHeadimgurl("");
        }
        try{
            userInfo.setNickname(jsonObject.getString("nickname"));
        }
        catch (Exception e){
            userInfo.setNickname("");
        }
        try{
            userInfo.setOpenid(jsonObject.getString("openid"));
        }
        catch (Exception e){
            userInfo.setOpenid(agent_openid);
        }
        try{
            userInfo.setSex(jsonObject.getInt("sex"));
        }
        catch (Exception e){
            userInfo.setSex(0);
        }
        try{
            userInfo.setSubscribe(jsonObject.getInt("subscribe"));
        }
        catch (Exception e)	{
            userInfo.setSubscribe(1);
        }
        try{
            userInfo.setUnionid(jsonObject.getString("unionid"));
        }
        catch (Exception e){
            userInfo.setUnionid("");
        }
        userInfo.setCity("");
        userInfo.setCountry("");
        userInfo.setPrivilege("");
        userInfo.setProvince("");
        return userInfo;
    }




}
