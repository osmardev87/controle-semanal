package com.br.gomesdee87.controlesemanal.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdminSetupRequest(
        @NotBlank String setupCode,
        @NotBlank @Size(min = 3, max = 64) String username,
        @NotBlank @Size(min = 6, max = 72) String password
) { }