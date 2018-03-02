package com.biz.controller.basic;

import com.alibaba.fastjson.JSON;
import com.biz.model.Hmodel.api.TOrderMain90;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.BaseUser;
import com.biz.service.api.OrderMain90ServiceI;
import com.biz.service.basic.AgentServiceI;
import com.biz.service.basic.UserServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.*;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ldd_person on 2017/5/2.
 */
@Controller
@RequestMapping("/jlcmt")
public class JiulingCouponsMTController extends BaseController{

    @Resource(name = "userService")
    private UserServiceI userService;

    @Resource(name = "agentService")
    private AgentServiceI agentServiceI;

    @Resource(name = "orderMain90Service")
    private OrderMain90ServiceI orderMain90ServiceI;

    /**
     * 跳转至久零券人工处理页面
     * @param mv
     * @return
     */
    @RequestMapping("/toView")
    public ModelAndView toView(ModelAndView mv){

        mv.setViewName("basic/manualTreatment/manualTreatment");

        return mv;
    }

    @RequestMapping(value = "/doManualTreatment")
    public void doIssuedGift(String phone,String total,String targetBrand,HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();

        //品牌登录
        if (getShiroAttribute("identity") != null && !StringUtil.isNullOrEmpty(getShiroAttribute("identity")) && getShiroAttribute("identity").toString().equals("2")) {


            User user = (User) getShiroAttribute("user");
            BaseUser baseUser = userService.findBaseUserByPhone(phone);

            //生成order_main_90表的订单数据
            TOrderMain90 tOrderMain90 = new TOrderMain90();
            tOrderMain90.setId(UuidUtil.get32UUID());
            tOrderMain90.setCode("90" + Tools.RandomDemo());
            BigDecimal bigDecimal =new BigDecimal(total);
            tOrderMain90.setPayCoin(bigDecimal.multiply(new BigDecimal(100)));
            tOrderMain90.setOpenId(baseUser.getOpen_id().toString());
            tOrderMain90.setBrandCode(targetBrand);
            tOrderMain90.setTradeType("offline");

            String id =(String)orderMain90ServiceI.save(tOrderMain90);

            Map<String, String> map1 = new HashMap<>();
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("brand_code", targetBrand);
            jSONObject.put("order_code", id);
            jSONObject.put("open_id", baseUser.getOpen_id());
            jSONObject.put("type", 3);
            jSONObject.put("source", 3);
            jSONObject.put("source_msg", "人工扣券");
            jSONObject.put("balance_90", (int) (Double.parseDouble(total) * 100));
            jSONObject.put("state", 1);
            jSONObject.put("commission", 0);
            jSONObject.put("tradeType", "");
            jSONObject.put("orderState", 1);
            jSONObject.put("orderTotal", 0);
            map1.put("json", jSONObject.toString());
            System.out.print(jSONObject.toString());

            String requestUrl = ConfigUtil.get("QT_Url") + "api/balance/operUserBalance90.ac";
            String x = URLConectionUtil.httpURLConnectionPostDiy(requestUrl, map1);
            x = URLDecoder.decode(x, "utf-8");
            if (x == null || x.trim().equals("")) {
                map.put("isok", false);
                map.put("msg", "扣劵失败");
            } else {
                System.out.println(x.toString());
                @SuppressWarnings({"unused", "unchecked"})
                Map<String, Object> map2 = JSON.parseObject(x, Map.class);
                if (map2 != null && map2.get("flag").equals("0"))  //发劵成功
                {
                    //修改order_main_90表 state 和pay_90字段
                    tOrderMain90.setState((short)1);
                    tOrderMain90.setPay90((int) (Double.parseDouble(total) * 100));
                    orderMain90ServiceI.update(tOrderMain90);
                    map.put("isok", true);
                    map.put("msg", map2.get("msg"));
                } else {
                    map.put("isok", false);
                    map.put("msg", map2.get("msg") == null ? "异常错误" : map2.get("msg"));
                }
            }
        }else{
            map.put("isok", false);
            map.put("msg", "只有代理商才有此功能！");
        }
            writeJson(map, response);
    }

    /**
     * 查询商户信息
     * @param response
     * @throws Exception
     */
    @RequestMapping("/queryBrandByAgent")
    public void queryBrandByAgent(HttpServletResponse response) throws Exception {
        User user = (User) getShiroAttribute("user");
        List<Map<String,Object>> brand_agent = agentServiceI.queryBrandByAgent(user.getIdentity_code().toString());
        writeJson(brand_agent,response);
    }
}
