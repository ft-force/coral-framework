package com.ftf.coral.util.crypto;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import com.ftf.coral.core.io.StreamUtil;
import com.ftf.coral.util.StringUtils;

public interface DigestEngine {

    class JavaDigestEngine implements DigestEngine {

        private MessageDigest messageDigest;

        JavaDigestEngine(final String algorithm) {
            try {
                this.messageDigest = MessageDigest.getInstance(algorithm);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public byte[] digest(final byte[] byteArray) {
            messageDigest.update(byteArray);
            return messageDigest.digest();
        }

        @Override
        public byte[] digest(final File file) throws IOException {
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            DigestInputStream dis = null;

            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                dis = new DigestInputStream(bis, messageDigest);

                while (dis.read() != -1) {}
            } finally {
                StreamUtil.close(dis);
                StreamUtil.close(bis);
                StreamUtil.close(fis);
            }

            return messageDigest.digest();
        }
    }

    /**
     * Creates new MD2 digest.
     */
    public static DigestEngine md2() {
        return new JavaDigestEngine("MD2");
    }

    /**
     * Creates new MD5 digest.
     */
    public static DigestEngine md5() {
        return new JavaDigestEngine("MD5");
    }

    /**
     * Creates new SHA-1 digest.
     */
    public static DigestEngine sha1() {
        return new JavaDigestEngine("SHA-1");
    }

    /**
     * Creates new SHA-256 digest.
     */
    public static DigestEngine sha256() {
        return new JavaDigestEngine("SHA-256");
    }

    /**
     * Creates new SHA-384 digest.
     */
    public static DigestEngine sha384() {
        return new JavaDigestEngine("SHA-384");
    }

    /**
     * Creates new SHA-512 digest.
     */
    public static DigestEngine sha512() {
        return new JavaDigestEngine("SHA-512");
    }

    /**
     * Returns byte-hash of input byte array.
     */
    public byte[] digest(byte[] input);

    /**
     * Returns byte-hash of input string.
     */
    public default byte[] digest(final String input) {
        return digest(StringUtils.getBytes(input));
    }

    /**
     * Returns digest of a file. Implementations may not read the whole file
     * into the memory.
     */
    public byte[] digest(final File file) throws IOException;

    /**
     * Returns string hash of input byte array.
     */
    public default String digestString(final byte[] byteArray) {
        return StringUtils.toHexString(digest(byteArray));
    }

    /**
     * Returns string hash of input string.
     */
    public default String digestString(final String input) {
        return StringUtils.toHexString(digest(input));
    }

    public default String digestString(final File file) throws IOException {
        return StringUtils.toHexString(digest(file));
    }
}
