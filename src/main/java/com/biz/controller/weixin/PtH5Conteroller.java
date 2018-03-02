package com.biz.controller.weixin;

import com.biz.model.Pmodel.weixin.UserInfo;
import com.biz.service.api.WxtsServiceI;
import com.biz.service.weixin.ApiServiceI;
import com.biz.service.weixin.WeiXinServiceI;
import com.framework.controller.BaseController;
import com.framework.weixn.weixConst;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.framework.utils.ConfigUtil;
import com.framework.utils.weixin.HttpRequestUtil;

import javax.annotation.Resource;

/**
 * Created by qziwm on 2017/6/14.
 */
@Controller
@RequestMapping("/api/h5Function")
public class PtH5Conteroller extends BaseController {

    @Resource(name = "weiXinService")
    private WeiXinServiceI weiXinService;

    @Resource(name = "apiService")
    private ApiServiceI apiService;

    @Resource(name = "wxtsService")
    private WxtsServiceI wxtsService;


    @RequestMapping(value = "/wx_red_open_id")
    public ModelAndView wx_red_open_id(String appid,String backUrl){

        System.out.println("=获取授权公众号的=appid=" + appid + "=backUrl=" + backUrl);
        if(org.apache.commons.lang.StringUtils.isBlank(appid)){
            mv.addObject("err_msg", "参数丢失");
            mv.setViewName("client/pt/oauth2error");
            return mv;
        }
        try
        {
            backUrl = java.net.URLEncoder.encode(backUrl, "utf-8");
            String redirect_uri =ConfigUtil.get("SYS_URL")+"/api/h5Function/wx_red_back_open_id.ac?sub_appid="+appid+"&backUrl="+backUrl;
            redirect_uri = java.net.URLEncoder.encode(redirect_uri, "utf-8");
            String oauth2Url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                    + appid
                    + "&redirect_uri="
                    + redirect_uri
                    + "&response_type=code&scope=snsapi_base&state=ok"
                    +"&component_appid="+ weixConst.APPID+"#wechat_redirect";

            mv.setViewName( "redirect:" + oauth2Url);
        } catch (Exception e)
        {
            logger.error("=获取授权公众号的open_id=appid报错=",e.fillInStackTrace());
            e.printStackTrace();
        }
        return mv;
    }



    /**
     * 获取授权公众号的open_id，返回页面
     */
    @RequestMapping(value = "/wx_red_back_open_id")
    public ModelAndView wx_do_back_open_id(String bd_open_id,String sub_appid,String backUrl,@RequestParam String code) throws Exception {
        System.out.println("获取code=" + code + "=backUrl=" + backUrl + "=sub_appid=" + sub_appid);
        String COMPONENT_ACCESS_TOKEN =weiXinService.get_component_access_token();
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/component/access_token?" +
                "appid=" + sub_appid +
                "&code=" + code +
                "&grant_type=authorization_code" +
                "&component_appid=" +weixConst.APPID+
                "&component_access_token="+COMPONENT_ACCESS_TOKEN;
        JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl,"GET", null);
        //logger.warn("返回sonObject=" + jsonObject);
        // 如果请求成功
        if (null != jsonObject)
        {
            try
            {
                Object object_openid =  jsonObject.get("openid");
                System.out.println("object_openid=" + object_openid);
                if(object_openid != null){
                    //第三方的open_id
                    String sub_openid = jsonObject.getString("openid");
                    //保存用户信息
                    wxtsService.getBaseUserByOpen_id(sub_openid,sub_appid);
                    mv.addObject("sub_open_id", sub_openid);
                }
                mv.setViewName( "redirect:" + backUrl);
                return mv;
            } catch (JSONException e)
            {
                System.out.println("=用户信息报错====" + e.fillInStackTrace());
                e.printStackTrace();
            }
        }
        mv.addObject("err_msg", "用户信息获取失败！");
        mv.setViewName("client/pt/oauth2error");
        return mv;
    }


}
