package com.lcp.common.log;

import com.lcp.security.UserDetailsCustom;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class HistoryRequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            // System.out.println("========= Begin request =========");
            // System.out.printf("servletPath: %s\n", request.getServletPath().replaceAll("[\\n]", ""));
            // System.out.printf("Header: x-request-id: %s\n", request.getHeader("x-request-id"));
            StringBuilder requestLog = new StringBuilder()
                    // .append("\n")
                    // .append("servletPath: ")
                    .append(request.getServletPath().replaceAll("[\\n]", ""))
                    .append("\n")
                    .append("method: ")
                    .append(request.getMethod())
                    .append("\n");

            var queryString = request.getQueryString();
            if (Strings.isNotBlank(queryString)) {
                requestLog.append("queryString: ")
                        .append(queryString.replaceAll("[\\n]", ""))
                        .append("\n");
            }
//            var user = UserDetailsCustom.getCurrentUser();
//            if (user != null) {
//                requestLog.append("clientIP: ")
//                        .append(RequestHelper.getClientIP().replaceAll("[\\n]", ""))
//                        .append("\n")
//                        .append("userId: ")
//                        .append(String.format("[%s]", user.getUserId()))
//                        .append("\n");
//            } else {
//                requestLog.append("clientIP: ")
//                        .append(RequestHelper.getClientIP().replaceAll("[\\n]", ""))
//                        .append("\n");
//            }
            requestLog.append("Header: origin: ").append(request.getHeader("origin"));
            log.info(requestLog.toString());

//            Enumeration<String> headerNames = request.getHeaderNames();
//            if (headerNames != null) {
//                while (headerNames.hasMoreElements()) {
//                    var headerName = headerNames.nextElement();
//                    var headerValue = request.getHeader(headerName).replaceAll("[\\n]", "");
//                    headerValue = headerValue.trim();
//                    if (Strings.isNotBlank(headerValue))
//                        System.out.printf("Header: %s \t %s \n", headerName, request.getHeader(headerName).replaceAll("[\\n]", ""));
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
