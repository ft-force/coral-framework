package com.ftf.coral.admin.core;

public class ScTokenManager {

    private static ThreadLocal<ScToken> scTokenHolder = new ThreadLocal<>();

    public static ScToken getCurrentScToken() {

        return scTokenHolder.get();
    }

    public static void putCurrentScToken(ScToken scToken) {

        scTokenHolder.set(scToken);
    }
}
