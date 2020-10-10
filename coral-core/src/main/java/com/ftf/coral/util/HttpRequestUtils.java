package com.ftf.coral.util;

import javax.servlet.http.HttpServletRequest;

public class HttpRequestUtils {

    public static String getTopDomain(HttpServletRequest request) {

        String topDomain;

        String referer = request.getHeader("Referer");
        if (null == referer) {
            referer = request.getHeader("referer");
        }
        String host = request.getHeader("Host");

        if (referer != null) {
            topDomain = URLUtil.getTopDomain(referer);
        } else if (!StringUtils.isEmpty(host)) {
            topDomain = URLUtil.getTopDomain(host);
        } else {
            topDomain = "ftf.com";
        }

        return topDomain;
    }

    public static String getReferer(HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        if (null == referer) {
            referer = request.getHeader("referer");
        }

        return referer;
    }

    public static String getAgent(HttpServletRequest request) {

        return request.getHeader("User-Agent");
    }
}
