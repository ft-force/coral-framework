package com.ftf.coral.runtime;

import java.util.concurrent.ConcurrentHashMap;

public class CoralContext {

    protected static ConcurrentHashMap<Class<?>, Object> apiMap = new ConcurrentHashMap<>();

    public static <T> void registAPI(Class<T> serviceType, T service) {
        apiMap.put(serviceType, service);
    }

    public static Object getAPI(String cls) {
        try {
            return getAPI(Class.forName(cls));
        } catch (Throwable e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAPI(Class<T> type) {
        return (T) apiMap.get(type);
    }
}
