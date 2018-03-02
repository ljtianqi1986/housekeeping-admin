package com.framework.utils.zook;

import com.alibaba.fastjson.JSON;
import com.biz.model.Singleton.ZkNode;
import com.framework.utils.ConfigUtil;
import com.framework.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by liujiajia on 2017/3/13.
 */
public class ZookeeperClientUtil {
    private ZooKeeper zk;
    //zookeeper地址
    private String servers;
    //链接超时时间
    private int sessionTimeout = 50000;

    private String mainPath;


    public ZooKeeper getAliveZk() {
        ZooKeeper aliveZk = zk;
        if (aliveZk != null && aliveZk.getState().isAlive()) {
            return aliveZk;
        } else {
            zkReconnect();
            return zk;
        }
    }
    public synchronized void zkReconnect() {
        close();
        try {
            connect();
        } catch (IOException e) {
        }
    }
    public synchronized void close() {
        if (zk != null) {
            try {
                zk.close();
            } catch (InterruptedException e) {
            }
            zk = null;
        }
    }

    private synchronized void connect() throws IOException {
        if (zk == null  && !StringUtils.isBlank(servers))
            zk = new ZooKeeper(servers, sessionTimeout, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if(!StringUtil.isNullOrEmpty(watchedEvent.getPath())){
                        System.out.println(watchedEvent.getPath());
                        if (!StringUtil.isNullOrEmpty(watchedEvent.getPath())){
                            String[] nodes=watchedEvent.getPath().split("/");
                            String node=nodes[nodes.length-1];
                            if (ConfigUtil.get("ZOOKNODE").equals(node)){
                                String path=mainPath+"/"+node;
                                System.out.print(path);
                                ZkNode.getIstance().setJsonConfig(JSON.parseObject(getData(path)));
                            }
                        }
                    }
                }
            });
    }
    public String getData(String path) {
        String result = null;
        try {
            byte [] data = getAliveZk().getData(path, Boolean.TRUE,null);
            if(null != data){
                result = new String(data, "UTF-8");
            }
        } catch (KeeperException e) {
        } catch (InterruptedException e) {
        } catch (UnsupportedEncodingException e) {
        }
        return result ;
    }
    public List<String> getChildren(){
        List<String> data = null;
        try {
            data = getAliveZk().getChildren(mainPath, Boolean.TRUE);
        } catch (KeeperException e) {
        } catch (InterruptedException e) {
        }
        return data;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }
    public String getMainPath() {
        return mainPath;
    }
    public void setMainPath(String mainPath) {
        this.mainPath = mainPath;
    }
    public void setServers(String servers) {
        this.servers = servers;
    }
}
