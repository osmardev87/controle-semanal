package com.br.gomesdee87.controlesemanal.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String telephone) {
        super("Usuário não encontrado com o telefone: " + telephone);
    }
}