package com.example.demo.service;

import org.springframework.stereotype.Service;

/**
 * ws 消息接收器
 *
 * @Author XZ.Tan
 * @Date: 2021/1/5 10:10
 * @Version 1.0
 */
@Service
public interface WsMsgReceiver {

    /**
     * 消息接收器
     *
     * @param redisMsg
     */
    void receiveMessage(Object redisMsg) throws Exception;

}