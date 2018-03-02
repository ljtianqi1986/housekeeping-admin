package com.biz.service.offlineCard;


import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Pmodel.offlineCard.PofflineCard;
import com.biz.model.Pmodel.offlineCard.PofflineCardDetail;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Paging;

import java.util.List;
import java.util.Map;

/*************************************************************************
* create by lzq
 *
* 文件名称 ：OrderServiceI.java 描述说明 ：
*
* 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
**************************************************************************/
public interface OfflineCardServiceI extends BaseServiceI<TorderMain> {

    /**
     * 获取全部实体卡
     * @return
     * @throws Exception
     */
    Paging showOfflineCard(Map map)throws Exception;

    /**
     * 获取某个具体实体卡信息
     * @param id
     * @return
     * @throws Exception
     */
    PofflineCard getCardById(String id) throws Exception;

    /**
     * 保存实体卡
     * @param pofflineCard
     * @return
     * @throws Exception
     */
    boolean saveOfflineCard(PofflineCard pofflineCard) throws Exception;

    /**
     * 保存实体卡主表
     * @param pofflineCard
     * @return
     * @throws Exception
     */
    PofflineCard saveOfflineCard_z(PofflineCard pofflineCard) throws Exception;

    /**
     * 查询码库
     * @param page
     * @return
     * @throws Exception
     */
    List<PofflineCardDetail> excelCardDetail(Map<String,Object> page) throws Exception;

    /**
     * 未使用的码库数量
     * @param code
     * @return
     * @throws Exception
     */
    int getIsNotUsedCount(String code) throws Exception;

    /**
     * 查询实体卡类型
     * @param identitycode
     * @return
     * @throws Exception
     */
    List<Map<String,String>> getMyType(String identitycode) throws Exception;

    /**
     * 商户列表
     * @param identitycode
     * @return
     * @throws Exception
     */
    List<Map<String,String>> getMyBrand(String identitycode) throws Exception;

    /**
     * 发放
     * @param offlineCard_f
     * @param user_code
     * @throws Exception
     */
    void addOfflineCardGrand(PofflineCard offlineCard_f, String user_code) throws Exception;

    /**
     * 子表,码库列表翻页查询
     * @param map
     * @return
     * @throws Exception
     */
    public Paging showOfflineCardDetail(Map map) throws Exception;


    /**
     * 查询发放记录
     * @param map
     * @return
     * @throws Exception
     */
    Paging showCardGrandDetail(Map map) throws Exception;

    /**
     * 作废实体卡
     * @param id
     * @throws Exception
     */
    String doCancellation(String id)throws Exception;

    /**
     * 退回实体卡
     * @param id
     * @throws Exception
     */
    String doBackList(String id)throws Exception;

    Map<String,Object> doCancelCardList(String id,String userId)throws Exception;

    /**
     * 加载指定发放
     * @param map
     * @return
     * @throws Exception
     */
    Paging showLoadCard(Map map) throws Exception;

    Map<String,Object> loadCardInterval(String id)throws Exception;

}
