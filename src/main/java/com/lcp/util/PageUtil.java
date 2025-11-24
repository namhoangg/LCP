package com.lcp.util;

import com.lcp.common.dto.BaseListRequest;
import com.lcp.common.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.*;

public class PageUtil {
    public static Pageable createPage(Integer page, Integer size) {
        int pageValue = page == null ? 0 : page;
        int sizeValue = size == null ? Constant.DEFAULT_PAGE_SIZE : size;
        return PageRequest.of(pageValue, sizeValue);
    }

    public static <T, C extends BaseListRequest> Order generateOrder(C criteriaRequest, CriteriaBuilder criteriaBuilder,
                                                                     Root<T> root, String columnSortDefault) {
        return generateOrder(criteriaRequest, criteriaBuilder, root, columnSortDefault, false);
    }

    public static <T, C extends BaseListRequest> Order generateOrder(C criteriaRequest, CriteriaBuilder criteriaBuilder,
                                                                     Root<T> root, String columnSortDefault, boolean isDesc) {
        if (criteriaRequest == null) {
            return criteriaBuilder.desc(root.get(columnSortDefault));
        }

        String sortBy = criteriaRequest.getSortBy();
        String sortField = columnSortDefault;

        if (!StringUtils.isEmpty(sortBy)) {
            String[] sortByArr = sortBy.split(":");
            sortField = sortByArr[0];
            if (sortByArr.length > 1) {
                String directionParam = sortByArr[1];
                if (directionParam.toUpperCase().equals(Constant.ASCENDING_SORT)) {
                    return criteriaBuilder.asc(root.get(sortField));
                } else {
                    return criteriaBuilder.desc(root.get(sortField));
                }
            }
        }

        // default case
        if (isDesc || sortField.equals(Constant.COLUMN_DEFAULT_SORT)
                || sortField.equals(Constant.COLUMN_CREATED_AT)
                || sortField.equals(Constant.COLUMN_ID))
            return criteriaBuilder.desc(root.get(sortField));
        return criteriaBuilder.asc(root.get(sortField));
    }

    public static <C extends BaseListRequest> Order generateOrder(C criteriaRequest, CriteriaBuilder criteriaBuilder,
                                                                  Join<?, ?> join, String sortField) {
        if (criteriaRequest.getSortDirection() == Sort.Direction.ASC) {
            return criteriaBuilder.asc(join.get(sortField));
        }
        return criteriaBuilder.desc(join.get(sortField));
    }

    public static <C extends BaseListRequest> Order generateSortOrder(C criteriaRequest, CriteriaBuilder criteriaBuilder,
                                                                      From join, String sortField) {
        if (criteriaRequest == null) {
            return criteriaBuilder.desc(join.get(sortField));
        }

        if (!criteriaRequest.hasSortDirection() &&
                (sortField.equals(Constant.COLUMN_DEFAULT_SORT)
                        || sortField.equals(Constant.COLUMN_CREATED_AT)
                        || sortField.equals(Constant.COLUMN_ID)
                        || sortField.equals(Constant.COLUMN_DISPLAY_POSITION))) {
            return criteriaBuilder.desc(join.get(sortField));
        }

        if (criteriaRequest.getSortDirection() == Sort.Direction.ASC) {
            return criteriaBuilder.asc(join.get(sortField));
        }
        return criteriaBuilder.desc(join.get(sortField));
    }
}
