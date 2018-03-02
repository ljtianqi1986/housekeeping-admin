package com.biz.service.api;

import com.biz.model.Pmodel.api.BaseUser;
import com.biz.model.Pmodel.api.CustomQrcode;
import com.biz.service.base.BaseServiceI;

import java.util.List;

/**
 * Created by lzq on 2017/2/4.
 */
public interface CustomQrcodeServiceI extends BaseServiceI {


    List<CustomQrcode> queryCustomQrcodeListByShop(String shop_id) throws  Exception;

    void delCustomQrcodeByCode(String code)throws  Exception;

    void insertCustomQrcode(CustomQrcode customQrcode)throws  Exception;
}
