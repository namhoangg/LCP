package com.lcp.util;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeZoneUtil {
    private static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss z");

    public static String formatToVietnamTime(OffsetDateTime utcTime) {
        if (utcTime == null) return null;
        ZonedDateTime vnTime = utcTime.atZoneSameInstant(VIETNAM_ZONE);
        return vnTime.format(FORMATTER);
    }

    public static OffsetDateTime convertToVietnamTime(OffsetDateTime utcTime) {
        if (utcTime == null) return null;
        return utcTime.atZoneSameInstant(VIETNAM_ZONE).toOffsetDateTime();
    }
}
