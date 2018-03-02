package com.biz.service.base;


import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Pmodel.PorderMain;
import com.biz.model.Pmodel.basic.PorderSend;
import com.biz.model.Pmodel.basic.Ppics;
import com.biz.model.Pmodel.pay17.Pay17BackPay;
import com.biz.model.Pmodel.pay17.Pay17SelectPay;
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
public interface Pay17ServiceI extends BaseServiceI<Object> {
    Pay17SelectPay getPay17_bs(String order_code, int total, String author_code, String device_ip, String pay17_shop_code, String pay17_user_code) throws Exception;
    Pay17SelectPay getPay17_select(String shop_code,String order_code,String out_trade_no) throws Exception;
    Pay17BackPay getPay17_backSelect(String shop_code,String back_code) throws Exception;
    Pay17BackPay getPay17_back(String order_code,String out_refund_no,String shop_code,String user_code,int refund_fee) throws Exception;
}
