package com.biz.controller.sys;

import com.alibaba.fastjson.JSON;
import com.biz.model.Pmodel.api.WxUser;
import com.biz.service.api.BaseUserServiceI;
import com.biz.service.weixin.WeiXinServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.SpringApplicationContextHolder;
import com.framework.utils.StringUtil;
import com.framework.utils.URLConectionUtil;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by qziwm on 2017/7/26.
 */
@Controller
public class SyncUserInfo extends BaseController implements Runnable{
/*
    @Autowired
    private WeiXinServiceI weiXinService;
*/


   WeiXinServiceI weiXinService = (WeiXinServiceI) SpringApplicationContextHolder.getBean("weiXinService");
    BaseUserServiceI baseUserService = (BaseUserServiceI) SpringApplicationContextHolder.getBean("baseUserService");

    private String accessToken;
    private String appid;
    @Override
    public void run() {
        String url="https://api.weixin.qq.com/cgi-bin/user/get";
        Map<String,String> parm=new HashMap<>();
        parm.put("access_token",accessToken);
        parm.put("next_openid","");
        String res= URLConectionUtil.httpURLConnectionPostDiy(url,parm);
        if(!"失败".equals(res))//先调用一次接口获取用户总数
        {
            WxUser returnInfo= JSON.parseObject(res,WxUser.class);
            if(StringUtil.isNullOrEmpty(returnInfo.getErrcode()))
            {
                int total=returnInfo.getTotal();
                //一次最大获取10000条，所以count最大10000，所以要计算获取次数
                int count=returnInfo.getCount();
                int times=total/count;
                if(total%count!=0)
                {times++;}
                String nextOpenId="";
                for(int i=0;i<times;i++)
                {
                    try {
                        nextOpenId=getUserList(url,nextOpenId,accessToken);//进行循环获取
                        if(StringUtil.isNullOrEmpty(nextOpenId))
                        {break;}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String getUserList(String url,String nextOpenId,String token) throws Exception {
        Map<String,String> parm=new HashMap<>();
        parm.put("access_token",token);
        parm.put("next_openid","");
        String res= URLConectionUtil.httpURLConnectionPostDiy(url,parm);
        if(!"失败".equals(res)) {
            WxUser returnInfo = JSON.parseObject(res, WxUser.class);
            if (StringUtil.isNullOrEmpty(returnInfo.getErrcode())) {
                List<String> list= (List<String>) returnInfo.getData().get("openid");
                nextOpenId=returnInfo.getNext_openid();
                saveUserList(list,token);
            }
            else if("40001".equals(returnInfo.getErrcode())||"40014".equals(returnInfo.getErrcode())||"41001".equals(returnInfo.getErrcode())||"42001".equals(returnInfo.getErrcode())){//当失败的时候重新获取一次token再次尝试获取
                token=weiXinService.get_authorizer_access_token(appid);
                nextOpenId=  getUserList(url,nextOpenId,token);
            }else
            {nextOpenId="";}
            return nextOpenId;
        }
        return "";
    }

    private void saveUserList(List<String> list,String token) throws Exception {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info";
        List<Map<String, Object>> userList = new ArrayList<>();
        for (String openId : list) {
            Map<String, String> map = new HashMap<>();
            Map<String, Object> resmap = new HashMap<>();
            map.put("access_token", token);
            map.put("openid", openId);
            map.put("lang", "zh_CN");

            String res = URLConectionUtil.httpURLConnectionPostDiy(url, map);
            if (!"失败".equals(res)) {
                resmap = JSON.parseObject(res, Map.class);
                if (!resmap.containsKey("errcode")) {
                    userList.add(resmap);
                } else if ("40001".equals(resmap.get("errcode")) || "40014".equals(resmap.get("errcode")) || "41001".equals(resmap.get("errcode")) || "42001".equals(resmap.get("errcode"))) {//当失败的时候重新获取一次token再次尝试获取
                    token = weiXinService.get_authorizer_access_token(appid);
                    map.put("access_token", token);
                    res = URLConectionUtil.httpURLConnectionPostDiy(url, map);
                    resmap = JSON.parseObject(res, Map.class);
                    if (!resmap.containsKey("errcode")) {
                        userList.add(resmap);
                    }
                }
            }
        }
        baseUserService.saveUserList(userList,appid);
       // System.out.printf(JSON.toJSONString(userList));
    }
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}
