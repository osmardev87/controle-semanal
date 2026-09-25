package com.br.gomesdee87.controlesemanal.finance;

import com.br.gomesdee87.controlesemanal.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "finance", uniqueConstraints = @UniqueConstraint(name = "uk_finance_user_client_id", columnNames = {"user_id", "client_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Finace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimento type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Categoria category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FormaPagamento payment;

    @Column(name = "client_id", length = 36)
    private String clientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}