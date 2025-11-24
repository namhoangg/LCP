package com.lcp.shipment.repository.custom.impl;

import com.lcp.quote.entity.Quote;
import com.lcp.quote.entity.Quote_;
import com.lcp.security.UserDetailsCustom;
import com.lcp.shipment.dto.ShipmentListRequest;
import com.lcp.shipment.entity.Shipment;
import com.lcp.shipment.entity.ShipmentStatus;
import com.lcp.shipment.entity.Shipment_;
import com.lcp.shipment.repository.custom.ShipmentRepositoryCustom;
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
public class ShipmentRepositoryCustomImpl implements ShipmentRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    @Override
    public Page<Shipment> list(ShipmentListRequest request) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Shipment> query = builder.createQuery(Shipment.class);
        Root<Shipment> root = query.from(Shipment.class);
        query.select(root).distinct(true);

        var quoteJoin = (Join<Shipment, Quote>) root.fetch(Shipment_.quote, JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        var shipmentStatusJoin = (Join<Shipment, ShipmentStatus>) root.fetch(Shipment_.shipmentStatus, JoinType.LEFT);

        if (StringUtils.isNotEmpty(request.getKeyword())) {
            String keywordLike = SearchUtil.format(request.getKeyword());
            predicates.add(builder.or(
                    builder.like(builder.lower(root.get(Shipment_.CODE)), keywordLike)
            ));
        }

        if (request.getSearchConditions() != null) {
            for (var searchCondition : request.getSearchConditions()) {
                From join = getJoin(searchCondition.getFieldName(), root, shipmentStatusJoin);
                String searchName = getSearchName(searchCondition.getFieldName());
                predicates.add(SearchUtil.getSearchPredicate(builder, join.get(searchName), searchCondition));
            }
        }

        var currentUser = UserDetailsCustom.getCurrentUser();
        if (currentUser != null && currentUser.getIsClient() && currentUser.getClientId() != null) {
            predicates.add(builder.equal(quoteJoin.get(Quote_.CLIENT_ID), currentUser.getClientId()));
        }
        query.where(predicates.toArray(new Predicate[]{}));
        query.orderBy(PageUtil.generateOrder(request, builder, root, Shipment_.UPDATED_AT));

        TypedQuery<Shipment> typedQuery = entityManager.createQuery(query);
        var total = QueryUtil.count(builder, query, root);
        Pageable pageable = request.getPageable();
        List<Shipment> list = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(list, pageable, total);
    }

    private String getSearchName(String fieldName) {
        switch (fieldName) {
            case Shipment_.SHIPMENT_STATUS:
                return "status";
            default:
                return fieldName;
        }
    }

    private From getJoin(String fieldName, From root, From shipmentStatusJoin) {
        switch (fieldName) {
            case "shipmentStatus":
                return shipmentStatusJoin;
            default:
                return root;
        }
    }
}
