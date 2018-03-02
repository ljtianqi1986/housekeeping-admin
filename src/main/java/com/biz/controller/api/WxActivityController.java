package com.biz.controller.api;

import com.alibaba.fastjson.JSON;
import com.biz.model.Pmodel.api.*;
import com.biz.service.api.WxActivityServiceI;
import com.biz.service.api.WxTemplateServiceI;
import com.biz.service.api.WxtsServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.ConfigUtil;
import com.framework.utils.MathUtil;
import com.framework.utils.URLConectionUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信活动接口
 *
 * @author lzq
 */
@Controller
@RequestMapping("/api/wxactivity")
public class WxActivityController extends BaseController {
    @Resource(name = "wxActivityService")
    private WxActivityServiceI wxActivityService;

    /**
     * 活动方法
     * @param response
     * @param open_id 用户openid
     * @param sceneType 活动类型(1:首次关注送)
     */
    @RequestMapping(value = "/wxpublic", method = RequestMethod.POST)
    private void wxscan(HttpServletResponse response,
                        @RequestParam String open_id,
                        @RequestParam String sceneType) {
        boolean pd=false;
        String msg="false";
        try {
            synchronized (this) {
                msg=wxActivityService.adActivityListDate(open_id.trim(),sceneType,null);
                if(!msg.equals("true")&&!msg.equals("false")){
                    writeJson(msg,response);
                }else if(msg.equals("true")){
                    writeJson(true,response);
                }else if(msg.equals("false")){
                    writeJson(false,response);
                }
            }
        } catch (Exception e) {
            logger.error("=扫描带参数二维码=报错=", e.fillInStackTrace());
            writeJson(false, response);
        }
    }



    //接口
    public Map<String, Object> APIInterface(Map<String, String> jSONObject, String url) {
        Map<String, Object> map2 = new HashMap<String, Object>();
        try {
            String requestUrl = ConfigUtil.get("QT_Url") + url;
            String x = URLConectionUtil.httpURLConnectionPostDiy(requestUrl, jSONObject);
            x = URLDecoder.decode(x, "utf-8");
            if (x == null || x.trim().equals("") || x.trim().equals("失败")) {
                // 失败
            } else {
                map2 = JSON.parseObject(x, Map.class);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            return map2;
        }
    }


}