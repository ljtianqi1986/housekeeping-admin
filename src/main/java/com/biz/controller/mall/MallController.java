package com.biz.controller.mall;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.biz.conf.Global;
import com.biz.model.Pmodel.basic.*;
import com.biz.model.Hmodel.basic.TwxGoodsGroup;
import com.biz.model.Pmodel.Ppics;
import com.biz.model.Pmodel.PwxGood;
import com.biz.model.Pmodel.PwxGoods;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.Pwxgoods;
import com.biz.model.Pmodel.basic.PwxgoodsProperty;
import com.biz.service.mall.MallServiceI;
import com.framework.controller.BaseController;
import com.framework.model.MessageBox;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.utils.ConfigUtil;
import com.framework.utils.StringUtil;
import com.framework.utils.URLConectionUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商户设置相关
 * Created by liujiajia on 2016/11/14.
 */
@Controller
@RequestMapping("/mall")
public class MallController extends BaseController{

    final static String GOOD_SKU_CHAIN = "goodSkuChain";

    @InitBinder("pwxgoods")
    public void initBinderFormBean1(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("pwxgoods.");
    }

    @Autowired
    private MallServiceI mallService;


    /**
     * 跳转联盟商品
     * @param mv
     * @return
     */
    @RequestMapping("toMall_lm")
    public ModelAndView toMall_lm(ModelAndView mv)throws Exception{
        String mapId="";
        List<TwxGoodsGroup> label_list = mallService.loadGoodsGroup(mapId);
        mv.addObject("list",label_list);
        mv.setViewName("basic/mall/mall_lm");
        return mv;
    }

    /**
     * 展示商户列表信息
     * @param response
     * @param request
     */
    @RequestMapping("showUnionMall")
    public void showUnionMall(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //必要的分页参数
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("name", request.getParameter("name")==null?"":request.getParameter("name"));
        params.put("groupId", request.getParameter("groupId")==null?"":request.getParameter("groupId"));
        params.put("shangxiajia", request.getParameter("shangxiajia")==null?"":request.getParameter("shangxiajia"));
        //todo add shopId in params
        params.put("isTicket","1");
        params.put("order",request.getParameter("order"));
        params.put("speed_code",request.getParameter("speed_code"));
        Pager<Pwxgoods> pager = new Pager();
        pager.setParameters(params);
        pager.setStartRecord(Integer.parseInt(request.getParameter("offset")));
        pager.setPageSize(Integer.parseInt(request.getParameter("limit")));

        pager=mallService.queryMall(pager);

        Paging<Pwxgoods> paging = new Paging<>();
        paging.setRows(pager.getExhibitDatas());
        paging.setTotal((long) pager.getRecordCount());

        writeJson(paging,response);
    }

    @RequestMapping("loadGroup")
    public void loadGroup(HttpServletResponse response, HttpServletRequest request,String pid)throws Exception{
        List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
        try{ list=mallService.loadGroup(pid);}
        catch (Exception e)
        {
            e.printStackTrace();
        }
        writeJson(list, response);
    }

    @RequestMapping("addGroup")
    public void addGroup(HttpServletResponse response, HttpServletRequest request,String pid,String type,String name)throws Exception{
       Map map=new HashMap();
       if(type.equals("1")){
           map.put("pid","0");
       }else{
           map.put("pid",pid);
       }
       map.put("name",name);
       map.put("id",getRndCode());
       String msg="";
        try{ msg=mallService.addGroup(map);}
        catch (Exception e)
        {
            e.printStackTrace();
        }
        writeJson(msg, response);
    }

    /**
     * 调用接口加载商户信息
     */
    @RequestMapping("/showVenderId")
    public void showVenderId(HttpServletResponse response, String name) throws Exception {
        name = URLDecoder.decode(name, "utf-8");
        List<Brand> list= mallService.showVenderId(name);
        writeJson(list, response);

    }

