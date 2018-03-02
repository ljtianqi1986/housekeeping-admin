package com.biz.service.basic;

import com.biz.model.Pmodel.PSysLog;
import com.biz.model.Pmodel.basic.Pmessage;
import com.framework.model.Pager;

/**
 * Created by tomchen on 17/1/9.
 */
public interface MessageServiceI {

    Pager<Pmessage> queryMessages(Pager pager) throws Exception;

    void updateState(String ids,String state) throws Exception;

    Pager<Pmessage> showSignDetail(Pager pager)throws Exception;

}
