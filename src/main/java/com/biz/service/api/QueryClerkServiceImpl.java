package com.biz.service.api;

import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Pmodel.Pactivity;
import com.biz.model.Pmodel.PfirstBalance;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.*;
import com.biz.model.Pmodel.basic.Base90Detail;
import com.biz.model.Pmodel.offlineCard.OfflineCardDetail;
import com.biz.model.Pmodel.offlineCard.PofflineCard;
import com.biz.model.Pmodel.weixin.UserInfo;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.ConfigUtil;
import com.framework.utils.StringUtil;
import com.framework.utils.Tools;
import com.framework.utils.UuidUtil;
import com.framework.utils.weixin.HttpRequestUtil;
import com.framework.utils.weixin.WechatAccessToken;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.biz.service.activity.ActivityServiceImpl.ListToString_Quotes;

/**
 * Created by lzq on 2017/2/4.
 */

@Service("queryClerkService")
public class QueryClerkServiceImpl extends BaseServiceImpl<TorderMain> implements QueryClerkServiceI  {
    @Resource(name = "daoSupport")
//    protected Logger logger = Logger.getLogger(this.getClass());
    private DaoSupport dao;

    @Override
    public List<HashMap<String, Object>> queryClerkByShopWithPhone(String shop_id) throws Exception {
        return (List<HashMap<String,Object>>) dao.findForList("QueryClerkDao.queryClerkByShopWithPhone",shop_id);
    }

    @Override
    public int findLoginName(String login_name)throws Exception{
        return (int)dao.findForObject("QueryClerkDao.findLoginName",login_name);
    }

    @Override
    public int getUserCountByPhone(String phone) throws Exception {
        if(StringUtils.isNotBlank(phone))
        {
            return (int) dao.findForObject("QueryClerkDao.getUserCountByPhone", phone);
        }else{
            return 0;
        }
    }

    @Override
    public void insertShopUser(User clerk_user) throws Exception {
        dao.save("QueryClerkDao.insertShopUser", clerk_user);
    }

    @Override
    public void updateClerkUserLock(User clerk_user) throws Exception {
        dao.update("QueryClerkDao.updateUserLock",clerk_user);
    }

    @Override
    public String delClerkByCode(String user_code) throws Exception{
        dao.update("QueryClerkDao.delUserByCode",user_code);
        return  "success";
    }

    @Override
    public User getUserByCode(String user_code) throws Exception {
        return (User) dao.findForObject("QueryClerkDao.getUserByCode",user_code);
    }

    @Override
    public void updateUserPhone(User user) throws Exception {
        dao.update("QueryClerkDao.updateUserPhone",user);
    }

    @Override
    public void updateUserPwd(User user) throws Exception {
        dao.update("QueryClerkDao.updateUserPwd",user);
    }

    @Override
    public OrderMain90 getOrderMain90ByCode(String order_code) throws Exception {
        return (OrderMain90) dao.findForObject("QueryClerkDao.getOrderMain90ByCode", order_code);
    }

    @Override
    public String getSysUserIdByOpenId(String open_id) throws Exception {
        return (String) dao.findForObject("QueryClerkDao.getSysUserIdByOpenId", open_id);
    }

    @Override
    public void insertOrderMain90(OrderMain90 orderMain90) throws Exception {
        dao.save("QueryClerkDao.insertOrderMain90", orderMain90);
    }

    @Override
    public BaseUser getBaseUserByOpen_id(String open_id) throws Exception {
        return (BaseUser) dao.findForObject("QueryClerkDao.getBaseUserByOpen_id", open_id);
    }


