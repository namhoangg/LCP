package com.lcp.rate.repository.custom.impl;

import com.google.common.base.Strings;
import com.lcp.provider.entity.Provider;
import com.lcp.provider.entity.Provider_;
import com.lcp.rate.dto.FclRateListRequest;
import com.lcp.rate.entity.FclRate;
import com.lcp.rate.entity.FclRate_;
import com.lcp.rate.repository.custom.FclRateRepositoryCustom;
import com.lcp.setting.entity.Unloco;
import com.lcp.setting.entity.Unloco_;
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
public class FclRateRepositoryCustomImpl implements FclRateRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    @Override
    public Page<FclRate> list(FclRateListRequest request) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FclRate> query = builder.createQuery(FclRate.class);
        Root<FclRate> root = query.from(FclRate.class);
        query.select(root).distinct(true);
        List<Predicate> predicates = new ArrayList<>();

        Join<FclRate, Provider> providerJoin = root.join(FclRate_.PROVIDER, JoinType.LEFT);
        Join<FclRate, Unloco> originJoin = root.join(FclRate_.ORIGIN, JoinType.LEFT);
        Join<FclRate, Unloco> destinationJoin = root.join(FclRate_.DESTINATION, JoinType.LEFT);

        if (!Strings.isNullOrEmpty(request.getKeyword())) {
            String keywordLike = SearchUtil.format(request.getKeyword());
            predicates.add(builder.or(
                    builder.like(builder.lower(providerJoin.get(Provider_.NAME)), keywordLike),
                    builder.like(builder.lower(originJoin.get(Unloco_.CODE)), keywordLike),
                    builder.like(builder.lower(destinationJoin.get(Unloco_.CODE)), keywordLike)
            ));
        }
        query.where(predicates.toArray(new Predicate[]{}));
        query.orderBy(PageUtil.generateOrder(request, builder, root, FclRate_.CREATED_AT));
        TypedQuery<FclRate> typedQuery = entityManager.createQuery(query);
        var total = QueryUtil.count(builder, query, root);
        Pageable pageable = request.getPageable();
        List<FclRate> list = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(list, pageable, total);
    }
}
