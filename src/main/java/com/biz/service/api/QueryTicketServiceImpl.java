package com.biz.service.api;

import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Pmodel.api.DetailVoucher;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by xy on 2017/2/24.
 */

@Service("queryTicketService")
public class QueryTicketServiceImpl extends BaseServiceImpl<TorderMain> implements QueryTicketServiceI {
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Override
    public Map<String, Object> queryTicketInfoByOrderId(String id) throws Exception {
       return (Map<String,Object>) dao.findForObject("QTTicketDao.queryTicketInfoByOrderId",id);
    }

    @Override
    public void insertDetailVoucher(DetailVoucher detailVoucher) throws Exception {
        dao.save("QTTicketDao.insertDetailVoucher", detailVoucher);
    }

    @Override
    public List<Map<String, Object>> queryVoucherDetail(Map<String, Object> mapParam) throws Exception {
        return (List<Map<String, Object>>) dao.findForList("QTTicketDao.queryVoucherDetail",mapParam);
    }

    @Override
    public int queryVoucherDetailCount(Map<String, Object> mapParam) throws Exception {
        return (int) dao.findForObject("QTTicketDao.queryVoucherDetailCount",mapParam);
    }
}