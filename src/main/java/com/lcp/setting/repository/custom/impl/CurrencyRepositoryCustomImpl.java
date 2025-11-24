package com.lcp.setting.repository.custom.impl;

import com.google.common.base.Strings;
import com.lcp.setting.dto.CurrencyListRequest;
import com.lcp.setting.entity.Currency;
import com.lcp.setting.entity.Currency_;
import com.lcp.setting.repository.custom.CurrencyRepositoryCustom;
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
public class CurrencyRepositoryCustomImpl implements CurrencyRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    @Override
    public Page<Currency> list(CurrencyListRequest request) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Currency> query = builder.createQuery(Currency.class);
        Root<Currency> root = query.from(Currency.class);
        query.select(root).distinct(true);
        List<Predicate> predicates = new ArrayList<>();
        if (!Strings.isNullOrEmpty(request.getKeyword())) {
            String keywordLike = SearchUtil.format(request.getKeyword());
            predicates.add(builder.or(
                    builder.like(builder.lower(root.get(Currency_.CODE)), keywordLike),
                    builder.like(builder.lower(root.get(Currency_.NAME)), keywordLike)
            ));
        }
        query.where(predicates.toArray(new Predicate[]{}));
        query.orderBy(PageUtil.generateOrder(request, builder, root, Currency_.NAME));
        TypedQuery<Currency> typedQuery = entityManager.createQuery(query);
        var total = QueryUtil.count(builder, query, root);
        Pageable pageable = request.getPageable();
        // Get full list of currencies
        List<Currency> list = typedQuery.setFirstResult((int) pageable.getOffset()).getResultList();
        return new PageImpl<>(list, pageable, total);
    }
}
