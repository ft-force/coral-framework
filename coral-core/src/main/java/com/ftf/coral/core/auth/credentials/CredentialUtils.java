package com.ftf.coral.core.auth.credentials;

import com.ftf.coral.util.IDUtils;
import com.ftf.coral.util.MD5Utils;
import com.ftf.coral.util.SaltUtils;

public class CredentialUtils {

    public static String getAccessKeyId() {

        return IDUtils.generateShort8Uuid();
    }

    public static String getSecretAccessKey(String accessKeyId) {

        String salt = SaltUtils.generateSalt();
        String mw = salt + accessKeyId;
        return MD5Utils.MD5Encode(mw).toUpperCase();
    }
}
