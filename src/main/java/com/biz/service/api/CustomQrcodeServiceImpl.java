package com.biz.service.api;

import com.biz.model.Pmodel.api.BaseUser;
import com.biz.model.Pmodel.api.CustomQrcode;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lzq on 2017/2/4.
 */

@Service("customQrcodeService")
public class CustomQrcodeServiceImpl extends BaseServiceImpl implements CustomQrcodeServiceI {
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Override
    public List<CustomQrcode> queryCustomQrcodeListByShop(String shop_code) throws Exception {
        return (List<CustomQrcode>) dao.findForList("CustomQrcodeDao.queryCustomQrcodeListByShop",shop_code);
    }

    @Override
    public void delCustomQrcodeByCode(String code) throws Exception {
        dao.update("CustomQrcodeDao.delCustomQrcodeByCode", code);
    }

    @Override
    public void insertCustomQrcode(CustomQrcode customQrcode) throws Exception {
        dao.save("CustomQrcodeDao.insertCustomQrcode", customQrcode);
    }
}
