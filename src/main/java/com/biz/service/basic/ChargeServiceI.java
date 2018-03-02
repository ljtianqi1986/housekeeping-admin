package com.biz.service.basic;

import com.biz.model.Hmodel.TChargeCoinDiary;
import com.biz.model.Pmodel.PChargeCoinDiary;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Pager;

/**
 * Created by tomchen on 17/1/7.
 */
public interface ChargeServiceI extends BaseServiceI<TChargeCoinDiary>{


    Pager<PChargeCoinDiary> queryJiulingCoinChargeDiaryPager(Pager<PChargeCoinDiary> pager) throws Exception;

}
