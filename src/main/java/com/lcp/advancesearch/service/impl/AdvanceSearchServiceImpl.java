package com.lcp.advancesearch.service.impl;

import com.lcp.advancesearch.dto.AdvanceSearchResponseDto;
import com.lcp.advancesearch.service.AdvanceSearchService;
import com.lcp.common.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdvanceSearchServiceImpl implements AdvanceSearchService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final SyncDataAdvanceSearchService syncAdvanceSearchService;
    private HashOperations<String, String, List<AdvanceSearchResponseDto>> hashOperations;

    @PostConstruct
    void init() {
        hashOperations = redisTemplate.opsForHash();
        syncAdvanceSearchService.process();
    }

    @Override
    public List<AdvanceSearchResponseDto> list(String tableName) {
        List<AdvanceSearchResponseDto> response = hashOperations.get(Constant.ADVANCE_SEARCH_CACHE_TABLE, tableName);
        return Optional.ofNullable(response).orElse(Collections.emptyList());
    }
}
