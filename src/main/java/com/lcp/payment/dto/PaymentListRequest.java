package com.lcp.payment.dto;

import com.lcp.common.dto.BaseListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Data
public class PaymentListRequest extends BaseListRequest {
    private Long shipmentId;
}
