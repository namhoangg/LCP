package com.lcp.invoice.service.impl;

import com.lcp.common.ApiMessageBase;
import com.lcp.common.PageResponse;
import com.lcp.common.enumeration.GeneralStatus;
import com.lcp.common.enumeration.NoteType;
import com.lcp.exception.ApiException;
import com.lcp.invoice.dto.DebitNoteCreateDto;
import com.lcp.invoice.dto.DebitNoteListRequest;
import com.lcp.invoice.dto.DebitNoteResponseDto;
import com.lcp.invoice.dto.DebitNoteUpdateDto;
import com.lcp.invoice.entity.DebitNote;
import com.lcp.invoice.entity.Invoice;
import com.lcp.invoice.mapper.DebitNoteMapper;
import com.lcp.invoice.repository.DebitNoteRepository;
import com.lcp.invoice.repository.InvoiceRepository;
import com.lcp.invoice.service.DebitNoteService;
import com.lcp.util.BigDecimalUtil;
import com.lcp.util.MapUtil;
import com.lcp.util.PersistentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DebitNoteServiceImpl implements DebitNoteService {
    private final DebitNoteRepository debitNoteRepository;
    private final InvoiceRepository invoiceRepository;

    @Transactional
    @Override
    public DebitNoteResponseDto create(DebitNoteCreateDto createDto) {
        DebitNote debitNote = DebitNoteMapper.createEntity(createDto);
        Invoice invoice = invoiceRepository.findById(debitNote.getInvoiceId())
                .orElseThrow(() -> new ApiException(new ApiMessageBase("Invoice not found")));
        debitNote.setShipmentId(invoice.getShipmentId());
        debitNoteRepository.save(debitNote);
        updateInvoiceAdjustmentAmount(debitNote.getInvoiceId());
        PersistentUtil.flushAndClear();
        return detail(debitNote.getId());
    }

    @Transactional
    @Override
    public DebitNoteResponseDto update(Long id, DebitNoteUpdateDto updateDto) {
        DebitNote debitNote = get(id);
        MapUtil.copyProperties(updateDto, debitNote);
        debitNoteRepository.save(debitNote);
        updateInvoiceAdjustmentAmount(debitNote.getInvoiceId());
        PersistentUtil.flushAndClear();
        return detail(debitNote.getId());
    }

    @Override
    public DebitNoteResponseDto detail(Long id) {
        DebitNote debitNote = get(id);
        return DebitNoteMapper.createResponse(debitNote);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        DebitNote debitNote = get(id);
        debitNote.setStatus(GeneralStatus.DELETED);
        debitNoteRepository.save(debitNote);
        updateInvoiceAdjustmentAmount(debitNote.getInvoiceId());
    }

    @Override
    public PageResponse<DebitNoteResponseDto> list(DebitNoteListRequest request) {
        Page<DebitNote> debitNotes = debitNoteRepository.list(request);
        return PageResponse.buildPageDtoResponse(
                debitNotes,
                DebitNoteMapper::createResponse
        );
    }

    private DebitNote get(Long id) {
        Optional<DebitNote> debitNoteOptional = debitNoteRepository.findById(id);
        if (debitNoteOptional.isEmpty()) {
            throw new ApiException(new ApiMessageBase("DebitNote not found"));
        }
        return debitNoteOptional.get();
    }

    private void updateInvoiceAdjustmentAmount(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ApiException(new ApiMessageBase("Invoice not found")));

        BigDecimal finalAmount = invoice.getOriginalAmount();

        List<DebitNote> debitNotes = debitNoteRepository.findByInvoiceId(invoiceId);

        for (DebitNote debitNote : debitNotes) {
            if (debitNote.getStatus() == GeneralStatus.ACTIVE) {
                if (debitNote.getNoteType().equals(NoteType.CREDIT)) {
                    finalAmount = BigDecimalUtil.subtract(finalAmount, debitNote.getAmount());
                } else {
                    finalAmount = BigDecimalUtil.add(finalAmount, debitNote.getAmount());
                }
            }
        }

        invoice.setAdjustedAmount(finalAmount);
        invoice.setTotalAmountDue(finalAmount.subtract(invoice.getTotalPaid()));
        invoiceRepository.save(invoice);
    }
}
