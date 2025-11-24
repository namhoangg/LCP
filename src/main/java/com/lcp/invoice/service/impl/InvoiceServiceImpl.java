package com.lcp.invoice.service.impl;

import com.lcp.common.ApiMessageBase;
import com.lcp.common.PageResponse;
import com.lcp.common.enumeration.CurrencyHardCode;
import com.lcp.common.enumeration.InvoiceStatus;
import com.lcp.exception.ApiException;
import com.lcp.invoice.dto.InvoiceCreateDto;
import com.lcp.invoice.dto.InvoiceListRequest;
import com.lcp.invoice.dto.InvoiceResponseDto;
import com.lcp.invoice.dto.InvoiceUpdateStatusDto;
import com.lcp.invoice.entity.Invoice;
import com.lcp.invoice.helper.InvoiceHelper;
import com.lcp.invoice.mapper.InvoiceMapper;
import com.lcp.invoice.repository.InvoiceRepository;
import com.lcp.invoice.service.InvoiceService;
import com.lcp.quote.entity.Quote;
import com.lcp.shipment.dto.ContainerTypeQuantityDto;
import com.lcp.shipment.entity.Shipment;
import com.lcp.shipment.repository.ShipmentRepository;
import com.lcp.shipment.service.ContainerService;
import com.lcp.util.BigDecimalUtil;
import com.lcp.util.MathUtil;
import com.lcp.util.PersistentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceHelper invoiceHelper;
    private final ShipmentRepository shipmentRepository;
    private final ContainerService containerService;

    @Transactional
    @Override
    public InvoiceResponseDto create(InvoiceCreateDto createDto) {

        Invoice invoice = InvoiceMapper.createEntity(createDto);
        calculateInvoice(invoice);
        invoice.setPaymentStatus(InvoiceStatus.UNPAID);
        invoice.setCode(invoiceHelper.genInvoiceCode());
        invoiceRepository.save(invoice);
        PersistentUtil.flushAndClear();
        return detail(invoice.getId());
    }

    @Transactional
    @Override
    public InvoiceResponseDto updateStatus(Long id, InvoiceUpdateStatusDto paymentStatus) {
        Invoice invoice = get(id);
        if (paymentStatus.getPaymentStatus() != null) {
            invoice.setPaymentStatus(paymentStatus.getPaymentStatus());
        }
        if (paymentStatus.getDueDate() != null) {
            invoice.setDueDate(paymentStatus.getDueDate());
        }
        invoiceRepository.save(invoice);
        PersistentUtil.flushAndClear();
        return detail(invoice.getId());
    }

    @Override
    public InvoiceResponseDto detail(Long id) {
        Invoice invoice = get(id);
        return InvoiceMapper.createResponse(invoice);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Invoice invoice = get(id);
        invoiceRepository.delete(invoice);
    }

    @Override
    public PageResponse<InvoiceResponseDto> list(InvoiceListRequest request) {
        Page<Invoice> invoices = invoiceRepository.list(request);
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;
        PageResponse<InvoiceResponseDto> pageResponse = PageResponse.buildPageDtoResponse(
                invoices, InvoiceMapper::createResponse
        );
        for (Invoice invoice : invoices.getContent()) {
            totalRevenue = BigDecimalUtil.add(totalRevenue, invoice.getTotalRevenue());
            totalCost = BigDecimalUtil.add(totalCost, invoice.getTotalCost());
        }
        pageResponse.getExtra()
                .put("totalRevenue", totalRevenue);
        pageResponse.getExtra()
                .put("totalCost", totalCost);
        return pageResponse;
    }

    private Invoice get(Long id) {
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(id);
        if (invoiceOptional.isEmpty()) {
            throw new ApiException(new ApiMessageBase("Invoice not found"));
        }
        return invoiceOptional.get();
    }

    private void calculateInvoice(Invoice invoice) {
        Shipment shipment = shipmentRepository.findById(invoice.getShipmentId())
                .orElseThrow(() -> new ApiException(new ApiMessageBase("Shipment not found")));
        String currencyCode = CurrencyHardCode.getCodeFromId(invoice.getCurrencyId());
        Quote quote = shipment.getQuote();
        invoice.setClientId(quote.getClientId());
        BigDecimal totalFreightCost = BigDecimal.ZERO;
        BigDecimal totalServiceCost = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO; //total freight cost with profit
        BigDecimal totalServiceAmount = BigDecimal.ZERO; // total service cost with profit
        List<ContainerTypeQuantityDto> containerTypeQuantityDtoList = containerService.countByContainerType(invoice.getShipmentId());
        Map<Long, BigDecimal> quantityMultiplierMap = containerTypeQuantityDtoList.stream()
                .collect(Collectors.toMap(
                        ContainerTypeQuantityDto::getContainerTypeId,
                        container -> BigDecimal.valueOf(container.getQuantity())
                ));

        for (var item : quote.getCargoChargePriceList()) {
            BigDecimal quantityMultiplier = quantityMultiplierMap.getOrDefault(item.getContainerTypeId(), BigDecimal.ONE);

            totalFreightCost = BigDecimalUtil.add(
                    totalFreightCost,
                    MathUtil.convertMoney(
                            (BigDecimalUtil.multiply(item.getBasePrice(), quantityMultiplier)),
                            item.getCurrencyId(),
                            invoice.getCurrencyId(),
                            invoice.getExchangeRate()));
            totalAmount = BigDecimalUtil.add(
                    totalAmount,
                    MathUtil.convertMoney(
                            (BigDecimalUtil.multiply(item.getTotalPrice(), quantityMultiplier)),
                            item.getCurrencyId(),
                            invoice.getCurrencyId(),
                            invoice.getExchangeRate()));
        }

        for (var item : quote.getServiceChargeMarkupList()) {
            totalServiceCost = MathUtil.convertMoney(
                    BigDecimalUtil.add(totalServiceCost, item.getBasePrice()),
                    item.getCurrencyId(),
                    invoice.getCurrencyId(),
                    invoice.getExchangeRate());
            totalServiceAmount = MathUtil.convertMoney(
                    BigDecimalUtil.add(totalServiceAmount, item.getTotalPrice()),
                    item.getCurrencyId(),
                    invoice.getCurrencyId(),
                    invoice.getExchangeRate());
        }

        BigDecimal taxRate = invoice.getTaxRate();

        BigDecimal taxAmount = MathUtil.formatNumber(BigDecimalUtil.multiply(totalAmount, taxRate), currencyCode);
        BigDecimal totalAmountWithTax = MathUtil.formatNumber(BigDecimalUtil.add(totalAmount, taxAmount), currencyCode);

        BigDecimal freightCostTaxAmount = MathUtil.formatNumber(BigDecimalUtil.multiply(totalFreightCost, taxRate), currencyCode);
        BigDecimal totalFreightCostWithTax = MathUtil.formatNumber(BigDecimalUtil.add(totalFreightCost, freightCostTaxAmount), currencyCode);

        BigDecimal taxServiceAmount = MathUtil.formatNumber(BigDecimalUtil.multiply(totalServiceAmount, taxRate), currencyCode);
        BigDecimal totalServiceAmountWithTax = MathUtil.formatNumber(BigDecimalUtil.add(totalServiceAmount, taxServiceAmount), currencyCode);

        BigDecimal serviceCostTaxAmount = MathUtil.formatNumber(BigDecimalUtil.multiply(totalServiceCost, taxRate), currencyCode);
        BigDecimal totalServiceCostWithTax = MathUtil.formatNumber(BigDecimalUtil.add(totalServiceCost, serviceCostTaxAmount), currencyCode);

        invoice.setTotalAmount(totalAmount);
        invoice.setTaxAmount(taxAmount);
        invoice.setTotalAmountWithTax(totalAmountWithTax);

        invoice.setTotalFreightCost(totalFreightCost);
        invoice.setTotalFreightCostWithTax(totalFreightCostWithTax);

        invoice.setTotalServiceAmount(totalServiceAmount);
        invoice.setTaxServiceAmount(taxServiceAmount);
        invoice.setTotalServiceAmountWithTax(totalServiceAmountWithTax);

        invoice.setTotalServiceCost(totalServiceCost);
        invoice.setTotalServiceCostWithTax(totalServiceCostWithTax);

        BigDecimal totalRevenue = MathUtil.formatNumber(BigDecimalUtil.add(totalAmount, totalServiceAmount), currencyCode);
        BigDecimal totalCost = MathUtil.formatNumber(BigDecimalUtil.add(totalFreightCost, totalServiceCost), currencyCode);

        invoice.setTotalRevenue(totalRevenue);
        invoice.setTotalCost(totalCost);

        invoice.setOriginalAmount(MathUtil.formatNumber(BigDecimalUtil.add(totalAmountWithTax, totalServiceAmountWithTax), currencyCode));
        invoice.setAdjustedAmount(invoice.getOriginalAmount());
        invoice.setTotalAmountDue(invoice.getOriginalAmount());

    }
}
