package com.example.demo.service.impl;

import com.example.demo.config.ParamManager;
import com.example.demo.controller.WsTestController;
import com.example.demo.listener.impl.MsgReceiverConfig;
import com.example.demo.model.RedisMsg;
import com.example.demo.service.WsMsgReceiver;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * 消息接收器
 * 接收redis订阅的消息
 * 可以根据不同业务写多个
 * 到 MsgReceiverConfig 注册即可
 * @see MsgReceiverConfig
 * 测试方法
 * @see WsTestController
 *
 * @Author XZ.Tan
 * @Date: 2021/1/5 17:38
 * @Version 1.0
 */
@Service
@Slf4j
public class WsMsgReceiverImpl implements WsMsgReceiver {

    @Override
    public void receiveMessage(Object redisMsg) throws Exception {

        log.info("{}-收到数据:{}", this.getClass(), redisMsg);

        /**
         * 转换结果
         */
        JSONObject jsonObject= JSONObject.fromObject(redisMsg);
        RedisMsg result = (RedisMsg) JSONObject.toBean(jsonObject, RedisMsg.class);

        RedisMsg notify = (RedisMsg) ParamManager.getInstance().getProgram(result.getNotifyFlag());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        notify.setData(result.getData());//设置数据
        synchronized (notify.getNotifyFlag()) {
            notify.getNotifyFlag().notify();//通知到号
            log.info("完成通知:{}[{}]", notify.getNotifyFlag(), System.identityHashCode(notify.getNotifyFlag()));
        }

    }

}