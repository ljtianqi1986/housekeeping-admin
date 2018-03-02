package com.biz.model.Pmodel.weixin;
  /*************************************************************************
 * 版本：         V1.0  
 *
 * 文件名称 ：UserInfo.java 描述说明 ：
 * 
 * 创建信息 : create by wanglun on 2015-11-28 下午4:14:03  修订信息 : modify by ( ) on (date) for ( )
 * 
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/

public class UserInfo {
	private String openid ="";
	private String nickname	="";//用户昵称
	private int sex	=0 ;//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
	private String province	="" ;//用户个人资料填写的省份
	private String city	="" ;//普通用户个人资料填写的城市
	private String country ="" ;//	国家，如中国为CN
	private String headimgurl	="" ;//用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
	private String privilege	="" ;//用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）
	private String unionid="";//只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。详见：获取用户个人信息（UnionID机制）
	private int subscribe = 0;//0:未关注1:已关注
	  private String id="";

	  public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public String getPrivilege() {
		return privilege;
	}
	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public int getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(int subscribe) {
		this.subscribe = subscribe;
	}

	  public String getId() {
		  return id;
	  }

	  public void setId(String id) {
		  this.id = id;
	  }
  }
