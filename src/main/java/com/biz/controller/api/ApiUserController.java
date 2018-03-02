package com.biz.controller.api;

import com.alibaba.fastjson.JSON;
import com.biz.model.Pmodel.PBaseUser;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.BaseUser;
import com.biz.model.Pmodel.basic.Base90Detail;
import com.biz.service.api.ApiUserServiceI;
import com.biz.service.api.BaseUserServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.ConfigUtil;
import com.framework.utils.StringUtil;
import com.framework.utils.URLConectionUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang3.Validate.notNull;

/**
 * Created by qziwm on 2017/6/14.
 */
@Controller
@RequestMapping("/api/user")
public class ApiUserController extends BaseController {
    @Resource(name = "baseUserService")
    private BaseUserServiceI baseUserService;
    @Resource(name = "apiUserService")
    private ApiUserServiceI apiUserService;
//    @Reference
//    private SmsUnifiedServiceI smsUnifiedService;

    /**
     * 解码小程序
     */
    @RequestMapping(value="/getUserUnionId")
    public void getUserUnionId(HttpServletResponse response,String encryptedData,String iv,String appid,String secret,String grant_type,String js_code) throws Exception {
        Map<String,String> parm=new HashMap<>();
        Map<String,Object> userParm=new HashMap<>();
        parm.put("appid",appid);
        parm.put("secret",secret);
        parm.put("grant_type",grant_type);
        parm.put("js_code",js_code);
        String res=URLConectionUtil.httpURLConnectionPostDiy("https://api.weixin.qq.com/sns/jscode2session",parm);
        try{
            parm.clear();
            parm=JSON.parseObject(res,Map.class);
            if(parm!=null)
            {
                String sessionKey=parm.get("session_key");
                userParm=  decryptUserInfo(encryptedData,iv,sessionKey);
            }
        }catch (Exception e)
        {}
writeJson(userParm,response);
    }
/*    *//**
     * 解码小程序
     *//*
    @RequestMapping(value="/decryptUserInfo")*/
    public Map<String,Object> decryptUserInfo(String encryptedData,String vi,String sessionKey) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        byte[] dataStr = Base64.decode(encryptedData);
        byte[] keyBytes = Base64.decode(sessionKey);
        int base = 16;
        if (keyBytes.length % base != 0) {
            int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
            keyBytes = temp;
        }

        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
        SecretKeySpec spec = new SecretKeySpec(keyBytes, "AES");
        AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
        parameters.init(new IvParameterSpec(Base64.decode(vi)));
        cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
        byte[] resultByte = cipher.doFinal(dataStr);
        String json = new String(resultByte, "UTF-8");
        Map<String,Object> map= JSON.parseObject(json,Map.class);
        //map.put("unionId","oKBUFwk9tRQRnnV_gHfE8m21D4JE");//没有绑定的开发者账号的测试数据
       // writeJson(map,response);
        return map;
    }
    /**
     * 获取用户个人中心首页数据
     */
    @RequestMapping(value="/getUserCenterInfo")
    public void getUserCenterInfo(HttpServletResponse response,String unionId) throws Exception {

        Map<String,Object> userInfo=new HashMap<>();
        try{
            userInfo=  baseUserService.getUserCenterInfo(unionId);
        }catch (Exception e)
        {
            e.getMessage();
        }

        writeJson(userInfo,response);
    }
    /**
     * 获取用户久零贝明细
     */
    @RequestMapping(value="/getUserCoin")
    public void getUserCoin(HttpServletResponse response,String unionId) throws Exception {

        Map<String,Object> coin=new HashMap<>();
        try{
            coin=  baseUserService.getUserCoin(unionId);
        }catch (Exception e)
        {
            e.getMessage();
        }

        writeJson(coin,response);
    }
    /**
     * 获取用户订单数据
     */
    @RequestMapping(value="/getUserOrderList")
    public void getUserOrderList(HttpServletResponse response,String unionId) throws Exception {

        Map<String,Object> order=new HashMap<>();
        try{
            order=  baseUserService.getUserOrderList(unionId);
        }catch (Exception e)
        {
            e.getMessage();
        }

        writeJson(order,response);
    }
    /**
     * 获取用户手机/生日/默认地址
     */
    @RequestMapping(value="/getUserPersonInfo")
    public void getUserPersonInfo(HttpServletResponse response,String unionId) throws Exception {

Map<String,Object> userInfo=new HashMap<>();
        try{
            userInfo=  baseUserService.getUserPersonInfo(unionId);
        }catch (Exception e)
        {
            e.getMessage();
        }

        writeJson(userInfo,response);
    }
    /**
     * 初始化用户unionid
     */
    @RequestMapping(value="/initUserUnionId")
    public void initUserUnionId(HttpServletResponse response) throws Exception {
        Map<String,String> map=new HashMap<>();
        try{
            baseUserService.updateInitUserUnionId();
            map.put("code","0");
        }catch (Exception e)
        {
            map.put("code","1");
        }
        writeJson(map,response);
    }

    /**
     * 久零劵明细--消费
     */
    @RequestMapping(value = "/orther_balance_out")
    public void orther_balance_out(String unionId,String page,HttpServletResponse response)
    {
        JSONObject jSONObject = new JSONObject();
        Map<String,Object> pd = new HashMap<>();
        logger.error("久零劵明细--消费接口=unionId=" + unionId);
        if(StringUtils.isBlank(unionId)){
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", "接口参数错误：unionId为空");
            jSONObject.put("return_data", null);
        }
        else {
            pd.put("unionId", unionId);
            int page_int = Integer.valueOf(page);
            int pageSize = 10;
            pd.put("pageSize", pageSize);
            pd.put("pageNum", pageSize * (page_int - 1));

            try {
                //消费金额
                //联盟商户退款金额
                pd.put("state","RefLM");
                pd.put("source", 3);
                List<Base90Detail> outList = apiUserService.selectOrtherBalanceOut(pd);

                Collections.sort(outList,new ApiUserController.ComparatorBase90Deatil());
                for (Base90Detail item : outList) {
                    String create_time = item.getCreate_time();
                    String c_time = create_time.substring(0, create_time.lastIndexOf(" "));
                    item.setCreate_time(c_time);
                }

                //消费总额
                String out_sum = apiUserService.out_sum(pd);
                JSONObject jsonObject_sum = new JSONObject();
                jsonObject_sum.put("out_sum", out_sum);
                JSONArray jsonStrs = new JSONArray();
                for (Base90Detail Detail : outList) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("create_time", notNull(Detail.getCreate_time()));
                    jsonObject.put("source", Detail.getSource());
                    jsonObject.put("business_name", Detail.getBusiness_name());
                    jsonObject.put("source_msg", notNull(Detail.getSource_msg()));
                    jsonObject.put("point_90", Detail.getPoint90());
                    jsonStrs.add(jsonObject);
                }
                jsonStrs.add(jsonObject_sum);
                jSONObject.put("return_code", 1);
                jSONObject.put("return_info", "");
                jSONObject.put("return_data", jsonStrs);
            } catch (Exception e) {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", e.getMessage());
                jSONObject.put("return_data", null);
                logger.error("久零劵明细--消费=报错=" ,e.fillInStackTrace());
                e.printStackTrace();
            }
        }

        writeJson(jSONObject,response);
    }

