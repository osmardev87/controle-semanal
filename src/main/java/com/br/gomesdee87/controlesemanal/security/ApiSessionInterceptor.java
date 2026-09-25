package com.br.gomesdee87.controlesemanal.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiSessionInterceptor implements HandlerInterceptor {
    public static final String USER_ID_ATTRIBUTE = "authenticatedUserId";
    public static final String PASSWORD_CHANGE_REQUIRED_ATTRIBUTE = "passwordChangeRequired";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var session = request.getSession(false);
        if (session == null || session.getAttribute(USER_ID_ATTRIBUTE) == null) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "Autenticação necessária");
            return false;
        }

        if (session.getAttribute(PASSWORD_CHANGE_REQUIRED_ATTRIBUTE) == null) {
            session.invalidate();
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "Entre novamente com telefone e senha");
            return false;
        }

        if (Boolean.TRUE.equals(session.getAttribute(PASSWORD_CHANGE_REQUIRED_ATTRIBUTE))) {
            String path = request.getRequestURI();
            boolean allowed = path.endsWith("/finace/session") ||
                    path.endsWith("/finace/logout") ||
                    path.endsWith("/api/users/me/password");
            if (!allowed) {
                writeError(response, HttpServletResponse.SC_FORBIDDEN, "Troque a senha temporária para continuar");
                return false;
            }
        }
        return true;
    }

    private void writeError(HttpServletResponse response, int status, String message) throws Exception {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
    }
}
