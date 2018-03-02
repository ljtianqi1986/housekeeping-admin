package com.framework.utils.weixin;

import com.biz.model.Pmodel.weixin.AccessToken;
import com.biz.model.Pmodel.weixin.OAuthAccessToken;
import com.biz.model.Pmodel.weixin.UserInfo;
import com.framework.utils.ConfigUtil;
import com.framework.utils.EmojiFilter;
import com.framework.utils.Tools;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.util.Date;


/**
 * 公众平台通用接口工具类
 * 
 */
public class WechatAccessToken
{
	// 获取微信公众号：access_token的接口地址（GET） 限2000（次/天）
	public final static String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	// 获取企业号access_token
	public final static String oauth_access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	public final static String get_user_info = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	public final static String get_user_is_join = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	/**
	 * 获取js_access_token
	 * @param appid
	 * @param appsecret
	 * @param code
	 * @return
	 */
	public static OAuthAccessToken getAccessToken(String appid, String appsecret,String code)
	{
		OAuthAccessToken accessToken = null;
		String requestUrl = oauth_access_token_url.replace("APPID", appid).replace(
				"SECRET", appsecret).replace("CODE", code);
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
				accessToken = new OAuthAccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
				accessToken.setOpenid(jsonObject.getString("openid"));
			} catch (JSONException e)
			{
				accessToken = null;
				// 获取token失败
			}
		}
		return accessToken;
	}
	
	/**
	 * 获取access_token
	 * 
	 * @param appid
	 *            凭证
	 * @param appsecret
	 *            密钥
	 * @return
	 */
	public static AccessToken getAccessToken(String appid, String appsecret)
	{
		AccessToken accessToken = null;
		String requestUrl = access_token_url.replace("APPID", appid).replace(
				"APPSECRET", appsecret);
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
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e)
			{
				accessToken = null;
				// 获取token失败
			}
		}
		return accessToken;
	}
	/**
	 * 获取用户详细信息
	 * @param access_token
	 * @param openid
	 * @return
	 */
	public static UserInfo getUserInfo(String access_token, String openid)
	{
		if(access_token==null || access_token=="")
		{
			return null;
		}
		UserInfo userInfo = null;
		String requestUrl = get_user_info.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
		//System.out.println("**************************************************************************************");
		//System.out.println(requestUrl);
		JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl,
				EnumMethod.GET.name(), null);
		if (jsonObject == null)
		{
			jsonObject = HttpRequestUtil.httpRequest(requestUrl,
					EnumMethod.GET.name(), null);
		}
