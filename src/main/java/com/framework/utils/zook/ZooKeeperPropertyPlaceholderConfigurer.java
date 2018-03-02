package com.framework.utils.zook;

import com.alibaba.fastjson.JSON;
import com.biz.model.Singleton.ZkNode;
import com.framework.utils.ConfigUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.List;
import java.util.Properties;

/**
 * Created by liujiajia on 2017/3/13.
 */
public class ZooKeeperPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {


    private ZookeeperClientUtil configurationClient;

    public void setConfigurationClient(ZookeeperClientUtil configurationClient) {
        this.configurationClient = configurationClient;
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {

        try {
            List<String> list = configurationClient.getChildren();
            for (String key : list) {
                String value = configurationClient.getData(configurationClient.getMainPath()+ "/"+ key);
                if (!StringUtils.isBlank(value)) {
                    ZkNode node= ZkNode.getIstance();
                    if(node!=null&&key.equals(ConfigUtil.get("ZOOKNODE"))){
                        props.put(key, value);
                        node.setJsonConfig(JSON.parseObject(value));
                    }

                }
            }
        } catch (Exception e) {
        }
        super.processProperties(beanFactoryToProcess, props);


    }
}
