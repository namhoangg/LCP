package com.lcp.configuration;


import com.lcp.util.PersistentUtil;
import com.lcp.util.QueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
public class CommonStaticContextInitializer {
    @Autowired
    EntityManager entityManager;

    @PostConstruct
    public void init() {
        QueryUtil.setEntityManager(entityManager);
        PersistentUtil.setEntityManager(entityManager);
    }
}
