package com.biz.controller.basic;

import com.alibaba.fastjson.JSON;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.Pslide;
import com.biz.model.Pmodel.basic.PwxgoodsGroup;
import com.biz.service.base.PicServiceI;
import com.biz.service.mq.PrmI;
import com.framework.controller.BaseController;
import com.framework.model.Paging;
import com.framework.model.Params;
import com.framework.utils.SqlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by tomchen on 17/1/6.
 */
@Controller
@RequestMapping("/pic")
public class PicController extends BaseController {

    @Autowired
    private PicServiceI picService;

    @Autowired
    private PrmI prm;

    @InitBinder("pslide")
    public void initBinderFormBean1(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("pslide.");
    }
    @InitBinder("pwxgoodsGroup")
    public void initBinderFormBean2(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("pwxgoodsGroup.");
    }

    @RequestMapping("phoneRunPic")
    public ModelAndView phoneRunPic(ModelAndView mv){
        mv.setViewName("basic/runPic/runPic");
        return mv;
    }

    /**
     * 加载轮辐信息
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("findPageForRunPic")
    public void findPageForRunPic(HttpServletResponse response, HttpServletRequest request) throws Exception
    {
        User user = (User)getShiroAttribute("user");
        //必要的分页参数
        SqlFactory factory=new SqlFactory();
        Params sqlParams=factory.getParams();
        sqlParams.setOrder(request.getParameter("order"));
        sqlParams.setPage(Integer.parseInt(request.getParameter("offset")));
        sqlParams.setRows(Integer.parseInt(request.getParameter("limit")));
        sqlParams.setSort(request.getParameter("sort"));
        sqlParams.setIdentity_code(user.getId());
        Paging paging=picService.findMerchantGrid(sqlParams);
        writeJson(paging,response);

    }

    /**
     * 跳转新增界面
     * @param mv
     * @return
     */
    @RequestMapping("toRunPicEdit")
    public ModelAndView toRunPicEdit(ModelAndView mv){
        mv.addObject("page_type","0");
        mv.setViewName("basic/runPic/runPic_edit");
        return mv;
    }

    /**
     * 跳转活动商品展示页
     * @param mv
     * @return
     */
    @RequestMapping("toGoodsList")
    public ModelAndView toGoodsList(ModelAndView mv){
        mv.setViewName("basic/runPic/picGoods");
        return mv;
    }


    /**
     *保存轮辐
     * @param response
     * @param request
     * @param pslide
     * @throws Exception
     */
    @RequestMapping("doSave")
    public void doSave(HttpServletResponse response, HttpServletRequest request,Pslide pslide) throws Exception {
        String msg="";
        msg=picService.doSave(pslide);
        writeJson(msg,response);
    }


    /**
     * 跳转编辑界面
     * @param mv
     * @return
     */
    @RequestMapping("toEdit")
    public ModelAndView toEdit(ModelAndView mv,String id){
        mv.addObject("page_type","1");
        mv.addObject("id",id);
        mv.setViewName("basic/runPic/runPic_edit");
        return mv;
    }

    /**
     *编辑显示信息
     * @param response
     * @param request
     * @param
     * @throws Exception
     */
    @RequestMapping("toGetEdit")
    public void toGetEdit(HttpServletResponse response, HttpServletRequest request,String id) throws Exception {
        Pslide pslide=picService.toGetEdit(id);
        writeJson(pslide,response);
    }

    /**
     *删除
     * @param response
     * @param request
     * @param
     * @throws Exception
     */
    @RequestMapping("delGridById")
    public void delGridById(HttpServletResponse response, HttpServletRequest request,String ids) throws Exception {
       String msg=picService.delGridById(ids);
        writeJson(msg,response);
    }

    /**
     * 跳转商品设置页面
     * @param mv
     * @return
     */
    @RequestMapping("toGoodsGroup")
    public ModelAndView toGoodsGroup(ModelAndView mv){
        mv.clear();
        mv.setViewName("basic/goodsGroup/goodsGroup");
        return mv;
    }

    @RequestMapping("showGoodsGroup")
    public void showGoodsGroup(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //必要的分页参数
        SqlFactory factory=new SqlFactory();
        Params sqlParams=factory.getParams();
        sqlParams.setOrder(request.getParameter("order"));
        sqlParams.setPage(Integer.parseInt(request.getParameter("offset")));
        sqlParams.setRows(Integer.parseInt(request.getParameter("limit")));
        sqlParams.setSort(request.getParameter("sort"));
        //  System.out.println(request.getParameter("customer"));
        sqlParams.setSearchtext(request.getParameter("name")==null?"":request.getParameter("name").toString());
        Paging paging=picService.findGoodsGroupGrid(sqlParams);
        System.out.println(JSON.toJSONString(paging));
        writeJson(paging,response);
    }

    @RequestMapping("toEditGroupDetail")
    public ModelAndView toEditGroupDetail(String id) throws Exception {
        mv.clear();
        mv.addObject("id",id);
        PwxgoodsGroup PwxgoodsGroup=picService.findPwxgoodsGroupById(id);
        mv.addObject("PwxgoodsGroup",PwxgoodsGroup);
        mv.setViewName("basic/goodsGroup/goodsGroupDetail");
        return mv;
    }

    @RequestMapping("loadInfo")
    public void loadInfo(HttpServletResponse response,String id) throws Exception {
        PwxgoodsGroup PwxgoodsGroup=picService.findPwxgoodsGroupById(id);
        writeJson(PwxgoodsGroup,response);
    }

    @RequestMapping("doUpdateGoodsGroup")
    public void doUpdateGoodsGroup(HttpServletResponse response,PwxgoodsGroup pwxgoodsGroup) throws Exception {
        String res="";
        try{
            picService.updateGoodsGroup(pwxgoodsGroup);
            res="success";
        }
        catch (Exception e)
        {res="操作失败！";}

        writeJson(res,response);
    }


    public void test22(){

    }
}
