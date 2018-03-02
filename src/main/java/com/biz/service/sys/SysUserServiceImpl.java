package com.biz.service.sys;

import com.alibaba.druid.util.Utils;
import com.biz.model.Hmodel.TSysUser;
import com.biz.model.Pmodel.SysUser;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Paging;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by yzljj on 2016/6/23.
 */
@Service("userClientService")
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements SysUserServiceI {

    @Autowired
    private BaseDaoI<TSysUser> userDao;
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Override
    public Paging getSysUserGridPage(Map<String,Object> params)throws Exception {
       // String sql="select * from demo_grid ";
        //String countSql="select count(1) from demo_grid ";
       // SqlFactory factory=new SqlFactory();
      //  System.out.println("AA;"+factory.getSql(sql,params,sqlParams));
       // System.out.println("BB;"+factory.getCountSql(countSql, params));
        Paging paging=dao.findForPaging("sysUserDao.sysUserList",params, "sysUserDao.sysUserCount", params);
        return paging;
    }

    @Override
    public Paging getBaseUserGridPage(Map<String,Object> params)throws Exception {
        Paging paging=dao.findForPaging("sysUserDao.baseUserPage",params, "sysUserDao.countBaseUser", params);
        return paging;
    }

    public void updateUserLock(Map<String,Object> params) throws Exception
    {
        dao.update("sysUserDao.updateUserLock", params);
    }

    /**
     * 获取
     *
     * @param user_code
     * @return
     * @throws Exception
     */
    public SysUser getUserByCode(String user_code) throws Exception
    {
        return (SysUser) dao.findForObject("sysUserDao.getUserByCode", user_code);
    }

    @Override
    public void insertUser(SysUser user) {
        TSysUser tuser=new TSysUser();
        BeanUtils.copyProperties(user,tuser, "isdel","createTime", "type","pwd", "sorts");
        tuser.setType((short) 2);
        tuser.setIsdel((short)0);
        tuser.setPwd(Utils.md5(user.getLoginName()));
        tuser.setSorts(Integer.valueOf(user.getSorts()));
        userDao.save(tuser);
    }

    @Override
    public void updateUser(SysUser user) {
        TSysUser tuser=userDao.getById(TSysUser.class,user.getId());
        BeanUtils.copyProperties(user,tuser, "isdel","loginName","createTime","sex","personName","isLock","type","pwd","sorts","identity","identity_code");
        userDao.update(tuser);
    }

    @Override
    public void delGridById(String ids) {
        String [] idList=ids.split(",");
        for (String id:idList) {
            TSysUser tuser= userDao.getById(TSysUser.class,id);
            tuser.setIsdel((short)1);
            userDao.update(tuser);
        }
    }

    @Override
    public List<Map<String, Object>> getAllAppid() throws Exception {
        return (List<Map<String, Object>>) dao.findForList("sysUserDao.getAllAppid",null);
    }
}
