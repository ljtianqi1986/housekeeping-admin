package com.biz.service.api;

import com.alibaba.fastjson.JSON;
import com.biz.conf.Global;
import com.biz.model.Hmodel.TbaseDetail;
import com.biz.model.Hmodel.basic.*;
import com.biz.model.Hmodel.goods.baseStandard;
import com.biz.model.Hmodel.goods.goodsBrand;
import com.biz.model.Hmodel.goods.goodsUnit;
import com.biz.model.Pmodel.PBaseUser;
import com.biz.model.Pmodel.api.ChangeNoticeTemp;
import com.biz.model.Pmodel.basic.Pgroup;
import com.biz.model.Pmodel.basic.Ppics;
import com.biz.model.Pmodel.basic.Pstandard;
import com.biz.model.Pmodel.basic.Ptag;
import com.biz.model.Pmodel.sys.PwxGoods;
import com.biz.model.Pmodel.sys.PwxGoodsStock;
import com.biz.service.basic.UserServiceI;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/***
 * lzq
 */
@Service("apiInterfaceService")
public class ApiInterfaceServiceImpl implements ApiInterfaceServiceI{

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Autowired
	private BaseDaoI<TwxGoodsGroup> wxgoodsGroup;
	@Autowired
	private BaseDaoI<Ttag> tagDao;

	@Autowired
	private BaseDaoI<baseStandard> standDao;
	@Autowired
	private BaseDaoI<Twxgoods> wxgoodsDao;
	@Autowired
	private BaseDaoI<TBaseBrand> basebrandao;

	@Autowired
	private BaseDaoI<Tbase90DetailLog>  baseDetailLogDao;
	@Autowired
	private BaseDaoI<goodsUnit> unitsDao;

	@Autowired
	private BaseDaoI<goodsBrand> brandDao;
	@Autowired
	private BaseDaoI<TwxgoodsProperty> wxgoodsPropertyDao;

	@Autowired
	private BaseDaoI<TwxgoodsStock> wxgoodsStockDao;

	@Autowired
	private BaseDaoI<TgoodsTag> goodsTagDao;

	@Autowired
	private BaseDaoI<Tpics> picsDao;
	@Autowired
	private BaseDaoI<TbaseDetail> baseDetailDao;

	@Autowired
	private UserServiceI userService;

	@Resource(name = "wxTemplateService")
	private WxTemplateServiceI wxTemplateService;

	/**
	 * 查询商品SKU信息
	 * 
	 * @param
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> queryGoodsSkuInfo(String code,
			String venderIdMain) throws Exception {
		if (StringUtil.isNullOrEmpty(code) || StringUtil.isNullOrEmpty(venderIdMain)) {
			Map<String, Object> result1 = new HashMap<String, Object>();
			result1.put("resultCode", "0");// 参数错误
			result1.put("resultMsg", "参数错误");// 参数错误
			return result1;
		} else {
			Map<String, Object> param = new HashMap<String, Object>();
			Map<String, Object> result2 = new HashMap<String, Object>();
			param.put("code", code);
			param.put("venderIdMain", venderIdMain);

			// 根据订单号去查券是否核销过
			Integer state = (Integer) dao.findForObject(
					"QtGoodsDao.getStateByCode", param);
			if (state.intValue() == 0) {
				result2.put("resultCode", "1");// 券不可用
				result2.put("resultMsg", "券不可用");
				return result2;
			}
			// 根据订单号和venderId去查券是否是该商家的
			Integer orderSum = (Integer) dao.findForObject(
					"QtGoodsDao.getOrderSumBy", param);
			if (orderSum == 0) {
				result2.put("resultCode", "2");// 券与商家不匹配
				result2.put("resultMsg", "券与商家不匹配");
				return result2;
			}

            Map<String,Object> stock_map=(Map<String,Object>)dao.findForObject("QtGoodsDao.getVenderIdByCode", code);
			String venderId = stock_map.get("venderId").toString();
            String stockId = stock_map.get("stockId").toString();
			param.put("venderId", venderId);
            param.put("stockId", stockId);
			result2 = (Map<String, Object>) dao.findForObject("QtGoodsDao.queryGoodsSkuInfo", param);
			if(result2 != null) {
				if (result2.get("goodsPath") != null) {
					result2.put("goodsPath", Global.getConfig("OSSURL")
							+ result2.get("goodsPath").toString());
				}
				List<Map<String, Object>> tags = (List<Map<String, Object>>) dao
						.findForList("QtGoodsDao.findTagsByVenderId", venderId);
				result2.put("tags", tags);
				result2.put("resultCode", "3");// 满足销券条件准备销券
				result2.put("resultMsg", "满足销券条件准备销券");
                result2.put("price", stock_map.get("price").toString());
                result2.put("stock", stock_map.get("stock").toString());
				return result2;
			}else{
				Map<String, Object> result3 = new HashMap<String, Object>();
				result3.put("resultCode", "7");//商品不存在
				result3.put("resultMsg", "商品不存在");
				return result3;
			}
		}
	}


	@Override
	public boolean doSellingCoupons(String code) throws Exception {
		boolean flag = true;
		int a = ((Integer) dao.update("QtGoodsDao.doSellingCoupons", code))
				.intValue();
		if (a == 0) {
			flag = false;
		}
		return flag;
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
	public Map<String, Object> updatecoin_90(Map<String, Object> map) throws Exception {

		Map<String, Object> jSONObject = new HashMap<>();
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
				isBalance=giveAmount>=amount;//手动发券计算充值金额够不够
			}else
			{isBalance=true;}
		}
		if(checkMap!=null&&checkMap.size()>0)
		{
			jSONObject.put("flag", "3");
			jSONObject.put("msg", "操作失败，重复调用");
		}else if(!isBalance)
		{
			jSONObject.put("flag", "2");
			jSONObject.put("msg", "操作失败，余额不足");
		}
		else//开始操作90币
		{
			//String url = ConfigUtil.get("weixinShop_URL")+"client_toClientCenter.do";
			//String content = "点此进入个人中心查看！";
			//String openId=getOpenIdBySysUserId(map.get("userId").toString());
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
					jSONObject=doOperCoin("BaseUserDao.doCutCoinGive","发券撤回","1",map,"OUT");
				}
				else{
					jSONObject.put("flag", "1");
					jSONObject.put("msg", "金额不对，撤回失败");
				}

				title= "亲，久零券发放撤回，赠送的"+map.get("amount")+"元久零贝已经从您的余额扣除";

			}
		//	wxTemplateService.kf_reply_news(openId,title,content,"",url);
		}

		return jSONObject;
	}

	@Override
	public Map<String, Object> updatebalance_90(Map<String, Object> map) throws Exception {

		JSONObject jSONObject = new JSONObject();
		List<Map<String,String>> checkMap=(List<Map<String, String>>) dao.findForList("BaseUserDao.getUserDetail90ByWx", map);
		Map<String,Object> user=(Map<String, Object>) dao.findForObject("BaseUserDao.getUserByOpenId", map);
		TBaseBrand brand=null;
		String source=map.get("source").toString();
		String state=map.get("state").toString();
		Double amount=Double.valueOf(map.get("balance_90").toString());

		boolean isBalance=false;
		if(user!=null)
		{
			String userBalance=user.get("balance_90").toString();
			double balance_90=Double.parseDouble(userBalance);
			if("1".equals(state))//如果是要支出久零券的，校验是否够
			{
				isBalance=balance_90>=amount;
			}else			{
				isBalance=true;
			}

		}
		if("2".equals(source))
		{
			String brandCode=map.get("brand_code")+"";
			brand=basebrandao.getById(TBaseBrand.class,brandCode);
			if(brand!=null) {
				if (amount > brand.getBalance90() + brand.getCreditTotal90() - brand.getCreditNow90()) {
					isBalance = false;
				} else {
					isBalance = true;
				}
			}else
			{
				isBalance = false;
			}
		}


		if(checkMap!=null&&checkMap.size()>0)
		{
			jSONObject.put("flag", "3");
			jSONObject.put("msg", "操作失败，重复调用");
		}else if(!isBalance)
		{
			jSONObject.put("flag", "2");
			jSONObject.put("msg", "操作失败，余额不足");
		}
		else if((!"9".equals(source))&&(!"10".equals(source)))
		{
			int res=(int) dao.update("BaseUserDao.operBalance90ByWx", map);
			if(res>0)
			{
				map.put("userId",user.get("id"));
				//int maxNUM= (int) dao.findForObject("BaseUserDao.getMaxBaseDetailId",map);
				//map.put("id",maxNUM+1);
				dao.save("BaseUserDao.saveDetail90ByWx", map);
				jSONObject.put("flag", "0");
				jSONObject.put("msg", "操作成功");
			}
			else
			{
				jSONObject.put("flag", "1");
				jSONObject.put("msg", "操作失败,找不到open_id对应用户");
			}
			if("2".equals(source))
			{
				if(brand!=null)
				{
					if(brand.getBalance90()>amount)
					{brand.setBalance90((long) (brand.getBalance90()-amount));}
					else
					{
						brand.setCreditNow90((int) (amount-brand.getBalance90()));
						brand.setBalance90((long) 0);
					}
					basebrandao.update(brand);
				}
			}
		}else//source为9的时候为手动发券撤回，10为实体卡撤回，单独处理
		{
				jSONObject=doCancal(map.get("order_code")+"",user,source);
		}

		return jSONObject;
	}

	private JSONObject doCancal(String order_code, Map<String, Object> user,String source) throws Exception {
		JSONObject jSONObject = new JSONObject();
		String userBalance=user.get("balance_90").toString();
		int balance_90=Integer.parseInt(userBalance);
		/**以下进行预处理，判断是否够退券**/
		TbaseDetail td=baseDetailDao.getById(TbaseDetail.class,order_code);

