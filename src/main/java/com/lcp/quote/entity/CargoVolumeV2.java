package com.lcp.quote.entity;

import com.lcp.common.EntityBase;
import com.lcp.rate.entity.FclRate;
import com.lcp.rate.entity.LclRate;
import com.lcp.util.BigDecimalUtil;
import com.lcp.util.MathUtil;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

import javax.persistence.*;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "cargo_volume_v2")
public class CargoVolumeV2 extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cargo_volume_v2_gen")
    @SequenceGenerator(name = "cargo_volume_v2_gen", sequenceName = "cargo_volume_v2_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('cargo_volume_v2_seq')")
    private Long id;

    private Long quoteId;

    private Long containerTypeId;

    // Base price for 1 container
    private BigDecimal basePrice;

    private BigDecimal profitRate;

}
