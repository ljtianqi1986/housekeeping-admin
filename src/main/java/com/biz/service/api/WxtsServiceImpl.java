package com.biz.service.api;

import com.alibaba.fastjson.JSON;
import com.biz.model.Hmodel.api.*;
import com.biz.model.Pmodel.api.*;
import com.biz.model.Pmodel.weixin.UserInfo;
import com.biz.service.base.BaseServiceImpl;
import com.biz.service.weixin.ApiServiceI;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.*;
import com.framework.utils.ConfigUtil;
import com.framework.utils.Tools;
import com.framework.utils.URLConectionUtil;
import com.framework.utils.UuidUtil;
import com.framework.utils.weixin.WechatAccessToken;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lzq on 2017/2/9.
 */
@Service("wxtsService")
public class WxtsServiceImpl extends BaseServiceImpl<Object> implements WxtsServiceI {
    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Autowired
    private BaseDaoI<TpaySceneDetail> paySceneDetailDao;
    @Autowired
    private BaseDaoI<TPeriodizationMain> tPeriodizationMainDao;
    @Autowired
    private BaseDaoI<TPeriodizationDetail> tPeriodizationDetailDao;
    @Autowired
    private BaseDaoI<TperiodizationShop> tperiodizationShopDao;
    @Autowired
    private BaseDaoI<TPeriodizationDetailLog> tPeriodizationDetailLogDao;

    @Resource(name = "wxUtilService")
    private WxUtilServiceI wxUtilService;

    @Resource(name = "wxTemplateService")
    private WxTemplateServiceI wxTemplateService;

    @Resource(name = "apiService")
    private ApiServiceI apiService;
    /**
     * 获取
     */
    @Override
    public PayScene getPaySceneById(String id) throws Exception {
        Map<String,Object> s_map=new HashMap<>();
        s_map.put("id",id);
        return (PayScene) dao.findForObject("WxtsDao.getPaySceneById", s_map);
    }

    @Override
    public List<BaseUser> getBaseUserByxy_openid(Map<String,Object> selectmap) throws Exception {
        return (List<BaseUser>) dao.findForList("WxtsDao.getBaseUserByxy_openid", selectmap);
    }

    /**
     * 更新某个字段
     */
    @Override
    public int updateWhereBaseUser(Map<String,Object> pd) throws Exception
    {
        return (Integer)dao.update("WxtsDao.updateWhereBaseUser", pd);
    }

    /**
     * 插入银行卡
     */
    @Override
    public boolean addWhereBaseUserByunionpay(Map<String,Object> pd) throws Exception
    {
        return (Integer)dao.update("WxtsDao.insertUserByUnionpay", pd)>0;
    }

    /**
     * 获取
     */
    @Override
    public OrderMainUnion getOrderMainUnionByCode(String code) throws Exception
    {
        return (OrderMainUnion) dao.findForObject("WxtsDao.getOrderMainUnionByCode", code);
    }

    /**
     *
     */
    @Override
    public Brand getBrandOnlyByCode(String brand_code) throws Exception
    {
        return (Brand) dao.findForObject("WxtsDao.getBrandOnlyByCode", brand_code);
    }

    /**
     * 更新某个字段
     */
    @Override
    public void updateWherePayScene(Map<String,Object> pd) throws Exception
    {
        dao.update("WxtsDao.updateWherePayScene", pd);
    }

    @Override
    public boolean findHasGiveAway(String open_id) throws Exception {
        return (int)dao.findForObject("WxtsDao.findHasGiveAway",open_id)>0;
    }

    @Override
    public TpaySceneDetail getDetail(int id) {
        return paySceneDetailDao.getByHql("from TpaySceneDetail where mainId="+id);
    }

    /**
     * 根据某些字段查询
     */
    @Override
    public List<BaseUser> selectWhereBaseUser(Map<String,Object> pd) throws Exception
    {
        return (List<BaseUser>) dao.findForList("WxtsDao.selectWhereBaseUser",pd);
    }


    /**
     * 更新某个字段
     */
    @Override
    public void updateWhereOrderMainUnion(Map<String,Object> pd) throws Exception
    {
        dao.update("WxtsDao.updateWhereOrderMainUnion", pd);
    }

