package com.biz.service.api;

import com.alibaba.fastjson.JSON;
import com.biz.conf.Global;
import com.biz.service.base.BaseServiceImpl;
import com.biz.service.base.OrderServiceI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.ConfigUtil;
import com.framework.utils.StringUtil;
import com.framework.utils.Tools;
import com.framework.utils.weixin.HttpRequestUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

//import com.google.gson.Gson;

/**
 * Created by lzq on 2017/2/9.
 */
@Service("wxTemplateService")
public class WxTemplateServiceImpl extends BaseServiceImpl<Object> implements WxTemplateServiceI {

    @Resource(name = "wxUtilService")
    private WxUtilServiceI wxUtilService;

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    /**
     * 客服接口推送消息
     */
    @Override
    public JSONObject send_kf_template(String open_id,String content){
        JSONObject jsonObject = new JSONObject();
        try {
            String token = wxUtilService.getAccessToken();
            String jsonString="{\"touser\":\""+open_id+"\","+
                    "\"msgtype\":\"text\","+
                    "\"text\":{"+
                    "\"content\": \""+content+"\"" +
                    "}"+
                    "}";
            String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + token;
            jsonObject = HttpRequestUtil.httpRequest(requestUrl, "POST", jsonString);
        } catch (Exception e) {
            String msg = e.getMessage();
            if(msg.length()>100){
                msg = msg.substring(0, 99);
            }
            jsonObject.put("errmsg", msg);
        }
        return jsonObject;
    }


    /**
     * 推送支付信息
     * @param scene_id 订单号
     * @param open_id
     * @param jl_total 获得90券金额
     * @param money  实际支付金额
     * @param Pay_time 付款时间
     * @throws Exception
     */
    @Override
    public void send_pay_template(int scene_id,String open_id,double jl_total,double money,String Pay_time) throws Exception {
        String pay_template = ConfigUtil.get("PAY_TEMPLATE");
        String url = ConfigUtil.get("weixinShop_URL") + "client_userPoint.do?scene_id="+scene_id;
        //JSONObject jsonObject = new JSONObject();
        String token = wxUtilService.getAccessToken();
        String remark = "更多详情请点击！";
        //Brand brand = brandService.getBrandOnlyByCode(orderMainUnion.getBrand_code());
        //jl_total = (brand.getProportion()==0?1:brand.getProportion())*orderMainUnion.getCash_total();
        try {
            String sendUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
                    + token;
            String sendString = "{"+
                    "\"touser\":\""+open_id+"\","+
                    "\"template_id\":\""+pay_template+"\","+
                    "\"url\":\""+url+"\","+
                    "\"data\":{"+
                    "\"productType\": {"+
                    "\"value\":\"获得久零券\","+
                    "\"color\":\"#000000\""+
                    "},"+
                    "\"name\": {"+
                    "\"value\":\""+jl_total+"元\","+
                    "\"color\":\"#2C12EA\""+
                    "},"+
                    "\"accountType\": {"+
                    "\"value\":\"金额\","+
                    "\"color\":\"#000000\""+
                    "},"+
                    "\"account\": {"+
                    "\"value\":\""+money+"元\","+
                    "\"color\":\"#2C12EA\""+
                    "},"+
                    "\"time\": {"+
                    "\"value\":\""+Pay_time+"\","+
                    "\"color\":\"#2C12EA\""+
                    "},"+
                    "\"remark\": {"+
                    "\"value\":\""+remark+"\","+
                    "\"color\":\"#EA15CE\""+
                    "}"+
                    "}"+
                    "}";
            JSONObject sendObject = HttpRequestUtil.httpRequest(sendUrl,"POST",sendString);
        } catch (Exception e) {
            String msg = e.getMessage();
            if(msg.length()>100){
                msg = msg.substring(0, 99);
            }
            //jsonObject.put("errmsg", msg);
            e.printStackTrace();
        }
    }


//    @Override
//    public void sendOrderRefundDelayInfo(String openId, double money, String state) throws Exception {
//        String pay_template = Global.getConfig("order_refund_id");
//        String token = wxUtilService.getAccessToken();
////        String remark = "长时间！";
//        try {
//            String sendUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
//                    + token;
//            String sendString = "{"+
//                    "\"touser\":\""+openId+"\","+
//                    "\"template_id\":\""+pay_template+"\","+
//                    "\"url\":\""+""+"\","+
//                    "\"data\":{"+
//                    "\"productType\": {"+
//                    "\"value\":\"获得久零券\","+
//                    "\"color\":\"#000000\""+
//                    "},"+
//                    "\"name\": {"+
//                    "\"value\":\""+0+"元\","+
//                    "\"color\":\"#2C12EA\""+
//                    "},"+
//                    "\"accountType\": {"+
//                    "\"value\":\"金额\","+
//                    "\"color\":\"#000000\""+
//                    "},"+
//                    "\"account\": {"+
//                    "\"value\":\""+money+"元\","+
//                    "\"color\":\"#2C12EA\""+
//                    "},"+
//                    "\"time\": {"+
//                    "\"value\":\""+Pay_time+"\","+
//                    "\"color\":\"#2C12EA\""+
//                    "},"+
//                    "\"remark\": {"+
//                    "\"value\":\""+remark+"\","+
//                    "\"color\":\"#EA15CE\""+
//                    "}"+
//                    "}"+
//                    "}";
//            JSONObject sendObject = HttpRequestUtil.httpRequest(sendUrl,"POST",sendString);
//        } catch (Exception e) {
//            String msg = e.getMessage();
//            if(msg.length()>100){
//                msg = msg.substring(0, 99);
//            }
//            //jsonObject.put("errmsg", msg);
//            e.printStackTrace();
//        }
//    }

