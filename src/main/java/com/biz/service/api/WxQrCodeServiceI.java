package com.biz.service.api;

import com.biz.service.base.BaseServiceI;

/**
 * Created by lzq on 2017/2/4.
 */
public interface WxQrCodeServiceI extends BaseServiceI {
    String getTempQrCode(int scene_id) throws Exception;
    String getForeverStrQrCode(String scene_str) throws Exception;
}
