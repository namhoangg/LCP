package com.lcp.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;
import com.lcp.common.Constant;
import com.lcp.common.RequestHelper;
import com.lcp.common.enumeration.GeneralStatus;
import com.lcp.util.PageUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
@SuperBuilder
public class BaseListRequest {
    private Boolean pagingIgnore = false;
    private GeneralStatus status;
    private String keyword;
    private Integer page;
    private Integer size;
    private String sortBy = "";
    private Integer dayRange;
    private String searchCondition;
    private String fieldNameMap;
    private List<Long> excludeIds;

    @JsonIgnore
    private List<String> includeFields;
    @JsonIgnore
    private List<SearchCondition> searchConditions; // convert at backend
    @JsonIgnore
    private Map<String, String> fieldNameMaps; // convert at backend

    public BaseListRequest() {
        this.sortBy = "";
    }

    public void setSearchCondition(String searchCondition) {
        this.searchCondition = searchCondition;
        if (StringUtils.isNotEmpty(searchCondition)) {
            var searchConditions = RequestHelper.fromJson(searchCondition, SearchCondition[].class);
            this.searchConditions = Arrays.asList(searchConditions);
        }
    }

    public void setFieldNameMap(String fieldNameMap) {
        this.fieldNameMap = fieldNameMap;
        if (!Strings.isNullOrEmpty(fieldNameMap)) {
            Type fieldMapType = new TypeToken<Map<String, String>>() {
            }.getType();
            fieldNameMaps = RequestHelper.fromJson(fieldNameMap, fieldMapType);
        }
    }

    @JsonIgnore
    public Pageable getPageable() {
        return PageUtil.createPage(page, size);
    }

    @JsonIgnore
    public Sort.Direction getSortDirection() {
        String[] sortByArr = sortBy.split(":");
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortByArr.length > 1) {
            String directionParam = sortByArr[1];
            if (directionParam.equalsIgnoreCase(Constant.DESCENDING_SORT)) {
                direction = Sort.Direction.DESC;
            }
        }
        return direction;
    }

    public boolean hasSortDirection() {
        if (Strings.isNullOrEmpty(sortBy))
            return false;
        String[] sortByArr = sortBy.split(":");
        return sortByArr.length > 1;
    }

    @JsonIgnore
    public String getSortField() {
        if (StringUtils.isEmpty(sortBy)) {
            return "";
        }
        String[] sortByArr = sortBy.split(":");
        return sortByArr[0];
    }
}
