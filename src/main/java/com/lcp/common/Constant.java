package com.lcp.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {
    public static final String API_CONTENT_TYPE = "application/json";
    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_PREFIX = "Bearer ";

    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm";
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DASH = "_";
    public static final String TO_CHAR = "to_char";
    public static final String REPLACE = "replace";
    public static final String SPACE = " ";

    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String ASCENDING_SORT = "ASC";
    public static final String DESCENDING_SORT = "DESC";
    public static final String COLUMN_DEFAULT_SORT = "updatedAt";
    public static final String COLUMN_CREATED_AT = "createdAt";
    public static final String COLUMN_CREATED_USERNAME = "createdUsername";
    public static final String COLUMN_UPDATED_USERNAME = "updatedUsername";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DISPLAY_POSITION = "displayPosition";
    public static final String VALIDATION_FAILED = "Failed to validate request";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";

    public static final int CODE_SUCCESS = 200;
    public static final int CODE_BAD_REQUEST = 400;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_FORBIDDEN = 403;
    public static final int CODE_NOT_FOUND = 404;
    public static final int CODE_CONFLICT = 409;
    public static final int CODE_INTERNAL_SERVER_ERROR = 500;
    public static final int CODE_SERVICE_UNAVAILABLE = 503;
    public static final String ADVANCE_SEARCH_CACHE_TABLE = "ADVANCE_SEARCH_TABLE_NAME";
    public static final String LIST_CONVERTER_SPLIT_CHAR = ";";
}
