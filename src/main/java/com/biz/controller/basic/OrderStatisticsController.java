package com.biz.controller.basic;

import com.alibaba.fastjson.JSON;
import com.biz.model.Hmodel.TSysUser;
import com.biz.model.Pmodel.SysUser;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.BaseUser;
import com.biz.model.Pmodel.api.DetailVoucher;
import com.biz.model.Pmodel.basic.Brand;
import com.biz.model.Pmodel.basic.Pbrand;
import com.biz.service.api.ApiInterfaceServiceI;
import com.biz.service.api.OrderMainServiceI;
import com.biz.service.api.QueryTicketServiceI;
import com.biz.service.basic.MerchantServiceI;
import com.biz.service.basic.UserServiceI;
import com.biz.service.sys.SysUserServiceI;
import com.framework.controller.BaseController;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.utils.ConfigUtil;
import com.framework.utils.StringUtil;
import com.framework.utils.URLConectionUtil;
import com.framework.utils.UuidUtil;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 订单统计分析
 * @author Atom
 *
 */
@Controller
@RequestMapping(value = "/orderStatistics")
public class OrderStatisticsController extends BaseController {


    @Resource(name = "userService")
    private UserServiceI userService;
    @Resource(name = "apiInterfaceService")
    private ApiInterfaceServiceI apiInterfaceService;
    @Resource(name = "queryTicketService")
    private QueryTicketServiceI queryTicketService;
    @Resource(name = "orderMainService")
    private OrderMainServiceI orderMainService;
    @Resource(name = "merchantService")
    private MerchantServiceI merchantService;

    @InitBinder("pbrand")
    public void initBinderFormBean1(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("pbrand.");
    }


    @RequestMapping(value = "/doIssuedGift")
    public void doIssuedGift(String ticketType,String phone,String total,HttpServletResponse response) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();

        //品牌登录
        if(getShiroAttribute("identity_code")==null|| StringUtil.isNullOrEmpty(getShiroAttribute("identity_code"))){
            map.put("isok", false);
            map.put("msg","只有联盟商户才有此功能！");
        }else{
            Brand brand=  merchantService.getMerchantById((String)getShiroAttribute("identity_code"));
            if(brand!=null && brand.getIsTicket()==1)
            {
                User user = (User)getShiroAttribute("user");
                BaseUser baseUser =userService.findBaseUserByPhone(phone);
                String sid=userService.findSidByCode(user.getIdentity_code());
                //if(baseUser!=null&&!baseUser.getOpen_id().equals("")&&sid!=null&&baseUser.getShop_id()!=null&&baseUser.getShop_id().equals(sid)) {
                Map<String, String> map1 = new HashMap<>();
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("brand_code", getShiroAttribute("identity_code"));
                jSONObject.put("user_code", user.getId());
                jSONObject.put("order_code", UuidUtil.get32UUID());
                jSONObject.put("open_id", baseUser.getOpen_id());
                jSONObject.put("type", 3);
                jSONObject.put("source", 2);
                jSONObject.put("source_msg", "远程发券");
                jSONObject.put("balance_90", (int) (Double.parseDouble(total) * 100));
                jSONObject.put("state", 2);
                jSONObject.put("commission", 0);
                jSONObject.put("tradeType", "");
                jSONObject.put("orderState", 1);
                jSONObject.put("orderTotal", 0);
                jSONObject.put("ticketType", ticketType);
                map1.put("json", jSONObject.toString());
                System.out.print(jSONObject.toString());

                String requestUrl = ConfigUtil.get("BALANCE_902") + "balance/operUserBalance90.ac";
                String x = URLConectionUtil.httpURLConnectionPostDiy(requestUrl, map1);
                x = URLDecoder.decode(x, "utf-8");
                if (x == null || x.trim().equals("")) {
                    map.put("isok", false);
                    map.put("msg", "发劵失败");
                } else {
                    System.out.println(x.toString());
                    @SuppressWarnings({"unused", "unchecked"})
                    Map<String, Object> map2 = JSON.parseObject(x, Map.class);
                    if (map2 != null && map2.get("flag").equals("0"))  //发劵成功
                    {
                        map.put("isok", true);
                        map.put("msg", map2.get("msg"));
                    } else {
                        map.put("isok", false);
                        map.put("msg", map2.get("msg") == null ? "异常错误" : map2.get("msg"));
                    }
                }
            /*}else{
                map.put("isok", false);
                map.put("msg","该手机号用户不存在或该手机号不属于该商户");
            }*/
            }else{
                map.put("isok", false);
                map.put("msg","本商户当前不可发劵！");
            }

        }

