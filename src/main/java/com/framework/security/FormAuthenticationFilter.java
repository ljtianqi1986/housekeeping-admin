/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.framework.security;

import com.framework.utils.AjaxJson;
import com.framework.utils.PrintJSON;
import com.framework.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 表单验证（包含验证码）过滤类
 * @author jeeplus
 * @version 2014-5-19
 */
@Service
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

	public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";
	public static final String DEFAULT_MOBILE_PARAM = "mobileLogin";
	public static final String DEFAULT_MESSAGE_PARAM = "message";

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;
	private String mobileLoginParam = DEFAULT_MOBILE_PARAM;
	private String messageParam = DEFAULT_MESSAGE_PARAM;

	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);
		if (password==null){
			password = "";
		}
		boolean rememberMe = isRememberMe(request);
		String host = StringUtil.getRemoteAddr((HttpServletRequest)request);
		String captcha = getCaptcha(request);
		boolean mobile = isMobileLogin(request);
		return new UsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha, mobile);
	}

	public String getCaptchaParam() {
		return captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}

	public String getMobileLoginParam() {
		return mobileLoginParam;
	}
	
	protected boolean isMobileLogin(ServletRequest request) {
        return WebUtils.isTrue(request, getMobileLoginParam());
    }
	
	public String getMessageParam() {
		return messageParam;
	}
	
	/**
	 * 登录成功之后跳转URL
	 */
	public String getSuccessUrl() {
		return super.getSuccessUrl();
	}
	
	@Override
	protected void issueSuccessRedirect(ServletRequest request,
			ServletResponse response) throws Exception {
		SystemAuthorizingRealm.Principal p = getPrincipal();
		if (p != null && !p.isMobileLogin()){
			 WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
		}else{
			//super.issueSuccessRedirect(request, response);//手机登录
			AjaxJson j = new AjaxJson();
			j.setSuccess(true);
			j.setMsg("登录成功!");
			j.put("username", p.getLoginName());
			j.put("name", p.getName());
			j.put("mobileLogin", p.isMobileLogin());
			j.put("JSESSIONID", p.getSessionid());
			PrintJSON.write((HttpServletResponse)response, j.getJsonStr());
		}
	}

	/**
	 * 获取当前登录者对象
	 */
	public static SystemAuthorizingRealm.Principal getPrincipal(){
		try{
			Subject subject = SecurityUtils.getSubject();
			SystemAuthorizingRealm.Principal principal = (SystemAuthorizingRealm.Principal)subject.getPrincipal();
			if (principal != null){
				return principal;
			}
//			subject.logout();
		}catch (UnavailableSecurityManagerException e) {

		}catch (InvalidSessionException e){

		}
		return null;
	}

	/**
	 * 登录失败调用事件
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token,AuthenticationException e, ServletRequest request, ServletResponse response){
		String className = e.getClass().getName(), message = "";
		if (IncorrectCredentialsException.class.getName().equals(className)
				|| UnknownAccountException.class.getName().equals(className)){
			message = "用户或密码错误, 请重试.";
		}
		else {
			if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")) {
				message = StringUtils.replace(e.getMessage(), "msg:", "");
			} else {
				message = "系统出现点问题，请稍后再试！";
				e.printStackTrace(); // 输出到控制台
			}
		}
        request.setAttribute(getFailureKeyAttribute(), className);
        request.setAttribute(getMessageParam(), message);
        return true;
	}
	
}