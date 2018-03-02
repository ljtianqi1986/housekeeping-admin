package com.framework.model;

import java.util.ArrayList;
import java.util.List;


/**
 * EasyUI DataGrid模型
 * 
 * @author 刘佳佳
 * 
 */
public class Paging<T> implements java.io.Serializable {

	private static final long serialVersionUID = -1244005288227801320L;
	private Long total = 0L;
	private List<T> rows = new ArrayList<T>();

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

}
