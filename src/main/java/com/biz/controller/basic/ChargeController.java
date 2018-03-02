package com.biz.controller.basic;

import com.biz.model.Pmodel.PChargeCoinDiary;
import com.biz.service.basic.ChargeServiceI;
import com.framework.controller.BaseController;
import com.framework.model.Pager;
import com.framework.model.Paging;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xy on 17/1/6.
 */
@Controller
@RequestMapping("/charge")
public class ChargeController extends BaseController {

@Resource(name="chargeService")
private ChargeServiceI chargeService;

    @RequestMapping("toJiulingCoid")
    public ModelAndView toLog(ModelAndView mv){
        mv.setViewName("basic/jiulingCoin");
        return mv;
    }


    @RequestMapping("showJiulingCoinChargeDiary")
    public void showJiulingCoinChargeDiary(HttpServletResponse response, HttpServletRequest request)throws Exception{

        //前端封装的查询参数
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("userName", request.getParameter("userName"));
        params.put("startDate", request.getParameter("startdate"));
        params.put("endDate", request.getParameter("enddate"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = Integer.parseInt(request.getParameter("offset"));

        Pager<PChargeCoinDiary> pager = new Pager();
        pager.setParameters(params);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);

        pager =  chargeService.queryJiulingCoinChargeDiaryPager(pager);

        Paging<PChargeCoinDiary> paging = new Paging<>();
        paging.setRows(pager.getExhibitDatas());
        paging.setTotal((long) pager.getRecordCount());

        writeJson(paging, response);
    }

}
