package com.biz.service.api;

import com.alibaba.fastjson.JSON;
import com.biz.conf.Global;
import com.biz.model.Hmodel.TBaseUser;
import com.biz.model.Hmodel.TSysUser;
import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Hmodel.api.TOrderDetail;
import com.biz.model.Pmodel.PhoneUserIntegral;
import com.biz.model.Pmodel.QT.PwxGoodsStockHistory;
import com.biz.model.Pmodel.api.*;
import com.biz.model.Pmodel.basic.Brand;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lzq on 2017/2/4.
 */

@Service("phoneUserService")
public class PhoneUserServiceImpl extends BaseServiceImpl<TorderMain> implements PhoneUserServiceI {
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Autowired
    private BaseDaoI<TorderMain> torderMainDao;

    @Autowired
    private BaseDaoI<TOrderDetail> tOrderDetailDao;

    @Autowired
    private BaseDaoI<TBaseUser> tbaseUserDao;
    
    @Autowired
    private BaseDaoI<TSysUser> tsysUserDao;


    @Override
    public JSONObject getUserForLoginhashMap(Map<String, Object> map){

        JSONObject jSONObject = new JSONObject();
        //图片前缀
        String imgUrl = ConfigUtil.get("OSSURL") +"ninezero/img_small/" ;
        //清空返回参数
        jSONObject.put("return_code", 0);
        jSONObject.put("return_data", null);
        jSONObject.put("return_info", "");
        try {
            //根据用户名密码查询数量
            Integer userCount = (Integer) dao.findForObject("PhoneUserDao.getUserForLoginhashMap", map);
            if (userCount.intValue() == 0) { // 不存在或非门店
                jSONObject.put("return_info", "用户名或密码有误！");
            }else if(userCount.intValue() > 1){
                jSONObject.put("return_info", "存在重复用户，请联系管理员！");
            }else{
                //根据用户名密码查询用户信息
                Map<String, Object> hm = (Map<String, Object>)dao.findForObject("PhoneUserDao.getUserInfoForLogin",map);
                if (hm == null || hm.get("is_90")==null || "0".equals(hm.get("is_90").toString())) {
                    jSONObject.put("return_info", "门店不存在或非门店用户！");
                }else {
                    //处理图片
                    if ( hm.get("logo_url") != null && StringUtils.isNotBlank(hm.get("logo_url").toString())) {
                        hm.put("shop_logo", imgUrl + hm.get("logo_url"));
                    }else {
                        hm.put("shop_logo", imgUrl + "2015-12-04/63b60c399ca14e2693b70df99c6d8379.jpg");
                    }
                    JSONObject subMsgs = JSONObject.fromObject(hm);
                    jSONObject.put("return_code", 1);
                    jSONObject.put("return_data", subMsgs);
                }
            }
        } catch (Exception e) {
            String msg = e.getMessage().length() > 30 ? e.getMessage().substring(0, 29) : e.getMessage();
            jSONObject.put("return_info", msg);
            e.printStackTrace();
        }
        return jSONObject;
    }

    @Override
    public Shop getShopBySid(String sid) throws Exception {
        return (Shop) dao.findForObject("PhoneUserDao.getShopBySid", sid);
    }


    @Override
    public Integer getOrderMainCount(String code) throws Exception {
        return (Integer) dao.findForObject("PhoneUserDao.getOrderMain90ByCode", code);
    }

//    @Override
//    public int doNineZeroPay(String open_id, String order_code, String cash_total)throws Exception{
//        int codeR = 0;
//
//        String tSysUserId = (String) dao.findForObject("PhoneUserDao.getSysUserIdByOpenId", open_id);
//        if(tSysUserId!=null && !tSysUserId.trim().equals(""))
//        {
//            Map<String,String> map90=new HashMap<>();
//            JSONObject jSONObject90 = new JSONObject();
//            jSONObject90.put("userId",tSysUserId);
//            jSONObject90.put("state","2");
//            jSONObject90.put("orderNum",order_code);
//            jSONObject90.put("source","2");
//            jSONObject90.put("amount",cash_total);
//            map90.put("json", jSONObject90.toString());
//            System.out.print(jSONObject90.toString());
//            String requestUrl = Global.getConfig("BALANCE_90")+"orther_operUserCoin90.do";
//            String x = URLConectionUtil.httpURLConnectionPostDiy(requestUrl, map90);
//            x = URLDecoder.decode(x, "utf-8");
//            if(x==null||x.trim().equals(""))
//            {
//                codeR=0;
//                String sys = "";
//            }else{
//                Map<String, Object> map2 = JSON.parseObject(x, Map.class);
//                if(map2!=null && map2.get("flag").equals("0"))
//                {
//                    codeR=1;
//                }else
//                {
//                    codeR=0;
//                }
//            }
//        }
//        return codeR;
//    }

