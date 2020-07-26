package com.ftf.coral.core.auth.signer;

import java.util.List;

import com.ftf.coral.core.auth.credentials.Credential;
import com.ftf.coral.core.http.Request;

public interface Signer {

    void sign(Request<?> request, List<String> bizHeaders, Credential credentials);

    boolean verify(Request<?> request);
}