		if ((td!=null)&&(td.getIsdel()==0))
		{
			if(td.getPoint90()>balance_90)
			{
				jSONObject.put("flag", "2");
				jSONObject.put("msg", "操作失败，余额不足");
			}

			Map<String,Object> map=new HashMap<>();
			map.put("balance_90",td.getPoint90());
			map.put("open_id",td.getOpenId());


			/**以下处理对商户进行退券**/
			if(source.equals("9")) {
				TBaseBrand brand = basebrandao.getById(TBaseBrand.class, td.getBrandId());
				if (brand.getCreditNow90() >= td.getPoint90()) {
					brand.setCreditNow90(brand.getCreditNow90() - td.getPoint90());
				} else if (brand.getCreditNow90() > 0) {
					brand.setBalance90(brand.getBalance90() + td.getPoint90() - brand.getCreditNow90());
					brand.setCreditNow90(0);
				} else {
					brand.setBalance90(brand.getBalance90() + td.getPoint90());
				}
				basebrandao.update(brand);
			}
			/**以下处理对用户进行退券**/
			int res=(int) dao.update("BaseUserDao.cancalBalance90ByWx", map);

			if(res>0)
			{/**以下处理对流水记录退券**/
				td.setIsdel(1);
				td.setCancelTime(new Date());
				baseDetailDao.update(td);
				jSONObject.put("flag", "0");
				jSONObject.put("msg", "操作成功");
			}
			else
			{
				jSONObject.put("flag", "1");
				jSONObject.put("msg", "操作失败,找不到对应用户");
			}
		}else
		{
			jSONObject.put("flag", "1");
			jSONObject.put("msg", "操作失败,找不到对应的发券记录");
		}

