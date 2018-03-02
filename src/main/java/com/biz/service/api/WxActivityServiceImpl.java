package com.biz.service.api;

import com.alibaba.fastjson.JSON;
import com.biz.model.Hmodel.TBizPerson;
import com.biz.model.Hmodel.api.TpaySceneDetail;
import com.biz.model.Pmodel.api.BaseUser;
import com.biz.model.Pmodel.api.OrderMainUnion;
import com.biz.model.Pmodel.api.PayScene;
import com.biz.model.Pmodel.api.RgGift;
import com.biz.model.Pmodel.api.activity.Pactivity;
import com.biz.model.Pmodel.api.activity.PfirstBalance;
import com.biz.model.Pmodel.weixin.UserInfo;
import com.biz.service.base.BaseServiceImpl;
import com.biz.service.basic.BizpersonServiceI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.*;
import com.framework.utils.weixin.WechatAccessToken;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("wxActivityService")
public class WxActivityServiceImpl extends BaseServiceImpl<Object> implements
        WxActivityServiceI {

    @SuppressWarnings("rawtypes")
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Resource(name = "wxTemplateService")
    private WxTemplateServiceI wxTemplateService;

    @Resource(name = "wxUtilService")
    private WxUtilServiceI wxUtilService;

    @Resource(name = "apiInterfaceService")
    private ApiInterfaceServiceI apiInterfaceService;

    @Resource(name = "wxtsService")
    private WxtsServiceI wxtsService;

    @Autowired
    private PhoneUserServiceI phoneUserServiceI;

    @Autowired
    private BizpersonServiceI bizpersonService;


    /****************************开始: 所有活动方法******************************************/

    /**
     * 满足当前的活动
     *
     * @param type 1:只查询"关注送活动"
     * @return
     * @throws Exception
     */
    private List<Pactivity> ActivityListDate(int type) throws Exception {
        List<Pactivity> activity = new ArrayList<Pactivity>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Map<String, Object> selectmap = new HashMap<>();
        selectmap.put("type", type);
        selectmap.put("nowTime", df.format(new Date()));
        if (type == 1) {
            //关注送活动
            activity = (List<Pactivity>) dao.findForList("WxActivityDao.ActivityList", selectmap);
        }

        return activity;


    }

    /**
     * ***********************首次关注加券点********************************
     *
     * @param openid
     * @return
     */
    @Override
    public String adActivityListDate(String openid,String sceneType,String appid) throws Exception {
        boolean pd = false;
        String msg="false";
        //参数验证
        if (!StringUtils.isNotBlank(openid)) {
            //openid空
            return "openid空";
        }
        if (!StringUtils.isNotBlank(sceneType)) {
            //二维码参数空
            return "活动参数空";
        }
        if("1".equals(sceneType)){//首次送
        if (isnull(openid)) {
            boolean user_bool= this.getselectUsermsgOpen_id(openid);
            if(user_bool){
                //读取用户信息，或新增用户
                BaseUser y_user= adduser(openid,appid);

                String mapidList="";//获取所有mapid，一次性查出所有规则已改加的积分数
                List<String> tmplist=new ArrayList<String>();
                //查询满足的活动
                List<Pactivity> Pactivity= ActivityListDate(1);
                for(Pactivity ac_tmp:Pactivity){
                    tmplist.add(ac_tmp.getMapid());
                }

                if(tmplist.size()>0){
                    //获取需要加的规则
                    mapidList=ListToString_Quotes(tmplist);
                    Map<String,Object> mapidListdate=new HashMap<>();
                    mapidListdate.put("mapidList",mapidList);
                    List<PfirstBalance> firstBalance_tmp=(List<PfirstBalance>)dao.findForList("WxActivityDao.getfirstBalanceMapList",mapidListdate);
                    int add_balance_90=0;
                    for(PfirstBalance first:firstBalance_tmp){
                        //增加积分
                        if(y_user!=null){
                            //发券
                            JSONObject jSONObject_q = new JSONObject();
                            jSONObject_q.put("brand_code","");
                            jSONObject_q.put("shop_code", "");
                            jSONObject_q.put("user_code", "");
                            jSONObject_q.put("order_code", first.getId());
                            jSONObject_q.put("open_id", y_user.getOpen_id());
                            jSONObject_q.put("type", "2");
                            jSONObject_q.put("source", "6");
                            jSONObject_q.put("source_msg", "首次关注发券");
                            jSONObject_q.put("balance_90", first.getBalance_90());
                            jSONObject_q.put("state", 2);
                            jSONObject_q.put("commission", "0");
                            jSONObject_q.put("tradeType", "");
                            jSONObject_q.put("orderState", "1");
                            jSONObject_q.put("orderTotal", "0");
                            jSONObject_q.put("ticketType",first.getTicketType());
                            pd = user_Balance90(jSONObject_q);
                            add_balance_90+=first.getBalance_90();
                        }
                    }
                    try{
                        if(add_balance_90>0){
                            wxTemplateService.send_kf_template(openid, "首次关注获得"+ (add_balance_90/100)+"券");
                        }

                    }catch (Exception e){

                    }

                }else{
                    pd=true;//无满足条件
                    msg="true";
                }
            }else{
                pd=true;//不需要送
                msg="true";
            }
        }
        return msg;
        }else{
            return "true";
        }
    }



    /**
     * 发券
     *
     * @return
     */
    private boolean user_Balance90(JSONObject jSONObject1) {
        Map<String, String> hash1 = new HashMap<String, String>();
        hash1.put("json", jSONObject1.toString());
        Map<String, Object> r_Interface = APIInterface(hash1, "api/balance/operUserBalance90.ac");
        if (r_Interface != null) {

            if ("0".equals(r_Interface.get("flag").toString()) || "3".equals(r_Interface.get("flag").toString()) ) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //接口
    public Map<String, Object> APIInterface(Map<String, String> jSONObject, String url) {
        Map<String, Object> map2 = new HashMap<String, Object>();
        try {
            String requestUrl = ConfigUtil.get("QT_Url") + url;
            String x = URLConectionUtil.httpURLConnectionPostDiy(requestUrl, jSONObject);
            x = URLDecoder.decode(x, "utf-8");
            if (x == null || x.trim().equals("") || x.trim().equals("失败")) {
                // 失败
            } else {
                map2 = JSON.parseObject(x, Map.class);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            return map2;
        }
    }

    /**
     * 空:false 不空:true
     *
     * @param s
     * @return
     */
    private boolean isnull(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    //读取用户信息，如果没注册，则自动注册
    private BaseUser adduser(String open_id,String appid) throws Exception {
        UserInfo userinfo_new=new UserInfo();
        String Nickname="",Headimgurl="",Unionid="";
        int Sex=0;
        try{
            String accessToken= wxUtilService.getAccessToken();
            userinfo_new= WechatAccessToken.getUserInfo(accessToken,open_id);
            if(userinfo_new!=null){
                Nickname=userinfo_new.getNickname();
                Sex=userinfo_new.getSex();
                Headimgurl=userinfo_new.getHeadimgurl();
                Unionid=userinfo_new.getUnionid();
            }
        }catch (Exception e){

        }


        String userId = UuidUtil.get32UUID();
        BaseUser saveuser = new BaseUser();
        saveuser.setId(userId);
        saveuser.setOpen_id(open_id);
        saveuser.setPerson_name(Nickname);//用户昵称
        saveuser.setSex(Sex);//值为1时是男性，值为2时是女性，值为0时是未知
        saveuser.setCover(Headimgurl);//用户头像
        saveuser.setUnionId(Unionid);
        saveuser.setAppid(appid);
        int i = (Integer) dao.save("WxtsDao.insertUser", saveuser);
        if (i > 0) {
            return saveuser;
        } else {
            return null;

        }
    }


    /**
     * *************************结束: 所有活动方法*****************************************
     */

    public boolean getselectUsermsgOpen_id(String open_id) throws Exception {
        Map<String, Object> select_map = new HashMap<>();
        select_map.put("open_id", open_id);
        return (Integer) dao.findForObject("WxActivityDao.selectUsermsgCount", select_map)==0;
    }


    /**
     * List<String> 转 String 逗号分隔（字段加引号）
     * @param listString
     * @return
     */
    public static String ListToString_Quotes(List<String> listString)
    {
        String rString="";
        for(String tmp:listString)
        {
            rString+="'"+tmp.trim()+"',";
        }

        if(rString.length()>0)
        {
            rString=rString.substring(0,rString.length()-1);
        }

        return  rString;
    }


    @Override
    public String doWxscan(String openId, String eventKey,String appid) throws Exception {
        String msg="false";
        //参数验证
        if (!StringUtils.isNotBlank(openId)) {
            //openid空
            return "openid空";
        }
        if (!StringUtils.isNotBlank(eventKey)) {
            //二维码参数空
            return "二维码参数空";
        }
        //open_id  发送方帐号（open_id）
        //eventKey 事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id

        if(eventKey.length()>10&&!eventKey.contains("bizperson_")&&!eventKey.contains("Shop_"))
        {
            Map<String,Object> jSONObject =apiInterfaceService.updateThirdOrderSendBalance(eventKey,openId);
            if("0".equals(jSONObject.get("code")))
            {
                String url=ConfigUtil.get("apisUrl")+"/api/finishOrder.ac";
                Map<String,String> parm=new HashMap<>();
                parm.put("code",eventKey);
                parm.put("openId",openId);
                String resString = URLConectionUtil.httpURLConnectionPostDiy(url,parm);
                try{//回调改写订单其实已经无意义了，因为http方式调用又没有事务，所以没再判断是否回写成功
                    //jSONObject= JSON.parseObject(resString,Map.class);
                }
                catch (Exception e)
                {}
            }else
            {
                return "false";
            }
            return "true";
        }

        ////获取用户，如果不存在，新增
        BaseUser y_user = wxtsService.getBaseUserByOpen_id(openId,appid);
        if (y_user == null) {
            String msgtmp="亲，用户新增失败,发券失败，请联系管理员！";
            wxTemplateService.send_kf_template(openId, msgtmp);
            return msgtmp;
        }

        PayScene payScene = wxtsService.getPaySceneById(eventKey);
        if (payScene != null) {
            String ticketType=payScene.getTicketType();
            if(StringUtil.isNullOrEmpty(ticketType)||!StringUtil.isNumeric(ticketType))
            {ticketType="0";}
            //扫码的是支付场景id
            if (payScene.getScene_type() == 1) { //人工发券临时二维码
                synchronized (this) {
                    RgGift rgGift = wxtsService.getRgGiftByCode(payScene.getMain_code());
                    if (rgGift == null) {
                        String msgtmp="亲，领券失败!请联系管理员！";
                        wxTemplateService.send_kf_template(openId,msgtmp);
                        return msgtmp;
                    }
                    if (rgGift.getState() == 1) {
                        String msgtmp="亲，该二维码已失效，久零券已被领取！";
                        wxTemplateService.send_kf_template(openId, msgtmp);
                        return msgtmp;
                    }

                    //发券
                    JSONObject jSONObject_q = new JSONObject();
                    jSONObject_q.put("ticketType", ticketType);
                    jSONObject_q.put("brand_code", rgGift.getBrand_code());
                    jSONObject_q.put("shop_code", rgGift.getShop_code());
                    jSONObject_q.put("user_code", rgGift.getUser_code());
                    jSONObject_q.put("order_code", payScene.getMain_code());
                    jSONObject_q.put("open_id", openId);
                    jSONObject_q.put("type", rgGift.getType());
                    jSONObject_q.put("source", "2");//人工发券
                    if("0".equals(rgGift.getType()))
                    {
                        jSONObject_q.put("source_msg", "pos机手动发券");
                    }else{
                        jSONObject_q.put("source_msg", "QT手动发券");
                    }
                    jSONObject_q.put("balance_90", rgGift.getPoint_90());
                    jSONObject_q.put("state", 2);
                    jSONObject_q.put("commission", "0");
                    jSONObject_q.put("tradeType", "MICROPAY");
                    jSONObject_q.put("orderState", "1");
                    jSONObject_q.put("orderTotal", "0");
                    jSONObject_q.put("ticketType",payScene.getTicketType());
                    int f_q = user_Balance902(jSONObject_q);

                    //赠送久零贝
                    int f_b = user_coin(y_user, rgGift, payScene);
                    if(f_q==0 || f_b==0){
                        //改变订单状态
                        wxtsService.updateGiftState(rgGift.getCode());
                        wxTemplateService.send_kf_template(openId, "您已经成功领取"+rgGift.getBrand_name()+"商户的"+MathUtil.mul(rgGift.getPoint_90(),0.01)+"久零券");
                        return "true";
                    }else if(f_q==3 || f_b==3){
                        wxTemplateService.send_kf_template(openId, "该二维码已经领取");
                        return "true";
                    }else
                    {
                        return "false";
                    }
                }

            } else if(payScene.getScene_type() == 0){ //订单
                boolean pd_bool=false;
                if (payScene.getState() == 0) { //0:未使用 1:已使用
                    if(payScene.getType()!=5){
                        //绑定会员
                        pd_bool= to_be_vip(payScene, openId,y_user);
                        //推送模板信息
                        send_point_template(payScene, openId);
                    }else
                    {
                        doSendForJlBank(payScene,openId);
                    }

                    if(pd_bool) {
                        //更新二维码失效
                        Map<String, Object> pd = new HashMap<>();
                        pd.put("id", payScene.getId());
                        pd.put("state", "1");
                        wxtsService.updateWherePayScene(pd);
                    }
                    return "true";
                } else {
                    wxTemplateService.send_kf_template(openId, "亲，该二维码已被使用！");
                    return "true";
                }
            }else if (payScene.getScene_type() == 2)//分期首次二维码 下单第一次扫码
            {
                //处理分期第一期
                try {
                    wxtsService.doDealPeriodizationByKeyFirst(payScene.getMain_code(),openId);//此处的mainCode为periodization_main 表id
                    return "true";
                }catch (Exception e){
                   return "false";
                }
            }else if(payScene.getScene_type() == 3)//分期后续二维码 分期领取商户发放的券  //后续处理关联商户
            {
                //处理分期后续
                try {
                    wxtsService.doDealPeriodizationByKeyOther(payScene.getMain_code(),openId);//此处的mainCode为periodization_shop 表id
                    return "true";
                }catch (Exception e){
                    return "false";
                }
            }
        }else if(eventKey.contains("bizperson_")){

            String  code_couponsMoney=eventKey.substring(eventKey.indexOf("_")+1,eventKey.length());
            String code = code_couponsMoney.substring(0,code_couponsMoney.indexOf("_"));
            //查询业务员当前是否送券
            TBizPerson tBizPerson = bizpersonService.getById(code);
            System.out.println("==========>"+code_couponsMoney);
            System.out.println(code);
            String couponseMoney= code_couponsMoney.substring(code_couponsMoney.indexOf("_")+1, code_couponsMoney.length());

            //查询是否送过卷
            boolean f =wxtsService.findHasGiveAway(openId);
            if(tBizPerson.getCouponsMoney()!=0&&!f){
                //客户扫描 业务员二维码 关注后送卷
                //发券
                JSONObject jSONObject_q = new JSONObject();
                jSONObject_q.put("brand_code","");
                jSONObject_q.put("shop_code", "");
                jSONObject_q.put("user_code", code);
                jSONObject_q.put("order_code","");
                jSONObject_q.put("open_id", openId);
                jSONObject_q.put("type", "2");
                jSONObject_q.put("source", "1");
                jSONObject_q.put("source_msg", "用户扫描业务员二维码");
                jSONObject_q.put("balance_90",couponseMoney);
                jSONObject_q.put("state", 2);
                jSONObject_q.put("commission", "0");
                jSONObject_q.put("tradeType", "NATIVE");
                jSONObject_q.put("orderState", "1");
                jSONObject_q.put("orderTotal", "0");
                int f_q = user_Balance902(jSONObject_q);

                if(f_q==0){
                    //成功后保存base_user表sales_id
                    wxtsService.updateBaseUserSalesId(openId,code);
                    wxTemplateService.send_kf_template(openId, "成功领取"+MathUtil.mul(new Double(couponseMoney),0.01)+"久零券");
                    return "true";
                }else
                {
                    return "false";
                }
            }
        }else if(eventKey.contains("Shop_")){
            if(y_user.getShop_id()!=null&&!y_user.getShop_id().equals("")){
            }else{
                String shop_id=eventKey.replace("Shop_","");
                if(shop_id!=null&&!shop_id.equals("")){
//                        y_user.setShop_id(shop_id);
                    wxtsService.updateShopId(openId,shop_id);
                    return "true";
                }else{
                    return "false";
                }
            }
        }
        return null;
    }

    /**
     * 发券
     *
     * @return 0成功 1失败 3重
     */
    private int user_Balance902(JSONObject jSONObject1) {
        int zt=1;
        Map<String, String> hash1 = new HashMap<String, String>();
        hash1.put("json", jSONObject1.toString());
        Map<String, Object> r_Interface = APIInterface(hash1, "api/balance/operUserBalance90.ac");
        if (r_Interface != null) {
            return zt=Integer.parseInt(r_Interface.get("flag").toString());
        } else {
            return zt;
        }
    }


    //发久零币 0成功 1失败 3重
    private int user_coin(BaseUser y_user, RgGift rgGift, PayScene payScene) {
        int zt=1;
        int gift_type = rgGift.getGift_type();
        //if (gift_type != 0 && rgGift.getPoint_90() > 0) {
        if (rgGift.getPoint_90() > 0 || rgGift.getCoin_90()>0) {
            double balance_90 = rgGift.getPoint_90();
            double bei_90 = 0;
            if (gift_type == 1)  //2% (qt)
            {
                bei_90 = balance_90 * 0.02;
            } else if (gift_type == 2)  //5% (qt)
            {
                bei_90 = balance_90 * 0.05;
            } else if(gift_type==0){//(Pos机)
                bei_90=MathUtil.mul(rgGift.getCoin_90(),100.0);//元转换分
            }

            if(bei_90==0){
                return zt;
            }
            bei_90 = MathUtil.mul(bei_90, 0.01);  //四舍五入，保留两位小数
            JSONObject jSONObject90 = new JSONObject();
            jSONObject90.put("userId", y_user.getId());
            jSONObject90.put("state", "1");
            jSONObject90.put("orderNum", payScene.getMain_code());
            jSONObject90.put("source", "3");
            jSONObject90.put("amount", bei_90);

            Map<String, String> map90 = new HashMap<>();
            map90.put("json", jSONObject90.toString());
            Map<String, Object> r_Interface = APIInterface(map90, "api/balance/operUserCoin90.ac");
            if (r_Interface != null) {
                return zt=Integer.parseInt(r_Interface.get("flag").toString());
            } else {
                return zt;
            }
        } else {
            return zt;
        }

    }


    /**
     * 绑定会员，订单处理
     */
    private boolean to_be_vip(PayScene payScene, String open_id,BaseUser user) {
        boolean pd_bool=false;
        try {
            int type = payScene.getType(); //0:未知 1：微信 2：支付宝 3：易支付 4:银联刷卡
            String pay_user_id = payScene.getPay_user_id();
            /*if (StringUtils.isBlank(pay_user_id)) {
                return;
            }*/
            if (type == 1) { //微信
                //已绑定
                if(!StringUtils.isBlank(pay_user_id)) {
                    if (StringUtils.isNotBlank(user.getXy_openid())) {
                        return false;
                    }
                }
                //查询该xy_openid是否绑定
                if(!StringUtils.isBlank(pay_user_id)) {
                    Map<String, Object> pd = new HashMap<>();
                    pd.put("xy_openid", pay_user_id);
                    List<BaseUser> list = wxtsService.getBaseUserByxy_openid(pd);
                    if (list != null && list.size() > 0) {
                        System.out.println("兴业openid没有");
                        return false;
                    }
                }
                //绑定xy_openid
                if(!StringUtils.isBlank(pay_user_id)) {
                    Map<String, Object> pds = new HashMap<String, Object>();
                    pds.put("open_id", open_id);
                    pds.put("xy_openid", pay_user_id);
                    if (wxtsService.updateWhereBaseUser(pds) == 0) {
                        wxTemplateService.send_kf_template(open_id, "xy_openid绑定失败！");
                        return false;
                    }
                }
                //处理订单
                pd_bool=handle_order(payScene, open_id,user.getId());
            }else if (type == 2) { //支付宝
                //已绑定
                if(!StringUtils.isBlank(pay_user_id)) {
                    if (StringUtils.isNotBlank(user.getScan_ali_id())) {
                        return false;
                    }
                }
                //查询该支付宝是否绑定
                if(!StringUtils.isBlank(pay_user_id)) {
                    Map<String, Object> pd = new HashMap();
                    pd.put("scan_ali_id", pay_user_id);
                    List<BaseUser> list = wxtsService.selectWhereBaseUser(pd);
                    if (list != null && list.size() > 0) {
                        return false;
                    }
                }
                //绑定支付宝
                if(!StringUtils.isBlank(pay_user_id)) {
                    Map<String, Object> pds = new HashMap<String, Object>();
                    pds.put("open_id", open_id);
                    pds.put("scan_ali_id", pay_user_id);
                    if (wxtsService.updateWhereBaseUser(pds) == 0) {
                        wxTemplateService.send_kf_template(open_id, "xy_openid绑定失败！");
                        return false;
                    }
                }
                //处理订单
                pd_bool=handle_order(payScene, open_id,user.getId());
            }else if (type == 3) { //易支付
                //已绑定
                if (StringUtils.isNotBlank(user.getScan_yi_id())) {
                    return false;
                }
                //查询该易支付是否绑定
                Map<String, Object> pd = new HashMap<>();
                pd.put("scan_yi_id", pay_user_id);
                List<BaseUser> list = wxtsService.selectWhereBaseUser(pd);
                if (list != null && list.size() > 0) {
                    return false;
                }
                //绑定易支付
                Map<String, Object> pds = new HashMap<String, Object>();
                pds.put("open_id", open_id);
                pds.put("scan_yi_id", pay_user_id);
                if (wxtsService.updateWhereBaseUser(pds) == 0) {
                    wxTemplateService.send_kf_template(open_id, "xy_openid绑定失败！");
                    return false;
                }
                //处理订单
                pd_bool=handle_order(payScene, open_id,user.getId());
            }else if (type == 4) { //银联刷卡

                //查询银联刷卡是否绑定
                BaseUser  user_objcet=phoneUserServiceI.getOneBaseUserByUnionpay(pay_user_id);
                if(user_objcet!=null){
                    return false;
                }
                //绑定银联刷卡
                Map<String, Object> pds = new HashMap<String, Object>();
                pds.put("id", UuidUtil.get32UUID());
                pds.put("userid", user.getId());
                pds.put("unionpayid", pay_user_id);
                if (!wxtsService.addWhereBaseUserByunionpay(pds)) {
                    wxTemplateService.send_kf_template(open_id, "xy_openid绑定失败！");
                    return false;
                }

                //处理订单
                pd_bool=handle_order(payScene, open_id,user.getId());
            }else if(type==8 ||  type==9 ||  type==6||  type==7){
                //处理订单
                pd_bool=handle_order(payScene, open_id,user.getId());
            }

            return pd_bool;
        } catch (Exception e) {
            //logger.error("=绑定会员=报错=", e.fillInStackTrace());
            e.printStackTrace();
            return false;

        }
    }

    /**
     * 订单处理
     */
    private boolean handle_order(PayScene payScene, String open_id,String Userid) {
        try {
            //获取交易成功的订单
            Map<String, Object> pd = new HashMap<String, Object>();
            String ticketType=payScene.getTicketType();
            if(StringUtil.isNullOrEmpty(ticketType)||!StringUtil.isNumeric(ticketType))
            {ticketType="0";}
            OrderMainUnion orderMain = wxtsService.getOrderMainUnionByCode(payScene.getMain_code());
            if (orderMain == null) {
                return false;
            }
            if (orderMain.getState() != 1) {
                return false;
            }
            //更新订单表，完善open_id和90券
            //Brand brand = wxtsService.getBrandOnlyByCode(orderMain.getBrand_code());
            //double jl_total = 0;
            //jl_total = (brand.getProportion() == 0 ? 1 : brand.getProportion()) * orderMain.getCash_total();
            pd.clear();
            pd.put("code", payScene.getMain_code());
            pd.put("open_id", open_id);
            //pd.put("gift_90", jl_total);
            wxtsService.updateWhereOrderMainUnion(pd);

            JSONObject jSONObject = new JSONObject();
            //发券
            JSONObject jSONObject_q = new JSONObject();
            jSONObject_q.put("brand_code", orderMain.getBrand_code());
            jSONObject_q.put("shop_code", orderMain.getShop_code());
            jSONObject_q.put("user_code", orderMain.getUser_code());
            jSONObject_q.put("order_code", payScene.getMain_code());
            jSONObject_q.put("open_id", open_id);
            jSONObject_q.put("type", "0");
            jSONObject_q.put("source", "1");
            jSONObject_q.put("source_msg", "pos机自动发券");
            jSONObject_q.put("ticketType", ticketType);
            jSONObject_q.put("balance_90", orderMain.getGift_90());
            jSONObject_q.put("state", 2);
            jSONObject_q.put("commission", orderMain.getCommission());
            jSONObject_q.put("tradeType", orderMain.getTrade_type());
            jSONObject_q.put("orderState", "1");
            jSONObject_q.put("orderTotal", orderMain.getOrder_total());
            jSONObject_q.put("ticketType", orderMain.getBalance_type());

            int f_q = user_Balance902(jSONObject_q);
            //发贝
            int f_b=user_coin2(Userid,payScene.getMain_code(),orderMain.getCoin_90());

            if((f_q==0 || f_q==3)&&(f_b==0 || f_b==3))
            {
                return true;
            }else{
                return false;
            }

        } catch (Exception e) {
            //logger.error("=订单处理=报错=", e.fillInStackTrace());
            e.printStackTrace();
            return false;
        }
    }

    //发久零币 0成功 1失败 3重
    private int user_coin2(String userId,String orderNum,Double bei_90) {
        int zt = 1;
        try {
            if (bei_90 > 0) {
                JSONObject jSONObject90 = new JSONObject();
                jSONObject90.put("userId", userId);
                jSONObject90.put("state", "1");
                jSONObject90.put("orderNum", orderNum);
                jSONObject90.put("source", "3");
                jSONObject90.put("amount", bei_90);

                Map<String, String> map90 = new HashMap<>();
                map90.put("json", jSONObject90.toString());
                Map<String, Object> r_Interface = APIInterface(map90, "api/balance/operUserCoin90.ac");
                if (r_Interface != null) {
                    return zt = Integer.parseInt(r_Interface.get("flag").toString());
                } else {
                    return zt;
                }
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 1;
        }

    }

    /**
     * 推送模板信息
     */
    private void send_point_template(PayScene payScene, String open_id) throws Exception {
        try {
            OrderMainUnion orderMain = wxtsService.getOrderMainUnionByCode(payScene.getMain_code());
            double jl_money= MathUtil.mul(orderMain.getGift_90(),0.01);
            double money=MathUtil.mul(orderMain.getCash_total(), 0.01);
//            wxTemplateService.send_pay_template(payScene.getId(), open_id,jl_money,money,orderMain.getPay_time());

            wxTemplateService.send_kf_template(open_id, "支付成功，支付金额："+money+"元，赠送久零券："+jl_money);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doSendForJlBank(PayScene payScene, String open_id) {
        TpaySceneDetail detail=wxtsService.getDetail(payScene.getId());//获取发券的参数
        Map<String,Object> map = new HashMap<>();
        //发券
        JSONObject jSONObject_q = new JSONObject();
        jSONObject_q.put("brand_code",detail.getBrandId());
        jSONObject_q.put("shop_code", detail.getShopId());
        jSONObject_q.put("user_code", detail.getUserId());
        jSONObject_q.put("order_code", payScene.getMain_code());
        jSONObject_q.put("open_id", open_id);
        jSONObject_q.put("type", "0");
        jSONObject_q.put("source", "1");
        jSONObject_q.put("source_msg", "pos机自动发券");
        jSONObject_q.put("balance_90", detail.getPoint90());
        jSONObject_q.put("state", 2);
        jSONObject_q.put("commission", detail.getCommission());
        jSONObject_q.put("tradeType", "JLBANK");
        jSONObject_q.put("orderState", "1");
        jSONObject_q.put("orderTotal", detail.getOrderTotal());
        int f_q = user_Balance902(jSONObject_q);
        wxTemplateService.send_kf_template(open_id, "成功领取"+MathUtil.mul(new Double(detail.getPoint90()),0.01)+"久零券");

    }

    @Override
    public String findPayMsg() throws Exception {
        return (String)dao.findForObject("userDao.findPayMsg",null);
    }

    @Override
    public Map<String, Object> messageJson(String openId) throws Exception {
        List<Map<String,Object>> list=(List<Map<String,Object>>) dao.findForList("userDao.findMessage",null);
        Map<String,Object> reMap=new HashedMap();
        reMap.put("touser",openId);
        reMap.put("msgtype","news");
        List<Map<String,Object>> messageList=new ArrayList<>();
        Map<String,Object> mesMap=new HashedMap();
        for(int i=0;i<list.size();i++){
            mesMap.put("title",list.get(i).get("title").toString());
            mesMap.put("description",list.get(i).get("title").toString());
            mesMap.put("url",list.get(i).get("url").toString());
            mesMap.put("picurl", ConfigUtil.get("OSSURL")+list.get(i).get("path").toString());
            messageList.add(mesMap);
            mesMap=new HashedMap();
        }
        mesMap.put("articles",messageList);
        reMap.put("news",mesMap);
        return reMap;
    }

    @Override
    public String messageXml(String openId, String appId) throws Exception {
        List<Map<String,Object>> list=(List<Map<String,Object>>) dao.findForList("userDao.findMessage",null);
        String messageXml="";
        messageXml+="<xml>";
        messageXml+="<ToUserName><![CDATA["+openId+"]]></ToUserName>";
        messageXml+="<FromUserName><![CDATA["+appId+"]]></FromUserName>";
        messageXml+="<CreateTime>"+new Date().getTime()+"</CreateTime>";
        messageXml+="<MsgType><![CDATA[news]]></MsgType>";
        messageXml+="<ArticleCount>"+list.size()+"</ArticleCount>";
        messageXml+="<Articles>";
        for(int i=0;i<list.size();i++){
            messageXml+="<item>" +
                    "<Title><![CDATA["+list.get(i).get("title").toString()+"]]></Title>" +
                    "<Description><![CDATA["+list.get(i).get("title").toString()+"]]></Description>" +
                    "<PicUrl><![CDATA["+ConfigUtil.get("OSSURL")+list.get(i).get("path").toString()+"]]></PicUrl>" +
                    "<Url><![CDATA["+list.get(i).get("url").toString()+"]]></Url>" +
                    "</item>";
        }
        messageXml+="</Articles>" +
                "</xml>";
        return messageXml;
    }



}
