package com.biz.service.activity;

import com.biz.model.Hmodel.TCardType;
import com.biz.model.Pmodel.basic.Pbizperson;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Pager;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by tomchen on 17/2/4.
 */
public interface CardTypeServiceI extends BaseServiceI<TCardType>{
    Pager<Map<String,Object>> queryCardTypes(Pager<Map<String,Object>> pager) throws Exception;

}
