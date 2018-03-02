package com.biz.service.basic;

import com.biz.model.Hmodel.basic.TBaseBrand;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.BaseLog;
import com.biz.model.Pmodel.basic.Recharge90;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by ldd_person on 2017/2/9.
 */
@Service("recharge90Service")
public class Recharge90ServiceImpl extends BaseServiceImpl<Recharge90> implements Recharge90ServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Autowired
    private BaseDaoI baseDao;
    @Autowired
    private BaseDaoI<TBaseBrand> brandDao;
    /**
     * 查询
     *
     * @return
     * @throws Exception
     */
    @Override
    public Pager<Recharge90> queryRecharge90ListForPage(Pager pager) throws Exception {
        return dao.findForPager1("Recharge90Dao.queryRecharge90ListForPage", "queryRecharge90Count",pager);
    }

    /**
     * 查询
     *
     * @return
     * @throws Exception
     */
    @Override
    public List queryRecharge90List(Map<String,Object> param) throws Exception {
        return (List) dao.findForList("Recharge90Dao.queryRecharge90List", param);
    }

    @Override
    public void saveRecharge90(Recharge90 recharge90, User user, HttpServletRequest request) throws Exception {

    TBaseBrand brand=brandDao.getById(TBaseBrand.class,recharge90.getBrand_code());
        StringBuilder sb = new StringBuilder("新增充值记录，品牌：brand_code:").append(brand.getBrandCode())
                .append(",").append("total_90:").append(recharge90.getTotal_90())
                .append(",credit_now_90:").append(brand.getCreditNow90()).append("balance_90:").append(brand.getBalance90());
        recharge90.setAgent_code(brand.getAgentCode());
        recharge90.setUser_code(user.getId());
        int point90=(int)(Double.valueOf(recharge90.getTotal90())*100);
        recharge90.setTotal_90(point90);
        int total_rmb=(int)(Double.valueOf(recharge90.getTotalrmb())*100);
        recharge90.setTotal_rmb(total_rmb);
        long credit_now_90 =0;
        if("0".equals(recharge90.getTicketType()))
        { credit_now_90=brand.getCreditNow90();}
        else if("1".equals(recharge90.getTicketType()))
        { credit_now_90=brand.getCreditNow90shop();}
        else if("2".equals(recharge90.getTicketType()))
        { credit_now_90=brand.getCreditNow90experience();}
        if (credit_now_90==0)//未透支  直接更新余额
        {
            if("0".equals(recharge90.getTicketType())) {
                brand.setBalance90(brand.getBalance90() + point90);
                recharge90.setTotalNow_90(new Long(brand.getBalance90()).intValue());
                sb.append(",未透支  直接更新余额,更新后balance90:" + (brand.getBalance90() + point90));
            }else if("1".equals(recharge90.getTicketType()))
            { brand.setBalance90shop(brand.getBalance90shop() + point90);
                recharge90.setTotalNow_90(new Long(brand.getBalance90shop()).intValue());
                sb.append(",未透支  直接更新余额,更新后balance90:" + (brand.getBalance90shop() + point90));}
            else if("2".equals(recharge90.getTicketType()))
            {
                brand.setBalance90experience(brand.getBalance90experience() + point90);
                recharge90.setTotalNow_90(new Long(brand.getBalance90experience()).intValue());
                sb.append(",未透支  直接更新余额,更新后balance90:" + (brand.getBalance90experience() + point90));
            }
        }else if(credit_now_90>0) {//先减透支，再更新余额
            if (credit_now_90>=point90)//充值的钱小于等于透支的钱   只更新透支额，不更新余额
            {
                if("0".equals(recharge90.getTicketType())) {
                    brand.setCreditNow90(brand.getCreditNow90()-point90);
                    recharge90.setTotalNow_90(-brand.getCreditNow90());
                    sb.append(",充值的钱小于等于透支的钱   只更新透支额，不更新余额，更新后credit_now_90:"+(credit_now_90-point90));

                }
                else if("1".equals(recharge90.getTicketType()))
                {brand.setCreditNow90shop(brand.getCreditNow90shop()-point90);
                    recharge90.setTotalNow_90(-brand.getCreditNow90shop());
                    sb.append(",充值的钱小于等于透支的钱   只更新透支额，不更新余额，更新后credit_now_90:"+(credit_now_90-point90));
                }else if("2".equals(recharge90.getTicketType()))
                {brand.setCreditNow90experience(brand.getCreditNow90experience()-point90);
                    recharge90.setTotalNow_90(-brand.getCreditNow90experience());
                    sb.append(",充值的钱小于等于透支的钱   只更新透支额，不更新余额，更新后credit_now_90:"+(credit_now_90-point90));
                }

            }else {
                if("0".equals(recharge90.getTicketType())) {
                    brand.setBalance90(brand.getBalance90()+point90-credit_now_90);
                    recharge90.setTotalNow_90(new Long(brand.getBalance90()).intValue());
                    brand.setCreditNow90(0);
                    sb.append(",充值的钱大于透支的钱   既更新透支额，也更新余额，更新后credit_now_90:0，balance90:"+(brand.getBalance90()+(point90-credit_now_90)));

                }
                else if("1".equals(recharge90.getTicketType()))
                {  brand.setBalance90shop(brand.getBalance90shop()+point90-credit_now_90);
                    recharge90.setTotalNow_90(new Long(brand.getBalance90shop()).intValue());
                    brand.setCreditNow90shop(0);
                    sb.append(",充值的钱大于透支的钱   既更新透支额，也更新余额，更新后credit_now_90:0，balance90:"+(brand.getBalance90shop()+(point90-credit_now_90)));
                }else if("2".equals(recharge90.getTicketType()))
                {  brand.setBalance90experience(brand.getBalance90experience()+point90-credit_now_90);
                    recharge90.setTotalNow_90(new Long(brand.getBalance90experience()).intValue());
                    brand.setCreditNow90experience(0);
                    sb.append(",充值的钱大于透支的钱   既更新透支额，也更新余额，更新后credit_now_90:0，balance90:"+(brand.getBalance90experience()+(point90-credit_now_90)));
                }

            }

        }
        brandDao.update(brand);
        dao.save("Recharge90Dao.insertRecharge90", recharge90);
        BaseLog bl = new BaseLog();
        bl.setDetail(sb.toString());
        bl.setIdentity_code(user.getIdentity_code());
        bl.setUser_name(user.getPersonName());
        bl.setIdentity(user.getIdentity());
        bl.setIp(getVistIP(request));
        bl.setModule_type("新增");
        bl.setModule_name("久零券充值记录");
        bl.setTable_name("recharge_90");
        bl.setTable_key(recharge90.getCode());
        dao.save("Recharge90Dao.insertBaseLog", bl);
    }
    /**
     * 获取登录用户的IP
     *
     * @throws Exception
     */
    public String getVistIP(HttpServletRequest request) throws Exception {
        String ip = "";
        if (request.getHeader("x-forwarded-for") == null) {
            ip = request.getRemoteAddr();
        } else {
            ip = request.getHeader("x-forwarded-for");
        }
        return ip;
    }
}
