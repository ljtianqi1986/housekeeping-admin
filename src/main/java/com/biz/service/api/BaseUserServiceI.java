package com.biz.service.api;

import com.biz.model.Pmodel.api.BaseUser;
import com.biz.service.base.BaseServiceI;

import java.util.List;
import java.util.Map;

/**
 * Created by lzq on 2017/2/4.
 */
public interface BaseUserServiceI extends BaseServiceI {

    public List<BaseUser> queryBaseUserByOnlyCode(String only_code) throws Exception;
    public String getSysUserIdByOpenId(String openid)throws Exception;
    public Double getCoin_90ByTUserId(String id)throws Exception;
    List<BaseUser> queryBaseUserByPhone(String phone) throws Exception;

    void updateInitUserUnionId()throws Exception;

    Map<String,Object> getUserCenterInfo(String unionId)throws Exception;

    Map<String,Object> getUserPersonInfo(String unionId)throws Exception;

    Map<String,Object> getUserOrderList(String unionId)throws Exception;

    Map<String,Object> getUserCoin(String unionId)throws Exception;

    List<Map<String,Object>> getUserAddressList(String unionId)throws Exception;

    Map<String,Object> getAddressById(String id)throws Exception;

    List<Map<String,Object>> getAddressList()throws Exception;

    Map<String,Object> saveAddressByUnionId(Map<String, Object> address)throws Exception;

    Map<String,Object> updateAddressByUnionId(Map<String, Object> address)throws Exception;

    Map<String,Object> updateUserBirthDayByUnionId(String unionId, String birthDay)throws Exception;

    void saveUserList(List<Map<String, Object>> userList, String appid)throws Exception;
}
