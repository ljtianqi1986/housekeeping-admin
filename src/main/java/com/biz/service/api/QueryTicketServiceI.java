package com.biz.service.api;

import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Pmodel.api.DetailVoucher;
import com.biz.service.base.BaseServiceI;

import java.util.List;
import java.util.Map;

/**
 * Created by xy on 2017/2/24.
 */
public interface QueryTicketServiceI extends BaseServiceI<TorderMain> {

    Map<String,Object> queryTicketInfoByOrderId(String id) throws Exception;
//商家消券
    void insertDetailVoucher(DetailVoucher detailVoucher)throws Exception;

    List<Map<String,Object>> queryVoucherDetail(Map<String, Object> mapParam)throws Exception;

    int queryVoucherDetailCount(Map<String, Object> mapParam)throws Exception;
}
