package com.biz.service.mq;

public interface PrmI {
    public void sendQueue(String exchange_key, String queue_key, Object object);
}
