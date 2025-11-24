package com.lcp.quote.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lcp.client.entity.Client;
import com.lcp.client.repository.ClientRepository;
import com.lcp.common.ApiMessageBase;
import com.lcp.common.PageResponse;
import com.lcp.common.dto.EmailBaseDto;
import com.lcp.common.enumeration.ContentType;
import com.lcp.common.enumeration.QuoteStatus;
import com.lcp.exception.ApiException;
import com.lcp.quote.dto.QuoteCreateRequest;
import com.lcp.quote.dto.QuoteCreateStaffDto;
import com.lcp.quote.dto.QuoteListRequest;
import com.lcp.quote.dto.QuoteResponseDto;
import com.lcp.quote.entity.Quote;
import com.lcp.quote.helper.QuoteHelper;
import com.lcp.quote.mapper.QuoteMapper;
import com.lcp.quote.mapper.QuoteRequestMapper;
import com.lcp.quote.repository.CargoVolumneRepository;
import com.lcp.quote.repository.QuotePriceDetailRepository;
import com.lcp.quote.repository.QuoteRepository;
import com.lcp.quote.service.QuoteService;
import com.lcp.rate.entity.FclRate;
import com.lcp.rate.entity.FclRateDetail;
import com.lcp.rate.entity.GoodKind;
import com.lcp.rate.repository.ChargeTypeRepository;
import com.lcp.rate.repository.FclRateRepository;
import com.lcp.rate.repository.GoodKindRepository;
import com.lcp.rate.repository.LclRateRepository;
import com.lcp.security.configuration.AppProperties;
import com.lcp.setting.entity.Unloco;
import com.lcp.setting.repository.UnlocoRepository;
import com.lcp.util.EmailService;
import com.lcp.util.MapUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.Charset.defaultCharset;

@Service
@RequiredArgsConstructor
@Log4j2
public class QuoteServiceImpl implements QuoteService {
    private final QuoteRepository quoteRepository;
    private final QuotePriceDetailRepository quotePriceDetailRepository;
    private final CargoVolumneRepository cargoVolumneRepository;
    private final QuoteHelper quoteHelper;
    private final EmailService emailService;
    private final FclRateRepository fclRateRepository;
    private final LclRateRepository lclRateRepository;
    private final AppProperties appProperties;
    private final ChargeTypeRepository chargeTypeRepository;
    private final ClientRepository clientRepository;
    private final UnlocoRepository unlocoRepository;
    private final GoodKindRepository goodKindRepository;

    @Transactional
    @Override
    public void create(Long id, QuoteCreateRequest createDto) {
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new ApiException(new ApiMessageBase("Quote not found")));
        MapUtil.copyProperties(createDto, quote);
        quote.setQuoteStatus(QuoteStatus.CREATED);
        quoteRepository.save(quote);
        sendQuoteEmail(quote);
    }

    // Helper method to format currency without decimal places
    private String formatCurrency(BigDecimal amount, String currency) {
        if (amount == null) {
            return "";
        }
        // Round to 0 decimal places
        amount = amount.setScale(0, RoundingMode.HALF_UP);

        // Create a custom number format that doesn't show decimals
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        formatter.setMaximumFractionDigits(0);
        formatter.setMinimumFractionDigits(0);

        // This pattern ensures no decimal separator is shown
        formatter.applyPattern("¤,###,##0");

        return formatter.format(amount).replace("$", " ") + currency;
    }

    @Transactional
    @Override
    public QuoteResponseDto update(Long id, QuoteCreateStaffDto updateDto) {
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new ApiException(new ApiMessageBase("Quote not found")));
        MapUtil.copyProperties(updateDto, quote);
        if (updateDto.getIsDraft() != null && updateDto.getIsDraft() && quote.getQuoteStatus() != QuoteStatus.STAFF_DRAFT) {
            throw new ApiException("Can not update quote from " + quote.getQuoteStatus() + " to draft");
        }
        if (updateDto.getIsDraft() != null && updateDto.getIsDraft() && quote.getQuoteStatus() == QuoteStatus.STAFF_DRAFT) {
            quote.setQuoteStatus(QuoteStatus.STAFF_DRAFT);
        } else {
            quote.setQuoteStatus(QuoteStatus.CREATED);
        }

        quoteRepository.save(quote);
        return QuoteMapper.createResponse(quote, QuoteMapper.DetailIncludeFields);
    }

