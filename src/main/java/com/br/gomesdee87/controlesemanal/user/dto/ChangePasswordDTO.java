package com.br.gomesdee87.controlesemanal.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordDTO(
        @NotBlank(message = "A senha temporária atual é obrigatória")
        String currentPassword,

        @NotBlank(message = "A nova senha é obrigatória")
        @Size(min = 12, max = 72, message = "A senha deve ter entre 12 e 72 caracteres")
        String newPassword
) {
}
