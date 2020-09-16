package com.ftf.coral.core.auth.signer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthorizationData {

    private static String AUTH_HEADER_REGEX =
        "\\s*[Hh]mac\\s*username=\"(.+)\",\\s*algorithm=\"(.+)\",\\s*headers=\"(.+)\",\\s*signature=\"(.+)\"";
    private static Pattern AUTH_HEADER_PATTERN = Pattern.compile(AUTH_HEADER_REGEX, Pattern.CASE_INSENSITIVE);

    private String username;
    private String algorithm;
    private String[] headerItems;
    private String signature;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String[] getHeaderItems() {
        return headerItems;
    }

    public void setHeaderItems(String[] headerItems) {
        this.headerItems = headerItems;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public static AuthorizationData parse(String authorizationToken) {

        Matcher matcher = AUTH_HEADER_PATTERN.matcher(authorizationToken);
        if (!matcher.find()) {
            return null;
        }

        String username = matcher.group(1);
        String algorithm = matcher.group(2);
        String headers = matcher.group(3);
        String signature = matcher.group(4);

        String[] headerItems = headers.split(" ");

        AuthorizationData authorizationData = new AuthorizationData();

        authorizationData.setUsername(username);
        authorizationData.setAlgorithm(algorithm);
        authorizationData.setHeaderItems(headerItems);
        authorizationData.setSignature(signature);

        return authorizationData;
    }
}
