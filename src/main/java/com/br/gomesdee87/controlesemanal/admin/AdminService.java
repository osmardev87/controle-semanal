package com.br.gomesdee87.controlesemanal.admin;

import com.br.gomesdee87.controlesemanal.user.User;
import com.br.gomesdee87.controlesemanal.user.UserRepository;
import com.br.gomesdee87.controlesemanal.user.UserService;
import com.br.gomesdee87.controlesemanal.user.dto.CreateNewUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private static final long ADMIN_ACCOUNT_ID = 1L;
    private final AdminAccountRepository adminRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AdminSetupCode setupCode;

    public boolean setupRequired() { return !adminRepository.existsById(ADMIN_ACCOUNT_ID); }

    @Transactional
    public synchronized AdminAccount createFirstAdmin(String code, String username, String password) {
        if (adminRepository.existsById(ADMIN_ACCOUNT_ID)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A configuração inicial já foi concluída.");
        }
        if (!setupCode.matches(code)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Código inicial inválido. Confira os logs atuais do servidor.");
        }
        validatePassword(password);
        AdminAccount admin = new AdminAccount();
        admin.setId(ADMIN_ACCOUNT_ID);
        admin.setUsername(username.trim());
        admin.setPasswordHash(passwordEncoder.encode(password));
        adminRepository.saveAndFlush(admin);
        setupCode.consume();
        return admin;
    }

    public AdminAccount authenticate(String username, String password) {
        AdminAccount admin = adminRepository.findByUsername(username.trim());
        if (admin == null || !passwordEncoder.matches(password, admin.getPasswordHash())) return null;
        return admin;
    }

    public List<User> listUsers() { return userRepository.findAll(); }

    public User createUser(CreateNewUsers request) {
        validatePassword(request.password());
        return userService.criarContaAdministrada(request);
    }

    public User updateUser(Long id, String name, String telephone) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada."));
        user.setName(name.trim());
        user.setTelephone(telephone.trim());
        return userRepository.save(user);
    }

    public void setTemporaryPassword(String telephone, String password) {
        validatePassword(password);
        userService.definirSenhaTemporaria(telephone, password);
    }

    private void validatePassword(String password) {
        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha deve ter no máximo 72 bytes.");
        }
    }
}