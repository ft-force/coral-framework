package com.ftf.coral.admin.core;

import com.ftf.coral.util.StringUtils;

public class CoralAdminCore {

    public static String tokenPrefix = "_sa_";

    public static void setTokenPrefix(String tokenPrefix) {

        if (StringUtils.isNotBlank(tokenPrefix)) {
            CoralAdminCore.tokenPrefix = tokenPrefix;
        }
    }

    public static String getTokenKey(String key) {
        return StringUtils.join(CoralAdminCore.tokenPrefix, key);
    }
}