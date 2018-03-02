package com.biz.service.api;

import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.weixin.HttpRequestUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by lzq on 2017/2/4.
 */

@Service("wxQrCodeService")
public class WxQrCodeServiceImpl extends BaseServiceImpl implements WxQrCodeServiceI {
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Resource(name = "wxUtilService")
    private WxUtilServiceI wxUtilService;

    /**
     * 获取临时二维码
     * 一天失效
    */
    @Override
    public String getTempQrCode(int scene_id) throws Exception
    {
        String back = "";
        try
        {
            String token = wxUtilService.getAccessToken();
            String requestUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + token;
            JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl,
                    "POST",
                    "{\"expire_seconds\": 2592000,\"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": \"" + scene_id + "\"}}}");
            if(jsonObject!=null&&jsonObject.size()>0){
                String ticket = jsonObject.getString("ticket");
                if(StringUtils.isNotBlank(ticket)){
                    back = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
                }
            }
        } catch (Exception e){
            //临时二维码=报错=
        }
        return back;
    }
    /**
     * 获取永久二维码
     */
    @Override
    public String getForeverStrQrCode(String scene_str) throws Exception
    {
        String back = "";
        try
        {
            String token = wxUtilService.getAccessToken();
            String requestUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + token;
            JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl,
                    "POST",
                    "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"" + scene_str + "\"}}}");
            if(jsonObject!=null&&jsonObject.size()>0){
                String ticket = jsonObject.getString("ticket");
                if(StringUtils.isNotBlank(ticket)){
                    back = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
                }
            }
        } catch (Exception e){
            //二维码=报错=
        }
        return back;
    }
}
