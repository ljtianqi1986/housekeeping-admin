package com.biz.service.offlineCard;

import com.biz.model.Hmodel.TbaseDetail;
import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Pmodel.offlineCard.PofflineCard;
import com.biz.model.Pmodel.offlineCard.PofflineCardDetail;
import com.biz.model.Pmodel.offlineCard.PofflineCardGrant;
import com.biz.service.api.ApiInterfaceServiceI;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.utils.StringUtil;
import com.framework.utils.UuidUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * **********************************************************************
 * create by lzq
 * <p>
 * 文件名称 ：OrderServiceI.java 描述说明 ：
 * <p>
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 * ************************************************************************
 */

@Service("offlineCardService")
public class OfflineCardServiceImpl extends BaseServiceImpl<TorderMain> implements OfflineCardServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Autowired
    private BaseDaoI<TbaseDetail> baseDetailDao;

    @Resource(name = "apiInterfaceService")
    private ApiInterfaceServiceI apiInterfaceService;

    /**
     * 主表，列表翻页查询
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public Paging showOfflineCard(Map map) throws Exception {
        //参数
        int limit = Integer.parseInt(map.get("limit").toString());
        int offset = Integer.parseInt(map.get("offset").toString());
        Pager<Map<String, Object>> pager = new Pager();
        pager.setParameters(map);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);
        //查询
        Pager<Map<String, Object>> pager_back = (Pager<Map<String, Object>>) dao
                .findForPager1("offlineCardDao.findOfflineCardPage",
                        "offlineCardDao.findOfflineCardCount", pager);
        //封装成适合前台使用的格式
        Paging paging = new Paging<>();
        paging.setRows(pager_back.getExhibitDatas());
        paging.setTotal((long) pager_back.getRecordCount());
        return paging;
    }


    /**
     * 子表,码库列表翻页查询
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public Paging showOfflineCardDetail(Map map) throws Exception {
        //参数
        int limit = Integer.parseInt(map.get("limit").toString());
        int offset = Integer.parseInt(map.get("offset").toString());
        Pager<Map<String, Object>> pager = new Pager();
        pager.setParameters(map);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);
        //查询
        Pager<Map<String, Object>> pager_back = (Pager<Map<String, Object>>) dao
                .findForPager1("offlineCardDao.getCardDetailsForPage",
                        "offlineCardDao.getCardDetailsForCount", pager);
        //封装成适合前台使用的格式
        Paging paging = new Paging<>();
        paging.setRows(pager_back.getExhibitDatas());
        paging.setTotal((long) pager_back.getRecordCount());
        return paging;
    }

    //获取某个具体实体卡信息
    @Override
    public PofflineCard getCardById(String id) throws Exception {
        return (PofflineCard) dao.findForObject("offlineCardDao.getCardById", id);
    }

    //未使用的码库数量
    @Override
    public int getIsNotUsedCount(String id) throws Exception {
        return (Integer)  dao.findForObject("offlineCardDao.getIsNotUsedCount",id);
    }

    //查询实体卡类型
    @Override
    public List<Map<String,String>> getMyType(String identitycode) throws Exception {
        return (List<Map<String, String>>) dao.findForList("offlineCardDao.getMyType",identitycode);
    }

    //商户列表
    @Override
    public List<Map<String,String>> getMyBrand(String identitycode) throws Exception{
        return (List<Map<String,String>>) dao.findForList("offlineCardDao.getMyBrand", identitycode);
    }


    //保存实体卡
    @Override
    public boolean saveOfflineCard(PofflineCard pofflineCard) throws Exception {
        boolean pd = false;
        PofflineCard offlineCard_temp = this.saveOfflineCard_z(pofflineCard);
        if (offlineCard_temp != null) {
            pd=true;
            final PofflineCard offlineCard_f=offlineCard_temp;
            new Thread(){
                public void run(){
                    try {
                        addinsertOfflineCardDetail(offlineCard_f);
                    } catch (Exception e) {
                        //logger.error("",e);
                    }
                }
            }.start();
        }
        return pd;
    }

    //保存实体卡主表
    @Override
    public PofflineCard saveOfflineCard_z(PofflineCard pofflineCard) throws Exception {
        //添加实体卡主表
        //查找并生成最新序列号
        Integer batch = (Integer) dao.findForObject("offlineCardDao.getMaxBatchNumber", null);
        if (batch == null)
            batch = 0;
        else
            batch++;
        pofflineCard.setBatch(batch);
        pofflineCard.setId(getRndCode());
        Integer i = (Integer) dao.save("offlineCardDao.insertOfflineCard", pofflineCard);
        if (i > 0) {
            return pofflineCard;
        } else {
            return null;
        }

    }


    //生成码库子表
    public void addinsertOfflineCardDetail(PofflineCard offlineCard) throws Exception
    {
        try {
            //生成码表数据并添加数据库
            Set<String> set=new HashSet<>();
            int i = 0;
            int count = offlineCard.getCardCount();//需要生成数量
            String card_number=(offlineCard.getBatch() > 9 ? offlineCard.getBatch() : "0" + offlineCard.getBatch()).toString();
            int tmepNum=100000;
            List<PofflineCardDetail> offlineCardDetails = new ArrayList<PofflineCardDetail>();
            int f=0;
            for (; i < count; i++){

                String code = getRandom();
                if (set.add(code)){
                    PofflineCardDetail cardDetail = new PofflineCardDetail();
                    cardDetail.setCardNumber(card_number+String.valueOf(tmepNum+(f++)));
                    cardDetail.setMainId(offlineCard.getId());
                    cardDetail.setCardCode(card_number + code);
                    offlineCardDetails.add(cardDetail);
                } else {
                    i--;
                    continue;
                }
                if ((i + 1) % 1000 == 0){
                    //batch insert
                    dao.save("offlineCardDao.insertOfflineCardDetail", offlineCardDetails);
                    offlineCardDetails = new ArrayList<PofflineCardDetail>();
                }

            }
            if (!offlineCardDetails.isEmpty()){
                dao.save("offlineCardDao.insertOfflineCardDetail", offlineCardDetails);
            }
            //更新生成状态字段 生成成功
            dao.update("offlineCardDao.updateCreatStateOK", offlineCard.getId());
            //logger.debug("生成成功！");
        } catch (Exception e) {
            //更新生成状态字段 生成失败
            dao.update("offlineCardDao.updateCreatStateFail", offlineCard.getId());
            //logger.debug("生成失败！");
        }
        //logger.debug("生成结束！");
    }


    /**
     *	导出码库
     * @throws Exception
     */
    @Override
    public List<PofflineCardDetail> excelCardDetail(Map<String,Object> page) throws Exception
    {
        return (List<PofflineCardDetail>)dao.findForList("offlineCardDao.excelCardDetail", page);
    }


    /**
     * 发放
     * @param offlineCard_f
     * @param user_code
     * @throws Exception
     */
    @Override
    public synchronized void addOfflineCardGrand(PofflineCard offlineCard_f, String user_code) throws Exception{

        try
        {
            if(offlineCard_f.getCardCodeType().equals("1")) {
                List<String> list = (List<String>) dao.findForList("offlineCardDao.getIsNotUsedCountList", offlineCard_f.getId());
                List<PofflineCardGrant> listSave = new ArrayList<>();
                for (int i = 0; i < offlineCard_f.getCardCount(); i++) {
                    PofflineCardGrant of = new PofflineCardGrant();
                    of.setId(UuidUtil.get32UUID());
                    of.setBrandId(offlineCard_f.getBrandId());
                    of.setTypeId(offlineCard_f.getTypeId());
                    of.setDetailId(list.get(i));
                    of.setUserId(user_code);
                    of.setBizPersonId(offlineCard_f.getBizPersonId());
                    of.setCardNotes(offlineCard_f.getCard_notes());
                    listSave.add(of);
                    dao.update("offlineCardDao.updateDetailState", list.get(i));
                    if ((i + 1) % 1000 == 0) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("list", listSave);
                        dao.save("offlineCardDao.insertOfflineCardGrand", map);
                        listSave = new ArrayList<PofflineCardGrant>();
                    }

                }
                if (!listSave.isEmpty()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("list", listSave);
                    dao.save("offlineCardDao.insertOfflineCardGrand", map);
                }
            }else{
                String[] ids=offlineCard_f.getCardCode().split(",");
                List<PofflineCardGrant> listSave = new ArrayList<>();
                for (int i = 0; i < ids.length; i++) {
                    PofflineCardGrant of = new PofflineCardGrant();
                    of.setId(UuidUtil.get32UUID());
                    of.setBrandId(offlineCard_f.getBrandId());
                    of.setTypeId(offlineCard_f.getTypeId());
                    of.setDetailId(ids[i]);
                    of.setUserId(user_code);
                    of.setBizPersonId(offlineCard_f.getBizPersonId());
                    of.setCardNotes(offlineCard_f.getCard_notes());
                    listSave.add(of);
                    dao.update("offlineCardDao.updateDetailState", ids[i]);
                    if ((i + 1) % 1000 == 0) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("list", listSave);
                        dao.save("offlineCardDao.insertOfflineCardGrand", map);
                        listSave = new ArrayList<PofflineCardGrant>();
                    }

                }
                if (!listSave.isEmpty()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("list", listSave);
                    dao.save("offlineCardDao.insertOfflineCardGrand", map);
                }
            }
        }
        catch (Exception e)
        {
            System.out.printf(e.getMessage());
        }

    }






    //生成数字英文6位随机数
    public String getRandom(){
        String code="";
        String chars="0123456789";
        for(int i=0;i<8;i++){
            int size=(int) (Math.random()*9);
            code+=chars.charAt(size);
        }
        return code;
    }

    public String getRndCode()
    {
        return UuidUtil.get32UUID();
    }





    @Override
    public Paging showCardGrandDetail(Map map) throws Exception {
        //参数
        Pager<Map<String, Object>> pager = calculateAndTransformByMap(map);
        //查询
        Pager<Map<String, Object>> pager_back = (Pager<Map<String, Object>>) dao
                .findForPager1("offlineCardDao.getCardGrandForPage",
                        "offlineCardDao.getCardGrandForCount", pager);
        //封装成适合前台使用的格式
        Paging paging = new Paging<>();
        paging.setRows(pager_back.getExhibitDatas());
        paging.setTotal((long) pager_back.getRecordCount());
        return paging;
    }


    @Override
    public String doCancellation(String id) throws Exception {
        dao.update("offlineCardDao.doCancellation", StringUtil.formatSqlIn(id));
        return "1";
    }

    @Override
    public String doBackList(String id) throws Exception {
        dao.update("offlineCardDao.doBackList",StringUtil.formatSqlIn(id));
        dao.update("offlineCardDao.updateGrantState",StringUtil.formatSqlIn(id));
        return "1";
    }

    @Override
    public Map<String, Object> doCancelCardList(String id,String userId) throws Exception {
        Map<String,Object> res=new HashedMap();
        Map<String,Object> json=new HashedMap();
        String[] ids=id.split(",");
        for(int i=0;i<ids.length;i++){
            PofflineCardDetail pofflineCardDetail=(PofflineCardDetail) dao.findForObject("offlineCardDao.getPofflineCardDetail",ids[i]);
            if(pofflineCardDetail!=null){
                if(pofflineCardDetail.getState()!=null&&pofflineCardDetail.getState()==1){
                    String order_code=(String)dao.findForObject("offlineCardDao.getOrder_code",ids[i]);
                    json.put("brand_code","");
                    json.put("shop_code","");
                    json.put("user_code",userId);
                    json.put("order_code",order_code);
                    json.put("open_id",pofflineCardDetail.getOpenId());
                    json.put("type","4");
                    json.put("source","10");
                    json.put("source_msg","实体卡撤回");
                    json.put("balance_90",pofflineCardDetail.getCardTotal());
                    json.put("state","1");
                    json.put("commission","0");
                    json.put("tradeType","RECHARGE");
                    json.put("orderState","1");
                    json.put("orderTotal","0");
                    res=apiInterfaceService.updateUserBalance_90(json);
                    if(res.get("flag").equals("0")){
                        //扣券成功 修改实体卡状态
                        dao.update("offlineCardDao.updateState",pofflineCardDetail.getId());
                    }
//                    return res;
                }else{
                    res.put("flag","1");
                    res.put("msg","该实体卡无法撤回");
                    return res;
                }
            }else{
                res.put("flag","1");
                res.put("msg","无效的实体卡");
                return res;
            }
        }
        return res;
    }

    @Override
    public Paging showLoadCard(Map map) throws Exception {
        //参数
        Pager<Map<String, Object>> pager = calculateAndTransformByMap(map);
        //查询
        Pager<Map<String, Object>> pager_back = (Pager<Map<String, Object>>) dao
                .findForPager1("offlineCardDao.showLoadCard",
                        "offlineCardDao.showLoadCardCount", pager);


        //封装成适合前台使用的格式
        Paging paging = new Paging<>();
        paging.setRows(pager_back.getExhibitDatas());
        paging.setTotal((long) pager_back.getRecordCount());
        return paging;
    }

    @Override
    public Map<String, Object> loadCardInterval(String id) throws Exception {
        return (Map<String, Object>) dao.findForObject("offlineCardDao.loadCardInterval",id);
    }
}

