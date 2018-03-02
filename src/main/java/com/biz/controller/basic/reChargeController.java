package com.biz.controller.basic;

import com.alibaba.fastjson.JSON;
import com.biz.conf.Global;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.JiuLingCoinHistory;
import com.biz.service.basic.RechargeServiceI;
import com.framework.controller.BaseController;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.utils.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * xy 2017/03/07
 */
@Controller
@RequestMapping("/recharge")
public class reChargeController extends BaseController {

    @Autowired
    private RechargeServiceI rechargeService;

    @InitBinder("jiuLingCoinHistory")
    public void initBinderFormBean1(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("jiuLingCoinHistory.");
    }


    @RequestMapping("toRecharge")
    public ModelAndView toRecharge(ModelAndView mv){
        mv.setViewName("basic/recharge");
        return mv;
    }

    @RequestMapping("showHistory")
    public void showJqGrid(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("userName", request.getParameter("userName"));
        params.put("startDate", request.getParameter("startdate"));
        params.put("endDate", request.getParameter("enddate"));
        params.put("type", request.getParameter("type"));
        params.put("targetUser", request.getParameter("targetUser"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = Integer.parseInt(request.getParameter("offset"));

        Pager<JiuLingCoinHistory> pager = new Pager();
        pager.setParameters(params);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);

        pager = rechargeService.queryJiuLingCoinList(pager);

        Paging<JiuLingCoinHistory> paging = new Paging<>();
        paging.setRows(pager.getExhibitDatas());
        paging.setTotal((long) pager.getRecordCount());

        writeJson(paging, response);
    }


    /**
     * 查询充值90贝信息
     * @param response
     * @param userName
     * @param startdate
     * @param enddate
     * @param type
     * @throws Exception
     */
    @RequestMapping("/loadRCInfo")
    public void loadRCInfo(HttpServletResponse response,String userName,String startdate,String enddate,String type,String targetUser) throws Exception{
        User user= (User) getShiroAttribute("user");
        Map<String,String> map=new HashMap<>();
        map.put("userName",userName);
        map.put("startdate",startdate);
        map.put("enddate",enddate);
        map.put("type",type);
        map.put("targetUser",targetUser);
        map=rechargeService.loadRCInfo(map);
        writeJson(map,response);
    }


    /**
     * 跳转新增
     */
    @RequestMapping(value = "/toInsert")
    public ModelAndView toInsert() throws Exception
    {
        JiuLingCoinHistory jiuLingCoin_History = new JiuLingCoinHistory();

        mv.addObject("jiuLingCoin_History", jiuLingCoin_History);

        mv.setViewName("basic/recharge_detail");
        return mv;
    }

    /**
     * 跳转新增
     */
    @RequestMapping(value = "/queryUserList")
    public void toInsert(HttpServletResponse response) throws Exception
    {
        List<Map<String,String>> user_list = rechargeService.queryUserListForJiuLingCoin();
       writeJson(user_list, response);
    }

    /**
     * 保存新增信息
     */
    @RequestMapping(value = "/doInsert", method = RequestMethod.POST)
    public void doInsert(JiuLingCoinHistory jiuLingCoinHistory,HttpServletResponse response) throws Exception
    {
        User user = (User)getShiroAttribute("userinfo");
        String ss="保存失败";
        String orderId = UuidUtil.get32UUID();
        double amount = jiuLingCoinHistory.getAmount1();
        boolean flagBei = true;
        boolean flagQuan = true;

        Map<String,Object> baseUserMap = rechargeService.checkJiulingUser(jiuLingCoinHistory);
        if ("0".equals(jiuLingCoinHistory.getType())) {
            Map<String, String> mapQuan = new HashMap<>();
            JSONObject jSONObjectQuan = new JSONObject();
            jSONObjectQuan.put("brand_code", "");
            jSONObjectQuan.put("order_code", orderId);
            jSONObjectQuan.put("user_code", user.getId());
            jSONObjectQuan.put("open_id", baseUserMap.get("open_id"));
            jSONObjectQuan.put("type", 3);
            jSONObjectQuan.put("source", 0);
            jSONObjectQuan.put("source_msg", "充值90贝发券");
            jSONObjectQuan.put("balance_90", (int) (amount * 100));
            jSONObjectQuan.put("state", 2);
            jSONObjectQuan.put("commission", 0);
            jSONObjectQuan.put("tradeType", "");
            jSONObjectQuan.put("orderState", 0);
            jSONObjectQuan.put("orderTotal", 0);
            jSONObjectQuan.put("ticketType", "2");
            mapQuan.put("json", jSONObjectQuan.toString());
            System.out.print(jSONObjectQuan.toString());

            String requestUrl2 = ConfigUtil.get("QT_Url") + "api/balance/operUserBalance90.ac";
            String x2 = URLConectionUtil.httpURLConnectionPostDiy(requestUrl2, mapQuan);
            x2 = URLDecoder.decode(x2, "utf-8");
            Map<String, Object> resultMapQuan = JSON.parseObject(x2, Map.class);
            if(resultMapQuan!=null && resultMapQuan.get("flag").equals("0")) {
                //flagQuan = true;
            }else{
                flagQuan = false;
            }
        }

        String source="";
        if ("0".equals(jiuLingCoinHistory.getType())) {
            source="1";
        }else if ("1".equals(jiuLingCoinHistory.getType())) {
            source="3";
        }

        if(flagQuan == true){
            Map<String,String> mapBei=new HashMap<>();
            JSONObject jSONObjectBei = new JSONObject();
            jSONObjectBei.put("userId",baseUserMap.get("id"));
            jSONObjectBei.put("state","1");
            jSONObjectBei.put("orderNum",orderId);
            jSONObjectBei.put("source",source);
            jSONObjectBei.put("amount",amount);
            mapBei.put("json", jSONObjectBei.toString());
            System.out.print(jSONObjectBei.toString());
            String requestUrl = Global.getConfig("QT_Url")+"api/balance/operUserCoin90.ac";
            String x = URLConectionUtil.httpURLConnectionPostDiy(requestUrl, mapBei);
            x = URLDecoder.decode(x, "utf-8");
            if(!StringUtil.isNullOrEmpty(x) )
            {
                Map<String, Object> resultMapBei = JSON.parseObject(x, Map.class);
                if(resultMapBei!=null && resultMapBei.get("flag").equals("0")) {

                }else{
                    flagBei = false;
                }
            }


        }

        if (flagBei && flagQuan) {
            ss = "保存成功";
        }
        writeJson(ss,response);
    }

    /**
     * 保存新增信息
     */
    //@RequestMapping(value = "/doInsert", method = RequestMethod.POST)
    //public void doInsert(JiuLingCoinHistory jiuLingCoinHistory,HttpServletResponse response) throws Exception
    //{
    //    User user = (User)getShiroAttribute("userinfo");
    //    jiuLingCoinHistory.setId(getRndCode());
    //    jiuLingCoinHistory.setOpUser(user.getId());
    //    //判断目标客户是否在久零贝账户体系内,如果存在直接return 继续往下走，如果不存在，则新增
    //    String targetUserId =rechargeService.checkJiulingUser(jiuLingCoinHistory);
    //
    //    //充值金额处理   元变分
    //    jiuLingCoinHistory.setAmount((int)(jiuLingCoinHistory.getAmount1()*100));
    //    boolean f= rechargeService.addTargetJiulingCoin(jiuLingCoinHistory);
    //
    //    jiuLingCoinHistory.setTargetUser(targetUserId);
    //    jiuLingCoinHistory.setReason(new StringBuilder("系统后台处理理由：").append(jiuLingCoinHistory.getReason()).toString());
    //    jiuLingCoinHistory.setState(jiuLingCoinHistory.getAmount1()>0?1:2);
    //    jiuLingCoinHistory.setSource(jiuLingCoinHistory.getAmount1()>0?6:7);
    //    jiuLingCoinHistory.setAmount1(Math.abs(jiuLingCoinHistory.getAmount1()));
    //    jiuLingCoinHistory.setSerialNum(String.valueOf(Tools.getRandomNum()));
    //    boolean n =rechargeService.insertJiuLingHistory(jiuLingCoinHistory);
    //    String ss="保存失败";
    //    if(f&&n){
    //        ss="保存成功";
    //    }
    //    writeJson(ss,response);
    //}

}
