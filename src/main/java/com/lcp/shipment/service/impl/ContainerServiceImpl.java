package com.lcp.shipment.service.impl;

import com.lcp.common.PageResponse;
import com.lcp.exception.ApiException;
import com.lcp.shipment.dto.*;
import com.lcp.shipment.entity.Container;
import com.lcp.shipment.mapper.ContainerMapper;
import com.lcp.shipment.repository.ContainerRepository;
import com.lcp.shipment.service.ContainerService;
import com.lcp.util.MapUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ContainerServiceImpl implements ContainerService {
    private final ContainerRepository containerRepository;

    @Transactional
    @Override
    public void upsert(ContainerWrapperUpsertDto upsertDto) {
        List<Container> existedContainers = containerRepository.findByShipmentId(upsertDto.getShipmentId());
        Map<Long, Container> existedContainerMap = existedContainers.stream().collect(Collectors.toMap(Container::getId, Function.identity()));
        List<ContainerUpsertDto> containerUpsertDtos = upsertDto.getContainers();
        if (containerUpsertDtos.stream().anyMatch(c -> c.getId() != null && !existedContainerMap.containsKey(c.getId()))) {
            throw new ApiException("Container not found");
        }

        List<Container> containersCreate = new ArrayList<>();
        List<Container> containersUpdate = new ArrayList<>();
        for (ContainerUpsertDto containerDto : containerUpsertDtos) {
            if (containerDto.getId() == null) {
                Container container = ContainerMapper.createEntity(containerDto);
                container.setShipmentId(upsertDto.getShipmentId());
                containersCreate.add(container);
            } else {
                Container container = existedContainerMap.get(containerDto.getId());
                MapUtil.copyProperties(containerDto, container);
                containersUpdate.add(container);
            }
        }
        containerRepository.saveAll(containersCreate);
        containerRepository.saveAll(containersUpdate);

    }

    @Override
    public List<ContainerTypeQuantityDto> countByContainerType(Long shipmentId) {
        List<Container> containers = containerRepository.findByShipmentId(shipmentId);
        return containers.stream()
                .collect(Collectors.groupingBy(Container::getContainerTypeId, Collectors.counting()))
                .entrySet().stream()
                .map(entry -> new ContainerTypeQuantityDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void bulkDelete(ContainerIdsRequestDto deleteDto) {
        List<Long> deleteIds = deleteDto.getIds();
        List<Container> existedContainers = containerRepository.findByShipmentId(deleteDto.getShipmentId());
        List<Container> deleteContainers = existedContainers.stream()
                .filter(c -> deleteIds.contains(c.getId()))
                .collect(Collectors.toList());
        if (deleteContainers.size() != deleteIds.size()) {
            throw new ApiException("Container not found");
        }
        containerRepository.deleteAll(deleteContainers);
    }

    @Override
    public PageResponse<ContainerResponseDto> list(ContainerListRequest request) {
        Page<Container> page = containerRepository.list(request);
        return PageResponse.buildPageDtoResponse(page, ContainerMapper::createResponse);
    }
}
