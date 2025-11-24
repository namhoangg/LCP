package com.lcp.acl.repository;

import com.lcp.acl.dto.ListRoleRequestDto;
import com.lcp.acl.entity.RoleEntity;
import com.lcp.acl.entity.RoleEntity_;
import com.lcp.util.PageUtil;
import com.lcp.util.QueryUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Component
public class RoleRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    public Page<RoleEntity> findAll(ListRoleRequestDto request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RoleEntity> cq = cb.createQuery(RoleEntity.class);
        Root<RoleEntity> root = cq.from(RoleEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        String keyword = request.getKeyword();
        if (keyword != null && !keyword.isBlank()) {
            String keywordLike = "%" + keyword.toLowerCase() + "%";
            predicates.add(cb.like(cb.lower(root.get(RoleEntity_.NAME)), keywordLike));
        }

        if (request.getType() != null) {
            predicates.add(cb.equal(root.get(RoleEntity_.TYPE), request.getType()));
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        cq.orderBy(PageUtil.generateOrder(request, cb, root, RoleEntity_.CREATED_AT));

        TypedQuery<RoleEntity> query = entityManager.createQuery(cq);
        Pageable pageable = request.getPageable();

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        long total = QueryUtil.count(cb, cq, root);
        List<RoleEntity> results = query.getResultList();

        return new PageImpl<>(results, pageable, total);
    }

}
