package com.lcp.advancesearch.service.impl;

import com.google.gson.reflect.TypeToken;
import com.lcp.advancesearch.dto.AdvanceSearchJsonDataDto;
import com.lcp.advancesearch.dto.AdvanceSearchResponseDto;
import com.lcp.common.Constant;
import com.lcp.common.RequestHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@Component
@RequiredArgsConstructor
public class SyncDataAdvanceSearchService {

    @Value("classpath:advance_search.json")
    private Resource resource;

    private final RedisTemplate<String, Object> redisTemplate;

    @Async
    public void process() {
        log.info("Initializing sync AdvanceSearch from advance_search.json to Redis");
        HashOperations<String, String, List<AdvanceSearchResponseDto>> hashOperations = redisTemplate.opsForHash();

        String advanceSearchDataAsString = advanceSearchDataAsString();
        Type fieldMapType = new TypeToken<HashMap<String, List<AdvanceSearchJsonDataDto>>>() {
        }.getType();
        HashMap<String, List<AdvanceSearchJsonDataDto>> rawMapData = RequestHelper.fromJson(advanceSearchDataAsString, fieldMapType);
        Map<String, List<AdvanceSearchResponseDto>> advanceSearchMapData = rawMapData.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        v -> v.getValue().stream().map(this::mapResponse).collect(Collectors.toList())
                ));
        log.info("Sync AdvanceSearch to redis with total element: {}", rawMapData.size());

        // update redis
        try {
            redisTemplate.delete(Constant.ADVANCE_SEARCH_CACHE_TABLE);
        } catch (Exception e) {
            log.info("Sync AdvanceSearch delete failed");
        }
        hashOperations.putAll(Constant.ADVANCE_SEARCH_CACHE_TABLE, advanceSearchMapData);
    }

    private String advanceSearchDataAsString() {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private AdvanceSearchResponseDto mapResponse(AdvanceSearchJsonDataDto jsonData) {
        AdvanceSearchResponseDto responseDto = new AdvanceSearchResponseDto();
        responseDto.setOperandType(jsonData.getOperandType());
        responseDto.setColumnName(jsonData.getColumnName());
        if (jsonData.getClazz() != null) {
            try {
                Class<?> clazz = Class.forName("com.lcp.common.enumeration." + jsonData.getClazz());
                if (clazz.isEnum()) {
                    Object[] enumConstants = clazz.getEnumConstants();
                    // Replace "clazz" with the enum's possible values
                    responseDto.setEnums(Arrays.stream(enumConstants).map(Object::toString).collect(Collectors.toList()));
                }
            } catch (ClassNotFoundException e) {
                System.err.println("Class Enum not found: " + jsonData.getClazz());
            }
        }
        return responseDto;
    }
}
