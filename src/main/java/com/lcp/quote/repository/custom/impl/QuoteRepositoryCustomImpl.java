package com.lcp.quote.repository.custom.impl;

import com.lcp.client.entity.Client;
import com.lcp.client.entity.Client_;
import com.lcp.common.EntityBase_;
import com.lcp.common.enumeration.QuoteStatus;
import com.lcp.provider.entity.Provider;
import com.lcp.quote.dto.QuoteListRequest;
import com.lcp.quote.entity.Quote;
import com.lcp.quote.entity.Quote_;
import com.lcp.quote.repository.custom.QuoteRepositoryCustom;
import com.lcp.security.UserDetailsCustom;
import com.lcp.setting.entity.Unloco;
import com.lcp.util.PageUtil;
import com.lcp.util.QueryUtil;
import com.lcp.util.SearchUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuoteRepositoryCustomImpl implements QuoteRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    @Override
    public Page<Quote> listQuoteRequest(QuoteListRequest request) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Quote> query = builder.createQuery(Quote.class);
        Root<Quote> root = query.from(Quote.class);
        query.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();

        var clientJoin = (Join<Quote, Client>) root.fetch(Quote_.client, JoinType.LEFT);
        var providerJoin = (Join<Quote, Provider>) root.fetch(Quote_.provider, JoinType.LEFT);
        var originJoin = (Join<Quote, Unloco>) root.fetch(Quote_.origin, JoinType.LEFT);
        var destinationJoin = (Join<Quote, Unloco>) root.fetch(Quote_.destination, JoinType.LEFT);


        if (StringUtils.isNotEmpty(request.getKeyword())) {
            String keywordLike = SearchUtil.format(request.getKeyword());
            predicates.add(builder.or(
                    builder.like(builder.lower(root.get(Quote_.CODE)), keywordLike)
            ));
        }

        if (request.getSearchConditions() != null) {
            for (var searchCondition : request.getSearchConditions()) {
                From join = getJoin(searchCondition.getFieldName(), root, clientJoin, providerJoin, originJoin, destinationJoin);
                String searchName = getSearchName(searchCondition.getFieldName());
                predicates.add(SearchUtil.getSearchPredicate(builder, join.get(searchName), searchCondition));
            }
        }

        if (request.getIsRequest() != null) {
            predicates.add(builder.equal(root.get("isRequest"), request.getIsRequest()));
        }

        if (request.getIsRequest() != null && request.getIsRequest()) {
            predicates.add(root.get(Quote_.QUOTE_STATUS).in(QuoteStatus.DRAFT, QuoteStatus.REQUESTED));
        }

        if (request.getIsRequest() == null || !request.getIsRequest()) {
            predicates.add(builder.not(root.get(Quote_.QUOTE_STATUS).in(QuoteStatus.DRAFT, QuoteStatus.REQUESTED)));
        }

        var currentUser = UserDetailsCustom.getCurrentUser();
        if (currentUser != null && currentUser.getIsClient() && currentUser.getClientId() != null) {
            predicates.add(builder.equal(clientJoin.get(Client_.ID), currentUser.getClientId()));
            predicates.add(builder.not(builder.equal(root.get(Quote_.QUOTE_STATUS), QuoteStatus.STAFF_DRAFT)));
        } else {
            // if staff, not get the client draft
            predicates.add(builder.not(builder.equal(root.get(Quote_.QUOTE_STATUS), QuoteStatus.DRAFT)));
        }

        query.where(predicates.toArray(new Predicate[]{}));

        From sortJoin = getJoin(request.getSortField(), root, clientJoin, providerJoin, originJoin, destinationJoin);
        query.orderBy(PageUtil.generateSortOrder(request, builder, sortJoin, getSortName(request.getSortField())));

        TypedQuery<Quote> typedQuery = entityManager.createQuery(query);
        var total = QueryUtil.count(builder, query, root);
        Pageable pageable = request.getPageable();
        List<Quote> list = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(list, pageable, total);
    }

    private String getSortName(String fieldName) {
        if (StringUtils.isEmpty(fieldName))
            return EntityBase_.UPDATED_AT;
        return getSearchName(fieldName);
    }

    private String getSearchName(String fieldName) {
        switch (fieldName) {
            case Quote_.ORIGIN:
            case Quote_.DESTINATION:
                return "code";
            case Quote_.CLIENT:
            case Quote_.PROVIDER:
                return "name";
            default:
                return fieldName;
        }
    }

    private From getJoin(String fieldName, From root, From clientJoin, From providerJoin, From originJoin, From destinationJoin) {
        switch (fieldName) {
            case Quote_.CLIENT:
                return clientJoin;
            case Quote_.PROVIDER:
                return providerJoin;
            case Quote_.ORIGIN:
                return originJoin;
            case Quote_.DESTINATION:
                return destinationJoin;
            default:
                return root;
        }
    }
}
