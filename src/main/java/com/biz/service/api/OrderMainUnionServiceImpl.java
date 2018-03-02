package com.biz.service.api;

import com.alibaba.fastjson.JSON;
import com.biz.model.Hmodel.TSysUser;
import com.biz.model.Hmodel.api.*;
import com.biz.model.Hmodel.basic.TBaseBrand;
import com.biz.model.Pmodel.PorderMainUnion;
import com.biz.model.Pmodel.api.*;
import com.biz.model.Pmodel.basic.Base90Detail;
import com.biz.model.Pmodel.basic.Brand;
import com.biz.model.Pmodel.pay17.Pay17BackPay;
import com.biz.model.Pmodel.pay17.Pay17SelectPay;
import com.biz.model.Singleton.ZkNode;
import com.biz.service.base.BaseServiceImpl;
import com.biz.service.base.Pay17ServiceI;
import com.biz.service.basic.ShopServiceI;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.*;
import com.framework.utils.weixin.HttpRequestUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import java.util.*;

/**
 * Created by lzq on 2017/2/4.
 */

@Service("orderMainUnionService")
public class OrderMainUnionServiceImpl extends BaseServiceImpl implements OrderMainUnionServiceI {
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Resource(name = "shopService")
    private ShopServiceI shopService;

    @Resource(name = "orderMainUnionService")
    private OrderMainUnionServiceI orderMainUnionService;

    @Resource(name = "wxTemplateService")
    private WxTemplateServiceI wxTemplateService;

    @Resource(name = "phoneUserService")
    private PhoneUserServiceI phoneUserService;

    @Resource(name = "wxUtilService")
    private WxUtilServiceI wxUtilService;

    @Autowired
    private PhoneUserServiceI phoneUserServiceI;

    @Autowired
    private BaseDaoI<TSysUser> userDao;
    @Autowired
    private BaseDaoI<TBaseBrand> brandDao;
    @Autowired
    private BaseDaoI<TpaySceneDetail> paySceneDetailDao;
    @Autowired
    private BaseDaoI<TperiodizationShop> tperiodizationShopDao;


    @Autowired
    private BaseDaoI<TPeriodizationMain> tPeriodizationMainDao;

    @Autowired
    private BaseDaoI<TPeriodizationDetail> tPeriodizationDetailDao;

    @Autowired
    private BaseDaoI<TPeriodizationDetailLog> tPeriodizationDetailLogDao;

    @Resource(name = "pay17Service")
    private Pay17ServiceI pay17Service;

    protected Logger logger = Logger.getLogger(this.getClass());

    /**
     * 获取
     */
    public PorderMainUnion getOrderMainUnionByCode(String code) throws Exception
    {
        return (PorderMainUnion) dao.findForObject("OrderMainUnionDao.getOrderMainUnionByCode", code);
    }

    @Override
    public void updateWhereOrderMainUnion(Map<String, Object> param) throws Exception{
        dao.update("OrderMainUnionDao.updateWhereOrderMainUnion", param);
    }

    //获取订单编号，当时自动发券的id，用于撤销
    @Override
    public String getOrderGetBalance90(String sourceId) throws Exception{
        if(Tools.isEmpty(sourceId)) {
            return "";
        }else{
            Map<String, Object> s_map = new HashMap<>();
            s_map.put("sourceId", sourceId);
            List<Map<String, Object>> r_map =(List<Map<String, Object>>)dao.findForList("OrderMainUnionDao.getOrderGetBalance90", s_map);
            if(r_map==null){
                return "";
            }else{
                if(r_map.size()==0)
                {
                    return "";
                }else if(r_map.size()>1){
                    return "";
                }else{
                    return r_map.get(0).get("id").toString();
                }
            }
        }
    }

    @Override
    public void insertOrderMainUnion(PorderMainUnion orderMainUnion) throws Exception {
        dao.save("OrderMainUnionDao.insertOrderMainUnion", orderMainUnion);
    }

    /**
     * 增加用户90券
     */
    public int add_balance_90(Map<String,Object> pd) throws Exception
    {
        return (int) dao.update("OrderMainUnionDao.add_balance_90", pd);
    }

    /**
     * 添加
     */
    public void insertBase90Detail(Base90Detail base90Detail) throws Exception {
        dao.save("OrderMainUnionDao.insertBase90Detail", base90Detail);
    }

