package com.lcp.common;

import java.util.Date;
import java.util.Set;

public interface ICacheService {
    Long add(String key, Object... values);

    Long add(String key, Date expireTime, Object... values);

    Long clearAndAdd(String key, Object... values);

    Boolean exist(String key, Object value);

    Set<Object> retrieveAll(String key);

    Boolean remove(String key);

    Long remove(String key, Object... values);

    Set<String> retrieveAllKeys(String pattern);
}
