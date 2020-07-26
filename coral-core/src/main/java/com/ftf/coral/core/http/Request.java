package com.ftf.coral.core.http;

import java.io.InputStream;
import java.util.Map;

public interface Request<T> {

    void addHeader(String name, String value);

    Map<String, String> getHeaders();

    String getResourcePath();

    void addParameter(String name, String value);

    Map<String, String[]> getParameters();

    HttpMethodName getHttpMethod();

    InputStream getContent();
}