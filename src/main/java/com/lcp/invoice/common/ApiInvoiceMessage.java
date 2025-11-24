package com.lcp.invoice.common;

import com.lcp.common.ApiMessageBase;
import lombok.Getter;

@Getter
public class ApiInvoiceMessage extends ApiMessageBase {
    public static ApiInvoiceMessage
            INVOICE_CREATE_SUCCESS = new ApiInvoiceMessage("invoice.create_success"),
            INVOICE_UPDATE_SUCCESS = new ApiInvoiceMessage("invoice.update_success"),
            INVOICE_DELETE_SUCCESS = new ApiInvoiceMessage("invoice.delete_success"),
            INVOICE_NOT_FOUND = new ApiInvoiceMessage("invoice.not_found"),
            INVOICE_ALREADY_PAID = new ApiInvoiceMessage("invoice.already_paid"),
            INVOICE_STATUS_UPDATE_SUCCESS = new ApiInvoiceMessage("invoice.status_update_success"),
            DEBIT_NOTE_CREATE_SUCCESS = new ApiInvoiceMessage("debit_note.create_success"),
            DEBIT_NOTE_UPDATE_SUCCESS = new ApiInvoiceMessage("debit_note.update_success"),
            DEBIT_NOTE_DELETE_SUCCESS = new ApiInvoiceMessage("debit_note.delete_success"),
            DEBIT_NOTE_NOT_FOUND = new ApiInvoiceMessage("debit_note.not_found");

    public ApiInvoiceMessage(String message) {
        super(message);
    }
}
