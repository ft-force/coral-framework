package com.ftf.coral.runtime.system;

abstract class JvmInfo extends JavaInfo {

    private final String JAVA_VM_NAME = SystemUtil.get("java.vm.name");
    private final String JAVA_VM_VERSION = SystemUtil.get("java.vm.version");
    private final String JAVA_VM_VENDOR = SystemUtil.get("java.vm.vendor");
    private final String JAVA_VM_INFO = SystemUtil.get("java.vm.info");
    private final String JAVA_VM_SPECIFICATION_NAME = SystemUtil.get("java.vm.specification.name");
    private final String JAVA_VM_SPECIFICATION_VERSION = SystemUtil.get("java.vm.specification.version");
    private final String JAVA_VM_SPECIFICATION_VENDOR = SystemUtil.get("java.vm.specification.vendor");

    /**
     * Returns JVM name.
     */
    public final String getJvmName() {
        return JAVA_VM_NAME;
    }

    /**
     * Returns JVM version.
     */
    public final String getJvmVersion() {
        return JAVA_VM_VERSION;
    }

    /**
     * Returns VM vendor.
     */
    public final String getJvmVendor() {
        return JAVA_VM_VENDOR;
    }

    /**
     * Returns additional VM information.
     */
    public final String getJvmInfo() {
        return JAVA_VM_INFO;
    }

    public final String getJvmSpecificationName() {
        return JAVA_VM_SPECIFICATION_NAME;
    }

    public final String getJvmSpecificationVersion() {
        return JAVA_VM_SPECIFICATION_VERSION;
    }

    public final String getJvmSpecificationVendor() {
        return JAVA_VM_SPECIFICATION_VENDOR;
    }
}
