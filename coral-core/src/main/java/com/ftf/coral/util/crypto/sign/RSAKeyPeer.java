package com.ftf.coral.util.crypto.sign;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSAKeyPeer {

    RSAPublicKey publicKey;
    RSAPrivateKey privateKey;

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(RSAPublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(RSAPrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKeyString() {
        StringBuffer buf = new StringBuffer();
        buf.append(publicKey.getModulus().toString()).append(":").append(publicKey.getPublicExponent().toString());
        return buf.toString();
    }

    public String getPrivateKeyString() {
        StringBuffer buf = new StringBuffer();
        buf.append(privateKey.getModulus().toString()).append(":").append(privateKey.getPrivateExponent().toString());
        return buf.toString();
    }

    public static String getPublicKeyString(RSAPublicKey publicKey) {
        StringBuffer buf = new StringBuffer();
        buf.append(publicKey.getModulus().toString()).append(":").append(publicKey.getPublicExponent().toString());
        return buf.toString();
    }

    public static String getPrivateKeyString(RSAPrivateKey privateKey) {
        StringBuffer buf = new StringBuffer();
        buf.append(privateKey.getModulus().toString()).append(":").append(privateKey.getPrivateExponent().toString());
        return buf.toString();
    }
}