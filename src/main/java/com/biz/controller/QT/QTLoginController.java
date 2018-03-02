package com.biz.controller.QT;

import com.alibaba.fastjson.JSON;
import com.biz.model.Pmodel.QT.PqtUser;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.ConfirmAnOrder;
import com.biz.service.activity.CardTypeServiceI;
import com.biz.service.api.PhoneUserServiceI;
import com.biz.service.basic.UserServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.ConfigUtil;
import com.framework.utils.MD5;
import com.framework.utils.URLConectionUtil;
import com.framework.utils.weixin.HttpRequestUtil;
import net.sf.json.JSONArray;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.authc.UsernamePasswordToken;

import javax.annotation.Resource;
import javax.naming.event.ObjectChangeListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.apache.shiro.session.Session;
import org.apache.shiro.authc.AuthenticationException;
import net.sf.json.JSONObject;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;



/**
 * Created by tomchen on 17/2/4.
 */
@Controller
@RequestMapping("/QT/loginQT")
public class QTLoginController extends BaseController {

    @Autowired
    private UserServiceI userService;

    private String Login_rul="/api/QTUser/phoneDeskLogin.ac";//QT登录接口

    /**
     * 登陆页
     * @param mv
     * @return
     */
    @RequestMapping("toLoginQT")
    public ModelAndView toCardType(ModelAndView mv){
        mv.addObject("msg","");
        mv.setViewName("QT/login/login");
        return mv;
    }

    // 定义address对象
    @InitBinder("user")
    public void initBinderAddress(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("user.");
    }

    /**
     * QT用户登录系统
     * @param mv
     * @return
     */
    @RequestMapping("toQTHome")
    public ModelAndView toQTHome(ModelAndView mv,User user)throws Exception{
        String msg="";
        try {
            Map<String, String> InterfaceMap=new HashMap<>();
            InterfaceMap.put("login_name",user.getUsername().trim());
            InterfaceMap.put("pwd", MD5.md5(user.getPassword().trim()));
            Map<String, Object> r_map=QTInterface(InterfaceMap, Login_rul);
            if(r_map==null || (r_map.size()==0)){
                mv.setViewName("QT/login/login"); //失败
            }else if((Integer) r_map.get("return_code")==0){
                mv.setViewName("QT/login/login");//失败
                msg=r_map.get("return_info").toString();
            }else if((Integer) r_map.get("return_code")==1){
                //成功
                PqtUser userMap = JSON.parseObject(r_map.get("return_data").toString(), PqtUser.class);

                setShiroAttribute("version", userService.getCurrentVersion());
                setShiroAttribute("QTUser", userMap);
                setShiroAttribute("Type", "QT");
                Session session=SecurityUtils.getSubject().getSession();
                mv.setViewName("QT/main/index");
            }
        }catch (AuthenticationException e){
            mv.setViewName("QT/login/login");
        }

        mv.addObject("msg",msg);
        return mv;
    }

    //首页
    @RequestMapping("toIndex")
    public ModelAndView toIndex(ModelAndView mv){
        mv.setViewName("QT/main/index");
        return mv;
    }




    //接口
    public Map<String, Object> QTInterface(Map<String,String> jSONObject,String url){
        Map<String, Object> map2=new HashMap<String, Object>();
        try {
            String requestUrl = ConfigUtil.get("QT_Url")+url;
            String x= URLConectionUtil.httpURLConnectionPostDiy(requestUrl,jSONObject);
            x = URLDecoder.decode(x, "utf-8");
            if(x==null||x.trim().equals("")){
               // 失败
            }else {
                map2 = JSON.parseObject(x, Map.class);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        finally {
            return map2;
        }
    }





}
