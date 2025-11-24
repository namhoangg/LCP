package com.lcp.acl.repository;

import com.lcp.acl.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    boolean existsByName(String name);

    boolean existsByIdAndName(UUID id, String name);

    boolean existsByNameAndIdNot(String name, UUID id);

    @Query("SELECT DISTINCT p FROM Permission p " +
            "LEFT JOIN PermissionRole rp ON p.id = rp.permissionId " +
            "WHERE rp.roleId IN :roleIds")
    List<Permission> findAllByRoleIdIn(List<UUID> roleIds);
}
