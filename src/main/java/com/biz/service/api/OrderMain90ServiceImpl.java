package com.biz.service.api;


import com.biz.model.Hmodel.api.TOrderMain90;
import com.biz.model.Hmodel.basic.Tagent;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by liujiajia on 2017/1/6.
 */
@Service("orderMain90Service")
public class OrderMain90ServiceImpl extends BaseServiceImpl<TOrderMain90> implements OrderMain90ServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Autowired
    private BaseDaoI<Tagent> agentDao;


}
