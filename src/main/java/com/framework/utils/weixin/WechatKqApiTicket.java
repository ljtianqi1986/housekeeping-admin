package com.framework.utils.weixin;

import com.biz.model.Pmodel.weixin.JsApiTicket;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;


/**
 * 公众平台通用接口工具类
 * 
 */
public class WechatKqApiTicket
{
	// 获取微信公众号 KqApiTicket的接口地址（GET） 限2000（次/天）
	public final static String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=wx_card";

	/**
	 * 获取KqApiTicket
	 * @param access_token
	 * @return
	 */
	public static JsApiTicket getKqApiTicket(String access_token)
	{
		JsApiTicket kqApiTicket = null;
		String requestUrl = jsapi_ticket_url.replace("ACCESS_TOKEN", access_token);
		JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl,
				EnumMethod.GET.name(), null);
		if (jsonObject == null)
		{
			jsonObject = HttpRequestUtil.httpRequest(requestUrl,
					EnumMethod.GET.name(), null);
		}
		// 如果请求成功
		if (null != jsonObject)
		{
			try
			{
				kqApiTicket = new JsApiTicket();
				kqApiTicket.setTicket(jsonObject.getString("ticket"));
				kqApiTicket.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e)
			{
				kqApiTicket = null;
				// 获取Ticket失败
			}
		}
		return kqApiTicket;
	}

}