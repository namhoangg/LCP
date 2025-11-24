package com.lcp.payment.service.impl;

import com.lcp.common.ApiMessageBase;
import com.lcp.common.PageResponse;
import com.lcp.common.enumeration.InvoiceStatus;
import com.lcp.common.enumeration.PaymentMethod;
import com.lcp.common.enumeration.PaymentStatus;
import com.lcp.exception.ApiException;
import com.lcp.invoice.entity.Invoice;
import com.lcp.invoice.repository.InvoiceRepository;
import com.lcp.payment.dto.PaymentCreateDto;
import com.lcp.payment.dto.PaymentListRequest;
import com.lcp.payment.dto.PaymentResponseDto;
import com.lcp.payment.dto.PaymentUpdateDto;
import com.lcp.payment.entity.Payment;
import com.lcp.payment.helper.PaymentHelper;
import com.lcp.payment.mapper.PaymentMapper;
import com.lcp.payment.repository.PaymentRepository;
import com.lcp.payment.service.PaymentService;
import com.lcp.paypal.service.PayPalService;
import com.lcp.security.UserDetailsCustom;
import com.lcp.util.BigDecimalUtil;
import com.lcp.util.PersistentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentHelper paymentHelper;
    private final PayPalService payPalService;
    private final InvoiceRepository invoiceRepository;

    @Transactional
    @Override
    public PaymentResponseDto create(PaymentCreateDto createDto) {
        Payment payment = PaymentMapper.createEntity(createDto);
        payment.setShipmentId(getShipmentIdFromInvoice(payment));
        payment.setPaymentStatus(PaymentStatus.UNPAID);
        payment.setCode(paymentHelper.genPaymentCode());
        if (payment.getPaymentMethod() == PaymentMethod.PAYPAL) {
            Map<String, Object> paymentResponse = payPalService.createOrder(payment.getAmount());
            // Extract approval link
            String approvalLink = null;
            List<Map<String, String>> links = (List<Map<String, String>>) paymentResponse.get("links");
            for (Map<String, String> link : links) {
                if ("approve".equals(link.get("rel"))) {
                    approvalLink = link.get("href");
                    break;
                }
            }
            payment.setTransactionId((String) paymentResponse.get("id"));
            payment.setApprovalUrl(approvalLink);
        }
        paymentRepository.save(payment);
        PersistentUtil.flushAndClear();
        return detail(payment.getId());
    }

    @Transactional
    @Override
    public PaymentResponseDto capture(String orderId) {
        Map<String, Object> paypalResponse = payPalService.captureOrder(orderId);
        if (paypalResponse != null) {
            if (paypalResponse.get("status").equals("COMPLETED")) {
                String transactionId = (String) paypalResponse.get("id");
                Payment payment = paymentRepository.findByTransactionId(transactionId);
                if (payment == null) {
                    throw new ApiException(new ApiMessageBase("Payment not found"));
                }
                payment.setPaymentStatus(PaymentStatus.PAID);
                payment.setFee(extractPayPalFeeAsBigDecimal(paypalResponse));
                payment.setPaymentDate(extractCaptureCreateTime(paypalResponse));
                paymentRepository.save(payment);
                //update invoice status
                Invoice invoice = updateInvoiceAmountDue(payment);
                assert invoice != null;
                invoiceRepository.save(invoice);

                PersistentUtil.flushAndClear();
                PaymentResponseDto responseDto = detail(payment.getId());
                responseDto.setPaypalResponse(paypalResponse);
                return responseDto;
            }
        }
        else {
            throw new ApiException("Payment not completed");
        }
        return null;
    }

    @Transactional
    @Override
    public PaymentResponseDto sendPaymentRequest(Long id) {
        Payment payment = get(id);
        if (payment.getPaymentStatus() == PaymentStatus.UNPAID) {
            payment.setPaymentStatus(PaymentStatus.REQUESTED);
            paymentRepository.save(payment);
            PersistentUtil.flushAndClear();
            return detail(payment.getId());
        } else {
            throw new ApiException("Payment cannot send request");
        }
    }

    @Transactional
    @Override
    public PaymentResponseDto updatePaymentRequest(Long id, PaymentUpdateDto updateDto) {
        if (Objects.requireNonNull(UserDetailsCustom.getCurrentUser()).getIsClient())
            throw new ApiException("Client cannot update payment request");
        Payment payment = get(id);
        if (payment.getPaymentStatus() == PaymentStatus.REQUESTED) {
            if (updateDto.getPaymentStatus() == PaymentStatus.PAID) {
                payment.setPaymentDate(OffsetDateTime.now());
                payment.setFee(BigDecimal.ZERO);
            }
            payment.setPaymentStatus(updateDto.getPaymentStatus() );
            paymentRepository.save(payment);
            updateInvoiceAmountDue(payment);
            PersistentUtil.flushAndClear();
            return detail(payment.getId());
        } else {
            throw new ApiException("Payment is not requested");
        }
    }

    @Override
    public PaymentResponseDto detail(Long id) {
        Payment payment = get(id);
        return PaymentMapper.createResponse(payment);
    }

    @Override
    public PageResponse<PaymentResponseDto> list(PaymentListRequest request) {
        Page<Payment> payments = paymentRepository.list(request);
        return PageResponse.buildPageDtoResponse(
                payments,
                PaymentMapper::createResponse
        );
    }

    private Payment get(Long id) {
        Optional<Payment> paymentOptional = paymentRepository.findById(id);
        if (paymentOptional.isEmpty()) {
            throw new ApiException("Payment not found");
        }
        return paymentOptional.get();
    }

    @SuppressWarnings("unchecked")
    public String extractPayPalFee(Map<String, Object> paypalResponse) {
        try {
            List<Map<String, Object>> purchaseUnits = (List<Map<String, Object>>) paypalResponse.get("purchase_units");
            if (purchaseUnits == null || purchaseUnits.isEmpty()) return null;

            Map<String, Object> payments = (Map<String, Object>) purchaseUnits.get(0).get("payments");
            if (payments == null) return null;

            List<Map<String, Object>> captures = (List<Map<String, Object>>) payments.get("captures");
            if (captures == null || captures.isEmpty()) return null;

            Map<String, Object> capture = captures.get(0);

            Map<String, Object> sellerBreakdown = (Map<String, Object>) capture.get("seller_receivable_breakdown");
            if (sellerBreakdown == null) return null;

            Map<String, Object> paypalFee = (Map<String, Object>) sellerBreakdown.get("paypal_fee");
            if (paypalFee == null) return null;

            return (String) paypalFee.get("value");

        } catch (ClassCastException e) {
            // Handle bad casting or missing keys
            e.printStackTrace();
            return null;
        }
    }

    private BigDecimal extractPayPalFeeAsBigDecimal(Map<String, Object> paypalResponse) {
        String fee = extractPayPalFee(paypalResponse);
        return fee != null ? new BigDecimal(fee) : null;
    }

    @SuppressWarnings("unchecked")
    public OffsetDateTime extractCaptureCreateTime(Map<String, Object> paypalResponse) {
        try {
            List<Map<String, Object>> purchaseUnits = (List<Map<String, Object>>) paypalResponse.get("purchase_units");
            if (purchaseUnits == null || purchaseUnits.isEmpty()) return null;

            Map<String, Object> payments = (Map<String, Object>) purchaseUnits.get(0).get("payments");
            if (payments == null) return null;

            List<Map<String, Object>> captures = (List<Map<String, Object>>) payments.get("captures");
            if (captures == null || captures.isEmpty()) return null;

            Map<String, Object> firstCapture = captures.get(0);
            String createTimeString = (String) firstCapture.get("create_time"); // ISO 8601 string

            if (createTimeString != null) {
                // Parses "2025-05-18T06:26:04Z" into an OffsetDateTime (UTC)
                return OffsetDateTime.parse(createTimeString);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Invoice updateInvoiceAmountDue(Payment payment) {
        if (payment.getInvoiceId() != null) {
            Invoice invoice = invoiceRepository.findById(payment.getInvoiceId())
                    .orElseThrow(() -> new ApiException(new ApiMessageBase("Invoice not found")));
            invoice.setTotalAmountDue(BigDecimalUtil.subtract(invoice.getAdjustedAmount(), invoice.getTotalPaid()));
            if (invoice.getTotalAmountDue().compareTo(BigDecimal.ZERO) == 0) {
                invoice.setPaymentStatus(InvoiceStatus.PAID);
            } else {
                invoice.setPaymentStatus(InvoiceStatus.PARTIAL);
            }
            invoiceRepository.save(invoice);
            return invoice;
        }
        return null;
    }

    private Long getShipmentIdFromInvoice(Payment payment) {
        if (payment.getInvoiceId() != null) {
            Invoice invoice = invoiceRepository.findById(payment.getInvoiceId())
                    .orElseThrow(() -> new ApiException(new ApiMessageBase("Invoice not found")));
            if (invoice.getTotalAmountDue() != null) {
                if (invoice.getTotalAmountDue().compareTo(BigDecimal.ZERO) == 0)
                    throw new ApiException("Invoice is already paid");
                else if (invoice.getTotalAmountDue().compareTo(payment.getAmount()) < 0)
                    throw new ApiException("Invoice is overpaid");
            }
            return invoice.getShipmentId();
        }
        return null;
    }
}
