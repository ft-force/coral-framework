package com.ftf.coral.util;

import java.util.Collection;
import java.util.Enumeration;

public class CollectionUtils {

    public static boolean isNotEmpty(Object[] array) {
        return array != null && array.length > 0;
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return false == isEmpty(collection);
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Enumeration<?> enumeration) {
        return false == isEmpty(enumeration);
    }

    public static boolean isEmpty(Enumeration<?> enumeration) {
        return null == enumeration || false == enumeration.hasMoreElements();
    }
}
