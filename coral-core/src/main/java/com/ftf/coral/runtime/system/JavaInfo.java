package com.ftf.coral.runtime.system;

import java.util.ArrayList;

abstract class JavaInfo extends HostInfo {

    private final String JAVA_VERSION = SystemUtil.get("java.version");
    private final int JAVA_VERSION_NUMBER = detectJavaVersionNumber();
    private final String JAVA_VENDOR = SystemUtil.get("java.vendor");
    private final String JAVA_VENDOR_URL = SystemUtil.get("java.vendor.url");
    private final String JAVA_SPECIFICATION_VERSION = SystemUtil.get("java.specification.version");
    private final String JAVA_SPECIFICATION_NAME = SystemUtil.get("java.specification.name");
    private final String JAVA_SPECIFICATION_VENDOR = SystemUtil.get("java.specification.vendor");
    private final String[] JRE_PACKAGES = buildJrePackages(JAVA_VERSION_NUMBER);

    /**
     * Returns Java version string, as specified in system property. Returned string contain major version, minor
     * version and revision.
     */
    public String getJavaVersion() {
        return JAVA_VERSION;
    }

    /**
     * Returns unified Java version as an integer.
     */
    public int getJavaVersionNumber() {
        return JAVA_VERSION_NUMBER;
    }

    /**
     * Returns Java vendor.
     */
    public String getJavaVendor() {
        return JAVA_VENDOR;
    }

    /**
     * Returns Java vendor URL.
     */
    public String getJavaVendorURL() {
        return JAVA_VENDOR_URL;
    }

    /**
     * Retrieves the version of the currently running JVM.
     */
    public String getJavaSpecificationVersion() {
        return JAVA_SPECIFICATION_VERSION;
    }

    public final String getJavaSpecificationName() {
        return JAVA_SPECIFICATION_NAME;
    }

    public final String getJavaSpecificationVendor() {
        return JAVA_SPECIFICATION_VENDOR;
    }

    // ---------------------------------------------------------------- packages

    /**
     * Returns list of packages, build into runtime jars.
     */
    public String[] getJrePackages() {
        return JRE_PACKAGES;
    }

    /**
     * Builds a set of java core packages.
     */
    private String[] buildJrePackages(final int javaVersionNumber) {
        final ArrayList<String> packages = new ArrayList<>();

        switch (javaVersionNumber) {
            case 9:
            case 8:
            case 7:
            case 6:
            case 5:
                // in Java1.5, the apache stuff moved
                packages.add("com.sun.org.apache");
                // fall through...
            case 4:
                if (javaVersionNumber == 4) {
                    packages.add("org.apache.crimson");
                    packages.add("org.apache.xalan");
                    packages.add("org.apache.xml");
                    packages.add("org.apache.xpath");
                }
                packages.add("org.ietf.jgss");
                packages.add("org.w3c.dom");
                packages.add("org.xml.sax");
                // fall through...
            case 3:
                packages.add("org.omg");
                packages.add("com.sun.corba");
                packages.add("com.sun.jndi");
                packages.add("com.sun.media");
                packages.add("com.sun.naming");
                packages.add("com.sun.org.omg");
                packages.add("com.sun.rmi");
                packages.add("sunw.io");
                packages.add("sunw.util");
                // fall through...
            case 2:
                packages.add("com.sun.java");
                packages.add("com.sun.image");
                // fall through...
            case 1:
            default:
                // core stuff
                packages.add("sun");
                packages.add("java");
                packages.add("javax");
                break;
        }

        return packages.toArray(new String[0]);
    }

    // ---------------------------------------------------------------- java
    // checks

    private int detectJavaVersionNumber() {
        String javaVersion = JAVA_VERSION;

        final int lastDashNdx = javaVersion.lastIndexOf('-');
        if (lastDashNdx != -1) {
            javaVersion = javaVersion.substring(0, lastDashNdx);
        }

        if (javaVersion.startsWith("1.")) {
            // up to java 8
            final int index = javaVersion.indexOf('.', 2);
            return Integer.parseInt(javaVersion.substring(2, index));
        } else {
            final int index = javaVersion.indexOf('.');
            return Integer.parseInt(index == -1 ? javaVersion : javaVersion.substring(0, index));
        }
    }

    /**
     * Checks if the currently running JVM is at least compliant with provided JDK version.
     */
    public boolean isAtLeastJavaVersion(final int version) {
        return JAVA_VERSION_NUMBER >= version;
    }

    /**
     * Checks if the currently running JVM is equal to provided version.
     */
    public boolean isJavaVersion(final int version) {
        return JAVA_VERSION_NUMBER == version;
    }
}
