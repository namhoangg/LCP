package com.lcp.security;


import com.lcp.common.ICacheService;
import com.lcp.common.RequestHelper;
import com.lcp.util.HashUtil;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Slf4j
@Component
public class AuthorizationService {
    /**
     * support login and save tokens to cache
     */
    private static final long AUTHORIZATION_CODE_EXPIRE = 900000; //15m
    private static final String AUTHORIZATION_CODE_FORMAT = "authorization_code_%d";
    private static final String TOKEN_KEY_FORMAT = "staff_token_%s";

    @Autowired
    private ICacheService cacheService;

    private String getTokenKey(Long staffId) {
        return String.format(TOKEN_KEY_FORMAT, staffId);
    }

    private String getSearchKey(Long staffId) {
        return String.format(TOKEN_KEY_FORMAT,
                ObjectUtils.defaultIfNull(staffId, "*"));
    }

    private String getAuthorizationCodeStoringKey(Long staffId) {
        return String.format(AUTHORIZATION_CODE_FORMAT, staffId);
    }

    public void addToken(Long staffId, String token) {
        String hashedToken = HashUtil.sha1(token);
        log.info("addToken: staffId: {}, token: {}", staffId, hashedToken);
        String key = getTokenKey(staffId);
        cacheService.add(key, hashedToken);
    }

    public boolean checkToken(Long staffId, String token) {
        String hashedToken = HashUtil.sha1(token);
        String key = getTokenKey(staffId);
        return cacheService.exist(key, hashedToken);
    }

    public void removeTokenSession() {
        log.info("removeTokenSession");
        UserDetailsCustom currentUser = UserDetailsCustom.getCurrentUser();
        if (currentUser != null) {
            String token = RequestHelper.getJwt();
            removeToken(currentUser.getUserId(), token);
        }
    }

    public void removeAllTokenByStaff(Long staffId) {
        log.info("removeToken: staffId: {}", staffId);
        if (staffId == null) {
            return;
        }
        String searchKey = getSearchKey(staffId);
        Set<String> allUserKeys = cacheService.retrieveAllKeys(searchKey);
        allUserKeys.forEach(key -> cacheService.remove(key));
    }


    public void removeToken(Long staffId, String token) {
        String hashedToken = HashUtil.sha1(token);
        String key = getTokenKey(staffId);
        cacheService.remove(key, hashedToken);
    }

    /**
     * manage authorization code storing
     */
    public void addAuthorizationCode(Long staffId, String authorizationCode) {
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + AUTHORIZATION_CODE_EXPIRE);
        log.info("addAuthorizationCode: staffId: {}, authorizationCode: {}, expires at: {}", staffId, authorizationCode, expireTime);
        String key = getAuthorizationCodeStoringKey(staffId);
        cacheService.add(key, expireTime, authorizationCode);
    }

    public boolean checkAuthorizationCode(Long staffId, String authorizationCode) {
        String key = getAuthorizationCodeStoringKey(staffId);
        return cacheService.exist(key, authorizationCode);
    }

    public void removeAuthorizationCode(Long staffId, String authorizationCode) {
        log.info("removeAuthorizationCode: staffId: {}, authorizationCode: {}", staffId, authorizationCode);
        String key = getAuthorizationCodeStoringKey(staffId);
        cacheService.remove(key, authorizationCode);
    }
}
