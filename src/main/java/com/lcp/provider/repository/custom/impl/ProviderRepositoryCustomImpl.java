package com.lcp.provider.repository.custom.impl;

import com.google.common.base.Strings;
import com.lcp.exception.ApiException;
import com.lcp.provider.dto.GetByQuoteRequest;
import com.lcp.provider.dto.ProviderListRequest;
import com.lcp.provider.entity.Provider;
import com.lcp.provider.entity.Provider_;
import com.lcp.provider.repository.custom.ProviderRepositoryCustom;
import com.lcp.rate.entity.FclRate;
import com.lcp.rate.entity.FclRate_;
import com.lcp.rate.entity.FclRateDetail;
import com.lcp.rate.entity.FclRateDetail_;
import com.lcp.util.PageUtil;
import com.lcp.util.QueryUtil;
import com.lcp.util.SearchUtil;

import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Log4j2
public class ProviderRepositoryCustomImpl implements ProviderRepositoryCustom {

    @Autowired
    EntityManager entityManager;

    @Override
    public Page<Provider> list(ProviderListRequest request) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Provider> query = builder.createQuery(Provider.class);
        Root<Provider> root = query.from(Provider.class);
        query.select(root).distinct(true);
        List<Predicate> predicates = new ArrayList<>();

        if (!Strings.isNullOrEmpty(request.getKeyword())) {
            String keywordLike = SearchUtil.format(request.getKeyword());
            predicates.add(builder.or(
                    builder.like(builder.lower(root.get(Provider_.CODE)), keywordLike),
                    builder.like(builder.lower(root.get(Provider_.NAME)), keywordLike)
            ));
        }
        query.where(predicates.toArray(new Predicate[]{}));
        query.orderBy(PageUtil.generateOrder(request, builder, root, Provider_.NAME));
        TypedQuery<Provider> typedQuery = entityManager.createQuery(query);
        var total = QueryUtil.count(builder, query, root);
        List<Provider> list;
        Pageable pageable = request.getPageable();

        // Check if pagingIgnore is true
        if (request.getPagingIgnore()) {
            // Retrieve all results without pagination
            list = typedQuery.getResultList();
            return new PageImpl<>(list, pageable, list.size());
        } else {
            // Apply pagination as before
            list = typedQuery.setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
            return new PageImpl<>(list, pageable, total);
        }
    }

    @Override
    public List<Provider> findAllByQuote(GetByQuoteRequest request) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Provider> query = builder.createQuery(Provider.class);
        Root<Provider> root = query.from(Provider.class);
        query.select(root).distinct(true);
        List<Predicate> predicates = new ArrayList<>();

        // Validate request parameters
        if (request == null || request.getOriginId() == null || request.getDestinationId() == null || 
            request.getEtd() == null || request.getContainerTypeIds() == null) {
            return Collections.emptyList();
        }

        Join<Provider, FclRate> fclRateJoin = root.join(Provider_.fclRates, JoinType.INNER);
        Join<FclRate, FclRateDetail> fclRateDetailJoin = fclRateJoin.join(FclRate_.details, JoinType.INNER);

        // Match origin and destination
        predicates.add(builder.equal(fclRateJoin.get(FclRate_.ORIGIN_ID), request.getOriginId()));
        predicates.add(builder.equal(fclRateJoin.get(FclRate_.DESTINATION_ID), request.getDestinationId()));

        // Check if the rate is valid for the current date
        predicates.add(builder.or(builder.lessThanOrEqualTo(fclRateJoin.get(FclRate_.VALID_FROM), request.getEtd()),
                builder.isNull(fclRateJoin.get(FclRate_.VALID_FROM))));
        predicates.add(builder.or(builder.greaterThanOrEqualTo(fclRateJoin.get(FclRate_.VALID_TO), request.getEtd()),
                builder.isNull(fclRateJoin.get(FclRate_.VALID_TO))));

        // Parse container type IDs
        List<Long> containerTypeIds;
        try {
            containerTypeIds = Arrays.stream(request.getContainerTypeIds().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            log.error("Invalid container type ID format in request: {}", request.getContainerTypeIds(), e);
            return Collections.emptyList();
        }

        // Skip this filter if no valid container types are provided
        if (!containerTypeIds.isEmpty()) {
            // Create a subquery to find rates that have ALL required container types
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<FclRateDetail> subRoot = subquery.from(FclRateDetail.class);

            subquery.select(subRoot.get("fclRateId"))
                .where(
                    builder.and(
                        subRoot.get(FclRateDetail_.CONTAINER_TYPE_ID).in(containerTypeIds),
                        builder.equal(subRoot.get("fclRateId"), fclRateJoin.get(FclRate_.id))
                    )
                )
                .groupBy(subRoot.get("fclRateId"))
                .having(builder.equal(builder.count(subRoot), (long) containerTypeIds.size()));

            // Only include rates that have all required container types
            predicates.add(fclRateJoin.get(FclRate_.id).in(subquery));
        } else {
            throw new ApiException("No valid container type IDs provided in request");
        }

        // Apply all predicates
        query.where(builder.and(predicates.toArray(new Predicate[0])));

        // Add ordering if needed (optional)
        query.orderBy(builder.asc(root.get("name")));

        // Execute the query with timeout to prevent long-running queries
        TypedQuery<Provider> typedQuery = entityManager.createQuery(query);
        typedQuery.setHint("javax.persistence.query.timeout", 10000); // 10 seconds timeout

        try {
            return typedQuery.getResultList();
        } catch (Exception e) {
            log.error("Error querying providers for quote request: {}", request, e);
            return Collections.emptyList();
        }
    }
}
