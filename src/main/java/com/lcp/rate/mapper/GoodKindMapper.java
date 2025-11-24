package com.lcp.rate.mapper;

import com.lcp.rate.dto.GoodKindResponse;
import com.lcp.rate.entity.GoodKind;

public class GoodKindMapper {
    public static GoodKindResponse createResponse(GoodKind goodKind) {
        GoodKindResponse response = new GoodKindResponse();
        response.setId(goodKind.getId());
        response.setName(goodKind.getName());
        response.setDescription(goodKind.getDescription());
        response.setIsRefrigerated(goodKind.getIsRefrigerated());
        response.setIsDefault(goodKind.getIsDefault());
        return response;
    }
}
