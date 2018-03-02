package com.biz.service.mall;

import com.biz.model.Pmodel.basic.*;
import com.biz.model.Hmodel.basic.TgoodsTag;
import com.biz.model.Hmodel.basic.TwxGoodsGroup;
import com.biz.model.Hmodel.basic.Twxgoods;
import com.biz.model.Pmodel.Ppics;
import com.biz.model.Pmodel.PwxGood;
import com.biz.model.Pmodel.PwxGoods;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Pager;

import java.util.List;
import java.util.Map;

/**
 * 商户Service
 * Created by liujiajia on 2017/1/6.
 */
public interface MallServiceI extends BaseServiceI<Twxgoods> {

    //Paging showUnionMall(Pager<Pwxgoods> pager) throws Exception;

    List<Map<String,Object>> loadGroup(String pid)throws Exception;

    String addGroup(Map map)throws Exception;

    List<Brand> showVenderId(String name) throws  Exception;

    Map<String, Object> saveGoodsAndStock(Pwxgoods pwxgoods,String shopId)throws Exception;

    Pwxgoods getByIdBySql(String id)throws Exception;

    List<PwxgoodsProperty> getwxgoodsProperty(String id)throws Exception;

    List<PwxgoodsStock> getwxgoodsStock(String id)throws Exception;

    List<Ppics> findPicsById(String id)throws Exception;

    List<Map<String, Object>> findWxgoodsPropertyName(String id)throws Exception;

    void delGridById(String ids) throws Exception;

    Pager queryMall(Pager<Pwxgoods> pager) throws Exception;

    /**
     * 加载分组
     * @return
     * @throws Exception
     */
    List<TwxGoodsGroup> loadGoodsGroup(String mapId) throws Exception;

    void upOrdown(String ids, String state, String userId, String type) throws Exception;


    List<TgoodsTag> loadGoodsTagsByGoodsId(String goodsId) throws Exception ;

    void saveGoodsInfo(PwxGoods label_list, String shopId) throws Exception;

    PwxGood getGoodsSku(String id) throws Exception ;

    void updateStockAndPrice(Pwxgoods pwxgoods) throws Exception;

    void addGoodAndStockFromChain(PwxGoods pwxGoodsSession, Pwxgoods pwxgoods,String mapId)throws Exception;

    PwxGoods getGoodsAndStockById(String id) throws Exception;

    List<Map<String,Object>> findBrandSpeed()throws Exception;

    void doSyncGroup() throws Exception;

}