//     @Override
//     public QuoteResponseDto detail(Long id) {
//         Quote quote = get(id);
//         return QuoteMapper.createResponse(quote, QuoteMapper.DetailIncludeFields);
//     }

//     @Transactional
//     @Override
//     public void delete(Long id) {
//         Quote quote = get(id);
//         quoteRepository.delete(quote);
//     }

    @Override
    public PageResponse<QuoteResponseDto> list(QuoteListRequest request) {
        Page<Quote> quotes = quoteRepository.listQuoteRequest(request);
        return PageResponse.buildPageDtoResponse(quotes, quote -> QuoteMapper.createResponse(quote, QuoteMapper.DetailIncludeFields));
    }

    @Override
    @Transactional
    public void staffCreate(QuoteCreateStaffDto createDto) {
        Quote quote = QuoteRequestMapper.createEntity(createDto);
        quote.setCode(quoteHelper.genQuoteCode());
        quote.setIsRequest(false);

        if (createDto.getIsDraft()) {
            quote.setQuoteStatus(QuoteStatus.STAFF_DRAFT);
        } else {
            quote.setQuoteStatus(QuoteStatus.CREATED);
        }


        quoteRepository.save(quote);
    }

    void sendQuoteEmail(Quote quote) {
        try {
            Client client = clientRepository.findById(quote.getClientId())
                    .orElseThrow(() -> new ApiException(new ApiMessageBase("Client not found")));

            String emailTemplate = StreamUtils.copyToString(
                    new ClassPathResource("email/sendQuote.html").getInputStream(), defaultCharset());

            String origin = "";
            String destination = "";

            Unloco originUnloco = unlocoRepository.findById(quote.getOriginId())
                    .orElseThrow(() -> new ApiException(new ApiMessageBase("Origin not found")));
            origin = originUnloco.getDisplayName();

            Unloco destinationUnloco = unlocoRepository.findById(quote.getDestinationId())
                    .orElseThrow(() -> new ApiException(new ApiMessageBase("Destination not found")));
            destination = destinationUnloco.getDisplayName();

            GoodKind goodKind = goodKindRepository.findById(quote.getGoodKindId())
                    .orElseThrow(() -> new ApiException(new ApiMessageBase("Goods not found")));

            String goodKindName = goodKind.getName();

            // Prepare data for email template
            Map<String, String> valuesMap = new HashMap<>();
            valuesMap.put("quoteNumber", quote.getCode());
            valuesMap.put("clientName", client.getName());
            valuesMap.put("origin", origin);
            valuesMap.put("destination", destination);
            valuesMap.put("goodsKind", goodKindName);
            valuesMap.put("goodsDescription", quote.getGoodDescription());
            valuesMap.put("note", quote.getNote());
            valuesMap.put("validUntilDate", quote.getValidUntil() != null ? quote.getValidUntil().toString() : "");
            valuesMap.put("detailUrl", appProperties.getFrontendUrl() + "/quotes/list?keyword=" + quote.getCode());

            // Generate email subject
            String subject = "[FreightFlex] New Quote Request";

            // Replace placeholders in template
            StringSubstitutor sub = new StringSubstitutor(valuesMap);
            String emailHtml = sub.replace(emailTemplate);

            // Send email
            EmailBaseDto emailBaseDto = new EmailBaseDto();
            emailBaseDto.setSubject(subject);
            emailBaseDto.setBody(emailHtml);
            emailBaseDto.setContentType(ContentType.HTML);
            emailBaseDto.setToEmails(Collections.singletonList(client.getEmail()));
            emailService.send(emailBaseDto);

        } catch (Exception e) {
            log.error("Failed to send quote email", e);
        }
    }


    // @Override
    // @Transactional
    // public void updateStatus(QuoteUpdateStatusDto updateStatusDto) {
    //     Quote quote = quoteRepository.findById(updateStatusDto.getQuoteId())
    //             .orElseThrow(() -> new ApiException(new ApiMessageBase("Quote not found")));

    //     QuoteStatus quoteStatus = updateStatusDto.getQuoteStatus();

    //     if (quoteStatus == null) {
    //         throw new ApiException(new ApiMessageBase("Status cannot be null"));
    //     }

    //     if (quoteStatus != QuoteStatus.ACCEPTED && quoteStatus != QuoteStatus.REJECTED && quoteStatus != QuoteStatus.REQUESTED) {
    //         throw new ApiException(new ApiMessageBase("Invalid status"));
    //     }

    //     if (quoteStatus == QuoteStatus.ACCEPTED && quote.getQuoteStatus() != QuoteStatus.CREATED) {
    //         throw new ApiException(new ApiMessageBase("Invalid status transition"));
    //     }

    //     if (quoteStatus == QuoteStatus.REJECTED && quote.getQuoteStatus() != QuoteStatus.CREATED) {
    //         throw new ApiException(new ApiMessageBase("Invalid status transition"));
    //     }

    //     if (quoteStatus == QuoteStatus.REQUESTED && quote.getQuoteStatus() != QuoteStatus.CREATED) {
    //         throw new ApiException(new ApiMessageBase("Invalid status transition"));
    //     }

    //     if (quoteStatus == QuoteStatus.REJECTED && Strings.isNullOrEmpty(updateStatusDto.getReason())) {
    //         throw new ApiException(new ApiMessageBase("Reject reason cannot be null"));
    //     }

    //     if (quoteStatus == QuoteStatus.REQUESTED && Strings.isNullOrEmpty(updateStatusDto.getReason())) {
    //         throw new ApiException(new ApiMessageBase("Request reason cannot be null"));
    //     }
    //     quote.setQuoteStatus(quoteStatus);
    //     quoteRepository.save(quote);
    // }

    @Override
    @Transactional
    public void accept(Long id, Long providerId, Long rateId) {
        Quote quote = get(id);
        if ((quote.getQuoteStatus() != QuoteStatus.CREATED && quote.getQuoteStatus() != QuoteStatus.ACCEPTED) || (quote.getValidUntil() != null && quote.getValidUntil().isBefore(LocalDate.now()))) {
            throw new ApiException(new ApiMessageBase("Invalid status transition"));
        }
        quote.setQuoteStatus(QuoteStatus.ACCEPTED);
        quote.setProviderId(providerId);
        FclRate fclRate = fclRateRepository.findById(rateId)
                .orElseThrow(() -> new ApiException(new ApiMessageBase("FCL rate not found")));
        quote.setEstimatedTransitTime(fclRate.getTransitTime());

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode inputArray = (ArrayNode) quote.getCargoChargeMarkup();
        // Create output array node
        ArrayNode outputArray = mapper.createArrayNode();

        for (JsonNode item : inputArray) {
            if (item.get("providerId").asLong() == providerId) {
                JsonNode cargoChargeMarkupItems = item.get("cargoChargeMarkupItems");
                // Process each markup item
                for (JsonNode markupItem : cargoChargeMarkupItems) {
                    Long containerTypeId = markupItem.get("containerTypeId").asLong();
                    BigDecimal basePrice = fclRate.getDetails().stream().findAny()
                            .filter(detail -> detail.getContainerTypeId().equals(containerTypeId))
                            .map(FclRateDetail::getRate)
                            .orElseThrow(() -> new ApiException(new ApiMessageBase("Container type not found")));
                    BigDecimal markup = markupItem.get("markup").decimalValue();
                    Long currencyId = fclRate.getCurrencyId();

                    ObjectNode newItem = mapper.createObjectNode();
                    newItem.put("containerTypeId", containerTypeId);
                    newItem.put("currencyId", currencyId);
                    newItem.put("basePrice", basePrice);
                    newItem.put("markup", markup);

                    outputArray.add(newItem);
                }
            }

        }
        quote.setCargoChargePrice(outputArray);
        quoteRepository.save(quote);
//        try {
//            String emailTemplate = StreamUtils.copyToString(
//                    new ClassPathResource("email/acceptQuote.html").getInputStream(), defaultCharset());
//
//            Map<String, String> valuesMap = new HashMap<>();
//            valuesMap.put("quoteNumber", quote.getCode());
//            valuesMap.put("clientName", quote.getClient().getName());
//            valuesMap.put("acceptanceDate", quote.getAcceptanceDate() != null ? quote.getAcceptanceDate().toString() : "");
//
//            // Generate email subject
//            String subject = "[FreightFlex] Quote Acceptance Confirmation";
//
//            // Replace placeholders in template
//            StringSubstitutor sub = new StringSubstitutor(valuesMap);
//            String emailHtml = sub.replace(emailTemplate);
//
//            // Send email
//            EmailBaseDto emailBaseDto = new EmailBaseDto();
//            emailBaseDto.setSubject(subject);
//            emailBaseDto.setBody(emailHtml);
//            emailBaseDto.setContentType(ContentType.HTML);
//            emailBaseDto.setToEmails(Collections.singletonList(quote.getClient().getEmail()));
//            emailService.send(emailBaseDto);
//        } catch (Exception e) {
//            log.error("Failed to send quote email", e);
//        }
    }

    @Override
    @Transactional
    public void reject(Long id) {
        Quote quote = get(id);
        if (quote.getQuoteStatus() != QuoteStatus.CREATED || (quote.getValidUntil() != null && quote.getValidUntil().isBefore(LocalDate.now()))) {
            throw new ApiException(new ApiMessageBase("Invalid status transition"));
        }
        quote.setQuoteStatus(QuoteStatus.REJECTED);
        quoteRepository.save(quote);
        try {
            String emailTemplate = StreamUtils.copyToString(
                    new ClassPathResource("email/rejectQuote.html").getInputStream(), defaultCharset());

            Map<String, String> valuesMap = new HashMap<>();
            valuesMap.put("quoteNumber", quote.getCode());
            valuesMap.put("clientName", quote.getClient().getName());
            valuesMap.put("quoteDate", quote.getCreatedAt().toString());

            // Generate email subject
            String subject = "[FreightFlex] Quote Rejection Confirmation";

            // Replace placeholders in template
            StringSubstitutor sub = new StringSubstitutor(valuesMap);
            String emailHtml = sub.replace(emailTemplate);

            // Send email
            EmailBaseDto emailBaseDto = new EmailBaseDto();
            emailBaseDto.setSubject(subject);
            emailBaseDto.setBody(emailHtml);
            emailBaseDto.setContentType(ContentType.HTML);
            emailBaseDto.setToEmails(Collections.singletonList(quote.getClient().getEmail()));
            emailService.send(emailBaseDto);
        } catch (Exception e) {
            log.error("Failed to send quote email", e);
        }
    }

