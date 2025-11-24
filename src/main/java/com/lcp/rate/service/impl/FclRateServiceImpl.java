package com.lcp.rate.service.impl;

import com.lcp.common.ApiMessageBase;
import com.lcp.common.PageResponse;
import com.lcp.common.enumeration.TransportType;
import com.lcp.exception.ApiException;
import com.lcp.rate.dto.FclRateCreateDto;
import com.lcp.rate.dto.FclRateDetailCreateDto;
import com.lcp.rate.dto.FclRateDetailUpdateDto;
import com.lcp.rate.dto.FclRateListRequest;
import com.lcp.rate.dto.FclRateResponseDto;
import com.lcp.rate.dto.FclRateUpdateDto;
import com.lcp.rate.entity.FclRate;
import com.lcp.rate.entity.FclRateDetail;
import com.lcp.rate.mapper.FclRateMapper;
import com.lcp.rate.repository.FclRateDetailRepository;
import com.lcp.rate.repository.FclRateRepository;
import com.lcp.rate.service.FclRateService;
import com.lcp.util.MapUtil;
import com.lcp.util.PersistentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FclRateServiceImpl implements FclRateService {
    private final FclRateRepository fclRateRepository;
    private final FclRateDetailRepository fclRateDetailRepository;

    @Override
    @Transactional
    public FclRateResponseDto create(FclRateCreateDto createDto) {
        FclRate fclRate = FclRateMapper.createEntity(createDto);
        fclRate.setTransportType(TransportType.ROAD_FCL);
        fclRateRepository.save(fclRate);

        for (FclRateDetailCreateDto detail : createDto.getDetails()) {
            FclRateDetail fclRateDetail = new FclRateDetail();
            fclRateDetail.setFclRateId(fclRate.getId());
            fclRateDetail.setContainerTypeId(detail.getContainerTypeId());
            fclRateDetail.setRate(detail.getRate());
            fclRateDetailRepository.save(fclRateDetail);
        }

        PersistentUtil.flushAndClear();
        return detail(fclRate.getId());
    }

    @Override
    @Transactional
    public FclRateResponseDto update(Long id, FclRateUpdateDto updateDto) {
        FclRate fclRate = get(id);
        MapUtil.copyProperties(updateDto, fclRate);
        fclRate.setDetails(null);
        fclRateRepository.save(fclRate);

        fclRateDetailRepository.deleteByFclRateId(id);
        List<FclRateDetail> fclRateDetails = new ArrayList<>();
        for (FclRateDetailUpdateDto detail : updateDto.getDetails()) {
            FclRateDetail fclRateDetail = new FclRateDetail();
            fclRateDetail.setFclRateId(fclRate.getId());
            fclRateDetail.setContainerTypeId(detail.getContainerTypeId());
            fclRateDetail.setRate(detail.getRate());
            fclRateDetails.add(fclRateDetail);
        }
        fclRateDetailRepository.saveAll(fclRateDetails);

        return null;
    }

    @Override
    public FclRateResponseDto detail(Long id) {
        FclRate fclRate = get(id);
        return FclRateMapper.createResponse(fclRate, FclRateMapper.DetailIncludeFields);
    }

    @Override
    @Transactional
    @Deprecated
    public void delete(Long id) {
        FclRate fclRate = get(id);
        fclRateRepository.delete(fclRate);
    }

    @Override
    public PageResponse<FclRateResponseDto> list(FclRateListRequest request) {
        Page<FclRate> fclRates = fclRateRepository.list(request);
        return PageResponse.buildPageDtoResponse(
                fclRates,
                fclRate -> FclRateMapper.createResponse(fclRate, request.getIncludeFields())
        );
    }

    @Override
    public FclRateResponseDto getByProvider(Long providerId) {
        FclRate fclRate = fclRateRepository.findByProviderId(providerId);
        if (fclRate == null) {
            throw new ApiException(new ApiMessageBase("FCL Rate not found for provider"));
        }
        return FclRateMapper.createResponse(fclRate, FclRateMapper.DetailIncludeFields);
    }

    private FclRate get(Long id) {
        Optional<FclRate> fclRateOptional = fclRateRepository.findById(id);
        if (fclRateOptional.isEmpty()) {
            throw new ApiException(new ApiMessageBase("FCL Rate not found"));
        }
        return fclRateOptional.get();
    }
}
