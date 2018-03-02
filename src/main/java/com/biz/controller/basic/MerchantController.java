package com.biz.controller.basic;

import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.Brand;
import com.biz.model.Pmodel.basic.BrandSpeed;
import com.biz.model.Pmodel.basic.Category;
import com.biz.model.Pmodel.basic.Pbrand;
import com.biz.model.Singleton.ZkNode;
import com.biz.service.basic.MerchantServiceI;
import com.framework.controller.BaseController;
import com.framework.model.MessageBox;
import com.framework.model.Paging;
import com.framework.model.Params;
import com.framework.utils.SqlFactory;
import com.framework.utils.StringUtil;
import com.framework.utils.zook.ZookeeperClientUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 商户设置相关
 * Created by liujiajia on 2016/11/14.
 */
@Controller
@RequestMapping("/merchant")
public class MerchantController extends BaseController{

    @Autowired
    private ZookeeperClientUtil zookeeperClient;

    @Autowired
    private MerchantServiceI merchantService;


    @InitBinder("pbrand")
    public void initBinderFormBean1(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("pbrand.");
    }


    /**
     * 跳转商户设置页面
     * @param mv
     * @return
     */
    @RequestMapping("toMerchant")
    public ModelAndView toMenu(ModelAndView mv){
        //String cc=zookeeperClient.getZkpa();
        System.out.println(ZkNode.getIstance().getJsonConfig());
        String cityId=ZkNode.getIstance().getJsonConfig().get("localProv")+"";
        mv.addObject("cityId",cityId);
        mv.setViewName("basic/merchant");
        return mv;
    }
    /**
     * 跳转商户合作量页面
     * @param mv
     * @return
     */
    @RequestMapping("toMerchantStatistics")
    public ModelAndView toMerchantStatistics(ModelAndView mv){
         mv.clear();
        mv.setViewName("basic/brand/brandstatistics");
        return mv;
    }
    /**
     * 跳转商户编辑页面
     * @param mv
     * @return
     */
    @RequestMapping("toEdit")
    public ModelAndView toEdit(ModelAndView mv,String id) throws Exception {
        mv.clear();
        mv.addObject("id",id);
        Pbrand pbrand=merchantService.findById(id);
        mv.addObject("cityId",pbrand.getCity());//回写的市id
        mv.addObject("proId",pbrand.getProvince());//回写的省id
        mv.addObject("speedId",pbrand.getSpeedCode());//回写的快速分类id
        mv.addObject("cf",pbrand.getCategoryFirst());//回写的第一级行业id
        mv.addObject("cs",pbrand.getCategorySecond());//回写的第二级行业id
        mv.addObject("bizId",pbrand.getBizCode());//回写的代理人id
        try {
            mv.addObject("proportionId",Double.valueOf(pbrand.getProportion()));
        }catch (Exception e) {
            mv.addObject("proportionId", 0);//回写的发券比例}
        }
        mv.addObject("expiryDateType",pbrand.getExpiryDateType());
        mv.addObject("zeroGoDateType",pbrand.getZeroGoDateType());
        mv.addObject("experienceDateType",pbrand.getExperienceDateType());
        mv.setViewName("basic/form_edit");
        return mv;
    }
    /**
     * 展示商户列表信息
     * @param response
     * @param request
     */
    @RequestMapping("showMerchant")
    public void showMerchant(HttpServletResponse response, HttpServletRequest request){
        String identity_code=(String)getShiroAttribute("identity_code");

        //必要的分页参数
        SqlFactory factory=new SqlFactory();
        Params sqlParams=factory.getParams();
        sqlParams.setOrder(request.getParameter("order"));
        sqlParams.setPage(Integer.parseInt(request.getParameter("offset")));
        sqlParams.setRows(Integer.parseInt(request.getParameter("limit")));
        sqlParams.setSort(request.getParameter("sort"));
        sqlParams.setIdentity_code(identity_code);
        String name=request.getParameter("name").toString();
        sqlParams.setSearchtext(name);
        Paging paging=merchantService.findMerchantGrid(sqlParams);
        writeJson(paging,response);
    }