//     @Override
//     public void changeRequest(Long id, QuoteRejectDto changeRequestDto) {
//         Quote quote = get(id);
//         if (quote.getQuoteStatus() != QuoteStatus.CREATED || (quote.getValidUntil() != null && quote.getValidUntil().isBefore(LocalDate.now()))) {
//             throw new ApiException(new ApiMessageBase("Invalid status transition"));
//         }
//         quote.setQuoteStatus(QuoteStatus.REQUESTED);
//         quote.setReason(changeRequestDto.getReason());
//         quote.setIsRequest(true);
//         quoteRepository.save(quote);

//         try {
//             String emailTemplate = StreamUtils.copyToString(
//                     new ClassPathResource("email/changeRequest.html").getInputStream(), defaultCharset());

//             Map<String, String> valuesMap = new HashMap<>();
//             valuesMap.put("quoteNumber", quote.getCode());
//             valuesMap.put("clientName", quote.getClient().getName());
//             valuesMap.put("staffName", quote.getClient().getServedByStaff().getLastname() + " " + quote.getClient().getServedByStaff().getFirstname());
//             valuesMap.put("changeReason", changeRequestDto.getReason());

//             // Generate email subject
//             String subject = "[FreightFlex] Quote Status Change";

//             // Replace placeholders in template
//             StringSubstitutor sub = new StringSubstitutor(valuesMap);
//             String emailHtml = sub.replace(emailTemplate);

