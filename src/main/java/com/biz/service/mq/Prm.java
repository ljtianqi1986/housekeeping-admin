package com.biz.service.mq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("prm")
public class Prm implements PrmI{

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendQueue(String exchange_key, String queue_key, Object object) {
        // convertAndSend 将Java对象转换为消息发送至匹配key的交换机中Exchange
        Map<String,Object> params=new HashMap<>();
        params.put("mobile","18860858018");
        params.put("content","123456 为您的登录验证码，请于3分钟内填写。如非本人操作，请忽略本短信。");
        params.put("smsCode","SMS-1002");
        params.put("smsAuthorCode","KAAA234NMSJL");
        //amqpTemplate.convertAndSend("rabbitMQ.test", "test", params);
        amqpTemplate.convertSendAndReceive("test",params);
    }
}
