package com.lcp.template.service.impl;

import com.lcp.common.Item;
import com.lcp.invoice.dto.InvoicePrintDto;
import com.lcp.invoice.entity.Invoice;
import com.lcp.invoice.mapper.InvoiceMapper;
import com.lcp.invoice.repository.InvoiceRepository;
import com.lcp.template.dto.TemplateRenderRequest;
import com.lcp.template.service.TemplateService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.nio.charset.Charset.defaultCharset;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {
    private final InvoiceRepository invoiceRepository;

    private static final TemplateEngine templateEngine;
    private static final String TEMPLATE_LOCAL = "US";

    static {
        templateEngine = new SpringTemplateEngine();
        StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(templateResolver);
        templateEngine.getConfiguration();
    }

    @Override
    public byte[] render(TemplateRenderRequest request) {
        try {
            String templateContent = StreamUtils.copyToString(
                    new ClassPathResource("templates/invoice.html").getInputStream(), defaultCharset());
            Context context = new Context(new Locale(TEMPLATE_LOCAL));
            context.setVariables(buildContext(request));
            context.setVariable("logoBase64", getBase64Logo());
            context.setVariable("date", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            context.setVariable("fromCompany", "FreightFlex");
            context.setVariable("fromAddress", "123 Main Street, Viet Nam");
            context.setVariable("fromPhone", "(123) 456-7890");

            context.setVariable("terms", "Your payment is due upon receipt.");

            String htmlContent = templateEngine.process(templateContent, context);
            ByteArrayInputStream inputStream;
            inputStream = new ByteArrayInputStream(this.renderPdfFromHtml(htmlContent));
            return inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return new byte[]{};
        }
    }

    private Map<String, Object> buildContext(TemplateRenderRequest request) {
        Map<String, Object> context = new HashMap<>();
        Invoice invoice = invoiceRepository.findById(request.getSourceId())
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        InvoicePrintDto printDto = InvoiceMapper.createPrintDto(invoice);
        context.put("invoice", printDto);
        context.put("items", List.of(
                new Item("Freight",
                        printDto.getTotalAmount().toString(),
                        printDto.getTaxAmount().toString(),
                        printDto.getTotalAmountWithTax().toString()),
                new Item("Service Charge",
                        printDto.getTotalServiceAmount().toString(),
                        printDto.getTaxServiceAmount().toString(),
                        printDto.getTotalServiceAmountWithTax().toString())
        ));
        context.put("companyClientInfo", printDto.getClient().getCompanyInfo());
        return context;
    }

    private byte[] renderPdfFromHtml(String html) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    public String getBase64Logo() throws IOException {
        ClassPathResource imageResource = new ClassPathResource("images/freight.png");
        byte[] imageBytes = FileCopyUtils.copyToByteArray(imageResource.getInputStream());
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
