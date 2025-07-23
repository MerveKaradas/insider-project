package com.web.demo.util;

import org.springframework.stereotype.Component;

@Component
public class GlobalContext {

    private static final ThreadLocal<String> currentUsername = new ThreadLocal<>();
    private static final ThreadLocal<String> ipAddress = new ThreadLocal<>();
    private static final ThreadLocal<String> userAgent = new ThreadLocal<>();

    public static void set(String username, String ip, String agent) {
        currentUsername.set(username);
        ipAddress.set(ip);
        userAgent.set(agent);
    }

    public static String getCurrentUsername() {
        return currentUsername.get();
    }

    public static String getIpAddress() {
        return ipAddress.get();
    }

    public static String getUserAgent() {
        return userAgent.get();
    }

    public static void clear() {
        currentUsername.remove();
        ipAddress.remove();
        userAgent.remove();
    }
}
