package com.lcp.acl.repository;

import com.lcp.acl.entity.PermissionResourceAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PermissionResourceActionRepository extends JpaRepository<PermissionResourceAction, UUID> {
    void deleteAllByPermissionId(UUID permissionId);

    List<PermissionResourceAction> findAllByPermissionIdIn(List<UUID> permissionIds);
}