    /**
     * rg_gift 改变状态未已经领取
     */
    @Override
    public boolean updateGiftState(String code) throws Exception
    {
        if(Tools.isEmpty(code)){
            return true;
        }else{
            Map<String,Object>map_s=new HashMap<>();
            map_s.put("code",code);
            return (Integer)dao.update("WxtsDao.updateGiftState", map_s)>0;
        }

    }


    /**
     * 获取
     */
    @Override
    public BaseUser getBaseUserByOpen_id(String open_id,String appid) throws Exception {
        BaseUser s_map = (BaseUser) dao.findForObject("WxtsDao.getBaseUserByOpen_id", open_id);
        if (s_map == null) {

            UserInfo userinfo_new=new UserInfo();
            String Nickname="",Headimgurl="",Unionid="";
            int Sex=0;
            try{
                String sub_appid=ConfigUtil.get("aolong_appid");
                userinfo_new = apiService.getUserInfo(sub_appid,open_id);
                if(userinfo_new!=null){
                    Nickname=userinfo_new.getNickname();
                    Sex=userinfo_new.getSex();
                    Headimgurl=userinfo_new.getHeadimgurl();
                    Unionid=userinfo_new.getUnionid();
                }
            }catch (Exception e){

            }

            String userId = UuidUtil.get32UUID();
            //新增
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


        } else {
            return s_map;
        }

    }

    /**
     * 获取
     */
    @Override
    public RgGift getRgGiftByCode(String code) throws Exception
    {
        return (RgGift) dao.findForObject("WxtsDao.getRgGiftByCode", code);
    }

