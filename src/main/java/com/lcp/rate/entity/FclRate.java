package com.lcp.rate.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "fcl_rate")
public class FclRate extends Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fcl_rate_gen")
    @SequenceGenerator(name = "fcl_rate_gen", sequenceName = "fcl_rate_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('fcl_rate_seq')")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "fclRateId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_fcl_rate_fcl_rate_detail"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<FclRateDetail> details;
}
