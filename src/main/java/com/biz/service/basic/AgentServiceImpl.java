package com.biz.service.basic;


import com.biz.model.Hmodel.basic.Tagent;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.OrderMain90;
import com.biz.model.Pmodel.basic.Pagent;
import com.biz.model.Pmodel.basic.PagentTree;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Paging;
import com.framework.model.Params;
import com.framework.utils.MD5;
import com.framework.utils.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by liujiajia on 2017/1/6.
 */
@Service("agentService")
public class AgentServiceImpl extends BaseServiceImpl<Tagent> implements AgentServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Autowired
    private BaseDaoI<Tagent> agentDao;

    @Override
    public Paging findAgentGrid(Params sqlParams) {
        Paging paging= dao.findForPagings("agentMapper.findAgentGrid",sqlParams,"agentMapper.findAgentCount",sqlParams);
        return paging;
    }

    @Override
    public List<PagentTree> getAgentListByUser(User user) throws Exception {
        return (List<PagentTree>) dao.findForList("agentMapper.getAgentListByUser",null);
    }

    @Override
    public void saveAgent(Pagent pagent) throws Exception {
        Tagent ag=new Tagent();
        BeanUtils.copyProperties(pagent,ag, "isdel","create_time","type","sort");
        ag.setSort(Integer.valueOf(pagent.getSort()));
        agentDao.save(ag);
        pagent.setPwd(MD5.md5(pagent.getPwd()));
        pagent.setAgent_code(ag.getAgent_code());
        pagent.setUserCode(UUID.randomUUID().toString().replace("-", ""));
        dao.save("userDao.saveAgentUser",pagent);
        Map<String,String>   userRole = new HashMap();
        userRole.put("code", UUID.randomUUID().toString());
        userRole.put("userId",pagent.getUserCode());
        userRole.put("roleId",pagent.getRole_code());
        dao.save("roleDao.saveUserRole",userRole);
    }

    @Override
    public void updateAgent(Pagent pagent) {
        Tagent ag=agentDao.getById(Tagent.class,pagent.getAgent_code());
        BeanUtils.copyProperties(pagent,ag, "isdel","create_time","type","sort","flag","agent_p_code","role_code");
        //Integer.valueOf(pagent.getSort());
        agentDao.update(ag);
    }

    @Override
    public Pagent findById(String id) throws Exception {
        Map<String,String> map=new HashMap<>();
        map.put("id",id);
        return (Pagent) dao.findForObject("agentMapper.findById",map);
    }

    @Override
    public void delGridById(String ids) {
        String [] idList=ids.split(",");
        for (String id:idList) {
            Tagent agent= agentDao.getById(Tagent.class,id);
            agent.setIsdel(1);
            agentDao.update(agent);
        }
    }

    @Override
    public String checkInfo(Pagent pagent) throws Exception {
        if(!StringUtil.isNullOrEmpty(pagent.getAgent_code()))
        {return "0";}
      Integer flag= (Integer) dao.findForObject("agentMapper.checkFlag",pagent);
        Integer loginName= (Integer) dao.findForObject("userDao.checkLoginName",pagent);
        if(flag>0 )
        {
            return "1";
        }
        else if(loginName>0)
        {return "2";}
        else
        {return "0";}
    }

    @Override
    public List<Pagent> getAgentListForSelect(User user) throws Exception {
        return (List<Pagent>) dao.findForList("agentMapper.getAgentListForSelect",user);
    }

    @Override
    public List<Map<String, Object>> queryBrandByAgent(String identity_code) throws Exception {
        return (List<Map<String, Object>>)dao.findForList("agentMapper.queryBrandByAgent",identity_code);
    }

    @Override
    public void saveOrderMain90(OrderMain90 order_main_90) throws Exception{
        dao.save("agentMapper.saveOrderMain90",order_main_90);
    }
}
