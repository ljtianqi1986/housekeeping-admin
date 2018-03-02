package com.biz.model.Pmodel;

/*************************************************************************
 * 文件名称 ：PorderDetail.java                   
 * 描述说明 ：
 * 
 * 创建信息 : create by ldd_person on 上午9:00:10
 * 修订信息 : modify by ( ) on (date) for ( )
 * 
 * 版权信息 : Copyright (c) 2015 扬州北斗科技有限公司 
 **************************************************************************/

/**
 *支付订单
 */
public class PorderMainUnion{
    /**
     * 对应店小翼的order_code,90开头
     */
    private String code;
    /**
     * 订单应支付额
     */
    private double order_total = 0.00;
    /**
     * 现金支付金额
     */
    private double cash_total = 0.00;
    /**
     * 优惠券产生的优惠金额
     */
    private double card_total = 0.00;
    /**
     * 所用优惠券张数
     */
    private int card_count = 0;
    /**
     * 个人open_id
     */
    private String open_id;
    /**
     * 营业员
     */
    private String user_code;
    /**
     * 门店主键
     */
    private String shop_code;
    /**
     * 品牌主键
     */
    private String brand_code;
    /**
     * 0:未生效1:交易成功2:错误3:已退款
     */
    private int state = 0;
    /**
     * 支付时间
     */
    private String pay_time;
    /**
     * 退款单号
     */
    private String back_code;
    /**
     * 退款时间
     */
    private String back_time;
    /**
     * 退款操作人
     */
    private String back_user_code;
    /**
     * 错误信息
     */
    private String error_pay_msg;
    /**
     * 支付类型 MICROPAY:刷卡支付 NATIVE:扫码支付 JSAPI:公众号支付
     */
    private String trade_type;
    /**
     * 赠送的90券
     */
    private double gift_90;
    /**
     * 久零佣金百分点
     */
    private double commission;
    /**
     * 兴业银行手续费百分点
     */
    private double procedures = 0.60;
    /**
     * 0:未结账1已结账
     */
    private int is_clean = 0;
    /**
     * 0:未知 1：微信 2：支付宝 3：易支付 4:银联卡，5：银联（新） 6：拉卡拉（新）
     */
    private int pay_type = 0;
    /**
     * 微信的open_id/支付宝的id/翼支付的id
     */
    private String pay_user_id;
    /**
     * 删除标志0：未删除，1：删除
     */
    private int isdel = 0;
    /**
     * 创建时间
     */
    private String create_time;
    /**
     * @author zhengXin 2016-11-02
     * 退款前原始订单应收金额
     */
    private double old_order_total = 0.00;
    /**
     * @author zhengXin 2016-11-02
     * 退款前原始订单赠送90券
     */
    private double old_gift_90;

    //0:平台券90券，1：特殊券 购物券
    private int balance_type=0;

    /***********************扩展字段开始***************************/
    /**
     * 营业员名称
     */
    private String person_name;
    /**
     * 消费者昵称
     */
    private String custom_name;
    /**
     * 商户名字
     */
    private String brand_name;

    private String business_name;

    private String agent_name;

    private String card_fee;

    private String realCard_fee;

    private String pay_coin;

    private double coin_90=0.0;//pos机付款，赠送的90贝



    /***********************扩展字段结束***************************/


    public double getCoin_90() {
        return coin_90;
    }

