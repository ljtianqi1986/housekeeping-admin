package com.biz.service.basic;

import com.biz.model.Hmodel.basic.Tagent;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.OrderMain90;
import com.biz.model.Pmodel.basic.Pagent;
import com.biz.model.Pmodel.basic.PagentTree;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Paging;
import com.framework.model.Params;

import java.util.List;
import java.util.Map;

/**
 * 商户Service
 * Created by liujiajia on 2017/1/6.
 */
public interface AgentServiceI extends BaseServiceI<Tagent> {
 /**
  * 分页查询所有代理商
  * @return
  * @throws Exception
  */
   Paging findAgentGrid(Params sqlParams);
 /**
  * 以树形结构查询代理商
  * @param user 无用，暂时保留
  * @return
  * @throws Exception
  */
    List<PagentTree> getAgentListByUser(User user) throws Exception;
 /**
  * 保存代理商
  * @param pagent
  * @throws Exception
  */
    void saveAgent(Pagent pagent) throws Exception;
 /**
  * 修改代理商
  * @param pagent
  * @throws Exception
  */
    void updateAgent(Pagent pagent);
 /**
  * 根据id查询代理商数据
  * @param  id
  * @return Pagent
  * @throws Exception
  */
    Pagent findById(String id)throws Exception;
 /**
  * 批量删除
  * @param  ids
  * @throws Exception
  */
    void delGridById(String ids);
 /**
  * 检测代理商是否数据唯一
  * @param  pagent
  * @return String
  * @throws Exception
  */
    String checkInfo(Pagent pagent)throws Exception;
 /**
  * 获取所有代理商列表
  * @return List
  * @throws Exception
  * @param user
  */
    List<Pagent> getAgentListForSelect(User user)throws Exception;

    List<Map<String,Object>> queryBrandByAgent(String identity_code) throws Exception;

    void saveOrderMain90(OrderMain90 order_main_90) throws Exception;
}
