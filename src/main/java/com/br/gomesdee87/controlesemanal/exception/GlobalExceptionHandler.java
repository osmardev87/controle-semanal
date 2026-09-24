package com.br.gomesdee87.controlesemanal.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ Tratar o erro de usuário não encontrado
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("timestamp", LocalDateTime.now().toString());
        erro.put("status", HttpStatus.NOT_FOUND.value());
        erro.put("error", "Não Encontrado");
        erro.put("message", ex.getMessage()); // ✅ Mensagem: "Usuário não encontrado com o telefone: 75998013546"
        erro.put("path", "/api/users");

        return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
    }

    // ✅ Tratar qualquer outro erro genérico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericError(Exception ex) {
        Map<String, Object> erro = new HashMap<>();
        erro.put("timestamp", LocalDateTime.now().toString());
        erro.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        erro.put("error", "Erro Interno");
        erro.put("message", ex.getMessage());

        return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}