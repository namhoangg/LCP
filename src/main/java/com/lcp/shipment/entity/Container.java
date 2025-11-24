package com.lcp.shipment.entity;

import com.lcp.common.EntityBase;
import com.lcp.rate.entity.ContainerType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "container")
public class Container extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "container_gen")
    @SequenceGenerator(name = "container_gen", sequenceName = "container_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('container_seq')")
    private Long id;
    private Long shipmentId;
    private String containerNumber;
    private Long containerTypeId;
    private Integer minTemp;
    private Integer maxTemp;

    private String sealNumber;
    private BigDecimal netWeight;
    private BigDecimal grossWeight;
    private BigDecimal volume;
    private String note;

    @ManyToOne
    @JoinColumn(name = "shipmentId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_container_shipment"))
    private Shipment shipment;

    @ManyToOne
    @JoinColumn(name = "containerTypeId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_container_container_type"))
    private ContainerType containerType;

    public String getContainerName() {
        return containerType != null ? containerType.getName() : "";
    }
}
