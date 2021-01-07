package com.example.demo.listener.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * spring 监听的方式订阅
 * 接受者
 * @see RedisReceiver
 *
 * @Author XZ.Tan
 * @Date: 2021/1/5 10:10
 * @Version 1.0
 */
//@Configuration
//@EnableCaching
public class RedisCacheConfig {

    /**
     *
     * @param connectionFactory
     * @param listenerAdapter1  对应监听适配器方法名称
     * @param listenerAdapter2 对应监听适配器方法名称
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter1, MessageListenerAdapter listenerAdapter2) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 可以添加多个 messageListener，配置不同的交换机(对应监听适配器方法名称)
        container.addMessageListener(listenerAdapter1, new PatternTopic("channel:test"));
        container.addMessageListener(listenerAdapter2, new PatternTopic("channel:test2"));
        return container;
    }

    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     * @param receiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter1(RedisReceiver receiver) {
        System.out.println("消息适配器1");
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     * @param receiver
     * @return
     */
    @Bean
    MessageListenerAdapter listenerAdapter2(RedisReceiver receiver) {
        System.out.println("消息适配器2");
        return new MessageListenerAdapter(receiver, "receiveMessage2");
    }

    @Bean
    StringRedisTemplate template(RedisConnectionFactory  connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }
}