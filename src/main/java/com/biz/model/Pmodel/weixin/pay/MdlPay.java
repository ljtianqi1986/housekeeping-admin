package com.biz.model.Pmodel.weixin.pay;


/**
 * 支付参数
 * @author GengLong
 *
 */
public class MdlPay
{
	// 微信号
	private String appId;
	// 商户id,如：100xxxxx
	private String partnerId;
	// 商户密钥 如：c9f0954adfa6481a3fd3d7d76
	private String partnerKey;
	
	//子账户
	private String sub_mch_id;
	//子账户
	private String sub_appid;

	
	
	public String getSub_mch_id() {
		return sub_mch_id;
	}

	public void setSub_mch_id(String sub_mch_id) {
		this.sub_mch_id = sub_mch_id;
	}

	public String getSub_appid() {
		return sub_appid;
	}

	public void setSub_appid(String sub_appid) {
		this.sub_appid = sub_appid;
	}

	public String getAppId()
	{
		return appId;
	}

	public void setAppId(String appId)
	{
		this.appId = appId;
	}

	public String getPartnerId()
	{
		return partnerId;
	}

	public void setPartnerId(String partnerId)
	{
		this.partnerId = partnerId;
	}

	public String getPartnerKey()
	{
		return partnerKey;
	}

	public void setPartnerKey(String partnerKey)
	{
		this.partnerKey = partnerKey;
	}

}
