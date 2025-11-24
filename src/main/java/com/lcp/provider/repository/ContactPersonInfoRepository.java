package com.lcp.provider.repository;

import com.lcp.provider.entity.ContactPersonInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactPersonInfoRepository extends JpaRepository<ContactPersonInfo, Long> {
}
