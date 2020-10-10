package com.ftf.coral.admin.core;

import com.ftf.coral.admin.protobuf.ScTokenSessionInfo;

public class ScAccountManager {

    private static ThreadLocal<ScTokenSessionInfo> scTokenSessionInfoHolder = new ThreadLocal<>();

    public static ScTokenSessionInfo getCurrentTokenSessionInfo() {

        return scTokenSessionInfoHolder.get();
    }

    public static void putCurrentTokenSessionInfo(ScTokenSessionInfo scTokenSessionInfo) {

        scTokenSessionInfoHolder.set(scTokenSessionInfo);
    }
}