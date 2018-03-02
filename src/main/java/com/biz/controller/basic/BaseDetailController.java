package com.biz.controller.basic;

import com.biz.conf.Global;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.OrderMainUnion;
import com.biz.model.Pmodel.basic.*;
import com.biz.service.basic.BaseDetailServiceI;
import com.biz.service.basic.BizpersonServiceI;
import com.biz.service.home.HomeServiceI;
import com.framework.controller.BaseController;
import com.framework.model.Paging;
import com.framework.model.Params;
import com.framework.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 商户设置相关
 * Created by liujiajia on 2016/11/14.
 */
@Controller
@RequestMapping("/baseDetail")
public class BaseDetailController extends BaseController{

    @Autowired
    private BaseDetailServiceI baseDetailService;
    @Autowired
    private HomeServiceI homeService;
    @Autowired
    private BizpersonServiceI bizpersonService;
    @InitBinder("pbaseDetail")
    public void initBinderFormBean1(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("pbaseDetail.");
    }
    /**
     * 跳转用券统计
     * @param mv
     * @return
     */
    @RequestMapping("toBaseDetailUse")
    public ModelAndView toBaseDetailUse(ModelAndView mv){
        mv.setViewName("basic/baseDetail/useQuery");
        return mv;
    }

    /**
     * 跳转运营报表
     * @param mv
     * @return
     */
    @RequestMapping("toDayData")
    public ModelAndView toDayData(ModelAndView mv) throws Exception {
        mv.clear();
        String dateString= laterDate(0);
        String cityId= Global.getLocalCity();
        Parea area=homeService.getAreaById(cityId);
        mv.addObject("nowDay",dateString);
        mv.addObject("area",area);
        mv.setViewName("basic/baseDetail/dayData");
        return mv;
    }

