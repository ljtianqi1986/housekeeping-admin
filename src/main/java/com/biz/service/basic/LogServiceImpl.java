package com.biz.service.basic;

import com.biz.model.Hmodel.TSysLog;
import com.biz.model.Pmodel.PSysLog;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by tomchen on 17/1/7.
 */
@Service("logService")
public class LogServiceImpl extends BaseServiceImpl<TSysLog> implements LogServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Override
    public Pager<PSysLog> queryLogs(Pager pager) throws Exception {
        return dao.findForPager1("logDao.queryLogs","logDao.queryLogsCount",pager);
    }
}
