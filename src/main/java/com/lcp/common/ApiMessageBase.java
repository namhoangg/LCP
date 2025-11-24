package com.lcp.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiMessageBase {

    public static ApiMessageBase
            SUCCESS = new ApiMessageBase(200, "success"),
            BAD_REQUEST = new ApiMessageBase(400, "bad-request"),
            UNAUTHORIZED = new ApiMessageBase(401, "unauthorized"),
            FORBIDDEN = new ApiMessageBase(403, "forbidden"),
            NOT_FOUND = new ApiMessageBase(404, "not-found"),
            UNSUPPORTED_MEDIA_TYPE = new ApiMessageBase(415, "unsupported-media-type"),
            INVALID_PARAM = new ApiMessageBase(499, "invalid-params"),
            INVALID_IN_FIELD = new ApiMessageBase(499, "invalid-in-field"),
            NOT_ALLOWED_TO_BE_UPDATED_IN_FIELD = new ApiMessageBase(499, "not-allowed-to-be-updated-in-field"),
            INVALID_FIELD_MISMATCH_EXPECTED = new ApiMessageBase(499, "invalid-field-mismatch-expected"),
            NOT_NULL_IN_FIELD = new ApiMessageBase(499, "notnull-in-field"),
            NOT_NULL_OR_ZERO_IN_FIELD = new ApiMessageBase(499, "notnull-or-zero-in-field"),
            EMPTY_IN_FIELD = new ApiMessageBase(499, "empty-in-field"),
            NOT_UPDATE_IN_FIELD = new ApiMessageBase(499, "not-update-in-field"),
            NOT_READABLE_PARAM = new ApiMessageBase(498, "not-readable-params"),
            MISSING_PARAM = new ApiMessageBase(497, "missing-params"),
            UPLOAD_SIZE_EXCEEDED = new ApiMessageBase(496, "upload-size-exceeded"),
            UNIQUES_LIST = new ApiMessageBase(499, "uniques_list"),
            REQUEST_METHOD_NOT_SUPPORT = new ApiMessageBase(405, "request-method-not-supported"),
            DATA_IN_USE = new ApiMessageBase(410, "data-is-referencing-to-some-entity"),
            DATA_INTEGRITY_VIOLATION = new ApiMessageBase(495, "data-integrity-violation"),
            INTERNAL_SERVER_ERROR = new ApiMessageBase(500, "internal-server-error"),
            COLUMN_NOT_FOUND = new ApiMessageBase(599, "column.not_found"),
            CANNOT_UN_DEFAULT = new ApiMessageBase(599, "cannot_un_default"),
            CANNOT_DEACTIVATE_DEFAULT_RECORD = new ApiMessageBase(599, "cannot_deactivate_default_record"),
            CANNOT_DELETE_DEFAULT_RECORD = new ApiMessageBase(599, "cannot_delete_default_record"),
            INLAND_FREIGHT_MODE_NOT_ALLOW = new ApiMessageBase(599, "inland_freight_mode_not_allow");

    protected final int code;
    protected final String message;


    public ApiMessageBase(String message) {
        this.message = message;
        this.code = 599;
    }
}
