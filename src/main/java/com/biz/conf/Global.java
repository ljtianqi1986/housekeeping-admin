package com.biz.conf;

import com.biz.model.Singleton.ZkNode;
import com.framework.utils.PropertiesLoader;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 全局配置类
 * Created by 刘佳佳 on 2016/9/22.
 */
public class Global {
    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = Maps.newHashMap();

    /**
     * 属性文件加载对象
     */
    private static PropertiesLoader loader = new PropertiesLoader("/conf/config.properties");


    /**
     * 获取管理端根路径
     */
    public static String getAdminPath() {
        return getConfig("adminPath");
    }

    /**
     * 通过键获取值
     *
     * @param key
     * @return
     */
    public static final String get(String key) {
        return loader.getProperty(key);
    }

    /**
     * 获取静态资源文件的地址
     * @return
     */
    public static String getStaticPath(){
        return getConfig("staticPath");
    }


    /**
     * 获取公司信息
     * @return
     */
    public static String getCompany(){
        return ZkNode.getIstance().getJsonConfig().get("company")+"";
    }

    /**
     * 获取当前版本信息
     * @return
     */
    public static String getVersion(){
        return ZkNode.getIstance().getJsonConfig().get("version")+"";
    }

    /**
     * 获取当前的省信息
     * @return
     */
    public static String getLocalProv(){
        return ZkNode.getIstance().getJsonConfig().get("localProv")+"";
    }

    /**
     * 获取当前的市信息
     * @return
     */
    public static String getLocalCity(){
        return ZkNode.getIstance().getJsonConfig().get("localCity")+"";
    }

    /**
     * 获取当前图片的url
     * @return
     */
    public static String getOSSURL(){
        return getConfig("OSSURL");
    }

    /**
     * 获取appId
     * @return
     */
    public static String getAppId(){
        return ZkNode.getIstance().getJsonConfig().get("appid")+"";
    }
    /**
     * 获取wharehoustId
     * @return
     */
    public static String getWharehoustId(){
        return ZkNode.getIstance().getJsonConfig().get("wharehouseId")+"";
    }
    /**
     * 获取当前JPG
     * @return
     */
    public static String getJPEG(){
        return getConfig("JPEG");
    }
    /**
     * 获取当前NPG
     * @return
     */
    public static String getPNG(){
        return getConfig("PNG");
    }

    /**
     * 获取pay17key
     * @return
     */
    public static String getPay17key(){
        return ZkNode.getIstance().getJsonConfig().get("pay17key")+"";
    }

    /**
     * 获取pay17shopcode
     * @return
     */
    public static String getPay17_shop_code(){
        return ZkNode.getIstance().getJsonConfig().get("pay17_shop_code")+"";
    }


    /**
     * 获取pay17usercode
     * @return
     */
    public static String getPay17_user_code(){
        return ZkNode.getIstance().getJsonConfig().get("pay17_user_code")+"";
    }

    /**
     * 获取配置
     * @see {fns:getConfig('adminPath')}
     */
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null){
            value = loader.getProperty(key);
            map.put(key, value != null ? value : StringUtils.EMPTY);
        }
        return value;
    }

    /**
     * 获取需要处理超时几天的售后订单
     * @return
     */
    public static String getCustomerDay(){
        return ZkNode.getIstance().getJsonConfig().get("customerDay")+"";
    }



    //    //阿里云OSS 对象调用地址前缀
//    public static final String OSSURL="http://9900oss.oss-cn-hangzhou.aliyuncs.com/";
//
//    public static final String OSSURL_mainType3="http://www.999000.cn/";
//
//    //jpg
//    public static final String JPEG="image/jpeg";
//    public static final String DOWNLOADURL="E:/project/file";
//
//
//    //png
//    public static final String PNG="image/png";
//
//    //阿里云OSS ACCESSKEY
//    public static final String ACCESSKEY="LTAI11IgG4CrFUNP";
//    //阿里云OSS 密钥
//    public static final String ACCESSKEYSECRET="ULvQlDxj0nzDzfvHeepoiJuDEF8kNZ";
//    //阿里云OSS 对象节点（杭州）
//    public static final String ENDPOINT="http://oss-cn-hangzhou.aliyuncs.com";
//    //阿里云OSS 对象调用地址前缀
//
    public static final int OPEN=0;


}
