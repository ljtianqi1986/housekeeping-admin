package com.biz.service.api;

import com.biz.service.base.BaseServiceI;

import java.util.Map;

/**
 * Created by lzq on 2017/2/4.
 */
public interface TicketServiceI extends BaseServiceI {
    Map<String,Object> phone_gift_rg_tyd_public(String shop_code,String user_code,String total,String gift_type,String card_code,String memo) throws Exception;

    Map<String,Object> phone_gift_rg_public(String shop_code, String user_code, String total, String ticketType)throws Exception;
}
