package com.framework.model;

/**
 * 用户和密码（包含验证码）令牌类
 * Created by liujiajia on 2016/12/1.
 */
public class MyUsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken{
    private static final long serialVersionUID = 1L;

    private String captcha;
    private boolean mobileLogin;

    public MyUsernamePasswordToken() {
        super();
    }

    public MyUsernamePasswordToken(String username, char[] password,
                                   boolean rememberMe, String host, String captcha, boolean mobileLogin) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
        this.mobileLogin = mobileLogin;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public boolean isMobileLogin() {
        return mobileLogin;
    }
}
