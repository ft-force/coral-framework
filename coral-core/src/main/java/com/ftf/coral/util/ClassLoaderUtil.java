package com.ftf.coral.util;

import java.security.AccessController;
import java.security.PrivilegedAction;

public class ClassLoaderUtil {

    public static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        } else {
            return AccessController.doPrivileged((PrivilegedAction<ClassLoader>)ClassLoader::getSystemClassLoader);
        }
    }
}
