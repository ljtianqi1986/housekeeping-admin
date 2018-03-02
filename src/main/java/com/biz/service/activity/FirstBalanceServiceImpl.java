package com.biz.service.activity;

import com.biz.model.Pmodel.PfirstBalance;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.Tools;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 商圈
 * @author caoyu
 * 
 */
@Service("firstBalanceService")
public class FirstBalanceServiceImpl extends BaseServiceImpl implements FirstBalanceServiceI{
	@Resource(name = "daoSupport")
	private DaoSupport dao;


    //新增活动
    @Override
    public boolean addFirstBalance(PfirstBalance firstBalance) throws Exception{
        if(Tools.isEmpty(firstBalance.getActivityId()) ){
            return false;
        }else{
            return (Integer)dao.save("FirstBalanceDao.insertFirstBalance",firstBalance)>0;
        }
    }

    @Override
    public boolean updateFirstBalance(PfirstBalance firstBalance) throws Exception{
        if(Tools.isEmpty(firstBalance.getActivityId()) ){
            return false;
        }else{
            return (Integer)dao.save("FirstBalanceDao.updateFirstBalance",firstBalance)>0;
        }
    }

    //更具获得id 查询具体活动
    @Override
    public PfirstBalance selectFirstBalance(String Id) throws Exception{
        Map<String,Object> selectmap=new HashMap<>();
        selectmap.put("id",Id);
        return (PfirstBalance)dao.findForObject("FirstBalanceDao.getfirstBalanceList",selectmap);
    }

	/**
	 * 查询 最新记录
	 *
	 * @return
	 * @throws Exception
	 */
    @Override
    public PfirstBalance queryfirstBalance() throws Exception{
        return (PfirstBalance) dao.findForObject("FirstBalanceDao.getfirstBalanceMapList", null);
    }



}
