package com.biz.service.basic;

import com.biz.model.Hmodel.TSysLog;
import com.biz.model.Pmodel.PSysLog;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Pager;

import java.util.List;

/**
 * Created by tomchen on 17/1/7.
 */
public interface LogServiceI extends BaseServiceI<TSysLog>{

    Pager<PSysLog> queryLogs(Pager pager) throws Exception;

}
