package com.lcp.acl.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(builderMethodName = "newBuilder", setterPrefix = "set")
public class PermissionMatrixResponseDto {
    private List<ActionDto> actions;
    private List<MatrixDto> matrix;
}
