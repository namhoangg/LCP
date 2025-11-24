package com.lcp.rate.repository.custom.impl;

import com.lcp.rate.dto.ServiceChargeListRequest;
import com.lcp.rate.entity.*;
import com.lcp.rate.repository.custom.ServiceChargeRepositoryCustom;
import com.lcp.util.PageUtil;
import com.lcp.util.QueryUtil;
import com.lcp.util.SearchUtil;
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
public class ServiceChargeRepositoryCustomImpl implements ServiceChargeRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    @Override
    public Page<ServiceCharge> list(ServiceChargeListRequest request) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ServiceCharge> query = builder.createQuery(ServiceCharge.class);
        Root<ServiceCharge> root = query.from(ServiceCharge.class);
        query.select(root).distinct(true);

        List<Predicate> predicates = new ArrayList<>();

        Join<ServiceCharge, ChargeType> chargeTypeJoin = root.join(ServiceCharge_.CHARGE_TYPE, JoinType.LEFT);

        if (request.getKeyword() != null) {
            String keywordLike = SearchUtil.format(request.getKeyword());
            predicates.add(builder.or(
                    builder.like(builder.lower(chargeTypeJoin.get(ChargeType_.NAME)), keywordLike)
            ));
        }
        query.where(predicates.toArray(new Predicate[]{}));
        query.orderBy(PageUtil.generateOrder(request, builder, root, ServiceCharge_.CREATED_AT));
        TypedQuery<ServiceCharge> typedQuery = entityManager.createQuery(query);
        var total = QueryUtil.count(builder, query, root);
        Pageable pageable = request.getPageable();
        List<ServiceCharge> list = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(list, pageable, total);
    }
}