    @Override
    public void doDealPeriodizationByKeyFirst(String key, String openId)throws Exception{
        //处理第一次分期
        //获取处理对象
        TPeriodizationMain tPeriodizationMain = tPeriodizationMainDao.getById(TPeriodizationMain.class,key);
        BaseUser baseUser = (BaseUser)dao.findForObject("WxtsDao.getBaseUserByOpenId",openId);
        if(baseUser != null && tPeriodizationMain != null)
        {
            if(tPeriodizationMain.getState().intValue() == 0)//没有关联操作关联
            {
                //跟新主表
                tPeriodizationMain.setUserId(baseUser.getId());
                tPeriodizationMain.setState(1);
                tPeriodizationMain.setIsRelated(1);
                tPeriodizationMainDao.update(tPeriodizationMain);

                String mainId = tPeriodizationMain.getId();
                String firstTermId = (String)dao.findForObject("WxtsDao.getFirstTermIdByMainId",mainId);
                String shopId = tPeriodizationMain.getShopId();
                String brandCode = (String)dao.findForObject("WxtsDao.getBrandCodeByShopId",shopId);
                Map<String,Object> noGiveCountMap = (Map<String,Object>)dao.findForObject("WxtsDao.getNoGiveCountByParam",mainId);
                int noGiveCount = 0;
                double lastTotalCoupon = 0.0;
                if(noGiveCountMap != null && noGiveCountMap.get("noGiveCount") != null )
                {
                    noGiveCount = Integer.valueOf(noGiveCountMap.get("noGiveCount").toString()).intValue();
                    lastTotalCoupon = Double.valueOf(noGiveCountMap.get("lastTotalCoupon").toString());
                }
                //当前期数
                Map<String,Object> nowTermMap = (Map<String,Object>)dao.findForObject("WxtsDao.getNowTermByParam",mainId);
                if(nowTermMap!= null && nowTermMap.get("detailId") != null ) {

                    Integer totalTerm = (Integer)dao.findForObject("WxtsDao.getTotalTermByParam",mainId);

                    //修改详细
                    TPeriodizationDetail tPeriodizationDetail
                            = tPeriodizationDetailDao.getById(TPeriodizationDetail.class,firstTermId);
                    Double total = tPeriodizationDetail.getThisTotal();

                    tPeriodizationDetail.setState(2);
                    tPeriodizationDetail.setGetTerm(1);
                    tPeriodizationDetail.setGetTime(new Date());
                    tPeriodizationDetailDao.update(tPeriodizationDetail);

                    //添加记录
                    TPeriodizationDetailLog tPeriodizationDetailLog = new TPeriodizationDetailLog();
                    tPeriodizationDetailLog.setId(UuidUtil.get32UUID());
                    tPeriodizationDetailLog.setMainId(key);
                    tPeriodizationDetailLog.setState(0);
                    tPeriodizationDetailLog.setUserId(baseUser.getId());
                    tPeriodizationDetailLog.setBrandCode(brandCode);
                    tPeriodizationDetailLog.setDetailId(nowTermMap.get("detailId").toString());
                    tPeriodizationDetailLog.setThisCoupon(total);
                    tPeriodizationDetailLog.setThisTerm(Integer.valueOf(nowTermMap.get("nowTerm").toString()));
                    tPeriodizationDetailLog.setThisGetTerms("1");
                    tPeriodizationDetailLog.setGetTime(new Date());
                    tPeriodizationDetailLog.setCreateTime(new Date());

                    tPeriodizationDetailLog.setMsg("分期发券第1期领取成功:"+ total+"久零券");
                    tPeriodizationDetailLog.setTotalCoupon(tPeriodizationMain.getCouponTotal());
                    tPeriodizationDetailLog.setTotalTerm(totalTerm);
                    tPeriodizationDetailLog.setType(0);
                    tPeriodizationDetailLog.setUserName(baseUser.getPerson_name());
                    //next
                    if(noGiveCount >0)
                    {
                        String nextDetailId = (String)dao.findForObject("WxtsDao.getNextDetailId",mainId);
                        TPeriodizationDetail tPeriodizationDetailNext = tPeriodizationDetailDao.getById(TPeriodizationDetail.class,nextDetailId);
                        tPeriodizationDetailLog.setLastTerm(noGiveCount);
                        tPeriodizationDetailLog.setLastCoupon(lastTotalCoupon);
                        tPeriodizationDetailLog.setNextStartTime(tPeriodizationDetailNext.getThisStartTime());
                        tPeriodizationDetailLog.setNextEndTime(tPeriodizationDetailNext.getThisEndTime());
                        tPeriodizationDetailLog.setNextCoupon(tPeriodizationDetailNext.getThisTotal());
                    }
                    tPeriodizationDetailLogDao.save(tPeriodizationDetailLog);

                    //发券
                    if(total.doubleValue() > 0)
                    {
                        int f_q = user_Balance90(brandCode,shopId,openId,(total*100)+"",nowTermMap.get("detailId").toString());//转分
                        if(f_q == 0)//发券成功
                        {
                            wxTemplateService.send_kf_template(openId, "分期发券第1期领取成功:"+ total+"久零券");
                        }else{
                            throw new RuntimeException("分期领取失败！");
                        }
                    }else
                    {
                        wxTemplateService.send_kf_template(openId, "分期发券第1期领取成功:"+ total+"久零券");
                    }
                }else{
                    wxTemplateService.send_kf_template(openId, "分期发券当期已领取！");
                }
            }
        }
    }


