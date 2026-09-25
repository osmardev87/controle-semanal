package com.br.gomesdee87.controlesemanal.finance.dto;

import com.br.gomesdee87.controlesemanal.finance.Categoria;
import com.br.gomesdee87.controlesemanal.finance.FormaPagamento;
import com.br.gomesdee87.controlesemanal.finance.TipoMovimento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinaceRequestDTO(

        @NotBlank(message = "A descrição é obrigatória")
        @Size(max = 100, message = "A descrição deve ter no máximo 100 caracteres")
        String description,

        @NotNull(message = "O valor é obrigatório")
        @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
        BigDecimal price,

        @NotNull(message = "A data é obrigatória")
        LocalDate date,

        @NotNull(message = "O tipo de movimento é obrigatório")
        TipoMovimento type,

        @NotNull(message = "A categoria é obrigatória")
        Categoria category,

        @NotNull(message = "A forma de pagamento é obrigatória")
        FormaPagamento payment,
        @Size(max = 36)
        String clientId
) {
}


