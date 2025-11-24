package com.lcp.rate.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "fcl_rate_detail")
public class FclRateDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fcl_rate_detail_gen")
    @SequenceGenerator(name = "fcl_rate_detail_gen", sequenceName = "fcl_rate_detail_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('fcl_rate_detail_seq')")
    private Long id;
    
    private Long fclRateId;
    private Long containerTypeId;

    @Column(precision = 10, scale = 4)
    private BigDecimal rate;

     @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "containerTypeId", referencedColumnName = "id", insertable = false, updatable = false)
    private ContainerType containerType;

    public String getContainerTypeName() {
        return containerType != null ? containerType.getName() : "";
    }
}
