package com.ftf.coral.core.auth.signer.internal;

import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class SignerUtils {

    private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyyMMdd").withZoneUTC();
    private static final DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss z")
        .withLocale(Locale.US).withZone(DateTimeZone.forID("GMT"));

    public static String formatDateStamp(long timeMilli) {
        return dateFormatter.print(timeMilli);
    }

    public static String formatTimestamp(long timeMilli) {
        return timeFormatter.print(timeMilli);
    }

    public static long parseMillis(String signDate) {
        return timeFormatter.parseMillis(signDate);
    }

    public static void main(String[] args) {
        System.out.println(SignerUtils.formatTimestamp(new Date().getTime()));
    }
}