package com.biz.service.basic;


import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.Shop;
import com.biz.model.Pmodel.basic.Brand;
import com.biz.model.Pmodel.basic.Category;
import com.biz.model.Pmodel.basic.Pdistrict;
import com.biz.model.Pmodel.basic.Pshop;
import com.biz.model.Pmodel.basic.*;
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
public interface ShopServiceI extends BaseServiceI<TorderMain> {
    /**
     * 获取全部订单
     * @return
     * @throws Exception
     */
    Paging showShop(Map map)throws Exception;

    /**
     * 查询第一级类目
     * @return
     * @throws Exception
     */
    List<Category> queryCategoryFirst()throws  Exception;

    /**
     * 查询次级类目
     * @param parent_code
     * @return
     * @throws Exception
     */
    List<Category> queryCategoryByParent(String parent_code)throws Exception;

    /**
     * 查询所属商圈
     * @param area_code
     * @return
     * @throws Exception
     */
    List<Pdistrict> queryDistrictByPcode(String area_code) throws Exception;

    /**
     * 查询商户
     * @param identity_code
     * @return
     * @throws Exception
     */
    List<Brand> loadBrand(String identity_code) throws Exception;

    /**
     * 保存或者新增门店信息
     * @param pshop
     * @return
     * @throws Exception
     */
    String doSave(Pshop pshop)throws  Exception;


    /**
     * 查询门店用户
     * @param map
     * @return
     * @throws Exception
     */
    Paging showShopAccount(Map map)throws Exception;

    /**
     *修改门店密码
     * @param user_code
     * @param newPwd
     * @return
     * @throws Exception
     */
    String updatePwd(String user_code,String newPwd) throws Exception;

    /**
     * 修改手机号
     * @param user_code
     * @param newPhone
     * @return
     * @throws Exception
     */
    String updatePhone(String user_code,String newPhone) throws  Exception;

    /**
     * 批量删除
     * @param  ids
     * @throws Exception
     */
    void delGridById(String ids)throws Exception;



    public String queryIdentityName(int identity, String identity_code) throws Exception;

    public Agent queryAgentByBrandCode(String brand_code) throws Exception;

    public List<Agent> queryAgentByCondition() throws Exception;

    public List<Brand> queryBrandByCondition(String agent_code, String is90) throws Exception;

    /**
     * 通过id获取shop
     * @param shop_id
     * @return
     * @throws Exception
     */
    Shop getShopBySid(String shop_id) throws Exception;

    List<Pshop> loadStock(String checkString)throws Exception;

    Shop getShopByCode(String identity_code) throws Exception;

    List<Shop> queryAllShopByBrand(String brandCode)throws Exception;

    Paging showBusiness(Map map)throws Exception;

    String doSaveBusiness(Category category) throws Exception;

    Pshop toGetEdit(String sid)throws Exception;

    List<Pshop> getShopListForSelect(String pid, User user)throws Exception;

    List<Brand> loadBrandByParm(Map<String, String> map)throws Exception;

    void updateQrCode(Pshop pshop) throws Exception;
    /**
     * 保存门店和商品的关联关系
     */
    String saveGoodsAndShop(String sid, String goodsIds, User user)throws Exception;
    /**
     * 取消门店和商品的关联关系
     */
    String updateGoodsAndShopForDrop(String sid, String goodsIds, User user)throws Exception;
    /**
     * 修改门店商品上下架
     */
    String updateGoodsAndShopUpAndDown(String sid, String goodsIds, User user, String isSale)throws Exception;

    Paging showShopGoods(Map map)throws Exception;
}
