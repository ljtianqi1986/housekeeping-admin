package com.biz.service.basic;

import com.alibaba.fastjson.JSON;
import com.biz.conf.Global;
import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.Shop;
import com.biz.model.Pmodel.basic.*;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*************************************************************************
 * create by lzq
 *
 * 文件名称 ：OrderServiceI.java 描述说明 ：
 *
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/

@Service("shopService")
public class ShopServiceImpl extends BaseServiceImpl<TorderMain> implements ShopServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Autowired
    private BaseDaoI baseDao;

    @Override
    public Paging showShop(Map map)throws Exception{
        int limit = Integer.parseInt(map.get("limit").toString());
        int offset = Integer.parseInt(map.get("offset").toString());
        Pager<Map<String, Object>> pager = new Pager();
        pager.setParameters(map);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);

        Pager<Map<String, Object>> pager_back = (Pager<Map<String, Object>>) dao
                .findForPager1("shopDao.queryShop",
                        "shopDao.queryShopCount", pager);
        //封装成适合前台使用的格式
        Paging paging = new Paging<>();
        paging.setRows(pager_back.getExhibitDatas());
        paging.setTotal((long) pager_back.getRecordCount());
        return paging;
    }

    /**
     * 查询一级类目
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Category> queryCategoryFirst() throws Exception {
        return (List<Category>) dao.findForList("shopDao.queryCategoryFirst",
                null);
    }

    /**
     * 查询某个类目的子类目
     *
     * @param parent_code
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Category> queryCategoryByParent(String parent_code) throws Exception {
        return (List<Category>) dao.findForList("shopDao.queryCategoryByParent",
                parent_code);
    }

    @Override
    public List<Pdistrict> queryDistrictByPcode(String area_code) throws Exception {
        return (List<Pdistrict>)dao.findForList("shopDao.queryDistrictByPcode",area_code);
    }

    @Override
    public List<Brand> loadBrand(String identity_code) throws Exception {
        return (List<Brand>)dao.findForList("shopDao.loadBrand",identity_code);
    }

    @Override
    public String doSave(Pshop pshop) throws Exception {
        String msg="";
        if(pshop.getType().equals("0")){
            //新增
            int count=findLoginName(pshop.getLogin_name());
            if(count!=0){
                msg="登录名重复，请重新填写登录名";
            }else{
                String sid = getRndCode();
                /**门店用户新增**/
                User shop_user = new User();
                shop_user.setLogin_name(pshop.getLogin_name());
                shop_user.setUser_code(getRndCode());
                shop_user.setPwd(MD5.md5(pshop.getPwd()));
                shop_user.setPerson_name(pshop.getBusiness_name());
                shop_user.setIdentity(4);
                shop_user.setPhone(pshop.getTelephone());//默认设置手机号为门店登录名
                shop_user.setRole_code("b94e46fac1fe4160a499456f2b18ed13");
                shop_user.setType(1);
                shop_user.setIdentity_code(sid);
                pshop.setSid(sid);
                Brand brand = (Brand) dao.findForObject("shopDao.getBrandByCode", pshop.getBrand_code());
              if(brand!=null&&!brand.equals("")){
                  pshop.setAgent_code(brand.getAgent_code());
              }else{
              }
                if(!StringUtil.isNullOrEmpty(pshop.getIs_90())&&"on".equals(pshop.getIs_90())){
                    pshop.setIs_90("1");
                }else{
                    pshop.setIs_90("0");
                }
                if(!StringUtil.isNullOrEmpty(pshop.getMoney_fixed())&&"on".equals(pshop.getMoney_fixed())){
                    pshop.setMoney_fixed("1");
                }else{
                    pshop.setMoney_fixed("0");
                }
                if(pshop.getAvg_price().equals("")){
                    pshop.setAvg_price("0");
                }
                String[] ids=pshop.getLongitude().split(",");
                pshop.setLongitude(ids[0]);
                pshop.setLatitude(ids[1]);
                dao.save("shopDao.insertShopUser", shop_user);
                dao.save("shopDao.insertShop", pshop);