//		System.out.println(jsonObject.getString("openid"));
		// 如果请求成功
		if (null != jsonObject)
		{
			try
			{
				userInfo = new UserInfo();
				userInfo.setCity(jsonObject.getString("city"));
				userInfo.setCountry(jsonObject.getString("country"));
				userInfo.setHeadimgurl(jsonObject.getString("headimgurl"));
                String ss=EmojiFilter.filterEmoji2(jsonObject.getString("nickname"));
                ss= EmojiFilter.filterEmoji(ss);//过滤
				userInfo.setNickname(ss);
				userInfo.setOpenid(jsonObject.getString("openid"));
				//userInfo.setPrivilege(jsonObject.getString("privilege"));
				userInfo.setProvince(jsonObject.getString("province"));
				userInfo.setSex(jsonObject.getInt("sex"));
//				userInfo.setSubscribe(jsonObject.getInt("subscribe"));
//				userInfo.setUnionid(jsonObject.getString("unionid"));
                if(jsonObject.containsKey("unionid")){
                    userInfo.setUnionid(jsonObject.getString("unionid"));
                }
			} catch (JSONException e)
			{
				userInfo = null;
				// 获取token失败
			}
		}
		System.out.println("**************************************************************************************");
		return userInfo;
	}

	/**
	 * 获取AccessToken
	 *
	 * @return
	 */
	public static String getOldAccessToken()
	{
		String newAccessToken = "";
		String accessTokenStr = Tools.readTxtFile(ConfigUtil.get("ACCESS_TOKEN"));
		if ("".equals(accessTokenStr))
		{
			System.out
					.println("################################################");
			System.out.println("载入token已超时，重新载入");
			System.out
					.println("################################################");
			newAccessToken = reloadOldAccessToken().getToken();
		}
		else
		{
			String[] accessTokenArg = accessTokenStr.split(",");
			long nowTime = new Date().getTime();
			long putTime = Long.parseLong(accessTokenArg[1]);
			long diffTime = Long.parseLong(accessTokenArg[2]);
			if (Math.abs(nowTime - putTime) >= diffTime)
			{
				System.out
						.println("################################################");
				System.out.println("载入token已超时，重新载入");
				System.out
						.println("################################################");
				newAccessToken = reloadOldAccessToken().getToken();
			} else
			{
				newAccessToken = accessTokenArg[0];
			}
		}
		return newAccessToken;
	}

	public static AccessToken reloadOldAccessToken()
	{
		AccessToken accessToken = getOldAccessToken(ConfigUtil.get("APPID"),
				ConfigUtil.get("APPSECRET"));
		Tools.writeFile(ConfigUtil.get("ACCESS_TOKEN"), accessToken.getToken() + ","
				+ new Date().getTime() + "," + (accessToken.getExpiresIn() * 1000));
		return accessToken;
	}

	/**
	 * 获取access_token
	 *
	 * @param appid
	 *            凭证
	 * @param appsecret
	 *            密钥
	 * @return
	 */
	public static AccessToken getOldAccessToken(String appid, String appsecret)
	{
		AccessToken accessToken = null;
		String requestUrl = access_token_url.replace("APPID", appid).replace(
				"APPSECRET", appsecret);
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
				accessToken = new AccessToken();
				accessToken.setToken(jsonObject.getString("access_token"));
				accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
			} catch (JSONException e)
			{
				accessToken = null;
				// 获取token失败
			}
		}
		return accessToken;
	}

	/**
	 * 获取用户详细信息
	 * @param openid
	 * @return
	 */
	public static UserInfo getUserInfo(String openid)
	{
		UserInfo userInfo = null;
		String access_token = getOldAccessToken();
		String requestUrl = get_user_is_join.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
		System.out.println("**************************************************************************************");
		System.out.println(requestUrl);
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
			userInfo = new UserInfo();
			try
			{
				userInfo.setHeadimgurl(jsonObject.getString("headimgurl"));
			}
			catch (Exception e)
			{
				userInfo.setHeadimgurl("");
			}
			try
			{
				userInfo.setNickname(jsonObject.getString("nickname"));
			}
			catch (Exception e)
			{
				userInfo.setNickname("");
			}
			try
			{
				userInfo.setOpenid(jsonObject.getString("openid"));
			}
			catch (Exception e)
			{
				userInfo.setOpenid(openid);
			}
			try
			{//国家，如中国为CN
				userInfo.setCountry(jsonObject.getString("country"));
			}
			catch (Exception e)
			{
				userInfo.setCountry("");
			}
			try
			{//用户个人资料填写的省份
				userInfo.setProvince(jsonObject.getString("province"));
			}
			catch (Exception e)
			{
				userInfo.setProvince("");
			}
			try
			{//普通用户个人资料填写的城市
				userInfo.setCity(jsonObject.getString("city"));
			}
			catch (Exception e)
			{
				userInfo.setCity("");
			}
			try
			{
				userInfo.setSex(jsonObject.getInt("sex"));
			}
			catch (Exception e)
			{
				userInfo.setSex(0);
			}
			try
			{
				userInfo.setSubscribe(jsonObject.getInt("subscribe"));
			}
			catch (Exception e)
			{
				userInfo.setSubscribe(1);
			}
			userInfo.setCity("");
			userInfo.setCountry("");
			userInfo.setPrivilege("");
			userInfo.setProvince("");
		}
		System.out.println("**************************************************************************************");
		return userInfo;
	}
	
}