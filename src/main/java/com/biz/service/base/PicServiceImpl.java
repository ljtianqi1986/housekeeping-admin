package com.biz.service.base;

import com.biz.model.Hmodel.basic.Tpics;
import com.biz.model.Hmodel.basic.TwxGoodsGroup;
import com.biz.model.Pmodel.basic.Pslide;
import com.biz.model.Pmodel.basic.PwxgoodsGroup;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Paging;
import com.framework.model.Params;
import com.framework.utils.StringUtil;
import com.framework.utils.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/*************************************************************************
 * 版本：         V1.0  
 *
 * 文件名称 ：PicServiceImpl.java 描述说明 ：
 * 
 * 创建信息 : create by 曹凯 on 2016-8-30 上午11:04:06  修订信息 : modify by ( ) on (date) for ( )
 * 
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/
@Service("picService")
public class PicServiceImpl extends BaseServiceImpl<Tpics> implements PicServiceI {
	@Resource(name = "daoSupport")
	private DaoSupport dao;
	@Autowired
	private BaseDaoI<Tpics> picDao;
    @Autowired
    private BaseDaoI<TwxGoodsGroup> wxgoodsGroupDao;

	@Override
	public Paging findMerchantGrid(Params sqlParams) throws Exception {
		Paging paging= dao.findForPagings("picDao.findPageForRunPicWX",sqlParams,"picDao.countForRunPicWX",sqlParams);
		return paging;
	}
    @Override
    public Paging findGoodsGroupGrid(Params sqlParams) throws Exception {
        Paging paging= dao.findForPagings("picDao.findGoodsGroupGrid",sqlParams,"picDao.findGoodsGroupCount",sqlParams);
        return paging;
    }

    @Override
    public PwxgoodsGroup findPwxgoodsGroupById(String id) throws Exception {
        return (PwxgoodsGroup) dao.findForObject("picDao.findPwxgoodsGroupById",id);
    }

    @Override
    public void updateGoodsGroup(PwxgoodsGroup pwxgoodsGroup) throws Exception{
        TwxGoodsGroup wg=wxgoodsGroupDao.getById(TwxGoodsGroup.class,pwxgoodsGroup.getId());
        wg.setName(pwxgoodsGroup.getName());
        if(null !=pwxgoodsGroup.getState() && pwxgoodsGroup.getState().equals("on"))
        {
            wg.setState((short)0);
        }else
        {
            wg.setState((short)1);
        }
        wg.setNote(pwxgoodsGroup.getNote());
        wg.setCustomImg(pwxgoodsGroup.getCustomImg());
        if(null !=pwxgoodsGroup.getIsCustom() && pwxgoodsGroup.getIsCustom().equals("on"))
        {
            wg.setIsCustom(1);
        }else
        {
            wg.setIsCustom(0);
        }
        wg.setSort(pwxgoodsGroup.getSort());
        wxgoodsGroupDao.update(wg);
    }
	@Override
	public String doSave(Pslide pslide) throws Exception {
		if (pslide.getPage_type().equals("0")) {
			pslide.setId(UuidUtil.get32UUID());
			//新增
			dao.save("picDao.saveSlide",pslide);
		}else{
			//更新
			dao.update("picDao.updateSlide",pslide);
		}
		return "保存成功";
	}

	@Override
	public Pslide toGetEdit(String id) throws Exception {
		return (Pslide) dao.findForObject("picDao.toGetEdit",id);
	}

	@Override
	public String delGridById(String ids) throws Exception {
		dao.update("picDao.delGridById", StringUtil.formatSqlIn(ids));
		return "操作成功！";
	}
}
