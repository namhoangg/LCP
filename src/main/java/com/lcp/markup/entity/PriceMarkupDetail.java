package com.lcp.markup.entity;

import com.lcp.common.EntityBase;
import com.lcp.rate.entity.ContainerType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "price_markup_detail")
public class PriceMarkupDetail extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "price_markup_detail_gen")
    @SequenceGenerator(name = "price_markup_detail_gen", sequenceName = "price_markup_detail_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('price_markup_detail_seq')")
    private Long id;
    @Column(precision = 10, scale = 4)
    private BigDecimal markupRate;
    private Long priceMarkupId;
    private Long containerTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "containerTypeId", referencedColumnName = "id", insertable = false, updatable = false)
    private ContainerType containerType;

    public String getContainerTypeName() {
        return containerType != null ? containerType.getName() : "";
    }
}
