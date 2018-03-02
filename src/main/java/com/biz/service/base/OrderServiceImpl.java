package com.biz.service.base;

import com.alibaba.fastjson.JSON;
import com.biz.conf.Global;
import com.biz.model.Hmodel.TBaseUser;
import com.biz.model.Hmodel.TRefundLog;
import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Pmodel.PorderMain;
import com.biz.model.Pmodel.api.OrderDetail;
import com.biz.model.Pmodel.api.Shop;
import com.biz.model.Pmodel.basic.*;
import com.biz.model.Pmodel.pay17.Pay17BackPay;
import com.biz.model.Singleton.ZkNode;
import com.biz.service.api.WxTemplateServiceI;
import com.biz.service.api.WxUtilServiceI;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.utils.*;
import com.framework.utils.pay17.HttpRequestUtil;
import com.framework.utils.pay17.Pay17OrderRefund;
import com.framework.utils.weixin.WXRefund;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;

/*************************************************************************
 * create by lzq
 *
 * 文件名称 ：OrderServiceI.java 描述说明 ：
 *
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/

@Service("orderService")
public class OrderServiceImpl extends BaseServiceImpl<TorderMain> implements OrderServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Autowired
    private BaseDaoI baseDao;

    @Autowired
    private BaseDaoI<TRefundLog> refundLogDao;

    @Autowired
    private BaseDaoI<TorderMain> torderMainDao;

    @Resource(name = "wxTemplateService")
    private WxTemplateServiceI wxTemplateService;

    @Resource(name = "wxUtilService")
    private WxUtilServiceI wxUtilService;

    @Resource(name = "pay17Service")
    private Pay17ServiceI pay17Service;

    @Autowired
    private BaseDaoI<TBaseUser> tsysUserDao;


    @Override
    public Paging queryOrders(Map map)throws Exception{
        int limit = Integer.parseInt(map.get("limit").toString());
        int offset = Integer.parseInt(map.get("offset").toString());
        Pager<Map<String, Object>> pager = new Pager();
        pager.setParameters(map);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);

        Pager<Map<String, Object>> pager_back = (Pager<Map<String, Object>>) dao
                .findForPager1("OrderDao.findPageShopBuyOrder",
                        "OrderDao.countShopBuyOrder", pager);
        //封装成适合前台使用的格式
        Paging paging = new Paging<>();
        paging.setRows(pager_back.getExhibitDatas());
        paging.setTotal((long) pager_back.getRecordCount());
        return paging;
    }
    @Override
    public Paging queryOrderUnions(Map map)throws Exception{
        int limit = Integer.parseInt(map.get("limit").toString());
        int offset = Integer.parseInt(map.get("offset").toString());
        Pager<Map<String, Object>> pager = new Pager();
        pager.setParameters(map);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);

        Pager<Map<String, Object>> pager_back = (Pager<Map<String, Object>>) dao
                .findForPager1("OrderDao.findPageOrderUnion",
                        "OrderDao.countOrderUnion", pager);
        //封装成适合前台使用的格式
        Paging paging = new Paging<>();
        paging.setRows(pager_back.getExhibitDatas());
        paging.setTotal((long) pager_back.getRecordCount());
        return paging;
    }

    @Override
    public Map<String, Object> findOrderInfoById(String id,String detailId) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("detailId",detailId);
        param.put("sameCode", "");
        Map<String, Object> order = (Map<String, Object>) dao.findForObject(
                "OrderDao.findOrderInfoById", param);
        List<Map<String, Object>> listDetail = (List<Map<String, Object>>) dao
                .findForList("OrderDao.findOrderDetailById", param);
        param.put("detailId",listDetail.get(0).get("id"));
        Map<String, Object> listRefund = (Map<String, Object>) dao
                .findForObject("OrderDao.findOrderRefundById", param);
        Map<String, Object> orderSend = (Map<String, Object>) dao
                .findForObject("OrderDao.findOrderSendById", param);

        List<Map<String, Object>> quanDetail = (List<Map<String, Object>>) dao
                .findForList("OrderDao.findBalance90DetailById", param);

        order.put("listDetail", listDetail);
        order.put("orderSend", orderSend);
        order.put("orderRefund", listRefund);
        order.put("quanDetail", quanDetail);
        return order;
    }

    @Override
    public List<PorderMain> exportExcel_shopDeMain(
            Map<String, Map<String, Object>> maps) throws Exception {

        return (List<PorderMain>) dao.findForList(
                "OrderDao.exportExcel_shopDeMain", maps);
    }



    @Override
    public List<PorderSend> orderSend(String orderid) throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("orderid", orderid);

        // 0:待付款1:待发货2:待收货3:待评价4:完成5:退款中6:已退款7:待备货8:未备货退款
        return (List<PorderSend>) dao.findForList("OrderDao.orderSendList", map);
    }


    // 改变状态(所有main 下的子订单都改变状态)
    @Override
    public boolean updateBoolOrderState(String orderid, int state) throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("orderid", orderid);
        map.put("state", state);
        // 0:待付款1:待发货2:待收货3:待评价4:完成5:退款中6:已退款7:待备货8:未备货退款
        boolean result= (Integer) dao.update("OrderDao.updateOrderDetailState", map) > 0;
        if(result){
           // upateSendState(orderid);//调接口更新erp发货状态
            List<Map<String,String>> details= (List<Map<String,String>>) dao.findForList("OrderDao.findDetailsIdByOrderId",map);
            for(Map<String,String> detailId:details) {
                if("0".equals(detailId.get("goodsType")))
                {
                    upateSendState(detailId.get("id"));//调接口更新erp发货状态
                }

            }
        }
        return result;
    }
    public void upateSendState(String orderid) throws Exception {
        try{
            Map<String,String> parm=new HashMap<>();
            //String stockId = (String)dao.findForObject("OrderDao.getStockIdByOrderId",orderid);
            //parm.put("stockId",stockId);
            parm.put("relateCode",orderid);
            String url=ConfigUtil.get("ERP_URL")+"/interface/doOnlineDeliverGoods.action";
            String resString=URLConectionUtil.httpURLConnectionPostDiy(url,parm);
            if(!StringUtil.isNullOrEmpty(resString)&&!"失败".equals(resString))
            {
                resString = URLDecoder.decode(resString, "utf-8");
                resString= JSON.parseObject(resString,String.class);
                Map<String,Object> resMap=JSON.parseObject(resString,Map.class);
                if("false".equals(resMap.get("sign"))) {
                    throw new RuntimeException(String.valueOf(resMap.get("msg")));
                }
            }else{
                throw new RuntimeException("更新ERP库存发货状态失败，连接服务器失败！");
            }

        }catch (Exception e){
            throw new RuntimeException("更新ERP库存发货状态失败，异常错误！");
        }
    }



    /**
     * 保存物流配送单号和配送方式
     * orderid 订单order_main 的id ，改变所有子订单的物流配送
     * sendType 0物流配送 1自己配送
     * logisticsCompany 快递公司名称
     * sendCode 快递单号
     */
    @Override
    public boolean saveOrderSend(String orderid, int sendType,
                                 String logisticsCompany, String sendCode) throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("orderid", orderid);
        map.put("sendType", sendType);
        map.put("logisticsCompany", logisticsCompany);
        map.put("sendCode", sendCode);
        return (Integer) dao.update("OrderDao.updateOrderSend", map) > 0;
    }

    @Override
    public Paging queryRefundOrders(Map map) throws Exception {
        int limit = Integer.parseInt(map.get("limit").toString());
        int offset = Integer.parseInt(map.get("offset").toString());
        Pager<Map<String, Object>> pager = new Pager();
        pager.setParameters(map);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);

        Pager<Map<String, Object>> pager_back = (Pager<Map<String, Object>>) dao.findForPager1("OrderDao.findPageShopRefundOrder", "OrderDao.countShopRefundOrder", pager);
        //封装成适合前台使用的格式
        Paging paging = new Paging<>();
        paging.setRows(pager_back.getExhibitDatas());
        paging.setTotal((long) pager_back.getRecordCount());
        return paging;
    }

    /**
     * 去退款咯
     * @return
     */
    /*public Map<String,Object> submitToRefund(String id,String userId,String refundId,int paymentRoute) throws Exception {
        Map<String,Object> result_map = new HashMap<String,Object>();
        System.out.println("=======================>开始申请退款啦");
        System.out.println("paymentRoute="+paymentRoute);
        try {
            String transaction_id="";
            String out_trade_no="";
            String total_fee="";
            String refundMoney="";
            String openId="";
            String buyUserId="";
            String orderid="";
            Map<String,Object> map =null;
            Map<String,Object> map_ =null;

                Map<String,Object> orderInfo = findRefundInfoById(id);
                transaction_id = orderInfo.get("transaction_id").toString();
                out_trade_no = orderInfo.get("id").toString();
                total_fee = orderInfo.get("payTotal").toString();
                refundMoney = orderInfo.get("payTotal").toString();
                String venderId = orderInfo.get("venderId").toString();
                openId = orderInfo.get("open_id").toString();
                buyUserId = orderInfo.get("userId").toString();
                orderid = orderInfo.get("id").toString();
                BigDecimal bigDecimal1 = new BigDecimal(Double.valueOf(orderInfo.get("balance_90").toString()));
                bigDecimal1 = new BigDecimal(bigDecimal1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
                int balance_90 = bigDecimal1.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();

                BigDecimal bigDecimal2 = new BigDecimal(Double.valueOf(orderInfo.get("coinPayTotal").toString()));
                BigDecimal bigDecimal = new BigDecimal(Double.valueOf(total_fee));
                BigDecimal bigDecimal3 = bigDecimal.add(bigDecimal2);
                double coin_90 = bigDecimal3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

                String goodsType = orderInfo.get("goodsType").toString();
                map = updateIntegralAndExperience(venderId,orderid,openId,String.valueOf(balance_90),goodsType);
                System.out.println("coin_90====================>"+coin_90);
                int total_fee_ = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                map_ = updateUserCoin(buyUserId,id,coin_90);

            System.out.println("加券====================>"+map);
            System.out.println("加贝====================>"+map_);

            if(map!=null&&map_!=null
                    &&(map.get("flag").toString().equals("0")||map.get("flag").toString().equals("3"))
                    &&(map_.get("flag").toString().equals("0")||map_.get("flag").toString().equals("3"))) {

                refundCallBack(result_map, id, openId, orderid, refundId, userId);//退款
                result_map.put("status", "0");
            }else{
                result_map.put("status", map.get("flag"));
                result_map.put("errormsg",map.get("msg").toString());
            }
            //if(map!=null&&map_!=null
            //        &&(map.get("flag").toString().equals("0")||map.get("flag").toString().equals("3"))
            //        &&(map_.get("flag").toString().equals("0")||map_.get("flag").toString().equals("3"))) {
            //
            //    //paymentRoute付款途径：0.余额支付 1.银联支付 2.支付宝支付 3.微信支付 4.兴业银行微信支付,6.翼支付，7.offline线下支付
            //    if(paymentRoute==4){
            //        result_map =  splitWXRefund(transaction_id,out_trade_no,total_fee,refundMoney);
            //        System.out.println("银行返回============================>"+result_map.get("status"));
            //        System.out.println("银行返回============================>"+result_map.get("message"));
            //
            //        if (result_map.get("status").toString().equals("0")) {
            //            System.out.println("银行返回============================>"+result_map);
            //            if (result_map.get("return_code").toString().equals("0")) {
            //                 refundCallBack(result_map, id, openId, orderid, refundId, userId);//退款
            //            }else{
            //                result_map.put("status",result_map.get("err_code").toString());
            //                result_map.put("errormsg",result_map.get("err_msg").toString());
            //            }
            //        }else{
            //            result_map.put("status",result_map.get("status").toString());
            //            result_map.put("errormsg",result_map.get("message").toString());
            //        }
            //    }else if(paymentRoute==5){
            //        result_map = splitPay17Refund(transaction_id, out_trade_no, total_fee, refundMoney, refundId);
            //        System.out.println("pay17返回============================>"+result_map.get("status"));
            //        System.out.println("pay17返回============================>"+result_map.get("message"));
            //
            //            if (result_map.get("return_code").toString().equals("0")) {
            //                refundCallBack(result_map, id, openId, orderid, refundId, userId);//退款
            //                //发送文本消息
            //                wxTemplateService.send_kf_template(openId, "退款成功，退款金额："+ MathUtil.mul(new Double(total_fee), 0.01)+"元");
            //            }else{
            //                result_map.put("status",result_map.get("err_code").toString());
            //                result_map.put("errormsg",result_map.get("err_msg").toString());
            //            }
            //    }
            //
            //}else{
            //    result_map.put("status", map.get("flag"));
            //    result_map.put("errormsg",map.get("msg").toString());
            //}

        } catch (Exception e) {
            result_map.put("status","1");
            result_map.put("errormsg","退款异常啦");
        }

        return result_map;
    }*/


    //status :0 成功 1 失败
    @Override
    public Map<String,Object> updatesubmitToRefund(String id,String userId,String refundId,int paymentRoute,String remarks) throws Exception {
        Map<String,Object> result_map = new HashMap<String,Object>();
        System.out.println("=======================>开始申请退款啦");
        System.out.println("paymentRoute=" + paymentRoute);
        try {
            String total_fee="";
            String openId="";
            String buyUserId="";
            String orderid="";
            int goodsType=-1;//0:普通商品，1券类商品
            String orderCode="";//订单code
            Map<String,Object> map_q =null;
            Map<String,Object> map_b =null;
            Map<String,Object> map_m =null;//退现金

            Map<String,Object> orderInfo = findRefundInfoById(id);
            List<String> unRefundList= (List<String>) dao.findForList("OrderDao.findunRefundList",orderInfo);//判断是不是最后一个未退款的清单
            List<String> isSendOrderDetail= (List<String>) dao.findForList("OrderDao.findIsSendOrderDetailId",orderInfo);//判断还有没有未退款的订单或者已经发货了的订单

            total_fee = orderInfo.get("payTotal").toString();
            String venderId = orderInfo.get("venderId").toString();
            openId = orderInfo.get("open_id").toString();
            buyUserId = orderInfo.get("userId").toString();
            orderid = orderInfo.get("id").toString();
            orderCode=orderInfo.get("code").toString();
            goodsType=Integer.parseInt(orderInfo.get("goodsType").toString());
            BigDecimal bigDecimal1 = new BigDecimal(Double.valueOf(orderInfo.get("balance_90").toString()));
            BigDecimal bigDecimal90 = new BigDecimal(Double.valueOf(orderInfo.get("balance90").toString()));
            BigDecimal bigDecimals90 = new BigDecimal(Double.valueOf(orderInfo.get("balance_shopping_90").toString()));
            BigDecimal bigDecimale90 = new BigDecimal(Double.valueOf(orderInfo.get("balance_experience_90").toString()));

            bigDecimal1 = new BigDecimal(bigDecimal1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
            int balance_90 = bigDecimal1.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            int balance90 = bigDecimal90.setScale(0, BigDecimal.ROUND_HALF_UP).intValue()* 100;
            int balance_shopping_90 = bigDecimals90.setScale(0, BigDecimal.ROUND_HALF_UP).intValue()* 100;
            int balance_experience_90 = bigDecimale90.setScale(0, BigDecimal.ROUND_HALF_UP).intValue()* 100;
           if(balance90>0&&balance_shopping_90>0)
            {
                result_map.put("status","1");
                result_map.put("errormsg","混合支付不允许退款");
                return  result_map;
            }
            BigDecimal bigDecimal2 = new BigDecimal(Double.valueOf(orderInfo.get("coinPayTotal").toString()));

            BigDecimal bigDecimal4=new BigDecimal(Double.valueOf(orderInfo.get("money").toString()));

            if(unRefundList.size()<=0&&isSendOrderDetail.size()<=0)//当该笔退款订单为最后一个退款的子订单并且未发货的时候，退运费
            {
                bigDecimal2= new BigDecimal(Double.valueOf(orderInfo.get("coinPayTotal").toString())+Double.valueOf(orderInfo.get("freightTotal").toString()));
                bigDecimal4=new BigDecimal(Double.valueOf(orderInfo.get("money").toString())+Double.valueOf(orderInfo.get("freightTotal").toString()));
            }
            BigDecimal bigDecimal = new BigDecimal(Double.valueOf(total_fee));
            BigDecimal bigDecimal3 = bigDecimal.add(bigDecimal2);
            double coin_90 = bigDecimal3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            //退款金额
            double refundMoney=bigDecimal4.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            String status="1";
            String errormsg="退款异常啦";
            boolean pd_q=false;//券 true:成功，false：成功
            if(goodsType==1){//如果是联盟商品，进行退券
            PbaseDetail detail= (PbaseDetail) dao.findForObject("baseDetailMapper.getDetailIdByOrder",orderid);
                map_q = updateIntegralAndExperienceNew(venderId,detail.getId(),openId,Integer.valueOf(detail.getPoint90()),detail.getTicketType()+"","9","1");
                if(map_q!=null){
                    if(map_q.get("flag").toString().equals("0")||map_q.get("flag").toString().equals("3")){
                        pd_q=true;
                    }else{
                        status=map_q.get("flag").toString();
                        errormsg=map_q.get("msg").toString();
                    }
                }
            }else{
              if(balance90>0)//如果有消费久零券退久零券
              {
                  map_q = updateIntegralAndExperienceNew(venderId,orderid,openId,balance90,"0","5","2");
                  if(map_q!=null){
                      if(map_q.get("flag").toString().equals("0")||map_q.get("flag").toString().equals("3")){
                          pd_q=true;
                      }else{
                          status=map_q.get("flag").toString();
                          errormsg=map_q.get("msg").toString();
                      }
                  }
              }
                if(balance_shopping_90>0)//如果有消费购物券券退购物券
                {
                    map_q = updateIntegralAndExperienceNew(venderId,orderid,openId,balance_shopping_90,"1","5","2");
                    if(map_q!=null){
                        if(map_q.get("flag").toString().equals("0")||map_q.get("flag").toString().equals("3")){
                            pd_q=true;
                        }else{
                            status=map_q.get("flag").toString();
                            errormsg=map_q.get("msg").toString();
                        }
                    }
                }
                if(balance_experience_90>0)//如果有消费体验券退体验券
                {
                    map_q = updateIntegralAndExperienceNew(venderId,orderid,openId,balance_experience_90,"2","5","2");
                    if(map_q!=null){
                        if(map_q.get("flag").toString().equals("0")||map_q.get("flag").toString().equals("3")){
                            pd_q=true;
                        }else{
                            status=map_q.get("flag").toString();
                            errormsg=map_q.get("msg").toString();
                        }
                    }
                }
            }
            boolean pd_b=true;//贝 true:成功，false：成功
            if(refundMoney>0) {
                map_b = updateUserCoin(buyUserId, id, refundMoney);
                if (map_b != null) {
                    if (map_b.get("flag").toString().equals("0") || map_b.get("flag").toString().equals("3")) {
                        pd_b = true;
                    } else {
                        status = map_b.get("flag").toString();
                        errormsg = map_b.get("msg").toString();
                    }
                }
            }
            boolean pd_m=true;//现金 true:成功，false：成功
         /*   boolean pd_m=false;//现金 true:成功，false：成功
            map_m = updateRefundMoney(refundMoney,"1-"+orderCode);
            if(map_m!=null){
                if(map_m.get("flag").toString().equals("0")){
                    pd_m=true;
                }else{
                    status=map_m.get("flag").toString();
                    errormsg=map_m.get("msg").toString();
                }
            }*/


            if(pd_q&&pd_b&&pd_m){
                //refundCallBack(result_map, id, openId, orderid, refundId, userId);//退款

                //退库存
                boolean update_kc=update_lm_jl_Ck(goodsType,orderid);

                if(update_kc){
                //保存订单
                    if(updateStates(orderid,6,refundId,"4",userId,remarks)){
                        status="0";
                        errormsg="";
                    }else{
                        status="1";
                        errormsg="保存状态失败";
                    }
                }else {
                    status="1";
                    errormsg="库存增加失败";
                }
            }

            result_map.put("status",status);
            result_map.put("errormsg",errormsg);
            result_map.put("balance_90",balance_90);
            result_map.put("coin_90",coin_90);
        } catch (Exception e) {
            result_map.put("status","1");
            result_map.put("errormsg","退款异常啦");
        }

        return result_map;
    }

    public Map<String,Object> findRefundInfoById(String id) throws Exception{
        return (Map<String,Object>)dao.findForObject("OrderDao.findRefundInfoById",id);
    }
    @Override
    public Map<String,Object> findOrderMainInfo(String id) throws Exception{
        Map<String,Object> r_map=new HashMap<>();
        if(Tools.isEmpty(id)){
            r_map.put("id","");
            r_map.put("code","");
        }else {
            r_map = (Map<String, Object>) dao.findForObject("OrderDao.findOrderMainInfo", id);
            if(r_map==null){
                r_map.put("id","");
                r_map.put("code","");
            }
        }
        return r_map;
    }

    /**
     * 取消，加库存
     * @param goodsType 0:普通商品（联盟），1券类商品（久零）
     * @param detailId
     * @return
     */
    public boolean update_lm_jl_Ck(int goodsType,String detailId) throws Exception {
        String cityId="";
            try{
            cityId = Global.getLocalCity();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        Map<String, String> paramStockUp = new HashMap<>();
        paramStockUp.put("detailId", detailId);
        paramStockUp.put("cityId", cityId);

        PwxGoodsStockbak list = (PwxGoodsStockbak) dao.findForObject("OrderDao.getHistoryListUpStock", paramStockUp);
        // 11线上仓库退货加库存
        list.setState(11);

        boolean pd=false;

        if(list!=null){
            if(goodsType==1){
                //联盟商品
                Map<String,Object> lm_cont=new HashMap<>();
                lm_cont.put("stockId",list.getLmStockId());
                lm_cont.put("count", list.getChangeNumber());
                pd=(Integer)dao.update("OrderDao.addStoreCount",lm_cont)>0;
            }else if (goodsType==0){
                //久零商品
                String requestUrl = ConfigUtil.get("ERP_URL")+"/interface/changeStockForWhareHouse.action";


                //赋值
                PwxGoodsStockHistory fz=new PwxGoodsStockHistory();
                fz.setRelateCode(list.getDetailId());
                fz.setStockId(list.getStockId());
                fz.setChangeNumber(list.getChangeNumber());
                //fz.setWhareHouseId(list.getWhareHouseId());
                fz.setWhareHouseId(Global.getWharehoustId());
                //fz.setState(list.getState());
                fz.setState(9);
                if(list.getOrderState()==0||list.getOrderState()==1)
                {
                    fz.setOrderStatus(0);  //未发货
                }else
                {
                    fz.setOrderStatus(1);  //已发货
                }
                fz.setCity(list.getCity());
                fz.setRelateCode(list.getOrderId());
                List<PwxGoodsStockHistory> fz_list=new ArrayList<PwxGoodsStockHistory>();
                fz_list.add(fz);

                PwxGoodsStockHistory cz=new PwxGoodsStockHistory();
                cz.setParmlist(fz_list);

                String jsontext=JSON.toJSONString(cz);
                Map<String, String> Param = new HashMap();
                Map<String,Object> resMap=new HashMap<>();
                Param.put("parm",jsontext);
                String x= URLConectionUtil.httpURLConnectionPostDiy(requestUrl,Param);
                x = URLDecoder.decode(x, "utf-8");
                if(x==null||x.trim().equals("")){
                    //throw new  RuntimeException("加库存失败");
                }else {
                    resMap = JSON.parseObject(x, Map.class);
                    //sign：false  失败；true  成功 *  msg：错误信息
                    if(resMap.get("sign")!=null&&resMap.get("sign").equals("true")){
                        pd=true;
                    }else{
                        //throw new  RuntimeException("加库存失败");
                    }
                }
            }

        }

        return pd;


    }

    //    0操作成功 1操作失败2余额不足3重复调用
    public Map<String,Object> updateIntegralAndExperienceNew(String venderId,String order_code,String openId,int balance_90,String ticketType,String source,String state) throws Exception {
        String requestUrl= ConfigUtil.get("BALANCE_902")+"balance/operUserBalance90.ac";
        Map<String,Object> map = new HashMap<String,Object>();
        Map<String,String > map1 =new HashMap<>();
        Map<String,Object> map_r = new HashMap<String,Object>();
        String flag="1";
        String msg="";
        try
        {
            if(balance_90>0){
                map.put("brand_code",venderId);
                map.put("shop_code","");
                map.put("user_code","");
                map.put("order_code",order_code);
                map.put("open_id",openId);
                map.put("type",2);
                map.put("source",source);
                map.put("source_msg","微商城用户退款");
                map.put("balance_90",String.valueOf(balance_90));
                map.put("state",state);
                map.put("commission","0");
                map.put("tradeType","");
                map.put("orderState","1");
                map.put("ticketType",ticketType);
                map.put("orderTotal","0");
                map1.put("json", JSON.toJSONString(map));
                String res= URLConectionUtil.httpURLConnectionPostDiy(requestUrl, map1);
                map.clear();
                map=JSON.parseObject(res,Map.class);
                flag=map.get("flag").toString();
                msg=map.get("msg").toString();
            }else{
                flag="0";
            }
        } catch (Exception e)
        {
            flag="1";
            msg=e.getMessage();
        }
        finally {
            map_r.put("flag",flag);
            map_r.put("msg",msg);
        }
        return map_r;
    }
//    0操作成功 1操作失败2余额不足3重复调用
    public Map<String,Object> updateIntegralAndExperience(String venderId,String order_code,String openId,int balance_90) throws Exception {
        String requestUrl= ConfigUtil.get("BALANCE_902")+"balance/operUserBalance90.ac";
        Map<String,Object> map = new HashMap<String,Object>();
        Map<String,String > map1 =new HashMap<>();
        Map<String,Object> map_r = new HashMap<String,Object>();
        String flag="1";
        String msg="";
        try
        {
            if(balance_90>0){
                map.put("brand_code",venderId);
                map.put("shop_code","");
                map.put("user_code","");
                map.put("order_code",order_code);
                map.put("open_id",openId);
                map.put("type",2);
                map.put("source","5");
                map.put("source_msg","微商城用户退款");
                map.put("balance_90",String.valueOf(balance_90));
                map.put("state",2);
                map.put("commission","0");
                map.put("tradeType","");
                map.put("orderState","1");
                map.put("orderTotal","0");
                map1.put("json", JSON.toJSONString(map));
                String res= URLConectionUtil.httpURLConnectionPostDiy(requestUrl, map1);
                map.clear();
                map=JSON.parseObject(res,Map.class);
                flag=map.get("flag").toString();
                msg=map.get("msg").toString();
            }else{
                flag="0";
            }
        } catch (Exception e)
        {
            flag="1";
            msg=e.getMessage();
        }
        finally {
            map_r.put("flag",flag);
            map_r.put("msg",msg);
        }
        return map_r;
    }

    //0操作成功 1操作失败2余额不足3重复调用
    public Map<String,Object> updateUserCoin(String userId,String orderNum,double amount) throws Exception {
        String requestUrl= ConfigUtil.get("BALANCE_902")+"balance/operUserCoin90_.ac";
        Map<String,Object> map = new HashMap<String,Object>();
        Map<String,String > map1 =new HashMap<>();
        Map<String,Object > map_r =new HashMap<>();
        String flag="1";
        String msg="";
        try
        {
            if(amount>=0.00){
                map.put("userId",userId);
                map.put("state","1");
                map.put("orderNum",orderNum);
                map.put("source","4");
                map.put("amount",String.valueOf(amount));
                map1.put("json", JSON.toJSONString(map));
                String res= URLConectionUtil.httpURLConnectionPostDiy(requestUrl, map1);
                map.clear();
                map=JSON.parseObject(res,Map.class);
                flag=map.get("flag").toString();
                msg=map.get("msg").toString();
            }else{
                map.put("flag",0);
                map.put("msg","无需加贝");
            }

        } catch (Exception e)
        {
            flag="1";
            msg=e.getMessage();
        }finally {
            map_r.put("flag",flag);
            map_r.put("msg",msg);
        }
        return map_r;
    }


    //0操作成功 1操作失败2余额不足3重复调用
    public Map<String,Object> updateRefundMoney(double refundMoney,String orderCode) throws Exception {
       Map<String,Object> map_r=new HashedMap();
        String flag="1";
        String msg="";
        int returnCode=0;
        try
        {

//            String orderMainId = (String)dao.findForObject("sysUserDao.getOrderMainIdByCode",orderCode);
//            TorderMain torderMain = torderMainDao.getById(TorderMain.class,orderMainId);
//            String shop_id = torderMain.getShopId();
//            Shop shop = (Shop)dao.findForObject("sysUserDao.getShopBySid",shop_id);
            Pay17BackPay backPay=pay17Service.getPay17_back(orderCode,orderCode,Global.getPay17_shop_code(),Global.getPay17_user_code(),(int)(refundMoney*100));
            msg=backPay.getReturn_info();
            returnCode =backPay.getReturn_code();
            if(returnCode==0){
                //错误
                flag="1";
            }else{
                flag="0";
            }

        } catch (Exception e)
        {
            flag="1";
            msg=e.getMessage();
        }finally {
            map_r.put("flag",flag);
            map_r.put("msg",msg);
        }
        return map_r;
    }




    private Map<String,Object> splitWXRefund(String transaction_id,String out_trade_no,String total_fee,String refundMoney) throws Exception {
        WXRefund wxRefund = new WXRefund();
        wxRefund.setMch_id(ConfigUtil.get("mch_id"));
        wxRefund.setTransaction_id(transaction_id);
        wxRefund.setOut_trade_no(out_trade_no);
        wxRefund.setOut_refund_no(UuidUtil.get32UUID());

        BigDecimal bigDecimal = new BigDecimal(Double.valueOf(total_fee));
        bigDecimal = new BigDecimal(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
        int total_fee_ = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        wxRefund.setTotal_fee(total_fee_);

        BigDecimal bigDecimal_ = new BigDecimal(Double.valueOf(refundMoney));
        bigDecimal_ = new BigDecimal(bigDecimal_.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
        int refundMoney_ = bigDecimal_.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        wxRefund.setRefund_fee(refundMoney_);
        wxRefund.setOp_user_id(ConfigUtil.get("mch_id"));
        wxRefund.setNonce_str(UuidUtil.get32UUID());
        wxRefund.setRefund_channel("BALANCE");
        wxRefund.setService("unified.trade.refund");
        return wxRefund.submitXmlToRefund();
    }

    private Map<String,Object> splitPay17Refund(String transaction_id,String out_trade_no,String total_fee,String refundMoney,String refundId) throws Exception {
        Pay17OrderRefund pay17Refund = new Pay17OrderRefund();
        pay17Refund.setOrder_code(out_trade_no);
        pay17Refund.setOut_refund_no(refundId);
        pay17Refund.setShop_code(ZkNode.getIstance().getJsonConfig().get("pay17_shop_code").toString());
        pay17Refund.setUser_code(ZkNode.getIstance().getJsonConfig().get("pay17_user_code").toString());
        BigDecimal bigDecimal_ = new BigDecimal(Double.valueOf(refundMoney));
        bigDecimal_ = new BigDecimal(bigDecimal_.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
        int refundMoney_ = bigDecimal_.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        pay17Refund.setRefund_fee(refundMoney_);
        JSONObject jsonObject = HttpRequestUtil.httpRequest("http://open.pay17.cn/phone_wechat_pay.do", "POST", JSON.toJSONString(pay17Refund.StitchingParam()));

        Map<String,Object> result = new HashMap<>();
        if(Integer.parseInt(jsonObject.get("return_code").toString())==1){
            //生成成功
            result.put("return_code",0);
        }else{
            result.put("err_code",jsonObject.get("err_code").toString());
            result.put("err_msg",jsonObject.get("err_code").toString()+":"+jsonObject.get("return_info").toString());
        }
        return result;
    }

    private int refundCallBack(String openId,String orderid,String refundId,String userId) throws Exception {
        //String out_trade_no = result_map.get("return_out_trade_no").toString();
        //String cash_refund_fee = result_map.get("return_refund_fee").toString();
        //String goodsId = out_trade_no.substring(out_trade_no.indexOf("0-"), out_trade_no.length());

        //saveRefundLog(id, "申请退款：" + id + ",退款金额：" + cash_refund_fee, userId);

        if (openId != null && !"".equals(openId)) {
            try {
                //暂不发送退款推送消息，因为有延迟
                //退款成功，修改订单状态
                updateStates(orderid, 6, refundId, "4", userId, "退款申请成功");
                return 1;
            } catch (Exception e) {
                //ignore
                e.printStackTrace();
                return 0;
            }
        }

        return 1;
    }

    public void saveRefundLog(String goodsId, String refundLog, String userId) throws Exception {
        //添加退款记录
        TRefundLog tsl = new TRefundLog();
        tsl.setDoUserId(userId);
        tsl.setGoodsId(goodsId);
        tsl.setState("11");//申请退款
        tsl.setModifyInfo(refundLog);
        tsl.setModifyTime(new Date());
        refundLogDao.save(tsl);
    }


    // 改变字表状态(更具 字表id)
    public boolean updateStates(String orderDetailid, int detailState,String id,String refundState,String userId,String remarks) throws Exception {

        Map<String, Object> detailmap = new HashMap<>();
        detailmap.put("orderid", orderDetailid);
        detailmap.put("state", detailState);

        // 0:待付款1:待发货2:待收货3:待评价4:完成5:退款中6:已退款7:待备货8:未备货退款
        boolean a=(Integer) dao.update("OrderDao.updateOrderDetailidState", detailmap) > 0;

        if(Tools.isEmpty(remarks))
        {
            remarks="";
        }

        Map<String, Object> refundmap = new HashMap<>();
        refundmap.put("id", id);
        refundmap.put("state", refundState);
        refundmap.put("userId", userId);
        refundmap.put("remarks", remarks);

        boolean b=(Integer) dao.update("OrderDao.updateRefund", refundmap)>0;

        return a&&b;
    }


    //退款信息详细（内容）
    @Override
    public Map<String, Object> orderRefund(String orderId) throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        // 0:待付款1:待发货2:待收货3:待评价4:完成5:退款中6:已退款7:待备货8:未备货退款
        return (Map<String, Object>) dao.findForObject("OrderDao.orderRefundMx", map) ;

    }

    //退款信息详细（图片）
    @Override
    public List<Ppics> orderRefundPic(String orderId) throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        // 0:待付款1:待发货2:待收货3:待评价4:完成5:退款中6:已退款7:待备货8:未备货退款
        return (List<Ppics>) dao.findForList("OrderDao.orderRefundMxPic", map);
    }

    public List<Map<String, Object>> getOutTimeOrder() throws Exception {
        String minus= ConfigUtil.get("orderTimeoutTime");//读取配置文件中的失效时间
        Map<String,String> map=new HashMap<>();
        map.put("minus",minus);
        List<Map<String, Object>> list= (List<Map<String, Object>>) dao.findForList("OrderDao.getOutTimeOrder",map);
        return list;
    }

    public void updateCloseOrder(List<Map<String, Object>> list) throws Exception {
        String cityId=  ZkNode.getIstance().getJsonConfig().get("localCity")+"";//读取配置文件中的城市id
        for(Map<String,Object> map:list)
        {
            try
            {
                map.put("cityId",cityId);
                List<PwxGoodsStockHistory> listStock = (List<PwxGoodsStockHistory>) dao.findForList("OrderDao.findStocksByOrderId",map);//获取调用接口参数
                String requestUrl = ConfigUtil.get("ERP_URL")+"/interface/changeStockForWhareHouse.action";
                PwxGoodsStockHistory pwxGoodsStockHistory=new PwxGoodsStockHistory();
                pwxGoodsStockHistory.setParmlist(listStock);
                Map<String, String> Param = new HashMap();
                Map<String,Object> resMap=new HashMap<>();
                String jsontext= JSON.toJSONString(pwxGoodsStockHistory);
                Param.put("parm",jsontext);
                String x= URLConectionUtil.httpURLConnectionPostDiy(requestUrl,Param);//调用接口加仓储线上仓库库存
                x = URLDecoder.decode(x, "utf-8");
                if(!StringUtil.isNullOrEmpty(x)){
                    resMap = JSON.parseObject(x, Map.class);
                    System.out.print(x);
                    if("true".equals(resMap.get("sign")))
                    {
                        updateOrderState(map);//如果成功调用接口，修改订单状态
                    }
                }
            }catch (Exception e)
            {
                e.getMessage();
            }
        }

    }

    private void updateOrderState(Map<String, Object> map) throws Exception {
        dao.update("OrderDao.updateOrderClose",map);
        dao.update("OrderDao.updateOrderDetaildelType",map);
    }


    @Override
    public PunionSumInfo loadUnionSumInfo(Map map)
    {
        PunionSumInfo punionSumInfo = new PunionSumInfo();

        try {
            punionSumInfo = (PunionSumInfo)dao.findForObject("OrderDao.loadUnionSumInfo",map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return punionSumInfo;
    }

    @Override
    public List<PorderMain> exportExcel_orderUnion(Map<String, Map<String, Object>> maps) throws Exception {
        return (List<PorderMain>) dao.findForList(
                "OrderDao.exportExcel_orderUnion", maps);
    }

    @Override
    public String findOpenId(String orderid) throws Exception {
        return (String) dao.findForObject("OrderDao.getOpenId",orderid);
    }

    @Override
    public PorderSend findPorderSend(String orderid) throws Exception {
        return (PorderSend) dao.findForObject("OrderDao.getPorderSend",orderid);
    }

    @Override
    public Map closeOrder(String orderMainId,String userCode)throws Exception{
        Map map = new HashMap();
        TorderMain torderMain = torderMainDao.getById(TorderMain.class,orderMainId);

        try{
            String return_info = "";
            int return_code = 1;
            String shop_id = torderMain.getShopId();
            Shop shop = (Shop)dao.findForObject("sysUserDao.getShopBySid",shop_id);

            TBaseUser tSysUser = tsysUserDao.getById(TBaseUser.class,torderMain.getBuyUserId());
            String open_id =tSysUser.getOpenId();


         /*   *//***********对传入金额处理*************//*
            if(return_code == 1)
            {
                if(return_code == 1)
                {
                    //店小翼key
                    String key = Httpurl.getKeyByParam(shop.getDxy_code(), shop.getDxy_person_code(), torderMain.getCode());
                    //判断线上线下支付
                    if(torderMain.getPaymentRoute().intValue() == 7 || torderMain.getPaymentRoute().intValue() == 1 )//线下支付//银联
                    {
                        return_code = 1;
                    }else{
                        *//****************调用店小翼退款接口**********************//*
                        String url = Global.getConfig("KQ_URL")+"phone_swiftPassBack.do?order_code="+torderMain.getCode()+"&total="+MathUtil.mul(Double.valueOf(torderMain.getPayTotal()),100.0).intValue()+"&user_code="+shop.getDxy_person_code()+"&backcode="+key;
                        String back_result = Httpurl.HttpURL(url);
                        JSONObject json = JSONObject.fromObject(back_result);
//                    logger.error("=QT端体验店退款,退款=json="+json);
                        return_info=json.getString("return_info");
                        return_code = json.getInt("return_code");
                    }
                }
            }*/
            if(return_code == 0){ //错误
                map.put("state", "0");
                map.put("msg", return_info);
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
                    jSONObject90.put("orderNum",torderMain.getCode());
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
                    map.put("state", "0");
                    map.put("msg", return_info);
                }else {
                    map.put("state", "1");
                    map.put("msg", return_info);
                    //torderMain.setState(2);//已退款
                   // torderMain.setBuyUserId(userCode);
                    //torderMain.setBackTime(new Date());
                    /******************加久零券**************************/
                    String x="";
                    List<OrderDetail> pOrderDetailList=(List<OrderDetail>)dao.findForList("sysUserDao.getOrderMainIdByDetailOrderId",orderMainId);
                    if(pOrderDetailList!=null && pOrderDetailList.size()>0)
                    {
                        for(int i=0;i<pOrderDetailList.size();i++)
                        {
                            Map<String,String> map1=new HashMap<>();
                            double double_total = 0;
                            int ticketType=0;
                            int needRefund=0;
                            int goodsType=-1;//0:普通商品，1券类商品
                            goodsType=pOrderDetailList.get(i).getGoodsType();
                            String  orderid = pOrderDetailList.get(i).getId();
                            if(pOrderDetailList.get(i).getIsPayCoupons()==1&&pOrderDetailList.get(i).getBalance_90()>0)
                            {
                                double_total=pOrderDetailList.get(i).getBalance_90();
                                ticketType=0;
                                needRefund=1;
                            }else if(pOrderDetailList.get(i).getIsPayExperience().equals("1")&&pOrderDetailList.get(i).getBalance_experience_90()>0)
                            {
                                double_total=pOrderDetailList.get(i).getBalance_experience_90();
                                ticketType=2;
                                needRefund=1;
                            }else if(pOrderDetailList.get(i).getIsPayShopping().equals("1")&&pOrderDetailList.get(i).getBalance_shopping_90()>0)
                            {
                                double_total=pOrderDetailList.get(i).getBalance_shopping_90();
                                ticketType=1;
                                needRefund=1;
                            }

                            JSONObject jSONObject1 = new JSONObject();
                            jSONObject1.put("open_id",open_id);
                            jSONObject1.put("balance_90",MathUtil.mul(Double.valueOf(double_total),100.0).intValue());
                           try {
                               jSONObject1.put("shop_code",shop.getSid());
                               jSONObject1.put("brand_code",shop.getBrand_code());
                           }
                            catch (Exception e)
                            {
                                jSONObject1.put("shop_code","");
                                jSONObject1.put("brand_code","");
                            }

                            jSONObject1.put("order_code",pOrderDetailList.get(i).getId());

                            jSONObject1.put("user_code",userCode);
                            jSONObject1.put("type", "1");
                            jSONObject1.put("source", "5");//退款
                            jSONObject1.put("source_msg", "关闭订单");
                            jSONObject1.put("state",2);
                            jSONObject1.put("commission","0");
                            jSONObject1.put("orderState","1");
                            jSONObject1.put("orderTotal","0");
                            jSONObject1.put("tradeType",torderMain.getPaymentRoute());
                            jSONObject1.put("ticketType",ticketType);
                            map1.put("json", jSONObject1.toString());
                            System.out.print(jSONObject1.toString());
                            String requestUrl = Global.getConfig("QT_Url")+"api/balance/operUserBalance90.ac";
                           if(1==needRefund){
                               x = URLConectionUtil.httpURLConnectionPostDiy(requestUrl, map1);
                               x = URLDecoder.decode(x, "utf-8");
                           }else {
                               x="{\"flag\":\"0\",\"msg\":\"无需退款\"}";
                           }
                            if(x==null||x.trim().equals("")){
                                map.put("state", "0");
                                map.put("msg", "加劵失败");
                            }else {
                                Map<String, Object> map2 = JSON.parseObject(x, Map.class);
                                if(map2!=null && map2.get("flag").equals("0"))  //加劵成功
                                { //退库存
                                    boolean update_kc=update_lm_jl_Ck(goodsType,orderid);

                                    if(update_kc){
                                        Map<String, Object> detailmap = new HashMap<>();
                                        detailmap.put("orderid", orderid);
                                        detailmap.put("state", 6);
                                        torderMain.setIsClosed(1);
                                        torderMainDao.update(torderMain);
                                        // 0:待付款1:待发货2:待收货3:待评价4:完成5:退款中6:已退款7:待备货8:未备货退款
                                        //boolean a=(Integer) dao.update("OrderDao.updateOrderDetailidState", detailmap) > 0;
                                    }else {
                                        map.put("state", "0");
                                        map.put("msg", "库存增加失败");
                                    }
                                }else
                                {
                                    map.put("state", "0");
                                    map.put("msg", "加劵失败");
                                }
                            }

                        }

                    }




                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return map;
    }
}
