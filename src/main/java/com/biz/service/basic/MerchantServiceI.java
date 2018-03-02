package com.biz.service.basic;

import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.Brand;
import com.biz.model.Pmodel.basic.BrandSpeed;
import com.biz.model.Pmodel.basic.Category;
import com.biz.model.Pmodel.basic.Pbrand;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Paging;
import com.framework.model.Params;

import java.util.List;
import java.util.Map;

/**
 * 商户Service
 * Created by liujiajia on 2017/1/6.
 */
public interface MerchantServiceI extends BaseServiceI<Object> {

    /**
     * 根据参数加载商户Grid
     * @param parm
     * @return
     */
    Paging findMerchantGrid(Params parm);

    /**
     * 根据商户id 加载商户
     * @param ids
     * @return
     * @throws Exception
     */
    Brand getMerchantById(String ids) throws Exception;

    /**
     * 根据商户ids 删除商户
     * @param ids
     * @throws Exception
     */
    void delGridById(String ids)throws  Exception;

    /**
     * 根据商户id 更新商户锁定状态
     * @param ids
     * @throws Exception
     */

    void changeStateById(String ids)throws Exception;

    /**
     * 根据商户id 重置商户密码
     * @param ids
     * @throws Exception
     */

    void resetPwdById(String ids)throws Exception;
    /**
     * 根据行业root ID 加载行业信息  如果为空 则加载一级信息
     * @param pid
     * @return
     * @throws Exception
     */

    List<Category> getIndustry(String pid)throws Exception;
    /**
     * 获取所有商户列表
     * @return List
     * @throws Exception
     * @param pid
     * @param user
     */
    List<Brand> getBrandListForSelect(String pid, User user)throws Exception;

    /**
     * 加载快速分类
     * @return
     * @throws Exception
     */
    List<BrandSpeed> showBrandSpeed() throws Exception;
    /**
     * 进行商户名称和登录名去重检查
     * @return
     * @throws Exception
     */
    String checkInfo(Pbrand pbrand)throws Exception;
    /**
     * 保存商户
     * @return
     * @throws Exception
     */
    void saveMerchant(Pbrand pbrand)throws Exception;
    /**
     * 修改商户
     * @return
     * @throws Exception
     */
    void updateMerchant(Pbrand pbrand)throws Exception;
    /**
     * 编辑回写读取数据
     * @return
     * @throws Exception
     */
    Pbrand findById(String id)throws Exception;

    Paging findBrandStatisticsGrid(Params sqlParams)throws Exception;

    void savePeriodization(String cycleDays,String scale,String brand_code,String userId) throws Exception;

    void updatePeriodization(String brandCode, String userId) throws Exception;

    List<Map<String,Object>> findPeriodizationByBrandId(String id) throws Exception;
}