    /**
     * 保存商品
     * @param response
     * @param pwxgoods
     * @throws Exception
     */
    @RequestMapping("doSave")
    public void doSave(HttpServletResponse response,Pwxgoods pwxgoods)throws Exception {
        //判断库存中有没有空值
        boolean b=true;
        Map<String, Object> map=new HashMap<>();
        List<PwxgoodsStock> stocklist=pwxgoods.getStocklist();
        if(stocklist!=null){
            if(stocklist.size()>0){
                for(int i=0;i<stocklist.size();i++){
                    if(stocklist.get(i).getPrice()==null){
                        b=false;
                    }else if(stocklist.get(i).getStock()==null){
                        b=false;
                    }else if(stocklist.get(i).getVenderId()==null){
                        b=false;
                    }
                }
            }
        }
        if(b){
            try {
                String shopId = (String) getShiroAttribute("shopId");

                if (!StringUtil.isNullOrEmpty(pwxgoods.getInfo())) {
                    String htmlStr = pwxgoods.getInfo();
                    Document doc = Jsoup.parseBodyFragment(htmlStr);
                    Elements elms = doc.select("img");
                    for (Element e : elms) {
                        String attr = e.attr("style");
                        if(!"".equals(attr) || null!=attr){
                            e.attr("style", "");
                        }
                        String attr2 = e.attr("height");
                        if(!"".equals(attr) || null!=attr){
                            e.attr("height", "");
                        }
                        String attr3 = e.attr("width");
                        if(!"".equals(attr) || null!=attr){
                            e.attr("width", "");
                        }
                    }
                    htmlStr = doc.body().html();
                    //System.out.println(htmlStr);
                    htmlStr = replaceNRT(htmlStr);
                    //System.out.println(htmlStr);

                    pwxgoods.setInfo(htmlStr);
                }

                 map=mallService.saveGoodsAndStock(pwxgoods,shopId);
            } catch (Exception e) {
                map.put("isok","no");
                map.put("msg","系统异常");
            }
        }else{
            map.put("isok","no");
            map.put("msg","保存失败，库存中有价格或者库存值或者商家编码没有填写");
        }
            writeJson(map,response);
    }

    /**
     * 跳转商品修改
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    @RequestMapping("toEdit")
    public ModelAndView toEdit(String id) throws Exception {
        mv.clear();
        //通过选择的品种id 加载详情
        Pwxgoods twxgoods = mallService.getByIdBySql(id);
        //属性集合
        List<PwxgoodsProperty> propertyList = mallService.getwxgoodsProperty(id);
        //商品库存集合
        List<PwxgoodsStock> stockList = mallService.getwxgoodsStock(id);
        //此品种所有图片信息
        List<Ppics> picslist = mallService.findPicsById(id);
        //属性名称（父级）
        Map<String, String> groupName = new HashMap<String, String>();
        List<Map<String, Object>> grouplist = mallService.findWxgoodsPropertyName(id);
        if (grouplist != null) {
            for (int i = 0; i < grouplist.size(); i++) {
                if (grouplist.get(i) != null) {
                    groupName.put("group" + i, grouplist.get(i).get("name").toString());
                }
            }
        }
        mv.addObject("piclist", picslist);
        mv.addObject("twxgoods", twxgoods);
        mv.addObject("info",replaceNRT(twxgoods.getInfo()));
        mv.addObject("propertyList", propertyList);
        mv.addObject("stockList", stockList);
        mv.addObject("page_type", "update");
        mv.addObject("groupName", groupName);
        mv.addObject("grouplist",grouplist);
        mv.addObject("unitId", twxgoods.getUnitId());
        mv.addObject("propertylistjson", JSONArray.toJSONString(propertyList));
        mv.addObject("stocklistjson", JSONArray.toJSONString(stockList));
        mv.addObject("grouplistjson", JSONArray.toJSONString(grouplist));
        mv.setViewName("basic/mall/mall_lmEdit");
        return mv;
    }


    /**
     * 更具id 删除商户信息
     * @param ids
     * @param response
     */
    @RequestMapping("delGridById")
    public void delGridById(String ids,HttpServletResponse response)throws Exception{
        MessageBox box=new MessageBox();
        try{
            if (ids == null) {
                box.setSuccess(false);
                box.setMsg("未指定");
            } else {
                mallService.delGridById(ids);
            }
        }catch (Exception e){
            box.setSuccess(false);
        }
        writeJson(box, response);
    }