    @Override
    public void doDealPeriodizationByKeyOther(String key, String openId)throws Exception{
        //处理后期分期
        TperiodizationShop tperiodizationShop =
                tperiodizationShopDao.getById(TperiodizationShop.class,key);
        BaseUser baseUser = (BaseUser)dao.findForObject("WxtsDao.getBaseUserByOpenId",openId);
        Map<String,String> map = new HashMap<>();
        String shopId = tperiodizationShop.getShopId();
        String userId = baseUser.getId();
        map.put("shopId",shopId);
        map.put("userId",userId);
        //获取分期主表信息
        String mainId = (String)dao.findForObject("WxtsDao.getPeriodizationMainIdByParam",map);
        if(!StringUtil.isNullOrEmpty(mainId))
        {
            TPeriodizationMain tPeriodizationMain =
                    tPeriodizationMainDao.getById(TPeriodizationMain.class,mainId);
            if(tPeriodizationMain.getState() == 1)
            {
                //商户
                String brandCode = (String)dao.findForObject("WxtsDao.getBrandCodeByShopId",shopId);
                //未发放期数
                Map<String,Object> noGiveCountMap = (Map<String,Object>)dao.findForObject("WxtsDao.getNoGiveCountByParam",mainId);
                int noGiveCount = 0;
                double lastTotalCoupon = 0.0;
                if(noGiveCountMap != null && noGiveCountMap.get("noGiveCount") != null )
                {
                    noGiveCount = Integer.valueOf(noGiveCountMap.get("noGiveCount").toString()).intValue();
                    lastTotalCoupon = Double.valueOf(noGiveCountMap.get("lastTotalCoupon").toString());
                }

                //当前期数
                Map<String,Object> nowTermMap = (Map<String,Object>)dao.findForObject("WxtsDao.getNowTermByParam",mainId);

                if(nowTermMap!= null && nowTermMap.get("detailId") != null )
                {
                    Integer totalTerm = (Integer)dao.findForObject("WxtsDao.getTotalTermByParam",mainId);
                    //获取分期信息
                    List<String> detailList = (List<String>)dao.findForList("WxtsDao.getDetailListsByMainId",mainId);
                    double allTotal = 0.00;
                    int[] termArr = new int[detailList.size()];
                    //处理子表
                    for(int i = 0; i < detailList.size(); i++)
                    {
                        TPeriodizationDetail tPeriodizationDetail
                                = tPeriodizationDetailDao.getById(TPeriodizationDetail.class,detailList.get(i));
                        Double total = tPeriodizationDetail.getThisTotal();

                        tPeriodizationDetail.setState(2);
                        tPeriodizationDetail.setGetTerm(Integer.valueOf(nowTermMap.get("nowTerm").toString()));
                        tPeriodizationDetail.setGetTime(new Date());
                        tPeriodizationDetailDao.update(tPeriodizationDetail);

                        allTotal += total.doubleValue();
                        termArr[i] = tPeriodizationDetail.getThisTerm().intValue();
                    }
                    String msgTerm = bubblingAndFormartToString(termArr);
                    //处理主表
                    if( noGiveCount == 0)//发放完成
                    {
                        tPeriodizationMain.setState(2);
                        tPeriodizationMainDao.update(tPeriodizationMain);
                    }
                    //添加记录
                    TPeriodizationDetailLog tPeriodizationDetailLog = new TPeriodizationDetailLog();
                    tPeriodizationDetailLog.setId(UuidUtil.get32UUID());
                    tPeriodizationDetailLog.setMainId(key);
                    tPeriodizationDetailLog.setState(0);
                    tPeriodizationDetailLog.setUserId(userId);
                    tPeriodizationDetailLog.setBrandCode(brandCode);
                    tPeriodizationDetailLog.setMsg("分期发券第"+msgTerm+"期领取成功:"+ allTotal+"久零券");
                    tPeriodizationDetailLog.setDetailId(nowTermMap.get("detailId").toString());
                    tPeriodizationDetailLog.setThisCoupon(allTotal);
                    tPeriodizationDetailLog.setThisTerm(Integer.valueOf(nowTermMap.get("nowTerm").toString()));
                    tPeriodizationDetailLog.setThisGetTerms(msgTerm);
                    tPeriodizationDetailLog.setGetTime(new Date());
                    tPeriodizationDetailLog.setCreateTime(new Date());
                    tPeriodizationDetailLog.setTotalCoupon(tPeriodizationMain.getCouponTotal());
                    tPeriodizationDetailLog.setTotalTerm(totalTerm);
                    tPeriodizationDetailLog.setType(1);
                    tPeriodizationDetailLog.setUserName(baseUser.getPerson_name());
                    //next
                    if(noGiveCount >0)
                    {
                        String nextDetailId = (String)dao.findForObject("WxtsDao.getNextDetailId",mainId);
                        TPeriodizationDetail tPeriodizationDetailNext = tPeriodizationDetailDao.getById(TPeriodizationDetail.class,nextDetailId);
                        tPeriodizationDetailLog.setLastTerm(noGiveCount);
                        tPeriodizationDetailLog.setLastCoupon(lastTotalCoupon);
                        tPeriodizationDetailLog.setNextStartTime(tPeriodizationDetailNext.getThisStartTime());
                        tPeriodizationDetailLog.setNextEndTime(tPeriodizationDetailNext.getThisEndTime());
                        tPeriodizationDetailLog.setNextCoupon(tPeriodizationDetailNext.getThisTotal());
                    }
                    tPeriodizationDetailLogDao.save(tPeriodizationDetailLog);
                    //修改扫码临时表
                    tperiodizationShop.setState(1);
                    tperiodizationShop.setUserId(userId);
                    tperiodizationShopDao.update(tperiodizationShop);
                    //发券
                    if(allTotal > 0)
                    {
                        int f_q = user_Balance90(brandCode,shopId,openId,(allTotal*100)+"",nowTermMap.get("detailId").toString());//转分
                        if(f_q == 0)//发券成功
                        {
                            wxTemplateService.send_kf_template(openId, "分期发券第"+msgTerm+"期领取成功:"+ allTotal+"久零券");
                        }else{
                            throw new RuntimeException("分期领取失败！");
                        }
                    }else{
                        wxTemplateService.send_kf_template(openId, "分期发券第"+msgTerm+"期领取成功:"+ allTotal+"久零券");
                    }
                }else{
                    wxTemplateService.send_kf_template(openId, "分期发券当期已领取！");
                }
            }else{
                wxTemplateService.send_kf_template(openId, "分期发券已领取完成！");
            }
        }

    }

