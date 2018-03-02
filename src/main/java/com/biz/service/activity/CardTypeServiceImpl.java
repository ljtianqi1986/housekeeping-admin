package com.biz.service.activity;

import com.biz.model.Hmodel.TCardType;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by tomchen on 17/2/4.
 */
@Service("cardTypeService")
public class CardTypeServiceImpl extends BaseServiceImpl<TCardType> implements CardTypeServiceI{

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Override
    public Pager<Map<String, Object>> queryCardTypes(Pager<Map<String, Object>> pager) throws Exception {
        return dao.findForPager1("cardTypeDao.findOfflineCardTypePage", "cardTypeDao.findOfflineCardTypeCount", pager);
    }
}
