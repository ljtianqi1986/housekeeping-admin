package com.biz.controller.api;


import com.alibaba.fastjson.JSON;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.*;
import com.biz.model.Pmodel.basic.Brand;
import com.biz.model.Singleton.ZkNode;
import com.biz.service.api.CustomQrcodeServiceI;
import com.biz.service.api.MyOrderServiceI;
import com.biz.service.api.PhoneUserServiceI;
import com.biz.service.basic.ShopServiceI;
import com.biz.service.basic.UserServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.ConfigUtil;
import com.framework.utils.QRCode;
import com.framework.utils.StringUtil;
import com.framework.utils.zook.ZookeeperClientUtil;
import com.google.zxing.BarcodeFormat;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * qt接口
 *
 * @author  lzq
 *
 */
@Controller
@RequestMapping("/api/QTUser")
public class PhoneUserController extends BaseController {


    @Resource(name = "phoneUserService")
    private PhoneUserServiceI phoneUserService;

    @Resource(name = "myOrderService")
    private MyOrderServiceI myOrderService;
    @Resource(name = "shopService")
    private ShopServiceI shopService;

    @Resource(name = "userService")
    private UserServiceI userService;
    @Resource(name = "customQrcodeService")
    private CustomQrcodeServiceI customQrcodeService;

    @Autowired
    private ZookeeperClientUtil zookeeperClient;
    /**
     * 90门店收银端登录
     */
    @RequestMapping(value = "/phoneDeskLogin")
    public void phoneDeskLogin(HttpServletResponse response,String login_name,
                                String pwd) {
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("login_name", login_name);
        hashMap.put("pwd_md5", pwd);

        //传入参数
        JSONObject jSONObject = phoneUserService.getUserForLoginhashMap(hashMap);
        writeJson(jSONObject, response);
    }


    /**
     * 90体验店门店当日收银汇总，细分到收银员
     * @param shop_code
     * @return
     */
    @RequestMapping(value="/phoneDailyOrderSummary")
    public void phoneDailyOrderSummary(HttpServletResponse response,String shop_code,String user_code)
    {
        Map<String,Object> JO = phoneUserService.getPhoneDailyOrderSummaryByShopCodeAndUserCode(shop_code,user_code);
        writeJson(JO, response);
    }