        writeJson(map,response);
    }



    /**
     * 远程发劵
     */
    @RequestMapping(value = "/toIssuedGift")
    public ModelAndView toIssuedGift() throws Exception
    {
        mv.setViewName("basic/brand/issuedGift");
        return mv;
    }

    @RequestMapping(value = "/orther_toCheckVoucher")
    public ModelAndView doQuery(HttpSession session) throws Exception
    {

        User user = (User)getShiroAttribute("user");

        mv.setViewName("basic/brand/doCheckVoucher");
        return mv;
    }

    @RequestMapping(value = "/orther_toWxReply")
    public ModelAndView orther_toWxReply(HttpSession session) throws Exception
    {

        User user = (User)getShiroAttribute("user");
        List<Map<String,Object>> map=orderMainService.getWxReply();
        mv.addObject("map",map);
        mv.setViewName("basic/brand/wxReply");
        return mv;
    }

    @RequestMapping(value = "/orther_toCheckVoucherForBrand")
    public ModelAndView orther_toCheckVoucherForBrand(HttpSession session,String orderId) throws Exception
    {

        User user = (User)getShiroAttribute("user");
        mv.clear();
        mv.addObject("orderId",orderId);
        mv.setViewName("basic/brand/doCheckVoucherForBrand");
        return mv;
    }
    /**
     * 获取商品信息
     * @param voucher
     * @throws Exception
     */
    @RequestMapping(value="/orther_getPro")
    public void orther_getPro(HttpSession session,String voucher,HttpServletResponse response) throws Exception
    {
        User user = (User)getShiroAttribute("user");
        Map<String,Object> map2 = new HashMap<String,Object>();
        Map<String, Object> res=new HashMap<>();
        map2 = apiInterfaceService.queryGoodsSkuInfo(voucher,user.getIdentity_code());



                if(map2!=null && map2.size()>0)  //核劵成功
                {
                    //满足销券条件准备销券
                    if(map2.get("resultCode") !=null && "3".equals(map2.get("resultCode").toString()))
                    {
                        boolean flag = apiInterfaceService.doSellingCoupons(voucher);
                        if(flag)
                        {
                            map2.put("resultCode", "4");//销券成功
                            map2.put("resultMsg", "销券成功");
                            //销券成功发送模板消息
                            apiInterfaceService.sendChangeNoticeTemplate(voucher);
                        }else{
                            map2.put("resultCode", "5");//销券成功
                            map2.put("resultMsg", "销券失败");
                        }
                    }
                }
        try {

            if(map2!=null && map2.get("resultCode").equals("4"))  //核劵成功
            {
                DetailVoucher detailVoucher = new DetailVoucher();
                detailVoucher.setBrand_code(user.getIdentity_code());
                detailVoucher.setVoucher_code(voucher);
                detailVoucher.setName(map2.get("name") == null ? "" : map2.get("name").toString());
                detailVoucher.setPic(map2.get("goodsPath") == null ? "" : map2.get("goodsPath").toString());
                detailVoucher.setFirstValue(map2.get("firstValue") == null ? "" : map2.get("firstValue").toString());
                detailVoucher.setSecondValue(map2.get("secondValue") == null ? "" : map2.get("secondValue").toString());
                detailVoucher.setThirdValue(map2.get("thirdValue") == null ? "" : map2.get("thirdValue").toString());
                detailVoucher.setIsSale(Integer.valueOf(map2.get("isSale") == null ? "0" : map2.get("isSale").toString()));
                detailVoucher.setPrice((int) (Double.valueOf(map2.get("price") == null ? "0.0" : map2.get("price").toString())*100));
                detailVoucher.setStock(Integer.valueOf(map2.get("stock")==null?"0":map2.get("stock").toString()));


                if(map2.get("tags")!=null){
                    List<Map> list = (List<Map>) map2.get("tags");
                    //Map<String,Object> tagsMap = JSON.parseObject(jsonArray+"", Map.class);
                    //  List<Map> list = JSON.parseArray(jsonArray+"", Map.class);
                    String tages="";
                    if(list!=null&&list.size()>0)
                    {
                        for(int i=0;i<list.size();i++)
                        {
                            if(list.get(i)!= null && list.get(i).get("tagName") != null)
                            {

                                tages+=list.get(i).get("tagName").toString()+",";
                            }
                        }

                    }
                    detailVoucher.setTags(!tages.trim().equals("")?tages.substring(0,tages.length()-1):tages);
                }else
                {
                    detailVoucher.setTags("");
                }

                queryTicketService.insertDetailVoucher(detailVoucher);
                //map2.remove("id");
                //map2.remove("isSale");
                //map2.remove("price");
                //map2.remove("stock");
                //map2.remove("goodsId");

                if(map2.get("secondValue") == null)
                {
                    map2.put("secondValue", "");
                }
                if(map2.get("thirdValue") == null)
                {
                    map2.put("thirdValue", "");
                }
                res=map2;
            }else
            {
                map2.put("name","");
                map2.put("goodsPath", "");
                map2.put("firstValue", "");
                map2.put("secondValue", "");
                map2.put("thirdValue", "");
                map2.put("isSale", "");
                map2.put("price", "");
                map2.put("stock", "");
                map2.put("tags", new ArrayList<Map>());
                res=map2;
            }

        } catch (Exception e) {
            System.out.println(e.toString());
            res=new HashMap<>();
        }

        writeJson(map2,response);

    }


    @RequestMapping("toQueryVoucher")
    public ModelAndView toQueryVoucher(ModelAndView mv){
        User user = (User)getShiroAttribute("user");
        mv.addObject("identity",user.getIdentity());
        mv.addObject("identity_code",ConfigUtil.get("agent_code"));
        if(user.getIdentity() == 2){
            mv.addObject("agent_select",user.getIdentity_code());
        }
        if(user.getIdentity() == 3){
            mv.addObject("brand_select",user.getIdentity_code());
        }

        mv.setViewName("basic/brand/queryVoucher");
        return mv;
    }

    /**
     * 查询扫码核劵记录
     */
    @RequestMapping(value = "/doQueryVoucher")
    public void doQueryVoucher(HttpServletResponse response, HttpServletRequest request) throws Exception
    {
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("code", request.getParameter("code"));
        params.put("name", request.getParameter("name"));
        params.put("startdate", request.getParameter("startdate"));
        params.put("endDate", request.getParameter("enddate"));
        params.put("agent", request.getParameter("agent"));
        params.put("brand", request.getParameter("brand"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = Integer.parseInt(request.getParameter("offset"));

        User user = (User)getShiroAttribute("user");
        if(user.getIdentity() == 3){
            params.put("brand", user.getIdentity_code());
        }
        Pager<DetailVoucher> pager = new Pager();
        pager.setParameters(params);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);

        pager = orderMainService.queryDetailVoucherList(pager);

        Paging<DetailVoucher> paging = new Paging<>();
        paging.setRows(pager.getExhibitDatas());
        paging.setTotal((long) pager.getRecordCount());

        writeJson(paging, response);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/doSaveReply")
    public void doSaveReply(HttpServletResponse response,Pbrand pbrand) throws Exception
    {
        Map<String,Object> reMap=new HashedMap();
        reMap=orderMainService.doSaveReply(pbrand);
        writeJson(reMap, response);
    }

    @RequestMapping(value = "/loadWxReply")
    public void loadWxReply(HttpServletResponse response) throws Exception
    {
        List<Pbrand> pbrand=orderMainService.loadWxReply();
        writeJson(pbrand, response);
    }
}	
