package com.example.demo.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 程序管理，用于参数注册
 * 可用于 <string，object>类注册
 *
 * @author
 * @copyright 修改记录：
 */
public class ParamManager {

    private static ParamManager manager;

    private Map<String, Object> map;

    static {
        manager = new ParamManager();
    }

    private ParamManager() {
        map = new HashMap<String, Object>();
    }

    public static ParamManager getInstance() {
        return manager;
    }

    /**
     * 注册程序，一个接口最多只能有一个实现
     *
     * @param key
     * @param param
     */
    public void regist(String key, Object param) throws Exception {
        Boolean isList = isList(key);

        if (Boolean.TRUE.equals(isList)) {
            // 已经注册为列表
            throw new Exception("");
        }

        // 注册单个程序
        map.put(key, param);
    }

    /**
     * 注册程序到列表，一个接口可以有多个实现
     *
     * @param key
     * @param param
     */
    @SuppressWarnings("unchecked")
    public void add(String key, Object param) throws Exception {
        Boolean isList = isList(key);

        if (isList != null && Boolean.FALSE.equals(isList)) {
            // 已经注册为单个程序
            throw new Exception("");
        }

        /*
         * 注册到列表
         */
        Object obj = map.get(key);
        if (obj == null) {
            obj = new ArrayList<Object>();
            map.put(key, obj);
        }
        ((List<Object>) obj).add(param);
    }

    /**
     * 获取接口实现的单个程序
     *
     * @param key
     * @return
     * @throws Exception
     */
    public Object getProgram(String key) throws Exception {
        Boolean isList = isList(key);

        if (isList == null) {
            // 无
            return null;
        }

        if (Boolean.FALSE.equals(isList)) {
            return map.get(key);
        } else {
            // 已经注册为程序列表
            throw new Exception("");
        }
    }

    /**
     * 获取接口实现的单个程序
     *
     * @param key
     * @return
     * @throws Exception
     */
    public void remove(String key) throws Exception {
        Boolean isList = isList(key);

        if (isList == null) {
            // 无
            return;
        }

        if (Boolean.FALSE.equals(isList)) {
            map.remove(key);
        } else {
            throw new Exception("");
        }
    }

    /**
     * 获取接口实现程序列表
     *
     * @param interfaceName
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Object> getProgramList(String interfaceName) throws Exception {
        Boolean isList = isList(interfaceName);

        if (isList == null) {
            // 无
            return null;
        }

        if (Boolean.TRUE.equals(isList)) {
            return (List<Object>) map.get(interfaceName);
        } else {
            // 已经注册为单个程序
            throw new Exception("");
        }
    }

    /**
     * 判断接口的注册是单个还是列表
     *
     * @param interfaceName
     * @return null 表示从未注册， true表示列表，false表示单个
     */
    protected Boolean isList(String interfaceName) {
        Boolean rslt = null;

        Object obj = map.get(interfaceName);
        if (obj != null) {
            rslt = obj instanceof List;
        }

        return rslt;
    }

}
