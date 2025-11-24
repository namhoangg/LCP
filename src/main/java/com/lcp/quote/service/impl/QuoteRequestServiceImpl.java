package com.lcp.quote.service.impl;

import com.lcp.client.dto.ClientCreateDto;
import com.lcp.client.entity.Client;
import com.lcp.client.helper.ClientHelper;
import com.lcp.client.mapper.ClientMapper;
import com.lcp.client.repository.ClientRepository;
import com.lcp.client.service.ClientService;
import com.lcp.common.ApiMessageBase;
import com.lcp.common.PageResponse;
import com.lcp.common.dto.EmailBaseDto;
import com.lcp.common.enumeration.ContentType;
import com.lcp.common.enumeration.QuoteStatus;
import com.lcp.exception.ApiException;
import com.lcp.provider.entity.CompanyInfo;
import com.lcp.provider.entity.ContactPersonInfo;
import com.lcp.provider.mapper.CompanyInfoMapper;
import com.lcp.provider.mapper.ContactPersonInfoMapper;
import com.lcp.provider.repository.CompanyInfoRepository;
import com.lcp.provider.repository.ContactPersonInfoRepository;
import com.lcp.quote.dto.*;
import com.lcp.quote.entity.Quote;
import com.lcp.quote.helper.QuoteHelper;
import com.lcp.quote.mapper.QuoteMapper;
import com.lcp.quote.mapper.QuoteRequestMapper;
import com.lcp.quote.repository.CargoVolumneRepository;
import com.lcp.quote.repository.QuoteRepository;
import com.lcp.quote.service.QuoteRequestService;
import com.lcp.security.configuration.AppProperties;
import com.lcp.setting.entity.Unloco;
import com.lcp.setting.repository.UnlocoRepository;
import com.lcp.staff.entity.Staff;
import com.lcp.staff.repository.StaffRepository;
import com.lcp.util.EmailService;
import com.lcp.util.MapUtil;
import com.lcp.util.PersistentUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.Charset.defaultCharset;

@Service
@RequiredArgsConstructor
@Log4j2
public class QuoteRequestServiceImpl implements QuoteRequestService {

    private final QuoteRepository quoteRepository;
    private final CargoVolumneRepository cargoVolumneRepository;
    private final ClientService clientService;
    private final QuoteHelper quoteHelper;
    private final ClientRepository clientRepository;
    private final StaffRepository staffRepository;
    private final CompanyInfoRepository companyInfoRepository;
    private final ContactPersonInfoRepository contactPersonInfoRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;
    private final EmailService emailService;
    private final UnlocoRepository unlocoRepository;

    @Transactional
    @Override
    public void create(QuoteCreateRequestClient createDto) {
        Quote quote = new Quote();
        MapUtil.copyProperties(createDto, quote);
        quote.setClientId(clientService.getCurrentClientId());
        quote.setCode(quoteHelper.genQuoteCode());
        quote.setIsRequest(true);

        if (createDto.getIsDraft() != null && createDto.getIsDraft()) {
            quote.setQuoteStatus(QuoteStatus.DRAFT);
        } else {
            quote.setQuoteStatus(QuoteStatus.REQUESTED);
        }

        quoteRepository.save(quote);
        PersistentUtil.flushAndClear();

        if (quote.getQuoteStatus() == QuoteStatus.REQUESTED) {
            sendQuoteRequestEmail(quote);
        }
    }

    void sendQuoteRequestEmail(Quote quote) {
        try {
            Client client = clientRepository.findById(quote.getClientId())
                    .orElseThrow(() -> new ApiException(new ApiMessageBase("Client not found")));

            if (client.getServedByStaff() != null) {
                String emailTemplate = StreamUtils.copyToString(
                        new ClassPathResource("email/sendQuoteRequestNotice.html").getInputStream(), defaultCharset());

                String origin = "";
                String destination = "";

                Unloco originUnloco = unlocoRepository.findById(quote.getOriginId())
                        .orElseThrow(() -> new ApiException(new ApiMessageBase("Origin not found")));
                origin = originUnloco.getDisplayName();

                Unloco destinationUnloco = unlocoRepository.findById(quote.getDestinationId())
                        .orElseThrow(() -> new ApiException(new ApiMessageBase("Destination not found")));
                destination = destinationUnloco.getDisplayName();


                // Prepare data for email template
                Map<String, String> valuesMap = new HashMap<>();
                valuesMap.put("quoteNumber", quote.getCode());
                valuesMap.put("clientName", client.getName());
                valuesMap.put("staffName", client.getServedByStaff() != null ? client.getServedByStaff().getFirstname() + " " + client.getServedByStaff().getLastname() : "");
                valuesMap.put("origin", origin);
                valuesMap.put("destination", destination);
                valuesMap.put("detailUrl", appProperties.getFrontendUrl() + "/quotes/request?keyword=" + quote.getCode());

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
                emailBaseDto.setToEmails(Collections.singletonList(client.getServedByStaff() != null ? client.getServedByStaff().getEmail() : ""));
                emailService.send(emailBaseDto);
            }

        } catch (Exception e) {
            log.error("Failed to send quote email", e);
        }
    }

