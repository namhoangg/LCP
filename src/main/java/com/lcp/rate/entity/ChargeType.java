package com.lcp.rate.entity;

import com.lcp.common.EntityBase;
import com.lcp.rate.enumration.CalculationType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "charge_type")
public class ChargeType extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "charge_type_gen")
    @SequenceGenerator(name = "charge_type_gen", sequenceName = "charge_type_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('charge_type_seq')")
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private CalculationType calculationType;
    private String description;
}
