package com.biz.service.home;

import com.biz.model.Hmodel.TMenu;
import com.biz.model.Pmodel.Pmenu;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.Pagent;
import com.biz.model.Pmodel.basic.Parea;
import com.biz.model.Pmodel.basic.Pbrand;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by yzljj on 2016/6/23.
 */
@Service("homeService")
public class HomeServiceImpl extends BaseServiceImpl<Object> implements  HomeServiceI {

    private  SimpleDateFormat  shortSdf;
	@Autowired
	private BaseDaoI<TMenu> menuDao;
	
	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Autowired
	private BaseDaoI baseDao;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pmenu> showLeftMenu() throws Exception {
		List<Pmenu> pmenuList=null;
		pmenuList=(List<Pmenu>)dao.findForList("homeDao.findAllMenu", null);
		return pmenuList;
	}

    @Override
    public List<Pmenu> showLeftMenuByRole(String deep) throws Exception {
        List<Pmenu> pmenuList=null;
        Map<String, Object> paramsMap=new HashMap<String, Object>();
        paramsMap.put("deep",deep);
        pmenuList=(List<Pmenu>)dao.findForList("homeDao.showLeftMenuByRole", paramsMap);
        return pmenuList;
    }

	@Override
	public List<Parea> getList(String deep) throws Exception {
		return (List<Parea>)dao.findForList("homeDao.getList", deep);
	}

	@Override
	public List<Parea> getList(String lev, String id) throws Exception {
		Map<String, Object> paramsMap=new HashMap<String, Object>();
		paramsMap.put("deep",lev);
		paramsMap.put("id",id);
		List<Parea> pareaList=(List<Parea>)dao.findForList("homeDao.getListParm", paramsMap);
		return pareaList;
	}

	@Override
	public List<Parea> getListByPid(String lev, String pid) throws Exception {
		Map<String, Object> paramsMap=new HashMap<String, Object>();
		paramsMap.put("deep",lev);
		paramsMap.put("pid",pid);
		List<Parea> pareaList=(List<Parea>)dao.findForList("homeDao.getListParmByPid", paramsMap);
		return pareaList;
	}

	@Override
	public HashMap<String, Object> queryUseGiftYes(String identity_code, int identity) throws Exception {
        Map<String,Object> pd = new HashMap<>();
        if(identity == 2) {
            pd.put("agent_code", identity_code);
        }else if (identity == 3){
            pd.put("brand_code", identity_code);
        }
        HashMap<String,Object> use_data = new HashMap<String, Object>();

        double use_offline = 0;//线下用券
        double use_line = 0;//线上用券
        double use_service = 0;//服务费
        double use_serviceMain = 0;//服务费
double user_total=0;//总计
        //昨日用券数据
        use_offline = (double)dao.findForObject("homeDao.getUseGiftOffLineYes", pd)/100.0;
        //昨日线上用券
        use_line = (double)dao.findForObject("homeDao.getUseGiftOnlineYes", pd)/100.0;
        //服务费
        use_service = (double)dao.findForObject("homeDao.getUseServiceYes", pd);
        use_serviceMain = (double)dao.findForObject("homeDao.getUseServiceMainYes", pd);
//		BigDecimal BDcard_total = new BigDecimal(card_total.get("card_total").toString());
//		BigDecimal BDcard_today = new BigDecimal(Double.toString(card_today));
//		BigDecimal BDcard_month = new BigDecimal(Double.toString(card_month));

        use_data.put("use_offline", use_offline);
        use_data.put("use_line",use_line);
        use_data.put("use_service", use_service+use_serviceMain*100);
       // user_total=use_offline+use_line;
        user_total=use_line;
        use_data.put("user_total", user_total);
        return use_data;
	}

