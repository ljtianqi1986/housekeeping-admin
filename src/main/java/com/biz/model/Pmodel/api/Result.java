package com.biz.model.Pmodel.api;

public class Result
{
	private int return_code = 0;//0错误 1成功
	private String return_info = "";
	private String qrcode_url = "";
	private String mainId = "";
	private Object return_data;

	public int getReturn_code()
	{
		return return_code;
	}
	public void setReturn_code(int return_code)
	{
		this.return_code = return_code;
	}
	public String getReturn_info()
	{
		return return_info;
	}
	public void setReturn_info(String return_info)
	{
		this.return_info = return_info;
	}

	public Object getReturn_data() {
		return return_data;
	}

	public void setReturn_data(Object return_data) {
		this.return_data = return_data;
	}

	public String getQrcode_url() {
		return qrcode_url;
	}

	public void setQrcode_url(String qrcode_url) {
		this.qrcode_url = qrcode_url;
	}

	public String getMainId() {
		return mainId;
	}

	public void setMainId(String mainId) {
		this.mainId = mainId;
	}
}
