package com.lcp.rate.service.impl;

import com.lcp.common.ApiMessageBase;
import com.lcp.common.PageResponse;
import com.lcp.exception.ApiException;
import com.lcp.rate.dto.ChargeTypeCreateDto;
import com.lcp.rate.dto.ChargeTypeListRequest;
import com.lcp.rate.dto.ChargeTypeResponseDto;
import com.lcp.rate.dto.ChargeTypeUpdateDto;
import com.lcp.rate.entity.ChargeType;
import com.lcp.rate.mapper.ChargeTypeMapper;
import com.lcp.rate.repository.ChargeTypeRepository;
import com.lcp.rate.service.ChargeTypeService;
import com.lcp.util.MapUtil;
import com.lcp.util.PersistentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChargeTypeServiceImpl implements ChargeTypeService {
    private final ChargeTypeRepository chargeTypeRepository;

    @Override
    @Transactional
    public ChargeTypeResponseDto create(ChargeTypeCreateDto createDto) {
        ChargeType chargeType = ChargeTypeMapper.createEntity(createDto);
        chargeTypeRepository.save(chargeType);
        PersistentUtil.flushAndClear();
        return detail(chargeType.getId());
    }

    @Override
    @Transactional
    public ChargeTypeResponseDto update(Long id, ChargeTypeUpdateDto updateDto) {
        ChargeType chargeType = get(id);
        MapUtil.copyProperties(updateDto, chargeType);
        chargeTypeRepository.save(chargeType);
        PersistentUtil.flushAndClear();
        return detail(chargeType.getId());
    }

    @Override
    public ChargeTypeResponseDto detail(Long id) {
        ChargeType chargeType = get(id);
        return ChargeTypeMapper.createResponse(chargeType);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ChargeType chargeType = get(id);
        chargeTypeRepository.delete(chargeType);
    }

    @Override
    public PageResponse<ChargeTypeResponseDto> list(ChargeTypeListRequest request) {
        Page<ChargeType> chargeTypes = chargeTypeRepository.list(request);
        return PageResponse.buildPageDtoResponse(chargeTypes, ChargeTypeMapper::createResponse);
    }

    private ChargeType get(Long id) {
        Optional<ChargeType> chargeTypeOptional = chargeTypeRepository.findById(id);
        if (chargeTypeOptional.isEmpty()) {
            throw new ApiException(new ApiMessageBase("Charge Type not found"));
        }
        return chargeTypeOptional.get();
    }
}
