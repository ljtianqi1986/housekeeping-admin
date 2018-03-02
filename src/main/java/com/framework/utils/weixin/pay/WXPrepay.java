package com.framework.utils.weixin.pay;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jdom2.JDOMException;
import org.jsoup.Jsoup;

import java.util.Map;
import java.util.TreeMap;

/**
 * 生成预支付订单号
 * @author GengLong
 *
 */
public class WXPrepay
{
	private static String unifiedorder = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	private static String orderquery = "https://api.mch.weixin.qq.com/pay/orderquery";
	
	private String appid;
	private String mch_id;
	private String nonce_str = OrderUtil.CreateNoncestr();
	private String body;
	private String out_trade_no;
	private String total_fee;
	private String spbill_create_ip;
	private String trade_type;
	private String notify_url;
	private String time_expire;
	private String time_start;
	private String sign;
	private String partnerKey;
	
	// 预支付订单号
	private String prepay_id;
	private String code_url;
	private String openid = "";
	


	/**
	 * 生成预支付订单
	 * 
	 * @return
	 */
	public String submitXmlGetPrepayId()
	{
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		HttpPost httpPost = new HttpPost(unifiedorder);
		String xml = getPackage();
		StringEntity entity;
		try
		{
			entity = new StringEntity(xml, "utf-8");
			httpPost.setEntity(entity);
			HttpResponse httpResponse;
			// post请求
			httpResponse = closeableHttpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null)
			{
				// 打印响应内容
				String result = EntityUtils.toString(httpEntity, "UTF-8");
				System.out.println(result);
				// 过滤
				result = result.replaceAll("<![CDATA[|]]>", "");
				String prepay_id = Jsoup.parse(result).select("prepay_id")
						.html();
				this.prepay_id = prepay_id;
				if (prepay_id != null)
					return prepay_id;
			}
			// 释放资源
			closeableHttpClient.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return prepay_id;
	}
	
	
	/**
	 * 生成预支付订单二维码url
	 * 
	 * @return
	 */
	public String submitXmlGetCodeUrl()
	{
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		HttpPost httpPost = new HttpPost(unifiedorder);
		String xml = getPackageForUrlCode();
		StringEntity entity;
		try
		{
			entity = new StringEntity(xml, "utf-8");
			httpPost.setEntity(entity);
			HttpResponse httpResponse;
			// post请求
			httpResponse = closeableHttpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null)
			{
				// 打印响应内容
				String result = EntityUtils.toString(httpEntity, "UTF-8");
				System.out.println(result);
				// 过滤
				result = result.replaceAll("<![CDATA[|]]>", "");
				String code_url = Jsoup.parse(result).select("code_url")
						.html();
				this.code_url = code_url;
				if (code_url != null)
					return code_url;
			}
			// 释放资源
			closeableHttpClient.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return code_url;
	}
	

