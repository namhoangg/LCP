package com.lcp.acl.repository;

import com.google.common.base.Strings;
import com.lcp.acl.dto.ListPermissionRequestDto;
import com.lcp.acl.entity.Permission;
import com.lcp.util.PageUtil;
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
public class PermissionCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Page<Permission> searchPermissions(ListPermissionRequestDto request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Main query for results
        CriteriaQuery<Permission> cq = cb.createQuery(Permission.class);
        Root<Permission> root = cq.from(Permission.class);

        // Filters
        List<Predicate> predicates = new ArrayList<>();

        if (!Strings.isNullOrEmpty(request.getKeyword())) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + request.getKeyword().toLowerCase() + "%"));
        }

        if (!predicates.isEmpty()) {
            cq.where(predicates.toArray(new Predicate[0]));
        }

        // Sorting (customize this if needed)
        cq.orderBy(PageUtil.generateOrder(request, cb, root, "name"));

        // Paging
        TypedQuery<Permission> query = entityManager.createQuery(cq);
        Pageable pageable = request.getPageable();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Permission> permissions = query.getResultList();

        // Count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Permission> countRoot = countQuery.from(Permission.class);
        countQuery.select(cb.countDistinct(countRoot));

        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(permissions, pageable, total);
    }

}
