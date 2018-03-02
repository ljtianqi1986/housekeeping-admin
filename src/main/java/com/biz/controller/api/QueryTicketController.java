package com.biz.controller.api;


import com.biz.model.Pmodel.api.DetailVoucher;
import com.biz.service.api.ApiInterfaceServiceI;
import com.biz.service.api.QueryTicketServiceI;
import com.biz.service.api.TicketServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.ConfigUtil;
import com.framework.utils.DateUtil;
import com.framework.utils.StringUtil;
import com.framework.utils.URLConectionUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * qt接口
 *
 * @author  lzq
 *
 */
@Controller
@RequestMapping("/api/QTTicket")
public class QueryTicketController extends BaseController {
    @Resource(name = "ticketService")
    private TicketServiceI ticketService;
    @Resource(name = "apiInterfaceService")
    private ApiInterfaceServiceI apiInterfaceService;
    @Resource(name = "queryTicketService")
    private QueryTicketServiceI queryTicketService;
    /**
     * 体验店的收银记录 分收银员|店长查询
     * type
     * trade_type：
     * 0:全部 1：刷卡 2：扫码 3：公众号
     *
     */
    @RequestMapping(value = "/queryTicketInfoByOrderId")
    public void queryTicketInfoByOrderId(String id,HttpServletResponse response)
    {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("return_code", 0);
        jSONObject.put("return_data", null);
            try {
                Map<String, Object> mapsRetur = queryTicketService.queryTicketInfoByOrderId(id);
                mapsRetur.put("date",DateUtil.dateToString(new Date()));
                jSONObject.put("return_code",1);
                jSONObject.put("return_info","");
                jSONObject.put("return_data", mapsRetur);
            } catch (Exception e) {
                jSONObject.put("return_info", e.getMessage());
                e.printStackTrace();
            }

            writeJson(jSONObject,response);


    }

    /**
     * pos机人工发券
     */
    @RequestMapping(value = "/phone_gift_rg")
    public void phone_gift_rg(HttpServletResponse response,String shop_code,String user_code,String total,String ticketType) throws Exception {
        logger.error("=人工发券=shop_code="+shop_code+"=user_code="+user_code+"=total="+total);
        if(StringUtil.isNullOrEmpty(ticketType))
        {ticketType="0";}
        Map<String,Object> jSONObject =ticketService.phone_gift_rg_public(shop_code, user_code, total,ticketType);
        writeJson(jSONObject,response);
    }
    /**
     * 体验店人工发券
     */
    @RequestMapping(value = "/phone_gift_rg_tyd")
    public void phone_gift_rg_tyd(HttpServletResponse response,String shop_code,String user_code,String total,String gift_type,String card_code,String memo) throws Exception {
        logger.error("=人工发券=shop_code="+shop_code+"=user_code="+user_code+"=total="+total+"=card_code="+card_code+"=memo="+memo);
        Map<String,Object> jSONObject =ticketService.phone_gift_rg_tyd_public(shop_code, user_code, total, gift_type, card_code, memo);
        writeJson(jSONObject,response);
    }


    /**
     * 商家消券
     * @param voucher
     * @throws Exception
     */
    @RequestMapping(value="/orther_getPro2")
    public void orther_getPro2(String voucher,String brandCode,HttpServletResponse response) throws Exception
    {
        //User user = (User) session.getAttribute(Const.SESSION_USER);
    //    Map<String,String> map1=new HashMap<>();
        Map<String,Object> map2 = new HashMap<String,Object>();
        Map<String, Object> res=new HashMap<>();
     /*   map1.put("code", voucher);
        map1.put("type", "1");     //1消劵
        map1.put("venderIdMain",brandCode);//
        String requestUrl = ConfigUtil.get("otherInterFace_URL")+"interface/querySku.action";*/
        map2 = apiInterfaceService.queryGoodsSkuInfo(voucher,brandCode);

            if(map2!=null && map2.size()>0)
           {
               //满足销券条件准备销券
               if(map2.get("resultCode") !=null && "3".equals(map2.get("resultCode").toString()))
               {
                   boolean flag = apiInterfaceService.doSellingCoupons(voucher);
                   if(flag)
                   {
                       map2.put("resultCode", "4");//销券成功
                        map2.put("resultMsg", "销券成功");
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
                    detailVoucher.setBrand_code(brandCode);
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
                    map2.remove("id");
                    map2.remove("isSale");
                    map2.remove("price");
                    map2.remove("stock");
                    map2.remove("goodsId");

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
writeJson(res,response);
    }

    /**
     * 商家消券
     * @param page
     * @throws Exception
     */
    @RequestMapping(value="/getVoucherDetail")
    public void getVoucherDetail(String page,HttpServletResponse response,String sid) throws Exception
    { Map<String, Object> res=new HashMap<>();

        if(StringUtils.isBlank(page)){
            res.put("return_code", 0);
            res.put("return_data", "");
            res.put("return_count", 0);
            res.put("return_info", "接口参数错误：page为空");

        }
        else
        {
            try{
                int page_int = Integer.valueOf(page);
                int pageSize = 10;
                Map<String,Object> mapParam = new HashMap<>();
                mapParam.put("pageSize", pageSize);
                mapParam.put("pageNum", pageSize * (page_int - 1));
                mapParam.put("sid", sid);

                List<Map<String, Object>> mapsRetur = queryTicketService.queryVoucherDetail(mapParam);
                int total = queryTicketService.queryVoucherDetailCount(mapParam);
                res.put("return_code", 1);
                res.put("return_data", mapsRetur);
                res.put("return_count", total);
                res.put("return_info", "成功");
            }catch (Exception e)
            {
                res.put("return_code", 0);
                res.put("return_data", "");
                res.put("return_count", 0);
                res.put("return_info", "接口参数错误：异常错误");
            }
        }

        writeJson(res,response);

    }

    /**
     * 第三方商户订单兑换久零券贝
     */
    @RequestMapping(value = "/exchangeCDkey")
    public void exchangeCDkey(HttpServletResponse response,String code,String openId) throws Exception {
        Map<String,Object> jSONObject =apiInterfaceService.updateThirdOrderSendBalance(code,openId);
        if("0".equals(jSONObject.get("code")))
        {
            String url=ConfigUtil.get("apisUrl")+"/api/finishOrder.ac";
            Map<String,String> parm=new HashMap<>();
            parm.put("code",code);
            parm.put("openId",openId);
            String resString = URLConectionUtil.httpURLConnectionPostDiy(url,parm);
            try{//回调改写订单其实已经无意义了，因为http方式调用又没有事务，所以没再判断是否回写成功
                //jSONObject= JSON.parseObject(resString,Map.class);
            }
            catch (Exception e)
            {}
        }
        writeJson(jSONObject,response);
    }
}