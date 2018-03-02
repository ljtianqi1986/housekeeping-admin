package com.biz.service.activity;

import com.biz.model.Pmodel.Pactivity;
import com.biz.model.Pmodel.PfirstBalance;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Paging;

import java.util.Map;

/**
 * Created by tomchen on 17/2/4.
 */
public interface FirstBalanceServiceI extends BaseServiceI{

    boolean addFirstBalance(PfirstBalance firstBalance) throws Exception;

    boolean updateFirstBalance(PfirstBalance firstBalance) throws Exception;
    PfirstBalance selectFirstBalance(String Id) throws Exception;
    PfirstBalance queryfirstBalance() throws Exception;
}