    public Map<String,Object> findRefundInfoById(String id) throws Exception{
        return (Map<String,Object>)dao.findForObject("OrderDao.findRefundInfoById",id);
    }
    /**
     * 审核结果通知
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void sendOrderRefundDelayInfo(String orderId,String remark,String state){
        try {
            Map<String,Object> orderInfo = findRefundInfoById(orderId);
            if(orderInfo==null){
                return;
            }
            if(orderInfo.containsKey ("open_id")){
                return;
            }
            if(Tools.isEmpty(orderInfo.get("open_id").toString())){
                return;
            }
            String total_fee = orderInfo.get("payTotal").toString();
            String openId = orderInfo.get("open_id").toString();
            String name =orderInfo.get("name").toString();
            BigDecimal bigDecimal1 = new BigDecimal(Double.valueOf(orderInfo.get("balance_90").toString()));
            bigDecimal1 = new BigDecimal(bigDecimal1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100);
            int balance_90 = bigDecimal1.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();

            BigDecimal bigDecimal2 = new BigDecimal(Double.valueOf(orderInfo.get("coinPayTotal").toString()));
            BigDecimal bigDecimal = new BigDecimal(Double.valueOf(total_fee));
            BigDecimal bigDecimal3 = bigDecimal.add(bigDecimal2);
            double coin_90 = bigDecimal3.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            String token = wxUtilService.getAccessToken();
            String detailNote = "";
            if(!Tools.isEmpty(remark)){
                remark=":"+remark;
            }
            if(!StringUtil.isNullOrEmpty(openId))
            {
                if(state.equals("3"))
                {
                    detailNote ="您申请的退款已被退回！";
                }else if((state.equals("2"))){
                    detailNote ="您申请的退款已同意！";
                }

                //send_kf_template(openId,detailNote+remark);
                String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
                        + token;
                Map p1 = new HashMap();
                p1.put("touser", openId);
                p1.put("template_id", Global.getConfig("order_refund_id"));
                p1.put("url",Global.getConfig("SYS_URL")+"/order/toOrderDetails.do?id="+orderId.trim()+"&type=4_1");
                p1.put("topcolor", "#dd127a");

                Map data = new HashMap();

                Map s11 = new HashMap();
                s11.put("value", detailNote);
                s11.put("color", "#173177");

                data.put("first", s11);

                //协商退款金额
                Map s22 = new HashMap();
                s22.put("value", backTxt(coin_90,balance_90));
                s22.put("color", "#173177");

                data.put("keyword1", s22);

                //商品详情
                Map s33 = new HashMap();
                s33.put("value", name);
                s33.put("color", "#173177");

                data.put("keyword2", s33);

                //订单编号
                Map s44 = new HashMap();
                s44.put("value", orderInfo.get("code").toString());
                s44.put("color", "#173177");

                data.put("keyword3", s44);

                //订单编号
                Map s77 = new HashMap();
                s77.put("value", "请至个人中心查看详情");
                s77.put("color", "#173177");

                data.put("remark", s77);

                p1.put("data", data);

                String result = JSON.toJSONString(p1);// 转换成json数据格式
                System.out.println("--------------------");
                System.out.println(result);

                JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl,"POST", result);
                System.out.println(jsonObject.getString("errcode"));
                System.out.println(jsonObject.getString("errmsg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String backTxt(double coin_90, double balance_90){
        String txt="";
        if(coin_90>0){
            txt+="久零贝金额:"+coin_90+"元";
        }
        if(balance_90>0){
            txt+="久零券金额:"+balance_90/100+"元";
        }
        return  txt;
    }

    @Override
    public Map<String, Object> sendChangeNoticeTemplate(String goodsName,  String changeNumber, String changeCount, String remark, String openId){
        Map<String, Object> map = new HashMap<>();
        map.put("return_code",0);
        map.put("return_info","模板消息发送失败！");
        try {
            String templateId = "";

            try{
                templateId = Global.getConfig("changeNoticeId");
            }catch (Exception e){
                map.put("return_code",0);
                map.put("return_info","缺少兑换商品模板");
                return map;
            }
            String token = wxUtilService.getAccessToken();
            String productType = "兑换商品";

//            String token = "gkHrZj3LZbD75QNRBHOidsyh_V78uBKqdjWxoJXQtk9Ehwuql6izXZPqmAfbHK-4oFubW9x-nU3_RuNL0gCqHHSUj5fvV9RRvLonqTVSAVpbFmBaZdKis73E6hfjW_g0PFYeAGATJC";
            String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
                    + token;
            Map p1 = new HashMap();
            p1.put("touser", openId);
            p1.put("template_id", templateId);
            p1.put("url","");
            p1.put("topcolor", "#dd127a");

            Map data = new HashMap();
            //商品名称
            Map mapGoodsName = new HashMap();
            mapGoodsName.put("value", goodsName);
            mapGoodsName.put("color", "#173177");
            data.put("name", mapGoodsName);

            //商品名称
            Map mapProductType = new HashMap();
            mapProductType.put("value", productType);
            mapProductType.put("color", "#173177");
            data.put("productType", mapProductType);

            //兑换单号
            Map mapChangeNumber = new HashMap();
            mapChangeNumber.put("value", changeNumber);
            mapChangeNumber.put("color", "#173177");
            data.put("certificateNumber", mapChangeNumber);

            //remark
            Map mapNumber = new HashMap();
            mapNumber.put("value", changeCount);
            mapNumber.put("color", "#173177");
            data.put("number", mapNumber);

            //remark
            Map mapRemark = new HashMap();
            mapRemark.put("value", remark);
            mapRemark.put("color", "#173177");
            data.put("remark", mapRemark);

            p1.put("data", data);

            String result = JSON.toJSONString(p1);// 转换成json数据格式
            JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl,"POST", result);
            if("ok".equals(jsonObject.getString("errmsg")))
            {
                map.put("return_code",1);
                map.put("return_info","模板消息发送成功！");
            }else{
                map.put("return_code",0);
                map.put("return_info",jsonObject.getString("errmsg"));
            }
            System.out.println(jsonObject.getString("errcode"));
            System.out.println(jsonObject.getString("errmsg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