    @RequestMapping("toMall90")
    public ModelAndView toMall90(ModelAndView mv) throws Exception {
        //todo get shopid from session
        User user = (User) getShiroAttribute("userinfo");
        String mapId = user.getIdentity_code();
//        String mapId = "131c505a69154b66891d309f7298aece";
        List<TwxGoodsGroup> label_list = mallService.loadGoodsGroup(mapId);

        mv.addObject("goodsGroups", label_list);
        mv.setViewName("basic/mall/mall90");
        return mv;
    }

    @RequestMapping("queryMall90")
    public void queryMall90(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //必要的分页参数
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("name", request.getParameter("name")==null?"":request.getParameter("name"));
        params.put("code", request.getParameter("code")==null?"":request.getParameter("code"));
        params.put("groupId", request.getParameter("groupId")==null?"":request.getParameter("groupId"));
        params.put("shangxiajia", request.getParameter("shangxiajia")==null?"":request.getParameter("shangxiajia"));
        //必要的分页参数
        params.put("order",request.getParameter("order"));
        //params.put("offset",Integer.parseInt(request.getParameter("offset")));
        //params.put("limit",Integer.parseInt(request.getParameter("limit")));
        //params.put("sort",request.getParameter("sort"));
        //params.put("begin",Integer.parseInt(request.getParameter("offset")));
        //params.put("rows",Integer.parseInt(request.getParameter("limit")));
        //必要的分页参数
        params.put("isTicket","0");

        User user = (User) getShiroAttribute("userinfo");
        //params.put("shopId",user.getMapId());

        Pager<Pwxgoods> pager = new Pager();
        pager.setParameters(params);
        pager.setStartRecord(Integer.parseInt(request.getParameter("offset")));
        pager.setPageSize(Integer.parseInt(request.getParameter("limit")));

        pager=mallService.queryMall(pager);

        Paging<Pwxgoods> paging = new Paging<>();
        paging.setRows(pager.getExhibitDatas());
        paging.setTotal((long) pager.getRecordCount());

        writeJson(paging,response);
    }

    /**
     * 加载分组
     *  @return
     * @throws Exception
     */
    @RequestMapping("loadGoodsGroup")
    public void loadGoodsGroup(HttpServletResponse response) throws Exception {

        User user = (User) getShiroAttribute("userinfo");
        List<TwxGoodsGroup> label_list = mallService.loadGoodsGroup(user.getMapId());
        writeJson(label_list, response);
    }

    @RequestMapping("deByIds")
    public void delGridById(HttpServletResponse response, HttpServletRequest request,String ids) throws Exception {
        MessageBox mb = getBox();
        try{
            mallService.executeHql(StringUtil.formateString("update Twxgoods set isdel =1 where id in ({0})",StringUtil.formatSqlIn(ids)));
            mb.setSuccess(true);
        }catch (Exception e){
            mb.setSuccess(false);
        }
        writeJson(mb,response);
    }


    @RequestMapping("upOrDown")
    public void upOrdown(HttpServletResponse response, String ids, String state, String type) throws Exception {
        MessageBox mb = getBox();
        try {
            User user = (User) getShiroAttribute("userinfo");
            mallService.upOrdown(ids, state, user.getId(), type);
            mb.setSuccess(true);
        }catch (Exception e){
            mb.setSuccess(false);
        }
        writeJson(mb,response);
    }

    @RequestMapping("toEdit90")
    public ModelAndView toEdit90(String id) throws Exception {
        Pwxgoods twxgoods = mallService.getByIdBySql(id);
        mv.clear();
        mv.addObject("id",id);
        mv.addObject("twxgoods", twxgoods);
        mv.setViewName("basic/mall/mall90Edit");
        return mv;
    }

