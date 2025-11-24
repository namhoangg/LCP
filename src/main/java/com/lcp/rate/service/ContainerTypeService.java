package com.lcp.rate.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.lcp.common.enumeration.GeneralStatus;
import com.lcp.rate.dto.ContainerTypeCreate;
import com.lcp.rate.dto.ContainerTypeResponse;
import com.lcp.rate.dto.ContainerTypeUpdate;
import com.lcp.rate.entity.ContainerType;
import com.lcp.rate.mapper.ContainerTypeMapper;
import com.lcp.rate.repository.ContainerTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContainerTypeService {
  private final ContainerTypeRepository containerTypeRepository;

  public void create(ContainerTypeCreate containerTypeCreate) {
    ContainerType containerType = ContainerTypeMapper.createEntity(containerTypeCreate);
    containerTypeRepository.save(containerType);
  }

  public void update(ContainerTypeUpdate containerTypeUpdate) {
    Optional<ContainerType> containerType = containerTypeRepository.findById(containerTypeUpdate.getId());
    if (containerType.isPresent()) {
      containerType.get().setName(containerTypeUpdate.getName());
      containerType.get().setTareWeight(containerTypeUpdate.getTareWeight());
      containerType.get().setMaxWeight(containerTypeUpdate.getMaxWeight());
      containerType.get().setMaxVolume(containerTypeUpdate.getMaxVolume());
      containerType.get().setHeight(containerTypeUpdate.getHeight());
      containerType.get().setWidth(containerTypeUpdate.getWidth());
      containerType.get().setLength(containerTypeUpdate.getLength());
      containerType.get().setIsRefrigerated(containerTypeUpdate.getIsRefrigerated());
      containerTypeRepository.save(containerType.get());
    } else {
      throw new RuntimeException("ContainerType not found");
    }
  }

  public void delete(Long id) {
    Optional<ContainerType> containerType = containerTypeRepository.findById(id);
    if (containerType.isPresent()) {
      containerType.get().setStatus(GeneralStatus.DELETED);
      containerTypeRepository.save(containerType.get());
    } else {
      throw new RuntimeException("ContainerType not found");
    }
  }

  public List<ContainerTypeResponse> list() {
    return containerTypeRepository.findAll().stream()
        .filter(containerType -> containerType.getStatus() == GeneralStatus.ACTIVE)
        .map(ContainerTypeMapper::createResponse)
        .collect(Collectors.toList());
  }

  public List<ContainerTypeResponse> listByIds(List<Long> ids) {
    return containerTypeRepository.findByIdIn(ids).stream()
        .filter(containerType -> containerType.getStatus() == GeneralStatus.ACTIVE)
        .map(ContainerTypeMapper::createResponse)
        .collect(Collectors.toList());
  }
}
