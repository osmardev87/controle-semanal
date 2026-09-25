package com.br.gomesdee87.controlesemanal.user;

import com.br.gomesdee87.controlesemanal.security.ApiSessionInterceptor;
import com.br.gomesdee87.controlesemanal.user.dto.ChangePasswordDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/me/password")
    public ResponseEntity<?> trocarMinhaSenha(
            @Valid @RequestBody ChangePasswordDTO request,
            @SessionAttribute(ApiSessionInterceptor.USER_ID_ATTRIBUTE) Long userId,
            @SessionAttribute(value = ApiSessionInterceptor.PASSWORD_CHANGE_REQUIRED_ATTRIBUTE, required = false)
            Boolean passwordChangeRequired,
            jakarta.servlet.http.HttpSession session) {
        if (!service.trocarSenha(userId, request)) {
            String error = Boolean.TRUE.equals(passwordChangeRequired)
                    ? "A senha temporária não confere."
                    : "A senha atual não confere.";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", error));
        }
        session.setAttribute(ApiSessionInterceptor.PASSWORD_CHANGE_REQUIRED_ATTRIBUTE, false);
        return ResponseEntity.noContent().build();
    }
}