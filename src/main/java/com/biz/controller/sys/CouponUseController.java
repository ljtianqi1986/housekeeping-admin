package com.biz.controller.sys;

import com.biz.model.Pmodel.basic.PbaseDetail;
import com.biz.service.base.CouponUseServiceI;
import com.framework.controller.BaseController;
import com.framework.model.Paging;
import com.framework.utils.ObjectExcelView;
import com.framework.utils.PageData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lzq on 2017/1/18.
 */
@Controller
@RequestMapping("/couponUse")
public class CouponUseController extends BaseController {

    @Resource(name = "couponUseService")
    private CouponUseServiceI couponUseService;

    /**
     * 跳转用券流水列表
     * @return
     */
    @RequestMapping("/toCouponUse")
    public ModelAndView toCouponUse(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sys/couponUse");
        return mv;
    }
    /**
     * 跳转用券明细
     * @return
     */
    @RequestMapping("/toDetail")
    public ModelAndView toDetail(String id){
        mv.clear();
        mv.addObject("id",id);
       // ModelAndView mv = new ModelAndView();
        mv.setViewName("sys/couponDetail");
        return mv;
    }

    /**
     * 跳转
     * @return
     */
    @RequestMapping("/toBase90Detail")
    public ModelAndView toBase90Detail(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sys/base90Detail");
        return mv;
    }

    /**
     * 跳转会员量
     * @return
     */
    @RequestMapping("/toMember")
    public ModelAndView toMember(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sys/member");
        return mv;
    }

    /**
     * 跳转90卡使用统计
     * @return
     */
    @RequestMapping("/to90UseCount")
    public ModelAndView to90UseCount(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sys/90UseCount");
        return mv;
    }

    /**
     * 加载订单列表
     * @return
     */
    @RequestMapping("/queryCouponUseDetail")
    public @ResponseBody void queryCouponUseDetail(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map map = getParameterByRequest(request);

        Paging paging= couponUseService.queryCouponUseDetail(map);

        writeJsonNoReplace(paging, response);
    }

    /**
     * 加载订单列表
     * @return
     */
    @RequestMapping("/loadTJInfo")
    public void loadTJInfo(HttpServletResponse response, HttpServletRequest request,String start,String end,String phone)throws Exception{
        //前端封装的查询参数
        Map<String,String> map = new HashMap<>();
     /*   if(!StringUtil.isNullOrEmpty(start))
        {start=start+" 00:00:00";}
        if(!StringUtil.isNullOrEmpty(end))
        {end=end+" 23:59:59";}*/
        map.put("startDate",start);
        map.put("endDate",end);
        map.put("phone",phone);
        Map<String,Object> paging= couponUseService.loadTJInfo(map);

        writeJsonNoReplace(paging, response);
    }

    /**
     * 加载90列表
     * @return
     */
    @RequestMapping("/queryBase90Detail")
    public @ResponseBody void queryBase90Detail(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map map = getParameterByRequest(request);

        Paging paging= couponUseService.queryBase90Detail(map);

        writeJsonNoReplace(paging, response);
    }

    /**
     * 加载统计数据
     * @return
     */
    @RequestMapping("/loadSumInfo")
    public void loadSumInfo(HttpServletResponse response, HttpServletRequest request,String start,String end,String name,String personName,String branName)throws Exception{
        //前端封装的查询参数
        Map<String,String> map = new HashMap<>();
        map.put("startDate",start);
        map.put("endDate",end);
        map.put("name",name);
        map.put("personName",personName);
        map.put("branName",branName);
        Map<String,Object> paging= couponUseService.loadSumInfo(map);
        writeJsonNoReplace(paging, response);
    }

    /**
     * 加载会员量
     * @return
     */
    @RequestMapping("/queryMember")
    public @ResponseBody void queryMember(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map map = getParameterByRequest(request);

        Paging paging= couponUseService.queryMember(map);

        writeJsonNoReplace(paging, response);
    }

    /**
     * 加载会员页面统计
     * @return
     */
    @RequestMapping("/loadSumMember")
    public void loadSumMember(HttpServletResponse response, HttpServletRequest request,String start,String end,String phone)throws Exception{
        //前端封装的查询参数
        Map<String,String> map = new HashMap<>();
        map.put("startDate",start);
        map.put("endDate",end);
        map.put("phone",phone);
        List<Map<String,Object>> paging= couponUseService.loadSumMember(map);
        writeJsonNoReplace(paging, response);
    }

    /**
     * 加载90统计
     * @return
     */
    @RequestMapping("/query90UseCount")
    public @ResponseBody void query90UseCount(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map map = getParameterByRequest(request);

        Paging paging= couponUseService.query90UseCount(map);

        writeJsonNoReplace(paging, response);
    }

