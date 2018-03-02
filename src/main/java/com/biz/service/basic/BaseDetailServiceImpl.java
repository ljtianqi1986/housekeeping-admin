package com.biz.service.basic;


import com.alibaba.fastjson.JSON;
import com.biz.model.Hmodel.TbaseDetail;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.OrderMainUnion;
import com.biz.model.Pmodel.basic.PbaseDetail;
import com.biz.model.Pmodel.basic.PyestodayReport;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Paging;
import com.framework.model.Params;
import com.framework.utils.ConfigUtil;
import com.framework.utils.StringUtil;
import com.framework.utils.Tools;
import com.framework.utils.URLConectionUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by liujiajia on 2017/1/6.
 */
@Service("baseDetailService")
public class BaseDetailServiceImpl extends BaseServiceImpl<TbaseDetail> implements BaseDetailServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Autowired
    private BaseDaoI<TbaseDetail> detailDao;

    @Override
    public Paging findBaseDetailGrid(Params sqlParams) throws Exception {
        return dao.findForPaging("baseDetailMapper.findPage",sqlParams,"baseDetailMapper.count",sqlParams);
    }

    @Override
    public List<PbaseDetail> findBaseDetailList(Map<String,Object> params)throws  Exception{
        return (List<PbaseDetail>)dao.findForList("baseDetailMapper.findList",params);
    }
    @Override
    public Paging findBaseDetailAutoGrid(Params sqlParams) throws Exception {
        return dao.findForPaging("baseDetailMapper.findAutoPage",sqlParams,"baseDetailMapper.countAuto",sqlParams);
    }

    @Override
    public Paging showCoinStatistics(Params sqlParams) throws Exception {
        return dao.findForPaging("baseDetailMapper.findCoinPage",sqlParams,"baseDetailMapper.countCoin",sqlParams);
    }

    @Override
    public Map<String, String> loadJLBInfo(Map<String, String> map) throws Exception {
        return (Map<String, String>) dao.findForObject("baseDetailMapper.loadJLBInfo",map);
    }

    @Override
    public List<OrderMainUnion> findBaseDetailAutoList(Map<String,Object> params)throws Exception{
        return (List<OrderMainUnion>)dao.findForList("baseDetailMapper.findAutoList",params);
    }
    @Override
    public PbaseDetail findDetailAuto(String id) throws Exception {
        return (PbaseDetail) dao.findForObject("baseDetailMapper.findDetailAuto",id);
    }

    @Override
    public Paging showBaseDetailStatistics(Params sqlParams) throws Exception {
        return dao.findForPaging("baseDetailMapper.findStatisticsPage",sqlParams,"baseDetailMapper.countStatistics",sqlParams);
    }

    @Override
    public Map<String, Object> queryGiftData(User user, String type, String code, String dataType, String startTime, String endTime,String source) throws Exception {
        Map<String,Object> map=new  HashMap<String, Object>();
        map.put("beginTime",startTime);
        map.put("endTime",endTime);
        map.put("type",type);
        map.put("code",code);
        map.put("dataType",dataType);
        map.put("source",source);
        if(2==user.getIdentity())
        {
            map.put("agent",user.getIdentity_code());
        }
        if(3==user.getIdentity())
        {
            map.put("brand",user.getIdentity_code());
        }
        List<Map<String,Object>> list= (List<Map<String, Object>>) dao.findForList("baseDetailMapper.queryGiftData",map);
        list=getFullDayData(list,startTime,endTime,dataType);
        double maxcount=0;
        double amountAll=0;
        for(int j=0;j<list.size();j++)
        {
            BigDecimal account= (BigDecimal) list.get(j).get("count");
            if(maxcount<account.doubleValue())
            {
                maxcount=account.doubleValue();
            }
            amountAll+=account.doubleValue();
        }
        HashMap<String, Object> res=new HashMap<String, Object>();
        res.put("amountAll",amountAll);
        res.put("maxCount",maxcount);
        res.put("size",(int)list.size()/7);
        res.put("list",list);
        if("1".equals(dataType))
        {
            res.put("dataSet","发券数");
        }
        else
        {
            res.put("dataSet","发券费用");
        }
        return res;
    }

    @Override
    public Map<String, String> loadZDInfo(Map<String, String> map) throws Exception {

        return (Map<String, String>) dao.findForObject("baseDetailMapper.loadZDInfo",map);
    }

    @Override
    public Map<String, String> loadSDInfo(Map<String, String> map) throws Exception {
        Map<String, String> res=new HashMap<>();
        Map<String, String> resff= (Map<String, String>) dao.findForObject("baseDetailMapper.loadSDInfoFF",map);
        Map<String, String> rescx= (Map<String, String>) dao.findForObject("baseDetailMapper.loadSDInfoCX",map);

        res.put("point",resff.get("point"));
        res.put("pointJL",resff.get("pointJL"));
        res.put("pointLG",resff.get("pointLG"));
        res.put("pointTY",resff.get("pointTY"));
        res.put("point90now",resff.get("point90Now"));
        res.put("count",resff.get("count"));
        res.put("pointc",rescx.get("point"));
        res.put("countc",rescx.get("count"));
        res.put("cxpointJL",rescx.get("pointJL"));
        res.put("cxpointLG",rescx.get("pointLG"));
        res.put("cxpointTY",rescx.get("pointTY"));
        return res;
    }

    @Override
    public Map<String, String> loadSTKInfo(Map<String, String> map) throws Exception {
        return (Map<String, String>) dao.findForObject("baseDetailMapper.loadSTKInfo",map);
    }



    private List<Map<String,Object>> getFullDayData(List<Map<String, Object>> list, String startTime, String endTime,String dataType) throws ParseException {

            List<Map<String,Object>> listRes=new ArrayList<>();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            Date dateBegin=sdf.parse(startTime);
            Date dateEnd=sdf.parse(endTime);
            Date dateNow=dateBegin;
            for(int i=0;dateNow.getTime()<=dateEnd.getTime();i++)
            {
                Map<String,Object> map=new HashMap<>();
                String dateNowString=sdf.format(dateNow);
                map.put("date",dateNow.getTime());
                Calendar c = Calendar.getInstance();
                c.setTime(dateNow);
                c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天
                dateNow = c.getTime();
                for(int j=0;j<list.size();j++)
                {
                    double count=0.0;
                    if("1".equals(dataType))
                    {
                        count= Double.valueOf(list.get(j).get("count")+"").doubleValue();
                    }
                    else
                    {
                        count= Double.valueOf(list.get(j).get("point90")+"").doubleValue();
                    }
                    if(dateNowString.equals(list.get(j).get("date")))
                    {
                        map.put("count",BigDecimal.valueOf(count));
                        break;
                    }
                    else
                    {
                        map.put("count", BigDecimal.valueOf(0.0));
                    }
                }
                if(list.size()==0)
                { map.put("count",BigDecimal.valueOf(0.0));}
                listRes.add(map);
            }
            return listRes;
         }


    @Override
    public Map<String, Object> deBase90Detail(String id) throws Exception {
        Map<String,Object> r_map=new HashMap<>();
        if(Tools.isEmpty(id)){
            r_map.put("success",false);
            r_map.put("msg", "id空");
            return r_map;
        }
        Map<String,Object> select_map1=new HashMap<String,Object>();
        select_map1.put("id",id);
        //查询记录
        PbaseDetail select_90=(PbaseDetail)dao.findForObject("baseDetailMapper.getBase90Detail",select_map1);
        //订单是否存在
        if(select_90==null){
            r_map.put("success",false);
            r_map.put("msg", "未找到记录");
            return r_map;
        }
        //记录状态，是否已经撤销
        if(select_90.getIsdel().equals("1")){
            r_map.put("success",false);
            r_map.put("msg", "本次交易已经撤销");
            return r_map;
        }

        int order_point90=Integer.parseInt(select_90.getPoint90());//分,记录当时送的券
        if(Tools.isEmpty(select_90.getOpenId().trim())){
            r_map.put("success",false);
            r_map.put("msg", "用户不存在");
            return r_map;
        }

        //查询用户
        Map<String,Object> select_map2=new HashMap<String,Object>();
        select_map2.put("open_id",select_90.getOpenId().trim());
        Map<String,Object> userMap=(Map<String,Object>)dao.findForObject("baseDetailMapper.getUserBalanceCoin",select_map2);
        if(userMap==null){
            r_map.put("success",false);
            r_map.put("msg", "用户未找到");
            return r_map;
        }

        //查询赠送贝记录
        Map<String,Object> select_map3=new HashMap<String,Object>();
        select_map2.put("orderNum", select_90.getSourceId().trim());
        Map<String,Object> orderCoin=(Map<String,Object>)dao.findForObject("baseDetailMapper.getUserBase90DetailCoin",select_map2);
        double order_coin=0.0;//当前记录送的90贝
        if(orderCoin!=null){
            order_coin=Double.parseDouble(orderCoin.get("amount").toString());
        }

        String userid=userMap.get("id").toString();//当前用户id
        int balance_90=Integer.parseInt(userMap.get("balance_90").toString());//分,当前用户剩余——90券
        int balance_shop_90=Integer.parseInt(userMap.get("balance_shopping_90").toString());//分,当前用户剩余——购物券
        double giveAmount=Double.parseDouble(userMap.get("giveAmount").toString());//元,当前用户剩余——贝

        //验证用户券是否足够
        if(select_90.getTicketType()==0){
            if(order_point90>balance_90){
                r_map.put("success",false);
                r_map.put("msg", "用户当前90券不足");
                return r_map;
            }
        }else{
            if(order_point90>balance_shop_90){
                r_map.put("success",false);
                r_map.put("msg", "用户当前购物券不足");
                return r_map;
            }
        }

        //验证用户贝是否足够
        if(order_coin>giveAmount){
            r_map.put("success",false);
            r_map.put("msg", "用户当前90贝不足");
            return r_map;
        }
        //接口扣券
        JSONObject jSONObject_q = new JSONObject();
        jSONObject_q.put("order_code",select_90.getId());
        if(select_90.getTicketType()==0){
            jSONObject_q.put("source","9");//9 手动发券撤回
        }else{
            jSONObject_q.put("source","10");//
        }
        jSONObject_q.put("state", "1");
        jSONObject_q.put("balance_90", order_point90);
        jSONObject_q.put("open_id", select_90.getOpenId());
        jSONObject_q.put("ticketType", select_90.getTicketType());
        boolean j = user_Balance90(jSONObject_q);

        //接口扣贝
        JSONObject jSONObject90 = new JSONObject();
        jSONObject90.put("userId", userid);
        jSONObject90.put("state", "2");
        jSONObject90.put("orderNum", select_90.getSourceId().trim());
        jSONObject90.put("source", "7");
        jSONObject90.put("amount", order_coin);
        boolean b=true;
        if(order_coin>0){
            b=user_90Coin(jSONObject90);
        }


        if(!j){
            r_map.put("success",false);
            r_map.put("msg", "减券失败");
            return r_map;
        }
        if(!b){
            r_map.put("success",false);
            r_map.put("msg", "减贝失败");
            return r_map;
        }

        //修改订单状态
        int up=(Integer)dao.update("baseDetailMapper.updateBase90Detail",select_map1);
        if(up>0){
            r_map.put("success",true);
            r_map.put("msg", "撤销成功");
        }else{
            r_map.put("success",false);
            r_map.put("msg", "修改状态失败");
        }
        return r_map;

    }

    @Override
    public Map<String, Object> loadUseInfo(Map<String, Object> map) throws Exception {

        Map<String,Object>res= (Map<String,Object>) dao.findForObject("CouponUse.loadTJInfo",map);
        Map<String,Object>resPerson= (Map<String,Object>) dao.findForObject("CouponUse.loadTJUseNum",map);
        res.put("useCount",resPerson.get("useCount"));
        return res;
    }

    @Override
    public List<Map<String, Object>> getOfflineCardTypeForSelect() throws Exception {
        return (List<Map<String, Object>>)dao.findForList("baseDetailMapper.getOfflineCardTypeForSelect",null);
    }

    @Override
    public PyestodayReport getYesTodayData(String dateString) throws Exception {
        PyestodayReport report=new PyestodayReport();
        //用券和发券统计
        PyestodayReport point= (PyestodayReport) dao.findForObject("baseDetailMapper.getPoint90DataByDate",dateString);
        //服务费
        PyestodayReport service=(PyestodayReport) dao.findForObject("baseDetailMapper.getServiceDataByDate",dateString);
        //90券充值
        PyestodayReport coin=(PyestodayReport) dao.findForObject("baseDetailMapper.getServiceCoinByDate",dateString);


        String dateStringYestoday= laterDate(1,dateString);
        Map<String,String> parm=new HashMap<>();
        parm.put("dateStringYestoday",dateStringYestoday);
        parm.put("dateString",dateString);
        //人员信息基础
       PyestodayReport userInfo=(PyestodayReport) dao.findForObject("baseDetailMapper.getUserInfoByDate",parm);
        //人员活跃信息
       PyestodayReport userActiveInfo=(PyestodayReport) dao.findForObject("baseDetailMapper.getUserActiveInfoByDate",parm);
        //商户门店信息
        PyestodayReport brandShopInfo=(PyestodayReport) dao.findForObject("baseDetailMapper.getBrandShopInfoByDate",parm);
        //总计久零券贝统计
        PyestodayReport staticsAll=(PyestodayReport) dao.findForObject("baseDetailMapper.getStaticsAllInfoByDate",parm);


        report=point;

        report.setServicePay(service.getServicePay());
        report.setCoinPay(service.getCoinPay());

        report.setCoin90Recharge(coin.getCoin90Recharge());
        report.setCoin90Give(coin.getCoin90Give());
        report.setCoin90RechargeCount(coin.getCoin90RechargeCount());
        report.setCoin90GiveCount(coin.getCoin90GiveCount());

        report.setUserToday(userInfo.getUserToday());
        report.setUserAll(userInfo.getUserAll());
        report.setUserYestoday(userInfo.getUserYestoday());
        report.setUserCancal(userInfo.getUserCancal());

        report.setUserActiveVoucher(userActiveInfo.getUserActiveVoucher());
        report.setUserActiveMall(userActiveInfo.getUserActiveMall());
        report.setUserActiveRecharge(userActiveInfo.getUserActiveRecharge());

        report.setBrandActive(brandShopInfo.getBrandActive());
        report.setBrandAll(brandShopInfo.getBrandAll());
        report.setBrandNew(brandShopInfo.getBrandNew());
        report.setShopActive(brandShopInfo.getShopActive());
        report.setShopNew(brandShopInfo.getShopNew());
        report.setShopAll(brandShopInfo.getShopAll());

        report.setCoin90Now(staticsAll.getCoin90Now());
        report.setCoin90Used(staticsAll.getCoin90Used());
        report.setCoin90RechargeNow(staticsAll.getCoin90RechargeNow());
        report.setCoin90RechargeUsed(staticsAll.getCoin90RechargeUsed());

        report.setPoint90Now(staticsAll.getPoint90Now());
        report.setPoint90AllOffline(staticsAll.getPoint90AllOffline());
        report.setPoint90AllOnline(staticsAll.getPoint90AllOnline());
        return report;
    }

    /**
     * 券接口
     *
     * @return 0成功 1失败 3重
     */
    private boolean user_Balance90(JSONObject jSONObject1) {
        int zt=1;
        Map<String, String> hash1 = new HashMap<String, String>();
        hash1.put("json", jSONObject1.toString());
        Map<String, Object> r_Interface = APIInterface(hash1, "api/balance/operUserBalance90.ac");
        if (r_Interface != null) {
            zt=Integer.parseInt(r_Interface.get("flag").toString());
        }
        if(zt==0 || zt==3){
            return true;
        }else{
            return false;
        }
    }

    /**
     *  贝接口
     *
     * @return 0成功 1失败 3重
     */
    private boolean user_90Coin(JSONObject jSONObject1) {
        int zt=1;
        Map<String, String> hash1 = new HashMap<String, String>();
        hash1.put("json", jSONObject1.toString());
        Map<String, Object> r_Interface = APIInterface(hash1, "api/balance/operUserCoin90.ac");
        if (r_Interface != null) {
            zt=Integer.parseInt(r_Interface.get("flag").toString());
        }
        if(zt==0 || zt==3){
            return true;
        }else{
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
     * 当前日期 指定天数之前的日期
     * @param days
     * @return
     */
    public  String laterDate(int days,String dateBegin) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date temp_date = null;
        try {
            Date d =format.parse(dateBegin);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.DATE, -days);
            temp_date = c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return format.format(temp_date);
    }


    @Override
    public Paging findFirstConcernGrid(Params sqlParams) throws Exception {
        return dao.findForPagings("baseDetailMapper.findFirstConcernPage",sqlParams,"baseDetailMapper.countFirstConcern",sqlParams);
    }

    @Override
    public Paging findCharge90CoinGrid(Params sqlParams) throws Exception {
        return dao.findForPagings("baseDetailMapper.findCharge90CoinPage",sqlParams,"baseDetailMapper.countCharge90Coin",sqlParams);
    }

    @Override
    public Paging findRefundCouponsGrid(Params sqlParams) throws Exception {
        return dao.findForPagings("baseDetailMapper.findRefundCouponsPage",sqlParams,"baseDetailMapper.countRefundCoupons",sqlParams);
    }

    @Override
    public Map<String, String> loadFCInfo(Map<String, String> map) throws Exception {
        return (Map<String, String>) dao.findForObject("baseDetailMapper.loadFCInfo",map);
    }

    @Override
    public Map<String, String> loadCCInfo(Map<String, String> map) throws Exception {
        return (Map<String, String>) dao.findForObject("baseDetailMapper.loadCCInfo",map);
    }

    @Override
    public Map<String, String> loadRCInfo(Map<String, String> map) throws Exception {
        return (Map<String, String>) dao.findForObject("baseDetailMapper.loadRCInfo",map);
    }

    @Override
    public List<PbaseDetail> findFirstConcernList(Map<String, Object> params) throws Exception {
        return (List<PbaseDetail>)dao.findForList("baseDetailMapper.findFirstConcernList",params);
    }

    @Override
    public List<PbaseDetail> findCharge90CoinList(Map<String, Object> params) throws Exception {
        return (List<PbaseDetail>)dao.findForList("baseDetailMapper.findCharge90CoinList",params);
    }

    @Override
    public List<PbaseDetail> findRefundCouponsList(Map<String, Object> params) throws Exception {
        return (List<PbaseDetail>)dao.findForList("baseDetailMapper.findRefundCouponsList",params);
    }

    @Override
    public PyestodayReport getDataByDate(String date) throws Exception {
        PyestodayReport report = new PyestodayReport();

        Map<String, Object> params = new HashMap<>();
        params.put("date", date);

        Map<String,Object> resultMap = (Map<String, Object>) dao.findForObject("baseDetailMapper.getDataByDate",date);
        if (resultMap != null && !StringUtil.isNullOrEmpty(resultMap.get("json"))) {
            report = JSON.parseObject((String) resultMap.get("json"), PyestodayReport.class);
        }

        return report;
    }

    @Override
    public Paging findScanGrid(Params sqlParams) throws Exception {
        return dao.findForPaging("baseDetailMapper.findScanPage",sqlParams,"baseDetailMapper.countScan",sqlParams);

    }

    @Override
    public Map<String, String> loadScanInfo(Map<String, String> map) throws Exception {
        return (Map<String, String>) dao.findForObject("baseDetailMapper.loadScanInfo",map);
    }

    @Override
    public List<PbaseDetail> findScanCouponsList(Map<String, Object> params) throws Exception {
        return (List<PbaseDetail>) dao.findForList("baseDetailMapper.findScanCouponsList",params);
    }
}
