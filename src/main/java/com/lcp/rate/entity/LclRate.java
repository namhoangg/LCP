package com.lcp.rate.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "lcl_rate")
public class LclRate extends Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lcl_rate_gen")
    @SequenceGenerator(name = "lcl_rate_gen", sequenceName = "lcl_rate_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('lcl_rate_seq')")
    private Long id;

    private Double minWeight;
    private Double maxWeight; // null if no limit
    private Double minVolume;
    private Double maxVolume; // null if no limit
    private Double pricePerWeight;
    private Double pricePerVolume;
    private Double minimumCharge;

    private Double volumetricFactor = 333.0;
}
