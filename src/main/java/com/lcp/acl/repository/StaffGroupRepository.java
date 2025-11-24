package com.lcp.acl.repository;

import com.lcp.acl.entity.StaffGroupEntity;
import io.micrometer.core.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StaffGroupRepository extends JpaRepository<StaffGroupEntity, UUID> {
    boolean existsById(@NonNull UUID id);

    boolean existsAllByIdIn(List<UUID> ids);
}