class ComparatorBase90Deatil implements Comparator<Base90Detail> {

    @Override
    public int compare(Base90Detail o1, Base90Detail o2) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date dateO1= null;
        Date dateO2=null;
        try {
            dateO1 = sdf.parse(o1.getCreate_time());
            dateO2 = sdf.parse(o2.getCreate_time());

            if(dateO1.after(dateO2)){
                return -1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }
}


    /**
     * 获取付款码
     */
    @RequestMapping(value = "/orther_getUserOnlyCode")
    public void orther_getUserOnlyCode(String unionId,HttpServletResponse response)
    {
        JSONObject jSONObject = new JSONObject();
        if(StringUtils.isBlank(unionId)){
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", "接口参数错误：unionId");
            jSONObject.put("return_data", null);
        }
        else
        {try {
            BaseUser user = apiUserService.getBaseUserByunionId(unionId);
            if(user==null){
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", "接口参数错误：无效的unionId");
                jSONObject.put("return_data", null);
            }
            else{
                String only_code = this.createPayCode("100800");
                if (only_code.length()!=20)
                {
                    jSONObject.put("return_code", 0);
                    jSONObject.put("return_info", "接口参数错误：付款码生成失败，请重新获取");
                    jSONObject.put("return_data", null);
                }else{
                    Map<String,Object> pd1 = new HashMap<>();
                    pd1.put("only_code", only_code);
                    pd1.put("unionId", unionId);
                    //更新 baseUser 付款码
                    apiUserService.updateBaseUserOnlyCode(pd1);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("only_code", only_code);  //付款码

                    jSONObject.put("return_code", 1);
                    jSONObject.put("return_info", "");
                    jSONObject.put("return_data", jsonObject);
                }
            }

            logger.error("获取付款吗接口返回jSONObject=" + jSONObject);

        } catch (Exception e) {
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", e.getMessage());
            jSONObject.put("return_data", null);
            logger.error("获取付款吗接口=报错=" ,e.fillInStackTrace());
            e.printStackTrace();
        }}

        writeJson(jSONObject,response);
    }

