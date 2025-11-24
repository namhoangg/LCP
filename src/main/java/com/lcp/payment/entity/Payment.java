package com.lcp.payment.entity;

import com.lcp.common.EntityBase;
import com.lcp.common.entity.Document;
import com.lcp.common.enumeration.PaymentMethod;
import com.lcp.common.enumeration.PaymentStatus;
import com.lcp.invoice.entity.Invoice;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "payment")
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class Payment extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_gen")
    @SequenceGenerator(name = "payment_gen", sequenceName = "payment_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('payment_seq')")
    private Long id;
    private Long invoiceId;
    private Long shipmentId;
    private String code;
    private BigDecimal amount;
    private BigDecimal fee;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    private String transactionId;
    private String approvalUrl;
    private OffsetDateTime paymentDate;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    private String note;
    private BigDecimal exchangeRate;
    private Long currencyId;

    @Type(type = "list-array")
    @Column(name = "attachment_ids", columnDefinition = "bigint[]")
    private List<Long> attachmentIds = new ArrayList<>();
    @Column(name = "attachment_id")
    private Long attachmentId;

    @OneToOne
    @JoinColumn(name = "attachment_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Document attachment;

    @ManyToOne
    @JoinColumn(name = "invoiceId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_payment_invoice"))
    private Invoice invoice;
}
