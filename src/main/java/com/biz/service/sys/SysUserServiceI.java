package com.biz.service.sys;

import com.biz.model.Pmodel.SysUser;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Paging;

import java.util.List;
import java.util.Map;

/**
 * Created by yzljj on 2016/6/23.
 */
public interface SysUserServiceI extends BaseServiceI<SysUser> {

    Paging getSysUserGridPage(Map<String, Object> params)throws Exception;
    Paging getBaseUserGridPage(Map<String, Object> params)throws Exception;

    void updateUserLock(Map<String,Object> params) throws Exception;

    /**
     * 获取
     *
     * @param user_code
     * @return
     * @throws Exception
     */
    public SysUser getUserByCode(String user_code) throws Exception;

    void insertUser(SysUser user);
    void updateUser(SysUser user);
    public void delGridById(String ids);

    List<Map<String,Object>> getAllAppid()throws Exception;
}
