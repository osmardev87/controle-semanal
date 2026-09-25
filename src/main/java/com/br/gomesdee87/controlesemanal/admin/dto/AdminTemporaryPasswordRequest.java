package com.br.gomesdee87.controlesemanal.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdminTemporaryPasswordRequest(
        @NotBlank @Size(min = 6, max = 72) String password
) { }