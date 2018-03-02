package com.biz.service.api;

import com.biz.model.Pmodel.api.*;
import com.biz.model.Pmodel.basic.Pbrand;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import com.framework.utils.UuidUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by lzq on 2017/2/4.
 */

@Service("orderMainService")
public class OrderMainServiceImpl extends BaseServiceImpl implements OrderMainServiceI {
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    /**
     * 联盟商户发劵记录
     */
    public List<Record90Detail> phone_90Record(Map<String, Object> hashMap) throws Exception
    {
        return (List<Record90Detail>)dao.findForList("OrderMainDao.phone_90Record",hashMap);
    }

    public int phone_90RecordCount(Map<String, Object> hashMap) throws Exception
    {
        return (int) dao.findForObject("OrderMainDao.phone_90RecordCount",hashMap);
    }


    public OrderMain90 getOrderMain90ByCode(String code) throws Exception
    {
        return (OrderMain90) dao.findForObject("OrderMainDao.getOrderMain90ByCode", code);
    }

    public Shop getShopBySid(String sid) throws Exception {
        return (Shop) dao.findForObject("OrderMainDao.getShopBySid", sid);
    }

    public String getSysUserIdByOpenId(String openid)throws Exception
    {
        return (String) dao.findForObject("OrderMainDao.getSysUserIdByOpenId", openid);
    }

    public Double getBalanceByOrderNum(String orderNum)throws Exception
    {
        Object total = dao.findForObject("OrderMainDao.getBalanceByOrderNum", orderNum);
        if(total==null){
            return 0.0;
        }else
        {
            return Double.parseDouble(total.toString());
        }
    }

    /**
     * 更新某个字段
     */
    public void updateWhereOrderMain90(Map<String,Object> params) throws Exception
    {
        dao.update("OrderMainDao.updateWhereOrderMain90", params);
    }

    @Override
    public List<OrderMainUnion> collectRecordForClerk(Map<String, Object> pd) throws Exception {
        if("1".equals(pd.get("type")+""))
        {
            pd.put("shop_id", pd.get("code")+"");
            return (List<OrderMainUnion>) dao.findForList("OrderMainUnionDao.collectRecord",pd);
        }else{
            return (List<OrderMainUnion>) dao.findForList("OrderMainUnionDao.collectRecordForClerk",pd);
        }
    }

    @Override
    public int checkNum(Map<String, Object> pd) throws Exception {
      return (int) dao.findForObject("OrderMainDao.checkNum",pd);
    }

    @Override
    public double checkMoneyNum(Map<String, Object> pd) throws Exception {
        return (double) dao.findForObject("OrderMainDao.checkMoneyNum",pd);
    }

    @Override
    public double collectMoneyNum(Map<String, Object> pd) throws Exception {
        return (double) dao.findForObject("OrderMainDao.collectMoneyNum",pd);
    }

    @Override
    public int collectNum(Map<String, Object> pd) throws Exception {
        return (int) dao.findForObject("OrderMainDao.collectNum",pd);
    }

    @Override
    public int getBalance_90(Map<String, Object> pd) throws Exception {
        return (int) dao.findForObject("OrderMainDao.getBalance_90",pd);
    }
    @Override
    public int getBalance_90_shop(Map<String, Object> pd) throws Exception {
        return (int) dao.findForObject("OrderMainDao.getBalance_90Shop",pd);
    }
    @Override
    public int getBalance_90_experience(Map<String, Object> pd) throws Exception {
        return (int) dao.findForObject("OrderMainDao.getBalance_90Experience",pd);
    }

    @Override
    public Pager<DetailVoucher> queryDetailVoucherList(Pager<DetailVoucher> pager) throws Exception{
        return dao.findForPager1("OrderMainDao.queryDetailVoucherList","OrderMainDao.queryDetailVoucherCount",pager);
    }

    @Override
    public List<Map<String, Object>> getWxReply() throws Exception {
        return (List<Map<String, Object>>) dao.findForList("OrderMainDao.getWxReply",null);
    }

    @Override
    public Map<String,Object> doSaveReply(Pbrand pbrand) throws Exception {
        Map<String,Object> map=new HashedMap();
        Map<String,Object> paramMap=new HashedMap();
        String titles[]=pbrand.getTitle().split(",");
        String paths[]=pbrand.getPath().split(",");
        String urls[]=pbrand.getUrl().split(",");
        String sorts[]=pbrand.getSort().split(",");
        String ids[]=pbrand.getId().split(",");
        if(titles.length==0||titles.length<pbrand.getCount()){
            map.put("flag","false");
            map.put("msg","标题不能为空");
            return map;
        }else{
            for(int i=0;i<titles.length;i++){
                if(titles[i]==null||titles[i].equals("")){
                    map.put("flag","false");
                    map.put("msg","第"+(i+1)+"条标题不能为空！");
                    return map;
                }else if(paths.length<=i||paths[i]==null||paths[i].equals("")){
                    map.put("flag","false");
                    map.put("msg","第"+(i+1)+"条图片不能为空！");
                    return map;
                }else if(urls.length<=i||urls[i]==null||urls[i].equals("")){
                    map.put("flag","false");
                    map.put("msg","第"+(i+1)+"条链接不能为空！");
                    return map;
                }else if(sorts.length<=i||sorts[i]==null||sorts[i].equals("")){
                    map.put("flag","false");
                    map.put("msg","第"+(i+1)+"条排序不能为空！");
                    return map;
                }
            }
            //更新前先删除
            dao.update("OrderMainDao.updateReplay",null);
            for(int i=0;i<titles.length;i++){
                paramMap.put("id",UuidUtil.get32UUID());
                paramMap.put("title",titles[i]);
                paramMap.put("url",urls[i]);
                paramMap.put("path",paths[i]);
                paramMap.put("sort",sorts[i]);
                dao.save("OrderMainDao.doSaveReply",paramMap);
            }
            map.put("flag","true");
        }
        return map;

    }

    @Override
    public List<Pbrand> loadWxReply() throws Exception {
        return (List<Pbrand>) dao.findForList("OrderMainDao.getWxReply",null);
    }


}
