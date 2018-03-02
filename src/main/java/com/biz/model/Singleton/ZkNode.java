package com.biz.model.Singleton;

import com.alibaba.fastjson.JSONObject;

/**
 * 单例对象 用于存放ZK watcher 中变化的配置项
 * Created by liujiajia on 2017/3/15.
 */
public class ZkNode {
    private String zkp;

    private String config;

    private JSONObject jsonConfig;

    public JSONObject getJsonConfig() {
        return jsonConfig;
    }

    public void setJsonConfig(JSONObject jsonConfig) {
        this.jsonConfig = jsonConfig;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }



    public String getZkp() {
        return zkp;
    }

    public void setZkp(String zkp) {
        this.zkp = zkp;
    }

    // 定义一个私有构造方法
    private ZkNode() {

    }
    //定义一个静态私有变量(不初始化，不使用final关键字，使用volatile保证了多线程访问时instance变量的可见性，避免了instance初始化时其他变量属性还没赋值完时，被另外线程调用)
    private static volatile ZkNode instance;

    //定义一个共有的静态方法，返回该类型实例
    public static ZkNode getIstance() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (instance == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (ZkNode.class) {
                //未初始化，则初始instance变量
                if (instance == null) {
                    instance = new ZkNode();
                }
            }
        }
        return instance;
    }
}
