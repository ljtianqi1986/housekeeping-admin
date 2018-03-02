package com.biz.service.api;

import com.alibaba.fastjson.JSON;
import com.biz.model.Pmodel.PBaseUser;
import com.biz.model.Pmodel.api.BaseUser;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.URLConectionUtil;
import com.framework.utils.UuidUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lzq on 2017/2/4.
 */

@Service("baseUserService")
public class BaseUserServiceImpl extends BaseServiceImpl implements BaseUserServiceI {
    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Resource(name = "wxUtilService")
    private WxUtilServiceI wxUtilService;

    public List<BaseUser> queryBaseUserByOnlyCode(String only_code) throws Exception
    {
        return (List<BaseUser>) dao.findForList("BaseUserDao.queryBaseUserByOnlyCode", only_code);
    }
    public List<BaseUser> queryBaseUserByPhone(String phone) throws Exception
    {
        return (List<BaseUser>) dao.findForList("BaseUserDao.queryBaseUserByPhone", phone);
    }

    @Override
    public void updateInitUserUnionId() throws Exception {
        List<PBaseUser> userList= (List<PBaseUser>) dao.findForList("BaseUserDao.findAllUser",null);
        for(PBaseUser user:userList)
        {
            String url="https://api.weixin.qq.com/cgi-bin/user/info";
            Map<String,String> map=new HashMap<>();
            Map<String,Object> resmap=new HashMap<>();
            String token=wxUtilService.getAccessToken();
            map.put("access_token",token);
            map.put("openid",user.getOpen_id());
            map.put("lang","zh_CN");
            String  res= URLConectionUtil.httpURLConnectionPostDiy(url,map);
            if(!"失败".equals(res))
            {
                resmap= JSON.parseObject(res,Map.class);
                if(!resmap.containsKey("errcode"))
                {
                    if(resmap.containsKey("unionid")&&resmap.containsKey("openid"))
                    {
                        dao.update("BaseUserDao.updateUserUnionId",resmap);
                    }
                }
            }

        }
    }

    @Override
    public Map<String, Object> getUserPersonInfo(String unionId) throws Exception {
        return (Map<String, Object>) dao.findForObject("BaseUserDao.getUserPersonInfo",unionId);
    }

    @Override
    public Map<String, Object> getUserOrderList(String unionId) throws Exception {
        List<Map<String,Object>> orderList= (List<Map<String, Object>>) dao.findForList("OrderDao.findOrderByUnionId",unionId);
        Map<String,Object> map=new HashMap<>();
        map.put("order",orderList);
        return map;
    }

    @Override
    public Map<String, Object> getUserCoin(String unionId) throws Exception {
        Map<String, Object> res=new HashMap<>();
        Map<String, Object> coin= (Map<String, Object>) dao.findForObject("BaseUserDao.getUserCenterInfo",unionId);
        List<Map<String, Object>> list= (List<Map<String, Object>>) dao.findForList("BaseUserDao.getUserCoinList",unionId);
        if(coin!=null){
        res.put("coin",coin);}
        else
        {
            coin=new  HashMap<>();
            coin.put("chargeAmount","0");
            coin.put("giveAmount","0");
            res.put("coin",coin);
        }
        res.put("list",list);
        return res;
    }

    @Override
    public List<Map<String, Object>> getUserAddressList(String unionId) throws Exception {
        return (List<Map<String, Object>>) dao.findForList("AddressDao.getUserAddressList",unionId);
    }

    @Override
    public Map<String, Object> getAddressById(String id) throws Exception {
        return (Map<String, Object>) dao.findForObject("AddressDao.getAddressById",id);
    }

    @Override
    public List<Map<String, Object>> getAddressList() throws Exception {
        return (List<Map<String, Object>>) dao.findForList("AddressDao.getAddressList",null);
    }

    @Override
    public Map<String, Object> saveAddressByUnionId(Map<String, Object> address) throws Exception {
        String id= UuidUtil.get32UUID();
        address.put("id",id);
        Map<String,Object>  resMap=new HashMap<>();
        if("1".equals(address.get("isdefault")+""))
        {dao.update("AddressDao.clearUserDefaultAddressByUnionId",address);}
        int res= (int) dao.save("AddressDao.saveAddressByUnionId",address);
        if(res>0)
        {
            resMap.put("flag","0");
            resMap.put("msg","操作成功");
        }else
        {
            resMap.put("flag","1");
            resMap.put("msg","操作失败");
        }
        return resMap;
    }

    @Override
    public Map<String, Object> updateAddressByUnionId(Map<String, Object> address) throws Exception {
        Map<String,Object>  resMap=new HashMap<>();
        if("1".equals(address.get("isdefault")+""))
        {dao.update("AddressDao.clearUserDefaultAddressByUnionId",address);}
        int res= (int) dao.update("AddressDao.updateAddressByUnionId",address);
        if(res>0)
        {
            resMap.put("flag","0");
            resMap.put("msg","操作成功");
        }else
        {
            resMap.put("flag","1");
            resMap.put("msg","操作失败");
        }
        return resMap;
    }

    @Override
    public Map<String, Object> updateUserBirthDayByUnionId(String unionId, String birthDay) throws Exception {
        Map<String, Object> parm=new HashMap<>();
        parm.put("unionId",unionId);
        parm.put("birthDay",birthDay);

        int res= (int) dao.update("BaseUserDao.updateUserBirthDayByUnionId",parm);
        parm.clear();
        if(res>0)
        {parm.put("flag","0");}
        else
        {
            parm.put("flag","1");
            parm.put("msg","修改生日失败！");
        }
        return parm;
    }

    @Override
    public void saveUserList(List<Map<String, Object>> userList, String appid) throws Exception {
        Map<String,Object> map=new HashMap<>();
        map.put("appid",appid);
       for(Map<String, Object> user:userList)
       {
           user.put("open_id",user.get("openid"));
           user.put("appid",appid);
           List<BaseUser> list= (List<BaseUser>) dao.findForList("BaseUserDao.selectUsermsg",user);
           if(list.size()==0)
           {
               user.put("id",UuidUtil.get32UUID());
               dao.save("BaseUserDao.saveUserInfo",user);
           }
       }
    }

    @Override
    public Map<String, Object> getUserCenterInfo(String unionId) throws Exception {
        return (Map<String, Object>) dao.findForObject("BaseUserDao.getUserCenterInfo",unionId);
    }
    public String getSysUserIdByOpenId(String openid)throws Exception
    {
        return (String) dao.findForObject("BaseUserDao.getSysUserIdByOpenId", openid);
    }
    public Double getCoin_90ByTUserId(String id)throws Exception
    {
        return (Double) dao.findForObject("BaseUserDao.getCoin_90ByTUserId", id);
    }
}
