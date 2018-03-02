package com.biz.service.basic;

import com.biz.model.Hmodel.basic.Tpics;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.BaseUser;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.hdao.BaseDaoI;
import com.biz.model.Hmodel.TSysUser;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Paging;
import com.framework.model.Params;
import com.framework.utils.StringUtil;
import com.framework.utils.Tools;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yzljj on 2016/6/23.
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements  UserServiceI {
    @Autowired
    private BaseDaoI<TSysUser> userDao;
    @Autowired
    private BaseDaoI<Tpics> picDao;
    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Override
    public User findByName(String username) {
        User user=new User();
        String hql= StringUtil.formateString("from TSysUser where loginName='{0}' and isdel=0 and isLock=0",username);
        TSysUser tUser= userDao.getByHql(hql);
        if (tUser!=null){

            BeanUtils.copyProperties(tUser,user);
            user.setUsername(tUser.getLoginName());
            user.setPassword(tUser.getPwd());
            user.setPerson_name(tUser.getPersonName());
            user.setIdentity(tUser.getIdentity());
            user.setIdentity_code(tUser.getIdentityCode());
            if(!StringUtil.isNullOrEmpty(tUser.getCoverId())) {
                Tpics pics = picDao.getById(Tpics.class, tUser.getCoverId());
                if(pics!=null)
                {user.setHeadUrl(pics.getPath());}
            }

            return user;
        }else{
            return  null;
        }

    }

    @Override
    public List<Map> findRolesByUser(User user){
      String sql=  StringUtil.formateString("SELECT\n" +
                "\ta.*\n" +
                "FROM\n" +
                "\tt_role a\n" +
                "LEFT JOIN t_user_role b ON a.id = b.role_id\n" +
                "LEFT JOIN t_user c ON c.id = b.user_id\n" +
                "WHERE\n" +
                "\tc.username = '{0}'",user.getUsername());
        List<Map> list= userDao.findBySql(sql);
        return list;
    }


    @Override
    public List<Map> findNameSpaceRolesByUser(User user){
        String sql=  StringUtil.formateString("SELECT\n" +
                "\tz.id,\n" +
                "\tz.roleName,\n" +
                "\tz.roleGroup,\n" +
                "\tz.roleMark,\n" +
                "\tz.iconCls,\n" +
                "\tz.isdel,\n" +
                "\tz.createTime,\n" +
                "\tGROUP_CONCAT(z.permission)  as permission\n" +
                "FROM\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\ta.id,\n" +
                "\t\t\ta.roleName,\n" +
                "\t\t\ta.roleGroup,\n" +
                "\t\t\ta.roleMark,\n" +
                "\t\t\ta.iconCls,\n" +
                "\t\t\ta.isdel,\n" +
                "\t\t\ta.createTime,\n" +
                "\t\t\trm.permission_nameSpace AS permission\n" +
                "\t\tFROM\n" +
                "\t\t\tt_role a\n" +
                "\t\tLEFT JOIN t_user_role b ON a.id = b.role_id\n" +
                "\t\tLEFT JOIN t_sys_user c ON c.id = b.user_id\n" +
                "\t\tLEFT JOIN t_sys_role_menu rm ON rm.role_id = a.id\n" +
                "\t\tWHERE\n" +
                "\t\t\tc.loginName = '{0}'\n" +
                "\t\tGROUP BY\n" +
                "\t\t\ta.id,\n" +
                "\t\t\trm.permission_nameSpace\n" +
                "\t) AS z\n" +
                "GROUP BY\n" +
                "\tz.id",user.getUsername());
        List<Map> list= userDao.findBySql(sql);
        return list;
    }


    @Override
    public List<Map> findPermissionList(User user){
        String sql=  StringUtil.formateString("SELECT\n" +
                "\t\t\ta.id,\n" +
                "\t\t\ta.roleName,\n" +
                "\t\t\ta.roleGroup,\n" +
                "\t\t\ta.roleMark,\n" +
                "\t\t\ta.iconCls,\n" +
                "\t\t\ta.isdel,\n" +
                "\t\t\ta.createTime,\n" +
                "\t\t\trm.permission_nameSpace AS permission\n" +
                "\t\tFROM\n" +
                "\t\t\tt_role a\n" +
                "\t\tLEFT JOIN t_user_role b ON a.id = b.role_id\n" +
                "\t\tLEFT JOIN t_sys_user c ON c.id = b.user_id\n" +
                "\t\tLEFT JOIN t_sys_role_menu rm ON rm.role_id = a.id\n" +
                "\t\tWHERE\n" +
                "\t\t\tc.loginName = '{0}'\n" +
                "\t\tGROUP BY\n" +
                "\t\t\ta.id,\n" +
                "\t\t\trm.permission_nameSpace",user.getUsername());
        List<Map> list= userDao.findBySql(sql);
        return list;
    }




    @Override
    public Paging findAgentUserGrid(Params sqlParams) throws Exception {
        Paging paging= dao.findForPagings("userDao.findAgentUserGrid",sqlParams,"userDao.findAgentUserCount",sqlParams);
        return paging;
    }

    @Override
    public String findIdentity_code(User user) throws Exception {
        return (String)dao.findForObject("userDao.findIdentity_code",user.getUsername());
    }

    @Override
    public User findUser(User user) throws Exception {
        return (User)dao.findForObject("userDao.findUser",user.getUsername());
    }

    @Override
    public User findUserByUserName(String userName) throws Exception {
        return (User)dao.findForObject("userDao.findUserByUserName",userName);
    }

    @Override
    public BaseUser findBaseUserByPhone(String phone) throws Exception {
        return (BaseUser)dao.findForObject("userDao.findBaseUserByPhone",phone);
    }


    //获取是否有分期发券权限(是否分期 0：不分期  1：分期)
    @Override
    public int findBaseUserisPeriodization(int identity,String identity_code) throws Exception {
        int isPeriodization=0;
        try{
            if(identity==4 && (!Tools.isEmpty(identity_code))){
                //门店登陆
                Map<String,Object> select_map=new HashMap<>();
                select_map.put("identity_code",identity_code);
                Map<String,Object> map_isPeriodization=(Map<String,Object>)dao.findForObject("userDao.UserBrandisPeriodization",select_map);
                if(map_isPeriodization!=null){
                    isPeriodization=Integer.parseInt(map_isPeriodization.get("isPeriodization").toString());
                }
            }
        }catch (Exception e){

        }
        finally {
            return isPeriodization;
        }
    }

    /**
     * 登录判断
     * @param hashMap
     * @return
     * @throws Exception
     */
    public User getUserForLoginhashMap(HashMap<String, Object> hashMap) throws Exception
    {
        return (User) dao.findForObject("userDao.getUserForLoginhashMap", hashMap);
    }

    /**
     * 获取
     */
    public BaseUser getBaseUserByOpen_id(String open_id) throws Exception
    {
        return (BaseUser) dao.findForObject("userDao.getBaseUserByOpen_id", open_id);
    }
    @Override
    public User getUserByCode(String user_code) throws Exception {
        Map<String,String> map=new HashMap<>();
        map.put("userCode",user_code);
        return (User) dao.findForObject("userDao.getUserByCode",map);
    }

    @Override
    public Map<String, Object> getShopIdByAppId(String appId) throws Exception {
        return (Map<String, Object>) dao.findForObject("userDao.getShopIdByAppId",appId);
    }

    @Override
    public boolean changePwd(String pwd,String userid)throws Exception{
        try{
            String hql= StringUtil.formateString("from TSysUser where id='{0}' ",userid);
            TSysUser tUser= userDao.getByHql(hql);
            tUser.setPwd(pwd);
            userDao.update(tUser);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public String findSidByCode(String identity_code) throws Exception {
        return (String)dao.findForObject("userDao.findSidByCode",identity_code);
    }
}
