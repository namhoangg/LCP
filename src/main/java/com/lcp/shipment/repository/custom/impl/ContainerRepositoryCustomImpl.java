package com.lcp.shipment.repository.custom.impl;

import com.lcp.common.EntityBase_;
import com.lcp.invoice.entity.DebitNote_;
import com.lcp.shipment.dto.ContainerListRequest;
import com.lcp.shipment.entity.Container;
import com.lcp.shipment.entity.Container_;
import com.lcp.shipment.repository.custom.ContainerRepositoryCustom;
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
public class ContainerRepositoryCustomImpl implements ContainerRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    @Override
    public Page<Container> list(ContainerListRequest request) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Container> query = builder.createQuery(Container.class);
        Root<Container> root = query.from(Container.class);
        query.select(root).distinct(true);
        List<Predicate> predicates = new ArrayList<>();

        if (request.getShipmentId() != null) {
            predicates.add(builder.equal(root.get(Container_.SHIPMENT_ID), request.getShipmentId()));
        }

        query.where(predicates.toArray(new Predicate[]{}));
        query.orderBy(PageUtil.generateOrder(request, builder, root, EntityBase_.UPDATED_AT));

        TypedQuery<Container> typedQuery = entityManager.createQuery(query);
        var total = QueryUtil.count(builder, query, root);
        Pageable pageable = request.getPageable();
        List<Container> list = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(list, pageable, total);
    }
}
