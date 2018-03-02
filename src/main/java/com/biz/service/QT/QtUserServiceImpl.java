package com.biz.service.QT;

import com.alibaba.fastjson.JSON;
import com.biz.conf.Global;
import com.biz.model.Hmodel.TBaseUser;
import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Hmodel.api.TOrderDetail;
import com.biz.model.Pmodel.PorderMainUnion;
import com.biz.model.Pmodel.QT.PgoodsDetail;
import com.biz.model.Pmodel.QT.PwxGoodsStockHistory;
import com.biz.model.Pmodel.api.BaseUser;
import com.biz.model.Pmodel.api.OrderDetail;
import com.biz.model.Pmodel.api.Result;
import com.biz.model.Pmodel.api.Shop;
import com.biz.model.Pmodel.pay17.Pay17SelectPay;
import com.biz.model.Singleton.ZkNode;
import com.biz.service.api.WxTemplateServiceI;
import com.biz.service.base.BaseServiceImpl;
import com.biz.service.base.Pay17ServiceI;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.*;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lzq on 2017/2/20.
 */
@Service("qtUserService")
public class QtUserServiceImpl extends BaseServiceImpl<TorderMain> implements QtUserServiceI {
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Resource(name = "pay17Service")
    private Pay17ServiceI pay17Service;

    @Autowired
    private BaseDaoI<TorderMain> torderMainDao;

    @Autowired
    private BaseDaoI<TOrderDetail> tOrderDetailDao;

    @Resource(name = "wxTemplateService")
    private WxTemplateServiceI wxTemplateService;

    @Autowired
    private BaseDaoI<TBaseUser> tsysUserDao;

    private String Generate_Order="/api/QToayWx/Generate_Order.ac";//收银生成订单

    private String OrderMain="/api/QToayWx/payOrderMain.ac";//直接收银付款

    private String DoCashPay="/api/QToayWx/toDoCashPay.ac";//商品收银付款

    //private String DoCashPay="/api/QToayWx/toDoCashPay.ac";//商品收银付款

    private String GoodsOrderAgain="/api/QToayWx/payGoodsOrderAgain.ac";//商品收银再次验证

    private String OrderMainAgain="/api/QToayWx/payOrderMainAgain.ac";//直接收银再次验证