    @Override
    public void send_pay_template(String open_id,String code,double pay, String pay_time) throws Exception {
        String pay_template = ConfigUtil.get("PAY_TEMPLATE");
        String url = ConfigUtil.get("weixinShop_URL") + "client_userPoint90.do?ordercode="+code;
        JSONObject jsonObject = new JSONObject();
        String token ="";
        String remark = "更多详情请点击！";
        try {
            //发送模板消息
            //		您好，您已成功消费。
            //		{{productType.DATA}}：{{name.DATA}}
            //		消费{{accountType.DATA}}：{{account.DATA}}
            //		消费时间：{{time.DATA}}
            //		{{remark.DATA}}
            String sendUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
                    + token;
            String sendString = "{"+
                    "\"touser\":\""+open_id+"\","+
                    "\"template_id\":\""+pay_template+"\","+
                    "\"url\":\""+url+"\","+
                    "\"data\":{"+
                    "\"productType\": {"+
                    "\"value\":\"扣除久零券\","+
                    "\"color\":\"#000000\""+
                    "},"+
                    "\"name\": {"+
                    "\"value\":\"0元\","+
                    "\"color\":\"#2C12EA\""+
                    "},"+
                    "\"accountType\": {"+
                    "\"value\":\"金额\","+
                    "\"color\":\"#000000\""+
                    "},"+
                    "\"account\": {"+
                    "\"value\":\""+pay/100+"元\","+
                    "\"color\":\"#2C12EA\""+
                    "},"+
                    "\"time\": {"+
                    "\"value\":\""+pay_time+"\","+
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
            jsonObject.put("errmsg", msg);
            e.printStackTrace();
        }
    }

    @Override
    public BaseUser getBaseUserByPhone(String phone) throws Exception {
        return (BaseUser) dao.findForObject("BaseUserDao.getBaseUserByPhone", phone);
    }

    @Override
    public Brand getBrandOnlyByCode(String brand_code) throws Exception {
        return (Brand) dao.findForObject("BaseUserDao.getBrandOnlyByCode", brand_code);
    }

    @Override
    public void insertRgGift(RgGift rgGift) throws Exception {
        dao.save("BaseUserDao.insertRgGift", rgGift);
    }

    @Override
    public void add_balance_90(Map<String, Object> map) throws Exception {
        dao.update("BaseUserDao.add_balance_90", map);
    }

    @Override
    public void minus_balance_90(Map<String, Object> map) throws Exception {
        dao.update("BaseUserDao.minus_balance_90", map);
    }

    /**
     * 增加当前透支额度
     */
    @Override
    public void add_credit_now_90(Map<String, Object> map) throws Exception {
        dao.update("BaseUserDao.add_credit_now_90", map);
    }

    @Override
    public void insertBase90Detail(Base90Detail base90Detail) throws Exception {
        dao.save("BaseUserDao.insertBase90Detail", base90Detail);
    }

    @Override
    public void updateWhereRgGift(Map<String, Object> map) throws Exception {
        dao.update("BaseUserDao.updateWhereRgGift", map);
    }

    @Override
    public RgGift getRgGiftByCode(String rg_code) throws Exception {
        return (RgGift) dao.findForObject("BaseUserDao.getRgGiftByCode", rg_code);
    }