    @Override
    public void updateBaseUserSalesId(String open_id, String code) throws Exception {
        Map<String,Object> param = new HashMap<>();
        param.put("open_id",open_id);
        param.put("code",code);
        dao.update("WxtsDao.updateBaseUserSalesId",param);
    }


    /**
     * 发券
     *
     * @return 0成功 1失败 3重
     */
    private int user_Balance90(String brand_code,
                               String shop_code,
                               String open_id,
                               String balance_90,
                               String orderCode) {

        //发券
        JSONObject jSONObject_q = new JSONObject();
        jSONObject_q.put("brand_code", brand_code);
        jSONObject_q.put("shop_code", shop_code);
        jSONObject_q.put("user_code", "");
        jSONObject_q.put("order_code", orderCode);
        jSONObject_q.put("open_id", open_id);
        jSONObject_q.put("type", "0");
        jSONObject_q.put("source", "1");//分期自动发券
        jSONObject_q.put("source_msg", "分期自动发券");
        jSONObject_q.put("balance_90", balance_90);
        jSONObject_q.put("state", 2);
        jSONObject_q.put("commission", "0");
        jSONObject_q.put("tradeType", "MICROPAY");
        jSONObject_q.put("orderState", "1");
        jSONObject_q.put("orderTotal", "0");
        int zt=1;
        Map<String, String> hash1 = new HashMap<String, String>();
        hash1.put("json", jSONObject_q.toString());
        Map<String, Object> r_Interface = APIInterface(hash1, "api/balance/operUserBalance90.ac");
        if (r_Interface != null) {
            return zt=Integer.parseInt(r_Interface.get("flag").toString());
        } else {
            return zt;
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
     * 冒泡排序后输出逗号拼接
     * @param array
     * @return
     */
    private String bubblingAndFormartToString(int[] array){
        for (int i = 0;i < array.length;i++){
            for(int j = i;j < array.length;j++){
                if (array[i] < array[j]){
                    int temp = array[i];
                    array[i] = array[j];
                    array[i] = temp;
                }
            }
        }
        StringBuffer sb = new StringBuffer();
        for (int i : array) {
            sb.append(i).append("、");
        }
        sb.deleteCharAt(sb.length()-1);

        return sb.toString();
    }

    @Override
    public void updateShopId(String open_id,String shop_id) throws Exception {
        Map<String,Object> map=new HashedMap();
        map.put("open_id",open_id);
        map.put("shop_id",shop_id);
        dao.update("WxtsDao.updateShopId",map);
    }
}
