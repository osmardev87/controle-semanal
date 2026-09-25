package com.br.gomesdee87.controlesemanal.admin;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminAccountRepository extends JpaRepository<AdminAccount, Long> {
    AdminAccount findByUsername(String username);
}