//             // Send email
//             EmailBaseDto emailBaseDto = new EmailBaseDto();
//             emailBaseDto.setSubject(subject);
//             emailBaseDto.setBody(emailHtml);
//             emailBaseDto.setContentType(ContentType.HTML);
//             emailBaseDto.setToEmails(Collections.singletonList(quote.getClient().getServedByStaff().getEmail()));
//             emailService.send(emailBaseDto);
//         } catch (Exception e) {
//             log.error("Failed to send quote email", e);
//         }
//     }

    @Override
    public List<QuoteResponseDto> clientQuoteList(Long id) {
        List<Quote> quotes = quoteRepository.findAllByClientIdAndQuoteStatusInAndValidUntilGreaterThanEqual(id, List.of(QuoteStatus.ACCEPTED, QuoteStatus.BOOKED), LocalDate.now());
        return quotes.stream()
                .map(quote -> QuoteMapper.createResponse(quote, QuoteMapper.DetailIncludeFields))
                .collect(Collectors.toList());
    }

    private Quote get(Long id) {
        Optional<Quote> quoteOptional = quoteRepository.findById(id);
        if (quoteOptional.isEmpty()) {
            throw new ApiException(new ApiMessageBase("Quote not found"));
        }
        return quoteOptional.get();
    }

//     @Override
//     public BigDecimal getTotalFreightBaseAmount(Quote quote) {
//         CargoVolume cargoVolume = quote.getCargoVolume();
//         if (cargoVolume.getIsFCL()) {
//             FclRate fclRate = fclRateRepository.findById(cargoVolume.getFclRateId())
//                     .orElseThrow(() -> new ApiException(new ApiMessageBase("FCL rate not found")));

