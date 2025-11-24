package com.lcp.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserClaims {
    private Long userId;

    private String email;

    private Date iat;

    private Date exp;

    private Boolean isClient;

    private Boolean isSuperAdmin;

    private Long clientId; // only for client

    @JsonIgnore
    public Map<String, Object> getClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("iat", iat);
        claims.put("exp", exp);
        claims.put("isClient", isClient);
        claims.put("isSuperAdmin", isSuperAdmin);
        claims.put("clientId", clientId);
        claims.put("email", email);
        return claims;
    }
}