    @RequestMapping(value = "doExcel")
    public ModelAndView doExcel(String phone,String start,String end) throws Exception {
        Date d = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmmss");
        String filename = sdf1.format(d);

        //前端封装的查询参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("startDate", start);
        params.put("endDate", end);
        params.put("phone",phone);
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        List<PageData> varList = new ArrayList<PageData>();
        List<PbaseDetail> list = couponUseService.findCouponList(params);
        titles.add("领取人"); // 1
        titles.add("手机号"); // 2
        titles.add("相关编号");//3
        titles.add("商户名称"); // 4
        titles.add("门店名称");// 5
        titles.add("来源");// 6
        titles.add("订单状态");// 7
        titles.add("用户券变化");// 8
        titles.add("服务费");// 9
        titles.add("支付方式");// 10
        titles.add("时间");// 11
        titles.add("使用途径");// 12
        titles.add("券种");// 13
        for (int i = 0; i < list.size(); i++) {
            PageData vpd = new PageData();
            PbaseDetail baseDetail = list.get(i);
            vpd.put("var1", baseDetail.getPerson_name()==null?"":baseDetail.getPerson_name()); // 1
            vpd.put("var2", baseDetail.getPhone()==null?"":baseDetail.getPhone()); // 1
            vpd.put("var3", baseDetail.getSourceId()==null?"":baseDetail.getSourceId()); // 1
            vpd.put("var4", baseDetail.getBrandName()==null?"":baseDetail.getBrandName()); // 1
            vpd.put("var5", baseDetail.getShopName()==null?"":baseDetail.getShopName()); // 1
            vpd.put("var6", baseDetail.getSourceMsg()==null?"":baseDetail.getSourceMsg()); // 1
            String state="";
            if(baseDetail.getState().equals("0")){
                state="未生效";
            }else if(baseDetail.getState().equals("1")){
                state="交易成功";
            }else if(baseDetail.getState().equals("2")&&baseDetail.getOrderType().equals("2")){
                state="错误";
            }else if(baseDetail.getState().equals("2")&&baseDetail.getOrderType().equals("1")){
                state="已退款";
            }else if(baseDetail.getState().equals("3")&&baseDetail.getOrderType().equals("2")){
                state="已退款";
            }else if(baseDetail.getState().equals("3")&&baseDetail.getOrderType().equals("1")){
                state="错误订单";
            }
            vpd.put("var7", state); // 1
            vpd.put("var8", baseDetail.getPoint90()==null?"":(double)Math.round(Double.valueOf(baseDetail.getPoint90())*100)/100); // 1
            vpd.put("var9", baseDetail.getServicePayTotal()==null?"":baseDetail.getServicePayTotal()); // 1
           String payType="";
            if(baseDetail.getPayType().equals("0")){
                payType="余额";
            }else if(baseDetail.getPayType().equals("1")){
                payType="银联";
            }else if(baseDetail.getPayType().equals("2")){
                payType="支付宝";
            }else if(baseDetail.getPayType().equals("3")){
                payType="微支付";
            }else if(baseDetail.getPayType().equals("5")){
                payType="微信(pay17)";
            }else if(baseDetail.getPayType().equals("7")){
                payType="线下支付";
            }else if(baseDetail.getPayType().equals("ZFB-MICROPAY")){
                payType="支付宝";
            }else if(baseDetail.getPayType().equals("UNIONPAY")){
                payType="银联";
            }else if(baseDetail.getPayType().equals("offline")){
                payType="线下支付";
            }else if(baseDetail.getPayType().equals("MICROPAY")){
                payType="微支付";
            }
            vpd.put("var10", payType); // 1
            vpd.put("var11",  baseDetail.getCreateTime()==null?"":baseDetail.getCreateTime()); // 1
            String type="";
            if(baseDetail.getType().equals("0")){
                type="pos机";
            }else if(baseDetail.getType().equals("1")){
                type="QT消券";
            }else if(baseDetail.getType().equals("2")){
                type="微商城";
            }else if(baseDetail.getType().equals("3")){
                type="远程发券";
            }else if(baseDetail.getType().equals("4")){
                type="实体卡";
            }
            vpd.put("var12", type); // 1
            if("0".equals(baseDetail.getTicketType()+""))
            { vpd.put("var13", "久零券"); }
            else if("1".equals(baseDetail.getTicketType()+""))
            { vpd.put("var13", "零购券"); }
            else if("2".equals(baseDetail.getTicketType()+""))
            { vpd.put("var13", "体验券"); }
            varList.add(vpd);
        }

        dataMap.put("titles", titles);
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView(); // 执行excel操作
        erv.setNewFileName(filename);
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }
    /**
     * 加载用券明细
     * @return
     */
    @RequestMapping("/findOrderInfoById")
    public void findOrderInfoById(HttpServletResponse response, HttpServletRequest request,String id)throws Exception{
        List<Map<String, Object>> quanDetail=new ArrayList<>();
        try{
            quanDetail=couponUseService.findOrderInfoById(id);
        }
        catch (Exception e)
        {}
        writeJson(quanDetail,response);
    }

}
