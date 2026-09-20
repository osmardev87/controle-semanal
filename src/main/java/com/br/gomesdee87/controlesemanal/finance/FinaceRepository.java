package com.br.gomesdee87.controlesemanal.finance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FinaceRepository extends JpaRepository<Finace, Long> {

    List<Finace> findByDateBetween(LocalDate inicio, LocalDate fim);
    List<Finace> findByDateBetween1(LocalDate inicioDoMes, LocalDate fimDoMes);
}