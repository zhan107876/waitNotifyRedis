package com.example.demo.controller;

import com.example.demo.config.BaseResult;
import com.example.demo.cst.RedisCst;
import com.example.demo.model.RedisMsg;
import com.example.demo.redis.RedisDAO;
import com.example.demo.service.SendData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 发送测试
 *
 * @Author XZ.Tan
 * @Date: 2020/12/31 18:26
 * @Version 1.0
 */
@Controller
@RequestMapping("/test")
public class WsTestController {

    @Autowired
    private RedisDAO redisDAO;

    @Autowired
    private SendData sendData;


    @ResponseBody
    @RequestMapping("/sendDataAndWaitResult.do")
    public BaseResult sendDataAndWaitResult() throws Exception {

        // 发送数据
        RedisMsg redisMsg = new RedisMsg();
        redisMsg.setNotifyFlag(new String("zz"));
        redisMsg.setData("张无忌");
        redisMsg.setHandler(this.getClass().getSimpleName());

        RedisMsg data = sendData.SendDataAndWaitResult(redisMsg, RedisCst.RECORD_LIST_TOPIC);

        return new BaseResult(data.toString());
    }

    @ResponseBody
    @RequestMapping("/sendData.do")
    public BaseResult sendData() throws Exception {

        // 发送数据
        RedisMsg redisMsg = new RedisMsg();
        redisMsg.setNotifyFlag(new String("zz"));
        redisMsg.setData("张无忌");
        redisMsg.setHandler(this.getClass().getSimpleName());

        RedisMsg data = sendData.sendData(redisMsg, RedisCst.RECORD_LIST_TOPIC);

        return new BaseResult(data.toString());
    }

    @ResponseBody
    @RequestMapping("/getWaitResult.do")
    public BaseResult getWaitResult() throws Exception {

        // 获取数据
        RedisMsg data = sendData.getWaitResult("zz");

        return new BaseResult(data.toString());
    }

}
