package com.lcp.exchangerate.entity;

import com.lcp.common.EntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "exchange_rate")
public class ExchangeRate extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exchange_rate_gen")
    @SequenceGenerator(name = "exchange_rate_gen", sequenceName = "exchange_rate_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('exchange_rate_seq')")
    private Long id;
    private Long fromCurrencyId;
    private Long toCurrencyId;
    private BigDecimal transferRate;
    private LocalDate createdDate;
}
