package com.lcp.setting.service;

import com.lcp.common.PageResponse;
import com.lcp.setting.dto.ImportUnlocoDto;
import com.lcp.setting.dto.UnlocoListRequest;
import com.lcp.setting.dto.UnlocoRequestDto;
import com.lcp.setting.dto.UnlocoResponseDto;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UnlocoService {
    PageResponse<UnlocoResponseDto> list(UnlocoListRequest request);

    UnlocoResponseDto detail(Long id);

    UnlocoResponseDto create(UnlocoRequestDto request);

    UnlocoResponseDto update(Long id, UnlocoRequestDto request);

    void delete(Long id);

    ResponseEntity<byte[]> export();

    List<ImportUnlocoDto> validate(MultipartFile file);

    void importUnlocos(List<ImportUnlocoDto> request);
}