    @Override
    public Map<String,Object> getQtDaoGouYuanByShopCode(String shopCode){
        Map<String, Object> mapRet = new HashMap<String, Object>();
        mapRet.put("return_code","0");
        mapRet.put("return_info","获取导购员失败");
        try {
//            Map<String,String> map90=new HashMap<>();
            JSONObject jSONObject90 = new JSONObject();
            jSONObject90.put("sid",shopCode);
//            map90.put("sid", jSONObject90.toString());
            System.out.print(jSONObject90.toString());
            String requestUrl = Global.getConfig("QT_Url")+"api/QTUser/getSalesList.ac";
            String x = URLConectionUtil.httpURLConnectionPostDiy(requestUrl, jSONObject90);
            x = URLDecoder.decode(x, "utf-8");
            if(x==null||x.trim().equals(""))
            {
                //调用失败
            }else
            {
                Map<String, Object> map2 = JSON.parseObject(x, Map.class);
                if(map2!=null && ((Integer)map2.get("return_code")).intValue() == 0)
                {
                    //失败
                }else
                {
                    mapRet.put("return_code","1");
                    mapRet.put("return_info","");
                    mapRet.put("return_data",map2.get("return_data"));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return mapRet;
    }



    @Override
    public String getGoodsDetailByCodes(String whareid, String code, String type,String count)throws Exception{
        Map<String, Object> map2 = null;
        String x="";
        try {

            JSONObject jSONObject90 = new JSONObject();
            jSONObject90.put("code",code);
            jSONObject90.put("whareid",whareid);
            jSONObject90.put("type","0");
            jSONObject90.put("count",count);
            System.out.print(jSONObject90.toString());
            String requestUrl = Global.getConfig("ERP_URL")+"/interface/querySku.action";
             x = URLConectionUtil.httpURLConnectionPostDiy(requestUrl, jSONObject90);
            x = URLDecoder.decode(x, "utf-8");
            if(x==null||x.trim().equals(""))
            {
                //调用失败
            }else
            {
//               map2 = JSON.parseObject(x, Map.class);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return x;
    }

    @Override
    public Map<String,Object> doProductOrder(String json, String only_code){
        Map<String,Object> mapRe = new HashMap<>();
        mapRe.put("result","fail");
        mapRe.put("msg", "生产订单失败！");
        try{
            //根据only_code 获取userId
            String userId = (String)dao.findForObject("PhoneUserDao.getUserIdByOnlyCode",only_code);

            List<PgoodsDetail> pgoodsDetailList = JSON.parseArray(json,PgoodsDetail.class);
            if(pgoodsDetailList != null && pgoodsDetailList.size() >0){
                TorderMain torderMain = new TorderMain();
                torderMainDao.save(torderMain);
                Double orderTotal = 0.00;
                List<TOrderDetail> orderDetailList = new ArrayList<TOrderDetail>();
                for(PgoodsDetail pgoodsDetail: pgoodsDetailList )
                {
                    Double goodsTotal = MathUtil.mul(Double.valueOf(pgoodsDetail.getGoodsPrice()),Double.valueOf(pgoodsDetail.getGoodsCount()));
                    TOrderDetail tOrderDetail = new TOrderDetail();
                    tOrderDetail.setId(UuidUtil.get32UUID());
                    tOrderDetail.setCount(Integer.valueOf(pgoodsDetail.getGoodsCount()));
                    tOrderDetail.setIsdel((short)0);
                    tOrderDetail.setPrice(Double.valueOf(pgoodsDetail.getGoodsPrice()));
                    tOrderDetail.setGoodsTotal(goodsTotal);
                    tOrderDetail.setPayTotal(MathUtil.mul(Double.valueOf(ZkNode.getIstance().getJsonConfig().get("service_charge")+""),goodsTotal));
                    tOrderDetail.setState((short)0);
                    tOrderDetail.setCreateTime(new Date());
                    tOrderDetail.setIsdel((short)0);
                    tOrderDetail.setStockId(pgoodsDetail.getGoodsStockId());
                    tOrderDetail.setGoodsId(pgoodsDetail.getGoodsId());
                    tOrderDetail.setOrderId(torderMain.getId());
//                    tOrderDetail.setWhareHouseId(pgoodsDetail.getWhareHouseId());
                    //放入集合
                    orderDetailList.add(tOrderDetail);
                    //计算总价
                    orderTotal = MathUtil.add(orderTotal,goodsTotal);
                    //开始插入数据循环插入
                    tOrderDetailDao.save(tOrderDetail);
                }
                torderMain.setGoodsTotal(orderTotal);
                torderMain.setState(0);
                torderMain.setIsdel(0);
                torderMain.setBuyUserId(userId);//用户付款吗
                torderMain.setCode(String.valueOf(getRndNumCode()));

                //开始插入数据循环插入
                torderMainDao.update(torderMain);

            }else{
                mapRe.put("msg", "没有商品！");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return mapRe;
    }



    @Override
    public Map<String, Object> toDoCashPay(String isCoinPay, String orderCode, String userPayCode, String shop_id, String device_info, String device_ip)
    {
        Map<String,Object> map = new HashMap<String, Object>();
        String trade_type = "";

        if(userPayCode.length() == 1 )
        {
            if("1".equals(userPayCode) || "2".equals(userPayCode))
            {
                // 失败
                map.put("return_code","0");
                map.put("return_msg","无效的支付方式");
                return map;
            }
        }

        //判断1为微信，2为支付宝
        if(userPayCode.startsWith("1")){ //微信
            trade_type = "MICROPAY";
        }else if(userPayCode.startsWith("2")){ //支付宝
            trade_type = "ZFB-MICROPAY";
        }else if(userPayCode.startsWith("3"))
        {
            trade_type = "offline";
        }else if(userPayCode.startsWith("4")){
            trade_type = "UNIONPAY";
        }

        map.put("return_code","0");//支付失败
        map.put("return_msg","支付失败！");
        try{
            //获取订单信息
            String orderMainId = (String)dao.findForObject("sysUserDao.getOrderMainIdByCode",orderCode);

            TorderMain torderMain = torderMainDao.getById(TorderMain.class,orderMainId);
            List<OrderDetail> pOrderDetail=(List<OrderDetail>)dao.findForList("sysUserDao.getOrderMainIdByDetailOrderId",orderMainId);
            int isTicket=pOrderDetail.get(0).getGoodsType();
            if(torderMain !=null && torderMain.getBuyUserId() != null)
            {
                String userId  = torderMain.getBuyUserId();
                BaseUser baseUser=(BaseUser) dao.findForObject("BaseUserDao.queryBaseUserByUserId",userId);
                Double needCoupon = torderMain.getGoodsTotal();//久零券额
                Double serviceTotal = Double.valueOf(CashMoney(needCoupon,torderMain.getBalance_type(),baseUser));//服务费

               // torderMain.setServicePayTotal(serviceTotal);
                //获取用户久零券和久零贝
                Map<String,Object> mapUserInfo = (Map<String,Object>)dao.findForObject("sysUserDao.getUserInfoByUserId",userId);
                String openid= mapUserInfo.get("open_id").toString();
                Double balance_90 = MathUtil.mul(0.01,Double.valueOf(mapUserInfo.get("coupon").toString())); //分转元
                Double balance_shopping_90=MathUtil.mul(0.01,Double.valueOf(mapUserInfo.get("balance_shopping_90").toString())); //分转元
                Double balance_experience_90=MathUtil.mul(0.01,Double.valueOf(mapUserInfo.get("balance_experience_90").toString())); //分转元
                Double userCoin = Double.valueOf(mapUserInfo.get("coin").toString()); //用户久零贝


                //根据支付方式付款（调用店小翼支付）
                Shop shop = (Shop)dao.findForObject("sysUserDao.getShopBySid",shop_id);
                if(balance_90.doubleValue() >= torderMain.getBalance_90().doubleValue())
                {
                    if(balance_shopping_90.doubleValue()<torderMain.getBalance_shopping_90().doubleValue()){
                        map.put("return_msg","用户零购券余额不足！");
                        return  map;
                    }
                    if(balance_experience_90.doubleValue()<torderMain.getBalance_experience_90().doubleValue()){
                        map.put("return_msg","用户体验券余额不足！");
                        return  map;
                    }

                    //调用接口修改库存
                    List<PwxGoodsStockHistory> list=(List<PwxGoodsStockHistory>) dao.findForList("sysUserDao.getHistoryList",torderMain.getId());

                    Map<String,Object> resMap=new HashMap<>();
                    //减库存
                    String y = modifyErpStockByList(list);
                    if(y==null||y.trim().equals("")){
                        // 失败
                        map.put("return_code","0");
                        map.put("return_msg","扣除库存失败");
                        return map;
                    }else {
                        resMap = JSON.parseObject(y, Map.class);
                        if(resMap.get("sign")!=null&&resMap.get("sign").equals("true")){

                        }else{
                            map.put("return_code","0");
                            map.put("return_msg",resMap.get("msg"));
                            return map;
                        }
                    }


                    Map<String, String> jSONObject = new HashMap<String, String>();
                    Map<String, Object> hashMap = new HashMap<String, Object>();
                    for(OrderDetail tdetail:pOrderDetail){
                        boolean flag=true;

                        if(!"1".equals(tdetail.getIsPay90())&&tdetail.getBalance_90()>0){
                            //调用久零券接口扣除久零券
                            jSONObject= new HashMap<String, String>();
                            hashMap = new HashMap<String, Object>();
                            hashMap.put("order_code", tdetail.getId());
                            hashMap.put("brand_code", shop.getBrand_code());
                            hashMap.put("shop_code", shop_id);
                            hashMap.put("user_code", "");
                            hashMap.put("open_id", openid);
                            hashMap.put("type", "1");
                            hashMap.put("source", "3");//消费
                            hashMap.put("source_msg", "用户体验店消费");//付款金额
                            hashMap.put("balance_90",MathUtil.mul(tdetail.getBalance_90(),100.0).intValue());//元转分
                            hashMap.put("state",1);//元转分
                            hashMap.put("commission","0");//元转分
                            hashMap.put("orderState","1");//元转分
                            hashMap.put("orderTotal","0");//元转分
                            hashMap.put("tradeType",trade_type);//元转分
                            hashMap.put("ticketType","0");//元转分
                            jSONObject.put("json",JSON.toJSONString(hashMap));
                            String couponUrl = Global.getConfig("QT_Url") + "api/balance/operUserBalance90.ac";
                            try {
                                String x= URLConectionUtil.httpURLConnectionPostDiy(couponUrl,jSONObject);
                                x = URLDecoder.decode(x, "utf-8");
                                if(x==null||x.trim().equals("")){
                                    flag=false;
                                    // 失败
                                    map.put("return_msg","久零券支付失败！");
                                    System.out.print("=================九零券支付失败，加回erp库存=====");
                                }else {
                                    Map<String, Object> map2 = JSON.parseObject(x, Map.class);
                                    if(map2!=null && map2.get("flag").equals("0"))//成功
                                    {
                                        map.put("return_code","1");
                                        torderMain.setPayCoupon(needCoupon);
                                        tdetail.setIsPayCoupons(1);
                                        tdetail.setTicketType("0");
                                        dao.update("sysUserDao.updateDetailState",tdetail);
                                    }else{
                                        flag=false;
                                        map.put("return_code","0");
                                        map.put("return_msg",map2.get("msg"));
                                        System.out.print("================="+map2.get("msg")+"，加回erp库存=====");
                                    }
                                }
                                if(!flag){
                                    //加erp库存
                                    try {
                                        list = transformList(list);
                                        modifyErpStockByList(list);
                                    } catch (Exception e) {
                                        System.out.print("加回erp库存失败");
                                    }
                                    return map;
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                        if(!"1".equals(tdetail.getIsPayShopping())&&tdetail.getBalance_shopping_90()>0){
                            //调用零购券接口扣除久零券
                            jSONObject= new HashMap<String, String>();
                            hashMap = new HashMap<String, Object>();
                            hashMap.put("order_code", tdetail.getId());
                            hashMap.put("brand_code", shop.getBrand_code());
                            hashMap.put("shop_code", shop_id);
                            hashMap.put("user_code", "");
                            hashMap.put("open_id", openid);
                            hashMap.put("type", "1");
                            hashMap.put("source", "3");//消费
                            hashMap.put("source_msg", "用户体验店消费");//付款金额
                            hashMap.put("balance_90",MathUtil.mul(tdetail.getBalance_shopping_90(),100.0).intValue());//元转分
                            hashMap.put("state",1);//元转分
                            hashMap.put("commission","0");//元转分
                            hashMap.put("orderState","1");//元转分
                            hashMap.put("orderTotal","0");//元转分
                            hashMap.put("tradeType",trade_type);//元转分
                            hashMap.put("ticketType","1");//元转分
                            jSONObject.put("json",JSON.toJSONString(hashMap));
                            String couponUrl = Global.getConfig("QT_Url") + "api/balance/operUserBalance90.ac";
                            try {
                                String x= URLConectionUtil.httpURLConnectionPostDiy(couponUrl,jSONObject);
                                x = URLDecoder.decode(x, "utf-8");
                                if(x==null||x.trim().equals("")){
                                    flag=false;
                                    // 失败
                                    map.put("return_msg","零购券支付失败！");
                                    System.out.print("=================零购券支付失败，加回erp库存=====");
                                }else {
                                    Map<String, Object> map2 = JSON.parseObject(x, Map.class);
                                    if(map2!=null && map2.get("flag").equals("0"))//成功
                                    {
                                        map.put("return_code","1");
                                        torderMain.setPayCoupon(needCoupon);
                                        tdetail.setIsPayCoupons(1);
                                        tdetail.setTicketType("1");
                                        dao.update("sysUserDao.updateDetailState",tdetail);
                                    }else{
                                        flag=false;
                                        map.put("return_code","0");
                                        map.put("return_msg",map2.get("msg"));
                                        System.out.print("================="+map2.get("msg")+"，加回erp库存=====");
                                    }
                                }
                                if(!flag){
                                    //加erp库存
                                    try {
                                        list = transformList(list);
                                        modifyErpStockByList(list);
                                    } catch (Exception e) {
                                        System.out.print("加回erp库存失败");
                                    }
                                    return map;
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                        if(!"1".equals(tdetail.getIsPayExperience())&&tdetail.getBalance_experience_90()>0){
                            //调用体验券接口扣除久零券
                            jSONObject= new HashMap<String, String>();
                            hashMap = new HashMap<String, Object>();
                            hashMap.put("order_code", tdetail.getId());
                            hashMap.put("brand_code", shop.getBrand_code());
                            hashMap.put("shop_code", shop_id);
                            hashMap.put("user_code", "");
                            hashMap.put("open_id", openid);
                            hashMap.put("type", "1");
                            hashMap.put("source", "3");//消费
                            hashMap.put("source_msg", "用户体验店消费");//付款金额
                            hashMap.put("balance_90",MathUtil.mul(tdetail.getBalance_experience_90(),100.0).intValue());//元转分
                            hashMap.put("state",1);//元转分
                            hashMap.put("commission","0");//元转分
                            hashMap.put("orderState","1");//元转分
                            hashMap.put("orderTotal","0");//元转分
                            hashMap.put("tradeType",trade_type);//元转分
                            hashMap.put("ticketType","2");//元转分
                            jSONObject.put("json",JSON.toJSONString(hashMap));
                            String couponUrl = Global.getConfig("QT_Url") + "api/balance/operUserBalance90.ac";
                            try {
                                String x= URLConectionUtil.httpURLConnectionPostDiy(couponUrl,jSONObject);
                                x = URLDecoder.decode(x, "utf-8");
                                if(x==null||x.trim().equals("")){
                                    flag=false;
                                    // 失败
                                    map.put("return_msg","体验券支付失败！");
                                    System.out.print("=================体验券支付失败，加回erp库存=====");
                                }else {
                                    Map<String, Object> map2 = JSON.parseObject(x, Map.class);
                                    if(map2!=null && map2.get("flag").equals("0"))//成功
                                    {
                                        map.put("return_code","1");
                                        torderMain.setPayCoupon(needCoupon);
                                        tdetail.setIsPayCoupons(1);
                                        tdetail.setTicketType("2");
                                        dao.update("sysUserDao.updateDetailState",tdetail);
                                    }else{
                                        flag=false;
                                        map.put("return_code","0");
                                        map.put("return_msg",map2.get("msg"));
                                        System.out.print("================="+map2.get("msg")+"，加回erp库存=====");
                                    }
                                }
                                if(!flag){
                                    //加erp库存
                                    try {
                                        list = transformList(list);
                                        modifyErpStockByList(list);
                                    } catch (Exception e) {
                                        System.out.print("加回erp库存失败");
                                    }
                                    return map;
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }

                    }



                    //调用久零贝扣除久零贝
                    if("1".equals(isCoinPay) && userCoin.doubleValue() > 0)
                    {
                        Double needCoin = 0.00;
                        String coinUrl = Global.getConfig("QT_Url") + "api/balance/operUserCoin90.ac";
                        if(userCoin.doubleValue() >= serviceTotal.doubleValue())
                        {
                            //扣所有
                            needCoin = serviceTotal;
                            torderMain.setCoinPayTotal(serviceTotal);
                            serviceTotal = 0.00;
                        }else{
                            //扣久零贝
                            needCoin = userCoin;
                            torderMain.setCoinPayTotal(userCoin);
                            serviceTotal = MathUtil.sub(serviceTotal,userCoin);
                        }
                        //调用久零券接口扣除久零券
                        Map<String, String> jSONObjectCoin = new HashMap<String, String>();
                        Map<String, Object> hashMapCoin = new HashMap<String, Object>();
                        hashMapCoin.put("userId", userId);
                        hashMapCoin.put("state", "2");
                        hashMapCoin.put("orderNum", orderCode);
                        hashMapCoin.put("source", "2");
                        hashMapCoin.put("amount", needCoin.toString());

                        jSONObjectCoin.put("json",JSON.toJSONString(hashMapCoin));
                        try {
                            String x= URLConectionUtil.httpURLConnectionPostDiy(coinUrl,jSONObjectCoin);
                            x = URLDecoder.decode(x, "utf-8");
                            if(x==null||x.trim().equals("")){
                                // 失败
                                map.put("return_msg","久零贝支付失败！");
                                System.out.print("=================久零贝支付失败，加回erp库存=====");
                                //加erp库存
                                try {
                                    list = transformList(list);
                                    modifyErpStockByList(list);
                                } catch (Exception e) {
                                    System.out.print("加回erp库存失败");
                                }
                                return map;
                            }else {
                                Map<String, Object> map2 = JSON.parseObject(x, Map.class);
                                if(map2!=null && map2.get("flag").equals("0"))//成功
                                {
                                    map.put("return_code","1");
                                }else{
                                    map.put("return_code","0");
                                    map.put("return_msg",map2.get("msg"));
                                    System.out.print("================="+map2.get("msg")+"，加回erp库存=====");
                                    //加erp库存
                                    try {
                                        list = transformList(list);
                                        modifyErpStockByList(list);
                                    } catch (Exception e) {
                                        System.out.print("加回erp库存失败");
                                    }

                                    return map;
                                }
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }

                    //店小翼支付接口
                    if(serviceTotal.doubleValue() > 0 && (trade_type.equals("MICROPAY") || trade_type.equals("ZFB-MICROPAY"))){
                        torderMain.setPayTotal(serviceTotal);
                        /*String urlahead="";
                        if(trade_type.equals("MICROPAY"))
                        {
                            torderMain.setPaymentRoute(3);
                            urlahead = "phone_swiftPassScanPay.do";
                        }else{
                            torderMain.setPaymentRoute(2);//支付宝
                            urlahead = "phone_swiftPassAliScanPay.do";
                        }

                        torderMainDao.update(torderMain);
                        String url = Global.getConfig("KQ_URL")+urlahead+"?" +
                                "shop_id="+shop.getDxy_code().trim()+
                                "&user_code="+shop.getDxy_person_code().trim()+
                                "&author_code="+userPayCode +
                                "&total="+MathUtil.mul(serviceTotal,100.0).intValue()+
                                "&order_code="+orderCode+
                                "&device_info="+device_info+//设备号
                                "&device_ip="+device_ip;//设备ip
                        String back_result = Httpurl.HttpURL_link(url);
                        if(StringUtil.isNullOrEmpty(back_result)){
                            return returnMapOfGoodsCash("0", "店小翼接口调用失败:",0,0,"",trade_type);
                        }else{
                            JSONObject json = JSONObject.fromObject(back_result);
                            int return_code =json.getInt("return_code");//0 失败 1 成功
                            if(return_code==0){
                                return returnMapOfGoodsCash("0", json.getString("error_pay_msg"),0,0,json.getString("return_info"),trade_type);
                            }
                        }*/

                        if(trade_type.equals("MICROPAY"))
                        {
                            torderMain.setPaymentRoute(3);
                        }else{
                            torderMain.setPaymentRoute(2);//支付宝
                        }
                        torderMainDao.update(torderMain);
                        Pay17SelectPay bs_r= pay17Service.getPay17_bs(orderCode.trim(), MathUtil.mul(serviceTotal,100.0).intValue(), userPayCode, device_ip, shop.getDxy_code().trim(), shop.getDxy_person_code().trim());
                        if(bs_r.getReturn_code()==0 || bs_r.getTrade_state()==0 || bs_r.getTrade_state()==2 || bs_r.getTrade_state()==5){
                            if(bs_r.getTrade_state()==5){
                                return returnMapOfGoodsCash("0", bs_r.getReturn_info(),0,0,"USERPAYING-"+bs_r.getReturn_info(),trade_type);
                            }else{
                                return returnMapOfGoodsCash("0", bs_r.getReturn_info(),0,0,bs_r.getReturn_info(),trade_type);
                            }
                        }
                    }else if(serviceTotal.doubleValue() > 0 && trade_type.equals("offline")){
                        torderMain.setPaymentRoute(7);
                        torderMain.setPayTotal(serviceTotal);
                    }else if(serviceTotal.doubleValue() == 0  && trade_type.equals("offline"))
                    {
                        torderMain.setPaymentRoute(7);
                        torderMain.setPayTotal(0.00);
                    }else if(serviceTotal.doubleValue() > 0 && trade_type.equals("UNIONPAY")){
                        torderMain.setPaymentRoute(1);
                        torderMain.setPayTotal(serviceTotal);
                    }else if(serviceTotal.doubleValue() == 0  && trade_type.equals("UNIONPAY"))
                    {
                        torderMain.setPaymentRoute(1);
                        torderMain.setPayTotal(0.00);
                    }


                    //付款成功处理订单
                    torderMain.setState(1);
                    torderMain.setPayTime(new Date());
                    torderMainDao.update(torderMain);
                  /*  if(isTicket==1)//联盟商品支付后送券
                    {
                        sendTicketForUnion(torderMain,pOrderDetail);
                    }else
                    {*/
                        sendTicketForJiuling(torderMain,pOrderDetail);
                   // }
                    map.put("return_code","1");//支付成功
                    map.put("return_msg","支付成功！");

                    try{
                        //模板消息
                        wxTemplateService.send_kf_template(openid, "订单"+orderCode+"支付成功");

                    } catch (Exception e) {

                    }

                }else{
                    map.put("return_msg","用户久零余额不足！");
                }
            }else{
                map.put("return_msg","订单不存在！");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return map;
    }

    private void sendTicketForJiuling(TorderMain torderMain, List<OrderDetail> pOrderDetail) throws Exception {
        String brandCode=torderMain.getBrandCode();//获取商户信息
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String,Object> brandInfo= (Map<String, Object>) dao.findForObject("brandMapper.getBrandInfoByCode",brandCode);
        Date now=new Date();
        for (OrderDetail detail:pOrderDetail)
        {

            if(StringUtil.isNullOrEmpty(detail.getEndTime())||StringUtil.isNullOrEmpty(detail.getStartTime()))//没有设定发券时间的不发券
            {continue;}
            if(now.getTime()<=sdfTime.parse(detail.getEndTime()).getTime()&&now.getTime()>=sdfTime.parse(detail.getStartTime()).getTime())
            {
                //在发券时间内的发券,发体验券
                doSendTicket(torderMain,detail,brandCode,"2",Double.valueOf(detail.getServicePayTotal()),detail.getId(),"QT服务费返券");
            }
        }

    }

    private void sendTicketForUnion(TorderMain torderMain, List<OrderDetail> pOrderDetail) throws Exception {

        String brandCode=torderMain.getBrandCode();//获取商户信息
        Map<String,Object> brandInfo= (Map<String, Object>) dao.findForObject("brandMapper.getBrandInfoByCode",brandCode);
        String isTicket=brandInfo.get("isTicket")+"";
        if(StringUtil.isNullOrEmpty(isTicket))
        {isTicket="0";}
        String goodsId=pOrderDetail.get(0).getGoodsId();//获取商品信息

        Map<String,Object> goodsInfo= (Map<String, Object>) dao.findForObject("mallDao.getGoodsInfoByCode",goodsId);
        String ticketType=goodsInfo.get("ticketType")+"";
        if(StringUtil.isNullOrEmpty(ticketType))
        {ticketType="0";}
        if("1".equals(isTicket))//只有isTicket=1才是开启送券
        {
            doSendTicket(torderMain, pOrderDetail.get(0), brandCode,ticketType,torderMain.getCoinPayTotal(),pOrderDetail.get(0).getId(),"QT购买商品返券");
        }
    }

    private void doSendTicket(TorderMain  order, OrderDetail detail, String brandCode, String ticketType,Double point90,String orderCode,String msg) throws Exception {
        String url=ConfigUtil.get("QT_Url")+"api/balance/operUserBalance90.ac";

        Map<String,String> userInfo= (Map<String, String>) dao.findForObject("sysUserDao.getUserInfoByUserId",order.getBuyUserId());

        Map<String,Object> map =new java.util.HashMap<>();
        map.put("brand_code",brandCode);
        map.put("shop_code","");
        map.put("user_code","");
        map.put("order_code",orderCode);
        map.put("open_id",userInfo.get("open_id"));
        map.put("type","1");
        map.put("source","11");
        map.put("source_msg",msg);
        map.put("balance_90",(new   Double(point90*100)).intValue()+"");
        map.put("state","2");
        map.put("commission","0");
        map.put("tradeType","");
        map.put("orderState","1");
        map.put("orderTotal","0");
        map.put("ticketType",ticketType);
        //map.put("json", JSON.toJSONString(map));
        Map<String,String> map1 =new java.util.HashMap<>();
        map1.put("json", JSON.toJSONString(map));
        String res= URLConectionUtil.httpURLConnectionPostDiy(url,map1);
        map1.clear();
        map1=JSON.parseObject(res,Map.class);
        map1.clear();
    }


    public long getRndNumCode()
    {
        Random rand = new Random();
        Date now = new Date();
        long t = now.getTime();
        return Long.valueOf(t * 1000L + (long)rand.nextInt(1000));
    }


    private Map<String, Object> returnMapOfGoodsCash(String result,String msg,double user_iscoin,double ew_paycoin, String return_info, String trade_type){
        Map<String, Object> r_map=new HashMap<>();
        r_map.put("return_code",result);//返回码1 ：成功 0：失败
        r_map.put("useriscoin",user_iscoin);//当前用户剩余90贝
        r_map.put("paycoin",ew_paycoin);//需要额外支付金额
        r_map.put("return_info",return_info);//需要额外支付金额
        r_map.put("msg",msg);
        r_map.put("return_msg","return_info");
        r_map.put("trade_type",trade_type);
        return r_map;
    }


    @Override
    public String getWhareid(String shopId) throws Exception {
        return (String)dao.findForObject("sysUserDao.getWhareid",shopId);
    }



    @Override
    public TorderMain getOrderMainById(String orderCode)throws Exception{

        String orderMainId = (String)dao.findForObject("sysUserDao.getOrderMainIdByCode",orderCode);

        if(StringUtil.isNullOrEmpty(orderMainId))
        {
            return null;
        }
        TorderMain torderMain = torderMainDao.getById(TorderMain.class,orderMainId);

        return torderMain;
    }


    @Override
    public Result goodsCashBackMoney(String orderCode, String userCode)throws Exception{

        Result result = new Result();

        int return_code = 1;
        String return_info = "";
        String orderMainId = (String)dao.findForObject("sysUserDao.getOrderMainIdByCode",orderCode);
        TorderMain torderMain = torderMainDao.getById(TorderMain.class,orderMainId);
        String shop_id = torderMain.getShopId();
        Shop shop = (Shop)dao.findForObject("sysUserDao.getShopBySid",shop_id);

        TBaseUser tSysUser = tsysUserDao.getById(TBaseUser.class,torderMain.getBuyUserId());
        String open_id =tSysUser.getOpenId();

        if(torderMain.getBalance_type()==3)  //混合支付不可退款
        {
            result.setReturn_code(0);
            result.setReturn_info("混合支付不可退款");

            return result;
        }

        /***********对传入金额处理*************/
        if(return_code == 1)
        {
            if(return_code == 1)
            {
                //店小翼key
                String key = getKeyByParam(shop.getDxy_code(),shop.getDxy_person_code(),orderCode);
                //判断线上线下支付
                if(torderMain.getPaymentRoute().intValue() == 7 || torderMain.getPaymentRoute().intValue() == 1 )//线下支付//银联
                {
                    return_code = 1;
                }else{
                    /****************调用店小翼退款接口**********************/
                    String url = Global.getConfig("KQ_URL")+"phone_swiftPassBack.do?order_code="+orderCode+"&total="+MathUtil.mul(Double.valueOf(torderMain.getPayTotal()),100.0).intValue()+"&user_code="+shop.getDxy_person_code()+"&backcode="+key;
                    String back_result = HttpURL(url);
                    JSONObject json = JSONObject.fromObject(back_result);
//                    logger.error("=QT端体验店退款,退款=json="+json);
                    return_info=json.getString("return_info");
                    return_code = json.getInt("return_code");
                }
            }
        }
        if(return_code == 0){ //错误
            result.setReturn_code(0);
            result.setReturn_info(return_info);
        }else if(return_code == 1){ //正确
            return_code = 0 ; /// 重新定位
            /****************判断是否需要退久零贝*************************/
            if(torderMain.getCoinPayTotal()!=null && torderMain.getCoinPayTotal().doubleValue()>0
                    && torderMain.getBuyUserId() !=null)
            {
                Map<String,String> map90=new HashMap<>();
                JSONObject jSONObject90 = new JSONObject();
                jSONObject90.put("userId",torderMain.getBuyUserId());
                jSONObject90.put("state","1");
                jSONObject90.put("orderNum",orderCode);
                jSONObject90.put("source","4");
                jSONObject90.put("amount",torderMain.getCoinPayTotal().toString());
                map90.put("json", jSONObject90.toString());
                System.out.print(jSONObject90.toString());
                String requestUrl = Global.getConfig("QT_Url")+"api/balance/operUserCoin90.ac";
                String x = URLConectionUtil.httpURLConnectionPostDiy(requestUrl, map90);
                x = URLDecoder.decode(x, "utf-8");
                if(!StringUtil.isNullOrEmpty(x) )
                {
                    Map<String, Object> map2 = JSON.parseObject(x, Map.class);
                    if(map2!=null && map2.get("flag").equals("0"))
                    {
                        return_code=1;
                    }
                }
            }else{
                return_code = 1;
            }
            if(return_code == 0) { //错误
                result.setReturn_code(0);
                result.setReturn_info(return_info);
            }else {
                result.setReturn_code(1);
                result.setReturn_info(return_info);
                torderMain.setState(2);//已退款
                torderMain.setBuyUserId(userCode);
                torderMain.setBackTime(new Date());
                /******************加久零券**************************/
                Map<String,String> map1=new HashMap<>();
                double double_total = 0;
                if(torderMain.getBalance_90()>0)
                {
                    double_total=torderMain.getBalance_90();
                }else if(torderMain.getBalance_experience_90()>0)
                {
                    double_total=torderMain.getBalance_experience_90();
                }else if(torderMain.getBalance_shopping_90()>0)
                {
                    double_total=torderMain.getBalance_shopping_90();
                }

                JSONObject jSONObject1 = new JSONObject();
                jSONObject1.put("open_id",open_id);
                jSONObject1.put("balance_90",MathUtil.mul(Double.valueOf(double_total),100.0).intValue());
                jSONObject1.put("shop_code",shop.getSid());
                jSONObject1.put("order_code",orderCode);
                jSONObject1.put("brand_code",shop.getBrand_code());
                jSONObject1.put("user_code",userCode);
                jSONObject1.put("type", "1");
                jSONObject1.put("source", "5");//退款
                jSONObject1.put("source_msg", "用户退款");
                jSONObject1.put("state",2);
                jSONObject1.put("commission","0");
                jSONObject1.put("orderState","1");
                jSONObject1.put("orderTotal","0");
                jSONObject1.put("tradeType",torderMain.getPaymentRoute());
                jSONObject1.put("ticketType",torderMain.getBalance_type());
                map1.put("json", jSONObject1.toString());
                System.out.print(jSONObject1.toString());
                String requestUrl = Global.getConfig("QT_Url")+"api/balance/operUserBalance90.ac";
                String x = URLConectionUtil.httpURLConnectionPostDiy(requestUrl, map1);
                x = URLDecoder.decode(x, "utf-8");
                if(x==null||x.trim().equals("")){
                    result.setReturn_code(0);
                    result.setReturn_info("加劵失败");
                }else {
                    Map<String, Object> map2 = JSON.parseObject(x, Map.class);
                    if(map2!=null && map2.get("flag").equals("0"))  //加劵成功
                    {
                        //调用接口修改库存
                        List<PwxGoodsStockHistory> listUpCount=(List<PwxGoodsStockHistory>) dao.findForList("sysUserDao.getHistoryListUpCount",torderMain.getId());
                        String upCountUrl = ConfigUtil.get("ERP_URL")+"/interface/changeStockForWhareHouse.action";
                        PwxGoodsStockHistory pwxGoodsStockHistory=new PwxGoodsStockHistory();
                        pwxGoodsStockHistory.setParmlist(listUpCount);
                        String jsonUpCounttext=JSON.toJSONString(pwxGoodsStockHistory);
                        Map<String, String> upCountParam = new HashMap();
                        Map<String,Object> resUpCpuntMap=new HashMap<>();
                        upCountParam.put("parm",jsonUpCounttext);
                        String xup= URLConectionUtil.httpURLConnectionPostDiy(upCountUrl,upCountParam);
                        xup = URLDecoder.decode(xup, "utf-8");
                        if(xup==null||xup.trim().equals("")){
                            // 失败
                            result.setReturn_code(0);
                            result.setReturn_info("增加库存失败");
                        }else {
                            resUpCpuntMap = JSON.parseObject(xup, Map.class);
                            if(resUpCpuntMap.get("sign")!=null&&resUpCpuntMap.get("sign").equals("true")){
                                result.setReturn_code(1);
                                result.setReturn_info("退款成功");
                                torderMainDao.update(torderMain);
                            }else{
                                result.setReturn_code(0);
                                result.setReturn_info("增加库存失败");
                            }
                        }
                    }else
                    {
                        result.setReturn_code(0);
                        result.setReturn_info("加劵失败");
                    }
                }
            }
        }

        return  result;
    }


    private String getKeyByParam(String dxyCode, String dxyPersonCode, String orderCode)
    {
        String key = "";
        try {
            StringBuffer key_sb = new StringBuffer();
            key_sb.append("SHOPCODE=");
            key_sb.append(dxyCode);
            key_sb.append("&USERCODE=");
            key_sb.append(dxyPersonCode);
            key_sb.append("&ORDERCODE=");
            key_sb.append(orderCode);
            System.err.println(key_sb);
            key = CryptTool.md5Digest(key_sb.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return key;
    }



    /**
     * 远程连接HttpURL
     * @param url
     * @return
     */
    private String HttpURL(String url){
        String result = "";
        try {
            URL localURL = new URL(url);
            String async = "";
            URLConnection connection = localURL.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

            httpURLConnection.setRequestProperty("Accept-Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            httpURLConnection.setDoOutput(true);

            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            BufferedReader reader = null;
            StringBuffer resultBuffer = new StringBuffer();
            String tempLine = null;

            if (httpURLConnection.getResponseCode() >= 300)
            {
                throw new Exception(
                        "HTTP Request is not success, Response code is "
                                + httpURLConnection.getResponseCode());
            }
            try
            {
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                reader = new BufferedReader(inputStreamReader);
                String asynckl = "";
                while ((tempLine = reader.readLine()) != null)
                {
                    resultBuffer.append(tempLine);
                }

            } finally
            {

                if (reader != null)
                {
                    reader.close();
                }

                if (inputStreamReader != null)
                {
                    inputStreamReader.close();
                }

                if (inputStream != null)
                {
                    inputStream.close();
                }

            }
            result = resultBuffer.toString();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
//            logger.error("=远程连接HttpURL报错==" ,e.fillInStackTrace());
            return result;
        }
    }

    /**
     * 服务费金额,券*百分比
     * @param total 90券
     * @return
     */
    private int CashMoney(double total,int balance_type,BaseUser userObjct){
        double  serviceChrge = 0.05;
        BigDecimal bg=new BigDecimal(0);
        try{
            if(balance_type==0){
                //久另券
                serviceChrge= Double.parseDouble(ZkNode.getIstance().getJsonConfig().get("service_charge")+"");//服务费比例
                bg = new BigDecimal(total*serviceChrge);
            }else if(balance_type==2){
                //体验券
                serviceChrge= Double.parseDouble(ZkNode.getIstance().getJsonConfig().get("service_experience_charge")+"");//服务费比例
                bg = new BigDecimal(total*serviceChrge);
            }else if(balance_type==1){
                //领购券
                serviceChrge= Double.parseDouble(ZkNode.getIstance().getJsonConfig().get("service_shop_charge")+"");//体验券服务费比例
                bg = new BigDecimal(total*serviceChrge);
            }else if(balance_type==3){
                //混合支付
                double serviceChrge2=0.05;
                serviceChrge= Double.parseDouble(ZkNode.getIstance().getJsonConfig().get("service_shop_charge")+"");
                serviceChrge2= Double.parseDouble(ZkNode.getIstance().getJsonConfig().get("service_charge")+"");//服务费比例
                if(userObjct.getBalance_shopping_90()/100>=total){
                    //如果零购券足够
                    bg = new BigDecimal(total*serviceChrge);
                }else{
                    //混合支付服务费
                    bg=new BigDecimal(userObjct.getBalance_shopping_90()/100*serviceChrge+(total-userObjct.getBalance_shopping_90()/100)*serviceChrge2);
                }
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return bg.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    @Override
    public Map<String, Object> toDoGoodPay(String isCoinPay, String str, String userPayCode, String shop_id, String type,String onlyCode,String ip) throws Exception {

        String result="";//返回类型fail:生成失败,success :成功
        String orderCode="";//订单
        String msg="";//返回信息
        Map<String,Object> resultMap=new HashedMap();
        str = URLDecoder.decode(str, "utf-8");
        Map<String,String> p_map=new HashMap<>();
        p_map.put("type",type);
        p_map.put("jsonSting",str);
            //直接收银
            //商品收银
        Map<String, Object> r_map= this.QTInterface(p_map, Generate_Order);
        if(!(r_map==null || (r_map.size()==0))){
            if(!r_map.get("result").equals("")&&r_map.get("result").equals("success")){
                //如果生成成功 付款
                JSONObject jSONObject = new JSONObject();
                Map<String,Object> mapJson = JSON.parseObject(str,Map.class);
                String orderMainId=r_map.get("orderCode").toString();
                PorderMainUnion p=(PorderMainUnion) dao.findForObject("sysUserDao.findOrderMainUnion",orderMainId);
                String openId=(String)dao.findForObject("sysUserDao.findByCode",onlyCode);
                jSONObject.put("isCoinPay",isCoinPay);
                jSONObject.put("openid",openId);
                jSONObject.put("orderCode",orderMainId);
                if(p!=null){
                    jSONObject.put("order_total",p.getPay_coin()+"");
                    jSONObject.put("iscoin_total",mapJson.get("needMoney")+"");
                }
                if(userPayCode.equals("8")){
                    jSONObject.put("userPayCode","4");
                    jSONObject.put("author_code","4");
                }else if(userPayCode.equals("9")){
                    jSONObject.put("userPayCode","3");
                    jSONObject.put("author_code","3");
                }else {
                    jSONObject.put("userPayCode",userPayCode);
                    jSONObject.put("author_code",userPayCode);
                }
                jSONObject.put("shopId",shop_id);
                jSONObject.put("device_ip",ip);
                jSONObject.put("device_info","");
                Map<String, String> hashMap_coin = new HashMap<String, String>();
                hashMap_coin.put("jsonSting", jSONObject.toString());
                Map<String, Object> pay_map=new HashMap<>();
                if(type.equals("1")){
                    //直接付款
                    pay_map= this.QTInterface(hashMap_coin, OrderMain);
                }else{
                    //商品付款
                    pay_map= this.QTInterface(jSONObject, DoCashPay);
                }
                    if(!(pay_map==null || (pay_map.size()==0))) {
                        resultMap.put("return_code",pay_map.get("return_code"));
                        if(pay_map.get("return_info")!=null){
                            resultMap.put("return_info",pay_map.get("return_info").toString());
                            resultMap.put("orderMainId",orderMainId);
                        }
                        resultMap.put("msg",pay_map.get("msg"));
                    }else{
                        resultMap.put("return_code","0");
                        resultMap.put("msg","支付失败");
                    }
            }else{
                resultMap.put("return_code","0");
                resultMap.put("msg","订单生成失败");
            }
        }

        return resultMap;
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

    @Override
    public Map<String, Object> checkPhoneCode(String orderCode, String onlyCode) throws Exception {
        Map<String,Object> map=new HashedMap();
        map.put("return_code","0");
        map.put("msg","再次验证失败");
        TorderMain torderMain=(TorderMain) dao.findForObject("sysUserDao.findTorderMain",orderCode);
        String openId=(String)dao.findForObject("sysUserDao.findByCode",onlyCode);
        Map<String,String> p_map=new HashMap<>();
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("orderCode",orderCode.toString());
        jSONObject.put("openid",openId);
        jSONObject.put("order_total",torderMain.getCoinPayTotal().toString());
        jSONObject.put("iscoin_total",torderMain.getPayTotal().toString());
        p_map.put("jsonSting",jSONObject.toString());
        Map<String, Object> re_map= this.QTInterface(p_map, GoodsOrderAgain);
        if(re_map!=null&&re_map.size()!=0){
            map.put("return_code",re_map.get("return_code").toString());
            if(map.get("return_code").equals("1")){
            }else{
                map.put("msg",re_map.get("msg").toString());
            }
        }
        return map;
    }

    @Override
    public Map<String, Object> checkPhoneCodeAgain(String orderCode, String onlyCode, String ip,String needMoney) throws Exception {
        Map<String,Object> map=new HashedMap();
        map.put("return_code","0");
        map.put("msg","再次验证失败");
        PorderMainUnion porderMainUnion=(PorderMainUnion) dao.findForObject("sysUserDao.findOrderMainUnion",orderCode);
        String openId=(String)dao.findForObject("sysUserDao.findByCode",onlyCode);
        Map<String,String> p_map=new HashMap<>();
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("orderCode",orderCode.toString());
        jSONObject.put("openid",openId);
        jSONObject.put("order_total",porderMainUnion.getPay_coin().toString());
        jSONObject.put("iscoin_total",needMoney.toString());
        jSONObject.put("device_ip",ip);
        p_map.put("jsonSting",jSONObject.toString());
        Map<String, Object> re_map= this.QTInterface(p_map, OrderMainAgain);
        if(re_map!=null&&re_map.size()!=0){

            if(re_map.get("return_code").equals("success")){
                map.put("return_code","1");
            }else{
                map.put("return_code","0");
                map.put("msg","尚未支付");
            }
        }
        return map;
    }


    /**
     * 调用接口修改库存
     * @param list
     * @return
     * @throws Exception
     */
    private String modifyErpStockByList(List<PwxGoodsStockHistory> list) throws  Exception{

        String requestUrl = ConfigUtil.get("ERP_URL")+"/interface/changeStockForWhareHouse.action";
        PwxGoodsStockHistory pwxGoodsStockHistory=new PwxGoodsStockHistory();
        pwxGoodsStockHistory.setParmlist(list);
        String jsontext=JSON.toJSONString(pwxGoodsStockHistory);
        Map<String, String> Param = new HashMap();
        Param.put("parm",jsontext);
        String y= URLConectionUtil.httpURLConnectionPostDiy(requestUrl,Param);
        y = URLDecoder.decode(y, "utf-8");

        return y;
    }


    /**
     * 讲减库存状态给为加库存
     * @param list
     * @return
     */
    private List<PwxGoodsStockHistory>  transformList(List<PwxGoodsStockHistory> list)
    {
       for(PwxGoodsStockHistory pwxGoodsStockHistory :list)
       {
           pwxGoodsStockHistory.setState(9);
       }

       return list;
    }
}
