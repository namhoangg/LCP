package com.lcp.markup.entity;

import com.lcp.common.EntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "price_markup")
public class PriceMarkup extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "price_markup_gen")
    @SequenceGenerator(name = "price_markup_gen", sequenceName = "price_markup_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('price_markup_seq')")
    private Long id;
    private String name;
    private String description;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "priceMarkupId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_price_markup_price_markup_detail"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<PriceMarkupDetail> priceMarkupDetails;
}
