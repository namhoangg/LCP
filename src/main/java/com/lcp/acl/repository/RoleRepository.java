package com.lcp.acl.repository;

import com.lcp.acl.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<RoleEntity, UUID> {
    boolean existsAllByIdIn(Collection<UUID> id);

    @Query("SELECT DISTINCT r FROM RoleEntity r " +
            "LEFT JOIN StaffRoleEntity sr ON r.id = sr.roleId " +
            "LEFT JOIN GroupRoleEntity sgr ON r.id = sgr.roleId " +
            "WHERE (sr.staffId = :userId) " +
            "OR (sgr.groupId IN :groupIds)")
    List<RoleEntity> findAllRoleOfUser(Long userId, List<UUID> groupIds);
}
