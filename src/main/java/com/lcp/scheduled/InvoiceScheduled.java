package com.lcp.scheduled;

import com.lcp.common.enumeration.InvoiceStatus;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import com.lcp.common.dto.EmailBaseDto;
import com.lcp.common.enumeration.ContentType;
import com.lcp.invoice.entity.Invoice;
import com.lcp.invoice.repository.InvoiceRepository;
import com.lcp.util.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static java.nio.charset.Charset.defaultCharset;

@Component
@RequiredArgsConstructor
@Log4j2
public class InvoiceScheduled {
  private final EmailService emailService;
  private final InvoiceRepository invoiceRepository;

  @PersistenceContext
  private EntityManager entityManager;

  @Scheduled(cron = "0 0 0  * * ?", zone = "Asia/Ho_Chi_Minh") 
  @Transactional
  public void checkOverdueInvoices() {
    List<Invoice> invoices = invoiceRepository.findAllByDueDateBeforeAndPaymentStatusNot(LocalDate.now(), InvoiceStatus.PAID);
    for (Invoice invoice : invoices) {
       try {
                String emailTemplate = StreamUtils.copyToString(
                        new ClassPathResource("email/remindOverdueInvoice.html").getInputStream(), defaultCharset());

                // Prepare data for email template
                Map<String, String> valuesMap = new HashMap<>();
                valuesMap.put("displayName", invoice.getClient().getName());
                valuesMap.put("invoiceNumber", invoice.getCode());
                valuesMap.put("dueDate", invoice.getDueDate().toString());
                valuesMap.put("totalAmount", invoice.getAdjustedAmount().toString());
                valuesMap.put("totalAmountClientHasPaid", invoice.getTotalPaid().toString());
                valuesMap.put("totalDebt", invoice.getTotalAmountDue().toString());
                valuesMap.put("currency", invoice.getCurrency().getCode());
                valuesMap.put("daysOverdue", invoice.getDueDate().until(LocalDate.now()).getDays() + "");
                valuesMap.put("issueDate", invoice.getCreatedAt().toLocalDate().toString());  

                // Generate email subject
                String subject = "[FreightFlex] Invoice #" + invoice.getCode() + " - Your invoice is overdue";

                // Replace placeholders in template
                StringSubstitutor sub = new StringSubstitutor(valuesMap);
                String emailHtml = sub.replace(emailTemplate);

                // Send email
                EmailBaseDto emailBaseDto = new EmailBaseDto();
                emailBaseDto.setSubject(subject);
                emailBaseDto.setBody(emailHtml);
                emailBaseDto.setContentType(ContentType.HTML);
                emailBaseDto.setToEmails(Collections.singletonList(invoice.getClient().getEmail()));
                emailService.send(emailBaseDto);
            } catch (Exception e) {
                log.error("Failed to send invoice overdue email", e);
            }
    }
  }

  @Scheduled(cron = "0 0 0  * * ?", zone = "Asia/Ho_Chi_Minh")
  @Transactional
  public void checkUpcomingOverdueInvoices() {
    System.out.println("Checking upcoming overdue invoices");
    List<Invoice> invoices = invoiceRepository.findAllByDueDateBeforeAndPaymentStatusNot(LocalDate.now().plusDays(3), InvoiceStatus.PAID);
    for (Invoice invoice : invoices) {
      try {
        String emailTemplate = StreamUtils.copyToString(
          new ClassPathResource("email/remindUpcommingOverdueInvoice.html").getInputStream(), defaultCharset());
        // Prepare data for email template
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("displayName", invoice.getClient().getName());
        valuesMap.put("invoiceNumber", invoice.getCode());
        valuesMap.put("dueDate", invoice.getDueDate().toString());
        valuesMap.put("totalAmount", invoice.getAdjustedAmount().toString());
        valuesMap.put("totalAmountClientHasPaid", invoice.getTotalPaid().toString());
        valuesMap.put("totalDebt", invoice.getTotalAmountDue().toString());
        valuesMap.put("currency", invoice.getCurrency().getCode());
        valuesMap.put("daysUntilDue", LocalDate.now().until(invoice.getDueDate()).getDays() + "");
        valuesMap.put("issueDate", invoice.getCreatedAt().toLocalDate().toString());

        System.out.println("valuesMap: " + valuesMap);

        // Generate email subject
        String subject = "[FreightFlex] Invoice #" + invoice.getCode() + " - Your invoice is due soon"; 

        // Replace placeholders in template
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        String emailHtml = sub.replace(emailTemplate);

        // Send email
        EmailBaseDto emailBaseDto = new EmailBaseDto();
        emailBaseDto.setSubject(subject);
        emailBaseDto.setBody(emailHtml);
        emailBaseDto.setContentType(ContentType.HTML);
        emailBaseDto.setToEmails(Collections.singletonList(invoice.getClient().getEmail()));
        emailService.send(emailBaseDto);  
        
      } catch (IOException e) {
        log.error("Failed to send invoice upcoming overdue email", e);
      }
    }
  }
   
  @Scheduled(cron = "0 20 23  * * ?", zone = "Asia/Ho_Chi_Minh")
  @Transactional
  public void overdueInvoice() {
    log.info("Updating payment status to overdue");
    invoiceRepository.updatePaymentStatusToOverdue();
    log.info("End updating payment status to overdue");
  }
}