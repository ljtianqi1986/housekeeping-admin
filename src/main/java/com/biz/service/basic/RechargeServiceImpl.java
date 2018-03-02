package com.biz.service.basic;

import com.biz.model.Pmodel.basic.JiuLingCoinHistory;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import com.framework.utils.StringUtil;
import com.framework.utils.UuidUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by tomchen on 17/1/7.
 */
@Service("rechargeService")
public class RechargeServiceImpl extends BaseServiceImpl<JiuLingCoinHistory> implements RechargeServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Override
    public Pager<JiuLingCoinHistory> queryJiuLingCoinList(Pager pager) throws Exception {
        return dao.findForPager1("rechargeDao.queryJiuLingCoinListForPage","rechargeDao.queryJiulingCoinCount",pager);
    }

    @Override
    public List<Map<String,String>> queryUserListForJiuLingCoin() throws Exception {
        return (List<Map<String,String>>)dao.findForList("userDao.queryUserListForJiuLingCoin",null);
    }

    //@Override
    //public String checkJiulingUser(JiuLingCoinHistory jiuLingCoin_history) {
    //    String userId="";
    //    try {
    //        if (!StringUtil.isNullOrEmpty(jiuLingCoin_history.getTargetUser())){
    //            //1.加载目标客户userid
    //            String uid=jiuLingCoin_history.getTargetUser();
    //            //2.查询目标客户是否存在
    //            userId=(String) dao.findForObject("rechargeDao.queryJiulingCoin",jiuLingCoin_history);
    //            if (StringUtils.isBlank(userId)){
    //                Map<String,Object> map=new HashedMap();
    //                map.put("id", UuidUtil.get32UUID());
    //                map.put("targetUser",uid);
    //                dao.save("rechargeDao.insertJiulingCoin", map);
    //                userId=map.get("id").toString();
    //            }
    //
    //        }
    //
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    //    return userId;
    //}

    @Override
    public Map<String,Object> checkJiulingUser(JiuLingCoinHistory jiuLingCoin_history)throws Exception {
        return (Map<String, Object>) dao.findForObject("rechargeDao.checkJiulingUser",jiuLingCoin_history);
    }

    @Override
    public boolean addTargetJiulingCoin(JiuLingCoinHistory jiuLingCoin_history) throws Exception{
        return (Integer)dao.update("rechargeDao.addTargetJiulingCoin",jiuLingCoin_history)>0;
    }

    @Override
    public boolean insertJiuLingHistory(JiuLingCoinHistory jiuLingCoin_history) throws Exception{
        return (Integer)dao.save("rechargeDao.insertJiuLingHistory",jiuLingCoin_history)>0;
    }

    @Override
    public Map<String, String> loadRCInfo(Map<String, String> map) throws Exception{
        return (Map<String, String>)dao.findForObject("rechargeDao.loadRCInfo",map);
    }

}
