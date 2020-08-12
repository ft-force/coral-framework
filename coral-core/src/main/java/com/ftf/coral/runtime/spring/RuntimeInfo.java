package com.ftf.coral.runtime.spring;

import java.lang.management.ManagementFactory;

public class RuntimeInfo {

    private final Runtime runtime = Runtime.getRuntime();

    /**
     * Returns MAX memory.
     */
    public final long getMaxMemory() {
        return runtime.maxMemory();
    }

    /**
     * Returns TOTAL memory.
     */
    public final long getTotalMemory() {
        return runtime.totalMemory();
    }

    /**
     * Returns FREE memory.
     */
    public final long getFreeMemory() {
        return runtime.freeMemory();
    }

    /**
     * Returns usable memory.
     */
    public final long getAvailableMemory() {
        return runtime.maxMemory() - runtime.totalMemory() + runtime.freeMemory();
    }

    /**
     * Returns used memory.
     */
    public final long getUsedMemory() {
        return runtime.totalMemory() - runtime.freeMemory();
    }

    /**
     * Returns PID of current Java process.
     */
    public final long getCurrentPID() {
        return Long.parseLong(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
    }

    /**
     * Returns number of CPUs.
     */
    public final long getCPUs() {
        return runtime.availableProcessors();
    }
}
