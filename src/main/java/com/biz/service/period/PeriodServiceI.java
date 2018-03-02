package com.biz.service.period;


import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Hmodel.api.TPeriodizationMain;
import com.biz.model.Pmodel.User;
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
public interface PeriodServiceI extends BaseServiceI<TPeriodizationMain> {

    Paging showPeroid(Map map) throws Exception;

    Paging showDetail(Map map)throws Exception;

    void delGridById(String id,User user)throws Exception;


}
