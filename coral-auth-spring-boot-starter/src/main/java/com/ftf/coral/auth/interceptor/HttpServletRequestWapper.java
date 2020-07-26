package com.ftf.coral.auth.interceptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ftf.coral.core.http.HttpMethodName;
import com.ftf.coral.core.http.Request;

public class HttpServletRequestWapper implements Request<HttpServletRequest> {

    private HttpServletRequest httpServletRequest;

    public HttpServletRequestWapper(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public void addHeader(String name, String value) {
        // ignore
    }

    @Override
    public Map<String, String> getHeaders() {

        Map<String, String> headers = new HashMap<>();

        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();

            headers.put(headerName, httpServletRequest.getHeader(headerName));
        }

        return headers;
    }

    @Override
    public String getResourcePath() {
        return httpServletRequest.getRequestURI();
    }

    @Override
    public void addParameter(String name, String value) {
        // ignore
    }

    @Override
    public Map<String, String[]> getParameters() {
        return httpServletRequest.getParameterMap();
    }

    @Override
    public HttpMethodName getHttpMethod() {

        return HttpMethodName.valueOf(httpServletRequest.getMethod());
    }

    @Override
    public InputStream getContent() {

        try {
            return httpServletRequest.getInputStream();
        } catch (IOException e) {
            // ignore
        }

        return null;
    }
}