    public void setCoin_90(double coin_90) {
        this.coin_90 = coin_90;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public String getCode(){
        return this.code;
    }

    public void setOrder_total(double order_total){
        this.order_total = order_total;
    }

    public double getOrder_total(){
        return this.order_total;
    }

    public void setCash_total(double cash_total){
        this.cash_total = cash_total;
    }

    public double getCash_total(){
        return this.cash_total;
    }

    public void setCard_total(double card_total){
        this.card_total = card_total;
    }

    public double getCard_total(){
        return this.card_total;
    }

    public void setCard_count(int card_count){
        this.card_count = card_count;
    }

    public int getCard_count(){
        return this.card_count;
    }

    public void setOpen_id(String open_id){
        this.open_id = open_id;
    }

    public String getOpen_id(){
        return this.open_id;
    }

    public void setUser_code(String user_code){
        this.user_code = user_code;
    }

    public String getUser_code(){
        return this.user_code;
    }

    public void setShop_code(String shop_code){
        this.shop_code = shop_code;
    }

    public String getShop_code(){
        return this.shop_code;
    }

    public void setBrand_code(String brand_code){
        this.brand_code = brand_code;
    }

    public String getBrand_code(){
        return this.brand_code;
    }

    public void setState(int state){
        this.state = state;
    }

    public int getState(){
        return this.state;
    }

    public void setPay_time(String pay_time){
        this.pay_time = pay_time;
    }

    public String getPay_time(){
        return this.pay_time;
    }

    public void setBack_code(String back_code){
        this.back_code = back_code;
    }

    public String getBack_code(){
        return this.back_code;
    }

    public void setBack_time(String back_time){
        this.back_time = back_time;
    }

    public String getBack_time(){
        return this.back_time;
    }

    public void setBack_user_code(String back_user_code){
        this.back_user_code = back_user_code;
    }

    public String getBack_user_code(){
        return this.back_user_code;
    }

    public void setError_pay_msg(String error_pay_msg){
        this.error_pay_msg = error_pay_msg;
    }

    public String getError_pay_msg(){
        return this.error_pay_msg;
    }

    public void setTrade_type(String trade_type){
        this.trade_type = trade_type;
    }

    public String getTrade_type(){
        return this.trade_type;
    }

    public void setGift_90(double gift_90){
        this.gift_90 = gift_90;
    }

    public double getGift_90(){
        return this.gift_90;
    }

    public void setCommission(double commission){
        this.commission = commission;
    }

    public double getCommission(){
        return this.commission;
    }

    public void setProcedures(double procedures){
        this.procedures = procedures;
    }

    public double getProcedures(){
        return this.procedures;
    }

    public void setIs_clean(int is_clean){
        this.is_clean = is_clean;
    }

    public int getIs_clean(){
        return this.is_clean;
    }

    public void setPay_type(int pay_type){
        this.pay_type = pay_type;
    }

    public int getPay_type(){
        return this.pay_type;
    }

    public void setPay_user_id(String pay_user_id){
        this.pay_user_id = pay_user_id;
    }

    public String getPay_user_id(){
        return this.pay_user_id;
    }

    public void setIsdel(int isdel){
        this.isdel = isdel;
    }

    public int getIsdel(){
        return this.isdel;
    }

    public void setCreate_time(String create_time){
        this.create_time = create_time;
    }

    public String getCreate_time(){
        return this.create_time;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getCustom_name() {
        return custom_name;
    }

    public void setCustom_name(String custom_name) {
        this.custom_name = custom_name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getCard_fee() {
        return card_fee;
    }

    public void setCard_fee(String card_fee) {
        this.card_fee = card_fee;
    }

    public double getOld_order_total() {
        return old_order_total;
    }

    public void setOld_order_total(double old_order_total) {
        this.old_order_total = old_order_total;
    }

    public double getOld_gift_90() {
        return old_gift_90;
    }

    public void setOld_gift_90(double old_gift_90) {
        this.old_gift_90 = old_gift_90;
    }

    public String getRealCard_fee() {
        return realCard_fee;
    }

    public void setRealCard_fee(String realCard_fee) {
        this.realCard_fee = realCard_fee;
    }

    public String getPay_coin() {
        return pay_coin;
    }

    public void setPay_coin(String pay_coin) {
        this.pay_coin = pay_coin;
    }

    public int getBalance_type() {
        return balance_type;
    }

    public void setBalance_type(int balance_type) {
        this.balance_type = balance_type;
    }
}
