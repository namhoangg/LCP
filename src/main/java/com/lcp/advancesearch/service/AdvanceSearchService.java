package com.lcp.advancesearch.service;

import com.lcp.advancesearch.dto.AdvanceSearchResponseDto;

import java.util.List;

public interface AdvanceSearchService {

    List<AdvanceSearchResponseDto> list(String tableName);
}