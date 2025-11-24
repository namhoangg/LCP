package com.lcp.shipment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lcp.common.dto.BaseListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Data
public class ContainerListRequest extends BaseListRequest {
    @JsonIgnore
    private Long shipmentId;
}
