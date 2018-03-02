package com.biz.service.basic;


import com.biz.model.Hmodel.basic.TBaseBrand;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.Brand;
import com.biz.model.Pmodel.basic.BrandSpeed;
import com.biz.model.Pmodel.basic.Category;
import com.biz.model.Pmodel.basic.Pbrand;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Paging;
import com.framework.model.Params;
import com.framework.utils.MD5;
import com.framework.utils.StringUtil;
import com.framework.utils.UuidUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by liujiajia on 2017/1/6.
 */
@Service("merchantService")
public class MerchantServiceImpl extends BaseServiceImpl<Object> implements MerchantServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Autowired
    private BaseDaoI baseDao;
    @Autowired
    private BaseDaoI<TBaseBrand> brandDao;
    @Override
    public Paging findMerchantGrid(Params parm) {
        Paging paging= dao.findForPagings("brandMapper.findMerchantGrid",parm,"brandMapper.findMerchantCount",parm);
        return paging;
    }

    @Override
    public Brand getMerchantById(String ids) throws Exception {
        Brand brand=(Brand) dao.findForObject("brandMapper.getMerchantById",ids);
        return brand;
    }

    @Override
    public void delGridById(String ids) throws Exception {
        String sql= StringUtil.formateString("update base_brand set isdel=1 where brand_code in({0})",StringUtil.formatSqlIn(ids));
        baseDao.executeSql(sql);
    }

    @Override
    public void changeStateById(String ids) throws Exception {
        String sql= StringUtil.formateString("update base_brand set islock=ABS(islock-1) where brand_code in({0})",StringUtil.formatSqlIn(ids));
        baseDao.executeSql(sql);
    }

    @Override
    public void resetPwdById(String ids) throws Exception {
        String sql= StringUtil.formateString("update t_sys_user set pwd='e10adc3949ba59abbe56e057f20f883e' where identity_code in({0})",StringUtil.formatSqlIn(ids));
        baseDao.executeSql(sql);
    }

    @Override
    public List<Category> getIndustry(String pid) throws Exception {
        Map<String, Object> paramsMap=new HashMap<String, Object>();
        paramsMap.put("pid",pid);
        List<Category> categoryList=( List<Category>) dao.findForList("brandMapper.getIndustry",paramsMap);
        return categoryList;
    }

    @Override
    public List<Brand> getBrandListForSelect(String pid, User user) throws Exception {
        Map<String, String> paramsMap=new HashMap<String, String>();
        paramsMap.put("pid",pid);
        paramsMap.put("identity",user.getIdentity()+"");
        paramsMap.put("identity_code",user.getIdentity_code()+"");
        return (List<Brand>) dao.findForList("brandMapper.getBrandListForSelect",paramsMap);
    }

    @Override
    public List<BrandSpeed> showBrandSpeed() throws Exception {
        return (List<BrandSpeed>) dao.findForList("brandSpeedMapper.findBrandSpeedList",null);
    }

    @Override
    public String checkInfo(Pbrand pbrand) throws Exception {
        if(!StringUtil.isNullOrEmpty(pbrand.getBrandCode()))
        {return "0";}
        Integer name= (Integer) dao.findForObject("brandMapper.checkName",pbrand);//判断名称是否重复
        Integer loginName= (Integer) dao.findForObject("userDao.checkLoginName",pbrand);//判断登录名是否重复
        if(name>0 )
        {
            return "1";
        }
        else if(loginName>0)
        {return "2";}
        else
        {return "0";}
    }

    @Override
    public void saveMerchant(Pbrand pbrand) throws Exception {
        TBaseBrand tb=new TBaseBrand();
        BeanUtils.copyProperties(pbrand,tb, "isdel","createTime","islock","balance90","creditTotal90","creditNow90","commission","procedures","is90","pageviews","sort","proportion","isPeriodization","sorts");
        tb.setSort(99);
        try {
            tb.setSorts(Integer.valueOf(pbrand.getSorts()));
        }
        catch (Exception e)
        {}
        try {
            tb.setBalance90experience(Long.valueOf(pbrand.getBalance90_experience()));
            tb.setCreditTotal90experience(Integer.valueOf(pbrand.getCreditTotal90_experience()));
            tb.setCreditNow90experience(Integer.valueOf(pbrand.getCreditNow90_experience()));
        }
        catch (Exception e)
        {
            tb.setBalance90experience((long) 0);
            tb.setCreditTotal90experience(0);
            tb.setCreditNow90experience(0);}
        try {
            tb.setBalance90shop(Long.valueOf(pbrand.getBalance90_shop()));
            tb.setCreditTotal90shop(Integer.valueOf(pbrand.getCreditTotal90_shop()));
            tb.setCreditNow90shop(Integer.valueOf(pbrand.getCreditNow90_shop()));
        }
        catch (Exception e)
        {
            tb.setBalance90shop(Long.valueOf(0));
            tb.setCreditTotal90shop(0);
            tb.setCreditNow90shop(0);
        }
        tb.setIsdel((short) 0);
        tb.setCreateTime(new Timestamp(new Date().getTime()));
        tb.setIslock((short) 0);
        tb.setIsPeriodization(Short.valueOf(pbrand.getIsPeriodization()));
        if(!StringUtil.isNullOrEmpty(pbrand.getIs90())&&"on".equals(pbrand.getIs90()))//判断是否是体验店
        {
            tb.setIs90((short) 1);
        }else
        {tb.setIs90((short) 0);}

        //判断是否送贝
        if(StringUtil.isNullOrEmpty(pbrand.getIscoin())){
            tb.setIscoin((short) 0);
        }else if("on".equals(pbrand.getIscoin().toString())){
            tb.setIscoin((short) 1);
        }else{
            tb.setIscoin((short) 0);
        }

        //判断是否发券
        if(StringUtil.isNullOrEmpty(pbrand.getIsTicket())){
            tb.setIsTicket((short) 0);
        }else if("on".equals(pbrand.getIsTicket().toString())){
            tb.setIsTicket((short) 1);
        }else{
            tb.setIsTicket((short) 0);
        }


        //判断是否支持零购卡
        if(StringUtil.isNullOrEmpty(pbrand.getIsZeroCheck())){
            tb.setIsZeroCheck((short) 0);
        }else if("on".equals(pbrand.getIsZeroCheck().toString())){
            tb.setIsZeroCheck((short) 1);
        }else{
            tb.setIsZeroCheck((short) 0);
        }

        try {
            tb.setBalance90(Long.valueOf(pbrand.getBalance90()));//90余额
        }catch (Exception e)
        {
            tb.setBalance90((long) 0);
        }

        try {
            tb.setCreditTotal90(Integer.valueOf(pbrand.getCreditTotal90()));//透支额度
        }catch (Exception e)
        {
            tb.setCreditTotal90(0);
        }
        try {
            tb.setCreditNow90(Integer.valueOf(pbrand.getCreditNow90()));//当前透支额度
        }catch (Exception e)
        {
            tb.setCreditNow90(0);
        }

        try {
            tb.setCommission(BigDecimal.valueOf(Double.valueOf(pbrand.getCommission())));//久零佣金百分点
        }catch (Exception e)
        {
            tb.setCommission(BigDecimal.valueOf(0));
        }

        try {
            tb.setProcedures(BigDecimal.valueOf(Double.valueOf(pbrand.getProcedures())));//兴业银行手续费百分点
        }catch (Exception e)
        {
            tb.setProcedures(BigDecimal.valueOf(0));
        }
        try {
            tb.setProportion(Double.valueOf(pbrand.getProportion()));//发券比例
        }catch (Exception e)
        {
            tb.setProportion(1.0);
        }

        try {
            tb.setCoinproportion(Double.valueOf(pbrand.getCoinproportion()));//发券比例
        }catch (Exception e)
        {
            tb.setCoinproportion(0.0);
        }

        brandDao.save(tb);
        //保存完商户，需要保存用户
        pbrand.setBrandCode(tb.getBrandCode());
        pbrand.setPwd(MD5.md5(pbrand.getPwd()));
        pbrand.setUserCode(UUID.randomUUID().toString().replace("-", ""));
        dao.save("userDao.saveBrandUser",pbrand);
        Map<String,String>   userRole = new HashMap();
        userRole.put("code", UUID.randomUUID().toString());
        userRole.put("userId",pbrand.getUserCode());
        userRole.put("roleId",pbrand.getRoleCode());
        dao.save("roleDao.saveUserRole",userRole);
    }

    @Override
    public void updateMerchant(Pbrand pbrand) throws Exception {
        TBaseBrand tb=brandDao.getById(TBaseBrand.class,pbrand.getBrandCode());
        BeanUtils.copyProperties(pbrand,tb, "isdel","agentCode","createTime","islock","balance90","creditTotal90","creditNow90","commission","procedures","is90","pageviews","sort","proportion","isPeriodization","sorts");
        try {
            tb.setSorts(Integer.valueOf(pbrand.getSorts()));
        }
        catch (Exception e)
        {}
        try {
            tb.setBalance90experience(Long.valueOf(pbrand.getBalance90_experience()));
            tb.setCreditTotal90experience(Integer.valueOf(pbrand.getCreditTotal90_experience()));
            tb.setCreditNow90experience(Integer.valueOf(pbrand.getCreditNow90_experience()));
        }
        catch (Exception e)
        {
            tb.setBalance90experience((long) 0);
            tb.setCreditTotal90experience(0);
            tb.setCreditNow90experience(0);}
        try {
            tb.setBalance90shop(Long.valueOf(pbrand.getBalance90_shop()));
            tb.setCreditTotal90shop(Integer.valueOf(pbrand.getCreditTotal90_shop()));
            tb.setCreditNow90shop(Integer.valueOf(pbrand.getCreditNow90_shop()));
        }
        catch (Exception e)
        {
            tb.setBalance90shop(Long.valueOf(0));
            tb.setCreditTotal90shop(0);
            tb.setCreditNow90shop(0);
        }
        if(!StringUtil.isNullOrEmpty(pbrand.getIs90())&&"on".equals(pbrand.getIs90()))//判断是否是体验店
        {
            tb.setIs90((short) 1);
        }else
        {tb.setIs90((short) 0);}

        //判断是否送贝
        if(StringUtil.isNullOrEmpty(pbrand.getIscoin())){
            tb.setIscoin((short) 0);
        }else if("on".equals(pbrand.getIscoin())){
            tb.setIscoin((short) 1);
        }else{
            tb.setIscoin((short) 0);
        }

        //判断是否发券
        if(StringUtil.isNullOrEmpty(pbrand.getIsTicket())){
            tb.setIsTicket((short) 0);
        }else if("on".equals(pbrand.getIsTicket().toString())){
            tb.setIsTicket((short) 1);
        }else{
            tb.setIsTicket((short) 0);
        }

        //判断是否送贝
        if(StringUtil.isNullOrEmpty(pbrand.getIsZeroCheck())){
            tb.setIsZeroCheck((short) 0);
        }else if("on".equals(pbrand.getIsZeroCheck())){
            tb.setIsZeroCheck((short) 1);
        }else{
            tb.setIsZeroCheck((short) 0);
        }

        //判断是否分期
        if(StringUtil.isNullOrEmpty(pbrand.getIsPeriodization())){
            tb.setIsPeriodization((short) 0);
        }else if("1".equals(pbrand.getIsPeriodization())){
            tb.setIsPeriodization((short) 1);
        }else{
            tb.setIsPeriodization((short) 0);
        }

        try {
            tb.setBalance90(Long.valueOf(pbrand.getBalance90()));//90余额
        }catch (Exception e)
        {
            tb.setBalance90((long) 0);
        }

        try {
            tb.setCreditTotal90(Integer.valueOf(pbrand.getCreditTotal90()));//透支额度
        }catch (Exception e)
        {
            tb.setCreditTotal90(0);
        }
        try {
            tb.setCreditNow90(Integer.valueOf(pbrand.getCreditNow90()));//当前透支额度
        }catch (Exception e)
        {
            tb.setCreditNow90(0);
        }

        try {
            tb.setCommission(BigDecimal.valueOf(Double.valueOf(pbrand.getCommission())));//久零佣金百分点
        }catch (Exception e)
        {
            tb.setCommission(BigDecimal.valueOf(0));
        }
        try {
            tb.setProcedures(BigDecimal.valueOf(Double.valueOf(pbrand.getProcedures())));//兴业银行手续费百分点
        }catch (Exception e)
        {
            tb.setProcedures(BigDecimal.valueOf(0));
        }

        try {
            tb.setProportion(Double.valueOf(pbrand.getProportion()));//发券比例
        }catch (Exception e)
        {
            tb.setProportion(1.0);
        }

        try {
            tb.setCoinproportion(Double.valueOf(pbrand.getCoinproportion()));//发券比例
        }catch (Exception e)
        {
            tb.setCoinproportion(0.0);
        }
        baseDao.update(tb);
    }

    @Override
    public Pbrand findById(String id) throws Exception {
        return (Pbrand) dao.findForObject("brandMapper.findById",id);
    }

    @Override
    public Paging findBrandStatisticsGrid(Params sqlParams) throws Exception {
        return dao.findForPaging("brandMapper.findBrandStatisticsPage",sqlParams,"brandMapper.countBrandStatistics",sqlParams);
    }

    @Override
    public void savePeriodization(String cycleDays,String scale,String brand_code,String userId) throws Exception {
        List<Map<String,Object>> params = new ArrayList<Map<String,Object>>();
        String[] sacles = scale.split(",");
        for(int i=0;i<sacles.length;i++){
            Map<String,Object> map = new HashMap<>();
            map.put("id", UuidUtil.get32UUID());
            map.put("termNum",i+1);
            map.put("scale",Double.valueOf(sacles[i])/100.0);
            map.put("cycleDays",cycleDays);
            map.put("brandCode",brand_code);
            map.put("userId",userId);
            params.add(map);
        }
        dao.save("brandMapper.savePeriodization",params);
    }

    @Override
    public void updatePeriodization(String brandCode, String userId) throws Exception {
        Map<String,Object> map =new HashMap<>();
        map.put("brandCode",brandCode);
        map.put("userId",userId);
        dao.update("brandMapper.updatePeriodization",map);
    }

    @Override
    public List<Map<String, Object>> findPeriodizationByBrandId(String id) throws Exception {
        return (List<Map<String, Object>>)dao.findForList("brandMapper.findPeriodizationByBrandId",id);
    }


}
