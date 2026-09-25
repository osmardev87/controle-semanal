package com.br.gomesdee87.controlesemanal.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdminUserUpdateRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Size(max = 20) String telephone
) { }