    /**
     * 体验店的收银记录 分收银员|店长查询
     * type
     * trade_type：
     * 0:全部 1：刷卡 2：扫码 3：公众号
     *
     */
    @RequestMapping(value = "/phone_queryRecordsByParam")
    public void phone_queryRecordsByParam(String code,String state,
                                                              String trade_type,
                                                           String page,String type,
                                                              String dateStart, String dateEnd,
                                                              String telephone,
                                                              String orderCode,HttpServletResponse response)
    {
        Map<String,Object> jSONObject = new HashMap<>();
        jSONObject.put("return_code", 0);
        jSONObject.put("return_data", null);
        if(StringUtils.isBlank(page)){
            jSONObject.put("return_info", "接口参数错误：page为空");
            writeJson(jSONObject,response);
        }else if(StringUtils.isBlank(type)){
            jSONObject.put("return_info", "接口参数错误：type为空");
            writeJson(jSONObject,response);
        }else{
            try {
                int page_int = Integer.valueOf(page);
                int pageSize = 10;
                Map<String,Object> mapParam = new HashMap<>();
                mapParam.put("pageSize", pageSize);
                mapParam.put("pageNum", pageSize * (page_int - 1));
                mapParam.put("state", state);

                if("2".equals(state))
                {
                    mapParam.put("mainState", "3");
                }else if("3".equals(state))
                {
                    mapParam.put("mainState", "2");
                }else{
                    mapParam.put("mainState", "1");
                }
                mapParam.put("trade_type", trade_type);
                mapParam.put("orderCode", orderCode);
                if(!StringUtil.isNullOrEmpty(telephone))
                {
                    //通过号码查询用户id
                    BaseUser userinfo=phoneUserService.getBaseUserByPhone(telephone);
                    if(!StringUtil.isNullOrEmpty(userinfo.getOpen_id())){
                        mapParam.put("open_id", userinfo.getOpen_id());
                    }

                }
                if("1".equals(type)) {
                    mapParam.put("shop_id", code);
                }else{
                    mapParam.put("user_code", code);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if(StringUtils.isNotBlank(dateStart))
                {
                    //拼接时间戳
                    String begin = dateStart.substring(0, 10);
                    String str2 = begin+" 00:00:00";
                    Date today2 = sdf.parse(str2);
                    mapParam.put("beginStamp", String.valueOf(today2.getTime()/1000));
                }
                if(StringUtils.isNotBlank(dateEnd))
                {
                    String end = dateEnd.substring(0, 10);
                    String str1 = end+" 23:59:59";
                    Date today1 = sdf.parse(str1);
                    mapParam.put("endStamp", String.valueOf(today1.getTime()/1000));
                }

                List<Map<String, Object>> mapsRetur = phoneUserService.queryRecordsByParam(mapParam);
                int total = phoneUserService.queryRecordsCountByParam(mapParam);
                jSONObject.put("return_code", 1);
                jSONObject.put("return_info", "");
                jSONObject.put("return_data", mapsRetur);
                jSONObject.put("total", total);
            } catch (Exception e) {
                jSONObject.put("return_info", e.getMessage());
                e.printStackTrace();
            }

            writeJsonNoReplace(jSONObject,response);
        }

    }


    /**
     * 体验店的收银记录 分收银员|店长查询
     * type
     * trade_type：
     * 0:全部 1：刷卡 2：扫码 3：公众号
     *
     */
    @RequestMapping(value = "/phone_queryMoneyByParam")
    public void phone_queryMoneyByParam(String code,String state,
                                          String trade_type,
                                          String page,String type,
                                          String dateStart, String dateEnd,
                                          String telephone,
                                          String orderCode,HttpServletResponse response)
    {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("return_code", 0);
        jSONObject.put("return_data", null);
        if(StringUtils.isBlank(page)){
            jSONObject.put("return_info", "接口参数错误：page为空");
            writeJson(jSONObject,response);
        }else if(StringUtils.isBlank(type)){
            jSONObject.put("return_info", "接口参数错误：type为空");
            writeJson(jSONObject,response);
        }else{
            try {
                int page_int = Integer.valueOf(page);
                int pageSize = 10;
                Map<String,Object> mapParam = new HashMap<>();
                mapParam.put("pageSize", pageSize);
                mapParam.put("pageNum", pageSize * (page_int - 1));
                mapParam.put("state", state);
                mapParam.put("trade_type", trade_type);
                mapParam.put("orderCode", orderCode);
                mapParam.put("telephone", telephone);
                if("1".equals(type)) {
                    mapParam.put("shop_id", code);
                }else{
                    mapParam.put("user_code", code);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if(StringUtils.isNotBlank(dateStart))
                {
                    //拼接时间戳
                    String begin = dateStart.substring(0, 10);
                    String str2 = begin+" 00:00:00";
                    Date today2 = sdf.parse(str2);
                    mapParam.put("beginStamp", String.valueOf(today2.getTime()/1000));
                }
                if(StringUtils.isNotBlank(dateEnd))
                {
                    String end = dateEnd.substring(0, 10);
                    String str1 = end+" 23:59:59";
                    Date today1 = sdf.parse(str1);
                    mapParam.put("endStamp", String.valueOf(today1.getTime()/1000));
                }

                List<Map<String, Object>> mapsRetur = phoneUserService.queryMoneyByParam(mapParam);
                int total = phoneUserService.queryMoneyCountByParam(mapParam);
                jSONObject.put("return_code", 1);
                jSONObject.put("return_info", "");
                jSONObject.put("return_data", mapsRetur);
                jSONObject.put("total", total);
            } catch (Exception e) {
                jSONObject.put("return_info", e.getMessage());
                e.printStackTrace();
            }

            writeJson(jSONObject,response);
        }

    }

    /**
     * 17 重置登录密码
     * @param user_code 用户id
     * @param pwd 旧密码
     * @param new_pwd 新密码
     * @return
     */
    @RequestMapping(value="/phoneResetPwd")
    public void phoneResetPwd(HttpServletResponse response,String user_code, String pwd, String new_pwd){
        writeJson(phoneUserService.resetPwd(user_code,pwd,new_pwd),response );
    }

    /**
     * 19 加载导购员
     * @param sid
     * @return
     */
    @RequestMapping(value="/getSalesList")
    public void getSalesList(String sid, HttpServletResponse response){
        Result result =  phoneUserService.getSalesList(sid);

        writeJson(result, response);
    }

    /**
     * 9  90网 用户查询认证 短信查询
     * @param shop_code
     * @param phone
     * @param total
     * @return
     */
    @RequestMapping(value="/phoneDoSendSMS")
    public void phoneDoSendSMS(HttpServletResponse response,String shop_code,String phone,String total,String iscoin) throws Exception {
        Map<String,Object> jSONObject =myOrderService.Verification_Payment_Phone(shop_code, phone, total, Boolean.parseBoolean(iscoin));
        writeJson(jSONObject, response);
    }



    /**
     * 体验店人工发券
     */
    @RequestMapping(value = "/phone_artificialCouponsForStore")
    public @ResponseBody JSONObject phone_artificialCouponsForStore(String shop_code,String user_code,String total,String card_code,String memo){
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("return_code", 0);
        try {
            if(StringUtils.isBlank(total)){
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", "金额不正确！");
                return jSONObject;
            }
            Shop shop = phoneUserService.getShopBySid(shop_code);
            if(shop == null ){
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", "门店不存在！");
                return jSONObject;
            }
            String brand_code = shop.getBrand_code();
            Brand brand = phoneUserService.getBrandOnlyByCode(brand_code);
            if(brand == null ){
                jSONObject.put("return_info", "商户不存在！");
                logger.error("商户不存在=brand_code="+brand_code);
                return jSONObject;
            }
            /***********对传入金额处理*************/
            double balance_90 = 0d;//分
            try {
                balance_90 = money_convert(total);
            } catch (Exception e) {
                jSONObject.put("return_info", "金额错误，金额为"+total);
                return jSONObject;
            }
            long brand_balance_90 = brand.getBalance_90();//九零券余额（分）
            long credit_total_90 = brand.getCredit_total_90();//久零券透支额度（分）
            long credit_now_90 = brand.getCredit_now_90();//久零券当前透支额度（分）
            if((brand_balance_90 + credit_total_90 - credit_now_90) <  balance_90){
                int edu = (int) (credit_now_90/100);
                jSONObject.put("return_info", "商户额度不够，当前可用透支额度"+edu);
                return jSONObject;
            }
            //人工发券记录
            RgGift rgGift = new RgGift();
            String code = String.valueOf(getRndCode());
            rgGift.setCode(code);
            rgGift.setBrand_code(brand_code);
            rgGift.setShop_code(shop_code);
            rgGift.setUser_code(user_code);
            rgGift.setPoint_90(balance_90);
            rgGift.setState(0);
            rgGift.setCard_code(card_code);
            rgGift.setMemo(memo);
            phoneUserService.insertRgGift(rgGift);
            //场景值
            PayScene payScene = new PayScene();
            payScene.setMain_code(code);
            payScene.setScene_type(1);
            phoneUserService.insertPayScene(payScene);
            int scene_id = payScene.getId();
            //关注二维码
//            String url = wxQrCodeService.getTempQrCode(scene_id);
            jSONObject.put("return_code", 1);
//            jSONObject.put("return_info", url);
        }catch (Exception e){
            String msg = e.getMessage();
            if(StringUtils.isNotBlank(msg) && msg.length()>30){
                msg = msg.substring(0, 29);
            }
            jSONObject.put("return_info", "错误-" +msg);
        }
        return jSONObject;
    }



    /**
     * 查询自定义支付二维码
     *
     * @return
     */
    @RequestMapping(value="/phone_queryCustomQrList")
    public void phone_queryCustomQrList(HttpServletResponse response,String shop_id) throws Exception {
        JSONObject jSONObject = new JSONObject();
        try {

            Shop shop = shopService.getShopBySid(shop_id);
            if(shop == null)
            {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", "门店不存在！");
                jSONObject.put("return_data", null);
            }else{
                List<CustomQrcode> customQrcodeList = customQrcodeService.queryCustomQrcodeListByShop(shop_id);
                JSONArray jsonStrs = new JSONArray();
                String shop_name = shop.getBusiness_name();
                if(customQrcodeList.size() > 0)
                {
                    for (CustomQrcode customQrcode : customQrcodeList) {
                        JSONObject jsonObject = new JSONObject();
                        User user = userService.getUserByCode(customQrcode.getUser_code());
                        jsonObject.put("code", customQrcode.getCode());//自定义二维码主键
                        jsonObject.put("shop_name", shop_name);
                        jsonObject.put("user_name", user.getPerson_name());
                        jsonObject.put("money", customQrcode.getMoney());
                        jsonObject.put("remark", customQrcode.getRemark());
                        jsonObject.put("img_url", ConfigUtil.get("OSSURL")+"img/"+customQrcode.getImg_url());
                        jsonStrs.add(jsonObject);
                    }
                }
                jSONObject.put("return_code", 1);
                jSONObject.put("return_info", "");
                jSONObject.put("return_data", jsonStrs);
                logger.error("查询门店自定义二维码接口=返回数据jSONObject=" + jSONObject);
            }

        } catch (Exception e) {
            String msg = e.getMessage();
            if(msg.length()>30){
                msg = msg.substring(0, 29);
            }
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", msg);
            jSONObject.put("return_data", null);
            logger.error("查询门店自定义二维码接口=报错=" ,e.fillInStackTrace());
            e.printStackTrace();
        }
        writeJson(jSONObject,response);
    }


    /**
     * 删除自定义二维码接口
     * @param code
     * @return
     */
    @RequestMapping(value = "/phone_delCustomQr")
    public void  delCustomQr(HttpServletResponse response,String code)
    {
        Result result = new Result();
        try
        {
            logger.error("创建自定义二维码接口====code="+code);
            customQrcodeService.delCustomQrcodeByCode(code);
            result.setReturn_code(1);
            result.setReturn_info("删除成功");
        }
        catch(Exception e)
        {
            String msg = e.getMessage();
            if(msg.length()>30){
                msg = msg.substring(0, 29);
            }
            result.setReturn_code(0);
            result.setReturn_info("错误-" + msg);
        }
        writeJson(result,response);
    }



    /**
     * 创建自定义二维码接口
     */
    @RequestMapping(value="/phone_createCustomQr")
    public void phone_createCustomQr(HttpServletResponse response,String shop_id, String user_code, String money, String remark)
    {
        logger.error("创建自定义二维码接口====shop_id="+shop_id+"=user_code="+user_code+"=money="+money+"=remark="+remark);
        String content = ConfigUtil.get("SYS_URL")+"client_prePay.do?id="+shop_id+","+user_code;
        Result result = new Result();
//		String content = "http://192.168.18.100:8080/money/client_prePay.do?id="+shop_id+","+user_code;
        try{
            //图片名称
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
            String date = df.format(new Date());// new Date()为获取当前系统时间
            String rnd_name = getRndCode();
            String file_name = date+"/"+rnd_name+".png";
            String imgurl =ConfigUtil.get("UPLOAD_IMG_QRCODE") +  "/" + file_name;
            File file = new File(imgurl);
            if (!file.exists())
            {
                if (!file.getParentFile().exists())
                {
                    file.getParentFile().mkdirs();
                }
            }
            CustomQrcode customQrcode = new CustomQrcode();
            if(StringUtils.isNotBlank(money)){ //固定金额
                customQrcode.setMoney(money);
            }
            if(StringUtils.isNotBlank(remark)){ //备注
                customQrcode.setRemark(remark);
            }
            customQrcode.setShop_code(shop_id);
            customQrcode.setUser_code(user_code);
            String code=getRndCode();
            customQrcode.setCode(code);
            content = content+ "," + code;
            customQrcode.setImg_url(file_name);
            customQrcodeService.insertCustomQrcode(customQrcode);

            Shop shop = shopService.getShopBySid(shop_id);
            String imgPath = ConfigUtil.get("OSSURL") +"ninezero/img_small/"+ shop.getLogo_url();

            BufferedImage image = QRCode.getencodeimg(content,imgPath, BarcodeFormat.QR_CODE, 4000, 2400, null);


            QRCode.graphicsGeneration(image,shop.getBusiness_name(),money,remark,imgurl,file_name);

            result.setReturn_code(1);
            result.setReturn_info(ConfigUtil.get("OSSURL")+file_name);

        }catch (Exception e){
            String msg = e.getMessage();
            if(msg.length()>30){
                msg = msg.substring(0, 29);
            }
            result.setReturn_code(0);
            result.setReturn_info("错误-" + msg);

        }
        writeJson(result,response);
    }


    /**
     * 根据主单号查询订单信息
     * param code （主单单号）
     * writeJson（list）
     * date 4.13
     * @author lzq
     */
    @RequestMapping(value="/qtGetOrderDetailByOrderCode")
    public void qtGetOrderDetailByOrderCode(HttpServletResponse response, String code)
    {
        Map<String, Object> map = new HashMap<>();

        map.put("return_code",0);
        map.put("return_info","订单查询失败！");
        if(StringUtil.isNullOrEmpty(code))
        {
            map.put("return_info","订单号不能为空！");
        }else{
            map = phoneUserService.qtGetOrderDetailByOrderCode(code);
        }

        writeJson(map,response);
    }

    /**
     * 根据子单id进行部分退款
     * param detailIds（子单id逗号拼接）
     * writeJson（list）
     * date 4.13
     * @author lzq
     */
    @RequestMapping(value="/qtBackOrderDetailByDetailIds")
    public void qtBackOrderDetailByDetailIds(HttpServletResponse response, String detailIds)
    {
        Map<String, Object> map = new HashMap<>();

        map.put("return_code",0);
        map.put("return_info","订单退款失败！");
        if(StringUtil.isNullOrEmpty(detailIds))
        {
            map.put("return_info","参数不能为空！");
        }else{
            map = phoneUserService.qtBackOrderDetailByDetailIds(detailIds);

            //处理主单
            if(map != null && "1".equals(map.get("return_code").toString()))
            {
                phoneUserService.updateMainState(detailIds);
            }
        }

        writeJson(map,response);
    }


    /**
     * 通过zk获取h5页面问答
     */
    @RequestMapping(value="/getH5Question")
    public  void getH5Question(HttpServletResponse response) {
        List<Map> list=new ArrayList<>();
        try{
            String question="";
            question= ZkNode.getIstance().getJsonConfig().get("wt")+"";
           list= JSON.parseArray(question,Map.class);
        }catch (Exception e)
        {
        }

        writeJson(list,response);
    }

    /***
     * 当日统计
     * 当天交易笔数，兑换券额，发放券额，退款笔数，
     * 退款券额，退款金额，服务费金额（支付宝、微信、现金、久零贝)
     * param userCode
     * date 2017.4.19
     * @author lzq
     */
    @RequestMapping(value="/qtGetSumInfoByUserCode")
    public void qtGetSumInfoByUserCode(HttpServletResponse response, String userCode, String shopId)
    {
        Map<String, Object> map = new HashMap<>();

        map.put("return_code",0);
        map.put("return_info","统计信息查询失败！");
        if(StringUtil.isNullOrEmpty(userCode) || StringUtil.isNullOrEmpty(shopId))
        {
            map.put("return_info","参数不能为空！");
        }else{
            map = phoneUserService.qtGetSumInfoByUserCode(userCode, shopId);
        }
        writeJson(map,response);
    }
}