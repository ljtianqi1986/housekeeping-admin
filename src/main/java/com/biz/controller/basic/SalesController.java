package com.biz.controller.basic;

import com.biz.model.Hmodel.basic.Tsales;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.Psales;
import com.biz.service.api.WxUtilServiceI;
import com.biz.service.basic.SalesServiceI;
import com.framework.controller.BaseController;
import com.framework.model.MessageBox;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.utils.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomchen on 17/2/6.
 * 导购员设置
 */
@Controller
@RequestMapping("/sales")
public class SalesController extends BaseController {

    @Autowired
    private SalesServiceI salesService;

    @Autowired
    private WxUtilServiceI wxUtilServiceI;

    @RequestMapping("toSales")
    public ModelAndView toSales(ModelAndView mv){
        mv.setViewName("basic/sales/sales");
        return mv;
    }

    @RequestMapping("showDatas")
    public void showJqGrid(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("name", request.getParameter("name"));
        params.put("phone", request.getParameter("phone"));


        //判断当前操作者是门店还是商户
        //todo 从session中判断
        User user = (User) getShiroAttribute("userinfo");

        //3:品牌4:实体门店
        params.put("identity", user.getIdentity());
        params.put("identityCode", user.getIdentity_code());

        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = Integer.parseInt(request.getParameter("offset"));

        Pager<Psales> pager = new Pager();
        pager.setParameters(params);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);

        pager = salesService.querySales(pager);

        Paging<Psales> paging = new Paging<>();
        paging.setRows(pager.getExhibitDatas());
        paging.setTotal((long) pager.getRecordCount());

        writeJson(paging, response);
    }

    @RequestMapping("toAdd")
    public ModelAndView toAdd(ModelAndView mv) throws Exception {
        //判断当前操作者是门店还是商户
        //todo 从session中判断
        User user = (User) getShiroAttribute("userinfo");

        //3:品牌4:实体门店
        //int identity = IDENTITY;
        //String identityCode = IDENTITY_CODE;

        List<Map<String, Object>> shops = salesService.getShopsForSales(user.getIdentity(), user.getIdentity_code());

        mv.addObject("identity", user.getIdentity());
        mv.addObject("shops", shops);
        mv.setViewName("basic/sales/salesDetail");
        return mv;
    }

    @RequestMapping("doSave")
    public void doSave(HttpServletResponse response, HttpServletRequest request,Psales psales) {
        MessageBox mb = getBox();
        try {
            if(StringUtil.isNullOrEmpty(psales.getId())){//add
                Tsales tsales = new Tsales();
                BeanUtils.copyProperties(psales, tsales);

                tsales.setCreateTime(new Date());
                salesService.save(tsales);
            }else{//update
                Tsales tsales = salesService.getById(psales.getId());
                BeanUtils.copyProperties(psales,tsales,"createTime","isdel");
                salesService.update(tsales);
            }
        }
        catch (Exception e){
            mb.setSuccess(false);
            mb.setMsg(e.getMessage());
            e.printStackTrace();
        }

        writeJson(mb,response);
    }

    @RequestMapping("toEdit")
    public ModelAndView toEdit(String id) throws Exception {
        mv.clear();
        mv.addObject("id",id);
        Tsales tsales = salesService.getById(id);

        //判断当前操作者是门店还是商户
        //todo 从session中判断
        User user = (User) getShiroAttribute("userinfo");

        //3:品牌4:实体门店
        List<Map<String, Object>> shops = salesService.getShopsForSales(user.getIdentity(), user.getIdentity_code());


        mv.addObject("sales",tsales);
        mv.addObject("shops", shops);
        mv.setViewName("basic/sales/salesDetail");
        return mv;
    }

    @RequestMapping("deByIds")
    public void delGridById(HttpServletResponse response, HttpServletRequest request,String ids) throws Exception {
        MessageBox mb = getBox();
        try{
            salesService.executeHql(StringUtil.formateString("update Tsales set isdel =1 where id in ({0})",StringUtil.formatSqlIn(ids)));
            mb.setSuccess(true);
        }catch (Exception e){
            mb.setSuccess(false);
        }
        writeJson(mb,response);
    }

}
