package com.br.gomesdee87.controlesemanal.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.br.gomesdee87.controlesemanal.exception.UserNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

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
}