    // From draft to requested, or draft to draft
    @Transactional
    @Override
    public void update(Long id, QuoteCreateRequestClient updateDto) {
        Quote quote = quoteRepository.findById(id)
                .orElseThrow(() -> new ApiException(new ApiMessageBase("Quote not found")));
        if (quote.getQuoteStatus() != null && quote.getQuoteStatus() != QuoteStatus.DRAFT && quote.getQuoteStatus() != QuoteStatus.REQUESTED && quote.getQuoteStatus() != QuoteStatus.CREATED && quote.getQuoteStatus() != QuoteStatus.ACCEPTED) {
            throw new ApiException("Quote already processed");
        }

        if (updateDto.getIsDraft() != null && updateDto.getIsDraft() && quote.getQuoteStatus() != QuoteStatus.DRAFT) {
            throw new ApiException("Can not update quote from " + quote.getQuoteStatus() + " to draft");
        }
        MapUtil.copyProperties(updateDto, quote);
        if (updateDto.getIsDraft() != null && updateDto.getIsDraft() && quote.getQuoteStatus() == QuoteStatus.DRAFT) {
            quote.setQuoteStatus(QuoteStatus.DRAFT);
        } else {
            quote.setQuoteStatus(QuoteStatus.REQUESTED);
        }
        quote.setProviderId(null);
        quote.setCargoChargePrice(null);
        quote.setCargoChargeMarkup(null);
        quote.setServiceChargeMarkup(null);
        quoteRepository.save(quote);
        PersistentUtil.flushAndClear();
        if (quote.getQuoteStatus() == QuoteStatus.REQUESTED) {
            sendQuoteRequestEmail(quote);
        }
    }

    // @Override
    // public QuoteRequestResponseDto detail(Long id) {
    //     Quote quote = get(id);
    //     return QuoteRequestMapper.createResponse(quote);
    // }

    // @Transactional
    // @Override
    // public void delete(Long id) {
    //     Quote quote = get(id);
    //     if (quote.getQuoteStatus() != null && quote.getQuoteStatus() != QuoteStatus.DRAFT) {
    //         throw new ApiException(new ApiMessageBase("Quote already processed"));
    //     }
    //     quoteRepository.delete(quote);
    //     PersistentUtil.flushAndClear();
    // }

    @Override
    public PageResponse<QuoteResponseDto> list(QuoteListRequest request) {
        Page<Quote> quotes = quoteRepository.listQuoteRequest(request);
        return PageResponse.buildPageDtoResponse(quotes, quote -> QuoteMapper.createResponse(quote, QuoteMapper.DetailIncludeFields));
    }

    @Override
    @Transactional
    public void requestQuote(LandingPageQuoteRequestDto request) {
        ClientCreateDto createDto = request.getClient();
        QuoteCreateRequest quoteRequestDto = request.getQuoteRequest();
        boolean exists = clientRepository.existsByEmail(createDto.getEmail());
        if (exists) {
            throw new ApiException("Client already exists with this email");
        }

        boolean existsStaffWithEmail = staffRepository.existsByEmail(createDto.getEmail());
        if (existsStaffWithEmail) {
            throw new ApiException("Staff already exists with this email");
        }

        CompanyInfo companyInfo = CompanyInfoMapper.createEntity(createDto.getCompanyInfo());
        companyInfoRepository.save(companyInfo);
        ContactPersonInfo contactPersonInfo = ContactPersonInfoMapper.createEntity(createDto.getContactPersonInfo());
        contactPersonInfoRepository.save(contactPersonInfo);

        createDto.setCompanyInfoId(companyInfo.getId());
        createDto.setContactPersonInfoId(contactPersonInfo.getId());
        Client client = ClientMapper.createEntity(createDto);
        Client latestClient = clientRepository.findFirstByOrderByIdDesc();
        client.setCode(ClientHelper.genClientCode(latestClient));
        Staff defaultStaff = staffRepository.findByCode(appProperties.getDefaultStaffCode());
        if (defaultStaff != null) {
            client.setServedBy(defaultStaff.getId());
        }
        String password = ClientHelper.genPassword();

        Staff staff = new Staff();
        staff.setEmail(client.getEmail());
        staff.setPassword(passwordEncoder.encode(password));
        staff.setCode(client.getCode());
        staff.setFirstname(client.getName());
        staff.setIsClient(true);
        staff.setForceChangePassword(false);
        staffRepository.save(staff);

        client.setStaffId(staff.getId());
        clientRepository.save(client);
        PersistentUtil.flushAndClear();

        Quote quote = QuoteRequestMapper.createEntity(request.getQuoteRequest());
        quote.setCode(quoteHelper.genQuoteCode());
        quote.setClientId(client.getId());
        quote.setIsRequest(true);
        quote.setQuoteStatus(QuoteStatus.REQUESTED);
        quoteRepository.save(quote);

        try {
            String emailTemplate = StreamUtils.copyToString(
                    new ClassPathResource("email/requestQuoteFromLandingPae.html").getInputStream(), defaultCharset());

            EmailBaseDto emailBaseDto = getEmailBaseDto(client, emailTemplate, password);
            emailService.send(emailBaseDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private EmailBaseDto getEmailBaseDto(Client client, String emailTemplate, String password) {
        Map<String, String> valuesMap = Map.of(
                "displayName", client.getName(),
                "quoteURL", appProperties.getFrontendUrl() + "/quotes/request",
                "email", client.getEmail(),
                "password", password
        );
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        String emailHtml = sub.replace(emailTemplate);
        EmailBaseDto emailBaseDto = new EmailBaseDto();
        emailBaseDto.setSubject("[FreightFlex] New Quote Request");
        emailBaseDto.setBody(emailHtml);
        emailBaseDto.setToEmails(List.of(client.getEmail()));
        return emailBaseDto;
    }

    // private Quote get(Long id) {
    //     Optional<Quote> quoteOptional = quoteRepository.findById(id);
    //     if (quoteOptional.isEmpty()) {
    //         throw new ApiException(new ApiMessageBase("Quote not found"));
    //     }
    //     return quoteOptional.get();
    // }
}