//                shopService.insertShop(shop_user, pshop);
            msg="保存成功";
            }
        }else{
            if(!StringUtil.isNullOrEmpty(pshop.getIs_90())&&"on".equals(pshop.getIs_90())){
                pshop.setIs_90("1");
            }else{
                pshop.setIs_90("0");
            }
            if(!StringUtil.isNullOrEmpty(pshop.getMoney_fixed())&&"on".equals(pshop.getMoney_fixed())){
                pshop.setMoney_fixed("1");
            }else{
                pshop.setMoney_fixed("0");
            }
            if(pshop.getAvg_price().equals("")){
                pshop.setAvg_price("0");
            }
            String[] ids=pshop.getLongitude().split(",");
            pshop.setLongitude(ids[0]);
            pshop.setLatitude(ids[1]);
            dao.update("shopDao.updateShop", pshop);
            msg="保存成功";
        }
        return msg;
    }

    @Override
    public Paging showShopAccount(Map map) throws Exception {
        int limit = Integer.parseInt(map.get("limit").toString());
        int offset = Integer.parseInt(map.get("offset").toString());
        Pager<Map<String, Object>> pager = new Pager();
        pager.setParameters(map);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);

        Pager<Map<String, Object>> pager_back = (Pager<Map<String, Object>>) dao
                .findForPager1("shopDao.queryShopAccount",
                        "shopDao.queryShopAccountCount", pager);
        //封装成适合前台使用的格式
        Paging paging = new Paging<>();
        paging.setRows(pager_back.getExhibitDatas());
        paging.setTotal((long) pager_back.getRecordCount());
        return paging;
    }

    @Override
    public String updatePwd(String user_code,String newPwd) throws Exception {
        Map map=new HashMap();
        map.put("user_code",user_code);
        map.put("newPwd",MD5.md5(newPwd));
        dao.update("shopDao.updatePwd",map);
        return "操作成功";
    }

    @Override
    public String updatePhone(String user_code, String newPhone) throws Exception {
        Map map=new HashMap();
        map.put("user_code",user_code);
        map.put("newPhone",newPhone);
        dao.update("shopDao.updatePhone",map);
        return "操作成功";
    }

    @Override
    public void delGridById(String ids) throws Exception{
        String [] idList=ids.split(",");
        for (String id:idList) {
           dao.update("shopDao.delGridById",id);
        }
    }

