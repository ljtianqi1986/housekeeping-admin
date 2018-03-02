package com.biz.model.Pmodel;

/**
 * Created by Administrator on 2017/2/4.
 */
public class PBaseUser {
    /**
     * 用户的id
     */
    private String id;
    /**
     * 用户的open_id
     */
    private String open_id;
    /**
     * 用户昵称
     */
    private String person_name;
    /**
     * 值为1时是男性，值为2时是女性，值为0时是未知
     */
    private int sex = 0;
    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 用户头像
     */
    private String cover;
    /**
     * 代金券余额
     */
    private double balance_cash;
    /**
     * 90券余额
     */
    private double balance_90;
    /**
     * 0:取消关注1:关注
     */
    private int state = 1;
    /**
     * 国家
     */
    private String country;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 生日
     */
    private String birthday;
    /**
     * 第一次扫码后绑定的支付宝id
     */
    private String scan_ali_id;
    /**
     * 第一次扫码后绑定的易支付的id
     */
    private String scan_yi_id;
    /**
     * 第一次扫码后绑定的银行卡的id
     */
    private String scan_yhk_id;
    /**
     * 兴业的open_id
     */
    private String xy_openid;
    /**
     * 删除标志0：未删除，1：删除
     */
    private int isdel = 0;
    /**
     * 创建时间
     */
    private String create_time;
    /**
     * 付款码 每分钟更新  唯一标识
     */
    private String only_code;
    /**
     * 90券总额
     */
    private double balance_90_total;
    /**
     * @author zhengXin 用户地理位置纬度
     */
    private String user_latitude;
    /**
     * @author zhengXin 用户地理位置经度
     */
    private String user_longitude;
    /**
     * 久零券余额是否正常
     */
    private String isBalance90Wrong;
    /***********************扩展字段开始***************************/

    /**
     * 久零券发放金额
     */
    private int point90;

    /**
     * 业务员名称
     */
    private String biz_person_name;

    private int gzpoint90=0;

    private String chargeAmount;
    private double totalAmount;

    private String unionId;

    private String business_name;

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    /***********************扩展字段结束***************************/
    public int getGzpoint90() {
        return gzpoint90;
    }

    public void setGzpoint90(int gzpoint90) {
        this.gzpoint90 = gzpoint90;
    }

    public void setOpen_id(String open_id){
        this.open_id = open_id;
    }

    public String getOnly_code()
    {
        return only_code;
    }

    public void setOnly_code(String only_code)
    {
        this.only_code = only_code;
    }

    public String getOpen_id(){
        return this.open_id;
    }

    public void setPerson_name(String person_name){
        this.person_name = person_name;
    }

    public String getPerson_name(){
        return this.person_name;
    }

    public void setSex(int sex){
        this.sex = sex;
    }

    public int getSex(){
        return this.sex;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getPhone(){
        return this.phone;
    }

    public void setCover(String cover){
        this.cover = cover;
    }

    public String getCover(){
        return this.cover;
    }

    public double getBalance_cash() {
        return balance_cash;
    }

    public void setBalance_cash(double balance_cash) {
        this.balance_cash = balance_cash;
    }

    public double getBalance_90() {
        return balance_90;
    }

    public void setBalance_90(double balance_90) {
        this.balance_90 = balance_90;
    }

    public void setState(int state){
        this.state = state;
    }

    public int getState(){
        return this.state;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public String getCountry(){
        return this.country;
    }

    public void setProvince(String province){
        this.province = province;
    }

    public String getProvince(){
        return this.province;
    }

    public void setCity(String city){
        this.city = city;
    }

    public String getCity(){
        return this.city;
    }

    public void setBirthday(String birthday){
        this.birthday = birthday;
    }

    public String getBirthday(){
        return this.birthday;
    }

    public void setScan_ali_id(String scan_ali_id){
        this.scan_ali_id = scan_ali_id;
    }

    public String getScan_ali_id(){
        return this.scan_ali_id;
    }

    public void setScan_yi_id(String scan_yi_id){
        this.scan_yi_id = scan_yi_id;
    }

    public String getScan_yi_id(){
        return this.scan_yi_id;
    }

    public void setScan_yhk_id(String scan_yhk_id){
        this.scan_yhk_id = scan_yhk_id;
    }

    public String getScan_yhk_id(){
        return this.scan_yhk_id;
    }

    public String getXy_openid() {
        return xy_openid;
    }

    public void setXy_openid(String xy_openid) {
        this.xy_openid = xy_openid;
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

    public double getBalance_90_total() {
        return balance_90_total;
    }

    public void setBalance_90_total(double balance_90_total) {
        this.balance_90_total = balance_90_total;
    }

    public String getUser_latitude() {
        return user_latitude;
    }

    public void setUser_latitude(String user_latitude) {
        this.user_latitude = user_latitude;
    }

    public String getUser_longitude() {
        return user_longitude;
    }

    public void setUser_longitude(String user_longitude) {
        this.user_longitude = user_longitude;
    }

    public int getPoint90() {
        return point90;
    }

    public void setPoint90(int point90) {
        this.point90 = point90;
    }

    public String getBiz_person_name() {
        return biz_person_name;
    }

    public void setBiz_person_name(String biz_person_name) {
        this.biz_person_name = biz_person_name;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getIsBalance90Wrong() {
        return isBalance90Wrong;
    }

    public void setIsBalance90Wrong(String isBalance90Wrong) {
        this.isBalance90Wrong = isBalance90Wrong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }
}
