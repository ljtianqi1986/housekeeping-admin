package com.biz.service.api;

import com.biz.model.Pmodel.api.Brand;
import com.biz.model.Pmodel.api.PayScene;
import com.biz.model.Pmodel.api.RgGift;
import com.biz.model.Pmodel.api.Shop;
import com.biz.model.Singleton.ZkNode;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.MathUtil;
import com.framework.utils.StringUtil;
import com.framework.utils.UuidUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lzq on 2017/2/4.
 */

@Service("ticketService")
public class TicketServiceImpl extends BaseServiceImpl implements TicketServiceI {
    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Resource(name = "phoneUserService")
    private PhoneUserServiceI phoneUserService;

    @Resource(name = "wxQrCodeService")
    private WxQrCodeServiceI wxQrCodeService;

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> phone_gift_rg_tyd_public(String shop_code, String user_code, String total, String gift_type, String card_code, String memo) throws Exception {
        Map<String, Object> jSONObject = new HashMap<String, Object>();
        try {
            if (StringUtils.isBlank(total)) {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", "金额不正确！");
                return jSONObject;
            }
            Shop shop = (Shop) dao.findForObject("OrderMain90Dao.getShopBySid", shop_code);
            if (shop == null) {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", "门店不存在！");
                return jSONObject;
            }
            String brand_code = shop.getBrand_code();
            Brand brand = (Brand) dao.findForObject("QTTicketDao.getBrandOnlyByCode", brand_code);
            if (brand == null) {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", "商户不存在！");
                return jSONObject;
            }
            //***********对传入金额处理*************//*
            double balance_90 = 0d;//分
            try {
                balance_90 = money_convert(total);
            } catch (Exception e) {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", "金额错误，金额为" + total);
                return jSONObject;
            }
            long brand_balance_90 = brand.getBalance_90();//九零券余额（分）
            long credit_total_90 = brand.getCredit_total_90();//久零券透支额度（分）
            long credit_now_90 = brand.getCredit_now_90();//久零券当前透支额度（分）
            if ((brand_balance_90 + credit_total_90 - credit_now_90) < balance_90) {
                jSONObject.put("return_code", 0);
                int edu = (int) (credit_now_90 / 100);
                jSONObject.put("return_info", "商户额度不够，当前可用透支额度" + edu);
                return jSONObject;
            }
            //人工发券记录
            RgGift rgGift = new RgGift();
            String code = String.valueOf(getRndCode());
            rgGift.setCode(code);
            rgGift.setBrand_code(brand_code);
            rgGift.setShop_code(shop_code);
            rgGift.setUser_code(user_code);
            rgGift.setPoint_90(balance_90);
            rgGift.setState(0);
            rgGift.setTypeInt(1);//插入类别  0：pos机  1：qt
            rgGift.setCard_code(card_code);
            rgGift.setMemo(memo);
            if (StringUtil.isNullOrEmpty(gift_type))//发券类型
            {
                rgGift.setGift_type(0);
            } else {
                rgGift.setGift_type(Integer.valueOf(gift_type));
            }
            phoneUserService.insertRgGift(rgGift);
            //场景值
            PayScene payScene = new PayScene();
            payScene.setMain_code(code);
            payScene.setScene_type(1);
            if("3".equals(gift_type))
            {
                payScene.setTicketType("2");
            }else if ("4".equals(gift_type))
            {
                payScene.setTicketType("1");
            }else
            {payScene.setTicketType("0");}
            phoneUserService.insertPayScene(payScene);
            int scene_id = payScene.getId();
            //关注二维码
            String url = wxQrCodeService.getTempQrCode(scene_id);
            jSONObject.put("return_code", 1);
            jSONObject.put("return_info", url);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (StringUtils.isNotBlank(msg) && msg.length() > 30) {
                msg = msg.substring(0, 29);
            }
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", "错误-" + msg);
        }
        return jSONObject;
    }

