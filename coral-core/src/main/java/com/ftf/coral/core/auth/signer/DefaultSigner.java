package com.ftf.coral.core.auth.signer;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ftf.coral.core.auth.AuthManager;
import com.ftf.coral.core.auth.credentials.Credential;
import com.ftf.coral.core.auth.signer.internal.SignerConstants;
import com.ftf.coral.core.auth.signer.internal.SignerRequestParams;
import com.ftf.coral.core.exception.AuthException;
import com.ftf.coral.core.http.Request;
import com.ftf.coral.util.CollectionUtils;
import com.ftf.coral.util.StringUtils;

public class DefaultSigner extends AbstractSigner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSigner.class);

    @Override
    public void sign(Request<?> request, List<String> bizHeaders, Credential credentials) {

        final SignerRequestParams signerParams = new SignerRequestParams(SignerConstants.HMAC_SHA256);

        List<String> signedHeaders = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(bizHeaders)) {
            bizHeaders.forEach(bizHeader -> {
                signedHeaders.add(bizHeader);
            });
        }

        request.addHeader(SignerConstants.X_DATE, signerParams.getFormattedSigningDateTime());
        signedHeaders.add(SignerConstants.X_DATE);

        String digest = this.calculateContentDigest(request);
        if (null != digest) {
            request.addHeader(SignerConstants.DIGEST, digest);
            signedHeaders.add(SignerConstants.DIGEST);
        }

        signedHeaders.add(SignerConstants.REQUEST_LINE);

        // 创建待签名的字符串
        final String stringToSign = createStringToSign(request, signedHeaders);

        request.addHeader(SignerConstants.AUTHORIZATION,
                        buildAuthorizationHeader(request, stringToSign, credentials, signerParams, signedHeaders));
    }

    @Override
    public boolean verify(Request<?> request) {

        String authorizationToken = request.getHeaders().get(SignerConstants.AUTHORIZATION);
        if (StringUtils.isBlank(authorizationToken)) {
            LOGGER.debug("No Authorization header found");
            return false;
        }

        AuthorizationData authorizationData = AuthorizationData.parse(authorizationToken);

        // 校验凭证是否存在
        Credential credential = AuthManager.getCredential(authorizationData.getUsername());
        if (credential == null) {
            LOGGER.debug("Credential not exists.");
            return false;
        }

        for (String header : authorizationData.getHeaderItems()) {
            if (SignerConstants.X_DATE.equalsIgnoreCase(header)) {
                // 获取日期(HTTP规范头部为小写)
                String singerDate = request.getHeaders().get(SignerConstants.X_DATE.toLowerCase());

            } else if (SignerConstants.DIGEST.equalsIgnoreCase(header)) {
                // 获取日期(HTTP规范头部为小写)
                String digest = request.getHeaders().get(SignerConstants.DIGEST.toLowerCase());
            }
        }

        // 创建待签名的字符串
        final String stringToSign = createStringToSign(request, Arrays.asList(authorizationData.getHeaderItems()));

        byte[] hmacsha256 = HmacUtils.hmacSha256(credential.getSecretAccessKey(), stringToSign);

        String signature = Base64.encodeBase64String(hmacsha256);

        boolean verifyResult = signature.equals(authorizationData.getSignature());

        if (verifyResult) {
            // 将应用信息放入当前线程上下文
            AuthManager.putCurrentAppId(credential.getAppId());
        }

        return verifyResult;
    }

    // Digest签名规则 SHA-256=base64(sha256(<body>))
    protected String calculateContentDigest(Request<?> request) {

        switch (request.getHttpMethod()) {
            case PUT:
            case PATCH:
            case POST:

                byte[] payloadBytes = getBinaryRequestPayload(request);

                try {
                    byte[] sha256n;
                    if (null != payloadBytes) {
                        sha256n = getSHA256(payloadBytes);
                        String sha256AndBase64 = Base64.encodeBase64String(sha256n);
                        return "SHA-256=" + sha256AndBase64;
                    }
                } catch (Exception e) {
                    throw new AuthException("calculating body digest error", (Throwable) e);
                }
                break;

            default:
                break;
        }

        return null;
    }

    protected String createStringToSign(Request<?> request, List<String> signedHeaders) {
        StringBuilder sb = new StringBuilder();
        for (String key : signedHeaders) {
            if (!SignerConstants.REQUEST_LINE.equals(key)) {
                String value = request.getHeaders().get(key);
                sb.append(key).append(": ").append(value).append("\n");
            }
        }

        String queryString = this.getCanonicalizedQueryString(request);
        String uri = null;
        if ("".equals(queryString)) {
            uri = request.getResourcePath();
        } else {
            uri = request.getResourcePath() + "?" + queryString;
        }
        sb.append(request.getHttpMethod().name()).append(" ").append(uri).append(" HTTP/1.1");

        return sb.toString();
    }

    private String buildAuthorizationHeader(Request<?> request, String stringToSign, Credential credentials,
                    SignerRequestParams signerParams, List<String> signedHeaders) {

        byte[] hmacsha256 = HmacUtils.hmacSha256(credentials.getSecretAccessKey(), stringToSign);

        String signature = Base64.encodeBase64String(hmacsha256);

        String signedHeaderStr = StringUtils.joinWithDelimiter(" ",
                        signedHeaders.toArray(new String[signedHeaders.size()]));

        StringBuilder authHeaderBuilder = new StringBuilder();
        authHeaderBuilder.append("hmac").append(" ").append("username=\"").append(credentials.getAccessKeyId())
                        .append("\", algorithm=\"").append(signerParams.getSigningAlgorithm()).append("\", headers=\"")
                        .append(signedHeaderStr).append("\",signature=\"").append(signature).append("\"");

        return authHeaderBuilder.toString();
    }

    public static byte[] getSHA256(byte[] str) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str);
            return messageDigest.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}