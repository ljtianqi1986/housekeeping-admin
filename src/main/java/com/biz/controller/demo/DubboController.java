package com.biz.controller.demo;

import com.biz.service.HService.dubbo.TestI;
import com.framework.controller.BaseController;
import com.framework.utils.ApplicationContextHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by liujiajia on 2016/12/27.
 */
@Controller
@RequestMapping("/dubbo")
public class DubboController extends BaseController {



    @RequestMapping("test")
    public void testDubbo(){

        try {
            TestI te22=(TestI)  ApplicationContextHelper.getBean("testService");
            String name= te22.testDubbo("3333");
            System.out.println(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
