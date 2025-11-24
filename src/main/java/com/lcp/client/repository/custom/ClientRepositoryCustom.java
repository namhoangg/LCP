package com.lcp.client.repository.custom;

import com.lcp.client.dto.ClientListRequest;
import com.lcp.client.entity.Client;
import org.springframework.data.domain.Page;

public interface ClientRepositoryCustom {
    Page<Client> list(ClientListRequest request);
}
