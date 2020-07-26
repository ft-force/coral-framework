package com.ftf.coral.core.auth.signer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.ftf.coral.core.exception.AuthException;
import com.ftf.coral.core.http.Request;
import com.ftf.coral.util.HttpUtils;

public abstract class AbstractSigner implements Signer {

    protected String getCanonicalizedQueryString(Request<?> request) {

        return this.getCanonicalizedQueryString(request.getParameters());
    }

    protected String getCanonicalizedQueryString(Map<String, String[]> parameters) {

        SortedMap<String, String> sorted = new TreeMap<String, String>();
        Iterator<Map.Entry<String, String[]>> pairs = parameters.entrySet().iterator();
        while (pairs.hasNext()) {
            Map.Entry<String, String[]> pair = pairs.next();
            String key = pair.getKey();

            String value = "";
            if (pair.getValue().length == 0) {
                value = "";
            } else if (pair.getValue().length == 1) {
                value = pair.getValue()[0];
            } else {
                value = Arrays.toString(pair.getValue());
            }

            sorted.put(HttpUtils.urlEncode(key, false), HttpUtils.urlEncode(value, false));
        }

        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String, String>> pairs2 = sorted.entrySet().iterator();
        while (pairs.hasNext()) {
            Map.Entry<String, String> pair = pairs2.next();
            builder.append(pair.getKey());
            builder.append("=");
            builder.append(pair.getValue());
            if (pairs.hasNext()) {
                builder.append("&");
            }
        }

        return builder.toString();
    }

    protected byte[] getBinaryRequestPayload(Request<?> request) {
        InputStream content = getBinaryRequestPayloadStream(request);

        if (content == null) {
            return null;
        }

        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            content.mark(-1);
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 5];
            while (true) {
                int bytesRead = content.read(buffer);
                if (bytesRead == -1) {
                    break;
                }
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            content.reset();

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            throw new AuthException("Unable to read request payload to sign request: " + e.getMessage(), e);
        } finally {
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    throw new AuthException("Unable to close byteArrayOutputStream: " + e.getMessage(), e);
                }
            }
        }
    }

    protected InputStream getBinaryRequestPayloadStream(Request<?> request) {

        try {
            InputStream is = request.getContent();
            if (is == null) {
                return new ByteArrayInputStream(new byte[0]);
            }
            if (!is.markSupported()) {
                throw new AuthException("Unable to read request payload to sign request.");
            }
            return is;
        } catch (Exception e) {
            throw new AuthException("Unable to read request payload to sign request: " + e.getMessage(), e);
        }
    }
}