    /**
     * 加载商品属性
     *  @return
     * @throws Exception
     */
    @RequestMapping("getGoodsSku")
    public void getGoodsSku(HttpServletResponse response,String id) throws Exception {
        PwxGood label_list =mallService.getGoodsSku(id);
        writeJson(label_list, response);
    }

    /**
     * 加载供应链商品属性
     *  @return
     * @throws Exception
     */
    @RequestMapping("getGoodsSkuFromChain")
    public void getGoodsSkuFromChain(HttpServletResponse response,String id) throws Exception {

        User user = (User) getShiroAttribute("userinfo");

        PwxGoods label_list = mallService.getGoodsAndStockById(id);


        writeJson(label_list, response);
    }

    //@RequestMapping("getGoodsSkuFromChain")
    //public void getGoodsSkuFromChain(HttpServletResponse response,String id) throws Exception {
    //
    //    User user = (User) getShiroAttribute("userinfo");
    //
    //    String res ="";
    //    String url= ConfigUtil.get("SUPPLY_URL")+"interface/getGoodsAndStockById.action";
    //    Map<String,String> map=new HashMap<>();
    //    map.put("id",id);
    //    res= URLConectionUtil.httpURLConnectionPostDiy(url,map);
    //    res= JSON.parseObject(res,String.class);
    //    // DubboServiceI dubboService=(DubboServiceI) SpringApplicationContextHolder.getBean("dubboService");
    //
    //    // res= dubboService.getGoodsAndStockById(id);
    //    PwxGoods label_list = JSON.parseObject(res,PwxGoods.class);
    //
    //    getSession().setAttribute(GOOD_SKU_CHAIN,label_list);
    //
    //    try {
    //        mallService.saveGoodsInfo(label_list, user.getMapId());
    //    }catch (Exception e)
    //    {
    //        e.printStackTrace();
    //    }
    //    writeJson(label_list, response);
    //}

    @RequestMapping("updateStockAndPrice")
    public void updateStockAndPrice(Pwxgoods pwxgoods,HttpServletResponse response){
        MessageBox mb = getBox();
        try{
            mallService.updateStockAndPrice(pwxgoods);
            mb.setSuccess(true);
            getSession().removeAttribute(GOOD_SKU_CHAIN);
        }catch (Exception e){
            mb.setSuccess(false);
        }
        writeJson(mb, response);
    }

    @RequestMapping("mall90Select")
    public ModelAndView mall90Select(ModelAndView mv) throws Exception {
        mv.setViewName("basic/mall/mall90Select");
        return mv;
    }

    @RequestMapping("queryMall90FromChain")
    public void queryMall90FromChain(HttpServletResponse response, HttpServletRequest request)throws Exception{
        String goods;

        //必要的分页参数
        Map<String,String> params=new HashMap<>();
        params.put("page", request.getParameter("offset")==null?"0":request.getParameter("offset"));
        params.put("pageSize", request.getParameter("limit")==null?"10":request.getParameter("limit"));
        params.put("goodsName", request.getParameter("goodsName")==null?"":request.getParameter("goodsName"));
        params.put("supplyName", request.getParameter("supplyName")==null?"":request.getParameter("supplyName"));
        params.put("goodCode", request.getParameter("goodCode")==null?"":request.getParameter("goodCode"));
        params.put("type","3");
        params.put("cityId","173");
        //todo get shopid from session
        //String shopId = (String) getShiroAttribute("identity_code");
        //String shopId = "131c505a69154b66891d309f7298aece";
        //params.put("shopId",shopId);

        String url=ConfigUtil.get("ERP_URL")+"interface/d_getGoodsList.action";
        goods= URLConectionUtil.httpURLConnectionPostDiy(url,params);
        goods=JSON.parseObject(goods,String.class);
        Map<String,Object> mapres=JSON.parseObject(goods,Map.class);
        List<PwxGoods> list= (List<PwxGoods>) mapres.get("goodsList");
        Integer count1 = (Integer) mapres.get("count");

        Paging<PwxGoods> paging = new Paging<>();
        paging.setRows(list);
        paging.setTotal((long) count1);

        writeJson(paging,response);
    }

