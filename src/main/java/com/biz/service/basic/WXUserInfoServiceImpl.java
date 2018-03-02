package com.biz.service.basic;

import com.alibaba.fastjson.JSON;
import com.biz.model.Pmodel.basic.UserList;
import com.biz.model.Pmodel.weixin.UserInfo;
import com.biz.service.api.WxUtilServiceI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.EmojiFilter;
import com.framework.utils.Tools;
import com.framework.utils.weixin.HttpRequestUtil;
import com.framework.utils.weixin.WechatAccessToken;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * 会员相关
 */
@Service("wxUserInfoService")
public class WXUserInfoServiceImpl implements WXUserInfoServiceI{

    @Autowired
    private WxUtilServiceI wxUtilServiceI;

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    public final static String get_user_is_join = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    /**
     * 获取用户
     *
     * @param next_openid 第一个拉取的OPENID，不填默认从头开始拉取
     */
    public UserList UserListOpenid(String next_openid) {
        UserList userList = new UserList();
        try {

            String token = wxUtilServiceI.getAccessToken();
            String requestUrl = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=" + token;
            if (!Tools.isEmpty(next_openid)) {
                requestUrl += "&next_openid=" + next_openid.trim();
            }
            //---------------------------------内容----------------------------


            JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl, "GET", null);
            if (jsonObject == null || jsonObject.size() == 0) {
                //失败
            } else {
                try {
                    //UserOpenid ss=new  UserOpenid();
                    userList = JSON.parseObject(jsonObject.toString(), UserList.class);
                    /*userList.setCount(Integer.parseInt(jsonObject.get("count").toString()));
                    userList.setNext_openid(jsonObject.get("next_openid").toString());
                    userList.setTotal(Integer.parseInt(jsonObject.get("total").toString()));*/

                   /* List<Map<String,Object>> tmpjsonObject=jsonObject.put("data");
                    for(Map<String,Object> tmp :(List<Map<String,Object>>)jsonObject.put("data")){

                    }*/
                    //userList.setData(JSON.parseObject(jsonObject.get("data").toString(), UserList.class));
                    //String s= jsonObject.toString();
                    //userList=JSON.parseObject(s, UserList.class);
                } catch (Exception e) {
                    userList = null;
                }
            }
        } catch (Exception e) {
            userList = null;
        } finally {
            return userList;
        }
    }

    /**
     * 查询所有本地缺失部分
     */
    public List<UserInfo> getUserIsNull() throws Exception {
        List<UserInfo> userinfo=(List<UserInfo>)dao.findForList("userDao.getUserIsNull",null);
        return userinfo;
    }



    /**
     * 获取用户信息
     *
     * @param openid
     * @return
     */
    public UserInfo getUserInfo(String openid) throws Exception {
        UserInfo userInfo = null;
        String access_token = wxUtilServiceI.getAccessToken();
        String requestUrl = get_user_is_join.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
        JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl, "GET", null);
        if (jsonObject == null) {
            jsonObject = HttpRequestUtil.httpRequest(requestUrl, "GET", null);
        }
        // 如果请求成功
        if (null != jsonObject) {
            userInfo = new UserInfo();

            try {
                userInfo.setSubscribe(jsonObject.getInt("subscribe"));
            } catch (Exception e) {
                userInfo.setSubscribe(1);
            }
            try {
                userInfo.setOpenid(jsonObject.getString("openid"));
            } catch (Exception e) {
                userInfo.setOpenid(openid);
            }

            if(jsonObject.getInt("subscribe")==1){

            try {
                userInfo.setHeadimgurl(jsonObject.getString("headimgurl"));
            } catch (Exception e) {
                userInfo.setHeadimgurl("");
            }
            try {
                String ss= EmojiFilter.filterEmoji2(jsonObject.getString("nickname"));
                ss=EmojiFilter.filterEmoji(ss);//过滤
                userInfo.setNickname(ss);
            } catch (Exception e) {
                userInfo.setNickname("");
            }
            try {//国家，如中国为CN
                userInfo.setCountry(jsonObject.getString("country"));
            } catch (Exception e) {
                userInfo.setCountry("");
            }
            try {//用户个人资料填写的省份
                userInfo.setProvince(jsonObject.getString("province"));
            } catch (Exception e) {
                userInfo.setProvince("");
            }
            try {//普通用户个人资料填写的城市
                userInfo.setCity(jsonObject.getString("city"));
            } catch (Exception e) {
                userInfo.setCity("");
            }
            try {
                userInfo.setSex(jsonObject.getInt("sex"));
            } catch (Exception e) {
                userInfo.setSex(0);
            }
            }else {

                userInfo.setHeadimgurl("");
                userInfo.setNickname("");
                userInfo.setCountry("");
                userInfo.setProvince("");
                userInfo.setCity("");
                userInfo.setSex(0);
            }

        }
        return userInfo;
    }

    /**
     * 清空 微信所有用户零时表
     */
    public void deleteUserList() throws Exception {

        dao.save("userDao.deleteBaseWxUser", null);
    }

    /**
     * 插入微信获取到的信息
     */
    public boolean adduserList(List<String> UserInfoList, int count) throws Exception {
        List<String> tmpList = new ArrayList<>();
        Integer j = 0;
        for (int i = 0; i < UserInfoList.size(); i++) {
            tmpList.add(UserInfoList.get(i));
            if (i >= 1000) {
                j += (Integer) dao.save("userDao.insertWxUser", tmpList);
                tmpList = new ArrayList<>();
            }
        }
        if(tmpList.size()>0) {
            j += (Integer) dao.save("userDao.insertWxUser", tmpList);
        }
        return count == j;
    }

    /**
     * 插入用户表
     */
    public boolean adduserInfoList(List<UserInfo> UserInfoList) throws Exception {
        List<UserInfo> tmpList = new ArrayList<UserInfo>();
            Integer j = 0;//共保存成功的行数
            for (int i = 0; i < UserInfoList.size(); i++) {
            tmpList.add(UserInfoList.get(i));
            if (i >= 100) {
                j += (Integer) dao.save("userDao.insertUserInfo", tmpList);
                tmpList = new ArrayList<UserInfo>();
            }
        }
        if(tmpList.size()>0) {
            j += (Integer) dao.save("userDao.insertUserInfo", tmpList);
        }
        return true;
    }



}




