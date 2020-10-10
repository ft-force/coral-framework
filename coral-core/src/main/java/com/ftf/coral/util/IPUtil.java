package com.ftf.coral.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IPUtil {

    private static Logger logger = LoggerFactory.getLogger(IPUtil.class);

    /**
     * 获取本地IP
     * 
     * @return String 本地IP
     */
    @SuppressWarnings("rawtypes")
    public static String getLocalIp() {
        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            boolean finded = false;// 是否找到外网IP
            while (netInterfaces.hasMoreElements() && !finded) {
                NetworkInterface ni = (NetworkInterface)netInterfaces.nextElement();
                Enumeration address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = (InetAddress)address.nextElement();
                    if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
                        netip = ip.getHostAddress();
                        finded = true;
                        break;
                    } else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
                        localip = ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            logger.info("err context", e);
        }

        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }

    public static String getClientIp(HttpServletRequest request) {

        HashMap<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String c = headerNames.nextElement();
            if ("X-Forwarded-For".equals(c)) {
                if (!headers.containsKey("X-Forwarded-For")) {
                    headers.put(c, request.getHeader(c));
                }
            } else {
                headers.put(c, request.getHeader(c));
            }
        }
        String clientIp = headers.get("X-Forwarded-For");
        if (clientIp == null) {
            clientIp = request.getRemoteHost();
        }

        return clientIp;
    }
}
