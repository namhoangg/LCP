package com.lcp.provider.dto;

import com.lcp.common.dto.BaseListRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Data
public class ProviderListRequest extends BaseListRequest {
}
