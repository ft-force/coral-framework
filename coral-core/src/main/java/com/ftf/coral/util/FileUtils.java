package com.ftf.coral.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import com.ftf.coral.CoralCore;
import com.ftf.coral.core.io.StreamUtil;
import com.ftf.coral.runtime.system.SystemUtil;
import com.ftf.coral.util.crypto.DigestEngine;

public class FileUtils {

    private static final String MSG_NOT_FOUND = "Not found: ";
    private static final String MSG_NOT_A_FILE = "Not a file: ";
    private static final String MSG_NOT_A_DIRECTORY = "Not a directory: ";
    private static final String MSG_UNABLE_TO_DELETE = "Unable to delete: ";
    private static final String MSG_CANT_CREATE = "Can't create: ";

    private static final int ZERO = 0;
    private static final String USER_HOME = "~";
    private static final int NEGATIVE_ONE = -1;

    public static File file(String fileName) {
        fileName = StringUtils.replace(fileName, USER_HOME, SystemUtil.info().getHomeDir());
        return new File(fileName);
    }

    public static String md5(final File file) throws IOException {
        return DigestEngine.md5().digestString(file);
    }

    // ------------------------------------------------ mkdirs

    public static File mkdirs(final String dirs) throws IOException {
        return mkdirs(file(dirs));
    }

    public static File mkdirs(final File dirs) throws IOException {
        if (dirs.exists()) {
            checkIsDirectory(dirs);
            return dirs;
        }
        return checkCreateDirectory(dirs);
    }

    public static File mkdir(final String dir) throws IOException {
        return mkdir(file(dir));
    }

    public static File mkdir(final File dir) throws IOException {
        if (dir.exists()) {
            checkIsDirectory(dir);
            return dir;
        }
        return checkCreateDirectory(dir);
    }

    // ------------------------------------------------ delete
    // file

    public static void deleteFile(final String destFile) throws IOException {
        deleteFile(file(destFile));
    }

    public static void deleteFile(final File destFile) throws IOException {
        checkIsFile(destFile);
        checkDeleteSuccessful(destFile);
    }

    // ------------------------------------------------ delete
    // dir

    public static void deleteDir(final String destDir) throws IOException {
        deleteDir(file(destDir));
    }

    public static void deleteDir(final File destDir) throws IOException {
        cleanDir(destDir);
        checkDeleteSuccessful(destDir);
    }

    public static void cleanDir(final String dest) throws IOException {
        cleanDir(file(dest));
    }

    public static void cleanDir(final File destDir) throws IOException {
        checkExists(destDir);
        checkIsDirectory(destDir);

        File[] files = destDir.listFiles();
        if (files == null) {
            throw new IOException("Failed to list contents of: " + destDir);
        }

        IOException exception = null;
        for (File file : files) {
            try {
                if (file.isDirectory()) {
                    deleteDir(file);
                } else {
                    file.delete();
                }
            } catch (IOException ioex) {
                exception = ioex;
                continue;
            }
        }

        if (exception != null) {
            throw exception;
        }
    }

    // ------------------------------------------------ read/write chars
    // ------------------------------------------------ read/write string

    public static void writeString(final String dest, final String data) throws IOException {
        writeString(file(dest), data, encoding());
    }

    public static void writeString(final File dest, final String data) throws IOException {
        writeString(dest, data, encoding());
    }

    public static void writeString(final File dest, final String data, final String encoding) throws IOException {
        outString(dest, data, encoding, false);
    }

    protected static void outString(final File dest, final String data, final String encoding, final boolean append)
        throws IOException {
        if (dest.exists()) {
            checkIsFile(dest);
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(dest, append);
            out.write(data.getBytes(encoding));
        } finally {
            StreamUtil.close(out);
        }
    }

    // ------------------------------------------------ stream

    public static void writeStream(final String dest, final InputStream in) throws IOException {
        writeStream(file(dest), in);
    }

    public static void writeStream(final File dest, final InputStream in) throws IOException {
        writeStream(new FileOutputStream(dest, false), in);
    }

    public static void writeStream(final FileOutputStream out, final InputStream in) throws IOException {
        try {
            StreamUtil.copy(in, out);
        } finally {
            StreamUtil.close(out);
        }
    }

    // ------------------------------------------------ read/write byte array

    public static byte[] readBytes(final String file) throws IOException {
        return readBytes(file(file));
    }

    public static byte[] readBytes(final File file) throws IOException {
        return readBytes(file, NEGATIVE_ONE);
    }

