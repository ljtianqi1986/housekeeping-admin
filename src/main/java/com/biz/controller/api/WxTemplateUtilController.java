package com.biz.controller.api;

import com.biz.service.api.WxTemplateServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 模板消息统一管理
 * Created by lzq on 2017/4/21.
 */
@Controller
@RequestMapping("/api/wxTemplateUtil")
public class WxTemplateUtilController extends BaseController {

    @Resource(name = "wxTemplateService")
    private WxTemplateServiceI wxTemplateService;


    @RequestMapping(value="/sendChangeNoticeTemplate")
    public void sendChangeNoticeTemplate(HttpServletResponse response, String goodsName, String changeNumber,String count,String remark, String openId)
    {
        Map<String, Object> map = new HashMap<>();

        map.put("return_code",0);
        map.put("return_info","模板消息发送失败！");
        if(StringUtil.isNullOrEmpty(openId) || StringUtil.isNullOrEmpty(openId))
        {
            map.put("return_info","openId不能为空！");
        }else{
            //
//            String goodsName = "蛋糕券";
//            String changeNumber = "140000000000000";
//            String count = "1";
//            String remark = "感谢你的支持！";
//            String openId = "obisKwUYetOB6fj1vBSqYf_afcJ0";

            map = wxTemplateService.sendChangeNoticeTemplate(goodsName, changeNumber,count, remark,openId);
        }
        writeJson(map,response);
    }
}
