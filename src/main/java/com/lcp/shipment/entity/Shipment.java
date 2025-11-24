package com.lcp.shipment.entity;

import com.lcp.common.EntityBase;
import com.lcp.quote.entity.Quote;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "shipment")
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class Shipment extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shipment_gen")
    @SequenceGenerator(name = "shipment_gen", sequenceName = "shipment_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('shipment_seq')")
    private Long id;
    private String code;
    private Long quoteId;
    private LocalDate etd;
    private LocalDate eta;
    private Long shipmentStatusId;
    private Boolean isOnTime = false;
    private String note;

    @Column(name = "attachment_ids", columnDefinition = "text")
    private String attachmentIds = "";

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipmentStatusId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_shipment_shipment_status"))
    private ShipmentStatus shipmentStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quoteId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_shipment_quote"))
    private Quote quote;

    @OneToMany
    @JoinColumn(name = "shipmentId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_shipment_container"))
    private List<Container> containers = new ArrayList<>();
}
