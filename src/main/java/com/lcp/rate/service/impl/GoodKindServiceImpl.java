package com.lcp.rate.service.impl;

import com.lcp.common.enumeration.GeneralStatus;
import com.lcp.rate.dto.GoodKindCreate;
import com.lcp.rate.dto.GoodKindResponse;
import com.lcp.rate.dto.GoodKindUpdate;
import com.lcp.rate.entity.GoodKind;
import com.lcp.rate.mapper.GoodKindMapper;
import com.lcp.rate.repository.GoodKindRepository;
import com.lcp.rate.service.GoodKindService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoodKindServiceImpl implements GoodKindService {

    private final GoodKindRepository goodKindRepository;

    @Override
    public void create(GoodKindCreate goodKindCreate) {
        GoodKind goodKind = new GoodKind();
        goodKind.setName(goodKindCreate.getName());
        goodKind.setDescription(goodKindCreate.getDescription());
        goodKind.setIsRefrigerated(goodKindCreate.getIsRefrigerated());
        goodKind.setIsDefault(false);
        goodKindRepository.save(goodKind);
    }

    @Override
    public void update(GoodKindUpdate goodKindUpdate) {
        GoodKind goodKind = goodKindRepository.findById(goodKindUpdate.getId())
                .orElseThrow(() -> new RuntimeException("GoodKind not found"));

        if (goodKind.getIsDefault()) {
            throw new RuntimeException("Default good kind cannot be updated");
        }

        goodKind.setName(goodKindUpdate.getName());
        goodKind.setDescription(goodKindUpdate.getDescription());
        goodKind.setIsRefrigerated(goodKindUpdate.getIsRefrigerated());
        goodKindRepository.save(goodKind);
    }

    @Override
    public void delete(Long id) {
        GoodKind goodKind = goodKindRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GoodKind not found"));

        if (goodKind.getIsDefault()) {  
            throw new RuntimeException("Default good kind cannot be deleted");
        }

        goodKind.setStatus(GeneralStatus.DELETED);
        goodKindRepository.save(goodKind);
    }

    @Override
    public List<GoodKindResponse> list() {
        List<GoodKind> goodKinds = goodKindRepository.findAllByStatus(GeneralStatus.ACTIVE);
        return goodKinds.stream()
                .map(GoodKindMapper::createResponse)
                .collect(Collectors.toList());
    }
}
