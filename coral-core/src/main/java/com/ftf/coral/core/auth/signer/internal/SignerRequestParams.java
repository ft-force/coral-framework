package com.ftf.coral.core.auth.signer.internal;

public final class SignerRequestParams {

    private final String signingAlgorithm;

    private final long signingDateTimeMilli;

    private final String formattedSigningDateTime;

    public SignerRequestParams(String signingAlgorithm) {

        if (signingAlgorithm == null) {
            throw new IllegalArgumentException("Signing Algorithm cannot be null");
        }

        this.signingAlgorithm = signingAlgorithm;
        this.signingDateTimeMilli = System.currentTimeMillis();
        this.formattedSigningDateTime = SignerUtils.formatTimestamp(signingDateTimeMilli);
    }

    public String getSigningAlgorithm() {
        return signingAlgorithm;
    }

    public long getSigningDateTimeMilli() {
        return signingDateTimeMilli;
    }

    public String getFormattedSigningDateTime() {
        return formattedSigningDateTime;
    }
}