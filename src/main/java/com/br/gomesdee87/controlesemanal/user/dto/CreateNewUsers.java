package com.br.gomesdee87.controlesemanal.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateNewUsers(
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
        String name,

        @NotBlank(message = "O telefone é obrigatório")
        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
        String telephone,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 12, max = 72, message = "A senha deve ter entre 12 e 72 caracteres")
        String password
) {
}
