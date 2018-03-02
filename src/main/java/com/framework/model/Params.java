package com.framework.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页参数
 * Created by 刘佳佳 on 2016/7/25.
 */
public class Params {
    private String sort="";         //排序字段name
    private String identity_code="";         //排序字段name
    private String order="asc";     //排序方式
    private String searchtext="";   //搜索框字段
    private Map<String,Object> parm=new HashMap<>();
    private Integer page=0;         //当前页数
    private Integer rows=0;         //

    public Params(String sort, String order, String searchtext, Integer page, Integer rows) {
        this.sort = sort;
        this.order = order;
        this.searchtext = searchtext;
        this.page = page;
        this.rows = rows;
    }

    public Params() {
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getSearchtext() {
        return searchtext;
    }

    public void setSearchtext(String searchtext) {
        this.searchtext = searchtext;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Map<String, Object> getParm() {
        return parm;
    }

    public void setParm(Map<String, Object> parm) {
        this.parm = parm;
    }

    public String getIdentity_code() {
        return identity_code;
    }

    public void setIdentity_code(String identity_code) {
        this.identity_code = identity_code;
    }
}
