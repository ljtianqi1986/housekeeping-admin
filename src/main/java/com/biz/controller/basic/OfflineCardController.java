package com.biz.controller.basic;

import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.Pbizperson;
import com.biz.model.Pmodel.offlineCard.PofflineCard;
import com.biz.model.Pmodel.offlineCard.PofflineCardDetail;
import com.biz.service.basic.BizpersonServiceI;
import com.biz.service.offlineCard.OfflineCardServiceI;
import com.framework.controller.BaseController;
import com.framework.model.Paging;
import com.framework.utils.ConfigUtil;
import com.framework.utils.ObjectExcelView;
import com.framework.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 实体卡相关
 * Created by liujiajia on 2016/11/14.
 */
@Controller
@RequestMapping("/offlineCard")
public class OfflineCardController extends BaseController {

    @Autowired
    private OfflineCardServiceI offlineCardService;

    @InitBinder("pofflineCard")
    public void initBinderFormBean1(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("pofflineCard.");
    }

    @Autowired
    private BizpersonServiceI bizpersonService;

    /*************************************页面跳转*************************************/

    //1:管理员 2:区域代理商 (base_agent 的id)3:品牌(商户)(base_brand 的id) 4:实体门店

    /**
     * 实体卡类型
     *
     * @param mv
     * @return
     */
    @RequestMapping("toQuery")
    public ModelAndView toQuery(ModelAndView mv) {
        mv.clear();
        mv.setViewName("basic/offlineCard/query");
        return mv;
    }

    /**
     * 生成
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("toCardAddEdit")
    public ModelAndView toCardAddEdit(String id) throws Exception {

        PofflineCard offlineCard = new PofflineCard();
        if (StringUtil.isNullOrEmpty(id.trim()) || id.trim().equalsIgnoreCase("undefined")) {
            //空
            id = "";
        } else {
            offlineCard = offlineCardService.getCardById(id.trim());
        }
        mv.clear();
        mv.addObject("id", id);
        mv.addObject("offlineCard", offlineCard);
        mv.setViewName("basic/offlineCard/cardAddEdit");
        return mv;
    }

    /**
     * 查询码库
     * @param id
     * @return
     */
    @RequestMapping("toCardDetail")
    public ModelAndView toCardDetail(String id) {
        mv.clear();
        mv.addObject("id", id);
        mv.setViewName("basic/offlineCard/queryDetail");
        return mv;
    }

    /**
     * 查询发放记录
     * @param id
     * @return
     */
    @RequestMapping("toCardGrandDetail")
    public ModelAndView toCardGrandDetail(String id) {
        mv.clear();
        mv.addObject("id", id);
        mv.setViewName("basic/offlineCard/getCardGrandDetails");
        return mv;
    }

    /**
     *加载指定发放码库
     * @param id
     * @return
     */
    @RequestMapping("loadCard")
    public ModelAndView loadCard(String id) throws Exception{
        mv.clear();
        Map<String,Object> map=offlineCardService.loadCardInterval(id);
        mv.addObject("maxNum",map.get("maxNum").toString());
        mv.addObject("minNum",map.get("minNum").toString());
        mv.addObject("id", id);
        mv.setViewName("basic/offlineCard/loadCard");
        return mv;
    }

    /**
     * 跳转发放页
     */
    @RequestMapping(value = "/toGrand")
    public ModelAndView toGrand(String id) throws Exception {
        mv.clear();
        /*boolean operatingAuthority=true;
        //权限
        String identity="2"; //session 取 1:管理员2:区域代理商3:品牌(商户)4:实体门店
        String identitycode="1001"; //代理商编号 session 取 user.getIdentity_code()
        if(identity.equals("2")){
            pd =offlineCardService.saveOfflineCard(pofflineCard);//保存
        }else{
            msg = "当前用户不是代理商";
        }*/


        // 单个码具体属性，即主表信息数据
        PofflineCard of = offlineCardService.getCardById(id);
        //未使用的码库数量
        int count = offlineCardService.getIsNotUsedCount(id);
        //类型  //List<Map<String, String>> listType = offlineCardService.getMyType(identitycode);
        //商户  //List<Map<String, String>> listBrand = offlineCardService.getMyBrand(identitycode);

        mv.addObject("id", id);
        mv.addObject("count", count);//未使用的码库数量
        mv.addObject("offlineCard", of);//单个码具体属性
        //mv.addObject("listBrand", listBrand);//商户
        //mv.addObject("listType", listType);//类型
        mv.setViewName("basic/offlineCard/carddetailGrand");
        return mv;
    }