    //旧——支付宝微信刷卡付(掉pay17)
    @Override
    public Result phone_wx_scan(Map<String, Object> map)throws Exception{
        Result result=new Result();
        Map<String, String> mapQrparam = new HashMap<>();
/***********对传入金额处理*************/
        double double_total = 0d;//分
        int int_total = 0; //分
        double jl_total=0;
        String shop_id=map.get("shop_id").toString();
        String user_code=map.get("user_code").toString();
        String author_code=map.get("author_code").toString();
        String total=map.get("total").toString();
        String order_code=map.get("order_code").toString();
        String device_info=map.get("device_info").toString();
        String device_ip=map.get("device_ip").toString();

        try {
            double_total = money_convert(map.get("total").toString());
            int_total = money_convert_Int(map.get("total").toString());
        } catch (Exception e) {
            throw new RuntimeException("金额错误，金额为"+total);
        }
        int scene_id = 0;
        try {
            Shop shop = shopService.getShopBySid(shop_id);
            BaseUser user = null;
            if(shop == null ){
                throw new RuntimeException("门店不存在");
            }

            PorderMainUnion orderMainUnion = orderMainUnionService.getOrderMainUnionByCode(order_code);
            if(orderMainUnion != null ){
                result.setReturn_info("订单已存在");
                throw new RuntimeException("订单已存在");
            }

            //判断1为微信，2为支付宝
            String trade_type = "";
            if(author_code.startsWith("1")){ //微信
                trade_type = "MICROPAY";
            }else if(author_code.startsWith("2")){ //支付宝
                trade_type = "ZFB-MICROPAY";
            }

            Brand brand = phoneUserService.getBrandOnlyByCode(shop.getBrand_code());
            double bilil=(brand.getProportion() == 0 ? 1.0 : brand.getProportion());

            //此处添加是否分期
            boolean isPeriodization = false;
            try{
                isPeriodization =  "1".equals(brand.getIsPeriodization())? true : false;
            }catch(Exception e)
            {
                e.printStackTrace();
            }

            jl_total = MathUtil.mul(bilil, (double)int_total);
            System.out.println("####jl_total:"+jl_total+",int_total:"+int_total+",bilil:"+bilil);

            //***********计算送90贝数*************//
            double coin_90_total=0.0;
            if(shop.getIscoin()==1){
                try{
                    double bl = Double.parseDouble(ZkNode.getIstance().getJsonConfig().get("service_charge").toString());
                    System.out.println("service_charge:::"+bl);
                    coin_90_total= MathUtil.mul(Double.parseDouble(total), bl);
                    System.out.println("coin_90_total:::"+coin_90_total);
                }catch(Exception e)
                {
                    System.out.println("zk_service_charge:::"+e.toString());
                    e.printStackTrace();
                }
            }

            /****************调用店小翼支付接口**********************/
            /*String url = ConfigUtil.get("KQ_URL")+"phone_swiftPassScanPay.do?shop_id="+shop.getDxy_code()+"&user_code="+shop.getDxy_person_code()+"&author_code="+author_code
                    +"&total="+int_total+"&order_code="+order_code+"&device_info="+device_info+"&device_ip="+device_ip;
            logger.error("=微信刷卡付=调用店小翼支付接口=url="+url);
            String back_result = HttpURL(url);
            JSONObject json = JSONObject.fromObject(back_result);
            int return_code = json.getInt("return_code");*/

            Pay17SelectPay bs_r= pay17Service.getPay17_bs(order_code, int_total,author_code,device_ip, shop.getDxy_code(),shop.getDxy_person_code());
            int return_code = bs_r.getReturn_code();
            logger.error("##################################################");
            logger.error("return_code"+return_code);
            logger.error("##################################################");
            /****************支付成功，判断用户是否会员**********************/



            //1.是会员，直接推送模板消息，订单-兴业手续费，久零券
            orderMainUnion = new PorderMainUnion();
            orderMainUnion.setGift_90(jl_total);
            orderMainUnion.setCode(order_code);
            orderMainUnion.setOrder_total(double_total);
            orderMainUnion.setCash_total(double_total);
            orderMainUnion.setCard_total(0);
            orderMainUnion.setCard_count(0);
            orderMainUnion.setCoin_90(coin_90_total);
            orderMainUnion.setBalance_type(shop.getTicketType());
            if(return_code == 0){ //错误
                result.setReturn_code(0);
                //result.setReturn_info(json.getString("return_info"));(店小翼)
                if(bs_r.getTrade_state()==5){
                    result.setReturn_info("USERPAYING-"+bs_r.getReturn_info());
                }else{
                    result.setReturn_info(bs_r.getReturn_info());
                }
                orderMainUnion.setState(2);
                //orderMainUnion.setError_pay_msg(json.getString("error_pay_msg"));(店小翼)
                orderMainUnion.setError_pay_msg(bs_r.getReturn_info());
            }else if(return_code == 1){ //正确
                result.setReturn_code(1);
                //String xy_openid = json.getString("pay_user_id");(店小翼)
                String xy_openid=bs_r.getCustomer_id();
                Map<String,Object> pd = new HashMap<>();
                if(author_code.startsWith("1")){ //微信
                    pd.put("xy_openid", xy_openid);
                }else if(author_code.startsWith("2")){ //支付宝
                    pd.put("scan_ali_id", xy_openid);
                }
                logger.error("##################################################");
                logger.error(pd);
                logger.error("##################################################");
                user = phoneUserService.getOneBaseUser(pd);
                //场景值  带参数关注二维码
                if(author_code.startsWith("1")){ //微信
                    scene_id = save_pay_scence(1,xy_openid,order_code);
                }else if(author_code.startsWith("2")){ //支付宝
                    scene_id = save_pay_scence(2,xy_openid,order_code);
                }
				/*@Update zhengXin start 2016-10-31  String link = temporaryCode(scene_id);*/
                String link = "";
                double totals = Double.valueOf(jl_total)/100;
                if(!isPeriodization) //不进行分期
                {
                    if (user != null && (
                            StringUtils.isNotEmpty(user.getScan_ali_id()) ||
                                    StringUtils.isNotEmpty(user.getScan_yhk_id()) ||
                                    StringUtils.isNotEmpty(user.getScan_yi_id()) ||
                                    StringUtils.isNotEmpty(user.getXy_openid()))
                            ) {
                        link = "亲爱的久零会员-你的"+ totals +"久零券已经到账！-可进入“辽宁久零”公众号内-查看和使用。";
                        wxTemplateService.send_kf_template(user.getOpen_id(), "支付成功，支付金额："+totals+"元，赠送久零券："+totals);

                    }
                    else
                    {
                        link = temporaryCode(scene_id);
                    }
                }else{//分期处理，生成二维码
                    mapQrparam = getPeriodizationLinkUrl(totals,shop_id,order_code,shop.getBrand_code(),xy_openid);
                    link = mapQrparam.get("link");
                    result.setMainId(mapQrparam.get("mainId"));
                }
				/*@Update zhengXin end 2016-10-31 */

                result.setReturn_info(link+","+trade_type);
                logger.error("=微信刷卡付=scene_id="+scene_id+"=link="+link);
                orderMainUnion.setState(1);
                //orderMainUnion.setPay_time(json.getString("pay_time"));(店小翼)
                orderMainUnion.setPay_time(VeDate.getStringDate());
                orderMainUnion.setPay_user_id(xy_openid);
                /****************会员自动发模板消息，增加券，记录**********************/
                if(user!=null){
                    orderMainUnion.setOpen_id(user.getOpen_id());
                }

            }
            orderMainUnion.setUser_code(user_code);
            orderMainUnion.setShop_code(shop_id);
            orderMainUnion.setCommission(brand.getCommission());
            orderMainUnion.setProcedures(brand.getProcedures());
            orderMainUnion.setBrand_code(shop.getBrand_code());
            orderMainUnion.setTrade_type(trade_type);
            orderMainUnion.setIs_clean(0);
            orderMainUnion.setPay_type(1);
            orderMainUnionService.insertOrderMainUnion(orderMainUnion);

            /****************会员自动发模板消息，增加券，记录 **********************/
            //此处添加分期逻辑处理
            if(user!=null && !isPeriodization){
                int f_q=1;//发券是否成功， 0成功 1失败 3重
                int f_b=1;//发贝是否成功， 0成功 1失败 3重
                String r_msg="";
                //发送模板消息
                send_pay_template(scene_id,user.getOpen_id(),orderMainUnion);

                JSONObject jSONObject1 = new JSONObject();
                jSONObject1.put("brand_code",shop.getBrand_code());
                jSONObject1.put("shop_code",shop_id);
                jSONObject1.put("user_code",user_code);
                jSONObject1.put("order_code",order_code);
                jSONObject1.put("open_id",user.getOpen_id());
                jSONObject1.put("type","0");
                jSONObject1.put("source","1");
                jSONObject1.put("source_msg","会员购买商品自动发券");
                jSONObject1.put("balance_90", jl_total);
                jSONObject1.put("state", "2");
                jSONObject1.put("commission", brand.getCommission());
                jSONObject1.put("tradeType",trade_type);
                jSONObject1.put("tradeType",trade_type);
                jSONObject1.put("orderState","1");
                jSONObject1.put("orderTotal",double_total);
                jSONObject1.put("ticketType",shop.getTicketType());
                f_q = user_quan(jSONObject1);
                f_b = user_coin(user.getId(),order_code,coin_90_total,1,3) ;
                if((f_q==0|| f_q==3)){
                    r_msg+="-发券成功;";
                }else{
                    r_msg+="-发券失败;";
                }

                if((f_b==0|| f_b==3)){
                    r_msg+="-发贝成功;";
                }else{
                    r_msg+="-发贝失败;";
                }

                if((f_q==0|| f_q==3) && (f_b==0|| f_b==3)){
                    result.setReturn_code(1);//0错误 1成功
                }else{
                    result.setReturn_code(0);//0错误 1成功
                }

                result.setReturn_info("************-***************"+r_msg+","+trade_type);
            }

        }catch (Exception e){
            throw new RuntimeException("微信刷卡付=报错");
        }
        return result;
    }