	/**
	 * 请求订单查询接口
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> reqOrderquery()
	{
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		HttpPost httpPost = new HttpPost(orderquery);
		String xml = getPackage();
		StringEntity entity;
		Map<String, String> map = null;
		try
		{
			entity = new StringEntity(xml, "utf-8");
			httpPost.setEntity(entity);
			HttpResponse httpResponse;
			// post请求
			httpResponse = closeableHttpClient.execute(httpPost);
			// getEntity()
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null)
			{
				// 打印响应内容
				String result = EntityUtils.toString(httpEntity, "UTF-8");
				// 过滤
				result = result.replaceAll("<![CDATA[|]]>", "");
				try
				{
					map = XMLUtil.doXMLParse(result);
				} catch (JDOMException e)
				{
					e.printStackTrace();
				}
			}
			// 释放资源
			closeableHttpClient.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}

	public String getPackage()
	{
		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		treeMap.put("appid", this.appid);
		treeMap.put("mch_id", this.mch_id);
		treeMap.put("nonce_str", this.nonce_str);
		treeMap.put("body", this.body);
		treeMap.put("time_expire", this.time_expire);
		treeMap.put("time_start", this.time_start);
		treeMap.put("out_trade_no", this.out_trade_no);
		treeMap.put("total_fee", this.total_fee);
		treeMap.put("spbill_create_ip", this.spbill_create_ip);
		treeMap.put("trade_type", this.trade_type);
		treeMap.put("notify_url", this.notify_url);
		treeMap.put("openid", this.openid);
		StringBuilder sb = new StringBuilder();
		for (String key : treeMap.keySet())
		{
			sb.append(key).append("=").append(treeMap.get(key)).append("&");
		}
		sb.append("key=" + partnerKey);
		sign = MD5Util.MD5Encode(sb.toString(), "utf-8").toUpperCase();
		treeMap.put("sign", sign);
		StringBuilder xml = new StringBuilder();
		xml.append("<xml>\n");
		for (Map.Entry<String, String> entry : treeMap.entrySet())
		{
			if ("body".equals(entry.getKey()) || "sign".equals(entry.getKey()))
			{
				xml.append("<" + entry.getKey() + "><![CDATA[")
						.append(entry.getValue())
						.append("]]></" + entry.getKey() + ">\n");
			} else
			{
				xml.append("<" + entry.getKey() + ">").append(entry.getValue())
						.append("</" + entry.getKey() + ">\n");
			}
		}
		xml.append("</xml>");
		System.out.println(xml.toString());
		return xml.toString();
	}
	
	public String getPackageForUrlCode()
	{
		TreeMap<String, String> treeMap = new TreeMap<String, String>();
		treeMap.put("appid", this.appid);
		treeMap.put("mch_id", this.mch_id);
		treeMap.put("nonce_str", this.nonce_str);
		treeMap.put("body", this.body);
		treeMap.put("out_trade_no", this.out_trade_no);
		treeMap.put("total_fee", this.total_fee);
		treeMap.put("spbill_create_ip", this.spbill_create_ip);
		treeMap.put("trade_type", this.trade_type);
		treeMap.put("notify_url", this.notify_url);
		treeMap.put("openid", this.openid);
		StringBuilder sb = new StringBuilder();
		for (String key : treeMap.keySet())
		{
			sb.append(key).append("=").append(treeMap.get(key)).append("&");
		}
		sb.append("key=" + partnerKey);
		sign = MD5Util.MD5Encode(sb.toString(), "utf-8").toUpperCase();
		treeMap.put("sign", sign);
		StringBuilder xml = new StringBuilder();
		xml.append("<xml>\n");
		for (Map.Entry<String, String> entry : treeMap.entrySet())
		{
			if ("body".equals(entry.getKey()) || "sign".equals(entry.getKey()))
			{
				xml.append("<" + entry.getKey() + "><![CDATA[")
						.append(entry.getValue())
						.append("]]></" + entry.getKey() + ">\n");
			} else
			{
				xml.append("<" + entry.getKey() + ">").append(entry.getValue())
						.append("</" + entry.getKey() + ">\n");
			}
		}
		xml.append("</xml>");
		System.out.println(xml.toString());
		return xml.toString();
	}
	

	public String getAppid()
	{
		return appid;
	}

	public void setAppid(String appid)
	{
		this.appid = appid;
	}

	public String getMch_id()
	{
		return mch_id;
	}

	public void setMch_id(String mch_id)
	{
		this.mch_id = mch_id;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}

	public String getOut_trade_no()
	{
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no)
	{
		this.out_trade_no = out_trade_no;
	}

	public String getTotal_fee()
	{
		return total_fee;
	}

	public void setTotal_fee(String total_fee)
	{
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip()
	{
		return spbill_create_ip;
	}

	public void setSpbill_create_ip(String spbill_create_ip)
	{
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTrade_type()
	{
		return trade_type;
	}

	public void setTrade_type(String trade_type)
	{
		this.trade_type = trade_type;
	}

	public String getNotify_url()
	{
		return notify_url;
	}

	public void setNotify_url(String notify_url)
	{
		this.notify_url = notify_url;
	}

	public String getPartnerKey()
	{
		return partnerKey;
	}

	public void setPartnerKey(String partnerKey)
	{
		this.partnerKey = partnerKey;
	}


	public String getOpenid() {
		return openid;
	}


	public void setOpenid(String openid) {
		this.openid = openid;
	}


	public String getTime_expire() {
		return time_expire;
	}


	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}


	public String getTime_start() {
		return time_start;
	}


	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}
	

	
	
}