    /**
     * 空
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/doNull")
    public ModelAndView doNull() throws Exception {
        mv.clear();
        return mv;
    }

    /**
     * *********************************方法************************************
     */
    /**
     * 主表，列表翻页查询
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping("showOfflineCard")
    public void showOfflineCard(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //必要的分页参数
        Map map = getParameterByRequest(request);
        //获取session
        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute("user");

        //map.put("agentId", "1001");//从session获取，暂时写死
        //map.put("name","");
        Paging paging = offlineCardService.showOfflineCard(map);

        writeJson(paging, response);
    }

    /**
     * 子表,码库列表翻页查询
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping("showOfflineCardDetail")
    public void showOfflineCardDetail(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //必要的分页参数
        Map map = getParameterByRequest(request);

        Paging paging = offlineCardService.showOfflineCardDetail(map);

        writeJson(paging, response);
    }



    /**
     * 子表,码库列表翻页查询
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping("showCardGrandDetail")
    public void showCardGrandDetail(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //必要的分页参数
        Map map = getParameterByRequest(request);

        Paging paging = offlineCardService.showCardGrandDetail(map);

        writeJson(paging, response);
    }


    //生成码库
    @RequestMapping("doSave")
    public void doSave(HttpServletResponse response, HttpServletRequest request, PofflineCard pofflineCard) throws Exception {
        String msg = "";
        try {

            String id = pofflineCard.getId();
            boolean pd = false;//是否成功
            if (StringUtil.isNullOrEmpty(pofflineCard.getId())) {
                //保存

                Session session = SecurityUtils.getSubject().getSession();
                User user = (User) session.getAttribute("userinfo");

                //String identity = "2"; //session 取 1:管理员2:区域代理商3:品牌4:实体门店
                //pofflineCard.setAgentId("1001");//代理商编号 session 取 user.getIdentity_code()
                //pofflineCard.setUserId("22222222");//操作人id，session取 user.getUser_code()

                int identity = user.getIdentity();
                pofflineCard.setAgentId(user.getIdentity_code());//代理商编号 session 取 user.getIdentity_code()
                pofflineCard.setUserId(user.getId());//操作人id，session取 user.getId()
                if (identity == 2) {
                    pd = offlineCardService.saveOfflineCard(pofflineCard);//保存
                } else {
                    msg = "当前用户不是代理商";
                }

            } else {
                //修改
                //不可以修改
            }

            if (pd) {
                msg = "success";
            }

        } catch (Exception e) {
            msg = "异常失败";
        }
        System.out.println(msg);
        writeJson(msg, response);
    }

    /**
     * 查看是否生成成功
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/checkIsSuccess", method = RequestMethod.POST)
    public void checkIsSuccess(HttpServletResponse response, String id) throws Exception {
        String CreatState = "0";
        if (!StringUtil.isNullOrEmpty(id)) {
            PofflineCard offlineCard = offlineCardService.getCardById(id);
            if (offlineCard != null) {
                CreatState = offlineCard.getCreatState().toString();
            }
        }
        writeJson(CreatState, response);
    }


    /**
     * 码库导出Excel
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/doExcel")
    public ModelAndView doExcel(String id) throws Exception {
        mv.clear();
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        Map<String, Object> pdmap = new HashMap<>();
        pdmap.put("id", id);

        String filename = sdf.format(d);
        List<PofflineCardDetail> cardDetail_list = offlineCardService.excelCardDetail(pdmap);
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List<String> titles = new ArrayList<String>();

        titles.add("卡号");     //1
        titles.add("密匙");     //2
        titles.add("领取状态");  //3
        titles.add("发放状态");  //3
        titles.add("发放商户");  //3
        titles.add("领取人");   //4
        titles.add("领取时间");  //5
        dataMap.put("titles", titles);
        List<Map<String, Object>> varList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < cardDetail_list.size(); i++) {
            Map<String, Object> vpd = new HashMap<>();
            PofflineCardDetail cardDetail = cardDetail_list.get(i);
            vpd.put("var1", cardDetail.getCardNumber()); // 1
            vpd.put("var2", cardDetail.getCardCode()); // 2
            if (cardDetail.getState() == 0) {
                vpd.put("var3", "未使用"); // 3
            } else if (cardDetail.getState() == 1) {
                vpd.put("var3", "已使用"); // 3
            }
            if ("0".equals(cardDetail.getIsGrand())) {
                vpd.put("var4", "已发放"); // 3
            } else {
                vpd.put("var4", "未发放"); // 3
            }
            vpd.put("var5", cardDetail.getBrandName() == null ? "" : cardDetail.getBrandName()); // 4
            vpd.put("var6", cardDetail.getPerson_name() == null ? "" : cardDetail.getPerson_name()); // 4
            vpd.put("var7", cardDetail.getUseTime()); // 5
            varList.add(vpd);
        }
        dataMap.put("varList", varList);
        ObjectExcelView erv = new ObjectExcelView(); // 执行excel操作
        erv.setNewFileName(filename);
        mv = new ModelAndView(erv, dataMap);
        return mv;
    }




    //加载，商户列表 和 类型列表
    @RequestMapping(value = "/doFQList")
    public void doFQList(HttpServletResponse response) throws Exception {
        User user = (User) getShiroAttribute("userinfo");
        String identitycode = user.getIdentity_code(); //代理商编号 session 取 user.getIdentity_code()
        // 类型
        List<Map<String, String>> listType = offlineCardService.getMyType(identitycode);
        //商户
        List<Map<String, String>> listBrand = offlineCardService.getMyBrand(identitycode);
        //业务代表
        List<Pbizperson> listBizPerson = bizpersonService.showBizPersonForList();
        Map<String, Object> map = new HashMap<>();
        map.put("listBrand", listBrand);//商户
        map.put("listType", listType);//类型
        map.put("listBizPerson",listBizPerson);
        writeJson(map, response);
    }



        /**
         * 发放
         */
    //@RequestMapping(value = "/doInsertGrand", method = RequestMethod.POST)
        @RequestMapping("doInsertGrand")
    public void doInsertGrand(PofflineCard pofflineCard,HttpServletResponse response) throws Exception
    {
        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute("userinfo");
        //String userIdsession="222222222222222222";//session 获取

        //未使用的码库数量
        int num=offlineCardService.getIsNotUsedCount(pofflineCard.getId());
        String msg="";
        if(pofflineCard.getCardCodeType().equals("1")&&num<pofflineCard.getCardCount()){
            msg=Integer.toString(num);//当前数量
        }
        else
        {
            msg="success";
            final PofflineCard offlineCard_f=pofflineCard;
            final String userId = user.getId();
            new Thread(){
                public void run(){
                    try {
                        offlineCardService.addOfflineCardGrand(offlineCard_f, userId);
                    } catch (Exception e) {
                        //logger.error("",e);
                    }
                }
            }.start();
            //mv.addObject("msg_html", htmlSuccess());
            //mv.setViewName("redirect:toGrand.do");
        }
        writeJson(msg,response);
    }


