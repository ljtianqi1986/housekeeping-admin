package com.biz.service.basic;

import com.biz.model.Hmodel.basic.Tagent;
import com.biz.model.Pmodel.basic.Pmessage;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import com.framework.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by tomchen on 17/1/9.
 */
@Service("messageService1")
public class MessageServiceImpl implements MessageServiceI{

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Override
    public Pager<Pmessage> queryMessages(Pager pager) throws Exception {
        return dao.findForPager1("messageDao.queryMessages","messageDao.queryMessagesCount",pager);
    }

    @Override
    public void updateState(String ids, String state) throws Exception {
        Pmessage pmessage=new Pmessage();
        pmessage.setUpdateId(StringUtil.formatSqlIn(ids));
        pmessage.setState(Integer.valueOf(state));
        dao.update("messageDao.updateState",pmessage);
    }

    @Override
    public Pager<Pmessage> showSignDetail(Pager pager) throws Exception {
        return dao.findForPager1("messageDao.showSignDetail","messageDao.showSignDetailCount",pager);
    }
}
