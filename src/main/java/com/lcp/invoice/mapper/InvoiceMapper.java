package com.lcp.invoice.mapper;

import com.lcp.client.entity.Client_;
import com.lcp.client.mapper.ClientMapper;
import com.lcp.invoice.dto.InvoiceCreateDto;
import com.lcp.invoice.dto.InvoicePrintDto;
import com.lcp.invoice.dto.InvoiceResponseDto;
import com.lcp.invoice.entity.Invoice;
import com.lcp.util.BigDecimalUtil;
import com.lcp.util.MapUtil;

import java.util.List;

public class InvoiceMapper {
    public static Invoice createEntity(InvoiceCreateDto createDto) {
        Invoice invoice = new Invoice();
        MapUtil.copyProperties(createDto, invoice);
        return invoice;
    }

    public static InvoiceResponseDto createResponse(Invoice entity) {
        InvoiceResponseDto responseDto = new InvoiceResponseDto();
        MapUtil.copyProperties(entity, responseDto);
        if (entity.getCurrency() != null) {
            responseDto.setCurrencyCode(entity.getCurrency().getCode());
        } else {
            responseDto.setCurrencyCode("VND");
        }
        return responseDto;
    }

    public static InvoicePrintDto createPrintDto(Invoice entity) {
        InvoicePrintDto printDto = new InvoicePrintDto();
        MapUtil.copyProperties(entity, printDto);
        printDto.setSubtotal(BigDecimalUtil.add(entity.getTotalAmount(), entity.getTotalServiceAmount()));
        printDto.setTax(BigDecimalUtil.add(entity.getTaxAmount(), entity.getTaxServiceAmount()));
        printDto.setTotal(BigDecimalUtil.add(printDto.getSubtotal(), printDto.getTax()));

        if (entity.getClient() != null) {
            printDto.setClient(ClientMapper.createResponse(entity.getClient(), List.of(Client_.COMPANY_INFO)));
            printDto.getClient().getCompanyInfo().setAddress(printDto.getClient().getCompanyInfo().getAddress()+
                    ", " + entity.getClient().getCompanyInfo().getCountry());
        }

        return printDto;
    }
}
