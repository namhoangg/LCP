package com.lcp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcp.client.entity.Client;
import com.lcp.client.repository.ClientRepository;
import com.lcp.security.configuration.AuthProperties;
import com.lcp.staff.entity.Staff;
import com.lcp.staff.repository.StaffRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final AuthProperties authProperties;
    private final ObjectMapper objectMapper;
    private final StaffRepository staffRepository;
    private final ClientRepository clientRepository;

    public String generateToken(Long userId) {
        Staff staff = staffRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Date now = new Date();
        Date expireDate = new Date(now.getTime() + authProperties.getJwtExpire());
        UserClaims userClaims = new UserClaims();
        userClaims.setUserId(userId);
        userClaims.setIat(now);
        userClaims.setExp(expireDate);
        userClaims.setIsClient(staff.getIsClient());
        userClaims.setIsSuperAdmin(staff.getIsSuperAdmin());
        userClaims.setClientId(null);
        if (staff.getIsClient()) {
            Client client = clientRepository.findByStaffId(staff.getId());
            userClaims.setClientId(client.getId());
        }
        return Jwts.builder()
                .setClaims(userClaims.getClaims())
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, authProperties.getJwtSecret())
                .compact();
    }

    public UserClaims getUserClaimsFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(authProperties.getJwtSecret())
                .parseClaimsJws(token)
                .getBody();

        return objectMapper.convertValue(claims, UserClaims.class);
    }
}
