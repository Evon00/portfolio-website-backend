package com.example.portfolio_website_backend.common.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * Client 정보 추출 클래스
 *
 * RequestHeader에서 IP, referer, userAgent를 추출한다.
 *
 */
@Component
public class ClientInfoExtractor {

    private static final String[] IP_HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR",
            "X-Real-IP"
    };

    public String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = null;

        // 프록시 헤더들을 순서대로 확인
        for (String header : IP_HEADERS) {
            ipAddress = request.getHeader(header);
            if (isValidIpAddress(ipAddress)) {
                break;
            }
        }

        // 헤더에서 찾지 못했으면 직접 연결 IP 사용
        if (!isValidIpAddress(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        // 여러 IP가 콤마로 구분된 경우 첫 번째(클라이언트) IP 사용
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }

        return ipAddress != null ? ipAddress : "unknown";
    }

    private boolean isValidIpAddress(String ip) {
        return ip != null &&
                !ip.isEmpty() &&
                !"unknown".equalsIgnoreCase(ip) &&
                !"null".equalsIgnoreCase(ip);
    }

    public String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent != null ? userAgent : "unknown";
    }

    public String getReferer(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return referer != null ? referer : "";
    }
}
