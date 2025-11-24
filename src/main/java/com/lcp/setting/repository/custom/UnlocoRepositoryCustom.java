package com.lcp.setting.repository.custom;

import com.lcp.setting.dto.UnlocoListRequest;
import com.lcp.setting.entity.Unloco;
import org.springframework.data.domain.Page;

public interface UnlocoRepositoryCustom {
    Page<Unloco> list(UnlocoListRequest request);
}
