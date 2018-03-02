package com.biz.model.Pmodel.basic;

/**
*商圈管理
*/
public class Pdistrict {
	/**
	 * 商圈主键
	 */
    private String district_code;
	/**
	 * 商圈名称
	 */
    private String district_name;
	/**
	 * 所属地区
	 */
    private String area_code;
	/**
	 * 代理商主键code
	 */
    private String agent_code;
	/**
	 * 删除标志0：未删除，1：删除
	 */
    private int isdel;
	/**
	 * 创建时间
	 */
    private String create_time;
	
    //*********扩展字段**************//
	private String area_name;

	public String getDistrict_code() {
		return district_code;
	}

	public void setDistrict_code(String district_code) {
		this.district_code = district_code;
	}

	public String getDistrict_name() {
		return district_name;
	}

	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}

	public String getArea_code() {
		return area_code;
	}

	public void setArea_code(String area_code) {
		this.area_code = area_code;
	}

	public String getAgent_code() {
		return agent_code;
	}

	public void setAgent_code(String agent_code) {
		this.agent_code = agent_code;
	}

	public int getIsdel() {
		return isdel;
	}

	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}
	
		
}