    /**
     * 新——微信支付宝付款(无第三方支付调用)
     * @param shop_id 门店 base_shop 的id (原参数)
     * @param user_code 操作人t_sys_user 的id (原参数)
     * @param pay_type 第三方类型：1银联，2：拉卡拉
     * @param author_code 1：微信(MICROPAY) 2:支付宝(ZFB-MICROPAY)(原参数) ，3.银联卡(UNIONPAY)，4:银联钱包(UNIONPAY-WALLET)，5:百度钱包(BAIDU-WALLET),6:京东钱包(JD-WALLET),7:拉卡拉钱包 (lkl-WALLET)
     * @param total total为字符的元(原参数)
     * @param order_code total为字符的元(原参数)
     * @param card_no 卡号,银联卡支付时必传(author_code=3)
     * @return
     * @throws Exception
     */
    @Override
    public Result phone_yl_lkl_scan(String shop_id,String user_code, String author_code, String total, String order_code,int pay_type,String card_no)throws Exception{

        Result result=new Result();
        Map<String, String> mapQrparam = new HashMap<>();
        //double double_total = 0d;//分
        int int_total = 0; //订单支付金额(分)
        double jl_total=0;//获得90券数(分)
        int scene_id = 0;//场景值  带参数关注二维码

        try {
            int_total = money_convert_Int(total);
        } catch (Exception e) {
            throw new RuntimeException("金额错误，金额为"+total);
        }

        if(Tools.isEmpty(author_code)){
            result.setReturn_info("author_code, 参数错误");
            throw new RuntimeException("author_code, 参数错误");
        }else{
            if(author_code.equals("3") && Tools.isEmpty(card_no)){
                result.setReturn_info("银行卡支付，卡号不能空");
                throw new RuntimeException("银行卡支付，卡号不能空");
            }
        }

        try {
            Shop shop = shopService.getShopBySid(shop_id);
            BaseUser user = null;
            if(shop == null ){
                throw new RuntimeException("门店不存在");
            }

            PorderMainUnion orderMainUnion = orderMainUnionService.getOrderMainUnionByCode(order_code);
            if(orderMainUnion != null ){
                result.setReturn_info("订单已存在");
                throw new RuntimeException("订单已存在");
            }

            //判断1为微信，2为支付宝,3银联.。。。
            String trade_type = getTrade_type(author_code);

            Brand brand = phoneUserService.getBrandOnlyByCode(shop.getBrand_code());
            double bilil=(brand.getProportion() == 0 ? 1.0 : brand.getProportion());

            //此处添加是否分期
            boolean isPeriodization = false;
            try{
                isPeriodization =  "1".equals(brand.getIsPeriodization())? true : false;
            }catch(Exception e)
            {
                e.printStackTrace();
            }

            jl_total = MathUtil.mul(bilil, (double)int_total);

            //***********计算送90贝数*************//
            double coin_90_total=0.0;
            if(shop.getIscoin()==1){
                try{
                    double bl = Double.parseDouble(ZkNode.getIstance().getJsonConfig().get("service_charge").toString());
                    coin_90_total= MathUtil.mul(Double.parseDouble(total), bl);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

            result.setReturn_code(1);
            String ys_openid="";//银联商户
            if (author_code.equals("3")) {
                //用银行卡支付，有个人卡号，查询是否绑定用户，如果绑定，插入
                ys_openid=card_no;
                user=phoneUserServiceI.getOneBaseUserByUnionpay(card_no);
            }
            //场景值  带参数关注二维码
            if(author_code.startsWith("1")){ //微信
                scene_id = save_pay_scence(1,ys_openid,order_code);
            }else if(author_code.startsWith("2")){ //支付宝
                scene_id = save_pay_scence(2,ys_openid,order_code);
            }else if(author_code.startsWith("3")){//银联卡
                scene_id = save_pay_scence(4,ys_openid,order_code);
            }else if(author_code.startsWith("4")){//银联钱包
                scene_id = save_pay_scence(8,ys_openid,order_code);
            }else if(author_code.startsWith("5")){//百度钱包
                scene_id = save_pay_scence(9,ys_openid,order_code);
            }else if(author_code.startsWith("6")){//京东钱包
                scene_id = save_pay_scence(6,ys_openid,order_code);
            }else if(author_code.startsWith("7")){//拉卡拉钱包
                scene_id = save_pay_scence(7,ys_openid,order_code);
            }

				/*@Update zhengXin start 2016-10-31  String link = temporaryCode(scene_id);*/
            String link = "";
            double totals = Double.valueOf(jl_total)/100;

            //1.是会员，直接推送模板消息，订单-兴业手续费，久零券
            orderMainUnion = new PorderMainUnion();
            orderMainUnion.setGift_90(jl_total);
            orderMainUnion.setCode(order_code);
            orderMainUnion.setOrder_total(int_total);
            orderMainUnion.setCash_total(int_total);
            orderMainUnion.setCard_total(0);
            orderMainUnion.setCard_count(0);
            orderMainUnion.setCoin_90(coin_90_total);
            orderMainUnion.setBalance_type(shop.getTicketType());
            if(!isPeriodization) //不进行分期
                {
                    if (user != null) {
                        //用银行卡支付，有个人卡号，查询是否绑定用户，如果绑定，插入
                        link = "亲爱的久零会员-你的"+ totals +"久零券已经到账！-可进入公众号内-查看和使用。";
                        wxTemplateService.send_kf_template(user.getOpen_id(), "支付成功，支付金额："+totals+"元，赠送久零券："+totals);
                    }
                    else
                    {
                        link = temporaryCode(scene_id);
                    }
                }else{//分期处理，生成二维码
                    mapQrparam = getPeriodizationLinkUrl(totals,shop_id,order_code,shop.getBrand_code(),ys_openid);
                    link = mapQrparam.get("link");
                    result.setMainId(mapQrparam.get("mainId"));
                }
				/*@Update zhengXin end 2016-10-31 */

            result.setReturn_info(link + "," + trade_type);
            orderMainUnion.setState(1);
            orderMainUnion.setPay_time(VeDate.getStringDate());
            orderMainUnion.setPay_user_id(ys_openid);
            if(user!=null){
                orderMainUnion.setOpen_id(user.getOpen_id());
            }
            orderMainUnion.setUser_code(user_code);
            orderMainUnion.setShop_code(shop_id);
            orderMainUnion.setCommission(brand.getCommission());
            orderMainUnion.setProcedures(brand.getProcedures());
            orderMainUnion.setBrand_code(shop.getBrand_code());
            orderMainUnion.setTrade_type(trade_type);
            orderMainUnion.setIs_clean(0);
            orderMainUnion.setPay_type(pay_type);
            orderMainUnionService.insertOrderMainUnion(orderMainUnion);

            /****************会员自动发模板消息，增加券，记录 **********************/
            //此处添加分期逻辑处理
            if(user!=null && !isPeriodization){
                int f_q=1;//发券是否成功， 0成功 1失败 3重
                int f_b=1;//发贝是否成功， 0成功 1失败 3重
                String r_msg="";
                //发送模板消息
                send_pay_template(scene_id,user.getOpen_id(),orderMainUnion);

                JSONObject jSONObject1 = new JSONObject();
                jSONObject1.put("brand_code",shop.getBrand_code());
                jSONObject1.put("shop_code",shop_id);
                jSONObject1.put("user_code",user_code);
                jSONObject1.put("order_code",order_code);
                jSONObject1.put("open_id",user.getOpen_id());
                jSONObject1.put("type","0");
                jSONObject1.put("source","1");
                jSONObject1.put("source_msg","会员购买商品自动发券");
                jSONObject1.put("balance_90", jl_total);
                jSONObject1.put("state", "2");
                jSONObject1.put("commission", brand.getCommission());
                jSONObject1.put("tradeType",trade_type);
                jSONObject1.put("tradeType",trade_type);
                jSONObject1.put("orderState","1");
                jSONObject1.put("orderTotal",int_total);
                jSONObject1.put("ticketType",shop.getTicketType());
                f_q = user_quan(jSONObject1);
                f_b = user_coin(user.getId(),order_code,coin_90_total,1,3) ;
                if((f_q==0|| f_q==3)){
                    r_msg+="-发券成功;";
                }else{
                    r_msg+="-发券失败;";
                }

                if((f_b==0|| f_b==3)){
                    r_msg+="-发贝成功;";
                }else{
                    r_msg+="-发贝失败;";
                }

                if((f_q==0|| f_q==3) && (f_b==0|| f_b==3)){
                    result.setReturn_code(1);//0错误 1成功
                }else{
                    result.setReturn_code(0);//0错误 1成功
                }

                result.setReturn_info("************-***************"+r_msg+","+trade_type);
            }

        }catch (Exception e){
            throw new RuntimeException("微信刷卡付=报错");
        }
        return result;
    }

    private String getTrade_type(String author_code){
        String trade_type="";
        if(author_code.startsWith("1")){ //微信
            trade_type = "MICROPAY";
        }else if(author_code.startsWith("2")){ //支付宝
            trade_type = "ZFB-MICROPAY";
        }else if(author_code.startsWith("3")){ //银联卡
            trade_type = "UNIONPAY";
        }else if(author_code.startsWith("4")){ //银联钱包
            trade_type = "UNIONPAY-WALLET";
        }else if(author_code.startsWith("5")){ //百度钱包
            trade_type = "BAIDU-WALLET";
        }else if(author_code.startsWith("6")){ //京东钱包
            trade_type = "JD-WALLET";
        }else if(author_code.startsWith("7")){ //拉卡拉钱包
            trade_type = "lkl-WALLET";
        }
        return trade_type;
    }

//发久零券 0成功 1失败 3重
    private int user_quan(JSONObject jSONObject1) {
        int zt = 1;
        try {
            Map<String, String> map1 = new HashMap<>();
            map1.put("json", jSONObject1.toString());
            Map<String, Object> r_Interface = APIInterface(map1, "api/balance/operUserBalance90.ac");
            if (r_Interface != null) {
                return zt = Integer.parseInt(r_Interface.get("flag").toString());
            } else {
                return zt;
            }
        } catch (Exception e) {
            return 1;
        }
    }

    //发久零币 0成功 1失败 3重
    private int user_coin(String userId,String orderNum,Double bei_90,int state,int source) {
        int zt = 1;
        try {
            if (bei_90 > 0) {
                JSONObject jSONObject90 = new JSONObject();
                jSONObject90.put("userId", userId);
                jSONObject90.put("state", state);
                jSONObject90.put("orderNum", orderNum);
                jSONObject90.put("source", source);
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
                System.out.println("diao_yong_90_bei::"+map2);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            return map2;
        }
    }


    @Override
    public Result phone_wx_back_money(Map<String, Object> map) throws Exception {

        logger.error("##################################################");
        logger.error("开始正确退款啦");
        logger.error("##################################################");
        System.out.println("##################################################");
        System.out.println("开始正确退款啦");
        System.out.println("##################################################");


        Result result=new Result();
        String order_code=map.get("order_code").toString();
        String total=map.get("total").toString();
        String user_code=map.get("user_code").toString();
        String main_user_id="";


        PorderMainUnion orderMain = orderMainUnionService.getOrderMainUnionByCode(order_code);
        if(orderMain ==null){
            throw new RuntimeException("订单不存在");
        }
        if(StringUtils.isNotBlank(orderMain.getOpen_id())){
            BaseUser user = phoneUserService.getBaseUserByOpen_id(orderMain.getOpen_id());
            double user_card_total = user.getBalance_90();
            double user_Shoping_card_total = user.getBalance_shopping_90();
            double card_total = orderMain.getGift_90();
            main_user_id=user.getId();
            //判断券是否足够
            if(orderMain.getBalance_type()==0){
                if (user_card_total < card_total) {
                    throw new RuntimeException("退款用户久零券余额不足");
                }
            }else{
                if (user_Shoping_card_total < card_total) {
                    throw new RuntimeException("退款用户购物券余额不足");
                }
            }

            //判断贝是否足够
            if(orderMain.getCoin_90()>user.getGiveAmount()){
                throw new RuntimeException("退款用户久零贝余额不足");
            }
        }

        Shop shop = shopService.getShopBySid(orderMain.getShop_code());
        /***********对传入金额处理*************/
        int int_total = 0; //分
        try {
            //int_total = money_convert_Int(total);
            int_total = Integer.valueOf(total);
        } catch (Exception e) {
            throw new RuntimeException("金额错误");
        }
        /***********对传入金额处理*************/
        StringBuffer key_sb = new StringBuffer();
        key_sb.append("SHOPCODE=");
        key_sb.append(shop.getDxy_code().trim());
        key_sb.append("&USERCODE=");
        key_sb.append(shop.getDxy_person_code().trim());
        key_sb.append("&ORDERCODE=");
        key_sb.append(order_code.trim());
        System.err.println(key_sb);
        String key = CryptTool.md5Digest(key_sb.toString());
        /****************调用店小翼退款接口**********************/
        /*String url = ConfigUtil.get("KQ_URL")+"phone_swiftPassBack.do?order_code="+order_code+"&total="+int_total+"&user_code="+shop.getDxy_person_code()+"&backcode="+key;
        String back_result = HttpURL(url);
        JSONObject json = JSONObject.fromObject(back_result);
        logger.error("=微信刷卡付,退款=json="+json);
        String return_info=json.getString("return_info");
        int return_code = json.getInt("return_code");*/

        Pay17BackPay backPay=pay17Service.getPay17_back(order_code,order_code,shop.getDxy_code(),shop.getDxy_person_code(),int_total);
        String return_info=backPay.getReturn_info();
        int return_code =backPay.getReturn_code();

        if(return_code == 0){ //错误
            result.setReturn_code(0);
            result.setReturn_info(return_info);
        }else if(return_code == 1){ //正确
            //订单赠送90券数额
            double double_total = orderMain.getGift_90();
            //订单原始金额与退款金额数值差
            double back_order_total = orderMain.getOrder_total()-orderMain.getOrder_total();
            //订单原始赠送90券与退款返还90券数额数值差
            double back_gift_90 = orderMain.getGift_90()-orderMain.getGift_90();

            result.setReturn_code(1);
            result.setReturn_info(return_info);
            Map<String,Object> pd = new HashMap<>();
            pd.put("code", order_code);
            pd.put("back_user_code", user_code);
            pd.put("state", 3);
            pd.put("back_code", return_info);
            pd.put("back_time", DateUtil.getTime());
            pd.put("old_order_total", orderMain.getOrder_total());	//保存退款前订单原始金额
            pd.put("old_gift_90", orderMain.getGift_90());			//保存退款前订单原始赠送90券
            pd.put("order_total", back_order_total);		//保存退款后订单原始金额与退款金额的数值差
            pd.put("gift_90", back_gift_90);				//保存退款后订单原始赠送90券与退款返还90券的数值差
            orderMainUnionService.updateWhereOrderMainUnion(pd);

            String base_90_dtail_id = orderMainUnionService.getOrderGetBalance90(order_code);

            Map<String,String> map1=new HashMap<>();
            JSONObject jSONObject1 = new JSONObject();
            jSONObject1.put("brand_code",shop.getBrand_code());
            jSONObject1.put("shop_code",orderMain.getShop_code());
            jSONObject1.put("user_code",user_code);
            jSONObject1.put("order_code",base_90_dtail_id);
            jSONObject1.put("open_id",orderMain.getOpen_id());
            jSONObject1.put("type","0");
            jSONObject1.put("source","10");
            jSONObject1.put("source_msg","用户退款");
            jSONObject1.put("balance_90", double_total);
            jSONObject1.put("state", "1");
            jSONObject1.put("commission", orderMain.getCommission());
            jSONObject1.put("tradeType", orderMain.getTrade_type());
            jSONObject1.put("orderState","3");
            jSONObject1.put("orderTotal",orderMain.getOrder_total());
            jSONObject1.put("ticketType",orderMain.getBalance_type());
            //map1.put("json", jSONObject1.toString());
            int f_q=1;//发券是否成功， 0成功 1失败 3重
            int f_b=1;//发贝是否成功， 0成功 1失败 3重
            String r_msg="";
            //不是会员不扣券
            if(double_total>0){
                f_q=user_quan(jSONObject1);
            }else{
                f_q=0;
            }
            //退贝
            f_b = user_coin(main_user_id,order_code,orderMain.getCoin_90(),2,7) ;

            if((f_q==0|| f_q==3) && (f_b==0|| f_b==3)){
                result.setReturn_code(1);//0错误 1成功
            }else{
                result.setReturn_code(0);//0错误 1成功
            }
            result.setReturn_info("****************************"+r_msg);

//
//            //发送模板消息
//            try{
//                send_refund_template(return_info,orderMain,int_total);
//            } catch (Exception e) {
//
//            }
        }
        return result;
    }


    //拉卡拉微信支付宝退款
    @Override
    public Result updatePhone_lkl_back_money(Map<String, Object> map) throws Exception {

        System.out.println("##################################################");
        System.out.println("开始正确退款啦");
        System.out.println("##################################################");
        boolean pf=true;//true//成功 false//失败
        String r_msg="";

        Result result=new Result();
        String order_code=map.get("order_code").toString();
        String total=map.get("total").toString();
        String user_code=map.get("user_code").toString();
        String main_user_id="";


        PorderMainUnion orderMain = orderMainUnionService.getOrderMainUnionByCode(order_code);
        if(orderMain ==null){
            throw new RuntimeException("订单不存在");
        }
        if(StringUtils.isNotBlank(orderMain.getOpen_id())){
            BaseUser user = phoneUserService.getBaseUserByOpen_id(orderMain.getOpen_id());
            double card_total = orderMain.getGift_90();
            main_user_id=user.getId();

            double user_Shop_card_total=user.getBalance_shopping_90();
            double user_card_total = user.getBalance_90();
            //判断券是否足够
            if(orderMain.getBalance_type()==0){
                //90券
                if (user_card_total < card_total) {
                    r_msg="退款用户久零券余额不足";
                    pf=false;
                }
            }else{
                //购物券
                if (user_Shop_card_total < card_total) {
                    r_msg="退款用户购物券余额不足";
                    pf=false;
                }
            }

            //判断贝是否足够
            if(orderMain.getCoin_90()>user.getGiveAmount()){
                r_msg="退款用户久零贝余额不足";
                pf=false;
            }
        }

        Shop shop = shopService.getShopBySid(orderMain.getShop_code());
        /***********对传入金额处理*************/
        int int_total = 0; //分
        try {
            //int_total = money_convert_Int(total);
            int_total = Integer.valueOf(total);
        } catch (Exception e) {
            r_msg="金额错误";
            pf=false;
        }

        String return_info="";
            //订单赠送90券数额
            double double_total = orderMain.getGift_90();
            //订单原始金额与退款金额数值差
            double back_order_total = orderMain.getOrder_total()-orderMain.getOrder_total();
            //订单原始赠送90券与退款返还90券数额数值差
            double back_gift_90 = orderMain.getGift_90()-orderMain.getGift_90();

        if(pf) {

            pf = false;
            if (StringUtils.isNotBlank(orderMain.getOpen_id())) {
                //查询当初的发券记录
                String base_90_dtail_id = orderMainUnionService.getOrderGetBalance90(order_code);
                if (!Tools.isEmpty(base_90_dtail_id)) {
                    Map<String, String> map1 = new HashMap<>();
                    JSONObject jSONObject1 = new JSONObject();
                    jSONObject1.put("brand_code", shop.getBrand_code());
                    jSONObject1.put("shop_code", orderMain.getShop_code());
                    jSONObject1.put("user_code", user_code);
                    jSONObject1.put("order_code", base_90_dtail_id);
                    jSONObject1.put("open_id", orderMain.getOpen_id());
                    jSONObject1.put("type", "0");
                    jSONObject1.put("source", "10");
                    jSONObject1.put("source_msg", "POS用户退款");
                    jSONObject1.put("balance_90", double_total);
                    jSONObject1.put("state", "1");
                    jSONObject1.put("commission", orderMain.getCommission());
                    jSONObject1.put("tradeType", orderMain.getTrade_type());
                    jSONObject1.put("orderState", "3");
                    jSONObject1.put("orderTotal", orderMain.getOrder_total());
                    jSONObject1.put("ticketType", orderMain.getBalance_type());
                    int f_q = 1;//发券是否成功， 0成功 1失败 3重
                    int f_b = 1;//发贝是否成功， 0成功 1失败 3重
                    //不是会员不扣券
                    if (double_total > 0) {
                        f_q = user_quan(jSONObject1);
                    } else {
                        f_q = 0;
                    }
                    //退贝
                    f_b = user_coin(main_user_id, order_code, orderMain.getCoin_90(), 2, 7);
                    if ((f_q == 0 || f_q == 3) && (f_b == 0 || f_b == 3)) {
                        r_msg = "用户退券或退贝失败";
                        pf = true;//1成功
                    } else {
                        pf = false;//0错误
                    }
                } else {
                    r_msg = "未找到领取记录";
                }

            } else {
                //不需要退用户券贝
                pf = true;
            }


            if (pf) {
                //成功，改变订单状态
                Map<String, Object> pd = new HashMap<>();
                pd.put("code", order_code);
                pd.put("back_user_code", user_code);
                pd.put("state", 3);
                pd.put("back_code", return_info);
                pd.put("back_time", DateUtil.getTime());
                pd.put("old_order_total", orderMain.getOrder_total());    //保存退款前订单原始金额
                pd.put("old_gift_90", orderMain.getGift_90());            //保存退款前订单原始赠送90券
                pd.put("order_total", back_order_total);        //保存退款后订单原始金额与退款金额的数值差
                pd.put("gift_90", back_gift_90);                //保存退款后订单原始赠送90券与退款返还90券的数值差
                orderMainUnionService.updateWhereOrderMainUnion(pd);
            }
        }

        if(pf){
            result.setReturn_code(1);//0错误 1成功
            result.setReturn_info("****************************退款成功");
        }else{
            result.setReturn_code(0);//0错误 1成功
            result.setReturn_info("****************************" + r_msg);
        }

        return result;
    }

    /**
     * 字符元--转为整数分
     */
    public double money_convert(String money){
        int int_m = money_convert_Int(money);
        double back_money = Double.valueOf(int_m);
        return back_money;
    }

    /**
     * 字符元--转为int分
     */
    public int money_convert_Int(String moneyStr){
        int result = 0;
        String strAdd = "";
        if (!moneyStr.contains("."))
        {
            strAdd = ".00";
        } else if (".".equals(moneyStr.substring(moneyStr.length() - 1)))
        {
            strAdd = "00";
        } else if (".".equals(moneyStr.substring(moneyStr.length() - 2,moneyStr.length() - 1)))
        {
            strAdd = "0";
        }
        moneyStr = moneyStr + strAdd;
        result =Integer.parseInt(moneyStr.replace(".", ""));
        return result;
    }

    /**
     * 远程连接HttpURL
     * @param url
     * @return
     */
    protected String HttpURL(String url){
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
            logger.error("=远程连接HttpURL报错==" ,e.fillInStackTrace());
            return result;
        }
    }

    /**
     * 保存支付场景
     * type: 0:未知 1：微信 2：支付宝 3：易支付 4银行卡,8:银联钱包，5:百度钱包,6:京东钱包,7:拉卡拉钱包
     */
    protected int save_pay_scence(int type,String pay_user_id,String order_code) throws Exception{
        PayScene payScene = new PayScene();
        payScene.setMain_code(order_code);
        payScene.setType(type);
        payScene.setPay_user_id(pay_user_id);
        payScene.setScene_type(0);
        phoneUserService.insertPayScene(payScene);
        return payScene.getId();
    }



    /**
     * 生成关注带参二维码
     */
    protected String temporaryCode(int scene_id) throws Exception {
        String access_token = wxUtilService.getAccessToken();
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + access_token;
        JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl,
                "POST",
                "{\"expire_seconds\": 2592000,\"action_name\": \"QR_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": \"" + scene_id + "\"}}}");
        String back = "";
        if(jsonObject!=null && jsonObject.size()>0){
            String ticket = jsonObject.getString("ticket");
            if(StringUtils.isNotBlank(ticket)){
                back = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
            }
        }
        return back;
    }

    /**
     * 推送支付模板信息
     */
    protected void send_pay_template(int scene_id, String open_id,
                                     PorderMainUnion orderMainUnion) throws Exception {
        logger.error("=推送支付模板信息=open_id=" + open_id + "=scene_id=" + scene_id);
        wxUtilService.send_pay_template(scene_id, open_id, orderMainUnion);
    }

    /**
     * 推送退款模板信息（联盟商户）
     */
    protected void send_refund_template(String back_code,PorderMainUnion orderMainUnion,Integer int_total) throws Exception {

        logger.error("=推送退款模板信息=open_id=" + orderMainUnion.getOpen_id() + "=int_total=" + int_total);
        wxUtilService.send_refund_template(back_code,orderMainUnion.getCode(),orderMainUnion.getOpen_id(), int_total);
    }





    @Override
    public Result updateBalanceForJLBankBack(String total, String userCode,String order_code) throws Exception {
        Result result = new Result();
        if(!StringUtil.isNullOrEmpty(total)&&StringUtil.isNumeric(total))//金额校验，是否为正确金额
        {
            int cash_total=Integer.valueOf(total);
            TSysUser userOper=userDao.getById(TSysUser.class,userCode);//获取营业员对象
            if(userOper!=null)
            {
                Shop shop = shopService.getShopBySid(userOper.getIdentityCode());//获取店铺
                TBaseBrand brand=brandDao.getById(TBaseBrand.class,shop.getBrand_code());//获取商户

                int scene_id = 0;
                double jl_total=0;
                double proportion=1.0;
                if(!StringUtil.isNullOrEmpty(brand.getProportion()))//获取商家自动发券比例
                {proportion=brand.getProportion();}
                jl_total = proportion*cash_total;//计算应该发多少券
                scene_id = save_pay_scence(5,"",order_code);
                save_pay_scenceDetail(scene_id,cash_total,jl_total,brand.getBrandCode(),shop.getSid(),userOper.getId(),brand.getCommission());
                String link = "";
                link = temporaryCode(scene_id);
                result.setReturn_code(1);
                result.setReturn_info(link+",JLBANK");
                return result;
            }
            else
            {
                result.setReturn_code(0);
                result.setReturn_info("操作失败，没有该收银员!");
                return result;
            }

        }else
        {
            result.setReturn_code(0);
            result.setReturn_info("操作失败，金额错误!");
            return result;
        }

    }

    @Override
    public int checkPayScene(String order_code) throws Exception {
        Map<String,String> map=new HashMap<>();
        map.put("order_code",order_code);

        List<Map<String,Object>> list= (List<Map<String, Object>>) dao.findForList("PhoneUserDao.findPaySceneByOrderCode",map);
        return list.size();
    }

    private void save_pay_scenceDetail(int scene_id, int cash_total, double jl_total, String brandCode, String sid, String userId, BigDecimal commission) {
        TpaySceneDetail detail=new TpaySceneDetail();
        detail.setBrandId(brandCode);
        detail.setShopId(sid);
        detail.setUserId(userId);
        detail.setCommission(commission.doubleValue());
        detail.setMainId(scene_id);
        detail.setPoint90((int)jl_total);
        detail.setOrderTotal((double)cash_total);
        paySceneDetailDao.save(detail);
    }


    /**
     *
     * @return
     */
    @Override
    public Map<String, String> getPeriodizationLinkUrl(double totals, String shopId,String orderId, String brandCode, String pay_userId)
    {
        Map<String, String> map = new HashMap<>();
        String link = "";
        String mainId = "";
        //获取商户分期信息

        try{
            List<PperiodizationSet> list = (List<PperiodizationSet>)dao.
                    findForList("PhoneUserDao.getPeriodizationListByBrandCode",brandCode);
            if(list!= null && list.size() > 0)
            {
                TPeriodizationMain tPeriodizationMain = new TPeriodizationMain();
                mainId = "9900"+getRndNumCode();
                Date date = new Date();
                tPeriodizationMain.setId(mainId);
                tPeriodizationMain.setOrderId(orderId);
                tPeriodizationMain.setCreateTime(date);
                tPeriodizationMain.setState(0);
                tPeriodizationMain.setIsRelated(0);
                tPeriodizationMain.setIsdel(0);
                tPeriodizationMain.setCouponTotal(totals);
                tPeriodizationMain.setShopId(shopId);

                double totalsNow = 0;
                int days = 0;//周期
                for(int i = 0; i <list.size(); i++)
                {
                    TPeriodizationDetail tPeriodizationDetail = new TPeriodizationDetail();

                    tPeriodizationDetail.setId(UuidUtil.get32UUID());
                    tPeriodizationDetail.setMainId(mainId);
                    tPeriodizationDetail.setCreateTime(date);
                    tPeriodizationDetail.setThisTerm(i+1);
                    tPeriodizationDetail.setState(0);
                    tPeriodizationDetail.setIsdel(0);
                    for(PperiodizationSet pperiodizationSet : list)
                    {
                        if((""+(i+1)).equals(pperiodizationSet.getTermNum()))//对应上期数
                        {
                            days = Integer.valueOf(pperiodizationSet.getCycleDays()).intValue();
                            double thisTotal = 0 ;
                            if((i+1) == list.size())//末尾减
                            {
                                thisTotal = totals - totalsNow;
                            }else{
                                thisTotal = totals * (Double.valueOf(pperiodizationSet.getScale()));

                                BigDecimal   b   =   new   BigDecimal(thisTotal);
                                thisTotal  =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                                totalsNow += thisTotal;
                            }
                            tPeriodizationDetail.setThisTotal(thisTotal);
                            break;
                        }
                    }
                    //设置开始时间结束时间
                    tPeriodizationDetail.setThisStartTime(
                            getDateLateByDateAndDays(date,(i)*days));
                    tPeriodizationDetail.setThisEndTime(
                            getDateLateByDateAndDays(date,(i+1)*days));

                    //保存字表
                    tPeriodizationDetailDao.save(tPeriodizationDetail);
                }
                //保存主表
                tPeriodizationMainDao.save(tPeriodizationMain);
                //将mainId存入扫码表中
                PayScene payScene = new PayScene();
                payScene.setMain_code(mainId);
                payScene.setType(5);
                payScene.setPay_user_id(pay_userId);
                payScene.setScene_type(2);//分期首次付款
                dao.save("PhoneUserDao.insertPayScene", payScene);
                //生成二维码
                link = temporaryCode(payScene.getId());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        map.put("link",link);
        map.put("mainId",mainId);

        return map;
    }


    //根据时间和期数获取时间
    private Date getDateLateByDateAndDays(Date dateTime, int days)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(dateTime);
        c.add(Calendar.DAY_OF_MONTH, days);//添加天数

        return c.getTime();
    }


    /**
     *
     */
    @Override
    public Result shopGetQrcodeByShopId(String shopId)throws Exception{
        //验证门店所在的商户是否支持分期
        Result result = new Result();
        String brandCode = (String)dao.findForObject("WxtsDao.getBrandCodeByShopId",shopId);

        TBaseBrand tBaseBrand = brandDao.getById(TBaseBrand.class, brandCode);

        if(tBaseBrand == null || tBaseBrand.getIsPeriodization() == null || tBaseBrand.getIsPeriodization().intValue() == 0)
        {
            result.setReturn_code(0);//失败
            result.setReturn_info("商户不支持分期");
            return result;
        }

        String qrcode_url = "";
        String mainId = "9901"+getRndNumCode();
        TperiodizationShop tperiodizationShop = new TperiodizationShop();

        tperiodizationShop.setId(mainId);
        tperiodizationShop.setShopId(shopId);
        tperiodizationShop.setCreateTime(new Date());
        tperiodizationShop.setState(0);

        tperiodizationShopDao.save(tperiodizationShop);
        result.setReturn_code(1);//成功、
        result.setMainId(mainId);

        //保存扫码信息
        PayScene payScene = new PayScene();
        payScene.setMain_code(mainId);
        payScene.setType(5);
        payScene.setPay_user_id("");
        payScene.setScene_type(3);//分期后续
        dao.save("PhoneUserDao.insertPayScene", payScene);

        qrcode_url = temporaryCode(payScene.getId());
        result.setQrcode_url(qrcode_url);
        return result;
    }


    @Override
    public Result getQrcodeStateAndInfo(String mainId)throws Exception{
        Result result = new Result();
        if(StringUtil.isNullOrEmpty(mainId) )
        {
            result.setReturn_code(0);//
            result.setReturn_info("参数不能为空");
            return result;
        }
        String logId = (String)dao.findForObject("PhoneUserDao.getLogIdByMainId",mainId);
        TPeriodizationDetailLog tPeriodizationDetailLog = tPeriodizationDetailLogDao.getById(TPeriodizationDetailLog.class,logId);

        if(tPeriodizationDetailLog == null )
        {
            result.setReturn_code(0);
            result.setReturn_info("用户未扫码或正在处理，请稍后重试");
            return result;
        }

        result.setReturn_code(1);
        result.setReturn_info("扫码领取成功");
        result.setReturn_data(tPeriodizationDetailLog);
        return result;
    }

}
