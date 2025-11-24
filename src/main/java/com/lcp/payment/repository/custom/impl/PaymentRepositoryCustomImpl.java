package com.lcp.payment.repository.custom.impl;

import com.lcp.common.EntityBase_;
import com.lcp.payment.dto.PaymentListRequest;
import com.lcp.payment.entity.Payment;
import com.lcp.payment.entity.Payment_;
import com.lcp.payment.repository.custom.PaymentRepositoryCustom;
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
public class PaymentRepositoryCustomImpl implements PaymentRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    @Override
    public Page<Payment> list(PaymentListRequest request) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Payment> query = builder.createQuery(Payment.class);
        Root<Payment> root = query.from(Payment.class);
        query.select(root).distinct(true);
        List<Predicate> predicates = new ArrayList<>();

        if (request.getShipmentId() != null) {
            predicates.add(builder.equal(root.get(Payment_.SHIPMENT_ID), request.getShipmentId()));
        }

        query.where(predicates.toArray(new Predicate[]{}));
        query.orderBy(PageUtil.generateOrder(request, builder, root, EntityBase_.UPDATED_AT));

        TypedQuery<Payment> typedQuery = entityManager.createQuery(query);
        var total = QueryUtil.count(builder, query, root);
        Pageable pageable = request.getPageable();
        List<Payment> list = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(list, pageable, total);
    }
}
