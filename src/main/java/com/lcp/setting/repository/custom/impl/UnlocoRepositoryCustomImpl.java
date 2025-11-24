package com.lcp.setting.repository.custom.impl;

import com.google.common.base.Strings;
import com.lcp.setting.dto.UnlocoListRequest;
import com.lcp.setting.entity.Unloco;
import com.lcp.setting.entity.Unloco_;
import com.lcp.setting.repository.custom.UnlocoRepositoryCustom;
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
public class UnlocoRepositoryCustomImpl implements UnlocoRepositoryCustom {
    @Autowired
    EntityManager entityManager;


    @Override
    public Page<Unloco> list(UnlocoListRequest request) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Unloco> query = builder.createQuery(Unloco.class);
        Root<Unloco> root = query.from(Unloco.class);
        query.select(root).distinct(true);
        List<Predicate> predicates = new ArrayList<>();
        if (!Strings.isNullOrEmpty(request.getKeyword())) {
            String keywordLike = SearchUtil.format(request.getKeyword());
            predicates.add(builder.or(
                    builder.like(builder.lower(root.get(Unloco_.CODE)), keywordLike),
                    builder.like(builder.lower(root.get(Unloco_.CITY_NAME)), keywordLike),
                    builder.like(builder.lower(root.get(Unloco_.CITY_CODE)), keywordLike),
                    builder.like(builder.lower(root.get(Unloco_.COUNTRY_CODE)), keywordLike),
                    builder.like(builder.lower(root.get(Unloco_.COUNTRY_NAME)), keywordLike)
            ));
        }
        if (request.getStatus() != null) {
            predicates.add(builder.equal(root.get(Unloco_.STATUS), request.getStatus()));
        }
        query.where(predicates.toArray(new Predicate[]{}));
        query.orderBy(PageUtil.generateOrder(request, builder, root, Unloco_.CODE));
        TypedQuery<Unloco> typedQuery = entityManager.createQuery(query);
        
        if (request.getPagingIgnore()) {
            List<Unloco> list = typedQuery.getResultList();
            return new PageImpl<>(list, request.getPageable(), list.size()); 
        } else {
            var total = QueryUtil.count(builder, query, root);
            Pageable pageable = request.getPageable();
            List<Unloco> list = typedQuery.setFirstResult((int) pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();
            return new PageImpl<>(list, pageable, total);
        }
    }
}
