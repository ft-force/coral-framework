package com.ftf.coral.core.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.ftf.coral.core.io.StreamUtil;

public class ExceptionUtil {

    public static String exceptionStackTraceToString(final Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);

        t.printStackTrace(pw);

        StreamUtil.close(pw);
        StreamUtil.close(sw);

        return sw.toString();
    }

    public static String exceptionChainToString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        while (t != null) {
            t.printStackTrace(pw);
            t = t.getCause();
        }

        StreamUtil.close(pw);
        StreamUtil.close(sw);

        return sw.toString();
    }
}
