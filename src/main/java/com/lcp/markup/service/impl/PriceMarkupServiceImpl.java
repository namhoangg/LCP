package com.lcp.markup.service.impl;

import com.lcp.common.ApiMessageBase;
import com.lcp.common.PageResponse;
import com.lcp.common.enumeration.GeneralStatus;
import com.lcp.exception.ApiException;
import com.lcp.markup.dto.PriceMarkupCreateDto;
import com.lcp.markup.dto.PriceMarkupListRequest;
import com.lcp.markup.dto.PriceMarkupResponseDto;
import com.lcp.markup.dto.PriceMarkupUpdateDto;
import com.lcp.markup.entity.PriceMarkup;
import com.lcp.markup.entity.PriceMarkupDetail;
import com.lcp.markup.mapper.PriceMarkupDetailMapper;
import com.lcp.markup.mapper.PriceMarkupMapper;
import com.lcp.markup.repository.PriceMarkupDetailRepository;
import com.lcp.markup.repository.PriceMarkupRepository;
import com.lcp.markup.service.PriceMarkupService;
import com.lcp.util.MapUtil;
import com.lcp.util.PersistentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceMarkupServiceImpl implements PriceMarkupService {
    private final PriceMarkupRepository priceMarkupRepository;
    private final PriceMarkupDetailRepository priceMarkupDetailRepository;

    @Transactional
    @Override
    public PriceMarkupResponseDto create(PriceMarkupCreateDto createDto) {
        PriceMarkup priceMarkup = PriceMarkupMapper.createEntity(createDto);
        priceMarkupRepository.save(priceMarkup);

        if (createDto.getPriceMarkupDetails() != null) {
            List<PriceMarkupDetail> priceMarkupDetailList = createDto.getPriceMarkupDetails().stream()
                    .map(detailDto -> {
                        PriceMarkupDetail detail = PriceMarkupDetailMapper.createEntity(detailDto);
                        detail.setPriceMarkupId(priceMarkup.getId());
                        return detail;
                    })
                    .collect(Collectors.toList());
            priceMarkupDetailRepository.saveAll(priceMarkupDetailList);
        }

        PersistentUtil.flushAndClear();
        return detail(priceMarkup.getId());
    }

    @Transactional
    @Override
    public PriceMarkupResponseDto update(Long id, PriceMarkupUpdateDto updateDto) {
        PriceMarkup priceMarkup = get(id);
        MapUtil.copyProperties(updateDto, priceMarkup);
        priceMarkupRepository.save(priceMarkup);

        if (updateDto.getPriceMarkupDetails() != null) {
            List<PriceMarkupDetail> priceMarkupDetailList = updateDto.getPriceMarkupDetails().stream()
                    .map(detailDto -> {
                        PriceMarkupDetail detail = priceMarkupDetailRepository.findById(detailDto.getId())
                                .orElseThrow(() -> new ApiException(new ApiMessageBase("PriceMarkupDetail not found")));
                        MapUtil.copyProperties(detailDto, detail);
                        return detail;
                    })
                    .collect(Collectors.toList());
            priceMarkupDetailRepository.saveAll(priceMarkupDetailList);
        }

        PersistentUtil.flushAndClear();
        return detail(priceMarkup.getId());
    }

    @Override
    public PriceMarkupResponseDto detail(Long id) {
        PriceMarkup priceMarkup = get(id);
        return PriceMarkupMapper.createResponse(priceMarkup);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        PriceMarkup priceMarkup = get(id);
        priceMarkup.setStatus(GeneralStatus.DELETED);
        priceMarkupRepository.save(priceMarkup);
    }

    @Override
    public PageResponse<PriceMarkupResponseDto> list(PriceMarkupListRequest request) {
        Page<PriceMarkup> page = priceMarkupRepository.list(request);
        return PageResponse.buildPageDtoResponse(page, PriceMarkupMapper::createResponse);
    }

    private PriceMarkup get(Long id) {
        Optional<PriceMarkup> priceMarkupOptional = priceMarkupRepository.findById(id);
        if (priceMarkupOptional.isEmpty()) {
            throw new ApiException(new ApiMessageBase("PriceMarkup not found"));
        }
        return priceMarkupOptional.get();
    }
}
