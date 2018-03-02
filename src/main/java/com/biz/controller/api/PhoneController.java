package com.biz.controller.api;

import com.biz.model.Pmodel.api.BaseUser;
import com.biz.model.Pmodel.api.OrderMainUnion;
import com.biz.model.Pmodel.api.Record90Detail;
import com.biz.model.Singleton.ZkNode;
import com.biz.service.api.BaseUserServiceI;
import com.biz.service.api.OrderMainServiceI;
import com.framework.controller.BaseController;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 包含常用登录，验券
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/api/QTrecord")
public class PhoneController extends BaseController {
	@Resource(name = "orderMainService")
	private OrderMainServiceI orderMainService;


    @Resource(name = "baseUserService")
    private BaseUserServiceI baseUserService;

	/**
	 * 发劵记录
	 */
	@RequestMapping(value = "/phone_90Record")
	public void phone_90Record(HttpServletResponse response, String code, String page)
	{
		//logger.error("验券记录=参数code=" + code+"=page="+page+"=type="+type);
		JSONObject jSONObject = new JSONObject();
		if(StringUtils.isBlank(page)){
			jSONObject.put("return_code", 0);
			jSONObject.put("return_info", "接口参数错误：page为空");
			jSONObject.put("return_data", null);
		}else{
            int page_int = Integer.valueOf(page);
            int pageSize = 10;
            Map<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("pageSize", pageSize);
            hashMap.put("pageNum", pageSize * (page_int - 1));
            hashMap.put("code", code);
            try {
                List<Record90Detail> record90DetailList =orderMainService.phone_90Record(hashMap);
                int total =orderMainService.phone_90RecordCount(hashMap);
                JSONArray jsonStrs = new JSONArray();
                for (Record90Detail record90Detai : record90DetailList) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("receive_name", record90Detai.getReceive_name());
                    jsonObject.put("clerk_name", record90Detai.getClerk_name());
                    jsonObject.put("point_90", record90Detai.getPoint_90());
                    jsonObject.put("source", record90Detai.getSource());
                    jsonObject.put("create_time", record90Detai.getCreate_time());
                    jsonStrs.add(jsonObject);
                }
                jSONObject.put("return_code", 1);
                jSONObject.put("return_info", "");
                jSONObject.put("return_data", jsonStrs);
                jSONObject.put("total", total);
                //logger.error("验券记录=返回数据jSONObject=" + jSONObject);
            } catch (Exception e) {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", e.getMessage());
                jSONObject.put("return_data", null);
                logger.error("发劵记录=报错=" ,e.fillInStackTrace());
                e.printStackTrace();
            }
        }

