package com.biz.service.basic;

import com.biz.model.Hmodel.TChargeCoinDiary;
import com.biz.model.Pmodel.PChargeCoinDiary;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by tomchen on 17/1/7.
 */
@Service("chargeService")
public class ChargeServiceImpl extends BaseServiceImpl<TChargeCoinDiary> implements ChargeServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;


    @Override
    public Pager<PChargeCoinDiary> queryJiulingCoinChargeDiaryPager(Pager<PChargeCoinDiary> pager) throws Exception {
        return dao.findForPager("chargeDao.queryJiulingCoinChargeDiary","chargeDao.queryJiulingCoinChargeDiaryCount",pager);
    }
}
