package com.ftf.coral.util.crypto.keygen;

public interface BytesKeyGenerator {

    int getKeyLength();

    byte[] generateKey();
}