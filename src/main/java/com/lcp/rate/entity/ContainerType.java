package com.lcp.rate.entity;

import com.lcp.common.EntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "container_type")
public class ContainerType extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "container_type_gen")
    @SequenceGenerator(name = "container_type_gen", sequenceName = "container_type_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('container_type_seq')")
    private Long id;
    private String name;
    private BigDecimal tareWeight;
    private BigDecimal maxWeight;
    private BigDecimal maxVolume;
    private BigDecimal height;
    private BigDecimal width;
    private BigDecimal length;
    private Boolean isRefrigerated;
}
