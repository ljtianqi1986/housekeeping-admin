package com.biz.service.basic;

import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.BaseUser;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Paging;
import com.framework.model.Params;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yzljj on 2016/6/23.
 */
public interface UserServiceI extends BaseServiceI<User> {
    public User findByName(String username);

    List<Map> findRolesByUser(User user);

    List<Map> findNameSpaceRolesByUser(User user);

    public List<Map> findPermissionList(User user);

    Paging findAgentUserGrid(Params sqlParams) throws  Exception;

    String findIdentity_code(User user) throws Exception;

    User findUser(User user) throws Exception;

    User findUserByUserName(String userName) throws Exception;

    BaseUser findBaseUserByPhone(String phone) throws Exception;

    User getUserForLoginhashMap(HashMap<String, Object> hashMap) throws Exception;

    BaseUser getBaseUserByOpen_id(String open_id) throws Exception;

    User getUserByCode(String user_code)throws Exception;

    Map<String, Object> getShopIdByAppId(String appId) throws Exception;

    boolean changePwd(String pwd,String userid)throws Exception;

    int findBaseUserisPeriodization(int identity,String identity_code) throws Exception;

    String findSidByCode(String identity_code)throws Exception;
}
