package com.br.gomesdee87.controlesemanal.finance.dto;

import com.br.gomesdee87.controlesemanal.finance.Categoria;
import com.br.gomesdee87.controlesemanal.finance.FormaPagamento;
import com.br.gomesdee87.controlesemanal.finance.TipoMovimento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinaceResponseDTO(

        Long id,

        String description,

        BigDecimal price,

        LocalDate date,

        TipoMovimento type,

        Categoria category,

        FormaPagamento payment,
        String clientId
) {
}