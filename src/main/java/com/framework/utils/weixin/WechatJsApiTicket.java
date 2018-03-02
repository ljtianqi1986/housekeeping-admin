package com.framework.utils.weixin;

import com.biz.model.Pmodel.weixin.JsApiTicket;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;


/**
 * 公众平台通用接口工具类
 * 
 */
public class WechatJsApiTicket
{
	// 获取微信公众号：JsApiTicket的接口地址（GET） 限2000（次/天）
	public final static String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

	/**
	 * 获取JsApiTicket
	 * @param access_token
	 * @return
	 */
	public static JsApiTicket getJsApiTicket(String access_token)
	{
		JsApiTicket jsApiTicket = null;
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
				jsApiTicket = new JsApiTicket();
				jsApiTicket.setTicket(jsonObject.getString("ticket"));
				jsApiTicket.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e)
			{
				jsApiTicket = null;
				// 获取Ticket失败
			}
		}
		return jsApiTicket;
	}

}