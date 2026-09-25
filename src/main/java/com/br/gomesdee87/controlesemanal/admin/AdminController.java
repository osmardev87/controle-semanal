package com.br.gomesdee87.controlesemanal.admin;

import com.br.gomesdee87.controlesemanal.admin.dto.AdminLoginRequest;
import com.br.gomesdee87.controlesemanal.admin.dto.AdminSetupRequest;
import com.br.gomesdee87.controlesemanal.admin.dto.AdminTemporaryPasswordRequest;
import com.br.gomesdee87.controlesemanal.admin.dto.AdminUserRequest;
import com.br.gomesdee87.controlesemanal.admin.dto.AdminUserResponse;
import com.br.gomesdee87.controlesemanal.admin.dto.AdminUserUpdateRequest;
import com.br.gomesdee87.controlesemanal.user.User;
import com.br.gomesdee87.controlesemanal.security.AdminSessionInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService service;

    @GetMapping("/setup/status")
    public Map<String, Boolean> setupStatus() {
        return Map.of("setupRequired", service.setupRequired());
    }

    @PostMapping("/setup")
    public ResponseEntity<?> setup(@Valid @RequestBody AdminSetupRequest request, HttpServletRequest servletRequest) {
        AdminAccount admin = service.createFirstAdmin(request.setupCode(), request.username(), request.password());
        HttpSession session = newAdminSession(servletRequest, admin);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("username", admin.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AdminLoginRequest request, HttpServletRequest servletRequest) {
        AdminAccount admin = service.authenticate(request.username(), request.password());
        if (admin == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Usuário ou senha inválidos."));
        newAdminSession(servletRequest, admin);
        return ResponseEntity.ok(Map.of("username", admin.getUsername()));
    }

    @GetMapping("/session")
    public Map<String, String> session(@SessionAttribute(AdminSessionInterceptor.ADMIN_ID_ATTRIBUTE) Long adminId,
                                       HttpSession session) {
        return Map.of("username", String.valueOf(session.getAttribute(AdminSessionInterceptor.ADMIN_USERNAME_ATTRIBUTE)));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users")
    public List<AdminUserResponse> listUsers() {
        return service.listUsers().stream().map(AdminUserResponse::from).toList();
    }

    @PostMapping("/users")
    public ResponseEntity<AdminUserResponse> createUser(@Valid @RequestBody AdminUserRequest request) {
        User user = service.createUser(new com.br.gomesdee87.controlesemanal.user.dto.CreateNewUsers(
                request.name(), request.telephone(), request.password()));
        return ResponseEntity.status(HttpStatus.CREATED).body(AdminUserResponse.from(user));
    }

    @PutMapping("/users/{id}")
    public AdminUserResponse updateUser(@PathVariable Long id, @Valid @RequestBody AdminUserUpdateRequest request) {
        return AdminUserResponse.from(service.updateUser(id, request.name(), request.telephone()));
    }

    @PostMapping("/users/{telephone}/temporary-password")
    public ResponseEntity<Void> setTemporaryPassword(@PathVariable String telephone,
            @Valid @RequestBody AdminTemporaryPasswordRequest request) {
        service.setTemporaryPassword(telephone, request.password());
        return ResponseEntity.noContent().build();
    }

    private HttpSession newAdminSession(HttpServletRequest request, AdminAccount admin) {
        HttpSession previous = request.getSession(false);
        if (previous != null) previous.invalidate();
        HttpSession session = request.getSession(true);
        session.setAttribute(AdminSessionInterceptor.ADMIN_ID_ATTRIBUTE, admin.getId());
        session.setAttribute(AdminSessionInterceptor.ADMIN_USERNAME_ATTRIBUTE, admin.getUsername());
        return session;
    }
}