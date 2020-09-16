package com.ftf.coral.runtime.system;

import java.io.File;

import com.ftf.coral.util.StringUtils;

/**
 * User information.
 */
abstract class UserInfo extends RuntimeInfo {

    private final String USER_NAME = SystemUtil.get("user.name");
    private final String USER_HOME = nosep(SystemUtil.get("user.home"));
    private final String USER_DIR = nosep(SystemUtil.get("user.dir"));
    private final String USER_LANGUAGE = SystemUtil.get("user.language");
    private final String USER_COUNTRY =
        ((SystemUtil.get("user.country") == null) ? SystemUtil.get("user.region") : SystemUtil.get("user.country"));
    private final String JAVA_IO_TMPDIR = SystemUtil.get("java.io.tmpdir");
    private final String JAVA_HOME = nosep(SystemUtil.get("java.home"));
    private final String[] SYSTEM_CLASS_PATH =
        StringUtils.splitc(SystemUtil.get("java.class.path"), File.pathSeparator);

    public final String getUserName() {
        return USER_NAME;
    }

    public final String getHomeDir() {
        return USER_HOME;
    }

    public final String getWorkingDir() {
        return USER_DIR;
    }

    public final String getTempDir() {
        return JAVA_IO_TMPDIR;
    }

    public final String getUserLanguage() {
        return USER_LANGUAGE;
    }

    public final String getUserCountry() {
        return USER_COUNTRY;
    }

    public String getJavaHomeDir() {
        return JAVA_HOME;
    }

    public String[] getSystemClasspath() {
        return SYSTEM_CLASS_PATH;
    }
}
