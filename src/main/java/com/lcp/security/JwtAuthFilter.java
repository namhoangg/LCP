package com.lcp.security;


import com.lcp.common.ApiMessageBase;
import com.lcp.common.Constant;
import com.lcp.common.Response;
import com.lcp.exception.ApiException;
import com.lcp.exception.RestTemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserDetailsServiceCustom userDetailsServiceCustom;

    @Autowired
    AuthorizationService authorizationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (StringUtils.hasText(jwt)) {
                var userClaims = jwtTokenProvider.getUserClaimsFromToken(jwt);
                var userDetails = userDetailsServiceCustom.loadUserByUserClaims(userClaims);
                boolean checkTokenResult = authorizationService.checkToken(userClaims.getUserId(), jwt);
                if (!checkTokenResult) {
                    Response.servletResponse(response, ApiMessageBase.UNAUTHORIZED);
                    return;
                }
                var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ApiException ex) {
            Response.servletResponse(response, ex.getCode(), ex.getMessage());
            return;
        } catch (RestTemplateException ex) {
            Response.servletResponse(response, ex.getCode(), ex.getMessage());
            return;
        } catch (Exception ex) {
            Response.servletResponse(response, ApiMessageBase.UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(Constant.AUTH_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constant.AUTH_PREFIX)) {
            return bearerToken.substring(Constant.AUTH_PREFIX.length());
        }
        return null;
    }
}
