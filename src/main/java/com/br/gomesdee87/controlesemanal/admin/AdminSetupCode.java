package com.br.gomesdee87.controlesemanal.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AdminSetupCode implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(AdminSetupCode.class);
    private final AdminAccountRepository repository;
    private final SecureRandom random = new SecureRandom();
    private volatile String setupCode;

    public AdminSetupCode(AdminAccountRepository repository) { this.repository = repository; }

    @Override
    public void run(ApplicationArguments args) {
        if (!repository.existsById(1L)) {
            byte[] bytes = new byte[32];
            random.nextBytes(bytes);
            setupCode = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            log.warn("ADMIN SETUP ONE-TIME CODE (copy from protected server logs): {}", setupCode);
        }
    }

    public boolean matches(String supplied) {
        String current = setupCode;
        return current != null && supplied != null && java.security.MessageDigest.isEqual(
                current.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                supplied.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    public void consume() { setupCode = null; }
    public boolean available() { return setupCode != null; }
}