package com.ftf.coral.auth;

public class AuthHelper {

    private static ThreadLocal<String> currentAppIdHolder;

    public static String getCurrentAppId() {

        return currentAppIdHolder.get();
    }

    public static void putCurrentAppId(String currentAppId) {

        currentAppIdHolder.set(currentAppId);
    }
}
