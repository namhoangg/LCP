package com.lcp.rate.entity;

import com.lcp.common.EntityBase;
import com.lcp.common.enumeration.TransportType;
import com.lcp.provider.entity.Provider;
import com.lcp.setting.entity.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "service_charge")
public class ServiceCharge extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_charge_gen")
    @SequenceGenerator(name = "service_charge_gen", sequenceName = "service_charge_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('service_charge_seq')")
    private Long id;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Long chargeTypeId;
    private Long currencyId;
    private BigDecimal price;
    private Long providerId;

    @OneToOne
    @JoinColumn(name = "providerId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_service_charge_provider"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chargeTypeId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_service_charge_charge_type"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ChargeType chargeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_service_charge_currency"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Currency currency;
}
