package com.biz.controller.QT;

import com.alibaba.fastjson.JSON;
import com.biz.model.Pmodel.QT.PqtUser;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.Result;
import com.biz.service.basic.UserServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.ConfigUtil;
import com.framework.utils.URLConectionUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by tomchen on 17/2/4.
 */
@Controller
@RequestMapping("/api/QTClerk")
public class QTClerkController extends BaseController {

    @Autowired
    private UserServiceI userService;

    @InitBinder("user")
    public void initBinderFormBean1(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("user.");
    }

    @RequestMapping("QTtoClerk")
    public ModelAndView QTtoClerk(ModelAndView mv){
        mv.setViewName("QT/clerk/clerk");
        return mv;
    }
    //查询收银员
    @RequestMapping("QTqueryClerk")
    public void QTqueryClerk(HttpServletResponse response) throws  Exception{
        JSONObject jSONObject = new JSONObject();
        Map map = new HashMap();
        PqtUser pqtUser=(PqtUser) getShiroAttribute("QTUser");
        String shopId=pqtUser.getIdentity_code();
            String requestUrl = ConfigUtil.get("QT_Url")+"/api/QTclerk/phone_QueryClerk.ac";
            jSONObject.put("shop_id",shopId);
            String x= URLConectionUtil.httpURLConnectionPostDiy(requestUrl,jSONObject);
            x = URLDecoder.decode(x, "utf-8");
            if(x==null||x.trim().equals("")){
                // 失败
            }else {
                map = JSON.parseObject(x, Map.class);
            }
          writeJson(map,response);
    }

    //新增收营员
    @RequestMapping("QTinsertClerk")
    public void QTinsertClerk(HttpServletResponse response,User user) throws  Exception{
        JSONObject jSONObject = new JSONObject();
        Map map = new HashMap();
        PqtUser pqtUser=(PqtUser) getShiroAttribute("QTUser");
        String shopId=pqtUser.getIdentity_code();
        String requestUrl = ConfigUtil.get("QT_Url")+"/api/QTclerk/phone_AddClerk.ac";
        jSONObject.put("shop_id",shopId);
        jSONObject.put("person_name",user.getPerson_name());
        jSONObject.put("login_name",user.getLogin_name());
        jSONObject.put("pwd",user.getPwd());
        jSONObject.put("phone",user.getPhone());
        String x= URLConectionUtil.httpURLConnectionPostDiy(requestUrl,jSONObject);
        x = URLDecoder.decode(x, "utf-8");
        if(x==null||x.trim().equals("")){
            // 失败
        }else {
            map = JSON.parseObject(x, Map.class);
        }
        writeJson(map,response);
    }

    //删除收营员
    @RequestMapping("delClerk")
    public void delClerk(HttpServletResponse response,String user_code) throws  Exception{
        JSONObject jSONObject = new JSONObject();
        Map map = new HashMap();
        String requestUrl = ConfigUtil.get("QT_Url")+"/api/QTclerk/phone_DelClerk.ac";
        jSONObject.put("user_code",user_code);
        String x= URLConectionUtil.httpURLConnectionPostDiy(requestUrl,jSONObject);
        x = URLDecoder.decode(x, "utf-8");
        if(x==null||x.trim().equals("")){
            // 失败
        }else {
            map = JSON.parseObject(x, Map.class);
        }
        writeJson(map,response);
    }

   //修改收营员密码
    @RequestMapping("updatePwd")
    public void updatePwd(HttpServletResponse response,User user) throws  Exception{
        JSONObject jSONObject = new JSONObject();
        Map map = new HashMap();
        String requestUrl = ConfigUtil.get("QT_Url")+"/api/QTclerk/phone_resetClerkPwd.ac";
        PqtUser pqtUser=(PqtUser) getShiroAttribute("QTUser");
        jSONObject.put("clerk_user_code",user.getId());
        jSONObject.put("user_code",pqtUser.getUser_code());
        jSONObject.put("pwd",user.getAdminPwd());
        jSONObject.put("clerk_pwd",user.getNewPwd());
        String x= URLConectionUtil.httpURLConnectionPostDiy(requestUrl,jSONObject);
        x = URLDecoder.decode(x, "utf-8");
        if(x==null||x.trim().equals("")){
            // 失败
        }else {
            map = JSON.parseObject(x, Map.class);
        }
        writeJson(map,response);
    }

    //修改收营员手机号
    @RequestMapping("phone_updatePhone")
    public void phone_updatePhone(HttpServletResponse response,String id,String phone) throws  Exception{
        JSONObject jSONObject = new JSONObject();
        Map map = new HashMap();
        String requestUrl = ConfigUtil.get("QT_Url")+"/api/QTclerk/phone_updatePhone.ac";
        jSONObject.put("user_code",id);
        jSONObject.put("phone",phone);
        String x= URLConectionUtil.httpURLConnectionPostDiy(requestUrl,jSONObject);
        x = URLDecoder.decode(x, "utf-8");
        if(x==null||x.trim().equals("")){
            // 失败
        }else {
            map = JSON.parseObject(x, Map.class);
        }
        writeJson(map,response);
    }


    //修改收营员锁定状态
    @RequestMapping("phone_LockClerk")
    public void phone_LockClerk(HttpServletResponse response,String id,String flag) throws  Exception{
        JSONObject jSONObject = new JSONObject();
        Map map = new HashMap();
        String requestUrl = ConfigUtil.get("QT_Url")+"/api/QTclerk/phone_LockClerk.ac";
        jSONObject.put("clerk_user_code",id);
        if(flag.equals("true")){
            jSONObject.put("islock","0");
        }else{
            jSONObject.put("islock","1");
        }
        String x= URLConectionUtil.httpURLConnectionPostDiy(requestUrl,jSONObject);
        x = URLDecoder.decode(x, "utf-8");
        if(x==null||x.trim().equals("")){
            // 失败
        }else {
            map = JSON.parseObject(x, Map.class);
        }
        writeJson(map,response);
    }


}
