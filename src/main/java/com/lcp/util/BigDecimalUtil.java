package com.lcp.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class BigDecimalUtil {
    public static BigDecimal convertFrom(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return new BigDecimal(str);
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        if (a == null)
            return b;
        if (b == null)
            return a;
        return a.add(b);
    }

    public static BigDecimal add(BigDecimal... values) {
        if (values == null || values.length == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal value : values) {
            if (value != null) {
                sum = sum.add(value);
            }
        }
        return sum;
    }

    public static int compare(BigDecimal one, BigDecimal other) {
        if (one == null && other == null)
            return 0;
        if (one == null)
            return -1;
        else if (other == null)
            return 1;
        return one.compareTo(other);
    }

    public static BigDecimal subtract(BigDecimal b1, BigDecimal b2) {
        if (b1 == null && b2 == null) {
            return null;
        }
        if (b1 == null) {
            b1 = new BigDecimal(0);
        }
        if (b2 == null) {
            b2 = new BigDecimal(0);
        }
        return b1.subtract(b2);
    }

    public static BigDecimal multiply(BigDecimal b1, BigDecimal b2) {
        if (b1 == null || b2 == null) {
            return null;
        }
        return b1.multiply(b2);
    }
}
