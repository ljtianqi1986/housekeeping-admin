package com.biz.service.api;

import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Pmodel.PBaseUser;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.*;
import com.biz.model.Pmodel.basic.Base90Detail;
import com.biz.model.Pmodel.offlineCard.OfflineCardDetail;
import com.biz.model.Pmodel.offlineCard.PofflineCard;
import com.biz.service.base.BaseServiceI;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lzq on 2017/2/4.
 */
public interface ApiUserServiceI extends BaseServiceI<TorderMain> {

List<HashMap<String,Object>> queryClerkByShopWithPhone(String shop_id) throws Exception;

    /**
     * 根据登录名判断是否重复
     * @param login_name
     * @return
     * @throws Exception
     */
    int findLoginName(String login_name) throws  Exception;

    /**
     * 查询手机号是否重复
     * @param phone
     * @return
     * @throws Exception
     */
    int getUserCountByPhone(String phone)throws Exception;

    /**
     * 新增店员
     * @param clerk_user
     * @throws Exception
     */
    void insertShopUser(User clerk_user) throws Exception;

    /**
     * 锁定店员
     * @param clerk_user
     * @throws Exception
     */
    void updateClerkUserLock(User clerk_user) throws Exception;

    /**
     * 删除店员
     * @param user_code
     * @return
     * @throws Exception
     */
    String delClerkByCode(String user_code) throws Exception;

    /**
     * 通过用户id获取用户
     * @param user_code
     * @return
     * @throws Exception
     */
    User getUserByCode(String user_code) throws Exception;

    /**
     * 修改用户手机号
     * @param user
     * @throws Exception
     */
    void updateUserPhone(User user) throws Exception;

    /**
     * 修改用户密码
     * @param user
     * @throws Exception
     */
    void updateUserPwd(User user) throws Exception;

    OrderMain90 getOrderMain90ByCode(String order_code) throws  Exception;

    String getSysUserIdByOpenId(String open_id) throws Exception;

    void insertOrderMain90(OrderMain90 orderMain90) throws Exception;

    BaseUser getBaseUserByunionId(String unionId) throws Exception;

    void send_pay_template(String open_id, String code, double pay, String pay_time)throws Exception;

    BaseUser getBaseUserByPhone(String phone) throws Exception;

    Brand getBrandOnlyByCode(String brand_code)throws Exception;

    void insertRgGift(RgGift rgGift) throws Exception;

    void add_balance_90(Map<String, Object> map) throws Exception;

    void minus_balance_90(Map<String, Object> map)throws Exception;

    void add_credit_now_90(Map<String, Object> map) throws Exception;

    void insertBase90Detail(Base90Detail base90Detail) throws Exception;

    void updateWhereRgGift(Map<String, Object> map) throws Exception;

    RgGift getRgGiftByCode(String rg_code)throws Exception;

    void send_kf_template(String open_id, String content)throws Exception;

    void kf_reply_news(String open_id, String title, String content, String cover, String url)throws Exception;

    JSONObject oper_balance_90ByWX(Map<String, Object> map)throws Exception;

    void add_auto_balance(String brand_code, String shop_code, String source_code, String open_id, double balance_90, String user_code)throws Exception;

    void add_rg_balance(String brand_code, String shop_code, String source_code, String open_id, double balance_90, String user_code)throws Exception;

    void add_balance_90(String open_id, double balance_90)throws Exception;

    void send_pay_template(int scene_id, String open_id,
                           OrderMainUnion orderMainUnion)throws Exception;

    OrderMainUnion getOrderMainUnionByCode(String orderMainId)throws Exception;

    boolean addActivityListDate(String openid)throws Exception;

    JSONObject sign_addBalance(Map<String, Object> map)throws Exception;

    OfflineCardDetail getOfflineCardDetailByCard_code(String card_code)throws Exception;

    PofflineCard getOfflineCardByCode(String main_code)throws Exception;

    void updateWhereOfflineCardDetail(Map<String, Object> map) throws Exception;

    void add_card_use_count(String main_code)throws Exception;

    List<Base90Detail> selectOrtherBalanceIn(Map<String, Object> map)throws Exception;

    String int_sum(Map<String, Object> map)throws Exception;

    List<Base90Detail> selectOrtherBalanceOut(Map<String, Object> map)throws Exception;

    String out_sum(Map<String, Object> map) throws Exception;

    void updateBaseUserOnlyCode(Map<String, Object> map)throws Exception;

    List<Base90Detail>selectWhereBase90Detail(Map<String, Object> map)throws Exception;

    List<OrderMain90> selectWhereOrderMain90(Map<String, Object> map)throws Exception;

    Map<String,Object> selectWhereOrderMain90Count(Map<String, Object> map)throws Exception;

    int getCountOrderMain90(Map<String, Object> map)throws Exception;

    void checkUserCoin(Map<String, Object> map)throws Exception;

    JSONObject oper_coin_90(Map<String, Object> map)throws Exception;

    List<String> getUserBalanceDetailListByOpenId(String open_id)throws Exception;
    PBaseUser getUserByUnionId(String unionId)throws Exception;

    void giveCoinByCardId(PBaseUser user, String card_code)throws Exception;

    void updateBsseUserPhone(User user)throws Exception;
}
