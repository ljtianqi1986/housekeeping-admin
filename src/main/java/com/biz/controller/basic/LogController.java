package com.biz.controller.basic;

import com.biz.model.Pmodel.PSysLog;
import com.biz.service.basic.FileServiceI;
import com.biz.service.basic.LogServiceI;
import com.framework.controller.BaseController;
import com.framework.model.Operator;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.model.Params;
import com.framework.utils.SqlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomchen on 17/1/6.
 */
@Controller
@RequestMapping("/log")
public class LogController extends BaseController {

    @Autowired
    private LogServiceI logService;


    @RequestMapping("toLog")
    public ModelAndView toLog(ModelAndView mv){
        mv.setViewName("basic/log");
        return mv;
    }

    @RequestMapping("showLogs")
    public void showJqGrid(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("userName", request.getParameter("userName"));
        params.put("startDate", request.getParameter("startdate"));
        params.put("endDate", request.getParameter("enddate"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = Integer.parseInt(request.getParameter("offset"));

        Pager<PSysLog> pager = new Pager();
        pager.setParameters(params);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);

        pager = logService.queryLogs(pager);

        Paging<PSysLog> paging = new Paging<>();
        paging.setRows(pager.getExhibitDatas());
        paging.setTotal((long) pager.getRecordCount());

        writeJson(paging, response);
    }

}
