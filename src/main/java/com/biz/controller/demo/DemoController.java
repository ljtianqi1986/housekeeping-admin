package com.biz.controller.demo;

import com.alibaba.fastjson.JSONObject;
//import com.biz.service.MService.goods.GoodsServiceI;
import com.biz.service.demo.DemoServiceI;
import com.biz.service.task.ZyServiceImpl;
import com.framework.controller.BaseController;
import com.framework.model.MessageBox;
import com.framework.model.Operator;
import com.framework.model.Paging;
import com.framework.model.Params;
import com.framework.utils.ApplicationContextHelper;
import com.framework.utils.SqlFactory;
import com.jswzc.jiuling.service.order.ZyServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*************************************************************************
 * 版本：         V1.0  
 *
 * 文件名称 ：DemoController.java 描述说明 ：新系统所有Demo样例
 * 
 * 创建信息 : create by 刘佳佳 on 2016-6-14 下午3:34:13  修订信息 : modify by ( ) on (date) for ( )
 * 
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/

@Controller
@RequestMapping("/demo")
public class DemoController extends BaseController {
	private static final long serialVersionUID = 1417800463296358902L;
	@Autowired
	private DemoServiceI demoService;

    @Autowired
    private ZyServiceI zyService;

	/**
	 * 表格Demo jqgrid
	 * @param mv
	 * @return
	 */
	@RequestMapping("toQgrid")
	public ModelAndView toQgrid(ModelAndView mv){
		mv.setViewName("demo/table1");
		return mv;
	}

    /**
     * Demo Table Json Data
     * @param response
     */
    @RequestMapping("showJqGrid")
    public void showJqGrid(HttpServletResponse response,HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map<String,Object> params=new HashMap<String, Object>();
        params.put(Operator._LIKE+"_customer", "%" + request.getParameter("customer")+"%");
        params.put(Operator._EQUALS+"_state",0);
        //必要的分页参数
        SqlFactory factory=new SqlFactory();
        Params sqlParams=factory.getParams();
        sqlParams.setOrder(request.getParameter("order"));
        sqlParams.setPage(Integer.parseInt(request.getParameter("offset")));
        sqlParams.setRows(Integer.parseInt(request.getParameter("limit")));
        sqlParams.setSort(request.getParameter("sort"));
        //分页
        Paging paging=demoService.getDemoPage(sqlParams,params);
//        System.out.println(">>>>>>>>> " + "每页数量：" + request.getParameter("limit")+
//                "排序字段："+request.getParameter("sort")+
//                "排序方式:"+request.getParameter("order")+
//                "搜索框字段:"+request.getParameter("searchtext"));
        writeJson(paging, response);
    }



    /**
     * 表格Demo BootStrapTable
     * @param mv
     * @return
     */
    @RequestMapping("toBootGrid")
    public ModelAndView toBootGrid(ModelAndView mv){
        mv.setViewName("demo/bootTable");
        return mv;
    }

    /**
     * 表格Demo BootStrapTable 批量删除
     * @param response
     * @param ids
     */
    @RequestMapping("delGridById")
    public void delGridById(HttpServletResponse response,String ids){
        MessageBox box=new MessageBox();
        try{
            if(ids!=null){
                demoService.delGridById(ids);
            }else{
                box.setSuccess(false);
                box.setMsg("未指定");
            }
        }catch (Exception e){
            box.setSuccess(false);
        }
        writeJson(box, response);
    }


    /**
     * Tab&Icons 样例
     * @param mv
     * @return
     */
    @RequestMapping("toTabIcon")
    public ModelAndView toTabIcon(ModelAndView mv){
        mv.setViewName("demo/tab_icon");
        return mv;
    }

    /**
     * 视屏&音频样例
     * @param mv
     * @return
     */
    @RequestMapping("toFilm")
    public ModelAndView toFilm(ModelAndView mv){
        mv.setViewName("demo/film");
        return mv;
    }

    /**
     * JsTree 样例
     * @param mv
     * @return
     */
    @RequestMapping("toTree")
    public ModelAndView toTree(ModelAndView mv){
        mv.setViewName("demo/jstree");
        return mv;
    }


    /**
     * LayerDate 日期控件
     * @param mv
     * @return
     */
    @RequestMapping("toDate")
    public ModelAndView toDate(ModelAndView mv){
        mv.setViewName("demo/layerDate");
        return mv;
    }


    /**
     * 富文本框Demo
     * @param mv
     * @return
     */
    @RequestMapping("toRichtext")
    public ModelAndView toRichtext(ModelAndView mv){
        mv.setViewName("demo/form_editors");
        return mv;
    }


    /**
     * 代码格式化工具
     * @param mv
     * @return
     */
    @RequestMapping("toCodeFmat")
    public ModelAndView toCodeFmat(ModelAndView mv){
        mv.setViewName("demo/code_fomat");
        return mv;
    }

    /**
     * Dubbo消费测试
     * @param mv
     * @return
     */
    @RequestMapping("dubboTest")
    public ModelAndView dubboTest(ModelAndView mv){
        mv.setViewName("demo/dubbotest");
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"/conf/spring-dubbo.xml"});
//        context.start();
//        try {
//            DubboServiceI dubboService = (DubboServiceI) context.getBean("dubboService");
//            System.out.println(dubboService.testConnect());
//        }catch (Exception e){
//            e.printStackTrace();
//        }

//        DubboServiceI dubboService=(DubboServiceI) ApplicationContextHelper.getBean("dubboService");
//        String aa=dubboService.testConnect();
//        mv.addObject("result",aa);
//        GoodsServiceI goodsService=(GoodsServiceI) ApplicationContextHelper.getBean("goodsService");
//        try {
//            List<Map<String, String>> list= goodsService.getGroupsList("1233","131c505a69154b66891d309f7298aece");
//            mv.addObject("result", JSONObject.toJSONString(list));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return mv;
    }


    /**
     * 表格Demo jqgrid
     * @param mv
     * @return
     */
   /* @RequestMapping("testCustom")
    public void  testCustom(ModelAndView mv)throws Exception{
        zyService.confirmCustomerService();
    }*/


}
