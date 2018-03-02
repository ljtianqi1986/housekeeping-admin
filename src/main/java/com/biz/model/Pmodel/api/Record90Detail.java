package com.biz.model.Pmodel.api;


public class Record90Detail
{
	private String create_time="";
	private String open_id="";
	private String receive_name="";   //领券人姓名
	private String clerk_name="";     //店员姓名
	private int point_90=0;           //券额
	private int source=0;             //类型
	
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
	public String getReceive_name() {
		return receive_name;
	}
	public void setReceive_name(String receive_name) {
		this.receive_name = receive_name;
	}
	public String getClerk_name() {
		return clerk_name;
	}
	public void setClerk_name(String clerk_name) {
		this.clerk_name = clerk_name;
	}
	public int getPoint_90() {
		return point_90;
	}
	public void setPoint_90(int point_90) {
		this.point_90 = point_90;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	
	
}