    @Override
    public BaseUser getBaseUserByOpen_id(String open_id) throws Exception
    {
        return (BaseUser) dao.findForObject("PhoneUserDao.getBaseUserByOpen_id", open_id);
    }


    /**
     * 添加
     */
    @Override
    public void insertOrderMain90(OrderMain orderMain90) throws Exception
    {
        dao.save("PhoneUserDao.insertOrderMain90", orderMain90);
    }

    @Override
    public JSONObject getPhoneDailyOrderSummaryByShopCodeAndUserCode(String shopCode, String userCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("return_code", 0);
        jsonObject.put("return_data", null);
        try {
            //修改为查询数量
            Integer shopCount = (Integer)dao.findForObject("PhoneUserDao.getShopCountByShopCode",shopCode);
            if(shopCount.intValue() == 0)
            {
                jsonObject.put("return_msg", "门店不存在！");
            }else{
                //未返回，查询数据
                Map<String,String> mapCode = new HashMap<>();
                mapCode.put("shopCode", shopCode);
                mapCode.put("userCode", userCode);
                List<Map<String,Object>> os_list =
                        (List<Map<String,Object>>)dao.findForList("PhoneUserDao.queryDailyOrderSummary", mapCode);
                jsonObject.put("return_code", 1);
                jsonObject.put("return_data", os_list);
                jsonObject.put("return_msg", "");
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            if(msg.length()>30){
                msg = msg.substring(0, 29);
            }
            jsonObject.put("return_msg", msg);
        }
        return jsonObject;
    }

    @Override
    public Result resetPwd(String user_code, String pwd, String new_pwd) {
        Result result = new Result();
        Map<String, Object> params = new HashMap<>();
        params.put("user_code", user_code);
        params.put("pwd", pwd);
        params.put("new_pwd", new_pwd);
        try {
            Map<String, Object> userMap = (Map<String, Object>)dao.findForObject("PhoneUserDao.getUserByCode",params);
            if (userMap != null) {
                String oldPwd = (String) userMap.get("pwd");
                if (pwd.equals(oldPwd)) {
                    dao.update("PhoneUserDao.resetPwd", params);
                    result.setReturn_code(1);
                    result.setReturn_info("密码修改成功");
                } else {
                    result.setReturn_code(0);
                    result.setReturn_info("原密码输入有误，请检查后重新输入");
                }
            } else {
                result.setReturn_code(0);
                result.setReturn_info("修改密码异常!");
            }
        } catch (Exception e) {
            result.setReturn_code(0);
            result.setReturn_info("修改密码异常!");
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 收银记录 分收银员|店长查询
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String,Object>> queryRecordsByParam(Map<String,Object> mapParam) throws Exception
    {

        //支付类型 MICROPAY:刷卡支付 NATIVE:扫码支付 JSAPI:公众号支付  OFFLINE：线下支付
        //付款途径：0.全部 1.银联支付 2.支付宝支付 3.微信支付 6.翼支付，7.offline线下支付
//        try {
//            List<OrderMain90> orderMainList = orderMain90Service.collectRecordForClerk(pd);
            List<Map<String,Object>> mapColect = (List<Map<String,Object>>) dao.findForList("PhoneUserDao.queryRecordsByParam",mapParam);

            return mapColect;
    }

    @Override
    public int queryRecordsCountByParam(Map<String, Object> mapParam) throws Exception {
        return (int) dao.findForObject("PhoneUserDao.queryRecordsCountByParam",mapParam);
    }


    @Override
    public List<Map<String, Object>> queryMoneyByParam(Map<String, Object> mapParam) throws Exception {
        List<Map<String,Object>> mapColect = (List<Map<String,Object>>) dao.findForList("PhoneUserDao.queryMoneyByParam",mapParam);

        return mapColect;
    }

    @Override
    public int queryMoneyCountByParam(Map<String, Object> mapParam) throws Exception {
        return (int) dao.findForObject("PhoneUserDao.queryMoneyCountByParam",mapParam);
    }

    @Override
    public Result getSalesList(String sid) {
        Result result = new Result();
        if (StringUtil.isNullOrEmpty(sid)) {
            result.setReturn_code(0);
            result.setReturn_info("接口参数错误：sid为空!");
            return result;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("sid", sid);
        try {
            List<Map<String,Object>> list = (List<Map<String,Object>>) dao.findForList("PhoneUserDao.getSalesList", params);
            if (list == null || list.isEmpty()) {
                result.setReturn_code(0);
                result.setReturn_info("接口参数错误：sid无效!");
                return result;
            }

            result.setReturn_code(1);
            result.setReturn_info("");
            result.setReturn_data(list);

        } catch (Exception e) {
            result.setReturn_code(0);
            result.setReturn_info("获取导购员列表异常!");
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public BaseUser getBaseUserByPhone(String phone) throws Exception {
        return (BaseUser) dao.findForObject("PhoneUserDao.getBaseUserByPhone", phone);
    }



    /**
     *
     */
    @Override
    public Brand getBrandOnlyByCode(String brand_code) throws Exception
    {
        return (Brand) dao.findForObject("PhoneUserDao.getBrandOnlyByCode", brand_code);
    }


    /**
     * 添加
     */
    @Override
    public void insertRgGift(RgGift rgGift) throws Exception
    {
        dao.save("PhoneUserDao.insertRgGift", rgGift);
    }


    /**
     * 添加
     */
    @Override
    public void insertPayScene(PayScene payScene) throws Exception
    {
        dao.save("PhoneUserDao.insertPayScene", payScene);
    }

    @Override
    public String getSysUserIdByOpenId(String open_id) throws Exception{
        return (String) dao.findForObject("PhoneUserDao.getSysUserIdByOpenId", open_id);
    }

    @Override
    public Double getCoin_90ByTUserId(String id) throws Exception{
        return (Double) dao.findForObject("PhoneUserDao.getCoin_90ByTUserId", id);
    }

    @Override
    public OrderMain getOrderMainByCode(String code) throws Exception{

        return (OrderMain)dao.findForObject("PhoneUserDao.getOrderMainByCode",code);
    }

    @Override
    public void updateWhereOrderMainByParam(Map<String, Object> mapPd) throws Exception{


    }

    /**
     * 获取
     */
    public BaseUser getOneBaseUser(Map<String,Object> pd) throws Exception
    {
        return (BaseUser) dao.findForObject("BaseUserDao.getOneBaseUser", pd);
    }

    /**
     * 通过银联银行卡获取用户信息
     */
    public BaseUser getOneBaseUserByUnionpay(String unionpayid) throws Exception
    {
        Map<String,Object> map=new HashMap<>();
        map.put("unionpayid",unionpayid);
        return (BaseUser) dao.findForObject("BaseUserDao.getOneBaseUserByUnionpay", map);
    }

    /**
     * 查询收银员积分明细
     * @param pd
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<PhoneUserIntegral> queryIntegralListByUser(PageData pd) throws Exception
    {
        return (List<PhoneUserIntegral>) dao.findForList("PhoneUserDao.queryIntegralListByUser",pd);
    }




    @Override
    public Map<String,Object> qtGetOrderDetailByOrderCode(String code){
        Map<String,Object> map = new HashMap<>();
        map.put("return_code",0);
        map.put("return_info","订单查询失败！");
        try{
            //查询主单信息\
            OrderBackMain orderBackMain = (OrderBackMain)dao.findForObject("PhoneUserDao.qtGetOrderMainByCode",code);

            //查询子单信息
            List<OrderBackDetail> orderBackDetailList = (List<OrderBackDetail>) dao.findForList("PhoneUserDao.qtGetOrderDetailListByCode",code);
            if(orderBackMain!= null && orderBackDetailList != null)
            {
                String goodsIds = "";
                for(OrderBackDetail orderBackDetail : orderBackDetailList)
                {
                    goodsIds+=orderBackDetail.getGoodsCode() +",";
                }

                //调借口查询商品信息
                try{
                    String requestUrl = ConfigUtil.get("ERP_URL")+"/interface/queryGoodsInfoByGoodsIds.action";
                    Map<String, String> Param = new HashMap();
                    Param.put("goodsIds",goodsIds);
                    String x= URLConectionUtil.httpURLConnectionPostDiy(requestUrl,Param);
                    x = URLDecoder.decode(x, "utf-8");
                    List<OrderBackDetail> erpGoodsInfoList = JSON.parseArray(x,OrderBackDetail.class);

                    //双重循环对比
                    for(OrderBackDetail orderBackDetail : orderBackDetailList)
                    {
                        for(OrderBackDetail erpGoods : erpGoodsInfoList)
                        {
                            if(orderBackDetail.getGoodsCode()!=null
                                    && erpGoods.getGoodsCode() !=null
                                    && orderBackDetail.getGoodsCode().equals(erpGoods.getGoodsCode()))
                            {
                                orderBackDetail.setGoodsName(
                                        erpGoods.getGoodsName() != null ?erpGoods.getGoodsName() : "" );
                                orderBackDetail.setGoodsProperty(
                                        erpGoods.getGoodsProperty() != null ?erpGoods.getGoodsProperty() : "");
                                orderBackDetail.setGoodsPrice(
                                        erpGoods.getGoodsPrice() != null ?erpGoods.getGoodsPrice() : "0");
                            }
                        }
                    }
                }catch(Exception e){

                }

                orderBackMain.setDetailList(orderBackDetailList);
            }
            map.put("return_code",1);//查询成功
            map.put("return_info","订单查询成功");
            map.put("return_data",orderBackMain);
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }



    @Override
    public Map<String,Object> qtBackOrderDetailByDetailIds(String detailIds){
        Map<String,Object> map = new HashMap<>();
        map.put("return_code",0);
        map.put("return_info","订单退款失败！");
        try{
            //处理退款
            String sqlIds = StringUtil.formatSqlIn(detailIds);
            //查询子单集合
            List<OrderBackDetail> orderBackDetailList = (List<OrderBackDetail>) dao.findForList("PhoneUserDao.qtGetOrderDetailListByDetailIds",sqlIds);

           if(orderBackDetailList !=null && orderBackDetailList.size()>0)
            {
                String paramId = orderBackDetailList.get(0).getDetailId();
                int balanceType = orderBackDetailList.get(0).getBalanceType();

                //混合支付不可退款  直接跳出方法
                if(balanceType==3)
                {
                    map.put("return_code",0);
                    map.put("return_info","混合支付不可退款！");
                    return map;
                }
                //erp 修改库存数据集
                List<PwxGoodsStockHistory> listUpCount = new ArrayList<>();
                //修改库存借口总对象
                PwxGoodsStockHistory pwxGoodsStockHistoryOb=new PwxGoodsStockHistory();
                //相关参数查询
                String orderMainId = (String)dao.findForObject("sysUserDao.getOrderMainIdByDetailId",paramId);
                TorderMain torderMain = torderMainDao.getById(TorderMain.class,orderMainId);
                String shop_id = torderMain.getShopId();
                Shop shop = (Shop)dao.findForObject("sysUserDao.getShopBySid",shop_id);

                TBaseUser tSysUser = tbaseUserDao.getById(TBaseUser.class,torderMain.getBuyUserId());
                String open_id =tSysUser.getOpenId();
                for(OrderBackDetail orderBackDetail : orderBackDetailList)
                {
                    boolean flag = true;
                    String detailId =orderBackDetail.getDetailId();
                    TOrderDetail orderDetail = tOrderDetailDao.getById(TOrderDetail.class,detailId);
                    //处理九零券
                    double double_total = 0;
                    if(orderDetail.getBalance_90()>0)
                    {
                        double_total=orderDetail.getBalance_90();
                    }else if(orderDetail.getBalance_experience_90()>0)
                    {
                        double_total=orderDetail.getBalance_experience_90();
                    }else if(orderDetail.getBalance_shopping_90()>0)
                    {
                        double_total=orderDetail.getBalance_shopping_90();
                    }
                    Map<String,String> map1=new HashMap<>();
                    JSONObject jSONObject1 = new JSONObject();
                    jSONObject1.put("open_id",open_id);
                    jSONObject1.put("balance_90", MathUtil.mul(Double.valueOf(double_total),100.0).intValue());
                    jSONObject1.put("shop_code",shop.getSid());
                    jSONObject1.put("order_code",orderDetail.getId());
                    jSONObject1.put("brand_code",shop.getBrand_code());
                    jSONObject1.put("user_code",torderMain.getBuyUserId());
                    jSONObject1.put("type", "1");
                    jSONObject1.put("source", "5");//退款
                    jSONObject1.put("source_msg", "用户退款");
                    jSONObject1.put("state",2);
                    jSONObject1.put("commission","0");
                    jSONObject1.put("orderState","1");
                    jSONObject1.put("orderTotal","0");
                    jSONObject1.put("tradeType",torderMain.getPaymentRoute());
                    jSONObject1.put("ticketType",balanceType);
                    map1.put("json", jSONObject1.toString());
                    System.out.print(jSONObject1.toString());
                    String requestUrl = Global.getConfig("QT_Url")+"api/balance/operUserBalance90.ac";
                    String x = URLConectionUtil.httpURLConnectionPostDiy(requestUrl, map1);
                    x = URLDecoder.decode(x, "utf-8");
                    if(x==null||x.trim().equals("")){
                        map.put("return_code",0);
                        map.put("return_info","订单退款失败！");
                        flag = false;
                    }else {
                        flag = true;
                    }
                    //退款
                    if(flag)
                    {
                        //加九零呗
                        Map<String,String> map90=new HashMap<>();
                        JSONObject jSONObject90 = new JSONObject();
                        jSONObject90.put("userId",torderMain.getBuyUserId());
                        jSONObject90.put("state","1");
                        jSONObject90.put("orderNum",orderDetail.getId());
                        jSONObject90.put("source","4");
                        jSONObject90.put("amount",orderBackDetail.getServicePay());
                        map90.put("json", jSONObject90.toString());
                        System.out.print(jSONObject90.toString());
                        String cointUrl = Global.getConfig("QT_Url")+"api/balance/operUserCoin90_.ac";//调用加赠送九零呗
                        String coinX = URLConectionUtil.httpURLConnectionPostDiy(cointUrl, map90);
                        coinX = URLDecoder.decode(coinX, "utf-8");
                        if(!StringUtil.isNullOrEmpty(coinX) )
                        {
                            Map<String, Object> map2 = JSON.parseObject(coinX, Map.class);
                            if(map2!=null && map2.get("flag").equals("0"))
                            {
                                flag = true;
                            }else{
                                flag = false;
                            }
                        }else{
                            flag = false;
                        }
                    }
                    if(flag){
                        //调用接口修改库存
                        PwxGoodsStockHistory pwxGoodsStockHistory =(PwxGoodsStockHistory)dao.findForObject("sysUserDao.getHistoryListUpCountByDetailId",detailId);
                        if(pwxGoodsStockHistory != null )
                        {
                            listUpCount.add(pwxGoodsStockHistory);
                        }
                    }
                    if(flag) {
                        //更改订单状态
                        orderDetail.setState((short) 6);//已退款
                        orderDetail.setBackTime(new Date());
                        tOrderDetailDao.update(orderDetail);
                    }
                }
                //批量处理erp库存
                if(listUpCount.size() > 0){
                    //处理库存
                    String upCountUrl = ConfigUtil.get("ERP_URL")+"/interface/changeStockForWhareHouse.action";
                    pwxGoodsStockHistoryOb.setParmlist(listUpCount);
                    String jsonUpCounttext=JSON.toJSONString(pwxGoodsStockHistoryOb);
                    Map<String, String> upCountParam = new HashMap();
                    Map<String,Object> resUpCpuntMap=new HashMap<>();
                    upCountParam.put("parm",jsonUpCounttext);
                    String xup= URLConectionUtil.httpURLConnectionPostDiy(upCountUrl,upCountParam);
                    xup = URLDecoder.decode(xup, "utf-8");
                    if(xup==null||xup.trim().equals("")){
                        // 失败
                        map.put("return_code",0);
                        map.put("return_info","增加库存失败！");
                    }else {
                        resUpCpuntMap = JSON.parseObject(xup, Map.class);
                        if(resUpCpuntMap.get("sign")!=null&&resUpCpuntMap.get("sign").equals("true")){
                            map.put("return_code",1);//查询成功
                            map.put("return_info","订单退款成功");
                        }else{
                            map.put("return_code",0);
                            map.put("return_info","增加库存失败！");
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }


    @Override
    public void updateMainState(String detailIds)
    {
        try{
            String[] paramIdArr = detailIds.split(",");
            String paramId = paramIdArr[0];
            String orderMainId = (String)dao.findForObject("sysUserDao.getOrderMainIdByDetailId",paramId);
            TorderMain torderMain = torderMainDao.getById(TorderMain.class,orderMainId);
            Integer count = (Integer)dao.findForObject("sysUserDao.getOrderCountById",orderMainId);

            if(count.intValue() == 0)//订单已全部退款
            {
                torderMain.setState(2);//已退款
                torderMain.setBackTime(new Date());
                torderMainDao.update(torderMain);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public Map<String,Object> qtGetSumInfoByUserCode(String userCode, String shopId){
        Map<String,Object> map = new HashMap<>();
        map.put("return_code",0);
        map.put("return_info","统计信息查询失败！");        
        try{
            //参数设置
            Map<String,String> param = new HashMap<String,String>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String startDate = sdf.format(date) +" 00:00:00";
            String endDate = sdf.format(date) +" 23:59:59";
            param.put("startDate",startDate);
            param.put("endDate",endDate);

            //根据userCode 判断 是否是全部
            if("-1".equals(userCode))//全部
            {
                param.put("shopId",shopId);
            }else {
                //判断账号类型
                param.put("userCode", userCode);
            }

            //查询总信息
            List<SectionSum> sectionSumList = (List<SectionSum>) dao.findForList("PhoneUserDao.qtGetSumInfoByUserCode",param);
            //查询退款信息
            List<SectionSum> sectionSumListBack = (List<SectionSum>) dao.findForList("PhoneUserDao.qtGetBackSumInfoByUserCode",param);
            //查询发券数量
            List<SectionSum> sectionSumListGive = (List<SectionSum>) dao.findForList("PhoneUserDao.qtGetGiveSumInfoByUserCode",param);
            if(sectionSumList!=null && sectionSumList.size() >0)
            {
                for(SectionSum sectionSumMain : sectionSumList)
                {
                    if(sectionSumListBack != null && sectionSumListBack.size()>0)
                    {
                        for(SectionSum sectionSumBack : sectionSumListBack)
                        {
                            if(sectionSumBack.getUserCode() != null && sectionSumMain.getUserCode().equals(sectionSumBack.getUserCode()))
                            {
                                sectionSumMain.setBackCount(sectionSumBack.getBackCount());
                                sectionSumMain.setBackCoupons(sectionSumBack.getBackCoupons());
                                sectionSumMain.setBackMoney(sectionSumBack.getBackMoney());
                                break;
                            }
                        }
                    }
                    if(sectionSumListGive!= null && sectionSumListGive.size()>0)
                    {
                        for(SectionSum sectionSumGive : sectionSumListGive){
                            if(sectionSumGive.getUserCode() != null && sectionSumMain.getUserCode().equals(sectionSumGive.getUserCode()))
                            {
                                sectionSumMain.setGiveCount(sectionSumGive.getGiveCount());
                                sectionSumMain.setGiveCoupons(sectionSumGive.getGiveCoupons());
                                break;
                            }
                        }
                    }
                }
            }else{
                SectionSum sectionSumEmpty= new SectionSum();
                sectionSumList.add(sectionSumEmpty);
            }
            map.put("return_code",1);//查询成功
            map.put("return_info","统计信息查询成功");
            map.put("return_data",sectionSumList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }
}
