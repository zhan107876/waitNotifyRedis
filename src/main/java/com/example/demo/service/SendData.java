package com.example.demo.service;

import com.example.demo.model.RedisMsg;

/**
 * 发送数据封装类
 *
 * @Author XZ.Tan
 * @Date: 2021/1/6 9:35
 * @Version 1.0
 */
public interface SendData {

    /**
     * 发送数据并等待响应通知
     *
     * @return
     */
    RedisMsg SendDataAndWaitResult(RedisMsg redisMsg, String topic) throws Exception;

    /**
     * 发送数据
     * notifyFlag 作为通知的标志
     *
     * @param redisMsg
     * @param topic
     * @return
     */
    RedisMsg sendData(RedisMsg redisMsg, String topic) throws Exception;

    /**
     * 获取结果，通过
     * notifyFlag 作为获取结果的标志
     *
     * @return
     */
    RedisMsg getWaitResult(String notifyFlag) throws Exception;

}