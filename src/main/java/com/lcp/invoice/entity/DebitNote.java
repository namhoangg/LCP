package com.lcp.invoice.entity;

import com.lcp.common.EntityBase;
import com.lcp.common.enumeration.NoteType;
import com.lcp.common.enumeration.PartyType;
import com.lcp.common.enumeration.PaymentMethod;
import com.lcp.shipment.entity.Shipment;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "debit_note")
public class DebitNote extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "debit_gen")
    @SequenceGenerator(name = "debit_gen", sequenceName = "debit_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('debit_seq')")
    private Long id;
    @Enumerated(EnumType.STRING)
    private NoteType noteType;
    private BigDecimal amount;
    private String description;
    private Long invoiceId;
    private Long shipmentId;
}
