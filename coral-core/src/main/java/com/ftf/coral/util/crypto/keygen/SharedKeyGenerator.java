package com.ftf.coral.util.crypto.keygen;

final class SharedKeyGenerator implements BytesKeyGenerator {

    private byte[] sharedKey;

    public SharedKeyGenerator(byte[] sharedKey) {
        this.sharedKey = sharedKey;
    }

    public int getKeyLength() {
        return sharedKey.length;
    }

    public byte[] generateKey() {
        return sharedKey;
    }
}