    /**
     * 当前日期 指定天数之前的日期
     * @param days
     * @return
     */
    public String laterDate(int days) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Date temp_date = null;
        try {
            Date d =new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.DATE, -days);
            temp_date = c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return format.format(temp_date);
    }

    /**
     * 跳转发券流水
     * @param mv
     * @return
     */
    @RequestMapping("toBaseDetail")
    public ModelAndView toBaseDetail(ModelAndView mv){
        mv.setViewName("basic/baseDetail/query");
        return mv;
    }
    /**
     * 跳转久零贝统计
     * @param mv
     * @return
     */
    @RequestMapping("toCoinQuery")
    public ModelAndView toCoinQuery(ModelAndView mv){
        mv.clear();

        mv.setViewName("basic/baseDetail/coinQuery");
        return mv;
    }

    /**
     * 发券流水分页显示
     * @return
     */
    @RequestMapping("showBaseDetail")
    public void showBaseDetail(HttpServletResponse response, HttpServletRequest request,String type) throws Exception {
        //必要的分页参数
        User user= (User) getShiroAttribute("user");
        SqlFactory factory=new SqlFactory();
        Params sqlParams=factory.getParams();
        sqlParams.setOrder(request.getParameter("order"));
        sqlParams.setPage(Integer.parseInt(request.getParameter("offset")));
        sqlParams.setRows(Integer.parseInt(request.getParameter("limit")));
        sqlParams.setSort(request.getParameter("sort"));
        sqlParams.getParm().put("start", request.getParameter("start"));
        sqlParams.getParm().put("end",request.getParameter("end"));
        sqlParams.getParm().put("type",type);
        sqlParams.getParm().put("brand",request.getParameter("brand"));
        sqlParams.getParm().put("shop",request.getParameter("shop"));
        sqlParams.getParm().put("writeOffType",request.getParameter("writeOffType"));

        if(2==user.getIdentity())
        {
            sqlParams.getParm().put("agent",user.getIdentity_code());
        }
        else
        {
            sqlParams.getParm().put("agent",request.getParameter("agent"));
        }
        if(3==user.getIdentity())
        {
            sqlParams.getParm().put("brand",user.getIdentity_code());
        }
        else
        {
            sqlParams.getParm().put("brand",request.getParameter("brand"));
        }
        if(4==user.getIdentity())
        {
            sqlParams.getParm().put("shop",user.getIdentity_code());
        }
        else
        {
            sqlParams.getParm().put("shop",request.getParameter("shop"));
        }
        sqlParams.getParm().put("phone",request.getParameter("phone"));
        if(!"1".equals(type)) {
            //手动发券和实体卡
            if("4".equals(type)){
                sqlParams.getParm().put("cardType",request.getParameter("cardType"));
            }
            Paging paging = baseDetailService.findBaseDetailGrid(sqlParams);
            writeJson(paging,response);
        }else
        {
            //自动发券
            sqlParams.getParm().put("tradeType",request.getParameter("tradeType"));
            Paging paging = baseDetailService.findBaseDetailAutoGrid(sqlParams);
            writeJson(paging,response);
        }

    }
    /**
     * 发券统计分页显示
     * @return
     */
    @RequestMapping("showBaseDetailStatistics")
    public void showBaseDetailStatistics(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //必要的分页参数
        User user= (User) getShiroAttribute("user");
        SqlFactory factory=new SqlFactory();
        Params sqlParams=factory.getParams();
        sqlParams.setOrder(request.getParameter("order"));
        sqlParams.setPage(Integer.parseInt(request.getParameter("offset")));
        sqlParams.setRows(Integer.parseInt(request.getParameter("limit")));
        sqlParams.setSort(request.getParameter("sort"));

        sqlParams.getParm().put("type",request.getParameter("type"));
        sqlParams.getParm().put("code",request.getParameter("code"));

        sqlParams.getParm().put("dataType",request.getParameter("dataType"));
      /*  String timeType=request.getParameter("time");
        String startTime= DateUtil.dateToSimpleString(new Date());
        String endTime=changeToEndTime(timeType);*/
        sqlParams.getParm().put("startTime",request.getParameter("start"));
        sqlParams.getParm().put("endTime",request.getParameter("end"));
        sqlParams.getParm().put("source",request.getParameter("source"));
        if(2==user.getIdentity())
        {
            sqlParams.getParm().put("agent",user.getIdentity_code());
        }
        if(3==user.getIdentity())
        {
            sqlParams.getParm().put("brand",user.getIdentity_code());
        }
        Paging paging=baseDetailService.showBaseDetailStatistics(sqlParams);

        writeJson(paging,response);
    }

    private String changeToEndTime(String timeType) {
        int time=Integer.valueOf(timeType);
        Date now=new Date();
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE,0-time);
        String endTime=DateUtil.dateToSimpleString(c.getTime());
        return  endTime;
    }

    /**
     * 跳转详情
     * @return
     */
    @RequestMapping("getDetailInfo")
    public void getDetailInfo(HttpServletResponse response, HttpServletRequest request,String id,String type) throws Exception {
        PbaseDetail detail=new PbaseDetail();
        if("1".equals(type))
        {
            detail=baseDetailService.findDetailAuto(id);
        }
        else if("2".equals(type))
        {}
        else if("4".equals(type))
        {}
        writeJson(detail,response);
    }

    /**
     * 跳转发券统计
     * @param mv
     * @return
     */
    @RequestMapping("toBaseDetailStatistics")
    public ModelAndView toBaseDetailStatistics(ModelAndView mv){
        mv.clear();
        String nowDate=DateUtil.dateToSimpleString(new Date());
        mv.addObject("nowDate",nowDate);
        mv.setViewName("basic/baseDetail/queryStatistics");
        return mv;
    }
    /**
     * 根据类型加载统计页面选择框的内容(商户/代理商/业务代表)
     * @param type
     * @return
     */
    @RequestMapping("getStatisticsListForSelect")
    public void getStatisticsListForSelect(HttpServletResponse response, HttpServletRequest request,String type) throws Exception {
        List<Map<String,String>> list=new ArrayList<>();

        User user= (User) getShiroAttribute("user");
        if("1".equals(type))//1,读取商户
        {
            List<Pbrand> brand_list = homeService.queryBrandByAgent(user.getIdentity_code(),user.getIdentity());//读取门店
            for(Pbrand p:brand_list)
            {
                Map<String,String> map=new HashMap<>();
                map.put("code",p.getBrandCode());
                map.put("name",p.getName());
                list.add(map);
            }
        }
        else if("2".equals(type))//2，读取代理商
        {
            List<Pagent> agent_list = homeService.queryAgentss(user.getIdentity_code(),user.getIdentity());//读取代理商
            for(Pagent p:agent_list)
            {
                Map<String,String> map=new HashMap<>();
                map.put("code",p.getAgent_code());
                map.put("name",p.getAgent_name());
                list.add(map);
            }
        }
        else if("3".equals(type))//3，读取业务代表
        {   List<Pbizperson> bizperson_list=bizpersonService.showBizPersonForList();
            for(Pbizperson p:bizperson_list)
            {
                Map<String,String> map=new HashMap<>();
                map.put("code",p.getCode());
                map.put("name",p.getPerson_name());
                list.add(map);
            }
        }

        writeJson(list,response);
    }



    /**
     * 根据类型加载统计页面选择框的内容(商户/代理商/业务代表)
     * @param type
     * @return
     */
    @RequestMapping("getStatisticsChart")
    public void getStatisticsChart(HttpServletResponse response, HttpServletRequest request,String type,String code,String dataType,String time) throws Exception {
        User user= (User) getShiroAttribute("user");
        String startTime= request.getParameter("start");
        String endTime=request.getParameter("end");
        String source=request.getParameter("source");
        Map<String, Object> data = baseDetailService.queryGiftData(user,type,code,dataType,startTime,endTime,source);

        writeJson(data,response);
    }



    /**
     * 自动发券汇总
     * @return
     */
    @RequestMapping("loadZDInfo")
    public void loadZDInfo(HttpServletResponse response,String agent,String brand,String shop,String start,String end,String phone,String tradeType,String writeOffType) throws Exception {
        User user= (User) getShiroAttribute("user");
        Map<String,String> map=new HashMap<>();
        if(2==user.getIdentity())
        {
            map.put("agent",user.getIdentity_code());
        }
        else
        {
            map.put("agent",agent);
        }
        if(3==user.getIdentity())
        {
            map.put("brand",user.getIdentity_code());
        }
        else
        {
            map.put("brand",brand);
        }
        if(4==user.getIdentity())
        {
            map.put("shop",user.getIdentity_code());
        }
        else
        {
            map.put("shop",shop);
        }
        map.put("writeOffType",writeOffType);
        map.put("phone",phone);
        map.put("tradeType",tradeType);
        map.put("start",start);
        map.put("end",end);
        map=baseDetailService.loadZDInfo(map);
        writeJson(map,response);
    }
    /**
     * 手动发券汇总
     * @return
     */
    @RequestMapping("loadSDInfo")
    public void loadSDInfo(HttpServletResponse response,String agent,String brand,String shop,String start,String end,String writeOffType,String phone) throws Exception {
        User user= (User) getShiroAttribute("user");
        Map<String,String> map=new HashMap<>();
        if(2==user.getIdentity())
        {
            map.put("agent",user.getIdentity_code());
        }
        else
        {
            map.put("agent",agent);
        }
        if(3==user.getIdentity())
        {
            map.put("brand",user.getIdentity_code());
        }
        else
        {
            map.put("brand",brand);
        }
        if(4==user.getIdentity())
        {
            map.put("shop",user.getIdentity_code());
        }
        else
        {
            map.put("shop",shop);
        }
        map.put("start",start);
        map.put("phone",phone);
        map.put("end",end);
        map.put("writeOffType",writeOffType);
        map=baseDetailService.loadSDInfo(map);
        writeJson(map,response);
    }
    /**
     * 手动发券汇总
     * @return
     */
    @RequestMapping("loadSTKInfo")
    public void loadSTKInfo(HttpServletResponse response,String start,String end,String type,String phone,String writeOffType) throws Exception {

        Map<String,String> map=new HashMap<>();
        map.put("start",start);
        map.put("end",end);
        map.put("phone",phone);
        map.put("type",type);
        map.put("writeOffType",writeOffType);
        map=baseDetailService.loadSTKInfo(map);
        writeJson(map,response);
    }


    @RequestMapping(value = "doExcel")
    public ModelAndView doExcel(String type,String agent,String brand,String shop,String start,String end,String phone,String tradeType,String cardTypeId,String writeOffType) throws Exception {
        Date d = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmmss");
        String filename = sdf1.format(d);
        DecimalFormat df = new DecimalFormat("#.00");
        //前端封装的查询参数
        Map<String, Object> params = new HashMap<String, Object>();
        User user= (User) getShiroAttribute("user");
        if(2==user.getIdentity())
        {
            params.put("agent",user.getIdentity_code());
        }
        else
        {
            params.put("agent",agent);
        }
        if(3==user.getIdentity())
        {
            params.put("brand",user.getIdentity_code());
        }
        else
        {
            params.put("brand",brand);
        }
        if(4==user.getIdentity())
        {
            params.put("shop",user.getIdentity_code());
        }
        else
        {
            params.put("shop",shop);
        }
      //  params.put("agent", agent);
       // params.put("brand", brand);

        if(Tools.isEmpty(writeOffType) || "undefined".equalsIgnoreCase(writeOffType)){
            writeOffType="";
        }

        params.put("start", start);
        params.put("end", end);
        params.put("type", type);
        params.put("phone",phone);
        params.put("tradeType",tradeType);
        params.put("cardType",cardTypeId);
        params.put("writeOffType",writeOffType);
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        List<PageData> varList = new ArrayList<PageData>();
        //手动发券和实体卡
        if (!"1".equals(type)) {

            List<PbaseDetail> list = baseDetailService.findBaseDetailList(params);

            if ("2".equals(type)) {
                titles.add("代理商"); // 1
                titles.add("商户"); // 2
                titles.add("门店");//3
                titles.add("发券金额"); // 4
                titles.add("交易状态");// 5
                titles.add("发券人");// 6
                titles.add("发券终端");// 7
                titles.add("领券人");// 8
                titles.add("领券手机号");// 9
                titles.add("发券时间");// 10
                titles.add("撤回时间");// 11
                if(1==user.getIdentity() || 2==user.getIdentity()) {
                    titles.add("核销状态");// 12
                    titles.add("正常/过期");// 13
                }
                titles.add("券种");// 14
                for (int i = 0; i < list.size(); i++) {
                    PageData vpd = new PageData();
                    PbaseDetail baseDetail = list.get(i);
                    vpd.put("var1", baseDetail.getAgentName()==null?"":baseDetail.getAgentName()); // 1
                    vpd.put("var2", baseDetail.getBrandName()==null?"":baseDetail.getBrandName()); // 1
                    vpd.put("var3", baseDetail.getShopIdName()==null?"":baseDetail.getShopIdName()); // 1
                    vpd.put("var4", baseDetail.getPoint90()==null?"0":df.format(Double.valueOf(baseDetail.getPoint90()))); // 1
                    if (baseDetail.getOrderState()==null) {
                        vpd.put("var5", "未知"); // 1
                    }else if(baseDetail.getOrderState().equals("0")) {
                        vpd.put("var5", "未生效"); // 1
                    } else if (baseDetail.getOrderState().equals("1")) {
                        vpd.put("var5", "交易成功"); // 1
                    } else if (baseDetail.getOrderState().equals("2")) {
                        vpd.put("var5", "错误"); // 1
                    } else if (baseDetail.getOrderState().equals("3")) {
                        vpd.put("var5", "已退款"); // 1
                    }
                    vpd.put("var6", baseDetail.getOperUserName()==null?"":baseDetail.getOperUserName()); // 1
                    if (baseDetail.getType()==null) {
                        vpd.put("var7", "未知"); // 1
                    }else if(baseDetail.getType().equals("0")) {
                        vpd.put("var7", "pos机"); // 1
                    } else if (baseDetail.getType().equals("1")) {
                        vpd.put("var7", "QT"); // 1
                    } else if (baseDetail.getType().equals("2")) {
                        vpd.put("var7", "微商城"); // 1
                    } else if (baseDetail.getType().equals("3")) {
                        vpd.put("var7", "远程发券"); // 1
                    } else {
                        vpd.put("var7", "其他"); // 1
                    }
                    vpd.put("var8", baseDetail.getUserName()==null?"":baseDetail.getUserName()); // 1
                    vpd.put("var9", baseDetail.getUserPhone()==null?"":baseDetail.getUserPhone()); // 1
                    vpd.put("var10", baseDetail.getCreateTime()==null?"":baseDetail.getCreateTime()); // 1
                    vpd.put("var11", baseDetail.getCancelTime() == null ? "" : baseDetail.getCancelTime()); // 1
                    if(1==user.getIdentity() || 2==user.getIdentity()) {
                        if (baseDetail.getWriteOffType() == 0) {
                            vpd.put("var12", "未知"); // 1
                        } else if (baseDetail.getWriteOffType() == 1 || baseDetail.getWriteOffType() == 3) {
                            vpd.put("var12", "已核销"); // 1
                        } else if (baseDetail.getWriteOffType() == 2) {
                            vpd.put("var12", "未核销"); // 1
                        }
                        if (baseDetail.getWriteOffState() == 0) {
                            vpd.put("var13", "正常"); // 1
                        } else if (baseDetail.getWriteOffState() == 1) {
                            vpd.put("var13", "过期"); // 1
                        }
                    }
                    if("0".equals(baseDetail.getTicketType()+""))
                    { vpd.put("var14", "久零券"); }
                    else if("1".equals(baseDetail.getTicketType()+""))
                    { vpd.put("var14", "零购券"); }
                    else if("2".equals(baseDetail.getTicketType()+""))
                    { vpd.put("var14", "体验券"); }
                    varList.add(vpd);
                }
            } else if ("4".equals(type)) {
                titles.add("实体卡名称"); // 1
                titles.add("实体卡类型"); // 2
                titles.add("实体卡密钥");//3
                titles.add("实体卡号"); // 4
                titles.add("发券金额");// 5
                titles.add("领券人");// 6
                titles.add("领券手机号");// 7
                titles.add("发券时间");// 8
                if(1==user.getIdentity() || 2==user.getIdentity()) {
                    titles.add("核销状态");// 9
                    titles.add("正常/过期");// 10
                }
                titles.add("券种");// 11
                for (int i = 0; i < list.size(); i++) {
                    PageData vpd = new PageData();
                    PbaseDetail baseDetail = list.get(i);
                    vpd.put("var1", baseDetail.getCardName()==null?"":baseDetail.getCardName()); // 1
                    vpd.put("var2", baseDetail.getCardType()==null?"":baseDetail.getCardType()); // 1
                    vpd.put("var3", baseDetail.getCardCode()==null?"":baseDetail.getCardCode()); // 1
                    vpd.put("var4", baseDetail.getSourceId()); // 1
                    vpd.put("var5", df.format(Double.valueOf(baseDetail.getPoint90()))); // 1
                    vpd.put("var6", baseDetail.getUserName()); // 1
                    vpd.put("var7", baseDetail.getUserPhone()); // 1
                    vpd.put("var8", baseDetail.getCreateTime()); // 1
                    if(1==user.getIdentity() || 2==user.getIdentity()) {
                        if (baseDetail.getWriteOffType() == 0) {
                            vpd.put("var9", "未知"); // 1
                        } else if (baseDetail.getWriteOffType() == 1 || baseDetail.getWriteOffType() == 3) {
                            vpd.put("var9", "已核销"); // 1
                        } else if (baseDetail.getWriteOffType() == 2) {
                            vpd.put("var9", "未核销"); // 1
                        }
                        if (baseDetail.getWriteOffState() == 0) {
                            vpd.put("var10", "正常"); // 1
                        } else if (baseDetail.getWriteOffState() == 1) {
                            vpd.put("var10", "过期"); // 1
                        }
                    }
                    if("0".equals(baseDetail.getTicketType()+""))
                    { vpd.put("var11", "久零券"); }
                    else if("1".equals(baseDetail.getTicketType()+""))
                    { vpd.put("var11", "零购券"); }
                    else if("2".equals(baseDetail.getTicketType()+""))
                    { vpd.put("var11", "体验券"); }
                    varList.add(vpd);
                }
            }
        } else {
            //自动发券

            List<OrderMainUnion> list2 = baseDetailService.findBaseDetailAutoList(params);
            titles.add("代理商"); // 1
            titles.add("商户"); // 2
            titles.add("门店");//3
            titles.add("订单编号"); // 4
            titles.add("订单金额");// 5
            titles.add("发券金额");// 6
            titles.add("发券费率");// 7
            titles.add("发券费用");// 8
            titles.add("交易状态");// 9
            titles.add("收银员");// 10
            titles.add("支付类型");// 11
            titles.add("领券人");// 12
            titles.add("领券手机号");// 13
            titles.add("发券时间");// 14
            if(1==user.getIdentity() || 2==user.getIdentity()) {
                titles.add("核销状态");// 15
                titles.add("正常/过期");// 16
            }
            titles.add("券种");// 17
            for (int i = 0; i < list2.size(); i++) {
                PageData vpd = new PageData();
                OrderMainUnion ordermain = list2.get(i);
                vpd.put("var1", ordermain.getAgent_name()); // 1
                vpd.put("var2", ordermain.getBrand_name()); // 1
                vpd.put("var3", ordermain.getBusiness_name()); // 1
                vpd.put("var4", ordermain.getCode()); // 1
                vpd.put("var5", ordermain.getOrder_total() / 100); // 1
                if (ordermain.getState() != 2) {
                    vpd.put("var6", ordermain.getGift_90() / 100); // 1
                } else {
                    vpd.put("var6", 0); // 1
                }
                vpd.put("var7", ordermain.getCommission() + "%"); // 1
                if (ordermain.getState() != 2) {
                    vpd.put("var8", ordermain.getCard_fee()); // 1
                } else {
                    vpd.put("var8", 0); // 1
                }
                if (ordermain.getState() == 0) {
                    vpd.put("var9", "未生效"); // 1
                } else if (ordermain.getState() == 1) {
                    vpd.put("var9", "交易成功"); // 1
                } else if (ordermain.getState() == 2) {
                    vpd.put("var9", "错误"); // 1
                } else if (ordermain.getState() == 3) {
                    vpd.put("var9", "已退款"); // 1
                }
                vpd.put("var10", ordermain.getPerson_name()); // 1

                if (ordermain.getTrade_type().equals("MICROPAY")) {
                    vpd.put("var11", "微支付"); // 1
                } else if (ordermain.getTrade_type().equals("ZFB-MICROPAY")) {
                    vpd.put("var11", "支付宝"); // 1
                } else if (ordermain.getTrade_type().equals("UNIONPAY")) {
                    vpd.put("var11", "银联"); // 1
                } else if (ordermain.getTrade_type().equals("offline")) {
                    vpd.put("var11", "线下支付"); // 1
                }

                vpd.put("var12", ordermain.getUserName()); // 1
                vpd.put("var13", ordermain.getUserPhone()); // 1
                vpd.put("var14", ordermain.getCreate_time()); // 1
                if(1==user.getIdentity() || 2==user.getIdentity()) {
                    if (ordermain.getWriteOffType() == 0) {
                        vpd.put("var15", "未知"); // 1
                    } else if (ordermain.getWriteOffType() == 1 || ordermain.getWriteOffType() == 3) {
                        vpd.put("var15", "已核销"); // 1
                    } else if (ordermain.getWriteOffType() == 2) {
                        vpd.put("var15", "未核销"); // 1
                    }
                    if (ordermain.getWriteOffState() == 0) {
                        vpd.put("var16", "正常"); // 1
                    } else if (ordermain.getWriteOffState() == 1) {
                        vpd.put("var16", "过期"); // 1
                    }
                    if("0".equals(ordermain.getTicketType()))
                    { vpd.put("var17", "久零券"); }
                    else if("1".equals(ordermain.getTicketType()))
                    { vpd.put("var17", "零购券"); }
                    else if("2".equals(ordermain.getTicketType()))
                    { vpd.put("var17", "体验券"); }
                }
                varList.add(vpd);
            }

        }

        dataMap.put("titles", titles);
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView(); // 执行excel操作
        erv.setNewFileName(filename);
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }





    /**
     * 发券统计分页显示
     * @return
     */
    @RequestMapping("showCoinStatistics")
    public void showCoinStatistics(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //必要的分页参数
        SqlFactory factory=new SqlFactory();
        Params sqlParams=factory.getParams();
        sqlParams.setOrder(request.getParameter("order"));
        sqlParams.setPage(Integer.parseInt(request.getParameter("offset")));
        sqlParams.setRows(Integer.parseInt(request.getParameter("limit")));
        sqlParams.setSort(request.getParameter("sort"));

        sqlParams.getParm().put("customer",request.getParameter("customer"));
        sqlParams.getParm().put("start",request.getParameter("start"));
        sqlParams.getParm().put("end",request.getParameter("end"));

        Paging paging=baseDetailService.showCoinStatistics(sqlParams);

        writeJson(paging,response);
    }


    /**
     * 手动发券汇总
     * @return
     */
    @RequestMapping("loadJLBInfo")
    public void loadJLBInfo(HttpServletResponse response,String customer,String start,String end) throws Exception {
        Map<String,String> map=new HashMap<>();
        map.put("customer",customer);
        map.put("start",start);
        map.put("end",end);
        map=baseDetailService.loadJLBInfo(map);
        writeJson(map,response);
    }


    @RequestMapping(value = "/deBase90Detail", method = RequestMethod.POST)
    public void deBase90Detail(HttpServletResponse response, String id) throws Exception {
        Map<String,Object> r_map=new HashMap<>();
        try{
            r_map=baseDetailService.deBase90Detail(id.trim());
        }catch (Exception e){
            r_map.put("success",false);
            r_map.put("msg", "未知错误");
        }
        writeJson(r_map,response);
    }



    /**
     * 自动发券汇总
     * @return
     */
    @RequestMapping("loadUseInfo")
    public void loadUseInfo(HttpServletResponse response,String start,String end) throws Exception {
        User user= (User) getShiroAttribute("user");
        Map<String,Object> map=new HashMap<>();
        map.put("identity",user.getIdentity()+"");
         map.put("identity_code",user.getId());
        map.put("startDate",start);
        map.put("endDate",end);
        map=baseDetailService.loadUseInfo(map);
        writeJson(map,response);
    }

    /**
     * 获取实体卡类型
     */
    @RequestMapping("/getOfflineCardTypeForSelect")
    public void getOfflineCardTypeForSelect(HttpServletResponse response) throws Exception {
        List<Map<String,Object>> map = baseDetailService.getOfflineCardTypeForSelect();

        writeJson(map,response);
    }

    /**
     * 获取昨日报表数据
     */
    //@RequestMapping("/getYesTodayData")
    //public void getYesTodayData(HttpServletResponse response) throws Exception {
    //    PyestodayReport report=new PyestodayReport();
    //    String dateString= laterDate(0);
    //    try{
    //        report=baseDetailService.getYesTodayData(dateString);
    //    }
    //    catch (Exception e)
    //    {
    //        e.getMessage();
    //    }
    //    writeJson(report,response);
    //}

    /**
     * 获取昨日报表数据
     */
    @RequestMapping("/getDataByDate")
    public void getDataByDate(HttpServletResponse response,String date) throws Exception {
        PyestodayReport report=new PyestodayReport();
        //String dateString= laterDate(0);
        try{
            report=baseDetailService.getDataByDate(date);
        }
        catch (Exception e)
        {
            e.getMessage();
        }
        writeJson(report,response);
    }



    /**
     * 其它发券流水
     * @return
     */
    @RequestMapping("/toOtherBaseDetail")
    public ModelAndView toOtherBaseDetail(ModelAndView mv){
        mv.clear();

        mv.setViewName("basic/otherBaseDetail/query");
        return mv;
    }

    /**
     * 首次关注送券分页显示
     * @return
     */
    @RequestMapping("/showFirstConcern")
    public void showFirstConcern(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //必要的分页参数
        User user= (User) getShiroAttribute("user");
        SqlFactory factory=new SqlFactory();
        Params sqlParams=factory.getParams();
        sqlParams.setPage(Integer.parseInt(request.getParameter("offset")));
        sqlParams.setRows(Integer.parseInt(request.getParameter("limit")));
        sqlParams.setSort(request.getParameter("sort"));
        sqlParams.getParm().put("start", request.getParameter("start"));
        sqlParams.getParm().put("end",request.getParameter("end"));
        sqlParams.getParm().put("brand",request.getParameter("brand"));
        sqlParams.getParm().put("shop",request.getParameter("shop"));

        sqlParams.getParm().put("phone",request.getParameter("phone"));
       if(2==user.getIdentity())
        {
            sqlParams.getParm().put("agent",user.getIdentity_code());
        }
        else
        {
            sqlParams.getParm().put("agent",request.getParameter("agent"));
        }
        if(3==user.getIdentity())
        {
            sqlParams.getParm().put("brand",user.getIdentity_code());
        }
        else
        {
            sqlParams.getParm().put("brand",request.getParameter("brand"));
        }
        if(4==user.getIdentity())
        {
            sqlParams.getParm().put("shop",user.getIdentity_code());
        }
        else
        {
            sqlParams.getParm().put("shop",request.getParameter("shop"));
        }

        Paging paging = baseDetailService.findFirstConcernGrid(sqlParams);
        writeJson(paging,response);
    }

    /**
     * 首次关注送券分页显示
     * @return
     */
    @RequestMapping("/showCharge90Coin")
    public void showCharge90Coin(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //必要的分页参数
        User user= (User) getShiroAttribute("user");
        SqlFactory factory=new SqlFactory();
        Params sqlParams=factory.getParams();
        sqlParams.setPage(Integer.parseInt(request.getParameter("offset")));
        sqlParams.setRows(Integer.parseInt(request.getParameter("limit")));
        sqlParams.setSort(request.getParameter("sort"));
        sqlParams.getParm().put("start", request.getParameter("start"));
        sqlParams.getParm().put("end",request.getParameter("end"));
        sqlParams.getParm().put("brand",request.getParameter("brand"));
        sqlParams.getParm().put("shop",request.getParameter("shop"));

        sqlParams.getParm().put("phone",request.getParameter("phone"));
        if(2==user.getIdentity())
        {
            sqlParams.getParm().put("agent",user.getIdentity_code());
        }
        else
        {
            sqlParams.getParm().put("agent",request.getParameter("agent"));
        }
        if(3==user.getIdentity())
        {
            sqlParams.getParm().put("brand",user.getIdentity_code());
        }
        else
        {
            sqlParams.getParm().put("brand",request.getParameter("brand"));
        }
        if(4==user.getIdentity())
        {
            sqlParams.getParm().put("shop",user.getIdentity_code());
        }
        else
        {
            sqlParams.getParm().put("shop",request.getParameter("shop"));
        }
        Paging paging = baseDetailService.findCharge90CoinGrid(sqlParams);
        writeJson(paging,response);
    }


    /**
     * 首次关注送券分页显示
     * @return
     */
    @RequestMapping("/showRefundCoupons")
    public void showRefundCoupons(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //必要的分页参数
        User user= (User) getShiroAttribute("user");
        SqlFactory factory=new SqlFactory();
        Params sqlParams=factory.getParams();
        sqlParams.setOrder(request.getParameter("order"));
        sqlParams.setPage(Integer.parseInt(request.getParameter("offset")));
        sqlParams.setRows(Integer.parseInt(request.getParameter("limit")));
        sqlParams.setSort(request.getParameter("sort"));
        sqlParams.getParm().put("start", request.getParameter("start"));
        sqlParams.getParm().put("end",request.getParameter("end"));
        sqlParams.getParm().put("brand",request.getParameter("brand"));
        sqlParams.getParm().put("shop",request.getParameter("shop"));

        sqlParams.getParm().put("phone",request.getParameter("phone"));
        if(2==user.getIdentity())
        {
            sqlParams.getParm().put("agent",user.getIdentity_code());
        }
        else
        {
            sqlParams.getParm().put("agent",request.getParameter("agent"));
        }
        if(3==user.getIdentity())
        {
            sqlParams.getParm().put("brand",user.getIdentity_code());
        }
        else
        {
            sqlParams.getParm().put("brand",request.getParameter("brand"));
        }
        if(4==user.getIdentity())
        {
            sqlParams.getParm().put("shop",user.getIdentity_code());
        }
        else
        {
            sqlParams.getParm().put("shop",request.getParameter("shop"));
        }
        Paging paging = baseDetailService.findRefundCouponsGrid(sqlParams);
        writeJson(paging,response);
    }

    /**
     * 首次关注送券汇总
     * @return
     */
    @RequestMapping("/loadFCInfo")
    public void loadFCInfo(HttpServletResponse response,String agent,String brand,String shop,String start,String end) throws Exception{
        User user= (User) getShiroAttribute("user");
        Map<String,String> map=new HashMap<>();

        if(2==user.getIdentity())
        {
            map.put("agent",user.getIdentity_code());
        }else{
            map.put("agent",agent);
        }

        if(3==user.getIdentity())
        {
            map.put("brand",user.getIdentity_code());
        }else{
            map.put("brand",brand);
        }

        if(4==user.getIdentity())
        {
            map.put("shop",user.getIdentity_code());
        }else{
            map.put("shop",shop);
        }

        map.put("start",start);
        map.put("end",end);
        map=baseDetailService.loadFCInfo(map);
        writeJson(map,response);
    }

    /**
     * 充值90贝送券汇总
     * @return
     */
    @RequestMapping("/loadCCInfo")
    public void loadCCInfo(HttpServletResponse response,String agent,String brand,String shop,String start,String end) throws Exception{
        User user= (User) getShiroAttribute("user");
        Map<String,String> map=new HashMap<>();

        if(2==user.getIdentity())
        {
            map.put("agent",user.getIdentity_code());
        }else{
            map.put("agent",agent);
        }

        if(3==user.getIdentity())
        {
            map.put("brand",user.getIdentity_code());
        }else{
            map.put("brand",brand);
        }

        if(4==user.getIdentity())
        {
            map.put("shop",user.getIdentity_code());
        }else{
            map.put("shop",shop);
        }

        map.put("start",start);
        map.put("end",end);
        map=baseDetailService.loadCCInfo(map);
        writeJson(map,response);
    }


    /**
     * 退券汇总
     * @return
     */
    @RequestMapping("/loadRCInfo")
    public void loadRCInfo(HttpServletResponse response,String agent,String brand,String shop,String start,String end) throws Exception{
        User user= (User) getShiroAttribute("user");
        Map<String,String> map=new HashMap<>();

        if(2==user.getIdentity())
        {
            map.put("agent",user.getIdentity_code());
        }else{
            map.put("agent",agent);
        }

        if(3==user.getIdentity())
        {
            map.put("brand",user.getIdentity_code());
        }else{
            map.put("brand",brand);
        }

        if(4==user.getIdentity())
        {
            map.put("shop",user.getIdentity_code());
        }else{
            map.put("shop",shop);
        }

        map.put("start",start);
        map.put("end",end);
        map=baseDetailService.loadRCInfo(map);
        writeJson(map,response);
    }


    /**
     * 优惠券导出
     * @param type
     * @param agent
     * @param brand
     * @param shop
     * @param start
     * @param end
     * @param phone
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "doCouponsExcel")
    public ModelAndView doCouponsExcel(String type,String agent,String brand,String shop,String start,String end,String phone) throws Exception {
        Date d = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmmss");
        String filename = sdf1.format(d);
        DecimalFormat df = new DecimalFormat("#.00");
        //前端封装的查询参数
        Map<String, Object> params = new HashMap<String, Object>();
        User user= (User) getShiroAttribute("user");
        if(2==user.getIdentity())
        {
            params.put("agent",user.getIdentity_code());
        }
        else
        {
            params.put("agent",agent);
        }
        if(3==user.getIdentity())
        {
            params.put("brand",user.getIdentity_code());
        }
        else
        {
            params.put("brand",brand);
        }
        if(4==user.getIdentity())
        {
            params.put("shop",user.getIdentity_code());
        }
        else
        {
            params.put("shop",shop);
        }
        params.put("start", start);
        params.put("end", end);
        params.put("phone",phone);
        Map<String, Object> dataMap = new HashMap<String, Object>();

        if(type.equals("1")){//首次关注送券
            List<PbaseDetail> list = baseDetailService.findFirstConcernList(params);
            dataMap = stitchFirstConcernParam(list);
        }else if(type.equals("2")){//充值90贝送券
            List<PbaseDetail> list = baseDetailService.findCharge90CoinList(params);
            dataMap = stitchCharge90CoinParam(list);
        }else if(type.equals("3")){//退款退券
            List<PbaseDetail> list = baseDetailService.findRefundCouponsList(params);
            dataMap = stitchRefundCouponsParam(list);
        }else if(type.equals("4")){//扫码送券
            List<PbaseDetail> list = baseDetailService.findScanCouponsList(params);
            dataMap = stitchScanCouponsParam(list);
        }

        ObjectExcelView erv = new ObjectExcelView(); // 执行excel操作
        erv.setNewFileName(filename);
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }

    private Map<String,Object> stitchScanCouponsParam(List<PbaseDetail> list) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        List<PageData> varList = new ArrayList<PageData>();
        titles.add("代理商"); // 1
        titles.add("商户"); // 2
        titles.add("发券金额"); // 1
        titles.add("发券类型"); // 2
        titles.add("领券人");// 3
        titles.add("领券手机号");// 4
        titles.add("发券时间");// 5

        for (int i = 0; i < list.size(); i++) {
            PageData vpd = new PageData();
            PbaseDetail baseDetail = list.get(i);
            vpd.put("var1", baseDetail.getAgentName()); // 1
            vpd.put("var2", baseDetail.getBrandName()); // 1
            vpd.put("var3", baseDetail.getPoint90()); // 1
            if(0==baseDetail.getTicketType()){
                vpd.put("var4", "久零券");
            }
            else if(1==baseDetail.getTicketType())
            {    vpd.put("var4", "零购券"); }
            else if(2==baseDetail.getTicketType())
            {    vpd.put("var4", "体验券"); }
            else
            {    vpd.put("var4", "未知"); }

            vpd.put("var5", baseDetail.getUserName()); // 1
            vpd.put("var6", baseDetail.getUserPhone()); // 1
            vpd.put("var7", baseDetail.getCreateTime()); // 1

            varList.add(vpd);
        }

        dataMap.put("titles", titles);
        dataMap.put("varList", varList);
        return dataMap;
    }


    private Map<String,Object> stitchRefundCouponsParam(List<PbaseDetail> list){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        List<PageData> varList = new ArrayList<PageData>();
        titles.add("代理商"); // 1
        titles.add("商户"); // 2
        titles.add("门店");
        titles.add("订单编号");
        titles.add("订单金额");
        titles.add("发券金额"); // 4
        titles.add("领券人");// 5
        titles.add("领券手机号");// 6
        titles.add("发券时间");// 7

        for (int i = 0; i < list.size(); i++) {
            PageData vpd = new PageData();
            PbaseDetail baseDetail = list.get(i);
            vpd.put("var1", baseDetail.getAgentName()); // 1
            vpd.put("var2", baseDetail.getBrandName()); // 1
            vpd.put("var3", baseDetail.getShopIdName()); // 1

            vpd.put("var4", baseDetail.getOrderId()); // 1
            vpd.put("var5", baseDetail.getMoney()); // 1
            vpd.put("var6", baseDetail.getCouponsMoney()); // 1
            vpd.put("var7", baseDetail.getUserName()); // 1
            vpd.put("var8", baseDetail.getUserPhone()); // 1
            vpd.put("var9", baseDetail.getCreateTime()); // 1

            varList.add(vpd);
        }

        dataMap.put("titles", titles);
        dataMap.put("varList", varList);
        return dataMap;
    }

    private Map<String,Object> stitchCharge90CoinParam(List<PbaseDetail> list){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        List<PageData> varList = new ArrayList<PageData>();
        titles.add("代理商"); // 1
        titles.add("商户"); // 2
        titles.add("订单编号");
        titles.add("订单金额");
        titles.add("发券金额"); // 4
        titles.add("发券费率"); // 4
        titles.add("领券人");// 5
        titles.add("领券手机号");// 6
        titles.add("发券时间");// 7

        for (int i = 0; i < list.size(); i++) {
            PageData vpd = new PageData();
            PbaseDetail baseDetail = list.get(i);
            vpd.put("var1", baseDetail.getAgentName()); // 1
            vpd.put("var2", baseDetail.getBrandName()); // 1
            vpd.put("var3", baseDetail.getSourceId()); // 1
            vpd.put("var4", Integer.parseInt(baseDetail.getOrderTotal())/100); // 1
            vpd.put("var5", Integer.parseInt(baseDetail.getPoint90())/100); // 1
            vpd.put("var6", baseDetail.getCommission()+"%"); // 1
            vpd.put("var6", baseDetail.getUserName()); // 1
            vpd.put("var7", baseDetail.getUserPhone()); // 1
            vpd.put("var8", baseDetail.getCreateTime()); // 1

            varList.add(vpd);
        }

        dataMap.put("titles", titles);
        dataMap.put("varList", varList);
        return dataMap;
    }

    private Map<String,Object> stitchFirstConcernParam(List<PbaseDetail> list){
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();
        List<PageData> varList = new ArrayList<PageData>();
        titles.add("代理商"); // 1
        titles.add("商户"); // 2
        titles.add("门店");//3
        titles.add("发券金额"); // 4
        titles.add("领券人");// 5
        titles.add("领券手机号");// 6
        titles.add("发券时间");// 7

        for (int i = 0; i < list.size(); i++) {
            PageData vpd = new PageData();
            PbaseDetail baseDetail = list.get(i);
            vpd.put("var1", baseDetail.getAgentName()); // 1
            vpd.put("var2", baseDetail.getBrandName()); // 1
            vpd.put("var3", baseDetail.getShopIdName()); // 1
            vpd.put("var4", Integer.parseInt(baseDetail.getPoint90())/100); // 1
            vpd.put("var5", baseDetail.getUserName()); // 1
            vpd.put("var6", baseDetail.getUserPhone()); // 1
            vpd.put("var7", baseDetail.getCreateTime()); // 1

            varList.add(vpd);
        }

        dataMap.put("titles", titles);
        dataMap.put("varList", varList);
        return dataMap;
    }
    /**
     * 扫码送券分页显示
     * @return
     */
    @RequestMapping("/showScanCoupons")
    public void showScanCoupons(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //必要的分页参数
        User user= (User) getShiroAttribute("user");
        SqlFactory factory=new SqlFactory();
        Params sqlParams=factory.getParams();
        sqlParams.setPage(Integer.parseInt(request.getParameter("offset")));
        sqlParams.setRows(Integer.parseInt(request.getParameter("limit")));
        sqlParams.setSort(request.getParameter("sort"));
        sqlParams.getParm().put("start", request.getParameter("start"));
        sqlParams.getParm().put("end",request.getParameter("end"));
        sqlParams.getParm().put("brand",request.getParameter("brand"));
        sqlParams.getParm().put("shop",request.getParameter("shop"));

        sqlParams.getParm().put("phone",request.getParameter("phone"));
        if(2==user.getIdentity())
        {
            sqlParams.getParm().put("agent",user.getIdentity_code());
        }
        else
        {
            sqlParams.getParm().put("agent",request.getParameter("agent"));
        }
        if(3==user.getIdentity())
        {
            sqlParams.getParm().put("brand",user.getIdentity_code());
        }
        else
        {
            sqlParams.getParm().put("brand",request.getParameter("brand"));
        }
        if(4==user.getIdentity())
        {
            sqlParams.getParm().put("shop",user.getIdentity_code());
        }
        else
        {
            sqlParams.getParm().put("shop",request.getParameter("shop"));
        }

        Paging paging = baseDetailService.findScanGrid(sqlParams);
        writeJson(paging,response);
    }

    /**
     * 扫码发券汇总
     * @return
     */
    @RequestMapping("loadScanInfo")
    public void loadScanInfo(HttpServletResponse response,String agent,String brand,String shop,String start,String end,String phone) throws Exception {
        User user= (User) getShiroAttribute("user");
        Map<String,String> map=new HashMap<>();
        if(2==user.getIdentity())
        {
            map.put("agent",user.getIdentity_code());
        }
        else
        {
            map.put("agent",agent);
        }
        if(3==user.getIdentity())
        {
            map.put("brand",user.getIdentity_code());
        }
        else
        {
            map.put("brand",brand);
        }
        if(4==user.getIdentity())
        {
            map.put("shop",user.getIdentity_code());
        }
        else
        {
            map.put("shop",shop);
        }
        map.put("start",start);
        map.put("phone",phone);
        map.put("end",end);
        map=baseDetailService.loadScanInfo(map);
        writeJson(map,response);
    }
}