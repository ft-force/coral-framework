package com.ftf.coral.core.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ftf.coral.core.auth.credentials.Credential;

public class AuthManager {

    private static ConcurrentHashMap<String, Credential> credentialCache = new ConcurrentHashMap<>();
    private static ThreadLocal<String> currentAppIdHolder = new ThreadLocal<>();

    public static Credential getCredential(String accessKeyId) {
        return credentialCache.get(accessKeyId);
    }

    public static void refreshCredentialCache(Map<String, Credential> newCredentialCache) {

        if (newCredentialCache == null || newCredentialCache.size() == 0) {
            credentialCache.clear();
            return;
        }

        if (credentialCache.size() == 0) {
            credentialCache.putAll(newCredentialCache);
            return;
        }

        List<Credential> waitToDeletes = new ArrayList<Credential>();
        List<Credential> waitToUpdates = new ArrayList<Credential>();

        credentialCache.forEach((accessKeyId, credential) -> {

            Credential newCredential = newCredentialCache.remove(accessKeyId);

            if (newCredential == null) {
                waitToDeletes.add(credential);
            } else if (!newCredential.equals(credential)) {
                waitToUpdates.add(newCredential);
            }
        });

        // 新增
        newCredentialCache.forEach((accessKeyId, credential) -> {
            credentialCache.put(accessKeyId, credential);
        });

        // 删除
        waitToDeletes.forEach(credential -> {
            credentialCache.remove(credential.getAccessKeyId());
        });

        // 修改
        waitToUpdates.forEach(credential -> {
            credentialCache.replace(credential.getAccessKeyId(), credential);
        });
    }

    public static String getCurrentAppId() {

        return currentAppIdHolder.get();
    }

    public static void putCurrentAppId(String currentAppId) {

        currentAppIdHolder.set(currentAppId);
    }
}
