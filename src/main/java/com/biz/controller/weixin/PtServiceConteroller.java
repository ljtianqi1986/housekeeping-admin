package com.biz.controller.weixin;

import com.biz.service.weixin.ApiServiceI;
import com.biz.service.weixin.WeiXinServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.Tools;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by qziwm on 2017/6/14.
 */
@Controller
@RequestMapping("/api/weixinApi")
public class PtServiceConteroller extends BaseController {
    @Resource(name = "apiService")
    private ApiServiceI apiService;

    @Resource(name = "weiXinService")
    private WeiXinServiceI weiXinService;


    /**
     * 获取菜单
     * @param response
     * @param appid 公众号id
     * @throws Exception
     */
    @RequestMapping(value = {"/getMenu"}, method = RequestMethod.GET)
    public void getMenu(HttpServletResponse response,String appid) throws Exception{

        JSONObject r_string=new JSONObject();
        if(!Tools.isEmpty(appid)){
            String authorizer_access_token=weiXinService.get_authorizer_access_token(appid.trim());
            r_string= apiService.get_Menu(authorizer_access_token);

        }
        writeJson(r_string.toString(),response);
    }


    /**
     * 修改菜单
     * @param response
     * @param appid
     * @param jsonString
     * @throws Exception
     */
    @RequestMapping(value = {"/setMenu"}, method = RequestMethod.POST)
    public void setMenu(HttpServletResponse response,String appid,String jsonString) throws Exception{

        JSONObject r_string=new JSONObject();
        if(!Tools.isEmpty(appid)){
            String authorizer_access_token=weiXinService.get_authorizer_access_token(appid.trim());
            r_string= apiService.set_Menu(authorizer_access_token,jsonString);
        }


        writeJson(r_string.toString(),response);
    }




}