    /**
     * 作废实体卡
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/doCancellation")
    public void doCancellation(HttpServletResponse response, String id) throws Exception {
        String CreatState = "0";
        if (!StringUtil.isNullOrEmpty(id)) {
            CreatState=offlineCardService.doCancellation(id);
        }
        writeJson(CreatState, response);
    }

    /**
     * 退回实体卡
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/doBackList")
    public void doBackList(HttpServletResponse response, String id) throws Exception {
        String CreatState = "0";
        if (!StringUtil.isNullOrEmpty(id)) {
            CreatState=offlineCardService.doBackList(id);
        }
        writeJson(CreatState, response);
    }

    /**
     * 撤回实体卡
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/doCancelCardList")
    public void doCancelCardList(HttpServletResponse response, String id) throws Exception {
        Map<String,Object> res = new HashMap<String, Object>();
        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute("user");
        String userId=user.getId();
        res.put("flag","1");
        res.put("msg","实体卡号为空");
        if (!StringUtil.isNullOrEmpty(id)) {
            res=offlineCardService.doCancelCardList(id,userId);
        }
        writeJson(res, response);
    }

    /**
     * 指定发放加载符合条件的发放列表
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping("showLoadCard")
    public void showLoadCard(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //必要的分页参数
        Map map = getParameterByRequest(request);

        Paging paging = offlineCardService.showLoadCard(map);

        writeJson(paging, response);
    }


}
