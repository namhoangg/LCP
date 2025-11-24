package com.lcp.rate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lcp.rate.entity.ContainerType;

public interface ContainerTypeRepository extends JpaRepository<ContainerType, Long> {
  Optional<ContainerType> findByName(String name);
  List<ContainerType> findByIdIn(List<Long> ids);
}