    /**
     * 通过商户Code 获取
     * @param response
     */
    @RequestMapping("getMerchantById")
    public void getMerchantById(String ids,HttpServletResponse response){
        Brand brand=null;
        if (!StringUtil.isNullOrEmpty(ids)){
            try {
                brand=merchantService.getMerchantById(ids);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        writeJson(brand,response);
    }

    /**
     * 更具id 删除商户信息
     * @param ids
     * @param response
     */
    @RequestMapping("delGridById")
    public void delGridById(String ids,HttpServletResponse response){
        MessageBox box=new MessageBox();
        try{
            if (ids == null) {
                box.setSuccess(false);
                box.setMsg("未指定");
            } else {
                merchantService.delGridById(ids);
            }
        }catch (Exception e){
            box.setSuccess(false);
        }
        writeJson(box, response);
    }

    /**
     * 更具id 更改商户状态
     * @param ids
     * @param response
     */
    @RequestMapping("changeStateById")
    public void changeStateById(String ids,HttpServletResponse response){
        MessageBox box=new MessageBox();
        try{
            if (ids != null) {
                merchantService.changeStateById(ids);
            } else {
                box.setSuccess(false);
                box.setMsg("未指定");
            }
        }catch (Exception e){
            box.setSuccess(false);
        }
        writeJson(box, response);
    }

    /**
     * 更具id 重置商户密码
     * @param ids
     * @param response
     */
    @RequestMapping("resetPwdById")
    public void resetPwdById(String ids,HttpServletResponse response){
        MessageBox box=new MessageBox();
        try{
            if (ids != null) {
                merchantService.resetPwdById(ids);
            } else {
                box.setSuccess(false);
                box.setMsg("未指定");
            }
        }catch (Exception e){
            box.setSuccess(false);
        }
        writeJson(box, response);
    }

    /**
     * 根据PID 加载行业数据
     * @param pid
     * @param response
     */
    @RequestMapping("showIndustry")
    public void showIndustry(String pid,HttpServletResponse response){
        if(StringUtil.isNullOrEmpty(pid)){
            pid="";
        }
        List<Category> category=null;
        try{
           category= merchantService.getIndustry(pid);
        }catch (Exception e){
            e.printStackTrace();
        }
        writeJson(category,response);
    }

    /**
     * 加载行业数据
     * @param response
     */
    @RequestMapping("showBrandSpeed")
    public void showBrandSpeed(HttpServletResponse response){
        List<BrandSpeed> list=null;
        try {
            list=merchantService.showBrandSpeed();
        } catch (Exception e) {
            e.printStackTrace();
        }
        writeJson(list,response);
    }

    /**
     * 获取所有商户列表
     * @param response
     */
    @RequestMapping("getBrandListForSelect")
    public void getBrandListForSelect(HttpServletResponse response, HttpServletRequest request,String pid) throws Exception {
        User user= (User) getShiroAttribute("user");
       if("10".equals(pid))
       {pid="";}
        List<Brand> list=merchantService.getBrandListForSelect(pid,user);
        writeJson(list,response);
    }


    /**
     * 保存/更新商户
     * @param response
     */
    @RequestMapping("doSave")
    public void doSave(HttpServletResponse response, HttpServletRequest request,Pbrand pbrand,String cycleDays,String scale) throws Exception {
       // List<Brand> list=merchantService.getBrandListForSelect();
        String msg="";
        String identity_code= (String) SecurityUtils.getSubject().getSession().getAttribute("identity_code");
     //  identity_code= (String) getShiroAttribute("identity_code");
        pbrand.setAgentCode(identity_code);
        String isOk=merchantService.checkInfo(pbrand);//去重检查
        if("0".equals(isOk)) {
            if (StringUtil.isNullOrEmpty(pbrand.getBrandCode())) {//如果没brandcode，新增
                merchantService.saveMerchant(pbrand);
                //判断是否支持分期发券
                if(pbrand.getIsPeriodization().equals("1")){
                    User userinfo = (User)getShiroAttribute("userinfo");
                    merchantService.savePeriodization(cycleDays,scale,pbrand.getBrandCode(),userinfo.getId());
                }
            } else {
                merchantService.updateMerchant(pbrand);//否则修改
                //判断是否支持分期发券
                if(pbrand.getIsPeriodization().equals("1")){
                    User userinfo = (User)getShiroAttribute("userinfo");
                    merchantService.updatePeriodization(pbrand.getBrandCode(), userinfo.getId());
                    merchantService.savePeriodization(cycleDays,scale,pbrand.getBrandCode(),userinfo.getId());
                }
            }
            msg = "success";
        } else if("1".equals(isOk))
        {msg = "商户名称已存在";}
        else if("2".equals(isOk))
        {msg = "登录名已存在";}
        writeJson(msg,response);
    }
    /**
     * 根据id获取商户信息(编辑回写)
     * @param response
     */
    @RequestMapping("getById")
    public void getById(HttpServletResponse response, HttpServletRequest request,String id) throws Exception {
        Pbrand pbrand=merchantService.findById(id);
        if(pbrand.getIsPeriodization().equals("1")){
            List<Map<String,Object>> periodization =merchantService.findPeriodizationByBrandId(id);
            pbrand.setPeriodization(periodization);
        }
        writeJson(pbrand,response);
    }



    /**
     * 展示商户列表信息
     * @param response
     * @param request
     */
    @RequestMapping("showBrandStatistics")
    public void showBrandStatistics(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String identity_code=(String)getShiroAttribute("identity_code");
        //必要的分页参数
        SqlFactory factory=new SqlFactory();
        Params sqlParams=factory.getParams();
        sqlParams.setOrder(request.getParameter("order"));
        sqlParams.setPage(Integer.parseInt(request.getParameter("offset")));
        sqlParams.setRows(Integer.parseInt(request.getParameter("limit")));
        sqlParams.setSort(request.getParameter("sort"));
        sqlParams.setIdentity_code(identity_code);
        sqlParams.getParm().put("name",request.getParameter("name"));
        sqlParams.getParm().put("type",request.getParameter("type"));
        Paging paging=merchantService.findBrandStatisticsGrid(sqlParams);
        writeJson(paging,response);
    }

}
