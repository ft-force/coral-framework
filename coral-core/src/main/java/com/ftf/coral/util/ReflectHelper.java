package com.ftf.coral.util;

import java.lang.reflect.Field;

public class ReflectHelper {

    public static Field getFieldByFieldName(Object obj, String fieldName) throws NoSuchFieldException {

        for (Class<?> c = obj.getClass(); c != Object.class; c = c.getSuperclass()) {
            try {
                return c.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                // Try parent
            }
        }

        throw new NoSuchFieldException(fieldName);
    }

    public static Object getValueByFieldName(Object obj, String fieldName)
                    throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

        Field field = getFieldByFieldName(obj, fieldName);

        Object value = null;

        if (field != null) {
            if (field.isAccessible()) {
                value = field.get(obj);
            } else {
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        }

        return value;
    }

    public static void setValueByFieldName(Object obj, String fieldName, Object value)
                    throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {

        Field field = getFieldByFieldName(obj, fieldName);

        if (field.isAccessible()) {
            field.set(obj, value);
        } else {
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        }
    }
}