//    @Override
    public int findLoginName(String login_name)throws Exception{
        return (int)dao.findForObject("shopDao.findLoginName",login_name);
    }

    /**
     * 得到随机数
     *
     * @return
     */
    public String getRndCode()
    {
        return UuidUtil.get32UUID();
    }

    /**
     * 查询identity_name
     *
     * @param identity
     * @param identity_code
     * @return
     * @throws Exception
     */
    public String queryIdentityName(int identity, String identity_code) throws Exception {
        Map<String,Object> pd = new HashMap<String,Object>();
        pd.put("identity", identity);
        pd.put("identity_code", identity_code);
        return (String) dao.findForObject("shopDao.queryIdentityName",pd);
    }

    /**
     * 查询代理商
     *
     * @param brand_code
     * @return
     * @throws Exception
     */
    public Agent queryAgentByBrandCode(String brand_code) throws Exception {
        return (Agent) dao.findForObject("ShopDao.queryAgentByBrandCode",brand_code);
    }

    /**
     * 查询代理商
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Agent> queryAgentByCondition() throws Exception {
        return (List<Agent>) dao.findForList("shopDao.queryAgentByCondition",null);
    }

    /**
     * 条件查询品牌
     *
     * @param agent_code
     * @param is90
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Brand> queryBrandByCondition(String agent_code, String is90) throws Exception {
        Map map = new HashMap();
        map.put("agent_code", agent_code);
        map.put("is90", is90);
        return (List<Brand>) dao.findForList("shopDao.queryBrandByCondition",map);
    }

    @Override
    public Shop getShopBySid(String shop_id) throws Exception {
        return (Shop)dao.findForObject("shopDao.getShopBySid",shop_id);
    }

    @Override
    public List<Pshop> loadStock(String checkString) throws Exception {
        return null;
    }

    /**
     * 获取
     *
     * @param sid
     * @return
     * @throws Exception
     */
    public Shop getShopByCode(String sid) throws Exception {
        Shop shop = (Shop) dao.findForObject("shopDao.getShopByCode", sid);
        return shop;
    }

    @Override
    public List<Shop> queryAllShopByBrand(String brandCode) throws Exception {
        return (List<Shop>) dao.findForList("ShopDao.queryAllShopByBrand",
                brandCode);
    }

    @Override
    public Paging showBusiness(Map map) throws Exception {
        int limit = Integer.parseInt(map.get("limit").toString());
        int offset = Integer.parseInt(map.get("offset").toString());
        Pager<Map<String, Object>> pager = new Pager();
        pager.setParameters(map);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);

        Pager<Map<String, Object>> pager_back = (Pager<Map<String, Object>>) dao
                .findForPager1("shopDao.showBusiness",
                        "shopDao.showBusinessCount", pager);
        //封装成适合前台使用的格式
        Paging paging = new Paging<>();
        paging.setRows(pager_back.getExhibitDatas());
        paging.setTotal((long) pager_back.getRecordCount());
        return paging;
    }

    @Override
    public String doSaveBusiness(Category category) throws Exception {
        String  msg="";
        category.setCategory_code(getRndCode());
        dao.save("shopDao.doSaveBusiness",category);
        return msg;
    }

    @Override
    public Pshop toGetEdit(String sid) throws Exception {
        return (Pshop) dao.findForObject("shopDao.toGetEdit",sid);
    }

    @Override
    public List<Pshop> getShopListForSelect(String pid, User user) throws Exception {
        Map<String, String> paramsMap=new HashMap<String, String>();
        paramsMap.put("pid",pid);
        paramsMap.put("identity",user.getIdentity()+"");
        paramsMap.put("identity_code",user.getIdentity_code()+"");
        return (List<Pshop>) dao.findForList("shopDao.getShopListForSelect",paramsMap);
    }

    @Override
    public List<Brand> loadBrandByParm(Map<String, String> map) throws Exception {
        return (List<Brand>)dao.findForList("shopDao.loadBrandByParm",map);
    }

    @Override
    public void updateQrCode(Pshop pshop) throws Exception {
        dao.update("shopDao.updateQrCode",pshop);
    }

    @Override
    public String saveGoodsAndShop(String sid, String goodsIds, User user) throws Exception {
        Map<String,Object> parm=new HashMap<>();
        parm.put("sid",sid);
        parm.put("goods",StringUtil.formatSqlIn(goodsIds));
        parm.put("userId",user.getUser_code());
        String []ids=goodsIds.split(",");
        //以下操作为去重操作，防止提交了已经授权的商品的id
        List<String> goodsIdList= (List<String>) dao.findForList("shopDao.getGoodsIdByShopAndGoodsId",parm);
         List<String> saveIds=new ArrayList<>();
        for(String id:ids)
         {
             boolean isIn=false;
             for(String goodsId:goodsIdList)
             {
                 if(id.equals(goodsId))
                 {
                     isIn=true;
                     break;
                 }
             }
             if(!isIn)
             {saveIds.add(id);}
         }
         //保存商品
         if(saveIds.size()>0)
         {
             boolean isPulled=getGoodsInfo(saveIds);
             if(isPulled)
             {
                 parm.put("saveIds",saveIds);
                 int  num= (int) dao.save("shopDao.saveGoodsAndShop",parm);
                 if(num>0)
                 {return "success";}
                 else
                 {return "授权失败!";}
             }else
             {return "授权失败，获取商品信息失败！";}
         }
        else{
             return "授权失败，没有需要额外授权的商品！";
         }
    }

    private boolean getGoodsInfo(List<String> saveIds) {
        boolean isPulled=false;
        String  ids="";
        for(String id:saveIds)
        {ids+=id+",";}
ids=ids.substring(0,ids.length()-1);
        try{
            String cityId= Global.getLocalCity();
            Map<String,String> parm=new HashMap<>();
            parm.put("goodsIds",ids);
            parm.put("cityId",cityId);
            String url=ConfigUtil.get("ERP_URL")+"/interface/doSendGoodsByGoodIdsAndCityId.action";
            String resString=URLConectionUtil.httpURLConnectionPostDiy(url,parm);
            if(!StringUtil.isNullOrEmpty(resString)&&!"失败".equals(resString))
            {
                Map<String,String> resMap=JSON.parseObject(resString,Map.class);
                if("0".equals(resMap.get("flag")))
                {
                    isPulled=true;
                }else
                {
                    isPulled=false;
                }
            }else
            {
                isPulled=false;
            }

        }catch (Exception e){
            isPulled=false;
        }
        return  isPulled;
    }

    @Override
    public String updateGoodsAndShopForDrop(String sid, String goodsIds, User user) throws Exception {
        Map<String,Object> parm=new HashMap<>();
        parm.put("sid",sid);
        parm.put("goods",StringUtil.formatSqlIn(goodsIds));
        //parm.put("userId",user.getUser_code());
      int  num= (int) dao.delete("shopDao.updateGoodsAndShopForDrop",parm);
            if(num>0)
            {return "success";}
            else
            {return "取消授权失败!";}
    }

    @Override
    public String updateGoodsAndShopUpAndDown(String sid, String goodsIds, User user, String isSale) throws Exception {
        Map<String,Object> parm=new HashMap<>();
        parm.put("sid",sid);
        parm.put("isSale",isSale);
        parm.put("goods",StringUtil.formatSqlIn(goodsIds));
        //parm.put("userId",user.getUser_code());
        int  num= (int) dao.update("shopDao.updateGoodsAndShopUpAndDown",parm);
        if(num>0)
        {return "success";}
        else
        {return "修改失败!";}
    }

    @Override
    public Paging showShopGoods(Map map) throws Exception {
        Paging page=new Paging();
        //通过erp获取商品列表
        String url=ConfigUtil.get("ERP_URL")+"/interface/d_getUnionGoodsList.action";
        Map<String,String> parm=new HashMap<>();
        //处理分页
        int limit = Integer.parseInt(map.get("limit").toString());
        int offset = Integer.parseInt(map.get("offset").toString());
        int pg=offset/limit;
        parm.put("page",pg+"");
       if(map.containsKey("goodsName")&&!StringUtil.isNullOrEmpty(map.get("goodsName")))
       { parm.put("goodsName", (String)map.get("goodsName") );}
        parm.put("pageSize",map.get("limit").toString());
        parm.put("partnersId", ConfigUtil.get("unionBrandId"));
       String res= URLConectionUtil.httpURLConnectionPostDiy(url,parm);
      //  String res="{\"count\":4,\"goodsList\":[{\"begin\":0,\"code\":\"aolong8776781\",\"createTime\":1499763702725,\"id\":\"402880ea5d30867c015d3090df210005\",\"name\":\"奥龙麻将机4\",\"nowStock\":20,\"page\":0,\"rows\":0,\"stockId\":\"402880ea5d30867c015d3090df290007\",\"stockName\":\"中灰 \",\"venderId\":\"4324frdsre32\"},{\"begin\":0,\"code\":\"aolong8776782\",\"createTime\":1499763702725,\"id\":\"402880ea5d30867c015d3090df210004\",\"name\":\"奥龙麻将机3\",\"nowStock\":20,\"page\":0,\"rows\":0,\"stockId\":\"402880ea5d30867c015d3090df290007\",\"stockName\":\"中灰 \",\"venderId\":\"4324frdsre32\"},{\"begin\":0,\"code\":\"aolong87767813\",\"createTime\":1499763702725,\"id\":\"402880ea5d30867c015d3090df26\",\"name\":\"奥龙麻将机22\",\"nowStock\":20,\"page\":0,\"rows\":0,\"stockId\":\"402880ea5d30867c015d3090df290007\",\"stockName\":\"中灰 \",\"venderId\":\"4324frdsre32\"},{\"begin\":0,\"code\":\"aolong877678\",\"createTime\":1499763702725,\"id\":\"402880ea5d30867c015d3090df210006\",\"name\":\"奥龙麻将机\",\"nowStock\":20,\"page\":0,\"rows\":0,\"stockId\":\"402880ea5d30867c015d3090df290007\",\"stockName\":\"中灰 \",\"venderId\":\"4324frdsre32\"}]}";
        res= JSON.parseObject(res,String.class);
        Map<String,Object> resMap=JSON.parseObject(res,Map.class);
        //商品总数
        page.setTotal(Long.valueOf(resMap.get("count")+""));
       List<Map<String,Object>>  goods= ( List<Map<String,Object>>) resMap.get("goodsList");
        if(goods.size()>0){ //当有商品的时候，要判断是否在本地有授权和是否上架
            resMap.put("sid",map.get("sid"));
            List<Map<String,Object>> localGoods= (List<Map<String, Object>>) dao.findForList("shopDao.getShopGoods",resMap);
            for(Map<String,Object> good:goods)
            {
                good.put("isSale","0");
                good.put("isHasGoods","0");
                for(Map<String,Object> localGood:localGoods)
                {
                    if(good.get("id").equals(localGood.get("goodsId")))
                    {
                        good.put("isSale",localGood.get("isSale"));
                        good.put("isHasGoods","1");
                        break;
                    }
                }
            }
        }

        page.setRows(goods);
        return page;
    }
}
