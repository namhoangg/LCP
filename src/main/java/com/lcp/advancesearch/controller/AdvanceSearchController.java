package com.lcp.advancesearch.controller;

import com.lcp.advancesearch.dto.AdvanceSearchResponseDto;
import com.lcp.advancesearch.service.AdvanceSearchService;
import com.lcp.common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/advance-search")
public class AdvanceSearchController {
    @Autowired
    private AdvanceSearchService advanceSearchService;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response<List<AdvanceSearchResponseDto>> list(@RequestParam String tableName) {
        return Response.success(advanceSearchService.list(tableName));
    }
}
