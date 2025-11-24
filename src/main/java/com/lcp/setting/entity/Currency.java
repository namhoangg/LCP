package com.lcp.setting.entity;

import com.lcp.common.EntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "currency")
public class Currency extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_gen")
    @SequenceGenerator(name = "currency_gen", sequenceName = "currency_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('currency_seq')")
    private Long id;
    private String code;
    private String name;
    private String symbol;
}