    @Override
    public Map<String, Object> phone_gift_rg_public(String shop_code, String user_code, String total, String ticketType) throws Exception {
        Map<String, Object> jSONObject = new HashMap<String, Object>();
        try {
            if (StringUtils.isBlank(total)) {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", "金额不正确！");
                return jSONObject;
            }
            Shop shop = (Shop) dao.findForObject("OrderMain90Dao.getShopBySid", shop_code);
            if (shop == null) {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", "门店不存在！");
                return jSONObject;
            }
            String brand_code = shop.getBrand_code();
            Brand brand = (Brand) dao.findForObject("QTTicketDao.getBrandOnlyByCode", brand_code);
            if (brand == null) {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", "商户不存在！");
                return jSONObject;
            }


            //***********对传入金额处理*************//*
            double balance_90 = 0d;//分
            try {
                balance_90 = money_convert(total);
            } catch (Exception e) {
                jSONObject.put("return_code", 0);
                jSONObject.put("return_info", "金额错误，金额为" + total);
                return jSONObject;
            }
            long brand_balance_90 =0;//九零券余额（分）
            long credit_total_90 = 0;//久零券透支额度（分）
            long credit_now_90 =0;//久零券当前透支额度（分）
            if(StringUtil.isNullOrEmpty(ticketType)||"0".equals(ticketType)){
                 brand_balance_90 = brand.getBalance_90();//九零券余额（分）
                 credit_total_90 = brand.getCredit_total_90();//久零券透支额度（分）
                 credit_now_90 = brand.getCredit_now_90();//久零券当前透支额度（分）
            }else if("1".equals(ticketType)){
                 brand_balance_90 = brand.getBalance_90_shop();//九零券余额（分）
                 credit_total_90 = brand.getCredit_total_90_shop();//久零券透支额度（分）
                 credit_now_90 = brand.getCredit_now_90_shop();//久零券当前透支额度（分）
            }else if("2".equals(ticketType)){
                 brand_balance_90 = brand.getBalance_90_experience();//九零券余额（分）
                 credit_total_90 = brand.getCredit_total_90_experience();//久零券透支额度（分）
                 credit_now_90 = brand.getCredit_now_90_experience();//久零券当前透支额度（分）

            }
            if ((brand_balance_90 + credit_total_90 - credit_now_90) < balance_90) {
                jSONObject.put("return_code", 0);
                int edu = (int) (credit_now_90 / 100);
                jSONObject.put("return_info", "商户额度不够，当前可用透支额度" + edu);
                return jSONObject;
            }

            //***********计算送90贝数*************//
            double coin_90_total=0.0;
            if(shop.getIscoin()==1){
                double bl = Double.parseDouble(ZkNode.getIstance().getJsonConfig().get("service_charge").toString());
                coin_90_total= MathUtil.mul(Double.parseDouble(total), bl);
            }

            //人工发券记录
            RgGift rgGift = new RgGift();
            String code = String.valueOf(getRndCode());
            rgGift.setCode(code);
            rgGift.setBrand_code(brand_code);
            rgGift.setShop_code(shop_code);
            rgGift.setUser_code(user_code);
            rgGift.setPoint_90(balance_90);
            rgGift.setState(0);
            rgGift.setTypeInt(0);
            rgGift.setCoin_90(coin_90_total);
            phoneUserService.insertRgGift(rgGift);
            //场景值
            PayScene payScene = new PayScene();
            payScene.setMain_code(code);
            payScene.setTicketType(ticketType);
            payScene.setScene_type(1);
            if(StringUtil.isNullOrEmpty(payScene.getTicketType())||!StringUtil.isNumeric(payScene.getTicketType()))
            {payScene.setTicketType("0");}
                phoneUserService.insertPayScene(payScene);
            int scene_id = payScene.getId();
            //关注二维码
            String url = wxQrCodeService.getTempQrCode(scene_id);
            jSONObject.put("return_code", 1);
            jSONObject.put("return_info", url);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (StringUtils.isNotBlank(msg) && msg.length() > 30) {
                msg = msg.substring(0, 29);
            }
            jSONObject.put("return_code", 0);
            jSONObject.put("return_info", "错误-" + msg);
        }
        return jSONObject;
    }

    /**
     * 字符元--转为整数分
     */
    public double money_convert(String money) {
        int int_m = money_convert_Int(money);
        double back_money = Double.valueOf(int_m);
        return back_money;
    }

    /**
     * 字符元--转为int分
     */
    public int money_convert_Int(String moneyStr) {
        int result = 0;
        String strAdd = "";
        if (!moneyStr.contains(".")) {
            strAdd = ".00";
        } else if (".".equals(moneyStr.substring(moneyStr.length() - 1))) {
            strAdd = "00";
        } else if (".".equals(moneyStr.substring(moneyStr.length() - 2, moneyStr.length() - 1))) {
            strAdd = "0";
        }
        moneyStr = moneyStr + strAdd;
        result = Integer.parseInt(moneyStr.replace(".", ""));
        return result;
    }

    /**
     * 得到随机数
     *
     * @return
     */
    public String getRndCode() {
        return UuidUtil.get32UUID();
    }

}