		return jSONObject;
	}


	private Map<String, Object> doOperCoin(String sql, String note, String type, Map<String,Object> map, String inOut) throws Exception {
		Map<String, Object> jSONObject = new HashMap<>();
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



	public String getOpenIdBySysUserId(String userid)throws Exception
	{
		return (String) dao.findForObject("BaseUserDao.getOpenIdBySysUserId", userid);
	}
	/**
     * 查询商品SKU信息
     * @param
     * @throws Exception
     */
	@Override
    public Map<String,Object> queryGoodsSkuInfoSingle(String code,String venderIdMain)throws Exception{
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        if(StringUtil.isNullOrEmpty(code)|| StringUtil.isNullOrEmpty(venderIdMain)){
			Map<String,Object> result1 =new HashMap<String,Object>();
        	result1.put("resultCode", "0");//参数错误
        	result1.put("resultMsg", "参数错误");//参数错误
            return result1;
        } else {
        	Map<String,Object> param = new HashMap<String,Object>();
			Map<String,Object> result2 =new HashMap<String,Object>();
        	String venderId = code;
            param.put("venderIdMain",venderIdMain);
        	param.put("venderId", venderId);
            
            result2 =(Map<String,Object>)dao.findForObject("QtGoodsDao.queryGoodsSkuInfo",param);
			if(result2 != null) {
				if (result2.get("goodsPath") != null) {
					result2.put("goodsPath", Global.getConfig("OSSURL") + result2.get("goodsPath").toString());
				}
				List<Map<String, Object>> tags = (List<Map<String, Object>>) dao.findForList("QtGoodsDao.findTagsByVenderId", venderId);
				result2.put("goodsCode", venderId);
				result2.put("tags", tags);
				if (compare_date(df.format(new Date()), "2016-12-31 23:59") != 1) {
					result2.put("interfaceNotice", "参数说明(2016-12-31 23:59后将不返回interfaceNotice参数),【name:商品名称】,【unit:单位(目前统一为 件)】,【goodsCode:商品编码】,【tags:标签】,【firstValue，secondValue，thirdValue:商品规格，拼接展示】 ");
				}
				result2.put("resultCode", "6");//商品信息请求成功
				result2.put("resultMsg", "商品信息请求成功");
				return result2;
			}else{
				Map<String,Object> result3 =new HashMap<String,Object>();
				result3.put("resultCode", "7");//商品不存在
				result3.put("resultMsg", "商品不存在");
				return result3;
			}
        }
    }



	
	/***
     * 时间比较
     * @param DATE1
     * @param DATE2
     * @return
     */
    private int compare_date(String DATE1, String DATE2) {
        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }







	@Override
	public Map<String, Object> saveGoodsInfo(List<PwxGoods> goodsList) throws Exception {
		Map<String,Object> res=new HashMap<>();
		for(PwxGoods goods:goodsList)
		{
			Ttag tag=tagDao.getById(Ttag.class,goods.getTag());
			TwxGoodsGroup group=wxgoodsGroup.getById(TwxGoodsGroup.class,goods.getGroupId());
			goodsUnit unit=unitsDao.getById(goodsUnit.class,goods.getUnitId());
			goodsBrand brand=brandDao.getById(goodsBrand.class,goods.getBrand());

			if(brand==null)
			{
				brand=new goodsBrand();
				brand.setId(goods.getBrand());
				brand.setName(goods.getBrandName());
				brand.setType(1);
				brandDao.save(brand);;
			}else
			{
				brand.setName(goods.getBrandName());
				brandDao.update(brand);;
			}
			if(unit==null)
			{
				unit=new goodsUnit();
				unit.setId(goods.getUnitId());
				unit.setName(goods.getUnitName());
				unit.setType(1);
				unitsDao.save(unit);;
			}else
			{
				unit.setName(goods.getUnitName());
				unitsDao.update(unit);;
			}
			Ptag tt=new Ptag();
			Pgroup tg=new Pgroup();
			if(tag==null) {
                tt.setName(goods.getTagName());
                tt.setId(goods.getTag());
                //	tt.setShopid(shopId);
                dao.save("WxflowerSettingDao.saveTag", tt);
            }else
			{
				tag.setName(goods.getTagName());
				tagDao.update(tag);
			}
			if(group==null)
			{
				String picId="";
				if(!StringUtil.isNullOrEmpty(goods.getGroupPath())) {
					Tpics pics = new Tpics();
					pics.setPath(goods.getGroupPath().replace("http://9900oss.oss-cn-hangzhou.aliyuncs.com/", ""));
					picsDao.save(pics);
					picId=pics.getId();
				}

				tg.setName(goods.getGroupName());
				tg.setId(goods.getGroupId());
				tg.setIcon(picId);
				//tg.setShopid(shopId);
				dao.save("WxflowerSettingDao.saveGroup",tg);
			}else
			{
				String picId="";
				if(!StringUtil.isNullOrEmpty(goods.getGroupPath())) {
					Tpics pics = new Tpics();
					pics.setPath(goods.getGroupPath().replace("http://9900oss.oss-cn-hangzhou.aliyuncs.com/", ""));
					picsDao.save(pics);
					picId=pics.getId();
				}
				group.setIcon(picId);
				group.setName(goods.getGroupName());
                group.setIsdel((short) 0);
				wxgoodsGroup.update(group);
			}
			Map<String,String> map=new HashMap<>();
			map.put("group",goods.getGroupId());
			map.put("tag",goods.getTag());
			List<String> list= (List<String>) dao.findForList("WxflowerSettingDao.getTagGroup",map);
			if(list==null||list.size()<=0)
			{
				map.put("id", UuidUtil.get32UUID());
				dao.save("WxflowerSettingDao.saveTagGroup",map);
			}
			if(goods.getCoverList()!=null&&goods.getCoverList().size()>0)
			{
				for(int i=0;i<goods.getCoverList().size();i++)
				{
					Tpics tp=picsDao.getById(Tpics.class,goods.getCoverList().get(i).getCoverId());
					if(tp==null)
					{
						Ppics pics=new Ppics();
						pics.setPath(goods.getCoverList().get(i).getCoverPath().replace("http://9900oss.oss-cn-hangzhou.aliyuncs.com/",""));
						pics.setMainType(1);
						pics.setId(goods.getCoverList().get(i).getCoverId());
						//  picsDao.save(pics);
						dao.save("WxflowerSettingDao.saveGoodsPics",pics);
					}

				}

			}
			if(goods.getCover()!=null)
			{
					Tpics tp=picsDao.getById(Tpics.class,goods.getCover().getCoverId());
					if(tp==null)
					{
						Ppics pics=new Ppics();
						pics.setPath(goods.getCover().getCoverPath().replace("http://9900oss.oss-cn-hangzhou.aliyuncs.com/",""));
						pics.setMainType(1);
						pics.setId(goods.getCover().getCoverId());
						//  picsDao.save(pics);
						dao.save("WxflowerSettingDao.saveGoodsPics",pics);
					}

			}
			saveStandardForInterface(goods);
		}

		res.put("flag","0");
		res.put("msg","操作成功!");

		return res;
	}



	private void saveStandardForInterface(PwxGoods goods) throws Exception {

		if(goods.getStocklist()!=null&&goods.getStocklist().size()>0)
		{
			List<Pstandard> list=new ArrayList<>();
			for(int i=0;i<goods.getStocklist().size();i++)
			{
				if(!StringUtil.isNullOrEmpty(goods.getStocklist().get(i).getTypesId1())) {
					baseStandard s1 = standDao.getById(baseStandard.class, goods.getStocklist().get(i).getTypesId1());
					baseStandard s1p = standDao.getById(baseStandard.class, goods.getStocklist().get(i).getStandard1Pid());
					if(s1==null)
					{
						Pstandard s=new Pstandard();
						s.setId(goods.getStocklist().get(i).getTypesId1());
						s.setName(goods.getStocklist().get(i).getStandard1Name());
					//	s.setShopId(shopId);
						s.setPid(goods.getStocklist().get(i).getStandard1Pid());
						if(list!=null&&list.size()>0) {
							boolean isIn=false;
							for (int j = 0; j < list.size(); j++) {
								if (list.get(j).getId().equals(s.getId()))
								{
									isIn=true;
								}
							}
							if(!isIn)
							{list.add(s);}
						}else
						{
							list=new ArrayList<>();
							list.add(s);
						}
					}
					if(s1p==null)
					{
						Pstandard s=new Pstandard();
						s.setId(goods.getStocklist().get(i).getStandard1Pid());
						s.setName(goods.getStocklist().get(i).getStandard1Pname());
						//s.setShopId(shopId);
						s.setPid("0");
						if(list!=null&&list.size()>0) {
							boolean isIn=false;
							for (int j = 0; j < list.size(); j++) {
								if (list.get(j).getId().equals(s.getId()))
								{
									isIn=true;
								}
							}
							if(!isIn)
							{list.add(s);}
						}else
						{
							list=new ArrayList<>();
							list.add(s);
						}


					}
				}

				if(!StringUtil.isNullOrEmpty(goods.getStocklist().get(i).getTypesId2())) {
					baseStandard s2 = standDao.getById(baseStandard.class, goods.getStocklist().get(i).getTypesId2());
					baseStandard s2p = standDao.getById(baseStandard.class, goods.getStocklist().get(i).getStandard2Pid());
					if(s2==null)
					{
						Pstandard s=new Pstandard();
						s.setId(goods.getStocklist().get(i).getTypesId2());
						s.setName(goods.getStocklist().get(i).getStandard2Name());
					//	s.setShopId(shopId);
						s.setPid(goods.getStocklist().get(i).getStandard2Pid());
						if(list!=null&&list.size()>0) {
							boolean isIn=false;
							for (int j = 0; j < list.size(); j++) {
								if (list.get(j).getId().equals(s.getId()))
								{
									isIn=true;
								}
							}
							if(!isIn)
							{list.add(s);}
						}else
						{
							list=new ArrayList<>();
							list.add(s);
						}
					}
					if(s2p==null)
					{
						Pstandard s=new Pstandard();
						s.setId(goods.getStocklist().get(i).getStandard2Pid());
						s.setName(goods.getStocklist().get(i).getStandard2Pname());
					//	s.setShopId(shopId);
						s.setPid("0");
						if(list!=null&&list.size()>0) {
							boolean isIn=false;
							for (int j = 0; j < list.size(); j++) {
								if (list.get(j).getId().equals(s.getId()))
								{
									isIn=true;
								}
							}
							if(!isIn)
							{list.add(s);}
						}
						else
						{
							list=new ArrayList<>();
							list.add(s);
						}
					}
				}
				if(!StringUtil.isNullOrEmpty(goods.getStocklist().get(i).getTypesId3())) {
					baseStandard s3 = standDao.getById(baseStandard.class, goods.getStocklist().get(i).getTypesId3());
					baseStandard s3p = standDao.getById(baseStandard.class, goods.getStocklist().get(i).getStandard3Pid());

					if(s3==null)
					{
						Pstandard s=new Pstandard();
						s.setId(goods.getStocklist().get(i).getTypesId3());
						s.setName(goods.getStocklist().get(i).getStandard3Name());
					//	s.setShopId(shopId);
						s.setPid(goods.getStocklist().get(i).getStandard3Pid());
						if(list!=null&&list.size()>0) {
							boolean isIn=false;
							for (int j = 0; j < list.size(); j++) {
								if (list.get(j).getId().equals(s.getId()))
								{
									isIn=true;
								}
							}
							if(!isIn)
							{list.add(s);}
						}else
						{
							list=new ArrayList<>();
							list.add(s);
						}
					}


					if(s3p==null)
					{
						Pstandard s=new Pstandard();
						s.setId(goods.getStocklist().get(i).getStandard3Pid());
						s.setName(goods.getStocklist().get(i).getStandard3Pname());
					//	s.setShopId(shopId);
						s.setPid("0");
						if(list!=null&&list.size()>0) {
							boolean isIn=false;
							for (int j = 0; j < list.size(); j++) {
								if (list.get(j).getId().equals(s.getId()))
								{
									isIn=true;
								}
							}
							if(!isIn)
							{list.add(s);}
						}
						else
						{
							list=new ArrayList<>();
							list.add(s);
						}

					}
				}
			}
			if(list!=null&&list.size()>0){
				Map<String,Object> map=new HashMap<>();
				map.put("list",list);
				dao.save("WxflowerSettingDao.saveStandardForInterface",map);}
		}
	}





	@Override
	public synchronized Map<String, Object> saveGoods(List<PwxGoods> goodsList) throws Exception {
		Map<String,Object> res=new HashMap<>();
		for(PwxGoods goods:goodsList)
		{
			Twxgoods tgoods=wxgoodsDao.getByHql("from Twxgoods where goodsIdSup='"+goods.getId()+"'");
			if(tgoods!=null)
			{updateGoodsStock(goods,tgoods);}
			else
			{saveGoodsStock(goods);}
		}
		res.put("flag","0");
		res.put("msg","操作成功!");
		return res;
	}

    @Override
    public Map<String, Object> updatecoin_90_(Map<String, Object> map) throws Exception {
        Map<String, Object> jSONObject = new HashMap<>();
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
                isBalance=giveAmount>=amount;//手动发券计算充值金额够不够
            }else
            {isBalance=true;}
        }
        if(checkMap!=null&&checkMap.size()>0)
        {
            jSONObject.put("flag", "3");
            jSONObject.put("msg", "操作失败，重复调用");
        }else if(!isBalance)
        {
            jSONObject.put("flag", "2");
            jSONObject.put("msg", "操作失败，余额不足");
        }
        else//开始操作90币
        {
            Map<String, Object> map1=new HashMap<>();
            map1.put("userId", map.get("userId"));
            map1.put("state", map.get("state"));
            map1.put("orderNum", map.get("orderNum"));
            map1.put("source", map.get("source"));
            map1.put("amount",amount);
            System.out.println("doGiveCoin=====================>"+map1);
            jSONObject = doOperCoin("BaseUserDao.doGiveCoin", "用户退款", "1", map1,"IN");
            jSONObject.put("flag", "0");
            jSONObject.put("msg", "操作成功");
        }

        return jSONObject;
    }

    private void saveGoodsStock(PwxGoods goods) {
		Twxgoods tgoods=new Twxgoods();
		tgoods.setName(goods.getName());
		//String appid = ConfigUtil.get("appid");
		String appid = Global.getAppId();
		System.out.println("appid:" + appid);
		Map<String,Object> resultMap = null;
		try {
			resultMap = userService.getShopIdByAppId(appid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resultMap != null && resultMap.get("shopid") != null) {
			tgoods.setBuyerId((String) resultMap.get("shopid"));
		}
		tgoods.setCoverId(goods.getNewCoverId());
		tgoods.setCode(goods.getCode());
		tgoods.setInfo(goods.getComment());
		tgoods.setBrand(goods.getBrand());
		tgoods.setUnitId(goods.getUnitId());
		tgoods.setIsTicket(goods.getGoodsType());
		tgoods.setCreateTime(new Date());
		tgoods.setGoodsIdSup(goods.getId());
		tgoods.setVenderIdMain(ConfigUtil.get("venderId"));
		try{tgoods.setTicketType(Integer.valueOf(goods.getTicketType()));}catch (Exception e)
		{tgoods.setTicketType(0);}
		try{tgoods.setStartTime(DateUtil.string2Date(goods.getStartTime()));}catch (Exception e)
		{tgoods.setStartTime(null);}
		try{tgoods.setEndTime(DateUtil.string2Date(goods.getEndTime()));}catch (Exception e)
		{tgoods.setEndTime(null);}

		wxgoodsDao.save(tgoods);//更新商品信息

		//stock操作
		for(PwxGoodsStock stock:goods.getStocklist())
		{
			saveStocks(stock,tgoods.getId());
		}
		//tag关联操作
			TgoodsTag tagNew=new TgoodsTag();
			tagNew.setIsdel((short) 0);
			tagNew.setCreateTime(new Date());
			tagNew.setTagId(goods.getTag());
			tagNew.setGoodsId(tgoods.getId());
			goodsTagDao.save(tagNew);
			Ttag tt=tagDao.getById(Ttag.class,tagNew.getTagId());
			tt.setNum(tt.getNum()+1);
			tagDao.update(tt);

		//图片操作
		for(PwxGoodsStock stock:goods.getCoverList())
		{
			Tpics pic=picsDao.getById(Tpics.class,stock.getCoverId());
			if(pic!=null)
			{pic.setMainId(tgoods.getId());
				picsDao.update(pic);
			}
		}
	}

	private void updateGoodsStock(PwxGoods goods, Twxgoods tgoods) throws Exception {

		tgoods.setName(goods.getName());
		tgoods.setCode(goods.getCode());
		tgoods.setInfo(goods.getComment());
		tgoods.setBrand(goods.getBrand());
		tgoods.setUnitId(goods.getUnitId());
		tgoods.setCoverId(goods.getNewCoverId());
		tgoods.setIsTicket(goods.getGoodsType());
		//String appid = ConfigUtil.get("appid");
		String appid = Global.getAppId();
		System.out.println("appid:" + appid);
		Map<String,Object> resultMap = null;
		try {
			resultMap = userService.getShopIdByAppId(appid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (resultMap != null && resultMap.get("shopid") != null) {
			tgoods.setBuyerId((String) resultMap.get("shopid"));
		}
		try{tgoods.setTicketType(Integer.valueOf(goods.getTicketType()));}catch (Exception e)
		{tgoods.setTicketType(0);}
		try{tgoods.setStartTime(DateUtil.string2Date(goods.getStartTime()));}catch (Exception e)
		{tgoods.setStartTime(null);}
		try{tgoods.setEndTime(DateUtil.string2Date(goods.getEndTime()));}catch (Exception e)
		{tgoods.setEndTime(null);}
		wxgoodsDao.update(tgoods);//更新商品信息

		//stock操作
		for(PwxGoodsStock stock:goods.getStocklist())
		{
			TwxgoodsStock tstock=wxgoodsStockDao.getByHql("from TwxgoodsStock where skuId='"+stock.getId()+"'");
			if(tstock!=null)
			{
				tstock.setStock(tstock.getStock()+stock.getStock());
				tstock.setStockNow(tstock.getStockNow()+stock.getStock());
                tstock.setPrice(stock.getPriceDiaopai());
                tstock.setPriceMoney(stock.getPriceMoney());
				if(tstock.getStockNow()<0)
				{
					throw new RuntimeException("NOSTCOK");
				}
				wxgoodsStockDao.update(tstock);
			}else
			{
				saveStocks(stock,tgoods.getId());
			}
		}


		//tag关联操作
		TgoodsTag tag=goodsTagDao.getByHql("from TgoodsTag where goodsId='"+tgoods.getId()+"'");
		if(!tag.getTagId().equals(goods.getTag()));
		{
			Ttag tt=tagDao.getById(Ttag.class,tag.getTagId());
			tt.setNum(tt.getNum()-1);
			tagDao.update(tt);
			goodsTagDao.delete(tag);
			TgoodsTag tagNew=new TgoodsTag();
			tagNew.setIsdel((short) 0);
			tagNew.setCreateTime(new Date());
			tagNew.setTagId(goods.getTag());
			tagNew.setGoodsId(tgoods.getId());
			goodsTagDao.save(tagNew);
			tt=tagDao.getById(Ttag.class,tagNew.getTagId());
			tt.setNum(tt.getNum()+1);
			tagDao.update(tt);
		}
		//图片操作
	List<Tpics> picsList=	picsDao.find("from Tpics  where mainId='"+tgoods.getId()+"'");
		Map<String,String> map=new HashMap<>();
		map.put("id",tgoods.getId());
		//dao.update("WxflowerSettingDao.updateCleanPic",map);
		for(PwxGoodsStock stock:goods.getCoverList())
		{
			Tpics pic=picsDao.getById(Tpics.class,stock.getCoverId());
			if(pic!=null)
			{pic.setMainId(tgoods.getId());
			picsDao.update(pic);
			}
		}
		for(Tpics picOld:picsList)
		{
			boolean isIn=false;
			for(PwxGoodsStock stock:goods.getCoverList())
			{
				if(stock.getCoverId().equals(picOld.getId()))
				{
					isIn=true;break;
				}
			}
			if(!isIn)
			{
				picOld.setMainId("");
				picsDao.update(picOld);
			}
		}
	}

	private void saveStocks(PwxGoodsStock stock,String goodsId) {
		TwxgoodsStock tstock=new TwxgoodsStock();
		tstock.setStockNow(stock.getStock());
		tstock.setStock(stock.getStock());
		tstock.setCreateTime(new Date());
		tstock.setGoodsId(goodsId);
		tstock.setIsdel((short) 0);
		tstock.setPrice(stock.getPriceDiaopai());
		tstock.setPriceMoney(stock.getPriceMoney());
		tstock.setSaleCount(0);
		tstock.setSkuId(stock.getId());
		tstock.setStockOccupy(0);
		tstock.setVenderId(stock.getVenderId());
		if(tstock.getStockNow()<0)
		{
			throw new RuntimeException("NOSTCOK");
		}
		if(!StringUtil.isNullOrEmpty(stock.getTypesId1()))
		{
			String proId=savePro(stock.getTypesId1(),goodsId);
			tstock.setTypesId1(proId);
		}
		if(!StringUtil.isNullOrEmpty(stock.getTypesId2()))
		{
			String proId=savePro(stock.getTypesId2(),goodsId);
			tstock.setTypesId2(proId);
		}
		if(!StringUtil.isNullOrEmpty(stock.getTypesId3()))
		{
			String proId=savePro(stock.getTypesId3(),goodsId);
			tstock.setTypesId3(proId);
		}
		wxgoodsStockDao.save(tstock);
	}

	private String savePro(String typeId, String goodsId) {

		List<TwxgoodsProperty> list =wxgoodsPropertyDao.find("from TwxgoodsProperty where goodsId='"+goodsId+"' and property='"+typeId+"'");
		TwxgoodsProperty tp;
		String id="";
		if(list!=null&&list.size()>0)
		{ 	tp=list.get(0);
			id=tp.getId();}
		else
		{
			tp=new TwxgoodsProperty();
			tp.setCreateTime(new Date());
			tp.setIsdel((short) 0);
			tp.setGoodsId(goodsId);
			tp.setProperty(typeId);
			tp.setType((short) 1);
			id=(String) wxgoodsPropertyDao.save(tp);
		}
		return  id;
	}



	@Override
	public void sendChangeNoticeTemplate(String mainId){
		//发送模板消息
		try{
			ChangeNoticeTemp changeNoticeTemp =
					(ChangeNoticeTemp)dao.findForObject("QtGoodsDao.getChangeNoticeTempByMainId",mainId);

			if(changeNoticeTemp != null)
			{
				String goodsName = changeNoticeTemp.getGoodsName();
				String changeNumber = mainId;
				String count = changeNoticeTemp.getCount();
				String remark = "感谢您的使用！";
				String openId = changeNoticeTemp.getOpenId();

				Map<String,Object> map =
						wxTemplateService.sendChangeNoticeTemplate(
								goodsName,changeNumber,count,remark,openId);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> updateUserBalance_90(Map<String, Object> map) throws Exception {
		if(!map.containsKey("ticketType")|| StringUtil.isNullOrEmpty(map.get("ticketType"))||!StringUtil.isNumeric(map.get("ticketType")+""))
		{map.put("ticketType","0");}

		JSONObject jSONObject = new JSONObject();
		List<Map<String,String>> checkMap=(List<Map<String, String>>) dao.findForList("BaseUserDao.getUserDetail90ByWx", map);//获取流水记录防止重复调用
		Map<String,Object> user=(Map<String, Object>) dao.findForObject("BaseUserDao.getUserBalanceByOpenId", map);//获取用户id和剩余久零券
		Map<String,Object> userMain=(Map<String, Object>) dao.findForObject("BaseUserDao.getUserByOpenId", map);//获取用户id和剩余久零券
		TBaseBrand brand=null;
		String brandCode=map.get("brand_code")+"";
		if(!StringUtil.isNullOrEmpty(brandCode))
		{brand=basebrandao.getById(TBaseBrand.class,brandCode);}
		Date expiryTime=null;
        if(brand!=null){
            map.put("sorts",brand.getSorts());
        }else
        {
            map.put("sorts",99);
        }
String ticketType=map.get("ticketType").toString();
		String source=map.get("source").toString();
		String state=map.get("state").toString();
		Double amount=Double.valueOf(map.get("balance_90").toString());

		boolean isBalance=false;
		boolean isBalanceOK=true;
		if(user!=null)
		{
			String userBalance=user.get("balance_90").toString();
			String balanceShop=user.get("balance_90_shop").toString();
			String balanceExperience=user.get("balance_90_experience").toString();
			double balance_90=Double.parseDouble(userBalance);
			double balance_shopping_90=Double.parseDouble(balanceShop);
			double balance_experience_90=Double.parseDouble(balanceExperience);
			double balance_90Main=Double.parseDouble(userMain.get("balance_90")+"");
			double balance_shop_main=Double.parseDouble(userMain.get("balance_shopping_90")+"");
			if("1".equals(state))//如果是要支出久零券的，校验是否够
			{
				if("1".equals(ticketType))
				{
					isBalance=balance_shopping_90>=amount;
				}else if("0".equals(ticketType)) {
					isBalance=balance_90>=amount;
				}else if("2".equals(ticketType)){
					isBalance=balance_experience_90>=amount;
				}
				map.put("point90Now",0);
				if((Math.abs(balance_90Main-balance_90)>0.01)||(Math.abs(balance_shop_main-balance_shopping_90)>0.01))
				{
					isBalanceOK=false;
				}

				if("9".equals(source)||"10".equals(source))//当撤回的时候，需要撤回总额
				{
					map.put("balance_90Total",map.get("balance_90"));
				}else
				{
					map.put("balance_90Total",0);
				}


			}else{
				map.put("point90Now",amount);
				isBalance=true;

				if(!"5".equals(source))//当退款的时候，不增加总额
				{
					map.put("balance_90Total",map.get("balance_90"));
				}else
				{
					map.put("balance_90Total",0);
				}

				//获取90券的时候读取商户信息获得过期时间
				int expiryDateType=2;
				if("2".equals(ticketType))//只有体验券过期时间是读取的，别的固定1年
				{
					if(brand!=null)
					{expiryDateType=brand.getExpiryDateType();}
					if(StringUtil.isNullOrEmpty(expiryDateType)||!StringUtil.isNumeric(expiryDateType+""))
					{
						expiryDateType=2;
					}
				}
				Date grantTime=new Date();
				try{
					grantTime=getGrantTime(map);
				}
				catch (Exception e)
				{
					e.getMessage();
				}
				String expiryTimeString=getExpiryTime(grantTime,expiryDateType);
				if(!StringUtil.isNullOrEmpty(expiryTimeString))
				{
					SimpleDateFormat sdfTime = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					expiryTime=sdfTime.parse(expiryTimeString);
				}
				else
				{
					SimpleDateFormat sdfTime = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					expiryTime=sdfTime.parse(DateUtil.laterDate(365));
				}
			}
		}
		map.put("expiryTime",expiryTime);
		if("2".equals(source)||"11".equals(source))//如果是手动发券的，需要计算商户的余额够不够
		{
			if(brand!=null) {
				if("0".equals(ticketType))
				{if (amount > brand.getBalance90() + brand.getCreditTotal90() - brand.getCreditNow90()) {
					isBalance = false;
				} else {
					isBalance = true;
				}
				} else  if("1".equals(ticketType))
				{
					if (amount > brand.getBalance90shop() + brand.getCreditTotal90shop() - brand.getCreditNow90shop()) {
						isBalance = false;
					} else {
						isBalance = true;
					}
				} else  if("2".equals(ticketType))
				{
					if (amount > brand.getBalance90experience() + brand.getCreditTotal90experience() - brand.getCreditNow90experience()) {
						isBalance = false;
					} else {
						isBalance = true;
					}
				}


			}else
			{
				isBalance = false;
			}
		}

		if(!isBalanceOK)
		{
			jSONObject.put("flag", "4");
			jSONObject.put("msg", "操作失败，发券记录与用户余额不匹配");
			dao.update("BaseUserDao.updateUserForBalance90Wrong",map);
			return jSONObject;
		}
		if(checkMap!=null&&checkMap.size()>0)
		{
			jSONObject.put("flag", "3");
			jSONObject.put("msg", "操作失败，重复调用");
			return jSONObject;
		}else if(!isBalance)
		{
			jSONObject.put("flag", "2");
			jSONObject.put("msg", "操作失败，余额不足");
			return jSONObject;
		}
		else /*if((!"9".equals(source))||(!"10".equals(source)))*///source为9或者10的时候，为撤销发券的动作，单独处理
		{
		/*	if(!"5".equals(source)||"9".equals(source)||"10".equals(source))
			{map.put("balance_90Total",map.get("balance_90"));}
			else
			{

			}*/

			String mainId= map.get("order_code")+"";
			if("3".equals(source)){
				int res=doOperBalanceForConsume(user,map,mainId);//当为消费久零券的时候，进行久零券的消券操作
				if(res<0)//消费处理失败，直接返回失败
				{
					jSONObject.put("flag", "2");
					jSONObject.put("msg", "操作失败，没有加券记录用于消费");
					throw new Exception("操作失败，没有加券记录用于消费");
				}
			}
            if("5".equals(source)){
				TbaseDetail detail =baseDetailDao.getByHql("from TbaseDetail where source=3 and isdel=0 and sourceId='"+map.get("order_code")+"' and ticketType='"+map.get("ticketType")+"'");
				map.put("ticketType",detail.getTicketType());
                int res=doOperBalanceForRefundsNew(user,map,mainId);//当为退款退券的时候，单独处理
                if(res<0)//消费处理失败，直接返回失败
                {
                    jSONObject.put("flag", "2");
                    jSONObject.put("msg", "操作失败，没有加券记录用于消费");
					throw new Exception("操作失败，没有加券记录用于消费");
                }

				ticketType=detail.getTicketType()+"";
				map.put("point90Now",res);
            }
            if("9".equals(source)||"10".equals(source)) {//单独处理撤回
				TbaseDetail detail =baseDetailDao.getById(TbaseDetail.class,map.get("order_code")+"");
				map.put("ticketType",detail.getTicketType());

				jSONObject = doCancalNew(map.get("order_code") + "", user, source,mainId,ticketType);
                if(!"0".equals(jSONObject.get("flag")))
                {throw new Exception("");}
				ticketType=detail.getTicketType()+"";
            }
            if("1".equals(ticketType))
			{dao.update("BaseUserDao.operUserShopBalance90ByWx", map);}
            	else if("0".equals(ticketType))
			{dao.update("BaseUserDao.operBalance90ByWx", map);}//修改用户余额
			else if("2".equals(ticketType)){
				dao.update("BaseUserDao.operExperienceBalance90ByWx", map);//修改体验券
			}


			map.put("userId",user.get("id"));
			dao.save("BaseUserDao.saveDetail90ByWx", map);//插入流水记录
				jSONObject.put("flag", "0");
				jSONObject.put("msg", "操作成功");

			if("2".equals(source)||"11".equals(source))//手动发券的时候操作商户的久零券余额
			{
				if(brand!=null)
				{
					if("0".equals(ticketType))
					{
						if(brand.getBalance90()>amount)
						{brand.setBalance90((long) (brand.getBalance90()-amount));}
						else
						{
							brand.setCreditNow90((int) (amount-brand.getBalance90()));
							brand.setBalance90((long) 0);
						}
					}else if("1".equals(ticketType))
					{
						if(brand.getBalance90shop()>amount)
						{brand.setBalance90shop((long) (brand.getBalance90shop()-amount));}
						else
						{
							brand.setCreditNow90shop((int) (amount-brand.getBalance90shop()));
							brand.setBalance90shop((long) 0);
						}
					}else if("2".equals(ticketType))
					{
						if(brand.getBalance90experience()>amount)
						{brand.setBalance90experience((long) (brand.getBalance90experience()-amount));}
						else
						{
							brand.setCreditNow90experience((int) (amount-brand.getBalance90experience()));
							brand.setBalance90experience((long) 0);
						}
					}

					basebrandao.update(brand);
				}
			}
		}/*else//source为9的时候为手动发券撤回，10为其他撤回，单独处理
		{

		}*/

		return jSONObject;
	}

	private Date getGrantTime(Map<String, Object> map) throws Exception {
		String source=map.get("source").toString();
		String sourceId=map.get("order_code").toString();
		String type=map.get("type").toString();
		String grantString="";
		if("2".equals(source))//手动发券
		{
			grantString= (String) dao.findForObject("CustomQrcodeDao.getCreateTimeByMainCode",sourceId);
		}
		else if("4".equals(source))//实体卡
		{
			grantString= (String) dao.findForObject("offlineCardDao.getCreateTimeByCardCode",sourceId);
		}
		else if("1".equals(source))//自动发券
		{
			if("2".equals(type))//微商城的查询微商城表，其他查询联盟订单表
			{
				grantString= (String) dao.findForObject("OrderMainDao.getCreateTimeByCode",sourceId);
			}
			else
			{
				grantString= (String) dao.findForObject("OrderMainUnionDao.getCreateTimeByMainCode",sourceId);
			}
		}
		if(StringUtil.isNullOrEmpty(grantString)) {
			return new Date();
		}
		else
		{
			return DateUtil.string2Date(grantString);
		}

	}

	private String getExpiryTime(Date grantTime,int expiryDateType) {
		String dateString="";
		switch (expiryDateType)
		{
			case 1:dateString=laterDate(grantTime,90);break;
			case 2:dateString=laterDate(grantTime,365);break;
			case 3:dateString=laterDate(grantTime,1095);break;
			default:dateString=laterDate(grantTime,365);break;
		}
		return  dateString;
	}
	public  String laterDate(Date grantTime,int days) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date temp_date = null;
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(grantTime);
			c.add(Calendar.DATE, days);
			temp_date = c.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return format.format(temp_date);
	}
	private int doOperBalanceForRefundsNew(Map<String, Object> user, Map<String, Object> map,String id) throws Exception {

			List<Map<String,Object>> list= (List<Map<String,Object>>) dao.findForList("OrderDao.getLogByOrderId",map);
		if(list.size()<=0)
		{throw  new Exception("没有找到对应的消费记录");}
		Integer amount=Integer.valueOf(map.get("balance_90").toString());
		for(Map<String,Object> log:list)
		{
			Integer point=Integer.valueOf(log.get("point90")+"");
			TbaseDetail detail =baseDetailDao.getById(TbaseDetail.class,log.get("sourceId")+"");
			Tbase90DetailLog detailLog=new Tbase90DetailLog();
			detailLog.setType(2);
			detailLog.setOrderId(id);
			detailLog.setSourceId(detail.getId());
			detailLog.setTicketType(map.get("ticketType")+"");
			if(point>=amount)
			{
				detail.setPoint90Now(detail.getPoint90Now()+amount);
				detailLog.setPoint90(amount);
				amount=0;

			}else
			{
				detail.setPoint90Now(detail.getPoint90Now()+point);
				amount=amount-point;
				detailLog.setPoint90(point);
			}
			baseDetailDao.update(detail);
			baseDetailLogDao.save(detailLog);
			if(amount<=0)
			{break;}
		}
		return amount;

	}
	private int doOperBalanceForRefunds(Map<String, Object> user, Map<String, Object> map,String id) {


        String hql=StringUtil.formateString("from TbaseDetail t where t.openId='{0}'  and t.state=0 and t.isdel=0 and t.inOuts=1 order by t.expireTime desc",map.get("open_id")+"");
        List<TbaseDetail> baseDetailList=baseDetailDao.find(hql);
        Integer amount=Integer.valueOf(map.get("balance_90").toString());
        if(baseDetailList.size()<=0)//如果没有找到加券记录，返回-1
        {return -1;}
        else//否则对记录按照顺序循环操作
        {
            for(TbaseDetail detail:baseDetailList)
            {
                if(amount<=0)
                {break;}

                if(detail.getPoint90Now().intValue()==detail.getPoint90().intValue())
                {
                    //没有被消费过的充值记录，不操作
                }
                else if((detail.getPoint90Now()+amount)>detail.getPoint90().intValue())
                {//从被消费过的充值记录开始，补全
					Tbase90DetailLog log=new Tbase90DetailLog();
					log.setType(2);
					log.setOrderId(id);
					log.setSourceId(detail.getId());
					log.setPoint90(detail.getPoint90()-detail.getPoint90Now());
                    amount=amount-(detail.getPoint90()-detail.getPoint90Now());
                    detail.setPoint90Now(detail.getPoint90());
                    baseDetailDao.update(detail);
					baseDetailLogDao.save(log);
                }
                else
                {
					Tbase90DetailLog log=new Tbase90DetailLog();
					log.setType(2);
					log.setOrderId(id);
					log.setSourceId(detail.getId());
					log.setPoint90(amount);
                    detail.setPoint90Now(detail.getPoint90Now()+amount);
                    amount=0;
                    baseDetailDao.update(detail);
					baseDetailLogDao.save(log);
                    break;
                }
            }
            if(amount<=0)
            {return 0;}
            else
            {return amount;}
        }
    }

    private int doOperBalanceForConsume(Map<String, Object> user, Map<String, Object> map,String id) {

		String hql=StringUtil.formateString("from TbaseDetail t where t.openId='{0}' and t.point90Now>0 and t.state=0 and t.isdel=0 and t.inOuts=1 and t.ticketType='{1}' order by t.sorts asc, t.expireTime asc",map.get("open_id")+"",map.get("ticketType")+"");
		List<TbaseDetail> baseDetailList=baseDetailDao.find(hql);

		Integer amount=Integer.valueOf(map.get("balance_90").toString());
		if(baseDetailList.size()<=0)//如果没有找到加券记录，返回-1
		{return -1;}
		else//否则对记录按照顺序循环操作
		{
			for(TbaseDetail detail:baseDetailList)
			{
				if(amount<=0)
				{break;}
				Tbase90DetailLog log=new Tbase90DetailLog();
				log.setType(0);
				log.setOrderId(id);
				log.setSourceId(detail.getId());
				log.setTicketType(map.get("ticketType")+"");
 				if(detail.getPoint90Now().intValue()>=amount)
				{
					log.setPoint90(amount);
					detail.setPoint90Now(detail.getPoint90Now()-amount);
					baseDetailDao.update(detail);
					baseDetailLogDao.save(log);
					break;
				}
				else
				{
					log.setPoint90(detail.getPoint90Now());
					amount=amount-detail.getPoint90Now();
					detail.setPoint90Now(0);
					baseDetailDao.update(detail);
					baseDetailLogDao.save(log);
				}
			}
		}
			return 1;
	}

    private JSONObject doCancalNew(String order_code, Map<String, Object> user, String source, String id, String ticketType) throws Exception {
        JSONObject jSONObject = new JSONObject();
        String userBalance=user.get("balance_90").toString();
        int balance_90=Integer.parseInt(userBalance);
        /**以下进行预处理，判断是否够退券**/
        TbaseDetail td=baseDetailDao.getById(TbaseDetail.class,order_code);
        if ((td!=null)&&(td.getIsdel()==0))
        {
            if(td.getPoint90()>balance_90)
            {
                jSONObject.put("flag", "2");
                jSONObject.put("msg", "操作失败，余额不足");
                return jSONObject;
            }

            Map<String,Object> map=new HashMap<>();
            map.put("balance_90",td.getPoint90());
            map.put("open_id",td.getOpenId());


            /**以下处理对商户进行退券**/
            if(source.equals("9")) {
                TBaseBrand brand = basebrandao.getById(TBaseBrand.class, td.getBrandId());
				if("0".equals(ticketType))
				{
					if (brand.getCreditNow90() >= td.getPoint90()) {
						brand.setCreditNow90(brand.getCreditNow90() - td.getPoint90());
					} else if (brand.getCreditNow90() > 0) {
						brand.setBalance90(brand.getBalance90() + td.getPoint90() - brand.getCreditNow90());
						brand.setCreditNow90(0);
					} else {
						brand.setBalance90(brand.getBalance90() + td.getPoint90());
					}
				}else if("1".equals(ticketType))
				{
					if (brand.getCreditNow90shop() >= td.getPoint90()) {
						brand.setCreditNow90shop(brand.getCreditNow90shop() - td.getPoint90());
					} else if (brand.getCreditNow90shop() > 0) {
						brand.setBalance90shop(brand.getBalance90shop() + td.getPoint90() - brand.getCreditNow90shop());
						brand.setCreditNow90shop(0);
					} else {
						brand.setBalance90shop(brand.getBalance90shop() + td.getPoint90());
					}
				}else if("2".equals(ticketType))
				{
					if (brand.getCreditNow90experience() >= td.getPoint90()) {
						brand.setCreditNow90experience(brand.getCreditNow90experience() - td.getPoint90());
					} else if (brand.getCreditNow90experience() > 0) {
						brand.setBalance90experience(brand.getBalance90experience() + td.getPoint90() - brand.getCreditNow90experience());
						brand.setCreditNow90experience(0);
					} else {
						brand.setBalance90experience(brand.getBalance90experience() + td.getPoint90());
					}
				}

                basebrandao.update(brand);
            }
            /**以下处理对用户进行退券**/
          //  int res=(int) dao.update("BaseUserDao.cancalBalance90ByWx", map);

            if(td.getPoint90Now()>=td.getPoint90())//当该笔记录的余额没有消费的时候，直接扣除,完成退券
            {
				Tbase90DetailLog log=new Tbase90DetailLog();
				log.setType(1);
				log.setOrderId(id);
				log.setSourceId(td.getId());
				log.setPoint90(td.getPoint90());
                td.setPoint90Now(td.getPoint90Now()-td.getPoint90());
				log.setTicketType(map.get("ticketType")+"");
				baseDetailLogDao.save(log);

            }else{
                int amount=td.getPoint90()-td.getPoint90Now();//当余额不足的时候，先扣该笔记录，然后从其他记录扣券
				Tbase90DetailLog log=new Tbase90DetailLog();
				log.setType(1);
				log.setOrderId(id);
				log.setSourceId(td.getId());
				log.setPoint90(td.getPoint90Now());
				log.setTicketType(map.get("ticketType")+"");
                td.setPoint90Now(0);
				baseDetailLogDao.save(log);

                String hql=StringUtil.formateString("from TbaseDetail t where t.openId='{0}' and t.point90Now>0 and t.state=0 and t.isdel=0 and t.inOuts=1 and t.ticketType='{1}' order by t.expireTime asc",map.get("open_id")+"",map.get("ticketType")+"");
                List<TbaseDetail> baseDetailList=baseDetailDao.find(hql);
                if(baseDetailList.size()<=0)//如果没有找到加券记录，返回-1
                {  jSONObject.put("flag", "2");
                    jSONObject.put("msg", "操作失败，余额不足");
                }
                else//否则对记录按照顺序循环操作
                {
                    for(TbaseDetail detail:baseDetailList)
                    {
                        if(amount<=0)
                        {break;}
						 log=new Tbase90DetailLog();
						log.setType(1);
						log.setOrderId(id);
						log.setSourceId(detail.getId());
						log.setTicketType(map.get("ticketType")+"");
                        if(detail.getPoint90Now()>=amount&&!detail.getId().equals(td.getId()))
                        {
							log.setPoint90(amount);
                            detail.setPoint90Now(detail.getPoint90Now()-amount);
                            baseDetailDao.update(detail);
							baseDetailLogDao.save(log);
                            break;
                        }
                        else if(!detail.getId().equals(td.getId()))
                        {
							log.setPoint90(detail.getPoint90Now());
                            amount=amount-detail.getPoint90Now();
                            detail.setPoint90Now(0);
                            baseDetailDao.update(detail);
							baseDetailLogDao.save(log);
                        }
                    }
                }
            }

                td.setIsdel(1);
                td.setCancelTime(new Date());
                baseDetailDao.update(td);
                jSONObject.put("flag", "0");
                jSONObject.put("msg", "操作成功");

        }else
        {
            jSONObject.put("flag", "1");
            jSONObject.put("msg", "操作失败,找不到对应的发券记录");
            return jSONObject;
        }

        return jSONObject;
    }

    @Override
    public Map<String, Object> getStatistics(Map<String, Object> map) throws Exception {


		/**
		 * 需要读取的数据有：三种发券流水总额+详细
		 * 用券流水的总额+详细
		 * 新增会员量
		 * **/
		Map<String,Object> resMap=new HashMap<>();
		//获取新增会员量
		int new_person = (int)dao.findForObject("StatisticsDao.getNewUser", map);
		//获取自动发券数据
		Map<String, String> autoMap=(Map<String, String>) dao.findForObject("StatisticsDao.loadZDInfo",map);
		//获取手动发券数据
		Map<String, String> resff= (Map<String, String>) dao.findForObject("StatisticsDao.loadSDInfoFF",map);
		Map<String, String> rescx= (Map<String, String>) dao.findForObject("StatisticsDao.loadSDInfoCX",map);
		//获取实体卡数据
		Map<String, String> stkMap=(Map<String, String>) dao.findForObject("StatisticsDao.loadSTKInfo",map);
		//获取用券数据
		Map<String, Object> useMap=(Map<String,Object>) dao.findForObject("StatisticsDao.loadTJInfo",map);
		resMap.put("userData",new_person);

		resMap.put("autoData",autoMap);
		resMap.put("manualData",resff);
		resMap.put("manualCancalData",rescx);
		resMap.put("cardData",stkMap);

		resMap.put("voucherData",useMap);


        return resMap;
    }

    @Override
    public Map<String, Object> updateInitUserData() throws Exception {
		Map<String,Object> res = new HashMap<String, Object>();
		//获取所有的用户
		List<PBaseUser> list= (List<PBaseUser>) dao.findForList("BaseUserDao.findAllUser",null);
		//第一层大循环，循环用户
		for(PBaseUser user:list)
		{
			//获取该用户的消费总金额
			String consumeString= (String) dao.findForObject("BaseUserDao.findUserAllConsume",user);
			Integer consumeAll=Integer.valueOf(consumeString);
		//	List<TbaseDetail> listConsume=baseDetailDao.find("from TbaseDetail where isdel=0 and inOuts=0 and openId='"+user.getOpen_id()+"' order by createTime asc ")
			//调子函数进行用户数据初始化
			updateUserBalanceForInit(user,consumeAll);
		}
		res.put("flag", "0");
		res.put("msg","操作成功");

		return res;
    }

	@Override
	public Map<String, Object> updateThirdOrderSendBalance(String code, String openId) throws Exception {
		String url=ConfigUtil.get("apisUrl")+"/api/getOrder.ac";
		Map<String,String> parm=new HashMap<>();
		parm.put("code",code);
		String resString= URLConectionUtil.httpURLConnectionPostDiy(url,parm);
		Map<String,Object> resMap=new HashMap<>();
		Map<String,Object> orderInfo= new HashMap<>();
		try{orderInfo=JSON.parseObject(resString,Map.class);}
		catch (Exception e)
		{
			resMap.put("code","1");
			resMap.put("errorMsg","获取订单数据失败");
		}
		//通过dubbo获取订单信息


		if("0".equals(orderInfo.get("code")))
		{
			resMap=sendBalanceAndCoin(orderInfo,openId);
		}
		else
		{
			return orderInfo;
		}
		if("0".equals(resMap.get("code")))
		{saveCodeDetails(orderInfo,openId);}
		return resMap;
	}

    @Override
    public Map<String, String> getUserInfoByOpenId(String open_id) throws Exception {
		Map<String, Object> map=new HashMap<>();
		map.put("open_id",open_id);
		return (Map<String, String>) dao.findForObject("BaseUserDao.getUserInfoByOpenId",map);
    }

    private void saveCodeDetails(Map<String, Object> orderInfo, String openId) throws Exception {
		orderInfo.put("openId",openId);
		orderInfo.put("detailId",UuidUtil.get32UUID());
		dao.save("OrderDao.saveCodeDetails",orderInfo);
	}

	//发券发贝
	private Map<String,Object> sendBalanceAndCoin(Map<String, Object> orderInfo,String openId) throws Exception {
		Map<String,Object> resMap=new HashMap<>();
		String type=orderInfo.get("type").toString();
		if("0".equals(type)||"2".equals(type))//发券
		{
			String balance=orderInfo.get("couponTotal").toString();
			Map<String,Object> map=new HashMap<>();
			map.put("brand_code",ConfigUtil.get("thirdBrandCode"));
			map.put("shop_code","");
			map.put("user_code","");
			map.put("order_code",orderInfo.get("cdKey"));
			map.put("open_id",openId);
			map.put("type","6");
			map.put("source","1");
			map.put("source_msg","用户消费自动发券");
			map.put("balance_90",Double.valueOf(balance)*100);
			map.put("state","2");
			map.put("commission","0");
			map.put("tradeType","");
			map.put("orderState","1");
			map.put("orderTotal",orderInfo.get("orderTotal"));
			Map<String,Object> backMap=updateUserBalance_90(map);
			if(!"0".equals(backMap.get("flag"))&&!"3".equals(backMap.get("flag")))
			{
				resMap.put("code","1");
				resMap.put("errorMsg",backMap.get("msg"));
				return resMap;
			}
		}
		if("1".equals(type)||"2".equals(type))//发贝
		{
			String coin=orderInfo.get("coinTotal").toString();
			String userId= (String) dao.findForObject("BaseUserDao.getSysUserIdByOpenId",openId);
			Map<String,Object> map=new HashMap<>();
			map.put("userId",userId);
			map.put("state",1);
			map.put("orderNum",orderInfo.get("cdKey"));
			map.put("source",3);
			map.put("amount",coin);
			Map<String,Object> backMap=updatecoin_90(map);
			if(!"0".equals(backMap.get("flag"))&&!"3".equals(backMap.get("flag")))
			{
				resMap.put("code","1");
				resMap.put("errorMsg",backMap.get("msg"));
				return resMap;
			}

		}
		resMap.put("code","0");
		String msgtmp="成功兑换";
		if(!StringUtil.isNullOrEmpty(orderInfo.get("couponTotal")))
		{
			msgtmp+=orderInfo.get("couponTotal")+"久零券,";
		}
		if(!StringUtil.isNullOrEmpty(orderInfo.get("couponTotal")))
		{
			msgtmp+=orderInfo.get("coinTotal")+"久零贝,";
		}
		msgtmp+="请前往个人中心查看！";
		//wxTemplateService.send_kf_template(openId, msgtmp);
		return  resMap;
	}

	private void updateUserBalanceForInit(PBaseUser user, Integer consumeAll) {
		List<TbaseDetail> list=baseDetailDao.find("from TbaseDetail where isdel=0 and inOuts=1 and openId='"+user.getOpen_id()+"' order by createTime asc ");
	if(list!=null&&list.size()>0)
		{
			for(TbaseDetail detail:list)
			{
				Tbase90DetailLog log=new Tbase90DetailLog();
				log.setSourceId(detail.getId());
				log.setType(0);
				log.setOrderId("oldData");
				detail.setPoint90Now(detail.getPoint90());
				detail.setExpireTime(DateUtil.string2Date(laterDate(detail.getCreateTime(),365)));
				if(consumeAll>0&&consumeAll<=detail.getPoint90Now())//有消费金额但是小于这笔券的券额，消费这笔券
				{
					detail.setPoint90Now(detail.getPoint90Now()-consumeAll);
					log.setPoint90(consumeAll);
					consumeAll=0;
				}else if(consumeAll>0&&consumeAll>detail.getPoint90Now())//有消费金额但是大于这笔券的券额，消费这笔券同时减去这笔券的券额
				{

					log.setPoint90(detail.getPoint90Now());
					consumeAll=consumeAll-detail.getPoint90Now();
					detail.setPoint90Now(0);
				}else
				{
					//没有消费金额了，无动作
				}
				baseDetailDao.update(detail);
				baseDetailLogDao.save(log);
			}
		}
	}
}
