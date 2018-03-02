package com.biz.controller.activity;

import com.biz.model.Hmodel.TCardType;
import com.biz.model.Pmodel.PcardType;
import com.biz.model.Pmodel.User;
import com.biz.service.activity.CardTypeServiceI;
import com.framework.controller.BaseController;
import com.framework.model.MessageBox;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.utils.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomchen on 17/2/4.
 */
@Controller
@RequestMapping("/cardType")
public class CardTypeController extends BaseController {

    @Autowired
    private CardTypeServiceI cardTypeService;

    @RequestMapping("toCardType")
    public ModelAndView toCardType(ModelAndView mv){
        mv.setViewName("activity/cardType/cardType");
        return mv;
    }

    @RequestMapping("showDatas")
    public void showJqGrid(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("name", request.getParameter("name"));
        User user = (User) getShiroAttribute("userinfo");
        String agentId = user.getIdentity_code();
        if(agentId!=null&&!agentId.equals("")){
        }else{
            agentId = "1001";//todo remove
        }
        params.put("agentId", agentId);

        //params.put("phone", request.getParameter("phone"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = Integer.parseInt(request.getParameter("offset"));

        Pager<Map<String,Object>> pager = new Pager();
        pager.setParameters(params);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);

        pager = cardTypeService.queryCardTypes(pager);

        Paging<Map<String,Object>> paging = new Paging<>();
        paging.setRows(pager.getExhibitDatas());
        paging.setTotal((long) pager.getRecordCount());

        writeJson(paging, response);
    }

    @RequestMapping("doSave")
    public void doSave(HttpServletResponse response, HttpServletRequest request,PcardType pcardType) {
        MessageBox mb = getBox();
        try {
            if(StringUtil.isNullOrEmpty(pcardType.getId())){//add
                TCardType tCardType = new TCardType();
                BeanUtils.copyProperties(pcardType,tCardType);
                tCardType.setIsFirst(Integer.valueOf(pcardType.getIsFirst()));
                User user = (User) getShiroAttribute("userinfo");
                String agentId=user.getIdentity_code();
                if(agentId!=null&&!agentId.equals("")){
                }else{
                    agentId = "1001";//todo remove
                }
                tCardType.setUserId(user.getId().toString());
                tCardType.setAgentId(agentId);
                //tCardType.setUserId("1");//todo remove
                //tCardType.setAgentId("1001");//todo remove

                Date d = new Date();
                tCardType.setLastUpdateTime(d);
                tCardType.setCreateTime(d);
                cardTypeService.save(tCardType);
            }else{//update
                TCardType tCardType = cardTypeService.getById(pcardType.getId());
                tCardType.setIsFirst(Integer.valueOf(pcardType.getIsFirst()));
                tCardType.setName(pcardType.getName());
                tCardType.setPercentage(pcardType.getPercentage());
                tCardType.setLastUpdateTime(new Date());
                cardTypeService.update(tCardType);
            }
        }
        catch (Exception e){
            mb.setSuccess(false);
            mb.setMsg(e.getMessage());
            e.printStackTrace();
        }

        writeJson(mb,response);
    }

    @RequestMapping("toEdit")
    public ModelAndView toEdit(String id) throws Exception {
        mv.clear();
        mv.addObject("id",id);
        TCardType tCardType = cardTypeService.getById(id);
        mv.addObject("cardType",tCardType);
        mv.setViewName("activity/cardType/cardTypeDetail");
        return mv;
    }

    @RequestMapping("deByIds")
    public void delGridById(HttpServletResponse response, HttpServletRequest request,String ids) throws Exception {
        MessageBox mb = getBox();
        try{
            cardTypeService.executeHql(StringUtil.formateString("update TCardType set isdel =1 where id in ({0})",StringUtil.formatSqlIn(ids)));
            mb.setSuccess(true);
        }catch (Exception e){
            mb.setSuccess(false);
        }
        writeJson(mb,response);
    }


}
