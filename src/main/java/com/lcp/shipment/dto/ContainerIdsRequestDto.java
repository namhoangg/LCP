package com.lcp.shipment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ContainerIdsRequestDto {
    @JsonIgnore
    private Long shipmentId;
    @NotEmpty
    private List<Long> ids;
}
