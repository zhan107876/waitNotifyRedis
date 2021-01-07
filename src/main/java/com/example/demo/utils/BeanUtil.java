package com.example.demo.utils;

import org.springframework.beans.BeanUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 * @Auther: lzl
 * @Date: 2020/8/24 17:22
 * @Description: bean 工具类
 */
public class BeanUtil {

    private static WriteLock lock = new ReentrantReadWriteLock().writeLock();

    private static final Map<Class<?>, Map<String, Field>> METADATA = new HashMap<>();

    /**
     * 对象转换
     *
     * @param source
     *            源对象
     * @param targetClass
     *            目标类
     * @return 返回一个新的目标类对象, 该目标类对象的属性值从源对象中拷贝而来
     */
    public static <T> T convert(Object source, Class<T> targetClass) {
        T target = newInstance(targetClass);
        copyProperties(source, target);
        return target;
    }

    /**
     * 属性拷贝, 当且仅当两个对象的非静态属性名称相同且对应的属性类型也相同时才进行属性值拷贝
     * 深复制。复制的两个对象不是同一个地址
     *
     * @param source
     *            源对象
     * @param target
     *            目标对象
     */
    public static void copyProperties(Object source, Object target) {
        try {
            // 源对象进行深复制
            Object src = deepClone(source);
            source = null;  //释放
            BeanUtils.copyProperties(src, target);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 序列化 深复制对象
     * @param src
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static<T> T deepClone(T src) throws IOException, ClassNotFoundException {
        Object obj = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(src);
        objectOutputStream.close();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        obj = objectInputStream.readObject();
        objectInputStream.close();
        return (T) obj;
    }

    /**
     * 创建类的一个实例
     *
     * @param beanClass
     *            类
     */
    public static <T> T newInstance(Class<T> beanClass) {
        try {
            return beanClass.newInstance();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}