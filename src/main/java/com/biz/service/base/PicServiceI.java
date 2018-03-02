package com.biz.service.base;


import com.biz.model.Hmodel.basic.Tpics;
import com.biz.model.Pmodel.basic.Pslide;
import com.biz.model.Pmodel.basic.PwxgoodsGroup;
import com.framework.model.Paging;
import com.framework.model.Params;

/*************************************************************************
* 版本：         V1.0
*
* 文件名称 ：PicServiceI.java 描述说明 ：
*
* 创建信息 : create by 曹凯 on 2016-8-30 上午11:01:56  修订信息 : modify by ( ) on (date) for ( )
*
* 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
**************************************************************************/
public interface PicServiceI extends BaseServiceI<Tpics> {

    Paging findMerchantGrid(Params sqlParams) throws Exception;

    PwxgoodsGroup findPwxgoodsGroupById(String id)throws Exception;
    /**
     * 保存轮播图
     * @param pslide
     * @return
     * @throws Exception
     */
    String doSave(Pslide pslide)throws Exception;

    /**
     * 回显数据
     * @param id
     * @return
     * @throws Exception
     */
    Pslide toGetEdit(String id)throws Exception;

    /**
     * 删除
     * @param ids
     * @return
     * @throws Exception
     */
    String delGridById(String ids)throws Exception;

    /**
     *
     * @param sqlParams
     * @return
     * @throws Exception
     */
    Paging findGoodsGroupGrid(Params sqlParams) throws Exception;

    /**
     * 更新商品分组
     * @param pwxgoodsGroup
     * @throws Exception
     */
    void updateGoodsGroup(PwxgoodsGroup pwxgoodsGroup) throws Exception;
}
