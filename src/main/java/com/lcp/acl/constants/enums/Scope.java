package com.lcp.acl.constants.enums;

public enum Scope {
    UNSPECIFIED,
    INDIVIDUAL,
    ALL;

    public static Scope fromCode(int code) {
        Scope[] values = Scope.values();
        if (code < 0 || code >= values.length) {
            throw new IllegalArgumentException("Invalid Scope code: " + code);
        }
        return values[code];
    }
}