        writeJsonNoReplace(jSONObject,response);
	}


    /**
     * 体验店，用户验证
     */
    @RequestMapping(value = "/phone_validate_user")
    public void phone_validate_user(HttpServletResponse response,String shop_code,String only_code,String total){
        logger.error("=体验店，用户验证=shop_code="+shop_code+"=only_code="+only_code+"=total="+total);
        JSONObject jSONObject = new JSONObject();

        if (only_code.length()!=20)
        {
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info","错误-非法付款码！");
            logger.error("体验店，用户验证错误，长度不对，非法付款码！");
            //return jSONObject;
            writeJson(jSONObject, response);
        }

        try {

            List<BaseUser> user_list = baseUserService.queryBaseUserByOnlyCode(only_code);
            if (user_list==null)
            {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info","错误-非法的付款码，无法支付");
                logger.error("找不到该付款码，无法支付");
                //return jSONObject;
                writeJson(jSONObject, response);
            }

            if (user_list.size()!=1)
            {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info","错误-付款码已失效，请刷新后再试");
                logger.error("生成相同的付款码，付款码失效");
                //return jSONObject;
                writeJson(jSONObject, response);
            }

            //判断付款码的是否失效
            /*Long curr_time = new Date().getTime();
            Long timestamp = Long.valueOf(only_code.substring(6,19));

            if((curr_time - timestamp)/1000>=65)
            {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info","错误-付款码已超时，请刷新后再试");
                logger.error("付款码已超时");
                //return jSONObject;
                writeJson(jSONObject, response);
            }*/

            /***********对传入金额处理*************/
            double balance_90 = 0d;//分
            try {
                balance_90 = money_convert(total);
            } catch (Exception e) {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", "金额错误，金额为"+total);
                logger.error("体验店，用户验证错误，金额为"+total);
                //return jSONObject;
                writeJson(jSONObject, response);
            }
            /***********验证用户90券够不够*************/
            BaseUser user = user_list.get(0);
            if(user == null ){
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", "未找到用户！");
                logger.error("用户不存在=only_code="+only_code);
                //return jSONObject;
                writeJson(jSONObject, response);
            }
            double user_balance = user.getBalance_90();
            if(user_balance < balance_90){
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", "用户久零券余额不足，目前余额为："+user_balance/100+"元");
                //return jSONObject;
                writeJson(jSONObject, response);
            }

            String tSysUserId = baseUserService.getSysUserIdByOpenId(user.getOpen_id());

            Double coin_90=baseUserService.getCoin_90ByTUserId(tSysUserId);
            coin_90=coin_90!=null ? coin_90: 0.0;
            //服务费:元
            BigDecimal balance = new BigDecimal(Double.valueOf(total)); //需要支付的钱
            BigDecimal service_charge = new BigDecimal(ZkNode.getIstance().getJsonConfig().get("service_charge")+""); //费率
            //四舍五入取两位小数
            //BigDecimal fee = balance.multiply(service_charge).setScale(2,BigDecimal.ROUND_HALF_UP);
            //四舍五入取整
            BigDecimal fee = balance.multiply(service_charge).setScale(0,BigDecimal.ROUND_HALF_UP);
            jSONObject.put("return_code", 1);
            jSONObject.put("return_info", fee);
            jSONObject.put("open_id", user.getOpen_id());
            if(coin_90>=fee.doubleValue())
            {
                jSONObject.put("ninezero", "1");
            }else
            {
                jSONObject.put("ninezero", "0");
            }
            jSONObject.put("needMoney",fee.doubleValue()-coin_90);
        }catch (Exception e){
            String msg = e.getMessage();
            if(StringUtils.isNotBlank(msg) && msg.length()>30){
                msg = msg.substring(0, 29);
            }
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", "错误-" +msg);
            logger.error("=体验店，用户验证=报错=" + e, e.fillInStackTrace());
        }
        writeJson(jSONObject, response);
        //return jSONObject;
    }








    /**
     * 收银记录 分收银员|店长查询
     * type
     * trade_type：
     * 0:全部 1：刷卡 2：扫码 3：公众号
     *
     *
     * @author zhengXin 2016-11-01 Update
     * 修改方法，增加收银记录检索，
     * 检索字段：
     * 	手机号--telephone
     * 	订单号--orderCode
     * 	时间区间--dateRange
     */
    @RequestMapping(value = "/phone_collectRecord2")
    public void phone_collectRecord2(HttpServletResponse response,String code,String state,String trade_type,
                                                         String page,String type, String dateRange, String telephone, String orderCode)
    {
        //JSONObject jSONObject = new JSONObject();
        Map<String,Object> jSONObject=new HashMap<>();
        boolean isOk=true;
        if(StringUtils.isBlank(page)){
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", "接口参数错误：page为空");
            jSONObject.put("return_data", null);
            isOk=false;
        }
        if(StringUtils.isBlank(type)){
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", "接口参数错误：type为空");
            jSONObject.put("return_data", null);
            isOk=false;
        }
if(isOk){
    int page_int = Integer.valueOf(page);
    int pageSize = 10;
    Map<String,Object> pd = new HashMap<>();
    pd.put("pageSize", pageSize);
    pd.put("pageNum", pageSize * (page_int - 1));
    pd.put("code", code);
    pd.put("state", state);
    pd.put("type", type);
    pd.put("trade_type", trade_type);

		/*Update zhengXin 2016-11-01 */
    pd.put("orderCode", orderCode);
    pd.put("telephone", telephone);

    //支付类型 MICROPAY:刷卡支付 NATIVE:扫码支付 JSAPI:公众号支付  OFFLINE：线下支付
    //0:全部 1：刷卡 2：扫码 3：公众号 4:线下支付
    try {
			/*Update zhengXin 2016-11-01 */
        if(StringUtils.isNotBlank(dateRange))
        {
            String time = dateRange;
            //拼接时间戳
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String begin = time.substring(0, 10);
            String str2 = begin+" 00:00:00";
            Date today2 = sdf.parse(str2);
            pd.put("beginStamp", String.valueOf(today2.getTime()/1000));
            String end = time.substring(13, 23);
            String str1 = end+" 23:59:59";
            Date today1 = sdf.parse(str1);
            pd.put("endStamp", String.valueOf(today1.getTime()/1000));
        }

        List<OrderMainUnion> orderMainList = orderMainService.collectRecordForClerk(pd);
        List<Map<String,Object>> jsonStrs = new ArrayList<Map<String,Object>>();
        for (OrderMainUnion orderMain : orderMainList) {
            Map<String,Object> jsonObject = new HashMap<>();
            jsonObject.put("code", notNull(orderMain.getCode()));
            jsonObject.put("card_total", orderMain.getCard_total());
            if (StringUtils.isNotBlank(state) && state.equals("3")) {
                double totals = orderMain.getOld_order_total()-orderMain.getOrder_total();
                jsonObject.put("order_total", totals);
            }
            else
            {
                jsonObject.put("order_total", orderMain.getOrder_total());
            }
            jsonObject.put("state", orderMain.getState());
            jsonObject.put("person_name", notNull(orderMain.getPerson_name()));
            jsonObject.put("create_time", notNull(orderMain.getCreate_time()));
            jsonObject.put("custom_name", notNull(orderMain.getCustom_name()));
            jsonObject.put("trade_type",notNull(orderMain.getTrade_type()));
            jsonObject.put("pay_type",notNull(String.valueOf(orderMain.getPay_type())));
            jsonObject.put("pay_user_id",notNull(String.valueOf(orderMain.getPay_user_id())));
            jsonObject.put("remark", "");
            jsonStrs.add(jsonObject);
        }
        jSONObject.put("return_code", 1);
        jSONObject.put("return_info", "");
        jSONObject.put("return_data", jsonStrs);
//			logger.error("收银记录=返回数据jSONObject=" + jSONObject);
    } catch (Exception e) {
        jSONObject.put("return_code", 0);
        jSONObject.put("return_info", e.getMessage());
        jSONObject.put("return_data", null);
        logger.error("收银记录=报错=" ,e.fillInStackTrace());
        e.printStackTrace();
    }
}

        writeJson(jSONObject,response); ;
    }
    public String notNull(String in)
    {
        if(StringUtils.isBlank(in)){
            return "";
        }
        return in;
    }


    /**
     * 首页统计
     * currentCoupons 正在使用优惠卷
     * todayUseCouponsMoney 今日用券金额
     * todayUseCouponsSum 今日用券数量
     * todayUsePerson 今日用券人数
     * todayGetPerson 今日领券人数
     * todayMoneyTotal 今日收银总额
     * todayMoneyCount 今日收银数量（笔）
     */
    @RequestMapping(value = "/phone_home_statistics")
    public void phone_home_statistics(HttpServletResponse response,String shop_id)
    {

        Map<String,Object> pd = new HashMap<>();
        pd.put("shop_id", shop_id);
        JSONObject jSONObject = new JSONObject();
        try {
            pd.put("time_type", 1);
            int today90Num =orderMainService.checkNum(pd);                    //发劵笔数
            double today90Money =orderMainService.checkMoneyNum(pd);          //发劵金额
            double todayMoneyTotal =orderMainService.collectMoneyNum(pd);     //收银总额
            int todayMoneyCount = orderMainService.collectNum(pd);            //收银笔数
            int balance_90 = orderMainService.getBalance_90(pd);              //90劵余额
            int balance_90_shop = orderMainService.getBalance_90_shop(pd);              //90劵余额
            int balance_90_experience = orderMainService.getBalance_90_experience(pd);              //90劵余额
            jSONObject.put("return_code", 1);
            jSONObject.put("return_info", "");
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("today90Num", today90Num);
            map.put("today90Money", today90Money);
            map.put("todayMoneyTotal", todayMoneyTotal);
            map.put("todayMoneyCount", todayMoneyCount);
            map.put("balance_90", balance_90);
            map.put("balance_90_shop", balance_90_shop);
            map.put("balance_90_experience", balance_90_experience);
            JSONObject subMsgs = JSONObject.fromObject(map);
            jSONObject.put("return_data", subMsgs);
            logger.error("统计首页接口返回jSONObject=" + jSONObject);
       } catch (Exception e) {
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", e.getMessage());
            jSONObject.put("return_data", null);
            logger.error("首页统计=报错=" ,e.fillInStackTrace());
            e.printStackTrace();
        }
writeJson(jSONObject,response);
    }
}
