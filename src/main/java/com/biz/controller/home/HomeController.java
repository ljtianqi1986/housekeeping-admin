package com.biz.controller.home;

import com.biz.model.Pmodel.Pmenu;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.Pagent;
import com.biz.model.Pmodel.basic.Pbrand;
import com.biz.service.home.HomeServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*************************************************************************
 * 版本：         V1.0  
 *
 * 文件名称 ：HomeController.java 描述说明 ：
 * 
 * 创建信息 : create by 刘佳佳 on 2016-6-14 下午3:34:13  修订信息 : modify by ( ) on (date) for ( )
 * 
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/

@Controller
@RequestMapping("/home")
public class HomeController extends BaseController {
	private static final long serialVersionUID = 1417800463296358902L;
	@Autowired
	private HomeServiceI homeService;


    //样例
/*    @Reference
    private com.jswzc.api.service.wxTemplate.WxTemplateServiceI wxTemplateServiceJswzc;
    @RequestMapping("ceshi")
    public void ceshi() throws Exception{
        String open_id="obisKwbgqvUzGWMvzu3esEOA8qxU";
        String accessToken="E6kqLWKhv9_XF272YvstQqRuUBLGvveRvhsxnYRZ-cQiLPTTU769Hxs6mWsFCI1qqRC01mXPLGLFudQAz7Z5flsbjpFTSvY7tsC7TyLywrw6JiYlZ9P60lMyqZItxZhbUNXjAIAXQA";
        wxTemplateServiceJswzc.customSend(open_id,accessToken,"测试");
    }*/


	/**
	 * 跳转首页
	 * @param mv
	 * @return
	 */
	@RequestMapping("toHome")
	public ModelAndView toHome(ModelAndView mv){
		mv.setViewName("main/index_v3");
		return mv;
	}

	/**
	 * 加载首页统计数据
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("loadHomeDatas")
	public void loadHomeDatas(HttpServletResponse response) throws Exception{
		User user= (User) getShiroAttribute("user");
		HashMap<String, Object> res_data = new HashMap<String, Object>();
		HashMap<String, Object> use_data = new HashMap<String, Object>();
		HashMap<String, Object> card_data = new HashMap<String, Object>();
		HashMap<String, Object> person_data = new HashMap<String, Object>();
		use_data = homeService.queryUseGiftYes(user.getIdentity_code(), user.getIdentity());//读取昨日用券
		card_data = homeService.queryGiftCardDataYes(user.getIdentity_code(), user.getIdentity());//读取昨日发券

			person_data = homeService.queryPersonNumYes(user.getIdentity_code(), user.getIdentity());//读取昨日新增会员

		List<Pagent> agent_list = homeService.queryAgentss(user.getIdentity_code(),user.getIdentity());//读取代理商
		List<Pbrand> brand_list = homeService.queryBrandByAgent(user.getIdentity_code(),user.getIdentity());//读取门店
		res_data.put("use_data",use_data);
		res_data.put("card_data",card_data);
		res_data.put("person_data",person_data);
		res_data.put("agent_list",agent_list);
	  	res_data.put("brand_list",brand_list);
		res_data.put("user",user);
		writeJson(res_data,response);
	}

	/**
	 * 加载首页统计数据
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("loadHomeDatasNew")
	public void loadHomeDatasNew(HttpServletResponse response) throws Exception{
		User user= (User) getShiroAttribute("user");
		HashMap<String, Object> res_data = new HashMap<String, Object>();
		HashMap<String, Object> use_data = new HashMap<String, Object>();
		HashMap<String, Object> card_data = new HashMap<String, Object>();
		HashMap<String, Object> person_data = new HashMap<String, Object>();
		HashMap<String, Object> brand_data = new HashMap<String, Object>();
		HashMap<String, Object> coin_data = new HashMap<String, Object>();
		HashMap<String, Object> coin_dataAll = new HashMap<String, Object>();
		use_data = homeService.queryUseGiftYes(user.getIdentity_code(), user.getIdentity());//读取昨日用券
		card_data = homeService.queryGiftCardDataYes(user.getIdentity_code(), user.getIdentity());//读取昨日发券
	 	person_data = homeService.queryPersonNumYes(user.getIdentity_code(), user.getIdentity());//读取昨日会员
		brand_data = homeService.queryBrandDataYes(user.getIdentity_code(), user.getIdentity());//读取昨日商户数据
		coin_data = homeService.queryCoinDataYes(user.getIdentity_code(), user.getIdentity());//读取昨日久零贝数据
		coin_dataAll = homeService.queryCoinDataAll(user.getIdentity_code(), user.getIdentity());//读取昨日久零贝数据

		List<Pagent> agent_list = homeService.queryAgentss(user.getIdentity_code(),user.getIdentity());//读取代理商
		List<Pbrand> brand_list = homeService.queryBrandByAgent(user.getIdentity_code(),user.getIdentity());//读取门店
		res_data.put("use_data",use_data);
		res_data.put("card_data",card_data);
		res_data.put("person_data",person_data);
		res_data.put("brand_data",brand_data);
		res_data.put("coin_data",coin_data);
		res_data.put("coin_dataAll",coin_dataAll);
		res_data.put("agent_list",agent_list);
		res_data.put("brand_list",brand_list);
		res_data.put("user",user);
		writeJson(res_data,response);
	}

    /**
     * 加载首页图表数据
     * @param response
     * @throws Exception
     */
    @RequestMapping("loadHomeCharts")
    public void loadHomeCharts(HttpServletResponse response,String type,String inout,String agentId,String brandId) throws Exception{

       User user= (User) getShiroAttribute("user");
        Map<String, Object> data = homeService.queryGiftData(Integer.valueOf(type),user,Integer.valueOf(inout),agentId,brandId);

        writeJson(data,response);
    }
	/**
	 * 根据用户权限加载左侧菜单数据
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping("showLeftMenu")
	public void showLeftMenu(HttpServletResponse response) throws Exception{
//		List<Pmenu> pmenuList=homeService.showLeftMenu();
		String roleids = getShiroSession().getAttribute("rolesids").toString();
		List<Pmenu> pmenuList=homeService.showLeftMenuByRole(StringUtil.formatSqlIn(roleids));
		writeJsonNoReplace(pmenuList, response);
	}
	
	/**
	 * 跳转个人资料
	 * @param mv
	 * @return
	 */
	@RequestMapping("toProfile")
	public ModelAndView toProfile(ModelAndView mv){
		mv.setViewName("home/profile");
		return mv;
	}

	/**
	 * 查询结果
	 * @param mv
	 * @return
	 */
	@RequestMapping("toSearchResults")
	public ModelAndView toSearchResults(ModelAndView mv){
		mv.setViewName("home/search_results");
		return mv;
	}

}
