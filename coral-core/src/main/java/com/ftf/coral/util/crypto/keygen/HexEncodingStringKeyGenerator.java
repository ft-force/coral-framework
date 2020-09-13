package com.ftf.coral.util.crypto.keygen;

import com.ftf.coral.util.crypto.codec.Hex;

final class HexEncodingStringKeyGenerator implements StringKeyGenerator {

    private final BytesKeyGenerator keyGenerator;

    public HexEncodingStringKeyGenerator(BytesKeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public String generateKey() {
        return new String(Hex.encode(keyGenerator.generateKey()));
    }
}