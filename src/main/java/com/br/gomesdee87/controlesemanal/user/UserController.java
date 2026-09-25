package com.br.gomesdee87.controlesemanal.user;

import com.br.gomesdee87.controlesemanal.security.ApiSessionInterceptor;
import com.br.gomesdee87.controlesemanal.user.dto.ChangePasswordDTO;
import com.br.gomesdee87.controlesemanal.user.dto.CreateNewUsers;
import com.br.gomesdee87.controlesemanal.user.dto.TemporaryPasswordDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService service;

    @Value("$" + "{app.password-admin-token:}")
    private String passwordAdminToken;
    @PostMapping
    public ResponseEntity<User> criar(@Valid @RequestBody CreateNewUsers request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @GetMapping
    public List<User> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{telephone}")
    public ResponseEntity<User> buscar(@PathVariable String telephone) {
        User user = service.buscarPorTelephone(telephone);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> atualizar(@PathVariable Long id, @RequestBody User user) {
        User existente = service.buscarPorId(id);
        if (existente == null) return ResponseEntity.notFound().build();
        existente.setName(user.getName());
        existente.setTelephone(user.getTelephone());
        return ResponseEntity.ok(service.salvar(existente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

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

    @PostMapping("/admin/{telephone}/temporary-password")
    public ResponseEntity<?> definirSenhaTemporaria(
            @PathVariable String telephone,
            @RequestHeader(value = "X-Password-Admin-Token", required = false) String suppliedToken,
            @Valid @RequestBody TemporaryPasswordDTO request) {
        if (passwordAdminToken == null || passwordAdminToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "A configuração administrativa de senhas não está habilitada."));
        }
        if (suppliedToken == null || !MessageDigest.isEqual(
                passwordAdminToken.getBytes(StandardCharsets.UTF_8),
                suppliedToken.getBytes(StandardCharsets.UTF_8))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Acesso não autorizado."));
        }
        service.definirSenhaTemporaria(telephone, request.password());
        return ResponseEntity.noContent().build();
    }
}
