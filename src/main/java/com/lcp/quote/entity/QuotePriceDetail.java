package com.lcp.quote.entity;

import com.lcp.common.EntityBase;
import com.lcp.provider.entity.Provider;
import com.lcp.rate.entity.ChargeType;
import com.lcp.setting.entity.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "quote_price_detail")
public class QuotePriceDetail extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quote_price_gen")
    @SequenceGenerator(name = "quote_price_gen", sequenceName = "quote_price_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('quote_price_seq')")
    private Long id;
    private Long quoteId;
    private Long chargeTypeId;
    private Long providerId;
    private Long quantity;
    private Long currencyId;
    private BigDecimal basePrice;
    private BigDecimal totalPrice;
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quoteId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_quote_price_detail_quote"))
    private Quote quote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chargeTypeId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_quote_price_detail_charge_type"))
    private ChargeType chargeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "providerId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_quote_price_detail_provider"))
    private Provider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_quote_price_detail_currency"))
    private Currency currency;


}
