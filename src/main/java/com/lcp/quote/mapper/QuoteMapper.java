package com.lcp.quote.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lcp.client.mapper.ClientMapper;
import com.lcp.common.enumeration.CurrencyHardCode;
import com.lcp.provider.mapper.ProviderMapper;
import com.lcp.quote.dto.QuoteResponseDto;
import com.lcp.quote.entity.Quote;
import com.lcp.quote.entity.Quote_;
import com.lcp.rate.mapper.GoodKindMapper;
import com.lcp.setting.mapper.UnlocoMapper;
import com.lcp.util.MapUtil;
import com.lcp.util.MathUtil;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

public class QuoteMapper {
    public static final List<String> DetailIncludeFields = List.of(
            Quote_.CLIENT,
            Quote_.PROVIDER,
            Quote_.ORIGIN,
            Quote_.DESTINATION,
            Quote_.GOOD_KIND
    );

    public static QuoteResponseDto createResponse(Quote entity, List<String> includeFields) {
        QuoteResponseDto responseDto = new QuoteResponseDto();
        MapUtil.copyProperties(entity, responseDto);

        if (CollectionUtils.isNotEmpty(includeFields)) {
            if (entity.getClient() != null && includeFields.contains(Quote_.CLIENT)) {
                responseDto.setClient(ClientMapper.createResponse(entity.getClient(), ClientMapper.DetailIncludeFields));
            }
            if (entity.getProvider() != null && includeFields.contains(Quote_.PROVIDER)) {
                responseDto.setProvider(ProviderMapper.createResponse(entity.getProvider(), ProviderMapper.DetailIncludeFields));
            }
            if (entity.getOrigin() != null && includeFields.contains(Quote_.ORIGIN)) {
                responseDto.setOrigin(UnlocoMapper.createResponse(entity.getOrigin()));
            }
            if (entity.getDestination() != null && includeFields.contains(Quote_.DESTINATION)) {
                responseDto.setDestination(UnlocoMapper.createResponse(entity.getDestination()));
            }
            if (entity.getGoodKind() != null && includeFields.contains(Quote_.GOOD_KIND)) {
                responseDto.setGoodKind(GoodKindMapper.createResponse(entity.getGoodKind()));
            }
        }

//        if (entity.getCargoChargePrice() != null && !entity.getCargoChargePrice().isNull()) {
//            responseDto.setCargoChargePriceForClient(getCargoChargePriceForClient(entity.getCargoChargePrice()));
//        }
//        if (entity.getServiceChargeMarkup() != null) {
//            responseDto.setServiceChargeForClient(getServiceChargeForClient(entity.getServiceChargeMarkup()));
//        }

        return responseDto;
    }

    private static JsonNode getCargoChargePriceForClient(JsonNode cargoChargePrice) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode inputArray = (ArrayNode) cargoChargePrice;

        // Create output array node
        ArrayNode outputArray = mapper.createArrayNode();

        for (JsonNode item : inputArray) {
            Long containerTypeId = item.get("containerTypeId").asLong();
            BigDecimal basePrice = item.get("basePrice").decimalValue();
            BigDecimal markup = item.get("markup").decimalValue();
            Long currencyId = item.get("currencyId").asLong();

            // Calculate totalPrice = basePrice * (1 + markup)
            BigDecimal totalPrice = MathUtil.formatNumber(basePrice.multiply(BigDecimal.ONE.add(markup)), CurrencyHardCode.getCodeFromId(currencyId));

            // Create new JSON object
            ObjectNode newItem = mapper.createObjectNode();
            newItem.put("containerTypeId", containerTypeId);
            newItem.put("totalPrice", totalPrice);
            newItem.put("currencyId", currencyId);

            outputArray.add(newItem);
        }

        return outputArray;
    }

    private static JsonNode getServiceChargeForClient(JsonNode serviceChargeMarkup) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode inputArray = (ArrayNode) serviceChargeMarkup;

        // Create output array node
        ArrayNode outputArray = mapper.createArrayNode();

        for (JsonNode item : inputArray) {
            Long containerTypeId = item.get("serviceChargeId").asLong();
            BigDecimal basePrice = item.get("basePrice").decimalValue();
            BigDecimal markup = item.get("markup").decimalValue();
            Long currencyId = item.get("currencyId").asLong();

            // Calculate totalPrice = basePrice * (1 + markup)
            BigDecimal totalPrice = MathUtil.formatNumber(basePrice.multiply(BigDecimal.ONE.add(markup)), CurrencyHardCode.getCodeFromId(currencyId));

            // Create new JSON object
            ObjectNode newItem = mapper.createObjectNode();
            newItem.put("serviceChargeId", containerTypeId);
            newItem.put("totalPrice", totalPrice);
            newItem.put("currencyId", currencyId);

            outputArray.add(newItem);
        }

        return outputArray;
    }
}
