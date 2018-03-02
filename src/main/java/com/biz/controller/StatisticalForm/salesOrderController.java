package com.biz.controller.StatisticalForm;

import com.biz.model.Pmodel.StatisticalForm.POrderInfo;
import com.biz.model.Pmodel.StatisticalForm.PsalesOrder;
import com.biz.service.StatisticalForm.SalesOrderServiceI;
import com.framework.controller.BaseController;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.utils.ObjectExcelView;
import com.framework.utils.PageData;
import com.framework.utils.StringUtil;
import com.framework.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tomchen on 17/2/6.
 * 导购员设置
 */
@Controller
@RequestMapping("/salesOrder")
public class salesOrderController extends BaseController {


    @Autowired
    private SalesOrderServiceI salesOrderService;

    //统计主页
    @RequestMapping("toSales")
    public ModelAndView toSales(ModelAndView mv){
        mv.setViewName("StatisticalForm/salesOrder");
        return mv;
    }

    //统计导购员详情
    @RequestMapping("toSalesMX")
    public ModelAndView toSalesMX(ModelAndView mv,String ids,String start_time,String end_time){
        mv.addObject("ids",ids);
        mv.addObject("start_time",start_time);
        mv.addObject("end_time",end_time);
        mv.setViewName("StatisticalForm/salesOrderMX");
        return mv;
    }

    //主表列表
    @RequestMapping("showSalesOrderList")
    public void showSalesOrderList(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map<String,Object> params=new HashMap<String, Object>();
        String start_time=request.getParameter("start_time");
                String end_time=request.getParameter("end_time");
        if(Tools.isEmpty(start_time) || Tools.isEmpty(end_time)){
            start_time="";
            end_time="";
        }else{
            start_time=request.getParameter("start_time")+ " 00:00:00";
            end_time=request.getParameter("end_time")+ " 00:00:00";
        }
        params.put("start_time", start_time);
        params.put("end_time", end_time);


        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = Integer.parseInt(request.getParameter("offset"));

        Pager<PsalesOrder> pager = new Pager();
        pager.setParameters(params);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);

        pager = salesOrderService.SalesOrderList(pager);

        Paging<PsalesOrder> paging = new Paging<>();
        paging.setRows(pager.getExhibitDatas());
        paging.setTotal((long) pager.getRecordCount());

        writeJson(paging, response);
    }


    //明细列表
    @RequestMapping("showSalesOrder_MX")
    public void showSalesOrder_MX(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map<String,Object> params=new HashMap<String, Object>();
        String start_time=request.getParameter("start_time");
        String end_time=request.getParameter("end_time");
        String ids=StringUtil.formatSqlIn(request.getParameter("ids"));
        if(Tools.isEmpty(start_time) || Tools.isEmpty(end_time)){
            start_time="";
            end_time="";
        }else{
            start_time=request.getParameter("start_time")+ " 00:00:00";
            end_time=request.getParameter("end_time")+ " 00:00:00";
        }
        params.put("start_time", start_time);
        params.put("end_time", end_time);
        params.put("ids", ids);


        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = Integer.parseInt(request.getParameter("offset"));

        Pager<POrderInfo> pager = new Pager();
        pager.setParameters(params);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);

        pager = salesOrderService.SalesOrderMX(pager);

        Paging<POrderInfo> paging = new Paging<>();
        paging.setRows(pager.getExhibitDatas());
        paging.setTotal((long) pager.getRecordCount());

        writeJson(paging, response);
    }


    @RequestMapping(value = "salesOrder_excel")
    public ModelAndView salesOrder_excel(String startDate,String endDate) throws Exception {
        Date d = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmmss");
        String filename = sdf1.format(d);

        //前端封装的查询参数
        Map<String, Object> params = new HashMap<String, Object>();
        if(Tools.isEmpty(startDate) || Tools.isEmpty(endDate)){
            params.put("start_time", "");
            params.put("end_time", "");
        }else{
            params.put("start_time", startDate+ " 00:00:00");
            params.put("end_time", endDate+ " 00:00:00");
        }


        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        List<PageData> varList = new ArrayList<PageData>();
        List<PsalesOrder> list = salesOrderService.findSalesOrderList(params);
        titles.add("姓名"); // 1
        titles.add("订单数量"); // 2
        titles.add("久零券金额");//3
        titles.add("零购券金额");//4
        titles.add("体验券金额");//5
        titles.add("服务费合计"); // 6
        titles.add("90贝支付金额");// 7
        titles.add("额外支付金额");// 8

        for (int i = 0; i < list.size(); i++) {
            PageData vpd = new PageData();
            PsalesOrder salesOrder = list.get(i);
            vpd.put("var1", salesOrder.getSalesName()==null?"":salesOrder.getSalesName()); // 1
            vpd.put("var2", salesOrder.getOrderCount()==null?"":salesOrder.getOrderCount()); // 1
            vpd.put("var3", salesOrder.getPayCoupon()==null?"":salesOrder.getPayCouponJL()); // 1
            vpd.put("var4", salesOrder.getCash_total()==null?"":salesOrder.getPayCouponLG()); // 1
            vpd.put("var5", salesOrder.getCoinPayTotal()==null?"":salesOrder.getPayCouponTY()); // 1
            vpd.put("var6", salesOrder.getOrder_total()==null?"":salesOrder.getCash_total()); // 1
            vpd.put("var7", salesOrder.getOrder_total()==null?"":salesOrder.getCoinPayTotal()); // 1
            vpd.put("var8", salesOrder.getOrder_total()==null?"":salesOrder.getOrder_total()); // 1

            varList.add(vpd);
        }

        dataMap.put("titles", titles);
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView(); // 执行excel操作
        erv.setNewFileName(filename);
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }
}
