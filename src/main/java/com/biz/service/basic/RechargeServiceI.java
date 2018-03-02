package com.biz.service.basic;

import com.biz.model.Pmodel.basic.JiuLingCoinHistory;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Pager;

import java.util.List;
import java.util.Map;

/**
 * Created by tomchen on 17/1/7.
 */
public interface RechargeServiceI extends BaseServiceI<JiuLingCoinHistory>{

    Pager<JiuLingCoinHistory> queryJiuLingCoinList(Pager pager) throws Exception;

    List<Map<String,String>> queryUserListForJiuLingCoin() throws Exception;

    Map<String,Object> checkJiulingUser(JiuLingCoinHistory jiuLingCoin_history) throws Exception;

    boolean addTargetJiulingCoin(JiuLingCoinHistory jiuLingCoin_history) throws Exception;

    boolean insertJiuLingHistory(JiuLingCoinHistory jiuLingCoin_history) throws Exception;

    /**
     * 查询充值90贝
     * @param map
     * @return
     */
    Map<String,String> loadRCInfo(Map<String, String> map)throws Exception;
}