	@Override
	public HashMap<String, Object> queryGiftCardDataYes(String identity_code, int identity)throws Exception {

        Map<String,Object> pd = new HashMap<>();
        if(identity == 2) {
            pd.put("agent_code", identity_code);
        }else if (identity == 3){
            pd.put("brand_code", identity_code);
        }else if (identity == 4){
            pd.put("user_code", identity_code);
        }
        HashMap<String,Object> card_data = new HashMap<String, Object>();

        double card_auto = 0;//自动发券
        double card_person = 0;//人工发券
        double card_jihuo = 0;//激活发券
        double card_total = 0;
        //昨日发券数据
         card_auto = (double) dao.findForObject("homeDao.getGiftCardAutoYes", pd)/100.0;
        card_person = (double) dao.findForObject("homeDao.getGiftCardPersonYes", pd)/100.0;

        //admin或者代理商
        if (identity==1 || identity==2)
        {
            double giftCard = (double) dao.findForObject("homeDao.getGiftCardJiHuoYes", pd);
            card_jihuo = giftCard/100;
        }
        card_total = card_person + card_jihuo + card_auto;

//		BigDecimal BDcard_total = new BigDecimal(card_total.get("card_total").toString());
//		BigDecimal BDcard_today = new BigDecimal(Double.toString(card_today));
//		BigDecimal BDcard_month = new BigDecimal(Double.toString(card_month));

        card_data.put("card_total",card_total);

        card_data.put("card_auto", card_auto);
        card_data.put("card_person",card_person);
        card_data.put("card_jihuo", card_jihuo);

        return card_data;
	}

	@Override
	public HashMap<String, Object> queryPersonNumYes(String identity_code, int identity)throws Exception {

        Map<String,Object> pd = new HashMap<>();
        if(identity == 2) {
            pd.put("agent_code", identity_code);
        }else if (identity == 3){
            pd.put("brand_code", identity_code);
        }else if (identity == 4){
            pd.put("user_code", identity_code);
        }
        HashMap<String,Object> person_data = new HashMap<String, Object>();
        //昨日新增会员
        int new_person = (int)dao.findForObject("homeDao.getNewUserYes", pd);
        int all_person = (int)dao.findForObject("homeDao.getNewUserAll", pd);
        int cancal_person = (int)dao.findForObject("homeDao.getCanalUserAll", pd);
        //昨日用券人数
        Object obj = dao.findForObject("homeDao.getUseGiftPersonYes", pd);
        int use_person = 0;
        if (obj!=null)
        {
            use_person = (int)obj;
        }
        //昨日领券人数
        Object obj1 = (Object)dao.findForObject("homeDao.getGetGiftPersonYes", pd);
        int get_person = 0;
        if (obj1!=null)
        {
            get_person = (int)obj1;
        }

//		BigDecimal BDcard_total = new BigDecimal(card_total.get("card_total").toString());
//		BigDecimal BDcard_today = new BigDecimal(Double.toString(card_today));
//		BigDecimal BDcard_month = new BigDecimal(Double.toString(card_month));

        person_data.put("cancal_person", cancal_person);
        person_data.put("new_person", new_person);
        person_data.put("use_person",use_person);
        person_data.put("get_person", get_person);
        person_data.put("all_person", all_person);

        return person_data;
	}

	@Override
	public List<Pagent> queryAgentss(String identity_code, int identity)throws Exception {
        if(identity==1||identity==2)//代理商管理和商户可以看到对应的代理商列表
        {

            Map<String,Object> pd = new HashMap<>();
            pd.put("identity_code",identity_code);
            pd.put("identity",identity);

            return (List<Pagent>) dao.findForList("homeDao.queryAgentss",pd);
        }
        else
        {return new ArrayList<>();}

	}

	@Override
	public List<Pbrand> queryBrandByAgent(String identity_code, int identity)throws Exception {
        if(identity==1||identity==2||identity==3)//只能商户/代理商/管理员看到
        {

            Map<String,Object> pd = new HashMap<>();
            pd.put("identity_code",identity_code);
            pd.put("identity",identity);

            return (List<Pbrand>) dao.findForList("homeDao.queryBrandByAgent",pd);
        }
        else
        {return new ArrayList<>();}
	}