//             return BigDecimalUtil.add(
// //                    BigDecimalUtil.multiply(BigDecimal.valueOf(cargoVolume.getTotalCont20dc()), fclRate.getRate20dc()),
// //                    BigDecimalUtil.multiply(BigDecimal.valueOf(cargoVolume.getTotalCont40dc()), fclRate.getRate40dc()),
// //                    BigDecimalUtil.multiply(BigDecimal.valueOf(cargoVolume.getTotalCont20rf()), fclRate.getRate20rf()),
// //                    BigDecimalUtil.multiply(BigDecimal.valueOf(cargoVolume.getTotalCont40rf()), fclRate.getRate40rf()),
// //                    BigDecimalUtil.multiply(BigDecimal.valueOf(cargoVolume.getTotalCont20hc()), fclRate.getRate20hc()),
// //                    BigDecimalUtil.multiply(BigDecimal.valueOf(cargoVolume.getTotalCont40hc()), fclRate.getRate40hc()),
// //                    BigDecimalUtil.multiply(BigDecimal.valueOf(cargoVolume.getTotalCont45hc()), fclRate.getRate45hc())
//             );
//         }
//         return BigDecimal.ZERO;
//     }

//     @Override
//     public BigDecimal getTotalFreightAmount(Quote quote) {
//         CargoVolume cargoVolume = quote.getCargoVolume();
//         if (cargoVolume.getIsFCL()) {
//             return MathUtil.doRound(BigDecimalUtil.multiply(getTotalFreightBaseAmount(quote), quote.getProfitRateCalculate()));
//         }
//         return BigDecimal.ZERO;
//     }
}
