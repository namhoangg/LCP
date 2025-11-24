package com.lcp.provider.service.impl;

import com.lcp.common.ApiMessageBase;
import com.lcp.common.PageResponse;
import com.lcp.exception.ApiException;
import com.lcp.provider.dto.*;
import com.lcp.provider.entity.CompanyInfo;
import com.lcp.provider.entity.ContactPersonInfo;
import com.lcp.provider.entity.Provider;
import com.lcp.provider.mapper.CompanyInfoMapper;
import com.lcp.provider.mapper.ContactPersonInfoMapper;
import com.lcp.provider.mapper.ProviderMapper;
import com.lcp.provider.repository.CompanyInfoRepository;
import com.lcp.provider.repository.ContactPersonInfoRepository;
import com.lcp.provider.repository.ProviderRepository;
import com.lcp.provider.service.ProviderService;
import com.lcp.quote.dto.CargoVolumeResponseDto;
import com.lcp.quote.repository.QuoteRepository;
import com.lcp.rate.entity.FclRate;
import com.lcp.rate.entity.FclRate_;
import com.lcp.rate.entity.LclRate;
import com.lcp.rate.mapper.FclRateMapper;
import com.lcp.rate.mapper.LclRateMapper;
import com.lcp.rate.repository.FclRateRepository;
import com.lcp.rate.repository.LclRateRepository;
import com.lcp.security.UserDetailsCustom;
import com.lcp.util.MapUtil;
import com.lcp.util.PersistentUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final CompanyInfoRepository companyInfoRepository;
    private final ContactPersonInfoRepository contactPersonInfoRepository;
    private final QuoteRepository quoteRepository;
    private final FclRateRepository fclRateRepository;
    private final LclRateRepository lclRateRepository;

    @Override
    @Transactional
    public ProviderResponseDto create(ProviderCreateDto createDto) {
        CompanyInfo companyInfo = CompanyInfoMapper.createEntity(createDto.getCompanyInfo());
        companyInfoRepository.save(companyInfo);
        createDto.setCompanyInfoId(companyInfo.getId());

        ContactPersonInfo contactPersonInfo = ContactPersonInfoMapper.createEntity(createDto.getContactPersonInfo());
        contactPersonInfoRepository.save(contactPersonInfo);
        createDto.setContactPersonInfoId(contactPersonInfo.getId());

        Provider provider = ProviderMapper.createEntity(createDto);
        providerRepository.save(provider);
        PersistentUtil.flushAndClear();
        return detail(provider.getId());
    }

    @Override
    @Transactional
    public ProviderResponseDto update(Long id, ProviderUpdateDto updateDto) {
        Provider provider = get(id);
        MapUtil.copyProperties(updateDto, provider);
        MapUtil.copyProperties(updateDto.getCompanyInfo(), provider.getCompanyInfo());
        MapUtil.copyProperties(updateDto.getContactPersonInfo(), provider.getContactPersonInfo());
        providerRepository.save(provider);
        PersistentUtil.flushAndClear();
        return detail(provider.getId());
    }

    @Override
    public ProviderResponseDto detail(Long id) {
        Provider provider = providerRepository.getOne(id);
        return ProviderMapper.createResponse(provider, ProviderMapper.DetailIncludeFields);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Provider provider = get(id);
        providerRepository.delete(provider);
    }

    @Override
    public PageResponse<ProviderResponseDto> list(ProviderListRequest request) {
        Long userId = UserDetailsCustom.getCurrentUserId();
        log.info("User ID: {}", userId);
        Page<Provider> providers = providerRepository.list(request);
        return PageResponse.buildPageDtoResponse(
                providers,
                provider -> ProviderMapper.createResponse(provider, request.getIncludeFields())
        );
    }

    @Override
    @Transactional
    public List<ProviderByQuoteResponse> getByQuote(GetByQuoteRequest request) {
        List<Provider> providers = providerRepository.findAllByQuote(request);

        List<Long> providerIds = providers.stream()
                .map(Provider::getId)
                .collect(Collectors.toList());

        List<FclRate> fclRate = fclRateRepository.findByProviderIdAndValidFromAndValidToAndOriginIdAndDestinationId(
                providerIds,
                LocalDate.now(),
                LocalDate.now(),
                request.getOriginId(),
                request.getDestinationId()
        );

        Map<Long, FclRate> fclRateMap = fclRate.stream()
                .collect(Collectors.toMap(FclRate::getProviderId, fclRateItem -> fclRateItem));

        return providers.stream()
                .map(provider -> {
                    ProviderByQuoteResponse response = new ProviderByQuoteResponse();
                    response.setProvider(ProviderMapper.createResponse(provider, ProviderMapper.DetailIncludeFields));
                    FclRate fclRateItem = fclRateMap.get(provider.getId());
                    if (fclRateItem != null) {
                        response.setFclRate(FclRateMapper.createResponse(fclRateItem, List.of(FclRate_.CURRENCY)));
                    }
                    return response;
                })
                .collect(Collectors.toList());
    }

    private Provider get(Long id) {
        Optional<Provider> providerOpt = providerRepository.findById(id);
        if (providerOpt.isEmpty()) {
            throw new ApiException(new ApiMessageBase("Provider not found"));
        }
        return providerOpt.get();
    }
}
