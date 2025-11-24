package com.lcp.common;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.*;
import java.util.function.Function;

@Data
public class PageResponse<T> {
    private int page;
    private int size;
    private long total;
    private Map<String, Object> extra = new HashMap<>();
    private List<T> data;

    public static <T> PageResponse<T> buildPageResponse(Page<T> page) {
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setTotal(page.getTotalElements());
        pageResponse.setSize(page.getPageable().getPageSize());
        pageResponse.setPage(page.getPageable().getPageNumber());
        pageResponse.setData(page.getContent());
        return pageResponse;
    }

    public static <S, T> PageResponse<T> convertPageResponse(PageResponse<S> page, Function<S, T> mapFunction) {
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setTotal(page.getTotal());
        pageResponse.setSize(page.getSize());
        pageResponse.setPage(page.getPage());
        pageResponse.setData(RequestHelper.createDtoList(page.getData(), mapFunction));
        return pageResponse;
    }

    public static <S, T> PageResponse<T> buildPageDtoResponse(Page<S> page, Function<S, T> mapFunction) {
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setTotal(page.getTotalElements());
        pageResponse.setSize(page.getPageable().getPageSize());
        pageResponse.setPage(page.getPageable().getPageNumber());
        pageResponse.setData(RequestHelper.createDtoList(page.getContent(), mapFunction));
        return pageResponse;
    }

    public static <T> PageResponse<T> fromListResponse(List<T> responseList) {
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setTotal(responseList.size());
        pageResponse.setSize(responseList.size());
        pageResponse.setPage(0);
        pageResponse.setData(responseList);
        return pageResponse;
    }

    public static <T> PageResponse<T> fromSetResponse(Set<T> responseList) {
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setTotal(responseList.size());
        pageResponse.setSize(responseList.size());
        pageResponse.setPage(0);
        pageResponse.setData(List.copyOf(responseList));
        return pageResponse;
    }

    public static <T> PageResponse<T> buildEmptyResponse() {
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setTotal(0);
        pageResponse.setSize(0);
        pageResponse.setPage(0);
        pageResponse.setData(new ArrayList<>());
        return pageResponse;
    }
}
