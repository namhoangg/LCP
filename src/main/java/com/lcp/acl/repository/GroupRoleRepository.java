package com.lcp.acl.repository;

import com.lcp.acl.entity.GroupRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GroupRoleRepository extends JpaRepository<GroupRoleEntity, Integer> {
    void deleteAllByRoleId(UUID id);

    List<GroupRoleEntity> findAllByRoleId(UUID id);

    void deleteAllByRoleIdAndGroupIdIn(UUID roleId, List<UUID> groupIds);
}
