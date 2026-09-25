package com.br.gomesdee87.controlesemanal.finance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FinaceRepository extends JpaRepository<Finace, Long> {
    List<Finace> findByUser_IdOrderByDateDescIdDesc(Long userId);

    Optional<Finace> findByUser_IdAndClientId(Long userId, String clientId);

    List<Finace> findByUser_IdAndDateBetweenOrderByDateDescIdDesc(Long userId, LocalDate inicio, LocalDate fim);

    @Modifying
    @Query("delete from Finace f where f.id = :id and f.user.id = :userId")
    int deleteByIdForUser(@Param("id") Long id, @Param("userId") Long userId);

    @Modifying
    @Query("delete from Finace f where f.user.id = :userId")
    int deleteAllForUser(@Param("userId") Long userId);
}