    /**
     * 生成付款码
     * agent_code
     * @return
     */
    private String createPayCode(String agent_code)
    {
        StringBuilder sb = new StringBuilder("90");
        sb.append(agent_code.substring(0,4));
        sb.append(this.getRandomCode(1));
        return sb.toString();
    }

    /**
     * 生成 随机数
     * @param RandomMax
     * @return
     */
    private String getRandomCode(int RandomMax)
    {
        Random rand = new Random();
        Date now = new Date();
        return (new StringBuilder(String.valueOf(Long.toString(now.getTime()))))
                .append(Integer.toString(rand.nextInt(RandomMax))).toString();
    }
    /**
     * 久零劵明细--领取
     * @throws Exception
     */
    @RequestMapping(value = "/orther_balance_in")
    public void orther_balance_in(String unionId,String page,HttpServletResponse response)
    {
        JSONObject jSONObject = new JSONObject();
        Map<String,Object> pd = new HashMap<>();
        logger.error("久零劵明细--领取接口=open_id=" + unionId);
        if(StringUtils.isBlank(unionId)){
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", "接口参数错误：open_id为空");
            jSONObject.put("return_data", null);

        }
        else
        { pd.put("unionId", unionId);
            pd.put("state", "in");
            int page_int = Integer.valueOf(page);
            int pageSize = 10;
            pd.put("pageSize", pageSize);
            pd.put("pageNum", pageSize * (page_int - 1));

            try {

                //领取金额
                List<Base90Detail> intList = apiUserService.selectOrtherBalanceIn(pd);
                Collections.sort(intList,new ApiUserController.ComparatorBase90Deatil());
                for (Base90Detail item : intList) {
                    String create_time = item.getCreate_time();
                    String c_time = create_time.substring(0, create_time.lastIndexOf(" "));
                    item.setCreate_time(c_time);
                }
                //领取总额
                String int_sum = apiUserService.int_sum(pd);
                JSONObject jsonObject_sum = new JSONObject();
                jsonObject_sum.put("int_sum", int_sum);
                JSONArray jsonStrs = new JSONArray();
                for (Base90Detail Detail : intList) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("create_time", notNull(Detail.getCreate_time()));
                    jsonObject.put("source", Detail.getSource());
                    jsonObject.put("business_name", Detail.getBusiness_name());
                    jsonObject.put("source_msg", notNull(Detail.getSource_msg()));
                    jsonObject.put("point_90", Detail.getPoint90());
                    jsonStrs.add(jsonObject);
                }
                jsonStrs.add(jsonObject_sum);
                jSONObject.put("return_code", 1);
                jSONObject.put("return_info", "");
                jSONObject.put("return_data", jsonStrs);
            } catch (Exception e) {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", e.getMessage());
                jSONObject.put("return_data", null);
                logger.error("久零劵明细--领取=报错=" ,e.fillInStackTrace());
                e.printStackTrace();
            }
        }
        writeJson(jSONObject,response);
    }


    int pageSize=10;//翻页10行
    /**
     * 充值记录
     * @param unionId
     * @param page 1开始计数翻页
     * @return
     */
    @RequestMapping(value = "/orther_offline_card")
    public void orther_offline_card(String unionId,int page,HttpServletResponse response) {
        JSONObject jSONObject = new JSONObject();
        try {
            Map<String,Object> pd = new HashMap<>();
            pd.put("unionId", unionId);
            pd.put("source", "4");
            /***开始：翻页***/
            int pageNum=(page-1)*pageSize;
            pd.put("pageSize", pageSize);
            pd.put("pageNum", pageNum);
            /***结束：翻页***/
            //充值记录
            List<Base90Detail> list = apiUserService.selectWhereBase90Detail(pd);
          /*  for (Base90Detail item : list) {
                String create_time = item.getCreate_time();
                String c_time = create_time.substring(0, create_time.lastIndexOf(" "));
                item.setCreate_time(c_time);
            }*/
            jSONObject.put("list", list);
        } catch (Exception e) {
            jSONObject.put("list", new ArrayList());
            e.printStackTrace();
        }
        writeJson(jSONObject,response);

    }

    /**
     * 充值
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/do90Recharge")
    public  void do90Recharge(String card_code,String unionId,HttpServletResponse response)throws Exception{
        Map<String, String> map = new HashMap<String, String>();
        PBaseUser user=apiUserService.getUserByUnionId(unionId);
        String openId=
        map.put("open_id",user.getOpen_id());
        map.put("card_code",card_code);
        String url= ConfigUtil.get("QT_Url")+"/api/QTclerk/orther_extract_card.ac";
        String res= URLConectionUtil.httpURLConnectionPostDiy(url,map);
        Map<String,Object> resMap=new HashMap<String,Object>();

        try {
            resMap= JSON.parseObject(res,Map.class);
            if(resMap!=null && resMap.get("errmsg")!=null)
            {
                if("ok".equals(resMap.get("errmsg").toString()))
                {
                    //开始计算久零贝比例并且赠送
                  apiUserService.giveCoinByCardId(user,card_code);
                }
            }
        }catch (Exception e)
        {
        }
            writeJson(resMap,response);
    }

    /**
     * 生成短信验证码并且发送短信
     */
