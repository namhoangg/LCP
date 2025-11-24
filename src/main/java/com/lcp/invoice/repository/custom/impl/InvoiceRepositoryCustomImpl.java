package com.lcp.invoice.repository.custom.impl;

import com.lcp.invoice.dto.InvoiceListRequest;
import com.lcp.invoice.entity.Invoice;
import com.lcp.invoice.entity.Invoice_;
import com.lcp.invoice.repository.custom.InvoiceRepositoryCustom;
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
public class InvoiceRepositoryCustomImpl implements InvoiceRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    @Override
    public Page<Invoice> list(InvoiceListRequest request) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Invoice> query = builder.createQuery(Invoice.class);
        Root<Invoice> root = query.from(Invoice.class);
        query.select(root).distinct(true);
        List<Predicate> predicates = new ArrayList<>();

        if (request.getShipmentId() != null) {
            predicates.add(builder.equal(root.get(Invoice_.SHIPMENT_ID), request.getShipmentId()));
        }

        query.where(predicates.toArray(new Predicate[]{}));
        query.orderBy(PageUtil.generateOrder(request, builder, root, Invoice_.UPDATED_AT));

        TypedQuery<Invoice> typedQuery = entityManager.createQuery(query);
        var total = QueryUtil.count(builder, query, root);
        Pageable pageable = request.getPageable();
        List<Invoice> list = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(list, pageable, total);
    }
}
