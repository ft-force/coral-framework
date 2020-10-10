package com.ftf.coral.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLUtil {

    public static final String CHARSET = "utf-8";
    private static final Pattern hostPattern =
        Pattern.compile("(http://|https://)?([^/|:]*)", Pattern.CASE_INSENSITIVE);
    private static final Pattern topDomainPattern =
        Pattern.compile("\"^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$\"",
            Pattern.CASE_INSENSITIVE);

    public static Map<String, String> parseParameters(final String query) {

        Map<String, String> params = new HashMap<>();

        if (query == null || query.trim().isEmpty()) {
            return params; // empty map
        }

        try {
            StringTokenizer st = new StringTokenizer(query.trim(), "&");

            while (st.hasMoreTokens()) {

                String param = st.nextToken();
                String pair[] = param.split("=");

                if (pair.length > 1) {
                    String key = URLDecoder.decode(pair[0], CHARSET);
                    if (params.containsKey(key)) {
                        continue;
                    }
                    params.put(key, URLDecoder.decode(pair[1], CHARSET));
                }
            }
        } catch (UnsupportedEncodingException e) {
        }

        return params;
    }

    public static String serializeParameters(final Map<String, String> params) {

        if (params == null || params.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {

            if (entry.getKey() == null || entry.getValue() == null) {
                continue;
            }

            try {
                String encodedKey = URLEncoder.encode(entry.getKey(), CHARSET);
                String encodedValue = URLEncoder.encode(entry.getValue(), CHARSET);

                if (sb.length() > 0) {
                    sb.append('&');
                }

                sb.append(encodedKey);
                sb.append('=');
                sb.append(encodedValue);
            } catch (UnsupportedEncodingException e) {
            }
        }

        return sb.toString();
    }

    public static String getTopDomain(String url) {

        if (url == null) {
            return null;
        }

        String result = url;

        Matcher matcher = topDomainPattern.matcher(url);
        if (matcher.find()) {
            result = matcher.group();
        } else {
            // 非标域名，直接返回host
            matcher = hostPattern.matcher(url);
            result = matcher.find() ? matcher.group(2) : url;
        }

        return result;
    }
}