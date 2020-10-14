package com.ftf.coral.business.context;

import com.ftf.coral.util.StringUtils;

public class UserContext {

    private static ThreadLocal<String> currentUserHolder = new ThreadLocal<String>();

    public static void setCurrentUser(String currentUser) {

        currentUserHolder.set(currentUser);
    }

    public static String getCurrentUser() {

        String currentUser = currentUserHolder.get();

        if (StringUtils.isNotBlank(currentUser)) {
            return currentUser;
        } else {
            return "guest";
        }
    }
}