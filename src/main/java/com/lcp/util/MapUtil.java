package com.lcp.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;


@Slf4j
public class MapUtil {

    public static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }

    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }

    public static <D, E> E mapProperties(D source, Class<E> target) {
        try {
            E e = target.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, e);
            return e;
        } catch (Exception e) {
            throw new RuntimeException("Map properties failed", e);
        }
    }
}