//    @RequestMapping(value="/sendSMSmessage")
//    public void sendSMSmessage(HttpServletResponse response,String phone) throws Exception {
//
//        Map<String,Object> message=new HashMap<>();
//        try{
//            //生成六位随机码
//            String code= Tools.getRandomNum() + "";
//            message.put("code",code);
//            String smsCode=ConfigUtil.get("smsCode");
//            String smsAuthorCode=ConfigUtil.get("smsAuthorCode");
//           Map<String, Object> res=  smsUnifiedService.sendSms(phone,code,smsCode,smsAuthorCode);
//           message.put("flag",res.get("res"));
//            message.put("flag","T");
//        }catch (Exception e)
//        {
//            e.getMessage();
//            message.put("flag","F");
//        }
//
//        writeJson(message,response);
//    }
    /**
     * 修改用户的绑定手机号
     */
    @RequestMapping(value="/updateUserPhone")
    public void updateUserPhone(HttpServletResponse response,String phone,String unionId) throws Exception {

        Map<String,Object> message=new HashMap<>();
        if(!StringUtil.isNullOrEmpty(unionId)) {
            try {
                BaseUser puser = apiUserService.getBaseUserByunionId(unionId);
                if (puser != null) {
                    User user = new User();
                    user.setPhone(phone);
                    user.setId(puser.getId());
                    apiUserService.updateBsseUserPhone(user);
                    message.put("flag", "0");
                } else {
                    message.put("flag", "1");
                }


            } catch (Exception e) {
                e.getMessage();
                message.put("flag", "1");
            }
        }else
        {   message.put("flag", "1");}

        writeJson(message,response);
    }



    /**
     * 获取用户地址列表
     */
    @RequestMapping(value="/getUserAddressList")
    public void getUserAddressList(HttpServletResponse response,String unionId) throws Exception {

       List<Map<String,Object>>  addr=new ArrayList<>();
        if(!StringUtil.isNullOrEmpty(unionId))
        {
            try{
                addr=  baseUserService.getUserAddressList(unionId);
            }catch (Exception e)
            {
                e.getMessage();
            }
        }


        writeJson(addr,response);
    }



    /**
     * 获取用户地址列表
     */
    @RequestMapping(value="/getAddressById")
    public void getAddressById(HttpServletResponse response,String id) throws Exception {

        Map<String,Object>  addr=new HashMap<>();
        try{
            addr=  baseUserService.getAddressById(id);
        }catch (Exception e)
        {
            e.getMessage();
        }

        writeJson(addr,response);
    }


    /**
     * 获取所有地址列表
     */
    @RequestMapping(value="/getAddressList")
    public void getAddressList(HttpServletResponse response) throws Exception {

        List<Map<String,Object>>  addr=new ArrayList<>();
        try{
            addr=  baseUserService.getAddressList();
        }catch (Exception e)
        {
            e.getMessage();
        }

        writeJson(addr,response);
    }

    /**
     * 更新用户生日
     */
    @RequestMapping(value="/updateUserBirthDay")
    public void updateUserBirthDay(HttpServletResponse response,String unionId,String birthDay) throws Exception {
        Map<String,Object>  resMap=new HashMap<>();
        if(StringUtil.isNullOrEmpty(unionId))
        {   resMap.put("flag","1");
            resMap.put("msg","修改生日失败！");
        }else
        {
            try{
                resMap=  baseUserService.updateUserBirthDayByUnionId(unionId,birthDay);
            }catch (Exception e)
            {
                e.getMessage();
                resMap.put("flag","1");
                resMap.put("msg","修改生日失败！");
            }
        }
        writeJson(resMap,response);
    }

    /**
     * 新增/修改用户的收货地址
     */
    @RequestMapping(value="/saveOrUpdateAddressByUnionId")
    public void saveOrUpdateAddressByUnionId(HttpServletResponse response,String unionid,String id,String province,String city,String county,String name,String phone,String addr,boolean isdefault) throws Exception {

        Map<String,Object>  resMap=new HashMap<>();
        Map<String,Object>  address=new HashMap<>();
        address.put("unionid",unionid);
        address.put("id",id);
        address.put("province",province);
        address.put("city",city);
        address.put("county",county);
        address.put("name",name);
        address.put("phone",phone);
        address.put("addr",addr);
        if(isdefault){address.put("isdefault","1");}
        else
        {address.put("isdefault","0");}

        try{
            if(StringUtil.isNullOrEmpty(id))
            { resMap=  baseUserService.saveAddressByUnionId(address);}
            else
            { resMap=  baseUserService.updateAddressByUnionId(address);}

        }catch (Exception e)
        {
            resMap.put("flag","1");
            e.getMessage();
        }

        writeJson(resMap,response);
    }
}
