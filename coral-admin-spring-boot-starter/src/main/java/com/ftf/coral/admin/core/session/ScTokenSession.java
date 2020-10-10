package com.ftf.coral.admin.core.session;

import java.util.List;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.ftf.coral.admin.core.ScToken;
import com.ftf.coral.admin.core.entity.ScAccount;
import com.ftf.coral.admin.protobuf.ScAccountInfo;
import com.ftf.coral.admin.protobuf.ScSessionInfo;
import com.ftf.coral.admin.protobuf.ScTokenSessionInfo;
import com.ftf.coral.util.CollectionUtils;
import com.ftf.coral.util.StringUtils;
import com.ftf.coral.util.SystemClock;
import com.google.protobuf.Int32Value;
import com.google.protobuf.Int64Value;
import com.google.protobuf.InvalidProtocolBufferException;

public class ScTokenSession {

    private static int SESSION_EXPIRE_SECONDS = 30 * 60;

    private RedisTemplate<String, byte[]> redisTemplate;

    public ScTokenSession(RedisTemplate<String, byte[]> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void init(ScToken scToken, ScAccount scAccount, List<String> roles) {

        // 初始化 sessionInfo
        ScSessionInfo.Builder scSessionInfoBuilder = ScSessionInfo.newBuilder();
        scSessionInfoBuilder.setBeginTime(Int64Value.newBuilder().setValue(SystemClock.now()));
        scSessionInfoBuilder.setDeviceInfo("deviceinfo");
        byte[] scSessionInfoBytes = scSessionInfoBuilder.build().toByteArray();

        if (scToken.isLogin()) {

            // 初始化 accountInfo
            ScAccountInfo.Builder scAccountInfoBuilder = ScAccountInfo.newBuilder();
            scAccountInfoBuilder.setAccountId(Int64Value.newBuilder().setValue(scAccount.getId()));
            scAccountInfoBuilder.setCategory(Int32Value.newBuilder().setValue(scAccount.getCategory()));
            scAccountInfoBuilder.setUsername(scAccount.getUsername());
            if (CollectionUtils.isNotEmpty(roles)) {
                scAccountInfoBuilder.addAllRoles(roles);
            }
            scAccountInfoBuilder.setLoginTime(Int64Value.newBuilder().setValue(SystemClock.now()));
            byte[] scAccountInfoBytes = scAccountInfoBuilder.build().toByteArray();

            String sessionKey = StringUtils.join("sa_ts_", scToken.getAccountIdMD5(), scToken.getSessionToken(),
                scToken.getClientSignature());
            String accountKey = StringUtils.join("sa_ta_", scToken.getAccountIdMD5());

            final RedisSerializer<String> keySerializer = redisTemplate.getStringSerializer();
            byte[] sessionKeyBytes = keySerializer.serialize(sessionKey);
            byte[] accountKeyBytes = keySerializer.serialize(accountKey);

            redisTemplate.executePipelined((RedisCallback<Object>)connection -> {
                connection.set(sessionKeyBytes, scSessionInfoBytes);
                connection.expire(sessionKeyBytes, SESSION_EXPIRE_SECONDS);
                connection.set(accountKeyBytes, scAccountInfoBytes);
                connection.expire(accountKeyBytes, SESSION_EXPIRE_SECONDS);
                return null;
            });

        } else {

            String sessionKey = StringUtils.join("sa_ts_", scToken.getSessionToken(), scToken.getClientSignature());

            final RedisSerializer<String> keySerializer = redisTemplate.getStringSerializer();
            byte[] sessionKeyBytes = keySerializer.serialize(sessionKey);

            redisTemplate.executePipelined((RedisCallback<Object>)connection -> {
                connection.set(sessionKeyBytes, scSessionInfoBytes);
                connection.expire(sessionKeyBytes, SESSION_EXPIRE_SECONDS);
                return null;
            });
        }
    }

    public void refresh(ScToken scToken) {

        if (scToken.isLogin()) {

            String sessionKey = StringUtils.join("sa_ts_", scToken.getAccountIdMD5(), scToken.getSessionToken(),
                scToken.getClientSignature());
            String accountKey = StringUtils.join("sa_ta_", scToken.getAccountIdMD5());

            final RedisSerializer<String> keySerializer = redisTemplate.getStringSerializer();
            byte[] sessionKeyBytes = keySerializer.serialize(sessionKey);
            byte[] accountKeyBytes = keySerializer.serialize(accountKey);

            // 刷新 session
            redisTemplate.executePipelined((RedisCallback<Object>)connection -> {
                connection.expire(sessionKeyBytes, SESSION_EXPIRE_SECONDS);
                connection.expire(accountKeyBytes, SESSION_EXPIRE_SECONDS);
                return null;
            });

        } else {

            String sessionKey = StringUtils.join("sa_ts_", scToken.getSessionToken(), scToken.getClientSignature());

            final RedisSerializer<String> keySerializer = redisTemplate.getStringSerializer();
            byte[] sessionKeyBytes = keySerializer.serialize(sessionKey);

            // 刷新 session
            redisTemplate.executePipelined((RedisCallback<Object>)connection -> {
                connection.expire(sessionKeyBytes, SESSION_EXPIRE_SECONDS);
                return null;
            });
        }
    }

    public ScTokenSessionInfo getScTokenSessionInfo(ScToken scToken) {

        try {
            if (scToken.isLogin()) {

                String sessionKey = StringUtils.join("sa_ts_", scToken.getAccountIdMD5(), scToken.getSessionToken(),
                    scToken.getClientSignature());
                String accountKey = StringUtils.join("sa_ta_", scToken.getAccountIdMD5());

                final RedisSerializer<String> keySerializer = redisTemplate.getStringSerializer();
                byte[] sessionKeyBytes = keySerializer.serialize(sessionKey);
                byte[] accountKeyBytes = keySerializer.serialize(accountKey);

                // 刷新 session
                List<Object> list = redisTemplate.executePipelined((RedisCallback<Object>)connection -> {
                    connection.get(sessionKeyBytes);
                    connection.get(accountKeyBytes);
                    return null;
                });

                if (list.get(0) != null && list.get(1) != null) {
                    ScTokenSessionInfo.Builder scTokenSessionInfoBuilder = ScTokenSessionInfo.newBuilder();
                    scTokenSessionInfoBuilder.setScSessionInfo(ScSessionInfo.parseFrom((byte[])list.get(0)));
                    scTokenSessionInfoBuilder.setScAccountInfo(ScAccountInfo.parseFrom((byte[])list.get(1)));

                    return scTokenSessionInfoBuilder.build();
                }

            } else {

                String sessionKey = StringUtils.join("sa_ts_", scToken.getSessionToken(), scToken.getClientSignature());

                final RedisSerializer<String> keySerializer = redisTemplate.getStringSerializer();
                byte[] sessionKeyBytes = keySerializer.serialize(sessionKey);

                // 刷新 session
                List<Object> list = redisTemplate.executePipelined((RedisCallback<Object>)connection -> {
                    connection.get(sessionKeyBytes);
                    return null;
                });

                if (list.get(0) != null) {
                    ScTokenSessionInfo.Builder scTokenSessionInfoBuilder = ScTokenSessionInfo.newBuilder();
                    scTokenSessionInfoBuilder.setScSessionInfo(ScSessionInfo.parseFrom((byte[])list.get(0)));

                    return scTokenSessionInfoBuilder.build();
                }
            }

            return null;
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    public void clear(ScToken scToken) {

        if (scToken.isLogin()) {
            String sessionKey = StringUtils.join("sa_ts_", scToken.getAccountIdMD5(), scToken.getSessionToken(),
                scToken.getClientSignature());

            final RedisSerializer<String> keySerializer = redisTemplate.getStringSerializer();
            byte[] sessionKeyBytes = keySerializer.serialize(sessionKey);

            redisTemplate.executePipelined((RedisCallback<Object>)connection -> {
                connection.del(sessionKeyBytes);
                return null;
            });
        }
    }
}
