package com.biz.model.Pmodel.weixin;

/**
 * 微信授权信息
 */
public class WeChatInfo {
	/**
	 * 授权方appid
	 */
	private String appid;
	/**
	 * 原始id
	 */
	private String origin_id;
    /**
     * 授权code
     */
    private String authorization_code = "";
	/**
	 * 刷新令牌
	 */
	private String refresh_token = "";

	/**
	 * 删除标志0：未删除，1：删除
	 */
	private int isdel = 0;
	/**
	 * 创建时间
	 */
	private String createTime;

    private String end_time="0";//失效时间

    private String access_token;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}


	public String getOrigin_id() {
		return origin_id;
	}

	public void setOrigin_id(String origin_id) {
		this.origin_id = origin_id;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}


	public int getIsdel() {
		return isdel;
	}

	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAuthorization_code() {
        return authorization_code;
    }

    public void setAuthorization_code(String authorization_code) {
        this.authorization_code = authorization_code;
    }

}
