package com.framework.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.framework.model.MessageBox;
import com.framework.model.SessionInfo;
import com.framework.utils.ConfigUtil;
import com.framework.utils.FastjsonFilter;
import com.framework.utils.URLConectionUtil;
import com.framework.utils.UuidUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.*;


public  class  BaseController implements Serializable
{
	private static final long serialVersionUID = 6357869213649815390L;
	protected Logger logger = Logger.getLogger(this.getClass());

//	private static final Logger logger = Logger.getLogger(BaseController.class);
	protected ModelAndView mv = this.getModelAndView();
	protected MessageBox box=this.getBox();
	public static Map<String,Object> cache = Collections.synchronizedMap( new HashMap<String,Object>());

	/**
	 * 获取ModelAndView
	 */
	public ModelAndView getModelAndView()
	{
		return new ModelAndView();
	}
	
	/**
	 * 获取消息盒子
	 * @return
	 */
	public MessageBox getBox(){
		return new MessageBox();
	}


	/**
	 * 得到request对象
	 */
	public HttpServletRequest getRequest()
	{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}
	
	
	
	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 * 
	 * @param object
	 * @param includesProperties
	 *            需要转换的属性
	 * @param excludesProperties
	 *            不需要转换的属性
	 */
	public void writeJsonByFilter(Object object, String[] includesProperties, String[] excludesProperties,HttpServletResponse response) {
		try {
			FastjsonFilter filter = new FastjsonFilter();// excludes优先于includes
			if (excludesProperties != null && excludesProperties.length > 0) {
				filter.getExcludes().addAll(Arrays.asList(excludesProperties));
			}
			if (includesProperties != null && includesProperties.length > 0) {
				filter.getIncludes().addAll(Arrays.asList(includesProperties));
			}
			logger.info("对象转JSON：要排除的属性[" + excludesProperties + "]要包含的属性[" + includesProperties + "]");
			String json;
			String User_Agent = getRequest().getHeader("User-Agent");
			if (StringUtils.indexOfIgnoreCase(User_Agent, "MSIE 6") > -1) {
				// 使用SerializerFeature.BrowserCompatible特性会把所有的中文都会序列化为\\uXXXX这种格式，字节数会多一些，但是能兼容IE6
				json = JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.BrowserCompatible);
			} else {
				// 使用SerializerFeature.WriteDateUseDateFormat特性来序列化日期格式的类型为yyyy-MM-dd hh24:mi:ss
				// 使用SerializerFeature.DisableCircularReferenceDetect特性关闭引用检测和生成
				json = JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);
			}
			json=json.trim();
			json=json.replace("\n", "");
			json=json.replace("\r", "");
			json=json.replace("\t", "");

			json=json.replaceAll("\n", "");
			json=json.replaceAll("\r", "");
			json=json.replaceAll("\t", "");
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}





	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 *
	 * @param object
	 * @param includesProperties
	 *            需要转换的属性
	 * @param excludesProperties
	 *            不需要转换的属性
	 */
	public void writeJsonByFilter2(Object object, String[] includesProperties, String[] excludesProperties,HttpServletResponse response) {
		try {
			FastjsonFilter filter = new FastjsonFilter();// excludes优先于includes
			if (excludesProperties != null && excludesProperties.length > 0) {
				filter.getExcludes().addAll(Arrays.asList(excludesProperties));
			}
			if (includesProperties != null && includesProperties.length > 0) {
				filter.getIncludes().addAll(Arrays.asList(includesProperties));
			}
			logger.info("对象转JSON：要排除的属性[" + excludesProperties + "]要包含的属性[" + includesProperties + "]");
			String json;
			String User_Agent = getRequest().getHeader("User-Agent");
			if (StringUtils.indexOfIgnoreCase(User_Agent, "MSIE 6") > -1) {
				// 使用SerializerFeature.BrowserCompatible特性会把所有的中文都会序列化为\\uXXXX这种格式，字节数会多一些，但是能兼容IE6
				json = JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.BrowserCompatible);
			} else {
				// 使用SerializerFeature.WriteDateUseDateFormat特性来序列化日期格式的类型为yyyy-MM-dd hh24:mi:ss
				// 使用SerializerFeature.DisableCircularReferenceDetect特性关闭引用检测和生成
				json = JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);
			}
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}





	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 * 
	 * @param object
	 * @throws IOException
	 */
	public void writeJson(Object object,HttpServletResponse response) {
		writeJsonByFilter(object, null, null,response);
	}

    public void writeJsonNoReplace(Object object,HttpServletResponse response) {
        writeJsonByFilter2(object, null, null,response);
    }
	/***
	 * 将对象转换成JSON字符串，并响应回前台(可排除不封装字段)
	 * @param object
	 * @param response
	 * @param str 不封装字段，字符串数组
	 */
	public void writeJson(Object object,HttpServletResponse response,String[] str) {
		writeJsonByFilter(object, null, str,response);
	}
	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 * @param object
	 * @param includesProperties
	 *            需要转换的属性
	 * @param excludesProperties
	 *            不需要转换的属性
	 */
	public void PrintJson(Object object, String[] includesProperties, String[] excludesProperties,HttpServletResponse response) {
		try {
			FastjsonFilter filter = new FastjsonFilter();// excludes优先于includes
			if (excludesProperties != null && excludesProperties.length > 0) {
				filter.getExcludes().addAll(Arrays.asList(excludesProperties));
			}
			if (includesProperties != null && includesProperties.length > 0) {
				filter.getIncludes().addAll(Arrays.asList(includesProperties));
			}
			String json;
			String User_Agent = getRequest().getHeader("User-Agent");
			if (StringUtils.indexOfIgnoreCase(User_Agent, "MSIE 6") > -1) {
				// 使用SerializerFeature.BrowserCompatible特性会把所有的中文都会序列化为\\uXXXX这种格式，字节数会多一些，但是能兼容IE6
				json = JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.BrowserCompatible);
			} else {
				// 使用SerializerFeature.WriteDateUseDateFormat特性来序列化日期格式的类型为yyyy-MM-dd hh24:mi:ss
				// 使用SerializerFeature.DisableCircularReferenceDetect特性关闭引用检测和生成
				json = JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);
			}
			System.out.println("转换后的JSON字符串：" + json);
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(json);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将对象转换成JSON字符串，并在控制台打印以便调试
	 * @param object
	 * @throws IOException
	 */
	public void PrintJson(Object object,HttpServletResponse response) {
		PrintJson(object, null, null,response);
	}

	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 * @param object
	 * @param includesProperties
	 *            需要转换的属性
	 */
	public void writeJsonByIncludesProperties(Object object, String[] includesProperties) {
		writeJsonByFilter(object, includesProperties, null,null);
	}

	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 * @param object
	 * @param excludesProperties
	 *            不需要转换的属性
	 */
	public void writeJsonByExcludesProperties(Object object, String[] excludesProperties) {
		writeJsonByFilter(object, null, excludesProperties,null);
	}

	public SessionInfo getLocalSessionInfo(){
		 SessionInfo sessionInfo=(SessionInfo) getRequest().getSession().getAttribute(ConfigUtil.getSessionInfoName());
		 return sessionInfo;
	}
	
	
	public HttpSession getSession(){
		return getRequest().getSession();
	}

    public Session getShiroSession(){
		return SecurityUtils.getSubject().getSession();
    }

    public void setShiroAttribute(String key,Object value){getShiroSession().setAttribute(key,value);}
    public Object getShiroAttribute(String key){return getShiroSession().getAttribute(key);}
	public String getRndCode()
	{
		return UuidUtil.get32UUID();
	}

	public Map getParameterByRequest(HttpServletRequest request)
	{
		Map map = new HashMap();
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);
			if (paramValues.length == 1) {
				String paramValue = paramValues[0];
				if (paramValue.length() != 0) {
					map.put(paramName, paramValue);
				}
			}
		}
		return map;
	}

    ///**
    // * 得到PageData
    // */
    //public PageData getPageData()
    //{
    //    mv.clear();
    //    PageData pds = new PageData();
    //    pds = new PageData(this.getRequest());
    //
    //    pds.put("SYS_NAME",); // 读取系统名称
    //
    //    mv.addObject("pdm", pds);
    //    return pds;
    //}

