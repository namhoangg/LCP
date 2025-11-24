package com.lcp.rate.service.impl;

import com.lcp.common.ApiMessageBase;
import com.lcp.common.PageResponse;
import com.lcp.exception.ApiException;
import com.lcp.rate.dto.ServiceChargeCreateDto;
import com.lcp.rate.dto.ServiceChargeListRequest;
import com.lcp.rate.dto.ServiceChargeResponseDto;
import com.lcp.rate.dto.ServiceChargeUpdateDto;
import com.lcp.rate.entity.ServiceCharge;
import com.lcp.rate.mapper.ServiceChargeMapper;
import com.lcp.rate.repository.ServiceChargeRepository;
import com.lcp.rate.service.ServiceChargeService;
import com.lcp.util.MapUtil;
import com.lcp.util.PersistentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceChargeServiceImpl implements ServiceChargeService {

    private final ServiceChargeRepository serviceChargeRepository;

    @Override
    @Transactional
    public ServiceChargeResponseDto create(ServiceChargeCreateDto createDto) {
        ServiceCharge serviceCharge = ServiceChargeMapper.createEntity(createDto);
        serviceChargeRepository.save(serviceCharge);
        PersistentUtil.flushAndClear();
        return detail(serviceCharge.getId());
    }

    @Override
    @Transactional
    public ServiceChargeResponseDto update(Long id, ServiceChargeUpdateDto updateDto) {
        ServiceCharge serviceCharge = get(id);
        MapUtil.copyProperties(updateDto, serviceCharge);
        serviceChargeRepository.save(serviceCharge);
        PersistentUtil.flushAndClear();
        return detail(serviceCharge.getId());
    }

    @Override
    public ServiceChargeResponseDto detail(Long id) {
        ServiceCharge serviceCharge = get(id);
        return ServiceChargeMapper.createResponse(serviceCharge);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ServiceCharge serviceCharge = get(id);
        serviceChargeRepository.delete(serviceCharge);
    }

    @Override
    public PageResponse<ServiceChargeResponseDto> list(ServiceChargeListRequest request) {
        Page<ServiceCharge> serviceCharges = serviceChargeRepository.list(request);
        return PageResponse.buildPageDtoResponse(serviceCharges, ServiceChargeMapper::createResponse);
    }

    public ServiceCharge get(Long id) {
        Optional<ServiceCharge> serviceChargeOptional = serviceChargeRepository.findById(id);
        if (serviceChargeOptional.isEmpty()) {
            throw new ApiException(new ApiMessageBase("Service Charge not found"));
        }
        return serviceChargeOptional.get();
    }
}
