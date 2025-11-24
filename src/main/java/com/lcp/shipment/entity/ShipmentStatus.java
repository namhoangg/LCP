package com.lcp.shipment.entity;

import com.lcp.common.EntityBase;
import com.lcp.common.enumeration.ShipmentStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "shipment_status")
public class ShipmentStatus extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shipment_status_gen")
    @SequenceGenerator(name = "shipment_status_gen", sequenceName = "shipment_status_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('shipment_status_seq')")
    private Long id;
    @Enumerated(EnumType.STRING)
    private ShipmentStatusEnum shipmentStatus;
    private LocalDate eta;
    private LocalDate ata;
    private String note;

}
