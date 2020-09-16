package com.ftf.coral.runtime.system;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Host information.
 */
abstract class HostInfo {

    /**
     * Delegate host info to be resolved lazy. Android detection will initialize this class too and since
     * {@code InetAddress.getLocalHost()} is forbidden in Android, we will get an exception.
     */
    private static class HostInfoLazy {
        private final String HOST_NAME;
        private final String HOST_ADDRESS;

        public HostInfoLazy() {
            String hostName;
            String hostAddress;

            try {
                final InetAddress localhost = InetAddress.getLocalHost();

                hostName = localhost.getHostName();
                hostAddress = localhost.getHostAddress();
            } catch (final UnknownHostException uhex) {
                hostName = "localhost";
                hostAddress = "127.0.0.1";
            }

            this.HOST_NAME = hostName;
            this.HOST_ADDRESS = hostAddress;
        }
    }

    private static HostInfoLazy hostInfoLazy;

    /**
     * Returns host name.
     */
    public final String getHostName() {
        if (hostInfoLazy == null) {
            hostInfoLazy = new HostInfoLazy();
        }
        return hostInfoLazy.HOST_NAME;
    }

    /**
     * Returns host IP address.
     */
    public final String getHostAddress() {
        if (hostInfoLazy == null) {
            hostInfoLazy = new HostInfoLazy();
        }
        return hostInfoLazy.HOST_ADDRESS;
    }

    // ---------------------------------------------------------------- util

    protected String nosep(final String in) {
        if (in.endsWith(File.separator)) {
            return in.substring(0, in.length() - 1);
        }
        return in;
    }
}
