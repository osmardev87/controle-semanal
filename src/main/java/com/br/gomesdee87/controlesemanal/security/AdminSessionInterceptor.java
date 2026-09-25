package com.br.gomesdee87.controlesemanal.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminSessionInterceptor implements HandlerInterceptor {
    public static final String ADMIN_ID_ATTRIBUTE = "authenticatedAdminId";
    public static final String ADMIN_USERNAME_ATTRIBUTE = "authenticatedAdminUsername";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var session = request.getSession(false);
        if (session != null && session.getAttribute(ADMIN_ID_ATTRIBUTE) != null) return true;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\":\"Entre com a conta administrativa.\"}");
        return false;
    }
}