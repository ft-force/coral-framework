package com.ftf.coral.util.crypto.keygen;

import java.security.SecureRandom;

final class SecureRandomBytesKeyGenerator implements BytesKeyGenerator {

    private final SecureRandom random;

    private final int keyLength;

    public SecureRandomBytesKeyGenerator() {
        this(DEFAULT_KEY_LENGTH);
    }

    public SecureRandomBytesKeyGenerator(int keyLength) {
        this.random = new SecureRandom();
        this.keyLength = keyLength;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public byte[] generateKey() {
        byte[] bytes = new byte[keyLength];
        random.nextBytes(bytes);
        return bytes;
    }

    private static final int DEFAULT_KEY_LENGTH = 8;
}