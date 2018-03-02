package com.biz.service.basic;

import com.biz.model.Hmodel.basic.Tsales;
import com.biz.model.Pmodel.basic.Psales;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomchen on 17/2/6.
 */
@Service("salesService")
public class SalesServiceImpl extends BaseServiceImpl<Tsales> implements SalesServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Override
    public Pager<Psales> querySales(Pager<Psales> pager) throws Exception {
        return dao.findForPager1("salesDao.querySales", "salesDao.querySalesCount", pager);
    }

    @Override
    public List<Map<String, Object>> getShopsForSales(int identity, String identityCode) throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("identity",identity);
        params.put("identityCode",identityCode);

        return (List<Map<String, Object>>) dao.findForList("salesDao.getShopsForSales",params);
    }
}
