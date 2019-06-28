package com.ps.accessservicedemo.other;

import java.util.HashMap;
import java.util.Map;

/**
 * 单例管理类
 *
 * @author donghailong
 * @date 2018/4/18
 */
public final class SingletonManager {
    private static final Map<String, Object> sInstanceMap = new HashMap<>();

    private SingletonManager() throws InstantiationException {
        throw new InstantiationException("SingletonManager not allow create instance!");
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(final Class<T> clazz) {
        final String clazzName = clazz.getName();
        if (!sInstanceMap.containsKey(clazzName)) {
            synchronized (sInstanceMap) {
                if (!sInstanceMap.containsKey(clazzName)) {
                    T instance = null;
                    try {
                        instance = clazz.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } finally {
                        if (null != instance) {
                            sInstanceMap.put(clazzName, instance);
                        }
                    }
                    return instance;
                }
            }
        }
        return (T) sInstanceMap.get(clazzName);
    }

    /**
     *
     */
    public static void clear() {
        sInstanceMap.clear();
    }

}

