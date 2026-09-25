package com.br.gomesdee87.controlesemanal.user;

import com.br.gomesdee87.controlesemanal.exception.UserNotFoundException;
import com.br.gomesdee87.controlesemanal.user.dto.ChangePasswordDTO;
import com.br.gomesdee87.controlesemanal.user.dto.CreateNewUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public User criar(CreateNewUsers request) {
        validarTamanhoSenha(request.password());
        User user = new User();
        user.setName(request.name());
        user.setTelephone(request.telephone());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setPasswordChangeRequired(false);
        return repository.save(user);
    }

    public User autenticar(String telephone, String password) {
        User user = repository.findByTelephone(telephone);
        if (user == null || user.getPasswordHash() == null ||
                !passwordEncoder.matches(password, user.getPasswordHash())) {
            return null;
        }
        return user;
    }

    public boolean trocarSenha(Long userId, ChangePasswordDTO request) {
        User user = repository.findById(userId).orElse(null);
        if (user == null || user.getPasswordHash() == null ||
                !passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            return false;
        }
        validarTamanhoSenha(request.newPassword());
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        user.setPasswordChangeRequired(false);
        repository.save(user);
        return true;
    }

    public User definirSenhaTemporaria(String telephone, String password) {
        validarTamanhoSenha(password);
        User user = buscarPorTelephone(telephone);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setPasswordChangeRequired(true);
        return repository.save(user);
    }

    public User salvar(User user) {
        return repository.save(user);
    }

    public List<User> listarTodos() {
        return repository.findAll();
    }

    public User buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public User buscarPorTelephone(String telephone) {
        if (!repository.existsByTelephone(telephone)) {
            throw new UserNotFoundException(telephone);
        }
        return repository.findByTelephone(telephone);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    private void validarTamanhoSenha(String password) {
        if (password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException("A senha excede o limite de 72 bytes do BCrypt.");
        }
    }
}
