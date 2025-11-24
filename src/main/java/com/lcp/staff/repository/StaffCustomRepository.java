package com.lcp.staff.repository;

import com.lcp.common.dto.BaseListRequest;
import com.lcp.staff.entity.Staff;
import com.lcp.staff.entity.Staff_;
import com.lcp.util.PageUtil;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Component
public class StaffCustomRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    public Page<Staff> findAll(BaseListRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Staff> cq = cb.createQuery(Staff.class);
        Root<Staff> root = cq.from(Staff.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get(Staff_.isClient), false));
        predicates.add(cb.equal(root.get(Staff_.isSuperAdmin), false));
        // Code must be not null
        predicates.add(cb.isNotNull(root.get(Staff_.code)));

        String keyword = request.getKeyword();
        if (keyword != null && !keyword.isBlank()) {
            String keywordLike = "%" + keyword.toLowerCase() + "%";
            // Concatenate first and last name for keyword search
            Expression<String> fullName = cb.concat(cb.lower(root.get(Staff_.lastname)), cb.lower(root.get(Staff_.firstname)));
            predicates.add(cb.like(fullName, keywordLike));
        }

        cq.where(predicates.toArray(new Predicate[0]));

        // Replace with actual column from Staff_
        cq.orderBy(PageUtil.generateOrder(request, cb, root, Staff_.CREATED_AT));

        TypedQuery<Staff> query = entityManager.createQuery(cq);
        Pageable pageable = request.getPageable();
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        // Count query
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Staff> countRoot = countQuery.from(Staff.class);
        countQuery.select(cb.count(countRoot));

        if (!predicates.isEmpty()) {
            countQuery.where(predicates.toArray(new Predicate[0]));
        }

        Long total = entityManager.createQuery(countQuery).getSingleResult();
        List<Staff> results = query.getResultList();

        return new PageImpl<>(results, pageable, total);
    }
}
