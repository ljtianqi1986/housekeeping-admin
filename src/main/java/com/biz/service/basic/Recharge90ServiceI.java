package com.biz.service.basic;

import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.Recharge90;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Pager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by ldd_person on 2017/2/9.
 */
public interface Recharge90ServiceI extends BaseServiceI<Recharge90> {

    public Pager<Recharge90> queryRecharge90ListForPage(Pager pager) throws Exception;

    List<Recharge90> queryRecharge90List(Map<String,Object> param) throws Exception;

    void saveRecharge90(Recharge90 recharge90, User user, HttpServletRequest request)throws Exception;
}
