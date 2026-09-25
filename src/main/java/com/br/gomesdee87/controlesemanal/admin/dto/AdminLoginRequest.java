package com.br.gomesdee87.controlesemanal.admin.dto;

import jakarta.validation.constraints.NotBlank;

public record AdminLoginRequest(
        @NotBlank String username,
        @NotBlank String password
) { }