package com.lcp.acl.repository;

import com.lcp.acl.entity.PermissionRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissionRoleRepository extends JpaRepository<PermissionRole, Integer> {
    void deleteAllByPermissionId(UUID permissionId);
}
