package com.lcp.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum CurrencyHardCode {
    USD(1L, "USD"),
    VND(2L, "VND"),;

    private final Long id;
    private final String code;

    public static String getCodeFromId(Long id) {
        for (CurrencyHardCode currency : CurrencyHardCode.values()) {
            if (Objects.equals(currency.getId(), id)) {
                return currency.getCode();
            }
        }
        throw new IllegalArgumentException("Currency with id " + id + " not found");
    }

    public static Boolean isVNDCurrency(Long id) {
        return Objects.equals(VND.getId(), id);
    }
}