    @Override
    public void send_kf_template(String open_id, String content) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            String token = WechatAccessToken.getOldAccessToken();
            String jsonString="{\"touser\":\""+open_id+"\","+
                    "\"msgtype\":\"text\","+
                    "\"text\":{"+
                    "\"content\": \""+content+"\"" +
                    "}"+
                    "}";
            String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + token;
            jsonObject = HttpRequestUtil.httpRequest(requestUrl,"POST", jsonString);
        } catch (Exception e) {
            String msg = e.getMessage();
            if(msg.length()>100){
                msg = msg.substring(0, 99);
            }
            jsonObject.put("errmsg", msg);
        }
    }

    /**
     * 客服消息接口
     * 发送图文消息（点击跳转到外链） 图文消息条数限制在8条以内，注意，如果图文数超过8，则将会无响应。
     */
    public void kf_reply_news(String open_id,String title,String content,String cover,String url){
        String token = WechatAccessToken.getOldAccessToken();
        String jsonString="{\"touser\":\""+open_id+"\","+
                "\"msgtype\":\"news\","+
                "\"news\":{"+
                "\"articles\":[{"+
                "\"title\": \""+title+"\"," +
                "\"description\": \""+content+"\"," +
                "\"url\": \""+url+"\"," +
                "\"picurl\": \""+cover+"\"" +
                "}]"+
                "}"+
                "}";
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + token;
        JSONObject jsonObject = HttpRequestUtil.httpRequest(requestUrl,"POST", jsonString);
//        return jsonObject;
    }

    @Override
    public JSONObject oper_balance_90ByWX(Map<String, Object> map) throws Exception {
        JSONObject jSONObject = new JSONObject();
        List<Map<String,String>> checkMap=(List<Map<String, String>>) dao.findForList("BaseUserDao.getUserDetail90ByWx", map);
        Map<String,Object> checkBalance=(Map<String, Object>) dao.findForObject("BaseUserDao.checkUser90Balance", map);
        boolean isBalance=false;
        if(checkBalance!=null)
        {

            String ss=checkBalance.get("balance").toString();
            //Integer balance=Integer.valueOf(ss);
            double ss_double=Double.parseDouble(ss);
            int balance=(int)ss_double;
            isBalance=balance>=0;


        }
        if(checkMap!=null&&checkMap.size()>0)
        {
            jSONObject.put("flag", "1");
            jSONObject.put("msg", "操作失败，重复调用");
        }else if(!isBalance)
        {
            jSONObject.put("flag", "2");
            jSONObject.put("msg", "操作失败，余额不足");
        }
        else
        {
            int res=(int) dao.update("BaseUserDao.operBalance90ByWx", map);
            if(res>0)
            {
                dao.save("BaseUserDao.saveDetail90ByWx", map);
                jSONObject.put("flag", "0");
                jSONObject.put("msg", "操作成功");
            }
            else
            {
                jSONObject.put("flag", "1");
                jSONObject.put("msg", "操作失败,找不到open_id对应用户");
            }
        }

        return jSONObject;
    }

    @Override
    public void add_auto_balance(String brand_code, String shop_code, String source_code, String open_id, double balance_90, String user_code) throws Exception {
        Base90Detail base90Detail = new Base90Detail();
        base90Detail.setBrand_code(brand_code);
        base90Detail.setShop_code(shop_code);
        base90Detail.setSource_code(source_code);
        base90Detail.setOpen_id(open_id);
        base90Detail.setSource(1);
        base90Detail.setSource_msg("关注绑定发券");
        int point_90 = (int) (balance_90*-1);
        base90Detail.setPoint_90(point_90);
        base90Detail.setUser_code(user_code);
        dao.save("BaseUserDao.insertBase90Detail", base90Detail);
    }

    @Override
    public void add_rg_balance(String brand_code, String shop_code, String source_code, String open_id, double balance_90, String user_code) throws Exception {
        /******************商户扣除90券**************************/
        Brand brand = getBrandOnlyByCode(brand_code);
        long brand_balance_90 = brand.getBalance_90();//九零券余额（分）
        long credit_total_90 = brand.getCredit_total_90();//久零券透支额度（分）
        long credit_now_90 = brand.getCredit_now_90();//久零券当前透支额度（分）
        if((brand_balance_90 + credit_total_90 - credit_now_90) <  balance_90){
            return;
        }
        if(brand_balance_90>=balance_90){
            Map<String,Object> pd = new HashMap<>();
            pd.put("brand_code", brand_code);
            pd.put("balance_90", balance_90);
            minus_balance_90(pd);
        }else{
            long minus = (long) (balance_90 - brand_balance_90);
            Map<String,Object> pd = new HashMap<>();
            pd.put("brand_code", brand_code);
            pd.put("minus", minus);
            add_credit_now_90(pd);
        }
        /******************发券记录**************************/
        Base90Detail base90Detail = new Base90Detail();
        base90Detail.setBrand_code(brand_code);
        base90Detail.setShop_code(shop_code);
        base90Detail.setSource_code(source_code);
        base90Detail.setOpen_id(open_id);
        base90Detail.setSource(2);
        base90Detail.setSource_msg("关注人工发券");
        int point_90 = (int) (balance_90*-1);
        base90Detail.setPoint_90(point_90);
        base90Detail.setUser_code(user_code);
        insertBase90Detail(base90Detail);
    }

    @Override
    public void add_balance_90(String open_id, double balance_90) throws Exception {
        Map<String,Object> pd = new HashMap<>();
        pd.put("open_id", open_id);
        pd.put("balance_90", balance_90);
        add_balance_90(pd);
    }

    @Override
    public void send_pay_template(int scene_id, String open_id, OrderMainUnion orderMainUnion) throws Exception {
        send_pay_template(scene_id, open_id, orderMainUnion);
    }

    @Override
    public OrderMainUnion getOrderMainUnionByCode(String orderMainId) throws Exception {
        return (OrderMainUnion) dao.findForObject("OrderMainUnionDao.getOrderMainUnionByCode", orderMainId);
    }

    @Override
    public boolean addActivityListDate(String openid) throws Exception {
        boolean pd=false;
        if(Tools.notEmpty(openid)){
            //查询用户信息
            try {
                List<BaseUser> userList=getselectUsermsgOpen_id(openid);
                if(userList.size()==0){

                    //读取用户信息，或新增用户
                    BaseUser y_user= adduser(openid);

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
                        List<PfirstBalance> firstBalance_tmp=(List<PfirstBalance>)dao.findForList("FirstBalanceDao.getfirstBalanceMapList",mapidListdate);

                        for(PfirstBalance first:firstBalance_tmp){
                            //增加积分
                            if(y_user!=null){
                                doFirstBalance(openid,first.getBalance_90(),first.getId());
                            }
                        }
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  pd;
    }

    /**
     * 获取
     */
    public List<BaseUser> getselectUsermsgOpen_id(String open_id) throws Exception
    {
        Map<String,Object> select_map=new HashMap<>();
        select_map.put("open_id",open_id);
        return (List<BaseUser>) dao.findForList("BaseUserDao.selectUsermsg", select_map);
    }

    //读取用户信息，如果没注册，则自动注册
    private BaseUser adduser(String openid) throws Exception {
        //加券接口
        BaseUser y_user =getBaseUserByOpen_id(openid);
        //新用户
        if(y_user == null){
            y_user = add_new_user(openid);
        }

        return y_user;
    }

    public BaseUser add_new_user(String open_id){
        UserInfo userInfo = WechatAccessToken.getUserInfo(open_id);
        BaseUser user = new BaseUser();
        //logger.error("=关注带参二维码成为新用户open_id="+open_id+"=userInfo="+userInfo);
        if(userInfo == null){
            return null;
        }
        try {
            user.setOpen_id(open_id);
            user.setCover(userInfo.getHeadimgurl());
            user.setPerson_name(StringUtil.filterEmoji(userInfo.getNickname()));
            user.setState(userInfo.getSubscribe());
            insertBaseUser(user);
        } catch (Exception e) {
            //logger.error("=关注带参二维码成为新用户报错=",e.fillInStackTrace());
            return null;
        }
        return user;
    }

    /**
     * 添加
     */
    public void insertBaseUser(BaseUser baseUser) throws Exception
    {
        dao.save("BaseUserDao.insertBaseUser", baseUser);
    }

    /**
     * 满足当前的活动
     * @param type  1:只查询"关注送活动"
     * @return
     * @throws Exception
     */
    private List<Pactivity> ActivityListDate(int type) throws Exception{
        List<Pactivity> activity =new ArrayList<Pactivity>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        Map<String,Object> selectmap=new HashMap<>();
        selectmap.put("type",type);
        selectmap.put("nowTime",df.format(new Date()));
        if(type==1){
            //关注送活动
            activity=(List<Pactivity>)dao.findForList("ActivityDao.ActivityList",selectmap);
        }

        return activity;


    }

    //首次关注发久零券
    private JSONObject doFirstBalance(String open_id,int balance_90,String source_code)
    {
        JSONObject jSONObject = new JSONObject();
        System.out.print("进入扫带参二维码发劵方法");
        try {

            /******************增加用户90券**************************/
            add_balance_90(open_id,balance_90);

            /******************发券记录**************************/
            Base90Detail base90Detail = new Base90Detail();
            base90Detail.setSource_code(source_code);
            base90Detail.setOpen_id(open_id);
            base90Detail.setSource(6);
            base90Detail.setSource_msg("首次关注发券");
            base90Detail.setPoint_90(balance_90);
            base90Detail.setBrand_code(null);
            base90Detail.setShop_code(null);
            base90Detail.setUser_code(null);
            dao.save("BaseUserDao.insertBase90UserDetail", base90Detail);


            String url =ConfigUtil.get("weixinShop_URL")+"client_toClientCenter.do";
            String title= "亲，恭喜你成功领取了"+balance_90/100+"元久零券";
            String content = "点此进入个人中心查看！";
            kf_reply_news(open_id, title, content, "", url);
            jSONObject.put("return_code", "1");
            jSONObject.put("return_info","发劵成功!");
            return jSONObject;
        } catch (Exception e) {
            jSONObject.put("return_code", "0");
            jSONObject.put("return_info","调用出错");
            return jSONObject;
        }
    }

    @Override
    public JSONObject sign_addBalance(Map<String, Object> map) throws Exception {
        JSONObject jSONObject = new JSONObject();
        List<Map<String,String>> checkMap=(List<Map<String, String>>) dao.findForList("BaseUserDao.getUserDetail90ByWx", map);
        if(checkMap!=null&&checkMap.size()>0)
        {
            jSONObject.put("flag", "1");
            jSONObject.put("msg", "操作失败，重复调用");
        }
        else
        {
            int res=(int) dao.update("BaseUserDao.operBalance90ByWx", map);
            if(res>0)
            {
                dao.save("BaseUserDao.saveDetail90ByWx", map);
                jSONObject.put("flag", "0");
                jSONObject.put("msg", "操作成功");
            }
            else
            {
                jSONObject.put("flag", "1");
                jSONObject.put("msg", "操作失败,找不到open_id对应用户");
            }
        }

        return jSONObject;
    }

    @Override
    public OfflineCardDetail getOfflineCardDetailByCard_code(String card_code) throws Exception {
        return (OfflineCardDetail) dao.findForObject("BaseUserDao.getOfflineCardDetailByCard_code", card_code);
    }

    @Override
    public PofflineCard getOfflineCardByCode(String main_code) throws Exception {
        return (PofflineCard) dao.findForObject("BaseUserDao.getOfflineCardByCode", main_code);
    }

    @Override
    public void updateWhereOfflineCardDetail(Map<String, Object> map) throws Exception {
        dao.update("BaseUserDao.updateWhereOfflineCardDetail", map);
    }

    @Override
    public void add_card_use_count(String main_code) throws Exception {
        dao.update("BaseUserDao.add_card_use_count", main_code);
    }

    @Override
    public List<Base90Detail> selectOrtherBalanceIn(Map<String, Object> map) throws Exception {
        return (List<Base90Detail>) dao.findForList("BaseUserDao.selectOrtherBalanceIn", map);
    }

    @Override
    public int int_sum(Map<String, Object> map) throws Exception {
        Object o = dao.findForObject("BaseUserDao.int_sum", map);
        return (int) (o != null ? o : 0);
    }

    @Override
    public List<Base90Detail> selectOrtherBalanceOut(Map<String, Object> map) throws Exception {
        return (List<Base90Detail>) dao.findForList("BaseUserDao.selectOrtherBalanceOut", map);
    }

    @Override
    public int out_sum(Map<String, Object> map) throws Exception {
        Object o = dao.findForObject("BaseUserDao.out_sum", map);
        return (int) (o != null ? o : 0);
    }

    @Override
    public void updateBaseUserOnlyCode(Map<String, Object> map) throws Exception {
        dao.update("BaseUserDao.updateBaseUserOnlyCode", map);
    }

    @Override
    public List<Base90Detail> selectWhereBase90Detail(Map<String, Object> map) throws Exception {
        return (List<Base90Detail>) dao.findForList("BaseUserDao.selectWhereBase90Detail", map);
    }

    @Override
    public List<OrderMain90> selectWhereOrderMain90(Map<String, Object> map) throws Exception {
        return (List<OrderMain90>) dao.findForList("OrderMain90Dao.selectWhereOrderMain90",map);
    }

    @Override
    public Map<String, Object> selectWhereOrderMain90Count(Map<String, Object> map) throws Exception {
        return (Map<String,Object>) dao.findForObject("OrderMain90Dao.selectWhereOrderMain90Count",map);
    }

    @Override
    public int getCountOrderMain90(Map<String, Object> map) throws Exception {
        Object o = dao.findForObject("OrderMain90Dao.getCountOrderMain90",map);
        return (int)(o!=null?o:0);
    }

    @Override
    public void checkUserCoin(Map<String, Object> map) throws Exception {
        int userCount= (int) dao.findForObject("BaseUserDao.findUserId",map);
        int userCount2= (int) dao.findForObject("BaseUserDao.findUserCoinId",map);
        if(userCount>0&&userCount2<=0)
        {
            map.put("userCoinId", UuidUtil.get32UUID());
            dao.save("BaseUserDao.saveUserCoin",map);
        }
    }

    @Override
    public JSONObject oper_coin_90(Map<String, Object> map) throws Exception {
            JSONObject jSONObject = new JSONObject();
            List<Map<String,String>> checkMap=(List<Map<String, String>>) dao.findForList("BaseUserDao.getbalanceSheet90ByWx", map);
            Map<String,Object> checkBalance=(Map<String, Object>) dao.findForObject("BaseUserDao.checkUser90Coin", map);
            boolean isBalance=false;
            String source=map.get("source").toString();
            Double amount=Double.valueOf(map.get("amount").toString());
            if(checkBalance!=null)
            {
                Double chargeAmount=Double.valueOf(checkBalance.get("chargeAmount").toString());
                Double giveAmount=Double.valueOf(checkBalance.get("giveAmount").toString());
                if("2".equals(source))//购买的时候计算总金额够不都
                {
                    isBalance=true;//chargeAmount+giveAmount>=amount;//新的逻辑，久零贝不够的时候先扣除，然后返回提示要缴纳多少现金
                }else if("5".equals(source))
                {
                    isBalance=chargeAmount>=amount;//提现的时候计算充值金额够不够
                }else if("7".equals(source))
                {
                    isBalance=giveAmount>=amount;//提现的时候计算充值金额够不够
                }else
                {isBalance=true;}

            }
            if(checkMap!=null&&checkMap.size()>0)
            {
                jSONObject.put("flag", "1");
                jSONObject.put("msg", "操作失败，重复调用");
            }else if(!isBalance)
            {
                jSONObject.put("flag", "2");
                jSONObject.put("msg", "操作失败，余额不足");
            }
            else//开始操作90币
            {String url = ConfigUtil.get("weixinShop_URL")+"client_toClientCenter.do";
                String content = "点此进入个人中心查看！";
                String openId=getOpenIdBySysUserId(map.get("userId").toString());
                String title="";
                if("1".equals(source))//充值
                {
                    jSONObject=doOperCoin("BaseUserDao.doRechargeCoin","用户充值","0",map,"IN");

                    /******************向用户发送模板消息************************/
                    title= "亲，恭喜你 成功充值了"+map.get("amount")+"元久零贝";

                }else if("2".equals(source))//消费
                {
                    Double chargeAmount=Double.valueOf(checkBalance.get("chargeAmount").toString());
                    Double giveAmount=Double.valueOf(checkBalance.get("giveAmount").toString());

                    Double amoutL=amount;//用于计算的需要消费的金额变量
                    Double chargeCut=0.0;//要扣除的充值金额
                    Double giveCut=0.0;//要扣除的赠送金额
                    Double needMoney=0.0;//要额外缴纳的金额


                    if(giveAmount>amount)//全部用赠送金额消费
                    {
                        giveCut=amount;
                        chargeCut=0.0;
                        needMoney=0.0;
                    }else //赠送金额不够，混合支付
                    {
                        giveCut=giveAmount;//赠送金额全部扣完;
                        amoutL=amoutL-giveCut;//减去赠送金额的钱
                        if(amoutL<=chargeAmount)//如果此时充值金额够，直接扣除完毕
                        {
                            chargeCut=amoutL;
                            needMoney=0.0;
                        }else//否则，全部扣除充值金额并且计算需要缴纳多少现金
                        {
                            chargeCut=chargeAmount;
                            amoutL=amoutL-chargeCut;
                            needMoney=amoutL;
                        }
                    }
                    Map<String, Object> map1=new HashMap<>();
                    Map<String, Object> map2=new HashMap<>();
                    if(giveCut>0)//如果有需要扣除的赠送金额，进行扣除
                    {
                        map1.put("userId",map.get("userId"));
                        map1.put("state",map.get("state"));
                        map1.put("orderNum",map.get("orderNum"));
                        map1.put("source",map.get("source"));
                        map1.put("amount",giveCut);
                        jSONObject=doOperCoin("BaseUserDao.doCutCoinGive","用户消费","1",map1,"OUT");

                    }
                    if(chargeCut>0)//如果有需要扣除的充值金额，进行扣除
                    {
                        map2.put("userId",map.get("userId"));
                        map2.put("state",map.get("state"));
                        map2.put("orderNum",map.get("orderNum"));
                        map2.put("source",map.get("source"));
                        map2.put("amount",chargeCut);
                        jSONObject=doOperCoin("BaseUserDao.doCutCoin","用户消费","0",map2,"OUT");
                    }
                    jSONObject.put("flag", "0");
                    jSONObject.put("msg", "操作成功");
                    jSONObject.put("needMoney",needMoney);




                    /******************向用户发送模板消息************************/
                    title= "亲，恭喜你 成功消费了"+(giveCut+chargeCut)+"元久零贝";
                }else if("3".equals(source))//赠送
                {
                    jSONObject=doOperCoin("BaseUserDao.doGiveCoin","赠送久零贝","1",map,"IN");

                    /******************向用户发送模板消息************************/
                    title= "亲，恭喜你 赠送了"+map.get("amount")+"元久零贝";
                }else if("4".equals(source))//退款
                {
                    Map<String, Object> map1=new HashMap<>();
                    Map<String, Object> map4=new HashMap<>();
                    Map<String, Object> map6=new HashMap<>();
                    Map<String, Object> map2= (Map<String, Object>) dao.findForObject("BaseUserDao.findSheetByMap",map);
                    Map<String, Object> map3= (Map<String, Object>) dao.findForObject("BaseUserDao.findSheetByMapGive",map);
                    Map<String, Object> map5= (Map<String, Object>) dao.findForObject("BaseUserDao.findSheetByMapExtra",map);
                    Double amounttk=0.0;
                    if(map2!=null) {
                        map1.put("userId", map.get("userId"));
                        map1.put("state", map.get("state"));
                        map1.put("orderNum", map.get("orderNum"));
                        map1.put("source", map.get("source"));
                        map1.put("amount", map2.get("amount"));
                        amounttk+=Double.valueOf(map2.get("amount").toString());
                        jSONObject = doOperCoin("BaseUserDao.doRechargeCoin", "用户退款", "0", map1,"IN");
                    }
                    if(map3!=null) {

                        map4.put("userId", map.get("userId"));
                        map4.put("state", map.get("state"));
                        map4.put("orderNum", map.get("orderNum"));
                        map4.put("source", map.get("source"));
                        map4.put("amount", map3.get("amount"));
                        amounttk+=Double.valueOf(map3.get("amount").toString());
                        jSONObject = doOperCoin("BaseUserDao.doGiveCoin", "用户退款", "1", map4,"IN");
                    }	if(map5!=null) {
                    map6.put("userId", map.get("userId"));
                    map6.put("state", map.get("state"));
                    map6.put("orderNum", map.get("orderNum"));
                    map6.put("source", map.get("source"));
                    map6.put("amount", map5.get("amount"));
                    amounttk+=Double.valueOf(map5.get("amount").toString());
                    jSONObject = doOperCoin("BaseUserDao.doGiveCoinExtra", "用户退款", "2", map4,"IN");
                }
                    if(map2==null&&map3==null&&map5==null)
                    {
                        jSONObject.put("flag", "0");
                        jSONObject.put("msg", "操作成功，不需要退币");
                    }
                    /******************向用户发送模板消息************************/
                    title= "亲，恭喜你 成功退款"+amounttk+"元久零贝";
                }else if("5".equals(source))//提现
                {
                    jSONObject=doOperCoin("BaseUserDao.doCutCoin","用户提现","0",map,"OUT");

                    /******************向用户发送模板消息************************/
                    title= "亲，恭喜你 成功提现"+map.get("amount")+"元久零贝";

                }else if("6".equals(source))//提现
                {
                    jSONObject=doOperCoin("BaseUserDao.doRechargeCoin","提现失败退回","0",map,"IN");

                    /******************向用户发送模板消息************************/
                    title= "亲，提现审核未通过，"+map.get("amount")+"元久零贝已经返回您的余额";

                }
                else if("7".equals(source))//手动发券撤回
                {

                    Map<String, Object> map2= (Map<String, Object>) dao.findForObject("BaseUserDao.findSheetByGive",map);
                    /******************向用户发送模板消息************************/
                    Double amountGive= Double.valueOf(map2.get("amount").toString());
                    if(Math.abs(amount-amountGive)<0.001)
                    {
                        jSONObject=doOperCoin("BaseUserDao.doCutCoinGive","手动发券撤回","1",map,"OUT");
                    }
                    else{
                        jSONObject.put("flag", "1");
                        jSONObject.put("msg", "金额不对，撤回失败");
                    }

                    title= "亲，久零券发放撤回，赠送的"+map.get("amount")+"元久零贝已经从您的余额扣除";

                }
                kf_reply_news(openId,title,content,"",url);
            }

            return jSONObject;

        }

    @Override
    public List<String> getUserBalanceDetailListByOpenId(String open_id) throws Exception {
        Map<String,String> map=new HashMap<>();
        map.put("openId",open_id);
        return (List<String>) dao.findForList("BaseUserDao.getUserBalanceDetailListByOpenId",map);
    }

    public String getOpenIdBySysUserId(String userid)throws Exception
    {
        return (String) dao.findForObject("BaseUserDao.getOpenIdBySysUserId", userid);
    }

    private JSONObject doOperCoin(String sql, String note, String type,Map<String,Object> map,String inOut) throws Exception {
        JSONObject jSONObject = new JSONObject();
        int res=(int) dao.update(sql, map);
        if(res>0)
        {

            String id= UuidUtil.get32UUID();
            String serialNum=/*"JN_"+inOut+"_"+*/getRndNumCode()+"";
            map.put("id",id);
            map.put("serialNum",serialNum);
            map.put("note",note);
            map.put("type",type);
            dao.save("BaseUserDao.saveSheet90", map);
            jSONObject.put("flag", "0");
            jSONObject.put("msg", "操作成功");
        }
        else
        {
            jSONObject.put("flag", "1");
            jSONObject.put("msg", "操作失败,找不到open_id对应用户");
        }
        return  jSONObject;
    }

    public long getRndNumCode()
    {
        Random rand = new Random();
        Date now = new Date();
        long t = now.getTime();
        return Long.valueOf(t * 1000L + (long)rand.nextInt(1000));
    }
}
