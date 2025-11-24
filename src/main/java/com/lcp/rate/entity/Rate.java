package com.lcp.rate.entity;

import com.lcp.common.EntityBase;
import com.lcp.common.enumeration.TransportType;
import com.lcp.provider.entity.Provider;
import com.lcp.setting.entity.Currency;
import com.lcp.setting.entity.Unloco;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@Data
public class Rate extends EntityBase {
    @Enumerated(EnumType.STRING)
    private TransportType transportType;
    private Long providerId;
    private Long originId;
    private Long destinationId;
    private Long transitTime;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Long currencyId;
    private String remark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "providerId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_rate_provider"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "originId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_rate_origin"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Unloco origin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinationId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_rate_destination"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Unloco destination;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_rate_currency"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Currency currency;
}