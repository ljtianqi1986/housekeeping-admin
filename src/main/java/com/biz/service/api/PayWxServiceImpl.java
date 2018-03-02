package com.biz.service.api;

import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * 支付相关业务
 *
 * Created by lzq on 2017/2/5.
 */

    @Service("payWxService")
    public class PayWxServiceImpl extends BaseServiceImpl implements PayWxServiceI {

        @Resource(name = "daoSupport")
        private DaoSupport dao;

}
