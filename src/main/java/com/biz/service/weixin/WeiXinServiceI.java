package com.biz.service.weixin;

import com.biz.model.Pmodel.weixin.WeChatInfo;
import com.biz.service.base.BaseServiceI;
import net.sf.json.JSONObject;

import java.nio.file.WatchEvent;
import java.util.Map;

/**
 * 商户Service
 * Created by liujiajia on 2017/1/6.
 */
public interface WeiXinServiceI extends BaseServiceI<Object> {
    String get_authorizer_access_token(String appid) throws Exception;
    String get_component_verify_ticket() throws Exception;
    boolean save_component_verify_ticket(Map<String, String> mapData) throws Exception;
    String get_pre_auth_code() throws Exception;
    JSONObject api_query_auth(String authorization_code)throws Exception;
    WeChatInfo getWeChatInfoByAppid(String appid) throws Exception;
    boolean save_WeChatInfoByAppid(String authorizer_appid,String auth_code, String authorizer_refresh_token,String authorizer_access_token,int expires_in) throws Exception;
    String get_component_access_token() throws Exception;
    void updateUserState(Map<String, Object> user)throws Exception;
    String toUserNameToAppid(String toUserName) throws Exception;
}
