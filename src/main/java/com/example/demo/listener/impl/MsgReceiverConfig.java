package com.example.demo.listener.impl;

import com.example.demo.cst.RedisCst;
import com.example.demo.service.WsMsgReceiver;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * 接收消息配置
 * container  和  wsListenerAdapter 都要配置
 *
 * @Author XZ.Tan
 * @Date: 2021/1/5 10:10
 * @Version 1.0
 */
@Configuration
@EnableCaching
public class MsgReceiverConfig {

    /**
     *
     * @param connectionFactory
     * @param wsListenerAdapter  对应监听适配器方法名称
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter wsListenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 可以添加多个 messageListener，配置不同的交换机(对应监听适配器方法名称)
        container.addMessageListener(wsListenerAdapter, new PatternTopic(RedisCst.RECORD_LIST_TOPIC));
        return container;
    }

    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     * @param receiver
     * @return
     */
    @Bean
    MessageListenerAdapter wsListenerAdapter(WsMsgReceiver receiver) {
        System.out.println("消息适配器WsMsgReceiver");
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean
    StringRedisTemplate template(RedisConnectionFactory  connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}