package com.lcp.markup.repository.custom.impl;

import com.lcp.common.EntityBase_;
import com.lcp.markup.dto.PriceMarkupListRequest;
import com.lcp.markup.entity.PriceMarkup;
import com.lcp.markup.repository.custom.PriceMarkupRepositoryCustom;
import com.lcp.util.PageUtil;
import com.lcp.util.QueryUtil;
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
public class PriceMarkupRepositoryCustomImpl implements PriceMarkupRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    @Override
    public Page<PriceMarkup> list(PriceMarkupListRequest request) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PriceMarkup> query = builder.createQuery(PriceMarkup.class);
        Root<PriceMarkup> root = query.from(PriceMarkup.class);
        query.select(root).distinct(true);
        List<Predicate> predicates = new ArrayList<>();

        if (request.getStatus() != null) {
            predicates.add(builder.equal(root.get(EntityBase_.STATUS), request.getStatus()));
        }

        query.where(predicates.toArray(new Predicate[]{}));
        query.orderBy(PageUtil.generateOrder(request, builder, root, EntityBase_.UPDATED_AT));

        TypedQuery<PriceMarkup> typedQuery = entityManager.createQuery(query);
        var total = QueryUtil.count(builder, query, root);
        Pageable pageable = request.getPageable();
        List<PriceMarkup> list;
        if (request.getPagingIgnore()) {
            list = typedQuery.getResultList();
            return new PageImpl<>(list, pageable, list.size());
        } else {
            list = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
            return new PageImpl<>(list, pageable, total);
        }
    }
}
