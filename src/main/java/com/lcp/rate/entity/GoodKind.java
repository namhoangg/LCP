package com.lcp.rate.entity;

import com.lcp.common.EntityBase;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "good_kind")
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodKind extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "good_kind_gen")
    @SequenceGenerator(name = "good_kind_gen", sequenceName = "good_kind_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('good_kind_seq')")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_refrigerated")
    private Boolean isRefrigerated = false;

    @Column(name = "is_default")
    private Boolean isDefault = false;
}
