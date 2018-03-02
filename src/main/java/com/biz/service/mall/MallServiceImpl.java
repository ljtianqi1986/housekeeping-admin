package com.biz.service.mall;


import com.alibaba.fastjson.JSON;
import com.biz.conf.Global;
import com.biz.model.Hmodel.basic.*;
import com.biz.model.Hmodel.goods.baseStandard;
import com.biz.model.Hmodel.goods.goodsBrand;
import com.biz.model.Hmodel.goods.goodsUnit;
import com.biz.model.Pmodel.Ppics;
import com.biz.model.Pmodel.PwxGood;
import com.biz.model.Pmodel.PwxGoods;
import com.biz.model.Pmodel.PwxGoodsStock;
import com.biz.model.Pmodel.basic.*;
import com.biz.model.Pmodel.sys.PwxGoodsForStock;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import com.framework.utils.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.biz.model.Pmodel.basic.PwxgoodsStock;

/**
 * Created by liujiajia on 2017/1/6.
 */
@Service("mallService")
public class MallServiceImpl extends BaseServiceImpl<Twxgoods> implements MallServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Autowired
    private BaseDaoI<TwxGoodsGroup> wxgoodsGroupDao;

    @Autowired
    private BaseDaoI<Twxgoods> twxgoodsDao;

    @Autowired
    private BaseDaoI<Twxgoods2> twxgoodsDao2;

    @Autowired
    private BaseDaoI<TgoodsTag> tgoodsTagDao;

    @Autowired
    private BaseDaoI<Ttag> tagDao;

    @Autowired
    private BaseDaoI<goodsUnit> unitsDao;

    @Autowired
    private BaseDaoI<goodsBrand> brandDao;

    @Autowired
    private BaseDaoI<Tpics> picsDao;

    @Autowired
    private BaseDaoI<baseStandard> standDao;


    @Autowired
    private BaseDaoI<TwxgoodsProperty> wxgoodsPropertyDao;

    @Autowired
    private BaseDaoI<TwxgoodsStock> wxgoodsStockDao;

    @Autowired
    private BaseDaoI<TwxgoodsStock2> wxgoodsStockDao2;


    @Override
    public Pager<Pwxgoods> queryMall(Pager<Pwxgoods> pager) throws Exception {
        Pager<Pwxgoods> pagerRe = dao.findForPager1("mallDao.showUnionMall","mallDao.showUnionMallCount", pager);
        List<Pwxgoods> listS = pagerRe.getExhibitDatas();
        //erp地址
        String stockUrl = Global.get("ERP_URL") +"/interface/getGoodsStockAllByCityOnline.action";
        String cityId = "";
        String goodsSup = "";
        try{
            cityId = Global.getLocalCity();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        for(Pwxgoods pwxgoods1 : listS)
        {
            if(!StringUtil.isNullOrEmpty(pwxgoods1.getGoodsIdSup()))
            {
                goodsSup += pwxgoods1.getGoodsIdSup() + ",";
                pwxgoods1.setStockNow("0");
            }
        }
try {
    if(!Tools.isEmpty(goodsSup)){
        //调用接口获取erp数据
        Map<String, String> hashMapStock = new HashMap<>();
        hashMapStock.put("goodsId", goodsSup);
        hashMapStock.put("cityId", cityId);
        String x= URLConectionUtil.httpURLConnectionPostDiy(stockUrl,hashMapStock);
        x = URLDecoder.decode(x, "utf-8");
        x= JSON.parseObject(x,String.class);
        PwxGoodsForStock pwxGoodsForStock = JSON.parseObject(x, PwxGoodsForStock.class);
        if(pwxGoodsForStock != null && pwxGoodsForStock.getStocklist() != null)
        {
            List<PwxGoodsStock> stocklist = pwxGoodsForStock.getStocklist();
            if(stocklist != null && stocklist.size() > 0 ){
                for(Pwxgoods pwxgoods : listS)
                {
                    for(PwxGoodsStock pwxGoodsStock : stocklist)
                    {
                        if(pwxgoods.getGoodsIdSup() != null
                                && pwxGoodsStock.getGoodsId() != null
                                && pwxgoods.getGoodsIdSup().equals(pwxGoodsStock.getGoodsId()))
                        {
                            pwxgoods.setStockNow(""+pwxGoodsStock.getStock());
                            break;
                        }
                    }
                }
            }
        }
    }
}catch (Exception e)
{}


        pagerRe.setExhibitDatas(listS);

        return pagerRe;
    }

    @Override
    public List<TwxGoodsGroup> loadGoodsGroup(String mapId) throws Exception {
        String hql = StringUtil.formateString("from TwxGoodsGroup where isdel=0 ", mapId);
        return wxgoodsGroupDao.find(hql);
    }

    @Override
    public void upOrdown(String ids, String state, String userId, String type) throws Exception {
        if (!StringUtil.isNullOrEmpty(ids)) {
            String hql = StringUtil.formateString("update Twxgoods t set t.isSale='{1}' where t.id in({0})", StringUtil.formatSqlIn(ids), state);
            twxgoodsDao.executeHql(hql);
            String[] a_ids = ids.split(",");
            //联盟商品
            if("1".equals(type)){
                for(String a_id : a_ids)
                {
                    if("0".equals(state))//下架
                    {
                        dao.update("mallDao.updateNumMunesSss",a_id);
                    }else{
                        dao.update("mallDao.updateNumSss",a_id);
                    }
                }
            }
        }

    }


    @Override
    public List<TgoodsTag> loadGoodsTagsByGoodsId(String goodsId) throws Exception {
        String hql = StringUtil.formateString("from TgoodsTag where isdel=0 and goodsId='{0}'", goodsId);
        return tgoodsTagDao.find(hql);
    }

    @Override
    public void saveGoodsInfo(PwxGoods label_list, String shopId) throws Exception {
        Ttag tag=tagDao.getById(Ttag.class,label_list.getTag());
        TwxGoodsGroup group=wxgoodsGroupDao.getById(TwxGoodsGroup.class,label_list.getGroupId());
        //List<Tpics> pics=picsDao.findBySql("from Tpics where ")
        goodsUnit unit=unitsDao.getById(goodsUnit.class,label_list.getUnitId());
        goodsBrand brand=brandDao.getById(goodsBrand.class,label_list.getBrand());

        if(brand==null)
        {
            brand=new goodsBrand();
            brand.setId(label_list.getBrand());
            brand.setName(label_list.getBrandName());
            brand.setType(1);
            brandDao.save(brand);
        }
        if(unit==null)
        {
            unit=new goodsUnit();
            unit.setId(label_list.getUnitId());
            unit.setName(label_list.getUnitName());
            unit.setType(1);
            unitsDao.save(unit);;
        }
        Ptag  tt=new Ptag();
        Pgroup  tg=new Pgroup();
        if(tag==null)
        {

            tt.setName(label_list.getTagName());
            tt.setId(label_list.getTag());
            tt.setShopid(shopId);
            dao.save("mallDao.saveTag",tt);
        }
        if(group==null)
        {
            String picId="";
            if(!StringUtil.isNullOrEmpty(label_list.getGroupPath())) {
                Tpics pics = new Tpics();
                pics.setPath(label_list.getGroupPath().replace("http://9900oss.oss-cn-hangzhou.aliyuncs.com/", ""));
                picsDao.save(pics);
                picId=pics.getId();
            }

            tg.setName(label_list.getGroupName());
            tg.setId(label_list.getGroupId());
            tg.setIcon(picId);
            tg.setShopid(shopId);
            dao.save("mallDao.saveGroup",tg);
        }
        Map<String,String> map=new HashMap<>();
        map.put("group",label_list.getGroupId());
        map.put("tag",label_list.getTag());
        List<String> list= (List<String>) dao.findForList("mallDao.getTagGroup",map);
        if(list==null||list.size()<=0)
        {
            map.put("id", UuidUtil.get32UUID());
            dao.save("mallDao.saveTagGroup",map);
        }
        if(label_list.getCoverList()!=null&&label_list.getCoverList().size()>0)
        {
            for(int i=0;i<label_list.getCoverList().size();i++)
            {
                Tpics tp=picsDao.getById(Tpics.class,label_list.getCoverList().get(i).getCoverId());
                if(tp==null)
                {
                    Ppics pics=new Ppics();
                    pics.setPath(label_list.getCoverList().get(i).getCoverPath().replace("http://9900oss.oss-cn-hangzhou.aliyuncs.com/",""));
                    pics.setMainType(1);
                    pics.setId(label_list.getCoverList().get(i).getCoverId());
                    //  picsDao.save(pics);
                    dao.save("mallDao.saveGoodsPics",pics);
                }

            }

        }
        saveStandardForInterface(label_list,shopId);

    }

    private void saveStandardForInterface(PwxGoods label_list, String shopId) throws Exception {
        if(label_list.getStocklist()!=null&&label_list.getStocklist().size()>0)
        {
            List<Pstandard> list=new ArrayList<>();
            for(int i=0;i<label_list.getStocklist().size();i++)
            {
                if(!StringUtil.isNullOrEmpty(label_list.getStocklist().get(i).getTypesId1())) {
                    baseStandard s1 = standDao.getById(baseStandard.class, label_list.getStocklist().get(i).getTypesId1());
                    baseStandard s1p = standDao.getById(baseStandard.class, label_list.getStocklist().get(i).getStandard1Pid());
                    if(s1==null)
                    {
                        Pstandard s=new Pstandard();
                        s.setId(label_list.getStocklist().get(i).getTypesId1());
                        s.setName(label_list.getStocklist().get(i).getStandard1Name());
                        s.setShopId(shopId);
                        s.setPid(label_list.getStocklist().get(i).getStandard1Pid());
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
                        s.setId(label_list.getStocklist().get(i).getStandard1Pid());
                        s.setName(label_list.getStocklist().get(i).getStandard1Pname());
                        s.setShopId(shopId);
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

                if(!StringUtil.isNullOrEmpty(label_list.getStocklist().get(i).getTypesId2())) {
                    baseStandard s2 = standDao.getById(baseStandard.class, label_list.getStocklist().get(i).getTypesId2());
                    baseStandard s2p = standDao.getById(baseStandard.class, label_list.getStocklist().get(i).getStandard2Pid());
                    if(s2==null)
                    {
                        Pstandard s=new Pstandard();
                        s.setId(label_list.getStocklist().get(i).getTypesId2());
                        s.setName(label_list.getStocklist().get(i).getStandard2Name());
                        s.setShopId(shopId);
                        s.setPid(label_list.getStocklist().get(i).getStandard2Pid());
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
                        s.setId(label_list.getStocklist().get(i).getStandard2Pid());
                        s.setName(label_list.getStocklist().get(i).getStandard2Pname());
                        s.setShopId(shopId);
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
                if(!StringUtil.isNullOrEmpty(label_list.getStocklist().get(i).getTypesId3())) {
                    baseStandard s3 = standDao.getById(baseStandard.class, label_list.getStocklist().get(i).getTypesId3());
                    baseStandard s3p = standDao.getById(baseStandard.class, label_list.getStocklist().get(i).getStandard3Pid());

                    if(s3==null)
                    {
                        Pstandard s=new Pstandard();
                        s.setId(label_list.getStocklist().get(i).getTypesId3());
                        s.setName(label_list.getStocklist().get(i).getStandard3Name());
                        s.setShopId(shopId);
                        s.setPid(label_list.getStocklist().get(i).getStandard3Pid());
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
                        s.setId(label_list.getStocklist().get(i).getStandard3Pid());
                        s.setName(label_list.getStocklist().get(i).getStandard3Pname());
                        s.setShopId(shopId);
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
                dao.save("mallDao.saveStandardForInterface",map);}
        }
    }

    @Override
    public PwxGood getGoodsSku(String id) throws Exception {
        PwxGood res=new PwxGood();
        List<PwxGoodsStock> stocklist= (List<PwxGoodsStock>) dao.findForList("mallDao.getStocklist", id);
        List<PwxGoodsStock> stockParList=(List<PwxGoodsStock>) dao.findForList("mallDao.getStockParList", id);
        res.setId(id);
        res.setStocklist(stocklist);
        res.setStockParList(stockParList);
        return res;
    }

    @Override
    public List<Map<String, Object>> loadGroup(String pid) throws Exception {
        return (List<Map<String, Object>>)dao.findForList("mallDao.loadGroup",pid);
    }

    @Override
    public String addGroup(Map map) throws Exception {
        dao.save("mallDao.addGroup",map);
        return "操作成功";
    }

    @Override
    public List<Brand> showVenderId(String name) throws Exception {
        Map<String,String> map1=new HashMap<>();
        if(name!=null&&!name.trim().equals("")){
            map1.put("searchtext",name);
        }else{
            map1.put("searchtext", "");
        }
        List<Brand> list=(List<Brand>) dao.findForList("brandMapper.findMerchantGrid",map1);
        return list;
    }

    @Override
    public Map<String, Object> saveGoodsAndStock(Pwxgoods pwxgoods,String shopId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        String goodsId = "";
        if(pwxgoods.getPage_type()!=null&&pwxgoods.getPage_type().equals("update")){
            boolean f = false;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Twxgoods twxgoods = twxgoodsDao.getById(Twxgoods.class, pwxgoods.getId());
            goodsId = twxgoods.getId();
            BeanUtils.copyProperties(pwxgoods, twxgoods, new String[]{"isSale","buyerId","createTime", "discount", "isdel", "sendTime", "saleTime"});
            twxgoods.setDiscount(pwxgoods.getDiscount() == null ? (short) 0 : (short) 1);
            twxgoods.setIsdel((short) 0);
            twxgoods.setCreateTime(new Date());
            if (pwxgoods.getSendTime() != null && pwxgoods.getSendTime() != "") {
                twxgoods.setSendTime(DateUtil.string2Date(pwxgoods.getSendTime()));
            }
            twxgoods.setSales(0);
            twxgoodsDao.update(twxgoods);
            //更新商品图片
            String hql = StringUtil.formateString("update Tpics set isdel=1 where mainId='{0}'", twxgoods.getId());
            picsDao.executeHql(hql);
            if (!pwxgoods.getGoodsPicId().isEmpty()) {
                hql = StringUtil.formateString("update Tpics set isdel=0,mainId='{0}' where id in({1})", twxgoods.getId(), StringUtil.formatSqlIn(pwxgoods.getGoodsPicId()));
                picsDao.executeHql(hql);
            }
            //查询库存
            String hqlstock = StringUtil.formateString("from TwxgoodsStock where isdel=0 and goodsId='{0}'", pwxgoods.getId());
            List<TwxgoodsStock> stocklist = wxgoodsStockDao.find(hqlstock);
            for (int i = 0; i < stocklist.size(); i++) {
                if (((TwxgoodsStock) (stocklist.get(i))).getStockOccupy() > 0) {
                    //stockOccupy
                    f = true;
                }
            }

            //获得list
            List<PwxgoodsStock> list = pwxgoods.getStocklist();
            String[] types1 = null;
            String[] types2 = null;
            String[] types3 = null;
            String[] price = null;
            String[] stock = null;
            String[] venderId = null;
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getTypesId1() != null && !list.get(i).getTypesId1().equals("")) {
                        types1 = list.get(i).getTypesId1().split(",");
                    }
                    if (list.get(i).getTypesId2() != null && !list.get(i).getTypesId2().equals("")) {
                        types2 = list.get(i).getTypesId2().split(",");
                    }
                    if (list.get(i).getTypesId3() != null && !list.get(i).getTypesId3().equals("")) {
                        types3 = list.get(i).getTypesId3().split(",");
                    }
                    if (list.get(i).getPrice() != null && !list.get(i).getPrice().equals("")) {
                        price = list.get(i).getPrice().split(",");
                    }
                    if (list.get(i).getStock() != null && !list.get(i).getStock().equals("")) {
                        stock = list.get(i).getStock().split(",");
                    }
                    if (list.get(i).getVenderId() != null && !list.get(i).getVenderId().equals("")) {
                        venderId = list.get(i).getVenderId().split(",");
                    }
                }
            }

            //更新商品属性信息
            Map<String, String> mapTypes1 = new HashMap<String, String>();
            Map<String, String> mapTypes2 = new HashMap<String, String>();
            Map<String, String> mapTypes3 = new HashMap<String, String>();
            boolean exist = true;
            TwxgoodsProperty twxgoodsProperty;
            if (!f) {
                wxgoodsPropertyDao.executeHql(StringUtil.formateString("Update TwxgoodsProperty set isdel=1 where goodsId='{0}'", pwxgoods.getId()));
                wxgoodsStockDao.executeHql(StringUtil.formateString("Update TwxgoodsStock set isdel=1 where goodsId='{0}'", pwxgoods.getId()));
                for (int i = 0; i < types1.length; i++) {
                    //新增商品商品属性
                    exist = true;
                    for (Iterator<String> iter = mapTypes1.keySet().iterator(); iter.hasNext(); ) {
                        String key = iter.next();
                        if (types1[i].equals(key)) {
                            exist = false;
                        }
                    }
                    //添加属性1
                    if (exist) {
                        if (types1 != null && types1.length > 0) {
                            twxgoodsProperty = new TwxgoodsProperty();
                            twxgoodsProperty.setGoodsId(twxgoods.getId());
                            twxgoodsProperty.setProperty(types1[i]);
                            twxgoodsProperty.setType((short) 0);
                            twxgoodsProperty.setIsdel((short) 0);
                            twxgoodsProperty.setCreateTime(new Date());
                            wxgoodsPropertyDao.save(twxgoodsProperty);
                            //将新增的属性添加到临时Map中
                            mapTypes1.put(types1[i], twxgoodsProperty.getId());
                        }
                    }
                    exist = true;
                    for (Iterator<String> iter = mapTypes2.keySet().iterator(); iter.hasNext(); ) {
                        String key = iter.next();
                        if (types2[i].equals(key)) {
                            exist = false;
                        }
                    }
                    //添加属性2
                    if (exist) {
                        if (types2 != null && types2.length > 0) {
                            twxgoodsProperty = new TwxgoodsProperty();
                            twxgoodsProperty.setGoodsId(twxgoods.getId());
                            twxgoodsProperty.setProperty(types2[i]);
                            twxgoodsProperty.setType((short) 1);
                            twxgoodsProperty.setIsdel((short) 0);
                            twxgoodsProperty.setCreateTime(new Date());
                            wxgoodsPropertyDao.save(twxgoodsProperty);
                            //将新增的属性添加到临时Map中
                            mapTypes2.put(types2[i], twxgoodsProperty.getId());
                        }
                    }
                    exist = true;
                    for (Iterator<String> iter = mapTypes3.keySet().iterator(); iter.hasNext(); ) {
                        String key = iter.next();
                        if (types3[i].equals(key)) {
                            exist = false;
                        }
                    }
                    //添加属性3
                    if (exist) {
                        if (types3 != null && types3.length > 0) {
                            twxgoodsProperty = new TwxgoodsProperty();
                            twxgoodsProperty.setGoodsId(twxgoods.getId());
                            twxgoodsProperty.setProperty(types3[i]);
                            twxgoodsProperty.setType((short) 2);
                            twxgoodsProperty.setIsdel((short) 0);
                            twxgoodsProperty.setCreateTime(new Date());
                            wxgoodsPropertyDao.save(twxgoodsProperty);
                            //将新增的属性添加到临时Map中
                            mapTypes3.put(types3[i], twxgoodsProperty.getId());
                        }
                    }
                    //}

                }

                //保存商品库存信息
                TwxgoodsStock twxgoodsStock;
                for (int i = 0; i < types1.length; i++) {
                    //if(!(ids.length>0 && ids[i]!=null && !ids[i].equals("")))
                    //{
                    twxgoodsStock = new TwxgoodsStock();
                    twxgoodsStock.setGoodsId(twxgoods.getId());
                    if (types1 != null && types1.length > 0) {
                        twxgoodsStock.setTypesId1(mapTypes1.get(types1[i]));
                    }
                    if (types2 != null && types2.length > 0) {
                        twxgoodsStock.setTypesId2(mapTypes2.get(types2[i]));
                    }
                    if (types3 != null && types3.length > 0) {
                        twxgoodsStock.setTypesId3(mapTypes3.get(types3[i]));
                    }
                    twxgoodsStock.setSaleCount(0);
                    twxgoodsStock.setStockOccupy(0);
                    if (price != null && price.length > i) {
                        twxgoodsStock.setPrice(Double.valueOf(price[i]));
                    } else {
                        twxgoodsStock.setPrice(0.0);
                    }
                    if (stock != null && stock.length > i) {
                        twxgoodsStock.setStock(Integer.valueOf(stock[i]));
                        twxgoodsStock.setStockNow(Integer.valueOf(stock[i]));
                    } else {
                        twxgoodsStock.setStock(0);
                        twxgoodsStock.setStockNow(0);
                    }
                    if (venderId != null && venderId.length > i) {
                        twxgoodsStock.setVenderId(venderId[i]);
                    } else {
                        twxgoodsStock.setVenderId("");
                    }
                    twxgoodsStock.setIsdel((short) 0);
                    twxgoodsStock.setCreateTime(new Date());

                    //todo check venderId in stock
                    //if (isExsitedVenderId(0, twxgoods.getVenderIdMain(), twxgoodsStock)) {
                    //    map.put("isok", "no");
                    //    map.put("msg", "该商家商品中规格项目存在相同的商家编码，请检查！");
                    //    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    //    return map;
                    //}
                    wxgoodsStockDao.save(twxgoodsStock);
                    //}
                }
            }
            if (f) {
                String ids[]=pwxgoods.getStockId().split(",");
                for (int i = 0; i < types1.length; i++) {
                    //更新商品属性
                    if (ids.length > 0 && ids[i] != null && !ids[i].equals("")) {
                        TwxgoodsStock goodsStock = wxgoodsStockDao.getById(TwxgoodsStock.class, ids[i]);
                        if (goodsStock != null) {
                            //更新库存信息
                            if (price != null && price.length > i) {
                                goodsStock.setPrice(Double.valueOf(price[i]));
                            } else {
                                goodsStock.setPrice(0.0);
                            }
                            if (stock != null && stock.length > i) {
                                int oldStockNow = goodsStock.getStockNow();
                                if(Integer.valueOf(stock[i]) < oldStockNow){
                                    map.put("isok", "no");
                                    map.put("msg", "设置的商品库存不得小于现有商品库存数量，请检查！");
                                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                    return map;
                                }

                                goodsStock.setStockNow(Integer.valueOf(stock[i]));
                                goodsStock.setStock(goodsStock.getStockNow() + goodsStock.getStockOccupy());

                                //if ((goodsStock.getStock() - goodsStock.getStockOccupy()) < 0) {
                                //    map.put("isok", "no");
                                //    map.put("msg", "设置的商品库存不得小于占用量，请检查！");
                                //    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                //    return map;
                                //} else {
                                //    goodsStock.setStockNow((goodsStock.getStock() - goodsStock.getStockOccupy()) <= 0 ? 0 : (goodsStock.getStock() - goodsStock.getStockOccupy()));
                                //}

                            } else {
                                goodsStock.setStock(0);
                            }
                            if (venderId != null && venderId.length > i) {
                                goodsStock.setVenderId(venderId[i]);
                            } else {
                                goodsStock.setVenderId("");
                            }
                            goodsStock.setIsdel((short) 0);
                            wxgoodsStockDao.save(goodsStock);
                        }
                    }
                }
                map.put("msg", "基本信息及库存修改成功！属性无法修改");
            } else {
                map.put("msg", "修改成功！");
            }

        }else {
            //新增商品
            Twxgoods twxgoods = new Twxgoods();
            BeanUtils.copyProperties(pwxgoods, twxgoods, new String[]{"createTime", "discount", "isdel", "sendTime", "saleTime"});
            twxgoods.setDiscount(pwxgoods.getDiscount() == null ? (short) 0 : (short) 1);
            twxgoods.setIsdel((short) 0);
            twxgoods.setCreateTime(new Date());
            if (pwxgoods.getSendTime() != null && pwxgoods.getSendTime() != "") {
                twxgoods.setSendTime(DateUtil.string2Date(pwxgoods.getSendTime()));
            }
            //twxgoods.setStockTotal(stockTotal);
            twxgoods.setIsSale(Short.parseShort("1"));
            twxgoods.setSales(0);
            twxgoods.setBuyerId(shopId);
            twxgoods.setIsTicket(1);
            goodsId = (String) twxgoodsDao.save(twxgoods);
            if (pwxgoods.getGoodsPicId() != null && !pwxgoods.getGoodsPicId().isEmpty()) {
                String hql = StringUtil.formateString("update Tpics t set t.mainId='{0}' where id in({1})", twxgoods.getId(), StringUtil.formatSqlIn(pwxgoods.getGoodsPicId()));
                picsDao.executeHql(hql);
            }

            List<PwxgoodsStock> list = pwxgoods.getStocklist();
            String[] types1 = null;
            String[] types2 = null;
            String[] types3 = null;
            String[] price = null;
            String[] stock = null;
            String[] venderId = null;
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getTypesId1() != null && !list.get(i).getTypesId1().equals("")) {
                        types1 = list.get(i).getTypesId1().split(",");
                    }
                    if (list.get(i).getTypesId2() != null && !list.get(i).getTypesId2().equals("")) {
                        types2 = list.get(i).getTypesId2().split(",");
                    }
                    if (list.get(i).getTypesId3() != null && !list.get(i).getTypesId3().equals("")) {
                        types3 = list.get(i).getTypesId3().split(",");
                    }
                    if (list.get(i).getPrice() != null && !list.get(i).getPrice().equals("")) {
                        price = list.get(i).getPrice().split(",");
                    }
                    if (list.get(i).getStock() != null && !list.get(i).getStock().equals("")) {
                        stock = list.get(i).getStock().split(",");
                    }
                    if (list.get(i).getVenderId() != null && !list.get(i).getVenderId().equals("")) {
                        venderId = list.get(i).getVenderId().split(",");
                    }
                }
            }
//        //添加商品属性信息
            Map<String, String> mapTypes1 = new HashMap<String, String>();
            Map<String, String> mapTypes2 = new HashMap<String, String>();
            Map<String, String> mapTypes3 = new HashMap<String, String>();
            boolean exist = true;
            TwxgoodsProperty twxgoodsProperty;
            for (int i = 0; i < types1.length; i++) {
                exist = true;
                for (Iterator<String> iter = mapTypes1.keySet().iterator(); iter.hasNext(); ) {
                    String key = iter.next();
                    if (types1[i].equals(key)) {
                        exist = false;
                    }
                }
                //添加属性1
                if (exist) {
                    if (types1 != null && types1.length > 0) {
                        twxgoodsProperty = new TwxgoodsProperty();
                        twxgoodsProperty.setGoodsId(twxgoods.getId());
                        twxgoodsProperty.setProperty(types1[i]);

                        twxgoodsProperty.setType((short) 0);
                        twxgoodsProperty.setIsdel((short) 0);
                        twxgoodsProperty.setCreateTime(new Date());
                        wxgoodsPropertyDao.save(twxgoodsProperty);
                        //将新增的属性添加到临时Map中
                        mapTypes1.put(types1[i], twxgoodsProperty.getId());
                    }

                }
                exist = true;
                for (Iterator<String> iter = mapTypes2.keySet().iterator(); iter.hasNext(); ) {
                    String key = iter.next();
                    if (types2[i].equals(key)) {
                        exist = false;
                    }
                }
                //添加属性2
                if (exist) {
                    if (types2 != null && types2.length > 0) {
                        twxgoodsProperty = new TwxgoodsProperty();
                        twxgoodsProperty.setGoodsId(twxgoods.getId());
                        twxgoodsProperty.setProperty(types2[i]);
                        twxgoodsProperty.setType((short) 1);
                        twxgoodsProperty.setIsdel((short) 0);
                        twxgoodsProperty.setCreateTime(new Date());
                        wxgoodsPropertyDao.save(twxgoodsProperty);
                        //将新增的属性添加到临时Map中
                        mapTypes2.put(types2[i], twxgoodsProperty.getId());

                    }

                }
                exist = true;
                for (Iterator<String> iter = mapTypes3.keySet().iterator(); iter.hasNext(); ) {
                    String key = iter.next();
                    if (types3[i].equals(key)) {
                        exist = false;
                    }
                }
                //添加是属性3
                if (exist) {
                    if (types3 != null && types3.length > 0) {
                        twxgoodsProperty = new TwxgoodsProperty();
                        twxgoodsProperty.setGoodsId(twxgoods.getId());
                        twxgoodsProperty.setProperty(types3[i]);
                        twxgoodsProperty.setType((short) 2);
                        twxgoodsProperty.setIsdel((short) 0);
                        twxgoodsProperty.setCreateTime(new Date());
                        wxgoodsPropertyDao.save(twxgoodsProperty);
                        //将新增的属性添加到临时Map中
                        mapTypes3.put(types3[i], twxgoodsProperty.getId());

                    }

                }
            }
//        ////保存商品库存信息
            TwxgoodsStock twxgoodsStock;
            for (int i = 0; i < types1.length; i++) {
                twxgoodsStock = new TwxgoodsStock();
                twxgoodsStock.setGoodsId(twxgoods.getId());
                if (types1 != null && types1.length > 0) {
                    twxgoodsStock.setTypesId1(mapTypes1.get(types1[i]));
                }
                if (types2 != null && types2.length > 0) {
                    twxgoodsStock.setTypesId2(mapTypes2.get(types2[i]));
                }
                if (types3 != null && types3.length > 0) {
                    twxgoodsStock.setTypesId3(mapTypes3.get(types3[i]));
                }
                twxgoodsStock.setSaleCount(0);
                twxgoodsStock.setStockOccupy(0);
                if (price != null && price.length > i) {
                    twxgoodsStock.setPrice(Double.valueOf(price[i]));
                } else {
                    twxgoodsStock.setPrice(0.0);
                }
                if (stock != null && stock.length > i) {
                    twxgoodsStock.setStock(Integer.valueOf(stock[i]));
                    twxgoodsStock.setStockNow(Integer.valueOf(stock[i]));
                } else {
                    twxgoodsStock.setStock(0);
                    twxgoodsStock.setStockNow(0);
                }
                if (venderId != null && venderId.length > i) {
                    twxgoodsStock.setVenderId(venderId[i]);
                } else {
                    twxgoodsStock.setVenderId("");
                }
                twxgoodsStock.setIsdel((short) 0);
                twxgoodsStock.setCreateTime(new Date());
                //todo check venderId in stock
                //if (isExsitedVenderId(0, twxgoods.getVenderIdMain(), twxgoodsStock)) {
                //    map.put("isok", "no");
                //    map.put("msg", "该商家商品中规格项目存在相同的商家编码，请检查！");
                //    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                //    return map;
                //}
                wxgoodsStockDao.save(twxgoodsStock);
            }

//        //
            dao.update("mallDao.updateNum", pwxgoods.getBrand());
            map.put("msg", "修改成功！");
        }
        return  map;
    }

    @Override
    public Pwxgoods getByIdBySql(String id) throws Exception {
        return (Pwxgoods) dao.findForObject("mallDao.getByIdBySql", id);
    }

    @Override
    public List<PwxgoodsProperty> getwxgoodsProperty(String id) throws Exception {
        return (List<PwxgoodsProperty>) dao.findForList("mallDao.findWxgoodsPropertyList", id);
    }

    @Override
    public List<PwxgoodsStock> getwxgoodsStock(String id) throws Exception {
        return (List<PwxgoodsStock>) dao.findForList("mallDao.findWxgoodsStockList", id);
    }

    @Override
    public List<Ppics> findPicsById(String id) throws Exception {
        return (List<Ppics>) dao.findForList("mallDao.findPicsByMainid", id);
    }

    @Override
    public List<Map<String, Object>> findWxgoodsPropertyName(String id) throws Exception {
        return (List<Map<String, Object>>) dao.findForList("mallDao.findWxgoodsPropertyName", id);
    }

    @Override
    public void delGridById(String ids) throws Exception {
        String sql= StringUtil.formateString("update base_wxgoods set isdel=1 where id in({0})",StringUtil.formatSqlIn(ids));
        twxgoodsDao.executeSql(sql);
    }

    @Override
    public void updateStockAndPrice(Pwxgoods pwxgoods) throws Exception {
        String[] ids = pwxgoods.getStockId().split(",");
        String[] prices = pwxgoods.getPrice().split(",");
        String[] stocks = pwxgoods.getStock().split(",");

        if (ids != null && prices != null && stocks != null && ids.length == prices.length && ids.length == stocks.length) {
            Map<String, Object> params = new HashMap<>();
            for (int i = 0; i < ids.length; i++) {
                params.clear();
                params.put("id", ids[i]);
                params.put("price", prices[i]);
                params.put("stock", stocks[i]);
                dao.update("mallDao.updateStockAndPrice", params);

            }

        }
    }

    @Override
    public void addGoodAndStockFromChain(PwxGoods pwxGoodsSession, Pwxgoods pwxgoods,String mapId) throws Exception {
        //1 判断good是否存在,不存在add
        Map<String,Object> goodsMap = (Map<String, Object>) dao.findForObject("mallDao.getGoodsById", pwxGoodsSession.getId());
        if (goodsMap == null || StringUtil.isNullOrEmpty(goodsMap.get("id"))) {
            Twxgoods2 twxgoods = new Twxgoods2();
            twxgoods.setId(pwxGoodsSession.getId());
            twxgoods.setName(pwxGoodsSession.getName());
            twxgoods.setInfo(pwxGoodsSession.getComment());
            twxgoods.setIsSale((short)1);
            twxgoods.setVenderIdMain("");
            twxgoods.setBuyerId(mapId);
            twxgoods.setUnitId(pwxGoodsSession.getUnitId());
            twxgoods.setBrand(pwxGoodsSession.getBrand());
            twxgoods.setIsTicket(0);
            twxgoods.setGoodsIdSup(pwxGoodsSession.getId());
            twxgoods.setIsdel((short) 0);
            twxgoods.setCreateTime(pwxGoodsSession.getCreateTime());
            twxgoodsDao2.save(twxgoods);

            //图片
            List<PwxGoodsStock> coverList = pwxGoodsSession.getCoverList();
            if (coverList != null && coverList.size() > 0) {
                for (int i = 0; i < coverList.size(); i++) {
                    PwxGoodsStock pwxGoodsStock = coverList.get(i);
                    Tpics tpics = new Tpics();
                    tpics.setId(UuidUtil.get32UUID());
                    tpics.setMainId(pwxGoodsSession.getId());
                    tpics.setMainType(3);
                    tpics.setPath(pwxGoodsStock.getCoverPath());
                    picsDao.save(tpics);
                }
            }

        }

        //2 循环判断该good的stock是否存在,不存在add,存在跟新库存(价格?)
        List<PwxGoodsStock> stocklist = pwxGoodsSession.getStocklist();
        //String[] ids = pwxgoods.getStockId().split(",");
        //String[] prices = pwxgoods.getPrice().split(",");
        //String[] stocks = pwxgoods.getStock().split(",");
        Map<String, Object> stockParams = new HashMap<>();
        if (stocklist != null && stocklist.size() > 0) {
            for (int i = 0; i < stocklist.size(); i++) {
                PwxGoodsStock stock = stocklist.get(i);
                Map<String,Object> stockMap = (Map<String, Object>) dao.findForObject("mallDao.getGoodsStockById",stock.getId());
                stockParams.clear();
                if(stockMap !=null && !StringUtil.isNullOrEmpty(stockMap.get("id"))){
                    stockParams.put("id", stock.getId());
                    stockParams.put("addStock", stock.getStock());
                    //stock存在,更新库存
                    //dao.update("mallDao.updateStock", stockParams);
                }else{
                    //stock不存在,新增
                    TwxgoodsStock2 twxgoodsStock = new TwxgoodsStock2();
                    BeanUtils.copyProperties(stock,twxgoodsStock);
                    twxgoodsStock.setId(stock.getId());
                    twxgoodsStock.setPrice(stock.getPriceDiaopai());
                    twxgoodsStock.setStockNow(stock.getStock());
                    twxgoodsStock.setStockOccupy(0);
                    twxgoodsStock.setIsdel((short) 0);
                    wxgoodsStockDao2.save(twxgoodsStock);

                    //3 不存在的stock添加对应的property
                    TwxgoodsProperty twxgoodsProperty = null;
                    if(!StringUtil.isNullOrEmpty(stock.getTypesId1())){
                        twxgoodsProperty = new TwxgoodsProperty();
                        twxgoodsProperty.setId(stock.getTypesId1());
                        twxgoodsProperty.setGoodsId(stock.getGoodsId());
                        twxgoodsProperty.setType((short) 1);
                        twxgoodsProperty.setProperty(stock.getTypesId1());
                        twxgoodsProperty.setIsdel((short) 0);
                        twxgoodsProperty.setCreateTime(new Date());
                        wxgoodsPropertyDao.save(twxgoodsProperty);
                    }
                    if(!StringUtil.isNullOrEmpty(stock.getTypesId2())){
                        twxgoodsProperty = new TwxgoodsProperty();
                        twxgoodsProperty.setId(stock.getTypesId2());
                        twxgoodsProperty.setGoodsId(stock.getGoodsId());
                        twxgoodsProperty.setType((short) 1);
                        twxgoodsProperty.setProperty(stock.getTypesId2());
                        twxgoodsProperty.setIsdel((short) 0);
                        twxgoodsProperty.setCreateTime(new Date());
                        wxgoodsPropertyDao.save(twxgoodsProperty);
                    }
                    if(!StringUtil.isNullOrEmpty(stock.getTypesId3())){
                        twxgoodsProperty = new TwxgoodsProperty();
                        twxgoodsProperty.setId(stock.getTypesId3());
                        twxgoodsProperty.setGoodsId(stock.getGoodsId());
                        twxgoodsProperty.setType((short) 1);
                        twxgoodsProperty.setProperty(stock.getTypesId3());
                        twxgoodsProperty.setIsdel((short) 0);
                        twxgoodsProperty.setCreateTime(new Date());
                        wxgoodsPropertyDao.save(twxgoodsProperty);
                    }

                }

            }
        }

    }

    @Override
    public PwxGoods getGoodsAndStockById(String id) throws Exception {
        //加载商品详情
        PwxGoods good = (PwxGoods) dao.findForObject("mallDao.getGoodsDetail", id);
        //加载商品库存信息
        List<PwxGoodsStock> stockList = (List<PwxGoodsStock>)dao.findForList("mallDao.getGoodsStockById2", id);
        //加载商品库存信息
        List<PwxGoodsStock> stockParList = (List<PwxGoodsStock>)dao.findForList("mallDao.getGoodsStandarParNodeList", id);
        //加在商品论幅图
        PwxGoodsStock cover = new PwxGoodsStock();
        List<PwxGoodsStock> picPathList = new ArrayList<PwxGoodsStock>();
        if(!StringUtil.isNullOrEmpty(good.getId())){
            cover.setId(good.getId());
            picPathList = (List<PwxGoodsStock>)dao.findForList("mallDao.getGoodsCoverList",cover );
        }

        //erp地址
        String stockUrl = Global.get("ERP_URL") +"/interface/getGoodsStockByCityOnline.action";
        String cityId = "";
        String goodsSup = good.getGoodsIdSup();
        try{
            cityId = Global.getLocalCity();
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        if(!Tools.isEmpty(goodsSup)) {
            //调用接口获取erp数据
            Map<String, String> hashMapStock = new HashMap<>();
            hashMapStock.put("goodsId", goodsSup);
            hashMapStock.put("cityId", cityId);
            String x = URLConectionUtil.httpURLConnectionPostDiy(stockUrl, hashMapStock);
            x = URLDecoder.decode(x, "utf-8");
            x = JSON.parseObject(x, String.class);
            PwxGoodsForStock pwxGoodsForStock = JSON.parseObject(x, PwxGoodsForStock.class);
            if (pwxGoodsForStock != null && pwxGoodsForStock.getStocklist() != null) {
                List<PwxGoodsStock> stocklistChian = pwxGoodsForStock.getStocklist();
                if (stocklistChian != null && stocklistChian.size() > 0) {
                    for (PwxGoodsStock stock : stockList) {
                        for (PwxGoodsStock stockChain : stocklistChian) {
                            if (stock.getVenderId() != null
                                    && stockChain.getVenderId() != null
                                    && stock.getVenderId().equals(stockChain.getVenderId())) {
                                stock.setStock(stockChain.getStock());
                                break;
                            }
                        }
                    }
                }
            }
        }


        good.setCoverList(picPathList);
        good.setStocklist(stockList);
        good.setStockParList(stockParList);
        return good;
    }


    /**
     *
     * @param type 0新增 1跟新(未使用)
     * @param goodsVenderId
     * @param twxgoodsStock
     * @return
     * @throws Exception
     */
    public boolean isExsitedVenderId(int type,String goodsVenderId,TwxgoodsStock twxgoodsStock) throws Exception {
        boolean isExsited = false;
        Map<String, Object> params = new HashMap<>();
        params.put("goodsVenderId", goodsVenderId);
        params.put("stockVenderId", twxgoodsStock.getVenderId());
        List<Map<String, Object>> list = (List<Map<String, Object>>) dao.findForList("mallDao.isExsitedVenderId", params);

        if(type==0){
            if (list != null && list.size() > 0) {
                isExsited = true;
            }
        }else if(type==1){
            //if (list == null || list.size() < 1) {
            //    isExsited = true;
            //} else if (list.size() > 1) {
            //    isExsited = false;
            //} else if (list.size() == 1) {
            //    //跟新商品时,会删除所有stock,并新增stock
            //    //因为在一个事务中,为提交,不能通过isdel来区别删除的stock
            //    //所以这边用过goodsId,typesId1,typesId2,typesId3来确定stock
            //    Map<String, Object> tempMap = list.get(0);
            //    String goodsId = (String) tempMap.get("goodsId");
            //    String typesId1 = (String) tempMap.get("typesId1");
            //    String typesId2 = (String) tempMap.get("typesId2");
            //    String typesId3 = (String) tempMap.get("typesId3");
            //
            //    if(goodsId.equals(twxgoodsStock.getGoodsId()) && typesId1.equals(twxgoodsStock.getTypesId1()) && typesId2.equals(twxgoodsStock.getTypesId2()) && typesId3.equals(twxgoodsStock.getTypesId3())){
            //        isExsited = true;
            //    }
            //
            //}
        }

        return isExsited;

    }

    @Override
    public List<Map<String, Object>> findBrandSpeed() throws Exception {
        return (List<Map<String,Object>>) dao.findForList("mallDao.findBrandSpeed",null);
    }

    @Override
    public void doSyncGroup() throws Exception {
        try{
            //获取所有90group的id
            //List<Map<String,Object>> groupList = (List<Map<String, Object>>) dao.findForList("mallDao.findGroup90", null);
            List<TwxGoodsGroup> groupList = wxgoodsGroupDao.find("from TwxGoodsGroup g where g.isdel=0 and g.isTicket=0");

            String groupIds = "";
            for (TwxGoodsGroup tempGroup : groupList) {
                groupIds += tempGroup.getId() + ",";
            }

            String cityId= Global.getLocalCity();
            Map<String,String> parm=new HashMap<>();
            parm.put("groupIds",groupIds);
            String url=ConfigUtil.get("ERP_URL")+"/interface/doSendGroupByIds.action";
            String resString=URLConectionUtil.httpURLConnectionPostDiy(url,parm);
            if(!StringUtil.isNullOrEmpty(resString)&&!"失败".equals(resString))
            {
                resString = URLDecoder.decode(resString, "utf-8");
                resString= JSON.parseObject(resString,String.class);
                Map<String,Object> resMap=JSON.parseObject(resString,Map.class);
                if("1".equals(resMap.get("flag"))) {
                    throw new RuntimeException(String.valueOf(resMap.get("msg")));
                }else{
                    List<Map<String, Object>> list = (List<Map<String, Object>>) resMap.get("data");

                    for (TwxGoodsGroup twxGoodsGroup : groupList) {
                        for (Map<String, Object> newGroupMap : list) {
                            String newGroupId = (String) newGroupMap.get("groupId");
                            if(newGroupId.equals(twxGoodsGroup.getId())){
                                twxGoodsGroup.setName((String) newGroupMap.get("groupName"));
                                twxGoodsGroup.setNote((String) newGroupMap.get("groupNote"));
                                //icon不一致,则删除旧图片,新增新图片
                                String groupIcon = (String) newGroupMap.get("groupIcon");
                                if (!twxGoodsGroup.getIcon().equals(groupIcon)) {
                                    if (!StringUtil.isNullOrEmpty(twxGoodsGroup.getIcon())) {
                                        Tpics oldPic = picsDao.getById(Tpics.class, twxGoodsGroup.getIcon());
                                        picsDao.delete(oldPic);
                                    }

                                    if (!StringUtil.isNullOrEmpty(groupIcon)) {
                                        Tpics oldKeyPics = picsDao.getById(Tpics.class,groupIcon);
                                        if (oldKeyPics == null || StringUtil.isNullOrEmpty(oldKeyPics.getId())) {
                                            Ppics newPic = new Ppics();
                                            newPic.setId(groupIcon);
                                            newPic.setName((String) newGroupMap.get("name"));
                                            newPic.setMainType((Integer) newGroupMap.get("mainType"));
                                            newPic.setPath((String) newGroupMap.get("path"));
                                            newPic.setSize(((BigDecimal) newGroupMap.get("size")).doubleValue());
                                            newPic.setIsdel(0);
                                            newPic.setCreateTtime(new Date());
                                            dao.save("mallDao.saveGroupNewIcon", newPic);
                                        }



                                    }

                                    twxGoodsGroup.setIcon(groupIcon);

                                }
                                wxgoodsGroupDao.update(twxGoodsGroup);
                                break;
                            }
                        }
                    }
                }
            }else {
                throw new RuntimeException("同步失败，连接服务器失败！");
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("同步失败，异常错误！");
        }
    }
}
