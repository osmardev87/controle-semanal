package com.br.gomesdee87.controlesemanal.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Ajuste para seu domínio depois
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<User> criar(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(user));
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
        user.setId(id);
        return ResponseEntity.ok(service.salvar(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}