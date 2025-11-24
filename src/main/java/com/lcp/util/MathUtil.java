package com.lcp.util;

import com.lcp.common.enumeration.CurrencyHardCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MathUtil {
    private static final int DEFAULT_SCALE = 2;
    private static final int PRINT_USD_SCALE = 2;
    private static final int PRINT_VND_SCALE = 0;

    private static final DecimalFormat decimalFormatVND = new DecimalFormat("#,###");
    private static final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    public static BigDecimal doRound(BigDecimal value) {
        return doRound(value, DEFAULT_SCALE);
    }

    public static BigDecimal doRound(BigDecimal value, int newScale) {
        return value.setScale(newScale, RoundingMode.HALF_UP);
    }

    public static String formatMoney(BigDecimal value, String currencyCode) {
        if (value == null || currencyCode == null) {
            return "";
        }
        if (currencyCode.equals(CurrencyHardCode.VND.getCode())) {
            return decimalFormatVND.format(value.setScale(PRINT_VND_SCALE, RoundingMode.HALF_UP));
        }
        return decimalFormat.format(value.setScale(PRINT_USD_SCALE, RoundingMode.HALF_UP));
    }

    public static BigDecimal formatNumber(BigDecimal value, String currencyCode) {
        if (value == null || currencyCode == null) {
            return null;
        }
        if (currencyCode.equals(CurrencyHardCode.VND.getCode())) {
            return value.setScale(PRINT_VND_SCALE, RoundingMode.HALF_UP);
        }
        return value.setScale(PRINT_USD_SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal convertMoney(BigDecimal value, Long fromCurrencyId, Long toCurrencyId, BigDecimal exchangeRate) {
        if (value == null || fromCurrencyId == null || toCurrencyId == null) {
            return null;
        }
        if (fromCurrencyId.equals(toCurrencyId)) {
            return value;
        }
        if (CurrencyHardCode.isVNDCurrency(fromCurrencyId)) {
            return formatNumber(value.divide(exchangeRate, DEFAULT_SCALE, RoundingMode.HALF_UP), CurrencyHardCode.getCodeFromId(toCurrencyId));
        } else {
            return formatNumber(value.multiply(exchangeRate), CurrencyHardCode.getCodeFromId(toCurrencyId));
        }
    }
}
