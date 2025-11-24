package com.lcp.invoice.repository;

import com.lcp.common.enumeration.InvoiceStatus;
import com.lcp.invoice.entity.Invoice;
import com.lcp.invoice.repository.custom.InvoiceRepositoryCustom;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>, InvoiceRepositoryCustom {


  List<Invoice> findAllByDueDateBeforeAndPaymentStatusNot(LocalDate dueDate, InvoiceStatus paymentStatus);

  @Query("UPDATE Invoice i set i.paymentStatus = 4 where i.dueDate < now() and i.paymentStatus != 1")
  @Modifying
  @Transactional
  void updatePaymentStatusToOverdue();
}
