package com.lcp.rate.repository.custom;

import com.google.common.base.Strings;
import com.lcp.common.dto.BaseListRequest;
import com.lcp.provider.entity.Provider;
import com.lcp.provider.entity.Provider_;
import com.lcp.rate.entity.LclRate;
import com.lcp.rate.entity.LclRate_;
import com.lcp.setting.entity.Unloco;
import com.lcp.setting.entity.Unloco_;
import com.lcp.util.PageUtil;
import com.lcp.util.QueryUtil;
import com.lcp.util.SearchUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class LclRateRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    public Page<LclRate> list(BaseListRequest request) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<LclRate> query = builder.createQuery(LclRate.class);
        Root<LclRate> root = query.from(LclRate.class);
        query.select(root).distinct(true);
        List<Predicate> predicates = new ArrayList<>();

        Join<LclRate, Provider> providerJoin = root.join(LclRate_.PROVIDER, JoinType.LEFT);
        Join<LclRate, Unloco> originJoin = root.join(LclRate_.ORIGIN, JoinType.LEFT);
        Join<LclRate, Unloco> destinationJoin = root.join(LclRate_.DESTINATION, JoinType.LEFT);

        if (!Strings.isNullOrEmpty(request.getKeyword())) {
            String keywordLike = SearchUtil.format(request.getKeyword());
            predicates.add(builder.or(
                    builder.like(builder.lower(providerJoin.get(Provider_.NAME)), keywordLike),
                    builder.like(builder.lower(originJoin.get(Unloco_.CODE)), keywordLike),
                    builder.like(builder.lower(destinationJoin.get(Unloco_.CODE)), keywordLike)
            ));
        }
        query.where(predicates.toArray(new Predicate[]{}));
        query.orderBy(PageUtil.generateOrder(request, builder, root, LclRate_.CREATED_AT));
        TypedQuery<LclRate> typedQuery = entityManager.createQuery(query);
        var total = QueryUtil.count(builder, query, root);
        Pageable pageable = request.getPageable();
        List<LclRate> list = typedQuery.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        return new PageImpl<>(list, pageable, total);
    }
}
