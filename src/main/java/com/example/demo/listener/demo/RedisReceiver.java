package com.example.demo.listener.demo;

/**
 * RedisReceiver
 *
 * @Author XZ.Tan
 * @Date: 2021/1/5 10:10
 * @Version 1.0
 */
//@Service
public class RedisReceiver {

    public void receiveMessage(String message) {
        System.out.println("消息来了："+message);
        //这里是收到通道的消息之后执行的方法
    }

    public void receiveMessage2(String message) {
        System.out.println("消息来了2："+message);
        //这里是收到通道的消息之后执行的方法
    }
}