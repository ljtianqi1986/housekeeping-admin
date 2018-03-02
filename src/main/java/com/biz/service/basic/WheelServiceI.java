package com.biz.service.basic;

import com.biz.model.Hmodel.basic.TwxWheelMain;
import com.biz.model.Pmodel.basic.PwxWheel;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Paging;
import com.framework.model.Params;

import java.util.Map;

/**
 * 商户Service
 * Created by liujiajia on 2017/1/6.
 */
public interface WheelServiceI extends BaseServiceI<TwxWheelMain> {

    Paging findWheelRecordGrid(Params sqlParams)throws Exception;

    Map<String,Object> getWheelInfo()throws Exception;

    Map<String,Object> getFreightSetting()throws Exception;

    void updateMain(String id, String state, String qishu);

    void saveWheel(PwxWheel pwxWheel)throws Exception;
    void updateFreight(Map<String,Object> map)throws Exception;

    Paging findWheelRecordForLevelGrid(Params sqlParams)throws Exception;

    void updateGetGoods(String id);
}
