package com.example.demo.service.impl;

import com.example.demo.config.ParamManager;
import com.example.demo.model.RedisMsg;
import com.example.demo.redis.RedisDAO;
import com.example.demo.service.SendData;
import com.example.demo.utils.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 发送数据方法
 *
 * @Author XZ.Tan
 * @Date: 2021/1/6 9:38
 * @Version 1.0
 */
@Service
@Slf4j
public class SendDataImpl implements SendData {

    @Autowired
    private RedisDAO redisDAO;

    /**
     * 默认5秒超时
     **/
    private static long WAIT_TIME = 5 * 1000L;

    @Override
    public RedisMsg SendDataAndWaitResult(RedisMsg redisMsg, String topic) throws Exception {

        /*
         * @Date: 2021/1/6 9:40
         * Step 1: 发送数据，设置通知标志
         *
         */
        JSONObject json = JSONObject.fromObject(redisMsg);
        ParamManager.getInstance().regist(redisMsg.getNotifyFlag(), new RedisMsg(redisMsg.getNotifyFlag()));
        redisDAO.convertAndSend(topic, json.toString());

        /*
         * @Date: 2021/1/6 9:40
         * Step 2: 等待结果
         */
        synchronized (redisMsg.getNotifyFlag()) {
            try {
                log.info("等待:{}[{}]", redisMsg.getNotifyFlag(), System.identityHashCode(redisMsg.getNotifyFlag()));
                redisMsg.getNotifyFlag().wait(WAIT_TIME);//等待唤醒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /*
         * @Date: 2021/1/6 9:53
         * Step 3: 封装结果返回
         */
        RedisMsg program = (RedisMsg) ParamManager.getInstance().getProgram(redisMsg.getNotifyFlag());
        RedisMsg result = BeanUtil.convert(program, RedisMsg.class);
        ParamManager.getInstance().remove(redisMsg.getNotifyFlag());
        return result;
    }

    @Override
    public RedisMsg sendData(RedisMsg redisMsg, String topic) throws Exception {
        /*
         * @Date: 2021/1/6 9:40
         * Step 1: 发送数据，设置通知标志
         *
         */
        JSONObject json = JSONObject.fromObject(redisMsg);
        ParamManager.getInstance().regist(redisMsg.getNotifyFlag(), new RedisMsg(redisMsg.getNotifyFlag()));
        redisDAO.convertAndSend(topic, json.toString());

        return redisMsg;
    }

    /**
     * 等待结束后立即删除数据
     *
     * @param notifyFlag
     * @return
     */
    @Override
    public RedisMsg getWaitResult(String notifyFlag) throws Exception {

        RedisMsg program = (RedisMsg) ParamManager.getInstance().getProgram(notifyFlag);

        try {
            /*
             * @Date: 2021/1/6 9:40
             * Step 2: 等待结果
             */
            synchronized (program.getNotifyFlag()) {
                try {
                    log.info("等待:{}[{}]", program.getNotifyFlag(), System.identityHashCode(program.getNotifyFlag()));
                    program.getNotifyFlag().wait(WAIT_TIME);//等待唤醒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            return new RedisMsg();
        }

        /*
         * @Date: 2021/1/6 9:53
         * Step 3: 封装结果返回
         */
        RedisMsg result = BeanUtil.convert(program, RedisMsg.class);
        ParamManager.getInstance().remove(program.getNotifyFlag());
        return result;
    }


}