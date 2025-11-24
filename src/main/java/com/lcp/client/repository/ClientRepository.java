package com.lcp.client.repository;

import com.lcp.client.entity.Client;
import com.lcp.client.repository.custom.ClientRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long>, ClientRepositoryCustom {
    Client findFirstByOrderByIdDesc();

    Client findByStaffId(Long staffId);

    boolean existsByEmail(String email);
}
