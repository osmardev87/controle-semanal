package com.br.gomesdee87.controlesemanal.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TemporaryPasswordDTO(
        @NotBlank(message = "A senha temporária é obrigatória")
        @Size(min = 6, max = 72, message = "A senha temporária deve ter entre 12 e 72 caracteres")
        String password
) {
}
