package com.biz.service.weixin;

import com.biz.model.Pmodel.weixin.UserInfo;
import com.biz.model.Pmodel.weixin.WeChatInfo;
import com.biz.service.base.BaseServiceI;
import net.sf.json.JSONObject;

import java.util.Map;

/**
 * 商户Service
 * Created by liujiajia on 2017/1/6.
 */
public interface ApiServiceI extends BaseServiceI<Object> {
    JSONObject get_Menu(String access_token)throws Exception;
    JSONObject set_Menu(String access_token,String jsonString)throws Exception;
    UserInfo getUserInfo(String authorizer_appid,String agent_openid) throws Exception;
}
