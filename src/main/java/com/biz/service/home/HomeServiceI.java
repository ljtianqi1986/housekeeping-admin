package com.biz.service.home;

import com.biz.model.Pmodel.Pmenu;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.Pagent;
import com.biz.model.Pmodel.basic.Parea;
import com.biz.model.Pmodel.basic.Pbrand;
import com.biz.service.base.BaseServiceI;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yzljj on 2016/6/23.
 */
public interface HomeServiceI extends BaseServiceI<Object> {

	List<Pmenu> showLeftMenu()throws Exception;

	List<Pmenu> showLeftMenuByRole(String sqlin) throws Exception;

	List<Parea> getList(String deep)throws Exception;

	/**
	 * 根据区域id加载区域信息
	 * lev："1"->省，"2"->市，"3"->区
	 * id：可以指定到具体哪一个省
	 * @param lev
	 * @param id
	 */
	List<Parea> getList(String lev,String id) throws Exception;

	/**
	 * 根据区域id加载区域信息
	 * lev："1"->省，"2"->市，"3"->区
	 * pid：区域上级
	 * @param lev
	 * @param pid
	 */
	List<Parea> getListByPid(String lev,String pid) throws Exception;

	/**
	 * 昨日发券
	 */
    HashMap<String,Object> queryUseGiftYes(String identity_code, int identity) throws Exception;
	/**
	 * 昨日用券
	 */
	HashMap<String,Object> queryGiftCardDataYes(String identity_code, int identity)throws Exception;
	/**
	 * 昨日新增会员
	 */
	HashMap<String,Object> queryPersonNumYes(String identity_code, int identity)throws Exception;
	/**
	 * 读取对应代理商
	 */
	List<Pagent> queryAgentss(String identity_code, int identity)throws Exception;
	/**
	 * 读取对应商户
	 */
	List<Pbrand> queryBrandByAgent(String identity_code, int identity)throws Exception;
/*
根据条件获取本周/月/季发(用)券统计
* */
	HashMap<String,Object> queryGiftData(int type, User user, int inout, String agentId, String brandId)throws Exception;

    HashMap<String,Object> queryBrandDataYes(String identity_code, int identity)throws Exception;

	HashMap<String,Object> queryCoinDataYes(String identity_code, int identity)throws Exception;

    HashMap<String,Object> queryCoinDataAll(String identity_code, int identity)throws Exception;
	/*
    通过id获取地区信息
    * */
    Parea getAreaById(String cityId)throws Exception;
}