//	/**
//	 * 得到随机数
//	 *
//	 * @return
//	 */
//	public String getRndCode()
//	{
//		return UuidUtil.get32UUID();
//	}


	/**
	 * 字符元--转为整数分
	 */
	public double money_convert(String money){
		int int_m = money_convert_Int(money);
		double back_money = Double.valueOf(int_m);
		return back_money;
	}

	/**
	 * 字符元--转为int分
	 */
	public int money_convert_Int(String moneyStr){
		int result = 0;
		String strAdd = "";
		if (!moneyStr.contains("."))
		{
			strAdd = ".00";
		} else if (".".equals(moneyStr.substring(moneyStr.length() - 1)))
		{
			strAdd = "00";
		} else if (".".equals(moneyStr.substring(moneyStr.length() - 2,moneyStr.length() - 1)))
		{
			strAdd = "0";
		}
		moneyStr = moneyStr + strAdd;
		result =Integer.parseInt(moneyStr.replace(".", ""));
		return result;
	}



	/**
	 * 远程连接HttpURL
	 * @param url
	 * @return
	 */
	protected String HttpURL(String url){
		String result = "";
		try {
			URL localURL = new URL(url);
			String async = "";
			URLConnection connection = localURL.openConnection();
			HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

			httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			httpURLConnection.setDoOutput(true);

			InputStream inputStream = null;
			InputStreamReader inputStreamReader = null;
			BufferedReader reader = null;
			StringBuffer resultBuffer = new StringBuffer();
			String tempLine = null;

			if (httpURLConnection.getResponseCode() >= 300)
			{
				throw new Exception(
						"HTTP Request is not success, Response code is "
								+ httpURLConnection.getResponseCode());
			}
			try
			{
				inputStream = httpURLConnection.getInputStream();
				inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
				reader = new BufferedReader(inputStreamReader);
				String asynckl = "";
				while ((tempLine = reader.readLine()) != null)
				{
					resultBuffer.append(tempLine);
				}

			} finally
			{

				if (reader != null)
				{
					reader.close();
				}

				if (inputStreamReader != null)
				{
					inputStreamReader.close();
				}

				if (inputStream != null)
				{
					inputStream.close();
				}

			}
			result = resultBuffer.toString();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("=远程连接HttpURL报错==" ,e.fillInStackTrace());
			return result;
		}
	}

	//接口
	public Map<String, Object> callQTInterface(Map<String,String> jSONObject,String url){
		Map<String, Object> map2=new HashMap<String, Object>();
		try {
			String requestUrl = ConfigUtil.get("QT_Url")+url;
			String x= URLConectionUtil.httpURLConnectionPostDiy(requestUrl,jSONObject);
			x = URLDecoder.decode(x, "utf-8");
			if(x==null||x.trim().equals("")){
				// 失败
			}else {
				map2 = JSON.parseObject(x, Map.class);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		finally {
			return map2;
		}
	}

}
