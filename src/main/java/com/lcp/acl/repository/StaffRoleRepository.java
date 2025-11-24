package com.lcp.acl.repository;

import com.lcp.acl.entity.StaffRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StaffRoleRepository extends JpaRepository<StaffRoleEntity, Integer> {
    List<StaffRoleEntity> findAllByRoleId(UUID id);

    void deleteAllByRoleId(UUID id);

    void deleteAllByRoleIdAndStaffIdIn(UUID roleId, List<Long> staffIds);
}
