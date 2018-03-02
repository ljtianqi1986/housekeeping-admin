package com.biz.service.demo;

import com.biz.model.Pmodel.Pmenu;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.model.Params;

import java.util.List;
import java.util.Map;

/**
 * Created by yzljj on 2016/6/23.
 */
public interface DemoServiceI extends BaseServiceI<Object> {

	List<Map> showJqGrid()throws Exception;

    /**
     * 获取DemoPage 分页
     * @param sqlParams
     * @param params
     * @return
     * @throws Exception
     */
    Paging getDemoPage(Params sqlParams,Map<String,Object> params)throws Exception;

    /**
     * 批量删除
     * @param ids
     * @throws Exception
     */
    void delGridById(String ids)throws Exception;
	
}
