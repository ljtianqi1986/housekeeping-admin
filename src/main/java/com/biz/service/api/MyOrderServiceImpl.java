package com.biz.service.api;

import com.alibaba.fastjson.JSON;
import com.biz.conf.Global;
import com.biz.model.Hmodel.TBaseUser;
import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Hmodel.api.TOrderDetail;
import com.biz.model.Hmodel.api.TOrderMain90;
import com.biz.model.Pmodel.QT.PgoodsDetail;
import com.biz.model.Pmodel.QT.PwxGoodsStockHistory;
import com.biz.model.Pmodel.api.*;
import com.biz.model.Pmodel.pay17.Pay17SelectPay;
import com.biz.model.Singleton.ZkNode;
import com.biz.service.base.BaseServiceImpl;
import com.biz.service.base.Pay17ServiceI;
import com.biz.service.basic.ShopServiceI;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;

@Service("myOrderService")
public class MyOrderServiceImpl extends BaseServiceImpl<Object> implements
        MyOrderServiceI {

	@SuppressWarnings("rawtypes")
	@Resource(name = "daoSupport")
	private DaoSupport dao;

    @Autowired
    private ShopServiceI shopService;

    @Resource(name = "pay17Service")
    private Pay17ServiceI pay17Service;


    @Resource(name = "baseUserService")
    private BaseUserServiceI baseUserService;

	@Autowired
	private BaseDaoI<TorderMain> torderMainDao;
    @Autowired
    private BaseDaoI<TBaseUser> tsysUserDao;
	@Autowired
	private BaseDaoI<TOrderDetail> tOrderDetailDao;

    @Resource(name = "wxTemplateService")
    private WxTemplateServiceI wxTemplateService;

	@Autowired
	private BaseDaoI<TOrderMain90> torderMain90Dao;


    //90券接口
    private String Balance_90_url="/api/balance/operUserBalance90.ac";
    //90贝接口
    private String Coin_90_url="/api/balance/operUserCoin90.ac";
    //店小翼支付
    private String dxy_pay_url="phone_swiftPassScanPay.do";

	private String dxy_ali_url="phone_swiftPassAliScanPay.do";
    //店小翼验证
    private String dxy_query_url="phone_swiftPassQuery.do";


	/***
	 * 生成订单
	 * 
	 * 
	 * ***/
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> addproduceOrder(ConfirmAnOrder orderList) throws Exception {
		Map<String,String> resultmap=new HashMap<String,String>();

		String openid=orderList.getOpenid().trim();
		Map<String,Object> usermap=(Map<String,Object>) dao.findForObject("MyOrderDao.getUserMsg", openid);
        if(usermap==null){
            resultmap.put("result", "fail");
            resultmap.put("msg", "用户不存在");
            return resultmap;
        }

		//获取用户信息
		String userId=usermap.get("id").toString();
		
		orderList.setAddressId("");//地址di
		orderList.setGiveType("0");
		orderList.setGiveMsg("");//特殊要求
		orderList.setGivePhone("");//收获人电话
		orderList.setDiscountState("0");//0(0不启用积分抵用，1启用)
		
		//调取收货地址
		Map<String, String> sendGoodsaddress=new HashMap<String,String>();
		sendGoodsaddress.put("addDetail", "");//收货详细地址
		sendGoodsaddress.put("protext", "");//收货省
		sendGoodsaddress.put("citytext", "");//收货市
		sendGoodsaddress.put("areatext", "");//收货区
		sendGoodsaddress.put("name", ""); //收件人姓名
		sendGoodsaddress.put("phone", ""); //收件人电话
		
		//拼装list,sku 集合
		String goodsListString="";//商品skuid，逗号分隔
		for(ConfirmAnOrderGood goods:orderList.getGoodsList()){
			if(goodsListString.equals("")){
				goodsListString+="'"+goods.getStockId()+"'";
			}else{
				goodsListString+=","+"'"+goods.getStockId()+"'";
			}
		}
		
		//调取商品信息
		List<ConfirmAnOrderGood> goodsList=(List<ConfirmAnOrderGood>)dao.findForList("MyOrderDao.getGoodsMsG", goodsListString);
		String shopId="";//店铺id
		String usemodel="";//1:统一运费,2模板运费
		double freightmoney=0.0;//统一运费
		String freightstate="";//运费是否叠加(0:不叠加1:叠加)

		//商品赋值数量
		for(ConfirmAnOrderGood goods:goodsList){
			shopId=goods.getShopId();
			usemodel=goods.getUsemodel();
			freightmoney=goods.getFreightmoney();
			freightstate=goods.getFreightstate();
			for(ConfirmAnOrderGood tmpgoods: orderList.getGoodsList()){
				if(tmpgoods.getStockId().equalsIgnoreCase(goods.getStockId())){
					goods.setCount(tmpgoods.getCount());
				}
			}
		}
		
		//验证
		for(ConfirmAnOrderGood goods:goodsList){
			if(goods.getIsOpen().equals("0")){
				resultmap.put("result", "fail");
				resultmap.put("msg", "店铺未营业");
				return resultmap;
			}
			if(goods.getIsSale().equals("0")){
				resultmap.put("result", "fail");
				resultmap.put("msg", "部分商品已下架");
				return resultmap;
			}if(goods.getCount()>goods.getStockNow()){
				resultmap.put("result", "fail");
				resultmap.put("msg", "库存不足");
				return resultmap;
			}
			if(goods.getCount()>goods.getStockNow()){
				resultmap.put("result", "fail");
				resultmap.put("msg", "库存不足");
				return resultmap;
			}
		}
		
		orderList.setGoodsList(goodsList);
		double integralmoney=0; //积分抵用
		double freight=0; //运费
		//计算运费
		freight=CalculationFreight(goodsList,usemodel,freightmoney,freightstate);
		
		//定义字符串
		double goodsTotal=0.0;//goodsTotal	decimal	商品总金额
		double allTotal=0;//allTotal	decimal	合计总金额
		double payTotal=0;//payTotal	decimal	需要额外支付的费用(抵去抵用券)
		double couponTotal=0;//优惠金额
		double couponsGoodsTotal=0;////goodsTotal	decimal	商品总金额(券)
		double counter_Fee=0;//合集手续费
		
		//List<OrderSend>  listSend=new ArrayList<OrderSend>();
		List<OrderMain> listMain=new ArrayList<OrderMain>();
		List<OrderDetail> listDetail =new ArrayList<OrderDetail>();
		
		OrderMain addOrderMain=new OrderMain();
		//主表属性
    	String mainid= String.valueOf(getRndNumCode()).trim();//主订单id
    	addOrderMain.setId(mainid);
    	String maincode= String.valueOf(getRndNumCode()).trim();
    	addOrderMain.setCode(maincode); //	code	varchar	订单编号
    	addOrderMain.setBuyUserId(userId); //buyUserId	varchar	购买人
    	addOrderMain.setBuyAddr(sendGoodsaddress.get("addDetail"));
    	addOrderMain.setPro(sendGoodsaddress.get("protext"));
    	addOrderMain.setCity(sendGoodsaddress.get("citytext"));
    	addOrderMain.setArea(sendGoodsaddress.get("areatext"));
    	addOrderMain.setState("0");//state	smallint	0:未付款1:已付款，2，订单处理中
    	addOrderMain.setIsGroup("3");//isGroup	smallint	0:正常订单1:团购订单,3线下订单
    	addOrderMain.setShopId(shopId);//varchar	商店id
    	addOrderMain.setGiveType(orderList.getGiveType());//smallint (0送自己，1送他人)
    	addOrderMain.setGiveMsg(orderList.getGiveMsg());//varchar	 特殊要求
    	addOrderMain.setGivePhone(orderList.getGivePhone());//varchar	电话
        addOrderMain.setPurchasingId(orderList.getPurchasingId());
    	if(Tools.isEmpty(orderList.getMessage())){
    		addOrderMain.setIsMessage("0");
    		addOrderMain.setMessage(orderList.getMessage());//varchar	贺卡留言，不启用则空
    	}else{
    		addOrderMain.setIsMessage("1");
    	}	
    	addOrderMain.setCouponTotal(couponTotal);
    	addOrderMain.setDiscountState(orderList.getDiscountState());//(0不启用积分抵用，1启用)
    	addOrderMain.setIntegralTotal(integralmoney);//积分抵用金额
    	addOrderMain.setUsemodel(orderList.getUsemodel());//smallint  1:统一运费,2模板运费
    	addOrderMain.setFreightstate(orderList.getFreightstate());//smallint  运费是否叠加(0:不叠加1:叠加)
    	addOrderMain.setFreightTotal(freight);//  decimal	物流金额
    	
    	for(ConfirmAnOrderGood goods:orderList.getGoodsList()){
    		
    		/**生成明细表填 OrderDetail*/
    		double goodsDetailTotal=0;//goodsTotal	decimal	商品总金额
    		double goodsPrice=goods.getPrice();//goodsTotal	decimal	商品总金额
    		int goodsCount=goods.getCount();//商品数量
    		goodsDetailTotal=goodsPrice*goodsCount;
    		
    		OrderDetail addOrderDetail=new OrderDetail();
    		addOrderDetail.setId(UuidUtil.get32UUID());
    		addOrderDetail.setOrderId(mainid) ;//orderId	varchar	订单总表
    		addOrderDetail.setGoodsId(goods.getGoodsId());//goodsId	varchar	商品编码
    		addOrderDetail.setPrice(goods.getPrice());//price	decimal	价格
    		addOrderDetail.setCount(goodsCount);//count	int	数量
    		addOrderDetail.setGoodsTotal(goodsDetailTotal);//单个商品总额
    		addOrderDetail.setSendId("");//sendId	varchar	物流对应id
    		addOrderDetail.setState("0");//state	smallint	0:待付款1:待发货2:待收货3:待评价4:完成5:退款中6:已退款7:待备货8:未备货取消
    		addOrderDetail.setIncom("0");//incom	int	来源（0：普通店家 1:闪电发货）
    		addOrderDetail.setShopId(shopId);//varchar	仓库ID
    		addOrderDetail.setFreightid("");//  varchar  模版id
    		addOrderDetail.setTypesId1(goods.getTypesId1());//   规格
    		addOrderDetail.setTypesId2(goods.getTypesId2());//   规格
    		addOrderDetail.setTypesId3(goods.getTypesId3());//   规格
    		addOrderDetail.setIntegralTotal(0);
    		addOrderDetail.setCouponTotal(0);//优惠券合计费用
    		addOrderDetail.setGoodsType(goods.getIsTicket());
			addOrderDetail.setStockId(goods.getStockId());
    		if(goods.getIsTicket()==0){
    			//普通商品
				addOrderDetail.setCouponsPayTotal(goodsDetailTotal);
				addOrderDetail.setPayTotal(goodsDetailTotal*0.03);
				couponsGoodsTotal+=goodsDetailTotal;//点券总金额
				counter_Fee+=(goodsDetailTotal*0.03);//合集手续费总费用
    		}else if(goods.getIsTicket()==1){
    			//点券商品
				addOrderDetail.setPayTotal(goodsDetailTotal);
				goodsTotal+=goodsDetailTotal;//商品总金额
    		}
    		
    		listDetail.add(addOrderDetail);		    
    	}
    	
    	addOrderMain.setGoodsTotal(goodsTotal);
    	addOrderMain.setCouponsPayTotal(couponsGoodsTotal);
    	allTotal=goodsTotal+freight+counter_Fee;
    	payTotal=allTotal-(integralmoney+couponTotal);
    	addOrderMain.setAllTotal(allTotal);
    	addOrderMain.setPayTotal(payTotal);
    	listMain.add(addOrderMain);
    	
    	
    	//比对价格是否和前台一致
    	double totalmoneychae=orderList.getPayTotal()-payTotal;
    	//对比点券是否和前台一致
    	double couponstotalmoneychae=orderList.getCouponsPayTotal()-couponsGoodsTotal;
    	
    	if(totalmoneychae>1 || totalmoneychae<-1){
    		resultmap.put("result", "fail");
			resultmap.put("msg", "支付金额出错");
			return resultmap;
    	}
    	else if(couponstotalmoneychae>0.1 || couponstotalmoneychae<-0.1){
    		resultmap.put("result", "fail");
			resultmap.put("msg", "点券金额出错");
			return resultmap;
    	}
    		else {
    		//存订单主表 //存订单明细表 //合并价格
			boolean flag =saveOrderAndOrderSend(listMain, listDetail);
			//更改库存		
			boolean flag1 = updateGoodsCount(orderList);
    	    if((!flag)||(!flag1)){
    	    	Double.parseDouble("错误");
    	    }
    	}
    	
    	resultmap.put("result", "success");
    	resultmap.put("ordercode", maincode);
		resultmap.put("msg", "下单成功");

		return resultmap;

	}

    @Override
    public Map<String, String> changeOrderState(String ids, String states,String mainStates) throws Exception {
        Map<String,String> resultmap=new HashMap<String,String>();
        Map<String,String> map=new HashMap<>();
        String[] id = ids.split(",");
        String[] state = states.split(",");
        String[] mainState=mainStates.split(",");
        if (id.length == state.length && id.length != 0&&id.length==mainState.length) {
            for(int i=0;i<id.length;i++){
                map.put("id",id[i]);
                map.put("state",state[i]);
                dao.update("OrderMainDao.changeOrderState",map);
                if(mainState[i]!=null&&mainState[i].equals("1")){
                    dao.update("OrderMainDao.changeMainState",map);
                }
            }
            resultmap.put("result","succcess");
            resultmap.put("msg","修改成功");
        }else{
            resultmap.put("result","fail");
            resultmap.put("msg","商品数量和订单状态数量不一致");
        }
        return resultmap;
    }

    @Override
    public Map<String, Object> getOrdersByBrandId(String brandId,String code,String beginTime,String endTime, String page, String rows) throws Exception {
		Map<String, Object> map=new HashMap<>();
		map.put("brandId",brandId);
        map.put("code",code);
        map.put("startDate",beginTime);
        map.put("endDate",endTime);
		int pg=0;
		int row=10;
		try{

			row= Integer.valueOf(rows);
		}
		catch (Exception e)
		{row=10;}
		try{
			pg= Integer.valueOf(page)*row;
		}
		catch (Exception e)
		{
			pg=0;
		}
		map.put("begin",pg);
		map.put("rows",row);
		List<Map<String, Object>> pager_back = (List<Map<String, Object>>) dao
				.findForList("OrderDao.getOrdersByBrandId", map);
		List<Map<String, Object>> count= (List<Map<String, Object>>) dao.findForList("OrderDao.getOrdersByBrandIdCount", map);
		map.clear();
		map.put("count",count.size());
		map.put("flag", "0");//销券成功
		map.put("msg", "查询完毕");
		map.put("orderList",pager_back);
		return map;
    }

    //预占库存操作
		public boolean updateGoodsCount(ConfirmAnOrder orderList) throws Exception {
			List<Map<String, Object>> countList = new ArrayList<Map<String, Object>>();
			List<ConfirmAnOrderGood> goodsList = orderList.getGoodsList();
	
			boolean p=true;
			for(ConfirmAnOrderGood goods:goodsList){
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("count", goods.getCount());
				model.put("stockId", goods.getStockId());
				countList.add(model);
				
				int sc = (int) dao.update("MyOrderDao.decreaseStoreCount2",model);
				if(sc==0)
				{
					p=false;
				}
			}
			//批量减预占及库存			
			//int sc = (int) dao.update("MyOrderDao.decreaseStoreCount",countList);
			//return sc>0;
			return p;
		}
	
	
	/**
	 * 生成订单,保存订单数据
	 * @param 
	 * 
	 * 
	 * @return
	 */
	public boolean saveOrderAndOrderSend(List<OrderMain> listMain,List<OrderDetail> listDetail) throws Exception {
		int sc1=0;
		int sc2=0;
		int sc3=0;
		for(OrderMain tmp1: listMain){
			sc1=(int)dao.save("MyOrderDao.insertOrderMain", tmp1);
		}
		for(OrderDetail tmp3: listDetail){
			sc3=(int)dao.save("MyOrderDao.insertOrderDetail", tmp3);
		}
		return sc1>0&&sc3>0;
	}
	
	
	/**
	 * 
	 * @param goodsList
	 * @param usemodel 1:统一运费,2模板运费
	 * @param freightmoney 统一运费
	 * @param freightstate 运费是否叠加(0:不叠加1:叠加)
	 * @return
	 */
	public double CalculationFreight(List<ConfirmAnOrderGood> goodsList,String usemodel,double freightmoney,String freightstate){

		double freight=0.0;
		if(usemodel.equals("1"))
		{
			if(freightstate.equals("0"))
			{
				freight=freightmoney;
			}else if(freightstate.equals("1")){
				int goodsListCount=goodsList.size();
				freight=goodsListCount*freightmoney;
			}
		}
		return freight;
	}
	
	
	
	
	public long getRndNumCode()
	{
		Random rand = new Random();
		Date now = new Date();
		long t = now.getTime();
		return Long.valueOf(t * 1000L + (long) rand.nextInt(1000));
	}



    /********************************开始：直接付款订单****************************************/
    //生成订单
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> addGenerateOrderMain90(String jsonSting) throws Exception {
        Map<String, Object> r_map=new HashMap<>();
        String result="fail";//fail:生成失败,success :成功
        String orderCode="";//订单号
        String openid="";//订单用户openid
        String msg="";//提示内容
        try {
            PgenerateOrder order_cs = new PgenerateOrder();
            if (jsonSting.equals(null) || (jsonSting.length()==0)) {
                return returnMap_order(result, orderCode, "参数空",openid);
             }

            order_cs = JSON.parseObject(jsonSting, PgenerateOrder.class);
            /*********生产订单：开始***********/
            double total=order_cs.getTotal();//合计90券金额
            //判断参数正确性
            String pd1=pd_parameter(order_cs);
            if(!pd1.equals("true")){
                return returnMap_order(result, orderCode, pd1,openid);
            }

            //店铺信息
            Shop shop =(Shop)dao.findForObject("OrderMain90Dao.getShopBySid",order_cs.getShop_code());
            if(shop == null ){
                return returnMap_order(result, orderCode, "门店不存在",openid);
            }

            //付款码查询用户信息
            List<BaseUser> user_list = baseUserService.queryBaseUserByOnlyCode(order_cs.getOnly_code());
            BaseUser userObjct=new BaseUser();
            if (user_list.size()==0)
            {
                return returnMap_order(result, orderCode, "找不到该付款码，无法支付",openid);
            }
            if (user_list.size()==1)
            {
                userObjct=user_list.get(0);
                openid=userObjct.getOpen_id();
            }else{
                return returnMap_order(result, orderCode, "错误-付款码已失效，请刷新后再试",openid);
            }


            //拼接订单参数
            String order_id=UuidUtil.get32UUID();//订单号
			String order_code=String.valueOf(getRndNumCode());//订单号
			String index=order_cs.getOnly_code().substring(0,2);//判断是90券-开头（90）
			int balance_type=0;
			if(index.equals("90")){
				balance_type=0;//久零券
			}else if(index.equals("91")){
				balance_type=2;//体验券
			}else if(index.equals("92")){
                //另购券
                balance_type=1;
            }else if(index.equals("93")){
				//混合支付
				balance_type=3;
			}

			int double_cash_total=CashMoney(total,balance_type,userObjct);//服务费金额(即90券 比例算出来的钱)  //转分
            OrderMain90 orderMain90=new OrderMain90();
            orderMain90.setCode(order_code);
			orderMain90.setId(order_id);
            orderMain90.setCash_total(double_cash_total*100);   //转分
            orderMain90.setTickets_total(total*100);		//转分
            orderMain90.setOpen_id(userObjct.getOpen_id());
            orderMain90.setUser_code(order_cs.getUser_code());//营业员 ,登陆者code
            orderMain90.setShop_code(order_cs.getShop_code());
            orderMain90.setBrand_code(shop.getBrand_code());
            orderMain90.setTrade_type("");
            orderMain90.setState(0);
            orderMain90.setBalance_type(balance_type);
            if(balance_type==0){
				orderMain90.setBalance_90(total);
				orderMain90.setBalance_experience_90(0);
				orderMain90.setBalance_shopping_90(0);
				orderMain90.setIsPayExperience90(1);
				orderMain90.setIsPayShopping90(1);
				orderMain90.setIsPay90(0);
			}else if(balance_type==1){
				orderMain90.setBalance_shopping_90(total);
				orderMain90.setBalance_90(0);
				orderMain90.setBalance_experience_90(0);
				orderMain90.setIsPayExperience90(1);
				orderMain90.setIsPayShopping90(0);
				orderMain90.setIsPay90(1);
			}else if(balance_type==2){
				orderMain90.setBalance_experience_90(total);
				orderMain90.setBalance_shopping_90(0);
				orderMain90.setBalance_90(0);
				orderMain90.setIsPayExperience90(0);
				orderMain90.setIsPayShopping90(1);
				orderMain90.setIsPay90(1);
			}else if(balance_type==3){
				if(userObjct.getBalance_shopping_90()/100>=total){
					orderMain90.setBalance_shopping_90(total);
					orderMain90.setBalance_90(0);
					orderMain90.setBalance_experience_90(0);
					orderMain90.setIsPayExperience90(0);
					orderMain90.setIsPayShopping90(1);
					orderMain90.setIsPay90(1);
				}else{
					orderMain90.setBalance_shopping_90(userObjct.getBalance_shopping_90()/100);
					orderMain90.setBalance_90(total-userObjct.getBalance_shopping_90()/100);
					orderMain90.setBalance_experience_90(0);
					orderMain90.setIsPayExperience90(1);
					orderMain90.setIsPayShopping90(0);
					orderMain90.setIsPay90(0);
				}
			}
            //保存
            Integer i=(Integer) dao.save("OrderMain90Dao.insertOrderMain90", orderMain90);
            if(i>0){
                orderCode=order_code;//成功
                result="success";
            }
            /*********生产订单：结束***********/
            return returnMap_order(result, orderCode,msg,openid);
        } catch (Exception e) {
            msg= e.toString();
            return returnMap_order(result, orderCode,msg,openid);
        }
    }


    private Map<String, Object> returnMap_order(String result,String orderCode,String msg,String openid){
        Map<String, Object> r_map=new HashMap<>();
        r_map.put("result",result);
        r_map.put("orderCode",orderCode);
        r_map.put("openid",openid);
        r_map.put("msg",msg);
        return r_map;
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

    //订单生成，参数验证
    private String pd_parameter(PgenerateOrder cs){
        double total=cs.getTotal();
        String shop_code=cs.getShop_code();
        String only_code=cs.getOnly_code();
        String user_code=cs.getUser_code();
        String msg="";
        if(total<=0){
            msg="金额必须大于零";
        }else if(weishu(total)>2) {
            msg="金额小数不能超过2位";
        }else if(StringUtil.isNullOrEmpty(shop_code)){
            msg="店铺参数空";
        }else if(StringUtil.isNullOrEmpty(only_code)){
            msg="付款码空";
        }else if(StringUtil.isNullOrEmpty(user_code)){
            msg="营业员空";
        }else{
            msg="true";
        }
        return msg;
    }

    //判断小数几位
    private int weishu(double numbers){
        String s =String.valueOf(numbers);
        //int position = s.length() - s.indexOf(".") + 1;
        int position=s.length()-s.indexOf(".")-1;
        return position;
    }

    /********************************结束：直接付款订单****************************************/


    /**
     * 付款码验证
     * @param shop_code
     * @param only_code
     * @param total
     * @param iscoin 是否使用9贝抵扣(0：使用 1：不使用)
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> Verification_Payment_Code(String shop_code,String only_code,String total,String iscoin) throws Exception {
        String result="0";//返回码1 ：成功 0：失败
        String msg="";
        double total_double=0.0;//应该付款的总券额
        double return_info=0.0;//服务费金额
        boolean ninezero=false;//90贝余额是否足够支付服务费 :true 足够,false :不够
        double ninecoinMoney=0.0;//90贝余额应付多少
        double needMoney=0.0;//还需支付多少钱
        boolean iscoinbool=Boolean.parseBoolean(iscoin);//true: 抵用， false：不抵用
        String user_openid="";//当前用户剩余的90券(元)
        double user_balance=0.0;//当前用户剩余的90券(元)
        double user_shopping_balance=0.0;//当前用户剩余的购物券(元)
		double user_experience_balance=0.0;//当前用户剩余的体验券
        double user_coin=0.0;//当前用户剩余的90贝(元)
        try{
            //参数格式验证
            String pd1=Payment_Code(shop_code, only_code, total);
            if(!pd1.equals("true")){
                return returnMap_Verification(result,pd1,return_info,ninezero,needMoney,iscoinbool,ninecoinMoney,user_openid);
            }



            //付款码是否正确验证,查询用户
            List<BaseUser> user_list = baseUserService.queryBaseUserByOnlyCode(only_code);
            BaseUser userObjct=new BaseUser();
            if (user_list.size()==0)
            {
                return returnMap_Verification(result,"找不到该付款码，无法支付",return_info,ninezero,needMoney,iscoinbool,ninecoinMoney,user_openid);
            }
            if (user_list.size()==1)
            {
                userObjct=user_list.get(0);
                user_openid=userObjct.getOpen_id();
            }else{
                return returnMap_Verification(result,"错误-付款码已失效，请刷新后再试",return_info,ninezero,needMoney,iscoinbool,ninecoinMoney,user_openid);
            }

			total_double=Double.parseDouble(total);
			if(only_code.startsWith("90")){
				return_info = CashMoney(total_double,0,null);
			} else if (only_code.startsWith("91")) {
				return_info = CashMoney(total_double,2,null);
			}else if(only_code.startsWith("92")){
				return_info = CashMoney(total_double,1,null);
			}else if(only_code.startsWith("93")){
				return_info = CashMoney(total_double,3,userObjct);
			}

            //90贝抵押计算
            if(iscoinbool){
                //90贝是否足够
                user_coin=userObjct.getChargeAmount()+userObjct.getGiveAmount()+userObjct.getExtraAmount();
                needMoney=user_coin-return_info;
                if(needMoney>0){
                    //90币足够
                    ninezero=true;
                    needMoney=0;
                    ninecoinMoney=return_info;
                }else{
                    //90币不够
                    ninezero=false;
                    needMoney=-needMoney;
                    ninecoinMoney=user_coin;
                }
            }else{
                needMoney=return_info;
            }

            //店铺验证(订单有验证，暂时不写)
            //90券是否足够
			if(only_code.startsWith("90")){
				user_balance=userObjct.getBalance_90()/100;
				if(user_balance < total_double){
					return returnMap_Verification(result,"用户久零券余额不足，目前余额为："+user_balance+"元",return_info,ninezero,needMoney,iscoinbool,ninecoinMoney,user_openid);
				}else{
					//90券足够，可以支付
					result="1";
				}
			} else if (only_code.startsWith("92")) {
				//临购券
				user_shopping_balance=userObjct.getBalance_shopping_90()/100;
				if(user_shopping_balance < total_double){
					return returnMap_Verification(result,"用户零购券余额不足，目前余额为："+user_shopping_balance+"元",return_info,ninezero,needMoney,iscoinbool,ninecoinMoney,user_openid);
				}else{
					//购物券足够，可以支付
					result="1";
				}
			}else if (only_code.startsWith("91")) {
				//体验券
				user_experience_balance=userObjct.getBalance_experience_90()/100;
				if(user_experience_balance < total_double){
					return returnMap_Verification(result,"用户体验券余额不足，目前余额为："+user_experience_balance+"元",return_info,ninezero,needMoney,iscoinbool,ninecoinMoney,user_openid);
				}else{
					//体验券券足够，可以支付
					result="1";
				}
			}else if (only_code.startsWith("93")) {
				//混合支付
				//零购券
				user_shopping_balance=userObjct.getBalance_shopping_90()/100;
				//久零券
				user_balance=userObjct.getBalance_90()/100;

				if(user_shopping_balance+user_balance < total_double){
					return returnMap_Verification(result,"用户久零券余额不足，目前余额为："+user_balance+"元",return_info,ninezero,needMoney,iscoinbool,ninecoinMoney,user_openid);
				}else{
					//体验券券足够，可以支付
					result="1";
				}
			}
			return returnMap_Verification(result,msg,return_info,ninezero,needMoney,iscoinbool,ninecoinMoney,user_openid);
        } catch (Exception e) {
            msg= e.toString();
            return returnMap_Verification(result,msg,return_info,ninezero,needMoney,iscoinbool,ninecoinMoney,user_openid);
        }
    }

    private Map<String, Object> returnMap_Verification(String result,String msg,double return_info,boolean ninezero,double needMoney,boolean iscoinbool,double ninecoinMoney,String user_openid){
        Map<String, Object> r_map=new HashMap<>();
        r_map.put("return_code",result);//返回码1 ：成功 0：失败
        r_map.put("return_info",return_info);//服务费金额
        r_map.put("ninezero",ninezero);//90贝余额是否足够支付服务费 :true 足够,false :不够
        r_map.put("ninecoinMoney",ninecoinMoney);//90贝应该支付的金额
        r_map.put("needMoney",needMoney);//还需支付多少钱
        r_map.put("iscoinbool",iscoinbool);//true: 抵用， false：不抵用
        r_map.put("openid",user_openid);//用户openid
        r_map.put("msg",msg);
        return r_map;
    }


    /**
     * 短信付款验证
     * @param shop_code
     * @param phone
     * @param total
     * @param iscoin
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> Verification_Payment_Phone(String shop_code,String phone,String total,boolean iscoin) throws Exception {
        String result="0";//返回码1 ：成功 0：失败
        String msg="";
        double total_double=0.0;//应该付款的总券额
        double return_info=0.0;//服务费金额
        boolean ninezero=false;//90贝余额是否足够支付服务费 :true 足够,false :不够
        double ninecoinMoney=0.0;//90贝余额应付多少
        double needMoney=0.0;//还需支付多少钱
        boolean iscoinbool=iscoin;//true: 抵用， false：不抵用
        String user_openid="";//当前用户剩余的90券(元)
        double user_balance=0.0;//当前用户剩余的90券(元)
        double user_coin=0.0;//当前用户剩余的90贝(元)
        String phone_code="";//短信验证码
        try{
            //参数格式验证
            String pd1=Payment_phone(shop_code, phone, total);
            if(!pd1.equals("true")){
                return returnMap_Verification_phone(result, pd1, return_info, ninezero, needMoney, iscoinbool, ninecoinMoney, user_openid, phone_code);
            }

            total_double=Double.parseDouble(total);
            return_info=CashMoney(total_double,0,null);

            //付款码是否正确验证,查询用户
            List<BaseUser> user_list = baseUserService.queryBaseUserByPhone(phone);
            BaseUser userObjct=new BaseUser();
            if (user_list.size()==0)
            {
                return returnMap_Verification_phone(result, "找不到该用户，无法支付", return_info, ninezero, needMoney, iscoinbool, ninecoinMoney, user_openid, phone_code);
            }
            if (user_list.size()==1)
            {
                userObjct=user_list.get(0);
                user_openid=userObjct.getOpen_id();
            }else{
                return returnMap_Verification_phone(result, "错误-用户信息出错，请联系管理员", return_info, ninezero, needMoney, iscoinbool, ninecoinMoney, user_openid, phone_code);
            }

            //90贝抵押计算
            if(iscoinbool){
                //90贝是否足够
                user_coin=userObjct.getChargeAmount()+userObjct.getGiveAmount()+userObjct.getExtraAmount();
                needMoney=user_coin-return_info;
                if(needMoney>0){
                    //90币足够
                    ninezero=true;
                    needMoney=0;
                    ninecoinMoney=return_info;
                }else{
                    //90币不够
                    ninezero=false;
                    needMoney=-needMoney;
                    ninecoinMoney=user_coin;
                }
            }else{
                needMoney=return_info;
            }

            //店铺验证(订单有验证，暂时不写)
            //90券是否足够
            user_balance=userObjct.getBalance_90()/100;
            if(user_balance < total_double){
                return returnMap_Verification_phone(result, "用户久零券余额不足，目前余额为：" + user_balance + "元", return_info, ninezero, needMoney, iscoinbool, ninecoinMoney, user_openid, phone_code);
            }

            /* 生成6位随机数 */
            phone_code =String.valueOf(Tools.getRandomNum()) ;
            SmsTencent.sendMsg(phone, phone_code);
            result="1";

            return returnMap_Verification_phone(result, msg, return_info, ninezero, needMoney, iscoinbool, ninecoinMoney, user_openid, phone_code);
        } catch (Exception e) {
            msg= e.toString();
            return returnMap_Verification_phone(result, msg, return_info, ninezero, needMoney, iscoinbool, ninecoinMoney, user_openid, phone_code);
        }
    }

    private Map<String, Object> returnMap_Verification_phone(String result,String msg,double return_info,boolean ninezero,double needMoney,boolean iscoinbool,double ninecoinMoney,String user_openid,String phone_code){
        Map<String, Object> r_map=new HashMap<>();
        r_map.put("return_code",result);//返回码1 ：成功 0：失败
        r_map.put("return_info",return_info);//服务费金额
        r_map.put("ninezero",ninezero);//90贝余额是否足够支付服务费 :true 足够,false :不够
        r_map.put("ninecoinMoney",ninecoinMoney);//90贝应该支付的金额
        r_map.put("needMoney",needMoney);//还需支付多少钱
        r_map.put("iscoinbool",iscoinbool);//true: 抵用， false：不抵用
        r_map.put("openid",user_openid);//用户openid
        r_map.put("phone_code",phone_code);//短信验证码
        r_map.put("msg",msg);
        return r_map;
    }


    //付款码，参数验证
    private String Payment_Code(String shop_code,String only_code,String total){

        String msg="";
        if(!isnumber(total)){
            msg="金额必须为数字";
        }else{
            double totaldouble=Double.parseDouble(total);

            //付款码超时验证
            Long curr_time = new Date().getTime();
            Long timestamp = Long.valueOf(only_code.substring(6,19));

            if(weishu(totaldouble)>2){
                msg="金额小数不能超过2位";
            }else if(StringUtil.isNullOrEmpty(shop_code)){
                msg="店铺参数空";
            }
            else if(StringUtil.isNullOrEmpty(only_code)){
                msg="付款码空";
            }else if(only_code.length()!=20) {
                msg="体验店，用户验证错误，长度不对，非法付款码！";
            }
            /*else if((curr_time - timestamp)/1000>=65){
                msg="错误-付款码已超时，请刷新后再试";
            }*/
            else{
                msg="true";
            }

        }
        return msg;
    }

    //手机号码，参数验证
    private String Payment_phone(String shop_code,String phone,String total){

        String msg="";
        if(!isnumber(total)){
            msg="金额必须为数字";
        }else{
            double totaldouble=Double.parseDouble(total);

            if(weishu(totaldouble)>2){
                msg="金额小数不能超过2位";
            }else if(StringUtil.isNullOrEmpty(shop_code)){
                msg="店铺参数空";
            }
            else if(StringUtil.isNullOrEmpty(phone)){
                msg="手机号不能为空";
            }else if(phone.length()!=11) {
                msg="手机号位数不正确";
            }
            else{
                msg="true";
            }

        }
        return msg;
    }



    /**
     * 90直接付款订单——付款方法
     * @param jsonSting
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> PayOrderMain90(String jsonSting) throws Exception {

        String result="0";//0:生成失败,1 :成功
        double pay_money=0.0;//实际支付的服务费费用
        double order_main_pay_money=0.0;//已经付款的金额
        double order_main_pay_balance90=0.0;//已经付款的90券
        double user_iscoin=0.0;//当前用户剩余90贝
        double ew_paycoin=0.0;//任然需要支付额外金额
        String trade_type = "";//付款方式 判断1为微信（MICROPAY），2为支付宝（ZFB-MICROPAY），3：现金付款(offline),4银联支付(UNIONPAY)
        try {
            String orderCode="";//订单code 即 id
            String openid="";//付款人openid
            double order_total=0.0;//用90币支付金额
            double iscoin_total=0.0;//额外支付金额
			//String ticketType="0";
            PatOrderMain90Parameter PayParameter=new PatOrderMain90Parameter();
            if (jsonSting.equals(null) || (jsonSting.length()==0)) {
                return returnMap_PayOrderMain90(result,"参数空",0.0,0.0,"","");
            }
            PayParameter = JSON.parseObject(jsonSting, PatOrderMain90Parameter.class);
            //参数格式验证
            String pd1=PayOrderMain90Parameter(PayParameter);
            if(!pd1.equals("true")){
                return returnMap_PayOrderMain90(result,pd1, 0.0, 0.0,"","");
            }
            //赋值参数
            orderCode=PayParameter.getOrderCode();
			/*ticketType=PayParameter.getBalance_type();
			if(StringUtil.isNullOrEmpty(ticketType)||!StringUtil.isNumeric(ticketType))
			{ticketType="0";}*/
            openid=PayParameter.getOpenid();
            order_total=PayParameter.getOrder_total();
            iscoin_total=PayParameter.getIscoin_total();
            pay_money=iscoin_total+order_total;
			String device_ip = PayParameter.getDevice_ip();
			String device_info = PayParameter.getDevice_info();

			if(PayParameter.getAuthor_code().length() == 1 )
			{
				if("1".equals(PayParameter.getAuthor_code()) || "2".equals(PayParameter.getAuthor_code()))
				{
					return returnMap_PayOrderMain90(result,"无效的付款方式",user_iscoin,ew_paycoin,"",trade_type);
				}
			}


            //判断1为微信，2为支付宝
            if(PayParameter.getAuthor_code().startsWith("1")){ //微信
                trade_type = "MICROPAY";
            }else if(PayParameter.getAuthor_code().startsWith("2")){ //支付宝
                trade_type = "ZFB-MICROPAY";
            }else if(PayParameter.getAuthor_code().startsWith("3")){ //现金付款
                trade_type = "offline";
            }else if(PayParameter.getAuthor_code().startsWith("4")){ //银联支付
                trade_type = "UNIONPAY";
            }

            double user_balance90=0.0;//当前用户剩余90券
			double user_balance_shopping_90=0.0;//用户剩余另购券
			double user_balance_experience_90=0.0;//用户体验券
            //订单状态是信息
            Map<String ,Object> selectmap=new HashMap<>();
            selectmap.put("orderCode",orderCode);
            OrderMain90 orderMain90=(OrderMain90)dao.findForObject("OrderMain90Dao.getOrderMain90",selectmap);
            if(orderMain90==null){
                return returnMap_PayOrderMain90(result,"订单不存在",user_iscoin,ew_paycoin,"",trade_type);
            }
            //验证付款用户是否和订单用户一致
            if(!orderMain90.getOpen_id().equalsIgnoreCase(openid)){
                return returnMap_PayOrderMain90(result,"付款用户和订单用户不一致",user_iscoin,ew_paycoin,"",trade_type);
            }

            order_main_pay_money=orderMain90.getOrder_total()/100+orderMain90.getPay_coin()/100; //转元
            order_main_pay_balance90=orderMain90.getPay_90()/100;//转元
            //+额外支付金额90币支付金额+已经付款金额 =是否正好满足服务费
            if(orderMain90.getCash_total()/100 !=(pay_money+order_main_pay_money)){
                return returnMap_PayOrderMain90(result,"付款金额不足，请重新尝试",user_iscoin,ew_paycoin,"",trade_type);
            }

            //获取用户信息
            BaseUser baseUser=new BaseUser();
            List<BaseUser> baseUser_List=(List<BaseUser>) dao.findForList("OrderMain90Dao.queryBaseUserByOpenid", openid);
            if (baseUser_List==null){
                return returnMap_PayOrderMain90(result, "未找到用户信息，请重新尝试",user_iscoin,ew_paycoin,"",trade_type);
            }
            if (baseUser_List.size()==1){
                baseUser=baseUser_List.get(0);
            }else{
                return returnMap_PayOrderMain90(result, "用户信息数据有误,请联系管理员",user_iscoin,ew_paycoin,"",trade_type);
            }
            //验证90券是否足够 (剩余需要支付的90券是否大于用户剩余金额)
            user_balance90=baseUser.getBalance_90()/100;
            if((orderMain90.getBalance_90()-user_balance90>0)&&(orderMain90.getIsPay90()==0)){
                return returnMap_PayOrderMain90(result, "用户90券额不够,当前剩余"+user_balance90+"元",user_iscoin,ew_paycoin,"",trade_type);
            }
            //验证零购券
			user_balance_shopping_90=baseUser.getBalance_shopping_90()/100;
			if((orderMain90.getBalance_shopping_90()-user_balance_shopping_90>0)&&(orderMain90.getIsPayShopping90()==0)){
				return returnMap_PayOrderMain90(result, "用户零购券额不够,当前剩余"+user_balance_shopping_90+"元",user_iscoin,ew_paycoin,"",trade_type);
			}
			//验证体验券
			user_balance_experience_90= baseUser.getBalance_experience_90()/100;
			if((orderMain90.getBalance_experience_90()-user_balance_experience_90>0)&&(orderMain90.getIsPayExperience90()==0)){
				return returnMap_PayOrderMain90(result, "用户体验券额不够,当前剩余"+user_balance_experience_90+"元",user_iscoin,ew_paycoin,"",trade_type);
			}

            //验证90贝是否足够
            user_iscoin=baseUser.getChargeAmount()+ baseUser.getGiveAmount()+baseUser.getExtraAmount();
            if(order_total>0 && (order_total>user_iscoin)){
                ew_paycoin=orderMain90.getCash_total()/100-order_total;
                if(ew_paycoin<0){
                    return returnMap_PayOrderMain90(result, "错误-请重新尝试",0,0,"",trade_type);
                }else{
                    return returnMap_PayOrderMain90(result, "用户90贝额不够",user_iscoin,ew_paycoin,"",trade_type);
                }
            }

			//改变订单状态，保存支付金额
			OrderMain90 updateOrderMain90=new OrderMain90();

            //调用扣90券接口
            if((orderMain90.getIsPay90()==0)&&(orderMain90.getBalance_90()>0)){
                JSONObject jSONObject_90 = new JSONObject();
                jSONObject_90.put("brand_code",orderMain90.getBrand_code());//商户id，没有留空字符串
                jSONObject_90.put("shop_code",orderMain90.getShop_code());//店铺id，没有留空字符串
                jSONObject_90.put("user_code","");//营业员id，没有留空字符串
                jSONObject_90.put("order_code",orderCode);//订单号/流水号/实体卡卡号
                jSONObject_90.put("open_id",openid);//用户openid
				jSONObject_90.put("ticketType","0");//发券类型
                jSONObject_90.put("type","1");//发券终端 0:pos机 1:qt 2:微商城 3:远程发券 4:实体卡 5:旧数据导入
                jSONObject_90.put("source","3");//类型0:充值；1:自动发券；2人工发券；3消券;4 久零券充值卡 5 用户退款 6首次关注加积分 7签到增加
                jSONObject_90.put("source_msg","QT直接付款扣久零券");//备注
//				double point90=(orderMain90.getTickets_total()/100-order_main_pay_balance90)*100;
                jSONObject_90.put("balance_90",Math.round(orderMain90.getBalance_90()*100));//int 久零券额，单位分
                jSONObject_90.put("state",1);//int 1为收入券2为支出券(以商户视角，就是用户收入就是2，用户支出就是1)
                jSONObject_90.put("commission",0);//自动发券久零佣金百分点，没有写0
                jSONObject_90.put("tradeType",trade_type);//自动发券支付类型 RECHARGE 储值卡主扫 MICROPAY 微信被扫 NATIVE 微信主扫 JSAPI 微信主扫 OFFLINE 现金支付 ZFB-MICROPAY 支付宝被扫 ZFB-NATIVE 支付宝主扫 ZFB-JSAPI 支付宝主扫 YZF-MICROPAY 翼支付被扫 UNIONPAY 银行卡支付 ROB 非店小翼平台支付
                jSONObject_90.put("orderState","1");//自动发券订单状态 0:未生效1:交易成功2:错误3:已退款 ，没有写1或者空字符串
                jSONObject_90.put("orderTotal","0");//自动发券订单金额，单位分，没有写0
                Map<String, String> hashMap_quan = new HashMap<String, String>();
                hashMap_quan.put("json", jSONObject_90.toString());
                Map<String, Object> r_90quan_map= this.QTInterface(hashMap_quan, Balance_90_url, "QT_Url");
                if(!r_90quan_map.get("flag").equals("0") && !r_90quan_map.get("flag").equals("3")){
                    return returnMap_PayOrderMain90(result,r_90quan_map.get("msg").toString() ,0,0,"",trade_type);
                }
            }
			updateOrderMain90.setIsPay90(1);
            //调用零购券
			if((orderMain90.getIsPayShopping90()==0)&&(orderMain90.getBalance_shopping_90()>0)){
				JSONObject jSONObject_90 = new JSONObject();
				jSONObject_90.put("brand_code",orderMain90.getBrand_code());//商户id，没有留空字符串
				jSONObject_90.put("shop_code",orderMain90.getShop_code());//店铺id，没有留空字符串
				jSONObject_90.put("user_code","");//营业员id，没有留空字符串
				jSONObject_90.put("order_code",orderCode);//订单号/流水号/实体卡卡号
				jSONObject_90.put("open_id",openid);//用户openid
				jSONObject_90.put("ticketType","1");//发券类型
				jSONObject_90.put("type","1");//发券终端 0:pos机 1:qt 2:微商城 3:远程发券 4:实体卡 5:旧数据导入
				jSONObject_90.put("source","3");//类型0:充值；1:自动发券；2人工发券；3消券;4 久零券充值卡 5 用户退款 6首次关注加积分 7签到增加
				jSONObject_90.put("source_msg","QT直接付款扣零购券");//备注
//				double point90=(orderMain90.getTickets_total()/100-order_main_pay_balance90)*100;
				jSONObject_90.put("balance_90",Math.round(orderMain90.getBalance_shopping_90()*100));//int 久零券额，单位分
				jSONObject_90.put("state",1);//int 1为收入券2为支出券(以商户视角，就是用户收入就是2，用户支出就是1)
				jSONObject_90.put("commission",0);//自动发券久零佣金百分点，没有写0
				jSONObject_90.put("tradeType",trade_type);//自动发券支付类型 RECHARGE 储值卡主扫 MICROPAY 微信被扫 NATIVE 微信主扫 JSAPI 微信主扫 OFFLINE 现金支付 ZFB-MICROPAY 支付宝被扫 ZFB-NATIVE 支付宝主扫 ZFB-JSAPI 支付宝主扫 YZF-MICROPAY 翼支付被扫 UNIONPAY 银行卡支付 ROB 非店小翼平台支付
				jSONObject_90.put("orderState","1");//自动发券订单状态 0:未生效1:交易成功2:错误3:已退款 ，没有写1或者空字符串
				jSONObject_90.put("orderTotal","0");//自动发券订单金额，单位分，没有写0
				Map<String, String> hashMap_quan = new HashMap<String, String>();
				hashMap_quan.put("json", jSONObject_90.toString());
				Map<String, Object> r_90quan_map= this.QTInterface(hashMap_quan, Balance_90_url, "QT_Url");
				if(!r_90quan_map.get("flag").equals("0") && !r_90quan_map.get("flag").equals("3")){
					return returnMap_PayOrderMain90(result,r_90quan_map.get("msg").toString() ,0,0,"",trade_type);
				}
			}
			updateOrderMain90.setIsPayShopping90(1);
			//调用体验券
			if((orderMain90.getIsPayExperience90()==0)&&(orderMain90.getBalance_experience_90()>0)){
				JSONObject jSONObject_90 = new JSONObject();
				jSONObject_90.put("brand_code",orderMain90.getBrand_code());//商户id，没有留空字符串
				jSONObject_90.put("shop_code",orderMain90.getShop_code());//店铺id，没有留空字符串
				jSONObject_90.put("user_code","");//营业员id，没有留空字符串
				jSONObject_90.put("order_code",orderCode);//订单号/流水号/实体卡卡号
				jSONObject_90.put("open_id",openid);//用户openid
				jSONObject_90.put("ticketType","2");//发券类型
				jSONObject_90.put("type","1");//发券终端 0:pos机 1:qt 2:微商城 3:远程发券 4:实体卡 5:旧数据导入
				jSONObject_90.put("source","3");//类型0:充值；1:自动发券；2人工发券；3消券;4 久零券充值卡 5 用户退款 6首次关注加积分 7签到增加
				jSONObject_90.put("source_msg","QT直接付款扣体验券");//备注
//				double point90=(orderMain90.getTickets_total()/100-order_main_pay_balance90)*100;
				jSONObject_90.put("balance_90",Math.round(orderMain90.getBalance_experience_90()*100));//int 久零券额，单位分
				jSONObject_90.put("state",1);//int 1为收入券2为支出券(以商户视角，就是用户收入就是2，用户支出就是1)
				jSONObject_90.put("commission",0);//自动发券久零佣金百分点，没有写0
				jSONObject_90.put("tradeType",trade_type);//自动发券支付类型 RECHARGE 储值卡主扫 MICROPAY 微信被扫 NATIVE 微信主扫 JSAPI 微信主扫 OFFLINE 现金支付 ZFB-MICROPAY 支付宝被扫 ZFB-NATIVE 支付宝主扫 ZFB-JSAPI 支付宝主扫 YZF-MICROPAY 翼支付被扫 UNIONPAY 银行卡支付 ROB 非店小翼平台支付
				jSONObject_90.put("orderState","1");//自动发券订单状态 0:未生效1:交易成功2:错误3:已退款 ，没有写1或者空字符串
				jSONObject_90.put("orderTotal","0");//自动发券订单金额，单位分，没有写0
				Map<String, String> hashMap_quan = new HashMap<String, String>();
				hashMap_quan.put("json", jSONObject_90.toString());
				Map<String, Object> r_90quan_map= this.QTInterface(hashMap_quan, Balance_90_url, "QT_Url");
				if(!r_90quan_map.get("flag").equals("0") && !r_90quan_map.get("flag").equals("3")){
					return returnMap_PayOrderMain90(result,r_90quan_map.get("msg").toString() ,0,0,"",trade_type);
				}
			}
			updateOrderMain90.setBalance_experience_90(1);
			//保存数据1
			updateOrderMain90.setCode(orderCode);
			updateOrderMain90.setState(0);
			updateOrderMain90.setPay_90((orderMain90.getTickets_total()/100-order_main_pay_balance90)*100); //分转成元   在转成分保存

            //调用扣90贝接口
            if(order_total>0){
                JSONObject jSONObject_coin = new JSONObject();
                jSONObject_coin.put("userId",baseUser.getId());//用户id，base_user表的id
                jSONObject_coin.put("state",2);//1：收入，2：支出，0：未知
                jSONObject_coin.put("orderNum",orderCode);//支出：订单号/提现流水号等，收入：充值流水号/充值卡卡号等
                jSONObject_coin.put("source",2);//来源/用途：1：充值，2：购买商品，3：发放久零券赠送 4:退款 5提现 6提现失败回退久零贝 7手动撤销发券退回赠送的久零贝
                jSONObject_coin.put("amount",order_total);//金额数量，单位元
                Map<String, String> hashMap_coin = new HashMap<String, String>();
                hashMap_coin.put("json", jSONObject_coin.toString());
                Map<String, Object> r_coin_map= this.QTInterface(hashMap_coin, Coin_90_url, "QT_Url");
                if(!r_coin_map.get("flag").equals("0") && !r_coin_map.get("flag").equals("3")){
                    return returnMap_PayOrderMain90(result,r_coin_map.get("msg").toString(),0,0,"",trade_type);
                }
            }
			//保存数据2
			updateOrderMain90.setPay_coin(order_total*100);//iscoin_total 90贝抵用金额 元转分
			updateOrderMain90.setTrade_type(trade_type);

            //店小翼支付接口B
            if((trade_type.equals("MICROPAY") || trade_type.equals("ZFB-MICROPAY")) && iscoin_total>0){
                //微信，支付宝支付，需要掉接口
                Pay17SelectPay bs_r= pay17Service.getPay17_bs(orderCode.trim(), (int) (iscoin_total * 100), PayParameter.getAuthor_code(), device_ip, orderMain90.getDxy_code().trim(), orderMain90.getDxy_person_code().trim());
                if(bs_r.getReturn_code()==0 || bs_r.getTrade_state()==0 || bs_r.getTrade_state()==2 || bs_r.getTrade_state()==5)
                {
                    dao.update("OrderMain90Dao.updateOrderMain90", updateOrderMain90);
                    if(bs_r.getTrade_state()==5){
                        return returnMap_PayOrderMain90(result,bs_r.getReturn_info(),0,0,"USERPAYING-"+bs_r.getReturn_info(),trade_type);
                    }else{
                        return returnMap_PayOrderMain90(result,bs_r.getReturn_info(),0,0,"",trade_type);
                    }
                }
            }
            updateOrderMain90.setState(1);
            updateOrderMain90.setOrder_total(iscoin_total*100);//额外支付费用  元转分
            Integer j=(Integer) dao.update("OrderMain90Dao.updateOrderMain90", updateOrderMain90);
            if(j>0){
                result="1";
            }

            try{
                //模板消息
                if(j>0){
                    wxTemplateService.send_kf_template(openid, "订单"+orderCode+"支付成功");
                }
            } catch (Exception e) {

            }

            return returnMap_PayOrderMain90(result, "",0,0,"",trade_type);
        } catch (Exception e) {
			e.printStackTrace();
			System.out.print(e.toString());
            return returnMap_PayOrderMain90(result,e.toString(),0.0,0.0,"",trade_type);
        }
    }





    //支付参数验证
    private String PayOrderMain90Parameter(PatOrderMain90Parameter yz){
        String msg="";
        if(StringUtil.isNullOrEmpty(yz.getOpenid())){
            msg="用户信息为空";
        }else if(StringUtil.isNullOrEmpty(yz.getOrderCode())){
            msg="订单号为空";
        }else{
            msg="true";
        }
        return msg;
    }

    private Map<String, Object> returnMap_PayOrderMain90(String result,String msg,double user_iscoin,double ew_paycoin, String return_info, String trade_type){
        Map<String, Object> r_map=new HashMap<>();
        r_map.put("return_code",result);//返回码1 ：成功 0：失败
        r_map.put("useriscoin",user_iscoin);//当前用户剩余90贝
        r_map.put("paycoin",ew_paycoin);//需要额外支付金额
        r_map.put("return_info",return_info);//需要额外支付金额
        r_map.put("msg",msg);
        r_map.put("trade_type",trade_type);
        return r_map;
    }


        //java判断数字类型（小数和整数）
    private boolean isnumber(String total){
        boolean pd=false;
        try {
            pd= total.matches("\\d+(\\.\\d+)?");
        }   catch (Exception e){

        }
        return pd;
    }


	@Override
	public Map<String,Object> doProductOrder(String json){
		Map<String,Object> mapRe = new HashMap<>();
		mapRe.put("result","fail");
		mapRe.put("msg", "生产订单失败！");

		try{
			//根据only_code 获取userId
            Map<String,Object> mapJson = JSON.parseObject(json,Map.class);

            if(mapJson == null)
            {
                mapRe.put("result","fail");
//                mapRe.put("orderCode",orderCode);
                mapRe.put("msg", "生产订单失败！");
                return mapRe;
            }

            String goodsArry = mapJson.get("goodsArry").toString();
            String only_code = mapJson.get("onlyCode").toString();
            String daogouYuan = mapJson.get("userCode").toString();
			String yinyeYuan = mapJson.get("cashUserCode").toString();
			String shopId = mapJson.get("shopId").toString();
			//付款码查询用户信息
			List<BaseUser> user_list = baseUserService.queryBaseUserByOnlyCode(only_code);
			BaseUser userObjct=new BaseUser();
			if (user_list.size()==0)
			{
				mapRe.put("result","fail");
				mapRe.put("msg", "找不到该付款码，无法支付！");
				return mapRe;
			}
			if (user_list.size()==1)
			{
				userObjct=user_list.get(0);
			}else{
				mapRe.put("result","fail");
				mapRe.put("msg", "错误-付款码已失效，请刷新后再试！");
				return mapRe;
			}


			List<PgoodsDetail> pgoodsDetailList = JSON.parseArray(goodsArry,PgoodsDetail.class);
			if(pgoodsDetailList != null && pgoodsDetailList.size() >0){
//				String only_code = pgoodsDetailList.get(0).getOnlyCode();
				String orderCode = String.valueOf(getRndNumCode());
                saveQtMsg(json,orderCode);//记录
				String userId = (String)dao.findForObject("PhoneUserDao.getUserIdByOnlyCode",only_code);
				TorderMain torderMain = new TorderMain();
				torderMainDao.save(torderMain);
				Double orderTotal = 0.00;
				List<TOrderDetail> orderDetailList = new ArrayList<TOrderDetail>();
				String whareId = (String)dao.findForObject("PhoneUserDao.getWhareIdByShopId",shopId);

				String index=only_code.substring(0,2);//判断是90券-开头（90）；购物券-开头（80）
				int balance_type=0;
				if(index.equals("90")){
					balance_type=0;//久零券
				}else if(index.equals("91")){
					balance_type=2;//体验券
				}else if(index.equals("92")){
					//另购券
					balance_type=1;
				}else if(index.equals("93")){
					//混合支付
					balance_type=3;
				}

				for(PgoodsDetail pgoodsDetail: pgoodsDetailList )
				{
					Double goodsTotal = MathUtil.mul(Double.valueOf(pgoodsDetail.getGoodsPrice()),Double.valueOf(pgoodsDetail.getGoodsCount()));
					TOrderDetail tOrderDetail = new TOrderDetail();
					tOrderDetail.setId(UuidUtil.get32UUID());
					tOrderDetail.setCount(Integer.valueOf(pgoodsDetail.getGoodsCount()));
					tOrderDetail.setIsdel((short)0);
					tOrderDetail.setPrice(Double.valueOf(pgoodsDetail.getGoodsPrice()));
					tOrderDetail.setGoodsTotal(goodsTotal);
					tOrderDetail.setPayTotal(Double.valueOf(CashMoney(goodsTotal,balance_type,userObjct)));
					tOrderDetail.setState((short)0);
					tOrderDetail.setCreateTime(new Date());
					tOrderDetail.setIsdel((short)0);
					tOrderDetail.setStockId(pgoodsDetail.getGoodsStockId());
					tOrderDetail.setGoodsId(pgoodsDetail.getGoodsId());
					tOrderDetail.setOrderId(torderMain.getId());
					tOrderDetail.setWhareHouseId(whareId);
					//放入集合
					orderDetailList.add(tOrderDetail);
					//计算总价
					orderTotal = MathUtil.add(orderTotal,goodsTotal);
					//开始插入数据循环插入
//					tOrderDetailDao.save(tOrderDetail);
				}
				String brandCode = (String)dao.findForObject("PhoneUserDao.getBrandCodeByShopId",shopId);
				torderMain.setGoodsTotal(orderTotal);
				torderMain.setSalesUserCode(daogouYuan);
				torderMain.setState(0);
				torderMain.setIsdel(0);
				torderMain.setBuyUserId(userId);//用户付款吗
				torderMain.setCode(orderCode);
                torderMain.setUserCode(yinyeYuan);
                torderMain.setSource(1);//线下
				torderMain.setBrandCode(brandCode);
				torderMain.setShopId(shopId);
				torderMain.setBalance_type(balance_type);
				torderMain.setServicePayTotal(Double.valueOf(CashMoney(orderTotal,balance_type,userObjct)));

				if(balance_type==0){
					//久零券
					torderMain.setBalance_90(orderTotal);
					torderMain.setBalance_experience_90(0.0);
					torderMain.setBalance_shopping_90(0.0);

				}else if(balance_type==1){
					//另购券
					torderMain.setBalance_90(0.0);
					torderMain.setBalance_experience_90(0.0);
					torderMain.setBalance_shopping_90(orderTotal);

				}else if(balance_type==2){
					//体验券
					torderMain.setBalance_90(0.0);
					torderMain.setBalance_experience_90(orderTotal);
					torderMain.setBalance_shopping_90(0.0);

				}else if(balance_type==3){
					//组合
					if(userObjct.getBalance_shopping_90()/100>=orderTotal){
						torderMain.setBalance_90(0.0);
						torderMain.setBalance_experience_90(0.0);
						torderMain.setBalance_shopping_90(orderTotal);
					}else{
						torderMain.setBalance_90(orderTotal-userObjct.getBalance_shopping_90()/100);
						torderMain.setBalance_experience_90(0.0);
						torderMain.setBalance_shopping_90(userObjct.getBalance_shopping_90()/100);
					}
				}
				torderMainDao.update(torderMain);

				/**
				 * 开始分单
				 */
				int hejihanghu=orderDetailList.size();
				double heji_90=0.0;//已经加了90券
				double heji_90_shop=0.0;//已经加了零购券
				double heji_90_experience=0.0;//已经加了体验券
				double heji_serPay=0;//已经加了服务费

				for(int i=0;i<hejihanghu;i++)
				{
					double detail_b=0;//子表，90贝
					double detail_j=0;//子表，金额
					double detail_90=0;
					double detail_90_shop=0;
					double detail_90_experience=0;
					double detail_serPay=0;
					if((i+1)==hejihanghu)
					{

						//90券
						detail_90=torderMain.getBalance_90()-heji_90;
						if(detail_90<0)
						{
							detail_90=0;
						}
						//零购券
						detail_90_shop=torderMain.getBalance_shopping_90()-heji_90_shop;
						if(detail_90_shop<0)
						{
							detail_90_shop=0;
						}
						//体验券
						detail_90_experience=torderMain.getBalance_experience_90()-heji_90_experience;
						if(detail_90_experience<0)
						{
							detail_90_experience=0;
						}
						//服务费
						detail_serPay=torderMain.getServicePayTotal()-heji_serPay;
						if(detail_serPay<0)
						{
							detail_serPay=0;
						}

					}else
					{

						//90券=比例*明细表金额
						BigDecimal bg4 = new BigDecimal(orderDetailList.get(i).getGoodsTotal()/torderMain.getGoodsTotal()*torderMain.getBalance_90());
						detail_90 = bg4.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						if(detail_90>torderMain.getBalance_90())
						{
							detail_90=torderMain.getBalance_90();
						}
						heji_90+=detail_90;

						//零购券=比例*明细表金额
						BigDecimal bg5 = new BigDecimal(orderDetailList.get(i).getGoodsTotal()/torderMain.getGoodsTotal()*torderMain.getBalance_shopping_90());
						detail_90_shop = bg5.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						if(detail_90_shop>torderMain.getBalance_shopping_90())
						{
							detail_90_shop=torderMain.getBalance_shopping_90();
						}
						heji_90_shop+=detail_90_shop;

						//体验券=比例*明细表金额
						BigDecimal bg6 = new BigDecimal(orderDetailList.get(i).getGoodsTotal()/torderMain.getGoodsTotal()*torderMain.getBalance_experience_90());
						detail_90_experience = bg6.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						if(detail_90_experience>torderMain.getBalance_experience_90())
						{
							detail_90_experience=torderMain.getBalance_experience_90();
						}
						heji_90_experience+=detail_90_experience;


						//服务费=比例*明细表金额
						BigDecimal bg7 = new BigDecimal(orderDetailList.get(i).getGoodsTotal()/torderMain.getGoodsTotal()*torderMain.getServicePayTotal());
						detail_serPay = bg7.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						if(detail_serPay>torderMain.getServicePayTotal())
						{
							detail_serPay=torderMain.getServicePayTotal();
						}
						heji_serPay+=detail_serPay;
					}

					//90券
					orderDetailList.get(i).setBalance_90(detail_90);
					orderDetailList.get(i).setIsPayCoupons((short) 0);
					//零购券
					orderDetailList.get(i).setBalance_shopping_90(detail_90_shop);
					orderDetailList.get(i).setIsPayShopping((short) 0);
					//体验券
					orderDetailList.get(i).setBalance_experience_90(detail_90_experience);
					orderDetailList.get(i).setIsPayExperience((short) 0);
					//服务费
					orderDetailList.get(i).setServicePayTotal(detail_serPay);
					//开始插入数据循环插入
					tOrderDetailDao.save(orderDetailList.get(i));
				}
				/**
				 * 结束分单
				 */



				mapRe.put("result","success");
				mapRe.put("orderCode",orderCode);
				mapRe.put("msg", "生成订单成功！");
			}else{
				mapRe.put("msg", "没有商品！");
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return mapRe;
	}

    //接口
    public Map<String, Object> QTInterface(Map<String,String> jSONObject,String url,String jdbc_url){
        Map<String, Object> map2=new HashMap<String, Object>();
        try {
            String requestUrl = ConfigUtil.get(jdbc_url)+url;
            String x= URLConectionUtil.httpURLConnectionPostDiy(requestUrl,jSONObject);
            x = URLDecoder.decode(x, "utf-8");
            if(x==null||x.trim().equals("") || x.trim().equals("失败")){
                // 失败
            }else {
                map2 = JSON.parseObject(x, Map.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return map2;
        }
    }




	/**
	 * 直接付款订单再次验证
	 * @param jsonSting
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> payOrderMainAgain(String jsonSting) throws Exception {

		String result="fail";//fail:生成失败,success :成功
		double pay_money=0.0;//实际支付的服务费费用
		double order_main_pay_money=0.0;//已经付款的金额
		double order_main_pay_balance90=0.0;//已经付款的90券
		double user_iscoin=0.0;//当前用户剩余90贝
		double ew_paycoin=0.0;//任然需要支付额外金额
		String trade_type = "";//付款方式 判断1为微信（MICROPAY），2为支付宝（ZFB-MICROPAY），3：现金付款(offline),4银联支付(UNIONPAY)
		try {
			String orderCode="";//订单code 即 id
			String openid="";//付款人openid
			double order_total=0.0;//用90币支付金额
			double iscoin_total=0.0;//额外支付金额

			PatOrderMain90Parameter PayParameter=new PatOrderMain90Parameter();
			if (jsonSting.equals(null) || (jsonSting.length()==0)) {
				return returnMap_PayOrderMain90(result,"参数空",0.0,0.0,"","");
			}
			PayParameter = JSON.parseObject(jsonSting, PatOrderMain90Parameter.class);
			//参数格式验证
			String pd1=PayOrderMain90Parameter(PayParameter);
			if(!pd1.equals("true")){
				return returnMap_PayOrderMain90(result,pd1, 0.0, 0.0,"","");
			}
			//赋值参数
			orderCode=PayParameter.getOrderCode();
			openid=PayParameter.getOpenid();
			order_total=PayParameter.getOrder_total();
			iscoin_total=PayParameter.getIscoin_total();
			pay_money=iscoin_total+order_total;

			//订单状态是信息
			Map<String ,Object> selectmap=new HashMap<>();
			selectmap.put("orderCode",orderCode);
			OrderMain90 orderMain90=(OrderMain90)dao.findForObject("OrderMain90Dao.getOrderMain90",selectmap);
			if(orderMain90==null){
				return returnMap_PayOrderMain90(result,"订单不存在",user_iscoin,ew_paycoin,"",trade_type);
			}

			order_main_pay_money=orderMain90.getOrder_total()/100+orderMain90.getPay_coin()/100;
			order_main_pay_balance90=orderMain90.getPay_90()/100;
			//验证90券是否足够 (剩余需要支付的90券是否大于用户剩余金额)（再次验证不需要）

			//验证90贝是否足够（再次验证不需要）

			//调用扣90券接口（再次验证不需要）

			//调用扣90贝接口（再次验证不需要）

			//微信，支付宝支付，需要掉接口
			/*String url = Global.getConfig("KQ_URL")+
					"phone_swiftPassQuery.do?order_code="+orderCode+
					"&user_code="+orderMain90.getDxy_person_code().trim();
			String back_result = Httpurl.HttpURL_link(url);
			if(StringUtil.isNullOrEmpty(back_result)){
				return returnMap_PayOrderMain90(result, "店小翼接口调用失败:",0,0,"",trade_type);
			}else{
				JSONObject json = JSONObject.fromObject(back_result);
				int return_code =json.getInt("return_code");//0 失败 1 成功
				if(return_code==0){
					return returnMap_PayOrderMain90(result, json.getString("error_pay_msg"),0,0,json.getString("return_info"),trade_type);
				}
			}*/
            Pay17SelectPay bs_r= pay17Service.getPay17_select(orderMain90.getDxy_code(), orderCode,null);
            if(bs_r.getReturn_code()==1 && bs_r.getTrade_state()==1){
                //改变订单状态，保存支付金额
                OrderMain90 updateOrderMain90=new OrderMain90();
                updateOrderMain90.setCode(orderCode);
                updateOrderMain90.setState(1);
                updateOrderMain90.setOrder_total(bs_r.getTotal());//额外支付费用
                Integer j=(Integer) dao.update("OrderMain90Dao.updateOrderMain90Again", updateOrderMain90);
                if(j>0){
                    result="success";

                    try{
                        //模板消息
                        wxTemplateService.send_kf_template(openid, "订单"+orderCode+"支付成功");

                    } catch (Exception e) {

                    }
                }
            }
            return returnMap_PayOrderMain90(result, "",0,0,"",trade_type);
		} catch (Exception e) {
			return returnMap_PayOrderMain90(result,e.toString(),0.0,0.0,"",trade_type);
		}
	}




	/**
	 * 商品收银再次验证
	 * @param jsonSting
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> payGoodsOrderAgain(String jsonSting) throws Exception {

		Map<String,Object> map = new HashMap<String, Object>();
		String trade_type = "";
		if (jsonSting.equals(null) || (jsonSting.length()==0)) {
			return returnMap_PayOrderMain90("0","参数空",0.0,0.0,"","");
		}
		PatOrderMain90Parameter PayParameter=new PatOrderMain90Parameter();
		PayParameter = JSON.parseObject(jsonSting, PatOrderMain90Parameter.class);

		String orderCode = PayParameter.getOrderCode();
		String orderMainId = (String)dao.findForObject("sysUserDao.getOrderMainIdByCode",orderCode);

		TorderMain torderMain = torderMainDao.getById(TorderMain.class,orderMainId);

		String shop_id = torderMain.getShopId();

		Shop shop = (Shop)dao.findForObject("sysUserDao.getShopBySid",shop_id);
		/*String url = Global.getConfig("KQ_URL")+
				"phone_swiftPassQuery.do?order_code="+orderCode+
				"&user_code="+shop.getDxy_code().trim();
		String back_result = Httpurl.HttpURL_link(url);
		if(StringUtil.isNullOrEmpty(back_result)){
			//再次验证失败
			return returnMap_PayOrderMain90("0", "店小翼接口调用失败:",0,0,"",trade_type);
		}else{
			JSONObject json = JSONObject.fromObject(back_result);
			int return_code =json.getInt("return_code");//0 失败 1 成功
			if(return_code==0){
				return returnMap_PayOrderMain90("0", json.getString("error_pay_msg"),0,0,json.getString("return_info"),trade_type);
			}
		}*/
		//调用接口修改库存
		List<PwxGoodsStockHistory> list=(List<PwxGoodsStockHistory>) dao.findForList("sysUserDao.getHistoryList",torderMain.getId());

        Pay17SelectPay bs_r= pay17Service.getPay17_select(shop.getDxy_code().trim(), orderCode,null);
        if(!(bs_r.getReturn_code()==1 && bs_r.getTrade_state()==1)){
            //失败
            return returnMap_PayOrderMain90("0", bs_r.getReturn_info(),0,0,bs_r.getReturn_info(),trade_type);
        }

		//付款成功处理订单
		torderMain.setState(1);
		torderMain.setPayTime(new Date());
		torderMainDao.update(torderMain);

		map.put("return_code","1");//支付成功
		map.put("return_msg","支付成功！");

        try{
            //模板消息
            //wxTemplateService.send_kf_template(openid, "订单"+orderCode+"支付成功");

        } catch (Exception e) {

        }
		return map;
	}
    public String getSysUserIdByOpenId(String openid)throws Exception
    {
        return (String) dao.findForObject("OrderMainDao.getSysUserIdByOpenId", openid);
    }
	@Override
	public Map<String,Object> cancleQTOrder(String jsonString) {
		PatOrderMain90Parameter PayParameter = JSON.parseObject(jsonString, PatOrderMain90Parameter.class);
        Map<String,Object> result = new HashMap<>();
		String orderCode = PayParameter.getOrderCode();
		String openid = PayParameter.getOpenid();
        int return_code=0;   //1:失败，0:成功
        String msg="";
		try {
			torderMain90Dao.executeHql("update TOrderMain90 t SET t.state=4 WHERE t.code='"+orderCode+"'");
			torderMainDao.executeHql("update TorderMain t SET t.state=4 WHERE t.code='"+orderCode+"'");

            TOrderMain90 torderMain90= torderMain90Dao.getByHql("from TOrderMain90 where code='"+orderCode+"'");
            TorderMain torderMain= torderMainDao.getByHql("from TorderMain where code='"+orderCode+"'");
            Integer pay90=0;     //需要退的券
            Double payCoin=0.0;   //需要退的贝
            String tSysUserId ="";
            String open_id="";
            String shop_code="";
            String brand_code="";
            String user_code="";
            String tradeType="";
            if(torderMain90!=null)
            {
                pay90 = torderMain90.getPay90();
                payCoin = torderMain90.getPayCoin().doubleValue();
                tSysUserId = getSysUserIdByOpenId(torderMain90.getOpenId());
                open_id=torderMain90.getOpenId();
                shop_code = torderMain90.getShopCode();
                brand_code = torderMain90.getBrandCode();
                user_code = torderMain90.getUserCode();
                tradeType = torderMain90.getTradeType();
            }else if(torderMain!=null)
            {
                pay90=MathUtil.mul(Double.valueOf(torderMain.getPayCoupon()),100.0).intValue();
                payCoin=torderMain.getCoinPayTotal();
                tSysUserId=torderMain.getBuyUserId();
                TBaseUser tSysUser = tsysUserDao.getById(TBaseUser.class,tSysUserId);
                open_id=tSysUser.getOpenId();
                Shop shop = (Shop)dao.findForObject("sysUserDao.getShopBySid",torderMain.getShopId());
                if(shop!=null)
                {
                    shop_code = shop.getSid();
                    brand_code = shop.getBrand_code();
                }
                user_code = torderMain.getUserCode()==null?"":torderMain.getUserCode();
                tradeType = torderMain.getPaymentRoute().toString();
            }
            /****************判断是否需要退久零贝*************************/
            if(payCoin!=null && payCoin>0 && !StringUtil.isNullOrEmpty(tSysUserId))
            {
                return_code=1;
                Map<String,String> map90=new HashMap<>();
                JSONObject jSONObject90 = new JSONObject();
                jSONObject90.put("userId",tSysUserId);
                jSONObject90.put("state","1");
                jSONObject90.put("orderNum",orderCode);
                jSONObject90.put("source","4");
                jSONObject90.put("amount",payCoin);
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
                        return_code=0;
                        msg="订单取消成功！";
                    }
                }
            }

            if(return_code==1)
            {
                msg="订单取消失败！";
            }else
            {
                /****************判断是否需要退久零券*************************/
                if(pay90!=null && pay90>0 && !StringUtil.isNullOrEmpty(tSysUserId))
                {
                    return_code=1;
                    Map<String,String> map1=new HashMap<>();
                    JSONObject jSONObject1 = new JSONObject();
                    jSONObject1.put("open_id",open_id);
                    jSONObject1.put("balance_90",pay90);
                    jSONObject1.put("shop_code",shop_code);
                    jSONObject1.put("order_code",orderCode);
                    jSONObject1.put("brand_code",brand_code);
                    jSONObject1.put("user_code",user_code);
                    jSONObject1.put("type", "1");
                    jSONObject1.put("source", "5");//退款
                    jSONObject1.put("source_msg", "用户退款");
                    jSONObject1.put("state",2);
                    jSONObject1.put("commission","0");
                    jSONObject1.put("orderState","1");
                    jSONObject1.put("orderTotal","0");
                    jSONObject1.put("tradeType",tradeType);
                    map1.put("json", jSONObject1.toString());
                    System.out.print(jSONObject1.toString());
                    String requestUrl = Global.getConfig("QT_Url")+"api/balance/operUserBalance90.ac";
                    String x = URLConectionUtil.httpURLConnectionPostDiy(requestUrl, map1);
                    x = URLDecoder.decode(x, "utf-8");
                    if(x==null||x.trim().equals("")){
                        return_code=1;
                        msg="订单取消失败！";
                    }else {
                        Map<String, Object> map2 = JSON.parseObject(x, Map.class);
                        if(map2!=null && map2.get("flag").equals("0"))  //发劵成功
                        {
                            return_code=0;
                            msg="订单取消成功！";
                        }else
                        {
                            return_code=1;
                            msg="订单取消失败！";
                        }
                    }
                }
            }
            result.put("return_code",return_code);
            result.put("msg",msg);

		}catch (Exception e){
			System.out.print("取消订单更新状态失败啦！");
            result.put("return_code",1);
            result.put("msg","订单取消失败！");
		}
        return result;
	}

    public void saveQtMsg(String jsonString,String orderCode) {
        try {
            if(!Tools.isEmpty(orderCode)){
                jsonString=orderCode+jsonString;
            }
            Map<String,Object> save_map=new HashMap<>();
            save_map.put("msgInfo",jsonString);
            dao.save("OrderMain90Dao.insertQtMsgInfo", save_map);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
