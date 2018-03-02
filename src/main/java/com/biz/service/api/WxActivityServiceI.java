package com.biz.service.api;

import com.biz.model.Pmodel.api.*;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Pager;

import java.util.List;
import java.util.Map;

/**
 * Created by lzq on 2017/2/4.
 */
public interface WxActivityServiceI extends BaseServiceI<Object> {

    String adActivityListDate(String openid,String sceneType,String appid) throws Exception;

    String doWxscan(String openId,String eventKey,String appid)throws Exception;

    String findPayMsg()throws Exception;

    Map<String,Object> messageJson(String openId)throws Exception;

    String messageXml(String openId,String appId)throws Exception;
}
