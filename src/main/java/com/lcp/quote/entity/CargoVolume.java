package com.lcp.quote.entity;

import com.lcp.common.EntityBase;
import com.lcp.rate.entity.FclRate;
import com.lcp.rate.entity.LclRate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Deprecated
@Table(name = "cargo_volume")
public class CargoVolume extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cargo_volume_gen")
    @SequenceGenerator(name = "cargo_volume_gen", sequenceName = "cargo_volume_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('cargo_volume_seq')")
    private Long id;
    @Column(name = "is_fcl")
    private Boolean isFCL;
    private Long quoteId;
    // FCL
    @Column(name = "total_cont_20dc")
    private Long totalCont20dc;
    @Column(name = "total_cont_40dc")
    private Long totalCont40dc;
    @Column(name = "total_cont_20rf")
    private Long totalCont20rf;
    @Column(name = "total_cont_40rf")
    private Long totalCont40rf;
    @Column(name = "total_cont_20hc")
    private Long totalCont20hc;
    @Column(name = "total_cont_40hc")
    private Long totalCont40hc;
    @Column(name = "total_cont_45hc")
    private Long totalCont45hc;

    // LCL
    private Double totalVolume;
    private Double totalWeight;

    @Column(name = "fcl_rate_id")
    private Long fclRateId;

    @Column(name = "lcl_rate_id")
    private Long lclRateId;

    @OneToOne
    @JoinColumn(name = "fcl_rate_id", referencedColumnName = "id", insertable = false, updatable = false)
    private FclRate fclRate;

    @OneToOne
    @JoinColumn(name = "lcl_rate_id", referencedColumnName = "id", insertable = false, updatable = false)
    private LclRate lclRate;

}
