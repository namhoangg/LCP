package com.lcp.invoice.entity;

import com.lcp.client.entity.Client;
import com.lcp.common.EntityBase;
import com.lcp.common.enumeration.InvoiceStatus;
import com.lcp.common.enumeration.NoteType;
import com.lcp.common.enumeration.PaymentStatus;
import com.lcp.payment.entity.Payment;
import com.lcp.setting.entity.Currency;
import com.lcp.shipment.entity.Shipment;
import com.lcp.util.BigDecimalUtil;
import com.lcp.util.MathUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "invoice")
public class Invoice extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_gen")
    @SequenceGenerator(name = "invoice_gen", sequenceName = "invoice_seq", allocationSize = 1)
    @Column(name = "id", columnDefinition = "bigint default nextval('invoice_seq')")
    private Long id;
    private String code;
    private Long shipmentId;
    private Long clientId;
    private Long currencyId;
    private LocalDate dueDate;
    @Column(precision = 10, scale = 4)
    private BigDecimal taxRate;
    // total freight with profit
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmountWithTax;
    // total service with profit
    private BigDecimal totalServiceAmount;
    private BigDecimal taxServiceAmount;
    private BigDecimal totalServiceAmountWithTax;
    // total freight cost
    private BigDecimal totalFreightCost;
    private BigDecimal totalFreightCostWithTax;
    // total service cost
    private BigDecimal totalServiceCost;
    private BigDecimal totalServiceCostWithTax;
    // total revenue
    private BigDecimal totalRevenue;
    // total cost
    private BigDecimal totalCost;

    private BigDecimal originalAmount;
    private BigDecimal adjustedAmount; // amount including debit notes
    private BigDecimal totalAmountDue;

    private InvoiceStatus paymentStatus;
    private BigDecimal exchangeRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipmentId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_invoice_shipment"))
    private Shipment shipment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_invoice_currency"))
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "clientId", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_invoice_client"))
    private Client client;

    @OneToMany()
    @JoinColumn(name = "invoiceId", referencedColumnName = "id", insertable = false, updatable = false)
    private List<DebitNote> debitNotes;

    @OneToMany()
    @JoinColumn(name = "invoiceId", referencedColumnName = "id", insertable = false, updatable = false)
    private List<Payment> payments;

    public BigDecimal getTotalPaid() {
        return payments.stream()
                .filter(payment -> payment.getPaymentStatus().equals(PaymentStatus.PAID))
                .map(payment ->
                        MathUtil.convertMoney(payment.getAmount(),
                                payment.getCurrencyId(), this.getCurrencyId(),
                                payment.getExchangeRate()))
                .reduce(BigDecimal.ZERO, BigDecimalUtil::add);
    }
}