    public static byte[] readBytes(final File file, final int count) throws IOException {
        checkExists(file);
        checkIsFile(file);
        long numToRead = file.length();
        if (numToRead >= Integer.MAX_VALUE) {
            throw new IOException("File is larger then max array size");
        }

        if (count > NEGATIVE_ONE && count < numToRead) {
            numToRead = count;
        }

        byte[] bytes = new byte[(int)numToRead];
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        randomAccessFile.readFully(bytes);
        randomAccessFile.close();

        return bytes;
    }

    public static void writeBytes(final String dest, final byte[] data) throws IOException {
        writeBytes(file(dest), data);
    }

    public static void writeBytes(final File dest, final byte[] data) throws IOException {
        writeBytes(dest, data, ZERO, data.length);
    }

    public static void writeBytes(final String dest, final byte[] data, final int off, final int len)
        throws IOException {
        writeBytes(file(dest), data, off, len);
    }

    public static void writeBytes(final File dest, final byte[] data, final int off, final int len) throws IOException {
        outBytes(dest, data, off, len, false);
    }

    public static void appendBytes(final String dest, final byte[] data) throws IOException {
        appendBytes(file(dest), data);
    }

    public static void appendBytes(final String dest, final byte[] data, final int off, final int len)
        throws IOException {
        appendBytes(file(dest), data, off, len);
    }

    public static void appendBytes(final File dest, final byte[] data) throws IOException {
        appendBytes(dest, data, ZERO, data.length);
    }

    public static void appendBytes(final File dest, final byte[] data, final int off, final int len)
        throws IOException {
        outBytes(dest, data, off, len, true);
    }

    private static void checkExists(final File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException(MSG_NOT_FOUND + file);
        }
    }

    protected static void outBytes(final File dest, final byte[] data, final int off, final int len,
        final boolean append) throws IOException {
        if (dest.exists()) {
            checkIsFile(dest);
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(dest, append);
            out.write(data, off, len);
        } finally {
            StreamUtil.close(out);
        }
    }

    private static void checkIsFile(final File file) throws IOException {
        if (!file.isFile()) {
            throw new IOException(MSG_NOT_A_FILE + file);
        }
    }

    private static void checkIsDirectory(final File dir) throws IOException {
        if (!dir.isDirectory()) {
            throw new IOException(MSG_NOT_A_DIRECTORY + dir);
        }
    }

    private static void checkDeleteSuccessful(final File dir) throws IOException {
        if (!dir.delete()) {
            throw new IOException(MSG_UNABLE_TO_DELETE + dir);
        }
    }

    private static File checkCreateDirectory(final File dir) throws IOException {
        if (!dir.mkdirs()) {
            throw new IOException(MSG_CANT_CREATE + dir);
        }
        return dir;
    }

    public static InputStream streamOf(final String file) throws IOException {
        return new FileInputStream(file);
    }

    public static InputStream streamOf(final String file, final String encoding) throws IOException {
        InputStream in = new FileInputStream(file);
        if (encoding.startsWith("UTF")) {
            in = unicodeInputStreamOf(in, encoding);
        }
        return in;
    }

    public static InputStream streamOf(final File file) throws IOException {
        return new FileInputStream(file);
    }

    public static InputStream streamOf(final File file, final String encoding) throws IOException {
        InputStream in = new FileInputStream(file);
        if (encoding.startsWith("UTF")) {
            in = unicodeInputStreamOf(in, encoding);
        }
        return in;
    }

    private static UnicodeInputStream unicodeInputStreamOf(final InputStream input, final String targetEncoding) {
        return new UnicodeInputStream(input, targetEncoding);
    }

    // ---------------------------------------------------- temp

    private static String tempPrefix() {
        return "ftf-";
    }

    public static File createTempFile() throws IOException {
        return createTempFile(tempPrefix(), null, null, true);
    }

    public static File createTempFile(final String prefix, final String suffix, final File tempDir,
        final boolean create) throws IOException {
        File file = createTempFile(prefix, suffix, tempDir);
        file.delete();
        if (create) {
            file.createNewFile();
        }
        return file;
    }

    public static File createTempFile(final String prefix, final String suffix, final File tempDir) throws IOException {
        int exceptionsCount = ZERO;
        while (true) {
            try {
                return File.createTempFile(prefix, suffix, tempDir).getCanonicalFile();
            } catch (IOException ioex) { // fixes
                                         // java.io.WinNTFileSystem.createFileExclusively
                                         // access denied
                if (++exceptionsCount >= 50) {
                    throw ioex;
                }
            }
        }
    }

    // ---------------------------------------------------- configs

    private static String encoding() {
        return CoralCore.encoding;
    }
}
