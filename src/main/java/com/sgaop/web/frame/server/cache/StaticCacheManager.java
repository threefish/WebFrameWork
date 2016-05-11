package com.sgaop.web.frame.server.cache;


import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class StaticCacheManager {

    private static HashMap staticCacheMap = new HashMap();

    /**
     * 单实例构造方法
     */
    private StaticCacheManager() {
        super();
    }

    /**
     * 得到缓存。同步静态方法
     *
     * @param key
     * @return
     */
    public synchronized static Object getCache(String key) {
        return staticCacheMap.get(key);
    }

    /**
     * 得到缓存
     *
     * @param key
     * @return
     */
    public static String getCacheStr(String key) {
        return staticCacheMap.get(key).toString();
    }


    /**
     * 判断是否存在一个缓存
     *
     * @param key
     * @return
     */
    public synchronized static boolean hasCache(String key) {
        return staticCacheMap.containsKey(key);
    }

    /**
     * 清除所有缓存
     */
    public synchronized static void clearAll() {
        staticCacheMap.clear();
    }

    /**
     * 清除指定的缓存
     *
     * @param key
     */
    public synchronized static void clearOnly(String key) {
        staticCacheMap.remove(key);
    }

    /**
     * 载入缓存
     *
     * @param key
     * @param obj
     */
    public synchronized static void putCache(String key, Object obj) {
        staticCacheMap.put(key, obj);
    }

    /**
     * 通过配置文件载入缓存
     *
     * @param properties
     */
    public synchronized static void putCache(Properties properties) {
        Enumeration enu = properties.propertyNames();
        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            putCache(key, properties.get(key));
        }
    }
} 
 