package com.ftf.coral.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpServletRequestUtil {

    public static String getCookie(HttpServletRequest request, String name) {

        javax.servlet.http.Cookie[] cookies = request.getCookies();
        if (CollectionUtils.isNotEmpty(cookies)) {
            for (javax.servlet.http.Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    public static void addCookie(HttpServletRequest request, HttpServletResponse response, String cookiePath,
        String name, String value) {

        // 写入 cookie
        String topDomain = HttpRequestUtils.getTopDomain(request);
        javax.servlet.http.Cookie d = new javax.servlet.http.Cookie(name, value);
        d.setDomain(topDomain);
        d.setHttpOnly(false);
        d.setPath(cookiePath);
        response.addCookie(d);
    }
}