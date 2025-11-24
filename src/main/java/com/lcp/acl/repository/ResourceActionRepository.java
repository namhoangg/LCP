package com.lcp.acl.repository;

import com.lcp.acl.entity.ResourceAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ResourceActionRepository extends JpaRepository<ResourceAction, Integer> {
    boolean existsAllByIdIn(Collection<Integer> id);
}
