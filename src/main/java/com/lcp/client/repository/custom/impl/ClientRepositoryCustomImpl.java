package com.lcp.client.repository.custom.impl;

import com.google.common.base.Strings;
import com.lcp.client.dto.ClientListRequest;
import com.lcp.client.entity.Client;
import com.lcp.client.entity.Client_;
import com.lcp.client.repository.custom.ClientRepositoryCustom;
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
public class ClientRepositoryCustomImpl implements ClientRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    @Override
    public Page<Client> list(ClientListRequest request) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Client> query = builder.createQuery(Client.class);
        Root<Client> root = query.from(Client.class);
        query.select(root).distinct(true);
        List<Predicate> predicates = new ArrayList<>();

        if (!Strings.isNullOrEmpty(request.getKeyword())) {
            String keywordLike = SearchUtil.format(request.getKeyword());
            predicates.add(builder.or(
                    builder.like(builder.lower(root.get(Client_.NAME)), keywordLike)
            ));
        }

        query.where(predicates.toArray(new Predicate[]{}));
        query.orderBy(PageUtil.generateOrder(request, builder, root, Client_.NAME));
        TypedQuery<Client> typedQuery = entityManager.createQuery(query);
        var total = QueryUtil.count(builder, query, root);

        List<Client> list;
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
}