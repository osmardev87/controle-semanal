package com.br.gomesdee87.controlesemanal.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Buscar por telefone (login)
    User findByTelephone(String telephone);
    // Verificar se número já existe
    boolean existsByTelephone(String telephone);
}