    @Override
    public HashMap<String, Object> queryGiftData(int type, User user, int inout, String agentId, String brandId) throws Exception {
          SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String beginTime="";
        String endTime="";
        if(type==1)//本周
        {
            beginTime=sdf.format(getCurrentWeekDayStartTime());
            endTime=sdf.format(getCurrentWeekDayEndTime());
        }
        else if(type==2)//本月
        {
            beginTime=sdf.format(getCurrentMonthStartTime());
            endTime=sdf.format(getCurrentMonthEndTime());
        } else if(type==3)//本季度
        {
            beginTime=sdf.format(getCurrentQuarterStartTime());
            endTime=sdf.format(getCurrentQuarterEndTime());
        }
       Map<String,Object> map=new  HashMap<String, Object>();
        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        map.put("identity",user.getIdentity());
        map.put("identityCode",user.getIdentity_code());
        if(3==user.getIdentity())
        {brandId=user.getIdentity_code();}
        if("10".equals(agentId))
        {agentId="";}
        map.put("agentId",agentId);
        map.put("brandId",brandId);
        if(inout==1)
        {map.put("source","0,1,2,4");}
        else
        {map.put("source","3");}

        HashMap<String, Object> res=new HashMap<String, Object>();
        List<Map<String,Object>> list= (List<Map<String, Object>>) dao.findForList("homeDao.queryGiftData",map);
        list=getFullDayData(list,beginTime,endTime);
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
        res.put("amountAll",amountAll);
        res.put("maxCount",maxcount);
        res.put("size",(int)list.size()/7);
        res.put("list",list);
        return res;
    }

    @Override
    public HashMap<String, Object> queryBrandDataYes(String identity_code, int identity) throws Exception {
        Map<String,Object> pd = new HashMap<>();
        pd.put("identity_code",identity_code);
        pd.put("identity",identity);
        return (HashMap<String, Object>) dao.findForObject("homeDao.queryBrandDataYes",pd);
    }

    @Override
    public HashMap<String, Object> queryCoinDataYes(String identity_code, int identity) throws Exception {
        Map<String,Object> pd = new HashMap<>();
        pd.put("identity_code",identity_code);
        pd.put("identity",identity);
        return (HashMap<String, Object>) dao.findForObject("homeDao.queryCoinDataYes",pd);
    }

    @Override
    public HashMap<String, Object> queryCoinDataAll(String identity_code, int identity) throws Exception {
        Map<String,Object> pd = new HashMap<>();
        pd.put("identity_code",identity_code);
        pd.put("identity",identity);
        return (HashMap<String, Object>) dao.findForObject("homeDao.queryCoinDataAll",pd);
    }

    @Override
    public Parea getAreaById(String cityId) throws Exception {
        return (Parea) dao.findForObject("homeDao.getAreaById",cityId);
    }

    private List<Map<String,Object>> getFullDayData(List<Map<String, Object>> list, String beginTime, String endTime) throws ParseException {
        List<Map<String,Object>> listRes=new ArrayList<>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date dateBegin=sdf.parse(beginTime);
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
                if(dateNowString.equals(list.get(j).get("date")))
                {
                    map.put("count",list.get(j).get("count"));
                    break;
                }
                else
                {
                    map.put("count",BigDecimal.valueOf(0.0));
                }
            }
            if(list.size()==0)
            { map.put("count",BigDecimal.valueOf(0.0));}
            listRes.add(map);
        }
      /*  Map<String,Object> map=new HashMap<>();
        map.put("date",dateNow.getTime());
        map.put("count",BigDecimal.valueOf(0.0));
        listRes.add(map);*/
        return listRes;
    }


    /**
     * 获得本周的第一天，周一
     *
     * @return
     */
    public   Date getCurrentWeekDayStartTime() {
        shortSdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;
            c.add(Calendar.DATE, -weekday);
            c.setTime(shortSdf.parse(shortSdf.format(c.getTime())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获得本周的最后一天，周日
     *
     * @return
     */
    public Date getCurrentWeekDayEndTime() {
        shortSdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK);
            c.add(Calendar.DATE, 8 - weekday);
            c.setTime(shortSdf.parse(shortSdf.format(c.getTime())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }
    /**
     * 获得本月的开始时间，即2012-01-01 00:00:00
     *
     * @return
     */
    public   Date getCurrentMonthStartTime() {
        shortSdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前月的结束时间，即2012-01-31 23:59:59
     *
     * @return
     */
    public   Date getCurrentMonthEndTime() {
        shortSdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, -1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前季度的开始时间，即2012-01-1 00:00:00
     *
     * @return
     */
    public   Date getCurrentQuarterStartTime() {
        shortSdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前季度的结束时间，即2012-03-31 23:59:59
     *
     * @return
     */
    public   Date getCurrentQuarterEndTime() {
        shortSdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }
}
