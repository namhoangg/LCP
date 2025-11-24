package com.lcp.common;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


public class RequestHelper {
    private static final Gson GSON = new Gson();
    private static final Gson GSON_RESPONSE = new GsonBuilder().setPrettyPrinting().create();


    public static String getToken() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();

        return request.getHeader(Constant.AUTH_HEADER);
    }


    public static String getJwt() {
        String bearerToken = getToken();
        if (org.springframework.util.StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constant.AUTH_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public static String convertToJson(Object obj) {
        return GSON.toJson(obj);
    }

    public static String prettyJson(Object obj) {
        return GSON_RESPONSE.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, Type type) {
        return GSON.fromJson(json, type);
    }

    public static <S, T> List<T> createDtoList(List<S> entityList, Function<S, T> mapFunction) {
        List<T> dtoList = new ArrayList<>();
        for (S entity : entityList) {
            T dto = mapFunction.apply(entity);
            dtoList.add(dto);
        }
        return dtoList;
    }

}
