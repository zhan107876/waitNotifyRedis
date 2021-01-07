package com.example.demo.redis;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository("redisDaoImpl")
public class RedisDAOImpl implements RedisDAO {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void put(String key, Object value) {
        this.put(key, value, null);
    }


    @Override
    public void put(String key, Object value, Long timeout) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @SuppressWarnings({"rawtypes", "unchecked"})
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
                byte[] valueBytes = null;
                if (value instanceof String) {
                    valueBytes = redisTemplate.getStringSerializer().serialize((String) value);
                } else {
                    Jackson2JsonRedisSerializer valueSerializer = new Jackson2JsonRedisSerializer(value.getClass());
                    valueBytes = valueSerializer.serialize(value);
                }
                connection.set(keyBytes, valueBytes);

                // 设置失效时间
                if (timeout != null && timeout.longValue() != 0L) {
                    redisTemplate.expire(key, timeout, TimeUnit.MINUTES);
                }

                return null;
            }
        });
    }

    @Override
    public <T> T get(String key, Class<T> valueClasses) {
        T result = redisTemplate.execute(new RedisCallback<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
                byte[] valueBytes = connection.get(keyBytes);
                if (valueBytes == null) {
                    return null;
                }

                if (valueClasses.getSimpleName().equals("String")) {
                    return (T) redisTemplate.getStringSerializer().deserialize(valueBytes);
                } else {
                    return new Jackson2JsonRedisSerializer<T>(valueClasses).deserialize(valueBytes);
                }
            }
        });
        return result;
    }
//
//    @Override
//    public Object getByJavaType(String key, JavaType javaType) {
//        Object result = redisTemplate.execute(new RedisCallback<Object>() {
//            @Override
//            @SuppressWarnings("unchecked")
//            public Object doInRedis(RedisConnection connection) throws DataAccessException {
//                byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
//                byte[] valueBytes = connection.get(keyBytes);
//                if (valueBytes == null) {
//                    return null;
//                }
//                try {
//                    return (new ObjectMapper()).readValue(valueBytes, javaType);
//                } catch (Exception e) {
//                    log.error(e);
//                    return null;
//                }
//            }
//        });
//        return result;
//    }

    @Override
    public void putSerializable(String key, Object value) {
        putSerializable(key, value, null);
    }

    @Override
    public void putSerializable(String key, Object value, Long timeout) {
        redisTemplate.execute(new RedisCallback<Object>() {
            @SuppressWarnings({"rawtypes", "unchecked"})
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
                byte[] valueBytes = null;
                if (value instanceof String) {
                    valueBytes = redisTemplate.getStringSerializer().serialize((String) value);
                } else {
                    try {
                        /*
                         * 序列化
                         */
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(bos);
                        oos.writeObject(value);
                        valueBytes = bos.toByteArray();
                    } catch (IOException e) {
                        log.error("Redis write serialize failed.", e);
                    }
                }// end if instanceof
                connection.set(keyBytes, valueBytes);

                // 设置失效时间
                if (timeout != null && timeout.longValue() != 0L) {
                    redisTemplate.expire(key, timeout, TimeUnit.MINUTES);
                }

                return null;
            }
        });
    }

    @Override
    public <T> T getSerializable(String key, Class<T> valueClasses) {
        T result = redisTemplate.execute(new RedisCallback<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] keyBytes = redisTemplate.getStringSerializer().serialize(key);
                byte[] valueBytes = connection.get(keyBytes);
                if (valueBytes == null) {
                    return null;
                }

                if (valueClasses.getSimpleName().equals("String")) {
                    return (T) redisTemplate.getStringSerializer().deserialize(valueBytes);
                } else {
                    try {
                        /*
                         * 非字串，反序列化
                         */
                        ByteArrayInputStream bis = new ByteArrayInputStream(valueBytes);
                        ObjectInputStream ois = null;
                        ois = new ObjectInputStream(bis);
                        Object msg = ois.readObject();
                        return (T) msg;
                    } catch (Exception e) {
                        log.error("Redis read deserialize failed.", e);
                    }
                }// end if String

                return null;
            }// end function doInRedis
        });
        return result;
    }


    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void deleteByPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix + "*");

        redisTemplate.delete(keys);
    }

    @Override
    public void delete(List<String> keys) {
        redisTemplate.delete(keys);
    }

    @Override
    public Set<String> getKeyByPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix + "*");
        return keys;
    }

    @Override
    public void convertAndSend(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message.toString());
        log.info("redis 推送消息完成:{}", message);
    }


}
