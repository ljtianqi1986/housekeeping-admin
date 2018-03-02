package com.biz.model.Pmodel.task;


import java.util.Map;

/**
 * Created by qziwm on 2017/5/15.
 */
public class PorderServerStatistics  {
   private  String flag;
    private  String msg;
    private Map<String,Object> data;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