    //@RequestMapping("toAdd90")
    //public ModelAndView toAdd90(String id) throws Exception {
    //    mv.clear();
    //    mv.addObject("id",id);
    //    mv.setViewName("basic/mall/mall90Add");
    //    return mv;
    //}

    //@RequestMapping("addGoodAndStockFromChain")
    //public void addGoodAndStockFromChain(Pwxgoods pwxgoods,HttpServletResponse response){
    //    MessageBox mb = getBox();
    //    try{
    //        User user = (User) getShiroAttribute("userinfo");
    //        PwxGoods pwxGoodsSession = (PwxGoods) getSession().getAttribute(GOOD_SKU_CHAIN);
    //        if(pwxGoodsSession==null ){
    //            mb.setSuccess(false);
    //            mb.setMsg("操作超时,请重新选择商品!");
    //        }else{
    //            mallService.addGoodAndStockFromChain(pwxGoodsSession,pwxgoods,user.getMapId());
    //            mb.setSuccess(true);
    //            //getSession().removeAttribute(GOOD_SKU_CHAIN);
    //        }
    //
    //    }catch (Exception e){
    //        mb.setSuccess(false);
    //        e.printStackTrace();
    //    }
    //    writeJson(mb, response);
    //}
    @RequestMapping("doSynchronize")
    public void doSynchronize(String ids,HttpServletResponse response){
      String res="fail";
        try{
            String cityId= Global.getLocalCity();
            Map<String,String> parm=new HashMap<>();
            parm.put("goodsIds",ids);
            parm.put("cityId",cityId);
            String url=ConfigUtil.get("ERP_URL")+"/interface/doSendGoodsByGoodIdsAndCityId.action";
            String resString=URLConectionUtil.httpURLConnectionPostDiy(url,parm);
            if(!StringUtil.isNullOrEmpty(resString)&&!"失败".equals(resString))
            {
                Map<String,String> resMap=JSON.parseObject(resString,Map.class);
                if("0".equals(resMap.get("flag")))
                {
                    res="success";
                }else
                {
                    res=resMap.get("msg");
                }
            }else
            {
                res="同步失败，连接服务器失败！";
            }

        }catch (Exception e){
            res="同步失败，异常错误！";
        }
        writeJson(res, response);
    }

    @RequestMapping("doSyncGroup")
    public void doSyncGroup(HttpServletResponse response){
        MessageBox mb = new MessageBox();
        try{
            mallService.doSyncGroup();
        }catch (RuntimeException e){
            mb.setSuccess(false);
            mb.setMsg(e.getMessage());
        }catch (Exception e){
            mb.setSuccess(false);
            mb.setMsg("同步分组异常!");
        }
        writeJson(mb, response);

    }

    public String replaceNRT(String str){
        String result = "";
        if (!StringUtil.isNullOrEmpty(str)) {
            result = str.replaceAll("\\n", "").replaceAll("\\r", "").replaceAll("\\t", "");
        }
        return result;
    }

    /**
     * 展示商户列表信息
     * @param response
     * @param request
     */
    @RequestMapping("showPicGoods")
    public void showPicGoods(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //必要的分页参数
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("name", request.getParameter("name")==null?"":request.getParameter("name"));
        params.put("groupId", request.getParameter("groupId")==null?"":request.getParameter("groupId"));
        params.put("shangxiajia", request.getParameter("shangxiajia")==null?"":request.getParameter("shangxiajia"));
        //todo add shopId in params
        params.put("order",request.getParameter("order"));
        params.put("speed_code",request.getParameter("speed_code"));
        Pager<Pwxgoods> pager = new Pager();
        pager.setParameters(params);
        pager.setStartRecord(Integer.parseInt(request.getParameter("offset")));
        pager.setPageSize(Integer.parseInt(request.getParameter("limit")));

        pager=mallService.queryMall(pager);

        Paging<Pwxgoods> paging = new Paging<>();
        paging.setRows(pager.getExhibitDatas());
        paging.setTotal((long) pager.getRecordCount());

        writeJson(paging,response);
    }

}
