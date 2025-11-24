package com.lcp.rate.repository.custom.impl;

import com.lcp.rate.dto.ChargeTypeListRequest;
import com.lcp.rate.entity.ChargeType;
import com.lcp.rate.entity.ChargeType_;
import com.lcp.rate.repository.custom.ChargeTypeRepositoryCustom;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Component
public class ChargeTypeRepositoryCustomImpl implements ChargeTypeRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    @Override
    public Page<ChargeType> list(ChargeTypeListRequest request) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ChargeType> query = builder.createQuery(ChargeType.class);
        Root<ChargeType> root = query.from(ChargeType.class);
        query.select(root).distinct(true);
        List<Predicate> predicates = new ArrayList<>();

        if (request.getKeyword() != null) {
             String keywordLike = SearchUtil.format(request.getKeyword());
             predicates.add(builder.or(
                     builder.like(builder.lower(root.get(ChargeType_.NAME)), keywordLike)
             ));
        }
        query.where(predicates.toArray(new Predicate[]{}));
        query.orderBy(PageUtil.generateOrder(request, builder, root, ChargeType_.NAME));
        TypedQuery<ChargeType> typedQuery = entityManager.createQuery(query);
        var total = QueryUtil.count(builder, query, root);
        Pageable pageable = request.getPageable();
        List<ChargeType> list = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(list, pageable, total);
    }
}
