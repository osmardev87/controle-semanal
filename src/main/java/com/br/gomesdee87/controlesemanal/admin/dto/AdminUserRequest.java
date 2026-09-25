package com.br.gomesdee87.controlesemanal.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdminUserRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Size(max = 20) String telephone,
        @NotBlank @Size(min = 6, max = 72) String password
) { }