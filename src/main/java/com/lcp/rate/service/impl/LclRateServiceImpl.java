package com.lcp.rate.service.impl;

import com.lcp.common.PageResponse;
import com.lcp.common.dto.BaseListRequest;
import com.lcp.exception.ApiException;
import com.lcp.rate.dto.LclRateCreateDto;
import com.lcp.rate.dto.LclRateResponseDto;
import com.lcp.rate.dto.LclRateUpdateDto;
import com.lcp.rate.entity.LclRate;
import com.lcp.rate.mapper.LclRateMapper;
import com.lcp.rate.repository.LclRateRepository;
import com.lcp.rate.repository.custom.LclRateRepositoryCustom;
import com.lcp.rate.service.LclRateService;
import com.lcp.util.MapUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LclRateServiceImpl implements LclRateService {
    private final LclRateRepository lclRateRepository;
    private final LclRateRepositoryCustom lclRateRepositoryCustom;

    @Override
    public void create(LclRateCreateDto createDto) {
        //Todo: validate user input
        LclRate lclRate = new LclRate();
        MapUtil.copyProperties(createDto, lclRate);
        lclRateRepository.save(lclRate);
    }

    @Override
    public void update(LclRateUpdateDto updateDto) {
        if (updateDto.getId() == null) {
            throw new ApiException("ID cannot be null");
        }
        LclRate lclRate = lclRateRepository.findById(updateDto.getId())
                .orElseThrow(() -> new ApiException("LCL rate not found"));

        MapUtil.copyProperties(updateDto, lclRate);
        lclRateRepository.save(lclRate);
    }

    @Override
    public LclRateResponseDto detail(Long id) {
        LclRate lclRate = lclRateRepository.findById(id)
                .orElseThrow(() -> new ApiException("LCL rate not found"));
        return LclRateMapper.createResponse(lclRate);
    }

    @Override
    public void delete(Long id) {
        LclRate lclRate = lclRateRepository.findById(id)
                .orElseThrow(() -> new ApiException("LCL rate not found"));
        lclRateRepository.delete(lclRate);
    }

    @Override
    public PageResponse<LclRateResponseDto> list(BaseListRequest request) {
        Page<LclRate> lclRates = lclRateRepositoryCustom.list(request);
        return PageResponse.buildPageDtoResponse(
                lclRates,
                LclRateMapper::createResponse
        );
    }
}
