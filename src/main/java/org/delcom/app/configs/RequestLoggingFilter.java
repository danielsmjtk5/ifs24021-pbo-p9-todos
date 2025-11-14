package org.delcom.app.configs;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @SuppressWarnings("unused")
    private int port;

    @SuppressWarnings("unused")
    private boolean livereload;

    private static final String RESET  = "\u001B[0m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED    = "\u001B[31m";
    private static final String CYAN   = "\u001B[36m";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        long start = System.currentTimeMillis();

        filterChain.doFilter(request, response);

        long duration = System.currentTimeMillis() - start;
        int status = response.getStatus();

        String color = getColorForStatus(status);

        String originInfo = getOriginFromStack();

        String remoteAddr = request.getRemoteAddr();

        // Skip well-known requests
        if (request.getRequestURI().startsWith("/.well-known")) {
            return;
        }

        System.out.printf(
                "%s%-6s %s %d %dms%s [%s] from %s%n",
                color,
                request.getMethod(),
                request.getRequestURI(),
                status,
                duration,
                RESET,
                originInfo,
                remoteAddr
        );
    }

    private String getColorForStatus(int status) {
        if (status >= 500) return RED;
        if (status >= 400) return YELLOW;
        if (status >= 200) return GREEN;
        return CYAN;
    }

    private String getOriginFromStack() {
        return Arrays.stream(Thread.currentThread().getStackTrace())
                .filter(s -> s.getClassName().startsWith("org.delcom"))
                .findFirst()
                .map(s -> s.getClassName() + "." + s.getMethodName() + ":" + s.getLineNumber())
                .orElse("unknown");
    }
}
