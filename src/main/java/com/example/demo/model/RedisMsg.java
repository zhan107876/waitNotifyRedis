package com.example.demo.model;

import lombok.Data;

import java.io.Serializable;

/**
 * RedisMsg
 *
 * @Author XZ.Tan
 * @Date: 2021/1/5 18:53
 * @Version 1.0
 */
@Data
public class RedisMsg implements Serializable {

    /**
     * 处理类
     */
    private String handler;

    /**
     * 通知标志
     */
    private String notifyFlag;

    /**
     * 数据
     */
    private Object data;

    public RedisMsg(String notifyFlag) {
        this.notifyFlag = notifyFlag;
    }

    public RedisMsg() {
    }
}
