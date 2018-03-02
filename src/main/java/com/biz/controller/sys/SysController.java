package com.biz.controller.sys;

import com.framework.controller.BaseController;
import com.framework.utils.UuidUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 系统设置相关
 * Created by liujiajia on 2016/11/14.
 */
@Controller
@RequestMapping("/sys")
public class SysController extends BaseController{

    /**
     * 跳转系统设置页面
     * @param mv
     * @return
     */
    @RequestMapping("toMenu")
    public ModelAndView toMenu(ModelAndView mv){
        mv.setViewName("sys/menu");
        return mv;
    }

    /**
     * 得到随机数
     *
     * @return
     */
    public String getRndCode()
    {
        return UuidUtil.get32UUID();
    }
}
