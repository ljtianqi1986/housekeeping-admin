package com.biz.controller.basic;

import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.Recharge90;
import com.biz.service.basic.LogServiceI;
import com.biz.service.basic.Recharge90ServiceI;
import com.biz.service.basic.ShopServiceI;
import com.framework.controller.BaseController;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.utils.ObjectExcelView;
import com.framework.utils.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 商户 久零券充值记录管理
 * @author GengLong
 * 创建时间：2016-10-22
 */
@Controller
@RequestMapping("/recharge90")
public class Recharge90Controller extends BaseController
{
	@Resource(name = "shopService")
	private ShopServiceI shopService;
    @Autowired
	private Recharge90ServiceI recharge90Service;
    @Autowired
    private LogServiceI logService;
	@InitBinder("recharge90")
	public void initBinderFormBean1(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("recharge90.");
	}
    @RequestMapping("toRecharge90")
    public ModelAndView toRecharge90(ModelAndView mv){
        mv.setViewName("basic/recharge90");
        return mv;
    }

	/**
	 * 查询界面
	 */
	@RequestMapping("showDoQuery")
	public void doQuery(HttpServletResponse response, HttpServletRequest request) throws Exception
	{
        //前端封装的查询参数
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("agent", request.getParameter("agent"));
        params.put("brand", request.getParameter("brand"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = Integer.parseInt(request.getParameter("offset"));

        Pager<Recharge90> pager = new Pager();
        pager.setParameters(params);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);

        pager = recharge90Service.queryRecharge90ListForPage(pager);

        Paging<Recharge90> paging = new Paging<>();
        paging.setRows(pager.getExhibitDatas());
        paging.setTotal((long) pager.getRecordCount());

        writeJson(paging, response);
	}

	/**
	 * 充值记录-自动导出Excel
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "doExcel")
	public ModelAndView doExcel(String agent,String brand) throws Exception
	{
		Date d = new Date();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmmss");
		String filename=sdf1.format(d);

        //前端封装的查询参数
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("agent",agent);
        params.put("brand",brand);

        Map<String,Object> pd = new HashMap<String,Object>();
        User user = (User) getShiroAttribute("user");
        user.setIdentity_name(shopService.queryIdentityName(user.getIdentity(), user.getIdentity_code()));
        //代理商登录
        if (user.getIdentity()==2)
        {
            mv.addObject("is_agent",1);
            pd.put("agent_select", user.getIdentity_code());
        }else if (user.getIdentity()==3)//品牌账号登陆
        {
            mv.addObject("is_brand",1);
            pd.put("brand_select", user.getIdentity_code());
        }

        List<Recharge90> recharge90_list = recharge90Service.queryRecharge90List(params);


		Map<String, Object> dataMap = new HashMap<String, Object>();
		List<String> titles = new ArrayList<String>();

		titles.add("代理商"); // 1
		titles.add("商户"); // 2
		titles.add("久零券总额");//3
		titles.add("消费的人民币"); // 4
		titles.add("久零余额");// 5
		titles.add("操作人");// 6
		titles.add("备注");// 7
		dataMap.put("titles", titles);
		List<PageData> varList = new ArrayList<PageData>();
		for (int i = 0; i < recharge90_list.size(); i++)
		{
			PageData vpd = new PageData();
			Recharge90 recharge90 = recharge90_list.get(i);
			vpd.put("var1", recharge90.getAgent_name()); // 1
			vpd.put("var2", recharge90.getBrand_name()); // 1
			vpd.put("var3", recharge90.getTotal_90()/100.0); // 1
			vpd.put("var4", recharge90.getTotal_rmb()/100.0); // 1
			vpd.put("var5", recharge90.getBalance_90()/100.0); // 1
			vpd.put("var6", recharge90.getPerson_name()); // 1
			vpd.put("var7", recharge90.getNotes()); // 1

			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView(); // 执行excel操作
		erv.setNewFileName(filename);
		mv = new ModelAndView(erv, dataMap);
		return mv;
	}
	/**
	 * 跳转新增
	 */
	@RequestMapping(value = "/toInsert")
	public ModelAndView toInsert() throws Exception
	{
		//User user = (User) this.getRequest().getSession().getAttribute(Const.SESSION_USER);
		//user.setIdentity_name( shopService.queryIdentityName(user.getIdentity(), user.getIdentity_code()));
		//
		////代理商登录
		//if (user.getIdentity()!=2)
		//{
		//	mv.addObject("error_msg", "只有代理商可以新增");
		//	mv.setViewName("nocan");
		//	return mv;
		//}
		//List<Brand> brand_list = shopService.queryBrandByCondition(user.getIdentity_code(),"");
		//mv.addObject("brand_list", brand_list);
		//List<BizPerson> biz_list = bizPersonService.queryBizPersonList();
		//mv.addObject("biz_list", biz_list);
		//Recharge90 recharge90 = new Recharge90();
        //
		//mv.addObject("recharge90", recharge90);
		//mv.addObject("page_type", "insert");
		//
		//mv.setViewName("base/recharge90/detail");
		return mv;
	}


	/**
	 * 保存新增信息
	 */
	@RequestMapping(value = "/doInsert", method = RequestMethod.POST)
	public ModelAndView doInsert(Recharge90 recharge90,HttpServletResponse response,HttpServletRequest request) throws Exception
	{
		String msg="";
		User user= (User) getShiroAttribute("user");
		try {
			recharge90.setCode(getRndCode());
			recharge90Service.saveRecharge90(recharge90,user,request);
			msg = "success";
		}catch (Exception e)
		{
			e.printStackTrace();
			msg = "操作失败";
		}
		writeJson(msg,response);
		return mv;
	}

}
