package com.biz.service.basic;

import com.alibaba.fastjson.JSON;
import com.biz.model.Pmodel.basic.UserList;
import com.biz.model.Pmodel.weixin.UserInfo;
import com.framework.utils.EmojiFilter;
import com.framework.utils.Tools;
import com.framework.utils.weixin.HttpRequestUtil;
import com.framework.utils.weixin.WechatAccessToken;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tomchen on 17/6/5.
 */
public interface WXUserInfoServiceI {

     UserList UserListOpenid(String next_openid);

     List<UserInfo> getUserIsNull() throws Exception ;

     UserInfo getUserInfo(String openid) throws Exception;

     void deleteUserList() throws Exception ;

     boolean adduserList(List<String> UserInfoList, int count) throws Exception;

     boolean adduserInfoList(List<UserInfo> UserInfoList) throws Exception ;




}
