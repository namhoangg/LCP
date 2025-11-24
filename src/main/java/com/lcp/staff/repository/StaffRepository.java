package com.lcp.staff.repository;


import com.lcp.staff.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsAllByIdIn(List<Long> ids);

    Long countDistinctByIsClientIsFalseAndIsSuperAdminFalse();

    List<Staff> findAllByIsClientIsFalseAndIsSuperAdminFalse();

    boolean existsByEmailAndIdNot(String email, Long id);

    Staff findByCode(String code);
}
