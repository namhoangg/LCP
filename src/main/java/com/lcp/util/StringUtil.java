package com.lcp.util;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringUtil {
    public static List<Long> stringToLongList(String data) {
        if (data == null || data.isBlank()) {
            return List.of();
        }

        return Stream.of(data.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    public static List<UUID> stringToUUIDList(String data) {
        if (data == null || data.isBlank()) {
            return List.of();
        }

        return Stream.of(data.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(UUID::fromString)
                .collect(Collectors.toList());
    }
}
