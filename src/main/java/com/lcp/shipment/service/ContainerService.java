package com.lcp.shipment.service;

import com.lcp.common.PageResponse;
import com.lcp.shipment.dto.*;

import java.util.List;

public interface ContainerService {
    void upsert(ContainerWrapperUpsertDto upsertDto);

    List<ContainerTypeQuantityDto> countByContainerType(Long shipmentId);

    void bulkDelete(ContainerIdsRequestDto deleteDto);

    PageResponse<ContainerResponseDto> list(ContainerListRequest request);
}
