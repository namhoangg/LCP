package com.lcp.rate.service;

import com.lcp.rate.dto.GoodKindCreate;
import com.lcp.rate.dto.GoodKindResponse;
import com.lcp.rate.dto.GoodKindUpdate;

import java.util.List;

public interface GoodKindService {
    void create(GoodKindCreate goodKindCreate);

    void update(GoodKindUpdate goodKindUpdate);

    void delete(Long id);

    List<GoodKindResponse> list();
}
